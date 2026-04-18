package com.vlife.shared.service.salary;

import com.vlife.shared.jdbc.client.JdbcClient;
import jakarta.inject.Singleton;

import java.time.LocalDateTime;
import java.util.Map;

@Singleton
public class EmployeeSalaryBudgetService {

    private final JdbcClient jdbc;
    private final SalaryDataLoader loader;

    public EmployeeSalaryBudgetService(JdbcClient jdbc, SalaryDataLoader loader) {
        this.jdbc = jdbc;
        this.loader = loader;
    }

    public void rebuild(String period) {

        int year = Integer.parseInt(period.substring(0, 4));
        LocalDateTime now = LocalDateTime.now();

        var config = loader.loadConfig();
        var scopes = loader.loadScopes();
        var targets = loader.loadTargets(year);
        var regions = loader.loadRegionConfigs();
        var managers = loader.loadManagers(period);

        jdbc.update("""
            DELETE FROM employee_salary_budgets
            WHERE period = :p
        """, Map.of("p", period));

        for (var s : scopes) {
            var t = targets.get(s.emp);
            var c = regions.get(s.region);

            if (c == null) {
                continue;
            }

            double bonGoc = t == null ? 0 : t.bonGoc;
            double bonLong = t == null ? 0 : t.bonLong;
            double bonBot = t == null ? 0 : t.bonBot;
            double clcn = t == null ? 0 : t.clcn;

            double targetGtqd =
                    bonGoc * c.hsGoc
                            + bonLong * c.hsLong
                            + bonBot * c.hsBot
                            + clcn * c.hsClcn;

            double totalBudget = round2(targetGtqd * config.unitPrice);
            double budget80 = round2(totalBudget * config.salaryRatio);
            double budget20 = round2(totalBudget * config.bonusRatio);

            boolean hasAsm = managers.contains(s.emp);

            jdbc.update("""
                INSERT INTO employee_salary_budgets (
                    period,
                    employee_id,
                    role_code,
                    region_code,
                    province_code,
                    total_budget,
                    budget_80,
                    budget_20,
                    has_asm,
                    created_at,
                    updated_at
                )
                VALUES (
                    :period,
                    :emp,
                    :role,
                    :region,
                    :province,
                    :total,
                    :b80,
                    :b20,
                    :hasAsm,
                    :createdAt,
                    :updatedAt
                )
            """, Map.ofEntries(
                    Map.entry("period", period),
                    Map.entry("emp", s.emp),
                    Map.entry("role", mapRoleCode(s.role)),
                    Map.entry("region", nvl(s.region)),
                    Map.entry("province", nvl(s.province)),
                    Map.entry("total", totalBudget),
                    Map.entry("b80", budget80),
                    Map.entry("b20", budget20),
                    Map.entry("hasAsm", hasAsm ? 1 : 0),
                    Map.entry("createdAt", now),
                    Map.entry("updatedAt", now)
            ));
        }
    }

    private String mapRoleCode(String role) {
        if (role == null) {
            return "UNKNOWN";
        }

        return switch (role) {
            case "SALE" -> "SALE_SELF";
            case "ASM" -> "MGR_PROV";
            case "RM" -> "MGR_REGION";
            case "TECH" -> "TECH_PROV";
            default -> role;
        };
    }

    private String nvl(String value) {
        return value == null ? "" : value;
    }

    private double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}