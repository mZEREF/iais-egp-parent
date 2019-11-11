package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * AppSubmisionServiceImpl
 *
 * @author suocheng
 * @date 11/6/2019
 */
@Service
@Slf4j
public class AppSubmissionServiceImpl implements AppSubmissionService {
    String draftUrl =  RestApiUrlConsts.HCSA_APP + RestApiUrlConsts.HCSA_APP_SUBMISSION_DRAFT;
    String submission = RestApiUrlConsts.HCSA_APP + RestApiUrlConsts.HCSA_APP_SUBMISSION;

    @Override
    public AppSubmissionDto submit(AppSubmissionDto appSubmissionDto) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return RestApiUtil.save(submission,appSubmissionDto,AppSubmissionDto.class);
    }

    @Override
    public AppSubmissionDto doSaveDraft(AppSubmissionDto appSubmissionDto) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return RestApiUtil.save(draftUrl,appSubmissionDto,AppSubmissionDto.class);
    }
}
