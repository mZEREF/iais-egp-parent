package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiLicenceQueryDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * RequestForInformationClientFallback
 *
 * @author junyu
 * @date 2019/12/18
 */
@Component
public class RequestForInformationClientFallback implements RequestForInformationClient{

    @Override
    public FeignResponseEntity<SearchResult<RfiLicenceQueryDto>> searchRfiLicence(SearchParam searchParam) {
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }
}
