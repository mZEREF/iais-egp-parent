package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2019/11/27 10:06
 */
public class InspectionCheckListValidation implements CustomizeValidator {
    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        Map<String, String> errMap = new HashMap<>();
        InspectionFillCheckListDto icDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,"fillcheckListDto");
        List<InspectionCheckQuestionDto> cqDtoList = icDto.getCheckList();
        if(StringUtil.isEmpty(icDto.getBestPractice())){
            errMap.put("bestPractice","Best Pracice is mandatory.");
        }
        if(StringUtil.isEmpty(icDto.getTuc())){
            errMap.put("tcu","TCU is mandatory.");
        }
        if(cqDtoList!=null && !cqDtoList.isEmpty()){
            for(InspectionCheckQuestionDto temp:cqDtoList){
                if(StringUtil.isEmpty(temp.getAnswer())){
                    errMap.put(temp.getId(),"Answer is mandaroty.");
                }
            }
        }else{
            errMap.put("allList","Please fill in checkList.");
        }
        return null;
    }
}
