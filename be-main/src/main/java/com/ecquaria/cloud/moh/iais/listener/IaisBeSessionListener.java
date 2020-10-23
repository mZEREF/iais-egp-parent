package com.ecquaria.cloud.moh.iais.listener;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.service.client.AuditTrailMainBeClient;
import com.ecquaria.sz.commons.util.Calculator;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.session.events.SessionCreatedEvent;
import org.springframework.session.events.SessionDeletedEvent;
import org.springframework.session.events.SessionExpiredEvent;

/**
 * IaisBeSessionListener
 *
 * @author Jinhua
 * @date 2020/9/29 14:50
 */
public class IaisBeSessionListener {
    @Autowired
    private AuditTrailMainBeClient auditTrailMainClient;

    @EventListener(SessionCreatedEvent.class)
    @Async
    public void sessionCreatedEvent(SessionCreatedEvent sessionEvent) {
    }

    @EventListener(SessionExpiredEvent.class)
    public void sessionExpiredEvent(SessionExpiredEvent sessionEvent) {
        LoginContext loginContext = sessionEvent.getSession().getAttribute(AppConsts.SESSION_ATTR_LOGIN_USER);
        if (loginContext != null) {
            AuditTrailDto loginDto = auditTrailMainClient.getLoginInfoBySessionId(sessionEvent.getSession().getId()).getEntity();
            Date now = new Date();
            if (loginDto != null) {
                Date before = Formatter.parseDateTime(loginDto.getActionTime());
                long duration = now.getTime() - before.getTime();
                int minutes = (int) Calculator.div(duration, 60000, 0);
                auditTrailMainClient.updateSessionDuration(sessionEvent.getSession().getId(), 30);
            }
        }
    }

    @EventListener(SessionDeletedEvent.class)
    public void sessionDeletedEvent(SessionDeletedEvent sessionEvent) {

    }
}
