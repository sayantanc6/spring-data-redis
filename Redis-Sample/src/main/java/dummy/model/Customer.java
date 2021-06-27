package dummy.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.stereotype.Component;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Details of a Customer
 * @author Sayantan
 *
 */
@Data
@AllArgsConstructor
@RedisHash("customer")
@Component
@NoArgsConstructor
public class Customer implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * id of a customer
	 */
	@SerializedName("id")
	@Id private String id;
	
	/**
	 * external id of a customer
	 */
	@SerializedName("externalId")
    @Indexed private String externalId;
	/**
	 * name of a cutomer
	 */
	@SerializedName("name")
    private String name;
	/**
	 * <code>List</code> of Accounts in a Customer
	 */
	@SerializedName("accounts")
    private List<Account> accounts = new ArrayList<>();
    
    public void addAccount(Account account) {
        this.accounts.add(account);
    }
}
