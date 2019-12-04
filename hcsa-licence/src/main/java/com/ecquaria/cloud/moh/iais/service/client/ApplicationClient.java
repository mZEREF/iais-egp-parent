package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationListDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * ApplicationClient
 *
 * @author suocheng
 * @date 11/26/2019
 */
@FeignClient(name = "iais-application", configuration = FeignConfiguration.class,
        fallback = ApplicationClientFallback.class)
public interface ApplicationClient {
    @RequestMapping(path = "/iais-application-be/applicationview/{appNo}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationViewDto> getAppViewByNo(@PathVariable("appNo") String appNo);
    @RequestMapping(path = "/iais-application-be/application/{appNo}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> getAppByNo(@PathVariable("appNo") String appNo);
    @RequestMapping(path = "/iais-application/files",method = RequestMethod.POST,produces = MediaType.TEXT_HTML_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> getDownloadFile( @RequestBody ApplicationListDto applicationListDtos);
    @RequestMapping(path = "/iais-application/list-application-dto",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> getApplicationDto();
}
