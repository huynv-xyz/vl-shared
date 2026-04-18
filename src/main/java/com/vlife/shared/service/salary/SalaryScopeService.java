package com.vlife.shared.service.salary;

import com.vlife.shared.jdbc.client.JdbcClient;
import jakarta.inject.Singleton;

import java.util.Map;

@Singleton
public class SalaryScopeService {

    private final JdbcClient jdbc;
    private final SalaryDataLoader loader;

    public SalaryScopeService(JdbcClient jdbc, SalaryDataLoader loader) {
        this.jdbc = jdbc;
        this.loader = loader;
    }

    public void calculate(String period) {

        var config = loader.loadConfig();

        jdbc.update("DELETE FROM salary_scopes WHERE period = :p",
                Map.of("p", period));

        var budgets = jdbc.queryList("""
            SELECT *
            FROM salary_budgets
            WHERE period = :p
        """, Map.of("p", period), rs -> {
            Row r = new Row();
            r.emp = rs.getInt("employee_id");
            r.role = rs.getString("role_code");
            r.region = rs.getString("region_code");
            r.province = rs.getString("province_code");
            r.salaryBudget = rs.getDouble("salary_budget");
            r.bonusBudget = rs.getDouble("bonus_budget");
            return r;
        });

        var roles = loader.loadRoleRates();
        var regions = loader.loadRegionConfigs();
        var targets = loader.loadTargets(Integer.parseInt(period.substring(0, 4)));
        var actuals = loader.loadActuals(period);

        for (var s : budgets) {

            var rr = roles.get(s.role);
            var rc = regions.get(s.region);
            var t = targets.get(s.emp);
            var a = actuals.get(s.emp);

            // ===== VALIDATE DATA =====
            if (rr == null) {
                System.out.println("❌ Missing role_rate: " + s.role);
                continue;
            }
            if (rc == null) {
                System.out.println("❌ Missing region_config: " + s.region);
                continue;
            }
            if (t == null) {
                System.out.println("❌ Missing target: emp=" + s.emp);
                continue;
            }

            // ===== TARGET GTQD =====
            double targetGtqd =
                    safe(t.bonGoc) * rc.hsGoc +
                            safe(t.bonLong) * rc.hsLong +
                            safe(t.bonBot) * rc.hsBot +
                            safe(t.clcn) * rc.hsClcn;

            if (targetGtqd == 0) {
                System.out.println("⚠️ ZERO TARGET: emp=" + s.emp);
            }

            // ===== ACTUAL GTQD =====
            double actualGtqd = 0;
            double debt = 0;

            if (a != null) {
                actualGtqd =
                        safe(a.bonGoc) * rc.hsGoc +
                                safe(a.bonLong) * rc.hsLong +
                                safe(a.bonBot) * rc.hsBot +
                                safe(a.clcn) * rc.hsClcn;

                debt = a.debt;
            }

            // ===== KPI =====
            double completion = targetGtqd == 0 ? 0 : actualGtqd / targetGtqd;

            boolean eligible =
                    completion >= config.minComplete &&
                            debt >= config.minDebt;

            // ===== ROLE SPLIT =====
            double totalBudget = s.salaryBudget + s.bonusBudget;

            double roleSalary = s.salaryBudget * rr.salaryRate;
            double roleBonus = eligible ? s.bonusBudget * rr.bonusRate : 0;

            double baseSalary = roleSalary * rr.basicRate;
            double allowance = roleSalary * rr.allowanceRate;

            double gross = baseSalary + allowance + roleBonus;

            // ===== DEBUG =====
            System.out.println("""
                EMP=%d | TARGET=%.2f | ACTUAL=%.2f | COMPLETE=%.4f
            """.formatted(
                    s.emp,
                    targetGtqd,
                    actualGtqd,
                    completion
            ));

            // ===== INSERT =====
            jdbc.update("""
                INSERT INTO salary_scopes (
                    period,
                    employee_id,
                    role_code,
                    region_code,
                    province_code,
                    total_budget,
                    salary_budget,
                    bonus_budget,
                    completion_rate,
                    debt_rate,
                    is_bonus_eligible,
                    role_salary,
                    role_bonus,
                    base_salary,
                    allowance,
                    gross_income
                )
                VALUES (
                    :p,:e,:r,:rg,:pv,:t,:s,:b,:c,:d,:ok,:rs,:rb,:bs,:al,:g
                )
            """, Map.ofEntries(
                    Map.entry("p", period),
                    Map.entry("e", s.emp),
                    Map.entry("r", safe(s.role)),
                    Map.entry("rg", safe(s.region)),
                    Map.entry("pv", safe(s.province)),
                    Map.entry("t", totalBudget),
                    Map.entry("s", s.salaryBudget),
                    Map.entry("b", s.bonusBudget),
                    Map.entry("c", completion),
                    Map.entry("d", debt),
                    Map.entry("ok", eligible ? 1 : 0),
                    Map.entry("rs", roleSalary),
                    Map.entry("rb", roleBonus),
                    Map.entry("bs", baseSalary),
                    Map.entry("al", allowance),
                    Map.entry("g", gross)
            ));
        }
    }

    static class Row {
        int emp;
        String role, region, province;
        double salaryBudget, bonusBudget;
    }

    private double safe(Double v) {
        return v == null ? 0 : v;
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}