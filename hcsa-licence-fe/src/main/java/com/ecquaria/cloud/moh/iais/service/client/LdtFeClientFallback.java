package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.LdtSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LdtFeClientFallback implements LdtFeClient {
    private FeignResponseEntity getFeignResponseEntity(Object... params) {
        return IaisEGPHelper.getFeignResponseEntity(params);
    }

    @Override
    public FeignResponseEntity<LdtSuperDataSubmissionDto> saveLdtSuperDataSubmissionDto(LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto) {
        return getFeignResponseEntity(ldtSuperDataSubmissionDto);
    }

    @Override
    public FeignResponseEntity<LdtSuperDataSubmissionDto> getLdtSuperDataSubmissionDto(String submissionNo) {
        return getFeignResponseEntity(submissionNo);
    }

    @Override
    public FeignResponseEntity<LdtSuperDataSubmissionDto> doUpdateDataSubmissionDraft(LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto) {
        return getFeignResponseEntity(ldtSuperDataSubmissionDto);
    }

    @Override
    public FeignResponseEntity<LdtSuperDataSubmissionDto> getLdtSuperDataSubmissionDtoDraftByConds(String orgId ,String dataSubmissionId) {
        return getFeignResponseEntity(orgId);
    }

    @Override
    public FeignResponseEntity<Void> deleteLdtSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType) {
        return getFeignResponseEntity(orgId, submissionType);
    }

    @Override
    public FeignResponseEntity<Void> updateDataSubmissionDraftStatus(String draftId, String status) {
        return getFeignResponseEntity(draftId, status);
    }

    @Override
    public FeignResponseEntity<LdtSuperDataSubmissionDto> getLdtSuperDataSubmissionDtoDraftByDraftNo(String draftNo) {
        return getFeignResponseEntity(draftNo);
    }
}
