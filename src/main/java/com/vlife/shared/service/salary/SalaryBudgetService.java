package com.vlife.shared.service.salary;

import com.vlife.shared.jdbc.client.JdbcClient;
import jakarta.inject.Singleton;

import java.util.Map;

@Singleton
public class SalaryBudgetService {

    private final JdbcClient jdbc;
    private final SalaryDataLoader loader;

    public SalaryBudgetService(JdbcClient jdbc, SalaryDataLoader loader) {
        this.jdbc = jdbc;
        this.loader = loader;
    }

    public void rebuild(String period) {

        int year = Integer.parseInt(period.substring(0, 4));
        var config = loader.loadConfig();

        jdbc.update("DELETE FROM salary_budgets WHERE period = :p",
                Map.of("p", period));

        var scopes = loader.loadScopes();
        var targets = loader.loadTargets(year);
        var regions = loader.loadRegionConfigs();
        var managers = loader.loadManagers(period);

        for (var s : scopes) {

            var t = targets.get(s.emp);
            var c = regions.get(s.region);

            if (t == null || c == null) continue;

            // 🔥 TARGET_GTQD (KHÔNG lưu, chỉ dùng tính tiền)
            double targetGtqd =
                    t.bonGoc * c.hsGoc +
                            t.bonLong * c.hsLong +
                            t.bonBot * c.hsBot +
                            t.clcn * c.hsClcn;

            // 🔥 THU_NHAP (monthly)
            double thuNhap = (targetGtqd / 12.0) * config.unitPrice;

            jdbc.update("""
                INSERT INTO salary_budgets (
                    period,
                    employee_id,
                    role_code,
                    region_code,
                    province_code,
                    total_budget,
                    salary_budget,
                    bonus_budget,
                    has_manager
                )
                VALUES (:p,:e,:r,:rg,:pv,:t,:s,:b,:m)
            """, Map.ofEntries(
                    Map.entry("p", period),
                    Map.entry("e", s.emp),
                    Map.entry("r", safe(s.role)),
                    Map.entry("rg", safe(s.region)),
                    Map.entry("pv", safe(s.province)),
                    Map.entry("t", thuNhap),
                    Map.entry("s", thuNhap * config.salaryRatio),
                    Map.entry("b", thuNhap * config.bonusRatio),
                    Map.entry("m", managers.contains(s.emp) ? 1 : 0)
            ));
        }
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }
}