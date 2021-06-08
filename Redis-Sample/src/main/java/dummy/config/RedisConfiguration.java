package dummy.config;

import java.lang.reflect.Type;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.http.MediaType;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;
import org.springframework.http.converter.xml.SourceHttpMessageConverter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.internal.bind.SqlDateTypeAdapter;

import dummy.model.RedisMessagePublisher;
import dummy.model.RedisMessageSubscriber;

@Configuration
@EnableRedisRepositories
@EnableTransactionManagement 
// @EnableScheduling
public class RedisConfiguration implements WebMvcConfigurer {
	
	@Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private Integer redisPort;
    
    @Value("${spring.redis.password}")
    private String redispass;
    
    @Bean
    public Gson gson() {
        GsonBuilder b = new GsonBuilder();
      //  b.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY);
        b.registerTypeAdapterFactory(DateTypeAdapter.FACTORY);
     //   b.registerTypeAdapterFactory(TimestampTypeAdapter.FACTORY);
        b.registerTypeAdapterFactory(SqlDateTypeAdapter.FACTORY);
     //   b.registerTypeAdapterFactory(LocalDateTimeTypeAdapter.FACTORY); 
        b.registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
            @Override
            public LocalDateTime deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                Instant instant = Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
                return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            }
        });
        b.registerTypeAdapter(LocalDate.class, new JsonDeserializer<LocalDate>() {
        	@Override
        		public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        	        return LocalDate.parse(json.getAsJsonPrimitive().getAsString());
        	}
        });
        b.registerTypeAdapter(LocalDate.class, new LocalDateAdapter());
        return b.create();
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        StringHttpMessageConverter stringConverter = new StringHttpMessageConverter();
        stringConverter.setWriteAcceptCharset(false);
        stringConverter.setSupportedMediaTypes(Collections
            .singletonList(MediaType.TEXT_PLAIN));
        converters.add(stringConverter);
        converters.add(new ByteArrayHttpMessageConverter());
        converters.add(new SourceHttpMessageConverter<>());
        GsonHttpMessageConverter gsonHttpMessageConverter = new GsonHttpMessageConverter();
        gsonHttpMessageConverter.setGson(gson());
        gsonHttpMessageConverter.setSupportedMediaTypes(Arrays
            .asList(MediaType.APPLICATION_JSON));
        converters.add(gsonHttpMessageConverter);
    }

	@SuppressWarnings("deprecation")
	@Bean
    public LettuceConnectionFactory redisConnectionFactory() {
		LettuceConnectionFactory factory = new LettuceConnectionFactory();
		factory.setHostName(redisHost);
		factory.setPort(redisPort);
		factory.setPassword(redispass);
        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setValueSerializer(new GenericToStringSerializer<Object>(Object.class));
        template.setEnableTransactionSupport(true);
        return template;
    }
    
    @Bean
    MessageListenerAdapter messageListener() {
        return new MessageListenerAdapter(new RedisMessageSubscriber());
    }

    @Bean
    RedisMessageListenerContainer redisContainer() {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(messageListener(), topic());
        return container;
    }

    @Bean
    RedisMessagePublisher redisPublisher() {
        return new RedisMessagePublisher(redisTemplate(), topic());
    }
    
    @Bean
    ChannelTopic topic() {
        return new ChannelTopic("pubsub:queue");
    }
}
