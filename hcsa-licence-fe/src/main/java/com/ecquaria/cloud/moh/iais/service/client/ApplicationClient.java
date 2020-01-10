package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDecl;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
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

import java.util.List;
import java.util.Map;

/**
 * @author Wenkang
 * @date 2019/11/26 14:28
 */
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = ApplicationClientFallback.class)
public interface ApplicationClient  {
    @GetMapping(path = "/iais-application/all-file")
    FeignResponseEntity<String> fileAll();

    @GetMapping(value = "/iais-application/rec-datas")
    FeignResponseEntity<String> recDatesToString();

    @GetMapping(value = "/iais-application/rec-file-datas")
    FeignResponseEntity<Map<String, Map<String, AppPremPreInspectionNcDocDto>>> recFileId();

    @PutMapping(path = "/iais-application/status")
    FeignResponseEntity<Void> updateStatus(@RequestParam("status") String status);

    @PutMapping(path = "/iais-application", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> updateApplication(@RequestBody ApplicationDto applicationDto);

    @PostMapping(path = "/iais-application/file-name",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String>  savedFileName(@RequestBody String fileName);

    @GetMapping(path = "/iais-application/application/results-by-groupid/{groupid}")
    FeignResponseEntity<List<ApplicationDto>> listApplicationByGroupId(@PathVariable("groupid") String groupId);

    @GetMapping(value = "/iais-application/applicationdto-id/{appId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> getApplicationById(@PathVariable(name = "appId") String appId);

    @GetMapping(path = "/iais-submission/draft",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto>  draftNumberGet(@RequestParam("draftNumber") String draftNumber);

    @PostMapping(path = "/iais-submission/draft",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto>  saveDraft(@RequestBody AppSubmissionDto appSubmissionDto );

    @PostMapping(path = "/iais-submission", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> saveSubmision(@RequestBody AppSubmissionDto appSubmissionDto);

    @PostMapping(path = "/iais-submission/requestInformation", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> saveReqeustInformationSubmision(@RequestBody AppSubmissionRequestInformationDto appSubmissionRequestInformationDto);

    @GetMapping(path = "/iais-application/application-premises-by-app-id/{applicationId}")
    FeignResponseEntity<AppGrpPremisesDto> getAppGrpPremisesDtoByAppGroId(@PathVariable("applicationId") String applicationId);

    @PostMapping(path = "/iais-application/self-decl", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> saveAllSelfDecl(@RequestBody  List<SelfDecl> selfDeclList);

    @GetMapping(path = "/iais-application/self-decl/{id}")
    FeignResponseEntity<String> getPremisesSelfDeclChklJson(String selfDeclId);

    @GetMapping(path = "/iais-application/application/correlations/{appid}")
    FeignResponseEntity<List<AppPremisesCorrelationDto>> listAppPremisesCorrelation(@PathVariable(name = "appid") String appId);

    @PutMapping(path="/iais-application/app-grp", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> doUpDate(@RequestBody ApplicationGroupDto applicationGroupDto);

    @GetMapping(value = "/iais-submission/submission-id")
    FeignResponseEntity<String> getSubmissionId();

    @GetMapping(value = "/iais-application/application/premises-scope/{correId}")
    FeignResponseEntity<List<AppSvcPremisesScopeDto>> getAppSvcPremisesScopeListByCorreId(@PathVariable(name = "correId") String correId);

    @GetMapping(value = "/iais-application/application/grp-premises/{appPreId}")
    FeignResponseEntity<AppGrpPremisesDto> getAppGrpPremise(@PathVariable(name = "appPreId")String appPreId);

    @RequestMapping(path = "/iais-submission/appSubmissionDto/{appNo}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
    FeignResponseEntity<AppSubmissionDto>  getAppSubmissionDtoByAppNo(@PathVariable("appNo") String appNo);

    @GetMapping(path = "/iais-inspection-fe/itemids/{appNo}", produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<String>> getItemIdsByAppNo(@PathVariable("appNo") String appNo);

    @PostMapping(value = "/iais-inspection-fe/appncdocs", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremPreInspectionNcDocDto>> saveAppNcDoc(@RequestBody List<AppPremPreInspectionNcDocDto> dtoList);

    @PutMapping(value = "/iais-inspection-fe/appncdoc", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremPreInspectionNcDocDto> updateAppNcDoc(@RequestBody AppPremPreInspectionNcDocDto appPremPreInspectionNcDocDto);

    @GetMapping(value = "/iais-inspection-fe/appncdociteid/{ncItemId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremPreInspectionNcDocDto> getNcDocByNcItemId(@PathVariable(name = "ncItemId") String ncItemId);

    @PutMapping(value = "/iais-inspection-fe/appprempreitemncu", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesPreInspectionNcItemDto> updateAppPreItemNc(@RequestBody AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto);

    @GetMapping(value = "/iais-inspection-fe/appncitemdto/{itemId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesPreInspectionNcItemDto> getNcItemByItemId(@PathVariable(name = "itemId") String itemId);

    @GetMapping(value = "/iais-inspection-fe/apprencdto/{preNcId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremPreInspectionNcDto> getPreNcByPreNcId(@PathVariable(name = "preNcId") String preNcId);

    @PostMapping(value = "/iais-inspection-fe/apppremprencdto", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremPreInspectionNcDto> saveAppPremPreNc(@RequestBody AppPremPreInspectionNcDto appPremPreInspectionNcDto);

    @PutMapping(value = "/iais-inspection-fe/apppremprencdtou", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremPreInspectionNcDto> updateAppPremPreNc(@RequestBody AppPremPreInspectionNcDto appPremPreInspectionNcDto);

    @GetMapping(value = "/iais-application/application/{AppNo}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationViewDto> searchAppByNo(@PathVariable("AppNo") String appNo);

    @GetMapping(value = "/iais-inspection-fe/AppPremNcByAppCorrId/{appCorrId}",produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremPreInspectionNcDto> getAppPremPreInsNcDtoByAppCorrId(@PathVariable(name = "appCorrId") String appCorrId);

    @PostMapping(value = "/iais-inspection-fe/apppremncitemdtoc",produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesPreInspectionNcItemDto> createAppNcItemDto(@RequestBody AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto);

    @GetMapping(path = "/iais-application/application/grp/{groupId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationGroupDto> getApplicarionGroup(@PathVariable("groupId") String groupId);

    @GetMapping(value = "/application-renwal-origin-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> applicationIsRenwalByOriginId();

    @GetMapping(path = "/iais-application/RescomDto/{appPremId}/{recomType}",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremisesRecommendationDto> getAppPremRecordByIdAndType(@PathVariable(value ="appPremId" ) String appPremId, @PathVariable(value ="recomType" ) String recomType);

}
