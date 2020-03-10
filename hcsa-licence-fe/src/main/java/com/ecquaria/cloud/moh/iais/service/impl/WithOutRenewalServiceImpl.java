package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.WithOutRenewalDto;
import com.ecquaria.cloud.moh.iais.service.WithOutRenewalService;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WithOutRenewalServiceImpl implements WithOutRenewalService {

    @Autowired
    LicenceClient licenceClient;

    @Autowired
    SystemAdminClient systemAdminClient;

    @Override
    public WithOutRenewalDto getRenewalViewByLicNo(String licenceNo)
    {
        return null;
    }

    @Override
    public List<AppSubmissionDto> getAppSubmissionDtos(List<String> licenceIds) {
        return licenceClient.getAppSubmissionDtos(licenceIds).getEntity();
    }

    @Override
    public String getAppGrpNoByAppType(String appType) {
        return systemAdminClient.applicationNumber(appType).getEntity();
    }
}
