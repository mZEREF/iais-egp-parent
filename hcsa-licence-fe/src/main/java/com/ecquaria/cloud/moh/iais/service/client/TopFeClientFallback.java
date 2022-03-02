package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;

/**
 * @Description TopFeClientFallback
 * @Auther zhixing on 12/28/2021.
 */
@Slf4j
public class TopFeClientFallback implements TopFeClient{
    private FeignResponseEntity getFeignResponseEntity(Object... params) {
        return IaisEGPHelper.getFeignResponseEntity(params);
    }
    @Override
    public FeignResponseEntity<TopSuperDataSubmissionDto> saveTopSuperDataSubmissionDto(TopSuperDataSubmissionDto topSuperDataSubmissionDto) {
        return getFeignResponseEntity(topSuperDataSubmissionDto);
    }

    @Override
    public FeignResponseEntity<TopSuperDataSubmissionDto> doUpdateDataSubmissionDraft(TopSuperDataSubmissionDto TopSuperDataSubmissionDto) {
        return getFeignResponseEntity(TopSuperDataSubmissionDto);
    }

    @Override
    public FeignResponseEntity<TopSuperDataSubmissionDto> getTopSuperDataSubmissionDtoDraftByDraftNo(String draftNo) {
        return getFeignResponseEntity(draftNo);
    }

    @Override
    public FeignResponseEntity<Void> updateDataSubmissionDraftStatus(String draftId, String status) {
        return getFeignResponseEntity(draftId, status);
    }

    @Override
    public FeignResponseEntity<TopSuperDataSubmissionDto> getTopSuperDataSubmissionDtoDraftByConds(String orgId, String Type) {
        return getFeignResponseEntity(orgId, Type);
    }

    @Override
    public void deleteTopSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType, String hciCode) {

    }

    @Override
    public FeignResponseEntity<Void> deleteTopSuperDataSubmissionDraftById(String draftId) {
        return getFeignResponseEntity(draftId);
    }

    @Override
    public FeignResponseEntity<TopSuperDataSubmissionDto> getTopSuperDataSubmissionDto(String submissionNo) {
        return getFeignResponseEntity(submissionNo);
    }

    @Override
    public FeignResponseEntity<PatientInformationDto> getTopPatientSelect(String idType, String idNumber, String nationality, String orgId) {
        return getFeignResponseEntity();
    }
}
