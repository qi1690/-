package my_crm_management;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.itcast.main.crm.domain.Customer;
import com.itcast.main.service.CustomerService;

@RunWith(SpringJUnit4ClassRunner.class)     
@ContextConfiguration(locations = "classpath:applicationContext.xml")
public class TestCustomer {
	@Autowired
	private CustomerService customerService;

	@Test
	public void testFindNoAssociationCustomers() {
		List<Customer> list = customerService.findNoAssociationCustomers();
		System.out.println(list);
	}
}
