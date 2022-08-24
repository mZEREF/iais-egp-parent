package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @Description TopDataSubmissionService
 * @Auther chenlei on 12/22/2021.
 */
public interface TopDataSubmissionService {
    Map<String, PremisesDto> getTopCenterPremises(String licenseeId);

    String getDraftNo(String dsType, String draftNo);

    TopSuperDataSubmissionDto saveDataSubmissionDraft(TopSuperDataSubmissionDto topSuperDataSubmissionDto);

    TopSuperDataSubmissionDto getTopSuperDataSubmissionDto(String submissionNo);

    String getSubmissionNo(String dsType);

    TopSuperDataSubmissionDto saveTopSuperDataSubmissionDto(TopSuperDataSubmissionDto topSuperDataSubmissionDto);

    TopSuperDataSubmissionDto saveTopSuperDataSubmissionDtoToBE(TopSuperDataSubmissionDto topSuperDataSubmissionDto);

    void updateDataSubmissionDraftStatus(String draftId, String status);

    TopSuperDataSubmissionDto getTopSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType, String userId);

    TopSuperDataSubmissionDto getTopSuperDataSubmissionDtoRfcDraftByConds(String orgId, String submissionType, String dataSubmissionId, String userId);

    void deleteTopSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType, String appType);

    void deleteTopSuperDataSubmissionDtoRfcDraftByConds(String orgId, String submissionType, String dataSubmissionId);

    TopSuperDataSubmissionDto getTopSuperDataSubmissionDtoByDraftNo(String draftNo);

    PatientInformationDto getTopPatientSelect(String idType, String idNumber, String orgId);

    void displayToolTipJudgement(HttpServletRequest request);

//    String getDraftNo();

 //   String getSubmissionNo();

    List<SelectOption> getSourseList(HttpServletRequest request);
    List<SelectOption> getSourseListAge(HttpServletRequest request);
    List<SelectOption> getSourseLists(HttpServletRequest request);
    List<SelectOption> getSourseListsDrug(HttpServletRequest request);
}
