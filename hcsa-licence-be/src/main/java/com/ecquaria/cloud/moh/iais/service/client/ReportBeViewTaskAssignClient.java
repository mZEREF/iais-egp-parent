package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.SuperPoolTaskQueryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Shicheng
 * @date 2021/11/29 17:40
 **/
@FeignClient(name = "halp-report", configuration = FeignConfiguration.class,
        fallback = ReportBeViewTaskAssignClientFallback.class)
public interface ReportBeViewTaskAssignClient {
    @PostMapping(value = "/halp-intra-dash/super-pool-drop")
    FeignResponseEntity<SearchResult<SuperPoolTaskQueryDto>> searchSuperDropPoolResult(@RequestBody SearchParam searchParam);
}
