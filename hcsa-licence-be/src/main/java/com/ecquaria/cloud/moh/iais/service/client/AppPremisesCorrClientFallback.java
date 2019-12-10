package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.http.HttpHeaders;

/**
 * AppPremisesCorrClientFallback
 *
 * @author Jinhua
 * @date 2019/12/10 19:54
 */
public class AppPremisesCorrClientFallback {
    public FeignResponseEntity<List<AppPremisesCorrelationDto>> getGroupAppsByNo(String appGropId) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
