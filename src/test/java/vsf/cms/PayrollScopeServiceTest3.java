package vsf.cms;

import com.vlife.shared.jdbc.client.JdbcClient;
import com.vlife.shared.service.salary.PayrollScopeService;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.annotation.NonNull;

import java.util.List;
import java.util.Map;

public class PayrollScopeServiceTest3 {

    public static void main(String[] args) {
        try (ApplicationContext ctx = ApplicationContext.run()) {

            @NonNull PayrollScopeService service =
                    ctx.getBean(PayrollScopeService.class);

            @NonNull JdbcClient jdbc =
                    ctx.getBean(JdbcClient.class);

            String period = "202603";

            // 1. run service
            int inserted = service.rebuildScopes(period);

            System.out.println("Inserted scopes: " + inserted + " for period: " + period);

            // 2. verify DB count
            long count = jdbc.queryLong("""
                SELECT COUNT(*)
                FROM payroll_scopes
                WHERE period = :period
            """, Map.of("period", period));

            System.out.println("Total scopes in DB: " + count);

            // 3. check gross > 0 (FIX)
            long nonZero = jdbc.queryLong("""
                SELECT COUNT(*)
                FROM payroll_scopes
                WHERE period = :period
                  AND gross_amount > 0
            """, Map.of("period", period));

            System.out.println("Scopes with gross > 0: " + nonZero);

            // 4. sample data (FIX ALL COLUMN NAMES)
            List<String> rows = jdbc.queryList("""
                SELECT CONCAT(
                    'EMP=', employee_id,
                    ', ROLE=', role_code,
                    ', GROSS=', gross_amount,
                    ', BONUS=', role_bonus_amount,
                    ', COMPLETION=', completion_rate
                ) AS line
                FROM payroll_scopes
                WHERE period = :period
                LIMIT 10
            """, Map.of("period", period), rs -> rs.getString("line"));

            System.out.println("---- SAMPLE DATA ----");
            rows.forEach(System.out::println);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}