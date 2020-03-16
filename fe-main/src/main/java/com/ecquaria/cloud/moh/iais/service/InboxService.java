package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.recall.RecallApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;

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
    Integer licActiveStatusNum(String licenseeId);
    Integer appDraftNum(String licenseeId);
    Integer unreadAndUnresponseNum(String userId);
    void updateDraftStatus(String draftNo, String status);
    Boolean recallApplication(RecallApplicationDto recallApplicationDto);
}
