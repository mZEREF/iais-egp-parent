package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.service.datasubmission.ArDataSubmissionService;
import lombok.SneakyThrows;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
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
        Map<String, String> eMsg = IaisCommonUtils.genNewHashMap();
        eMsg.put("field","donation");
        String errMsg002 = MessageUtil.getMessageDesc("DS_ERR002",eMsg);
        EmbryoTransferStageDto embryoTransferStageDto = (EmbryoTransferStageDto) obj;
        List<EmbryoTransferDetailDto> embryoTransferDetailDtos =  embryoTransferStageDto.getEmbryoTransferDetailDtos();
        int detailNum = embryoTransferStageDto.getTransferNum();
        for (int i = 1; i <= detailNum; i++) {
            if (StringUtil.isEmpty(embryoTransferDetailDtos.get(i-1).getEmbryoAge())) {
                String errMsg = null;
                if (i == 1) {
                    errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","1st Embryo", "field");
                } else if (i==2) {
                    errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","2nd Embryo", "field");
                } else if (i == 3) {
                    errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","3rd Embryo", "field");
                } else {
                    errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006",i +"th Embryo", "field");
                }
                errorMap.put(i+"EmbryoAge",errMsg);
            }
            if (StringUtil.isEmpty(embryoTransferDetailDtos.get(i-1).getEmbryoType())) {
                String errMsg = null;
                if (i == 1) {
                    errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "Was the 1st Embryo Transferred a fresh or thawed embryo?", "field");
                } else if (i == 2) {
                    errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "Was the 2nd Embryo Transferred a fresh or thawed embryo?", "field");
                } else if (i == 3) {
                    errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "Was the 3rd Embryo Transferred a fresh or thawed embryo?", "field");
                } else {
                    errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "Was the " + i + "th Embryo Transferred a fresh or thawed embryo?", "field");
                }
                errorMap.put(i + "EmbryoType", errMsg);
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
        for (int i =0;i < detailNum; i++) {
            if (DataSubmissionConsts.EMBRYO_TRANSFER_EMBRYO_TYPE_FRESH.equals(embryoTransferDetailDtos.get(i).getEmbryoType())) {
                freshEmbryoNum++;
            } else if (DataSubmissionConsts.EMBRYO_TRANSFER_EMBRYO_TYPE_THAWED.equals(embryoTransferDetailDtos.get(i).getEmbryoType())) {
                thawedEmbryoNum++;
            }
        }
        ArCurrentInventoryDto arCurrentInventoryDto = arSuperDataSubmissionDto.getArCurrentInventoryDto();
        if (freshEmbryoNum > arCurrentInventoryDto.getFreshEmbryoNum()) {
            errorMap.put("FreshEmbryosNum", errMsg002);
        }
        if (thawedEmbryoNum > arCurrentInventoryDto.getThawedEmbryoNum()) {
            errorMap.put("thawedEmbryosNum", errMsg002);
        }
        return errorMap;
    }
}
