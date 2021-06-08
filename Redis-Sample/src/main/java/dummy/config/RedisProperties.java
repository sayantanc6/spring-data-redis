package dummy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Configuration
public class RedisProperties {

	@Value("${spring.redis.port}")
	private  int redisPort;
	
	@Value("${spring.redis.host}")
	private String redisHost;

	public RedisProperties(int redisPort, String redisHost) {
		super();
		this.redisPort = redisPort;
		this.redisHost = redisHost;
	}

	public RedisProperties() {
		super();
	}
}
