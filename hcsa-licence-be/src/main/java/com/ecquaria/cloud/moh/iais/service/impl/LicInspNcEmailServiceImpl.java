package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.service.LicInspNcEmailService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * LicInspNcEmailServiceImpl
 *
 * @author junyu
 * @date 2020/2/27
 */
@Service
public class LicInspNcEmailServiceImpl implements LicInspNcEmailService {




    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;


    @Override
    public LicenceViewDto getLicenceDtoByLicPremCorrId(String licPremCorrId) {
        return hcsaLicenceClient.getLicenceViewDtoByLicPremCorrId(licPremCorrId).getEntity();
    }


}
