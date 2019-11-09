package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
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


    private final FilterParameter filterParameter;

    @Autowired
    private InboxDelegator(FilterParameter filterParameter,InboxService inboxService){
        this.filterParameter = filterParameter;
        this.inboxService = inboxService;
    }

    public void doStart(BaseProcessClass bpc){

        log.debug(StringUtil.changeForLog("the doStart start ...."));
        HttpServletRequest request = bpc.request;
        filterParameter.setClz(InterInboxDto.class);
        filterParameter.setSearchAttr("inboxParam");
        filterParameter.setResultAttr("inboxResult");
        filterParameter.setSortField("id");
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        QueryHelp.setMainSql("interInboxQuery","inboxQuery",searchParam);
        SearchResult searchResult = inboxService.inboxDoQuery(searchParam);

        if(!StringUtil.isEmpty(searchResult)){
            ParamUtil.setSessionAttr(request,"inboxParam", searchParam);
            ParamUtil.setRequestAttr(request,"inboxResult", searchResult);
        }
    }

    public void prepare(BaseProcessClass bpc){

    }

}
