package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;

import java.util.Map;

/**
 * @Description DpDataSubmissionService
 * @Auther chenlei on 11/18/2021.
 */
public interface DpDataSubmissionService {

    Map<String, PremisesDto> getDpCenterPremises(String licenseeId);

    String getDraftNo(String dsType, String draftNo);

    DpSuperDataSubmissionDto saveDataSubmissionDraft(DpSuperDataSubmissionDto dpSuperDataSubmissionDto);

    String getSubmissionNo(String dsType);

    DpSuperDataSubmissionDto saveDpSuperDataSubmissionDto(DpSuperDataSubmissionDto dpSuperDataSubmissionDto);

    DpSuperDataSubmissionDto saveDpSuperDataSubmissionDtoToBE(DpSuperDataSubmissionDto dpSuperDataSubmissionDto);

    void updateDataSubmissionDraftStatus(String draftId, String status);

    DpSuperDataSubmissionDto getDpSuperDataSubmissionDtoDraftByConds(String orgId, String type, String hciCode);

    void deleteDpSuperDataSubmissionDtoDraftByConds(String orgId, String type, String hciCode);

}
