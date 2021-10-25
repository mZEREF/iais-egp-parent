package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Description PatientServiceImp
 * @Auther chenlei on 10/25/2021.
 */
@Service
@Slf4j
public class PatientServiceImpl implements PatientService {

    @Autowired
    private LicenceClient licenceClient;

    @Override
    public PatientDto getPatientDto(String idNumber, String nationality, String orgId) {
        if (StringUtil.isEmpty(idNumber) || StringUtil.isEmpty(nationality) || StringUtil.isEmpty(orgId)) {
            return null;
        }
        return licenceClient.getPatientDto(idNumber, nationality, orgId).getEntity();
    }

}
