package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;

/**
 * @author zixian
 * @date 2020/10/29 10:41
 * @description
 */
public interface MasterCodeService {
    void inactiveMasterCode(AuditTrailDto auditTrailDto);
    void activeMasterCode(AuditTrailDto auditTrailDto);
}
