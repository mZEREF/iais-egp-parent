package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.action.NewApplicationDelegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.organization.OrganizationConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesDoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSvcPersonAndExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppDeclarationMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGroupMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcCgoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChargesPageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcChckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcClinicalDirectorDto;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.HcsaFeeBundleItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.LicenceFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgGiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.SgNoValidator;
import com.ecquaria.cloud.moh.iais.common.validation.ValidationUtils;
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
import com.ecquaria.cloud.moh.iais.validate.serviceInfo.ValidateCharges;
import com.ecquaria.cloud.moh.iais.validate.serviceInfo.ValidateClincalDirector;
import com.ecquaria.cloud.moh.iais.validate.serviceInfo.ValidateVehicle;
import com.ecquaria.cloud.submission.client.model.SubmitResp;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sop.util.CopyUtil;
import sop.webflow.rt.api.BaseProcessClass;
import sop.webflow.rt.api.Process;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        log.debug(StringUtil.changeForLog("updateDraftStatus start ..."));
        applicationFeClient.updateDraftStatus(draftNo,status);
        log.debug(StringUtil.changeForLog("updateDraftStatus end ..."));
    }

    @Override
    public ProfessionalResponseDto retrievePrsInfo(String profRegNo){
        log.debug(StringUtil.changeForLog("retrieve prs info start ..."));
        log.debug("prof Reg No is {}",profRegNo);
        ProfessionalResponseDto professionalResponseDto = null;
        if(!StringUtil.isEmpty(profRegNo)){
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
            } catch (Exception e) {
                professionalResponseDto = new ProfessionalResponseDto();
                log.info(StringUtil.changeForLog("retrieve prs info start ..."));
                log.error(e.getMessage(), e);
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
            }
            appDeclarationDocShowPageDto.setFileMaxIndex(appDeclarationDocDtoList.size());
            appDeclarationDocShowPageDto.setPageShowFileMap(map);
            appDeclarationDocShowPageDto.setPageShowFileDtos(pageShowFileDtos);
            appDeclarationDocShowPageDto.setPageShowFileHashMap(pageShowFileHashMap);
            return appDeclarationDocShowPageDto;
    }

    @Override
    public AppDeclarationMessageDto getAppDeclarationMessageDto(HttpServletRequest request, String type) {
        AppDeclarationMessageDto appDeclarationMessageDto=new AppDeclarationMessageDto();
        if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(type)){
            String preliminaryQuestionKindly = request.getParameter("preliminaryQuestionKindly");
            String preliminaryQuestionItem1 = request.getParameter("preliminaryQuestionItem1");
            String preliminaryQuestiontem2 = request.getParameter("preliminaryQuestiontem2");
            appDeclarationMessageDto.setPreliminaryQuestionKindly(preliminaryQuestionKindly);
            appDeclarationMessageDto.setPreliminaryQuestionItem1(preliminaryQuestionItem1);
            appDeclarationMessageDto.setPreliminaryQuestiontem2(preliminaryQuestiontem2);
        }else if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(type)){
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
            appDeclarationMessageDto.setCompetenciesRemark(criminalRecordsItem1);
            String criminalRecordsItem2 = request.getParameter("criminalRecordsItem2");
            appDeclarationMessageDto.setCriminalRecordsItem2(criminalRecordsItem2);
            String criminalRecordsItem3 = request.getParameter("criminalRecordsItem3");
            appDeclarationMessageDto.setCriminalRecordsItem3(criminalRecordsItem3);
            String criminalRecordsItem4 = request.getParameter("criminalRecordsItem4");
            appDeclarationMessageDto.setCriminalRecordsItem4(criminalRecordsItem4);
            String criminalRecordsRemark = request.getParameter("criminalRecordsRemark");
            appDeclarationMessageDto.setCriminalRecordsRemark(criminalRecordsRemark);

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
        return appDeclarationMessageDto;
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
            int easMtsVehicleCount = getEasMtsVehicleCount(appSvcRelatedInfoDtos);
            log.debug("eas nad mts vehicle count is {}",easMtsVehicleCount);
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
                            if(easMtsVehicleCount <= 10 ){
                                licenceFeeDto.setBundle(1);
                            }else {
                                licenceFeeDto.setBundle(2);
                            }
                        }else{
                            String hciCode = appGrpPremisesDtos.get(0).getHciCode();
                            setEasMtsBundleInfo(licenceFeeDto, hciCode, hcsaFeeBundleItemDtos, serviceCode, appSubmissionDto.getLicenseeId(), easMtsVehicleCount);
                        }
                    } else if(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)){
                        if(hadEas && hadMts){
                            //new eas and mts
                            licenceFeeDto.setBundle(3);
                        }else{
                            String hciCode = appGrpPremisesDtos.get(0).getHciCode();
                            setEasMtsBundleInfo(licenceFeeDto, hciCode, hcsaFeeBundleItemDtos, serviceCode, appSubmissionDto.getLicenseeId(), easMtsVehicleCount);
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
            int easMtsVehicleCount = getEasMtsVehicleCount(appSvcRelatedInfoDtos);
            log.debug("eas nad mts vehicle count is {}",easMtsVehicleCount);
            List<HcsaFeeBundleItemDto> hcsaFeeBundleItemDtos = appConfigClient.getActiveBundleDtoList().getEntity();

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
                    //set bundle
                    if(!IaisCommonUtils.isEmpty(hcsaFeeBundleItemDtos)){
                        String serviceCode=baseServiceDto.getSvcCode();
                        log.debug(StringUtil.changeForLog("set bundle info ..."));
                        if(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode)){
                            if(hadEas && hadMts){
                                //judge vehicle count
                                if(easMtsVehicleCount <= 10 ){
                                    licenceFeeDto.setBundle(1);
                                }else {
                                    licenceFeeDto.setBundle(2);
                                }
                            }else{
                                String hciCode = appGrpPremisesDtos.get(0).getHciCode();
                                setEasMtsBundleInfo(licenceFeeDto, hciCode, hcsaFeeBundleItemDtos, serviceCode, appSubmissionDto.getLicenseeId(), easMtsVehicleCount);
                            }
                        } else if(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)){
                            if(hadEas && hadMts){
                                //new eas and mts
                                licenceFeeDto.setBundle(3);
                            }else{
                                String hciCode = appGrpPremisesDtos.get(0).getHciCode();
                                setEasMtsBundleInfo(licenceFeeDto, hciCode, hcsaFeeBundleItemDtos, serviceCode, appSubmissionDto.getLicenseeId(), easMtsVehicleCount);
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
                            if(hadEas && hadMts){
                                //judge vehicle count
                                if(easMtsVehicleCount <= 10 ){
                                    licenceFeeDto.setBundle(1);
                                }else {
                                    licenceFeeDto.setBundle(2);
                                }
                            }else{
                                String hciCode = appGrpPremisesDtos.get(0).getHciCode();
                                setEasMtsBundleInfo(licenceFeeDto, hciCode, hcsaFeeBundleItemDtos, serviceCode, appSubmissionDto.getLicenseeId(), easMtsVehicleCount);
                            }
                        } else if(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)){
                            if(hadEas && hadMts){
                                //new eas and mts
                                licenceFeeDto.setBundle(3);
                            }else{
                                String hciCode = appGrpPremisesDtos.get(0).getHciCode();
                                setEasMtsBundleInfo(licenceFeeDto, hciCode, hcsaFeeBundleItemDtos, serviceCode, appSubmissionDto.getLicenseeId(), easMtsVehicleCount);
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
        appSubmissionDto.setAmount(amount);
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
        log.debug(StringUtil.changeForLog("notEmptyDocList size:" +  notEmptyDocList.size()));
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
                                    handlerDupPremDoc(dupPrem,appGrpPrimaryDocDto,appGrpPremisesDtos,newPrimaryDocList);
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
        //
        Map<String, AppSvcPrincipalOfficersDto> licPersonMap = (Map<String, AppSvcPrincipalOfficersDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.LICPERSONSELECTMAP);
        //
        Map<String, List<HcsaSvcPersonnelDto>> allSvcAllPsnConfig = getAllSvcAllPsnConfig(bpc.request);
        List<AppSvcRelatedInfoDto> dto = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        ServiceStepDto serviceStepDto = new ServiceStepDto();
        for (int i = 0; i < dto.size(); i++) {
            String serviceId = dto.get(i).getServiceId();
            List<HcsaServiceStepSchemeDto> hcsaServiceStepSchemeDtos = serviceConfigService.getHcsaServiceStepSchemesByServiceId(serviceId);
            serviceStepDto.setHcsaServiceStepSchemeDtos(hcsaServiceStepSchemeDtos);
            List<HcsaSvcPersonnelDto> currentSvcAllPsnConfig = serviceConfigService.getSvcAllPsnConfig(hcsaServiceStepSchemeDtos, serviceId);
            Map<String, String> map = doCheckBox(bpc, sB, allSvcAllPsnConfig, currentSvcAllPsnConfig, dto.get(i),systemParamConfig.getUploadFileLimit(),systemParamConfig.getUploadFileType(),appSubmissionDto.getAppGrpPremisesDtoList());
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
    public Map<String, String> doCheckBox(BaseProcessClass bpc, StringBuilder sB, Map<String, List<HcsaSvcPersonnelDto>> allSvcAllPsnConfig, List<HcsaSvcPersonnelDto> currentSvcAllPsnConfig, AppSvcRelatedInfoDto dto, int uploadFileLimit, String sysFileType,List<AppGrpPremisesDto> appGrpPremisesDtos) {
        String serviceId = dto.getServiceId();
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        for (HcsaSvcPersonnelDto hcsaSvcPersonnelDto : currentSvcAllPsnConfig) {
            String psnType = hcsaSvcPersonnelDto.getPsnType();
            int mandatoryCount = hcsaSvcPersonnelDto.getMandatoryCount();
            if (ApplicationConsts.PERSONNEL_PSN_TYPE_PO.equals(psnType)) {
                List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = dto.getAppSvcPrincipalOfficersDtoList();
                validatePersonMandatoryCount(Collections.singletonList(appSvcPrincipalOfficersDtoList),errorMap,ApplicationConsts.PERSONNEL_PSN_TYPE_PO,mandatoryCount,serviceId,sB);
            } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL.equals(psnType)) {
                List<AppSvcPersonnelDto> appSvcPersonnelDtoList = dto.getAppSvcPersonnelDtoList();
                validatePersonMandatoryCount(Collections.singletonList(appSvcPersonnelDtoList),errorMap,ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL,mandatoryCount,serviceId,sB);
            } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnType)) {
                List<AppSvcCgoDto> appSvcCgoDtoList = dto.getAppSvcCgoDtoList();
                validatePersonMandatoryCount(Collections.singletonList(appSvcCgoDtoList),errorMap,ApplicationConsts.PERSONNEL_PSN_TYPE_CGO,mandatoryCount,serviceId,sB);
            } else if (ApplicationConsts.PERSONNEL_PSN_TYPE_MAP.equals(psnType)) {
                List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = dto.getAppSvcMedAlertPersonList();
                validatePersonMandatoryCount(Collections.singletonList(appSvcMedAlertPersonList),errorMap,ApplicationConsts.PERSONNEL_PSN_TYPE_MAP,mandatoryCount,serviceId,sB);
            }else if(ApplicationConsts.PERSONNEL_VEHICLES.equals(psnType)){
                List<AppSvcVehicleDto> appSvcVehicleDtoList = dto.getAppSvcVehicleDtoList();
                validatePersonMandatoryCount(Collections.singletonList(appSvcVehicleDtoList),errorMap,ApplicationConsts.PERSONNEL_VEHICLES,mandatoryCount,serviceId,sB);
            }else if(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR.equals(psnType)){
                List<AppSvcClinicalDirectorDto> appSvcClinicalDirectorDtoList = dto.getAppSvcClinicalDirectorDtoList();
                validatePersonMandatoryCount(Collections.singletonList(appSvcClinicalDirectorDtoList),errorMap,ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR,mandatoryCount,serviceId,sB);
            }else if(ApplicationConsts.PERSONNEL_CHARGES.equals(psnType)){
                AppSvcChargesPageDto appSvcChargesPageDto = dto.getAppSvcChargesPageDto();
                if(appSvcChargesPageDto!=null){
                    validatePersonMandatoryCount(Collections.singletonList(appSvcChargesPageDto.getGeneralChargesDtos()),errorMap,ApplicationConsts.PERSONNEL_CHARGES,mandatoryCount,serviceId,sB);
                }else {
                    errorMap.put("appSvcChargesPageDto","appSvcChargesPageDto is null");
                }
            }else if(ApplicationConsts.PERSONNEL_CHARGES_OTHER.equals(psnType)){
                AppSvcChargesPageDto appSvcChargesPageDto = dto.getAppSvcChargesPageDto();
                if(appSvcChargesPageDto!=null){
                    validatePersonMandatoryCount(Collections.singletonList(appSvcChargesPageDto.getOtherChargesDtos()),errorMap,ApplicationConsts.PERSONNEL_CHARGES,mandatoryCount,serviceId,sB);
                }else {
                    errorMap.put("otherAppSvcChargesPageDto","other appSvcChargesPageDto is null");
                }
            }
        }
        List<AppSvcPrincipalOfficersDto> appSvcMedAlertPersonList = dto.getAppSvcMedAlertPersonList();
        Map<String, AppSvcPersonAndExtDto> licPersonMap = (Map<String, AppSvcPersonAndExtDto>) ParamUtil.getSessionAttr(bpc.request, NewApplicationDelegator.LICPERSONSELECTMAP);
        Map<String, String> map = NewApplicationHelper.doValidateMedAlertPsn(appSvcMedAlertPersonList, licPersonMap, dto.getServiceCode());
        log.info(JsonUtil.parseToJson(map));
        if (!map.isEmpty()) {
            sB.append(serviceId);
            errorMap.put("Medaler", "error");
        }
        List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList = dto.getAppSvcLaboratoryDisciplinesDtoList();
        List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos = allSvcAllPsnConfig.get(serviceId);

        List<AppSvcCgoDto> appSvcCgoDtoList = dto.getAppSvcCgoDtoList();
        doAppSvcCgoDto(hcsaSvcPersonnelDtos, errorMap, appSvcCgoDtoList, serviceId, sB);
        log.info(sB.toString());
        List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList = dto.getAppSvcDisciplineAllocationDtoList();
        doSvcDis(errorMap, appSvcDisciplineAllocationDtoList, serviceId, sB);
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(errorMap) + "doSvcDis"));
        dolabory(errorMap,appSvcDisciplineAllocationDtoList, appSvcLaboratoryDisciplinesDtoList, serviceId, sB);
        log.info(sB.toString());
        doSvcDisdolabory(errorMap, appSvcDisciplineAllocationDtoList, appSvcLaboratoryDisciplinesDtoList, serviceId, sB);
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(errorMap) + "doSvcDisdolabory"));
        List<AppSvcPrincipalOfficersDto> appSvcPrincipalOfficersDtoList = dto.getAppSvcPrincipalOfficersDtoList();
        Map<String, String> govenMap = NewApplicationHelper.doValidateGovernanceOfficers(dto.getAppSvcCgoDtoList(), licPersonMap, dto.getServiceCode());
        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(govenMap)));
        if (!govenMap.isEmpty()) {
            errorMap.put("CGO", "error");
            sB.append(serviceId);
            log.info("govenMap is error");
        }
        doPO(hcsaSvcPersonnelDtos, errorMap, appSvcPrincipalOfficersDtoList, serviceId, sB);
        log.info(sB.toString());
        List<AppSvcPersonnelDto> appSvcPersonnelDtoList = dto.getAppSvcPersonnelDtoList();
        doAppSvcPersonnelDtoList(hcsaSvcPersonnelDtos, errorMap, appSvcPersonnelDtoList, serviceId, sB,dto.getServiceCode());
        log.info(sB.toString());
        List<AppSvcDocDto> appSvcDocDtoLit = dto.getAppSvcDocDtoLit();
        doSvcDocument(errorMap, appSvcDocDtoLit, serviceId, sB,uploadFileLimit,sysFileType);
        List<HcsaSvcDocConfigDto> svcDocConfigDtos = IaisCommonUtils.genNewArrayList();
        List<HcsaSvcDocConfigDto> premServiceDocConfigDtos = IaisCommonUtils.genNewArrayList();
        List<HcsaSvcDocConfigDto> hcsaSvcDocDtos = serviceConfigService.getAllHcsaSvcDocs(dto.getServiceId());
        if (!IaisCommonUtils.isEmpty(hcsaSvcDocDtos)) {
            for(HcsaSvcDocConfigDto hcsaSvcDocConfigDto:hcsaSvcDocDtos){
                if ("0".equals(hcsaSvcDocConfigDto.getDupForPrem())) {
                    svcDocConfigDtos.add(hcsaSvcDocConfigDto);
                } else if ("1".equals(hcsaSvcDocConfigDto.getDupForPrem())) {
                    premServiceDocConfigDtos.add(hcsaSvcDocConfigDto);
                }
            }
        }
        Map<String,String> svcDocErrMap = IaisCommonUtils.genNewHashMap();
        NewApplicationHelper.svcDocMandatoryValidate(svcDocConfigDtos,dto.getAppSvcDocDtoLit(),appGrpPremisesDtos,dto,svcDocErrMap);
        if(svcDocErrMap.size() > 0){
            sB.append(serviceId);
            errorMap.put("svcDoc", "error");
        }
        log.info(sB.toString());

        log.info(StringUtil.changeForLog(JsonUtil.parseToJson(errorMap)));
        validateCharges.doValidateCharges(errorMap,dto.getAppSvcChargesPageDto());
        String currSvcCode = (String) ParamUtil.getSessionAttr(bpc.request,NewApplicationDelegator.CURRENTSVCCODE);
        validateClincalDirector.doValidateClincalDirector(errorMap,dto.getAppSvcClinicalDirectorDtoList(),currSvcCode);

        validateVehicle.doValidateVehicles(errorMap,dto.getAppSvcVehicleDtoList());

        return errorMap;
    }

    private void  validatePersonMandatoryCount(List<Object> list,Map<String,String> map,String type,Integer mandatoryCount,String serviceId,StringBuilder sB){
        if (list == null) {
            if (mandatoryCount > 0) {
                map.put("error"+type, type);
                sB.append(serviceId);
                log.info(StringUtil.changeForLog(type+" null"));
            }
        } else if (list.size() < mandatoryCount) {
            map.put("error"+type, type);
            sB.append(serviceId);
            log.info(StringUtil.changeForLog(type+" mandatoryCount"));
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
        OrgGiroAccountInfoDto orgGiroAccountInfoDto = organizationLienceseeClient.getGiroAccByLicenseeId(licenseeId).getEntity();
        if(!StringUtil.isEmpty(orgGiroAccountInfoDto.getOrganizationId())){
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
    public Map<String, List<AppSvcDisciplineAllocationDto>> getDisciplineAllocationDtoList(AppSubmissionDto appSubmissionDto, String svcId) throws CloneNotSupportedException {
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
        Map<String,List<AppSvcDisciplineAllocationDto>> reloadDisciplineAllocationMap = IaisCommonUtils.genNewHashMap();
        for(AppGrpPremisesDto appGrpPremisesDto : appGrpPremisesDtoList){
            List<AppSvcDisciplineAllocationDto> reloadDisciplineAllocation = IaisCommonUtils.genNewArrayList();
            String premisesIndexNo = appGrpPremisesDto.getPremisesIndexNo();
            //get curr premises's appSvcChckListDto
            List<AppSvcChckListDto> appSvcChckListDtoList = IaisCommonUtils.genNewArrayList();
            List<AppSvcLaboratoryDisciplinesDto> appSvcLaboratoryDisciplinesDtoList =appSvcRelatedInfoDto.getAppSvcLaboratoryDisciplinesDtoList();
            List<AppSvcCgoDto> appSvcCgoDtoList = appSvcRelatedInfoDto.getAppSvcCgoDtoList();
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
                                    for(AppSvcCgoDto appSvcCgoDto:appSvcCgoDtoList){
                                        if(idNo.equals(appSvcCgoDto.getIdNo())){
                                            log.info(StringUtil.changeForLog("set cgoSel ..."));
                                            appSvcDisciplineAllocationDto.setCgoSelName(appSvcCgoDto.getName());
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

    private static void doAppSvcCgoDto(List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos, Map map, List<AppSvcCgoDto> list, String serviceId, StringBuilder sB) {
        if (list == null) {
            if (hcsaSvcPersonnelDtos != null) {
                for (HcsaSvcPersonnelDto every : hcsaSvcPersonnelDtos) {
                    String psnType = every.getPsnType();
                    if (ApplicationConsts.PERSONNEL_PSN_TYPE_CGO.equals(psnType)) {
                        log.info("PERSONNEL_PSN_TYPE_CGO null");
                        sB.append(serviceId);
                        return;
                    }
                }
            }

            return;
        }


        boolean flag = false;
        for (int i = 0; i < list.size(); i++) {
            String assignSelect = list.get(i).getAssignSelect();
            if ("".equals(assignSelect) || assignSelect == null) {
                map.put("cgoassignSelect" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","cgoassignSelect","field"));
                flag = true;
            }
            String idType = list.get(i).getIdType();
            if (StringUtil.isEmpty(idType)) {
                map.put("cgotype" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","cgotype","field"));
                flag = true;
            }
            String mobileNo = list.get(i).getMobileNo();
            if (StringUtil.isEmpty(mobileNo)) {
                map.put("cgomobileNo" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","cgomobileNo","field"));
                flag = true;
            } else {
                if (!mobileNo.matches("^[8|9][0-9]{7}$")) {
                    map.put("cgomobileNo" + i, "GENERAL_ERR0007");
                    flag = true;
                }
            }
            String emailAddr = list.get(i).getEmailAddr();
            if (StringUtil.isEmpty(emailAddr)) {
                map.put("cgoemailAddr" + i, MessageUtil.replaceMessage("GENERAL_ERR0006","cgoemailAddr","field"));
                flag = true;
            } else {
                if (!ValidationUtils.isEmail(emailAddr)) {
                    map.put("cgoemailAddr" + i, "GENERAL_ERR0014");
                    flag = true;
                }
            }
            if (flag) {
                sB.append(serviceId);

            }

        }
    }

    private static void dolabory(Map<String, String> map,List<AppSvcDisciplineAllocationDto> appSvcDisciplineAllocationDtoList, List<AppSvcLaboratoryDisciplinesDto> list, String serviceId, StringBuilder sB) {
        if (list != null && list.isEmpty()) {

        }
        if(list!=null&&appSvcDisciplineAllocationDtoList!=null&&!list.isEmpty()){
            for(AppSvcLaboratoryDisciplinesDto appSvcLaboratoryDisciplinesDto : list){
                List<AppSvcChckListDto> appSvcChckListDtoList = appSvcLaboratoryDisciplinesDto.getAppSvcChckListDtoList();
                if(appSvcChckListDtoList!=null&&!appSvcChckListDtoList.isEmpty()){
                    for(AppSvcChckListDto appSvcChckListDto : appSvcChckListDtoList){
                        boolean flag=false;
                        for(AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto : appSvcDisciplineAllocationDtoList){
                            if(appSvcChckListDto.getChkLstConfId().equals(appSvcDisciplineAllocationDto.getChkLstConfId())){
                                flag=true;
                                break;
                            }
                        }
                        if(!flag){
                            map.put("allocation","allocation error");
                            sB.append(serviceId);
                        }
                    }
                }
            }

        }
    }

    private static void doSvcDis(Map map, List<AppSvcDisciplineAllocationDto> list, String serviceId, StringBuilder sB) {
        if (list == null) {
            return;
        } else {
            for (AppSvcDisciplineAllocationDto appSvcDisciplineAllocationDto : list) {
                String idNo = appSvcDisciplineAllocationDto.getIdNo();
                if (StringUtil.isEmpty(idNo)) {
                    map.put("idNo", "idNo empty");
                    sB.append(serviceId);
                    return;
                }
            }
        }
    }

    private static void doSvcDisdolabory(Map map, List<AppSvcDisciplineAllocationDto> appSvcDislist, List<AppSvcLaboratoryDisciplinesDto> appSvclaborlist, String serviceId, StringBuilder sB) {
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
                    sB.append(serviceId);
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

    private static void doPO(List<HcsaSvcPersonnelDto> hcsaSvcPersonnelDtos, Map oneErrorMap, List<AppSvcPrincipalOfficersDto> poDto, String serviceId, StringBuilder sB) {
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
        if(StringUtil.isEmpty(hciCode) || StringUtil.isEmpty(licenseeId) || IaisCommonUtils.isEmpty(svcNameList)){
            return false;
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
            if(!StringUtil.isEmpty(bundleId)){
                for(HcsaFeeBundleItemDto hcsaFeeBundleItemDto:hcsaFeeBundleItemDtos){
                    if(bundleId.equals(hcsaFeeBundleItemDto.getBundleId()) && !svcCode.equals(hcsaFeeBundleItemDto.getSvcCode())){
                        result.add(hcsaFeeBundleItemDto);
                    }
                }
            }

        }
        return result;
    }

    private  int getEasMtsVehicleCount(List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos){
        int result = 0;
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)){
            for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
                String serviceCode = appSvcRelatedInfoDto.getServiceCode();
                if(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE.equals(serviceCode) || AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE.equals(serviceCode)){
                    List<AppSvcVehicleDto> appSvcVehicleDtos = appSvcRelatedInfoDto.getAppSvcVehicleDtoList();
                    if(!IaisCommonUtils.isEmpty(appSvcVehicleDtos)){
                        result = result + appSvcVehicleDtos.size();
                    }
                }
            }
        }
        return result;
    }

    private void setEasMtsBundleInfo(LicenceFeeDto licenceFeeDto, String hciCode, List<HcsaFeeBundleItemDto> hcsaFeeBundleItemDtos, String serviceCode, String licenseeId, int easMtsVehicleCount){
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
        if(hadBundleLicence){
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
}