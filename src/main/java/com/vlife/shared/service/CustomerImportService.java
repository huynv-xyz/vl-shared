package com.vlife.shared.service;

import com.vlife.shared.jdbc.dao.CustomerDao;
import com.vlife.shared.jdbc.entity.Customer;
import jakarta.inject.Singleton;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
public class CustomerImportService {

    private final CustomerDao customerDao;

    public CustomerImportService(CustomerDao customerDao) {
        this.customerDao = customerDao;
    }

    public int importCsv(String filePath) throws Exception {
        return importCsv(Path.of(filePath));
    }

    public int importCsv(Path path) throws Exception {
        List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);

        int headerIndex = findHeaderIndex(allLines);
        if (headerIndex < 0) {
            throw new IllegalArgumentException("Không tìm thấy dòng header trong file customer CSV");
        }

        StringBuilder csvData = new StringBuilder();
        for (int i = headerIndex; i < allLines.size(); i++) {
            csvData.append(allLines.get(i)).append('\n');
        }

        try (
                BufferedReader ignored = Files.newBufferedReader(path, StandardCharsets.UTF_8);
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

                Customer customer = mapRecord(record);
                if (customer == null || isBlank(customer.getCode())) {
                    continue;
                }

                boolean ok = insertOrUpdate(customer);
                if (ok) {
                    affected++;
                }
            }

            return affected;
        }
    }

    private int findHeaderIndex(List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            String line = safe(lines.get(i));
            if (line.contains("Mã khách hàng") && line.contains("Tên khách hàng")) {
                return i;
            }
        }
        return -1;
    }

    private Customer mapRecord(CSVRecord record) {
        Map<String, String> row = normalizeRecord(record);

        String code = firstNonBlank(
                row.get("Mã khách hàng"),
                row.get("Ma_KH"),
                row.get("MA_KH"),
                row.get("code")
        );

        if (isBlank(code)) {
            return null;
        }

        String name = firstNonBlank(
                row.get("Tên khách hàng"),
                row.get("Ten_KH"),
                row.get("TEN_KH"),
                row.get("name")
        );

        String type = firstNonBlank(
                row.get("Loại KH"),
                row.get("LOAI_KH"),
                row.get("PHÂN_LOẠI_KH"),
                row.get("type")
        );

        String region = firstNonBlank(
                row.get("KHU_VUC"),
                row.get("Khu vực"),
                row.get("region")
        );

        if (isBlank(region)) {
            region = resolveRegionFromCode(code);
        }

        Customer x = new Customer();
        x.setCode(safe(code).trim().toUpperCase());
        x.setName(safe(name).trim());
        x.setType(safe(type).trim().toUpperCase());
        x.setRegion(safe(region).trim().toUpperCase());
        x.setStatus(1);
        x.setNote(null);

        LocalDateTime now = LocalDateTime.now();
        x.setCreatedAt(now);
        x.setUpdatedAt(now);

        normalizeEntity(x);
        return x;
    }

    private boolean insertOrUpdate(Customer x) {
        Optional<Customer> oldOpt = customerDao.findByCode(x.getCode());

        if (oldOpt.isEmpty()) {
            customerDao.insert(x);
            return true;
        }

        Customer old = oldOpt.get();
        old.setName(x.getName());
        old.setType(x.getType());
        old.setRegion(x.getRegion());
        old.setStatus(x.getStatus());
        old.setNote(x.getNote());
        old.setUpdatedAt(LocalDateTime.now());

        normalizeEntity(old);
        return customerDao.updateAll(old.getId(), old) > 0;
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
        if (s == null) {
            return "";
        }
        return s.replace("\uFEFF", "")
                .replace('\u00A0', ' ')
                .replaceAll("\\s+", " ")
                .trim();
    }

    private boolean isEmptyRecord(CSVRecord record) {
        for (String v : record) {
            if (v != null && !v.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    private String resolveRegionFromCode(String code) {
        String x = safe(code).trim().toUpperCase();
        if (x.startsWith("MB.") || x.startsWith("MB_") || x.startsWith("MB-")) {
            return "MB";
        }
        return "MN";
    }

    private void normalizeEntity(Customer x) {
        if (isBlank(x.getCode())) {
            x.setCode(null);
        }
        if (!isBlank(x.getCode())) {
            x.setCode(x.getCode().trim().toUpperCase());
        }

        if (isBlank(x.getName())) {
            x.setName("");
        } else {
            x.setName(x.getName().trim());
        }

        if (isBlank(x.getType())) {
            x.setType("");
        } else {
            x.setType(x.getType().trim().toUpperCase());
        }

        if (isBlank(x.getRegion())) {
            x.setRegion("MN");
        } else {
            x.setRegion(x.getRegion().trim().toUpperCase());
        }

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

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    private String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String v : values) {
            if (v != null && !v.trim().isEmpty()) {
                return v.trim();
            }
        }
        return null;
    }
}