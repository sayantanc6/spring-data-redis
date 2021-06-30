package dummy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 
 * @author Sayantan
 *http://localhost:8080/swagger-ui/index.html?configUrl=/api-docs/swagger-config
 */
@SpringBootApplication 
public class RedisSampleApplication { 

	public static void main(String[] args) {
		SpringApplication.run(RedisSampleApplication.class, args);
	}

}
