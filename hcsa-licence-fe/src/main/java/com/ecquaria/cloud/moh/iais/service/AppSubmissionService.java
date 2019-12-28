package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import sop.webflow.rt.api.Process;

/**
 * AppSubmissionService
 *
 * @author suocheng
 * @date 11/9/2019
 */
public interface AppSubmissionService {
    public AppSubmissionDto submit(AppSubmissionDto appSubmissionDto, Process process);
    public AppSubmissionDto submitRequestInformation(AppSubmissionDto appSubmissionDto, Process process);

    public AppSubmissionDto doSaveDraft(AppSubmissionDto appSubmissionDto);

    public String getDraftNo(String appType);
    public String getGroupNo(String appType);
    public  Double getGroupAmount(AppSubmissionDto appSubmissionDto);
    public PreOrPostInspectionResultDto judgeIsPreInspection(AppSubmissionDto appSubmissionDto);
    public void setRiskToDto(AppSubmissionDto appSubmissionDto);
    public AppSubmissionDto getAppSubmissionDtoByAppNo(String appNo);
}
