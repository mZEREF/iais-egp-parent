package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.SmsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.WithdrawApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspRectificationSaveDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.WithdrawalService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import com.ecquaria.cloud.submission.client.App;
import com.ecquaria.egp.core.application.sender.SMSSender;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.xbill.DNS.Master;

@Service
@Slf4j
public class WithdrawalServiceImpl implements WithdrawalService {


    @Autowired
    private SystemAdminClient systemAdminClient;

    @Autowired
    private MsgTemplateClient msgTemplateClient;


    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private CessationClient cessationClient;

    @Autowired
    private OrganizationLienceseeClient organizationLienceseeClient;

    @Autowired
    private AppConfigClient appConfigClient;

    @Autowired
    private HcsaConfigFeClient hcsaConfigFeClient;

    @Autowired
    private FeEicGatewayClient feEicGatewayClient;

    @Autowired
    private Environment env;

    @Autowired
    private NotificationHelper notificationHelper;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Value("${iais.email.sender}")
    private String mailSender;

    @Value("${iais.system.one.address}")
    private String systemAddressOne;

    private static final String TEMPLATE_WITHDRAWAL_EMAIL         = "0DC58FEB-CF98-EA11-BE7A-000C29D29DB0";
    private static final String TEMPLATE_WITHDRAWAL_SMS           = "0A7AA342-B01D-EB11-8B7C-000C29FD17F9";
    private static final String TEMPLATE_WITHDRAWAL_MESSAGE       = "FF312463-B01D-EB11-8B7C-000C29FD17F9";

