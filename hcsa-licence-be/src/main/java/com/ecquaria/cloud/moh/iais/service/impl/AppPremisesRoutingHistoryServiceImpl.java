package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AppPremisesRoutingHistoryServiceImpl
 *
 * @author suocheng
 * @date 11/26/2019
 */
@Service
@Slf4j
public class AppPremisesRoutingHistoryServiceImpl implements AppPremisesRoutingHistoryService {
    @Autowired
    private AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient;

    @Override
    public AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto) {
        FeignResponseEntity<AppPremisesRoutingHistoryDto> response = appPremisesRoutingHistoryClient.
                createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);
        return response.getEntity();
       // return RestApiUtil.postGetObject(RestApiUrlConsts.IAIS_APPLICATION_HISTORY,appPremisesRoutingHistoryDto,AppPremisesRoutingHistoryDto.class);
    }

    @Override
    public List<AppPremisesRoutingHistoryDto> getAppPremisesRoutingHistoryDtosByAppNo(String appNo) {
        return appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorysByAppNo(appNo).getEntity();
    }

    @Override
    public AppPremisesRoutingHistoryDto getAppPremisesRoutingHistoryForCurrentStage(String appNo, String stageId) {
        return appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorysByAppNoAndStageId(appNo,stageId).getEntity();
    }

    @Override
    public AppPremisesRoutingHistoryDto getSecondRouteBackHistoryByAppNo(String appNo,String status) {
        return appPremisesRoutingHistoryClient.getSecondRouteBackHistoryByAppNo(appNo,status).getEntity();
    }

    @Override
    public List<AppPremisesRoutingHistoryDto> createHistorys(List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtoList) {
        return appPremisesRoutingHistoryClient.createAppPremisesRoutingHistorys(appPremisesRoutingHistoryDtoList).getEntity();
    }

}
