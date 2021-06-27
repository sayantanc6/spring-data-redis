package dummy.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dummy.model.Transaction;

/**
 * 
 * @author Sayantan
 *
 */
@Repository
public interface TransactionRepository extends CrudRepository<Transaction, String> {

	List<Transaction> findByFromAccountId(String fromAccountId);
    List<Transaction> findByToAccountId(String toAccountId);
}
