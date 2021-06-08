package dummy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.google.gson.Gson;

import dummy.controller.MyController;
import dummy.model.Account;
import dummy.model.Customer;
import dummy.repo.CustomerRepository;
import lombok.SneakyThrows;

@SpringBootTest(classes = RedisSampleApplication.class,
				webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = MyController.class)
@AutoConfigureMockMvc
class RedisSampleApplicationTests {
	
	@MockBean
	CustomerRepository repository;
	
	@Autowired
	MockMvc mvc;
	
	@Autowired
	Gson gson;
	
	@MockBean
	MyController controller;
	
	
	@Test
	@SneakyThrows(Exception.class)
	 void postcustomer_saveredis_httpstatusOK()  {
		List<Account> acclist = new ArrayList<>(); 
    	acclist.add(new Account(1367, "54000", 54));
    	Customer customer = new Customer(2, "34", "abc", acclist);
    	mvc.perform(post("/addcustomer")
    		.accept(MediaType.APPLICATION_JSON_VALUE)
    		.content(gson.toJson(customer, Customer.class)))
    		.andExpect(status().isOk());
	}
	
	@Test
	void redis_should_save() {
		
		List<Account> acclist = new ArrayList<>();
    	acclist.add(new Account(1367, "54000", 54));
    	Customer customer = new Customer(2, "34", "abc", acclist);
    	
		repository.save(customer);
    	assertThat(repository.findByExternalId("34")).isNotNull();
	}
	
	

	@Test
	void contextLoads() {
	}

}
