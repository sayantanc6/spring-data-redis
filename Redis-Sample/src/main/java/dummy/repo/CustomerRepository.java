package dummy.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dummy.model.Account;
import dummy.model.Customer;

/**
 * 
 * @author Sayantan
 *
 */
@Repository
public interface CustomerRepository extends CrudRepository<Customer,String> {
 
	Customer findByExternalId(String externalId);
    List<Account> findByAccountsId(String id); 
}
