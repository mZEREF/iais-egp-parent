package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;

/**
 * @author Shicheng
 * @date 2021/6/8 10:23
 **/
public class AppSvcVehicleBeMainClientFallBack implements AppSvcVehicleBeMainClient {
    @Override
    public FeignResponseEntity<List<AppSvcVehicleDto>> createAppSvcVehicleDtoList(List<AppSvcVehicleDto> appSvcVehicleDtos) {
        return IaisEGPHelper.getFeignResponseEntity("createAppSvcVehicleDtoList",appSvcVehicleDtos);
    }

    @Override
    public FeignResponseEntity<List<AppSvcVehicleDto>> getAppSvcVehicleDtoListByCorrId(String appPremCorrId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppSvcVehicleDtoListByCorrId",appPremCorrId);
    }

    @Override
    public FeignResponseEntity<List<AppSvcVehicleDto>> getSvcVehicleDtoListByCorrIdStatus(String appPremCorrId, String status) {
        return IaisEGPHelper.getFeignResponseEntity("getSvcVehicleDtoListByCorrIdStatus",appPremCorrId,status);
    }
}
