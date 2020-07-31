package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.SendTaskTypeDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

import java.util.List;

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
