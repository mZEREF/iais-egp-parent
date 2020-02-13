package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditSystemPotentialDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataDto;

import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2020/2/10 13:00
 */
public interface AuditSystemPotitalListService {
    List<AuditTaskDataDto> getSystemPotentailAdultList(AuditSystemPotentialDto dto);
}
