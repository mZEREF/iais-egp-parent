package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.service.AssessmentGuideService;
import com.ecquaria.cloud.moh.iais.service.client.ConfigInboxClient;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AssessmentGuideImpl implements AssessmentGuideService {

    @Autowired
    private ConfigInboxClient configInboxClient;

    @Override
    public List<HcsaServiceDto> getServicesInActive() {
        return configInboxClient.getActiveServices().getEntity();
    }
}
