package com.ecquaria.cloud.moh.iais.validation.decaration;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AppValidatorHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.validation.Declarations;

import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/5/15 14:52
 */
public class DeclarationOnBankruptcy implements Declarations {
    @Override
    public void validateDeclarations(Map<String, String> map, AppDeclarationMessageDto appDeclarationMessageDto) {
        if(appDeclarationMessageDto==null){
            return;
        }
        boolean flag=false;
        String bankruptcyItem1 = appDeclarationMessageDto.getBankruptcyItem1();
        String err06 = MessageUtil.replaceMessage("GENERAL_ERR0006","this","field");
        if(StringUtil.isEmpty(bankruptcyItem1)){
            map.put("bankruptcyItem1", err06);
        }else {
            if("0".equals(bankruptcyItem1)){
                flag=true;
            }
        }
        String bankruptcyItem2 = appDeclarationMessageDto.getBankruptcyItem2();
        if(StringUtil.isEmpty(bankruptcyItem2)){
            map.put("bankruptcyItem2", err06);
        }else {
            if("0".equals(bankruptcyItem2)){
                flag=true;
            }
        }
        String bankruptcyItem3 = appDeclarationMessageDto.getBankruptcyItem3();
        if(StringUtil.isEmpty(bankruptcyItem3)){
            map.put("bankruptcyItem3", err06);
        }else {
            if("0".equals(bankruptcyItem3)){
                flag=true;
            }
        }
        String bankruptcyItem4 = appDeclarationMessageDto.getBankruptcyItem4();
        if (StringUtil.isEmpty(bankruptcyItem4)) {
            map.put("bankruptcyItem4", err06);
        } else {
            if ("0".equals(bankruptcyItem4)) {
                flag = true;
            }
        }
        String bankruptcyRemark = appDeclarationMessageDto.getBankruptcyRemark();
        if(StringUtil.isEmpty(bankruptcyRemark)&&flag){
            map.put("bankruptcyRemark", err06);
        }else if(!StringUtil.isEmpty(bankruptcyItem1)&&bankruptcyItem1.length()>=1000){
            String general_err0041= AppValidatorHelper.repLength("this","1000");
            map.put("bankruptcyRemark",general_err0041);
        }
    }
}
