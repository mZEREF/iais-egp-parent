package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArEnquiryCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArEnquiryDonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArEnquiryTransactionHistoryResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionAdvEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquiryAjaxPatientResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquirySubResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
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
    FeignResponseEntity<List<PremisesDto>> getAllArCenterPremisesDtoByPatientCode(@RequestParam(name = "patientCode") String patientCode);
}
