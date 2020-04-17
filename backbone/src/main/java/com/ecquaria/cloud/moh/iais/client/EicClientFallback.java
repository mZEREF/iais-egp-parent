package com.ecquaria.cloud.moh.iais.client;

import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * AtEicClientFallback
 *
 * @author Jinhua
 * @date 2020/4/17 14:47
 */
public class EicClientFallback {
    public FeignResponseEntity<List<EicRequestTrackingDto>> getPendingRecords(@PathVariable("moduleName") String moduleName) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
