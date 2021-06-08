package dummy.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import dummy.model.Account;
import dummy.model.Customer;
import dummy.model.Transaction;
import dummy.repo.CustomerRepository;
import dummy.repo.RedisBatchRepository;
import dummy.repo.TransactionRepository;

@RestController
public class MyController {
	 
	@Autowired
	RedisBatchRepository batchrepository;

	@Autowired
    CustomerRepository repository;
	
	@Autowired
    TransactionRepository transrepository;

    @PostMapping(value = "/addcustomer",
					produces = MediaType.APPLICATION_JSON_VALUE,
					consumes = MediaType.APPLICATION_JSON_VALUE,
					headers = "Accept=application/json")
    public Customer add(@RequestBody Customer customer) {
        return repository.save(customer);
    }

    @GetMapping(value = "/{id}",
			produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			headers = "Accept=application/json")
    public Customer findById(@PathVariable("id") Long id) {
        Optional<Customer> optCustomer = repository.findById(id);
        return optCustomer.orElse(null);
    }
    
    @GetMapping(value = "/{id}",
			produces = MediaType.APPLICATION_JSON_VALUE,
			headers = "Accept=application/json")
    public Transaction findByIdTrans(Long id) {
        Optional<Transaction> optTransaction = transrepository.findById(id);
        return optTransaction.orElse(null);
    }

    @GetMapping(value = "/from/{accountId}",
			produces = MediaType.APPLICATION_JSON_VALUE,
			headers = "Accept=application/json")
    public List<Transaction> findByFromAccountId(@PathVariable("accountId") Long accountId) {
        return transrepository.findByFromAccountId(accountId);
    }

    @GetMapping(value = "/to/{accountId}",
			produces = MediaType.APPLICATION_JSON_VALUE,
			headers = "Accept=application/json")
    public List<Transaction> findByToAccountId(@PathVariable("accountId") Long accountId) {
        return transrepository.findByToAccountId(accountId);
    }
    
    @GetMapping(value = "/insertAllCust",
			produces = MediaType.APPLICATION_JSON_VALUE,
			headers = "Accept=application/json")
    public void insertAllCust() {
    	List<Account> acclist = new ArrayList<>();
    	acclist.add(new Account(1367, "54000", 54));
    	acclist.add( new Account(1368, "54000", 54));
    	List<Customer> custlist = new ArrayList<>();
    	custlist.add(new Customer(1, "34", "abc", acclist));
    	custlist.add(new Customer(2, "34", "abc", acclist));
		batchrepository.insertAll(custlist, "customer");
	}
    
    @GetMapping(value = "/updateAllCust",
			produces = MediaType.APPLICATION_JSON_VALUE,
			headers = "Accept=application/json")
    public void updateAllCust() { 
    	List<Account> acclist = new ArrayList<>();
    	acclist.add(new Account(1367, "54000", 54));
    	acclist.add( new Account(1368, "54000", 54));
    	List<Customer> custlist = new ArrayList<>();
    	custlist.add(new Customer(1, "34", "abc", acclist));
    	custlist.add(new Customer(2, "34", "abc", acclist));
    	batchrepository.updateAllCustomers(custlist, "sayantan");
	}
    
    @GetMapping(value = "/updateAllAcc",
			produces = MediaType.APPLICATION_JSON_VALUE,
			headers = "Accept=application/json")
    public void updateAllAccount() { 
    	List<Account> acclist = new ArrayList<>();
    	acclist.add(new Account(1367, "54000", 54));
    	acclist.add( new Account(1368, "54000", 54));
    	batchrepository.updateAllAccounts(acclist, 58);
	}
    
    @GetMapping(value = "/deleteAllCust",
			produces = MediaType.APPLICATION_JSON_VALUE,
			headers = "Accept=application/json")
    public void deleteAllCust() { 
    	List<Account> acclist = new ArrayList<>();
    	acclist.add(new Account(1367, "54000", 54));
    	acclist.add( new Account(1368, "54000", 54));
    	List<Customer> custlist = new ArrayList<>();
    	custlist.add(new Customer(1, "34", "abc", acclist));
    	custlist.add(new Customer(2, "34", "abc", acclist));
    	batchrepository.deleteAll(custlist, "sayantan");
	}
}
