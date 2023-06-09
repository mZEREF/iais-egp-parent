package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DrugMedicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DrugPrescribedDispensedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

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

    List<DpSuperDataSubmissionDto> saveDpSuperDataSubmissionDtoList(List<DpSuperDataSubmissionDto> dpSuperDataSubmissionDto);


    List<DpSuperDataSubmissionDto> saveDpSuperDataSubmissionDtoToBE(List<DpSuperDataSubmissionDto> dpSuperDataSubmissionDto);

    void updateDataSubmissionDraftStatus(String draftId, String status);

    DpSuperDataSubmissionDto getDpSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType, String svcName,
            String hciCode, String userId);

    DpSuperDataSubmissionDto getDpSuperDataSubmissionDtoRfcDraftByConds(String orgId, String submissionType, String svcName, String hciCode, String dataSubmissionId, String userId);

    void deleteDpSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType, String hciCode);

    void deleteDpSuperDataSubmissionDtoRfcDraftByConds(String orgId, String submissionType, String hciCode, String dataSubmissionId);

    ProfessionalResponseDto retrievePrsInfo(String doctorReignNo);

    DpSuperDataSubmissionDto getDpSuperDataSubmissionDto(String submissionNo);

    DpSuperDataSubmissionDto getDpSuperDataSubmissionDtoByDraftNo(String draftNo);

    DrugPrescribedDispensedDto getDrugMedicationDtoBySubmissionNo(String submissionNo);

    List<DrugMedicationDto> getDrugMedicationDtoBySubmissionNoForDispensed(String submissionNo,String rfcSubmissionNo);

    void displayToolTipJudgement(HttpServletRequest request);

}
