package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppAlignAppQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppLicBundleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import sop.webflow.rt.api.BaseProcessClass;
import sop.webflow.rt.api.Process;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * AppSubmissionService
 *
 * @author suocheng
 * @date 11/9/2019
 */
public interface AppSubmissionService {

    AppSubmissionDto submit(AppSubmissionDto appSubmissionDto, Process process);

    AppSubmissionDto submitRequestInformation(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto, Process process);

    AppSubmissionDto submitRequestRfcRenewInformation(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto,
            Process process);

    List<ApplicationDto> listApplicationByGroupId(String groupId);

    List<AppSubmissionDto> saveAppsForRequestForGoupAndAppChangeByList(List<AppSubmissionDto> appSubmissionDtos);

    AppSubmissionDto getAppSubmissionDtoDraft(String draftNo);

    List<AppSubmissionDto> getAppSubmissionDtoDrafts(String draftNo);

    void handleDraft(String draftNo, String licenseeId, AppSubmissionDto appSubmissionDto,
            List<AppSubmissionDto> appSubmissionDtoList);

    void handleDraft(String licenseeId, List<AppSubmissionDto> appSubmissionDtos);

    AppSubmissionDto doSaveDraft(AppSubmissionDto appSubmissionDto);

    void updateDrafts(String licenseeId, List<String> licenceIds, String draftNo);

    void deleteDraftAsync(String draftNo, String appGrpId);

    String getDraftNo(String appType);

    String getGroupNo(String appType);

    FeeDto getNewAppAmount(AppSubmissionDto appSubmissionDto, boolean isCharity);

    FeeDto getRenewalAmount(List<AppSubmissionDto> appSubmissionDtoList, boolean isCharity);


    PreOrPostInspectionResultDto judgeIsPreInspection(AppSubmissionDto appSubmissionDto);

    void setRiskToDto(AppSubmissionDto appSubmissionDto);

    RiskResultDto getRiskResultDtoByServiceCode(List<RiskResultDto> riskResultDtoList, String serviceCode);

    AppSubmissionDto getAppSubmissionDtoByAppNo(String appNo);

    AppSubmissionDto getAppSubmissionDto(String appNo);

    AppSubmissionDto getAppSubmissionDtoByLicenceId(String licenceId);

    AppSubmissionDto viewAppSubmissionDto(String licenceId);

    AppSubmissionDto submitRenew(AppSubmissionDto appSubmissionDto);

    MsgTemplateDto getMsgTemplateById(String id);

    void feSendEmail(EmailDto emailDto);

    void updateApplicationsStatus(String appGroupId, String stuts);

    void setDraftNo(AppSubmissionDto appSubmissionDto);

    void deleteOverdueDraft(String draftValidity);

    AppFeeDetailsDto saveAppFeeDetails(AppFeeDetailsDto appFeeDetailsDto);

    ApplicationDto getMaxVersionApp(String appNo);

    void updateMsgStatus(String msgId, String status);

    InterMessageDto getInterMessageById(String msgId);

    List<AppAlignLicQueryDto> getAppAlignLicQueryDto(String licenseeId, List<String> svcNames, List<String> premTypeList);

    List<AppAlignAppQueryDto> getAppAlignAppQueryDto(String licenseeId, List<String> svcIdList);

    Boolean isNewLicensee(String licenseeId);

    InterMessageDto getInterMessageBySubjectLike(String subject, String status);

    void sendEmailAndSMSAndMessage(AppSubmissionDto appSubmissionDto, String applicantName);

    List<InterMessageDto> getInterMessageByRefNo(String refNo);

    AppSubmissionDto getAppSubmissionDtoByAppGrpNo(String appGrpNo);

    void setPreviewDta(AppSubmissionDto appSubmissionDto, BaseProcessClass bpc) throws CloneNotSupportedException;

    void sendEmailForGiroFailAndSMSAndMessage(ApplicationGroupDto applicationGroupDto);

    ApplicationDto getAppById(String appId);

    List<MenuLicenceDto> setPremAdditionalInfo(List<MenuLicenceDto> menuLicenceDtos);

    List<GiroAccountInfoDto> getGiroAccountByHciCodeAndOrgId(List<String> hciCode, String orgId);

    List<String> saveFileList(List<File> fileList);

    void updateDraftStatus(String draftNo, String status);

    List<ApplicationSubDraftDto> getDraftListBySvcCodeAndStatus(List<String> svcCodeList, String status, String licenseeId,
            String appType);

    boolean canApplyEasOrMts(String licenseeId, List<HcsaServiceDto> hcsaServiceDtos);

    List<AppDeclarationDocDto> getAppDeclarationDocDto(HttpServletRequest request);

    LicenceDto getLicenceDtoById(String licenceId);

    SubLicenseeDto getSubLicenseeByLicenseeId(String licenseeId, String uenNo);

    boolean validateSubLicenseeDto(Map<String, String> errorMap, SubLicenseeDto subLicenseeDto, HttpServletRequest request);

    void updatePayment(AppSubmissionDto appSubmissionDto, String pmtRefNo);

    List<AppLicBundleDto> getBundleList(String item, boolean licOrApp);

    List<ApplicationDto> getApplicationsByLicenseeId(String licenseeId);

}
