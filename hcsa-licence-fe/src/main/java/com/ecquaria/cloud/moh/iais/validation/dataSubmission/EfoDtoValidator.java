package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.EfoCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

/**
 * EfoDtoValidate
 *
 * @author junyu
 * @date 2021/10/21
 */
@Component
@Slf4j
public class EfoDtoValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest httpServletRequest) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        ArSuperDataSubmissionDto arSuperDataSubmissionDto= DataSubmissionHelper.getCurrentArDataSubmission(httpServletRequest);
        EfoCycleStageDto efoCycleStageDto=arSuperDataSubmissionDto.getEfoCycleStageDto();
        Date sDate = efoCycleStageDto.getStartDate();
        String reason = efoCycleStageDto.getReason();
        String othersReason = efoCycleStageDto.getOtherReason();


        if (!StringUtil.isEmpty(sDate) ) {
            Date today = new Date();
            if( sDate.after(today)) {
                errorMap.put("startDate", MessageUtil.replaceMessage("DS_ERR001","Date Started", "field"));
            }
        }
        if(efoCycleStageDto.getIsMedicallyIndicated()==1){
            if (!StringUtil.isEmpty(reason)&& DataSubmissionConsts.EFO_REASON_OTHERS.equals(reason)) {
                if (StringUtil.isEmpty(othersReason)) {
                    String errMsg = MessageUtil.replaceMessage("GENERAL_ERR0006","Others Reason", "field");
                    errorMap.put("othersReason", errMsg);
                }else if(othersReason.length()>100){
                    Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                    repMap.put("number","100");
                    repMap.put("fieldNo","Others Reason");
                    String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                    errorMap.put("othersReason", errMsg);
                }
            }
        }
        if(efoCycleStageDto.getIsMedicallyIndicated()==0){
            if(!StringUtil.isEmpty(reason)&&reason.length()>66){
                Map<String, String> repMap=IaisCommonUtils.genNewHashMap();
                repMap.put("number","66");
                repMap.put("fieldNo","Reason");
                String errMsg = MessageUtil.getMessageDesc("GENERAL_ERR0036",repMap);
                errorMap.put("reason", errMsg);
            }
        }
        return errorMap;
    }

}
