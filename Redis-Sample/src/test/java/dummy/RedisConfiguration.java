package dummy;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.boot.test.context.TestConfiguration;

import dummy.config.RedisProperties;
import redis.embedded.RedisServer;

@TestConfiguration
public class RedisConfiguration {

	private RedisServer redisServer;
	
	public RedisConfiguration(RedisProperties properties) throws IOException {
		this.redisServer = new RedisServer(properties.getRedisPort());
	}
	
	@PostConstruct
	public void start() {
		 redisServer.start();
	}
	
	@PreDestroy
	public void stop() {
		 redisServer.stop();
	}
}
