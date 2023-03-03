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
        final String GENERAL_ERR0002 = "GENERAL_ERR0002";
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        OocyteRetrievalStageDto oocyteRetrievalStageDto = (OocyteRetrievalStageDto) obj;
        if (StringUtil.isNotEmpty(oocyteRetrievalStageDto.getMatureRetrievedNum()) &&
                (!StringUtil.isNumber(oocyteRetrievalStageDto.getMatureRetrievedNum()))) {
            errorMap.put("matureRetrievedNum", GENERAL_ERR0002);
        }
        if (StringUtil.isNotEmpty(oocyteRetrievalStageDto.getImmatureRetrievedNum()) &&
                (!StringUtil.isNumber(oocyteRetrievalStageDto.getImmatureRetrievedNum()))) {
            errorMap.put("immatureRetrievedNum", GENERAL_ERR0002);
        }
        if (StringUtil.isNotEmpty(oocyteRetrievalStageDto.getOtherRetrievedNum()) &&
                (!StringUtil.isNumber(oocyteRetrievalStageDto.getOtherRetrievedNum()))) {
            errorMap.put("otherRetrievedNum", GENERAL_ERR0002);
        }

        return errorMap;
    }
}
