package com.Minh.DrIO.config;

import com.Minh.DrIO.websocket.StrokeMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.regex.Pattern;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
// Cấu hình redis trong spring
@Configuration
public class RedisConfig {
/* tạo resdistemplate giao tiếp với redis với tác vụ update, fetch, delete với
key: String
value: Object
*/  @Bean
public RedisTemplate<String, StrokeMessage> redisTemplate(RedisConnectionFactory connectionFactory) {
    RedisTemplate<String, StrokeMessage> template = new RedisTemplate<>();
    template.setConnectionFactory(connectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());
    Jackson2JsonRedisSerializer<StrokeMessage> serializer = new Jackson2JsonRedisSerializer<>(StrokeMessage.class);
    template.setValueSerializer(serializer);
    template.setHashValueSerializer(serializer);
    template.afterPropertiesSet();
    return template;
};
    @Bean
    public RedisTemplate<String, Object> generalRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());

        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);

//        // Tạo ObjectMapper và bật default typing
//        ObjectMapper objectMapper = new ObjectMapper();
//        objectMapper.activateDefaultTyping(
//                objectMapper.getPolymorphicTypeValidator(),
//                ObjectMapper.DefaultTyping.NON_FINAL  
//        );
//        serializer.setObjectMapper(objectMapper);

        template.setValueSerializer(serializer);
        template.setHashValueSerializer(serializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean
        public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {
            // tạo một instance mới của listener
            RedisMessageListenerContainer container = new RedisMessageListenerContainer();
            //thiết lập connection
            container.setConnectionFactory(redisConnectionFactory);
            // thêm một đối tượng xử lí realtime từ các channel với mẫu room:*:updates
            //ví dụ room 123 updates
            container.addMessageListener(new RedisMessageSubscriber(), new PatternTopic("room:*:updates"));
            return container;
        }
}
