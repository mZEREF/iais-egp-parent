package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;

import java.util.List;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-11-06 09:48
 **/

public interface InboxService {

    String getServiceNameById(String serviceId);
    InterInboxUserDto getUserInfoByUserId(String userId);
    SearchResult<InboxAppQueryDto> appDoQuery(SearchParam searchParam);
    SearchResult<InboxQueryDto> inboxDoQuery(SearchParam searchParam);
    SearchResult<InboxLicenceQueryDto> licenceDoQuery(SearchParam searchParam);
    void recallApplication(String appNo);
    Integer licActiveStatusNum(String licenseeId);
    Integer appDraftNum(List<String> appOrgIds);
    Integer unreadAndUnresponseNum(String userId);
    void updateDraftStatus(String draftNo, String status);
}
