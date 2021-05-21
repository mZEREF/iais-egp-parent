package com.ecquaria.cloud.moh.iais.validation.declarationsValidate.PreliminaryQuestionValidate;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.validation.declarationsValidate.Declarations;


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
        boolean flag=false;
        String competenciesItem1 = appDeclarationMessageDto.getCompetenciesItem1();
        if(StringUtil.isEmpty(competenciesItem1)){
            map.put("competenciesItem1", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }else {
            if("1".equals(competenciesItem1)){
                flag=true;
            }
        }
        String competenciesItem2 = appDeclarationMessageDto.getCompetenciesItem2();
        if(StringUtil.isEmpty(competenciesItem2)){
            map.put("competenciesItem2", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }else {
            if("1".equals(competenciesItem2)){
                flag=true;
            }
        }
        String competenciesItem3 = appDeclarationMessageDto.getCompetenciesItem3();
        if(StringUtil.isEmpty(competenciesItem3)){
            map.put("competenciesItem3", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }else {
            if("1".equals(competenciesItem3)){
                flag=true;
            }
        }
        String competenciesRemark = appDeclarationMessageDto.getCompetenciesRemark();
        if(StringUtil.isEmpty(competenciesRemark)&&flag){
            map.put("competenciesRemark", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }else if(!StringUtil.isEmpty(competenciesRemark)&&competenciesRemark.length()>1000){
            String general_err0041= NewApplicationHelper.repLength("this","1000");
            map.put("competenciesRemark",general_err0041);
        }

    }
}
