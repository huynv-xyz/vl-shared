package vsf.cms;

import com.vlife.shared.service.ProductImportService;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.annotation.NonNull;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProductImport {
    public static void main(String[] args) throws Exception {
        try (ApplicationContext ctx = ApplicationContext.run()) {
            @NonNull ProductImportService service =
                    ctx.getBean(ProductImportService.class);

            String filePath = "files/product.csv";

            System.out.println("user.dir = " + System.getProperty("user.dir"));
            System.out.println("absolute = " + Path.of(filePath).toAbsolutePath());
            System.out.println("exists = " + Files.exists(Path.of(filePath)));

            int inserted = service.importCsv(filePath);

            System.out.println("Inserted: " + inserted);
        }
    }
}
