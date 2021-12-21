package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PgtStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssSuperDataSubmissionDto;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import java.util.Arrays;
import java.util.List;

/**
 * @Description VssFeClientFallback
 * @Auther fanghao on 12/21/2021.
 */
@Slf4j
public class VssFeClientFallback implements VssFeClient{
    private FeignResponseEntity getFeignResponseEntity(Object... params) {
        log.warn("--------Params: " + Arrays.toString(params) + "-----------");
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<VssSuperDataSubmissionDto> saveVssSuperDataSubmissionDto(VssSuperDataSubmissionDto vssSuperDataSubmissionDto) {
        return getFeignResponseEntity(vssSuperDataSubmissionDto);
    }

    @Override
    public FeignResponseEntity<VssSuperDataSubmissionDto> doUpdateDataSubmissionDraft(VssSuperDataSubmissionDto vssSuperDataSubmissionDto) {
        return getFeignResponseEntity(vssSuperDataSubmissionDto);
    }

    @Override
    public FeignResponseEntity<VssSuperDataSubmissionDto> getVssSuperDataSubmissionDtoDraftByDraftNo(String draftNo) {
        return getFeignResponseEntity(draftNo);
    }
    @Override
    public FeignResponseEntity<Void> deleteVssSuperDataSubmissionDraftById(String draftId) {
        return getFeignResponseEntity(draftId);
    }

    @Override
    public FeignResponseEntity<VssSuperDataSubmissionDto> getVssSuperDataSubmissionDto(String submissionNo) {
        return getFeignResponseEntity(submissionNo);
    }
    @Override
    public FeignResponseEntity<Void> updateDataSubmissionDraftStatus(String draftId, String status) {
        return getFeignResponseEntity(draftId, status);
    }

    @Override
    public FeignResponseEntity<VssSuperDataSubmissionDto> getVssSuperDataSubmissionDtoDraftByConds(String orgId, String type, String svcName, String hciCode) {
        return getFeignResponseEntity(orgId, type, svcName, hciCode);
    }

    @Override
    public void deleteVssSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType, String hciCode) {

    }

}
