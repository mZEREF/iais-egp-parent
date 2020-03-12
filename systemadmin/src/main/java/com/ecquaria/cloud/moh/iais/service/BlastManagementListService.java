package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
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
    void sendEmail(EmailDto emailDto, Map<String, byte[]> attachments);
}
