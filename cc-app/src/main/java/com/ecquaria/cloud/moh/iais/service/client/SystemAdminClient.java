package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Wenkang
 * @date 2019/11/27 17:11
 */
@FeignClient(name = "system-admin", configuration = FeignConfiguration.class,
        fallback =SystemAdminClientFallback.class)
public interface SystemAdminClient  {
    @RequestMapping(path = "/draft-number/{type}",method = RequestMethod.GET)
    FeignResponseEntity<String> draftNumber(@PathVariable(name = "type")  String applicationType);
    @RequestMapping(path = "/application-number/{type}")
    FeignResponseEntity<String> applicationNumber(@PathVariable(name = "type") String applicationType);
    /*@RequestMapping(path = "/api/postcodes" ,method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PostCodeDto> getPostCodeByCode(@RequestParam(value = "searchField") String fieldName,
                                                       @RequestParam(value = "filterValue") String filterValue);*/

    @RequestMapping(path="//api-postcodes/postal-code", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PostCodeDto> getPostCodeByCode(@RequestParam(value = "postalCode") String postalCode);
}
