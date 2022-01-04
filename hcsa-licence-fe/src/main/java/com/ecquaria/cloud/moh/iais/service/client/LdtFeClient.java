package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.LdtSuperDataSubmissionDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "hcsa-licence", configuration = FeignConfiguration.class, fallback = LdtFeClientFallback.class)
public interface LdtFeClient {
    @PostMapping(value = "/ldt-common/ldt-data-submission", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LdtSuperDataSubmissionDto> saveDpSuperDataSubmissionDto(@RequestBody LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto);

    @GetMapping(value = "/ldt-common/ldt-data-submission/{submissionNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LdtSuperDataSubmissionDto> getDpSuperDataSubmissionDto(@PathVariable("submissionNo") String submissionNo);

    @PostMapping(value = "/data-submission/draft/ldt", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<LdtSuperDataSubmissionDto> doUpdateDataSubmissionDraft(@RequestBody LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto);
}
