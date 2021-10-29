package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EmbryoTransferStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public class EmbryoTransferStageDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(Object obj, String[] profiles, HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        EmbryoTransferStageDto embryoTransferStageDto = (EmbryoTransferStageDto) obj;
        return errorMap;
    }
}
