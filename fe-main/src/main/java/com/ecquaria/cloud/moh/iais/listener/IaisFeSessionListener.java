package com.ecquaria.cloud.moh.iais.listener;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.client.AuditTrailMainClient;
import com.ecquaria.cloud.submission.client.wrapper.SubmissionClient;
import com.ecquaria.sz.commons.util.Calculator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.session.events.SessionCreatedEvent;
import org.springframework.session.events.SessionDeletedEvent;
import org.springframework.session.events.SessionExpiredEvent;
import org.springframework.stereotype.Component;

import java.util.Date;

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
    private SubmissionClient submissionClient;
    @Autowired
    private AuditTrailMainClient auditTrailMainClient;

    @EventListener(SessionCreatedEvent.class)
    @Async
    public void sessionCreatedEvent(SessionCreatedEvent sessionEvent) {
    }

    @EventListener(SessionExpiredEvent.class)
    public void sessionExpiredEvent(SessionExpiredEvent sessionEvent) {
        LoginContext loginContext = sessionEvent.getSession().getAttribute(AppConsts.SESSION_ATTR_LOGIN_USER);
        if (loginContext != null) {
            AuditTrailDto auditTrailDto = new AuditTrailDto();
            IaisEGPHelper.setAuditLoginUserInfo(auditTrailDto);
            auditTrailDto.setOperation(AuditTrailConsts.OPERATION_SESSION_TIMEOUT);
            IaisEGPHelper.callSaveAuditTrail(auditTrailDto);
            AuditTrailDto loginDto = auditTrailMainClient.getLoginInfoBySessionId(sessionEvent.getSession().getId()).getEntity();
            Date now = new Date();
            if (loginDto != null) {
                Date before = Formatter.parseDateTime(loginDto.getActionTime());
                long duration = now.getTime() - before.getTime();
                int minutes = (int) Calculator.div(duration, 60000, 0);
                auditTrailDto.setTotalSessionDuration(minutes);
                auditTrailMainClient.updateSessionDuration(sessionEvent.getSession().getId(), minutes);
            }
        }
    }

    @EventListener(SessionDeletedEvent.class)
    public void sessionDeletedEvent(SessionDeletedEvent sessionEvent) {

    }

}
