package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewHciNameDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesOperationalUnitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArEnquiryCoFundingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.EventBusLicenceGroupDtos;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.HcsaLicenceGroupFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.KeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppPremCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicBaseSpecifiedCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceAppRiskInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGrpDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicInspectionGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremInspGrpCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesAuditDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesAuditInspectorDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.PostInsGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ProfessionalInformationQueryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
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
 * @date 2019/12/4 16:02
 */
@FeignClient(name = "hcsa-licence", configuration = FeignConfiguration.class,
        fallback = HcsaLicenceClientFallback.class)
public interface HcsaLicenceClient {

    @GetMapping(value = "/hcsa-licence/licence-dto-by-hci-code",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> getLicenceDtoByHciCode(@RequestParam("hciCode")String hciCode,@RequestParam("licenseeId") String licenseeId);

    
    @RequestMapping(path = "/hcsa-licence/hci-code-licence-number",method = RequestMethod.GET)
    FeignResponseEntity<Integer> licenceNumber(@RequestParam("hciCode") String hciCode,@RequestParam("serviceCode") String serviceCode);

    @RequestMapping(path = "/hcsa-licence/service-group-licence-number",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String > groupLicenceNumber(@RequestBody LicenceGrpDto licenceGrpDto);

    @RequestMapping(path = "/hcsa-licence-transport/licences",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceGroupDto>> createLicence(@RequestBody List<LicenceGroupDto> licenceGroupDtoList);

    @RequestMapping(path = "/hcsa-premises/latestVersion/{hciCode}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PremisesDto> getLatestVersionPremisesByHciCode(@PathVariable(name = "hciCode") String hciCode);

    @GetMapping(path = "/hcsa-licence/getActiveHCICode",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity< List<String>> getActiveHCICode();

    @RequestMapping(path = "/hcsa-key-personnel/latestVersion/{idNo}/{orgId}",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<KeyPersonnelDto> getLatestVersionKeyPersonnelByidNoAndOrgId(@PathVariable(name = "idNo") String idNo,
                                                                           @PathVariable(name = "orgId") String orgId);
    @RequestMapping(value = "/hcsa-licence-fe-date",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE,
            method = RequestMethod.POST)
    FeignResponseEntity<Map<String, List<LicenceDto>>> licenceRenwal(@RequestBody List<Integer>  days);
    @PostMapping (value = "/hcsa-licence-unsend-email",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String,List<LicenceDto>>> unsendEmail(@RequestBody List<Integer>  days);
    @GetMapping(value = "/hcsa-licence/licPremisesCorrelationsByPremises/{licCorrId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicPremisesDto>> getlicPremisesCorrelationsByPremises(@PathVariable("licCorrId") String licCorreId);

    @RequestMapping(value = "/hcsa-licence/resHcsaLicenceGroupFee",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE
    ,method = RequestMethod.POST)
    FeignResponseEntity<List<HcsaLicenceGroupFeeDto>> retrieveHcsaLicenceGroupFee(@RequestBody List<String> licenceIds);
    @RequestMapping(value = "/hcsa-licence/licences",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE
            ,method = RequestMethod.POST)
    FeignResponseEntity<List<LicenceDto>> retrieveLicenceDtos(@RequestBody List<String> licenceIds);
    @GetMapping(value = "/hcsa-licence/base-spec-licence",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> getBaseOrSpecLicence(@RequestParam("licenceId") String licenceId);
    @GetMapping(value = "/hcsa-licence/licence-id-premises-dto",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PremisesDto>> getPremisess(@RequestParam("licenceId") String licenceId);

    @GetMapping(value = "/hcsa-licence/licence/{licenceId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getLicenceDtoById(@PathVariable(name="licenceId") String licenceId);

    @GetMapping(value = "/hcsa-licence/licenceById/{licId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getLicDtoById(@PathVariable("licId") String licenceId);

    @GetMapping(path= "/hcsa-licence/lic-corrid{licenceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicAppCorrelationDto>> getLicCorrBylicId(@PathVariable(value = "licenceId") String licenceId);

    @GetMapping(path= "/hcsa-licence/lic-corr-appId{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicAppCorrelationDto>> getLicCorrByappId(@PathVariable(value = "appId")String appId);

    @GetMapping(path = "/hcsa-key-personnel/professional/{psnId}/information/",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<PersonnelsDto> getProfessionalInformationByKeyPersonnelId(@PathVariable(value ="psnId" ) String psnId);

    @RequestMapping(path = "/hcsa-licence/licence-by-licno",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> getLicDtosByLicNos(@RequestBody List<String> licenceNos);

    @GetMapping(path = "/hcsa-licence-transport/EventBusLicenceGroupDtos/{refNo}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<EventBusLicenceGroupDtos> getEventBusLicenceGroupDtosByRefNo(@PathVariable(name = "refNo") String refNo);

    @PutMapping(path = "/hcsa-licence-transport", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateEicTrackStatus(@RequestBody EicRequestTrackingDto dto);

    @RequestMapping(path = "/hcsa-licence/lic-systemaudit-searchparam",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<AuditTaskDataDto>> searchSysAduit(SearchParam searchParam);
    @PostMapping(value = "/hcsa-licence/cessation-licences-ids",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> updateLicences(@RequestBody List<LicenceDto> licenceDtos);


    @PostMapping(value = "/hcsa-key-personnel/professional-information/results"
            , produces = MediaType.APPLICATION_JSON_VALUE
            , consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<ProfessionalInformationQueryDto>> searchProfessionalInformation
            (@RequestBody SearchParam searchParam);

    @GetMapping(path = "/hcsa-licence/LicPrembylicId{LicId}",produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<List<LicPremisesDto>> getLicPremListByLicId(@PathVariable(value ="LicId" ) String LicId);

    @GetMapping(value = "/hcsa-key-personnel/getPersonnelDtoByLicId/{licId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PersonnelsDto>> getPersonnelDtoByLicId(@PathVariable(name = "licId") String licId);

    @GetMapping(value = "hcsa-licence/cessation-licences/{status}/{endDate}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> cessationLicenceDtos(@PathVariable(name = "status") String status,@PathVariable(name = "endDate") String endDate);

    @GetMapping(value = "hcsa-licence/cessation-licences-status/{status}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> listLicencesByStatus(@PathVariable(name = "status") String status);

    @GetMapping(value = "hcsa-licence/audittculist",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AuditTaskDataFillterDto>> getAuditTcuList();

    @GetMapping(value = "/hcsa-licence/licenceViewDto",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceViewDto> getLicenceViewDtoByLicPremCorrId(@RequestParam(value ="licPremCorrId") String licPremCorrId);

    @PostMapping(path = "/hcsa-licence/licinspectiongroup-result", consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicInspectionGroupDto> createLicInspectionGroup(@RequestBody LicInspectionGroupDto licInspectionGroupDto);

    @PutMapping(path = "/hcsa-licence/licinspectiongroup-resultup", consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicInspectionGroupDto> updateLicInspectionGroup(@RequestBody LicInspectionGroupDto licInspectionGroupDto);

    @PostMapping(path = "/hcsa-licence/licpreminspgrpcorre-result", consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicPremInspGrpCorrelationDto> createLicInspectionGroupCorre(@RequestBody LicPremInspGrpCorrelationDto licPremInspGrpCorrelationDto);

    @PutMapping(path = "/hcsa-licence/licpreminspgrpcorre-resultup", consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicPremInspGrpCorrelationDto> updateLicInspectionGroupCorre(@RequestBody LicPremInspGrpCorrelationDto licPremInspGrpCorrelationDto);

    @PostMapping(path = "/hcsa-licence/icpremaudit-result", consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicPremisesAuditDto> createLicPremAudit(@RequestBody LicPremisesAuditDto licPremisesAuditDto);

    @GetMapping(path = "/hcsa-licence/icpremaudit-getresult", consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicPremisesAuditDto> getLicPremAuditByGuid(@RequestParam("guid") String guid);
    @PutMapping(path = "/hcsa-licence/icpremaudit-resultup", consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicPremisesAuditDto> updateLicPremAudit(@RequestBody LicPremisesAuditDto licPremisesAuditDto);

    @PostMapping(path = "/hcsa-licence/icpremauditinsp-result", consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicPremisesAuditInspectorDto> createLicPremisesAuditInspector(@RequestBody LicPremisesAuditInspectorDto licPremisesAuditInspectorDto);

    @PutMapping(path = "/hcsa-licence/icpremauditinsp-resultup", consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<LicPremisesAuditInspectorDto> updateLicPremisesAuditInspector(@RequestBody LicPremisesAuditInspectorDto licPremisesAuditInspectorDto);
    @GetMapping(value = "/hcsa-licence/licence-dto-svc-name",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> getLicenceDtosBySvcName(@RequestParam("svcName") String svcName);

    @GetMapping(value = "/hcsa-licence/postInspection-map",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String,List<String>>> getPostInspectionMap();

    @PostMapping(value = "/hcsa-licence/submission-app-licIds", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppSubmissionDto>> getAppSubmissionDtos(@RequestBody List<String> licenceIds);

    @PostMapping(value = "/hcsa-licence/licId-premises", consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicPremisesDto>> getPremisesByLicIds(@RequestBody List<String> licenceIds);

    @GetMapping(path= "/hcsa-licence/application-licence-correlation/{licId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getAppIdsByLicId(@PathVariable("licId") String licId);

    @GetMapping(value = "/hcsa-licence/licId-premises-hcicode-audit",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicPremisesAuditDto> getLicPremisesAuditDtoByLicIdAndHCICode(@RequestParam("licId") String licId,@RequestParam("HCICode") String HCICode);

    @GetMapping(value = "/hcsa-licence/baseLicId-list-specLicId",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getActSpecIdByActBaseId(@RequestParam("licId") String licId);

    @GetMapping(value = "/hcsa-licence/licence-bylicence-byNo",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getLicBylicNo(@RequestParam(value = "licenceNo")String licenceNo);

    @PostMapping(value = "/hcsa-licence/licence-out-date",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getLicenceOutDate(@RequestBody List<LicenseeDto> lics,
                                                        @RequestParam(value = "outDateMonth") int outDateMonth);

    @GetMapping(value = "/hcsa-licence/premise-cessation-id", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PremisesDto> getLicPremisesDtoById(@RequestParam(value = "id")String id);

    @GetMapping(path = "/hcsa-licence-transport/PremisesGroupDto/{originLicenceId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PremisesGroupDto>> getPremisesGroupDtos(@PathVariable(name = "originLicenceId") String originLicenceId);

    @PostMapping(value = "/hcsa-licence/LicAppCorrelationDtos",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicAppCorrelationDto>> getLicAppCorrelationDtosByApplicationIds(@RequestBody List<String> appIds);

    @PostMapping(value = "/hcsa-premises/hciCode/premises",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PremisesDto> getHciCodePremises(@RequestBody AppGrpPremisesEntityDto appGrpPremisesEntityDto);

    @GetMapping(path= "/hcsa-licence/licence-orgId/{licId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getLicdtoByOrgId(@PathVariable("licId") String licId);
    @GetMapping(value = "/hcsa-licence/application-view-dto-by-hci-name",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationViewHciNameDto>> getApplicationViewHciNameDtoByHciName(@RequestParam(name = "hciName") String hciName, @RequestParam(name = "licensee") String licensee,@RequestParam("premisesType")String premisesType);
    @PostMapping(value = "/hcsa-licence/application-view-by-address",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApplicationViewHciNameDto>> getApplicationViewHciNameDtoByAddress(@RequestBody Map<String,String> map);

    @GetMapping(value = "/hcsa-licence/licence-submission", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> getAppSubmissionDto(@RequestParam(value = "licenceId" ) String licenceId);

    @GetMapping(value = "/hcsa-licence/licence-view-submission", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<AppSubmissionDto> viewAppSubmissionDto(@RequestParam(value = "licenceId" ) String licenceId);

    @GetMapping(value = "/hcsa-licence/find-newest-licId")
    FeignResponseEntity<String> findNewestLicId(@RequestParam("licenceId") String licenceId);
    @GetMapping(value = "/hcsa-licence/find-lic-effDate",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> getLicByEffDate();
    @GetMapping(path= "/hcsa-licence/lice-app-correlation-by-id/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicAppCorrelationDto> getOneLicAppCorrelationByApplicationId(@PathVariable("appId") String appId);

    @PostMapping(value = "/hcsa-licence/save-licence-app-correlation",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicAppCorrelationDto> saveLicenceAppCorrelation(@RequestBody LicAppCorrelationDto licAppCorrelationDto);

    @GetMapping(value = "/hcsa-licence/group-licence-running-number")
    FeignResponseEntity<String> groupLicenceRunningNumber(@RequestParam("groupLicence")String groupLicence);

    @PostMapping(value = "/hcsa-licence/list-lic-ins-grp-ids",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicInspectionGroupDto>> getLicInsGrpByIds(@RequestBody List<String> insGrpIds);
    @PostMapping(value = "/hcsa-licence/test-licId", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> testLicence(@RequestBody List<LicInspectionGroupDto> licInspectionGroupDtos);

    @GetMapping(value = "/hcsa-licence/find-lic-effDateApproved",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity <List<LicenceDto>> getLicDtosWithApproved();

    @PostMapping(value = "/hcsa-licence/save-lic-app-risk-by-licdtos",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceAppRiskInfoDto>> saveLicenceAppRiskInfoDtosByLicIds(@RequestBody List<LicenceDto> licenceDtos);

    @GetMapping(value = "/hcsa-licence/LicBaseSpecifiedCorrelation/{svcType}/{originLicenceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicBaseSpecifiedCorrelationDto>> getLicBaseSpecifiedCorrelationDtos(@PathVariable("svcType") String svcType,
                                                                                                 @PathVariable("originLicenceId") String originLicenceId);

    @GetMapping(value = "/hcsa-licence/licence-orgId-corrId/get-postInsGroupDto",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PostInsGroupDto> getPostInsGroupDto(@RequestParam(name = "licId") String licId, @RequestParam(name = "corrId") String  corrId);

    @PostMapping(value = "/hcsa-licence/licence-orgId-corrId/savePostInsGroupDto",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PostInsGroupDto> savePostInsGroupDto(@RequestBody PostInsGroupDto postInsGroupDto);

    @GetMapping(value = "/hcsa-licence/get-premise-hciCodeName-be",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PremisesDto> getPremiseDtoByHciCodeOrName(@RequestParam("hciCodeName") String hciCodeName);

    @GetMapping(value = "/hcsa-licence/listHciName",produces =MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> listHciNames();

    @GetMapping(path= "/hcsa-licence/licence-corrId/hci-code", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> getHciCodeByCorrId(@RequestParam(name = "corrId") String corrId);

    @GetMapping(value = "/lic-common/get-sub-licensees-by-id/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SubLicenseeDto> getSubLicenseesById(@PathVariable("id") String id);

    @GetMapping(path= "/hcsa-licence/licence-appCorrId/licAppPremCorrelationDto", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicAppPremCorrelationDto> getLicAppPremCorrelationDtoByCorrId(@RequestParam(name = "corrId") String corrId);

    @PostMapping(value = "/lic-common/modification/premises",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PremisesDto>> savePremises(@RequestBody List<PremisesDto> premisesDtos);

    @GetMapping(value = "/hcsa-licence/premises/{premisedId}/floor-units", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppPremisesOperationalUnitDto>> getPremisesFloorUnits(@PathVariable("premisedId") String premisedId);

    @GetMapping(value = "/ar-common/patient-info/{patientCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PatientInfoDto> patientInfoDtoByPatientCode(@PathVariable("patientCode") String patientCode);

    @GetMapping(value = "/ar-common/patient-info/{submissionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PatientInfoDto> patientInfoDtoBySubmissionId(@PathVariable("submissionId") String submissionId);


    @GetMapping(value = "/data-submission-be/patient-inventory/{patientCode}/{hciCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PatientInventoryDto> patientInventoryByCode(@PathVariable("patientCode") String patientCode, @PathVariable("hciCode") String hciCode);

    @GetMapping(value = "/data-submission-be/patient-Co-Funding-History/{patientCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ArEnquiryCoFundingHistoryDto> patientCoFundingHistoryByCode(@PathVariable("patientCode") String patientCode);

}
