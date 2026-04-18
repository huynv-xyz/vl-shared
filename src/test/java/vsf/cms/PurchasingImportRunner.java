package vsf.cms;

import com.vlife.shared.service.PurchasingImportService;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.annotation.NonNull;

import java.nio.file.Files;
import java.nio.file.Path;

public class PurchasingImportRunner {

    public static void main(String[] args) throws Exception {
        try (ApplicationContext ctx = ApplicationContext.run()) {
            @NonNull PurchasingImportService service =
                    ctx.getBean(PurchasingImportService.class);

            String filePath = "files/xnk.csv";

            System.out.println("user.dir = " + System.getProperty("user.dir"));
            System.out.println("absolute = " + Path.of(filePath).toAbsolutePath());
            System.out.println("exists = " + Files.exists(Path.of(filePath)));

            PurchasingImportService.ImportResult result = service.importCsv(filePath);

            System.out.println("Import result = " + result);
            System.out.println("Total affected = " + result.totalAffected());
        }
    }
}