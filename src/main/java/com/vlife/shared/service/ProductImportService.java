package com.vlife.shared.service;

import com.vlife.shared.jdbc.dao.ProductDao;
import com.vlife.shared.jdbc.entity.Product;
import jakarta.inject.Singleton;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.*;

@Singleton
public class ProductImportService {

    private final ProductDao productDao;

    public ProductImportService(ProductDao productDao) {
        this.productDao = productDao;
    }

    public int importCsv(String filePath) throws Exception {
        return importCsv(Path.of(filePath));
    }

    public int importCsv(Path path) throws Exception {
        List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);

        int headerIndex = findHeaderIndex(allLines);
        if (headerIndex < 0) {
            throw new IllegalArgumentException("Không tìm thấy dòng header trong file product CSV");
        }

        StringBuilder csvData = new StringBuilder();
        for (int i = headerIndex; i < allLines.size(); i++) {
            csvData.append(allLines.get(i)).append('\n');
        }

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
            int affected = 0;

            for (CSVRecord record : parser) {
                if (isEmptyRecord(record)) {
                    continue;
                }

                Product product = mapRecord(record);
                if (product == null || isBlank(product.getCode())) {
                    continue;
                }

                boolean ok = insertOrUpdate(product);
                if (ok) {
                    affected++;
                }
            }

            return affected;
        }
    }

    // ================= HEADER =================

    private int findHeaderIndex(List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            String line = normalizeHeader(safe(lines.get(i)));

            if (line.contains("mã hàng") && line.contains("tên hàng")) {
                return i;
            }
        }
        return -1;
    }

    // ================= MAP =================

    private Product mapRecord(CSVRecord record) {
        Map<String, String> row = normalizeRecord(record);

        String code = firstNonBlank(
                row.get("mã hàng"),
                row.get("mã sản phẩm"),
                row.get("ma_hang"),
                row.get("ma_sp"),
                row.get("code")
        );

        if (isBlank(code)) {
            return null;
        }

        String name = firstNonBlank(
                row.get("tên hàng"),
                row.get("tên sản phẩm"),
                row.get("ten_hang"),
                row.get("ten_sp"),
                row.get("name")
        );

        String unit = firstNonBlank(
                row.get("đvt"),
                row.get("đơn vị tính"),
                row.get("don_vi_tinh"),
                row.get("unit")
        );

        Product x = new Product();
        x.setCode(safe(code).trim().toUpperCase());
        x.setName(safe(name).trim());
        x.setUnit(trimToNull(unit));
        x.setStatus(1);

        LocalDateTime now = LocalDateTime.now();
        x.setCreatedAt(now);
        x.setUpdatedAt(now);

        normalizeEntity(x);
        return x;
    }

    private boolean insertOrUpdate(Product x) {
        Optional<Product> oldOpt = productDao.findByCode(x.getCode());

        if (oldOpt.isEmpty()) {
            productDao.insert(x);
            return true;
        }

        Product old = oldOpt.get();
        old.setName(x.getName());
        old.setUnit(x.getUnit());
        old.setStatus(x.getStatus());
        old.setUpdatedAt(LocalDateTime.now());

        normalizeEntity(old);
        return productDao.updateAll(old.getId(), old) > 0;
    }

    // ================= UTIL =================

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
                .replaceAll("\\s+", " ")
                .trim()
                .toLowerCase(); // 🔥 FIX CHÍNH
    }

    private boolean isEmptyRecord(CSVRecord record) {
        for (String v : record) {
            if (v != null && !v.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private void normalizeEntity(Product x) {
        if (isBlank(x.getCode())) {
            x.setCode(null);
        } else {
            x.setCode(x.getCode().trim().toUpperCase());
        }

        if (isBlank(x.getName())) {
            x.setName("");
        } else {
            x.setName(x.getName().trim());
        }

        x.setUnit(trimToNull(x.getUnit()));

        if (x.getStatus() == null) {
            x.setStatus(1);
        }

        LocalDateTime now = LocalDateTime.now();
        if (x.getCreatedAt() == null) {
            x.setCreatedAt(now);
        }
        if (x.getUpdatedAt() == null) {
            x.setUpdatedAt(now);
        }
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

    private String firstNonBlank(String... values) {
        if (values == null) return null;
        for (String v : values) {
            if (v != null && !v.trim().isEmpty()) {
                return v.trim();
            }
        }
        return null;
    }
}