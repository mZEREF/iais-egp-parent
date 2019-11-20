package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppInboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;

import java.util.List;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-11-06 09:48
 **/

public interface InboxService {

    List<AppInboxQueryDto> applicationDoQuery();
    SearchResult<InboxQueryDto> inboxDoQuery(SearchParam searchParam);
}
