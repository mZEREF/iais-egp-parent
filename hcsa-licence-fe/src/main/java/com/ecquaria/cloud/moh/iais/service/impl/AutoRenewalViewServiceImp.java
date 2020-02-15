package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AutoRenewalDto;
import com.ecquaria.cloud.moh.iais.service.AutoRenewalViewService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AutoRenewalViewServiceImp
 *
 * @author caijing
 * @date 2020/1/7
 */
@Service
public class AutoRenewalViewServiceImp implements AutoRenewalViewService {
    @Autowired
    HcsaLicenClient hcsaLicenClient;


    @Override
    public AutoRenewalDto getAutoRenewalViewByLicNo(String licenceNo) {
        return null;
    }
}
