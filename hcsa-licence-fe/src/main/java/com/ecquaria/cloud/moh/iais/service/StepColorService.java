package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;

import java.util.Map;

/**
 * @author Wenkang
 * @date 2021/6/2 10:20
 */
public interface StepColorService {

    void setStepColor(Map<String,String> map, String serviceConfig , AppSubmissionDto appSubmissionDto);
}
