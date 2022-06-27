package com.ecquaria.cloud.moh.iais.validation.decaration;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.validation.Declarations;

import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/5/15 15:09
 */
public class GeneralAccuracyDeclaration implements Declarations {
    @Override
    public void validateDeclarations(Map<String, String> map, AppDeclarationMessageDto appDeclarationMessageDto) {
        if(appDeclarationMessageDto==null){
            return;
        }
        String generalAccuracyItem1 = appDeclarationMessageDto.getGeneralAccuracyItem1();
        if(StringUtil.isEmpty(generalAccuracyItem1)){
            map.put("generalAccuracyItem1", MessageUtil.replaceMessage("GENERAL_ERR0006","this","field"));
        }
    }
}
