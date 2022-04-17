package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCurrentInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArEnquiryCoFundingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArEnquiryCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArEnquiryDonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArEnquiryTransactionHistoryResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionAdvEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquiryAjaxPatientResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquirySubResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsLaboratoryDevelopTestEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PgtStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * AssistedReproductionClient
 *
 * @author junyu
 * @date 2021/11/19
 */
@FeignClient(name = "hcsa-licence", configuration = FeignConfiguration.class, fallback = AssistedReproductionClientFallback.class)
public interface AssistedReproductionClient {

    @PostMapping(value = "/ar-common/ar-search-patient-param", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<AssistedReproductionEnquiryResultsDto>> searchPatientByParam(
            @RequestBody SearchParam searchParam);

    @PostMapping(value = "/ar-common/ar-search-patient-ajax-param", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<AssistedReproductionEnquiryAjaxPatientResultsDto>> searchPatientAjaxByParam(
            @RequestBody SearchParam searchParam);

    @PostMapping(value = "/ar-common/ar-search-submission-param", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<AssistedReproductionEnquirySubResultsDto>> searchSubmissionByParam(
            @RequestBody SearchParam searchParam);

    @PostMapping(value = "/ar-common/ar-search-patient-param", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<AssistedReproductionAdvEnquiryResultsDto>> searchPatientAdvByParam(
            @RequestBody SearchParam searchParam);

    @PostMapping(value = "/ar-common/ar-search-transaction-history-param", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<ArEnquiryTransactionHistoryResultDto>> searchTransactionHistoryByParam(
            @RequestBody SearchParam searchParam);

    @PostMapping(value = "/ar-common/ar-search-cycle-stage-param", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<ArEnquiryCycleStageDto>> searchCycleStageByParam(
            @RequestBody SearchParam searchParam);

    @PostMapping(value = "/ar-common/ar-search-donor-sample-param", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<ArEnquiryDonorSampleDto>> searchDonorSampleByParam(
            @RequestBody SearchParam searchParam);

    @GetMapping(value = "/ar-common/cycle-stage-id", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<DataSubmissionDto>> getAllDataSubmissionByCycleId(@RequestParam("cycleId") String cycleId);

    @GetMapping(value = "/ar-common/ar-center-premises-patient-code", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PremisesDto>> getAllArCenterPremisesDtoByPatientCode(@RequestParam(name = "patientCode") String patientCode,@RequestParam(name = "orgId") String orgId);

    @GetMapping(value = "/ar-common/patient-info/{patientCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PatientInfoDto> patientInfoDtoByPatientCode(@PathVariable("patientCode") String patientCode);

    @GetMapping(value = "/ar-common/patient-info-sub/{submissionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PatientInfoDto> patientInfoDtoBySubmissionId(@PathVariable("submissionId") String submissionId);

    @GetMapping(value = "/data-submission-be/patient-Co-Funding-History/{patientCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ArEnquiryCoFundingHistoryDto> patientCoFundingHistoryByCode(@PathVariable("patientCode") String patientCode);

    @GetMapping(value = "/data-submission-be/pgt-stage/{patientCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PgtStageDto>> listPgtStageByPatientCode(@PathVariable("patientCode") String patientCode) ;

    @GetMapping(value = "/ar-common/ar-data-submission/{submissionNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDto(@PathVariable("submissionNo")String submissionNo);

    @GetMapping(value = "/ar-common/ar-data-submission-submission-id/{submissionId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDtoByDsId(@PathVariable("submissionId") String submissionId);

    @PostMapping(value = "/ldt-common/ldt-search-ldt-param", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<DsLaboratoryDevelopTestEnquiryResultsDto>> searchLdtByParam(
            @RequestBody SearchParam searchParam);

    @GetMapping(value = "/ar-common/ar-current-inventory-by-patientCode", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ArCurrentInventoryDto>> getArCurrentInventoryDtosByPatientCode(@RequestParam(name = "patientCode") String patientCode);
}
