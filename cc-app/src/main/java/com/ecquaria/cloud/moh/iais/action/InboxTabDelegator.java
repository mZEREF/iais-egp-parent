package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.InboxTabService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-10-15 17:47
 **/
@Delegator("inboxTabDelegator")
@Slf4j
public class InboxTabDelegator {

    private final FilterParameter filterParameter;
    private final InboxTabService inboxTabService;

    @Autowired
    private InboxTabDelegator(FilterParameter filterParameter,InboxTabService inboxTabService){
        this.filterParameter = filterParameter;
        this.inboxTabService = inboxTabService;
    }

    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        filterParameter.setClz(ApplicationDto.class);
        filterParameter.setSearchAttr("applicationParam");
        filterParameter.setResultAttr("applicationResult");
        filterParameter.setSortField("id");
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        QueryHelp.setMainSql("applicationQuery","appQuery",searchParam);
        SearchResult searchResult = inboxTabService.doQuery(searchParam);

        if(!StringUtil.isEmpty(searchResult)){
            ParamUtil.setSessionAttr(request,"applicationParam", searchParam);
            ParamUtil.setRequestAttr(request,"applicationResult", searchResult);
        }
    }

    public void applicationList(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the application list start ...."));
        String applicationType = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
        List<ApplicationDto> applicationDtos = inboxTabService.searchByAppType(applicationType);
        if(StringUtil.isEmpty(applicationDtos)){
            ParamUtil.setRequestAttr(bpc.request,IaisEGPConstant.CRUD_ACTION_TYPE,applicationDtos);
        }
    }
}

