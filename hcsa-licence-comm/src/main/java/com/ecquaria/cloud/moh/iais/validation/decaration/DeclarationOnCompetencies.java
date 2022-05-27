package com.ecquaria.cloud.moh.iais.validation.decaration;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AppValidatorHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.validation.Declarations;

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
        String err06 = MessageUtil.replaceMessage("GENERAL_ERR0006","this","field");
        if(StringUtil.isEmpty(competenciesItem1)){
            map.put("competenciesItem1", err06);
        }else {
            if("0".equals(competenciesItem1)){
                flag=true;
            }
        }
        String competenciesItem2 = appDeclarationMessageDto.getCompetenciesItem2();
        if(StringUtil.isEmpty(competenciesItem2)){
            map.put("competenciesItem2", err06);
        }else {
            if("0".equals(competenciesItem2)){
                flag=true;
            }
        }
        String competenciesItem3 = appDeclarationMessageDto.getCompetenciesItem3();
        if(StringUtil.isEmpty(competenciesItem3)){
            map.put("competenciesItem3", err06);
        }else {
            if("0".equals(competenciesItem3)){
                flag=true;
            }
        }
        String competenciesRemark = appDeclarationMessageDto.getCompetenciesRemark();
        if(StringUtil.isEmpty(competenciesRemark)&&flag){
            map.put("competenciesRemark", err06);
        }else if(!StringUtil.isEmpty(competenciesRemark)&&competenciesRemark.length()>=1000){
            String general_err0041= AppValidatorHelper.repLength("this","1000");
            map.put("competenciesRemark",general_err0041);
        }

    }
}
