package com.ecquaria.cloud.moh.iais.validate.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.validate.ValidateFlow;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/4/25 14:06
 */
@Component
public class ValidateVehicle implements ValidateFlow {
    @Override
    public void doValidateVehicles(Map<String,String> map, List<AppSvcVehicleDto> appSvcVehicleDtos) {
        if(appSvcVehicleDtos==null){
            return;
        }
        for(int i=0;i<appSvcVehicleDtos.size();i++){
            String vehicleName = appSvcVehicleDtos.get(i).getVehicleName();
            String chassisNum = appSvcVehicleDtos.get(i).getChassisNum();
            String engineNum = appSvcVehicleDtos.get(i).getEngineNum();
            if(StringUtil.isEmpty(vehicleName)){
                map.put("vehicleName"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Address Type", "field"));
            }

            if(StringUtil.isEmpty(chassisNum)){
                map.put("chassisNum"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Address Type", "field"));
            }

            if(StringUtil.isEmpty(engineNum)){
                map.put("engineNum"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Address Type", "field"));
            }

        }
    }
}
