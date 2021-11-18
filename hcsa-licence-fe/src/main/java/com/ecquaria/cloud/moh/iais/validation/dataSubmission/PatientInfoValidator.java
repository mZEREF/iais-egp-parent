package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.HusbandDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import com.ecquaria.cloud.moh.iais.service.datasubmission.impl.PatientServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @Description PatientInfoValidator
 * @Auther chenlei on 10/25/2021.
 */
@Slf4j
public class PatientInfoValidator implements CustomizeValidator {

    @Override
    public Map<String, String> validate(Object obj, String profile, HttpServletRequest request) {
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        //if (StringUtil.isIn("save", profiles)) {
        PatientInfoDto patientInfo = (PatientInfoDto) obj;
        PatientDto patient = patientInfo.getPatient();
        if (patient == null) {
            patient = new PatientDto();
        }
        ValidationResult result = WebValidationHelper.validateProperty(patient, "save");
        if (result != null) {
            map.putAll(result.retrieveAll());
        }
        LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
        String orgId = Optional.ofNullable(loginContext).map(LoginContext::getOrgId).orElse("");
        PatientService patientService = SpringContextHelper.getContext().getBean(PatientService.class);
        PatientDto patientDto = patientService.getPatientDto(patient.getIdType(), patient.getIdNumber(),
                patient.getNationality(), orgId);
        if (patientDto != null && (StringUtil.isEmpty(patient.getId()) || !Objects.equals(patientDto.getId(), patient.getId()))) {
            map.put("idNumber", MessageUtil.getMessageDesc("DS_ERR007"));
        }
        String birthDate = patient.getBirthDate();
        if (!StringUtil.isEmpty(birthDate) && CommonValidator.isDate(birthDate)) {
            try {
                if (Formatter.compareDateByDay(birthDate) > 0) {
                    map.put("birthDate", MessageUtil.replaceMessage("DS_ERR001", "Date of Birth", "field"));
                }
            } catch (Exception e) {
                log.error(StringUtil.changeForLog(e.getMessage()), e);
            }
        }
        if (patient.isPreviousIdentification()) {
            PatientDto previous = patientInfo.getPrevious();
            if (previous == null) {
                previous = new PatientDto();
            }
            result = WebValidationHelper.validateProperty(previous, "ART");
            if (result != null && result.isHasErrors()) {
                map.putAll(result.retrieveAll("pre", ""));
            }
            boolean retrievePrevious = patientInfo.isRetrievePrevious();
            if (!retrievePrevious) {
                //map.put("retrievePrevious", "DS_ERR005");
            } else if (StringUtil.isEmpty(previous.getId())) {
                map.put("retrievePrevious", "GENERAL_ACK018");
            }
            if (!StringUtil.isEmpty(previous.getIdNumber()) && previous.getIdNumber().equals(patient.getIdNumber())
                    && Objects.equals(previous.getIdType(), patient.getIdType())
                    && Objects.equals(previous.getNationality(), patient.getNationality())) {
                map.put("preIdNumber", "GENERAL_ERR0051");
            }
        }
        HusbandDto husband = patientInfo.getHusband();
        if (husband == null) {
            husband = new HusbandDto();
        }
        result = WebValidationHelper.validateProperty(husband, "save");
        if (result != null) {
            map.putAll(result.retrieveAll("", "Hbd"));
        }
        if (!StringUtil.isEmpty(husband.getIdNumber()) && husband.getIdNumber().equals(patient.getIdNumber())
                && Objects.equals(husband.getIdType(), patient.getIdType())
                && Objects.equals(husband.getNationality(), patient.getNationality())) {
            map.put("idNumberHbd", "GENERAL_ERR0051");
        }
        //}
        return map;
    }

}
