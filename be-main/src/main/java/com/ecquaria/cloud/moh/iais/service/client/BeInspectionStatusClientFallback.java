package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;

/**
 * @author Shicheng
 * @date 2019/12/10 16:52
 **/
public class BeInspectionStatusClientFallback {
    FeignResponseEntity<List<AppInspectionStatusDto>> getAppInspectionStatusByStatus(String status){
        return IaisEGPHelper.getFeignResponseEntity("getAppInspectionStatusByStatus",status);
    }

    FeignResponseEntity<List<AppInspectionStatusDto>> getAppInspecStatusByIds(String ids){
        return IaisEGPHelper.getFeignResponseEntity("getAppInspecStatusByIds",ids);
    }

    FeignResponseEntity<AppInspectionStatusDto> getAppInspectionStatusByPremId(String appPremCorreId){
        return IaisEGPHelper.getFeignResponseEntity("getAppInspectionStatusByPremId",appPremCorreId);
    }

    FeignResponseEntity<List<AppInspectionStatusDto>> create(List<AppInspectionStatusDto> appInspecStatusDtos){
        return IaisEGPHelper.getFeignResponseEntity("create",appInspecStatusDtos);
    }

    FeignResponseEntity<List<AppInspectionStatusDto>> update(AppInspectionStatusDto appInspecStatusDto){
        return IaisEGPHelper.getFeignResponseEntity("update",appInspecStatusDto);
    }

    FeignResponseEntity<AppInspectionStatusDto> createAppInspectionStatusByAppDto(ApplicationDto applicationDto){
        return IaisEGPHelper.getFeignResponseEntity("createAppInspectionStatusByAppDto",applicationDto);
    }
}
