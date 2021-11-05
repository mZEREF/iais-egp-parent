package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EmbryoTransferStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * EmbryoTransferStageDtoValidator
 *
 * @author jiawei
 * @date 11/1/2021
 */

public class EmbryoTransferStageDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(Object obj, String profile, HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        EmbryoTransferStageDto embryoTransferStageDto = (EmbryoTransferStageDto) obj;
        //TODO 1st Date of Transfer and 2nd Date of Transfer (if applicable)•	Cannot be earlier than cycle start date
        if (embryoTransferStageDto.getTransferNum() > 1){
            if (StringUtil.isEmpty(embryoTransferStageDto.getSecondEmbryoAge())){
                String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "Age of 2nd Embryo Transferred", "field");
                errorMap.put("secondEmbryoAge", errMsg);
            }
            if (StringUtil.isEmpty(embryoTransferStageDto.getSecondEmbryoType())){
                String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "Was the 2nd Embryo Transferred a fresh or thawed embryo?", "field");
                errorMap.put("secondEmbryoType", errMsg);
            }
        }
        if (embryoTransferStageDto.getTransferNum() > 2){
            if (StringUtil.isEmpty(embryoTransferStageDto.getThirdEmbryoAge())){
                String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "Age of 3rd Embryo Transferred", "field");
                errorMap.put("thirdEmbryoAge", errMsg);
            }
            if (StringUtil.isEmpty(embryoTransferStageDto.getThirdEmbryoType())){
                String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "Was the 3rd Embryo Transferred a fresh or thawed embryo?", "field");
                errorMap.put("thirdEmbryoType", errMsg);
            }
        }
        return errorMap;
    }
}
