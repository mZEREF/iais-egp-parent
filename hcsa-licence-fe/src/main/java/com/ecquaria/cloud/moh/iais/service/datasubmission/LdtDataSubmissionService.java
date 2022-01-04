package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.LdtSuperDataSubmissionDto;

public interface LdtDataSubmissionService {

    LdtSuperDataSubmissionDto saveDpSuperDataSubmissionDto(LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto);

    LdtSuperDataSubmissionDto getDpSuperDataSubmissionDto(String submissionNo);

    String getSubmissionNo();

    String getDraftNo();

    LdtSuperDataSubmissionDto saveDataSubmissionDraft(LdtSuperDataSubmissionDto ldtSuperDataSubmissionDto);
}
