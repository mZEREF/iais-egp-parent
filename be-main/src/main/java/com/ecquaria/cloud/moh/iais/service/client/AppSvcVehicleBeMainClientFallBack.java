package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;

/**
 * @author Shicheng
 * @date 2021/6/8 10:23
 **/
public class AppSvcVehicleBeMainClientFallBack implements AppSvcVehicleBeMainClient {
    @Override
    public FeignResponseEntity<List<AppSvcVehicleDto>> createAppSvcVehicleDtoList(List<AppSvcVehicleDto> appSvcVehicleDtos) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<AppSvcVehicleDto> updateAppSvcVehicleDto(AppSvcVehicleDto appSvcVehicleDto) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<AppSvcVehicleDto>> getAppSvcVehicleDtoListByCorrId(String appPremCorrId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<AppSvcVehicleDto>> getSvcVehicleDtoListByCorrIdStatus(String appPremCorrId, String status) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
