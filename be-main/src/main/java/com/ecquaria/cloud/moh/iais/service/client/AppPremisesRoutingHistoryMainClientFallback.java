package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * AppPremisesRoutingHistoryMainClientFallback
 *
 * @author suocheng
 * @date 11/26/2019
 */
public class AppPremisesRoutingHistoryMainClientFallback implements AppPremisesRoutingHistoryMainClient {

    @Override
    public FeignResponseEntity<AppPremisesRoutingHistoryDto> getAppPremisesRoutingHistorysByAppNoAndStageId(String appNo, String stageId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
