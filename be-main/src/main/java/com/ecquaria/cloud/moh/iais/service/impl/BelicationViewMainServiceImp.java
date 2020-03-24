package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.service.BelicationViewMainService;
import com.ecquaria.cloud.moh.iais.service.client.BelicationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigMainClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BelicationViewMainServiceImp implements BelicationViewMainService {
    @Autowired
    private BelicationClient belicationClient;
    @Autowired
    private OrganizationMainClient organizationClient;
    @Autowired
    private HcsaConfigMainClient hcsaConfigClient;

    @Override
    public ApplicationDto updateApplicaiton(ApplicationDto applicationDto) {

        return  belicationClient.updateApplication(applicationDto).getEntity();
    }

}
