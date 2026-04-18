package vsf.cms;

import com.vlife.shared.service.salary.PayrollService;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.annotation.NonNull;

public class PayrollServiceTest {

    public static void main(String[] args) {
        try (ApplicationContext ctx = ApplicationContext.run()) {


            @NonNull PayrollService service =
                    ctx.getBean(PayrollService.class);

            String period = "202603";

            service.run(period);

            System.out.println("Payroll calculated for period: " + period);
        }
    }
}