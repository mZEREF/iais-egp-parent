package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.LdtSuperDataSubmissionDto;

public interface LdtDataSubmissionService {

    LdtSuperDataSubmissionDto saveLdtSuperDataSubmissionDto(LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto);

    LdtSuperDataSubmissionDto getLdtSuperDataSubmissionDto(String submissionNo);

    String getSubmissionNo();

    String getDraftNo();

    LdtSuperDataSubmissionDto saveDataSubmissionDraft(LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto);

    LdtSuperDataSubmissionDto getLdtSuperDataSubmissionDraftByConds(String orgId);

    void deleteLdtSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType);

    void updateDataSubmissionDraftStatus(String draftId, String status);

    LdtSuperDataSubmissionDto saveLdtSuperDataSubmissionDtoToBE(LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto);
}
