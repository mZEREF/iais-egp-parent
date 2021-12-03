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
import com.ecquaria.cloud.moh.iais.validate.ValidateFlow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Wenkang
 * @date 2021/4/25 14:06
 */
@Component
@Slf4j
public class ValidateVehicle implements ValidateFlow {

    private static final String VEHICLE_NAME = "vehicleName";
    private static final String ENGINE_NAME = "engineNum";
    private static final String CHASSIS_NAME = "chassisNum";

    @Override
    public void doValidateVehicles(Map<String, String> errorMap, List<AppSvcVehicleDto> appSvcVehicleDtoAlls,
            List<AppSvcVehicleDto> appSvcVehicleDtos, List<AppSvcVehicleDto> oldAppSvcVehicleDto) {
        if (appSvcVehicleDtos == null) {
            return;
        }
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        List<String> vehicleNumList = new ArrayList<>(appSvcVehicleDtos.size());
        List<String> chassisNumList = new ArrayList<>(appSvcVehicleDtos.size());
        List<String> engineNumNumList = new ArrayList<>(appSvcVehicleDtos.size());
        for (int i = 0; i < appSvcVehicleDtos.size(); i++) {
            AppSvcVehicleDto currentDto = appSvcVehicleDtos.get(i);
            String vehicleNum = currentDto.getVehicleNum();
            if (!currentDto.isDummyVehNum() && StringUtil.isNotEmpty(vehicleNum)){
                if (vehicleNum.length() > 10) {
                    map.put(VEHICLE_NAME + i, NewApplicationHelper.repLength("Vehicle Number", "10"));
                } else if (!VehNoValidator.validateNumber(vehicleNum)) {
                    map.put(VEHICLE_NAME + i, "GENERAL_ERR0017");
                } else {
                    vehicleNum = vehicleNum.toLowerCase(AppConsts.DFT_LOCALE);
                    if (vehicleNumList.contains(vehicleNum)) {
                        map.put(VEHICLE_NAME + i, MessageUtil.getMessageDesc("NEW_ERR0012"));
                    } else {
                        vehicleNumList.add(vehicleNum);
                    }
                }
            }

            String chassisNum = currentDto.getChassisNum();
            if (StringUtil.isEmpty(chassisNum)) {
                map.put(CHASSIS_NAME + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Chassis Number", "field"));
            } else if (chassisNum.length() > 25) {
                map.put(CHASSIS_NAME + i, NewApplicationHelper.repLength("Chassis Number", "25"));
            } else {
                chassisNum = chassisNum.toLowerCase(AppConsts.DFT_LOCALE);
                if (chassisNumList.contains(chassisNum)) {
                    map.put(CHASSIS_NAME + i, MessageUtil.getMessageDesc("NEW_ERR0012"));
                } else {
                    chassisNumList.add(chassisNum);
                }
            }

            String engineNum = currentDto.getEngineNum();
            if (StringUtil.isEmpty(engineNum)) {
                map.put(ENGINE_NAME + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Engine Number", "field"));
            } else if (engineNum.length() > 25) {
                map.put(ENGINE_NAME + i, NewApplicationHelper.repLength("Engine Number", "25"));
            } else {
                engineNum = engineNum.toLowerCase(AppConsts.DFT_LOCALE);
                if (engineNumNumList.contains(engineNum)) {
                    map.put(ENGINE_NAME + i, MessageUtil.getMessageDesc("NEW_ERR0012"));
                } else {
                    engineNumNumList.add(engineNum);
                }
            }
        }
        // validate for other services and existed db
        for (int i = 0; i < appSvcVehicleDtos.size(); i++) {
            AppSvcVehicleDto currentDto = appSvcVehicleDtos.get(i);
            validateCurrentVehicle(map, VEHICLE_NAME, currentDto, i, appSvcVehicleDtoAlls);
            validateCurrentVehicle(map, CHASSIS_NAME, currentDto, i, appSvcVehicleDtoAlls);
            validateCurrentVehicle(map, ENGINE_NAME, currentDto, i, appSvcVehicleDtoAlls);
            validateExistVehicle(map, VEHICLE_NAME, currentDto, i, oldAppSvcVehicleDto);
            validateExistVehicle(map, CHASSIS_NAME, currentDto, i, oldAppSvcVehicleDto);
            validateExistVehicle(map, ENGINE_NAME, currentDto, i, oldAppSvcVehicleDto);
        }
        log.info(StringUtil.changeForLog("Validate Vehicles ->" + JsonUtil.parseToJson(map)));
        errorMap.putAll(map);
    }

    private void validateExistVehicle(Map<String, String> map, String name, AppSvcVehicleDto currentDto, int index,
            List<AppSvcVehicleDto> oldAppSvcVehicleDto) {
        if (oldAppSvcVehicleDto == null || oldAppSvcVehicleDto.isEmpty()) {
            return;
        }
        String value = getValue(currentDto, name);
        if (oldAppSvcVehicleDto.stream().anyMatch(asv -> value.equalsIgnoreCase(getValue(asv, name)))) {
            map.put(name + index, MessageUtil.getMessageDesc("NEW_ERR0012"));
        }
    }

    private void validateCurrentVehicle(Map<String, String> map, String name, AppSvcVehicleDto currentDto, int index,
            List<AppSvcVehicleDto> appSvcVehicleDtoAlls) {
        String value = getValue(currentDto, name);
        long count = appSvcVehicleDtoAlls.stream().filter(asv -> value.equalsIgnoreCase(getValue(asv, name))).count();
        if (count >= 1) {
            map.put(name + index, MessageUtil.getMessageDesc("NEW_ERR0012"));
        }
    }

    private String getValue(AppSvcVehicleDto asv, String name) {
        String val = null;
        if (VEHICLE_NAME.equals(name)) {
            if (asv.isDummyVehNum()){
                val = asv.getVehicleName();
            }else {
                val = asv.getVehicleNum();
            }
        } else if (CHASSIS_NAME.equals(name)) {
            val = asv.getChassisNum();
        } else if (ENGINE_NAME.equals(name)) {
            val = asv.getEngineNum();
        }
        return Optional.ofNullable(val).orElseGet(() -> "");
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
        List<AppSvcVehicleDto> appSvcVehicleDtos = IaisCommonUtils.genNewArrayList();
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
