package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGroupMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
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
     AppSubmissionDto submitRequestRfcRenewInformation(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto, Process process);
     AppSubmissionDto submitPremisesListRequestInformation(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto, Process process);

    List<ApplicationDto> listApplicationByGroupId(String groupId);

     AppSubmissionDto doSaveDraft(AppSubmissionDto appSubmissionDto);

     String getDraftNo(String appType);
     String getGroupNo(String appType);
     FeeDto getNewAppAmount(AppSubmissionDto appSubmissionDto,boolean isCharity);
     FeeDto getGroupAmount(AppSubmissionDto appSubmissionDto,boolean isCharity);
     FeeDto getRenewalAmount(List<AppSubmissionDto> appSubmissionDtoList,boolean isCharity);
     FeeDto getCharityRenewalAmount(List<AppSubmissionDto> appSubmissionDtoList,boolean isCharity);
     PreOrPostInspectionResultDto judgeIsPreInspection(AppSubmissionDto appSubmissionDto);
     void setRiskToDto(AppSubmissionDto appSubmissionDto);
     AppSubmissionDto getAppSubmissionDtoByAppNo(String appNo);
     AppSubmissionDto getAppSubmissionDto(String appNo);
     AppSubmissionDto getAppSubmissionDtoByLicenceId(String licenceId);
     AppSubmissionDto viewAppSubmissionDto(String licenceId);
     FeeDto getGroupAmendAmount(AmendmentFeeDto amendmentFeeDto);
     AppSubmissionDto submitRequestChange(AppSubmissionDto appSubmissionDto, Process process);
     AppSubmissionDto submitRenew(AppSubmissionDto appSubmissionDto);
     MsgTemplateDto getMsgTemplateById(String id);
     void feSendEmail(EmailDto emailDto);
     ApplicationGroupDto createApplicationDataByWithOutRenewal(RenewDto renewDto);
     void updateApplicationsStatus(String appGroupId,String stuts);
     boolean checkRenewalStatus(String licenceId);
     AppSubmissionDto getExistBaseSvcInfo(List<String> licenceIds);
    void transform(AppSubmissionDto appSubmissionDto,String licenseeId) throws Exception;
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
    AppGrpPrimaryDocDto getMaxVersionPrimaryComDoc(String appGrpId,String configDocId,String seqNum);
    AppSvcDocDto getMaxVersionSvcComDoc(String appGrpId, String configDocId,String seqNum);
    AppGrpPrimaryDocDto getMaxVersionPrimarySpecDoc(String appGrpId,String configDocId,String appNo,String seqNum);
    AppSvcDocDto getMaxVersionSvcSpecDoc(String appGrpId, String configDocId,String appNo,String seqNum);
    AppSubmissionDto getAppSubmissionDtoByAppGrpNo(String appGrpNo);
    List<AppGrpPrimaryDocDto> syncPrimaryDoc(String appType,Boolean isRfi,List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos, List<HcsaSvcDocConfigDto> primaryDocConfig) throws CloneNotSupportedException;
    List<AppGrpPrimaryDocDto> handlerPrimaryDoc(List<AppGrpPremisesDto> appGrpPremisesDtos,List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos);
    Map<String, String> doPreviewAndSumbit(BaseProcessClass bpc);
    Map<String, List<HcsaSvcPersonnelDto>> getAllSvcAllPsnConfig(HttpServletRequest request);
    Map<String, String> doCheckBox(BaseProcessClass bpc, StringBuilder sB, Map<String, List<HcsaSvcPersonnelDto>> allSvcAllPsnConfig, List<HcsaSvcPersonnelDto> currentSvcAllPsnConfig, AppSvcRelatedInfoDto dto, int uploadFileLimit, String sysFileType,List<AppGrpPremisesDto> appGrpPremisesDtos);
    List<AppGrpPrimaryDocDto> documentValid(HttpServletRequest request, Map<String, String> errorMap,boolean setIsPassValidate);
    Map<String,String> doPreviewSubmitValidate(Map<String,String> previewAndSubmitMap,AppSubmissionDto appSubmissionDto,AppSubmissionDto oldAppSubmissionDto,BaseProcessClass bpc);
    boolean isGiroAccount(String licenseeId);
    void removePreviousPremTypeInfo(AppSubmissionDto appSubmissionDto) throws CloneNotSupportedException;
    void changeSvcScopeIdByConfigName(List<HcsaSvcSubtypeOrSubsumedDto> newConfigInfo,AppSubmissionDto appSubmissionDto) throws CloneNotSupportedException;
    Map<String,List<AppSvcDisciplineAllocationDto>> getDisciplineAllocationDtoList(AppSubmissionDto appSubmissionDto,String svcId) throws CloneNotSupportedException;
    void setPreviewDta(AppSubmissionDto appSubmissionDto,BaseProcessClass bpc) throws CloneNotSupportedException;
     void sendEmailForGiroAccountAndSMSAndMessage(AppSubmissionDto appSubmissionDto,String applicantName);
    List<LicAppCorrelationDto> getLicDtoByLicId(String licId);
    ApplicationDto getAppById(String appId);
    List<MenuLicenceDto> setPremAdditionalInfo(List<MenuLicenceDto> menuLicenceDtos);
    List<GiroAccountInfoDto> getGiroAccountByHciCodeAndOrgId(List<String> hciCode,String orgId);
    boolean checkIsGiroAcc(AppSubmissionDto appSubmissionDto,String orgId);
    List<String> saveFileList(List<File> fileList);
    List<AppGrpPrimaryDocDto> getMaxSeqNumPrimaryDocList(String appGrpId);
    List<AppSvcDocDto> getMaxSeqNumSvcDocList(String appGrpId);
    void updateDraftStatus(String draftNo,String status);
    List<ApplicationSubDraftDto> getDraftListBySvcCodeAndStatus(List<String> svcCodeList,String status,String licenseeId,String appType);
}
