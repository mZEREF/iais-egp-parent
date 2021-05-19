package com.ecquaria.cloud.moh.iais.validation.declarationsValidate.PreliminaryQuestionValidate;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.validation.declarationsValidate.Declarations;


import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/5/15 13:52
 */
public class Statements implements Declarations {
    @Override
    public void validateDeclarations(Map<String, String> map, AppDeclarationMessageDto appDeclarationMessageDto) {
        if(appDeclarationMessageDto==null){
            return;
        }
        String preliminaryQuestionItem1 = appDeclarationMessageDto.getPreliminaryQuestionItem1();
        if(StringUtil.isEmpty(preliminaryQuestionItem1)){
            map.put("preliminaryQuestionItem1", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }
        String preliminaryQuestiontem2 = appDeclarationMessageDto.getPreliminaryQuestiontem2();
        if(StringUtil.isEmpty(preliminaryQuestiontem2)){
            map.put("preliminaryQuestiontem2", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }
    }
}
