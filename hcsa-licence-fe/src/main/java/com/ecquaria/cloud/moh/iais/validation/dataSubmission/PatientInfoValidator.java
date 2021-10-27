package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.HusbandDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Description PatientInfoValidator
 * @Auther chenlei on 10/25/2021.
 */
public class PatientInfoValidator implements CustomizeValidator {

    @Override
    public Map<String, String> validate(Object obj, String[] profiles, HttpServletRequest request) {
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        if (StringUtil.isIn("AR", profiles)) {
            PatientInfoDto patientInfo = (PatientInfoDto) obj;
            PatientDto patient = patientInfo.getPatient();
            if (patient == null) {
                patient = new PatientDto();
            }
            ValidationResult result = WebValidationHelper.validateProperty(patient, "AR");
            if (result != null) {
                map.putAll(result.retrieveAll());
            }
            if (patient.isPreviousIdentification()) {
                PatientDto previous = patientInfo.getPatient();
                if (previous == null) {
                    previous = new PatientDto();
                }
                boolean retrievePrevious = patientInfo.isRetrievePrevious();
                if (!retrievePrevious) {
                    map.put("retrievePrevious", "DS_ERR005");
                } else if (!StringUtil.isEmpty(previous.getName())) {
                    map.put("retrievePrevious", "GENERAL_ACK018");
                }
                result = WebValidationHelper.validateProperty(previous, "AR");
                if (result != null) {
                    map.putAll(result.retrieveAll());
                }
            }
            HusbandDto husband = patientInfo.getHusband();
            if (husband == null) {
                husband = new HusbandDto();
            }
            result = WebValidationHelper.validateProperty(husband, "AR");
            if (result != null) {
                map.putAll(result.retrieveAll("", "Hbd"));
            }
        }
        return map;
    }

}
