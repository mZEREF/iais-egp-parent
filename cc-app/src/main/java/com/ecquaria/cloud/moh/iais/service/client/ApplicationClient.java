package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author Wenkang
 * @date 2019/11/26 14:28
 */
@FeignClient(name = "iais-application", configuration = FeignConfiguration.class,
        fallback = ApplicationClientFallback.class)
public interface ApplicationClient  {
    @RequestMapping(path = "/iais-application/all-file",method = RequestMethod.GET)
    FeignResponseEntity<String> fileAll();
    @RequestMapping(path = "/iais-application/status",method = RequestMethod.PUT)
    FeignResponseEntity<Boolean> updateStatus();
    @RequestMapping(path = "/iais-application/file-name",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String>  savedFileName(@RequestBody String fileName);
    @RequestMapping(path = "/application/results-by-groupid/{groupid}" ,method = RequestMethod.GET)
    FeignResponseEntity<List<ApplicationDto>> listApplicationByGroupId(@PathVariable("groupid") String groupId);
    @RequestMapping(path = "/iais-submission/draft",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto>  draftNumberGet(@RequestParam("draftNumber") String draftNumber);
    @RequestMapping(path = "/iais-submission/draft",method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto>  saveDraft(@RequestBody AppSubmissionDto appSubmissionDto );
    @RequestMapping(path = "/iais-submission",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> saveSubmision(@RequestBody AppSubmissionDto appSubmissionDto);

}
