package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import sop.webflow.rt.api.Process;

/**
 * AppSubmissionService
 *
 * @author suocheng
 * @date 11/9/2019
 */
public interface AppSubmissionService {
    public AppSubmissionDto submit(AppSubmissionDto appSubmissionDto, Process process);
    public AppSubmissionDto submitRequestInformation(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto, Process process);
    public AppSubmissionDto submitPremisesListRequestInformation(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto, Process process);

    public AppSubmissionDto doSaveDraft(AppSubmissionDto appSubmissionDto);

    public String getDraftNo(String appType);
    public String getGroupNo(String appType);
    public FeeDto getGroupAmount(AppSubmissionDto appSubmissionDto);
    public PreOrPostInspectionResultDto judgeIsPreInspection(AppSubmissionDto appSubmissionDto);
    public void setRiskToDto(AppSubmissionDto appSubmissionDto);
    public AppSubmissionDto getAppSubmissionDtoByAppNo(String appNo);
    public AppSubmissionDto getAppSubmissionDtoByLicenceId(String licenceId);
    public FeeDto getGroupAmendAmount(AmendmentFeeDto amendmentFeeDto);
    public AppSubmissionDto submitRequestChange(AppSubmissionDto appSubmissionDto, Process process);
    public AppSubmissionDto submitRenew(AppSubmissionDto appSubmissionDto);
    public MsgTemplateDto getMsgTemplateById(String id);
    public void feSendEmail(EmailDto emailDto);
}
