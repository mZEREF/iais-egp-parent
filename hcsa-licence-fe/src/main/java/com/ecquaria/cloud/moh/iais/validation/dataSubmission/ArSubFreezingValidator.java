package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSubFreezingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
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
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        ArSuperDataSubmissionDto arSuperDataSubmission = DataSubmissionHelper.getCurrentArDataSubmission(request);
        if(arSuperDataSubmission != null) {
            ArSubFreezingStageDto arSubFreezingStageDto = arSuperDataSubmission.getArSubFreezingStageDto();
            if(arSubFreezingStageDto != null) {
                String cryopreservedNum = arSubFreezingStageDto.getCryopreservedNum() + "";
                Date cryopreservedDate = arSubFreezingStageDto.getCryopreservedDate();
                if(cryopreservedDate != null) {
                    if(cryopreservedDate.after(new Date())) {
                        errMap.put("cryopreservedDate", MessageUtil.replaceMessage("DS_ERR010", "Cryopreservation Date", "field"));
                    }
                }
                if (!StringUtil.isEmpty(cryopreservedNum)){
                    String cryoNumValiStr;
                    String firstStr = cryopreservedNum.substring(0,1);
                    if("-".equals(firstStr)) {
                        cryoNumValiStr = cryopreservedNum.substring(1);
                    } else {
                        cryoNumValiStr = cryopreservedNum;
                    }
                    if (cryoNumValiStr.length() > 2) {
                        errMap.put("cryopreservedNum", MessageUtil.replaceMessage("DS_ERR009", "No. Cryopreserved", "field"));
                    }
                    Pattern pattern = compile("(-?[1-9]\\d*)|0");
                    boolean hoursFlag = pattern.matcher(cryopreservedNum).matches();
                    if (!hoursFlag) {
                        errMap.put("cryopreservedNum", "GENERAL_ERR0002");
                    }
                }
            }
        }
        if(errMap.isEmpty()) {
            return null;
        }
        return errMap;
    }
}
