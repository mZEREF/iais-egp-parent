package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsCenterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.KeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LaboratoryDevelopTestDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicBaseSpecifiedCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnlAssessQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.SelfPremisesListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxDataSubmissionQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InboxLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageSearchDto;
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


/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-11-29 13:17
 **/
@FeignClient(name = "hcsa-licence",configuration = FeignConfiguration.class,fallback = LicenceInboxFallback.class)
public interface LicenceInboxClient {
    @PostMapping(path = "/hcsa-licence-transport/licence-param",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<InboxLicenceQueryDto>> searchResultFromLicence(@RequestBody SearchParam searchParam);

    @GetMapping(path= "/hcsa-licence/application-licence-premises", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppGrpPremisesDto>> getDistinctPremisesByLicenseeId(@RequestParam(value = "licenseeId") String licenseeId, @RequestParam(value ="serviceName") String serviceName);

    @GetMapping(path= "/hcsa-licence-rfc/licence-premises", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PremisesListQueryDto>> getPremises(@RequestParam(value = "licenseeId" ) String licenseeId);

    @GetMapping(value = "/hcsa-licence/licPremisesCorrelationsById/{licCorrId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicPremisesDto> getlicPremisesCorrelationsByPremises(@PathVariable("licCorrId") String licCorreId);

    @RequestMapping(path = "/hcsa-licence-rfc/licence-personnels",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PersonnelListQueryDto>> getPersonnel(@RequestParam(value = "licenseeId")String licenseeId);

    @RequestMapping(path = "/hcsa-licence-rfc/psn-param",method = RequestMethod.GET,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<PersonnelQueryDto>> searchPsnInfo(@RequestBody SearchParam searchParam);

    @RequestMapping(path = "/hcsa-licence-rfc/assess-psn-param",method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<PersonnlAssessQueryDto>> assessPsnDoQuery(@RequestBody SearchParam searchParam);

    @GetMapping(path= "/hcsa-licence-transport/licence-active-num")
    FeignResponseEntity<Integer> getLicActiveStatusNum(@RequestParam("licenseeId")String licenseeId);

    @GetMapping(path= "/hcsa-licence-rfc/licence-bylicence-byNo/{licenceNo}")
    FeignResponseEntity<LicenceDto> getLicBylicNo();

    @RequestMapping(path= "/hcsa-licence-rfc/licence-bylicence-byid/{licenceId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getLicBylicId(@RequestParam(value = "licenceId") String licenceId);
    @GetMapping(value = "/hcsa-licence/licenceById/{licId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getLicDtoById(@PathVariable("licId") String licenceId);
    @GetMapping(value = "/hcsa-licence/licence-id-premises",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PremisesDto>> getPremisesDto(@RequestParam("licenceId") String licenceId);

    @PostMapping(value = "/hcsa-licence-rfc/self-premises-query-param",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<SelfPremisesListQueryDto>> searchResultPremises(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/personnel-roles",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<KeyPersonnelDto>> getKeyPersonnelByRole(@RequestBody List<String> roles);

    @GetMapping(value = "/hcsa-licence/app-svc-align-licence",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<AppAlignLicQueryDto>> getAppAlignLicQueryDto(@RequestParam("licenseeId") String licenseeId, @RequestParam("svcNameStr") String svcNameStr,@RequestParam("premTypeStr") String premTypeStr );

    @RequestMapping(path = "/hcsa-licence/menuLicence",method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE },
            consumes = {MediaType.APPLICATION_JSON_VALUE})
    FeignResponseEntity<SearchResult<MenuLicenceDto>> getMenuLicence(SearchParam searchParam);

    @GetMapping(path= "/hcsa-licence/licence-orgId/{orgId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getLicdtoByOrgId(@PathVariable(value = "orgId") String orgId);

    @GetMapping(path= "/hcsa-licence/root-licence-orgId/{orgId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getRootLicenceDtoByOrgId(@PathVariable(value = "orgId") String orgId);

    @GetMapping(value = "/hcsa-licence//check-new-licensee")
    FeignResponseEntity<Boolean> checkIsNewLicsee(@RequestParam("licenseeId") String licenseeId);

    @GetMapping(path = "/hcsa-licence/licenceView/{licenceId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceViewDto>  getLicenceViewByLicenceId(@PathVariable("licenceId") String licenceId);

    @GetMapping(path = "/hcsa-licence/allStatusLicenceView/{licenceId}",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceViewDto>  getAllStatusLicenceByLicenceId(@PathVariable("licenceId") String licenceId);

    @GetMapping(path = "/hcsa-licence/assessment-personnel",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PersonnelListDto>>  getPersonnelListAssessment(@RequestParam("idNos") List<String> idNos,@RequestParam("orgId") String orgId);

    @GetMapping(value = "/hcsa-licence/lic-premises",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PremisesDto>> getPremisesByLicseeIdAndSvcName(@RequestParam("licenseeId") String licenseeId, @RequestParam("svcNameStr") String svcNameStr);
    @GetMapping(value = "/hcsa-licence/appeal-new-application",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity <List<LicenceDto>> isNewApplication(@RequestParam("application") String application);
    @GetMapping(value = "/hcsa-licence/appeal-new-licence",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> isNewLicence(@RequestParam("licenceId") String licenceId);

    @GetMapping(value = "/hcsa-licence/licence-dto-licensee-id",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> getLicenceDtosByLicenseeId(@RequestParam("licenseeId") String licenseeId);

    @GetMapping(value = "/hcsa-licence/first-licence",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LicenceDto> getFirstLicenceDtosByLicenseeId(@RequestParam("licenseeId") String licenseeId);

    @PostMapping(value = "/hcsa-licence/get-premises-additional",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<MenuLicenceDto>> setPremAdditionalInfo(@RequestBody List<MenuLicenceDto> menuLicenceDtos);

    @PostMapping(value = "/lic-common/laboratory-develop-test",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LaboratoryDevelopTestDto> saveLaboratoryDevelopTest(@RequestBody LaboratoryDevelopTestDto laboratoryDevelopTestDto);

    @GetMapping(value = "/hcsa-licence/licence-dto-by-hci-code",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> getLicenceDtoByHciCode(@RequestParam("hciCode")String hciCode,@RequestParam("licenseeId") String licenseeId);
    @PostMapping(path = "/hcsa-licence/bundle-licence",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> getBundleLicence(@RequestBody LicenceDto licenceDto);

    @GetMapping(value = "/hcsa-licence/individual-sub-licensees", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<SubLicenseeDto>> getIndividualSubLicensees(@RequestParam("orgId") String orgId);

    @GetMapping(value = "/hcsa-licence/base-spec-licence",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> getBaseOrSpecLicence(@RequestParam("licenceId") String licenceId);

    @PutMapping(value = "/lic-common/refresh-sub-licensee", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> refreshSubLicenseeInfo(@RequestBody LicenseeDto licenseeDto);

    @PostMapping(value = "/data-submission/data-submission-param",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity <SearchResult<InboxDataSubmissionQueryDto>> searchLicence(@RequestBody SearchParam searchParam);

    @DeleteMapping(value = "/data-submission/draft-data-submission", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> deleteArSuperDataSubmissionDtoDraftByDraftNo(@RequestParam("draftNo") String draftNo);

    @GetMapping(value = "/lic-common/lic-licenseeId",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicenceDto>> getActiveLicencesByLicenseeId(@RequestParam("licenseeId") String licenseeId);

    @GetMapping(value = "/hcsa-licence/LicBaseSpecifiedCorrelation/{svcType}/{originLicenceId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<LicBaseSpecifiedCorrelationDto>> getLicBaseSpecifiedCorrelationDtos(@PathVariable("svcType") String svcType,
                                                                                                 @PathVariable("originLicenceId") String originLicenceId);

    @GetMapping(value = "/data-submission/data-submission-rfc-withdrawn-count-cycle-id", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Integer> getRfcCountByCycleId(@RequestParam("cycleId") String cycleId);

    @GetMapping(value = "/ar-common/ar-data-submission/{submissionNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDto(@PathVariable("submissionNo")String submissionNo);

    @GetMapping(value = "/ar-common/cycle-patientCode", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<CycleDto>> cycleByPatientCode(@RequestParam("patientCode") String patientCode);

    @GetMapping(value = "/ar-common/cycle-stage-id", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<DataSubmissionDto>> getAllDataSubmissionByCycleId(@RequestParam("cycleId") String cycleId);

    @GetMapping(value = "/lic-common/ds-center-organizationId",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<DsCenterDto>> getDsCenterDtosByOrganizationId(@RequestParam("organizationId") String organizationId);

    @GetMapping(value = "/data-submission/change-data-submission-id-status", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateDataSubmissionByIdChangeStatus(@RequestParam("id") String id, @RequestParam("lockStatus") Integer lockStatus);

    @PostMapping(path = "/data-submission/dss-draft-num", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Integer> dssDraftNum(InterMessageSearchDto interMessageSearchDto);

    @GetMapping(value = "/dp-common/dp-use-cycle/{submissionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> hasDonorSampleUseCycleBySubmissionId(@PathVariable("submissionId") String submissionId);
}