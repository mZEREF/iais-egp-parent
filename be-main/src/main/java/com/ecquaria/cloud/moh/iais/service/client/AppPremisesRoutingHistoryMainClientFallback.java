package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryExtDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import java.util.Map;


/**
 * AppPremisesRoutingHistoryMainClientFallback
 *
 * @author suocheng
 * @date 11/26/2019
 */
public class AppPremisesRoutingHistoryMainClientFallback implements AppPremisesRoutingHistoryMainClient {

    @Override
    public FeignResponseEntity<AppPremisesRoutingHistoryDto> getAppPremisesRoutingHistorysByAppNoAndStageId(String appNo, String stageId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremisesRoutingHistorysByAppNoAndStageId",appNo,stageId);
    }

    @Override
    public FeignResponseEntity<AppPremisesRoutingHistoryDto> getAppPremisesRoutingHistorysByAppNoAndStageId(String appNo, String stageId, String roleId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremisesRoutingHistorysByAppNoAndStageId",appNo,stageId,roleId);
    }

    @Override
    public FeignResponseEntity<AppPremisesRoutingHistoryDto> getAppPremisesRoutingHistorysByAppNoAndStageId(String appNo, String stageId, String roleId, String appStatus) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremisesRoutingHistorysByAppNoAndStageId",appNo,stageId,roleId,appStatus);
    }

    @Override
    public FeignResponseEntity<AppPremisesRoutingHistoryDto> createAppPremisesRoutingHistory(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto) {
        return IaisEGPHelper.getFeignResponseEntity("createAppPremisesRoutingHistory",appPremisesRoutingHistoryDto);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesRoutingHistoryDto>> getAppPremisesRoutingHistorysByAppNo(String appNo) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremisesRoutingHistorysByAppNo",appNo);
    }

    @Override
    public FeignResponseEntity<List<AppPremisesRoutingHistoryDto>> createAppPremisesRoutingHistorys(List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos) {
        return IaisEGPHelper.getFeignResponseEntity("createAppPremisesRoutingHistorys",appPremisesRoutingHistoryDtos);
    }

    @Override
    public FeignResponseEntity<AppPremisesRoutingHistoryDto> getAppPremisesRoutingHistorySubStage(String corrId, String stageId) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremisesRoutingHistorySubStage",corrId,stageId);
    }

    @Override
    public FeignResponseEntity<AppPremisesRoutingHistoryDto> getSecondRouteBackHistoryByAppNo(String appNo, String status) {
        return IaisEGPHelper.getFeignResponseEntity("getSecondRouteBackHistoryByAppNo",appNo,status);
    }

    @Override
    public FeignResponseEntity<AppPremisesRoutingHistoryExtDto> getAppPremisesRoutingHistoryExtByHistoryAndComponentName(String appPremRhId, String componentName) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremisesRoutingHistoryExtByHistoryAndComponentName",appPremRhId,componentName);
    }

    @Override
    public FeignResponseEntity<Map<String, AppPremisesRoutingHistoryDto>> getAppPremisesRoutingHistoriesSubStage(Map<String, String> paramMap) {
        return IaisEGPHelper.getFeignResponseEntity("getAppPremisesRoutingHistoriesSubStage",paramMap);
    }
}
