package com.ecquaria.cloud.moh.iais.validate.serviceInfo;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.HcsaLicenceGroupFeeDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.VehNoValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenClient;
import com.ecquaria.cloud.moh.iais.validate.ValidateFlow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/4/25 14:06
 */
@Component
@Slf4j
public class ValidateVehicle implements ValidateFlow {
    @Autowired
    private HcsaLicenClient hcsaLicenClient;
    @Override
    public void doValidateVehicles(Map<String,String> map, List<AppSvcVehicleDto> appSvcVehicleDtos) {
        if(appSvcVehicleDtos==null){
            return;
        }
        List<String> vehicleNameList=new ArrayList<>(appSvcVehicleDtos.size());
        List<String>chassisNumList=new ArrayList<>(appSvcVehicleDtos.size());
        List<String>engineNumNumList=new ArrayList<>(appSvcVehicleDtos.size());
        List<String> validateVehicleName=new ArrayList<>(appSvcVehicleDtos.size());
        Map<Integer,String> indexMap=new HashMap<>(appSvcVehicleDtos.size());
        for(int i=0;i<appSvcVehicleDtos.size();i++){
            String vehicleName = appSvcVehicleDtos.get(i).getVehicleName();
            String chassisNum = appSvcVehicleDtos.get(i).getChassisNum();
            String engineNum = appSvcVehicleDtos.get(i).getEngineNum();
            if(StringUtil.isEmpty(vehicleName)){
                map.put("vehicleName"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Address Type", "field"));
            }else {
                if(vehicleNameList.contains(vehicleName)){
                    map.put("vehicleName"+i, MessageUtil.getMessageDesc("NEW_ERR0012"));
                }else {
                    vehicleNameList.add(vehicleName);
                }
                if(vehicleName.length() > 10){
                    String general_err0041= NewApplicationHelper.repLength("Vehicle Number","10");
                    map.put("vehicleName" + i, general_err0041);
                } else if(! VehNoValidator.validateNumber(vehicleName)){
                    map.put("vehicleName" + i, "GENERAL_ERR0017");
                }else {
                    //validate  vehicle number used
                    validateVehicleName.add(vehicleName);
                    indexMap.put(i,vehicleName);
                }
            }

            if(StringUtil.isEmpty(chassisNum)){
                map.put("chassisNum"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Address Type", "field"));
            }else {
                if(chassisNumList.contains(chassisNum)){
                    map.put("chassisNum"+i, MessageUtil.getMessageDesc("NEW_ERR0012"));
                }else {
                    chassisNumList.add(chassisNum);
                }
            }

            if(StringUtil.isEmpty(engineNum)){
                map.put("engineNum"+i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Address Type", "field"));
            }else {
                if(engineNumNumList.contains(engineNum)){
                    map.put("engineNum"+i, MessageUtil.getMessageDesc("NEW_ERR0012"));
                }else {
                    engineNumNumList.add(engineNum);
                }
            }
        }
        if(!validateVehicleName.isEmpty()){
            List<Boolean> entity = hcsaLicenClient.vehicleIsUsed(validateVehicleName).getEntity();
            if(!entity.isEmpty()){
                validateVehicleName.forEach((v)->{
                    indexMap.forEach((k,var)->{
                        if(v.equals(var)){
                            map.put("vehicleName"+k,MessageUtil.getMessageDesc("NEW_ERR0028"));
                        }
                    });
                });
            }
        }
        log.info(StringUtil.changeForLog("=======> ValidateCharges->"+ JsonUtil.parseToJson(map)));
    }
}
