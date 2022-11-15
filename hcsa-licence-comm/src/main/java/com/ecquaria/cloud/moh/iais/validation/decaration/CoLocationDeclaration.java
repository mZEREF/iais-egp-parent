package com.ecquaria.cloud.moh.iais.validation.decaration;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.validation.Declarations;

import java.util.Map;

/**
 * @author chenlei on 11/15/2022.
 */
public class CoLocationDeclaration implements Declarations {

    @Override
    public void validateDeclarations(Map<String, String> map, AppDeclarationMessageDto appDeclarationMessageDto) {
        if (appDeclarationMessageDto == null) {
            return;
        }
        String coLocationItem1 = appDeclarationMessageDto.getCoLocationItem1();
        if (StringUtil.isEmpty(coLocationItem1)) {
            map.put("coLocationItem1", MessageUtil.replaceMessage("GENERAL_ERR0006", "this", "field"));
        }
        String coLocationItem2 = appDeclarationMessageDto.getCoLocationItem2();
        if (StringUtil.isEmpty(coLocationItem2)) {
            map.put("coLocationItem2", MessageUtil.replaceMessage("GENERAL_ERR0006", "this", "field"));
        }
    }

}
