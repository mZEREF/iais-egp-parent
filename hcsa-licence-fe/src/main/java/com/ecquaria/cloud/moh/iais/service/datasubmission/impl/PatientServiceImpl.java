package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;
import com.ecquaria.cloud.moh.iais.service.client.DpFeClient;
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
    private ArFeClient arFeClient;

    @Autowired
    private DpFeClient dpFeClient;

    @Override
    public PatientDto getArPatientDto(String idType, String idNumber, String nationality, String orgId) {
        log.info(StringUtil.changeForLog("----- Param: " + orgId + " : " + idType
                + " : " + idNumber + " : " + nationality + " -----"));
        if (StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNumber) || StringUtil.isEmpty(nationality)
                || StringUtil.isEmpty(orgId)) {
            return null;
        }
        return arFeClient.getPatientDto(idType, idNumber, nationality, orgId).getEntity();
    }
    @Override
    public PatientDto getDpPatientDto(String idType, String idNumber, String nationality, String orgId) {
        log.info(StringUtil.changeForLog("----- Param: " + orgId + " : " + idType
                + " : " + idNumber + " : " + nationality + " -----"));
        if (StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNumber) || StringUtil.isEmpty(nationality)
                || StringUtil.isEmpty(orgId)) {
            return null;
        }
        return dpFeClient.getDpPatientDto(idType, idNumber, nationality, orgId).getEntity();
    }

}
