package com.ecquaria.cloud.moh.iais.validation.decaration;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.validation.Declarations;

import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/5/15 13:52
 */
public class PreliminaryQuestion implements Declarations {
    @Override
    public void validateDeclarations(Map<String, String> map, AppDeclarationMessageDto appDeclarationMessageDto) {
        if(appDeclarationMessageDto==null){
            return;
        }
        String preliminaryQuestionKindly = appDeclarationMessageDto.getPreliminaryQuestionKindly();
        if(StringUtil.isEmpty(preliminaryQuestionKindly)){
            map.put("preliminaryQuestionKindly", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }
    }
}
