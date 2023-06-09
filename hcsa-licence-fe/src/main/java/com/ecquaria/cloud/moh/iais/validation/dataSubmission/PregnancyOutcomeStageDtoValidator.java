package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PregnancyOutcomeStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * PregnancyOutcomeStageDtoValidator
 *
 * @author jiawei
 * @date 2021/11/2
 */

public class PregnancyOutcomeStageDtoValidator implements CustomizeValidator {
    @SneakyThrows
    @Override
    public Map<String, String> validate(Object obj, String profile, HttpServletRequest request) {
        final String MASTER_FIRST_SINGLETION = "OSIOU001";
        final String MASTER_FIRST_TWIN = "OSIOU002";
        final String MASTER_FIRST_TRIPLET = "OSIOU003";
        final String MASTER_FIRST_ABOVE = "OSIOU004";
        final String MASTER_FIRST_UNKNOWN = "OSIOU005";

        final String MASTER_OUTCOME_LIVE = "OUTOPRE001";
        final String MASTER_OUTCOME_NO_LIVE = "OUTOPRE002";
        final String MASTER_OUTCOME_UNKNOWN = "OUTOPRE003";
        final String MASTER_OUTCOME_OTHERS = "OUTOPRE004";
        final String GENERAL_ERR0006     =  "GENERAL_ERR0006";
        final String GENERAL_ERR0002     =  "GENERAL_ERR0002";

        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        PregnancyOutcomeStageDto pregnancyOutcomeStageDto = (PregnancyOutcomeStageDto) obj;
        String firstUltrasoundOrderShow = pregnancyOutcomeStageDto.getFirstUltrasoundOrderShow();
        String pregnancyOutcome = pregnancyOutcomeStageDto.getPregnancyOutcome();

        int femaleLiveBirthNum = getInt(pregnancyOutcomeStageDto.getFemaleLiveBirthNum());
        int maleLiveBirthNum = getInt(pregnancyOutcomeStageDto.getMaleLiveBirthNum());
        int totalLiveBirth = femaleLiveBirthNum + maleLiveBirthNum;
        if (MASTER_OUTCOME_OTHERS.equals(pregnancyOutcome)) {
            if (StringUtil.isEmpty(pregnancyOutcomeStageDto.getOtherPregnancyOutcome())) {
                errorMap.put("otherPregnancyOutcome",GENERAL_ERR0006);
            }
        }
        if (MASTER_OUTCOME_LIVE.equals(pregnancyOutcome)) {
            if (StringUtil.isEmpty(pregnancyOutcomeStageDto.getDeliveryDateType()) && StringUtil.isEmpty(pregnancyOutcomeStageDto.getDeliveryDate())) {
                errorMap.put("deliveryDate", GENERAL_ERR0006);
            }
            if (StringUtil.isEmpty(pregnancyOutcomeStageDto.getBabyDetailsUnknown())) {
                errorMap.put("babyDetailsUnknown", GENERAL_ERR0006);
            }
            if (StringUtil.isEmpty(pregnancyOutcomeStageDto.getFemaleLiveBirthNum())) {
                errorMap.put("femaleLiveBirthNum", GENERAL_ERR0006);
            }
            if (StringUtil.isEmpty(pregnancyOutcomeStageDto.getMaleLiveBirthNum())) {
                errorMap.put("maleLiveBirthNum", GENERAL_ERR0006);
            }
            if(totalLiveBirth == 0){
                errorMap.put("totalLiveBirthNum", MessageUtil.getMessageDesc("OAPPT_ERR005"));
            }
            if (totalLiveBirth > 1 && MASTER_FIRST_SINGLETION.equals(firstUltrasoundOrderShow)) {
                Map<String, String> params = IaisCommonUtils.genNewHashMap();
                params.put("field1", "No. Live Birth (Total)");
                params.put("field2", "1");
                errorMap.put("totalLiveBirthNum", MessageUtil.getMessageDesc("DS_ERR065", params));
            }
            if (totalLiveBirth > 2 && MASTER_FIRST_TWIN.equals(firstUltrasoundOrderShow)) {
                Map<String, String> params = IaisCommonUtils.genNewHashMap();
                params.put("field1", "No. Live Birth (Total)");
                params.put("field2", "2");
                errorMap.put("totalLiveBirthNum", MessageUtil.getMessageDesc("DS_ERR065", params));
            }
            if (totalLiveBirth > 3 && MASTER_FIRST_TRIPLET.equals(firstUltrasoundOrderShow)) {
                Map<String, String> params = IaisCommonUtils.genNewHashMap();
                params.put("field1", "No. Live Birth (Total)");
                params.put("field2", "3");
                errorMap.put("totalLiveBirthNum", MessageUtil.getMessageDesc("DS_ERR065", params));
            }
        }
        if (MASTER_OUTCOME_NO_LIVE.equals(pregnancyOutcome) || MASTER_OUTCOME_UNKNOWN.equals(pregnancyOutcome) || MASTER_OUTCOME_LIVE.equals(pregnancyOutcome) && !MASTER_FIRST_SINGLETION.equals(firstUltrasoundOrderShow)) {
            if (StringUtil.isEmpty(pregnancyOutcomeStageDto.getStillBirthNum())) {
                errorMap.put("stillBirthNum", GENERAL_ERR0006);
            }
            if (StringUtil.isEmpty(pregnancyOutcomeStageDto.getSpontAbortNum())) {
                errorMap.put("spontAbortNum", GENERAL_ERR0006);
            }
            if (StringUtil.isEmpty(pregnancyOutcomeStageDto.getIntraUterDeathNum())) {
                errorMap.put("intraUterDeathNum", GENERAL_ERR0006);
            }
        }
        if (pregnancyOutcomeStageDto.getNicuCareBabyNum() > totalLiveBirth) {
            Map<String, String> params = IaisCommonUtils.genNewHashMap();
            params.put("field1", "Total No. of Baby Admitted to NICU Care");
            params.put("field2", "Total No. of Live Births");
            errorMap.put("NICUCareBabyNum", MessageUtil.getMessageDesc("DS_ERR065", params));
        }
        if (pregnancyOutcomeStageDto.getL2CareBabyNum() + pregnancyOutcomeStageDto.getL3CareBabyNum() > pregnancyOutcomeStageDto.getNicuCareBabyNum()) {
            Map<String, String> params = IaisCommonUtils.genNewHashMap();
            params.put("field1", "Total sum of No. of Baby Admitted to L2 Care and No. of Baby Admitted to L3 Care");
            params.put("field2", "Total No. of Baby Admitted to NICU Care");
            errorMap.put("l2CareBabyNum", MessageUtil.getMessageDesc("DS_ERR065", params));
            errorMap.put("l3CareBabyNum", MessageUtil.getMessageDesc("DS_ERR065", params));
        }
        if (isNotEmptyAndNotNum(pregnancyOutcomeStageDto.getFemaleLiveBirthNum())) {
            errorMap.put("femaleLiveBirthNum", GENERAL_ERR0002);
        }
        if (isNotEmptyAndNotNum(pregnancyOutcomeStageDto.getMaleLiveBirthNum())) {
            errorMap.put("maleLiveBirthNum", GENERAL_ERR0002);
        }
        if (isNotEmptyAndNotNum(pregnancyOutcomeStageDto.getStillBirthNum())) {
            errorMap.put("stillBirthNum", GENERAL_ERR0002);
        }
        if (isNotEmptyAndNotNum(pregnancyOutcomeStageDto.getSpontAbortNum())) {
            errorMap.put("spontAbortNum", GENERAL_ERR0002);
        }
        if (isNotEmptyAndNotNum(pregnancyOutcomeStageDto.getIntraUterDeathNum())) {
            errorMap.put("intraUterDeathNum", GENERAL_ERR0002);
        }
        if (isNotEmptyAndNotNum(pregnancyOutcomeStageDto.getL2CareBabyDays())) {
            errorMap.put("l2CareBabyDays", GENERAL_ERR0002);
        }
        if (isNotEmptyAndNotNum(pregnancyOutcomeStageDto.getL3CareBabyDays())) {
            errorMap.put("l3CareBabyDays", GENERAL_ERR0002);
        }
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        CycleDto cycle = arSuperDataSubmissionDto.getCycleDto();
        ArDataSubmissionService arDataSubmissionService = SpringContextHelper.getContext().getBean(ArDataSubmissionService.class);
        Date startDate = arDataSubmissionService.getCycleStartDate(cycle.getId());
        if (startDate != null && pregnancyOutcomeStageDto.getDeliveryDate() != null) {
            if (pregnancyOutcomeStageDto.getDeliveryDate().before(startDate)) {
                errorMap.put("deliveryDate", MessageUtil.getMessageDesc("DS_ERR066"));
            }
        }
        return errorMap;
    }

    private int getInt(String s) {
        if (StringUtil.isNumber(s)) {
            return Integer.parseInt(s);
        }
        return 0;
    }

    private boolean isNotEmptyAndNotNum(String s) {
        return StringUtil.isNotEmpty(s) && (!StringUtil.isNumber(s));
    }
}
