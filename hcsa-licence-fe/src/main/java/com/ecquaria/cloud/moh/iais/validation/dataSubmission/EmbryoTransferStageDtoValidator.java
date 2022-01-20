package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCurrentInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EmbryoTransferStageDto;
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
        CycleDto cycleDto = arSuperDataSubmissionDto.getCycleDto();
        String cycleId = cycleDto.getId();
        ArDataSubmissionService arDataSubmissionService = SpringContextHelper.getContext().getBean(ArDataSubmissionService.class);
        Date startDate = arDataSubmissionService.getCycleStartDate(cycleId);
        if (startDate != null) {
            if (embryoTransferStageDto.getFirstTransferDate() != null && embryoTransferStageDto.getFirstTransferDate().before(startDate)) {
                errorMap.put("firstTransferDate", "Cannot be earlier than cycle start date");
            }
            if (embryoTransferStageDto.getSecondTransferDate() != null && embryoTransferStageDto.getSecondTransferDate().before(startDate)) {
                errorMap.put("secondTransferDate", "Cannot be earlier than cycle start date");
            }
        }

        int freshEmbryoNum = 0;
        int thawedEmbryoNum = 0;
        if (DataSubmissionConsts.EMBRYO_TRANSFER_EMBRYO_TYPE_FRESH.equals(embryoTransferStageDto.getFirstEmbryoType())) {
            freshEmbryoNum++;
        } else if (DataSubmissionConsts.EMBRYO_TRANSFER_EMBRYO_TYPE_THAWED.equals(embryoTransferStageDto.getFirstEmbryoType())) {
            thawedEmbryoNum++;
        }
        if (DataSubmissionConsts.EMBRYO_TRANSFER_EMBRYO_TYPE_FRESH.equals(embryoTransferStageDto.getSecondEmbryoType())) {
            freshEmbryoNum++;
        } else if (DataSubmissionConsts.EMBRYO_TRANSFER_EMBRYO_TYPE_THAWED.equals(embryoTransferStageDto.getSecondEmbryoType())) {
            thawedEmbryoNum++;
        }
        if (DataSubmissionConsts.EMBRYO_TRANSFER_EMBRYO_TYPE_FRESH.equals(embryoTransferStageDto.getThirdEmbryoType())) {
            freshEmbryoNum++;
        } else if (DataSubmissionConsts.EMBRYO_TRANSFER_EMBRYO_TYPE_THAWED.equals(embryoTransferStageDto.getThirdEmbryoType())) {
            thawedEmbryoNum++;
        }
        ArCurrentInventoryDto arCurrentInventoryDto = arSuperDataSubmissionDto.getArCurrentInventoryDto();
        if (freshEmbryoNum > arCurrentInventoryDto.getFreshEmbryoNum()) {
            errorMap.put("FreshEmbryosNum", "No. of Fresh Embryos cannot be greater than total number of fresh Embryos tagged patient");
        }
        if (thawedEmbryoNum > arCurrentInventoryDto.getThawedEmbryoNum()) {
            errorMap.put("thawedEmbryosNum", "No. of Thawed Embryos cannot be greater than total number of thawed Embryos tagged patient");
        }
        return errorMap;
    }
}
