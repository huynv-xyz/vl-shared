package com.vlife.shared.service;

import com.vlife.shared.jdbc.dao.SalesTransactionDao;
import com.vlife.shared.jdbc.entity.SalesTransaction;
import jakarta.inject.Singleton;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class SalesTransactionImportService {

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("d/M/yyyy");

    private final SalesTransactionDao salesTransactionDao;

    public SalesTransactionImportService(SalesTransactionDao salesTransactionDao) {
        this.salesTransactionDao = salesTransactionDao;
    }

    public int replaceAllFromCsv(String filePath) throws Exception {
        salesTransactionDao.deleteAllData();
        return importCsv(filePath);
    }

    public int importCsv(String filePath) throws Exception {
        return importCsv(Path.of(filePath));
    }

    public int importCsv(Path path) throws Exception {
        List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);

        int headerIndex = findHeaderIndex(allLines);
        if (headerIndex < 0) {
            throw new IllegalArgumentException("Không tìm thấy dòng header trong file CSV");
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
            int inserted = 0;

            for (CSVRecord record : parser) {
                if (isEmptyRecord(record)) {
                    continue;
                }

                SalesTransaction entity = mapRecord(record);
                if (entity == null) {
                    continue;
                }

                salesTransactionDao.insert(entity);
                inserted++;
            }

            return inserted;
        }
    }

    private int findHeaderIndex(List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            String line = safe(lines.get(i));
            if (line.contains("Ngày hạch toán")
                    && line.contains("Ngày chứng từ")
                    && line.contains("Số chứng từ")
                    && line.contains("Mã khách hàng")) {
                return i;
            }
        }
        return -1;
    }

    private SalesTransaction mapRecord(CSVRecord record) {
        Map<String, String> row = normalizeRecord(record);

        SalesTransaction x = new SalesTransaction();

        x.setPostingDate(parseDateTime(row.get("Ngày hạch toán")));
        x.setDocumentDate(parseDateTime(row.get("Ngày chứng từ")));
        x.setDocumentNo(row.get("Số chứng từ"));

        x.setCustomerCode(row.get("Mã khách hàng"));
        x.setCustomerName(row.get("Tên khách hàng"));
        x.setDescription(row.get("Diễn giải chung"));

        x.setProductCode(row.get("Mã hàng"));
        x.setProductName(row.get("Tên hàng"));
        x.setUnit(firstNonBlank(
                row.get("Đơn vị chính (ĐVC)"),
                row.get("Đơn vị chính (ĐV C)"),
                row.get("Đơn vị chính")
        ));

        x.setSaleQty(parseDouble(firstNonBlank(
                row.get("Tổng SL bán theo ĐVC"),
                row.get(" Tổng SL bán theo ĐVC "),
                row.get("Tổng SL bán theo ĐVC ")
        )));

        x.setReturnQty(parseDouble(firstNonBlank(
                row.get("Tổng SL trả lại theo ĐVC"),
                row.get(" Tổng SL trả lại theo ĐVC "),
                row.get("Tổng SL trả lại theo ĐVC ")
        )));

        x.setSaleUserCode(row.get("Mã nhân viên bán hàng"));
        x.setSaleUserName(row.get("Tên nhân viên bán hàng"));
        x.setSaleUserTitle(row.get("Chức danh"));
        x.setContactName(row.get("Người liên hệ"));

        x.setVthhCon(row.get("VTHH_CON"));
        x.setVthhGroupName(row.get("Tên nhóm VTHH"));
        x.setCustomerType(row.get("PHÂN_LOẠI_KH"));
        x.setExtDetail2(row.get("Trường mở rộng chi tiết 2"));

        x.setIsGift(parseInt(row.get("HÀNG_TẶNG")));
        x.setPrivateCode(row.get("MÃ_RIÊNG"));

        x.setSlRiengTl(parseDouble(row.get("SL_RIÊNG_TL")));
        x.setSlTlNhom(parseDouble(row.get("SL_TL_NHÓM")));
        x.setSlLB2c(parseDouble(row.get("SL_L_B2C")));
        x.setSlLB2b(parseDouble(row.get("SL_L_B2B")));
        x.setSlHdn(parseDouble(row.get("SL_HDN")));
        x.setDiemHdn(parseDouble(row.get("DIEM_HDN")));

        x.setProcessMonth(parseProcessMonth(
                firstNonBlank(
                        row.get("THANG_XU_LY"),
                        row.get("THÁNG_XỬ_LÝ")
                ),
                x.getPostingDate(),
                x.getDocumentDate()
        ));

        x.setNpp(row.get("NPP"));
        x.setValidCode(firstNonBlank(row.get("MA_HOP _LE"), row.get("MA_HOP_LE")));
        x.setHdnStatus(row.get("TINH_TRANG_HDN"));
        x.setCommonGroup(row.get("NHÓM-CHUNG"));
        x.setRegion(row.get("KHU_VUC"));

        x.setSlHdnK0MaRieng(parseDouble(firstNonBlank(
                row.get("SL_HDN_K0_MA_RIENG"),
                row.get("SL_HDN_KO_MA_RIENG")
        )));

        x.setStatus(1);
        x.setNote(null);

        normalizeEntity(x);
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

    private LocalDateTime parseDateTime(String value) {
        String s = safe(value).trim();
        if (s.isEmpty()) {
            return null;
        }

        try {
            LocalDate date = LocalDate.parse(s, DATE_FMT);
            return LocalDateTime.of(date, LocalTime.MIN);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private int parseProcessMonth(String value, LocalDateTime postingDate, LocalDateTime documentDate) {
        LocalDateTime dt = postingDate != null ? postingDate : documentDate;

        String s = cleanNumber(value);
        if (!s.isEmpty()) {
            try {
                int month = (int) Double.parseDouble(s);

                if (month >= 1 && month <= 12) {
                    if (dt != null) {
                        return dt.getYear() * 100 + month;
                    }
                    return month;
                }

                return month;
            } catch (Exception ignored) {
            }
        }

        if (dt == null) {
            return 0;
        }

        return dt.getYear() * 100 + dt.getMonthValue();
    }

    private int parseInt(String value) {
        String s = cleanNumber(value);
        if (s.isEmpty()) {
            return 0;
        }
        try {
            return (int) Double.parseDouble(s);
        } catch (Exception e) {
            return 0;
        }
    }

    private double parseDouble(String value) {
        String s = cleanNumber(value);
        if (s.isEmpty()) {
            return 0D;
        }
        try {
            return Double.parseDouble(s);
        } catch (Exception e) {
            return 0D;
        }
    }

    private String cleanNumber(String value) {
        String s = safe(value);

        s = s.replace("\u00A0", "")
                .replace("\u2007", "")
                .replace("\u202F", "")
                .replace(" ", "")
                .trim();

        if (s.isEmpty() || "-".equals(s)) {
            return "";
        }

        if (s.startsWith("(") && s.endsWith(")")) {
            s = "-" + s.substring(1, s.length() - 1);
        }

        s = s.replaceAll("[^0-9,.-]", "");
        s = s.replace(",", "");

        return s;
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

    private void normalizeEntity(SalesTransaction x) {
        if (x.getSaleQty() == null) x.setSaleQty(0D);
        if (x.getReturnQty() == null) x.setReturnQty(0D);
        if (x.getSlRiengTl() == null) x.setSlRiengTl(0D);
        if (x.getSlTlNhom() == null) x.setSlTlNhom(0D);
        if (x.getSlLB2c() == null) x.setSlLB2c(0D);
        if (x.getSlLB2b() == null) x.setSlLB2b(0D);
        if (x.getSlHdn() == null) x.setSlHdn(0D);
        if (x.getDiemHdn() == null) x.setDiemHdn(0D);
        if (x.getSlHdnK0MaRieng() == null) x.setSlHdnK0MaRieng(0D);
        if (x.getIsGift() == null) x.setIsGift(0);
        if (x.getProcessMonth() == null) x.setProcessMonth(0);
        if (x.getStatus() == null) x.setStatus(1);
    }
}