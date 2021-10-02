package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.action.ClinicalLaboratoryDelegator;
import com.ecquaria.cloud.moh.iais.action.HcsaFileAjaxController;
import com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.constant.renewal.RenewalConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesDoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonAndExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGroupMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDisciplineAllocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcLaboratoryDisciplinesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPrincipalOfficersDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcBusinessDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.HcsaFeeBundleItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.LicenceFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeIndividualDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RecommendInspectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgGiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceFeConstant;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.NewApplicationConstant;
import com.ecquaria.cloud.moh.iais.dto.AppDeclarationDocShowPageDto;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.PageShowFileDto;
import com.ecquaria.cloud.moh.iais.dto.ServiceStepDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.LicenseeService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.AppEicClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.ComFileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.EicClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FeMessageClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationLienceseeClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.moh.iais.utils.SingeFileUtil;
import com.ecquaria.cloud.moh.iais.validate.serviceInfo.ValidateCharges;
import com.ecquaria.cloud.moh.iais.validate.serviceInfo.ValidateClincalDirector;
import com.ecquaria.cloud.moh.iais.validate.serviceInfo.ValidateVehicle;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sop.webflow.rt.api.BaseProcessClass;
import sop.webflow.rt.api.Process;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * AppSubmisionServiceImpl
 *
 * @author suocheng
 * @date 11/6/2019
 */
@Service
@Slf4j
public class AppSubmissionServiceImpl implements AppSubmissionService {
   // String draftUrl =  RestApiUrlConsts.HCSA_APP + RestApiUrlConsts.HCSA_APP_SUBMISSION_DRAFT;
    //String submission = RestApiUrlConsts.HCSA_APP + RestApiUrlConsts.HCSA_APP_SUBMISSION;

    @Autowired
    private ApplicationFeClient applicationFeClient;
    @Autowired
    private AppConfigClient appConfigClient;
    @Autowired
    private LicenceClient licenceClient;
    @Autowired
    private EventBusHelper eventBusHelper;
    @Autowired
    private AppEicClient appEicClient;
    @Autowired
    private ComFileRepoClient comFileRepoClient;
    @Autowired
    private ValidateCharges validateCharges;
    @Autowired
    private ValidateVehicle validateVehicle;
    @Autowired
    private ValidateClincalDirector validateClincalDirector;
    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Autowired
    private SystemAdminClient systemAdminClient;
    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private FeEicGatewayClient feEicGatewayClient;
    @Autowired
    private FeMessageClient feMessageClient;
    @Autowired
    private AppSubmissionService appSubmissionService;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    private ServiceConfigServiceImpl serviceConfigService;
    @Autowired
    private RequestForChangeServiceImpl requestForChangeService;
    @Autowired
    private OrganizationLienceseeClient organizationLienceseeClient;
    @Autowired
    private LicenseeService licenseeService;

    @Value("${moh.halp.prs.enable}")
    private String prsFlag;

