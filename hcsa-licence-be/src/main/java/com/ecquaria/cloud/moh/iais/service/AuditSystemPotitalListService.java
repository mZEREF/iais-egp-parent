package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditSystemPotentialDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;

import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2020/2/10 13:00
 */
public interface AuditSystemPotitalListService {
    List<AuditTaskDataFillterDto> getSystemPotentailAdultList(AuditSystemPotentialDto dto);
    List<AuditTaskDataFillterDto> getSystemPotentailAdultList();
    List<AuditTaskDataFillterDto> getSystemPotentailAdultCancelList();
    List<AuditTaskDataFillterDto> getCanInactiveAudit();
    void inActiveAudit(AuditTaskDataFillterDto auditTaskDataFillterDto,AuditTrailDto intranet);
}
