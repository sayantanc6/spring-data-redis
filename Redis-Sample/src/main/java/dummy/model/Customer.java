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

@Data
@AllArgsConstructor
@RedisHash("customer")
@Component
@NoArgsConstructor
public class Customer implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@SerializedName("id")
	@Id private long id;
	
	@SerializedName("externalId")
    @Indexed private String externalId;
	
	@SerializedName("name")
    private String name;
	
	@SerializedName("accounts")
    private List<Account> accounts = new ArrayList<>();
    
    public void addAccount(Account account) {
        this.accounts.add(account);
    }
}
