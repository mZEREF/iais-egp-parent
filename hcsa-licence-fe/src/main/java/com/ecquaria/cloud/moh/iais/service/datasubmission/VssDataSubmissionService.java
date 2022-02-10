package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;

import java.util.Map;

/**
 * @Description VssDataSubmissionService
 * @Auther chenlei on 12/15/2021.
 */
public interface VssDataSubmissionService {
    Map<String, PremisesDto> getVssCenterPremises(String licenseeId);

    String getDraftNo(String dsType, String draftNo);

    VssSuperDataSubmissionDto saveDataSubmissionDraft(VssSuperDataSubmissionDto vssSuperDataSubmissionDto);

    VssSuperDataSubmissionDto getVssSuperDataSubmissionDto(String submissionNo);

    String getSubmissionNo(String dsType);

    VssSuperDataSubmissionDto saveVssSuperDataSubmissionDto(VssSuperDataSubmissionDto vssSuperDataSubmissionDto);

    VssSuperDataSubmissionDto saveVssSuperDataSubmissionDtoToBE(VssSuperDataSubmissionDto vssSuperDataSubmissionDto);

    void updateDataSubmissionDraftStatus(String draftId, String status);

    VssSuperDataSubmissionDto getVssSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType, String svcName,
                                                                     String hciCode);

    void deleteVssSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType, String hciCode);

    VssSuperDataSubmissionDto getVssSuperDataSubmissionDtoByDraftNo(String draftNo);
}
