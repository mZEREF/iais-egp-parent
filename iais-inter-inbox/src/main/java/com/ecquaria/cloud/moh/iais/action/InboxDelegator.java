package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppInboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.InboxConst;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.InboxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-11-06 09:46
 **/

@Delegator("inboxDelegator")
@Slf4j
public class InboxDelegator {


    @Autowired
    private InboxService inboxService;


    @Autowired
    private InboxDelegator(InboxService inboxService){
        this.inboxService = inboxService;
    }

    public void startStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the doStart start ...."));
    }

    public void prepareStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the prepareStep start ...."));
        HttpServletRequest request = bpc.request;
        prepareSelectOption(bpc);
        FilterParameter inboxParameter = new FilterParameter();
        inboxParameter.setClz(InboxQueryDto.class);
        inboxParameter.setSearchAttr(InboxConst.INBOX_PARAM);
        inboxParameter.setResultAttr(InboxConst.INBOX_RESULT);
        inboxParameter.setSortField("id");
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, inboxParameter);
        String inboxType = ParamUtil.getString(request,"inboxType");
        System.err.println("inboxType ---> " + inboxType);
        QueryHelp.setMainSql("interInboxQuery","inboxQuery",searchParam);
        SearchResult searchResult = inboxService.inboxDoQuery(searchParam);

        if(!StringUtil.isEmpty(searchResult)){
            ParamUtil.setSessionAttr(request,InboxConst.INBOX_PARAM, searchParam);
            ParamUtil.setRequestAttr(request,InboxConst.INBOX_RESULT, searchResult);
        }


        List<AppInboxQueryDto> appInboxQueryDtoList = inboxService.applicationDoQuery();
        if(!StringUtil.isEmpty(appInboxQueryDtoList)){
            ParamUtil.setRequestAttr(request,InboxConst.APP_RESULT, appInboxQueryDtoList);
        }
    }

    public void searchInbox(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the searchInboxStep start ...."));
        HttpServletRequest request = bpc.request;
        String switchAction = ParamUtil.getString(request, InboxConst.SWITCH_ACTION);
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(request,InboxConst.INBOX_PARAM);
        if(switchAction.equals(InboxConst.SEARCH_INBOX)){
            String inboxType = ParamUtil.getString(request,"inboxType");
            String inboxService = ParamUtil.getString(request,"inboxService");
            if(inboxType != null){
                searchParam.addFilter("message_type",inboxType);
            }
            if(inboxService != null){
                searchParam.addFilter("service",inboxType);
            }
        }
        QueryHelp.setMainSql("interInboxQuery","inboxQuery",searchParam);
        SearchResult searchResult = inboxService.inboxDoQuery(searchParam);

        if(!StringUtil.isEmpty(searchResult)){
            ParamUtil.setSessionAttr(request,InboxConst.INBOX_PARAM, searchParam);
            ParamUtil.setRequestAttr(request,InboxConst.INBOX_RESULT, searchResult);
        }

    }


    public void switchStep(BaseProcessClass bpc){
        log.debug("The prepareSwitch start ...");
        String switchAction = ParamUtil.getString(bpc.request, InboxConst.SWITCH_ACTION);
        log.debug("*******************crudAction-->:" + switchAction);
        log.debug("The prepareSwitch end ...");
    }
    private void prepareSelectOption(BaseProcessClass bpc){
        List<SelectOption> inboxServiceSelectList = new ArrayList<>();
        inboxServiceSelectList.add(new SelectOption("1", "All"));
        inboxServiceSelectList.add(new SelectOption("2", "First"));
        inboxServiceSelectList.add(new SelectOption("3", "Second"));
        ParamUtil.setRequestAttr(bpc.request, "inboxServiceSelect", inboxServiceSelectList);

        List<SelectOption> inboxTypSelectList = new ArrayList<>();
        inboxTypSelectList.add(new SelectOption("1", "All"));
        inboxTypSelectList.add(new SelectOption("Notification", "Notification"));
        inboxTypSelectList.add(new SelectOption("Announcement", "Announcement"));
        inboxTypSelectList.add(new SelectOption("Query", "Query"));
        ParamUtil.setRequestAttr(bpc.request, "inboxTypeSelect", inboxTypSelectList);

        List<SelectOption> applicationTypeSelectList = new ArrayList<>();
        applicationTypeSelectList.add(new SelectOption("1", "All"));
        applicationTypeSelectList.add(new SelectOption("Renewal", "Renewal"));
        applicationTypeSelectList.add(new SelectOption("New Licence", "New Licence"));
        applicationTypeSelectList.add(new SelectOption("Group", "Group"));
        ParamUtil.setRequestAttr(bpc.request, "applicationType", applicationTypeSelectList);


        List<SelectOption> applicationStatusSelectList = new ArrayList<>();
        applicationStatusSelectList.add(new SelectOption("1", "All"));
        applicationStatusSelectList.add(new SelectOption("Approved", "Approved"));
        applicationStatusSelectList.add(new SelectOption("Pending", "Pending"));
        applicationStatusSelectList.add(new SelectOption("Draft", "Draft"));
        ParamUtil.setRequestAttr(bpc.request, "applicationStatus", applicationStatusSelectList);

        List<SelectOption> selectApplicationSelectList = new ArrayList<>();
        selectApplicationSelectList.add(new SelectOption("1", "Option 1"));
        selectApplicationSelectList.add(new SelectOption("2", "Option 2"));
        selectApplicationSelectList.add(new SelectOption("3", "Option 3"));
        selectApplicationSelectList.add(new SelectOption("4", "Option 4"));
        ParamUtil.setRequestAttr(bpc.request, "selectApplication", selectApplicationSelectList);

    }
}
