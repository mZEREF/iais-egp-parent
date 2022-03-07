package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInformationDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.TopFeClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.TopPatientSelectService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @Description TopPatientSelectServiceImpl
 * @Auther zhixing on 2/21/2022.
 */
@Service
@Slf4j
public class TopPatientSelectServiceImpl implements TopPatientSelectService {

    @Autowired
    private TopFeClient topFeClient;

    @Override
    public PatientInformationDto getTopPatientSelect(String idType, String idNumber, String nationality, String orgId) {
        log.info(StringUtil.changeForLog("----- Param: " + orgId + " : " + idType
                + " : " + idNumber + " : " + nationality + " -----"));
        if (StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNumber) || StringUtil.isEmpty(nationality)
                || StringUtil.isEmpty(orgId)) {
            return null;
        }
        return topFeClient.getTopPatientSelect(idType, idNumber, nationality, orgId).getEntity();
    }
}
