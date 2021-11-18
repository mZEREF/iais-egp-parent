package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.IuiTreatmentSubsidiesDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.constant.DataSubmissionConstant;
import com.ecquaria.cloud.moh.iais.service.client.ArFeClient;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * EmbryoTransferStageDtoValidator
 *
 * @author zhixing
 * @date 11/16/2021
 */

public class IuiTreatmentSubsidiesDtoValidator implements CustomizeValidator {
    private ArFeClient arFeClient;

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(request, DataSubmissionConstant.AR_DATA_SUBMISSION);
        CycleDto cycleDto = arSuperDataSubmissionDto.getCycleDto();
        List<IuiTreatmentSubsidiesDto> oldIuiTreatmentSubsidiesDtos = arFeClient.getIuiTreatmentSubsidiesDtosByPhc(cycleDto.getPatientCode(), cycleDto.getHciCode(), cycleDto.getCycleType()).getEntity();
        int count=oldIuiTreatmentSubsidiesDtos.size();

        if(count>=3){
            errorMap.put("artCoFunding" ,"The total number of IUI subsidies entered by patients in the system is 3");
        }
        return errorMap;
    }
}
