package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EmbryoTransferStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Map;

/**
 * EmbryoTransferStageDtoValidator
 *
 * @author jiawei
 * @date 11/1/2021
 */

public class EmbryoTransferStageDtoValidator implements CustomizeValidator {
    @SneakyThrows
    @Override
    public Map<String, String> validate(Object obj, String profile, HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        EmbryoTransferStageDto embryoTransferStageDto = (EmbryoTransferStageDto) obj;
        if (embryoTransferStageDto.getTransferNum() > 1) {
            if (StringUtil.isEmpty(embryoTransferStageDto.getSecondEmbryoAge())) {
                String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "Age of 2nd Embryo Transferred", "field");
                errorMap.put("secondEmbryoAge", errMsg);
            }
            if (StringUtil.isEmpty(embryoTransferStageDto.getSecondEmbryoType())) {
                String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "Was the 2nd Embryo Transferred a fresh or thawed embryo?", "field");
                errorMap.put("secondEmbryoType", errMsg);
            }
        }
        if (embryoTransferStageDto.getTransferNum() > 2) {
            if (StringUtil.isEmpty(embryoTransferStageDto.getThirdEmbryoAge())) {
                String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "Age of 3rd Embryo Transferred", "field");
                errorMap.put("thirdEmbryoAge", errMsg);
            }
            if (StringUtil.isEmpty(embryoTransferStageDto.getThirdEmbryoType())) {
                String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "Was the 3rd Embryo Transferred a fresh or thawed embryo?", "field");
                errorMap.put("thirdEmbryoType", errMsg);
            }
        }
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        ArCycleStageDto arCycleStageDto = arSuperDataSubmissionDto.getArCycleStageDto();
        if (arCycleStageDto != null && StringUtil.isNotEmpty(arCycleStageDto.getStartDate())) {
            if (embryoTransferStageDto.getFirstTransferDate().before(new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT).parse(arCycleStageDto.getStartDate()))) {
                errorMap.put("firstTransferDate", "Cannot be earlier than cycle start date");
            }
            if (embryoTransferStageDto.getSecondTransferDate().before(new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT).parse(arCycleStageDto.getStartDate()))) {
                errorMap.put("secondTransferDate", "Cannot be earlier than cycle start date");
            }
        }
        return errorMap;
    }
}
