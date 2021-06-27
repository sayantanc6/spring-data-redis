package dummy.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.stereotype.Component;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @author Sayantan
 *
 */
@Data
@AllArgsConstructor
@RedisHash("transaction")
@Component
@NoArgsConstructor
public class Transaction {

	@Id
	@SerializedName("id")
    private String id; 
	
	@SerializedName("amount")
    private int amount;
	
	@SerializedName("date")
    private Date date;
	
    @Indexed
    @SerializedName("fromAccountId")
    private long fromAccountId;
    
    @Indexed
    @SerializedName("toAccountId")
    private long toAccountId;
}
