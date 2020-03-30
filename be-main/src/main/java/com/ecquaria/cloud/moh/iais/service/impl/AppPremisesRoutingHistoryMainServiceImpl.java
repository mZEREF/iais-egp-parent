package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryMainService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryMainClient;
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
    public AppPremisesRoutingHistoryDto getAppPremisesRoutingHistoryForCurrentStage(String appNo, String stageId) {
        return appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorysByAppNoAndStageId(appNo,stageId).getEntity();
    }

}
