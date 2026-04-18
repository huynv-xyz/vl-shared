package com.vlife.shared.service.salary;

import com.vlife.shared.jdbc.client.JdbcClient;
import jakarta.inject.Singleton;

import java.util.*;

@Singleton
public class SalaryResultService {

    private final JdbcClient jdbc;

    public SalaryResultService(JdbcClient jdbc) {
        this.jdbc = jdbc;
    }

    public void calculate(String period) {

        // 1. clear
        jdbc.update("DELETE FROM salary_results WHERE period = :p",
                Map.of("p", period));

        // 2. load scopes
        List<Row> scopes = jdbc.queryList("""
            SELECT *
            FROM salary_scopes
            WHERE period = :p
        """, Map.of("p", period), rs -> {
            Row r = new Row();
            r.emp = rs.getInt("employee_id");
            r.base = rs.getDouble("base_salary");
            r.allowance = rs.getDouble("allowance");
            r.bonus = rs.getDouble("role_bonus");
            r.gross = rs.getDouble("gross_income");
            return r;
        });

        // 3. group by employee
        Map<Integer, Agg> map = new HashMap<>();

        for (Row r : scopes) {

            Agg agg = map.computeIfAbsent(r.emp, k -> new Agg());

            agg.base += r.base;
            agg.allowance += r.allowance;
            agg.bonus += r.bonus;
            agg.gross += r.gross;
        }

        // 4. calculate + insert
        for (Map.Entry<Integer, Agg> e : map.entrySet()) {

            int emp = e.getKey();
            Agg a = e.getValue();

            double bhxh = 0;
            double tax = 0;
            double union = 0;

            double net = a.gross - bhxh - tax - union;

            jdbc.update("""
                INSERT INTO salary_results (
                    period,
                    employee_id,
                    total_base_salary,
                    total_allowance,
                    total_bonus,
                    gross_income,
                    social_insurance,
                    union_fee,
                    personal_income_tax,
                    net_income
                )
                VALUES (:p,:e,:base,:allowance,:bonus,:gross,:bhxh,:union,:tax,:net)
            """, Map.ofEntries(
                    Map.entry("p", period),
                    Map.entry("e", emp),
                    Map.entry("base", a.base),
                    Map.entry("allowance", a.allowance),
                    Map.entry("bonus", a.bonus),
                    Map.entry("gross", a.gross),
                    Map.entry("bhxh", bhxh),
                    Map.entry("union", union),
                    Map.entry("tax", tax),
                    Map.entry("net", net)
            ));
        }
    }

    // ===== inner classes =====

    static class Row {
        int emp;
        double base, allowance, bonus, gross;
    }

    static class Agg {
        double base = 0;
        double allowance = 0;
        double bonus = 0;
        double gross = 0;
    }
}