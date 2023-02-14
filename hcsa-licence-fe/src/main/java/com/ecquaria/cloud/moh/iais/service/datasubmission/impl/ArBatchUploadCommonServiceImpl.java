package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArBatchUploadCommonService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * ArBatchUploadCommonServiceImpl
 *
 * @Author dongchi
 * @Date 2023/2/14 14:57
 **/
@Service
@Slf4j
public class ArBatchUploadCommonServiceImpl implements ArBatchUploadCommonService {
    @Autowired
    private PatientService patientService;

    @Override
    public PatientInfoDto setPatientInfo(String idType, String idNo, HttpServletRequest request) {
        if(idType == null || idNo == null) {
            return null;
        }
        PatientInfoDto patientInfoDto ;
        if ("FIN".equals(idType)) {
            boolean finValidation = SgNoValidator.validateFin(idNo);
            if(!finValidation) return null;

        } else if("NRIC".equals(idType)) {
            boolean nricValidation = SgNoValidator.validateNric(idNo);
            if (!nricValidation) return null;
        }
        String orgId = null;
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
        if (loginContext != null) {
            orgId = loginContext.getOrgId();
        }
        if ("FIN".equals(idType) || "NRIC".equals(idType)) {
            // birthday now not in template
            // patientInfoDto = patientService.getPatientInfoDtoByIdTypeAndIdNumberAndBirthDate(idType,idNo, Formatter.formatDateTime(birthDate, AppConsts.DEFAULT_DATE_BIRTHDATE_FORMAT), orgId);
            patientInfoDto = patientService.getPatientInfoDtoByIdTypeAndIdNumber(idType,idNo, orgId);
        }else {
            patientInfoDto = patientService.getPatientInfoDtoByIdTypeAndIdNumber(idType,idNo, orgId);
        }
        return patientInfoDto;
    }
}
