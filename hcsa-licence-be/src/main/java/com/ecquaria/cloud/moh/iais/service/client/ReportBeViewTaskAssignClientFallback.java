package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.SuperPoolTaskQueryDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * @author Shicheng
 * @date 2021/11/29 17:40
 **/
@Component
public class ReportBeViewTaskAssignClientFallback implements ReportBeViewTaskAssignClient {

    @Override
    public FeignResponseEntity<SearchResult<SuperPoolTaskQueryDto>> searchSuperDropPoolResult(SearchParam searchParam) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
