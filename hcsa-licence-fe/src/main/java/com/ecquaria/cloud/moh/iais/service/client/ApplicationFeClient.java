package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesDoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessment;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppliSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGroupMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesSelfDeclChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.WithdrawApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
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
 * @author Wenkang
 * @date 2019/11/26 14:28
 */
@FeignClient(name = "hcsa-application", configuration = FeignConfiguration.class,
        fallback = ApplicationFeClientFallback.class)
public interface ApplicationFeClient {
    @GetMapping(value = "/iais-application/correlation/application-number",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremisesCorrelationDto> getCorrelationByAppNo(@RequestParam("appNo") String appNo);

    @PostMapping(path = "/iais-application/all-file",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> fileAll(@RequestBody List<String> grpids);

    @GetMapping(value = "/iais-application/rec-datas")
    FeignResponseEntity<String> recDatesToString();

    @GetMapping(value = "/iais-application/rec-file-datas")
    FeignResponseEntity<Map<String, List<AppPremPreInspectionNcDocDto>>> recFileId();

    @RequestMapping(value = "/iais-application/status",produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE,method =RequestMethod.PUT)
    FeignResponseEntity<Void> updateStatus(@RequestBody Map<String,List<String>> map);

    @PostMapping(value = "/iais-application/app-corr-appt",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity <List<AppPremisesCorrelationDto>> appPremCorrDtosByApptViewDtos(@RequestBody List<ApptViewDto> apptViewDtos);

    @PutMapping(path = "/iais-application/lastApplication", produces = MediaType.APPLICATION_JSON_VALUE,
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
    FeignResponseEntity<AppSubmissionDto> saveDraft(@RequestBody AppSubmissionDto appSubmissionDto );

    @PostMapping(path = "/iais-submission/draft/special/{licenseeId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateDrafts(@PathVariable("licenseeId")String licenseeId, @RequestBody List<String> licenceIds,
            @RequestParam("excludeDraftNo") String excludeDraftNo);

    @PostMapping(path = "/iais-submission", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> saveSubmision(@RequestBody AppSubmissionDto appSubmissionDto);

    @PostMapping(value = "/iais-submission/cessation-apps-save",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> saveApps(@RequestBody AppSubmissionDto appSubmissionDto);

    @PostMapping(value = "/iais-submission/withdrawn-apps-save",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> saveWithdrawnApps(@RequestBody Map<String,Object> map);

    @PostMapping(path = "/iais-submission/requestInformation", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> saveReqeustInformationSubmision(@RequestBody AppSubmissionRequestInformationDto appSubmissionRequestInformationDto);

    @PostMapping(path = "/iais-submission/save-RFC-renew-requestInformation", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> saveRFCOrRenewRequestInformation(@RequestBody AppSubmissionRequestInformationDto appSubmissionRequestInformationDto);
    @GetMapping(path = "/iais-application/application-premises-by-app-id/{applicationId}")
    FeignResponseEntity<List<AppGrpPremisesDto>> getAppGrpPremisesDtoByAppGroId(@PathVariable("applicationId") String applicationId);

    @GetMapping(path = "/iais-application/application-premises-by-corr-id/{corrId}")
    FeignResponseEntity<AppGrpPremisesDto> getAppGrpPremisesByCorrId(@PathVariable("corrId") String corrId);

    @PostMapping(path = "/iais-self-assessment/self-assessment/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesSelfDeclChklDto>> saveAllSelfAssessment(@RequestBody List<SelfAssessment> selfAssessmentList);

    @GetMapping(path = "/iais-self-assessment/correlation/self-assessment/{correlationId}")
    FeignResponseEntity<List<AppPremisesSelfDeclChklDto>> getAppPremisesSelfDeclByCorrelationId(@PathVariable(value = "correlationId") String correlationId);

    @PutMapping(path = "/iais-self-declaration/correlation/self-decl/inactive", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> inActiveLastVersionByGroupId(@RequestBody  List<String> lastVersionId);

    @GetMapping(path = "/iais-self-assessment/self-assessment/answer-data/{groupId}")
    FeignResponseEntity<List<AppPremisesSelfDeclChklDto>> getAppPremisesSelfDeclChklListByGroupId(@PathVariable(value = "groupId") String groupId);


    @GetMapping(path = "/iais-application/application/correlations/{appid}")
    FeignResponseEntity<List<AppPremisesCorrelationDto>> listAppPremisesCorrelation(@PathVariable(name = "appid") String appId);

    @PutMapping(path="/iais-application/app-grp", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> doUpDate(@RequestBody ApplicationGroupDto applicationGroupDto);

    @PutMapping(path="/iais-application/payment-app-grp", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> doPaymentUpDate(@RequestBody ApplicationGroupDto applicationGroupDto);
    @PutMapping(value = "/iais-application/payment-app-grp-id-no",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> paymentUpDateByGrpNo(@RequestBody ApplicationGroupDto applicationGroup);

    @GetMapping(value = "/iais-submission/submission-id")
    FeignResponseEntity<String> getSubmissionId();

    @GetMapping(value = "/iais-application/application/premises-scope/{correId}")
    FeignResponseEntity<List<AppSvcPremisesScopeDto>> getAppSvcPremisesScopeListByCorreId(@PathVariable(name = "correId") String correId);

    @GetMapping(value = "/iais-application/application/grp-premises/{appPreId}")
    FeignResponseEntity<AppGrpPremisesEntityDto> getAppGrpPremise(@PathVariable(name = "appPreId")String appPreId);

    /**
     *  only for RFI applicaiton
     * @param appNo
     * @return
     */
    @RequestMapping(path = "/iais-submission/appSubmissionDto/{appNo}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
    FeignResponseEntity<AppSubmissionDto>  getAppSubmissionDtoByAppNo(@PathVariable("appNo") String appNo);

    @RequestMapping(path = "/iais-submission/appSubmissionDto",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
    FeignResponseEntity<AppSubmissionDto>  getAppSubmissionDto(@RequestParam("appNo") String appNo);

    @RequestMapping(path = "/iais-submission/appSubmissionDto/v2",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE )
    FeignResponseEntity<AppSubmissionDto>  gainSubmissionDto(@RequestParam("appNo") String appNo);

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
    @PostMapping(value = "/iais-submission/application-rfc-licences-grp-app",  consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSubmissionDto>> saveAppsForRequestForGoupAndAppChangeByList(@RequestBody List<AppSubmissionDto> appSubmissionDtos);
    @GetMapping(value = "/appeal/list-application-by-origin-licence-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> getApplicationsByLicenceId(@RequestParam("originLicenceId") String originLicenceId);

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
    FeignResponseEntity<List<AppSvcPrincipalOfficersDto>> getAppGrpPersonnelByGrpId(@RequestParam("grpId") String grpId);

    @GetMapping(value = "/iais-application/apps-by-licId/{licId}")
    FeignResponseEntity<List<ApplicationDto>> getApplicationsByLicId(@PathVariable(name = "licId") String licId);

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
    @PostMapping(value = "/iais-submission/darft-service-codes",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> selectDarft(@RequestBody Map<String,Object> serviceCodes);

    @PostMapping(value = "/iais-submission/application/without-renew",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationGroupDto> createApplicationDataByWithOutRenewal(@RequestBody RenewDto renewDto);

    @GetMapping(value = "/appeal/appeal-eligibility")
    FeignResponseEntity<Boolean> isAppealEligibility(@RequestParam("id") String id);

    @GetMapping(value = "/iais-self-assessment/self-assessment/{groupId}/status/", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Integer> getApplicationSelfAssMtStatusByGroupId(@PathVariable(value = "groupId") String groupId);

    @GetMapping(value = "/iais-self-assessment/self-assessment/{corrId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<SelfAssessment>> receiveSelfAssessmentDataByCorrId(@PathVariable(value = "corrId") String corrId);

    @PutMapping(value = "/iais-application/batch-update/application", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> updateApplicationList(@RequestBody List<ApplicationDto> applicationDtoList);
    @PostMapping(value = "/iais-application/app-group-misc-dto",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppGroupMiscDto> saveAppGroupMiscDto(@RequestBody AppGroupMiscDto appGroupMiscDto);
    @GetMapping(value = "/iais-application/application-group-no",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> getApplicationsByGroupNo(@RequestParam("groupNo") String groupNo);

    @PutMapping(value = "/iais-application",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> updateApplicationDto(@RequestBody ApplicationDto applicationDto);
    @PutMapping(value = "/iais-submission/delete-app-overdue-draft",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> deleteOverdueDraft(@RequestBody String draftValidity);
    @PostMapping(value = "/iais-application/app-fee-details-renew",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppFeeDetailsDto> saveAppFeeDetails(@RequestBody AppFeeDetailsDto appFeeDetailsDto);
    @GetMapping(value = "/iais-application/app-fee-detail-by-application-no",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppFeeDetailsDto> getAppFeeDetailsDtoByApplicationNo(@RequestParam("applicationNo") String applicationNo);

    @GetMapping(value = "/iais-application/app-grp-premises-by-hci-name",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppGrpPremisesDto>> getAppGrpPremisesDtoByHciName(@RequestParam("hciName") String hciName,@RequestParam("licencessId") String licencessId,@RequestParam("premType") String premType);
    @GetMapping(value = "/iais-application/application-dto-by-appNo",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> getApplicationDtoByAppNo(@RequestParam("appNo") String appNo);
    @GetMapping(value = "/appeal/application-withdrawal-by-app-id")
    FeignResponseEntity<Boolean> isApplicationWithdrawal(@RequestParam("appId") String appId);
    @PostMapping(value = "/iais-application/pending-app-premises",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppGrpPremisesEntityDto>> getPendAppPremises(@RequestBody AppPremisesDoQueryDto appPremisesDoQueryDto);
    @GetMapping(value = "/appeal/licence-appeal-or-cessation-by-licence-id")
    FeignResponseEntity<Boolean> isLiscenceAppealOrCessation(@RequestParam("licenceId") String licenceId);
    @GetMapping(value = "/appeal/apppremisemisc-by-appIdOrLicenceId",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppPremiseMiscDto> getAppPremiseMiscDtoByAppId(@RequestParam("appIdOrLicenceId")String appIdOrLicenceId);

    @PostMapping(value = "/iais-application/fe-application-dto-list",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationDto>> saveApplicationDtos(@RequestBody List<ApplicationDto> applicationDtos);
    @GetMapping(value = "/iais-application/application-by-corrId",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationDto> getApplicationByCorrId(@RequestParam("corrId")String corrId);
    @GetMapping(value = "/iais-application/all-appGrpDto-paying")
    FeignResponseEntity<List<ApplicationGroupDto>> getAppGrpDtoPaying();
    @DeleteMapping(value = "/iais-submission/draft-by-numbers",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> deleteDraftNUmber(@RequestBody List<String> draftNumbers);
    @PostMapping(value = "/iais-application/application-by-types-and-status",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<WithdrawApplicationDto>> getApplicationByAppTypesAndStatus(@RequestBody List<String[]> appTandS,@RequestParam("licenseeId")String licenseeId);
    @GetMapping(value = "/iais-submission/get-draft-by-lic-app-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationSubDraftDto>> getDraftByLicAppId(@RequestParam(value = "licAppId",required = false)String licAppId);
    @PutMapping(value = "/iais-submission/delete-draft-by-no")
    FeignResponseEntity  deleteDraftByNo(@RequestParam("draftNo") String draftNo);
    @GetMapping(value = "/iais-application/app-edit-select-by-type",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppEditSelectDto>> getAppEditSelectDtos(@RequestParam(name="appId")String appId, @RequestParam(name = "changeType")String changeType);
    @GetMapping(value = "/iais-submission/get-prem-by-app-no",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppGrpPremisesEntityDto> getPremisesByAppNo(@RequestParam("appNo") String appNo);

    @PostMapping(path = "/iais-submission/requestInformation-cessation", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> saveRfcCessationSubmision(@RequestBody AppSubmissionRequestInformationDto appSubmissionRequestInformationDto);
    @GetMapping(value = "/appeal/app-premise-misc-dto-relate-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremiseMiscDto>> getAppPremiseMiscDtoRelateId(@RequestParam("relateId") String relateId);

    @GetMapping(value = "/iais-application/rfi-withdrawal-info",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<WithdrawnDto> getWithdrawAppInfo(@RequestParam("appNo") String appNo);

    @PostMapping(path = "/iais-submission/requestForInformation-withdrawal", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> saveRfcWithdrawSubmission(@RequestBody AppSubmissionRequestInformationDto appSubmissionRequestInformationDto);
    @GetMapping(value = "/iais-application/max-version-primary-com-doc",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppGrpPrimaryDocDto> getMaxVersionPrimaryComDoc(@RequestParam(name = "appGrpId")String appGrpId,@RequestParam(name = "configDocId")String configDocId,@RequestParam(name = "seqNum")String seqNum);
    @GetMapping(value = "/iais-application/max-version-svc-com-doc",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSvcDocDto> getMaxVersionSvcComDoc(@RequestParam(name = "appGrpId")String appGrpId, @RequestParam(name = "configDocId")String configDocId,@RequestParam(name = "seqNum")String seqNum);

    @GetMapping(value = "/iais-application/max-version-primary-spec-doc",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppGrpPrimaryDocDto> getMaxVersionPrimarySpecDoc(@RequestParam(name = "appGrpId")String appGrpId,@RequestParam(name = "configDocId")String configDocId,@RequestParam(name = "appNo")String appNo,@RequestParam(name = "seqNum")String seqNum);
    @PostMapping(value = "/iais-application/max-version-svc-spec-doc",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSvcDocDto> getMaxVersionSvcSpecDoc(@RequestBody AppSvcDocDto appSvcDocDto,@RequestParam(name = "appNo")String appNo);
    @GetMapping(value = "/appeal/app-special-doc-group-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppliSpecialDocDto>> getAppliSpecialDocDtoByGroupId(@RequestParam("groupId") String groupId);
    @GetMapping(value = "/appeal/app-special-doc-by-corrId",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppliSpecialDocDto>> getAppliSpecialDocDtoByCorrId(@RequestParam("corrId") String corrId);
    @GetMapping(value = "/iais-submission/appSubmissionDto/{appGrpNo}",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> getAppSubmissionDtoByAppGrpNo(@PathVariable("appGrpNo") String appGrpNo);
    @PutMapping(path="/iais-application/app-grp-pmt-status", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationGroupDto> updateAppGrpPmtStatus(@RequestBody ApplicationGroupDto applicationGroupDto);
    @GetMapping(value = "/hcsa-app-common/app-grp-appNo/{appNo}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationGroupDto> getAppGrpByAppNo(@PathVariable("appNo") String appNo);
    @GetMapping(value = "/iais-application/max-seq-num-primary-doc",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppGrpPrimaryDocDto>> getMaxSeqNumPrimaryDocList(@RequestParam("appGrpId")String appGrpId);
    @GetMapping(value = "/iais-application/max-seq-num-svc-doc",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSvcDocDto>> getMaxSeqNumSvcDocList(@RequestParam("appGrpId")String appGrpId);
    @PutMapping(value = "/iais-submission/draft/{draftNo}/{status}")
    FeignResponseEntity<String> updateDraftStatus(@PathVariable("draftNo")String draftNo, @PathVariable("status")String status);
    @PostMapping(value = "/iais-submission/draft-by-svc-codes",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationSubDraftDto>> getDraftListBySvcCodeAndStatus(@RequestBody List<String> svcCodeList, @RequestParam("licenseeId")String licenseeId, @RequestParam("status")String status,@RequestParam("appType")String appType);
    @GetMapping(value = "/iais-application/app-group-misc/grp-id/misc-type",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppGroupMiscDto> getAppGroupMiscDtoByGrpIdAndTypeAndStatus(@RequestParam("appGrpId")String appGrpId, @RequestParam("miscType")String miscType, @RequestParam("status")String status);
    @GetMapping(value = "/iais-application/declaration-message",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppDeclarationMessageDto>> getAppDeclarationMessageDto(@RequestParam("appGrpId")String appGrpId);
    @GetMapping(value = "/iais-application/declaration-doc",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppDeclarationDocDto>> getAppDeclarationDocDto(@RequestParam("appGrpId")String appGrpId);
    @GetMapping(value = "/get-vehicle-vehicle-number",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSvcVehicleDto>> getAppSvcVehicleDtoByVehicleNumber(@RequestParam("vehicleNumber") String vehicleNumber);
    @PutMapping(path="/iais-application/fe-giro-retrigger", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApplicationGroupDto> updateAppGrpPmtStatus(@RequestBody ApplicationGroupDto applicationGroupDto, @RequestParam(name = "giroAccNo") String giroAccNo);

    @GetMapping(value = "/iais-submission/active-vehicles", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSvcVehicleDto>> getActiveVehicles();

    @PutMapping(path="/iais-application/payment-update", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> updatePaymentByAppGrp(@RequestBody ApplicationGroupDto applicationGroupDto);

    @GetMapping(value = "/iais-submission/draft-by-lic-app-id",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationSubDraftDto>> getDraftByLicAppIdAndStatus(@RequestParam("licAppId") String licAppId,@RequestParam("status") String status);

    @PutMapping(value="/iais-application/inactive-declaration-message", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> inActiveDeclaration(@RequestBody AppSubmissionDto appSubmissionDto);

}
