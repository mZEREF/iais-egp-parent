package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ReschApptGrpPremsQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesSelfDeclChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionForAuditDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationListFileDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.NotificateApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RequestInformationSubmitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.SelfAssMtEmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiApplicationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
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
 * ApplicationClient
 *
 * @author suocheng
 * @date 11/26/2019
 */
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = ApplicationClientFallback.class)
public interface ApplicationClient {
    @RequestMapping(path = "/iais-application-be/applicationview/{correlationId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationViewDto> getAppViewByCorrelationId(@PathVariable("correlationId") String correlationId);
    @RequestMapping(path = "/iais-application-be/application/{appNo}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> getAppByNo(@PathVariable("appNo") String appNo);
    @RequestMapping(path = "/iais-application/files",method = RequestMethod.POST,produces = MediaType.TEXT_HTML_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> getDownloadFile( @RequestBody ApplicationListFileDto applicationListDtos);
    @PostMapping (value = "/iais-application/parse",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity< List<ApplicationListFileDto>> parse(@RequestBody String str);
    @RequestMapping(path = "/iais-application/list-application-dto",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> getApplicationDto();
    @RequestMapping(path = "/iais-application-group-be/{appGroupId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationGroupDto> getAppById(@PathVariable("appGroupId") String appGroupId);
    @RequestMapping(path = "/iais-application-group-be",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationGroupDto> updateApplication(@RequestBody ApplicationGroupDto applicationGroupDto);
    @RequestMapping(path = "/iais-application-be/applications/{appGropId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> getGroupAppsByNo(@PathVariable("appGropId") String appGropId);
    @RequestMapping(path = "/iais-application-be",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> updateApplication(@RequestBody ApplicationDto applicationDto);
    @RequestMapping(path = "/iais-application-history",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesRoutingHistoryDto> create(@RequestBody AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto );

    @DeleteMapping(value = "/iais-inspection/sc-history/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> removeHistoryById(@PathVariable("id")String id);

    @PostMapping(value = "/iais-application-history/pre-insp-history", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesRoutingHistoryDto>> getSubmitPreInspHistory(@RequestBody AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto);

    @RequestMapping(path = "/iais-application-be/application-group",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationLicenceDto>> getGroup(@RequestParam(name = "day",required = false)  Integer day);

    @RequestMapping(path = "/iais-application-group-be/groups",method = RequestMethod.PUT,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationGroupDto>> updateApplications(@RequestBody List<ApplicationGroupDto> applicationGroupDtos);

    @GetMapping(value = "/iais-application-be/application/premises-scope/{correId}")
    FeignResponseEntity<List<AppSvcPremisesScopeDto>> getAppSvcPremisesScopeListByCorreId(@PathVariable(name = "correId") String correId);

    @RequestMapping(path = "/iais-broadcast",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<BroadcastApplicationDto> createBroadcast(@RequestBody BroadcastApplicationDto broadcastApplicationDto);

    @PostMapping(value = "/iais-adhoc-checklist-conifg/adhoc-items", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AdhocCheckListConifgDto> saveAdhocChecklist(@RequestBody AdhocCheckListConifgDto adhocConfigDto);

    @GetMapping(value = "/iais-adhoc-checklist-conifg/adhoc-checklist/config/{appremId}/results",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AdhocChecklistItemDto>> getAdhocByAppPremCorrId(@PathVariable(name = "appremId") String appremId);

    @GetMapping(value = "/iais-adhoc-checklist-conifg/all-version-show/results/{appremId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AdCheckListShowDto>> getAllVersionAdhocList(@PathVariable(name = "appremId") String appremId);

    @PostMapping(path = "/iais-adhoc-checklist-conifg/singleconfig",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AdhocCheckListConifgDto> saveAppAdhocConfig(@RequestBody AdhocCheckListConifgDto adhocCheckListConifgDto);

    @PutMapping(path = "/iais-adhoc-checklist-conifg/singleconfigupdate",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<AdhocCheckListConifgDto> updateAppAdhocConfig(@RequestBody AdhocCheckListConifgDto adhocCheckListConifgDto);


    @GetMapping(value = "/iais-adhoc-checklist-conifg/adhoc-checklist/config/{premId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AdhocCheckListConifgDto> getAdhocConfigByAppPremCorrId(@PathVariable(name = "premId") String appremId);

    @GetMapping(value = "/iais-adhoc-checklist-conifg/adhoc-check-config-for-draft/{premId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AdhocCheckListConifgDto>> getAdhocCheckConfigFOrDraftById(@PathVariable(name = "premId") String appremId);

    @PostMapping(path = "/iais-adhoc-checklist-conifg/itemstorage",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<AdhocChecklistItemDto>> saveAdhocItems(@RequestBody List<AdhocChecklistItemDto> itemDtoList);

    @GetMapping(value = "/iais-licence-view/appSubmissionDto/{appId}",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> getAppSubmissionByAppId(@PathVariable("appId") String appId);
    @GetMapping(value = "/iais-licence-view/appSubmissionDto-old-application-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> getAppSubmissionByoldAppId(@RequestParam("oldAppId")String oldAppId);
    @PostMapping (path = "/iais-application-be/app-rfi-param",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<RfiApplicationQueryDto>> searchApp(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/iais-application/files-rec-inspec", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> saveInspecRecDate(@RequestBody ApplicationListFileDto applicationListFileDto);

    @GetMapping(value = "/iais-inspection/appdto/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> getApplicationById(@PathVariable(name = "id") String id);

    @GetMapping(value = "/iais-application/list-request-inf-application-dto",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto >> getRequesForInfList();

    @PutMapping(path = "/iais-application/status")
    FeignResponseEntity<Void> updateStatus(@RequestParam("status") String status);

    @PostMapping (path = "/iais-broadcast/RequestInformationSubmitDtos",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<RequestInformationSubmitDto>> getRequestInformationSubmitDto(@RequestBody List<ApplicationDto> applicationDtos);

    @GetMapping(value = "/iais-apppremisescorrelation-be/apppremisescorrelationdto/{appCorreId}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesCorrelationDto> getLastAppPremisesCorrelationDtoByCorreId(@PathVariable("appCorreId") String appCorreId);
    @PostMapping(value = "/iais-application-be/application-last-app-no",consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> getLastApplicationByAppNo(@RequestBody ApplicationDto applicationDto);

    @PostMapping(path = "/iais-licence-view/app-edit-select",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppEditSelectDto> createAppEditSelectDto(@RequestBody AppEditSelectDto appEditSelectDto);

    @GetMapping(value = "/iais-application-be/app-prem-corr/{appId}")
    FeignResponseEntity<AppPremisesCorrelationDto> getAppPremisesCorrelationDtosByAppId(@PathVariable(name = "appId") String appId);

    @GetMapping(value = "/iais-application-be/appGrps-pay-success",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationGroupDto>> getAppGrpsPaySuc();

    @GetMapping(value = "/iais-application-be/applications/{appGrpId}",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>>getAppDtosByAppGrpId(@PathVariable(name = "appGrpId") String appGrpId);

    @GetMapping(value = "/iais-application-be/app-reject",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>>getAppDtosReject();

    @GetMapping(value = "/iais-application-be/cessation/date-type/{type}/{date}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesCorrelationDto>> getAppPreCorrDtos(@PathVariable(name = "type") String type, @PathVariable(name = "date") String date);

    @PutMapping(value = "/iais-application-be/cessation-application",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> updateCessationApplications(@RequestBody List<ApplicationDto> applicationDtos);

    @PostMapping(value = "/iais-application-be/cessation-applicationDtos-ids",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> getApplicationDtosByIds(@RequestBody List<String> ids);

    @PostMapping(value = "/iais-appt-inspec-be", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesInspecApptDto>> createAppPremisesInspecApptDto(@RequestBody List<AppPremisesInspecApptDto> appPremisesInspecApptDtos);

    @PutMapping(value = "/iais-appt-inspec-be", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesInspecApptDto> updateAppPremisesInspecApptDto(@RequestBody AppPremisesInspecApptDto appPremisesInspecApptDto);

    @GetMapping(value = "/iais-application-be/appCount/{groupId}/{status}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Integer> getAppCountByGroupIdAndStatus(@PathVariable(name = "groupId") String groupId,
                                                               @PathVariable(name = "status") String status);

    @GetMapping(value = "/iais-licence-view/app-edit-select-by-type",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppEditSelectDto>> getAppEditSelectDto(@RequestParam(name="appId")String appId, @RequestParam(name = "changeType")String changeType);

    @PostMapping(value = "/iais-application-be/apps-by-licId/{licId}")
    FeignResponseEntity<ApplicationDto> getApplicationByLicId(@PathVariable(name = "licId") String licId);

    @GetMapping(value = "/iais-application-be/v1/self-decl/user-account/")
    FeignResponseEntity<List<ApplicationGroupDto>> getUserAccountByNotSubmittedSelfDecl();

    @PostMapping(value = "/iais-application-be/app-service-ids", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, String>> getServiceIdsByCorrIdsFromPremises(@RequestBody List<String> appPremCorrIds);

    @PostMapping(value = "/file-existence",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ProcessFileTrackDto> isFileExistence(@RequestBody Map<String,String> map);

    @PostMapping(value = "/iais-application-be/submision-post-inspection",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> saveSubmision(@RequestBody AppSubmissionDto appSubmissionDto );

    @PostMapping(value = "/iais-application-be/submision-apps",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> saveApps(@RequestBody AppSubmissionDto appSubmissionDto );

    @PostMapping(value = "/iais-apppreinsncitem-be/nc-doc-zip", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, List<AppPremPreInspectionNcDocDto>>> zipIsReadyByApplicationId(@RequestBody List<ProcessFileTrackDto> processFileTrackDtos);
    @PostMapping(value = "/iais-application-be/application-rfc-licences",  consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSubmissionDto>> saveAppsByPostInspection(@RequestBody List<AppSubmissionDto> appSubmissionDtos);

    @GetMapping(value = "/iais-application-be/prem-corr-list/{appGroupId}")
    FeignResponseEntity<List<AppPremisesCorrelationDto>> getPremCorrDtoByAppGroupId(@PathVariable(name = "appGroupId") String appGroupId);

    @GetMapping(value = "/iais-apppremisescorrelation-be/app-prem-corrone/{appNo}", produces = MediaType.APPLICATION_JSON_VALUE ,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesCorrelationDto> getAppPremCorrByAppNo(@PathVariable("appNo") String appNo);

    @GetMapping(value = "/iais-application-be/application-post",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> getPostApplication(@RequestParam(name = "appType") String appType,@RequestParam(name = "appStatus") String appStatus);

    @PostMapping(value = "/iais-application-be/all-app-data",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> getBeData(@RequestBody List<String> grpids);

    @PutMapping(value = "/iais-application-be/application-be-withdrawal")
    FeignResponseEntity<List<ApplicationDto>> saveWithdrawn();

    @GetMapping(value = "/iais-application-be/corrId-appId/{appId}")
    FeignResponseEntity<String> getCorrIdByAppId(@PathVariable(name = "appId") String appId);

    @GetMapping(value = "/iais-application-be/grpNo-apps/{grpNo}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> getAppsByGrpNo(@PathVariable(name = "grpNo") String grpNo);
    @GetMapping(value = "/iais-application-be/application-refno-by-licId-HclCode",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> getRefNoByLicId(@RequestParam ("licId")String licId,@RequestParam ("HclCode")String HclCode);

    @GetMapping(value = "/iais-application-be/finish-application-refno-by-licId-HclCode",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> getExistAppByLicId(@RequestParam ("licId")String licId,@RequestParam ("HclCode")String HclCode);
    @GetMapping(value = "/iais-apppremisescorrelation-be/app-premises-correlations/{appId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesCorrelationDto>> getAppPremisesCorrelationsByAppId(@PathVariable("appId") String appId);

    @GetMapping(value = "/iais-application-be/app-rec-status/{appStatus}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> getApplicationByStatus(@PathVariable(name = "appStatus") String appStatus);

    @RequestMapping(path = "/iais-application-be/getAppSubmissionForAuditDtoByAppGroupNo",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
    FeignResponseEntity<AppSubmissionForAuditDto>  getAppSubmissionForAuditDto(@RequestParam("appGroupNo") String appGroupNo);
    @GetMapping(value = "/iais-application-be/application-group-no-by-RefNo",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> getRefNoByRefNo(@RequestParam ("RefNo")String RefNo);

    @GetMapping(value = "/iais-group-misc/NotificateApplicationDtos",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<NotificateApplicationDto>> getNotificateApplicationDtos() ;

    @PutMapping(path = "/iais-group-misc",produces = { MediaType.APPLICATION_JSON_VALUE },consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<NotificateApplicationDto> updateNotificateApplicationDto(@RequestBody NotificateApplicationDto notificateApplicationDto);

    @GetMapping(path = "/iais-self-assessment-be/correlation/self-assessment/{correlationId}")
    FeignResponseEntity<List<AppPremisesSelfDeclChklDto>> getAppPremisesSelfDeclByCorrelationId(@PathVariable(value = "correlationId") String correlationId);

    @GetMapping(value = "/iais-application-be/application-by-taskid",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> getApplicationBytaskId(@RequestParam ("ref") String ref);

    @PostMapping(value = "/iais-application-be/application-group-cess",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationLicenceDto>> getCessGroup(@RequestBody List<String>  groupIds);

    @GetMapping(value = "/iais-application-be/app-fee-detail-by-application-no",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppFeeDetailsDto> getAppFeeDetailsDtoByApplicationNo(@RequestParam ("applicationNo") String applicationNo);

    @PostMapping(value = "/iais-application-be/save-app-return-fee",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppReturnFeeDto> saveAppReturnFee(@RequestBody AppReturnFeeDto appReturnFeeDto);

    @GetMapping(value = "/iais-apppreinsncitem-be/adhoc-item-be/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AdhocChecklistItemDto> getAdhocChecklistItemById(@PathVariable(name = "id") String id);

    @GetMapping(value = "/iais-application-be/application/grp-premises/{appPreId}")
    FeignResponseEntity<AppGrpPremisesEntityDto> getAppGrpPremise(@PathVariable(name = "appPreId")String appPreId);

    @GetMapping(value = "/iais-application-be/application/pending-reminder/self-assessment/", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<SelfAssMtEmailDto>> getPendingSubmitSelfAss(@RequestParam("msgKey") String msgKey);

    @GetMapping(value = "/iais-application-be/application-list-by-application-no",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> getApplicationDtosByApplicationNo(@RequestParam("applicationNo")String applicationNo);
    @PostMapping(value = "/iais-licence-view/app-edit-selsect-by-application-ids",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppEditSelectDto>> getAppEditSelectDtosByAppIds(@RequestBody List<String> applicationIds);

    @PostMapping (path = "/iais-appt-inspec-be/appt-Resch-param",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<ReschApptGrpPremsQueryDto>> searchApptReschGrpPrems(@RequestBody SearchParam searchParam);
    @PostMapping(value = "/iais-application/rfi-be-aoolication",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> updateApplicationOfRfi(@RequestBody List<ApplicationDto> rfiApplications);

    @GetMapping(value = "/iais-application-be/application-cgo-by-application-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSvcCgoDto> getApplicationCgoByAppId(@RequestParam(name = "applicationId") String applicationId,@RequestParam("psnType") String psnType);

}
