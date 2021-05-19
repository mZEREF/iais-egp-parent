package com.ecquaria.cloud.moh.iais.validation.declarationsValidate.PreliminaryQuestionValidate;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.validation.declarationsValidate.Declarations;


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
        if(StringUtil.isEmpty(criminalRecordsItem1)){
            map.put("criminalRecordsItem1", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }else {
            if("1".equals(criminalRecordsItem1)){
                flag=true;
            }
        }
        String criminalRecordsItem2 = appDeclarationMessageDto.getCriminalRecordsItem2();
        if(StringUtil.isEmpty(criminalRecordsItem2)){
            map.put("criminalRecordsItem2", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));

        }else {
            if("1".equals(criminalRecordsItem2)){
                flag=true;
            }
        }
        String criminalRecordsItem3 = appDeclarationMessageDto.getCriminalRecordsItem3();
        if(StringUtil.isEmpty(criminalRecordsItem3)){
            map.put("criminalRecordsItem3", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));

        }else {
            if("1".equals(criminalRecordsItem3)){
                flag=true;
            }
        }
        String criminalRecordsItem4 = appDeclarationMessageDto.getCriminalRecordsItem4();
        if(StringUtil.isEmpty(criminalRecordsItem4)){
            map.put("criminalRecordsItem4", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));

        }else {
            if("1".equals(criminalRecordsItem4)){
                flag=true;
            }
        }
        String criminalRecordsRemark = appDeclarationMessageDto.getCriminalRecordsRemark();
        if(StringUtil.isEmpty(criminalRecordsRemark)&&flag){
            map.put("criminalRecordsRemark", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }
    }
}
