package vsf.cms;

import com.vlife.shared.jdbc.dao.CustomerDao;
import com.vlife.shared.jdbc.entity.Customer;
import io.micronaut.context.ApplicationContext;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.data.model.Page;
import io.micronaut.data.model.Pageable;

import java.util.List;

public class CustomerTest {
    public static void main(String[] args) {
        try (ApplicationContext ctx = ApplicationContext.run()) {
            @NonNull CustomerDao dao = ctx.getBean(CustomerDao.class);

            Page<Customer> rs = dao.findAll(Pageable.from(0 , 20));
            @NonNull List<Customer> items = rs.getContent();
            for(Customer item:  items) {
                System.out.println("item:"+ item.getName());
            }
        }
    }
}
