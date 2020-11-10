package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.QueryHelperResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.QueryHandlerFeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * HcsaApplicationDelegator
 *
 * @author zhilin
 * @date 10/22/2020
 */
@Delegator("queryHelperFeDelegator")
@Slf4j
public class QueryHelperFeDelegator {

    @Autowired
    private QueryHandlerFeService queryHandlerService;

    private final String MIMA = "WOWAFKLKJHGF";
    public void start(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("queryHelperDelegator do cleanSession start ...."));
        ParamUtil.setSessionAttr(bpc.request,"queryResult","E");
    }

    public void prepareLogin(BaseProcessClass bpc){

    }

    public void doQuery(BaseProcessClass bpc){
        String querySql = ParamUtil.getString(bpc.request,"querySql");
        String moduleNameDropdown = ParamUtil.getString(bpc.request,"moduleNameDropdown");
        log.info(StringUtil.changeForLog("------querySql : " + querySql));
        log.info(StringUtil.changeForLog("------moduleNameDropdown : " + moduleNameDropdown));
        if(!StringUtil.isEmpty(querySql)){
            ParamUtil.setRequestAttr(bpc.request,"querySql",querySql);
        }
        if(!StringUtil.isEmpty(moduleNameDropdown)){
            ParamUtil.setRequestAttr(bpc.request,"moduleNameDropdownValue",moduleNameDropdown);
        }
        QueryHelperResultDto queryHelperResultDto = queryHandlerService.getQueryHelperResultDtoList(querySql, moduleNameDropdown);
        if(queryHelperResultDto != null){
            ParamUtil.setRequestAttr(bpc.request,"queryResult","Y");
            log.info(StringUtil.changeForLog("------queryResult size(): " + queryHelperResultDto.getSearchResult().size()));
        }else{
            ParamUtil.setRequestAttr(bpc.request,"queryResult","N");
            log.info(StringUtil.changeForLog("------queryResult : null"));
        }
        ParamUtil.setRequestAttr(bpc.request,"QueryHelperResultDto",queryHelperResultDto);
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
        List<SelectOption> moduleNameList = IaisCommonUtils.genNewArrayList();
        moduleNameList.add(new SelectOption("inter-inbox","email-sms"));
        moduleNameList.add(new SelectOption("event-bus","event-bus"));
        moduleNameList.add(new SelectOption("hsca-application-fe","hsca-application-fe"));
        moduleNameList.add(new SelectOption("audit-trail","audit-trail"));
        moduleNameList.add(new SelectOption("hcsa-licence-fe","hcsa-licence-fe"));
        moduleNameList.add(new SelectOption("organization-fe","organization-fe"));
        moduleNameList.add(new SelectOption("hcsa-config","hcsa-config"));
        moduleNameList.add(new SelectOption("system-admin","system-admin"));
        moduleNameList.add(new SelectOption("payment","payment"));
        ParamUtil.setSessionAttr(bpc.request, "moduleNameDropdown", (Serializable)moduleNameList);
    }

    private String getCurrentPassword(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        int day = calendar.get(Calendar.DATE);
        String dayStr = day < 10 ? "0" + day : day + "";
        String currentPassword = MIMA + dayStr;
        return currentPassword;
    }
}
