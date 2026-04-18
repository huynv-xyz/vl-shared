package vsf.cms;

import com.vlife.shared.service.SalesTransactionImportService;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.annotation.NonNull;

public class SalesTransactionImportServiceTest {

    public static void main(String[] args) throws Exception {
        try (ApplicationContext ctx = ApplicationContext.run()) {
            @NonNull SalesTransactionImportService service =
                    ctx.getBean(SalesTransactionImportService.class);

            String filePath = "files/misa1.csv";

            int inserted = service.importCsv(filePath);

            System.out.println("Inserted: " + inserted);
        }
    }
}