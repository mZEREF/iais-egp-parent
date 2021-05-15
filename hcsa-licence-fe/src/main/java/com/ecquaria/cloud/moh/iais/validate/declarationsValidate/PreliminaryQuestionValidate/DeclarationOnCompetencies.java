package com.ecquaria.cloud.moh.iais.validate.declarationsValidate.PreliminaryQuestionValidate;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.validate.declarationsValidate.Declarations;

import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/5/15 14:55
 */
public class DeclarationOnCompetencies implements Declarations {
    @Override
    public void validateDeclarations(Map<String, String> map, AppDeclarationMessageDto appDeclarationMessageDto) {
        if(appDeclarationMessageDto==null){
            return;
        }
        String competenciesItem1 = appDeclarationMessageDto.getCompetenciesItem1();
        if(StringUtil.isEmpty(competenciesItem1)){
            map.put("competenciesItem1", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }
        String competenciesItem2 = appDeclarationMessageDto.getCompetenciesItem2();
        if(StringUtil.isEmpty(competenciesItem2)){
            map.put("competenciesItem2", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }
        String competenciesItem3 = appDeclarationMessageDto.getCompetenciesItem3();
        if(StringUtil.isEmpty(competenciesItem3)){
            map.put("competenciesItem3", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }
        String competenciesRemark = appDeclarationMessageDto.getCompetenciesRemark();
        if(StringUtil.isEmpty(competenciesRemark)){
            map.put("competenciesItem3", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }
    }
}
