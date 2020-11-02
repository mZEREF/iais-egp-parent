package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.Calendar;
import java.util.Date;

/**
 * HcsaApplicationDelegator
 *
 * @author zhilin
 * @date 10/22/2020
 */
@Delegator("queryHelperDelegator")
@Slf4j
public class QueryHelperDelegator {
    private final String MIMA = "WOWAFKLKJHGF";
    public void start(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("queryHelperDelegator do cleanSession start ...."));
    }

    public void prepareLogin(BaseProcessClass bpc){

    }

    public void doLogin(BaseProcessClass bpc){
        String queryHelperPassword = ParamUtil.getString(bpc.request, "queryHelperPassword");
        String flag = "N";
        if(getCurrentPassword().equals(queryHelperPassword)){
            flag = "Y";
        }
        ParamUtil.setRequestAttr(bpc.request, "crud_action_type", flag);
    }

    public void prepareQuery(BaseProcessClass bpc){

    }

    private String getCurrentPassword(){
        Calendar calendar = Calendar.getInstance();//日历对象
        calendar.setTime(new Date());
        int day = calendar.get(Calendar.DATE);
        String dayStr = day < 10 ? "0" + day : day + "";
        String currentPassword = MIMA + dayStr;
        return currentPassword;
    }
}
