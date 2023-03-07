package com.ecquaria.cloud.moh.iais.listener;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.RedisNameSpaceConstant;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.helper.RedisCacheHelper;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.client.AuditTrailMainBeClient;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.session.events.SessionCreatedEvent;
import org.springframework.session.events.SessionDeletedEvent;
import org.springframework.session.events.SessionExpiredEvent;
import org.springframework.stereotype.Component;

/**
 * IaisBeSessionListener
 *
 * @author Jinhua
 * @date 2020/9/29 14:50
 */
@Component
@Slf4j
public class IaisBeSessionListener {
    @Autowired
    private AuditTrailMainBeClient auditTrailMainClient;

    @Autowired
    private RedisCacheHelper redisHelper;

    @EventListener(SessionCreatedEvent.class)
    @Async
    public void sessionCreatedEvent(SessionCreatedEvent sessionEvent) {
        String sessionId = sessionEvent.getSessionId();
        log.info("Session Created Event : -> {}", sessionId);
        Date createdDt = redisHelper.get(RedisNameSpaceConstant.CACHE_NAME_ACTIVE_SESSION_SET, sessionId);
        if (createdDt == null) {
            redisHelper.set(RedisNameSpaceConstant.CACHE_NAME_ACTIVE_SESSION_SET, sessionId, new Date(), RedisCacheHelper.SESSION_DEFAULT_EXPIRE);
        }
    }

    @EventListener(SessionExpiredEvent.class)
    public void sessionExpiredEvent(SessionExpiredEvent sessionEvent) {
        String sessionId = sessionEvent.getSessionId();
        LoginContext loginContext = sessionEvent.getSession().getAttribute(AppConsts.SESSION_ATTR_LOGIN_USER);
        if (loginContext != null) {
            AuditTrailDto loginDto = auditTrailMainClient.getLoginInfoBySessionId(sessionEvent.getSession().getId()).getEntity();
            if (loginDto != null) {
                loginDto.setOperationType(AuditTrailConsts.OPERATION_TYPE_INTRANET);
                AuditTrailHelper.callSaveSessionDuration(sessionEvent.getSession().getId(), 30);
            }
        }
        Date createdDt = redisHelper.get(RedisNameSpaceConstant.CACHE_NAME_ACTIVE_SESSION_SET, sessionId);
        if (createdDt == null) {
            redisHelper.delete(RedisNameSpaceConstant.CACHE_NAME_ACTIVE_SESSION_SET, sessionId);
        }
    }

    @EventListener(SessionDeletedEvent.class)
    public void sessionDeletedEvent(SessionDeletedEvent sessionEvent) {
        String sessionId = sessionEvent.getSessionId();
        log.info("Session Delete Event : -> {}", sessionId);
        Date createdDt = redisHelper.get(RedisNameSpaceConstant.CACHE_NAME_ACTIVE_SESSION_SET, sessionId);
        if (createdDt == null) {
            redisHelper.delete(RedisNameSpaceConstant.CACHE_NAME_ACTIVE_SESSION_SET, sessionId);
        }
    }
}