    @Override
    public AppSubmissionDto submit(AppSubmissionDto appSubmissionDto, Process process) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appSubmissionDto= applicationFeClient.saveSubmision(appSubmissionDto).getEntity();
        //asynchronous save the other data.
        eventBus(appSubmissionDto, process);
        return appSubmissionDto;
    }
    @Override
    public void sendEmailAndSMSAndMessage(AppSubmissionDto appSubmissionDto,String applicantName){
        if(appSubmissionDto.getAppType().equals(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION)){
            try{
                List<ApplicationDto> applicationDtos = appSubmissionDto.getApplicationDtos();
                if(IaisCommonUtils.isEmpty(applicationDtos)){
                    applicationDtos = applicationFeClient.getApplicationsByGroupNo(appSubmissionDto.getAppGrpNo()).getEntity();
                    appSubmissionDto.setApplicationDtos(applicationDtos);
                }
                ApplicationDto applicationDto =  applicationDtos.get(0);
                String applicationType =  MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType());
                int index = 0;
                StringBuilder stringBuilderAPPNum = new StringBuilder();
                String temp = "have";
                if(appSubmissionDto.getApplicationDtos().size() == 1){
                    temp = "has";
                }
                for(ApplicationDto applicationDtoApp : appSubmissionDto.getApplicationDtos()){
                    if(index == 0){
                        stringBuilderAPPNum.append(applicationDtoApp.getApplicationNo());
                    }else {
                        stringBuilderAPPNum.append(" and ");
                        stringBuilderAPPNum.append(applicationDtoApp.getApplicationNo());
                    }
                    index++;
                }
                String applicationNumber = stringBuilderAPPNum.toString();
                Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
                subMap.put("ApplicationType", applicationType);
                subMap.put("ApplicationNumber", applicationNumber);
                subMap.put("temp", temp);
                String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_EMAIL,subMap);
                String smsSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_SMS,subMap);
                String messageSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_MSG,subMap);
                log.debug(StringUtil.changeForLog("emailSubject : " + emailSubject));
                log.debug(StringUtil.changeForLog("smsSubject : " + smsSubject));
                log.debug(StringUtil.changeForLog("messageSubject : " + messageSubject));
                Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
                templateContent.put("ApplicantName", applicantName);
                templateContent.put("ApplicationType",  applicationType);
                templateContent.put("ApplicationNumber", applicationNumber);
                templateContent.put("ApplicationDate", Formatter.formatDateTime(new Date()));
                templateContent.put("isSelfAssessment","No");
                String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
                templateContent.put("systemLink", loginUrl);
                String paymentMethodName = "noNeedPayment";
                String payMethod = appSubmissionDto.getPaymentMethod();
                if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod)) {
                    paymentMethodName = "GIRO";
                    templateContent.put("usualDeduction","next 7 working days");
                    templateContent.put("accountNumber",appSubmissionDto.getGiroAcctNum());
                    //need change giro
                }else if (ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT.equals(payMethod)
                        || ApplicationConsts.PAYMENT_METHOD_NAME_NETS.equals(payMethod)
                        || ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW.equals(payMethod)) {
                    paymentMethodName = "onlinePayment";
                }
                templateContent.put("emailAddress", systemParamConfig.getSystemAddressOne());
                templateContent.put("paymentMethod", paymentMethodName);
                templateContent.put("paymentAmount", appSubmissionDto.getTotalAmountGroup() == null ?  appSubmissionDto.getAmountStr() : String.valueOf(appSubmissionDto.getTotalAmountGroup()));
                String syName = "<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"<br/>"+AppConsts.MOH_AGENCY_NAME+"</b>";
                templateContent.put("MOH_AGENCY_NAME",syName);
                EmailParam emailParam = new EmailParam();
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_EMAIL);
                emailParam.setTemplateContent(templateContent);
                emailParam.setSubject(emailSubject);
                emailParam.setQueryCode(applicationDto.getApplicationNo());
                emailParam.setReqRefNum(applicationDto.getApplicationNo());
                emailParam.setRefId(applicationDto.getApplicationNo());
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                notificationHelper.sendNotification(emailParam);

                EmailParam smsParam = new EmailParam();
                smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_SMS);
                smsParam.setSubject(smsSubject);
                smsParam.setQueryCode(applicationDto.getApplicationNo());
                smsParam.setReqRefNum(applicationDto.getApplicationNo());
                smsParam.setRefId(applicationDto.getApplicationNo());
                smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
                notificationHelper.sendNotification(smsParam);

                EmailParam msgParam = new EmailParam();
                msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_NAP_001_MSG);
                msgParam.setTemplateContent(templateContent);
                msgParam.setSubject(messageSubject);
                msgParam.setQueryCode(applicationDto.getApplicationNo());
                msgParam.setReqRefNum(applicationDto.getApplicationNo());
                msgParam.setRefId(applicationDto.getApplicationNo());
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
                List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList){
                    if( !svcCodeList.contains(appSvcRelatedInfoDto.getServiceCode())){
                        svcCodeList.add(appSvcRelatedInfoDto.getServiceCode());
                    }
                }
                msgParam.setSvcCodeList(svcCodeList);
                msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                notificationHelper.sendNotification(msgParam);
                log.info("end send email sms and msg");
            }catch (Exception e){
                log.error(e.getMessage(),e);
                log.info("send app sumbit email fail");
            }
        }

    }

    @Override
    public List<LicAppCorrelationDto> getLicDtoByLicId(String licId) {
        return licenceClient.getLicCorrBylicId(licId).getEntity();
    }

    @Override
    public ApplicationDto getAppById(String appId) {
        return applicationFeClient.getApplicationById(appId).getEntity();
    }

    @Override
    public List<MenuLicenceDto> setPremAdditionalInfo(List<MenuLicenceDto> menuLicenceDtos) {
        return licenceClient.setPremAdditionalInfo(menuLicenceDtos).getEntity();
    }

    @Override
    public List<GiroAccountInfoDto> getGiroAccountByHciCodeAndOrgId(List<String> hciCode, String orgId) {
        log.debug("AppSubmissionServiceImpl getGiroAccount [hciCode] hciCode is empty: {},[orgId] orgId:{}",IaisCommonUtils.isEmpty(hciCode) ,orgId);
        return licenceClient.getGiroAccountByHciCodeAndOrgId(hciCode,orgId).getEntity();
    }

    @Override
    public boolean checkIsGiroAcc(AppSubmissionDto appSubmissionDto, String orgId) {
        boolean isGiroAcc = false;
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        if(!IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
            List<String> hciCodeList = IaisCommonUtils.genNewArrayList();
            for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                String hciCode = appGrpPremisesDto.getHciCode();
                if(!StringUtil.isEmpty(hciCode)){
                    hciCodeList.add(hciCode);
                }
            }
            log.debug(StringUtil.changeForLog("hciCodeList size:"+hciCodeList.size()));
            if(hciCodeList.size() > 0 && !StringUtil.isEmpty(orgId)){
                log.debug("checkIsGiroAcc [orgId] orgId is {}",orgId);
                List<GiroAccountInfoDto> giroAccountInfoDtos = getGiroAccountByHciCodeAndOrgId(hciCodeList,orgId);
                if(giroAccountInfoDtos != null){
                    log.debug(StringUtil.changeForLog("giroAccountInfoDtos size:"+giroAccountInfoDtos.size()));
                    if(giroAccountInfoDtos.size() > 1){
                        String targetAcctNo = giroAccountInfoDtos.get(0).getAcctNo();
                        log.debug("checkIsGiroAcc [targetAcctNo] targetAcctNo is {}",targetAcctNo);
                        if(!StringUtil.isEmpty(targetAcctNo)){
                            for(int i=1;i<giroAccountInfoDtos.size();i++){
                                String acctNo = giroAccountInfoDtos.get(i).getAcctNo();
                                log.debug("checkIsGiroAcc [acctNo] acctNo is {}",acctNo);
                                if(!StringUtil.isEmpty(acctNo) && !targetAcctNo.equals(acctNo)){
                                    break;
                                }
                                if(i == giroAccountInfoDtos.size()-1){
                                    isGiroAcc = true;
                                }
                            }
                        }
                    }else if(giroAccountInfoDtos.size() == 1){
                        isGiroAcc = true;
                    }
                }
                if(isGiroAcc){
                    appSubmissionDto.setGiroAcctNum(giroAccountInfoDtos.get(0).getAcctNo());
                }
            }
        }
        log.debug("checkIsGiroAcc [isGiroAcc] isGiroAcc {}",isGiroAcc);
        return isGiroAcc;
    }

    @Override
    public List<String> saveFileList(List<File> fileList) {
        return comFileRepoClient.saveFileRepo(fileList);
    }

    @Override
    public List<AppGrpPrimaryDocDto> getMaxSeqNumPrimaryDocList(String appGrpId) {
        return applicationFeClient.getMaxSeqNumPrimaryDocList(appGrpId).getEntity();
    }

    @Override
    public List<AppSvcDocDto> getMaxSeqNumSvcDocList(String appGrpId) {
        return applicationFeClient.getMaxSeqNumSvcDocList(appGrpId).getEntity();
    }

    @Override
    public void updateDraftStatus(String draftNo, String status) {
        log.debug(StringUtil.changeForLog("The doPaymentUpDate start ..."));
        applicationFeClient.updateDraftStatus(draftNo,status);
        log.debug(StringUtil.changeForLog("updateDraftStatus end ..."));
    }

    @Override
    public ProfessionalResponseDto retrievePrsInfo(String profRegNo){
        log.debug(StringUtil.changeForLog("retrieve prs info start ..."));
        log.info(StringUtil.changeForLog("prof Reg No is " + profRegNo + " - PRS flag is " + prsFlag));
        ProfessionalResponseDto professionalResponseDto = null;
        if ("Y".equals(prsFlag) && !StringUtil.isEmpty(profRegNo)) {
            List<String> prgNos = IaisCommonUtils.genNewArrayList();
            prgNos.add(profRegNo);
            ProfessionalParameterDto professionalParameterDto =new ProfessionalParameterDto();
            professionalParameterDto.setRegNo(prgNos);
            professionalParameterDto.setClientId("22222");
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String format = simpleDateFormat.format(new Date());
            professionalParameterDto.setTimestamp(format);
            professionalParameterDto.setSignature("2222");
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            try {
                List<ProfessionalResponseDto> professionalResponseDtos = feEicGatewayClient.getProfessionalDetail(professionalParameterDto, signature.date(), signature.authorization(),
                        signature2.date(), signature2.authorization()).getEntity();
                if(professionalResponseDtos != null && professionalResponseDtos.size() > 0){
                    professionalResponseDto = professionalResponseDtos.get(0);
                }
                log.info(StringUtil.changeForLog("ProfessionalResponseDto: " + JsonUtil.parseToJson(professionalResponseDto)));
            } catch (Exception e) {
                professionalResponseDto = new ProfessionalResponseDto();
                professionalResponseDto.setHasException(true);
                log.info(StringUtil.changeForLog("retrieve prs info start ..."));
                log.error(StringUtil.changeForLog(e.getMessage()), e);
            }
        }
        log.debug(StringUtil.changeForLog("retrieve prs res dto end ..."));
        return professionalResponseDto;
    }

    @Override
    public List<ApplicationSubDraftDto> getDraftListBySvcCodeAndStatus(List<String> svcCodeList, String status, String licenseeId,String appType) {
        return applicationFeClient.getDraftListBySvcCodeAndStatus(svcCodeList,licenseeId,status,appType).getEntity();
    }

    @Override
    public boolean canApplyEasOrMts(String licenseeId, List<HcsaServiceDto> hcsaServiceDtos) {
        log.debug(StringUtil.changeForLog("check can create eas or mts service start ..."));
        boolean canCreateEasOrMts = false;
        if(!StringUtil.isEmpty(licenseeId) && !IaisCommonUtils.isEmpty(hcsaServiceDtos)){
            List<String> svcNames = IaisCommonUtils.genNewArrayList();
            for(HcsaServiceDto hcsaServiceDto:hcsaServiceDtos){
                svcNames.add(hcsaServiceDto.getSvcName());
            }
            AppPremisesDoQueryDto appPremisesDoQueryDto = new AppPremisesDoQueryDto();
            List<HcsaServiceDto>  HcsaServiceDtoList= appConfigClient.getHcsaServiceByNames(svcNames).getEntity();
            List<String> svcIds = IaisCommonUtils.genNewArrayList();
            for(HcsaServiceDto hcsaServiceDto:HcsaServiceDtoList){
                svcIds.add(hcsaServiceDto.getId());
            }
            appPremisesDoQueryDto.setLicenseeId(licenseeId);
            appPremisesDoQueryDto.setSvcIdList(svcIds);
            String svcNameStr = JsonUtil.parseToJson(svcNames);
            List<PremisesDto> premisesDtos = licenceClient.getPremisesByLicseeIdAndSvcName(licenseeId,svcNameStr).getEntity();
            List<AppGrpPremisesEntityDto> appGrpPremisesEntityDtos = applicationFeClient.getPendAppPremises(appPremisesDoQueryDto).getEntity();
            log.debug("licence record size {}",premisesDtos.size());
            log.debug("pending application record size {}",appGrpPremisesEntityDtos.size());
            if(IaisCommonUtils.isEmpty(premisesDtos) && IaisCommonUtils.isEmpty(appGrpPremisesEntityDtos)){
                canCreateEasOrMts = true;
            }
        }
        log.debug(StringUtil.changeForLog("check can create eas or mts service end ..."));
        return canCreateEasOrMts;
    }

    @Override
    public AppDeclarationDocShowPageDto getFileAppDecInfo(List<AppDeclarationDocDto> appDeclarationDocDtoList) {

//            List<AppPremisesSpecialDocDto> viewDoc = withdrawnDto.getAppPremisesSpecialDocDto();
            List<PageShowFileDto> pageShowFileDtos = IaisCommonUtils.genNewArrayList();
            Map<String,File> map= IaisCommonUtils.genNewHashMap();
            Map<String, PageShowFileDto> pageShowFileHashMap = IaisCommonUtils.genNewHashMap();
            AppDeclarationDocShowPageDto appDeclarationDocShowPageDto = new AppDeclarationDocShowPageDto();
            if (appDeclarationDocDtoList != null && !appDeclarationDocDtoList.isEmpty()) {
                for (int i = 0; i < appDeclarationDocDtoList.size(); i++) {
                    PageShowFileDto pageShowFileDto = new PageShowFileDto();
                    String index = appDeclarationDocDtoList.get(i).getSeqNum().toString();
                    if (StringUtil.isEmpty(index)){
                        pageShowFileDto.setIndex(String.valueOf(i));
                        pageShowFileDto.setFileMapId("selectedFileDiv" + i);
                    }else{
                        pageShowFileDto.setFileMapId("selectedFileDiv" + index);
                        pageShowFileDto.setIndex(index);
                    }
                    pageShowFileDto.setFileName(appDeclarationDocDtoList.get(i).getDocName());
                    pageShowFileDto.setSize(appDeclarationDocDtoList.get(i).getDocSize());
                    pageShowFileDto.setMd5Code(appDeclarationDocDtoList.get(i).getMd5Code());
                    pageShowFileDto.setFileUploadUrl(appDeclarationDocDtoList.get(i).getFileRepoId());
                    pageShowFileDtos.add(pageShowFileDto);
                    if (StringUtil.isEmpty(index)){
                        map.put("selectedFile" + i, null);
                        pageShowFileHashMap.put("selectedFile" + i, pageShowFileDto);
                    }else{
                        map.put("selectedFile" + index, null);
                        pageShowFileHashMap.put("selectedFile" + index, pageShowFileDto);
                    }
                }
                appDeclarationDocShowPageDto.setFileMaxIndex(appDeclarationDocDtoList.size());
            }
            appDeclarationDocShowPageDto.setPageShowFileMap(map);
            appDeclarationDocShowPageDto.setPageShowFileDtos(pageShowFileDtos);
            appDeclarationDocShowPageDto.setPageShowFileHashMap(pageShowFileHashMap);
            return appDeclarationDocShowPageDto;
    }

    @Override
    public AppDeclarationMessageDto getAppDeclarationMessageDto(HttpServletRequest request, String type) {
        AppDeclarationMessageDto appDeclarationMessageDto = new AppDeclarationMessageDto();
        appDeclarationMessageDto.setAppType(type);
        appDeclarationMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(type)) {
            String preliminaryQuestionKindly = request.getParameter("preliminaryQuestionKindly");
            String preliminaryQuestionItem1 = request.getParameter("preliminaryQuestionItem1");
            String preliminaryQuestiontem2 = request.getParameter("preliminaryQuestiontem2");
            String effectiveDt = request.getParameter("effectiveDt");
            appDeclarationMessageDto.setPreliminaryQuestionKindly(preliminaryQuestionKindly);
            appDeclarationMessageDto.setPreliminaryQuestionItem1(preliminaryQuestionItem1);
            appDeclarationMessageDto.setPreliminaryQuestiontem2(preliminaryQuestiontem2);
            if(effectiveDt!=null){
                try {
                    Date parse = new SimpleDateFormat("dd/MM/yyyy").parse(effectiveDt);
                    appDeclarationMessageDto.setEffectiveDt(parse);
                } catch (ParseException e) {

                }
            }
        } else if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(type)) {
            String preliminaryQuestionKindly = request.getParameter("preliminaryQuestionKindly");
            appDeclarationMessageDto.setPreliminaryQuestionKindly(preliminaryQuestionKindly);
            String bankruptcyItem1 = request.getParameter("bankruptcyItem1");
            appDeclarationMessageDto.setBankruptcyItem1(bankruptcyItem1);
            String bankruptcyItem2 = request.getParameter("bankruptcyItem2");
            appDeclarationMessageDto.setBankruptcyItem2(bankruptcyItem2);
            String bankruptcyItem3 = request.getParameter("bankruptcyItem3");
            appDeclarationMessageDto.setBankruptcyItem3(bankruptcyItem3);
            String bankruptcyItem4 = request.getParameter("bankruptcyItem4");
            appDeclarationMessageDto.setBankruptcyItem4(bankruptcyItem4);
            String bankruptcyRemark = request.getParameter("bankruptcyRemark");
            appDeclarationMessageDto.setBankruptcyRemark(bankruptcyRemark);
            String competenciesItem1 = request.getParameter("competenciesItem1");
            appDeclarationMessageDto.setCompetenciesItem1(competenciesItem1);
            String competenciesItem2 = request.getParameter("competenciesItem2");
            appDeclarationMessageDto.setCompetenciesItem2(competenciesItem2);
            String competenciesItem3 = request.getParameter("competenciesItem3");
            appDeclarationMessageDto.setCompetenciesItem3(competenciesItem3);
            String competenciesRemark = request.getParameter("competenciesRemark");
            appDeclarationMessageDto.setCompetenciesRemark(competenciesRemark);
            String criminalRecordsItem1 = request.getParameter("criminalRecordsItem1");
            appDeclarationMessageDto.setCriminalRecordsItem1(criminalRecordsItem1);
            String criminalRecordsItem2 = request.getParameter("criminalRecordsItem2");
            appDeclarationMessageDto.setCriminalRecordsItem2(criminalRecordsItem2);
            String criminalRecordsItem3 = request.getParameter("criminalRecordsItem3");
            appDeclarationMessageDto.setCriminalRecordsItem3(criminalRecordsItem3);
            String criminalRecordsItem4 = request.getParameter("criminalRecordsItem4");
            appDeclarationMessageDto.setCriminalRecordsItem4(criminalRecordsItem4);
            String criminalRecordsRemark = request.getParameter("criminalRecordsRemark");
            appDeclarationMessageDto.setCriminalRecordsRemark(criminalRecordsRemark);
            String generalAccuracyItem1 = request.getParameter("generalAccuracyItem1");
            appDeclarationMessageDto.setGeneralAccuracyItem1(generalAccuracyItem1);

        } else if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(type)) {
            // Preliminary Question
            String preliminaryQuestionKindly = request.getParameter("preliminaryQuestionKindly");
            appDeclarationMessageDto.setPreliminaryQuestionKindly(preliminaryQuestionKindly);
            // Declaration on Bankruptcy
            appDeclarationMessageDto.setBankruptcyItem1(ParamUtil.getString(request, "bankruptcyItem1"));
            appDeclarationMessageDto.setBankruptcyItem2(ParamUtil.getString(request, "bankruptcyItem2"));
            appDeclarationMessageDto.setBankruptcyItem3(ParamUtil.getString(request, "bankruptcyItem3"));
            appDeclarationMessageDto.setBankruptcyItem4(ParamUtil.getString(request, "bankruptcyItem4"));
            appDeclarationMessageDto.setBankruptcyRemark(ParamUtil.getString(request, "bankruptcyRemark"));
            // Declaration on Competencies
            appDeclarationMessageDto.setCompetenciesItem1(ParamUtil.getString(request, "competenciesItem1"));
            appDeclarationMessageDto.setCompetenciesItem2(ParamUtil.getString(request, "competenciesItem2"));
            appDeclarationMessageDto.setCompetenciesItem3(ParamUtil.getString(request, "competenciesItem3"));
            appDeclarationMessageDto.setCompetenciesRemark(ParamUtil.getString(request, "competenciesRemark"));
            // Declaration on Criminal Records and Past Suspension/ Revocation under PHMCA/HCSA
            appDeclarationMessageDto.setCriminalRecordsItem1(ParamUtil.getString(request, "criminalRecordsItem1"));
            appDeclarationMessageDto.setCriminalRecordsItem2(ParamUtil.getString(request, "criminalRecordsItem2"));
            appDeclarationMessageDto.setCriminalRecordsItem3(ParamUtil.getString(request, "criminalRecordsItem3"));
            appDeclarationMessageDto.setCriminalRecordsItem4(ParamUtil.getString(request, "criminalRecordsItem4"));
            appDeclarationMessageDto.setCriminalRecordsRemark(ParamUtil.getString(request, "criminalRecordsRemark"));
            // General Accuracy Declaration
            appDeclarationMessageDto.setGeneralAccuracyItem1(ParamUtil.getString(request, "generalAccuracyItem1"));
        }
        appDeclarationMessageDto.setAppType(type);
        appDeclarationMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        return appDeclarationMessageDto;
    }
    @Deprecated
    @Override
    public List<AppDeclarationDocDto> getAppDeclarationDocDto(HttpServletRequest request) {
        Map<String, File> fileMap = (Map<String, File>)request.getSession().getAttribute("seesion_files_map_ajax_feselectedDeclFile");
        List<PageShowFileDto> pageShowFileDtos =new ArrayList<>(5);
        List<AppDeclarationDocDto> appDeclarationDocDtoList=new ArrayList<>(12);
        List<File> files=new ArrayList<>(5);
        if(fileMap!=null&&!fileMap.isEmpty()){
            fileMap.forEach((k,v)->{
                if(v!=null){
                    files.add(v);
                    SingeFileUtil singeFileUtil=SingeFileUtil.getInstance();
                    String fileMd5 = singeFileUtil.getFileMd5(v);
                    PageShowFileDto pageShowFileDto =new PageShowFileDto();
                    pageShowFileDto.setFileName(v.getName());
                    String e = k.substring(k.lastIndexOf('e') + 1);
                    pageShowFileDto.setIndex(e);
                    pageShowFileDto.setFileMapId("selectedFileDiv"+e);
                    Long l = v.length() / 1024;
                    pageShowFileDto.setSize(Integer.valueOf(l.toString()));
                    pageShowFileDto.setMd5Code(fileMd5);
                    pageShowFileDtos.add(pageShowFileDto);
                    AppDeclarationDocDto appDeclarationDocDto=new AppDeclarationDocDto();
                    appDeclarationDocDto.setDocName(v.getName());
                    appDeclarationDocDto.setDocSize(Integer.valueOf(l.toString()));
                    appDeclarationDocDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    appDeclarationDocDto.setMd5Code(fileMd5);
                    appDeclarationDocDto.setVersion(Integer.valueOf(1));
                    appDeclarationDocDto.setSeqNum(Integer.valueOf(e));
                    appDeclarationDocDtoList.add(appDeclarationDocDto);
                }else {

                }
            });
        }
        List<String> list = comFileRepoClient.saveFileRepo(files);
        if(list!=null){
            ListIterator<String> iterator = list.listIterator();
            for(int j=0;j< appDeclarationDocDtoList.size();j++){
                String fileRepoId = appDeclarationDocDtoList.get(j).getFileRepoId();
                if(fileRepoId==null){
                    if(iterator.hasNext()){
                        String next = iterator.next();
                        pageShowFileDtos.get(j).setFileUploadUrl(next);
                        appDeclarationDocDtoList.get(j).setFileRepoId(next);
                        iterator.remove();
                    }
                }
            }
        }
        Collections.sort(pageShowFileDtos,(s1,s2)->s1.getFileMapId().compareTo(s2.getFileMapId()));
        request.getSession().setAttribute("pageShowFileDtos", pageShowFileDtos);
        return appDeclarationDocDtoList;
    }

    @Override
    public void validateFile(PageShowFileDto pageShowFileDto, Map<String, String> map, int i) {
        int configFileSize = systemParamConfig.getUploadFileLimit();
        String configFileType = FileUtils.getStringFromSystemConfigString(systemParamConfig.getUploadFileType());
        List<String> fileTypes = Arrays.asList(configFileType.split(","));
        if(pageShowFileDto.getSize()/1024>configFileSize){
            map.put("file"+i, MessageUtil.replaceMessage("GENERAL_ERR0019", String.valueOf(configFileSize),"sizeMax"));
        }
        String substring = pageShowFileDto.getFileName().substring(pageShowFileDto.getFileName().lastIndexOf('.') + 1);
        if(!fileTypes.contains(substring.toUpperCase())){
            map.put("file"+i,MessageUtil.replaceMessage("GENERAL_ERR0018", configFileType,"fileType"));
        }
        if(pageShowFileDto.getFileName().length()>100){
            map.put("file"+i,MessageUtil.getMessageDesc("GENERAL_ERR0022"));
        }
    }
    @Override
    public String getFileAppendId(String appType) {
        StringBuilder s = new StringBuilder("selected");
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)) {
            s.append("New");
        }else if (ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(appType)){
            s.append("Cess");
        }else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
            s.append("RFC");
        }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
            s.append("RENEW");
        }
        s.append("File");
        return s.toString();
    }

    @Override
    public void initDeclarationFiles(List<AppDeclarationDocDto> appDeclarationDocDtos, String appType, HttpServletRequest request) {
        if (IaisCommonUtils.isEmpty(appDeclarationDocDtos)) {
            return;
        }
        String fileAppendId = getFileAppendId(appType);
        AppDeclarationDocShowPageDto dto = (AppDeclarationDocShowPageDto) request.getSession().getAttribute(fileAppendId + "DocShowPageDto");
        if (Objects.nonNull(dto)) {
            return;
        }
        List<PageShowFileDto> pageShowFileDtos = IaisCommonUtils.genNewArrayList();
        HashMap<String,File> map= IaisCommonUtils.genNewHashMap();
        Map<String, PageShowFileDto> pageShowFileHashMap = IaisCommonUtils.genNewHashMap();
        for (int i = 0, len = appDeclarationDocDtos.size(); i < len; i++) {
            AppDeclarationDocDto viewDoc = appDeclarationDocDtos.get(i);
            String index = String.valueOf(Optional.ofNullable(viewDoc.getSeqNum()).orElseGet(() -> 0));
            PageShowFileDto pageShowFileDto = new PageShowFileDto();
            pageShowFileDto.setFileMapId(fileAppendId + "Div" + index);
            pageShowFileDto.setIndex(index);
            pageShowFileDto.setFileName(viewDoc.getDocName());
            pageShowFileDto.setSize(viewDoc.getDocSize());
            pageShowFileDto.setMd5Code(viewDoc.getMd5Code());
            pageShowFileDto.setFileUploadUrl(viewDoc.getFileRepoId());
            pageShowFileDto.setVersion(Optional.ofNullable(viewDoc.getVersion()).orElseGet(() -> 1));
            pageShowFileDtos.add(pageShowFileDto);
            map.put(fileAppendId + index, null);
            pageShowFileHashMap.put(fileAppendId + index, pageShowFileDto);
        }
        // put page entity to sesstion
        dto = new AppDeclarationDocShowPageDto();
        dto.setFileMaxIndex(appDeclarationDocDtos.size());
        dto.setPageShowFileDtos(pageShowFileDtos);
        dto.setPageShowFileHashMap(pageShowFileHashMap);
        request.getSession().setAttribute(fileAppendId + "DocShowPageDto", dto);
        request.getSession().setAttribute(HcsaFileAjaxController.SEESION_FILES_MAP_AJAX + fileAppendId, map);
    }

    @Override
    public boolean validateDeclarationDoc(Map<String, String> errorMap, String fileAppendId, boolean isMandatory, HttpServletRequest request) {
        boolean isValid = true;
        Map<String, File> fileMap = (Map<String, File>) ParamUtil.getSessionAttr(request,
                HcsaFileAjaxController.SEESION_FILES_MAP_AJAX + fileAppendId);
        if (isMandatory && (fileMap == null || fileMap.isEmpty())) {
            errorMap.put(fileAppendId + "Error", MessageUtil.replaceMessage("GENERAL_ERR0006", "this", "field"));
            isValid = false;
        }
        return isValid;
    }

    @Override
    public LicenceDto getLicenceDtoById(String licenceId) {
        return licenceClient.getLicDtoById(licenceId).getEntity();
    }

    @Override
    public List<OrgGiroAccountInfoDto> getOrgGiroAccDtosByLicenseeId(String licenseeId) {
        return organizationLienceseeClient.getGiroAccByLicenseeId(licenseeId).getEntity();
    }

    @Override
    public SubLicenseeDto getSubLicenseeByLicenseeId(String licenseeId, String uenNo) {
        LicenseeDto licenseeDto = organizationLienceseeClient.getLicenseeById(licenseeId).getEntity();
        Map<String, String> fieldMap = IaisCommonUtils.genNewHashMap();
        fieldMap.put("name", "licenseeName");
        fieldMap.put("organizationId", "orgId");
        fieldMap.put("officeTelNo", "telephoneNo");
        fieldMap.put("emilAddr", "emailAddr");
        SubLicenseeDto subLicenseeDto = MiscUtil.transferEntityDto(licenseeDto, SubLicenseeDto.class, fieldMap);
        if (subLicenseeDto == null) {
            subLicenseeDto = new SubLicenseeDto();
        }
        subLicenseeDto.setUenNo(uenNo);
        if (OrganizationConstants.LICENSEE_TYPE_CORPPASS.equals(subLicenseeDto.getLicenseeType())) {
            subLicenseeDto.setLicenseeType(OrganizationConstants.LICENSEE_SUB_TYPE_COMPANY);
        } else if (OrganizationConstants.LICENSEE_SUB_TYPE_SOLO.equals(subLicenseeDto.getLicenseeType())) {
            subLicenseeDto.setAssignSelect(IaisEGPConstant.ASSIGN_SELECT_ADD_NEW);
            LicenseeIndividualDto licenseeIndividualDto = Optional.ofNullable(licenseeDto)
                    .map(LicenseeDto::getLicenseeIndividualDto)
                    .orElseGet(LicenseeIndividualDto::new);
            subLicenseeDto.setIdType(licenseeIndividualDto.getIdType());
            subLicenseeDto.setIdNumber(licenseeIndividualDto.getIdNo());
            if (!StringUtil.isEmpty(licenseeDto.getMobileNo())) {
                subLicenseeDto.setTelephoneNo(licenseeDto.getMobileNo());
            }
            subLicenseeDto.setLicenseeType(OrganizationConstants.LICENSEE_SUB_TYPE_SOLO);
        }
        return subLicenseeDto;
    }

    @Override
    public boolean validateSubLicenseeDto(Map<String, String> errorMap, SubLicenseeDto subLicenseeDto, HttpServletRequest request) {
        if (subLicenseeDto == null) {
            if (errorMap != null) {
                errorMap.put("licenseeType", "Invalid Data");
            }
            return false;
        }
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        String propertyName = "save";
        if (OrganizationConstants.LICENSEE_SUB_TYPE_INDIVIDUAL.equals(subLicenseeDto.getLicenseeType())){
            propertyName = "soloSave";
        }
        ValidationResult result = WebValidationHelper.validateProperty(subLicenseeDto, propertyName);
        if (result != null) {
            map = result.retrieveAll();
        }

        // add log
        if (!map.isEmpty()) {
            log.info(StringUtil.changeForLog("Error Message For the Sub Licensee : " + map + " - " +
                    JsonUtil.parseToJson(subLicenseeDto)));
        }
        if (OrganizationConstants.LICENSEE_SUB_TYPE_COMPANY.equals(subLicenseeDto.getLicenseeType())) {
            if (!map.isEmpty() && errorMap != null) {
                errorMap.put("licenseeType", "Invalid Licensee Type");
            }
            return map.isEmpty();
        } else {
            if (errorMap != null) {
                errorMap.putAll(map);
            }
            return map.isEmpty();
        }
    }

    @Override
    public Map<String, String> validateSectionLeaders(List<AppSvcPersonnelDto> appSvcSectionLeaderList) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if (appSvcSectionLeaderList == null || appSvcSectionLeaderList.isEmpty()) {
            return errorMap;
        }
        HttpServletRequest request = MiscUtil.getCurrentRequest();
        if (request != null) {
            request.setAttribute(ClinicalLaboratoryDelegator.SECTION_LEADER_LIST, appSvcSectionLeaderList);
        }
        for (int i = 0, len = appSvcSectionLeaderList.size(); i < len; i++) {
            ValidationResult result = WebValidationHelper.validateProperty(appSvcSectionLeaderList.get(i),
                    ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER);
            if (result != null) {
                int index = i;
                Map<String, String> map = result.retrieveAll();
                map.forEach((k, v) -> errorMap.put(k + index, v));
            }
        }
        return errorMap;
    }

    @Override
    public void doValidateDisciplineAllocation(Map<String, String> map, List<AppSvcDisciplineAllocationDto> daList,
            AppSvcRelatedInfoDto currentSvcDto) {
        if (daList == null || daList.isEmpty()) {
            return;
        }
        Map<String, String> cgoMap = new HashMap<>();
        Map<String, String> slMap = new HashMap<>();
        for (int i = 0; i < daList.size(); i++) {
            String idNo = daList.get(i).getIdNo();
            if (StringUtil.isEmpty(idNo)) {
                map.put("disciplineAllocation" + i,
                        MessageUtil.replaceMessage("GENERAL_ERR0006", "Clinical Governance Officers", "field"));
            } else {
                cgoMap.put(idNo, idNo);
            }
            String indexNo = daList.get(i).getSlIndex();
            if (StringUtil.isEmpty(indexNo)) {
                map.put("disciplineAllocationSl" + i, MessageUtil.replaceMessage("GENERAL_ERR0006", "Section Leader", "field"));
            } else {
                slMap.put(indexNo, indexNo);
            }
        }
        if (map.isEmpty()) {
            String error = MessageUtil.getMessageDesc("NEW_ERR0011");
            List<AppSvcPrincipalOfficersDto> appSvcCgoList = (List<AppSvcPrincipalOfficersDto>) CopyUtil.copyMutableObjectList(
                    currentSvcDto.getAppSvcCgoDtoList());
            List<AppSvcPrincipalOfficersDto> appSvcCgoDtos = IaisCommonUtils.genNewArrayList();
            if (appSvcCgoList != null) {
                if (daList.size() < appSvcCgoList.size()) {
                    return;
                }
                if (appSvcCgoList.size() <= daList.size()) {
                    cgoMap.forEach((k, v) -> {
                        for (AppSvcPrincipalOfficersDto appSvcCgoDto : appSvcCgoList) {
                            String idNo = appSvcCgoDto.getIdNo();
                            if (k.equals(idNo)) {
                                appSvcCgoDtos.add(appSvcCgoDto);
                            }
                        }
                    });
                }
                appSvcCgoList.removeAll(appSvcCgoDtos);
                StringBuilder stringBuilder = new StringBuilder();
                for (AppSvcPrincipalOfficersDto appSvcCgoDto : appSvcCgoList) {
                    stringBuilder.append(appSvcCgoDto.getName()).append(',');
                }
                if (!StringUtil.isEmpty(stringBuilder.toString())) {
                    String string = stringBuilder.toString();
                    String substring = string.substring(0, string.lastIndexOf(','));

                    if (substring.contains(",")) {
                        error = error.replaceFirst("is", "are");
                    }
                    String replace = error.replace("{CGO Name}", substring);
                    map.put("CGO", replace);
                }
            }
            List<AppSvcPersonnelDto> sectionLeaderDtoList = (List<AppSvcPersonnelDto>) CopyUtil.copyMutableObjectList(
                    currentSvcDto.getAppSvcSectionLeaderList());
            List<AppSvcPersonnelDto> sectionLeaderDtos = IaisCommonUtils.genNewArrayList();
            if (sectionLeaderDtoList != null) {
                if (daList.size() < sectionLeaderDtoList.size()) {
                    return;
                }
                if (daList.size() >= sectionLeaderDtoList.size()) {
                    slMap.forEach((k, v) -> {
                        for (AppSvcPersonnelDto sectionLeaderDto : sectionLeaderDtoList) {
                            String indexNo = sectionLeaderDto.getIndexNo();
                            if (k.equals(indexNo)) {
                                sectionLeaderDtos.add(sectionLeaderDto);
                            }
                        }
                    });
                }
                sectionLeaderDtoList.removeAll(sectionLeaderDtos);
                StringBuilder stringBuilder = new StringBuilder();
                for (AppSvcPersonnelDto sectionLeaderDto : sectionLeaderDtoList) {
                    stringBuilder.append(sectionLeaderDto.getName()).append(',');
                }
                if (!StringUtil.isEmpty(stringBuilder.toString())) {
                    String string = stringBuilder.toString();
                    String substring = string.substring(0, string.lastIndexOf(','));
                    if (substring.contains(",")) {
                        error = error.replaceFirst("is", "are");
                    }
                    String replace = error.replace("{CGO Name}", substring);
                    map.put("SL", replace);
                }
            }
        }
    }

    @Override
    public List<AppSvcVehicleDto> getActiveVehicles(List<String> appIds) {
        List<AppSvcVehicleDto> vehicles = applicationFeClient.getActiveVehicles().getEntity();
        if (vehicles == null || vehicles.isEmpty()) {
            return vehicles;
        }
        if (appIds == null) {
            appIds = IaisCommonUtils.genNewArrayList(0);
        }
        final List<String> finalAppIds = appIds;
        List<LicAppCorrelationDto> licAppCorrList = licenceClient.getInactiveLicAppCorrelations().getEntity();
        List<AppSvcVehicleDto> result = IaisCommonUtils.genNewArrayList(vehicles.size());
        vehicles.forEach(vehicle -> {
            if (!isIn(vehicle.getAppId(), licAppCorrList) && !finalAppIds.contains(vehicle.getAppId())) {
                result.add(vehicle);
            }
        });
        return result;
    }


    private boolean isIn(String appId, List<LicAppCorrelationDto> licAppCorrList) {
        if (licAppCorrList == null || licAppCorrList.isEmpty()) {
            return false;
        }
        if (StringUtil.isEmpty(appId)) {
            return true;
        }
        return licAppCorrList.stream().anyMatch(corr -> appId.equals(corr.getApplicationId()));
    }

    @Override
    public List<AppDeclarationDocDto> getDeclarationFiles(String appType, HttpServletRequest request) {
        return getDeclarationFiles(appType, request, false);
    }

    @Override
    public List<AppDeclarationDocDto> getDeclarationFiles(String appType, HttpServletRequest request, boolean forPrint) {
        String fileAppendId = getFileAppendId(appType);
        Map<String, File> fileMap = (Map<String, File>) ParamUtil.getSessionAttr(request,
                HcsaFileAjaxController.SEESION_FILES_MAP_AJAX + fileAppendId);
        if (IaisCommonUtils.isEmpty(fileMap)) {
            request.getSession().setAttribute(fileAppendId + "DocShowPageDto", null);
            return null;
        }
        AppDeclarationDocShowPageDto dto = (AppDeclarationDocShowPageDto) request.getSession().getAttribute(
                fileAppendId + "DocShowPageDto");
        if (Objects.isNull(dto)) {
            dto = new AppDeclarationDocShowPageDto();
            dto.setPageShowFileHashMap(IaisCommonUtils.genNewHashMap());
        }
        Map<String, PageShowFileDto> pageShowFileHashMap = dto.getPageShowFileHashMap();
        List<PageShowFileDto> pageDtos = IaisCommonUtils.genNewArrayList();
        List<File> files = IaisCommonUtils.genNewArrayList();
        List<AppDeclarationDocDto> docDtos = IaisCommonUtils.genNewArrayList();
        SingeFileUtil singeFileUtil = SingeFileUtil.getInstance();
        fileMap.forEach((s, file) -> {
            // the current uploaed files
            String index = s.substring(fileAppendId.length());
            if (file != null) {
                long length = file.length();
                if (length > 0) {
                    Long size = length / 1024;
                    files.add(file);
                    AppDeclarationDocDto docDto = new AppDeclarationDocDto();
                    docDto.setDocName(file.getName());
                    String fileMd5 = singeFileUtil.getFileMd5(file);
                    docDto.setMd5Code(singeFileUtil.getFileMd5(file));
                    docDto.setDocSize(Integer.valueOf(size.toString()));
                    docDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    docDto.setSeqNum(Integer.valueOf(index));
                    Optional<Integer> versions = pageShowFileHashMap.entrySet()
                            .stream()
                            .filter(i -> s.equals(i.getKey()))
                            .map(i -> i.getValue().getVersion())
                            .findAny();
                    docDto.setVersion(versions.orElse(0) + 1);
                    docDtos.add(docDto);
                    PageShowFileDto pageShowFileDto = new PageShowFileDto();
                    pageShowFileDto.setIndex(index);
                    pageShowFileDto.setFileName(file.getName());
                    pageShowFileDto.setFileMapId(fileAppendId + "Div" + index);
                    pageShowFileDto.setSize(Integer.valueOf(size.toString()));
                    pageShowFileDto.setMd5Code(fileMd5);
                    pageDtos.add(pageShowFileDto);
                }
            } else {
                // the previous / old files
                PageShowFileDto pageShowFileDto = pageShowFileHashMap.get(s);
                if (Objects.nonNull(pageShowFileDto)) {
                    AppDeclarationDocDto docDto = new AppDeclarationDocDto();
                    docDto.setDocName(pageShowFileDto.getFileName());
                    docDto.setMd5Code(pageShowFileDto.getMd5Code());
                    docDto.setDocSize(pageShowFileDto.getSize());
                    docDto.setFileRepoId(pageShowFileDto.getFileUploadUrl());
                    docDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    docDto.setSeqNum(Integer.valueOf(index));
                    docDto.setVersion(Optional.ofNullable(pageShowFileDto.getVersion()).orElseGet(() -> 1));
                    docDtos.add(docDto);
                    pageDtos.add(pageShowFileDto);
                }
            }
        });
        if (!forPrint) {
            dto.setPageShowFileDtos(pageDtos);
            request.getSession().setAttribute(fileAppendId + "DocShowPageDto", dto);
            // dto.setFileMaxIndex(pageDtos.size());
            List<String> list = comFileRepoClient.saveFileRepo(files);
            if (list != null) {
                ListIterator<String> iterator = list.listIterator();
                for (int j = 0; j < docDtos.size(); j++) {
                    String fileRepoId = docDtos.get(j).getFileRepoId();
                    if (fileRepoId == null) {
                        if (iterator.hasNext()) {
                            String next = iterator.next();
                            pageDtos.get(j).setFileUploadUrl(next);
                            docDtos.get(j).setFileRepoId(next);
                            iterator.remove();
                        }
                    }
                }
            }
        }
        return docDtos;
    }

    @Override
    public void sendEmailForGiroFailAndSMSAndMessage( ApplicationGroupDto applicationGroupDto) {
        try{
            log.info(StringUtil.changeForLog("---------applicationGroupDto appgroupno : " + applicationGroupDto.getGroupNo() +" sendEmailForGiroFailAndSMSAndMessage start ----------"));
            AppSubmissionDto appSubmissionDto =appSubmissionService.getAppSubmissionDto(applicationGroupDto.getGroupNo()+"-01");
            List<ApplicationDto> applicationDtos = appSubmissionDto.getApplicationDtos();
            if(IaisCommonUtils.isEmpty(applicationDtos)){
                applicationDtos = applicationFeClient.getApplicationsByGroupNo(appSubmissionDto.getAppGrpNo()).getEntity();
                appSubmissionDto.setApplicationDtos(applicationDtos);
            }
            ApplicationDto applicationDto =  applicationDtos.get(0);
            String applicationType =  MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType());
            int index = 0;
            StringBuilder stringBuilderAPPNum = new StringBuilder();
            for(ApplicationDto applicationDtoApp : appSubmissionDto.getApplicationDtos()){
                if(index == 0){
                    stringBuilderAPPNum.append(applicationDtoApp.getApplicationNo());
                }else {
                    stringBuilderAPPNum.append(" and ");
                    stringBuilderAPPNum.append(applicationDtoApp.getApplicationNo());
                }
                index++;
            }
            String applicationNumber = stringBuilderAPPNum.toString();
            Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
            subMap.put("ApplicationType", applicationType);
            subMap.put("ApplicationNumber", applicationNumber);
            String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_006_EMAIL,subMap);
            String smsSubject = getEmailSubject(MsgTemplateConstants. MSG_TEMPLATE_EN_FEP_006_SMS ,subMap);
            String messageSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_006_MSG,subMap);
            log.debug(StringUtil.changeForLog("emailSubject : " + emailSubject));
            log.debug(StringUtil.changeForLog("smsSubject : " + smsSubject));
            log.debug(StringUtil.changeForLog("messageSubject : " + messageSubject));
            Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();

            templateContent.put("ApplicantName", getApplicantName(applicationGroupDto));
            templateContent.put("ApplicationType",  applicationType);
            templateContent.put("ApplicationNumber", applicationNumber);
            templateContent.put("ApplicationDate", Formatter.formatDateTime(new Date()));
            long time = new Date().getTime() + 1000 * 60 * 60 * 24 *7L;
            templateContent.put("monthOfGiro",Formatter.formatDateTime(new Date(time),Formatter.DATE));
            templateContent.put("email", systemParamConfig.getSystemAddressOne());
            String syName = "<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"<br/>"+AppConsts.MOH_AGENCY_NAME+"</b>";
            templateContent.put("MOH_AGENCY_NAME",syName);
            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_006_EMAIL);
            emailParam.setTemplateContent(templateContent);
            emailParam.setSubject(emailSubject);
            emailParam.setQueryCode(applicationDto.getApplicationNo());
            emailParam.setReqRefNum(applicationDto.getApplicationNo());
            emailParam.setRefId(applicationDto.getApplicationNo());
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
            notificationHelper.sendNotification(emailParam);

            EmailParam smsParam = new EmailParam();
            smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_006_SMS);
            smsParam.setSubject(smsSubject);
            smsParam.setQueryCode(applicationDto.getApplicationNo());
            smsParam.setReqRefNum(applicationDto.getApplicationNo());
            smsParam.setRefId(applicationDto.getApplicationNo());
            smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
            notificationHelper.sendNotification(smsParam);

            EmailParam msgParam = new EmailParam();
            msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_FEP_006_MSG);
            msgParam.setTemplateContent(templateContent);
            msgParam.setSubject(messageSubject);
            msgParam.setQueryCode(applicationDto.getApplicationNo());
            msgParam.setReqRefNum(applicationDto.getApplicationNo());
            msgParam.setRefId(applicationDto.getApplicationNo());
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList){
                if( !svcCodeList.contains(appSvcRelatedInfoDto.getServiceCode())){
                    svcCodeList.add(appSvcRelatedInfoDto.getServiceCode());
                }
            }
            msgParam.setSvcCodeList(svcCodeList);
            msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            notificationHelper.sendNotification(msgParam);
            log.info("end send email sms and msg");
        }catch (Exception e){
            log.error(e.getMessage(),e);
            log.info("send app sumbit email fail");
        }
    }

    private String getApplicantName(ApplicationGroupDto applicationGroupDto){
        String applicantId = applicationGroupDto.getSubmitBy();
        if(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(applicationGroupDto.getAppType()) ||
                ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(applicationGroupDto.getAppType())) {
            String licenseeId = applicationGroupDto.getLicenseeId();
            LicenseeDto licenseeDto = licenseeService.getLicenseeDtoById(licenseeId);
            return licenseeDto.getName() == null ? "" :licenseeDto.getName();
        }else{
            OrgUserDto orgUserDto = organizationLienceseeClient.retrieveOneOrgUserAccount(applicantId).getEntity();
           return orgUserDto.getDisplayName() == null ? "" : orgUserDto.getDisplayName();
        }
    }

    private String getEmailSubject(String templateId, Map<String, Object> subMap){
        String subject = "-";
        if(!StringUtil.isEmpty(templateId)){
            MsgTemplateDto emailTemplateDto =appSubmissionService.getMsgTemplateById(templateId);
            if(emailTemplateDto != null){
                try {
                    if(!IaisCommonUtils.isEmpty(subMap)){
                        subject = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getTemplateName(),subMap);
                    }else{
                        subject = emailTemplateDto.getTemplateName();
                    }
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }
        return subject;
    }

    @Override
    public AppSubmissionDto submitRequestInformation(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto, Process process) {
        appSubmissionRequestInformationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        AppSubmissionDto appSubmissionDto = appSubmissionRequestInformationDto.getAppSubmissionDto();
        //asynchronous save the other data.
        informationEventBus(appSubmissionRequestInformationDto, process);
        return appSubmissionDto;
    }

    @Override
    public AppSubmissionDto submitRequestRfcRenewInformation(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto, Process process) {
        appSubmissionRequestInformationDto.setEventRefNo(UUID.randomUUID().toString());
        SubmitResp submitResp = eventBusHelper.submitAsyncRequest(appSubmissionRequestInformationDto,
                generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_APPSUBMIT, EventBusConsts.OPERATION_REQUEST_RFC_RENEW_INFORMATION_SUBMIT,
                appSubmissionRequestInformationDto.getEventRefNo(), process);
        AppSubmissionDto appSubmissionDto = appSubmissionRequestInformationDto.getAppSubmissionDto();
        return appSubmissionDto;
    }

    @Override
    public AppSubmissionDto submitPremisesListRequestInformation(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto, Process process) {
        appSubmissionRequestInformationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        AppSubmissionDto appSubmissionDto = appSubmissionRequestInformationDto.getAppSubmissionDto();
        //asynchronous save the other data.
        premisesListInformationEventBus(appSubmissionRequestInformationDto, process);
        return appSubmissionDto;
    }

    @Override
    public List<ApplicationDto> listApplicationByGroupId(String groupId) {
        return applicationFeClient.listApplicationByGroupId(groupId).getEntity();
    }

    @Override
    public AppSubmissionDto doSaveDraft(AppSubmissionDto appSubmissionDto) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return applicationFeClient.saveDraft(appSubmissionDto).getEntity();
    }

    @Override
    public String getDraftNo(String appType) {
        return systemAdminClient.draftNumber(appType).getEntity();
    }

    @Override
    public String getGroupNo(String appType) {

        return systemAdminClient.applicationNumber(appType).getEntity();
    }

    @Override
    public FeeDto getNewAppAmount(AppSubmissionDto appSubmissionDto,boolean isCharity) {
        log.debug(StringUtil.changeForLog("the getNewAppAmount start ...."));
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        List<String> premisesTypes = IaisCommonUtils.genNewArrayList();
        List<LicenceFeeDto> licenceFeeQuaryDtos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
            log.debug("appGrpPremisesDtos size {}",appGrpPremisesDtos.size());
            for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                String premType = appGrpPremisesDto.getPremisesType();
                if(!StringUtil.isEmpty(premType)){
                    premisesTypes.add(premType);
                }
            }
            boolean hadEas = false;
            boolean hadMts = false;
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                String serviceCode = appSvcRelatedInfoDto.getServiceCode();
                if(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode)){
                    hadEas = true;
                }else if(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)){
                    hadMts = true;
                }
            }
            int easVehicleCount = getEasVehicleCount(appSvcRelatedInfoDtos);
            int mtsVehicleCount = getMtsVehicleCount(appSvcRelatedInfoDtos);
            log.debug("eas vehicle count is {}",easVehicleCount);
            log.debug("mts vehicle count is {}",mtsVehicleCount);
            List<HcsaFeeBundleItemDto> hcsaFeeBundleItemDtos = appConfigClient.getActiveBundleDtoList().getEntity();
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                LicenceFeeDto licenceFeeDto = new LicenceFeeDto();
                licenceFeeDto.setBundle(0);
                String serviceCode = appSvcRelatedInfoDto.getServiceCode();
                if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                    licenceFeeDto.setBaseService(serviceCode);
                }else if(ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(appSvcRelatedInfoDto.getServiceType())){
                    if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getBaseServiceId())){
                        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(appSvcRelatedInfoDto.getBaseServiceId());
                        if(hcsaServiceDto != null) {
                            licenceFeeDto.setBaseService(hcsaServiceDto.getSvcCode());
                        }else{
                            log.info(StringUtil.changeForLog("current svc"+serviceCode+"'s baseSvcInfo is empty"));
                        }
                    }else{
                        log.info("base svcId is empty");
                    }
                }
                licenceFeeDto.setServiceCode(serviceCode);
                licenceFeeDto.setServiceName(appSvcRelatedInfoDto.getServiceName());
                licenceFeeDto.setPremises(premisesTypes);
