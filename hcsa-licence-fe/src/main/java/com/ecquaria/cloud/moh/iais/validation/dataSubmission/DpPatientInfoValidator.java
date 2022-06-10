package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.service.datasubmission.PatientService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * DpPatientInfoValidator
 *
 * @author fanghao
 * @date 2021/11/29
 */

@Component
@Slf4j
public class DpPatientInfoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate( HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        DpSuperDataSubmissionDto dpSuperDataSubmissionDto = DataSubmissionHelper.getCurrentDpDataSubmission(request);
        PatientDto patientDto = dpSuperDataSubmissionDto.getPatientDto() == null ? new PatientDto() :dpSuperDataSubmissionDto.getPatientDto();
        String birthDate = patientDto.getBirthDate();

        LoginContext loginContext = DataSubmissionHelper.getLoginContext(request);
        String orgId = Optional.ofNullable(loginContext).map(LoginContext::getOrgId).orElse("");
        PatientService patientService = SpringContextHelper.getContext().getBean(PatientService.class);
        PatientDto patient = patientService.getDpPatientDto(patientDto.getIdType(), patientDto.getIdNumber(),
                patientDto.getNationality(), orgId);
        if (patient != null && (StringUtil.isEmpty(patientDto.getId()) || !Objects.equals(patient.getId(), patientDto.getId()))) {
            errorMap.put("idNumber", MessageUtil.getMessageDesc("DS_ERR007"));
        }

        if(!StringUtil.isEmpty(patientDto.getIdType())){
            if (patientDto.getIdType().equals(DataSubmissionConsts.DTV_ID_TYPE_PASSPORT)){
                if(StringUtil.isEmpty(patientDto.getNationality())){
                    errorMap.put("nationality", "GENERAL_ERR0006");
                }
            }
        }
        if(StringUtil.isNotEmpty(patientDto.getNationality())&& patientDto.getNationality().equals("NAT0001")){
            if(StringUtil.isEmpty(patientDto.getEthnicGroup())){
                errorMap.put("ethnicGroup", "GENERAL_ERR0006");
            }
        }
        if(StringUtil.isNotEmpty(patientDto.getIdType())&&StringUtil.isNotEmpty(patientDto.getIdNumber())){
            if(!SgNoValidator.validateNewIdNoForDataSubmission(patientDto.getIdType(), patientDto.getIdNumber())){
                errorMap.put("idNumber", "RFC_ERR0012");
            }
        }

        if(StringUtil.isNotEmpty(patientDto.getPostalCode())){

            if(StringUtil.isNumber(patientDto.getPostalCode())  && Long.valueOf(patientDto.getPostalCode())<0){
                errorMap.put("postalCode", "Negative numbers are not allowed on this field.");
            }else if("NAT0001".equals(patientDto.getNationality())){
                    if(!CommonValidator.isValidePostalCode(patientDto.getPostalCode())){
                        errorMap.put("postalCode", "NEW_ERR0004");
                    }
            }else {
                if(patientDto.getPostalCode().length()>20){
                    String general_err0041 = NewApplicationHelper.repLength("Postal Code", "20");
                    errorMap.put("postalCode", general_err0041);
                }
            }
        }
        if(StringUtil.isNotEmpty(patientDto.getMobileNo())||StringUtil.isNotEmpty(patientDto.getHomeTelNo())){
            if(StringUtil.isNumber(patientDto.getMobileNo())&&Long.valueOf(patientDto.getMobileNo())<0){
                errorMap.put("mobileNo", "Negative numbers are not allowed on this field.");
            }else if("NAT0001".equals(patientDto.getNationality())){
                if(!CommonValidator.isMobile(patientDto.getMobileNo())){
                    errorMap.put("mobileNo", "GENERAL_ERR0007");
                }
            }
            if(StringUtil.isNumber(patientDto.getPostalCode())&&Long.valueOf(patientDto.getHomeTelNo())<0){
                errorMap.put("homeTelNo", "Negative numbers are not allowed on this field.");
            }else if("NAT0001".equals(patientDto.getNationality())){
                    if(!CommonValidator.isTelephoneNo(patientDto.getHomeTelNo())){
                        errorMap.put("homeTelNo", "GENERAL_ERR0015");
                    }
            }
        }
        if(StringUtil.isNotEmpty(patientDto.getEthnicGroup())){
            if("ECGP004".equals(patientDto.getEthnicGroup()) && StringUtil.isEmpty(patientDto.getEthnicGroupOther())){
                errorMap.put("ethnicGroupOther", "GENERAL_ERR0006");
            }else if(StringUtil.isNotEmpty(patientDto.getEthnicGroupOther())&&patientDto.getEthnicGroupOther().length()>20){
                String general_err0041 = NewApplicationHelper.repLength("Other Ethnic Group", "20");
                errorMap.put("ethnicGroupOther", general_err0041);
            }
        }
        if(!StringUtil.isEmpty(patientDto.getAddrType())){
            if(patientDto.getAddrType().equals(DataSubmissionConsts.DP_PATIENTINFO_ADDRESS_TYPE_APT_BLK)){
                if(StringUtil.isEmpty(patientDto.getBlkNo())){
                    errorMap.put("blkNo", "GENERAL_ERR0006");
                }
                if(StringUtil.isEmpty(patientDto.getFloorNo())){
                    errorMap.put("floorNo", "GENERAL_ERR0006");
                }
                if(StringUtil.isEmpty(patientDto.getUnitNo())){
                    errorMap.put("unitNo", "GENERAL_ERR0006");
                }
            }
        }
        if(!StringUtil.isEmpty(birthDate)){
            try {
                if(CommonValidator.isDate(birthDate) && Formatter.compareDateByDay(birthDate) >0){
                    errorMap.put("birthDate", MessageUtil.replaceMessage("DS_ERR001", "Date of Birth", "field"));
                }
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }

        String postalCode = patientDto.getPostalCode();
        String mobileNo = patientDto.getMobileNo();
        String homeTelNo  =patientDto.getHomeTelNo();
        if(StringUtil.isNotEmpty(mobileNo) && !StringUtil.isNumber(mobileNo)){
            errorMap.put("mobileNo", "GENERAL_ERR0002");
        }
        if(StringUtil.isNotEmpty(postalCode) && !StringUtil.isNumber(postalCode)){
            errorMap.put("postalCode", "GENERAL_ERR0002");
        }
        if(StringUtil.isNotEmpty(homeTelNo) && !StringUtil.isNumber(homeTelNo)){
            errorMap.put("homeTelNo", "GENERAL_ERR0002");
        }
        return errorMap;
    }
}
