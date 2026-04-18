package com.vlife.shared.service.salary;

import com.vlife.shared.jdbc.client.JdbcClient;
import jakarta.inject.Singleton;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class SalaryDataLoader {

    private final JdbcClient jdbc;

    public SalaryDataLoader(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    // ===== LOAD SCOPES =====
    public List<Scope> loadScopes() {
        LocalDate today = LocalDate.now();

        return jdbc.queryList("""
            SELECT s.employee_id, r.code role_code, rg.code region_code, p.code province_code
            FROM employee_scopes s
            JOIN roles r ON r.id = s.role_id
            JOIN regions rg ON rg.id = s.region_id
            LEFT JOIN provinces p ON p.id = s.province_id
            JOIN employees e ON e.id = s.employee_id
            WHERE s.status = 1
              AND e.status = 1
              AND s.effective_from <= :today
              AND (s.effective_to IS NULL OR s.effective_to >= :today)
        """, Map.of("today", today), rs -> {
            Scope s = new Scope();
            s.emp = rs.getInt("employee_id");
            s.role = rs.getString("role_code");
            s.region = rs.getString("region_code");
            s.province = rs.getString("province_code");
            return s;
        });
    }

    // ===== LOAD TARGET =====
    public Map<Integer, Target> loadTargets(int year) {
        return jdbc.queryList("""
            SELECT * FROM sales_targets WHERE year = :y
        """, Map.of("y", year), rs -> {
            Target t = new Target();
            t.emp = rs.getInt("employee_id");
            t.bonGoc = rs.getDouble("bon_goc");
            t.bonLong = rs.getDouble("bon_la_long");
            t.bonBot = rs.getDouble("bon_la_bot");
            t.clcn = rs.getDouble("clcn");
            return Map.entry(t.emp, t);
        }).stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // ===== LOAD ACTUAL =====
    public Map<Integer, Actual> loadActuals(String period) {
        return jdbc.queryList("""
            SELECT * FROM sales_actuals WHERE period = :p
        """, Map.of("p", period), rs -> {
            Actual a = new Actual();
            a.emp = rs.getInt("employee_id");
            a.bonGoc = rs.getDouble("bon_goc");
            a.bonLong = rs.getDouble("bon_la_long");
            a.bonBot = rs.getDouble("bon_la_bot");
            a.clcn = rs.getDouble("clcn");
            a.debt = rs.getDouble("debt_rate");
            return Map.entry(a.emp, a);
        }).stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // ===== LOAD REGION CONFIG =====
    public Map<String, RegionConfig> loadRegionConfigs() {
        LocalDate today = LocalDate.now();

        return jdbc.queryList("""
            SELECT rg.code, c.*
            FROM region_income_configs c
            JOIN regions rg ON rg.id = c.region_id
            WHERE c.status = 1
              AND c.effective_from <= :today
              AND (c.effective_to IS NULL OR c.effective_to >= :today)
        """, Map.of("today", today), rs -> {
            RegionConfig c = new RegionConfig();
            c.hsGoc = rs.getDouble("hs_bon_goc");
            c.hsLong = rs.getDouble("hs_bon_la_l");
            c.hsBot = rs.getDouble("hs_bon_la_b");
            c.hsClcn = rs.getDouble("hs_clcn");
            return Map.entry(rs.getString("code"), c);
        }).stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (a, b) -> b
        ));
    }

    // ===== LOAD ROLE RATE =====
    public Map<String, RoleRate> loadRoleRates() {
        return jdbc.queryList("""
            SELECT r.code, rr.*
            FROM role_rates rr
            JOIN roles r ON r.id = rr.role_id
            WHERE rr.status = 1
        """, Map.of(), rs -> {
            RoleRate rr = new RoleRate();
            rr.salaryRate = rs.getDouble("salary_rate");
            rr.bonusRate = rs.getDouble("bonus_rate");
            rr.basicRate = rs.getDouble("basic_salary_rate");
            rr.allowanceRate = rs.getDouble("allowance_rate");
            return Map.entry(rs.getString("code"), rr);
        }).stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    // ===== LOAD MANAGER =====
    public Set<Integer> loadManagers(String period) {
        return jdbc.queryList("""
            SELECT DISTINCT sales_employee_id
            FROM manager_mappings
            WHERE period = :p
        """, Map.of("p", period), rs -> rs.getInt(1))
                .stream().collect(Collectors.toSet());
    }

    public SalaryConfig loadConfig() {
        LocalDate today = LocalDate.now();

        var map = jdbc.queryList("""
            SELECT config_key, config_value
            FROM system_configs
            WHERE status = 1
              AND effective_from <= :today
              AND (effective_to IS NULL OR effective_to >= :today)
        """, Map.of("today", today), rs -> Map.entry(
                rs.getString("config_key"),
                rs.getDouble("config_value")
        )).stream().collect(Collectors.toMap(
                Map.Entry::getKey,
                Map.Entry::getValue,
                (a, b) -> b
        ));

        SalaryConfig c = new SalaryConfig();

        c.salaryRatio = map.getOrDefault("SALARY_RATIO", 0.8);
        c.bonusRatio = map.getOrDefault("BONUS_RATIO", 0.2);
        c.minComplete = map.getOrDefault("MIN_COMPLETION_RATE", 0.8);
        c.minDebt = map.getOrDefault("MIN_DEBT_RATE", 0.95);
        c.unitPrice = map.getOrDefault("GTQD_UNIT_PRICE", 0.0);

        return c;
    }

    // ===== MODELS =====
    public static class Scope {
        public int emp;
        public String role, region, province;
        public double salaryBudget, bonusBudget;
    }

    public static class Target {
        public int emp;
        public double bonGoc, bonLong, bonBot, clcn;
    }

    public static class Actual {
        public int emp;
        public double bonGoc, bonLong, bonBot, clcn, debt;
    }

    public static class RegionConfig {
        public double hsGoc, hsLong, hsBot, hsClcn;
    }

    public static class RoleRate {
        public double salaryRate, bonusRate, basicRate, allowanceRate;
    }

    static class SalaryConfig {
        double salaryRatio;
        double bonusRatio;
        double minComplete;
        double minDebt;
        double unitPrice;
    }
}