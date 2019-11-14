package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
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
        FilterParameter inboxParameter = new FilterParameter();
        inboxParameter.setClz(InboxQueryDto.class);
        inboxParameter.setSearchAttr(InboxConst.INBOX_PARAM);
        inboxParameter.setResultAttr(InboxConst.INBOX_RESULT);
        inboxParameter.setSortField("id");
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, inboxParameter);
        QueryHelp.setMainSql("interInboxQuery","inboxQuery",searchParam);
        SearchResult searchResult = inboxService.inboxDoQuery(searchParam);

        if(!StringUtil.isEmpty(searchResult)){
            ParamUtil.setSessionAttr(request,InboxConst.INBOX_PARAM, searchParam);
            ParamUtil.setRequestAttr(request,InboxConst.INBOX_RESULT, searchResult);
        }


        log.debug(StringUtil.changeForLog("*****   the getAppList start  *****"));
        FilterParameter appParameter = new FilterParameter();
        appParameter.setClz(InboxAppQueryDto.class);
        appParameter.setSearchAttr(InboxConst.APP_PARAM);
        appParameter.setResultAttr(InboxConst.APP_RESULT);
        appParameter.setSortField("id");
        SearchParam searchAppParam = IaisEGPHelper.getSearchParam(request, appParameter);
        QueryHelp.setMainSql("interInboxQuery","AppQuery",searchAppParam);
        SearchResult searchAppResult = inboxService.applicationDoQuery(searchAppParam);

        if(!StringUtil.isEmpty(searchAppResult)){
            ParamUtil.setSessionAttr(request,InboxConst.APP_PARAM, searchAppParam);
            ParamUtil.setRequestAttr(request,InboxConst.APP_RESULT, searchAppResult);
        }
    }
}
