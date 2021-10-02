package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.recall.RecallApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxMsgMaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterInboxUserDto;

import java.util.List;
import java.util.Map;

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
    boolean updateMsgStatus(String[] msgId);
    Boolean canRecallApplication(RecallApplicationDto recallApplicationDto);
    List<RecallApplicationDto> canRecallApplications(List<RecallApplicationDto> recallApplicationDtos);
    RecallApplicationDto recallApplication(RecallApplicationDto recallApplicationDto);
    public Map<String,String> checkRenewalStatus(String licenceId);
    void updateMsgStatusTo(String msgId,String msgStatus);
    Boolean checkEligibility(String appId);
    List<InboxMsgMaskDto> getInboxMaskEntity(String msgId);
    List<PremisesDto> getPremisesByLicId(String licenceId);
    AuditTrailDto getLastLoginInfo(String loginUserId, String sessionId);
    Map<String,String> checkRfcStatus(String licenceId);
    Map<String,Boolean> listResultCeased(List<String> licIds);
    Map<String,String> appealIsApprove(String appIdOrLicenceId,String type);
    List<ApplicationSubDraftDto> getDraftByLicAppId(String licAppId);
    ApplicationDraftDto getDraftByAppNo(String appNo);
    ApplicationGroupDto getAppGroupByGroupId(String appGroupId);
    void  deleteDraftByNo(String draftNo);
    LicenceViewDto getLicenceViewDtoByLicenceId(String licenceId);
    LicenseeDto getLicenseeDtoBylicenseeId(String licenseeId);
    List<ApplicationSubDraftDto> getDraftByLicAppIdAndStatus(String licAppId,String status);
    Map<String,Boolean> getMapCanInsp();
}
