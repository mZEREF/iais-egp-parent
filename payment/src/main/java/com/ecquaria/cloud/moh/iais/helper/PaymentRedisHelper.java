package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.usersession.UserSession;
import com.ecquaria.cloud.usersession.UserSessionUtil;
import com.ecquaria.cloud.usersession.client.UserSessionService;
import com.ecquaria.cloudfeign.FeignException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ecq.commons.exception.BaseException;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import sop.iwe.SessionManager;
import sop.rbac.user.User;

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

    public void copySessionAttr(String oldSessionId, HttpServletRequest request) throws FeignException, BaseException {
        HttpSession session = request.getSession();
        this.redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
        this.redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        Map<Object, Object> map = this.redisTemplate.opsForHash().entries("spring:session:sessions:" + oldSessionId);
        if (map != null && session != null) {
            for (Map.Entry<Object, Object> ent : map.entrySet()) {
                String key = (String) ent.getKey();
                if (key.startsWith("sessionAttr:")) {
                    String sessionKey = key.replaceFirst("sessionAttr:", "");
                    log.debug(StringUtil.changeForLog("Session Key => " + sessionKey));
                    ObjectMapper mapper = new ObjectMapper();
                    String jsonString = null;
                    try {
                        jsonString = mapper.writeValueAsString(ent.getValue());
                    } catch (JsonProcessingException e) {
                        log.debug(e.getMessage(),e);
                    }
                    log.debug(StringUtil.changeForLog(sessionKey+" => " + jsonString));
                    session.setAttribute(sessionKey, ent.getValue());//NOSONAR
                }
            }
            log.debug("Old session Id ==> " + oldSessionId);
            log.debug("New session id ==> " + session.getId());
            User user = SessionManager.getInstance(request).getCurrentUser();
            List<UserSession> usesses = UserSessionService.getInstance()
                    .retrieveActiveSessionByUserDomainAndUserId(user.getUserDomain(), user.getId());
            if (usesses != null && usesses.size() > 0) {
                for (UserSession us : usesses) {
                    UserSessionUtil.killUserSession(us.getSessionId());
                }
            }
            UserSessionService.getInstance().registerSession(request, session);
        }
    }
}
