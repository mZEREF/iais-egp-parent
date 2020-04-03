package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import java.util.List;

/**
 * AppPremisesRoutingHistoryService
 *
 * @author suocheng
 * @date 11/26/2019
 */
public interface AppPremisesRoutingHistoryService {
    public AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto);
    public List<AppPremisesRoutingHistoryDto> createHistorys(List<AppPremisesRoutingHistoryDto>  appPremisesRoutingHistoryDtoList);
    public List<AppPremisesRoutingHistoryDto> getAppPremisesRoutingHistoryDtosByAppNo(String appNo);
    public AppPremisesRoutingHistoryDto getAppPremisesRoutingHistoryForCurrentStage(String appNo, String stageId) ;
    public AppPremisesRoutingHistoryDto getSecondRouteBackHistoryByAppNo(String appNo);
}
