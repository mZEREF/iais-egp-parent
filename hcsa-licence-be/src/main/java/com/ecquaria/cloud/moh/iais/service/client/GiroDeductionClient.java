package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.giro.GiroDeductionDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author Wenkang
 * @date 2021/3/15 15:31
 */
@FeignClient(url = "http://192.168.7.85:8883",name = "hcsa-application")
public interface GiroDeductionClient {
    @PostMapping(value = "search-realut-giro",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<GiroDeductionDto>> giroDeductionDtoSearchResult(@RequestBody SearchParam searchParam);
}
