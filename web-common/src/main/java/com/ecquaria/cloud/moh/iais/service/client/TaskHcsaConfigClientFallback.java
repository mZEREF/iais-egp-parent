package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * TaskHcsaConfigClientFallback
 *
 * @author suocheng
 * @date 12/4/2019
 */
public class TaskHcsaConfigClientFallback {
    public FeignResponseEntity<List<HcsaSvcStageWorkingGroupDto>> getWrkGrp(List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDto){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    public FeignResponseEntity<String> getSendTaskType(SendTaskTypeDto sendTaskTypeDto){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
