package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.http.HttpHeaders;

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
}
