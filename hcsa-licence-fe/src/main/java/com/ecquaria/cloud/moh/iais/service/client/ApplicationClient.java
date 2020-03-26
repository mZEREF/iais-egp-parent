package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDecl;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDeclaration;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
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
    @PostMapping(path = "/iais-application/all-file",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> fileAll(@RequestBody List<String> grpids);

    @GetMapping(value = "/iais-application/rec-datas")
    FeignResponseEntity<String> recDatesToString();

    @GetMapping(value = "/iais-application/rec-file-datas")
    FeignResponseEntity<Map<String, List<AppPremPreInspectionNcDocDto>>> recFileId();

    @RequestMapping(value = "/iais-application/status",produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,method =RequestMethod.PUT)
    FeignResponseEntity<Void> updateStatus(@RequestBody Map<String,List<String>> map);

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
    FeignResponseEntity<List<String>> saveAllSelfDecl(@RequestBody  List<SelfDeclaration> selfDeclList);

    @GetMapping(path = "/iais-application/group/correlation/has-self-decl/{groupId}/record")
    FeignResponseEntity<Boolean> hasSelfDeclRecord(@PathVariable(value = "groupId") String groupId);

    @GetMapping(path = "/iais-self-decl-rfi/rfi-data/{groupId}")
    FeignResponseEntity<List<SelfDecl>> getSelfDeclRfiData(@PathVariable(value = "groupId") String groupId);

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
    FeignResponseEntity<AppGrpPremisesEntityDto> getAppGrpPremise(@PathVariable(name = "appPreId")String appPreId);

    @RequestMapping(path = "/iais-submission/appSubmissionDto/{appNo}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
    FeignResponseEntity<AppSubmissionDto>  getAppSubmissionDtoByAppNo(@PathVariable("appNo") String appNo);

    @GetMapping(path = "/iais-inspection-fe/itemids/{appPremCorrId}", produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<String>> getItemIdsByAppNo(@PathVariable("appPremCorrId") String appPremCorrId);

    @PostMapping(value = "/iais-inspection-fe/appncdocs", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremPreInspectionNcDocDto>> saveAppNcDoc(@RequestBody List<AppPremPreInspectionNcDocDto> dtoList);

    @PutMapping(value = "/iais-inspection-fe/appncdoc", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremPreInspectionNcDocDto> updateAppNcDoc(@RequestBody AppPremPreInspectionNcDocDto appPremPreInspectionNcDocDto);

    @GetMapping(value = "/iais-inspection-fe/nc-doc-list/{id}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremPreInspectionNcDocDto>> getNcDocListByItemId(@PathVariable(name = "id") String id);

    @PutMapping(value = "/iais-inspection-fe/appprempreitemncu", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesPreInspectionNcItemDto> updateAppPreItemNc(@RequestBody AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto);

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
    FeignResponseEntity<ApplicationGroupDto> getApplicationGroup(@PathVariable("groupId") String groupId);

    @GetMapping(value = "/application-renwal-origin-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> applicationIsRenwalByOriginId();

    @GetMapping(path = "/iais-application/RescomDto/{appPremId}/{recomType}",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AppPremisesRecommendationDto> getAppPremRecordByIdAndType(@PathVariable(value ="appPremId" ) String appPremId, @PathVariable(value ="recomType" ) String recomType);

    @PostMapping(path = "/iais-submission/application-rfc", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> saveAppsForRequestForChange(@RequestBody AppSubmissionDto appSubmissionDto);
    
    @GetMapping(path = "/iais-application/application-licenceId", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity< List<ApplicationDto>> getAppByLicIdAndExcludeNew(@RequestParam(name = "licenceId")String licenceId);

    @PostMapping(value = "/iais-application/appGrps-by-ids", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationGroupDto>>getApplicationGroupsByIds(@RequestBody List<String> appGrpIds);

    @GetMapping(value = "/appeal/application-last-version",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> getApplicationDtoByVersion( @RequestParam(name = "applicationNo") String applicationNo);
    @PostMapping(value = "/appeal/application-appeal",consumes = MediaType.APPLICATION_JSON_VALUE,produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppealPageDto> submitAppeal(@RequestBody AppealPageDto appealDto);

    @GetMapping(value = "/appeal/list-hci-name-address",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppInsRepDto> getHciNameAndAddress(@RequestParam("appId") String appId);

    @PostMapping(value = "/iais-submission/application-rfc-licences",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSubmissionDto>> saveAppsForRequestForChangeByList(@RequestBody List<AppSubmissionDto> appSubmissionDtos);

    @GetMapping(value = "/appeal/list-application-by-licene-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> getApplicationsByLicenceId(@RequestParam("licenceId") String licenceId);

    @PostMapping(value = "/iais-submission/application-renew",  consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> saveAppsForRenew(@RequestBody AppSubmissionDto appSubmissionDto);

    @GetMapping(value = "/iais-application/app-one/{appPremCorrId}")
    FeignResponseEntity<ApplicationDto> getApplicationByCorreId(@PathVariable(name = "appPremCorrId")String appPremCorrId);

    @GetMapping(value = "/iais-application/app-group/{appPremCorrId}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<ApplicationDto>> getPremisesApplicationsByCorreId(@PathVariable(name = "appPremCorrId")String appPremCorrId);

    @GetMapping(value = "/iais-application/apppremisescorrelationdto-fe/{appCorreId}", produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<AppPremisesCorrelationDto>> getLastAppPremisesCorrelationDtoByCorreId(@PathVariable("appCorreId") String appCorreId);

    @GetMapping(value = "/appeal/application-premises-misc",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity <AppPremiseMiscDto>getAppPremisesMisc(@RequestParam("correId") String correId);

    @GetMapping(value = "/appeal/application-group-peronnel-by-grp-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSvcCgoDto>> getAppGrpPersonnelByGrpId(@RequestParam("grpId") String grpId);

    @PostMapping(value = "/iais-application/apps-by-licId/{licId}")
    FeignResponseEntity<ApplicationDto> getApplicationByLicId(@PathVariable(name = "licId") String licId);

    @GetMapping(value = "/appeal/list-of-application-group-personnel",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppGrpPersonnelDto>> getAppGrpPersonnelDtosByGrpId(@RequestParam("grpId") String grpId);

    @GetMapping(value = "/appeal/app-premises-special-doc-by-corre-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesSpecialDocDto> getAppPremisesSpecialDocDtoByCorreId(@RequestParam("correld") String correld);

    @PostMapping(value = "/appeal/app-svc-key-person-by-application",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSvcKeyPersonnelDto>> getAppSvcKeyPersonnel(@RequestBody ApplicationDto applicationDto);
    @PostMapping(value = "/file-existence",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ProcessFileTrackDto> isFileExistence(@RequestBody Map<String,String> map);
    @GetMapping(value = "/appeal/reason-used")
    FeignResponseEntity<Boolean> isUseReason(@RequestParam("id") String id,@RequestParam("reason") String reason);
    @GetMapping(value = "/")
    FeignResponseEntity<String> getRequestForInfo(@RequestParam(value = "applicationId") String applicationId);

}
