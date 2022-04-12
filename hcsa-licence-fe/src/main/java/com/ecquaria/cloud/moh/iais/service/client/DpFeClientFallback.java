package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DrugMedicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PgtStageDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description DpFeClientFallback
 * @Auther chenlei on 11/18/2021.
 */
@Slf4j
public class DpFeClientFallback implements DpFeClient {

    private FeignResponseEntity getFeignResponseEntity(Object... params) {
        return IaisEGPHelper.getFeignResponseEntity(params);
    }

    @Override
    public FeignResponseEntity<DpSuperDataSubmissionDto> saveDpSuperDataSubmissionDto(
            DpSuperDataSubmissionDto dpSuperDataSubmissionDto) {
        return getFeignResponseEntity(dpSuperDataSubmissionDto);
    }

    @Override
    public FeignResponseEntity<DpSuperDataSubmissionDto> doUpdateDataSubmissionDraft(
            DpSuperDataSubmissionDto dpSuperDataSubmissionDto) {
        return getFeignResponseEntity(dpSuperDataSubmissionDto);
    }

    @Override
    public FeignResponseEntity<Void> updateDataSubmissionDraftStatus(String draftId, String status) {
        return getFeignResponseEntity(draftId, status);
    }

    @Override
    public FeignResponseEntity<Void> deleteDpSuperDataSubmissionDraftById(String draftId) {
        return getFeignResponseEntity(draftId);
    }

    @Override
    public FeignResponseEntity<DpSuperDataSubmissionDto> getDpSuperDataSubmissionDto(String submissionNo) {
        return getFeignResponseEntity(submissionNo);
    }

    @Override
    public FeignResponseEntity<PatientDto> getDpPatientDto(String idType, String idNumber, String nationality, String orgId) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<DpSuperDataSubmissionDto> getDpSuperDataSubmissionDtoByDraftNo(String draftNo) {
        return getFeignResponseEntity(draftNo);
    }

    @Override
    public FeignResponseEntity<DpSuperDataSubmissionDto> getDpSuperDataSubmissionDtoDraftByConds(String orgId, String type,
            String svcName, String hciCode) {
        return getFeignResponseEntity(orgId, type, svcName, hciCode);
    }

    @Override
    public FeignResponseEntity<List<PgtStageDto>> listPgtStageByPatientCode(String patientCode) {
        return getFeignResponseEntity(patientCode);
    }

    @Override
    public FeignResponseEntity<Void> deleteDpSuperDataSubmissionDtoDraftByConds(String orgId, String type, String hciCode) {
        return getFeignResponseEntity(orgId, type, hciCode);
    }
    @Override
    public FeignResponseEntity<List<DrugMedicationDto>> getDrugMedicationDtoBySubmissionNo(String submissionNo) {
        return getFeignResponseEntity(submissionNo);
    }
    @Override
    public FeignResponseEntity<List<DrugMedicationDto>> getDrugMedicationDtoBySubmissionNoForDispensed(String submissionNo) {
        return getFeignResponseEntity(submissionNo);
    }
}
