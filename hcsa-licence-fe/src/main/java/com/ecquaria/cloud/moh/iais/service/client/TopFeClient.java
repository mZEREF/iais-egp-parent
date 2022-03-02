package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "hcsa-licence", configuration = FeignConfiguration.class, fallback = TopFeClientFallback.class)
public interface TopFeClient {
    @PostMapping(value = "/top-common/top-data-submission", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<TopSuperDataSubmissionDto> saveTopSuperDataSubmissionDto(
            @RequestBody TopSuperDataSubmissionDto topSuperDataSubmissionDto);

    @PostMapping(value = "/data-submission/draft/top", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<TopSuperDataSubmissionDto> doUpdateDataSubmissionDraft(
            @RequestBody TopSuperDataSubmissionDto topSuperDataSubmissionDto);

    @GetMapping(value = "/data-submission/draft-top-data-submission/{draftNo}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<TopSuperDataSubmissionDto> getTopSuperDataSubmissionDtoDraftByDraftNo(@PathVariable("draftNo") String draftNo);

    @PostMapping(value = "/data-submission/draft/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateDataSubmissionDraftStatus(@RequestParam("draftId") String draftId,
                                                              @RequestParam("Status") String status);

    @GetMapping(value = "/data-submission/top-data-submission/{orgId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<TopSuperDataSubmissionDto> getTopSuperDataSubmissionDtoDraftByConds(@PathVariable("orgId") String orgId,
                                                                                            @RequestParam("submissionType") String submissionType);


    @DeleteMapping(value = "/data-submission/draft-top-data-submission/special", consumes = MediaType.APPLICATION_JSON_VALUE)
    void deleteTopSuperDataSubmissionDtoDraftByConds(@RequestParam(name = "orgId") String orgId,
                                                     @RequestParam(name = "submissionType") String submissionType,
                                                     @RequestParam(name = "hciCode") String hciCode);

    @DeleteMapping(value = "/data-submission/draft/{draftId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> deleteTopSuperDataSubmissionDraftById(@PathVariable("draftId") String draftId);

    @GetMapping(value = "/top-common/top-data-submission/{submissionNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<TopSuperDataSubmissionDto> getTopSuperDataSubmissionDto(@PathVariable("submissionNo") String submissionNo);

    @GetMapping(value = "/top-common/patient/idnumber-nationality", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PatientInformationDto> getTopPatientSelect(@RequestParam(name = "idType") String idType,
                                                                   @RequestParam(name = "idNumber") String idNumber, @RequestParam(name = "nationality") String nationality,
                                                                   @RequestParam(name = "orgId") String orgId);
}
