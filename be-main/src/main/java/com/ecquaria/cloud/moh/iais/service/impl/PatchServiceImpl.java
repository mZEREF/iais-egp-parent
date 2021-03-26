package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.service.PatchService;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * PatchServiceImpl
 *
 * @author Jinhua
 * @date 2021/3/26 15:52
 */
@Service
public class PatchServiceImpl implements PatchService {
    @Autowired
    private OrganizationMainClient organizationMainClient;

    @Override
    public void patchWorkingGrpWithRole() {
        organizationMainClient.patchWorkingGrpForRoles("Go");
    }
}
