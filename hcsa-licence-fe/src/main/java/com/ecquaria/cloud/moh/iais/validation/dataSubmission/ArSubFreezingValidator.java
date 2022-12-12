package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCurrentInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSubFreezingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2021/11/1 16:11
 **/
public class ArSubFreezingValidator implements CustomizeValidator {

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(request);
        if (arSuperDataSubmission != null) {
            ArSubFreezingStageDto arSubFreezingStageDto = arSuperDataSubmission.getArSubFreezingStageDto();
            if (arSubFreezingStageDto != null) {
                if (!"1".equals(arSubFreezingStageDto.getIsFreshEmbryo())  && !"1".equals(arSubFreezingStageDto.getIsThawedEmbryo())
                        && !"1".equals(arSubFreezingStageDto.getIsFreshOocyte()) && !"1".equals(arSubFreezingStageDto.getIsThawedOocyte())) {
                    errMap.put("cryopreservedType", "GENERAL_ERR0006");
                }
                if ("1".equals(arSubFreezingStageDto.getIsFreshOocyte()) && StringUtil.isEmpty(arSubFreezingStageDto.getFreshOocyteCryopNum())) {
                    errMap.put("freshOocyteCryopNum", "GENERAL_ERR0006");
                }
                if ("1".equals(arSubFreezingStageDto.getIsThawedOocyte()) && StringUtil.isEmpty(arSubFreezingStageDto.getThawedOocyteCryopNum())){
                    errMap.put("thawedOocyteCryopNum", "GENERAL_ERR0006");
                }
                if ("1".equals(arSubFreezingStageDto.getIsFreshEmbryo()) && StringUtil.isEmpty(arSubFreezingStageDto.getFreshEmbryoCryopNum())) {
                    errMap.put("freshEmbryoCryopNum", "GENERAL_ERR0006");
                }
                if ("1".equals(arSubFreezingStageDto.getIsThawedEmbryo()) && StringUtil.isEmpty(arSubFreezingStageDto.getThawedEmbryoCryopNum())) {
                    errMap.put("thawedEmbryoCryopNum", "GENERAL_ERR0006");
                }
                //Cannot be greater than number of fresh oocytes or fresh embryos under patient's inventory currently
                ArCurrentInventoryDto arCurrentInventoryDto = arSuperDataSubmission.getArCurrentInventoryDto();
                if (arCurrentInventoryDto != null) {
                    if ("1".equals(arSubFreezingStageDto.getIsFreshOocyte()) && toInt(arSubFreezingStageDto.getFreshOocyteCryopNum()) && Integer.parseInt(arSubFreezingStageDto.getFreshOocyteCryopNum()) > arCurrentInventoryDto.getFreshOocyteNum()) {
                        errMap.put("freshOocyteCryopNum", "Cannot be greater than number of fresh oocytes under patient's inventory currently");
                    }
                    if ("1".equals(arSubFreezingStageDto.getIsThawedOocyte()) && toInt(arSubFreezingStageDto.getThawedOocyteCryopNum()) && Integer.parseInt(arSubFreezingStageDto.getThawedOocyteCryopNum()) > arCurrentInventoryDto.getThawedOocyteNum()) {
                        errMap.put("thawedOocyteCryopNum", "Cannot be greater than number thawed oocytes under patient's inventory currently");
                    }
                    if ("1".equals(arSubFreezingStageDto.getIsFreshEmbryo()) && toInt(arSubFreezingStageDto.getFreshEmbryoCryopNum()) && Integer.parseInt(arSubFreezingStageDto.getFreshEmbryoCryopNum()) > arCurrentInventoryDto.getFreshEmbryoNum()) {
                        errMap.put("freshEmbryoCryopNum", "Cannot be greater than number of fresh Embryos under patient's inventory currently");
                    }
                    if ("1".equals(arSubFreezingStageDto.getIsThawedEmbryo()) && toInt(arSubFreezingStageDto.getThawedEmbryoCryopNum()) && Integer.parseInt(arSubFreezingStageDto.getThawedEmbryoCryopNum()) > arCurrentInventoryDto.getThawedEmbryoNum()) {
                        errMap.put("thawedEmbryoCryopNum", "Cannot be greater than number of thawed embryos under patient's inventory currently");
                    }
                }
            }
        }
        return errMap;
    }

    private boolean toInt(String str) {
        return StringUtil.isNotEmpty(str) && CommonValidator.isPositiveInteger(str);
    }
}
