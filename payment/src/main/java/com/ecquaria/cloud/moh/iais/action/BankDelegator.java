package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

/**
 * @author weilu
 * date 2019/11/12 9:29
 */

@Delegator(value = "bank")
@Slf4j
public class BankDelegator {


    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>bank");
    }


    public void  deal (BaseProcessClass bpc) {

        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>bank");
        HttpServletRequest request = bpc.request;
        String statue = request.getParameter("statue");
        log.info(StringUtil.changeForLog("The flag value ----> "+statue));
    }
}
