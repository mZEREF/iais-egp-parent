package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;

/**
 * AppSubmisionService
 *
 * @author suocheng
 * @date 11/6/2019
 */
public interface AppSubmisionService {
    public void submit(AppSubmissionDto appSubmissionDto);
}
