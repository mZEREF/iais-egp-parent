package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.OocyteRetrievalStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
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
public class OocyteRetrievalDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(Object obj, String profile, HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        OocyteRetrievalStageDto oocyteRetrievalStageDto = (OocyteRetrievalStageDto) obj;
        if (!(oocyteRetrievalStageDto.getIsFromPatient() || oocyteRetrievalStageDto.getIsFromPatientTissue()
                || oocyteRetrievalStageDto.getIsFromDonor() || oocyteRetrievalStageDto.getIsFromDonorTissue())) {
            String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "Oocyte(s) was retrieved from", "field");
            errorMap.put("oocyteRetrievalFrom", errMsg);
        }
        if ((oocyteRetrievalStageDto.getIsFromPatient() || oocyteRetrievalStageDto.getIsFromPatientTissue())
                && (oocyteRetrievalStageDto.getIsFromDonor() || oocyteRetrievalStageDto.getIsFromDonorTissue())) {
            Map<String, String> repMap = IaisCommonUtils.genNewHashMap(2);
            repMap.put("0", "Patient");
            repMap.put("1", "Directed Donor");
            String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "Oocyte(s) was retrieved from", "field");
            errorMap.put("oocyteRetrievalFrom", errMsg);
        }
        //TODO Directed Donor is checked if 'Is this a Directed Donation' is 'Yes'
        return errorMap;
    }
}
