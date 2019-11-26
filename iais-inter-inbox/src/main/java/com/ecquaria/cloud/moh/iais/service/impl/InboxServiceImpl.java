package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppInboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.constant.InboxConst;
import com.ecquaria.cloud.moh.iais.service.InboxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class InboxServiceImpl implements InboxService {

    @Override
    public List<AppInboxQueryDto> applicationDoQuery() {
        List<AppInboxQueryDto> appInboxQueryDtoList = new ArrayList<>();
        return RestApiUtil.postGetList(InboxConst.INBOX_URL+"/app-param",appInboxQueryDtoList,AppInboxQueryDto.class);
    }

    @Override
    public SearchResult<InboxAppQueryDto> appDoQuery(SearchParam searchParam) {
        log.debug("JsonUtil app-param"+JsonUtil.parseToJson(searchParam));
        return RestApiUtil.query(InboxConst.APP_URL+"/app-param",searchParam);
    }

    @Override
    public SearchResult<InboxQueryDto> inboxDoQuery(SearchParam searchParam) {
        log.debug("JsonUtil inbox-param"+JsonUtil.parseToJson(searchParam));
        return RestApiUtil.query(InboxConst.INBOX_URL+"/inbox-param",searchParam);
    }

}
