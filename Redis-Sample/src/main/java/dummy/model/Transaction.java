package dummy.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@RedisHash("transaction")
@Component
@NoArgsConstructor
public class Transaction {

	@Id
    private Long id; 
    private int amount;
    private Date date;
    @Indexed
    private long fromAccountId;
    @Indexed
    private long toAccountId;
}
