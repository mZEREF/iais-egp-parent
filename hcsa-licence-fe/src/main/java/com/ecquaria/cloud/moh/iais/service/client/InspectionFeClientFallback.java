package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;

/**
 * @author Shicheng
 * @date 2020/2/18 11:27
 **/
public class InspectionFeClientFallback implements InspectionFeClient {
    @Override
    public FeignResponseEntity<List<AppPremisesInspecApptDto>> createAppPremisesInspecApptDto(List<AppPremisesInspecApptDto> appPremisesInspecApptDtos) {
        return IaisEGPHelper.getFeignResponseEntity("createAppPremisesInspecApptDto",appPremisesInspecApptDtos);
    }

    @Override
    public FeignResponseEntity<AppPremisesInspecApptDto> updateAppPremisesInspecApptDto(AppPremisesInspecApptDto appPremisesInspecApptDto) {
        return IaisEGPHelper.getFeignResponseEntity("updateAppPremisesInspecApptDto",appPremisesInspecApptDto);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesInspecApptDto>> updateAppPremisesInspecApptDtoList(List<AppPremisesInspecApptDto> appPremisesInspecApptDtos) {
        return IaisEGPHelper.getFeignResponseEntity("updateAppPremisesInspecApptDtoList",appPremisesInspecApptDtos);
    }

    @Override
    public FeignResponseEntity<AppPremisesInspecApptDto> getSpecificDtoByAppPremCorrId(String appPremCorrId) {
        return IaisEGPHelper.getFeignResponseEntity("getSpecificDtoByAppPremCorrId",appPremCorrId);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesInspecApptDto>> getSystemDtosByAppPremCorrId(String appPremCorrId) {
        return IaisEGPHelper.getFeignResponseEntity("getSystemDtosByAppPremCorrId",appPremCorrId);
    }

    @Override
    public FeignResponseEntity<Integer> getMaxReschedulingVersion(String appPremCorrId) {
        return IaisEGPHelper.getFeignResponseEntity("getMaxReschedulingVersion",appPremCorrId);
    }

    @Override
    public FeignResponseEntity<String> deleteByFileReportId(String fileId) {
        return IaisEGPHelper.getFeignResponseEntity("deleteByFileReportId",fileId);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesPreInspectionNcItemDto>> getNcItemDtoListByAppPremNcId(String appNcId) {
        return IaisEGPHelper.getFeignResponseEntity("getNcItemDtoListByAppPremNcId",appNcId);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesInspecApptDto>> getSystemDtosByAppPremCorrIdList(List<String> appPremCorrIds) {
        return IaisEGPHelper.getFeignResponseEntity("getSystemDtosByAppPremCorrIdList",appPremCorrIds);
    }

    @Override
    public FeignResponseEntity<List<AppPremPreInspectionNcDocDto>> deleteNcDocByIds(List<String> ids) {
        return IaisEGPHelper.getFeignResponseEntity("deleteNcDocByIds",ids);
    }

    @Override
    public FeignResponseEntity<AdhocChecklistItemDto> getAdhocChecklistItemById(String id) {
        return IaisEGPHelper.getFeignResponseEntity("getAdhocChecklistItemById",id);
    }

    @Override
    public FeignResponseEntity<List<AppSvcVehicleDto>> getAppSvcVehicleDtoListByCorrId(String appPremCorrId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppSvcVehicleDtoListByCorrId",appPremCorrId);
    }

    @Override
    public FeignResponseEntity<List<AppSvcVehicleDto>> getSvcVehicleDtoListByCorrIdStatus(String appPremCorrId, String status) {
        return IaisEGPHelper.getFeignResponseEntity("getSvcVehicleDtoListByCorrIdStatus",appPremCorrId,status);
    }

    @Override
    public FeignResponseEntity<List<AppSvcVehicleDto>> getVehiclesByCorrIdStatusAct(String appPremCorrId, String status, String actCode) {
        return IaisEGPHelper.getFeignResponseEntity("getVehiclesByCorrIdStatusAct",appPremCorrId,status,actCode);
    }

    @Override
    public FeignResponseEntity<List<AppSvcVehicleDto>> getVehiclesByStatusAct(String status, String actCode) {
        return IaisEGPHelper.getFeignResponseEntity("getVehiclesByStatusAct",status,actCode);
    }
}
