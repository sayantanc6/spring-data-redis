package dummy.repo;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import dummy.model.Transaction;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {

	List<Transaction> findByFromAccountId(Long fromAccountId);
    List<Transaction> findByToAccountId(Long toAccountId);
}
 