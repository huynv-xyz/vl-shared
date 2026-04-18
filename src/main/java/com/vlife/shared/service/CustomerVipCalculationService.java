package com.vlife.shared.service;

import com.vlife.shared.dto.CustomerVipReportRow;
import com.vlife.shared.jdbc.dao.CustomerDao;
import com.vlife.shared.jdbc.dao.CustomerVipYearlyResultDao;
import com.vlife.shared.jdbc.dao.SalesTransactionDao;
import com.vlife.shared.jdbc.dao.vip.VipPointRuleDao;
import com.vlife.shared.jdbc.dao.vip.VipPrivateBonusRuleDao;
import com.vlife.shared.jdbc.dao.vip.VipTierDao;
import com.vlife.shared.jdbc.entity.Customer;
import com.vlife.shared.jdbc.entity.CustomerVipYearlyResult;
import com.vlife.shared.jdbc.entity.SalesTransaction;
import com.vlife.shared.jdbc.entity.vip.VipPointRule;
import com.vlife.shared.jdbc.entity.vip.VipPrivateBonusRule;
import com.vlife.shared.jdbc.entity.vip.VipTier;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;
import jakarta.inject.Singleton;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class CustomerVipCalculationService {

    private static final int CUSTOMER_PAGE_SIZE = 500;

    private final CustomerDao customerDao;
    private final SalesTransactionDao salesTransactionDao;
    private final VipPointRuleDao vipPointRuleDao;
    private final VipPrivateBonusRuleDao vipPrivateBonusRuleDao;
    private final VipTierDao vipTierDao;
    private final CustomerVipYearlyResultDao customerVipYearlyResultDao;

    public CustomerVipCalculationService(
            CustomerDao customerDao,
            SalesTransactionDao salesTransactionDao,
            VipPointRuleDao vipPointRuleDao,
            VipPrivateBonusRuleDao vipPrivateBonusRuleDao,
            VipTierDao vipTierDao,
            CustomerVipYearlyResultDao customerVipYearlyResultDao
    ) {
        this.customerDao = customerDao;
        this.salesTransactionDao = salesTransactionDao;
        this.vipPointRuleDao = vipPointRuleDao;
        this.vipPrivateBonusRuleDao = vipPrivateBonusRuleDao;
        this.vipTierDao = vipTierDao;
        this.customerVipYearlyResultDao = customerVipYearlyResultDao;
    }

    public List<CustomerVipReportRow> calculateYear(int year) {
        return calculateByProcessMonthRange(year * 100 + 1, year * 100 + 12);
    }

    public List<CustomerVipReportRow> calculateByProcessMonthRange(int fromProcessMonth, int toProcessMonth) {
        validateProcessMonthRange(fromProcessMonth, toProcessMonth);

        List<CustomerVipReportRow> result = new ArrayList<>();

        int pageNumber = 0;
        while (true) {
            Page<Customer> page = customerDao.search(
                    null,
                    null,
                    null,
                    1,
                    Pageable.from(pageNumber, CUSTOMER_PAGE_SIZE)
            );

            List<Customer> customers = page.getContent();
            if (customers == null || customers.isEmpty()) {
                break;
            }

            for (Customer customer : customers) {
                CustomerVipReportRow row = calculateOneCustomer(customer, fromProcessMonth, toProcessMonth);
                if (row != null) {
                    result.add(row);
                }
            }

            if (!page.hasNext()) {
                break;
            }
            pageNumber++;
        }

        return result;
    }

    public CustomerVipReportRow calculateOneCustomer(String customerCode, int fromProcessMonth, int toProcessMonth) {
        validateProcessMonthRange(fromProcessMonth, toProcessMonth);

        if (isBlank(customerCode)) {
            return null;
        }

        Customer customer = customerDao.findByCode(customerCode.trim().toUpperCase()).orElse(null);
        if (customer == null || customer.getStatus() == null || customer.getStatus() != 1) {
            return null;
        }

        return calculateOneCustomer(customer, fromProcessMonth, toProcessMonth);
    }

    private CustomerVipReportRow calculateOneCustomer(Customer customer, int fromProcessMonth, int toProcessMonth) {
        List<SalesTransaction> items = salesTransactionDao.getByCustomerCodeAndProcessMonthRange(
                customer.getCode(),
                fromProcessMonth,
                toProcessMonth
        );

        if (items == null || items.isEmpty()) {
            return null;
        }

        double totalVipPoint = calculateTotalVipPoint(items, customer.getRegion());
        double privateBonusAmount = calculatePrivateBonus(items);

        String groupCode = resolveGroupCode(customer.getType(), customer.getRegion());

        VipTier currentTier = getMatchedTier(groupCode, totalVipPoint);
        VipTier nextTier = getNextTier(groupCode, currentTier);

        String tierCode = currentTier != null ? currentTier.getName() : null;
        String tierName = currentTier != null ? currentTier.getName() : "Chưa đủ điểm xét VIP";
        double rewardAmount = currentTier != null ? getRewardByGroup(currentTier, groupCode) : 0D;
        double totalRewardAmount = totalVipPoint * rewardAmount;
        double finalBonusAmount = totalRewardAmount + privateBonusAmount;

        String nextTierCode = nextTier != null ? nextTier.getName() : null;
        String nextTierName = nextTier != null ? nextTier.getName() : null;
        double missingPoint = nextTier != null
                ? Math.max(0D, getPointByGroup(nextTier, groupCode) - totalVipPoint)
                : 0D;
        String missingPointMessage = buildMissingPointMessage(missingPoint, nextTierName);

        int calcYear = resolveCalcYear(fromProcessMonth, toProcessMonth);

        persistYearlyResult(
                calcYear,
                customer,
                groupCode,
                totalVipPoint,
                tierCode,
                tierName,
                rewardAmount,
                totalRewardAmount,
                privateBonusAmount,
                finalBonusAmount,
                nextTierCode,
                nextTierName,
                missingPoint,
                missingPointMessage,
                buildNote(fromProcessMonth, toProcessMonth)
        );

        CustomerVipReportRow out = new CustomerVipReportRow();
        out.ma_kh = customer.getCode();
        out.ten_kh = customer.getName();
        out.loai_kh = customer.getType();
        out.diem_vip = round(totalVipPoint);
        out.hang_vip = tierName;
        out.gia_tri_moi_diem = round(rewardAmount);
        out.tong_tien_thuong = round(totalRewardAmount);
        out.thuong_nhom_rieng = round(privateBonusAmount);
        out.tong_thuong_sau_cung = round(finalBonusAmount);
        out.hang_sap_len = nextTierName;
        out.diem_con_thieu = round(missingPoint);

        return out;
    }

    private void persistYearlyResult(
            int calcYear,
            Customer customer,
            String groupCode,
            double totalVipPoint,
            String tierCode,
            String tierName,
            double rewardAmount,
            double totalRewardAmount,
            double privateBonusAmount,
            double finalBonusAmount,
            String nextTierCode,
            String nextTierName,
            double missingPoint,
            String missingPointMessage,
            String note
    ) {
        CustomerVipYearlyResult entity = customerVipYearlyResultDao
                .findByYearAndCustomerCode(calcYear, customer.getCode())
                .orElseGet(CustomerVipYearlyResult::new);

        entity.setCalcYear(calcYear);
        entity.setCustomerCode(customer.getCode());
        entity.setCustomerName(customer.getName());
        entity.setCustomerType(customer.getType());
        entity.setRegion(customer.getRegion());
        entity.setGroupCode(groupCode);
        entity.setTotalVipPoint(totalVipPoint);
        entity.setTierCode(tierCode);
        entity.setTierName(tierName);
        entity.setRewardAmount(rewardAmount);
        entity.setTotalRewardAmount(totalRewardAmount);
        entity.setPrivateBonusAmount(privateBonusAmount);
        entity.setFinalBonusAmount(finalBonusAmount);
        entity.setNextTierCode(nextTierCode);
        entity.setNextTierName(nextTierName);
        entity.setMissingPointToNext(missingPoint);
        entity.setMissingPointMessage(missingPointMessage);
        entity.setStatus(1);
        entity.setNote(note);

        if (entity.getId() == null) {
            customerVipYearlyResultDao.insert(entity);
        } else {
            customerVipYearlyResultDao.updateAll(entity.getId(), entity, true);
        }
    }

    private void validateProcessMonthRange(int fromProcessMonth, int toProcessMonth) {
        if (fromProcessMonth <= 0 || toProcessMonth <= 0) {
            throw new IllegalArgumentException("fromProcessMonth/toProcessMonth không hợp lệ");
        }

        if (fromProcessMonth > toProcessMonth) {
            throw new IllegalArgumentException("fromProcessMonth không được lớn hơn toProcessMonth");
        }

        int fromMonth = fromProcessMonth % 100;
        int toMonth = toProcessMonth % 100;

        if (fromMonth < 1 || fromMonth > 12 || toMonth < 1 || toMonth > 12) {
            throw new IllegalArgumentException("processMonth phải có dạng yyyymm hợp lệ");
        }
    }

    private int resolveCalcYear(int fromProcessMonth, int toProcessMonth) {
        return fromProcessMonth / 100;
    }

    private String buildNote(int fromProcessMonth, int toProcessMonth) {
        return "calc_range=" + fromProcessMonth + "-" + toProcessMonth;
    }

    /**
     * Giữ đúng flow cũ:
     * - bỏ dòng status != 1
     * - bỏ dòng hdn_status = KO
     * - group theo vthh_con
     * - nhưng đang lấy giá trị sl_hdn cuối cùng của từng vthh_con
     *   (KHÔNG cộng dồn), vì code cũ dùng put(vthhCon, slHdn)
     * - tra rule theo vthh_con + sl_hdn
     * - lấy hệ số MB/MN
     * - tổng điểm VIP = sum(he_so * sl_hdn)
     */
    private double calculateTotalVipPoint(List<SalesTransaction> items, String region) {
        if (items == null || items.isEmpty()) {
            return 0D;
        }

        Map<String, Double> slHdnByVthhCon = new LinkedHashMap<>();

        for (SalesTransaction x : items) {
            if (x == null || x.getStatus() == null || x.getStatus() != 1) {
                continue;
            }

            if ("KO".equalsIgnoreCase(safe(x.getHdnStatus()))) {
                continue;
            }

            String vthhCon = safe(x.getVthhCon()).trim().toUpperCase();
            if (vthhCon.isEmpty()) {
                continue;
            }

            double slHdn = nvl(x.getSlHdn());
            if (slHdn == 0D) {
                continue;
            }

            // Giữ đúng code cũ: overwrite giá trị cuối cùng, không cộng dồn
            slHdnByVthhCon.put(vthhCon, slHdn);
        }

        double totalVipPoint = 0D;
        boolean isMb = "MB".equalsIgnoreCase(safe(region));

        for (Map.Entry<String, Double> e : slHdnByVthhCon.entrySet()) {
            String vthhCon = e.getKey();
            double slHdn = e.getValue();

            VipPointRule rule = vipPointRuleDao.findMatchedRule(vthhCon, slHdn, 1).orElse(null);
            if (rule == null || rule.getStatus() == null || rule.getStatus() != 1) {
                continue;
            }

            double heSo = isMb ? nvl(rule.getHeSoMb()) : nvl(rule.getHeSoMn());
            totalVipPoint += heSo * slHdn;
        }

        return totalVipPoint;
    }

    private double calculatePrivateBonus(List<SalesTransaction> items) {
        double total = 0D;

        for (SalesTransaction x : items) {
            if (x == null || x.getStatus() == null || x.getStatus() != 1) {
                continue;
            }

            String privateCode = safe(x.getPrivateCode()).trim().toUpperCase();
            if (privateCode.isEmpty()) {
                continue;
            }

            VipPrivateBonusRule rule = vipPrivateBonusRuleDao.findByCode(privateCode).orElse(null);
            if (rule == null || rule.getStatus() == null || rule.getStatus() != 1) {
                continue;
            }

            total += nvl(x.getSlRiengTl()) * nvl(rule.getAmount());
        }

        return total;
    }

    private VipTier getMatchedTier(String groupCode, double totalVipPoint) {
        List<VipTier> tiers = vipTierDao.findAllActiveOrdered();
        if (tiers == null || tiers.isEmpty()) {
            return null;
        }

        VipTier matched = null;
        for (VipTier tier : tiers) {
            if (tier == null) {
                continue;
            }

            double requiredPoint = getPointByGroup(tier, groupCode);
            if (requiredPoint <= 0) {
                continue;
            }

            if (totalVipPoint >= requiredPoint) {
                matched = tier;
            }
        }

        return matched;
    }

    private VipTier getNextTier(String groupCode, VipTier currentTier) {
        List<VipTier> tiers = vipTierDao.findAllActiveOrdered();
        if (tiers == null || tiers.isEmpty()) {
            return null;
        }

        List<VipTier> supportedTiers = new ArrayList<>();
        for (VipTier tier : tiers) {
            if (tier == null) {
                continue;
            }
            if (getPointByGroup(tier, groupCode) > 0) {
                supportedTiers.add(tier);
            }
        }

        if (supportedTiers.isEmpty()) {
            return null;
        }

        if (currentTier == null) {
            return supportedTiers.get(0);
        }

        boolean foundCurrent = false;
        for (VipTier tier : supportedTiers) {
            if (foundCurrent) {
                return tier;
            }

            if (sameTier(currentTier, tier)) {
                foundCurrent = true;
            }
        }

        return null;
    }

    private boolean sameTier(VipTier a, VipTier b) {
        if (a == null || b == null) {
            return false;
        }

        if (a.getId() != null && b.getId() != null) {
            return a.getId().equals(b.getId());
        }

        return safe(a.getName()).equalsIgnoreCase(safe(b.getName()));
    }

    private double getPointByGroup(VipTier tier, String groupCode) {
        if (tier == null) {
            return 0D;
        }

        String g = safe(groupCode).trim().toUpperCase();
        if ("MB_B2B".equals(g)) {
            return nvl(tier.getMbB2bPoint());
        }
        if ("B2C".equals(g)) {
            return nvl(tier.getB2cPoint());
        }
        if ("B2B".equals(g)) {
            return nvl(tier.getB2bPoint());
        }

        return 0D;
    }

    private double getRewardByGroup(VipTier tier, String groupCode) {
        if (tier == null) {
            return 0D;
        }

        String g = safe(groupCode).trim().toUpperCase();
        if ("MB_B2B".equals(g)) {
            return nvl(tier.getMbB2bReward());
        }
        if ("B2C".equals(g)) {
            return nvl(tier.getB2cReward());
        }
        if ("B2B".equals(g)) {
            return nvl(tier.getB2bReward());
        }

        return 0D;
    }

    private String resolveGroupCode(String customerType, String region) {
        String type = safe(customerType).trim().toUpperCase();
        String area = safe(region).trim().toUpperCase();

        if ("B2C".equals(type)) {
            return "B2C";
        }

        if ("B2B".equals(type) && "MB".equals(area)) {
            return "MB_B2B";
        }

        if ("B2B".equals(type)) {
            return "B2B";
        }

        return type;
    }

    private String buildMissingPointMessage(double missingPoint, String nextTierName) {
        if (isBlank(nextTierName)) {
            return null;
        }

        if (missingPoint <= 0) {
            return "Đã đạt điều kiện lên hạng " + nextTierName;
        }

        return "Còn thiếu " + formatNumber(missingPoint) + " điểm để lên hạng " + nextTierName;
    }

    private double round(double v) {
        return Math.round(v * 100.0) / 100.0;
    }

    private String formatNumber(double v) {
        DecimalFormat df = new DecimalFormat("#,###.##");
        return df.format(v);
    }

    private double nvl(Double v) {
        return v == null ? 0D : v;
    }

    private Integer nvl(Integer v) {
        return v == null ? 0 : v;
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }
}