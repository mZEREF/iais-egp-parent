package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    FilterParameter inboxParameter = new FilterParameter();
    FilterParameter appParameter = new FilterParameter();
    FilterParameter LicenceParameter = new FilterParameter();

    /**
     *
     * @param bpc
     * @Decription Step ---> Start
     */
    public void startStep(BaseProcessClass bpc){

        log.debug(StringUtil.changeForLog("Step ---> Start"));
    }

    /**
     *
     * @param bpc
     * @Description Step ---> PrepareDate
     */
    public void prepareStep(BaseProcessClass bpc){

        log.debug(StringUtil.changeForLog("Step ---> PrepareDate"));
        HttpServletRequest request = bpc.request;
        prepareSelectOption(bpc);
        inboxParameter.setClz(InboxQueryDto.class);
        inboxParameter.setSearchAttr(InboxConst.INBOX_PARAM);
        inboxParameter.setResultAttr(InboxConst.INBOX_RESULT);
        inboxParameter.setSortField("id");
        SearchParam inboxParam = IaisEGPHelper.getSearchParam(request, true,inboxParameter);

        QueryHelp.setMainSql("interInboxQuery","inboxQuery",inboxParam);
        SearchResult inboxResult = inboxService.inboxDoQuery(inboxParam);

        if(!StringUtil.isEmpty(inboxResult)){
            ParamUtil.setSessionAttr(request,InboxConst.INBOX_PARAM, inboxParam);
            ParamUtil.setRequestAttr(request,InboxConst.INBOX_RESULT, inboxResult);
        }

        /**
         * Application SearchResult
         */

        appParameter.setClz(InboxAppQueryDto.class);
        appParameter.setSearchAttr(InboxConst.APP_PARAM);
        appParameter.setResultAttr(InboxConst.APP_RESULT);
        appParameter.setSortField("id");
        SearchParam appParam = IaisEGPHelper.getSearchParam(request, true,appParameter);

        QueryHelp.setMainSql("interInboxQuery","applicationQuery",appParam);
        SearchResult appResult = inboxService.appDoQuery(appParam);

        if(!StringUtil.isEmpty(inboxResult)){
            ParamUtil.setSessionAttr(request,InboxConst.APP_PARAM, appParam);
            ParamUtil.setRequestAttr(request,InboxConst.APP_RESULT, appResult);
        }

//        List<AppInboxQueryDto> appInboxQueryDtoList = inboxService.applicationDoQuery();
//        if(!StringUtil.isEmpty(appInboxQueryDtoList)){
//            ParamUtil.setRequestAttr(request,InboxConst.APP_RESULT, appInboxQueryDtoList);
//        }
    }

    /**
     *
     * @param bpc
     * @Description Step ---> DoSearch
     */
    public void doSearch(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> DoSearch"));
        HttpServletRequest request = bpc.request;
        String switchAction = ParamUtil.getString(request, InboxConst.SWITCH_ACTION);
        if(switchAction.equals(InboxConst.SEARCH_INBOX)){
            String inboxType = ParamUtil.getString(request,"inboxType");
            String licenceNo = ParamUtil.getString(request,"inboxAdvancedSearch");
            String inboxService = ParamUtil.getString(request,"inboxService");
            log.debug(StringUtil.changeForLog("licenceNo ....") + licenceNo);
            Map<String,Object> inboxMap = new HashMap<>();
            if(inboxType != null){
                inboxMap.put("messageType",inboxType);
            }
            if(inboxService != null){
                inboxMap.put("interService",inboxService);
            }
//            inboxParameter.setFilters(inboxMap);
        }
    }

    /**
     *
     * @param bpc
     * @Description Step ---> DoSort
     */
    public void doSort(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("Step ---> DoSort"));
        HttpServletRequest request = bpc.request;
        String switchAction = ParamUtil.getString(request, InboxConst.SWITCH_ACTION);
        log.debug(StringUtil.changeForLog("switchAction" + switchAction));
    }

    /**
     *
     * @param bpc
     * @Description Step ---> Switch
     */
    public void switchStep(BaseProcessClass bpc){
        log.debug("The prepareSwitch start ...");
        String switchAction = ParamUtil.getString(bpc.request, InboxConst.SWITCH_ACTION);
        log.debug("*******************crudAction-->:" + switchAction);
        log.debug("The prepareSwitch end ...");
    }

    /**
     *
     * @param bpc
     * @description Data to Form select part
     */
    private void prepareSelectOption(BaseProcessClass bpc){
        List<SelectOption> inboxServiceSelectList = new ArrayList<>();
        inboxServiceSelectList.add(new SelectOption("1", "All"));
        inboxServiceSelectList.add(new SelectOption("Blood Banking", "Blood Banking"));
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