    @Override
    public void saveWithdrawn(List<WithdrawnDto> withdrawnDtoList) {
        withdrawnDtoList.forEach(h -> {
            String grpNo = systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL).getEntity();
            String licenseeId = h.getLicenseeId();
            AppSubmissionDto appSubmissionDto = applicationClient.gainSubmissionDto(h.getApplicationNo()).getEntity();
            transform(appSubmissionDto,licenseeId);
            appSubmissionDto.setAppGrpNo(grpNo);
            AppSubmissionDto newAppSubmissionDto = applicationClient.saveSubmision(appSubmissionDto).getEntity();
            List<ApplicationDto> applicationDtoList = newAppSubmissionDto.getApplicationDtos();
            for (ApplicationDto applicationDto:applicationDtoList
                 ) {
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = newAppSubmissionDto.getAppSvcRelatedInfoDtoList();
                if (appSvcRelatedInfoDtoList != null && appSvcRelatedInfoDtoList.size() >0){
                    String serviceId = appSvcRelatedInfoDtoList.get(0).getServiceId();
                    String appStatus = getAppStatus(serviceId,ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL);
                    if (!StringUtil.isEmpty(appStatus)){
                        applicationDto.setStatus(appStatus);
                    }
                }

            }
            applicationClient.updateApplicationList(applicationDtoList);
            h.setNewApplicationId(applicationDtoList.get(0).getId());
            applicationClient.saveApps(newAppSubmissionDto).getEntity();
        });
        List<String> withdrawnList = cessationClient.saveWithdrawn(withdrawnDtoList).getEntity();
        List<ApplicationDto> applicationDtoList = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(withdrawnList)){
            withdrawnDtoList.forEach(h -> {
                AppSubmissionDto appSubmissionDto = applicationClient.gainSubmissionDto(h.getApplicationNo()).getEntity();
                if (appSubmissionDto != null){
                    ApplicationDto applicationDto = applicationClient.getApplicationById(h.getApplicationId()).getEntity();
                    String serviceId = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceId();
                    if (!StringUtil.isEmpty(serviceId)){
                        applicationDtoList.add(applicationDto);
                        Map<String, Object> msgInfoMap = IaisCommonUtils.genNewHashMap();
                        msgInfoMap.put("ApplicationNumber", h.getApplicationNo());
                        msgInfoMap.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
                        LicenseeDto licenseeDto = organizationLienceseeClient.getLicenseeById(h.getLicenseeId()).getEntity();
                        List<ApplicationDto> applicationDtoList2 = hcsaConfigFeClient.returnFee(applicationDtoList).getEntity();
                        String applicantName = licenseeDto.getName();
                        msgInfoMap.put("Applicant", applicantName);
                        if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationDto.getApplicationType())
                                || ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationDto.getApplicationType())){
                            msgInfoMap.put("paymentStatus","0");
                            msgInfoMap.put("returnMount",applicationDtoList2.get(0).getReturnFee());
                        }else{
                            msgInfoMap.put("paymentStatus","1");
                        }
                        msgInfoMap.put("MOH_AGENCY_NAME",AppConsts.MOH_AGENCY_NAME);
                        msgInfoMap.put("emailAddress",systemAddressOne);
                        msgInfoMap.put("ApplicationDate",Formatter.formatDateTime(new Date(),"dd/MM/yyyy"));
                        try {
                            EmailParam emailParam = sendNotification(msgInfoMap,applicationDto);
                            notificationHelper.sendNotification(emailParam);
                            EmailParam emailParamSms = sendSms(msgInfoMap,applicationDto);
                            notificationHelper.sendNotification(emailParamSms);
                            sendInboxMessage(applicationDto,serviceId,msgInfoMap);
                        } catch (Exception e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            });
        }
    }

    @Override
    public void saveRfiWithdrawn(List<WithdrawnDto> withdrawnDtoList) {
        withdrawnDtoList.forEach(h -> {
            String appId = h.getNewApplicationId();
            ApplicationDto applicationDto = applicationClient.getApplicationById(appId).getEntity();
            String originLicenceId = applicationDto.getOriginLicenceId();
            List<String> licIds = IaisCommonUtils.genNewArrayList();
            licIds.add(originLicenceId);
            AppSubmissionDto appSubmissionDto = applicationClient.getAppSubmissionDtoByAppNo(applicationDto.getApplicationNo()).getEntity();
            try {
                transformRfi(appSubmissionDto, h.getLicenseeId(), applicationDto);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
        cessationClient.saveWithdrawn(withdrawnDtoList).getEntity();
    }

    private void transformRfi(AppSubmissionDto appSubmissionDto, String licenseeId, ApplicationDto applicationDto) throws Exception {
        AppSubmissionRequestInformationDto appSubmissionRequestInformationDto = new AppSubmissionRequestInformationDto();
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto)CopyUtil.copyMutableObject(appSubmissionDto);
        appSubmissionRequestInformationDto.setOldAppSubmissionDto(oldAppSubmissionDto);
        Double amount = 0.0;
        AuditTrailDto internet = AuditTrailHelper.getCurrentAuditTrailDto();
        appSubmissionDto.setAppGrpId(applicationDto.getAppGrpId());
        ApplicationGroupDto entity1 = applicationClient.getApplicationGroup(applicationDto.getAppGrpId()).getEntity();
        appSubmissionDto.setFromBe(false);
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL);
        appSubmissionDto.setAppGrpNo(entity1.getGroupNo());
        appSubmissionDto.setAmount(amount);
        appSubmissionDto.setAuditTrailDto(internet);
        appSubmissionDto.setPreInspection(true);
        appSubmissionDto.setRequirement(true);
        appSubmissionDto.setLicenseeId(licenseeId);
        appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
        appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
        setRiskToDto(appSubmissionDto);
        List<AppPremisesRoutingHistoryDto> hisList;
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        String gatewayUrl = env.getProperty("iais.inter.gateway.url");
        Map<String, Object> params = IaisCommonUtils.genNewHashMap(1);
        params.put("appNo", applicationDto.getApplicationNo());
        hisList = IaisEGPHelper.callEicGatewayWithParamForList(gatewayUrl + "/v1/app-routing-history", HttpMethod.GET, params,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization(), AppPremisesRoutingHistoryDto.class).getEntity();
        if(hisList!=null){
            for(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto : hisList){
                if(ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION.equals(appPremisesRoutingHistoryDto.getProcessDecision())
                        || InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION.equals(appPremisesRoutingHistoryDto.getProcessDecision())){
                    if(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(appPremisesRoutingHistoryDto.getAppStatus())){
                        applicationDto.setStatus(ApplicationConsts.PENDING_ASO_REPLY);
                    }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING.equals(appPremisesRoutingHistoryDto.getAppStatus())){
                        applicationDto.setStatus(ApplicationConsts.PENDING_PSO_REPLY);
                    }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS.equals(appPremisesRoutingHistoryDto.getAppStatus())){
                        applicationDto.setStatus(ApplicationConsts.PENDING_INP_REPLY);
                    }
                }
            }
        }
        appSubmissionRequestInformationDto.setRfiStatus(applicationDto.getStatus());
        appSubmissionRequestInformationDto.setAppSubmissionDto(appSubmissionDto);
        applicationClient.saveRfcWithdrawSubmission(appSubmissionRequestInformationDto).getEntity();
    }

    @Override
    public WithdrawnDto getWithdrawAppInfo(String appNo) {
        return applicationClient.getWithdrawAppInfo(appNo).getEntity();
    }

    @Override
    public List<WithdrawApplicationDto> getCanWithdrawAppList(List<String[]> appTandS,String licenseeId) {
        List<WithdrawApplicationDto> withdrawApplicationDtoList = applicationClient.getApplicationByAppTypesAndStatus(appTandS,licenseeId).getEntity();
        return withdrawApplicationDtoList;
    }

    private EmailParam sendSms(Map<String, Object> msgInfoMap, ApplicationDto applicationDto) throws IOException, TemplateException {
        log.info(StringUtil.changeForLog("***************** send withdraw application sms  *****************"));
        EmailParam emailParam = new EmailParam();
        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(TEMPLATE_WITHDRAWAL_SMS).getEntity();
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        map.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
        map.put("ApplicationNumber", applicationDto.getApplicationNo());
        String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(),map);
        emailParam.setTemplateId(TEMPLATE_WITHDRAWAL_SMS);
        emailParam.setTemplateContent(msgInfoMap);
        emailParam.setQueryCode(applicationDto.getApplicationNo());
        emailParam.setReqRefNum(applicationDto.getApplicationNo());
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
        emailParam.setRefId(applicationDto.getApplicationNo());
        emailParam.setSubject(subject);
        return emailParam;
    }

    private void sendInboxMessage(ApplicationDto applicationDto,String serviceId,Map<String, Object> map) throws IOException, TemplateException {
        log.info(StringUtil.changeForLog("***************** send withdraw application message  *****************"));
        EmailParam messageParam = new EmailParam();
        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(TEMPLATE_WITHDRAWAL_MESSAGE).getEntity();
        Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
        subMap.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
        subMap.put("ApplicationNumber", applicationDto.getApplicationNo());
        String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(),subMap);
        HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
        List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
        if(serviceDto != null){
            svcCodeList.add(serviceDto.getSvcCode());
        }
        messageParam.setTemplateId(TEMPLATE_WITHDRAWAL_MESSAGE);
        messageParam.setTemplateContent(map);
        messageParam.setQueryCode(applicationDto.getApplicationNo());
        messageParam.setReqRefNum(applicationDto.getApplicationNo());
        messageParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        messageParam.setRefId(applicationDto.getApplicationNo());
        messageParam.setSubject(subject);
        messageParam.setSvcCodeList(svcCodeList);
        notificationHelper.sendNotification(messageParam);
    }

    private EmailParam sendNotification(Map<String, Object> msgInfoMap, ApplicationDto applicationDto) throws IOException, TemplateException {
        log.info(StringUtil.changeForLog("***************** send withdraw application Notification  *****************"));
        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(TEMPLATE_WITHDRAWAL_EMAIL).getEntity();
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        map.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
        map.put("ApplicationNumber", applicationDto.getApplicationNo());
        String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(),map);
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(TEMPLATE_WITHDRAWAL_EMAIL);
        emailParam.setTemplateContent(msgInfoMap);
        emailParam.setQueryCode(applicationDto.getApplicationNo());
        emailParam.setReqRefNum(applicationDto.getApplicationNo());
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        emailParam.setRefId(applicationDto.getApplicationNo());
        emailParam.setSubject(subject);
        log.info(StringUtil.changeForLog("***************** send withdraw application Notification  end*****************"));
        return emailParam;
    }

    private void transform(AppSubmissionDto appSubmissionDto,String licenseeId){
        AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        String serviceId = appSvcRelatedInfoDtoList.get(0).getServiceId();
        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
        String svcId = hcsaServiceDto.getId();
        HcsaServiceDto hcsaServiceDto1 = appConfigClient.getActiveHcsaServiceDtoById(svcId).getEntity();
        String svcCode = hcsaServiceDto.getSvcCode();
        appSvcRelatedInfoDtoList.get(0).setServiceId(hcsaServiceDto1.getId());
        appSvcRelatedInfoDtoList.get(0).setServiceCode(svcCode);
        appSubmissionDto.setAppGrpId(null);
        appSubmissionDto.setFromBe(false);
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL);
        appSubmissionDto.setAmount(0.0);
        appSubmissionDto.setAuditTrailDto(auditTrailDto);
        appSubmissionDto.setPreInspection(true);
        appSubmissionDto.setRequirement(true);
        appSubmissionDto.setLicenseeId(licenseeId);
        appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
        appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
        setRiskToDto(appSubmissionDto);
    }

    private void setRiskToDto(AppSubmissionDto appSubmissionDto) {
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<RiskAcceptiionDto> riskAcceptiionDtoList = IaisCommonUtils.genNewArrayList();
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
            riskAcceptiionDto.setApptype(appSubmissionDto.getAppType());
            riskAcceptiionDto.setScvCode(appSvcRelatedInfoDto.getServiceCode());
            riskAcceptiionDtoList.add(riskAcceptiionDto);
        }
        List<RiskResultDto> riskResultDtoList = appConfigClient.getRiskResult(riskAcceptiionDtoList).getEntity();
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            String serviceCode = appSvcRelatedInfoDto.getServiceCode();
            RiskResultDto riskResultDto = getRiskResultDtoByServiceCode(riskResultDtoList, serviceCode);
            if (riskResultDto != null) {
                appSvcRelatedInfoDto.setDoRiskDate(riskResultDto.getDoRiskDate());
                appSvcRelatedInfoDto.setScore(riskResultDto.getScore());
            }
        }
    }

    private RiskResultDto getRiskResultDtoByServiceCode(List<RiskResultDto> riskResultDtoList, String serviceCode) {
        RiskResultDto result = null;
        if (riskResultDtoList == null || StringUtil.isEmpty(serviceCode)) {
            return result;
        }
        for (RiskResultDto riskResultDto : riskResultDtoList) {
            if (serviceCode.equals(riskResultDto.getSvcCode())) {
                result = riskResultDto;
            }
        }
        return result;
    }

    private String getAppStatus(String serviceId,String appType) {
        String appStatus;
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        List<HcsaSvcRoutingStageDto> serviceConfig = feEicGatewayClient.getServiceConfig(serviceId, appType, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
        if (IaisCommonUtils.isEmpty(serviceConfig)) {
            return null;
        } else {
            String stageCode = serviceConfig.get(0).getStageCode();
            switch (stageCode) {
                case RoleConsts.USER_ROLE_ASO:
                    appStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING;
                    break;
                case RoleConsts.USER_ROLE_INSPECTIOR:
                    appStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION;
                    break;
                case RoleConsts.USER_ROLE_PSO:
                    appStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING;
                    break;
                case RoleConsts.USER_ROLE_AO1:
                    appStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01;
                    break;
                case RoleConsts.USER_ROLE_AO2:
                    appStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02;
                    break;
                default:
                    appStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
            }
        }
        return appStatus;
    }
}