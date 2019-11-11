package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;

/**
 * AppSubmissionService
 *
 * @author suocheng
 * @date 11/9/2019
 */
public interface AppSubmissionService {
    public void submit(AppSubmissionDto appSubmissionDto);

    public AppSubmissionDto doSaveDraft(AppSubmissionDto appSubmissionDto);

}