//                licenceFeeDto.setOnlyNewSpecified(onlySpecifiedSvc);
                if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo())){
                    licenceFeeDto.setOnlyNewSpecified(true);
                }else if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                    licenceFeeDto.setIncludeBase(true);
                }
                licenceFeeDto.setCharity(isCharity);
                //exiting offSite licence
                log.info(StringUtil.changeForLog("svcName:"+appSvcRelatedInfoDto.getServiceName()));
                Boolean existingOnSiteLic = licenceClient.existingOnSiteOrConveLic(appSvcRelatedInfoDto.getServiceName(),appSubmissionDto.getLicenseeId()).getEntity();
                log.info(StringUtil.changeForLog("existing onSite licncence:"+existingOnSiteLic));
                licenceFeeDto.setExistOnsite(existingOnSiteLic);
                //set bundle
                if(!IaisCommonUtils.isEmpty(hcsaFeeBundleItemDtos)){
                    log.debug(StringUtil.changeForLog("set bundle info ..."));
                    if(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode)){
                        if(hadEas && hadMts){
                            //judge vehicle count
                            if(easVehicleCount+mtsVehicleCount <= 10 ){
                                licenceFeeDto.setBundle(1);
                            }else {
                                licenceFeeDto.setBundle(2);
                            }
                        }else{
                            String hciCode = appGrpPremisesDtos.get(0).getHciCode();
                            setEasMtsBundleInfo(licenceFeeDto, hciCode, hcsaFeeBundleItemDtos, serviceCode, appSubmissionDto.getLicenseeId(), easVehicleCount,appSubmissionDto.getAppType());
                        }
                    } else if(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)){
                        if(hadEas && hadMts){
                            //new eas and mts
                            licenceFeeDto.setBundle(3);
                        }else{
                            String hciCode = appGrpPremisesDtos.get(0).getHciCode();
                            setEasMtsBundleInfo(licenceFeeDto, hciCode, hcsaFeeBundleItemDtos, serviceCode, appSubmissionDto.getLicenseeId(), mtsVehicleCount,appSubmissionDto.getAppType());
                        }
                    } else{
                        licenceFeeDto.setBundle(0);
                    }
                }else{
                    licenceFeeDto.setBundle(0);
                }
                licenceFeeQuaryDtos.add(licenceFeeDto);
            }
        }
        log.info(StringUtil.changeForLog("licenceFeeQuaryDtos:"+JsonUtil.parseToJson(licenceFeeQuaryDtos)));
        log.debug(StringUtil.changeForLog("the getNewAppAmount end ...."));
        return appConfigClient.newFee(licenceFeeQuaryDtos).getEntity();
    }

    @Override
    public FeeDto getGroupAmount(AppSubmissionDto appSubmissionDto,boolean isCharity) {
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getGroupAmount start ...."));
        log.info(StringUtil.changeForLog("current account is charity:"+isCharity));
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        List<LicenceFeeDto> linenceFeeQuaryDtos = IaisCommonUtils.genNewArrayList();
        List<String> premisessTypes =  IaisCommonUtils.genNewArrayList();

        for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
            premisessTypes.add(appGrpPremisesDto.getPremisesType());
        }
        List<String> baseServiceIds = IaisCommonUtils.genNewArrayList();
        List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtos = appConfigClient.serviceCorrelation().getEntity();
        boolean onlySpecifiedSvc = false;
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo())){
                onlySpecifiedSvc = true;
            }
        }
       if(onlySpecifiedSvc && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())){
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                LicenceFeeDto licenceFeeDto = new LicenceFeeDto(); licenceFeeDto.setBundle(0);
                if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                    licenceFeeDto.setIncludeBase(true);
                }
                HcsaServiceDto baseServiceDto = HcsaServiceCacheHelper.getServiceById(appSvcRelatedInfoDto.getBaseServiceId());
                licenceFeeDto.setBaseService(baseServiceDto.getSvcCode());
                licenceFeeDto.setServiceCode(appSvcRelatedInfoDto.getServiceCode());
                licenceFeeDto.setServiceName(appSvcRelatedInfoDto.getServiceName());
                licenceFeeDto.setPremises(premisessTypes);
                if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo())){
                    licenceFeeDto.setOnlyNewSpecified(true);
                }else{
                    licenceFeeDto.setIncludeBase(true);
                }
                licenceFeeDto.setCharity(isCharity);
                linenceFeeQuaryDtos.add(licenceFeeDto);
            }
        }else{
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                    baseServiceIds.add(appSvcRelatedInfoDto.getServiceId());
                }else if(ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(appSvcRelatedInfoDto.getServiceType())){
                    for(HcsaServiceCorrelationDto hcsaServiceCorrelationDto:hcsaServiceCorrelationDtos){
                        if(appSvcRelatedInfoDto.getServiceId().equals(hcsaServiceCorrelationDto.getSpecifiedSvcId())){
                            baseServiceIds.add(hcsaServiceCorrelationDto.getBaseSvcId());
                        }
                    }
                }
            }
        }
        log.info(StringUtil.changeForLog("base service size:"+baseServiceIds.size()));
        if(!onlySpecifiedSvc) {
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                LicenceFeeDto licenceFeeDto = new LicenceFeeDto(); licenceFeeDto.setBundle(0);
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(appSvcRelatedInfoDto.getServiceCode());
                if (hcsaServiceDto == null) {
                    log.info(StringUtil.changeForLog("hcsaServiceDto is empty "));
                    continue;
                }
                if (ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(hcsaServiceDto.getSvcType())) {
                    licenceFeeDto.setBaseService(hcsaServiceDto.getSvcCode());
                    licenceFeeDto.setIncludeBase(true);
                } else if (ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(hcsaServiceDto.getSvcType())) {
                    for (HcsaServiceCorrelationDto hcsaServiceCorrelationDto : hcsaServiceCorrelationDtos) {
                        if (hcsaServiceDto.getId().equals(hcsaServiceCorrelationDto.getSpecifiedSvcId())) {
                            String baseSvcId = hcsaServiceCorrelationDto.getBaseSvcId();
                            if (baseServiceIds.contains(baseSvcId)) {
                                log.info("can match base service ...");
                                HcsaServiceDto baseService = HcsaServiceCacheHelper.getServiceById(baseSvcId);
                                licenceFeeDto.setBaseService(baseService.getSvcCode());
                            }
                        }
                    }
                }
                licenceFeeDto.setServiceCode(hcsaServiceDto.getSvcCode());
                licenceFeeDto.setServiceName(hcsaServiceDto.getSvcName());
                licenceFeeDto.setPremises(premisessTypes);
                licenceFeeDto.setCharity(isCharity);
                if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
                    String licenceId = appSubmissionDto.getLicenceId();
                    Date licExpiryDate = appSubmissionDto.getLicExpiryDate();
                    licenceFeeDto.setExpiryDate(licExpiryDate);
                    licenceFeeDto.setLicenceId(licenceId);
                }
                linenceFeeQuaryDtos.add(licenceFeeDto);
            }
            log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl linenceFeeQuaryDtos.size() is -->:" + linenceFeeQuaryDtos.size()));
        }
        String appTYpe = appSubmissionDto.getAppType();
        FeeDto entity ;
        if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appTYpe)){
            entity = appConfigClient.renewFee(linenceFeeQuaryDtos).getEntity();
        }else{
            entity = appConfigClient.newFee(linenceFeeQuaryDtos).getEntity();
        }
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getGroupAmount end ...."));
        return  entity;
    }
    @Override
    public FeeDto getRenewalAmount(List<AppSubmissionDto> appSubmissionDtoList,boolean isCharity){
        List<LicenceFeeDto> linenceFeeQuaryDtos = IaisCommonUtils.genNewArrayList();
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getRenewalAmount start ...."));
        log.info(StringUtil.changeForLog("current account is charity:"+isCharity));
        String hciCodeEas = null;
        String hciCodeMts = null;
        boolean hadEas = false;
        boolean hadMts = false;
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtosAll=IaisCommonUtils.genNewArrayList();
        for(AppSubmissionDto appSubmissionDto : appSubmissionDtoList){
             appSvcRelatedInfoDtosAll.addAll(appSubmissionDto.getAppSvcRelatedInfoDtoList()) ;
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            String hciCode = appGrpPremisesDtos.get(0).getHciCode();
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSubmissionDto.getAppSvcRelatedInfoDtoList()){
                String serviceCode = appSvcRelatedInfoDto.getServiceCode();
                if(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode)){
                    hadEas = true;
                    hciCodeEas = hciCode;
                }else if(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)){
                    hadMts = true;
                    hciCodeMts = hciCode;
                }
            }

        }
        int easVehicleCount = getEasVehicleCount(appSvcRelatedInfoDtosAll);
        int mtsVehicleCount = getMtsVehicleCount(appSvcRelatedInfoDtosAll);
        for(AppSubmissionDto appSubmissionDto : appSubmissionDtoList){
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            List<String> premisessTypes =  IaisCommonUtils.genNewArrayList();
            if(isCharity){
                List<AppGrpPremisesDto> result = removeDuplicates(appGrpPremisesDtos);
                for(AppGrpPremisesDto appGrpPremisesDto:result){
                    premisessTypes.add(appGrpPremisesDto.getPremisesType());
                }
            }else{
                for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                    premisessTypes.add(appGrpPremisesDto.getPremisesType());
                }
            }
            List<String> baseServiceIds = IaisCommonUtils.genNewArrayList();
            List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtos = appConfigClient.serviceCorrelation().getEntity();
            boolean onlySpecifiedSvc = false;
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo())){
                    onlySpecifiedSvc = true;
                }
            }


            log.debug("eas vehicle count is {}",easVehicleCount);
            log.debug("mts vehicle count is {}",mtsVehicleCount);
            List<HcsaFeeBundleItemDto> hcsaFeeBundleItemDtos = appConfigClient.getActiveBundleDtoList().getEntity();

            if(onlySpecifiedSvc && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    LicenceFeeDto licenceFeeDto = new LicenceFeeDto(); licenceFeeDto.setBundle(0);
                    if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                        licenceFeeDto.setIncludeBase(true);
                    }
                    licenceFeeDto.setHciCode(appGrpPremisesDtos.get(0).getHciCode());
                    HcsaServiceDto baseServiceDto = HcsaServiceCacheHelper.getServiceById(appSvcRelatedInfoDto.getBaseServiceId());
                    licenceFeeDto.setBaseService(baseServiceDto.getSvcCode());
                    licenceFeeDto.setServiceCode(appSvcRelatedInfoDto.getServiceCode());
                    licenceFeeDto.setServiceName(appSvcRelatedInfoDto.getServiceName());
                    licenceFeeDto.setPremises(premisessTypes);
                    if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo())){
                        licenceFeeDto.setOnlyNewSpecified(true);
                    }else{
                        licenceFeeDto.setIncludeBase(true);
                    }
                    licenceFeeDto.setCharity(isCharity);
                    //set bundle
                    if(!IaisCommonUtils.isEmpty(hcsaFeeBundleItemDtos)){
                        String serviceCode=baseServiceDto.getSvcCode();
                        log.debug(StringUtil.changeForLog("set bundle info ..."));
                        if(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode)){
                            if(easVehicleCount <= 10 ){
                                licenceFeeDto.setBundle(1);
                            }else {
                                licenceFeeDto.setBundle(2);
                            }
                            if(hadEas && hadMts){
                                if(hciCodeEas!=null&&hciCodeMts!=null){
                                    if(hciCodeEas.equals(hciCodeMts)){
                                        if(easVehicleCount+mtsVehicleCount <= 10 ){
                                            licenceFeeDto.setBundle(1);
                                        }else {
                                            licenceFeeDto.setBundle(2);
                                        }
                                    }
                                }
                            }else{
                                String hciCode = appGrpPremisesDtos.get(0).getHciCode();
                                setEasMtsBundleInfo(licenceFeeDto, hciCode, hcsaFeeBundleItemDtos, serviceCode, appSubmissionDto.getLicenseeId(), easVehicleCount,appSubmissionDto.getAppType());
                            }
                        } else if(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)){
                            if(mtsVehicleCount <= 10 ){
                                licenceFeeDto.setBundle(1);
                            }else {
                                licenceFeeDto.setBundle(2);
                            }
                            if(hadEas && hadMts){
                                if(hciCodeEas!=null&&hciCodeMts!=null){
                                    if(hciCodeEas.equals(hciCodeMts)){
                                        licenceFeeDto.setBundle(3);
                                    }
                                }
                            }else{
                                String hciCode = appGrpPremisesDtos.get(0).getHciCode();
                                setEasMtsBundleInfo(licenceFeeDto, hciCode, hcsaFeeBundleItemDtos, serviceCode, appSubmissionDto.getLicenseeId(), mtsVehicleCount,appSubmissionDto.getAppType());
                            }
                        } else{
                            licenceFeeDto.setBundle(0);
                        }
                    }else{
                        licenceFeeDto.setBundle(0);
                    }
                    linenceFeeQuaryDtos.add(licenceFeeDto);
                }
            }else{
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                        baseServiceIds.add(appSvcRelatedInfoDto.getServiceId());
                    }else if(ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(appSvcRelatedInfoDto.getServiceType())){
                        for(HcsaServiceCorrelationDto hcsaServiceCorrelationDto:hcsaServiceCorrelationDtos){
                            if(appSvcRelatedInfoDto.getServiceId().equals(hcsaServiceCorrelationDto.getSpecifiedSvcId())){
                                baseServiceIds.add(hcsaServiceCorrelationDto.getBaseSvcId());
                            }
                        }
                    }
                }
            }
            log.info(StringUtil.changeForLog("base service size:"+baseServiceIds.size()));
            if(!onlySpecifiedSvc) {
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    LicenceFeeDto licenceFeeDto = new LicenceFeeDto(); licenceFeeDto.setBundle(0);
                    licenceFeeDto.setHciCode(appGrpPremisesDtos.get(0).getHciCode());
                    HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(appSvcRelatedInfoDto.getServiceCode());
                    if (hcsaServiceDto == null) {
                        log.info(StringUtil.changeForLog("hcsaServiceDto is empty "));
                        continue;
                    }
                    if (ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(hcsaServiceDto.getSvcType())) {
                        licenceFeeDto.setBaseService(hcsaServiceDto.getSvcCode());
                        licenceFeeDto.setIncludeBase(true);
                    } else if (ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(hcsaServiceDto.getSvcType())) {
                        for (HcsaServiceCorrelationDto hcsaServiceCorrelationDto : hcsaServiceCorrelationDtos) {
                            if (hcsaServiceDto.getId().equals(hcsaServiceCorrelationDto.getSpecifiedSvcId())) {
                                String baseSvcId = hcsaServiceCorrelationDto.getBaseSvcId();
                                if (baseServiceIds.contains(baseSvcId)) {
                                    log.info("can match base service ...");
                                    HcsaServiceDto baseService = HcsaServiceCacheHelper.getServiceById(baseSvcId);
                                    licenceFeeDto.setBaseService(baseService.getSvcCode());
                                }
                            }
                        }
                    }
                    licenceFeeDto.setServiceCode(hcsaServiceDto.getSvcCode());
                    licenceFeeDto.setServiceName(hcsaServiceDto.getSvcName());
                    licenceFeeDto.setPremises(premisessTypes);
                    licenceFeeDto.setCharity(isCharity);
                    Boolean existingOnSiteLic = licenceClient.existingOnSiteOrConveLic(appSvcRelatedInfoDto.getServiceName(),appSubmissionDto.getLicenseeId()).getEntity();
                    licenceFeeDto.setExistOnsite(existingOnSiteLic);
                    if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
                        String licenceId = appSubmissionDto.getLicenceId();
                        LicenceDto licenceDto = requestForChangeService.getLicenceById(licenceId);
                        if(licenceDto != null){
                            int migrated = licenceDto.getMigrated();
                            if(0 != migrated){
                                licenceFeeDto.setOldLicenceId(licenceDto.getId());
                                licenceFeeDto.setMigrated(migrated);
                            }
                        }
                        Date licExpiryDate = appSubmissionDto.getLicExpiryDate();
                        licenceFeeDto.setExpiryDate(licExpiryDate);
                        licenceFeeDto.setLicenceId(licenceId);
                    }
                    //set bundle
                    if(!IaisCommonUtils.isEmpty(hcsaFeeBundleItemDtos)){
                        String serviceCode=hcsaServiceDto.getSvcCode();
                        log.debug(StringUtil.changeForLog("set bundle info ..."));
                        if(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode)){
                            if(easVehicleCount <= 10 ){
                                licenceFeeDto.setBundle(1);
                            }else {
                                licenceFeeDto.setBundle(2);
                            }
                            if(hadEas && hadMts){
                                if(hciCodeEas!=null&&hciCodeMts!=null){
                                    if(hciCodeEas.equals(hciCodeMts)){
                                        if(easVehicleCount+mtsVehicleCount <= 10 ){
                                            licenceFeeDto.setBundle(1);
                                        }else {
                                            licenceFeeDto.setBundle(2);
                                        }
                                    }
                                }
                            }else{
                                String hciCode = appGrpPremisesDtos.get(0).getHciCode();
                                setEasMtsBundleInfo(licenceFeeDto, hciCode, hcsaFeeBundleItemDtos, serviceCode, appSubmissionDto.getLicenseeId(), easVehicleCount,appSubmissionDto.getAppType());
                            }
                        } else if(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)){
                            if(mtsVehicleCount <= 10 ){
                                licenceFeeDto.setBundle(1);
                            }else {
                                licenceFeeDto.setBundle(2);
                            }
                            if(hadEas && hadMts){
                                if(hciCodeEas!=null&&hciCodeMts!=null){
                                    if(hciCodeEas.equals(hciCodeMts)){
                                        licenceFeeDto.setBundle(3);
                                    }
                                }
                            }else{
                                String hciCode = appGrpPremisesDtos.get(0).getHciCode();
                                setEasMtsBundleInfo(licenceFeeDto, hciCode, hcsaFeeBundleItemDtos, serviceCode, appSubmissionDto.getLicenseeId(), mtsVehicleCount,appSubmissionDto.getAppType());
                            }
                        } else{
                            licenceFeeDto.setBundle(0);
                        }
                    }else{
                        licenceFeeDto.setBundle(0);
                    }
                    linenceFeeQuaryDtos.add(licenceFeeDto);
                }
                log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl linenceFeeQuaryDtos.size() is -->:" + linenceFeeQuaryDtos.size()));
            }
        }
        FeeDto entity ;
        entity = appConfigClient.renewFee(linenceFeeQuaryDtos).getEntity();
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getGroupAmount end ...."));
        return  entity;
    }
    @Override
    public FeeDto getCharityRenewalAmount(List<AppSubmissionDto> appSubmissionDtoList,boolean isCharity){
        List<LicenceFeeDto> linenceFeeQuaryDtos = IaisCommonUtils.genNewArrayList();
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getCharityRenewalAmount start ...."));
        log.info(StringUtil.changeForLog("current account is charity:"+isCharity));
        List<String> premises = IaisCommonUtils.genNewArrayList();
        for(AppSubmissionDto appSubmissionDto : appSubmissionDtoList){
            List<String> premisessTypes =  IaisCommonUtils.genNewArrayList();
            String licenceId1 = appSubmissionDto.getLicenceId();
            List<PremisesDto> PremisesDtoList = licenceClient.getPremisesDto(licenceId1).getEntity();
            for(PremisesDto dto : PremisesDtoList){
                boolean flag = true;
                String premisesType = dto.getPremisesType();
                if(premises.size() == 0){
                    premises.add(dto.getHciCode());
                    premisessTypes.add(premisesType);
                    flag = false;
                }else{
                    for(String premisesCode : premises){
                        if(premisesCode.equals(dto.getHciCode())){
                            flag = false;
                            break;
                        }
                    }
                }
                if(flag){
                    premisessTypes.add(premisesType);
                    premises.add(dto.getHciCode());
                }
            }
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            List<String> baseServiceIds = IaisCommonUtils.genNewArrayList();
            List<HcsaServiceCorrelationDto> hcsaServiceCorrelationDtos = appConfigClient.serviceCorrelation().getEntity();
            boolean onlySpecifiedSvc = false;
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo())){
                    onlySpecifiedSvc = true;
                }
            }
            if(onlySpecifiedSvc && ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appSubmissionDto.getAppType())){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    LicenceFeeDto licenceFeeDto = new LicenceFeeDto(); licenceFeeDto.setBundle(0);
                    if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                        licenceFeeDto.setIncludeBase(true);
                    }
                    licenceFeeDto.setHciCode(appSubmissionDto.getAppGrpPremisesDtoList().get(0).getHciCode());
                    HcsaServiceDto baseServiceDto = HcsaServiceCacheHelper.getServiceById(appSvcRelatedInfoDto.getBaseServiceId());
                    licenceFeeDto.setBaseService(baseServiceDto.getSvcCode());
                    licenceFeeDto.setServiceCode(appSvcRelatedInfoDto.getServiceCode());
                    licenceFeeDto.setServiceName(appSvcRelatedInfoDto.getServiceName());
                    licenceFeeDto.setPremises(premisessTypes);
                    if(!StringUtil.isEmpty(appSvcRelatedInfoDto.getRelLicenceNo())){
                        licenceFeeDto.setOnlyNewSpecified(true);
                    }else{
                        licenceFeeDto.setIncludeBase(true);
                    }
                    licenceFeeDto.setCharity(isCharity);
                    linenceFeeQuaryDtos.add(licenceFeeDto);
                }
            }else{
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    if(ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(appSvcRelatedInfoDto.getServiceType())){
                        baseServiceIds.add(appSvcRelatedInfoDto.getServiceId());
                    }else if(ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(appSvcRelatedInfoDto.getServiceType())){
                        for(HcsaServiceCorrelationDto hcsaServiceCorrelationDto:hcsaServiceCorrelationDtos){
                            if(appSvcRelatedInfoDto.getServiceId().equals(hcsaServiceCorrelationDto.getSpecifiedSvcId())){
                                baseServiceIds.add(hcsaServiceCorrelationDto.getBaseSvcId());
                            }
                        }
                    }
                }
            }
            log.info(StringUtil.changeForLog("base service size:"+baseServiceIds.size()));
            if(!onlySpecifiedSvc) {
                for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                    LicenceFeeDto licenceFeeDto = new LicenceFeeDto(); licenceFeeDto.setBundle(0);
                    licenceFeeDto.setHciCode(appSubmissionDto.getAppGrpPremisesDtoList().get(0).getHciCode());
                    HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(appSvcRelatedInfoDto.getServiceCode());
                    if (hcsaServiceDto == null) {
                        log.info(StringUtil.changeForLog("hcsaServiceDto is empty "));
                        continue;
                    }
                    if (ApplicationConsts.SERVICE_CONFIG_TYPE_BASE.equals(hcsaServiceDto.getSvcType())) {
                        licenceFeeDto.setBaseService(hcsaServiceDto.getSvcCode());
                        licenceFeeDto.setIncludeBase(true);
                    } else if (ApplicationConsts.SERVICE_CONFIG_TYPE_SUBSUMED.equals(hcsaServiceDto.getSvcType())) {
                        for (HcsaServiceCorrelationDto hcsaServiceCorrelationDto : hcsaServiceCorrelationDtos) {
                            if (hcsaServiceDto.getId().equals(hcsaServiceCorrelationDto.getSpecifiedSvcId())) {
                                String baseSvcId = hcsaServiceCorrelationDto.getBaseSvcId();
                                if (baseServiceIds.contains(baseSvcId)) {
                                    log.info("can match base service ...");
                                    HcsaServiceDto baseService = HcsaServiceCacheHelper.getServiceById(baseSvcId);
                                    licenceFeeDto.setBaseService(baseService.getSvcCode());
                                }
                            }
                        }
                    }
                    licenceFeeDto.setServiceCode(hcsaServiceDto.getSvcCode());
                    licenceFeeDto.setServiceName(hcsaServiceDto.getSvcName());
                    licenceFeeDto.setPremises(premisessTypes);
                    licenceFeeDto.setCharity(isCharity);
                    Boolean existingOnSiteLic = licenceClient.existingOnSiteOrConveLic(appSvcRelatedInfoDto.getServiceName(),appSubmissionDto.getLicenseeId()).getEntity();
                    if(premisessTypes.contains(ApplicationConsts.PREMISES_TYPE_OFF_SITE)){
                        licenceFeeDto.setExistOnsite(existingOnSiteLic);
                    }
                    if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appSubmissionDto.getAppType())) {
                        String licenceId = appSubmissionDto.getLicenceId();
                        LicenceDto licenceDto = requestForChangeService.getLicenceById(licenceId);
                        if(licenceDto != null){
                            int migrated = licenceDto.getMigrated();
                            if(0 != migrated){
                                licenceFeeDto.setOldLicenceId(licenceDto.getId());
                                licenceFeeDto.setMigrated(migrated);
                            }
                        }
                        Date licExpiryDate = appSubmissionDto.getLicExpiryDate();
                        licenceFeeDto.setExpiryDate(licExpiryDate);
                        licenceFeeDto.setLicenceId(licenceId);
                    }
                    linenceFeeQuaryDtos.add(licenceFeeDto);
                }
                log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl linenceFeeQuaryDtos.size() is -->:" + linenceFeeQuaryDtos.size()));
            }
        }
        FeeDto entity ;
        entity = appConfigClient.renewFee(linenceFeeQuaryDtos).getEntity();
        log.debug(StringUtil.changeForLog("the AppSubmisionServiceImpl getGroupAmount end ...."));
        return  entity;
    }

    private List<AppGrpPremisesDto> removeDuplicates(List<AppGrpPremisesDto> appGrpPremisesDtoList){
        if(IaisCommonUtils.isEmpty(appGrpPremisesDtoList)){
            return null;
        }
        List<AppGrpPremisesDto> result = IaisCommonUtils.genNewArrayList();
        if(result.size() == 0){
            result.add(appGrpPremisesDtoList.get(0));
        }else{
            for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
                boolean flag = true;
                for(AppGrpPremisesDto temp : result){
                    if(temp.getHciCode().equals(appGrpPremisesDto.getHciCode())){
                        flag = false;
                        break;
                    }
                }
                if(flag){
                    result.add(appGrpPremisesDto);
                }
            }
        }
        return result;
    }

    @Override
    public PreOrPostInspectionResultDto judgeIsPreInspection(AppSubmissionDto appSubmissionDto) {
        RecommendInspectionDto recommendInspectionDto = new RecommendInspectionDto();
        List<RiskAcceptiionDto> riskAcceptiionDtos = IaisCommonUtils.genNewArrayList();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
            riskAcceptiionDto.setScvCode(appSvcRelatedInfoDto.getServiceCode());
            riskAcceptiionDto.setSvcType(appSvcRelatedInfoDto.getServiceType());
            riskAcceptiionDto.setBaseServiceCodeList(appSvcRelatedInfoDto.getBaseServiceCodeList());
            riskAcceptiionDtos.add(riskAcceptiionDto);
        }

        return appConfigClient.recommendIsPreInspection(recommendInspectionDto).getEntity();
    }

    @Override
    public void setRiskToDto(AppSubmissionDto appSubmissionDto) {
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<RiskAcceptiionDto> riskAcceptiionDtoList = IaisCommonUtils.genNewArrayList();
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
            riskAcceptiionDto.setScvCode(appSvcRelatedInfoDto.getServiceCode());
            riskAcceptiionDto.setApptype(appSubmissionDto.getAppType());
            riskAcceptiionDtoList.add(riskAcceptiionDto);
        }
        List<RiskResultDto> riskResultDtoList = appConfigClient.getRiskResult(riskAcceptiionDtoList).getEntity();
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            String serviceCode = appSvcRelatedInfoDto.getServiceCode();
            RiskResultDto riskResultDto = getRiskResultDtoByServiceCode(riskResultDtoList,serviceCode);
            if(riskResultDto!= null){
                appSvcRelatedInfoDto.setScore(riskResultDto.getScore());
                appSvcRelatedInfoDto.setDoRiskDate(riskResultDto.getDoRiskDate());
            }
        }
    }

    @Override
    public AppSubmissionDto getAppSubmissionDtoByAppNo(String appNo) {
        return applicationFeClient.getAppSubmissionDtoByAppNo(appNo).getEntity();
    }

    @Override
    public AppSubmissionDto getAppSubmissionDto(String appNo) {
        return applicationFeClient.getAppSubmissionDto(appNo).getEntity();
    }

    private RiskResultDto getRiskResultDtoByServiceCode(List<RiskResultDto> riskResultDtoList,String serviceCode){
        RiskResultDto result = null;
        if(riskResultDtoList == null || StringUtil.isEmpty(serviceCode)){
            return null;
        }
        for(RiskResultDto riskResultDto : riskResultDtoList){
            if(serviceCode.equals(riskResultDto.getSvcCode())){
                result = riskResultDto ;
                break;
            }
        }
        return result;
    }

    private void  informationEventBus(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto, Process process){
        //prepare request parameters
        appSubmissionRequestInformationDto.setEventRefNo(UUID.randomUUID().toString());
        SubmitResp submitResp = eventBusHelper.submitAsyncRequest(appSubmissionRequestInformationDto,
                generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_APPSUBMIT, EventBusConsts.OPERATION_REQUEST_INFORMATION,
                appSubmissionRequestInformationDto.getEventRefNo(), process);
    }

    private void premisesListInformationEventBus(AppSubmissionRequestInformationDto appSubmissionRequestInformationDto,
                                                 Process process){
        appSubmissionRequestInformationDto.setEventRefNo(appSubmissionRequestInformationDto.getAppSubmissionDto().getAppGrpNo());
        SubmitResp submitResp = eventBusHelper.submitAsyncRequest(appSubmissionRequestInformationDto,
                generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_REQUEST_INFORMATION,
                appSubmissionRequestInformationDto.getEventRefNo(), process);
    }

    private  void eventBus(AppSubmissionDto appSubmissionDto, Process process){
        //prepare request parameters
        appSubmissionDto.setEventRefNo(appSubmissionDto.getAppGrpNo());

        SubmitResp submitResp = eventBusHelper.submitAsyncRequest(appSubmissionDto, generateIdClient.getSeqId().getEntity(),
                EventBusConsts.SERVICE_NAME_APPSUBMIT,
                EventBusConsts.OPERATION_NEW_APP_SUBMIT,
                appSubmissionDto.getEventRefNo(), process);
    }

    @Override
    public AppSubmissionDto getAppSubmissionDtoByLicenceId(String licenceId) {
        return licenceClient.getAppSubmissionDto(licenceId).getEntity();
    }

    @Override
    public AppSubmissionDto viewAppSubmissionDto(String licenceId) {

        return  licenceClient.viewAppSubmissionDto(licenceId).getEntity();
    }

    @Override
    public FeeDto getGroupAmendAmount(AmendmentFeeDto amendmentFeeDto) {
        return appConfigClient.amendmentFee(amendmentFeeDto).getEntity();
    }

    @Override
    public AppSubmissionDto submitRequestChange(AppSubmissionDto appSubmissionDto, Process process) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appSubmissionDto = applicationFeClient.saveAppsForRequestForChange(appSubmissionDto).getEntity();
        //eventBus(appSubmissionDto, process);
        return appSubmissionDto;
    }

    @Override
    public AppSubmissionDto submitRenew(AppSubmissionDto appSubmissionDto) {
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appSubmissionDto = applicationFeClient.saveAppsForRenew(appSubmissionDto).getEntity();
        return appSubmissionDto;
    }

    @Override
    public MsgTemplateDto getMsgTemplateById(String id) {
        MsgTemplateDto msgTemplateDto = systemAdminClient.getMsgTemplate(id).getEntity();
        return msgTemplateDto;
    }

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Autowired
    private EicClient eicClient;

    @Value("${spring.application.name}")
    private String currentApp;
    @Value("${iais.current.domain}")
    private String currentDomain;

    @Override
    public void feSendEmail(EmailDto emailDto) {
        String moduleName = currentApp + "-" + currentDomain;
        String refNo = String.valueOf(System.currentTimeMillis());
        EicRequestTrackingDto dto = new EicRequestTrackingDto();
        dto.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
        dto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        dto.setActionClsName(this.getClass().getName());
        dto.setActionMethod("callEicSendEmail");
        dto.setDtoClsName(emailDto.getClass().getName());
        dto.setDtoObject(JsonUtil.parseToJson(emailDto));
        dto.setRefNo(refNo);
        dto.setModuleName(moduleName);
        eicClient.saveEicTrack(dto);
        callEicSendEmail(emailDto);
        dto = eicClient.getPendingRecordByReferenceNumber(refNo).getEntity();
        Date now = new Date();
        dto.setProcessNum(1);
        dto.setFirstActionAt(now);
        dto.setLastActionAt(now);
        dto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        List<EicRequestTrackingDto> list = IaisCommonUtils.genNewArrayList(1);
        list.add(dto);
        eicClient.updateStatus(list);
    }

    public void callEicSendEmail(EmailDto emailDto){
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        feEicGatewayClient.feSendEmail(emailDto,signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
    }

    @Override
    public ApplicationGroupDto createApplicationDataByWithOutRenewal(RenewDto renewDto) {
        return applicationFeClient.createApplicationDataByWithOutRenewal(renewDto).getEntity();
    }

    @Override
    public void updateApplicationsStatus(String appGroupId, String stuts) {
        List<ApplicationDto> applicationDtos = listApplicationByGroupId(appGroupId);
        for(ApplicationDto application : applicationDtos){
            application.setStatus(stuts);
            applicationFeClient.updateApplication(application);
        }
    }

    @Override
    public boolean checkRenewalStatus(String licenceId) {
        boolean flag = true;
        List<ApplicationDto> apps = applicationFeClient.getAppByLicIdAndExcludeNew(licenceId).getEntity();
        for(ApplicationDto app : apps){
            if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(app.getApplicationType())
                    && !(ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(app.getStatus()))){
                flag = false;
            }
        }
        return flag;
    }

    @Override
    public AppSubmissionDto getExistBaseSvcInfo(List<String> licenceIds) {
        return licenceClient.getExistBaseSvcInfo(licenceIds).getEntity();
    }

    @Override
    public void transform(AppSubmissionDto appSubmissionDto, String licenseeId) throws Exception{
        Double amount = 0.0;
        AuditTrailDto internet = AuditTrailHelper.getCurrentAuditTrailDto();
        String grpNo = systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE).getEntity();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList){
            String serviceName = appSvcRelatedInfoDto.getServiceName();
            HcsaServiceDto hcsaServiceDto =appConfigClient.getActiveHcsaServiceDtoByName(serviceName).getEntity();
            String svcId = hcsaServiceDto.getId();
            String svcCode = hcsaServiceDto.getSvcCode();
            appSvcRelatedInfoDto.setServiceId(svcId);
            appSvcRelatedInfoDto.setServiceCode(svcCode);
            appSvcRelatedInfoDto.setServiceName(hcsaServiceDto.getSvcName());
            List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos= serviceConfigService.loadLaboratoryDisciplines(svcId);
            if(hcsaSvcSubtypeOrSubsumedDtos!=null && !hcsaSvcSubtypeOrSubsumedDtos.isEmpty()){
                appSubmissionService.changeSvcScopeIdByConfigName(hcsaSvcSubtypeOrSubsumedDtos,appSubmissionDto);
            }
            List<AppSvcDocDto> appSvcDocDtoLit = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
            if(appSvcDocDtoLit!=null){
                appSvcDocDtoLit.forEach((v)->{
                    if(v.getSvcDocId()!=null){
                        HcsaSvcDocConfigDto entity = appConfigClient.getHcsaSvcDocConfigDtoById(v.getSvcDocId()).getEntity();
                        String dupForPerson = entity.getDupForPerson();
                        v.setDupForPerson(dupForPerson);
                    }
                });
            }
        }
        requestForChangeService.svcDocToPresmise(appSubmissionDto);
        appSubmissionDto.setAppGrpNo(grpNo);
        appSubmissionDto.setFromBe(false);
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        requestForChangeService.changeDocToNewVersion(appSubmissionDto);
        appSubmissionDto.setAmount(appSubmissionDto.getAmount()==null?amount:appSubmissionDto.getAmount());
        appSubmissionDto.setAuditTrailDto(internet);
        appSubmissionDto.setPreInspection(true);
        appSubmissionDto.setRequirement(true);
        appSubmissionDto.setLicenseeId(licenseeId);
        appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_PAYMENT);
        appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
        String draftNo = appSubmissionDto.getDraftNo();
        if(draftNo==null){
            draftNo = appSubmissionService.getDraftNo(appSubmissionDto.getAppType());
            appSubmissionDto.setDraftNo(draftNo);
        }
        appSubmissionDto.setAppGrpId(null);
        setRiskToDto(appSubmissionDto);
        requestForChangeService.setRelatedInfoBaseServiceId(appSubmissionDto);
    }

    @Override
    public void saveAppsubmission(AppSubmissionDto appSubmissionDto) {
        AppSubmissionDto entity = applicationFeClient.saveSubmision(appSubmissionDto).getEntity();
        applicationFeClient.saveApps(entity).getEntity();
    }

    @Override
    public void setDraftNo(AppSubmissionDto appSubmissionDto) {
        String appType=null;
        if(appSubmissionDto!=null){
             appType = appSubmissionDto.getAppType();
        }
        if(appType!=null){
            String draft = systemAdminClient.draftNumber(appType).getEntity();
            appSubmissionDto.setDraftNo(draft);
        }

    }

    @Override
    public void saveAppGrpMisc(AppGroupMiscDto appGroupMiscDto) {
        applicationFeClient.saveAppGroupMiscDto(appGroupMiscDto);
    }

    @Override
    public List<AppSubmissionDto> getAppSubmissionDtoByGroupNo(String groupNo) {
        List<ApplicationDto> applicationDtos = applicationFeClient.getApplicationsByGroupNo(groupNo).getEntity();
        List<AppSubmissionDto> appSubmissionDtos =IaisCommonUtils.genNewArrayList();
        for(ApplicationDto applicationDto : applicationDtos){
            AppSubmissionDto appSubmissionDto = applicationFeClient.getAppSubmissionDtoByAppNo(applicationDto.getApplicationNo()).getEntity();
            appSubmissionDtos.add(appSubmissionDto);
        }
        return appSubmissionDtos;
    }

    @Override
    public void deleteOverdueDraft(String draftValidity) {
        applicationFeClient.deleteOverdueDraft(draftValidity);
    }

    @Override
    public List<AppGrpPremisesDto> getAppGrpPremisesDto(String appNo) {
        List<AppGrpPremisesDto> entity = applicationFeClient.getAppGrpPremisesDtoByAppGroId(appNo).getEntity();
        return entity;
    }

    @Override
    public AppFeeDetailsDto saveAppFeeDetails(AppFeeDetailsDto appFeeDetailsDto) {
        return   applicationFeClient.saveAppFeeDetails(appFeeDetailsDto).getEntity();
    }

    @Override
    public ApplicationDto getMaxVersionApp(String appNo) {
        return applicationFeClient.getApplicationDtoByVersion(appNo).getEntity();
    }

    @Override
    public void updateMsgStatus(String msgId, String status) {
        feMessageClient.updateMsgStatus(msgId,status);
    }



    @Override
    public InterMessageDto getInterMessageById(String msgId) {
        return feMessageClient.getInterMessageById(msgId).getEntity();
    }

    @Override
    public List<String> getHciFromPendAppAndLic(String licenseeId, List<HcsaServiceDto> hcsaServiceDtos) {
        List<String> result = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(licenseeId) && !IaisCommonUtils.isEmpty(hcsaServiceDtos)){
            List<String> svcNames = IaisCommonUtils.genNewArrayList();
            for(HcsaServiceDto hcsaServiceDto:hcsaServiceDtos){
                svcNames.add(hcsaServiceDto.getSvcName());
            }
            AppPremisesDoQueryDto appPremisesDoQueryDto = new AppPremisesDoQueryDto();
            List<HcsaServiceDto>  HcsaServiceDtoList= appConfigClient.getHcsaServiceByNames(svcNames).getEntity();
            List<String> svcIds = IaisCommonUtils.genNewArrayList();
            for(HcsaServiceDto hcsaServiceDto:HcsaServiceDtoList){
                svcIds.add(hcsaServiceDto.getId());
            }
            appPremisesDoQueryDto.setLicenseeId(licenseeId);
            appPremisesDoQueryDto.setSvcIdList(svcIds);
            String svcNameStr = JsonUtil.parseToJson(svcNames);
            List<PremisesDto> premisesDtos = licenceClient.getPremisesByLicseeIdAndSvcName(licenseeId,svcNameStr).getEntity();
            List<AppGrpPremisesEntityDto> appGrpPremisesEntityDtos = applicationFeClient.getPendAppPremises(appPremisesDoQueryDto).getEntity();
            if(!IaisCommonUtils.isEmpty(premisesDtos)){
                for(PremisesDto premisesHciDto:premisesDtos){
                    result.addAll(NewApplicationHelper.genPremisesHciList(premisesHciDto));
                }
            }
            if(!IaisCommonUtils.isEmpty(appGrpPremisesEntityDtos)){
                for(AppGrpPremisesEntityDto premisesEntityDto:appGrpPremisesEntityDtos){
                    PremisesDto premisesDto = MiscUtil.transferEntityDto(premisesEntityDto,PremisesDto.class);
                    result.addAll(NewApplicationHelper.genPremisesHciList(premisesDto));
                }
            }
        }
        return result;
    }

    @Override
    public List<AppGrpPremisesEntityDto> getPendAppPremises(String licenseeId, List<HcsaServiceDto> hcsaServiceDtos) {
        List<AppGrpPremisesEntityDto> appGrpPremisesEntityDtos = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(licenseeId) && !IaisCommonUtils.isEmpty(hcsaServiceDtos)) {
            List<String> svcNames = IaisCommonUtils.genNewArrayList();
            for(HcsaServiceDto hcsaServiceDto:hcsaServiceDtos){
                svcNames.add(hcsaServiceDto.getSvcName());
            }
            AppPremisesDoQueryDto appPremisesDoQueryDto = new AppPremisesDoQueryDto();
            List<HcsaServiceDto>  HcsaServiceDtoList= appConfigClient.getHcsaServiceByNames(svcNames).getEntity();
            List<String> svcIds = IaisCommonUtils.genNewArrayList();
            for(HcsaServiceDto hcsaServiceDto:HcsaServiceDtoList){
                svcIds.add(hcsaServiceDto.getId());
            }
            appPremisesDoQueryDto.setLicenseeId(licenseeId);
            appPremisesDoQueryDto.setSvcIdList(svcIds);
            appGrpPremisesEntityDtos = applicationFeClient.getPendAppPremises(appPremisesDoQueryDto).getEntity();
        }
        return appGrpPremisesEntityDtos;
    }

    @Override
    public List<AppAlignLicQueryDto> getAppAlignLicQueryDto(String licenseeId, List<String> svcNameList,List<String> premTypeList) {
        List<AppAlignLicQueryDto> appAlignLicQueryDtos = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(licenseeId) && !IaisCommonUtils.isEmpty(svcNameList) && !IaisCommonUtils.isEmpty(premTypeList)) {
            String svcNames = JsonUtil.parseToJson(svcNameList);
            String premTypeStr = JsonUtil.parseToJson(premTypeList);
            appAlignLicQueryDtos = licenceClient.getAppAlignLicQueryDto(licenseeId,svcNames,premTypeStr).getEntity();
        }
        return appAlignLicQueryDtos;
    }

    @Override
    public List<AppGrpPremisesDto> getLicPremisesInfo(String id) {
        return licenceClient.getLicPremisesById(id).getEntity();
    }

    @Override
    public Boolean isNewLicensee(String licenseeId) {
        return licenceClient.checkIsNewLicsee(licenseeId).getEntity();
    }

    @Override
    public InterMessageDto getInterMessageBySubjectLike(String subject, String status) {
        return feMessageClient.getInterMessageBySubjectLike(subject,status).getEntity();

    }

    @Override
    public AppGrpPremisesEntityDto getPremisesByAppNo(String appNo) {
        return applicationFeClient.getPremisesByAppNo(appNo).getEntity();
    }

    @Override
    public AppGrpPrimaryDocDto getMaxVersionPrimaryComDoc(String appGrpId, String configDocId,String seqNum) {
        return applicationFeClient.getMaxVersionPrimaryComDoc(appGrpId,configDocId,seqNum).getEntity();
    }

    @Override
    public AppSvcDocDto getMaxVersionSvcComDoc(String appGrpId, String configDocId,String seqNum) {
        return applicationFeClient.getMaxVersionSvcComDoc(appGrpId,configDocId,seqNum).getEntity();
    }

    @Override
    public AppGrpPrimaryDocDto getMaxVersionPrimarySpecDoc(String appGrpId, String configDocId, String appNo,String seqNum) {
        return applicationFeClient.getMaxVersionPrimarySpecDoc(appGrpId,configDocId,appNo,seqNum).getEntity();
    }

    @Override
    public AppSvcDocDto getMaxVersionSvcSpecDoc(AppSvcDocDto appSvcDocDto,String appNo) {
        return applicationFeClient.getMaxVersionSvcSpecDoc(appSvcDocDto,appNo).getEntity();
    }

    @Override
    public AppSubmissionDto getAppSubmissionDtoByAppGrpNo(String appGrpNo) {
        return applicationFeClient.getAppSubmissionDtoByAppGrpNo(appGrpNo).getEntity();
    }

    @Override
    public List<AppGrpPrimaryDocDto> syncPrimaryDoc(String appType,Boolean isRfi,List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos, List<HcsaSvcDocConfigDto> primaryDocConfig) throws CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("do syncPrimaryDoc start ..."));
        log.debug(StringUtil.changeForLog("appType:"+ appType));
        log.debug(StringUtil.changeForLog("isRfi:"+ isRfi));
        List<AppGrpPrimaryDocDto> newGrpPrimaryDocList = IaisCommonUtils.genNewArrayList();
        //rfc/renew for primary doc
        boolean rfcOrRenwOrNew = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType) || ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType) || ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType);
        if(!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos) && !IaisCommonUtils.isEmpty(primaryDocConfig)){
            List<String> docConfigIds = IaisCommonUtils.genNewArrayList();
            for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtos){
                docConfigIds.add(appGrpPrimaryDocDto.getSvcComDocId());
            }
            log.debug(StringUtil.changeForLog("docConfigIds size:"+docConfigIds.size()));
            List<HcsaSvcDocConfigDto> oldHcsaSvcDocConfigDtos = serviceConfigService.getPrimaryDocConfigByIds(docConfigIds);
            if(!IaisCommonUtils.isEmpty(oldHcsaSvcDocConfigDtos)){
                if(rfcOrRenwOrNew && !isRfi){
                    for(HcsaSvcDocConfigDto oldDocConfig:oldHcsaSvcDocConfigDtos){
                        for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtos){
                            if(oldDocConfig.getId().equals(appGrpPrimaryDocDto.getSvcComDocId())){
                                String oldDocTitle = oldDocConfig.getDocTitle();
                                log.debug(StringUtil.changeForLog("old doc title:"+oldDocTitle));
                                for(HcsaSvcDocConfigDto docConfig:primaryDocConfig){
                                    String newConfigDocTitle = docConfig.getDocTitle();
                                    log.debug(StringUtil.changeForLog("new config doc title:"+newConfigDocTitle));
                                    if(newConfigDocTitle.equals(oldDocTitle)){
                                        AppGrpPrimaryDocDto newGrpPrimaryDoc = (AppGrpPrimaryDocDto) CopyUtil.copyMutableObject(appGrpPrimaryDocDto);
                                        newGrpPrimaryDoc.setSvcComDocId(docConfig.getId());
                                        newGrpPrimaryDoc.setSvcComDocName(docConfig.getDocTitle());
                                        //newGrpPrimaryDoc.setDocConfigVersion(docConfig.getVersion());
                                        newGrpPrimaryDocList.add(newGrpPrimaryDoc);
                                        //break;
                                    }
                                }
                                //break;
                            }
                        }
                    }
                }else if(isRfi){
                    log.debug(StringUtil.changeForLog("set doc title for rfi ..."));
                    //set title name
                    for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtos){
                        for(HcsaSvcDocConfigDto oldDocConfig:oldHcsaSvcDocConfigDtos){
                            if(oldDocConfig.getId().equals(appGrpPrimaryDocDto.getSvcComDocId())){
                                AppGrpPrimaryDocDto newGrpPrimaryDoc = (AppGrpPrimaryDocDto) CopyUtil.copyMutableObject(appGrpPrimaryDocDto);
                                newGrpPrimaryDoc.setSvcComDocName(oldDocConfig.getDocTitle());
                                //newGrpPrimaryDoc.setDocConfigVersion(oldDocConfig.getVersion());
                                newGrpPrimaryDocList.add(newGrpPrimaryDoc);
                                break;
                            }
                        }
                    }
                }
            }
        }
        log.debug(StringUtil.changeForLog("do syncPrimaryDoc end ..."));
        return newGrpPrimaryDocList;
    }

    @Override
    public List<AppGrpPrimaryDocDto> handlerPrimaryDoc(List<AppGrpPremisesDto> appGrpPremisesDtos,List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos) {
        log.debug(StringUtil.changeForLog("do handlerPrimaryDoc start ..."));
        if(IaisCommonUtils.isEmpty(appGrpPremisesDtos) || IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos)){
            return appGrpPrimaryDocDtos;
        }
        log.debug(StringUtil.changeForLog("appGrpPrimaryDocDtos size:" +  appGrpPrimaryDocDtos.size()));
        //remove empty doc
        List<AppGrpPrimaryDocDto> notEmptyDocList = IaisCommonUtils.genNewArrayList();
        for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtos){
            if(!StringUtil.isEmpty(appGrpPrimaryDocDto.getFileRepoId())){
                notEmptyDocList.add(appGrpPrimaryDocDto);
            }
        }
        // log.debug(StringUtil.changeForLog("notEmptyDocList size:" +  notEmptyDocList.size()));
        //add empty doc
        List<HcsaSvcDocConfigDto> docConfigDtos = serviceConfigService.getAllHcsaSvcDocs(null);
        List<AppGrpPrimaryDocDto> newPrimaryDocList = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(docConfigDtos)){
            log.debug(StringUtil.changeForLog("hcsaSvcDocDtos not empty ..."));
            log.debug(StringUtil.changeForLog("hcsa svc doc config dto size:" +  docConfigDtos.size()));
            if(notEmptyDocList != null && notEmptyDocList.size() > 0){
                List<HcsaSvcDocConfigDto> oldHcsaSvcDocDtos = serviceConfigService.getPrimaryDocConfigById(notEmptyDocList.get(0).getSvcComDocId());
                log.debug(StringUtil.changeForLog("oldHcsaSvcDocDtos:" +  JsonUtil.parseToJson(oldHcsaSvcDocDtos)));
                for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto:docConfigDtos){
                    String docTitle = hcsaSvcDocConfigDto.getDocTitle();
                    String dupPrem = hcsaSvcDocConfigDto.getDupForPrem();
                    int i = 0;
                    for(HcsaSvcDocConfigDto oldHcsaSvcDocDto:oldHcsaSvcDocDtos){
                        if(docTitle.equals(oldHcsaSvcDocDto.getDocTitle())){
                            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = NewApplicationHelper.getAppGrpprimaryDocDto(oldHcsaSvcDocDto.getId(),notEmptyDocList);
                            if(IaisCommonUtils.isEmpty(appGrpPrimaryDocDtoList)){
                                AppGrpPrimaryDocDto appGrpPrimaryDocDto = NewApplicationHelper.genEmptyPrimaryDocDto(hcsaSvcDocConfigDto.getId());
                                handlerDupPremDoc(dupPrem,appGrpPrimaryDocDto,appGrpPremisesDtos,newPrimaryDocList);
                            }else{
                                for(AppGrpPrimaryDocDto appGrpPrimaryDocDto:appGrpPrimaryDocDtoList){
                                    appGrpPrimaryDocDto.setSvcComDocId(hcsaSvcDocConfigDto.getId());
                                    // handlerDupPremDoc(dupPrem,appGrpPrimaryDocDto,appGrpPremisesDtos,newPrimaryDocList);
                                    newPrimaryDocList.add(appGrpPrimaryDocDto);
                                }
                            }
                            break;
                        }
                        if(i == oldHcsaSvcDocDtos.size()){
                            //add empty doc
                            AppGrpPrimaryDocDto appGrpPrimaryDocDto = NewApplicationHelper.genEmptyPrimaryDocDto(hcsaSvcDocConfigDto.getId());
                            newPrimaryDocList.add(appGrpPrimaryDocDto);
                        }
                        i++;
                    }
                }
            }else{
                for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto:docConfigDtos){
                    AppGrpPrimaryDocDto appGrpPrimaryDocDto = NewApplicationHelper.genEmptyPrimaryDocDto(hcsaSvcDocConfigDto.getId());
                    newPrimaryDocList.add(appGrpPrimaryDocDto);
                }
            }
        }
        log.debug(StringUtil.changeForLog("newPrimaryDocList size:" +  newPrimaryDocList.size()));
        log.debug(StringUtil.changeForLog("newPrimaryDocList:" +  JsonUtil.parseToJson(newPrimaryDocList)));
        log.debug(StringUtil.changeForLog("do handlerPrimaryDoc end ..."));
        return newPrimaryDocList;
    }

    /**
     * validate the all submission data
     *
     * @param bpc
     * @return
     */
    @Override
    public Map<String, String> doPreviewAndSumbit(BaseProcessClass bpc) {
        Map<String, String> previewAndSubmitMap = IaisCommonUtils.genNewHashMap();
        //
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.OLDAPPSUBMISSIONDTO);
        previewAndSubmitMap = doPreviewSubmitValidate(previewAndSubmitMap,appSubmissionDto,oldAppSubmissionDto,bpc);
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),previewAndSubmitMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
        return previewAndSubmitMap;
    }

    @Override
    public Map<String, String> doPreviewSubmitValidate(Map<String, String> previewAndSubmitMap, AppSubmissionDto appSubmissionDto, AppSubmissionDto oldAppSubmissionDto,BaseProcessClass bpc) {
        StringBuilder sB = new StringBuilder(40);
        HashMap<String, String> coMap = (HashMap<String, String>) bpc.request.getSession().getAttribute("coMap");
        // sub licensee (licensee details)
        SubLicenseeDto subLicenseeDto = appSubmissionDto.getSubLicenseeDto();
        if (validateSubLicenseeDto(previewAndSubmitMap, subLicenseeDto, bpc.request)) {
            coMap.put("licensee", "licensee");
        } else {
            coMap.put("licensee", "");
        }
        // premises
        List<String> premisesHciList = (List<String>) ParamUtil.getSessionAttr(bpc.request, NewApplicationConstant.PREMISES_HCI_LIST);
        String keyWord = MasterCodeUtil.getCodeDesc("MS001");
        boolean isRfi = NewApplicationHelper.checkIsRfi(bpc.request);
        Map<String, String> premissMap = requestForChangeService.doValidatePremiss(appSubmissionDto, oldAppSubmissionDto, premisesHciList, keyWord, isRfi);
        premissMap.remove("hciNameUsed");
        if (!premissMap.isEmpty()) {
            previewAndSubmitMap.put("premiss", MessageUtil.replaceMessage("GENERAL_ERR0006","premiss","field"));
            String premissMapStr = JsonUtil.parseToJson(premissMap);
            log.info(StringUtil.changeForLog("premissMap json str:" + premissMapStr));
            coMap.put("premises", "");
        } else {
            coMap.put("premises", "premises");
        }
        // service info
        List<AppSvcRelatedInfoDto> dto = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<AppSvcVehicleDto> appSvcVehicleDtos = IaisCommonUtils.genNewArrayList();
        dto.stream().forEach(obj -> {
            if (!IaisCommonUtils.isEmpty(obj.getAppSvcVehicleDtoList())) {
                appSvcVehicleDtos.addAll(obj.getAppSvcVehicleDtoList());
            }
        });
        ServiceStepDto serviceStepDto = new ServiceStepDto();
        for (int i = 0; i < dto.size(); i++) {
            AppSvcRelatedInfoDto currSvcInfoDto = dto.get(i);
            String serviceId = currSvcInfoDto.getServiceId();
            List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = serviceConfigService.getHcsaServiceStepSchemesByServiceId(serviceId);
            serviceStepDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemeDtos);
            List<HcsaSvcPersonnelDto> currentSvcAllPsnConfig = serviceConfigService.getSvcAllPsnConfig(hcsaServiceStepSchemeDtos, serviceId);
            // vehicle
            List<String> appIds = NewApplicationHelper.getRelatedAppId(currSvcInfoDto.getAppId(), appSubmissionDto.getLicenceId(),
                    currSvcInfoDto.getServiceName());
            List<AppSvcVehicleDto> oldAppSvcVehicleDto = appSubmissionService.getActiveVehicles(appIds);
            Map<String, String> map = doCheckBox(bpc, sB, hcsaServiceStepSchemeDtos, currentSvcAllPsnConfig, currSvcInfoDto,dto,
                    appSubmissionDto.getAppGrpPremisesDtoList(), oldAppSvcVehicleDto);
            if (!map.isEmpty()) {
                sB.append(serviceId);
                previewAndSubmitMap.putAll(map);
                previewAndSubmitMap.put("service", MessageUtil.replaceMessage("GENERAL_ERR0006","service","field"));
                String mapStr = JsonUtil.parseToJson(map);
                log.info(StringUtil.changeForLog("map json str:" + mapStr));
            }
            NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),map,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
        }
        Map<String, String> documentMap = IaisCommonUtils.genNewHashMap();
        documentValid(bpc.request, documentMap,false);
        doCommomDocument(bpc.request, documentMap);
        NewApplicationHelper.setAudiErrMap(isRfi,appSubmissionDto.getAppType(),documentMap,appSubmissionDto.getRfiAppNo(),appSubmissionDto.getLicenceNo());
        if (!documentMap.isEmpty()) {
            previewAndSubmitMap.put("document", MessageUtil.replaceMessage("GENERAL_ERR0006","document","field"));
            String documentMapStr = JsonUtil.parseToJson(documentMap);
            log.info(StringUtil.changeForLog("documentMap json str:" + documentMapStr));
            coMap.put("document", "");
        } else {
            coMap.put("document", "document");
        }
        if (!StringUtil.isEmpty(sB.toString())) {
            previewAndSubmitMap.put("serviceId", sB.toString());
            coMap.put("information", "");
        } else {
            coMap.put("information", "information");
        }
        bpc.request.getSession().setAttribute("coMap", coMap);

        String errIcon = (String) bpc.request.getSession().getAttribute("serviceConfig");
        if(!StringUtil.isEmpty(errIcon)){
            sB.append(errIcon);
        }
        bpc.request.getSession().setAttribute("serviceConfig",sB.toString());
        log.info(StringUtil.changeForLog("Error Message for Preview Submit Validation: " + previewAndSubmitMap));
        return previewAndSubmitMap;
    }

    @Override
    public Map<String, List<HcsaSvcPersonnelDto>> getAllSvcAllPsnConfig(HttpServletRequest request) {
        Map<String, List<HcsaSvcPersonnelDto>> svcAllPsnConfig = (Map<String, List<HcsaSvcPersonnelDto>>) ParamUtil.getSessionAttr(request, NewApplicationDelegator.SERVICEALLPSNCONFIGMAP);
        if (svcAllPsnConfig == null) {
            AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            List<String> svcIds = IaisCommonUtils.genNewArrayList();
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                svcIds.add(appSvcRelatedInfoDto.getServiceId());
            }
            List<HcsaServiceStepSchemeDto> svcStepConfigs = serviceConfigService.getHcsaServiceStepSchemesByServiceId(svcIds);
            svcAllPsnConfig = serviceConfigService.getAllSvcAllPsnConfig(svcStepConfigs, svcIds);
        }
        return svcAllPsnConfig;
    }

    @Override
    public Map<String, String> doCheckBox(BaseProcessClass bpc, StringBuilder sB,
            List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos, List<HcsaSvcPersonnelDto> currentSvcAllPsnConfig,
            AppSvcRelatedInfoDto dto, List<AppSvcRelatedInfoDto> dtos, List<AppGrpPremisesDto> appGrpPremisesDtos) {
        return doCheckBox(bpc, sB, hcsaServiceStepSchemeDtos, currentSvcAllPsnConfig, dto, dtos, appGrpPremisesDtos, null);
    }

    private Map<String, String> doCheckBox(BaseProcessClass bpc, StringBuilder sB,
            List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos, List<HcsaSvcPersonnelDto> currentSvcAllPsnConfig,
            AppSvcRelatedInfoDto dto, List<AppSvcRelatedInfoDto> dtos, List<AppGrpPremisesDto> appGrpPremisesDtos,
            List<AppSvcVehicleDto> oldAppSvcVehicleDto) {
        int uploadFileLimit = systemParamConfig.getUploadFileLimit();
        String sysFileType = systemParamConfig.getUploadFileType();
        String serviceId = dto.getServiceId();
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        for (HcsaSvcPersonnelDto hcsaSvcPersonnelDto : currentSvcAllPsnConfig) {
            String psnType = hcsaSvcPersonnelDto.getPsnType();
            int mandatoryCount = hcsaSvcPersonnelDto.getMandatoryCount();
            valiatePersonnelCount(dto, psnType, mandatoryCount, sB, errorMap);
        }
        String prsError = MessageUtil.getMessageDesc("GENERAL_ERR0042");
        for (HcsaServiceStepSchemeDto step : hcsaServiceStepSchemeDtos) {
            String currentStep = Optional.ofNullable(step)
                    .map(HcsaServiceStepSchemeDto::getStepCode)
                    .orElse(HcsaConsts.STEP_BUSINESS_NAME);
            if (HcsaConsts.STEP_BUSINESS_NAME.equals(currentStep)) {
                // business name
                List<AppSvcBusinessDto> appSvcBusinessDtoList = dto.getAppSvcBusinessDtoList();
                Map<String,String> businessNameErrorMap = IaisCommonUtils.genNewHashMap();
                NewApplicationHelper.doValidateBusiness(appSvcBusinessDtoList, dto.getApplicationType(), dto.getLicenceId(),
                        businessNameErrorMap);
                if (!businessNameErrorMap.isEmpty()) {
                    errorMap.putAll(businessNameErrorMap);
                    errorMap.put("Business Name", "error");
                }
            } else if (HcsaConsts.STEP_VEHICLES.equals(currentStep)) {
                // Vehicles
                List<AppSvcVehicleDto> appSvcVehicleDtos = IaisCommonUtils.genNewArrayList();
                if (!IaisCommonUtils.isEmpty(dtos)) {
                    for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : dtos) {
                        // Don't add current service vehicles
                        if (Objects.equals(appSvcRelatedInfoDto.getServiceId(), serviceId)) {
                            continue;
                        }
                        List<AppSvcVehicleDto> appSvcVehicleDtoList = appSvcRelatedInfoDto.getAppSvcVehicleDtoList();
                        if (!IaisCommonUtils.isEmpty(appSvcVehicleDtoList)) {
                            appSvcVehicleDtos.addAll(appSvcVehicleDtoList);
                        }
                    }
                }
                validateVehicle.doValidateVehicles(errorMap, appSvcVehicleDtos, dto.getAppSvcVehicleDtoList(), oldAppSvcVehicleDto);
            } else if (HcsaConsts.STEP_CLINICAL_DIRECTOR.equals(currentStep)) {
                // Clinical Director
                String currSvcCode = dto.getServiceCode();
                if (StringUtil.isEmpty(currSvcCode)) {
                    HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
                    currSvcCode = Optional.of(hcsaServiceDto).map(HcsaServiceDto::getSvcCode).orElseGet(() -> "");
                }
                List<AppSvcPrincipalOfficersDto> appSvcClinicalDirectorDtos = dto.getAppSvcClinicalDirectorDtoList();
                validateClincalDirector.doValidateClincalDirector(errorMap, dto.getAppSvcClinicalDirectorDtoList(), currSvcCode);
                if (appSvcClinicalDirectorDtos != null && "Y".equals(prsFlag)) {
                    int i = 0;
                    for (AppSvcPrincipalOfficersDto person : appSvcClinicalDirectorDtos) {
                        if (!NewApplicationHelper.checkProfRegNo(person.getProfRegNo())) {
                            errorMap.put("profRegNo" + i, prsError);
                            break;
                        }
                        i++;
                    }
                }
            } else if (HcsaConsts.STEP_LABORATORY_DISCIPLINES.equals(currentStep)) {
                List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = dto.getAppSvcLaboratoryDisciplinesDtoList();
                List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList = dto.getAppSvcDisciplineAllocationDtoList();
                doSvcDisdolabory(errorMap, appSvcDisciplineAllocationDtoList, appSvcLaboratoryDisciplinesDtoList);
            } else if (HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS.equals(currentStep)) {
                Map<String, String> govenMap = IaisCommonUtils.genNewHashMap();
                List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = dto.getAppSvcCgoDtoList();
                doAppSvcCgoDto(currentSvcAllPsnConfig, govenMap, appSvcCgoDtoList);
                if (govenMap.isEmpty()) {
                    Map<String, AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(
                            bpc.request, NewApplicationDelegator.LICPERSONSELECTMAP);
                    govenMap.putAll(NewApplicationHelper.doValidateGovernanceOfficers(appSvcCgoDtoList, licPersonMap,
                            dto.getServiceCode()));
                }
                if (appSvcCgoDtoList != null && govenMap.isEmpty() && "Y".equals(prsFlag)) {
                    int i = 0;
                    for (AppSvcPrincipalOfficersDto person : appSvcCgoDtoList) {
                        if (!NewApplicationHelper.checkProfRegNo(person.getProfRegNo())) {
                            govenMap.put("profRegNo" + i, prsError);
                            break;
                        }
                        i++;
                    }
                }
                if (!govenMap.isEmpty()) {
                    errorMap.putAll(govenMap);
                    errorMap.put("CGO", "error");
                }
            } else if (HcsaConsts.STEP_SECTION_LEADER.equals(currentStep)) {
                // Section Leader
                Map<String, String> map = validateSectionLeaders(dto.getAppSvcSectionLeaderList());
                if (!map.isEmpty()) {
                    errorMap.putAll(map);
                    errorMap.put(ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER, "error");
                }
            } else if (HcsaConsts.STEP_DISCIPLINE_ALLOCATION.equals(currentStep)) {
                List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList = dto.getAppSvcDisciplineAllocationDtoList();
                doValidateDisciplineAllocation(errorMap, appSvcDisciplineAllocationDtoList, dto);
            } else if (HcsaConsts.STEP_CHARGES.equals(currentStep)) {
                validateCharges.doValidateCharges(errorMap, dto.getAppSvcChargesPageDto());
            } else if (HcsaConsts.STEP_SERVICE_PERSONNEL.equals(currentStep)) {
                List<AppSvcPersonnelDto> appSvcPersonnelDtoList = dto.getAppSvcPersonnelDtoList();
                doAppSvcPersonnelDtoList(currentSvcAllPsnConfig, errorMap, appSvcPersonnelDtoList, serviceId, sB,
                        dto.getServiceCode());
                if (appSvcPersonnelDtoList != null && "Y".equals(prsFlag)) {
                    int i = 0;
                    for (AppSvcPersonnelDto person : appSvcPersonnelDtoList) {
                        if (!NewApplicationHelper.checkProfRegNo(person.getProfRegNo())) {
                            errorMap.put("regnNo" + i, prsError);
                            break;
                        }
                        i++;
                    }
                }
            } else if (HcsaConsts.STEP_PRINCIPAL_OFFICERS.equals(currentStep)) {
                List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = dto.getAppSvcPrincipalOfficersDtoList();
                AppSubmissionDto appSubmissionDto = getAppSubmissionDto(bpc.request);
                doPO(currentSvcAllPsnConfig, errorMap, appSvcPrincipalOfficersDtoList, appSubmissionDto.getSubLicenseeDto(), serviceId, sB);
            } else if (HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER.equals(currentStep)) {
                List<AppSvcPrincipalOfficersDto> appSvcKeyAppointmentHolderList = dto.getAppSvcKeyAppointmentHolderDtoList();
                Map<String, AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(
                        bpc.request, NewApplicationDelegator.LICPERSONSELECTMAP);
                Map<String, String> map = NewApplicationHelper.doValidateKeyAppointmentHolder(appSvcKeyAppointmentHolderList, licPersonMap,
                        dto.getServiceCode());
                if (!map.isEmpty()) {
                    errorMap.putAll(map);
                    errorMap.put("KeyAppointmentHolder", "error");
                }
            } else if (HcsaConsts.STEP_MEDALERT_PERSON.equals(currentStep)) {
                List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = dto.getAppSvcMedAlertPersonList();
                Map<String, AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(
                        bpc.request, NewApplicationDelegator.LICPERSONSELECTMAP);
                Map<String, String> map = NewApplicationHelper.doValidateMedAlertPsn(appSvcMedAlertPersonList, licPersonMap,
                        dto.getServiceCode());
                if (!map.isEmpty()) {
                    errorMap.putAll(map);
                    errorMap.put("Medaler", "error");
                }
            } else if (HcsaConsts.STEP_DOCUMENTS.equals(currentStep)) {
                List<AppSvcDocDto> appSvcDocDtoLit = dto.getAppSvcDocDtoLit();
                doSvcDocument(errorMap, appSvcDocDtoLit, serviceId, sB, uploadFileLimit, sysFileType);
                List<HcsaSvcDocConfigDto> svcDocConfigDtos = IaisCommonUtils.genNewArrayList();
                List<HcsaSvcDocConfigDto> premServiceDocConfigDtos = IaisCommonUtils.genNewArrayList();
                List<HcsaSvcDocConfigDto> hcsaSvcDocDtos = serviceConfigService.getAllHcsaSvcDocs(dto.getServiceId());
                if (!IaisCommonUtils.isEmpty(hcsaSvcDocDtos)) {
                    for (HcsaSvcDocConfigDto hcsaSvcDocConfigDto : hcsaSvcDocDtos) {
                        if ("0".equals(hcsaSvcDocConfigDto.getDupForPrem())) {
                            svcDocConfigDtos.add(hcsaSvcDocConfigDto);
                        } else if ("1".equals(hcsaSvcDocConfigDto.getDupForPrem())) {
                            premServiceDocConfigDtos.add(hcsaSvcDocConfigDto);
                        }
                    }
                }
                Map<String, String> svcDocErrMap = IaisCommonUtils.genNewHashMap();
                NewApplicationHelper.svcDocMandatoryValidate(svcDocConfigDtos, dto.getAppSvcDocDtoLit(), appGrpPremisesDtos, dto,
                        svcDocErrMap);
                if (svcDocErrMap.size() > 0) {
                    errorMap.putAll(svcDocErrMap);
                    errorMap.put("svcDoc", "error");
                }
            }
        }
        if (!errorMap.isEmpty()) {
            sB.append(serviceId);
        }
        log.info(StringUtil.changeForLog("Error Message in doCheckBox for [" + dto.getServiceCode() + "] : " + errorMap));
        return errorMap;
    }

    /**
     * validate all related service infos mandatory count
     *
     * @param dto
     * @param psnType
     * @param mandatoryCount
     * @param sB
     * @param errorMap
     */
    private void valiatePersonnelCount(AppSvcRelatedInfoDto dto, String psnType, int mandatoryCount, StringBuilder sB,
            Map<String, String> errorMap) {
        String serviceId = dto.getServiceId();
        if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)) {
            validatePersonMandatoryCount(dto.getAppSvcPrincipalOfficersDtoList(), errorMap,
                    psnType, mandatoryCount, serviceId, sB);
        } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL.equals(psnType)) {
            validatePersonMandatoryCount(dto.getAppSvcPersonnelDtoList(), errorMap,
                    psnType, mandatoryCount, serviceId, sB);
        } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnType)) {
            validatePersonMandatoryCount(dto.getAppSvcCgoDtoList(), errorMap,
                    psnType, mandatoryCount, serviceId, sB);
        } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_MAP.equals(psnType)) {
            validatePersonMandatoryCount(dto.getAppSvcMedAlertPersonList(), errorMap,
                    psnType, mandatoryCount, serviceId, sB);
        } else if (ApplicationConsts.PERSONNEL_VEHICLES.equals(psnType)) {
            validatePersonMandatoryCount(dto.getAppSvcVehicleDtoList(), errorMap,
                    psnType, mandatoryCount, serviceId, sB);
        } else if (ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR.equals(psnType)) {
            validatePersonMandatoryCount(dto.getAppSvcClinicalDirectorDtoList(), errorMap,
                    psnType, mandatoryCount, serviceId, sB);
        } else if (ApplicationConsts.PERSONNEL_CHARGES.equals(psnType)) {
            List<AppSvcChargesDto> appSvcChargesDtos = Optional.ofNullable(dto.getAppSvcChargesPageDto())
                    .map(AppSvcChargesPageDto::getGeneralChargesDtos)
                    .orElseGet(() -> null);
            validatePersonMandatoryCount(appSvcChargesDtos, errorMap, psnType, mandatoryCount, serviceId, sB);
        } else if (ApplicationConsts.PERSONNEL_CHARGES_OTHER.equals(psnType)) {
            List<AppSvcChargesDto> otherChargesDtos = Optional.ofNullable(dto.getAppSvcChargesPageDto())
                    .map(AppSvcChargesPageDto::getOtherChargesDtos)
                    .orElseGet(() -> null);
            validatePersonMandatoryCount(otherChargesDtos, errorMap, psnType, mandatoryCount, serviceId, sB);
        } else if (ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER.equals(psnType)) {
            List<AppSvcPersonnelDto> sectionLeaderList = dto.getAppSvcSectionLeaderList();
            validatePersonMandatoryCount(sectionLeaderList, errorMap, psnType, mandatoryCount, serviceId, sB);
        } else if (ApplicationConsts.PERSONNEL_PSN_KAH.equals(psnType)) {
            List<AppSvcPrincipalOfficersDto> appSvcKeyAppointmentHolderDtoList = dto.getAppSvcKeyAppointmentHolderDtoList();
            validatePersonMandatoryCount(appSvcKeyAppointmentHolderDtoList, errorMap, psnType, mandatoryCount, serviceId, sB);
        }
    }

    private void validatePersonMandatoryCount(List<?> list, Map<String, String> map, String type, Integer mandatoryCount,
            String serviceId, StringBuilder sB) {
        if (list == null) {
            if (mandatoryCount > 0) {
                map.put("error" + type, type);
                sB.append(serviceId);
                log.info(StringUtil.changeForLog(type + " null"));
            }
        } else if (list.size() < mandatoryCount) {
            map.put("error" + type, type);
            sB.append(serviceId);
            log.info(StringUtil.changeForLog(type + " mandatoryCount"));
        }
    }

    @Override
    public List<AppGrpPrimaryDocDto> documentValid(HttpServletRequest request, Map<String, String> errorMap,boolean setIsPassValidate) {
        log.info(StringUtil.changeForLog("the do doValidatePremiss start ...."));
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = appSubmissionDto.getAppGrpPrimaryDocDtos();
        if (appGrpPrimaryDocDtoList == null) {
            return null;
        }
        for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtoList) {
            if(StringUtil.isEmpty(appGrpPrimaryDocDto.getMd5Code())){
                continue;
            }
            String keyName = "";
            if (StringUtil.isEmpty(appGrpPrimaryDocDto.getPremisessName()) && StringUtil.isEmpty(appGrpPrimaryDocDto.getPremisessType())) {
                //common
                keyName = "common" + appGrpPrimaryDocDto.getSvcComDocId();
            } else {
                keyName = "prem" + appGrpPrimaryDocDto.getSvcComDocId() + appGrpPrimaryDocDto.getPremisessName();
            }
            long length = appGrpPrimaryDocDto.getRealDocSize();
            int uploadFileLimit = systemParamConfig.getUploadFileLimit();
            if (length / 1024 / 1024 > uploadFileLimit) {
                errorMap.put(keyName, MessageUtil.replaceMessage("GENERAL_ERR0019", String.valueOf(uploadFileLimit), "sizeMax"));
                continue;
            }
            Boolean flag = Boolean.FALSE;
            String name = appGrpPrimaryDocDto.getDocName();
            if(name.length() > 100){
                errorMap.put(keyName,MessageUtil.getMessageDesc("GENERAL_ERR0022"));
            }
            String substring = name.substring(name.lastIndexOf('.') + 1);
            String sysFileType = systemParamConfig.getUploadFileType();
            String[] sysFileTypeArr = FileUtils.fileTypeToArray(sysFileType);
            for (String f : sysFileTypeArr) {
                if (f.equalsIgnoreCase(substring)) {
                    flag = Boolean.TRUE;
                }
            }
            if (!flag) {
                errorMap.put(keyName, MessageUtil.replaceMessage("GENERAL_ERR0018", sysFileType, "fileType"));
            }
            String errMsg = errorMap.get(keyName);
            if (StringUtil.isEmpty(errMsg) && setIsPassValidate) {
                appGrpPrimaryDocDto.setPassValidate(true);
            }
        }
        return appGrpPrimaryDocDtoList;
    }

    @Override
    public boolean isGiroAccount(String licenseeId) {
        boolean result = false;
        List<OrgGiroAccountInfoDto> orgGiroAccountInfoDtos = organizationLienceseeClient.getGiroAccByLicenseeId(licenseeId).getEntity();
        if(!IaisCommonUtils.isEmpty(orgGiroAccountInfoDtos)){
            result = true;
        }
        return result;
    }

    @Override
    public void removePreviousPremTypeInfo(AppSubmissionDto appSubmissionDto) throws CloneNotSupportedException {
        if(appSubmissionDto != null){
            List<String> svcIds = IaisCommonUtils.genNewArrayList();
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                    String svcId = appSvcRelatedInfoDto.getServiceId();
                    if(!StringUtil.isEmpty(svcId)){
                        svcIds.add(svcId);
                    }
                }
                if(svcIds.size() > 0){
                    Set<String> premisesType = serviceConfigService.getAppGrpPremisesTypeBySvcId(svcIds);
                    if(!IaisCommonUtils.isEmpty(appGrpPremisesDtos)){
                        List<AppGrpPremisesDto> newPremisesList = IaisCommonUtils.genNewArrayList();
                        for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                            if(premisesType.contains(appGrpPremisesDto.getPremisesType())){
                                AppGrpPremisesDto newPremisesDto = (AppGrpPremisesDto) CopyUtil.copyMutableObject(appGrpPremisesDto);
                                newPremisesList.add(newPremisesDto);
                            }
                        }
                        appSubmissionDto.setAppGrpPremisesDtoList(newPremisesList);
                        NewApplicationHelper.removePremiseEmptyAlignInfo(appSubmissionDto);
                    }
                }
            }
        }
    }
    /*
     *change new version subtype
     * -------------------------
     * version 1 subtype id is A
     * -------------------------
     * update to version 2 id is B
     * -------------------------
     * change licnece XXX (version 1 ) subtype id A -> B
    */
    @Override
    public void changeSvcScopeIdByConfigName(List<HcsaSvcSubtypeOrSubsumedDto> newConfigInfo,AppSubmissionDto appSubmissionDto) throws CloneNotSupportedException {
        log.debug(StringUtil.changeForLog("do changeSvcScopeIdByConfigName start ..."));
        log.debug(StringUtil.changeForLog("newConfigInfo size :"+ newConfigInfo.size()));
        Map<String, HcsaSvcSubtypeOrSubsumedDto> newSvcScopeNameMap = IaisCommonUtils.genNewHashMap();
        NewApplicationHelper.recursingSvcScopeKeyIsName(newConfigInfo,newSvcScopeNameMap);
        Map<String, HcsaSvcSubtypeOrSubsumedDto> newSvcScopeIdMap = IaisCommonUtils.genNewHashMap();
        NewApplicationHelper.recursingSvcScope(newConfigInfo,newSvcScopeIdMap);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                List<AppSvcLaboratoryDisciplinesDto> laboratoryDisciplinesDtos = appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
                List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtos = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
                if(!IaisCommonUtils.isEmpty(laboratoryDisciplinesDtos)){
                    for(AppSvcLaboratoryDisciplinesDto laboratoryDisciplinesDto:laboratoryDisciplinesDtos){
                        List<AppSvcChckListDto> svcScopeList = laboratoryDisciplinesDto.getAppSvcChckListDtoList();
                        if(!IaisCommonUtils.isEmpty(svcScopeList)){
                            List<String> svcScopeIdList = IaisCommonUtils.genNewArrayList();
                            for(AppSvcChckListDto svcScope:svcScopeList){
                                svcScopeIdList.add(svcScope.getChkLstConfId());
                            }
                            List<HcsaSvcSubtypeOrSubsumedDto> oldHcsaSvcSubtypeOrSubsumedDtos = serviceConfigService.getSvcSubtypeOrSubsumedByIdList(svcScopeIdList);
                            List<String> newSvcScopeIdList = IaisCommonUtils.genNewArrayList();
                            List<AppSvcChckListDto> newSvcScopeList = IaisCommonUtils.genNewArrayList();
                            for(AppSvcChckListDto svcScope:svcScopeList){
                                for(HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto:oldHcsaSvcSubtypeOrSubsumedDtos){
                                    if(svcScope.getChkLstConfId().equals(hcsaSvcSubtypeOrSubsumedDto.getId())){
                                        HcsaSvcSubtypeOrSubsumedDto newHcsaSvcSubtypeOrSubsumedDto = newSvcScopeNameMap.get(hcsaSvcSubtypeOrSubsumedDto.getName());
                                        if(NewApplicationConstant.PLEASEINDICATE.equals(hcsaSvcSubtypeOrSubsumedDto.getName())){
                                            AppSvcChckListDto newAppSvcChckListDto = (AppSvcChckListDto) CopyUtil.copyMutableObject(svcScope);
                                            newAppSvcChckListDto.setChkLstConfId(newHcsaSvcSubtypeOrSubsumedDto.getId());
                                            newAppSvcChckListDto.setChkLstType(newHcsaSvcSubtypeOrSubsumedDto.getType());
                                            newAppSvcChckListDto.setChkName(newHcsaSvcSubtypeOrSubsumedDto.getName());
                                            newAppSvcChckListDto.setParentName(newHcsaSvcSubtypeOrSubsumedDto.getParentId());
                                            newAppSvcChckListDto.setChildrenName(newHcsaSvcSubtypeOrSubsumedDto.getChildrenId());
                                            newSvcScopeList.add(newAppSvcChckListDto);
                                            newSvcScopeIdList.add(newHcsaSvcSubtypeOrSubsumedDto.getId());
                                        }
                                        if(newHcsaSvcSubtypeOrSubsumedDto != null){
                                            recursingChooseLabUpward(newSvcScopeIdMap,newHcsaSvcSubtypeOrSubsumedDto.getId(),newSvcScopeIdList,newSvcScopeList);
                                            break;
                                        }
                                    }
                                }
                            }
                            laboratoryDisciplinesDto.setAppSvcChckListDtoList(newSvcScopeList);
                        }
                    }
                }
                if(!IaisCommonUtils.isEmpty(appSvcDisciplineAllocationDtos)){
                    List<String> svcScopeIdList = IaisCommonUtils.genNewArrayList();
                    List<AppSvcDisciplineAllocationDto> newDisciplineAllocationDtoList = IaisCommonUtils.genNewArrayList();
                    for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto:appSvcDisciplineAllocationDtos){
                        svcScopeIdList.add(appSvcDisciplineAllocationDto.getChkLstConfId());
                    }
                    List<HcsaSvcSubtypeOrSubsumedDto> oldHcsaSvcSubtypeOrSubsumedDtos = serviceConfigService.getSvcSubtypeOrSubsumedByIdList(svcScopeIdList);
                    for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto:appSvcDisciplineAllocationDtos){
                        for(HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto:oldHcsaSvcSubtypeOrSubsumedDtos){
                            if(appSvcDisciplineAllocationDto.getChkLstConfId().equals(hcsaSvcSubtypeOrSubsumedDto.getId())){
                                HcsaSvcSubtypeOrSubsumedDto newHcsaSvcSubtypeOrSubsumedDto = newSvcScopeNameMap.get(hcsaSvcSubtypeOrSubsumedDto.getName());
                                if(newHcsaSvcSubtypeOrSubsumedDto != null){
                                    AppSvcDisciplineAllocationDto newDisciplineAllocationDto = (AppSvcDisciplineAllocationDto) CopyUtil.copyMutableObject(appSvcDisciplineAllocationDto);
                                    newDisciplineAllocationDto.setChkLstConfId(newHcsaSvcSubtypeOrSubsumedDto.getId());
                                    newDisciplineAllocationDto.setChkLstName(newHcsaSvcSubtypeOrSubsumedDto.getName());
                                    newDisciplineAllocationDtoList.add(newDisciplineAllocationDto);
                                }
                            }
                        }
                    }
                    appSvcRelatedInfoDto.setAppSvcDisciplineAllocationDtoList(newDisciplineAllocationDtoList);
                }
            }
            appSubmissionDto.setAppSvcRelatedInfoDtoList(appSvcRelatedInfoDtos);
        }
        log.debug(StringUtil.changeForLog("do changeSvcScopeIdByConfigName end ..."));
    }

    //for preview get one svc's DisciplineAllocation
    @Override
    public HashMap<String, List<AppSvcDisciplineAllocationDto>> getDisciplineAllocationDtoList(AppSubmissionDto appSubmissionDto, String svcId) throws CloneNotSupportedException {
        log.info(StringUtil.changeForLog("get DisciplineAllocationDtoList start..."));
        if(appSubmissionDto == null || StringUtil.isEmpty(svcId)){
            return null;
        }
        log.info(StringUtil.changeForLog(svcId+"svcId"));
        AppSvcRelatedInfoDto appSvcRelatedInfoDto = null;
        if(!IaisCommonUtils.isEmpty(appSubmissionDto.getAppSvcRelatedInfoDtoList())){
            for(AppSvcRelatedInfoDto item:appSubmissionDto.getAppSvcRelatedInfoDtoList()){
                log.info(StringUtil.changeForLog(item.getServiceId()+"item.getServiceId()"));
                if(svcId.equals(item.getServiceId())){
                    appSvcRelatedInfoDto = item;
                    break;
                }
            }
        }
        List<AppSvcDisciplineAllocationDto> allocationDto = null;
        if(appSvcRelatedInfoDto != null){
            allocationDto = appSvcRelatedInfoDto.getAppSvcDisciplineAllocationDtoList();
        }else{
            log.info(StringUtil.changeForLog("can not found the match appSvcRelatedInfoDto ..."));
            return null;
        }
        List<HcsaSvcSubtypeOrSubsumedDto> svcScopeDtoList = serviceConfigService.loadLaboratoryDisciplines(svcId);
        Map<String, HcsaSvcSubtypeOrSubsumedDto> svcScopeAlignMap = IaisCommonUtils.genNewHashMap();
        NewApplicationHelper.recursingSvcScope(svcScopeDtoList,svcScopeAlignMap);

        List<AppGrpPremisesDto> appGrpPremisesDtoList = appSubmissionDto.getAppGrpPremisesDtoList();
        HashMap<String,List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = IaisCommonUtils.genNewHashMap();
        for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
            List<AppSvcDisciplineAllocationDto> reloadDisciplineAllocation = IaisCommonUtils.genNewArrayList();
            String premisesIndexNo = appGrpPremisesDto.getPremisesIndexNo();
            //get curr premises's appSvcChckListDto
            List<AppSvcChckListDto> appSvcChckListDtoList = IaisCommonUtils.genNewArrayList();
            List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList =appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
            List<AppSvcPrincipalOfficersDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
            List<AppSvcPersonnelDto> appSlList = appSvcRelatedInfoDto.getAppSvcSectionLeaderList();
            if(!IaisCommonUtils.isEmpty(appSvcLaboratoryDisciplinesDtoList) && !StringUtil.isEmpty(premisesIndexNo)){
                log.info(StringUtil.changeForLog("appSvcLaboratoryDisciplinesDtoList size:"+appSvcLaboratoryDisciplinesDtoList.size()));
                for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto:appSvcLaboratoryDisciplinesDtoList){
                    if(premisesIndexNo.equals(appSvcLaboratoryDisciplinesDto.getPremiseVal())){
                        appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                        break;
                    }
                }
                log.debug(StringUtil.changeForLog("appSvcChckListDtoList size:"+appSvcChckListDtoList.size()));
                List<AppSvcChckListDto> newAppSvcChckListDtos = NewApplicationHelper.handlerPleaseIndicateLab(appSvcChckListDtoList,svcScopeAlignMap);
                log.debug(StringUtil.changeForLog("newAppSvcChckListDtos size:"+appSvcChckListDtoList.size()));
                for(AppSvcChckListDto appSvcChckListDto:newAppSvcChckListDtos){
                    AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto = null;
                    String chkSvcId = appSvcChckListDto.getChkLstConfId();
                    if(!StringUtil.isEmpty(chkSvcId) && !IaisCommonUtils.isEmpty(allocationDto)){
                        log.info(StringUtil.changeForLog("allocationDto size:"+allocationDto.size()));
                        for(AppSvcDisciplineAllocationDto allocation:allocationDto){
                            if(premisesIndexNo.equals(allocation.getPremiseVal()) && chkSvcId.equals(allocation.getChkLstConfId())){
                                log.info(StringUtil.changeForLog("set chkName ..."));
                                appSvcDisciplineAllocationDto = (AppSvcDisciplineAllocationDto) CopyUtil.copyMutableObject(allocation);
                                //set chkName
                                String chkName = appSvcChckListDto.getChkName();
                                if(NewApplicationConstant.PLEASEINDICATE.equals(chkName)){
                                    appSvcDisciplineAllocationDto.setChkLstName(appSvcChckListDto.getOtherScopeName());
                                }else{
                                    appSvcDisciplineAllocationDto.setChkLstName(chkName);
                                }
                                //set selCgoName
                                String idNo = allocation.getIdNo();
                                if(!IaisCommonUtils.isEmpty(appSvcCgoDtoList) && !StringUtil.isEmpty(idNo)){
                                    for(AppSvcPrincipalOfficersDto appSvcCgoDto:appSvcCgoDtoList){
                                        if(idNo.equals(appSvcCgoDto.getIdNo())){
                                            log.info(StringUtil.changeForLog("set cgoSel ..."));
                                            appSvcDisciplineAllocationDto.setCgoSelName(appSvcCgoDto.getName());
                                            break;
                                        }
                                    }
                                }
                                if (!IaisCommonUtils.isEmpty(appSlList) && !StringUtil.isEmpty(allocation.getSlIndex())) {
                                    for (AppSvcPersonnelDto avpd : appSlList) {
                                        if(allocation.getSlIndex().equals(avpd.getIndexNo())){
                                            appSvcDisciplineAllocationDto.setSectionLeaderName(avpd.getName());
                                            break;
                                        }
                                    }
                                }
                                break;
                            }
                        }
                        if(appSvcDisciplineAllocationDto == null){
                            log.info(StringUtil.changeForLog("new AppSvcDisciplineAllocationDto"));
                            appSvcDisciplineAllocationDto = new AppSvcDisciplineAllocationDto();
                            appSvcDisciplineAllocationDto.setPremiseVal(premisesIndexNo);
                            appSvcDisciplineAllocationDto.setChkLstConfId(appSvcChckListDto.getChkLstConfId());
                            appSvcDisciplineAllocationDto.setChkLstName(appSvcChckListDto.getChkName());
                        }
                        reloadDisciplineAllocation.add(appSvcDisciplineAllocationDto);
                    }
                }
                reloadDisciplineAllocationMap.put(premisesIndexNo, reloadDisciplineAllocation);
            }
        }
        log.info(StringUtil.changeForLog("get DisciplineAllocationDtoList end..."));
        return reloadDisciplineAllocationMap;
    }

    @Override
    public void setPreviewDta(AppSubmissionDto appSubmissionDto, BaseProcessClass bpc) throws CloneNotSupportedException {
        if(appSubmissionDto != null){
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                String svcId = (String) ParamUtil.getSessionAttr(bpc.request,"SvcId");
                ParamUtil.setRequestAttr(bpc.request, "currentPreviewSvcInfo", appSvcRelatedInfoDtos.get(0));
                Map<String,List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap= appSubmissionService.getDisciplineAllocationDtoList(appSubmissionDto,svcId);
                ParamUtil.setRequestAttr(bpc.request, "reloadDisciplineAllocationMap", (Serializable) reloadDisciplineAllocationMap);
                //PO/DPO
                if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
                    NewApplicationHelper.setPreviewPo(appSvcRelatedInfoDtos.get(0),bpc.request);
                }
            }
            AppEditSelectDto appEditSelectDto = new AppEditSelectDto();
            boolean licenseeEdit = Optional.ofNullable(appSubmissionDto.getSubLicenseeDto())
                    .map(SubLicenseeDto::getLicenseeType)
                    .filter(licenseeType -> StringUtil.isEmpty(licenseeType) ||
                            OrganizationConstants.LICENSEE_SUB_TYPE_INDIVIDUAL.equals(licenseeType))
                    .isPresent();
            appEditSelectDto.setLicenseeEdit(licenseeEdit);
            appEditSelectDto.setPremisesEdit(true);
            appEditSelectDto.setDocEdit(true);
            appEditSelectDto.setServiceEdit(true);
            appSubmissionDto.setAppEditSelectDto(appEditSelectDto);
        }
    }

    public void  updateInboxMsgStatus(String eventRefNum ,String submissionId){
        log.debug("---updateInboxMsgStatus start---------------");
        if( !StringUtil.isEmpty(eventRefNum)) {
            log.debug(StringUtil.changeForLog("--------------- releaseTimeForInsUserCallBack eventRefNum :" + eventRefNum + " submissionId :" + submissionId + "--------------------"));
            EicRequestTrackingDto eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eventRefNum).getEntity();
            String jsonObj = eicRequestTrackingDto.getDtoObject();
            if(!StringUtil.isEmpty(jsonObj)){
                AppSubmissionDto appSubmissionDto = JsonUtil.parseToObject(jsonObj,AppSubmissionDto.class);
                if(appSubmissionDto != null){
                    String rfiMsgId = appSubmissionDto.getRfiMsgId();
                    log.debug(StringUtil.changeForLog("rfiMsgId:"+rfiMsgId));
                    feMessageClient.updateMsgStatus(rfiMsgId,MessageConstants.MESSAGE_STATUS_RESPONSE);
                }
            }else{
                log.debug(StringUtil.changeForLog("jsonObj is empty"));
            }
        }
        log.debug("---updateInboxMsgStatus end---------------");
    }

    private static void doSvcDocument(Map<String, String> map, List<AppSvcDocDto> appSvcDocDtoLit, String serviceId, StringBuilder sB, int uploadFileLimit, String sysFileType) {
        if (appSvcDocDtoLit != null) {
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtoLit) {
                Integer docSize = appSvcDocDto.getDocSize();
                String docName = appSvcDocDto.getDocName();
                if(docName==null){
                    continue;
                }
                Boolean flag = Boolean.FALSE;
                String substring = docName.substring(docName.lastIndexOf('.') + 1);
                if (docSize/1024 > uploadFileLimit) {
                    sB.append(serviceId);
                    map.put("svcDocError","error");
                }

                if(docName.length() > 100){
                    map.put("svcDocError","error");
                }

                String[] sysFileTypeArr = FileUtils.fileTypeToArray(sysFileType);
                for (String f : sysFileTypeArr) {
                    if (f.equalsIgnoreCase(substring)) {
                        flag = Boolean.TRUE;
                    }
                }
                if (!flag) {
                    sB.append(serviceId);
                    map.put("svcDocError","error");
                }
            }


        }


    }

    private static void doAppSvcPersonnelDtoList(List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos, Map map, List<AppSvcPersonnelDto> appSvcPersonnelDtos, String serviceId, StringBuilder sB,String svcCode) {
        if (appSvcPersonnelDtos == null) {
            if (hcsaSvcPersonnelDtos != null) {
                for (HcsaSvcPersonnelDto every : hcsaSvcPersonnelDtos) {
                    String psnType = every.getPsnType();
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL.equals(psnType)) {
                        sB.append(serviceId);
                        return;
                    }
                }
            }

            return;
        }

        boolean flag = false;
        String errName = MessageUtil.replaceMessage("GENERAL_ERR0006","Name","field");
        String errDesignation = MessageUtil.replaceMessage("GENERAL_ERR0006","Designation","field");
        String errRegnNo = MessageUtil.replaceMessage("GENERAL_ERR0006","Professional Regn. No.","field");
        String errWrkExpYear = MessageUtil.replaceMessage("GENERAL_ERR0006","Relevant working experience (Years)","field");
        String errQualification = MessageUtil.replaceMessage("GENERAL_ERR0006","Qualification","field");
        String errSelSvcPsnel = MessageUtil.replaceMessage("GENERAL_ERR0006","Select Service Personnel","field");
        for (int i = 0; i < appSvcPersonnelDtos.size(); i++) {
            if (AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode)) {
                String designation = appSvcPersonnelDtos.get(i).getDesignation();
                if (StringUtil.isEmpty(designation)) {
                    map.put("designation" + i, errDesignation);
                    flag = true;
                }
                String name = appSvcPersonnelDtos.get(i).getName();
                if (StringUtil.isEmpty(name)) {
                    map.put("name" + i, errName);
                    flag = true;
                }
                String profRegNo = appSvcPersonnelDtos.get(i).getProfRegNo();
                if (StringUtil.isEmpty(profRegNo)) {
                    map.put("regnNo" + i, errRegnNo);
                    flag = true;
                }
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                if (StringUtil.isEmpty(wrkExpYear)) {
                    map.put("wrkExpYear" + i, errWrkExpYear);
                    flag = true;
                } else {
                    if (!wrkExpYear.matches("^[0-9]*$")) {
                        map.put("wrkExpYear" + i, "GENERAL_ERR0002");
                        flag = true;
                    }
                }
            } else if (AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(svcCode)) {
                String name = appSvcPersonnelDtos.get(i).getName();
                if (StringUtil.isEmpty(name)) {
                    map.put("name" + i, errName);
                    flag = true;
                }
                String quaification = appSvcPersonnelDtos.get(i).getQualification();
                if (StringUtil.isEmpty(quaification)) {
                    map.put("qualification" + i, errQualification);
                    flag = true;
                }
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                if (StringUtil.isEmpty(wrkExpYear)) {
                    map.put("wrkExpYear" + i, errWrkExpYear);
                    flag = true;
                } else {
                    if (!wrkExpYear.matches("^[0-9]*$")) {
                        map.put("wrkExpYear" + i, "GENERAL_ERR0002");
                        flag = true;
                    }
                }
            }else if(!AppServicesConsts.SERVICE_CODE_TISSUE_BANKING.equals(svcCode)
                    && !AppServicesConsts.SERVICE_CODE_BLOOD_BANKING.equals(svcCode)
                    && !AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_IMAGING.equals(svcCode)
                    && !AppServicesConsts.SERVICE_CODE_NUCLEAR_MEDICINE_ASSAY.equals(svcCode)){
                String name = appSvcPersonnelDtos.get(i).getName();
                String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                String quaification = appSvcPersonnelDtos.get(i).getQualification();
                if (StringUtil.isEmpty(name)) {
                    map.put("name" + i, errName);
                    flag = true;
                }
                if (StringUtil.isEmpty(wrkExpYear)) {
                    map.put("wrkExpYear" + i, errWrkExpYear);
                    flag = true;
                } else {
                    if (!wrkExpYear.matches("^[0-9]*$")) {
                        map.put("wrkExpYear" + i, "GENERAL_ERR0002");
                        flag = true;
                    }
                }
                if (StringUtil.isEmpty(quaification)) {
                    map.put("quaification" + i, errQualification);
                    flag = true;
                }
            } else {
                String personnelSel = appSvcPersonnelDtos.get(i).getPersonnelType();
                if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_REGISTERED_NURSE.equals(personnelSel)) {
                    String profRegNo = appSvcPersonnelDtos.get(i).getProfRegNo();
                    String name = appSvcPersonnelDtos.get(i).getName();
                    if (StringUtil.isEmpty(name)) {
                        map.put("name" + i, errName);
                        flag = true;
                    }
                    if (StringUtil.isEmpty(profRegNo)) {
                        map.put("regnNo" + i, errRegnNo);
                        flag = true;
                    }
                } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIOLOGY_PROFESSIONAL.equals(personnelSel)) {
                    String name = appSvcPersonnelDtos.get(i).getName();
                    String designation = appSvcPersonnelDtos.get(i).getDesignation();
                    String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                    String qualification = appSvcPersonnelDtos.get(i).getQualification();

                    if (StringUtil.isEmpty(name)) {
                        map.put("name" + i, errName);
                        flag = true;
                    }
                    if (StringUtil.isEmpty(designation)) {
                        map.put("designation" + i, errDesignation);
                        flag = true;
                    }
                    if (StringUtil.isEmpty(wrkExpYear)) {
                        map.put("wrkExpYear" + i, errWrkExpYear);
                        flag = true;
                    } else {
                        if (!wrkExpYear.matches("^[0-9]*$")) {
                            map.put("wrkExpYear" + i, "GENERAL_ERR0002");
                            flag = true;
                        }
                    }
                    if (StringUtil.isEmpty(qualification)) {
                        map.put("qualification" + i, errQualification);
                        flag = true;
                    }
                } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_MEDICAL_PHYSICIST.equals(personnelSel)) {
                    String name = appSvcPersonnelDtos.get(i).getName();
                    String wrkExpYear = appSvcPersonnelDtos.get(i).getWrkExpYear();
                    String quaification = appSvcPersonnelDtos.get(i).getQualification();
                    if (StringUtil.isEmpty(name)) {
                        map.put("name" + i, errName);
                        flag = true;
                    }
                    if (StringUtil.isEmpty(wrkExpYear)) {
                        map.put("wrkExpYear" + i, errWrkExpYear);
                        flag = true;
                    } else {
                        if (!wrkExpYear.matches("^[0-9]*$")) {
                            map.put("wrkExpYear" + i, "GENERAL_ERR0002");
                            flag = true;
                        }
                    }
                    if (StringUtil.isEmpty(quaification)) {
                        map.put("quaification" + i, errQualification);
                        flag = true;
                    }
                } else if (ApplicationConsts.SERVICE_PERSONNEL_PSN_TYPE_RADIATION_SAFETY_OFFICER.equals(personnelSel)) {
                    String name = appSvcPersonnelDtos.get(i).getName();
                    if (StringUtil.isEmpty(name)) {
                        map.put("name" + i, errName);
                        flag = true;
                    }
                }
            }

            if (flag) {
                sB.append(serviceId);
            }
        }

    }

    private static void doAppSvcCgoDto(List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos, Map map, List<AppSvcPrincipalOfficersDto> list) {
        if (list == null) {
            if (hcsaSvcPersonnelDtos != null) {
                for (HcsaSvcPersonnelDto every : hcsaSvcPersonnelDtos) {
                    String psnType = every.getPsnType();
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnType)) {
                        log.info("PERSONNEL_PSN_TYPE_CGO null");
                        map.put(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO, "CGO can't be null");
                        return;
                    }
                }
            }
            return;
        }

        for (int i = 0; i < list.size(); i++) {
            String assignSelect = list.get(i).getAssignSelect();
            if ("".equals(assignSelect) || assignSelect == null) {
                map.put("cgoassignSelect" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","cgoassignSelect","field"));
            }
            String idType = list.get(i).getIdType();
            if (StringUtil.isEmpty(idType)) {
                map.put("cgotype" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","cgotype","field"));
            }
            String mobileNo = list.get(i).getMobileNo();
            if (StringUtil.isEmpty(mobileNo)) {
                map.put("cgomobileNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","cgomobileNo","field"));
            } else {
                if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                    map.put("cgomobileNo" + i, "GENERAL_ERR0007");
                }
            }
            String emailAddr = list.get(i).getEmailAddr();
            if (StringUtil.isEmpty(emailAddr)) {
                map.put("cgoemailAddr" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","cgoemailAddr","field"));
            } else {
                if (!ValidationUtils.isEmail(emailAddr)) {
                    map.put("cgoemailAddr" + i, "GENERAL_ERR0014");
                }
            }
        }
    }

    private static void doSvcDisdolabory(Map map, List<AppSvcDisciplineAllocationDto> appSvcDislist, List<AppSvcLaboratoryDisciplinesDto> appSvclaborlist) {
        if (appSvclaborlist == null || appSvclaborlist.isEmpty()) {
            return;
        } else if (appSvclaborlist != null && !appSvclaborlist.isEmpty()) {
            List<AppSvcChckListDto> appSvcChckListDtoList = IaisCommonUtils.genNewArrayList();
            for (AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : appSvclaborlist) {
                appSvcChckListDtoList.addAll(appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList());
            }

            if (appSvcChckListDtoList != null) {
                if (appSvcDislist == null) {
                    map.put("appSvcDislist", "appSvcDislist null");
                    return;
                } else {
                  /*  if( appSvcChckListDtoList.size()!=appSvcDislist.size()){
                        log.info(appSvcChckListDtoList.size()+" appSvcChckListDtoList ");
                        log.info(appSvcDislist.size()+" appSvcDislist ");
                        map.put("appSvcChckListDtoListsize","size");
                        sB.append(serviceId);
                        return;
                    }
                    */
                }
            }
        }
    }

    private static void doPO(List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos, Map oneErrorMap, List<AppSvcPrincipalOfficersDto> poDto, SubLicenseeDto subLicenseeDto, String serviceId, StringBuilder sB) {
        if (poDto == null) {
            log.info("podto is null");
            if (hcsaSvcPersonnelDtos != null) {
                for (HcsaSvcPersonnelDto every : hcsaSvcPersonnelDtos) {
                    String psnType = every.getPsnType();
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)) {
                        sB.append(serviceId);
                        return;
                    }
                }
            }

            return;
        }


        boolean flag = false;
        int poIndex = 0;
        int dpoIndex = 0;
        List<String> stringList = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < poDto.size(); i++) {
            String psnType = poDto.get(i).getPsnType();
            if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)) {

                StringBuilder stringBuilder = new StringBuilder(10);

                String assignSelect = poDto.get(i).getAssignSelect();
                if ("-1".equals(assignSelect)) {
                    oneErrorMap.put("assignSelect" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","assignSelect","field"));
                } else {
                    //do by wenkang
                    String mobileNo = poDto.get(i).getMobileNo();
                    String officeTelNo = poDto.get(i).getOfficeTelNo();
                    String emailAddr = poDto.get(i).getEmailAddr();
                    String idNo = poDto.get(i).getIdNo();
                    String name = poDto.get(i).getName();
                    String salutation = poDto.get(i).getSalutation();
                    String designation = poDto.get(i).getDesignation();
                    String idType = poDto.get(i).getIdType();

                    if ("-1".equals(idType)) {
                        oneErrorMap.put("idType" + poIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","idType","field"));
                    }
                    if (StringUtil.isEmpty(name)) {
                        oneErrorMap.put("name" + poIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","name","field"));
                    } else if (name.length() > 66) {

                    }
                    if (StringUtil.isEmpty(salutation)) {
                        oneErrorMap.put("salutation" + poIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","salutation","field"));
                    }
                    if (StringUtil.isEmpty(designation)) {
                        oneErrorMap.put("designation" + poIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","designation","field"));
                    }
                    if (!StringUtil.isEmpty(idNo)) {
                        if (OrganizationConstants.ID_TYPE_FIN.equals(idType)) {
                            boolean b = SgNoValidator.validateFin(idNo);
                            if (!b) {
                                oneErrorMap.put("NRICFIN", "GENERAL_ERR0008");
                            } else {
                                stringBuilder.append(idType).append(idNo);

                            }
                        }
                        if (OrganizationConstants.ID_TYPE_NRIC.equals(idType)) {
                            boolean b1 = SgNoValidator.validateNric(idNo);
                            if (!b1) {
                                oneErrorMap.put("NRICFIN", "GENERAL_ERR0008");
                            } else {
                                stringBuilder.append(idType).append(idNo);

                            }
                        }
                    } else {
                        oneErrorMap.put("NRICFIN", MessageUtil.replaceMessage("GENERAL_ERR0006","NRICFIN","field"));
                    }
                    if (!StringUtil.isEmpty(mobileNo)) {

                        if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                            oneErrorMap.put("mobileNo" + poIndex, "GENERAL_ERR0007");
                        }
                    } else {
                        oneErrorMap.put("mobileNo" + poIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","mobileNo","field"));
                    }
                    if (!StringUtil.isEmpty(emailAddr)) {
                        if (!ValidationUtils.isEmail(emailAddr)) {
                            oneErrorMap.put("emailAddr" + poIndex, "GENERAL_ERR0014");
                        } else if (emailAddr.length() > 66) {

                        }
                    } else {
                        oneErrorMap.put("emailAddr" + poIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","emailAddr","field"));
                    }
                    if (!StringUtil.isEmpty(officeTelNo)) {
                        if (!officeTelNo.matches(IaisEGPConstant.OFFICE_TELNO_MATCH)) {
                            oneErrorMap.put("officeTelNo" + poIndex, "GENERAL_ERR0015");
                        }
                    } else {
                        oneErrorMap.put("officeTelNo" + poIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","officeTelNo","field"));
                    }
                }
                poIndex++;
                String s = stringBuilder.toString();

                if (stringList.contains(s)) {

                    oneErrorMap.put("NRICFIN", "NEW_ERR0012");

                } else {
                    stringList.add(stringBuilder.toString());
                }
            }

            if (ApplicationConsts.PERSONNEL_PSN_TYPE_DPO.equals(psnType)) {
                StringBuilder stringBuilder = new StringBuilder(10);
                String salutation = poDto.get(i).getSalutation();
                String name = poDto.get(i).getName();
                String idType = poDto.get(i).getIdType();
                String mobileNo = poDto.get(i).getMobileNo();
                String emailAddr = poDto.get(i).getEmailAddr();
                String idNo = poDto.get(i).getIdNo();
                String designation = poDto.get(i).getDesignation();
                String officeTelNo = poDto.get(i).getOfficeTelNo();

                if (StringUtil.isEmpty(designation) || "-1".equals(designation)) {
                    oneErrorMap.put("deputyDesignation" + dpoIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","deputyDesignation","field"));
                }
                if (StringUtil.isEmpty(salutation) || "-1".equals(salutation)) {
                    oneErrorMap.put("deputySalutation" + dpoIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","deputySalutation","field"));
                }

                if (StringUtil.isEmpty(idType) || "-1".equals(idType)) {
                    oneErrorMap.put("deputyIdType" + dpoIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","deputyIdType","field"));
                }
                if (StringUtil.isEmpty(name)) {
                    oneErrorMap.put("deputyName" + dpoIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","deputyName","field"));
                } else if (name.length() > 66) {

                }
                if (StringUtil.isEmpty(officeTelNo)) {
                    oneErrorMap.put("deputyofficeTelNo" + dpoIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","deputyofficeTelNo","field"));
                } else {
                    if (!officeTelNo.matches(IaisEGPConstant.OFFICE_TELNO_MATCH)) {
                        oneErrorMap.put("deputyofficeTelNo" + dpoIndex, "GENERAL_ERR0015");
                    }
                }
                if (StringUtil.isEmpty(idNo)) {
                    oneErrorMap.put("deputyIdNo" + dpoIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","deputyIdNo","field"));
                }
                if ("FIN".equals(idType)) {
                    boolean b = SgNoValidator.validateFin(idNo);
                    if (!b) {
                        oneErrorMap.put("deputyIdNo" + dpoIndex, "GENERAL_ERR0008");
                    } else {
                        stringBuilder.append(idType).append(idNo);
                    }
                }
                if ("NRIC".equals(idType)) {
                    boolean b1 = SgNoValidator.validateNric(idNo);
                    if (!b1) {
                        oneErrorMap.put("deputyIdNo" + dpoIndex, "GENERAL_ERR0008");
                    } else {
                        stringBuilder.append(idType).append(idNo);
                    }
                }

                if (StringUtil.isEmpty(mobileNo)) {
                    oneErrorMap.put("deputyMobileNo" + dpoIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","deputyMobileNo","field"));
                } else {
                    if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                        oneErrorMap.put("deputyMobileNo" + dpoIndex, "GENERAL_ERR0007");
                    }
                }
                if (StringUtil.isEmpty(emailAddr)) {
                    oneErrorMap.put("deputyEmailAddr" + dpoIndex, MessageUtil.replaceMessage("GENERAL_ERR0006","deputyEmailAddr","field"));
                } else {
                    if (!ValidationUtils.isEmail(emailAddr)) {
                        oneErrorMap.put("deputyEmailAddr" + dpoIndex, "GENERAL_ERR0014");
                    } else if (emailAddr.length() > 66) {

                    }
                }
                if (subLicenseeDto != null){
                    String subLicenseeIdType = subLicenseeDto.getIdType();
                    String subLicenseeIdNumber = subLicenseeDto.getIdNumber();
                    if (StringUtil.isNotEmpty(subLicenseeIdType) && StringUtil.isNotEmpty(subLicenseeIdNumber)) {
                        if (subLicenseeIdType.equals(idType) && subLicenseeIdNumber.equals(idNo)) {
                            oneErrorMap.put("conflictError" + dpoIndex, MessageUtil.getMessageDesc("NEW_ERR0034"));
                        }
                    }
                }
                dpoIndex++;

                String s = stringBuilder.toString();

                if (stringList.contains(s) && !StringUtil.isEmpty(s)) {

                    oneErrorMap.put("NRICFIN", "NEW_ERR0012");

                } else {
                    stringList.add(stringBuilder.toString());
                }
            }


        }
        if (flag) {
            sB.append(serviceId);
        }
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(oneErrorMap) + "oneErrorMap"));
    }

    private void doCommomDocument(HttpServletRequest request, Map<String, String> documentMap) {
        AppSubmissionDto appSubmissionDto = getAppSubmissionDto(request);

        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList = appSubmissionDto.getAppGrpPrimaryDocDtos();
        List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigList = (List<HcsaSvcDocConfigDto>) request.getSession().getAttribute(NewApplicationDelegator.COMMONHCSASVCDOCCONFIGDTO);
        if (commonHcsaSvcDocConfigList == null) {
            List<HcsaSvcDocConfigDto> hcsaSvcDocDtos;
            boolean isRfi = NewApplicationHelper.checkIsRfi(request);
            List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
            if(isRfi && appGrpPrimaryDocDtos != null && appGrpPrimaryDocDtos.size() > 0){
                hcsaSvcDocDtos = serviceConfigService.getPrimaryDocConfigById(appGrpPrimaryDocDtos.get(0).getSvcComDocId());
            }else{
                hcsaSvcDocDtos = serviceConfigService.getAllHcsaSvcDocs(null);
            }
            if (hcsaSvcDocDtos != null) {
                List<HcsaSvcDocConfigDto> commonHcsaSvcDocConfigDto  = IaisCommonUtils.genNewArrayList();
                for (HcsaSvcDocConfigDto hcsaSvcDocConfigDto : hcsaSvcDocDtos) {
                    if ("0".equals(hcsaSvcDocConfigDto.getDupForPrem())) {
                        commonHcsaSvcDocConfigDto.add(hcsaSvcDocConfigDto);
                    }
                }
                commonHcsaSvcDocConfigList = commonHcsaSvcDocConfigDto;
            } else {
                return;
            }
        }
        for (HcsaSvcDocConfigDto comm : commonHcsaSvcDocConfigList) {
            String name = "common" + comm.getId();

            Boolean isMandatory = comm.getIsMandatory();
            String err006 = MessageUtil.replaceMessage("GENERAL_ERR0006", "Document", "field");
            if (isMandatory && appGrpPrimaryDocDtoList == null || isMandatory && appGrpPrimaryDocDtoList.isEmpty()) {
                documentMap.put(name, err006);
            } else if (isMandatory && !appGrpPrimaryDocDtoList.isEmpty()) {
                Boolean flag = Boolean.FALSE;
                for (AppGrpPrimaryDocDto appGrpPrimaryDocDto : appGrpPrimaryDocDtoList) {
                    if(StringUtil.isEmpty(appGrpPrimaryDocDto.getMd5Code())){
                        continue;
                    }
                    String svcComDocId = appGrpPrimaryDocDto.getSvcComDocId();
                    if(!comm.getId().equals(svcComDocId)&&comm.getDocTitle().equals(appGrpPrimaryDocDto.getSvcComDocName())){
                        appGrpPrimaryDocDto.setSvcComDocId(comm.getId());
                        svcComDocId=comm.getId();
                    }
                    if (comm.getId().equals(svcComDocId)) {
                        flag = Boolean.TRUE;
                        break;
                    }
                }
                if (!flag) {
                    documentMap.put(name, err006);
                }
            }
        }

    }

    private AppSvcRelatedInfoDto getAppSvcRelatedInfoDto(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos){
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            return appSvcRelatedInfoDtos.get(0);
        }
        return new AppSvcRelatedInfoDto();
    }

    private boolean compareHciName(List<AppGrpPremisesDto> appGrpPremisesDtos,List<AppGrpPremisesDto> oldAppGrpPremisesDtos){
        int length = appGrpPremisesDtos.size();
        int oldLength = oldAppGrpPremisesDtos.size();
        if(length == oldLength){
            for(int i=0; i<length; i++){
                AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtos.get(0);
                AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtos.get(0);
                if(!getHciName(appGrpPremisesDto).equals(getHciName(oldAppGrpPremisesDto))){
                    return false;
                }
            }
        }
        //is same
        return true;
    }
    private String getHciName(AppGrpPremisesDto appGrpPremisesDto){
        String hciName = "";
        if(ApplicationConsts.PREMISES_TYPE_ON_SITE.equals(appGrpPremisesDto.getPremisesType())){
            hciName = appGrpPremisesDto.getHciName();
        }else if(ApplicationConsts.PREMISES_TYPE_CONVEYANCE.equals(appGrpPremisesDto.getPremisesType())){
            hciName = appGrpPremisesDto.getConveyanceVehicleNo();
        }
        return hciName;
    }

    private AppSvcLaboratoryDisciplinesDto getDisciplinesDto(List<AppSvcLaboratoryDisciplinesDto> disciplinesDto,String hciName){
        for(AppSvcLaboratoryDisciplinesDto iten:disciplinesDto){
            if(hciName.equals(iten.getPremiseVal())){
                return iten;
            }
        }
        return new AppSvcLaboratoryDisciplinesDto();
    }
    private boolean compareLocation(List<AppGrpPremisesDto> appGrpPremisesDtos,List<AppGrpPremisesDto> oldAppGrpPremisesDtos){
        int length = appGrpPremisesDtos.size();
        int oldLength = oldAppGrpPremisesDtos.size();
        if(length == oldLength){
            for(int i=0; i<length; i++){
                AppGrpPremisesDto appGrpPremisesDto = appGrpPremisesDtos.get(0);
                AppGrpPremisesDto oldAppGrpPremisesDto = oldAppGrpPremisesDtos.get(0);
                if(!appGrpPremisesDto.getAddress().equals(oldAppGrpPremisesDto.getAddress())){
                    return false;
                }
            }
        }
        //is same
        return true;
    }

    private void handlerDupPremDoc(String dupPrem,AppGrpPrimaryDocDto targetDto,List<AppGrpPremisesDto> appGrpPremisesDtos,List<AppGrpPrimaryDocDto> newPrimaryDocList){
        if(AppConsts.NO.equals(dupPrem)){
            newPrimaryDocList.add(targetDto);
        }else if(AppConsts.YES.equals(dupPrem)){
            for(AppGrpPremisesDto appGrpPremisesDto:appGrpPremisesDtos){
                targetDto.setPremisessName(appGrpPremisesDto.getPremisesIndexNo());
                targetDto.setPremisessType(appGrpPremisesDto.getPremisesType());
                newPrimaryDocList.add(targetDto);
            }
        }

    }

    private static AppSubmissionDto getAppSubmissionDto(HttpServletRequest request) {
        AppSubmissionDto appSubmissionDto = (AppSubmissionDto) ParamUtil.getSessionAttr(request, NewApplicationDelegator.APPSUBMISSIONDTO);
        if (appSubmissionDto == null) {
            log.info(StringUtil.changeForLog("appSubmissionDto is empty "));
            appSubmissionDto = new AppSubmissionDto();
        }
        return appSubmissionDto;
    }

    private void turnId(List<HcsaSvcSubtypeOrSubsumedDto> hcsaSvcSubtypeOrSubsumedDtos, Map<String, HcsaSvcSubtypeOrSubsumedDto> allCheckListMap) {

        for (HcsaSvcSubtypeOrSubsumedDto dto : hcsaSvcSubtypeOrSubsumedDtos) {
            allCheckListMap.put(dto.getId(), dto);
            if (dto.getList() != null && dto.getList().size() > 0) {
                turnId(dto.getList(), allCheckListMap);
            }
        }
    }

    private void recursingChooseLabUpward(Map<String, HcsaSvcSubtypeOrSubsumedDto> map,String targetSvcScopeId,List<String> svcScopeIdList,List<AppSvcChckListDto> newSvcScopeList){
        HcsaSvcSubtypeOrSubsumedDto hcsaSvcSubtypeOrSubsumedDto = map.get(targetSvcScopeId);
        if(hcsaSvcSubtypeOrSubsumedDto != null){
            String id = hcsaSvcSubtypeOrSubsumedDto.getId();
            if(!svcScopeIdList.contains(id)){
                //check this parent checkbox
                AppSvcChckListDto appSvcChckListDto = new AppSvcChckListDto();
                appSvcChckListDto.setChkLstConfId(id);
                appSvcChckListDto.setChkLstType(hcsaSvcSubtypeOrSubsumedDto.getType());
                appSvcChckListDto.setChkName(hcsaSvcSubtypeOrSubsumedDto.getName());
                appSvcChckListDto.setParentName(hcsaSvcSubtypeOrSubsumedDto.getParentId());
                appSvcChckListDto.setChildrenName(hcsaSvcSubtypeOrSubsumedDto.getChildrenId());
                newSvcScopeList.add(appSvcChckListDto);
                svcScopeIdList.add(id);
            }
            String parentId  = hcsaSvcSubtypeOrSubsumedDto.getParentId();
            if(!StringUtil.isEmpty(parentId)){
                if(!svcScopeIdList.contains(parentId)){
                    //turn
                    recursingChooseLabUpward(map,parentId,svcScopeIdList,newSvcScopeList);
                }
            }
        }

    }

    private boolean getBundleLicenceByHciCode(String hciCode, String licenseeId, List<String> svcNameList) {
        if( StringUtil.isEmpty(licenseeId) || IaisCommonUtils.isEmpty(svcNameList)){
            return false;
        }
        if(StringUtil.isEmpty(hciCode)){
            hciCode="###";
        }
        return licenceClient.getBundleLicence(hciCode,licenseeId,svcNameList).getEntity();
    }

    private List<HcsaFeeBundleItemDto> getBundleDtoBySvcCode(List<HcsaFeeBundleItemDto> hcsaFeeBundleItemDtos, String svcCode){
        List<HcsaFeeBundleItemDto> result = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(hcsaFeeBundleItemDtos) && !StringUtil.isEmpty(svcCode)){
            //get target bundleId
            String bundleId = null;
            for(HcsaFeeBundleItemDto hcsaFeeBundleItemDto:hcsaFeeBundleItemDtos){
                if(svcCode.equals(hcsaFeeBundleItemDto.getSvcCode())){
                    bundleId = hcsaFeeBundleItemDto.getBundleId();
                    break;
                }
            }
            if (bundleId != null) {
                for(HcsaFeeBundleItemDto hcsaFeeBundleItemDto:hcsaFeeBundleItemDtos){
                    if(bundleId.equals(hcsaFeeBundleItemDto.getBundleId()) && !svcCode.equals(hcsaFeeBundleItemDto.getSvcCode())){
                        result.add(hcsaFeeBundleItemDto);
                    }
                }
            }

        }
        return result;
    }

    private  int getEasVehicleCount(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos){
        int result = 0;
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                String serviceCode = appSvcRelatedInfoDto.getServiceCode();
                if(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode) ){
                    List<AppSvcVehicleDto> appSvcVehicleDtos = appSvcRelatedInfoDto.getAppSvcVehicleDtoList();
                    if(!IaisCommonUtils.isEmpty(appSvcVehicleDtos)){
                        result = result + appSvcVehicleDtos.size();
                    }
                }
            }
        }
        return result;
    }
    private  int getMtsVehicleCount(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos){
        int result = 0;
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                String serviceCode = appSvcRelatedInfoDto.getServiceCode();
                if(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)){
                    List<AppSvcVehicleDto> appSvcVehicleDtos = appSvcRelatedInfoDto.getAppSvcVehicleDtoList();
                    if(!IaisCommonUtils.isEmpty(appSvcVehicleDtos)){
                        result = result + appSvcVehicleDtos.size();
                    }
                }
            }
        }
        return result;
    }

    private void setEasMtsBundleInfo(LicenceFeeDto licenceFeeDto, String hciCode, List<HcsaFeeBundleItemDto> hcsaFeeBundleItemDtos, String serviceCode, String licenseeId, int easMtsVehicleCount,String appType){
        List<HcsaFeeBundleItemDto> bundleDtos = getBundleDtoBySvcCode(hcsaFeeBundleItemDtos,serviceCode);
        boolean hadBundleLicence = false;
        if(!IaisCommonUtils.isEmpty(bundleDtos)){
            log.debug(StringUtil.changeForLog("get bundle licence ..."));
            log.debug("hciCode is {}",hciCode);
            log.debug("licenseeId is {}",licenseeId);
            List<String> bundleSvcNameList = IaisCommonUtils.genNewArrayList();
            for(HcsaFeeBundleItemDto hcsaFeeBundleItemDto:bundleDtos){
                bundleSvcNameList.add(HcsaServiceCacheHelper.getServiceByCode(hcsaFeeBundleItemDto.getSvcCode()).getSvcName());
            }
            hadBundleLicence = getBundleLicenceByHciCode(hciCode, licenseeId, bundleSvcNameList);
        }
        log.debug("hadBundleLicence is {}",hadBundleLicence);
        if(hadBundleLicence&&appType.equals(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION)){
            //found bundle licence
            licenceFeeDto.setBundle(4);
        }else{
            //judge vehicle count
            if(easMtsVehicleCount <= 10 ){
                licenceFeeDto.setBundle(1);
            }else {
                licenceFeeDto.setBundle(2);
            }
        }
    }

    @Override
    public void clearSession(HttpServletRequest request) {
        if (request == null) {
            return;
        }
        HttpSession session = request.getSession();
        // New Application - Declaration - clear uploaded dto
        String fileAppendId = getFileAppendId(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        session.removeAttribute(fileAppendId + "DocShowPageDto");
        session.removeAttribute(HcsaFileAjaxController.SEESION_FILES_MAP_AJAX + fileAppendId);
        // Request for Change
        fileAppendId = getFileAppendId(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        session.removeAttribute(fileAppendId + "DocShowPageDto");
        session.removeAttribute(HcsaFileAjaxController.SEESION_FILES_MAP_AJAX + fileAppendId);
        // Cessation
        fileAppendId = getFileAppendId(ApplicationConsts.APPLICATION_TYPE_CESSATION);
        session.removeAttribute(fileAppendId + "DocShowPageDto");
        session.removeAttribute(HcsaFileAjaxController.SEESION_FILES_MAP_AJAX + fileAppendId);
        // Renewal
        fileAppendId = getFileAppendId(ApplicationConsts.APPLICATION_TYPE_RENEWAL);
        session.removeAttribute(fileAppendId + "DocShowPageDto");
        session.removeAttribute(HcsaFileAjaxController.SEESION_FILES_MAP_AJAX + fileAppendId);
        // View and Print
        session.removeAttribute("viewPrint");

        //clear Session
        session.removeAttribute(NewApplicationDelegator.ALL_SVC_NAMES);
        session.removeAttribute(NewApplicationDelegator.APPSUBMISSIONDTO);
        session.removeAttribute(NewApplicationDelegator.HCSASERVICEDTO);
        session.removeAttribute(RenewalConstants.WITHOUT_RENEWAL_APPSUBMISSION_ATTR);
        //Primary Documents
        session.removeAttribute(NewApplicationDelegator.COMMONHCSASVCDOCCONFIGDTO);
        session.removeAttribute(NewApplicationDelegator.PREMHCSASVCDOCCONFIGDTO);
        session.removeAttribute(NewApplicationDelegator.RELOADAPPGRPPRIMARYDOCMAP);
        session.removeAttribute(NewApplicationDelegator.DRAFTCONFIG);
        Map<String, AppSvcPrincipalOfficersDto> psnMap = IaisCommonUtils.genNewHashMap();
        session.setAttribute(NewApplicationDelegator.PERSONSELECTMAP, (Serializable) psnMap);
        session.removeAttribute(AppServicesConsts.HCSASERVICEDTOLIST);

        session.removeAttribute("oldSubmitAppSubmissionDto");
        session.removeAttribute("submitAppSubmissionDto");
        session.removeAttribute("appSubmissionDtos");
        session.removeAttribute("rfiHcsaService");
        session.removeAttribute("ackPageAppSubmissionDto");
        session.removeAttribute("serviceConfig");
        session.removeAttribute("app-rfc-tranfer");
        session.removeAttribute("rfc_eqHciCode");
        session.removeAttribute("declaration_page_is");

        session.removeAttribute(NewApplicationConstant.PREMISES_HCI_LIST);
        session.removeAttribute(NewApplicationDelegator.LICPERSONSELECTMAP);
        session.removeAttribute(HcsaLicenceFeConstant.DASHBOARDTITLE);
        session.removeAttribute("AssessMentConfig");
        session.removeAttribute(NewApplicationDelegator.CURR_ORG_USER_ACCOUNT);
        session.removeAttribute(NewApplicationDelegator.PRIMARY_DOC_CONFIG);
        session.removeAttribute(NewApplicationDelegator.SVC_DOC_CONFIG);
        session.removeAttribute("app-rfc-tranfer");
        HashMap<String, String> coMap = new HashMap<>(4);
        coMap.put("licensee", "");
        coMap.put("premises", "");
        coMap.put("document", "");
        coMap.put("information", "");
        coMap.put("previewli", "");
        session.setAttribute(NewApplicationConstant.CO_MAP, coMap);
        //request For Information Loading
        session.removeAttribute(NewApplicationDelegator.REQUESTINFORMATIONCONFIG);
        session.removeAttribute("HcsaSvcSubtypeOrSubsumedDto");
        // CR: Licensee Details
        session.removeAttribute(NewApplicationDelegator.LICENSEE_MAP);
        // clear selectLicence
        Enumeration<?> names = session.getAttributeNames();
        if (names != null) {
            while (names.hasMoreElements()) {
                String name = (String) names.nextElement();
                if (name.startsWith("selectLicence")) {
                    session.removeAttribute(name);
                }
            }
        }
        session.removeAttribute(NewApplicationDelegator.RFC_APP_GRP_PREMISES_DTO_LIST);
        session.removeAttribute(NewApplicationDelegator.PREMISESTYPE);
    }
}