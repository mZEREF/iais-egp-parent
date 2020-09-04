package com.ecquaria.cloud.moh.iais.listener;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import lombok.extern.slf4j.Slf4j;
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
public class IaisSessionListener {
    @EventListener(SessionCreatedEvent.class)
    @Async
    public void sessionCreatedEvent(SessionCreatedEvent sessionEvent) {
        LoginContext loginContext = sessionEvent.getSession().getAttribute(AppConsts.SESSION_ATTR_LOGIN_USER);
        log.info(StringUtil.changeForLog("<==== Session created ====> "
                + (loginContext == null ? "N.A." : loginContext.getLoginId())));
    }

    @EventListener(SessionExpiredEvent.class)
    public void sessionExpiredEvent(SessionExpiredEvent sessionEvent) {
        LoginContext loginContext = sessionEvent.getSession().getAttribute(AppConsts.SESSION_ATTR_LOGIN_USER);
        log.info(StringUtil.changeForLog("<==== Session timeout ====> "
                + (loginContext == null ? "N.A." : loginContext.getLoginId())));
    }

    @EventListener(SessionDeletedEvent.class)
    public void sessionDeletedEvent(SessionDeletedEvent sessionEvent) {

    }

}
