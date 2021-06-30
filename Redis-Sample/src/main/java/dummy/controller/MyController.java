package dummy.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dummy.model.Account;
import dummy.model.Customer;
import dummy.model.Transaction;
import dummy.repo.CustomerRepository;
import dummy.repo.RedisBatchRepository;
import dummy.repo.TransactionRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

/**
 *  This class is an API exposed to CRUD operation using Redis Server.
 *  You can add more API as you want  with respect to Swagger OpenAPI 3.0 with security
 * @author Sayantan 
 * @since JDK 1.0 
 * @version 1.0
 * @see RedisBatchRepository
 * @see CustomerRepository
 * @see TransactionRepository
 */
@OpenAPIDefinition(servers = { @Server(url = "http://localhost:8080") }, info = @Info(title = "Sample Spring Boot API", version = "v2", description = "A demo project using Spring Boot with Swagger-UI enabled", license = @License(name = "MIT License", url = "www.abc.com"), contact = @Contact(url = "www.abc.com", name = "Sayantan Chatterjee")))
@RestController
@RequestMapping("/api")
public class MyController {
	 
	@Autowired
	RedisBatchRepository batchrepository;

	@Autowired
    CustomerRepository repository;
	
	@Autowired
    TransactionRepository transrepository;
	/**
	 * To save as a <code>Customer</code> cache with Redis.  
	 * @param customer needs to be JSON body
	 * @return {@link Customer} 
	 */
    @PostMapping(value = "/addcustomer",
					produces = MediaType.APPLICATION_JSON_VALUE,
					consumes = MediaType.APPLICATION_JSON_VALUE,
					headers = "Accept=application/json")
    public Customer add(@RequestBody Customer customer) {
        return repository.save(customer);
    }
    /**
     * To delete a <code>Customer</code> cache with it's identifier.
     * @param id identifier of a Customer
     * @return {@link Customer}
     */
    @ApiOperation(value = "Search Customer by customerId", produces = "application/json")
    @GetMapping(value = "/{id}",
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			headers = "Accept=application/json")
    public Customer findById(@ApiParam(name = "customerId",
            value = "The Id of the customer to be viewed",
            required = true)@PathVariable("id") String id) {
    	return repository.findById(id).get(); 
    }
    /**
     * To get a <code>Transaction</code> cache with it's identifier
     * @param id identifier of a Transaction 
     * @return {@link Transaction}
     */
    @ApiOperation(value = "Search transaction by transactionId", produces = "application/json")
    @GetMapping(value = "/{id}",
			produces = MediaType.APPLICATION_JSON_VALUE,
			headers = "Accept=application/json")
    public Transaction findByIdTrans(@ApiParam(name = "transactionId",
            value = "The Id of the transaction to be viewed",
            required = true)@PathVariable("id") String id) {
    	return transrepository.findById(id).get();

    }
    /**
     * To get a <code>List</code> of <code>Transaction</code> from <code>accountId</code>
     * @param accountId identifier of an account.
     * @return <code>List</code> - <code>Transaction</code>
     */
    @ApiOperation(value = "Search from Account by accountId", produces = "application/json")
    @GetMapping(value = "/from/{accountId}",
			produces = MediaType.APPLICATION_JSON_VALUE,
			headers = "Accept=application/json")
    public List<Transaction> findByFromAccountId(
    		@ApiParam(name = "studentId",
            value = "The Id from the account to be viewed",
            required = true)@PathVariable("accountId") String accountId) {
    	return transrepository.findByFromAccountId(accountId);
    }
    /**
     * To get a <code>List</code> of <code>Transaction</code> to <code>accountId</code>
     * @param accountId identifier of an account.
     * @return <code>List</code> - <code>Transaction</code>
     */
    @ApiOperation(value = "Search Customer by customerId", produces = "application/json")
    @GetMapping(value = "/to/{accountId}",produces = MediaType.APPLICATION_JSON_VALUE,headers = "Accept=application/json")
    public List<Transaction> findByToAccountId(@ApiParam(name = "transactionId",value = "The Id to the Transaction to be viewed",required = true)
    @PathVariable("accountId") String accountId) {
    	return transrepository.findByToAccountId(accountId);
    }
    /**
     * To insert all Customers with batch processing
     */
    @GetMapping(value = "/insertAllCust",
			produces = MediaType.APPLICATION_JSON_VALUE,
			headers = "Accept=application/json")
    public void insertAllCust() {
    	List<Account> acclist = new ArrayList<>();
    	acclist.add(new Account("1367", "54001", 54)); 
    	acclist.add( new Account("1368", "54000", 54));
    	List<Customer> custlist = new ArrayList<>();
    	custlist.add(new Customer("1", "34", "abc", acclist));
    	custlist.add(new Customer("2", "34", "abc", acclist));
		batchrepository.insertAll(custlist, "customer");
	}
    /**
     * To update All customers with batch processing
     */
    @GetMapping(value = "/updateAllCust",
			produces = MediaType.APPLICATION_JSON_VALUE,
			headers = "Accept=application/json")
    public void updateAllCust() { 
    	List<Account> acclist = new ArrayList<>();
    	acclist.add(new Account("1367", "54000", 54));
    	acclist.add( new Account("1368", "54000", 54));
    	List<Customer> custlist = new ArrayList<>();
    	custlist.add(new Customer("1", "34", "abc", acclist));
    	custlist.add(new Customer("2", "34", "abc", acclist));
    	batchrepository.updateAllCustomers(custlist, "sayantan");
	}
    /**
     * To update All accounts with batch processing
     */
    @GetMapping(value = "/updateAllAcc",
			produces = MediaType.APPLICATION_JSON_VALUE,
			headers = "Accept=application/json")
    public void updateAllAccount() { 
    	List<Account> acclist = new ArrayList<>();
    	acclist.add(new Account("1367", "54000", 54));
    	acclist.add( new Account("1368", "54000", 54));
    	batchrepository.updateAllAccounts(acclist, 58);
	}
    /**
     * To delete All customers with batch processing
     */
    @GetMapping(value = "/deleteAllCust",
			produces = MediaType.APPLICATION_JSON_VALUE,
			headers = "Accept=application/json")
    public void deleteAllCust() { 
    	List<Account> acclist = new ArrayList<>();
    	acclist.add(new Account("1367", "54000", 54));
    	acclist.add( new Account("1368", "54000", 54));
    	List<Customer> custlist = new ArrayList<>();
    	custlist.add(new Customer("1", "34", "abc", acclist));
    	custlist.add(new Customer("2", "34", "abc", acclist));
    	batchrepository.deleteAll(custlist, "sayantan");
	}
}
