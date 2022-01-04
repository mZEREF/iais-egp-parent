package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.LdtSuperDataSubmissionDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import java.util.Arrays;

@Slf4j
public class LdtFeClientFallback implements LdtFeClient {
    private FeignResponseEntity getFeignResponseEntity(Object... params) {
        log.warn("--------Params: " + Arrays.toString(params) + "-----------");
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<LdtSuperDataSubmissionDto> saveDpSuperDataSubmissionDto(LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto) {
        return getFeignResponseEntity(ldtSuperDataSubmissionDto);
    }

    @Override
    public FeignResponseEntity<LdtSuperDataSubmissionDto> getDpSuperDataSubmissionDto(String submissionNo) {
        return getFeignResponseEntity(submissionNo);
    }

    @Override
    public FeignResponseEntity<LdtSuperDataSubmissionDto> doUpdateDataSubmissionDraft(LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto) {
        return getFeignResponseEntity(ldtSuperDataSubmissionDto);
    }
}
