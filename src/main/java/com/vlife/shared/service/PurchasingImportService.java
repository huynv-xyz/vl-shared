package com.vlife.shared.service;

import com.vlife.shared.jdbc.dao.ProductDao;
import com.vlife.shared.jdbc.dao.purchasing.ContractDao;
import com.vlife.shared.jdbc.dao.purchasing.ContractItemDao;
import com.vlife.shared.jdbc.dao.purchasing.CurrencyDao;
import com.vlife.shared.jdbc.dao.purchasing.NationDao;
import com.vlife.shared.jdbc.dao.purchasing.SupplierDao;
import com.vlife.shared.jdbc.entity.base.AuditEntity;
import com.vlife.shared.jdbc.entity.purchasing.Currency;
import com.vlife.shared.jdbc.entity.purchasing.Nation;
import com.vlife.shared.jdbc.entity.purchasing.Supplier;
import jakarta.inject.Singleton;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Singleton
public class PurchasingImportService {

    private final NationDao nationDao;
    private final CurrencyDao currencyDao;
    private final SupplierDao supplierDao;
    private final ProductDao productDao;
    private final ContractDao contractDao;
    private final ContractItemDao contractItemDao;

    public PurchasingImportService(
            NationDao nationDao,
            CurrencyDao currencyDao,
            SupplierDao supplierDao,
            ProductDao productDao,
            ContractDao contractDao,
            ContractItemDao contractItemDao
    ) {
        this.nationDao = nationDao;
        this.currencyDao = currencyDao;
        this.supplierDao = supplierDao;
        this.productDao = productDao;
        this.contractDao = contractDao;
        this.contractItemDao = contractItemDao;
    }

    public ImportResult importCsv(String filePath) throws Exception {
        return importCsv(Path.of(filePath));
    }

    public ImportResult importCsv(Path path) throws Exception {
        List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);

        int headerIndex = findHeaderIndex(allLines);
        if (headerIndex < 0) {
            throw new IllegalArgumentException("Không tìm thấy dòng header trong file purchasing CSV");
        }

        StringBuilder csvData = new StringBuilder();
        for (int i = headerIndex; i < allLines.size(); i++) {
            csvData.append(allLines.get(i)).append('\n');
        }

        ImportResult result = new ImportResult();

        try (
                CSVParser parser = CSVParser.parse(
                        csvData.toString(),
                        CSVFormat.DEFAULT
                                .builder()
                                .setHeader()
                                .setSkipHeaderRecord(true)
                                .setIgnoreEmptyLines(true)
                                .setTrim(true)
                                .build()
                )
        ) {
            for (CSVRecord record : parser) {
                if (isEmptyRecord(record)) {
                    continue;
                }

                Map<String, String> row = normalizeRecord(record);

                String supplierCode = firstNonBlank(
                        row.get("ma_ncc"),
                        row.get("supplier_code")
                );
                String supplierName = firstNonBlank(
                        row.get("ten_ncc"),
                        row.get("supplier_name")
                );
                String nationName = firstNonBlank(
                        row.get("quoc_gia"),
                        row.get("country"),
                        row.get("nation")
                );
                String productCode = firstNonBlank(
                        row.get("ma_hang"),
                        row.get("ma_sp"),
                        row.get("product_code")
                );
                String productName = firstNonBlank(
                        row.get("ten_hang"),
                        row.get("ten_sp"),
                        row.get("product_name")
                );
                String unit = firstNonBlank(
                        row.get("dvt"),
                        row.get("don_vi_tinh"),
                        row.get("unit")
                );
                String contractCode = firstNonBlank(
                        row.get("so_hd"),
                        row.get("ma_hd"),
                        row.get("contract_code")
                );
                String currencyCode = firstNonBlank(
                        row.get("loai_tien"),
                        row.get("currency"),
                        row.get("currency_code")
                );

                if (isBlank(contractCode) || isBlank(supplierCode) || isBlank(productCode)) {
                    result.skipped++;
                    continue;
                }

                try {
                    Nation nation = upsertNation(nationName);
                    Currency currency = upsertCurrency(currencyCode, row);
                    Supplier supplier = upsertSupplier(supplierCode, supplierName, nation);

                    if (nation != null) result.nations++;
                    if (currency != null) result.currencies++;
                    if (supplier != null) result.suppliers++;

                } catch (Exception e) {
                    result.errors++;
                }
            }
        }

