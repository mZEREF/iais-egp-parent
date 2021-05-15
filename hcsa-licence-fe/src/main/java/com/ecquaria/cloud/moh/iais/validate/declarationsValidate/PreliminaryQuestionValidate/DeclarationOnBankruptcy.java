package com.ecquaria.cloud.moh.iais.validate.declarationsValidate.PreliminaryQuestionValidate;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.validate.declarationsValidate.Declarations;

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
        if(StringUtil.isEmpty(bankruptcyItem1)){
            map.put("bankruptcyItem1", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }else {
            if("1".equals(bankruptcyItem1)){
                flag=true;
            }
        }
        String bankruptcyItem2 = appDeclarationMessageDto.getBankruptcyItem2();
        if(StringUtil.isEmpty(bankruptcyItem2)){
            map.put("bankruptcyItem2", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }else {
            if("1".equals(bankruptcyItem2)){
                flag=true;
            }
        }
        String bankruptcyItem3 = appDeclarationMessageDto.getBankruptcyItem3();
        if(StringUtil.isEmpty(bankruptcyItem3)){
            map.put("bankruptcyItem3", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }else {
            if("1".equals(bankruptcyItem3)){
                flag=true;
            }
        }
        String bankruptcyItem4 = appDeclarationMessageDto.getBankruptcyItem4();
        if(StringUtil.isEmpty(bankruptcyItem4)){
            map.put("bankruptcyItem3", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }else {
            if("1".equals(bankruptcyItem4)){
                flag=true;
            }
        }
        String bankruptcyRemark = appDeclarationMessageDto.getBankruptcyRemark();
        if(StringUtil.isEmpty(bankruptcyRemark)&&flag){
            map.put("bankruptcyRemark", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }
    }
}
