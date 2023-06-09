package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCurrentInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ThawingStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.CommonValidator;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * ThawingStageDtoValidator
 *
 * @author jiawei
 * @date 2021/10/27
 */

public class ThawingStageDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(Object obj, String profile, HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ThawingStageDto thawingStageDto = (ThawingStageDto) obj;
        if (!(thawingStageDto.getHasEmbryo() || thawingStageDto.getHasOocyte())) {
            String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "Thawing Oocyte(s) or Embryo(s)", "field");
            errorMap.put("thawings", errMsg);
        }
        if (thawingStageDto.getHasEmbryo()) {
            if (StringUtil.isEmpty(thawingStageDto.getThawedEmbryosSurvivedNum())) {
                String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "No. of Embryos Survived after Thawing", "field");
                errorMap.put("thawedEmbryosSurvivedNum", errMsg);
            } else if (!StringUtil.isNumber(thawingStageDto.getThawedEmbryosSurvivedNum())) {
                errorMap.put("thawedEmbryosSurvivedNum", MessageUtil.getMessageDesc("GENERAL_ERR0002"));
            }
            if (StringUtil.isEmpty(thawingStageDto.getThawedEmbryosNum())) {
                String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "No. of Embryos Thawed", "field");
                errorMap.put("thawedEmbryosNum", errMsg);
            } else if (!StringUtil.isNumber(thawingStageDto.getThawedEmbryosNum())) {
                errorMap.put("thawedEmbryosNum", MessageUtil.getMessageDesc("GENERAL_ERR0002"));
            }
        }
        if (thawingStageDto.getHasOocyte()) {
            if (StringUtil.isEmpty(thawingStageDto.getThawedOocytesNum())) {
                String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "No. of Oocytes Thawed", "field");
                errorMap.put("thawedOocytesNum", errMsg);
            } else if (!StringUtil.isNumber(thawingStageDto.getThawedOocytesNum())) {
                errorMap.put("thawedOocytesNum", MessageUtil.getMessageDesc("GENERAL_ERR0002"));
            }
            if (StringUtil.isEmpty(thawingStageDto.getThawedOocytesSurvivedMatureNum())) {
                String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "No. of Oocytes Survived after Thawing (Mature)", "field");
                errorMap.put("thawedOocytesSurvivedMatureNum", errMsg);
            } else if (!StringUtil.isNumber(thawingStageDto.getThawedOocytesSurvivedMatureNum())) {
                errorMap.put("thawedOocytesSurvivedMatureNum", MessageUtil.getMessageDesc("GENERAL_ERR0002"));
            }
            if (StringUtil.isEmpty(thawingStageDto.getThawedOocytesSurvivedImmatureNum())) {
                String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "No. of Oocytes Survived after Thawing (Immature)", "field");
                errorMap.put("thawedOocytesSurvivedImmatureNum", errMsg);
            } else if (!StringUtil.isNumber(thawingStageDto.getThawedOocytesSurvivedImmatureNum())) {
                errorMap.put("thawedOocytesSurvivedImmatureNum", MessageUtil.getMessageDesc("GENERAL_ERR0002"));
            }
            if (StringUtil.isEmpty(thawingStageDto.getThawedOocytesSurvivedOtherNum())) {
                String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "No. of Oocytes Survived after Thawing (Others)", "field");
                errorMap.put("thawedOocytesSurvivedOtherNum", errMsg);
            } else if (!StringUtil.isNumber(thawingStageDto.getThawedOocytesSurvivedOtherNum())) {
                errorMap.put("thawedOocytesSurvivedOtherNum", MessageUtil.getMessageDesc("GENERAL_ERR0002"));
            }
        }
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = DataSubmissionHelper.getCurrentArDataSubmission(request);
        ArCurrentInventoryDto arCurrentInventoryDto = arSuperDataSubmissionDto.getArCurrentInventoryDto();

        int thawedOocytesNumInt = toInt(thawingStageDto.getThawedOocytesNum());
        int hawedEmbryosNumInt = toInt(thawingStageDto.getThawedEmbryosNum());
        int thawedOocytesSurvivedMatureNumInt = toInt(thawingStageDto.getThawedOocytesSurvivedMatureNum());
        int thawedOocytesSurvivedImmatureNumInt = toInt(thawingStageDto.getThawedOocytesSurvivedImmatureNum());
        int thawedOocytesSurvivedOtherNumInt = toInt(thawingStageDto.getThawedOocytesSurvivedOtherNum());
        int thawedEmbryosSurvivedNumInt = toInt(thawingStageDto.getThawedEmbryosSurvivedNum());
        if (arCurrentInventoryDto != null) {
            if (thawedOocytesNumInt > arCurrentInventoryDto.getFrozenOocyteNum()) {
                Map<String,String> params = IaisCommonUtils.genNewHashMap();
                params.put("item","No. of Oocytes Thawed");
                params.put("inventory","frozen oocytes");
                errorMap.put("thawedOocytesNum", MessageUtil.getMessageDesc("DS_ERR060",params));
            }
            if (hawedEmbryosNumInt > arCurrentInventoryDto.getFrozenEmbryoNum()) {
                Map<String,String> params = IaisCommonUtils.genNewHashMap();
                params.put("item","No. of Oocytes Embryos");
                params.put("inventory","frozen embryos");
                errorMap.put("thawedEmbryosNum", MessageUtil.getMessageDesc("DS_ERR060",params));
            }
        }

        if ((thawedOocytesSurvivedMatureNumInt + thawedOocytesSurvivedImmatureNumInt
                + thawedOocytesSurvivedOtherNumInt) > thawedOocytesNumInt) {
            errorMap.put("thawedOocytesNum", "DS_ERR064");
        }
        if (thawedEmbryosSurvivedNumInt > hawedEmbryosNumInt) {
            errorMap.put("thawedEmbryosNum", "Survived Thawing cannot be greater than No. Embryo");
        }
        return errorMap;
    }

    private int toInt(String str){
        return StringUtil.isNotEmpty(str) && CommonValidator.isPositiveInteger(str)?Integer.parseInt(str):0;
    }
}
