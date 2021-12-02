package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

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
        PatientDto patientDto = dpSuperDataSubmissionDto.getPatientDto();
        String birthDate = patientDto.getBirthDate();
        if (patientDto == null) {
            patientDto = new PatientDto();
        }
        if(!StringUtil.isEmpty(patientDto.getIdType())){
            if (patientDto.getIdType().equals(DataSubmissionConsts.AR_ID_TYPE_PASSPORT_NO)){
                if(StringUtil.isEmpty(patientDto.getNationality())){
                    errorMap.put("nationality", "GENERAL_ERR0006");
                }
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
                e.printStackTrace();
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
