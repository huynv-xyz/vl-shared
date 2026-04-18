package com.vlife.shared.service.salary;

import com.vlife.shared.jdbc.client.JdbcClient;
import jakarta.inject.Singleton;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Singleton
public class PayrollService {

    private final JdbcClient jdbc;

    public PayrollService(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public int run(String period) {

        // 1. clear old
        jdbc.update("""
            DELETE FROM payrolls
            WHERE period = :period
        """, Map.of("period", period));

        // 2. load config
        double bhRate = getConfig("SOCIAL_INSURANCE_RATE");
        double unionRate = getConfig("UNION_RATE");
        double personalDeduction = getConfig("PERSONAL_DEDUCTION");
        double dependentDeduction = getConfig("DEPENDENT_DEDUCTION");

        // 3. load aggregated scopes
        List<Row> rows = jdbc.queryList("""
            SELECT 
                e.id AS employee_id,
                e.name,
                e.insurance_base,
                e.dependent_count,
                e.is_union_member,

                SUM(ps.base_salary) AS total_base_salary,
                SUM(ps.allowance_amount) AS total_allowance,
                SUM(ps.role_bonus_amount) AS total_bonus,
                SUM(ps.gross_amount) AS gross_total

            FROM payroll_scopes ps
            JOIN employees e ON e.id = ps.employee_id

            WHERE ps.period = :period

            GROUP BY e.id, e.name, e.insurance_base, e.dependent_count, e.is_union_member
        """, Map.of("period", period), rs -> {
            Row r = new Row();
            r.employeeId = rs.getInt("employee_id");
            r.name = rs.getString("name");

            r.insuranceBase = rs.getDouble("insurance_base");
            r.dependentCount = rs.getInt("dependent_count");
            r.isUnionMember = rs.getInt("is_union_member") == 1;

            r.totalBase = rs.getDouble("total_base_salary");
            r.totalAllowance = rs.getDouble("total_allowance");
            r.totalBonus = rs.getDouble("total_bonus");
            r.gross = rs.getDouble("gross_total");

            return r;
        });

        int count = 0;

        for (Row r : rows) {

            // 4. BHXH
            double bhxh = r.insuranceBase * bhRate;

            // 5. union fee
            double unionFee = r.isUnionMember
                    ? r.gross * unionRate
                    : 0;

            // 6. taxable income
            double taxable = r.gross
                    - bhxh
                    - personalDeduction
                    - r.dependentCount * dependentDeduction;

            if (taxable < 0) taxable = 0;

            // 7. tax
            double tax = calculateTax(taxable);

            // 8. net
            double net = r.gross - bhxh - unionFee - tax;

            // 9. insert
            jdbc.update("""
                INSERT INTO payrolls (
                    period,
                    employee_id,
                    total_base_salary,
                    total_allowance,
                    total_bonus,
                    gross_total,
                    social_insurance,
                    union_fee,
                    personal_income_tax,
                    net_total,
                    created_at
                )
                VALUES (
                    :period,
                    :emp,
                    :base,
                    :allowance,
                    :bonus,
                    :gross,
                    :bhxh,
                    :union,
                    :tax,
                    :net,
                    :now
                )
            """, Map.ofEntries(
                    Map.entry("period", period),
                    Map.entry("emp", r.employeeId),
                    Map.entry("base", r.totalBase),
                    Map.entry("allowance", r.totalAllowance),
                    Map.entry("bonus", r.totalBonus),
                    Map.entry("gross", r.gross),
                    Map.entry("bhxh", bhxh),
                    Map.entry("union", unionFee),
                    Map.entry("tax", tax),
                    Map.entry("net", net),
                    Map.entry("now", LocalDateTime.now())
            ));

            count++;
        }

        return count;
    }

    private double calculateTax(double income) {

        // đọc bảng tax_brackets
        List<TaxRow> brackets = jdbc.queryList("""
            SELECT income_from, tax_rate, quick_deduction
            FROM tax_brackets
            WHERE status = 1
            ORDER BY income_from DESC
        """, Map.of(), rs -> {
            TaxRow t = new TaxRow();
            t.from = rs.getDouble("income_from");
            t.rate = rs.getDouble("tax_rate");
            t.quick = rs.getDouble("quick_deduction");
            return t;
        });

        for (TaxRow b : brackets) {
            if (income >= b.from) {
                return income * b.rate - b.quick;
            }
        }

        return 0;
    }

    private double getConfig(String key) {
        return jdbc.queryOne("""
            SELECT config_value
            FROM system_configs
            WHERE config_key = :key
              AND status = 1
            ORDER BY effective_from DESC
            LIMIT 1
        """, Map.of("key", key), rs -> rs.getDouble("config_value"))
                .orElseThrow(() -> new RuntimeException("Missing config: " + key));
    }

    private static class Row {
        int employeeId;
        String name;

        double insuranceBase;
        int dependentCount;
        boolean isUnionMember;

        double totalBase;
        double totalAllowance;
        double totalBonus;
        double gross;
    }

    private static class TaxRow {
        double from;
        double rate;
        double quick;
    }
}