package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.http.HttpHeaders;

/**
 * TaskApplicationClientFallback
 *
 * @author suocheng
 * @date 12/4/2019
 */
public class TaskApplicationClientFallback implements TaskApplicationClient{

    @Override
    public FeignResponseEntity<List<AppPremisesCorrelationDto>> getGroupAppsByNo(String appGropId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<AppPremisesCorrelationDto>> getAppPremisesCorrelationsByAppId(String appId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<AppPremisesRoutingHistoryDto>> createAppPremisesRoutingHistorys(List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<List<AppGrpPersonnelDto>> getPersonnelByRoleAndGrpid(List<String> role, String grpid) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
