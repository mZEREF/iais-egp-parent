package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.LdtSuperDataSubmissionDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hcsa-licence", configuration = FeignConfiguration.class, fallback = LdtFeClientFallback.class)
public interface LdtFeClient {
    @PostMapping(value = "/ldt-common/ldt-data-submission", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LdtSuperDataSubmissionDto> saveLdtSuperDataSubmissionDto(@RequestBody LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto);

    @GetMapping(value = "/ldt-common/ldt-data-submission/{submissionNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LdtSuperDataSubmissionDto> getLdtSuperDataSubmissionDto(@PathVariable("submissionNo") String submissionNo);

    @PostMapping(value = "/data-submission/draft/ldt", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LdtSuperDataSubmissionDto> doUpdateDataSubmissionDraft(@RequestBody LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto);


    @GetMapping(value = "/data-submission/ldt-data-submission/{orgId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LdtSuperDataSubmissionDto> getLdtSuperDataSubmissionDtoDraftByConds(@PathVariable("orgId") String orgId);

    @DeleteMapping(value = "/data-submission/draft-ldt-data-submission/special", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> deleteLdtSuperDataSubmissionDtoDraftByConds(@RequestParam(name = "orgId") String orgId,
                                                                          @RequestParam(name = "submissionType") String submissionType);

    @PostMapping(value = "/data-submission/draft/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateDataSubmissionDraftStatus(@RequestParam("draftId") String draftId,
                                                              @RequestParam("Status") String status);
}
