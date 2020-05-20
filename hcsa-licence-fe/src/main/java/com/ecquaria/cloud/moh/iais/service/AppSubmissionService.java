package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import sop.webflow.rt.api.Process;

import java.util.List;

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

    List<ApplicationDto> listApplicationByGroupId(String groupId);

    public AppSubmissionDto doSaveDraft(AppSubmissionDto appSubmissionDto);

    public String getDraftNo(String appType);
    public String getGroupNo(String appType);
    public FeeDto getGroupAmount(AppSubmissionDto appSubmissionDto);
    public PreOrPostInspectionResultDto judgeIsPreInspection(AppSubmissionDto appSubmissionDto);
    public void setRiskToDto(AppSubmissionDto appSubmissionDto);
    public AppSubmissionDto getAppSubmissionDtoByAppNo(String appNo);
    public AppSubmissionDto getAppSubmissionDto(String appNo);
    public AppSubmissionDto getAppSubmissionDtoByLicenceId(String licenceId);
    public FeeDto getGroupAmendAmount(AmendmentFeeDto amendmentFeeDto);
    public AppSubmissionDto submitRequestChange(AppSubmissionDto appSubmissionDto, Process process);
    public AppSubmissionDto submitRenew(AppSubmissionDto appSubmissionDto);
    public MsgTemplateDto getMsgTemplateById(String id);
    public void feSendEmail(EmailDto emailDto);
    public ApplicationGroupDto createApplicationDataByWithOutRenewal(RenewDto renewDto);
    public void updateApplicationsStatus(String appGroupId,String stuts);
    public boolean checkRenewalStatus(String licenceId);
    public AppSubmissionDto getExistBaseSvcInfo(List<String> licenceIds);
    void transform(AppSubmissionDto appSubmissionDto,String licenseeId);
    void saveAppsubmission(AppSubmissionDto appSubmissionDto );
    boolean compareAndSendEmail(AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto);
    void setDraftNo(AppSubmissionDto appSubmissionDto);
    void  saveAppGrpMisc(AppGroupMiscDto appGroupMiscDto);
    List<AppSubmissionDto> getAppSubmissionDtoByGroupNo(String groupNo);
}
