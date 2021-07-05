package com.ecquaria.cloud.moh.iais.validate.serviceInfo;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
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

    @Override
    public void doValidateVehicles(Map<String, String> errorMap, List<AppSvcVehicleDto> appSvcVehicleDtoAlls,
            List<AppSvcVehicleDto> appSvcVehicleDtos, List<AppSvcVehicleDto> oldAppSvcVehicleDto) {
        if (appSvcVehicleDtos == null) {
            return;
        }
        Map<String, String> map = null;
        List<String> vehicleNameList = new ArrayList<>(appSvcVehicleDtos.size());
        List<String> chassisNumList = new ArrayList<>(appSvcVehicleDtos.size());
        List<String> engineNumNumList = new ArrayList<>(appSvcVehicleDtos.size());
        List<String> validateVehicleName = new ArrayList<>(appSvcVehicleDtos.size());
        Map<Integer, String> indexMap = new HashMap<>(appSvcVehicleDtos.size());
        for (int i = 0; i < appSvcVehicleDtos.size(); i++) {
            map = IaisCommonUtils.genNewHashMap();
            String vehicleName = appSvcVehicleDtos.get(i).getVehicleName();
            if (StringUtil.isEmpty(vehicleName)) {
                map.put("vehicleName" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Address Type", "field"));
            } else if (vehicleName.length() > 10) {
                map.put("vehicleName" + i, NewApplicationHelper.repLength("Vehicle Number", "10"));
            } else if (!VehNoValidator.validateNumber(vehicleName)) {
                map.put("vehicleName" + i, "GENERAL_ERR0017");
            } else {
                vehicleName = vehicleName.toLowerCase(AppConsts.DFT_LOCALE);
                if (vehicleNameList.contains(vehicleName)) {
                    map.put("vehicleName" + i, MessageUtil.getMessageDesc("NEW_ERR0012"));
                } else {
                    vehicleNameList.add(vehicleName);
                    int vehicleCount=0;
                    for (AppSvcVehicleDto asv:appSvcVehicleDtoAlls
                    ) {
                        if (asv.getVehicleName()!=null&&asv.getVehicleName().equals(vehicleName)){
                            vehicleCount++;
                        }
                    }
                    if(vehicleCount>=2){
                        map.put("vehicleName"+i, MessageUtil.getMessageDesc("NEW_ERR0012"));
                    }
                }
            }

            String chassisNum = appSvcVehicleDtos.get(i).getChassisNum();
            if (StringUtil.isEmpty(chassisNum)) {
                map.put("chassisNum" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Chassis Number", "field"));
            } else if (chassisNum.length() > 25) {
                map.put("chassisNum" + i, NewApplicationHelper.repLength("Chassis Number", "25"));
            } else {
                chassisNum = chassisNum.toLowerCase(AppConsts.DFT_LOCALE);
                if (chassisNumList.contains(chassisNum)) {
                    map.put("chassisNum" + i, MessageUtil.getMessageDesc("NEW_ERR0012"));
                } else {
                    chassisNumList.add(chassisNum);
                    int chassisCount=0;
                    for (AppSvcVehicleDto asv:appSvcVehicleDtoAlls
                    ) {
                        if (asv.getChassisNum()!=null&&asv.getChassisNum().equals(chassisNum)){
                            chassisCount++;
                        }
                    }
                    if(chassisCount>=2){
                        map.put("chassisNum"+i, MessageUtil.getMessageDesc("NEW_ERR0012"));
                    }
                }
            }

            String engineNum = appSvcVehicleDtos.get(i).getEngineNum();
            if (StringUtil.isEmpty(engineNum)) {
                map.put("engineNum" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Engine Number", "field"));
            } else if (engineNum.length() > 25) {
                map.put("engineNum" + i, NewApplicationHelper.repLength("Engine Number", "25"));
            } else {
                engineNum = engineNum.toLowerCase(AppConsts.DFT_LOCALE);
                if (engineNumNumList.contains(engineNum)) {
                    map.put("engineNum" + i, MessageUtil.getMessageDesc("NEW_ERR0012"));
                } else {
                    engineNumNumList.add(engineNum);
                    int engineCount=0;
                    for (AppSvcVehicleDto asv:appSvcVehicleDtoAlls
                    ) {
                        if (asv.getEngineNum()!=null&&asv.getEngineNum().equals(engineNum)){
                            engineCount++;
                        }
                    }
                    if(engineCount>=2){
                        map.put("engineNum"+i, MessageUtil.getMessageDesc("NEW_ERR0012"));
                    }
                }
            }

            if (map.isEmpty()) {
                validateCurrentVehicle(map, "vehicleName", vehicleName, i, appSvcVehicleDtoAlls);
                validateCurrentVehicle(map, "chassisNum", vehicleName, i, appSvcVehicleDtoAlls);
                validateCurrentVehicle(map, "engineNum", engineNum, i, appSvcVehicleDtoAlls);
                validateExistVehicle(map, "vehicleName", vehicleName, i, oldAppSvcVehicleDto);
                validateExistVehicle(map, "chassisNum", vehicleName, i, oldAppSvcVehicleDto);
                validateExistVehicle(map, "engineNum", engineNum, i, oldAppSvcVehicleDto);
            }

            log.info(StringUtil.changeForLog("Validate Vehicles " + i + "->" + JsonUtil.parseToJson(map)));
            errorMap.putAll(map);
        }
    }

    private void validateExistVehicle(Map<String, String> map, String name, String value, int index,
            List<AppSvcVehicleDto> oldAppSvcVehicleDto) {
        if (oldAppSvcVehicleDto == null || oldAppSvcVehicleDto.isEmpty()) {
            return;
        }
        if (oldAppSvcVehicleDto.stream().anyMatch(asv -> value.equalsIgnoreCase(getValue(asv, name)))) {
            map.put(name + index, MessageUtil.getMessageDesc("NEW_ERR0012"));
        }
    }

    private void validateCurrentVehicle(Map<String, String> map, String name, String value, int index,
            List<AppSvcVehicleDto> appSvcVehicleDtoAlls) {
        int count = 0;
        for (AppSvcVehicleDto asv : appSvcVehicleDtoAlls) {
            if (value.equalsIgnoreCase(getValue(asv, name))) {
                count++;
            }
        }
        if (count >= 2) {
            map.put(name + index, MessageUtil.getMessageDesc("NEW_ERR0012"));
        }
    }

    private String getValue(AppSvcVehicleDto asv, String name) {
        if ("vehicleName".equals(name)) {
            return asv.getVehicleName();
        } else if ("chassisNum".equals(name)) {
            return asv.getChassisNum();
        } else if ("engineNum".equals(name)) {
            return asv.getEngineNum();
        }
        return "";
    }

    @Override
    public void doValidateVehicles(Map<String, String> map, AppSubmissionDto appSubmissionDto) {
        if (appSubmissionDto == null) {
            return;
        }
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (appSvcRelatedInfoDtoList == null) {
            return;
        }
        List<AppSvcVehicleDto> appSvcVehicleDtos =IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList)) {
            appSvcRelatedInfoDtoList.stream().forEach(obj -> {
                if (!IaisCommonUtils.isEmpty(obj.getAppSvcVehicleDtoList())) {
                    appSvcVehicleDtos.addAll(obj.getAppSvcVehicleDtoList());
                }
            });
        }
        for (int i = 0; i < appSvcRelatedInfoDtoList.size(); i++) {
            List<AppSvcVehicleDto> appSvcVehicleDtoList = appSvcRelatedInfoDtoList.get(i).getAppSvcVehicleDtoList();
            if (appSvcVehicleDtoList == null) {
                continue;
            }
            doValidateVehicles(map, appSvcVehicleDtos, appSvcVehicleDtoList, null);
        }
    }

}
