package com.ecquaria.cloud.moh.iais.listener;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import lombok.extern.slf4j.Slf4j;

/**
 * IaisSessionListener
 *
 * @author Jinhua
 * @date 2020/4/3 15:31
 */
@WebListener
@Slf4j
public class IaisSessionListener implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        log.info("<======= Session created =======>");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        log.info("<======= Session destroyed =======>");
    }
}
