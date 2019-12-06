package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author Wenkang
 * @date 2019/12/4 16:02
 */
@FeignClient(name = "iais-hcsa-licence", configuration = FeignConfiguration.class,
        fallback = HcsaLicenceClientFallback.class)
public interface HcsaLicenceClient {
    @RequestMapping(path = "/hcsa-licence/hci-code-licence-number/{hciCode}",method = RequestMethod.GET)
    FeignResponseEntity<Integer> licenceNumber(@PathVariable("hciCode") String hciCode);
    @RequestMapping(path = "/hcsa-licence/service-group-licence-number/{serivceCode}",method = RequestMethod.GET)
    FeignResponseEntity<String > groupLicenceNumber(@PathVariable("serivceCode") String groupLicence);
    @RequestMapping(path = "/hcsa-licence-transport/licences",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceGroupDto>> createLicence(@RequestBody List<LicenceGroupDto> licenceGroupDtoList);

    @RequestMapping(path = "/hcsa-premises/latestVersion/{hciCode}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PremisesDto> getLatestVersionPremisesByHciCode(@PathVariable(name = "hciCode") String hciCode);

    @GetMapping(path = "/iais-hcsa-service/one-of-hcsa-service/{serviceId}", produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<HcsaServiceDto> getHcsaServiceDtoByServiceId(@PathVariable(value = "serviceId") String serviceId);
}