        return result;
    }

    private int findHeaderIndex(List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            String line = normalizeHeader(safe(lines.get(i)));
            if (line.contains("ngay_hd") && line.contains("so_hd") && line.contains("ma_ncc") && line.contains("ma_hang")) {
                return i;
            }
        }
        return -1;
    }

    private Nation upsertNation(String nationName) {
        nationName = trimToNull(nationName);
        if (nationName == null) return null;

        String code = buildNationCode(nationName);

        Optional<Nation> oldOpt = nationDao.findByCode(code);
        if (oldOpt.isPresent()) {
            Nation old = oldOpt.get();
            old.setCode(code);
            old.setName(nationName);
            touchUpdate(old);
            nationDao.updateAll(old.getId(), old);
            return old;
        }

        Nation x = new Nation();
        x.setCode(code);
        x.setName(nationName);
        touchCreate(x);
        nationDao.insert(x);
        return x;
    }

    private Currency upsertCurrency(String currencyCode, Map<String, String> row) {
        currencyCode = normalizeCurrencyCode(currencyCode);
        if (currencyCode == null) return null;

        Double exchangeRate = parseDouble(row.get("ty_gia"));

        Optional<Currency> oldOpt = currencyDao.findByCode(currencyCode);
        if (oldOpt.isPresent()) {
            Currency old = oldOpt.get();
            old.setCode(currencyCode);
            old.setName(defaultCurrencyName(currencyCode));
            old.setSymbol(defaultCurrencySymbol(currencyCode));
            if (exchangeRate != null) {
                old.setExchangeRate(exchangeRate);
            }
            touchUpdate(old);
            currencyDao.updateAll(old.getId(), old);
            return old;
        }

        Currency x = new Currency();
        x.setCode(currencyCode);
        x.setName(defaultCurrencyName(currencyCode));
        x.setSymbol(defaultCurrencySymbol(currencyCode));
        x.setExchangeRate(exchangeRate != null ? exchangeRate : 0);
        touchCreate(x);
        currencyDao.insert(x);
        return x;
    }

    private Supplier upsertSupplier(String supplierCode, String supplierName, Nation nation) {
        supplierCode = trimToNull(supplierCode);
        supplierName = trimToNull(supplierName);

        if (supplierCode == null) return null;

        Optional<Supplier> oldOpt = supplierDao.findByCode(supplierCode);
        if (oldOpt.isPresent()) {
            Supplier old = oldOpt.get();
            old.setCode(supplierCode);
            old.setName(supplierName);
            old.setNationId(nation != null ? nation.getId() : null);
            touchUpdate(old);
            supplierDao.updateAll(old.getId(), old);
            return old;
        }

        Supplier x = new Supplier();
        x.setCode(supplierCode);
        x.setName(supplierName);
        x.setNationId(nation != null ? nation.getId() : null);
        touchCreate(x);
        supplierDao.insert(x);
        return x;
    }



    private Map<String, String> normalizeRecord(CSVRecord record) {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, String> e : record.toMap().entrySet()) {
            String key = normalizeHeader(e.getKey());
            String value = safe(e.getValue()).trim();
            map.put(key, value);
        }
        return map;
    }

    private String normalizeHeader(String s) {
        if (s == null) return "";
        return s.replace("\uFEFF", "")
                .replace('\u00A0', ' ')
                .replace("\n", " ")
                .replace("\r", " ")
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase()
                .replace("đ", "d");
    }

    private boolean isEmptyRecord(CSVRecord record) {
        for (String v : record) {
            if (v != null && !v.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private LocalDateTime parseDateTime(String s) {
        s = trimToNull(s);
        if (s == null) return null;

        List<DateTimeFormatter> formatters = List.of(
                DateTimeFormatter.ofPattern("M/d/yyyy"),
                DateTimeFormatter.ofPattern("MM/dd/yyyy"),
                DateTimeFormatter.ofPattern("d/M/yyyy"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
        );

        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(s, formatter).atStartOfDay();
            } catch (Exception ignored) {
            }
        }

        return null;
    }

    private BigDecimal parsePercent(String s) {
        s = trimToNull(s);
        if (s == null) return null;
        s = s.replace("%", "").trim();
        return parseDecimal(s);
    }

    private Double parseDouble(String s) {
        BigDecimal v = parseDecimal(s);
        return v == null ? null : v.doubleValue();
    }

    private BigDecimal parseDecimal(String s) {
        s = trimToNull(s);
        if (s == null) return null;

        s = s.replace("\"", "")
                .replace(" ", "")
                .replace(",", "")
                .replace("−", "-")
                .replace("–", "-");

        if (s.isEmpty() || "-".equals(s)) {
            return BigDecimal.ZERO;
        }

        try {
            return new BigDecimal(s);
        } catch (Exception e) {
            return null;
        }
    }

    private String buildNationCode(String nationName) {
        nationName = trimToNull(nationName);
        if (nationName == null) return null;

        String x = nationName.toUpperCase(Locale.ROOT)
                .replaceAll("[^A-Z0-9]+", "_")
                .replaceAll("_+", "_")
                .replaceAll("^_|_$", "");

        if (x.length() > 20) {
            x = x.substring(0, 20);
        }

        return x;
    }

    private String normalizeCurrencyCode(String code) {
        code = trimToNull(code);
        if (code == null) return null;
        return code.toUpperCase(Locale.ROOT);
    }

    private String defaultCurrencyName(String code) {
        if (code == null) return null;
        return switch (code) {
            case "USD" -> "US Dollar";
            case "VND" -> "Vietnam Dong";
            case "EUR" -> "Euro";
            default -> code;
        };
    }

    private String defaultCurrencySymbol(String code) {
        if (code == null) return null;
        return switch (code) {
            case "USD" -> "$";
            case "VND" -> "₫";
            case "EUR" -> "€";
            default -> null;
        };
    }

    private BigDecimal zeroIfNull(BigDecimal value) {
        return value != null ? value : BigDecimal.ZERO;
    }

    private void touchCreate(AuditEntity x) {
        LocalDateTime now = LocalDateTime.now();
        if (x.getCreatedAt() == null) {
            x.setCreatedAt(now);
        }
        if (x.getUpdatedAt() == null) {
            x.setUpdatedAt(now);
        }
    }

    private void touchUpdate(AuditEntity x) {
        x.setUpdatedAt(LocalDateTime.now());
    }

    private String trimToNull(String s) {
        if (s == null) return null;
        s = s.trim();
        return s.isEmpty() ? null : s;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    private String defaultString(String s) {
        return s == null ? "" : s.trim();
    }

    private String firstNonBlank(String... values) {
        if (values == null) return null;
        for (String v : values) {
            if (v != null && !v.trim().isEmpty()) {
                return v.trim();
            }
        }
        return null;
    }

    public static class ImportResult {
        public int nations;
        public int currencies;
        public int suppliers;
        public int products;
        public int contracts;
        public int contractItems;
        public int skipped;
        public int errors;

        public int totalAffected() {
            return nations + currencies + suppliers + products + contracts + contractItems;
        }

        @Override
        public String toString() {
            return "ImportResult{" +
                    "nations=" + nations +
                    ", currencies=" + currencies +
                    ", suppliers=" + suppliers +
                    ", products=" + products +
                    ", contracts=" + contracts +
                    ", contractItems=" + contractItems +
                    ", skipped=" + skipped +
                    ", errors=" + errors +
                    '}';
        }
    }
}