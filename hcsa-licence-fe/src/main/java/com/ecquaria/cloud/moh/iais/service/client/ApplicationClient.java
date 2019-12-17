package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDecl;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Wenkang
 * @date 2019/11/26 14:28
 */
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = ApplicationClientFallback.class)
public interface ApplicationClient  {
    @GetMapping(path = "/iais-application/all-file")
    FeignResponseEntity<String> fileAll();

    @PutMapping(path = "/iais-application/status")
    FeignResponseEntity<Void> updateStatus();

    @PostMapping(path = "/iais-application/file-name",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String>  savedFileName(@RequestBody String fileName);

    @GetMapping(path = "/iais-application/application/results-by-groupid/{groupid}")
    FeignResponseEntity<List<ApplicationDto>> listApplicationByGroupId(@PathVariable("groupid") String groupId);

    @GetMapping(path = "/iais-submission/draft",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto>  draftNumberGet(@RequestParam("draftNumber") String draftNumber);

    @PostMapping(path = "/iais-submission/draft",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto>  saveDraft(@RequestBody AppSubmissionDto appSubmissionDto );

    @PostMapping(path = "/iais-submission", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> saveSubmision(@RequestBody AppSubmissionDto appSubmissionDto);

    @GetMapping(path = "/iais-application/application-premises-by-app-id/{applicationId}")
    FeignResponseEntity<AppGrpPremisesDto> getAppGrpPremisesDtoByAppGroId(@PathVariable("applicationId") String applicationId);

    @PostMapping(path = "/iais-application/self-decl", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveSelfDecl(@RequestBody  List<SelfDecl> selfDeclList);

    @GetMapping(path = "/iais-application/application/correlations/{appid}")
    FeignResponseEntity<List<AppPremisesCorrelationDto>> listAppPremisesCorrelation(@PathVariable(name = "appid") String appId);

    @PutMapping(path="/iais-application/app-grp")
    FeignResponseEntity<String> doUpDate(@RequestBody ApplicationGroupDto applicationGroupDto);

    @GetMapping(value = "/iais-submission/submission-id")
    FeignResponseEntity<String> getSubmissionId();

    @GetMapping(value = "/iais-application/application/premises-scope/{correId}")
    FeignResponseEntity<List<AppSvcPremisesScopeDto>> getAppSvcPremisesScopeListByCorreId(@PathVariable(name = "correId") String correId);

    @RequestMapping(path = "/iais-submission/appSubmissionDto/{appId}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
    FeignResponseEntity<AppSubmissionDto>  getAppSubmissionDtoByAppId(@PathVariable("appId") String appId);
}
