package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.constant.InboxConst;
import com.ecquaria.cloud.moh.iais.service.InboxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class InboxServiceImpl implements InboxService {

    @Override
    public List<InterInboxDto> selectAllInbox() {
        return RestApiUtil.getList(InboxConst.INBOX_URL,List.class);
    }

    @Override
    public SearchResult<InboxAppQueryDto> applicationDoQuery(SearchParam searchParam) {
        return RestApiUtil.query(InboxConst.APP_URL+"/app-param",searchParam);
    }

    @Override
    public SearchResult<InboxQueryDto> inboxDoQuery(SearchParam searchParam) {
        return RestApiUtil.query(InboxConst.INBOX_URL+"/inbox-param",searchParam);
    }

}
