package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;
import com.ecquaria.cloud.moh.iais.service.client.DpFeClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Autowired
    private GenerateIdClient generateIdClient;

    @Override
    public DataSubmissionDto getPatientDataSubmissionByConds(String idType, String idNumber, String nationality, String orgId,
            String patientType) {
        log.info(StringUtil.changeForLog("PatientDto - " + idType + " : " + idNumber
                + " : " + nationality + " : " + orgId + " : " + patientType));
        if (StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNumber) || StringUtil.isEmpty(nationality)
                || StringUtil.isEmpty(orgId) || StringUtil.isEmpty(patientType)) {
            return null;
        }
        return arFeClient.getPatientDataSubmissionByConds(idType, idNumber, nationality, orgId, patientType).getEntity();
    }

    @Override
    public PatientDto getActivePatientByConds(String idType, String idNumber, String nationality, String orgId, String patientType) {
        log.info(StringUtil.changeForLog("PatientDto - " + idType + " : " + idNumber
                + " : " + nationality + " : " + orgId + " : " + patientType));
        if (StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNumber) || StringUtil.isEmpty(nationality)
                || StringUtil.isEmpty(orgId) || StringUtil.isEmpty(patientType)) {
            return null;
        }
        return arFeClient.getActivePatientByConds(idType, idNumber, nationality, orgId, patientType).getEntity();
    }

    @Override
    public PatientDto getActivePatientByCond(String name, String idNumber, String nationality, String orgId, String patientType) {
        log.info(StringUtil.changeForLog("PatientDto - " + name + " : " + idNumber
                + " : " + nationality + " : " + orgId + " : " + patientType));
        if (StringUtil.isEmpty(name) || StringUtil.isEmpty(idNumber) || StringUtil.isEmpty(nationality)
                || StringUtil.isEmpty(orgId) || StringUtil.isEmpty(patientType)) {
            return null;
        }
        return arFeClient.getActivePatientByCond(name, idNumber, nationality, orgId, patientType).getEntity();
    }

    @Override
    public PatientInfoDto getPatientInfoDtoByIdTypeAndIdNumber(String idType,String idNumber,String orgId) {
        return arFeClient.getPatientInfoDtoByIdTypeAndIdNumber(idType,idNumber, orgId).getEntity();
    }

    @Override
    public PatientInfoDto getPatientInfoDtoByIdTypeAndIdNumberAndBirthDate(String idType, String idNumber, String birthDate, String orgId) {
        return arFeClient.getPatientByIdTypeAndIdNumberAndBirthDate(idType, idNumber, birthDate, orgId).getEntity();
    }

    @Override
    public PatientDto getActiveArPatientByConds(String idType, String idNumber, String nationality, String orgId) {
        return getActivePatientByConds(idType, idNumber, nationality, orgId, DataSubmissionConsts.DS_PATIENT_ART);
    }

    @Override
    public PatientDto getActiveArPatientByCond(String name, String idNumber, String nationality, String orgId) {
        return getActivePatientByCond(name, idNumber, nationality, orgId, DataSubmissionConsts.DS_PATIENT_ART);
    }

    @Override
    public PatientDto getArPatientDto(String idType, String idNumber, String nationality, String orgId) {
        log.info(StringUtil.changeForLog("----- Param: " + orgId + " : " + idType
                + " : " + idNumber + " : " + nationality + " -----"));
        if (StringUtil.isEmpty(idType) || StringUtil.isEmpty(idNumber) || StringUtil.isEmpty(nationality)
                || StringUtil.isEmpty(orgId)) {
            return null;
        }
        List<PatientDto> patientDtoList = arFeClient.getPatientsByConds(idType, idNumber, nationality, orgId,
                DataSubmissionConsts.DS_PATIENT_ART).getEntity();
        if (patientDtoList == null || patientDtoList.isEmpty()) {
            return null;
        }
        return patientDtoList.get(0);
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

    @Override
    public String getPatientCode(String patientCode) {
        if (StringUtil.isNotEmpty(patientCode)) {
            return patientCode;
        }
        return generateIdClient.getSeqId().getEntity();
    }

    @Override
    public PatientInfoDto getPatientInfoDto(String patientCode) {
        log.info(StringUtil.changeForLog("----- Param: " + patientCode));
        if (StringUtil.isEmpty(patientCode)) {
            return null;
        }
        return arFeClient.patientInfoDtoByPatientCode(patientCode).getEntity();
    }

    @Override
    public PatientDto getPatientDtoBySubmissionId(String submissionId) {
        log.info(StringUtil.changeForLog("----- Param: " + submissionId));
        if (StringUtil.isEmpty(submissionId)) {
            return null;
        }
        return dpFeClient.getPatientDtoBySubmissionId(submissionId).getEntity();
    }

    @Override
    public String judgeIdType(String isNricFin, String idNo) {
        String idType = null;
        if (AppConsts.NO.equals(isNricFin)) {
            idType = DataSubmissionConsts.DTV_ID_TYPE_PASSPORT;
        } else if (StringUtil.isEmpty(isNricFin)) {
            return null;
        } else if (StringUtil.isNotEmpty(isNricFin)) {
            boolean isNric = SgNoValidator.validateNric(idNo);
            boolean isFin = SgNoValidator.validateFin(idNo);
            if (isNric) {
                idType = DataSubmissionConsts.DTV_ID_TYPE_NRIC;
            } else if (isFin) {
                idType = DataSubmissionConsts.DTV_ID_TYPE_FIN;
            } else {
                idType = DataSubmissionConsts.DTV_ID_TYPE_NRIC;
            }
        }
        return idType;
    }

    @Override
    public String judgeIdType(String idNo) {
        String idType = DataSubmissionConsts.DTV_ID_TYPE_PASSPORT;
        if (StringUtil.isNotEmpty(idNo)) {
            boolean isNric = SgNoValidator.validateNric(idNo);
            boolean isFin = SgNoValidator.validateFin(idNo);
            if (isNric) {
                idType = DataSubmissionConsts.DTV_ID_TYPE_NRIC;
            } else if (isFin){
                idType = DataSubmissionConsts.DTV_ID_TYPE_FIN;
            }
        }
        return idType;
    }
}
