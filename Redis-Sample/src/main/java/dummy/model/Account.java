package dummy.model;

import java.io.Serializable;

import org.springframework.data.redis.core.index.Indexed;
import org.springframework.stereotype.Component;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor 
@Component
@NoArgsConstructor
public class Account implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Indexed
	@SerializedName("id")
	private String id;
	
	@SerializedName("number")
    private String number;
	
	@SerializedName("balance")
    private int balance;
}
