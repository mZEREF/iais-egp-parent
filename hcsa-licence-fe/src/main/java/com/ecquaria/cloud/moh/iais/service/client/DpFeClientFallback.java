package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PgtStageDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import java.util.Arrays;
import java.util.List;

/**
 * @Description DpFeClientFallback
 * @Auther chenlei on 11/18/2021.
 */
@Slf4j
public class DpFeClientFallback implements DpFeClient{

    private FeignResponseEntity getFeignResponseEntity(Object... params) {
        log.warn("--------Params: " + Arrays.toString(params) + "-----------");
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
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
    public FeignResponseEntity<DpSuperDataSubmissionDto> getDpSuperDataSubmissionDtoDraftByDraftNo(String draftNo) {
        return getFeignResponseEntity(draftNo);
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
    public FeignResponseEntity<DpSuperDataSubmissionDto> getDpSuperDataSubmissionDtoDraftByConds(String orgId, String type,
            String svcName, String hciCode) {
        return getFeignResponseEntity(orgId, type, svcName, hciCode);
    }

    @Override
    public FeignResponseEntity<List<PgtStageDto>> listPgtStageByPatientCode(String patientCode) {
        return getFeignResponseEntity(patientCode);
    }

    @Override
    public void deleteDpSuperDataSubmissionDtoDraftByConds(String orgId, String type, String hciCode) {
    }

}
