package dummy.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;

@Component
public class RedisMessagePublisher implements MessagePublisher{
	
	@Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired 
    private ChannelTopic topic;
    
    
    
    public RedisMessagePublisher() {
	}

	public RedisMessagePublisher(final RedisTemplate<String, Object> redisTemplate,  final ChannelTopic topic) {
        this.redisTemplate = redisTemplate;
        this.topic = topic;
    }

	@Override
	public void publish(final String message) {
		redisTemplate.convertAndSend(topic.getTopic(), "mytest");
	}
	
	@Scheduled(fixedDelay = 1000)
	public void publish() {
		redisTemplate.convertAndSend(topic.getTopic(), "mytest");
	}

}
