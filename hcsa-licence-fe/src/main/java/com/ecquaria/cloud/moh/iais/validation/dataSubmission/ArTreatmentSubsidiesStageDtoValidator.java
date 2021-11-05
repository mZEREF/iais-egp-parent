package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArTreatmentSubsidiesStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
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
        //TODO validate isThereAppeal
        return errorMap;
    }
}
