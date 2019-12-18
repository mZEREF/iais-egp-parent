package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.ReqForInfoSearchQueryDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

/**
 * RequestForInformationClientFallback
 *
 * @author junyu
 * @date 2019/12/18
 */
public class RequestForInformationClientFallback {
    public FeignResponseEntity<SearchResult<ReqForInfoSearchQueryDto>> searchRfiApp(SearchParam searchParam){
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
