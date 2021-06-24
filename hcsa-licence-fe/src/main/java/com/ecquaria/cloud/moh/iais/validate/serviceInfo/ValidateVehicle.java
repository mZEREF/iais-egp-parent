package com.ecquaria.cloud.moh.iais.validate.serviceInfo;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.HcsaLicenceGroupFeeDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.VehNoValidator;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
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
    private ApplicationFeClient applicationFeClient;
    @Override
    public void doValidateVehicles(Map<String,String> map, List<AppSvcVehicleDto> appSvcVehicleDtos,String licenseeId, List<AppSvcVehicleDto> oldAppSvcVehicleDto) {
        if(appSvcVehicleDtos==null){
            return;
        }
        List<String> vehicleNameList=new ArrayList<>(appSvcVehicleDtos.size());
        List<String>chassisNumList=new ArrayList<>(appSvcVehicleDtos.size());
        List<String>engineNumNumList=new ArrayList<>(appSvcVehicleDtos.size());
        List<String> validateVehicleName=new ArrayList<>(appSvcVehicleDtos.size());
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
                    //validate  vehicle number used. how to do ? RFC Renew Rfi how to do ?I haven't come up with a solution yet
                    //1. licence used ,rfc renew use some vehicle no.--ok
                    //2. rfc renew change vehicle A-->B ，rfi use A ---> ok。
                    if(oldAppSvcVehicleDto==null){
                     /*   List<AppSvcVehicleDto> appSvcVehicleDtoList = applicationFeClient.getAppSvcVehicleDtoByVehicleNumber(vehicleName,licenseeId).getEntity();
                        if(!appSvcVehicleDtoList.isEmpty()){
                            map.put("vehicleName" + i, "NEW_ERR0028");
                        }*/
                    }else {

                    }

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
        //need validate
        if(!validateVehicleName.isEmpty()){
           /* List<Boolean> entity = hcsaLicenClient.vehicleIsUsed(validateVehicleName).getEntity();
            if(!entity.isEmpty()){
                validateVehicleName.forEach((v)->{
                    indexMap.forEach((k,var)->{
                        if(v.equals(var)){
                            map.put("vehicleName"+k,MessageUtil.getMessageDesc("NEW_ERR0028"));
                        }
                    });
                });
            }*/
            //need to do check application premises vehicle name;
        }
        log.info(StringUtil.changeForLog("=======> ValidateCharges->"+ JsonUtil.parseToJson(map)));
    }

    @Override
    public void doValidateVehicles(Map<String, String> map, AppSubmissionDto appSubmissionDto) {
        if(appSubmissionDto==null){
            return;
        }
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(appSvcRelatedInfoDtoList==null){
            return;
        }
        Map<String,String> vehicleNameMap=new HashMap<>(10);
        Map<String,String> chassisNumMap=new HashMap<>(10);
        Map<String,String> engineNumMap=new HashMap<>(10);
        for (int i = 0; i < appSvcRelatedInfoDtoList.size(); i++) {
            List<AppSvcVehicleDto> appSvcVehicleDtoList = appSvcRelatedInfoDtoList.get(i).getAppSvcVehicleDtoList();
            if(appSvcVehicleDtoList==null){
                continue;
            }
            for (int i1 = 0; i1 < appSvcVehicleDtoList.size(); i1++) {
                String vehicleName = appSvcVehicleDtoList.get(i1).getVehicleName();
                String chassisNum = appSvcVehicleDtoList.get(i1).getChassisNum();
                String engineNum = appSvcVehicleDtoList.get(i1).getEngineNum();
                String v = vehicleNameMap.get(vehicleName);
                if(v==null){
                    vehicleNameMap.put(vehicleName,vehicleName);
                }else {
                    map.put("vehicleName"+i1, MessageUtil.getMessageDesc("NEW_ERR0012"));
                }
                String v1 = chassisNumMap.get(chassisNum);
                if(v1==null){
                    chassisNumMap.put(chassisNum,chassisNum);
                }else {
                    map.put("chassisNum"+i, MessageUtil.getMessageDesc("NEW_ERR0012"));
                }
                String v2 = engineNumMap.get(engineNum);
                if(v2==null){
                    engineNumMap.put(engineNum,engineNum);
                }else {
                    map.put("engineNum"+i, MessageUtil.getMessageDesc("NEW_ERR0012"));
                }
            }
        }
    }
}
