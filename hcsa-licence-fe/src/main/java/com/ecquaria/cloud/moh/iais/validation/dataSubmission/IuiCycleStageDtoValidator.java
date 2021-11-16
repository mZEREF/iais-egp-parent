package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.IuiCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2021/11/12 15:42
 **/
public class IuiCycleStageDtoValidator implements CustomizeValidator {

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(request);
        if(arSuperDataSubmission != null) {
            IuiCycleStageDto iuiCycleStageDto = arSuperDataSubmission.getIuiCycleStageDto();
            if(iuiCycleStageDto != null) {
                Date iuiCycleStartDate = iuiCycleStageDto.getStartDate();
            }
        }
        if(errMap.isEmpty()) {
            return null;
        }
        return errMap;
    }
}
