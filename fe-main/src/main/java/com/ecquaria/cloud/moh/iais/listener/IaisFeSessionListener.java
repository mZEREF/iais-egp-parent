package com.ecquaria.cloud.moh.iais.listener;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.RedisNameSpaceConstant;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.helper.RedisCacheHelper;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.client.AuditTrailMainClient;
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
 * IaisSessionListener
 *
 * @author Jinhua
 * @date 2020/4/3 15:31
 */
@Component
@Slf4j
public class IaisFeSessionListener {
    @Autowired
    private AuditTrailMainClient auditTrailMainClient;

    @Autowired
    private RedisCacheHelper redisHelper;

    @EventListener(SessionCreatedEvent.class)
    @Async
    public void sessionCreatedEvent(SessionCreatedEvent sessionEvent) {
        String sessionId = sessionEvent.getSessionId();
        log.info("Session Created Event : -> {}", sessionId);
        Date createdDt = redisHelper.get(RedisNameSpaceConstant.CACHE_NAME_ACTIVE_SESSION_SET, sessionId);
        int asCount = redisHelper.keyNumbers(RedisNameSpaceConstant.CACHE_NAME_ACTIVE_SESSION_SET);
        log.info("Active Session Count => {}", asCount);
        if (createdDt == null && asCount < 100) {
            redisHelper.set(RedisNameSpaceConstant.CACHE_NAME_ACTIVE_SESSION_SET, sessionId, new Date(), RedisCacheHelper.SESSION_DEFAULT_EXPIRE);
        }
    }

    @EventListener(SessionExpiredEvent.class)
    public void sessionExpiredEvent(SessionExpiredEvent sessionEvent) {
        String sessionId = sessionEvent.getSessionId();
        LoginContext loginContext = sessionEvent.getSession().getAttribute(AppConsts.SESSION_ATTR_LOGIN_USER);
        if (loginContext != null) {
            AuditTrailDto loginDto = auditTrailMainClient.getLoginInfoBySessionId(sessionId).getEntity();
            AuditTrailDto auditTrailDto = MiscUtil.transferEntityDto(loginDto, AuditTrailDto.class);
            IaisEGPHelper.setAuditLoginUserInfo(auditTrailDto);
            auditTrailDto.setOperationType(AuditTrailConsts.OPERATION_TYPE_INTERNET);
            auditTrailDto.setOperation(AuditTrailConsts.OPERATION_SESSION_TIMEOUT);
            AuditTrailHelper.callSaveAuditTrail(auditTrailDto);
            if (loginDto != null) {
                AuditTrailHelper.callSaveSessionDuration(sessionEvent.getSession().getId(), 30);
            }
        }
        Date createdDt = redisHelper.get(RedisNameSpaceConstant.CACHE_NAME_ACTIVE_SESSION_SET, sessionId);
        if (createdDt != null) {
            redisHelper.delete(RedisNameSpaceConstant.CACHE_NAME_ACTIVE_SESSION_SET, sessionId);
        }
    }

    @EventListener(SessionDeletedEvent.class)
    public void sessionDeletedEvent(SessionDeletedEvent sessionEvent) {
        String sessionId = sessionEvent.getSessionId();
        log.info("Session Delete Event : -> {}", sessionId);
        Date createdDt = redisHelper.get(RedisNameSpaceConstant.CACHE_NAME_ACTIVE_SESSION_SET, sessionId);
        if (createdDt != null) {
            redisHelper.delete(RedisNameSpaceConstant.CACHE_NAME_ACTIVE_SESSION_SET, sessionId);
        }
    }

}
