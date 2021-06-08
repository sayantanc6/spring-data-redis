package dummy.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dummy.model.Account;
import dummy.model.Customer;

@Repository
public interface CustomerRepository extends CrudRepository<Customer,Long> {
 
	Customer findByExternalId(String externalId);
    List<Account> findByAccountsId(Long id); 
}
