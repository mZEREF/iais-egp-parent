package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;

/**
 * BePremisesRoutingHistoryClientFallback
 *
 * @author suocheng
 * @date 11/26/2019
 */
public class BePremisesRoutingHistoryClientFallback {
    public FeignResponseEntity<AppPremisesRoutingHistoryDto> createAppPremisesRoutingHistory(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto){
        return IaisEGPHelper.getFeignResponseEntity("createAppPremisesRoutingHistory",appPremisesRoutingHistoryDto);
    }
    public FeignResponseEntity<List<AppPremisesRoutingHistoryDto>> getAppPremisesRoutingHistorysById( String appId){
        return IaisEGPHelper.getFeignResponseEntity("getAppPremisesRoutingHistorysById",appId);
    }
    public FeignResponseEntity<AppPremisesRoutingHistoryDto> getAppPremisesRoutingHistorysByAppIdAndStageId(String appId,String stageId){
        return IaisEGPHelper.getFeignResponseEntity("getAppPremisesRoutingHistorysByAppIdAndStageId",appId,stageId);
    }

    public FeignResponseEntity<List<AppPremisesRoutingHistoryDto>> createAppPremisesRoutingHistorys(
            List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos) {
        return IaisEGPHelper.getFeignResponseEntity("createAppPremisesRoutingHistorys",appPremisesRoutingHistoryDtos);
    }
}
