package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.OocyteRetrievalStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
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
        if (StringUtil.isNotEmpty(oocyteRetrievalStageDto.getMatureRetrievedNum()) &&
                (!StringUtil.isNumber(oocyteRetrievalStageDto.getMatureRetrievedNum()))) {
            errorMap.put("matureRetrievedNum", MessageUtil.getMessageDesc("GENERAL_ERR0002"));
        }
        if (StringUtil.isNotEmpty(oocyteRetrievalStageDto.getImmatureRetrievedNum()) &&
                (!StringUtil.isNumber(oocyteRetrievalStageDto.getImmatureRetrievedNum()))) {
            errorMap.put("immatureRetrievedNum", MessageUtil.getMessageDesc("GENERAL_ERR0002"));
        }
        if (StringUtil.isNotEmpty(oocyteRetrievalStageDto.getOtherRetrievedNum()) &&
                (!StringUtil.isNumber(oocyteRetrievalStageDto.getOtherRetrievedNum()))) {
            errorMap.put("otherRetrievedNum", MessageUtil.getMessageDesc("GENERAL_ERR0002"));
        }
        if (!(oocyteRetrievalStageDto.getIsFromPatient() || oocyteRetrievalStageDto.getIsFromPatientTissue()
                || oocyteRetrievalStageDto.getIsFromDonor() || oocyteRetrievalStageDto.getIsFromDonorTissue()
                || oocyteRetrievalStageDto.getIsNoDirectedDonor() || oocyteRetrievalStageDto.getIsNoDirectedDonorTissue())) {
            String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006", "Oocyte(s) was retrieved from", "field");
            errorMap.put("oocyteRetrievalFrom", errMsg);
        }
        if ((oocyteRetrievalStageDto.getIsFromPatient() || oocyteRetrievalStageDto.getIsFromPatientTissue())
                && (oocyteRetrievalStageDto.getIsFromDonor() || oocyteRetrievalStageDto.getIsFromDonorTissue())) {
            Map<String, String> repMap = IaisCommonUtils.genNewHashMap(2);
            repMap.put("0", "Patient");
            repMap.put("1", "Directed Donor");
            String errMsg = MessageUtil.getMessageDesc("DS_ERR008", repMap);
            errorMap.put("oocyteRetrievalFrom", errMsg);
        }
        return errorMap;
    }
}
