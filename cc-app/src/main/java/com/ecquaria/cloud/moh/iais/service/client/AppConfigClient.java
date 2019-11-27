package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import java.util.Set;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * AppConfigClient
 *
 * @author Jinhua
 * @date 2019/11/19 9:47
 */
@FeignClient(name = "hcsa-config", configuration = FeignConfiguration.class,
        fallback = AppConfigClientFallback.class)
public interface AppConfigClient {
    @RequestMapping(path = "/iais-hcsa-service/hcsa-service-by-ids",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<HcsaServiceDto>> getHcsaServiceDtosById(List<String> ids);
    @RequestMapping(path = "/iais-hcsa-service/all-service",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceDto>>  allHcsaService();
    @RequestMapping(path = "/iais-hcsa-checklist/config/self-desc-results",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult> listSelfDescConfig(@RequestBody SearchParam searchParam);
    @RequestMapping(path = "/iais-hcsa-service/hcsa-service-by-ids",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<HcsaServiceDto>> getHcsaService(@RequestBody List<String> serviceId);
    @RequestMapping(path = "/iais-hcsa-service/application-type-by-ids",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Set<String>> getAppGrpPremisesTypeBySvcId(@RequestBody List<String> serviceId);
    @RequestMapping(path = "/iais-hcsa-service/service/{code}",method = RequestMethod.GET)
    FeignResponseEntity<String>  getServiceIdByCode(@PathVariable(name = "code")String svcCode);
    @RequestMapping(path = "/iais-hcsa-service/svc-doc-config-results",method = RequestMethod.GET)
    FeignResponseEntity<List<HcsaSvcDocConfigDto>> getHcsaSvcDocConfig(String serviceId,@RequestParam(name="flag") Boolean flag);
}
