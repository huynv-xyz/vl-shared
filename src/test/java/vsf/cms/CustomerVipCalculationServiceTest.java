package vsf.cms;

import com.vlife.shared.dto.CustomerVipReportRow;
import com.vlife.shared.service.CustomerVipCalculationService;
import io.micronaut.context.ApplicationContext;

import java.util.List;

public class CustomerVipCalculationServiceTest {
    public static void main(String[] args) {
        try (ApplicationContext ctx = ApplicationContext.run()) {
            CustomerVipCalculationService service = ctx.getBean(CustomerVipCalculationService.class);

            List<CustomerVipReportRow> rows = service.calculateYear(2026);

            System.out.println("Total rows: " + rows.size());
            for (CustomerVipReportRow row : rows) {
                System.out.println(row.ma_kh + " - " + row.ten_kh + " - " + row.hang_vip);
            }
        }
    }
}