package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssFileDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @Description VssFeClientFallback
 * @Auther fanghao on 12/21/2021.
 */
@Slf4j
public class VssFeClientFallback implements VssFeClient{
    private FeignResponseEntity getFeignResponseEntity(Object... params) {
        return IaisEGPHelper.getFeignResponseEntity(params);
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
    public FeignResponseEntity<List<VssFileDto>> getListVssDocumentDtoStatus(String status) {
        return getFeignResponseEntity(status);
    }

    @Override
    public FeignResponseEntity<Void> updateVssDocumentStatusByTreId(String treatmentId, String status) {
        return  getFeignResponseEntity(treatmentId,status);
    }

    @Override
    public FeignResponseEntity<Void> updateDataSubmissionDraftStatus(String draftId, String status) {
        return getFeignResponseEntity(draftId, status);
    }

    @Override
    public FeignResponseEntity<VssSuperDataSubmissionDto> getVssSuperDataSubmissionDtoDraftByConds(String orgId, String type, String userId) {
        return getFeignResponseEntity(orgId, type);
    }

    @Override
    public FeignResponseEntity<Void> deleteVssSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType) {
        return getFeignResponseEntity(orgId, submissionType);
    }

}
