package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ThawingStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class ThawingStageDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(Object obj, String[] profiles, HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ThawingStageDto thawingStageDto = (ThawingStageDto) obj;
        if (!(thawingStageDto.getHasEmbryo() || thawingStageDto.getHasOocyte())){
            String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","Thawing Oocyte(s) or Embryo(s)", "field");
            errorMap.put("thawings",errMsg);
        }
        return errorMap;
    }
}
