package com.ecquaria.cloud.moh.iais.validation.dataSubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PostTerminationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TerminationOfPregnancyDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.helper.DataSubmissionHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Slf4j
public class PostTerminationValidator implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        TopSuperDataSubmissionDto topSuperDataSubmissionDto = DataSubmissionHelper.getCurrentTopDataSubmission(request);
        TerminationOfPregnancyDto terminationOfPregnancyDto = topSuperDataSubmissionDto.getTerminationOfPregnancyDto();
        PostTerminationDto postTerminationDto=terminationOfPregnancyDto.getPostTerminationDto();
        if(postTerminationDto==null){
            postTerminationDto=new PostTerminationDto();
        }
        if (!StringUtil.isEmpty(postTerminationDto.getGivenPostCounselling())  ) {
            if(postTerminationDto.getGivenPostCounselling()==true){
                if(StringUtil.isEmpty(postTerminationDto.getCounsellorIdType())){
                    errorMap.put("counsellorIdType", "GENERAL_ERR0006");
                }
                if(StringUtil.isEmpty(postTerminationDto.getCounsellorIdNo())){
                    errorMap.put("counsellorIdNo", "GENERAL_ERR0006");
                }
                if(StringUtil.isEmpty(postTerminationDto.getCounsellorName())){
                    errorMap.put("counsellorName", "GENERAL_ERR0006");
                }
                if(StringUtil.isEmpty(postTerminationDto.getCounsellingDate())){
                    errorMap.put("counsellingDate", "GENERAL_ERR0006");
                }else {
                    try {
                        if (Formatter.compareDateByDay(postTerminationDto.getCounsellingDate()) > 0) {
                            errorMap.put("counsellingDate", MessageUtil.replaceMessage("DS_ERR001", "Date of Counselling", "field"));
                        }
                    } catch (Exception e) {
                        log.error(StringUtil.changeForLog(e.getMessage()), e);
                    }
                }
                if(StringUtil.isEmpty(postTerminationDto.getCounsellingPlace())){
                    errorMap.put("TopPlace", "GENERAL_ERR0006");
                }
            }
        }

        if(!StringUtil.isEmpty(postTerminationDto.getGivenPostCounselling())){
            if(postTerminationDto.getGivenPostCounselling()==true){
                if(StringUtil.isEmpty(postTerminationDto.getCounsellingRslt())){
                    errorMap.put("counsellingRslt", "GENERAL_ERR0006");
                }
            }
        }
        if(!StringUtil.isEmpty(postTerminationDto.getGivenPostCounselling())){
            if(postTerminationDto.getGivenPostCounselling()==true){
                if("TOPCR007".equals(postTerminationDto.getCounsellingRslt())){
                    if(StringUtil.isEmpty(postTerminationDto.getOtherCounsellingRslt())){
                        errorMap.put("otherCounsellingRslt", "GENERAL_ERR0006");
                    }
                }

            }
        }
        if(!StringUtil.isEmpty(postTerminationDto.getGivenPostCounselling())){
            if(postTerminationDto.getGivenPostCounselling()==false){
                    if(StringUtil.isEmpty(postTerminationDto.getIfCounsellingNotGiven())){
                        errorMap.put("ifCounsellingNotGiven", "GENERAL_ERR0006");
                }

            }
        }
        return errorMap;
    }
}
