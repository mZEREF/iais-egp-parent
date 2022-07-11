package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.Dto.HuangKunRoomDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.service.HuangKunRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName: HuangKunDelegator
 * @author: haungkun
 * @date: 2022/7/7 16:42
 */
@Delegator("huangKunDelegator")
@Slf4j
public class HuangKunDelegator {

    @Autowired
    HuangKunRoomService huangKunRoomService;

    /**
     *  AutoStep: prepareData
     * @param bpc
     */
    public void prepareData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchResult<HuangKunRoomDto> rooms = huangKunRoomService.doQuery();
        ParamUtil.setRequestAttr(request, "rooms", rooms);
    }

    /**
     * AutoStep: doSearch
     * @param bpc
     */
    public void doSearch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
    }
}
