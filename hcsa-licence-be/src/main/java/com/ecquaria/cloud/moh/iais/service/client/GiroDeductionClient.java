package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.giro.GiroDeductionDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author Wenkang
 * @date 2021/3/15 15:31
 */
@FeignClient(name = "iais-payment",url = "192.168.6.117:8881")
public interface GiroDeductionClient {
    @PutMapping(value = "iais-payment/update-giro-realut-status-by-groups",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<GiroDeductionDto>> updateDeductionDtoSearchResultUseGroups(@RequestBody List<GiroDeductionDto> giroDeductionDto);

}
