package dummy.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dummy.model.Account;
import dummy.model.Customer;
import dummy.model.Transaction;
import dummy.repo.CustomerRepository;
import dummy.repo.RedisBatchRepository;
import dummy.repo.TransactionRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@OpenAPIDefinition(servers = { @Server(url = "http://localhost:8080") }, info = @Info(title = "Sample Spring Boot API", version = "v1", description = "A demo project using Spring Boot with Swagger-UI enabled", license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html"), contact = @Contact(url = "https://www.linkedin.com/in/bchen04/", name = "Sayantan Chatterjee")))
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
    @Operation(summary = "Returns a greeting message")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(type = "object"))) })
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
    @Operation(summary = "Returns a greeting message")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(type = "object"))) })
    @GetMapping(value = "customer",
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			headers = "Accept=application/json")
    public Customer findById(@RequestParam("custid") String id) {
    	return repository.findById(id).get(); 
    }
    /**
     * To get a <code>Transaction</code> cache with it's identifier
     * @param id identifier of a Transaction 
     * @return {@link Transaction}
     */
    @ApiOperation(value = "Search transaction by transactionId", produces = "application/json")
    @GetMapping(value = "transaction",
			produces = MediaType.APPLICATION_JSON_VALUE,
			headers = "Accept=application/json")
    public Transaction findByIdTrans(@RequestParam("transid") String id) {
    	return transrepository.findById(id).get();

    }
    /**
     * To get a <code>List</code> of <code>Transaction</code> from <code>accountId</code>
     * @param accountId identifier of an account.
     * @return <code>List</code> - <code>Transaction</code>
     */
    @Operation(summary = "Returns a greeting message")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(type = "object"))) })
       @GetMapping(value = "/fromaccount",
			produces = MediaType.APPLICATION_JSON_VALUE,
			headers = "Accept=application/json")
    public List<Transaction> findByFromAccountId(@RequestParam("fromaccountId") String accountId) {
    	return transrepository.findByFromAccountId(accountId);
    }
    /**
     * To get a <code>List</code> of <code>Transaction</code> to <code>accountId</code>
     * @param accountId identifier of an account.
     * @return <code>List</code> - <code>Transaction</code>
     */
    @Operation(summary = "Returns a greeting message")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(type = "object"))) })
       @GetMapping(value = "/toaccount",produces = MediaType.APPLICATION_JSON_VALUE,headers = "Accept=application/json")
    public List<Transaction> findByToAccountId(@RequestParam("toaccountId") String accountId) {
    	return transrepository.findByToAccountId(accountId);
    }
    /**
     * To insert all Customers with batch processing
     */
    @Operation(summary = "Returns a greeting message")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(type = "object"))) })
    @PostMapping(value = "/insertAllCust",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			headers = "Accept=application/json")
    public void insertAllCust(@RequestBody List<Customer> custList) {
		
		  batchrepository.insertAll(custList, "customer");
	}
    /**
     * To update All customers with batch processing
     */
    @Operation(summary = "Returns a greeting message")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(type = "object"))) })
   @PostMapping(value = "/updateAllCust",
			consumes  = MediaType.APPLICATION_JSON_VALUE,
			headers = "Accept=application/json")
    public void updateAllCust(@RequestBody List<Customer> custList) { 
		  batchrepository.updateAllCustomers(custList, "sayantan");
	}
    /**
     * To update All accounts with batch processing
     */
    @Operation(summary = "Returns a greeting message")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(type = "object"))) })
    @PostMapping(value = "/updateAllAcc",
			consumes  = MediaType.APPLICATION_JSON_VALUE,
			headers = "Accept=application/json")
    public void updateAllAccount(@RequestBody List<Account> accList) { 

		  batchrepository.updateAllAccounts(accList, 58);
	}
    /**
     * To delete All customers with batch processing
     */
    @Operation(summary = "Returns a greeting message")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "successful operation", content = @Content(schema = @Schema(type = "object"))) })
    @GetMapping(value = "/deleteAllCust",
			consumes = MediaType.APPLICATION_JSON_VALUE,
			headers = "Accept=application/json")
    public void deleteAllCust(@RequestBody List<Customer> custList) { 
		  batchrepository.deleteAll(custList, "sayantan");
	}
}
