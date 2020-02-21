package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;

import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2020/2/19 10:54
 */
public interface AuditSystemListService {
    void getInspectors(List<AuditTaskDataFillterDto> auditTaskDataDtos);

    List<SelectOption> getAuditOp();

    void doSubmit(List<AuditTaskDataFillterDto> auditTaskDataDtos);
}
