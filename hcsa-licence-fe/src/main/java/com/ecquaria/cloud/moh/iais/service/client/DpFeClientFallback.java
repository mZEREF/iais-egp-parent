package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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
    public FeignResponseEntity<DpSuperDataSubmissionDto> getDpSuperDataSubmissionDtoRfcDraftByConds(String orgId, String type, String svcName, String hciCode, String dataSubmissionId) {
        return getFeignResponseEntity(orgId, type, svcName, hciCode, dataSubmissionId);
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
    public FeignResponseEntity<Void> deleteDpSuperDataSubmissionDtoRfcDraftByConds(String orgId, String submissionType, String hciCode, String dataSubmissionId) {
        return getFeignResponseEntity(orgId, submissionType, hciCode,dataSubmissionId);
    }

    @Override
    public FeignResponseEntity<DrugPrescribedDispensedDto> getDrugMedicationDtoBySubmissionNo(String submissionNo) {
        return getFeignResponseEntity(submissionNo);
    }
    @Override
    public FeignResponseEntity<List<DrugMedicationDto>> getDrugMedicationDtoBySubmissionNoForDispensed(String pSubmissionNo,
                                                                                                       String dSubmissionNo) {
        return getFeignResponseEntity(pSubmissionNo,dSubmissionNo);
    }
}
