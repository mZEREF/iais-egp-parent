package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.IuiCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PregnancyOutcomeStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
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
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        PregnancyOutcomeStageDto pregnancyOutcomeStageDto = (PregnancyOutcomeStageDto) obj;
        String firstUltrasoundOrderShow = pregnancyOutcomeStageDto.getFirstUltrasoundOrderShow();
        String pregnancyOutcome = pregnancyOutcomeStageDto.getPregnancyOutcome();

        int femaleLiveBirthNum = getInt(pregnancyOutcomeStageDto.getFemaleLiveBirthNum());
        int maleLiveBirthNum = getInt(pregnancyOutcomeStageDto.getMaleLiveBirthNum());
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
            if (femaleLiveBirthNum + maleLiveBirthNum < 1) {
                errorMap.put("maleLiveBirthNum", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
                errorMap.put("femaleLiveBirthNum", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            }
        }
        if ("OUTOPRE002".equals(pregnancyOutcome)) {
            int stillBirthNum = getInt(pregnancyOutcomeStageDto.getStillBirthNum());
            int spontAbortNum = getInt(pregnancyOutcomeStageDto.getSpontAbortNum());
            int intraUterDeathNum = getInt(pregnancyOutcomeStageDto.getIntraUterDeathNum());
            if (stillBirthNum + spontAbortNum + intraUterDeathNum < 1) {
                errorMap.put("stillBirthNum", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
                errorMap.put("spontAbortNum", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
                errorMap.put("intraUterDeathNum", MessageUtil.getMessageDesc("GENERAL_ERR0006"));
            }
        }
        if (pregnancyOutcomeStageDto.getNicuCareBabyNum() > femaleLiveBirthNum + maleLiveBirthNum) {
            errorMap.put("NICUCareBabyNum", MessageUtil.getMessageDesc("The filed cannot be greater than Total No. of Live Births"));
        }
        if (pregnancyOutcomeStageDto.getL2CareBabyNum() + pregnancyOutcomeStageDto.getL3CareBabyNum() > pregnancyOutcomeStageDto.getNicuCareBabyNum()) {
            errorMap.put("l2CareBabyNum", MessageUtil.getMessageDesc("Total sum of No. of Baby Admitted to L2 Care and No. of Baby Admitted to L3 Care cannot be greater than Total No. of Baby Admitted to NICU Care"));
            errorMap.put("l3CareBabyNum", MessageUtil.getMessageDesc("Total sum of No. of Baby Admitted to L2 Care and No. of Baby Admitted to L3 Care cannot be greater than Total No. of Baby Admitted to NICU Care"));
        }
        if (isNotEmptyAndNotNum(pregnancyOutcomeStageDto.getFemaleLiveBirthNum())) {
            errorMap.put("femaleLiveBirthNum", MessageUtil.getMessageDesc("GENERAL_ERR0002"));
        }
        if (isNotEmptyAndNotNum(pregnancyOutcomeStageDto.getMaleLiveBirthNum())) {
            errorMap.put("maleLiveBirthNum", MessageUtil.getMessageDesc("GENERAL_ERR0002"));
        }
        if (isNotEmptyAndNotNum(pregnancyOutcomeStageDto.getStillBirthNum())) {
            errorMap.put("stillBirthNum", MessageUtil.getMessageDesc("GENERAL_ERR0002"));
        }
        if (isNotEmptyAndNotNum(pregnancyOutcomeStageDto.getSpontAbortNum())) {
            errorMap.put("spontAbortNum", MessageUtil.getMessageDesc("GENERAL_ERR0002"));
        }
        if (isNotEmptyAndNotNum(pregnancyOutcomeStageDto.getIntraUterDeathNum())) {
            errorMap.put("intraUterDeathNum", MessageUtil.getMessageDesc("GENERAL_ERR0002"));
        }
        if (isNotEmptyAndNotNum(pregnancyOutcomeStageDto.getL2CareBabyDays())) {
            errorMap.put("l2CareBabyDays", MessageUtil.getMessageDesc("GENERAL_ERR0002"));
        }
        if (isNotEmptyAndNotNum(pregnancyOutcomeStageDto.getL3CareBabyDays())) {
            errorMap.put("l2CareBabyDays", MessageUtil.getMessageDesc("GENERAL_ERR0002"));
        }
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        CycleDto cycle = arSuperDataSubmissionDto.getCycleDto();
        String cycleType = "";
        Date cycleStartDate = null;
        if (cycle != null) {
            cycleType = cycle.getCycleType();
        }
        if (DataSubmissionConsts.DS_CYCLE_AR.equals(cycleType)) {
            ArCycleStageDto arCycleStageDto = arSuperDataSubmissionDto.getArCycleStageDto();
            if (arCycleStageDto != null) {
                cycleStartDate = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT).parse(arCycleStageDto.getStartDate());
            }
        } else if (DataSubmissionConsts.DS_CYCLE_IUI.equals(cycleType)) {
            IuiCycleStageDto iuiCycleStageDto = arSuperDataSubmissionDto.getIuiCycleStageDto();
            if (iuiCycleStageDto != null) {
                cycleStartDate = iuiCycleStageDto.getStartDate();
            }
        }
        if (cycleStartDate != null && pregnancyOutcomeStageDto.getDeliveryDate() != null) {
            if (pregnancyOutcomeStageDto.getDeliveryDate().before(cycleStartDate)) {
                errorMap.put("deliveryDate", "Cannot be earlier than cycle start date");
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
