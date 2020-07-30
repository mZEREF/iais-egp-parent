package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryMainService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryMainClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AppPremisesRoutingHistoryServiceImpl
 *
 * @author suocheng
 * @date 11/26/2019
 */
@Service
@Slf4j
public class AppPremisesRoutingHistoryMainServiceImpl implements AppPremisesRoutingHistoryMainService {
    @Autowired
    private AppPremisesRoutingHistoryMainClient appPremisesRoutingHistoryClient;

    @Override
    public AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto) {
        FeignResponseEntity<AppPremisesRoutingHistoryDto> response = appPremisesRoutingHistoryClient.
                createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);
        return response.getEntity();
        // return RestApiUtil.postGetObject(RestApiUrlConsts.IAIS_APPLICATION_HISTORY,appPremisesRoutingHistoryDto,AppPremisesRoutingHistoryDto.class);
    }

    @Override
    public AppPremisesRoutingHistoryDto getAppPremisesRoutingHistoryForCurrentStage(String appNo, String stageId) {
        return appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorysByAppNoAndStageId(appNo,stageId).getEntity();
    }

    @Override
    public AppPremisesRoutingHistoryDto getAppPremisesRoutingHistoryForCurrentStage(String appNo, String stageId, String roleId) {
        return appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorysByAppNoAndStageId(appNo,stageId,roleId).getEntity();
    }

    @Override
    public AppPremisesRoutingHistoryDto getAppPremisesRoutingHistoryForCurrentStage(String appNo, String stageId, String roleId, String appStatus) {
        return appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorysByAppNoAndStageId(appNo,stageId,roleId,appStatus).getEntity();
    }

    @Override
    public AppPremisesRoutingHistoryDto getSecondRouteBackHistoryByAppNo(String appNo,String status) {
        return appPremisesRoutingHistoryClient.getSecondRouteBackHistoryByAppNo(appNo,status).getEntity();
    }
}
