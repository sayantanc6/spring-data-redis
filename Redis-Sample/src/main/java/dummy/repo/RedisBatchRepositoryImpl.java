package dummy.repo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.PartialUpdate;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisKeyValueTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import dummy.model.Account;
import dummy.model.Customer;
import lombok.SneakyThrows;

@Repository
public class RedisBatchRepositoryImpl<K, V> implements RedisBatchRepository {
	

	public RedisTemplate<K, V> template;
	
	@Autowired 
	public RedisKeyValueTemplate kvtemplate;
	
	List<V> itemslist = new ArrayList<>();
	
	byte[] data;
	
	Set<byte[]> keys;

	@SuppressWarnings("hiding")
	@Override
	public <V> void insertAll(List<V> items,String key) {
		template.executePipelined(new RedisCallback<Object>() {

			@Override
			@SneakyThrows(DataAccessException.class)
			public Object doInRedis(RedisConnection connection) {
			items.forEach(item -> {
				 try {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					ObjectOutputStream os = new ObjectOutputStream(out);
					os.writeObject(item);
					connection.lPush(key.getBytes(), out.toByteArray());
				 }catch (IOException e) {
				}
			});
				
				return null;
			}
	 });
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<V> readAll(String key) {
		template.executePipelined(new RedisCallback<Object>() {
			
			@Override
			@SneakyThrows(DataAccessException.class)
			public Object doInRedis(RedisConnection connection) {
				
				Set<byte[]> keys = template.getConnectionFactory().getConnection().keys(key.getBytes());
				
				Iterator<byte[]> it = keys.iterator(); 
				while(it.hasNext()){
				    try {
						byte[] data = (byte[])it.next();
						ByteArrayInputStream in = new ByteArrayInputStream(data);
						ObjectInputStream is = new ObjectInputStream(in);
						V object = (V) is.readObject();
						itemslist.add(object);
					} catch (ClassNotFoundException | IOException e) {
						e.printStackTrace();
					}
				}

				return itemslist;
			}
		});
		return itemslist;
	}

	@Override
	public void updateAllCustomers(List<Customer> customers, String name) {
		template.executePipelined(new RedisCallback<Object>() {

			@Override
			@SneakyThrows(DataAccessException.class)
			public Object doInRedis(RedisConnection connection) {
				customers.forEach(cust -> {
					kvtemplate.update(new PartialUpdate<>(cust.getId(), Customer.class)
							.set("name", name));
				}); 
				return null;
			}
		});
	}

	@Override
	public void updateAllAccounts(List<Account> accounts, int balance) {

		template.executePipelined(new RedisCallback<Object>() {

			@Override
			@SneakyThrows(DataAccessException.class)
			public Object doInRedis(RedisConnection connection) {
				accounts.forEach(cust -> {
					kvtemplate.update(new PartialUpdate<>(cust.getId(), Customer.class)
							.set("balance", balance));
				}); 
				return null;
			}
		});
	}

	@Override
	public <E> void deleteAll(List<E> items,String key) {
		template.executePipelined(new RedisCallback<Object>() {

			@Override
			@SneakyThrows(DataAccessException.class)
			public Object doInRedis(RedisConnection connection) {
				for (int i = 0; i < items.size(); i++) {
					connection.rPop(key.getBytes());
				}
				return null;
			}
		});
	}

}
