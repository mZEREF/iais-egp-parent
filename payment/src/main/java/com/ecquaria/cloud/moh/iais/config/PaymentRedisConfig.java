package com.ecquaria.cloud.moh.iais.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * PaymentRedisConfig
 *
 * @author Jinhua
 * @date 2020/11/19 18:08
 */
@Configuration
@AutoConfigureAfter({RedisAutoConfiguration.class})
public class PaymentRedisConfig {
    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Bean(name = "paymentRedisTemplate")
    public RedisTemplate<String, Object> paymentRedisTemplate() {
        JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer(Thread.currentThread().getContextClassLoader());
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate();
        redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
        redisTemplate.setConnectionFactory(this.connectionFactory);
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }
}
