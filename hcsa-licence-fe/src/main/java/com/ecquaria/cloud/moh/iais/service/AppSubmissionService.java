package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGroupMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
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
    public FeeDto getNewAppAmount(AppSubmissionDto appSubmissionDto,boolean isCharity);
    public FeeDto getGroupAmount(AppSubmissionDto appSubmissionDto,boolean isCharity);
    public FeeDto getRenewalAmount(List<AppSubmissionDto> appSubmissionDtoList,boolean isCharity);
    public FeeDto getCharityRenewalAmount(List<AppSubmissionDto> appSubmissionDtoList,boolean isCharity);
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
    void setDraftNo(AppSubmissionDto appSubmissionDto);
    void  saveAppGrpMisc(AppGroupMiscDto appGroupMiscDto);
    List<AppSubmissionDto> getAppSubmissionDtoByGroupNo(String groupNo);
    void deleteOverdueDraft(String draftValidity);
    List<AppGrpPremisesDto> getAppGrpPremisesDto(String appNo);
    AppFeeDetailsDto saveAppFeeDetails(AppFeeDetailsDto appFeeDetailsDto);
    ApplicationDto getMaxVersionApp(String appNo);
    void updateMsgStatus(String msgId, String status);
    InterMessageDto getInterMessageById(String msgId);
    List<String> getHciFromPendAppAndLic(String licenseeId, List<HcsaServiceDto> hcsaServiceDtos);
    List<AppGrpPremisesEntityDto> getPendAppPremises(String licenseeId,List<HcsaServiceDto> hcsaServiceDtos);
    List<AppAlignLicQueryDto> getAppAlignLicQueryDto(String licenseeId,List<String> svcNames);
    List<AppGrpPremisesDto> getLicPremisesInfo(String id);
    Boolean isNewLicensee(String licenseeId);
    InterMessageDto getInterMessageBySubjectLike(String subject,String status);
    AppGrpPremisesEntityDto getPremisesByAppNo(String appNo);
    void sendEmailAndSMSAndMessage(AppSubmissionDto appSubmissionDto,String applicantName);
    AppGrpPrimaryDocDto getMaxVersionPrimaryDoc(String appGrpId,String configDocId);
}
