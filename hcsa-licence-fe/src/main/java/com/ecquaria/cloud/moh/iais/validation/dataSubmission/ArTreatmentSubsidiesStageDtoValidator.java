package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArTreatmentSubsidiesStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
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
        if (arTreatmentSubsidiesStageDto.getIsThereAppeal() != null && StringUtil.isNotEmpty(arTreatmentSubsidiesStageDto.getCoFunding())){
            doValidateFreshCount(freshCount, frozenCount, errorMap,
                    arTreatmentSubsidiesStageDto.getIsThereAppeal(), arTreatmentSubsidiesStageDto.getCoFunding());
            doValidateFrozenCount(freshCount, frozenCount, errorMap,
                    arTreatmentSubsidiesStageDto.getIsThereAppeal(), arTreatmentSubsidiesStageDto.getCoFunding());
            if (freshCount >= 3 && frozenCount >= 3 && !arTreatmentSubsidiesStageDto.getIsThereAppeal()){
                errorMap.put("isThereAppealShow","Patient has utilised more than 3 co-funded fresh and 3 co-funded frozen ART cycles");
            }
        }
        return errorMap;
    }

    private static void doValidateFreshCount(int freshCount, int frozenCount, Map<String, String> errorMap, Boolean flag, String type){
        if (freshCount >= 3 && frozenCount < 3 && !flag){
            if (DataSubmissionConsts.ART_APPLE_FRESH_THREE.equals(type)){
                errorMap.put("isThereAppealShow","Patient has utilised more than 3 co-funded fresh ART cycles");
            }
        }
    }

    private static void doValidateFrozenCount(int freshCount, int frozenCount, Map<String, String> errorMap, Boolean flag, String type){
        if (freshCount < 3 && frozenCount >= 3 && !flag){
            if (DataSubmissionConsts.ART_APPLE_FROZEN_THREE.equals(type)){
                errorMap.put("isThereAppealShow","Patient has utilised more than 3 co-funded fresh ART cycles");
            }
        }
    }
}
