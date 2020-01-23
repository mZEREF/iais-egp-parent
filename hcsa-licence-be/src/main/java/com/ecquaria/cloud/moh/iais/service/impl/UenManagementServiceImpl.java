package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.organization.MohUenDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UenDto;
import com.ecquaria.cloud.moh.iais.service.UenManagementService;
import com.ecquaria.cloud.moh.iais.service.client.UenManagementClient;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * UenManagementServiceImpl
 *
 * @author junyu
 * @date 2020/1/22
 */
public class UenManagementServiceImpl implements UenManagementService {
    @Autowired
    UenManagementClient uenManagementClient;

    private UenDto getUenDetails(String uenNo) {
        //等ACRA api
        return null;
    }

    @Override
    public boolean validityCheckforAcraissuedUen(MohUenDto mohUenDto) {
        MohUenDto mohUenDto1=uenManagementClient.getMohUenById(mohUenDto.getUenNo()).getEntity();
        //等ACRA api
        return false;
    }

    @Override
    public boolean generatesMohIssuedUen(MohUenDto mohUenDto) {
        MohUenDto mohUenDto1= uenManagementClient.generatesMohIssuedUen(mohUenDto).getEntity();
        //等ACRA api
        return false;
    }
}
