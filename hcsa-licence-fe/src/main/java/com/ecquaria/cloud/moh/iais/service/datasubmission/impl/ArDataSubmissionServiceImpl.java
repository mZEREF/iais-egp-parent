package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Description ArDataSubmissionServiceImpl
 * @Auther chenlei on 10/22/2021.
 */
@Service
@Slf4j
public class ArDataSubmissionServiceImpl implements ArDataSubmissionService {

    @Autowired
    private LicenceClient licenceClient;

    @Override
    public List<AppGrpPremisesDto> getAppGrpPremises(String licenseeId, String serviceName) {
        return licenceClient.getDistinctPremisesByLicenseeId(licenseeId, serviceName).getEntity();
    }

}
