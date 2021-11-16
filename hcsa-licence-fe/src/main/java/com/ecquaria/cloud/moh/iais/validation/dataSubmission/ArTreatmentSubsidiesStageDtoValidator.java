package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArTreatmentSubsidiesStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * ArTreatmentSubsidiesStageDtoValidator
 *
 * @author jiawei
 * @date 2021/11/1
 */

public class ArTreatmentSubsidiesStageDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(Object obj, String profile, HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ArTreatmentSubsidiesStageDto arTreatmentSubsidiesStageDto = (ArTreatmentSubsidiesStageDto) obj;
        int freshCount = ParamUtil.getInt(request,"freshCount",0);
        int frozenCount = ParamUtil.getInt(request,"frozenCount",0);
        if ((freshCount >= 3 || frozenCount >= 3) && !arTreatmentSubsidiesStageDto.getIsThereAppeal()){
            errorMap.put("isThereAppealShow","Disallow submission if patient's total AR subsidies entered in the system is 3 Fresh / Frozen and option selected for 'Is there an Appeal?' is 'No'");
        }
        return errorMap;
    }
}
