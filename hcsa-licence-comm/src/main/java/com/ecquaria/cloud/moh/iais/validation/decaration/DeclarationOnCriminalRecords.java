package com.ecquaria.cloud.moh.iais.validation.decaration;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AppValidatorHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.validation.Declarations;

import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/5/15 14:58
 */
public class DeclarationOnCriminalRecords implements Declarations {
    @Override
    public void validateDeclarations(Map<String, String> map, AppDeclarationMessageDto appDeclarationMessageDto) {
        if(appDeclarationMessageDto==null){
            return;
        }
        boolean flag=false;
        String criminalRecordsItem1 = appDeclarationMessageDto.getCriminalRecordsItem1();
        String err06 = MessageUtil.replaceMessage("GENERAL_ERR0006","this","field");
        if(StringUtil.isEmpty(criminalRecordsItem1)){
            map.put("criminalRecordsItem1", err06);
        }else {
            if("0".equals(criminalRecordsItem1)){
                flag=true;
            }
        }
        String criminalRecordsItem2 = appDeclarationMessageDto.getCriminalRecordsItem2();
        if(StringUtil.isEmpty(criminalRecordsItem2)){
            map.put("criminalRecordsItem2", err06);

        }else {
            if("0".equals(criminalRecordsItem2)){
                flag=true;
            }
        }
        String criminalRecordsItem3 = appDeclarationMessageDto.getCriminalRecordsItem3();
        if(StringUtil.isEmpty(criminalRecordsItem3)){
            map.put("criminalRecordsItem3", err06);

        }else {
            if("0".equals(criminalRecordsItem3)){
                flag=true;
            }
        }
        String criminalRecordsItem4 = appDeclarationMessageDto.getCriminalRecordsItem4();
        if(StringUtil.isEmpty(criminalRecordsItem4)){
            map.put("criminalRecordsItem4", err06);

        }else {
            if("0".equals(criminalRecordsItem4)){
                flag=true;
            }
        }
        String criminalRecordsRemark = appDeclarationMessageDto.getCriminalRecordsRemark();
        if(StringUtil.isEmpty(criminalRecordsRemark)&&flag){
            map.put("criminalRecordsRemark", err06);
        }else if(!StringUtil.isEmpty(criminalRecordsRemark)&&criminalRecordsRemark.length()>=1000){
            String general_err0041= AppValidatorHelper.repLength("this","1000");
            map.put("criminalRecordsRemark",general_err0041);
        }
    }
}
