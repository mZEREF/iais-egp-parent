package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
@Slf4j
public class TreatmentValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = (VssSuperDataSubmissionDto) ParamUtil.getSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION);
        VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto() == null ? new VssTreatmentDto() : vssSuperDataSubmissionDto.getVssTreatmentDto();
        TreatmentDto treatmentDto = vssTreatmentDto.getTreatmentDto() == null ? new TreatmentDto() : vssTreatmentDto.getTreatmentDto();
        String lastChildBirthday = treatmentDto.getLastChildBirthday();
        String residenceStatus = treatmentDto.getResidenceStatus();
        if(!StringUtil.isEmpty(residenceStatus) && residenceStatus.equals(DataSubmissionConsts.RESIDENCE_STATUS_OTHERS)){
           if(StringUtil.isEmpty(treatmentDto.getOtherResidenceStatus())){
               errorMap.put("otherResidenceStatus", "GENERAL_ERR0006");
           }
        }
        String ethnicGroup = treatmentDto.getEthnicGroup();
        if(!StringUtil.isEmpty(ethnicGroup) && ethnicGroup.equals("ECGP004")){
            if(StringUtil.isEmpty(treatmentDto.getOtherEthnicGroup())){
                errorMap.put("otherEthnicGroup", "GENERAL_ERR0006");
            }
        }
       String occupation = treatmentDto.getOccupation();
        if(StringUtil.isNotEmpty(occupation) && occupation.equals("VSSOP011")){
            if(StringUtil.isEmpty(treatmentDto.getOtherOccupation())){
                errorMap.put("otherOccupation", "GENERAL_ERR0006");
            }
        }
        String sterilizationReason = treatmentDto.getSterilizationReason();
        if(StringUtil.isNotEmpty(sterilizationReason) && sterilizationReason.equals("VSSRFS009")){
            if(StringUtil.isEmpty(treatmentDto.getOtherSterilizationReason())){
                errorMap.put("otherSterilizationReason", "GENERAL_ERR0006");
            }
        }
        String livingChildrenNo = treatmentDto.getLivingChildrenNo();
        if(StringUtil.isNotEmpty(livingChildrenNo) && StringUtil.isNumber(livingChildrenNo)){
            if(Integer.parseInt(livingChildrenNo) >0){
                if(StringUtil.isEmpty(lastChildBirthday)){
                    errorMap.put("lastChildBirthday", "GENERAL_ERR0006");
                }
            }

        }
        if(!StringUtil.isEmpty(livingChildrenNo) && !StringUtil.isNumber(livingChildrenNo)){
            errorMap.put("livingChildrenNo", "GENERAL_ERR0002");
        }

        if(StringUtil.isNotEmpty(lastChildBirthday)){
            try {
                if(CommonValidator.isDate(lastChildBirthday) && Formatter.compareDateByDay(lastChildBirthday) >=0){
                    errorMap.put("lastChildBirthday","Must be earlier then current date");
                }
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }

        return errorMap;
    }
}
