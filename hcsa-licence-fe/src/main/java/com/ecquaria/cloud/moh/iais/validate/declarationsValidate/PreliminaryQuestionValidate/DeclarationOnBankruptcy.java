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
        String bankruptcyItem1 = appDeclarationMessageDto.getBankruptcyItem1();
        if(StringUtil.isEmpty(bankruptcyItem1)){
            map.put("bankruptcyItem1", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }
        String bankruptcyItem2 = appDeclarationMessageDto.getBankruptcyItem2();
        if(StringUtil.isEmpty(bankruptcyItem2)){
            map.put("bankruptcyItem2", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }
        String bankruptcyItem3 = appDeclarationMessageDto.getBankruptcyItem3();
        if(StringUtil.isEmpty(bankruptcyItem3)){
            map.put("bankruptcyItem3", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }
        String bankruptcyItem4 = appDeclarationMessageDto.getBankruptcyItem4();
        if(StringUtil.isEmpty(bankruptcyItem4)){
            map.put("bankruptcyItem3", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }
        String bankruptcyRemark = appDeclarationMessageDto.getBankruptcyRemark();
        if(StringUtil.isEmpty(bankruptcyRemark)){
            map.put("bankruptcyRemark", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }
    }
}
