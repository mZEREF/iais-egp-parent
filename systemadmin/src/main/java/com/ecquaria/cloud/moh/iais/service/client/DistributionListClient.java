package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.system.DistributionListDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.DistributionListWebDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author guyin
 * @date 2019/12/28 10:45
 */
@FeignClient(name = "system-admin", configuration = FeignConfiguration.class, fallback = IntranetUserClientFallback.class)
public interface DistributionListClient {


    @PostMapping(value = "/iais-emails/getDistributionList",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<DistributionListDto>> getDistributionList(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/iais-emails/getDistributionListNoParam",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<DistributionListWebDto>> getDistributionListNoParam(@RequestParam("mode") String mode);

    @PostMapping(value = "/iais-emails/saveDistributionList", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<DistributionListWebDto> saveDistributionList(@RequestBody DistributionListWebDto distributionListDto);

    @PostMapping(value = "/iais-emails/deleteDistributionList", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> deleteDistributionList(@RequestBody List<String> list);

    @PostMapping(value = "/iais-emails/getDistributionListById", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<DistributionListWebDto> getDistributionListById(@RequestParam("id") String id);


}
