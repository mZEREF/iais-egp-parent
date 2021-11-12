package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PregnancyOutcomeStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * PregnancyOutcomeStageDtoValidator
 *
 * @author jiawei
 * @date 2021/11/2
 */

public class PregnancyOutcomeStageDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(Object obj, String profile, HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        PregnancyOutcomeStageDto pregnancyOutcomeStageDto = (PregnancyOutcomeStageDto) obj;
        String firstUltrasoundOrderShow = pregnancyOutcomeStageDto.getFirstUltrasoundOrderShow();
        String pregnancyOutcome = pregnancyOutcomeStageDto.getPregnancyOutcome();
        if ("OUTOPRE004".equals(pregnancyOutcome)) {
            if (StringUtil.isEmpty(pregnancyOutcomeStageDto.getOtherPregnancyOutcome())) {
                errorMap.put("otherPregnancyOutcome", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            }
        }
        if ("OUTOPRE001".equals(pregnancyOutcome)) {
            if (StringUtil.isEmpty(pregnancyOutcomeStageDto.getDeliveryDateType()) && StringUtil.isEmpty(pregnancyOutcomeStageDto.getDeliveryDate())) {
                errorMap.put("deliveryDate", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            }
            if (StringUtil.isEmpty(pregnancyOutcomeStageDto.getBabyDetailsUnknown())) {
                errorMap.put("babyDetailsUnknown", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            }
            if (pregnancyOutcomeStageDto.getFemaleLiveBirthNum() + pregnancyOutcomeStageDto.getMaleLiveBirthNum() < 1) {
                errorMap.put("maleLiveBirthNum", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
                errorMap.put("femaleLiveBirthNum", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            }
        }
        if ("OUTOPRE002".equals(pregnancyOutcome)) {
            if (pregnancyOutcomeStageDto.getStillBirthNum() + pregnancyOutcomeStageDto.getSpontAbortNum() + pregnancyOutcomeStageDto.getIntraUterDeathNum() < 1) {
                errorMap.put("stillBirthNum", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
                errorMap.put("spontAbortNum", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
                errorMap.put("intraUterDeathNum", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            }
        }
        if (pregnancyOutcomeStageDto.getNicuCareBabyNum() > pregnancyOutcomeStageDto.getFemaleLiveBirthNum() + pregnancyOutcomeStageDto.getMaleLiveBirthNum()) {
            errorMap.put("NICUCareBabyNum", MessageUtil.getMessageDesc("The filed cannot be greater than Total No. of Live Births"));
        }
        if (pregnancyOutcomeStageDto.getL2CareBabyDays() + pregnancyOutcomeStageDto.getL3CareBabyDays() > pregnancyOutcomeStageDto.getNicuCareBabyNum()) {
            errorMap.put("l2CareBabyNum", MessageUtil.getMessageDesc("Total sum of No. of Baby Admitted to L2 Care and No. of Baby Admitted to L3 Care cannot be greater than Total No. of Baby Admitted to NICU Care"));
            errorMap.put("l3CareBabyNum", MessageUtil.getMessageDesc("Total sum of No. of Baby Admitted to L2 Care and No. of Baby Admitted to L3 Care cannot be greater than Total No. of Baby Admitted to NICU Care"));
        }
        return errorMap;
    }
}
