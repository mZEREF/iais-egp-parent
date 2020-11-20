package com.ecquaria.cloud.moh.iais.helper;

import java.util.Map;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * PaymentRedisHelper
 *
 * @author Jinhua
 * @date 2020/11/19 18:11
 */
@Component
@Slf4j
public class PaymentRedisHelper {
    @Autowired
    @Qualifier("paymentRedisTemplate")
    private RedisTemplate<String, Object> redisTemplate;

    public void copySessionAttr(String oldSessionId, HttpSession session) {
        this.redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        this.redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        Map<Object, Object> map = this.redisTemplate.opsForHash().entries("spring:session:sessions:" + oldSessionId);
        if (map != null && session != null) {
            for (Map.Entry<Object, Object> ent : map.entrySet()) {
                String key = (String) ent.getKey();
                if (key.startsWith("sessionAttr:")) {
                    String sessionKey = key.replaceFirst("sessionAttr:", "");
                    log.debug("Session Key => " + sessionKey);
                    session.setAttribute(sessionKey, ent.getValue());
                }
            }
        }
    }
}
