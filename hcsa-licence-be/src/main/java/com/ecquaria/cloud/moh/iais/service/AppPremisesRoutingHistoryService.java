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
    List<AppPremisesRoutingHistoryDto> getAppPremisesRoutingHistoryDtosByCorrId(String corrId);
    public List<AppPremisesRoutingHistoryDto> getAppPremisesRoutingHistoryDtosByAppNoAndRoleIds(String appNo, List<String> roleIds);
    public AppPremisesRoutingHistoryDto getAppPremisesRoutingHistoryForCurrentStage(String appNo, String stageId) ;
    AppPremisesRoutingHistoryDto getActiveAppPremisesRoutingHistoryForCurrentStage(String appNo, String stageId);
    public AppPremisesRoutingHistoryDto getAppPremisesRoutingHistoryForCurrentStage(String appNo, String stageId, String roleId) ;
    public AppPremisesRoutingHistoryDto getAppPremisesRoutingHistoryForCurrentStage(String appNo, String stageId, String roleId, String appStatus) ;
    public AppPremisesRoutingHistoryDto getAppHistoryByAppNoAndActionBy(String appNo,String actionBy);
    public AppPremisesRoutingHistoryDto getSecondRouteBackHistoryByAppNo(String appNo,String status);
}
