package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2020-01-16 19:51
 **/
@Delegator(value = "cleanCalendarDelegator")
@Slf4j
public class cleanCalendarDelegator {

    public void doStart(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

    }
}
