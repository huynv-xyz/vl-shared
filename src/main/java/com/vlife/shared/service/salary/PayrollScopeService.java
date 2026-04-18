package com.vlife.shared.service.salary;

import com.vlife.shared.jdbc.client.JdbcClient;
import jakarta.inject.Singleton;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Singleton
public class PayrollScopeService {

    private final JdbcClient jdbc;

    public PayrollScopeService(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public int rebuildScopes(String period) {

        // 1. clear
        jdbc.update("""
            DELETE FROM payroll_scopes
            WHERE period = :period
        """, Map.of("period", period));

        // 2. load config
        double salaryRatio = getConfig("SALARY_RATIO");
        double bonusRatio  = getConfig("BONUS_RATIO");
        double minComplete = getConfig("MIN_COMPLETION_RATE");

        // 3. load data
        List<Row> rows = jdbc.queryList("""
            SELECT 
                b.employee_id,
                b.role_code,
                b.region_code,
                b.province_code,

                b.total_budget,

                rr.salary_rate,
                rr.bonus_rate,
                rr.basic_salary_rate,
                rr.allowance_rate,

                COALESCE(sa.main, 0) AS actual_gtqd,
                COALESCE(st.target_gtqd, 0) / 12 AS target_gtqd_month

            FROM employee_salary_budgets b

            JOIN roles r ON r.code = b.role_code

            JOIN role_rates rr 
                ON rr.role_id = r.id
               AND rr.status = 1

            LEFT JOIN sales_actuals sa 
                ON sa.employee_id = b.employee_id
               AND sa.period = :period

            LEFT JOIN (
                SELECT 
                    t.employee_id,
                    (
                        COALESCE(t.bon_goc,0) * c.hs_bon_goc +
                        COALESCE(t.bon_la_long,0) * c.hs_bon_la_l +
                        COALESCE(t.bon_la_bot,0) * c.hs_bon_la_b +
                        COALESCE(t.clcn,0) * c.hs_clcn
                    ) AS target_gtqd
                FROM sales_targets t
                JOIN employee_scopes s ON s.employee_id = t.employee_id
                JOIN region_income_configs c ON c.region_id = s.region_id
                WHERE t.year = SUBSTRING(:period,1,4)
            ) st ON st.employee_id = b.employee_id

            WHERE b.period = :period
        """, Map.of("period", period), rs -> {
            Row r = new Row();
            r.employeeId = rs.getInt("employee_id");
            r.roleCode = rs.getString("role_code");
            r.regionCode = rs.getString("region_code");
            r.provinceCode = rs.getString("province_code");

            r.totalBudget = rs.getDouble("total_budget");

            r.salaryRate = rs.getDouble("salary_rate");
            r.bonusRate = rs.getDouble("bonus_rate");
            r.baseRate = rs.getDouble("basic_salary_rate");
            r.allowanceRate = rs.getDouble("allowance_rate");

            r.actualGtqd = rs.getDouble("actual_gtqd");
            r.targetGtqdMonth = rs.getDouble("target_gtqd_month");

            return r;
        });

        int count = 0;

        // 4. process
        for (Row r : rows) {

            double salaryPool = r.totalBudget * salaryRatio;
            double bonusPool  = r.totalBudget * bonusRatio;

            double roleSalary = salaryPool * r.salaryRate;
            double roleBonus  = bonusPool * r.bonusRate;

            double completion = r.targetGtqdMonth > 0
                    ? r.actualGtqd / r.targetGtqdMonth
                    : 0;

            boolean eligible = completion >= minComplete;

            if (!eligible) {
                roleBonus = 0;
            }

            double base = roleSalary * r.baseRate;
            double allowance = roleSalary * r.allowanceRate;

            double gross = base + allowance + roleBonus;

            // 5. insert
            jdbc.update("""
                INSERT INTO payroll_scopes (
                    period,
                    employee_id,
                    role_code,
                    region_code,
                    province_code,
                    income_amount,
                    salary_portion,
                    bonus_portion,
                    role_salary_amount,
                    role_bonus_amount,
                    base_salary,
                    allowance_amount,
                    completion_rate,
                    debt_rate,
                    is_bonus_eligible,
                    gross_amount,
                    created_at
                )
                VALUES (
                    :period,
                    :emp,
                    :role,
                    :region,
                    :province,
                    :income,
                    :salaryPortion,
                    :bonusPortion,
                    :roleSalary,
                    :roleBonus,
                    :base,
                    :allowance,
                    :completion,
                    :debt,
                    :eligible,
                    :gross,
                    :now
                )
            """, Map.ofEntries(
                    Map.entry("period", period),
                    Map.entry("emp", r.employeeId),
                    Map.entry("role", r.roleCode),
                    Map.entry("region", r.regionCode),
                    Map.entry("province", r.provinceCode),

                    Map.entry("income", r.totalBudget),
                    Map.entry("salaryPortion", salaryPool),
                    Map.entry("bonusPortion", bonusPool),

                    Map.entry("roleSalary", roleSalary),
                    Map.entry("roleBonus", roleBonus),

                    Map.entry("base", base),
                    Map.entry("allowance", allowance),

                    Map.entry("completion", completion),
                    Map.entry("debt", 1.0), // TODO: lấy từ DB sau

                    Map.entry("eligible", eligible ? 1 : 0),
                    Map.entry("gross", gross),
                    Map.entry("now", LocalDateTime.now())
            ));

            count++; // 🔥 QUAN TRỌNG
        }

        return count;
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
        String roleCode;
        String regionCode;
        String provinceCode;

        double totalBudget;

        double salaryRate;
        double bonusRate;
        double baseRate;
        double allowanceRate;

        double actualGtqd;
        double targetGtqdMonth;
    }
}