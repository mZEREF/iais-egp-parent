package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class TreatmentValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        VssSuperDataSubmissionDto vssSuperDataSubmissionDto = (VssSuperDataSubmissionDto) ParamUtil.getSessionAttr(request, DataSubmissionConstant.VSS_DATA_SUBMISSION);
        VssTreatmentDto vssTreatmentDto = vssSuperDataSubmissionDto.getVssTreatmentDto();
        TreatmentDto treatmentDto = vssTreatmentDto.getTreatmentDto();
        if(treatmentDto == null){
            treatmentDto = new TreatmentDto();
        }
        String residenceStatus = treatmentDto.getResidenceStatus();
        if(!StringUtil.isEmpty(residenceStatus) && residenceStatus.equals(DataSubmissionConsts.RESIDENCE_STATUS_OTHERS)){
           if(StringUtil.isEmpty(treatmentDto.getOtherResidenceStatus())){
               errorMap.put("otherResidenceStatus", "GENERAL_ERR0006");
           }
        }
        String ethnicGroup = treatmentDto.getEthnicGroup();
        if(!StringUtil.isEmpty(ethnicGroup) && ethnicGroup.equals("ETHG005")){
            if(StringUtil.isEmpty(treatmentDto.getOtherEthnicGroup())){
                errorMap.put("otherEthnicGroup", "GENERAL_ERR0006");
            }
        }
        Integer livingChildrenNo = treatmentDto.getLivingChildrenNo();
        if(livingChildrenNo !=null && livingChildrenNo>=1){
            if(StringUtil.isEmpty(treatmentDto.getLastChildBirthday())){
                errorMap.put("lastChildBirthday", "GENERAL_ERR0006");
            }
        }
        return errorMap;
    }
}
