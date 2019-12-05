package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
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
public class AppPremisesRoutingHistoryServiceImpl implements AppPremisesRoutingHistoryService {
    @Autowired
    private AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient;
    @Autowired
    private ApplicationClient applicationClient;
    @Override
    public AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto) {
//        FeignResponseEntity<AppPremisesRoutingHistoryDto> response = appPremisesRoutingHistoryClient.
//                createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);
//        return response.getEntity();
        return   applicationClient.create(appPremisesRoutingHistoryDto).getEntity();
    }

//    @Override
//    public List<AppPremisesRoutingHistoryDto> createHistorys(List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtoList) {
//        return RestApiUtil.save(RestApiUrlConsts.APPLICATION_HISTORYS,appPremisesRoutingHistoryDtoList,List.class);
//    }

}
