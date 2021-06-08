package dummy.repo;

import java.io.IOException;
import java.util.List;

import dummy.model.Account;
import dummy.model.Customer;

public interface RedisBatchRepository {

	 <E> void insertAll(List<E> items,String key);
	  <V> List<V> readAll(String key) throws IOException, ClassNotFoundException; 
	 void updateAllCustomers(List<Customer> customers, String name);
	 void updateAllAccounts(List<Account> accounts,int balance);
	 <E> void deleteAll(List<E> items,String key);
} 
