package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.InboxService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class InbocServiceImpl implements InboxService {

    private static final String URL="inter-inbox:8877/inter-inbox";

    @Override
    public List<InterInboxDto> selectAllInbox() {
        return null;
    }

    @Override
    public SearchResult<InterInboxDto> inboxDoQuery(SearchParam searchParam) {
        return RestApiUtil.query(URL+"/param",searchParam);
    }

}
