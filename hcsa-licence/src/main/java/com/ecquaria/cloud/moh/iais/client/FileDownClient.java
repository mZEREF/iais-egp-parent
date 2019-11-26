package com.ecquaria.cloud.moh.iais.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * @author Wenkang
 * @date 2019/11/26 10:27
 */
@FeignClient(name = "iais-application", configuration = FeignConfiguration.class,
        fallback =FileDownClientFallback.class)
public interface FileDownClient {
    @RequestMapping(path = "/iais-application/files",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity  <Boolean> getDownloadFile(@RequestBody String file);
    @RequestMapping(path = "/iais-application/list-application-dto" ,method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity  <List<ApplicationDto>>getApplicationDto();
}
