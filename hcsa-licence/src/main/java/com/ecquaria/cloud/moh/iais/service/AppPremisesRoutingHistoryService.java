package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;

/**
 * AppPremisesRoutingHistoryService
 *
 * @author suocheng
 * @date 11/26/2019
 */
public interface AppPremisesRoutingHistoryService {
    public AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto);
   // public List<AppPremisesRoutingHistoryDto> createHistorys(List<AppPremisesRoutingHistoryDto>  appPremisesRoutingHistoryDtoList);
}
