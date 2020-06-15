package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailAttachMentDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.SmsDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementListDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.EmailAuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ResendListDto;

import java.util.List;
import java.util.Map;

/**
 * @author guyin
 * @date 2019/12/28 10:40
 */
public interface BlastManagementListService {
    SearchResult<BlastManagementListDto> blastList(SearchParam searchParam);
    SearchResult<EmailAuditTrailDto> auditList(SearchParam searchParam);
    SearchResult<ResendListDto> resendList(SearchParam searchParam);
    BlastManagementDto saveBlast(BlastManagementDto blastManagementDto);
    void setSchedule(BlastManagementDto blastManagementDto);
    void deleteBlastList(List<String> list);
    BlastManagementDto getBlastById(String id);
    List<BlastManagementDto> getBlastBySendTime(String date);
    void setActual(String id);
    String getMessageId();
    void sendEmail(EmailDto emailDto, Map<String, byte[]> attachments);
    List<OrgUserDto> retrieveOrgUserAccount(List<String> ids);
    Boolean checkUse(List<String> disList);
    void sendSMS(List<String> recipts, SmsDto sms, String reqRefNum);
    List<String> getEmailByRoleId(String roleId);
    List<String> getSmsByRoleId(String roleId);
    EmailAttachMentDto getEmailById(String id);
    void setEmailResend(String id);
}
