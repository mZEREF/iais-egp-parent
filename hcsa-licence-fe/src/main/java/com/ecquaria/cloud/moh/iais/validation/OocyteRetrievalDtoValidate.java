package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.OocyteRetrievalStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * OocyteRetrievalDtoValidate
 *
 * @author jiawei
 * @date 2021/10/22
 */

@Component
@Slf4j
public class OocyteRetrievalDtoValidate implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ArSuperDataSubmissionDto arSuperDataSubmissionDto = (ArSuperDataSubmissionDto) ParamUtil.getSessionAttr(request, "arSuperDataSubmissionDto");
        OocyteRetrievalStageDto oocyteRetrievalStageDto = arSuperDataSubmissionDto.getOocyteRetrievalStageDto();
        if (oocyteRetrievalStageDto != null){
            if (!(oocyteRetrievalStageDto.getIsFromPatient() || oocyteRetrievalStageDto.getIsFromPatientTissue()
                    || oocyteRetrievalStageDto.getIsFromDonor() || oocyteRetrievalStageDto.getIsFromDonorTissue())){
                String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","Oocyte(s) was retrieved from", "field");
                errorMap.put("oocyteRetrievalFrom",errMsg);
            }
        }
        return errorMap;
    }
}
