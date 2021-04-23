package com.ecquaria.cloud.moh.iais.validate.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.validate.ValidateFlow;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/4/23 13:18
 */
@Component
public class ValidateEasmts implements ValidateFlow {
    @Override
    public Map<String, String> doValidatePremises(Map<String, String> map, AppGrpPremisesDto appGrpPremisesDto,Integer index) {
        String easMtsHciName = appGrpPremisesDto.getEasMtsHciName();
        if(StringUtil.isEmpty(easMtsHciName)){
            map.put("easMtsHciName"+index, MessageUtil.replaceMessage("GENERAL_ERR0006", "Name of HCI", "field"));
        }else {

        }
        String easMtsPostalCode = appGrpPremisesDto.getEasMtsPostalCode();
        if(StringUtil.isEmpty(easMtsPostalCode)){
            map.put("easMtsPostalCode"+index,MessageUtil.replaceMessage("GENERAL_ERR0006", "Postal Code ", "field"));
        }
        String easMtsAddressType = appGrpPremisesDto.getEasMtsAddressType();
        if(StringUtil.isEmpty(easMtsAddressType)){
            map.put("easMtsAddressType"+index,MessageUtil.replaceMessage("GENERAL_ERR0006", "Address Type", "field"));
        }
        String easMtsStreetName = appGrpPremisesDto.getEasMtsStreetName();
        if(StringUtil.isEmpty(easMtsStreetName)){
            map.put("easMtsStreetName"+index,MessageUtil.replaceMessage("GENERAL_ERR0006", "Street Name", "field"));
        }else {

        }
        String easMtsPubEmail = appGrpPremisesDto.getEasMtsPubEmail();
        if(StringUtil.isEmpty(easMtsPubEmail)){
            map.put("easMtsPubEmail"+index,MessageUtil.replaceMessage("GENERAL_ERR0006", "Public email", "field"));
        }else {

        }
        String easMtsPubHotline = appGrpPremisesDto.getEasMtsPubHotline();
        if(StringUtil.isEmpty(easMtsPubHotline)){
            map.put("easMtsPubHotline"+index,MessageUtil.replaceMessage("GENERAL_ERR0006", "Public Hotline", "field"));
        }else {

        }
        return map;
    }
}
