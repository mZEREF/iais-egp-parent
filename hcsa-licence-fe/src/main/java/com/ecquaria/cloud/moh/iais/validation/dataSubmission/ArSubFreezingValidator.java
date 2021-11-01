package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSubFreezingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

/**
 * @author Shicheng
 * @date 2021/11/1 16:11
 **/
public class ArSubFreezingValidator implements CustomizeValidator {

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String ,String> map= IaisCommonUtils.genNewHashMap();
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(request);
        if(arSuperDataSubmission != null) {
            ArSubFreezingStageDto arSubFreezingStageDto = arSuperDataSubmission.getArSubFreezingStageDto();
            if(arSubFreezingStageDto != null) {
                String cryopreservedNum = arSubFreezingStageDto.getCryopreservedNum() + "";
                Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
                if (StringUtil.isEmpty(cryopreservedNum)){
                    return null;
                }
                if (cryopreservedNum.length() > 2) {
                    errMap.put("cryopreservedNum", "DS_ERR009");
                }
                Pattern pattern = compile("[0-9]*");
                boolean hoursFlag = pattern.matcher(cryopreservedNum).matches();
                if (!hoursFlag) {
                    errMap.put("cryopreservedNum", "GENERAL_ERR0002");
                }
            }
        }
        return map;
    }
}
