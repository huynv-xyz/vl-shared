package vsf.cms;

import com.vlife.shared.service.salary.SalaryBudgetService;
import com.vlife.shared.service.salary.SalaryScopeService;
import com.vlife.shared.service.salary.SalaryResultService;
import com.vlife.shared.service.salary.SalaryOrchestratorService;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.annotation.NonNull;

public class SalaryPipelineTest {

    public static void main(String[] args) {
        try (ApplicationContext ctx = ApplicationContext.run()) {

            String period = "202603";

            @NonNull SalaryBudgetService budgetService =
                    ctx.getBean(SalaryBudgetService.class);

            @NonNull SalaryScopeService scopeService =
                    ctx.getBean(SalaryScopeService.class);

            @NonNull SalaryResultService resultService =
                    ctx.getBean(SalaryResultService.class);

            @NonNull SalaryOrchestratorService orchestrator =
                    ctx.getBean(SalaryOrchestratorService.class);

            // ===== CHOOSE MODE =====
            String mode = "ALL"; // BUDGET | SCOPE | RESULT | ALL

            mode = "BUDGET";
            mode = "SCOPE";
            //mode = "RESULT";
            mode = "ALL";

            switch (mode) {

                case "BUDGET":
                    budgetService.rebuild(period);
                    System.out.println("Budget done");
                    break;

                case "SCOPE":
                    scopeService.calculate(period);
                    System.out.println("Scope done");
                    break;

                case "RESULT":
                    resultService.calculate(period);
                    System.out.println("Result done");
                    break;

                case "ALL":
                default:
                    orchestrator.run(period);
                    System.out.println("Full pipeline done");
                    break;
            }

            System.out.println("Finished for period: " + period);
        }
    }
}