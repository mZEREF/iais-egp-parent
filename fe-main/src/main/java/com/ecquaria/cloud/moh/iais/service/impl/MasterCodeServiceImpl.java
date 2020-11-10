package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminMainFeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author zixian
 * @date 2020/11/9 18:11
 * @description
 */
@Service
@Slf4j
public class MasterCodeServiceImpl implements MasterCodeService {
    @Autowired
    private SystemAdminMainFeClient systemAdminMainFeClient;
    @Override
    public void inactiveMasterCode(AuditTrailDto auditTrailDto) {
        systemAdminMainFeClient.inactiveMasterCode(auditTrailDto);
    }
}
