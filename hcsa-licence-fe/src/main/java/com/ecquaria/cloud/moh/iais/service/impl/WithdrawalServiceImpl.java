package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.service.client.LicEicClient;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspRectificationSaveDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.WithdrawalService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FeMessageClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloud.submission.client.App;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class WithdrawalServiceImpl implements WithdrawalService {

    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;

    @Autowired
    private SystemAdminClient systemAdminClient;

    @Autowired
    private MsgTemplateClient msgTemplateClient;

    @Autowired
    private LicEicClient licEicClient;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private CessationClient cessationClient;

    @Autowired
    private FeMessageClient feMessageClient;

    @Autowired
    private AppConfigClient appConfigClient;

    @Autowired
    private FeEicGatewayClient feEicGatewayClient;

    @Autowired
    private Environment env;

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

    public static final String TEMPLATE_WITHDRAWAL_ID         = "0DC58FEB-CF98-EA11-BE7A-000C29D29DB0";

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
        if (!IaisCommonUtils.isEmpty(withdrawnList)){
            withdrawnDtoList.forEach(h -> {
                AppSubmissionDto appSubmissionDto = applicationClient.gainSubmissionDto(h.getApplicationNo()).getEntity();
                if (appSubmissionDto != null){
                    String serviceId = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceId();
                    if (!StringUtil.isEmpty(serviceId)){
                        String serviceName = HcsaServiceCacheHelper.getServiceById(serviceId).getSvcName();
                        Map<String, Object> msgInfoMap = IaisCommonUtils.genNewHashMap();
                        msgInfoMap.put("reqAppNo",h.getApplicationNo());
                        msgInfoMap.put("S_LName",serviceName);
                        msgInfoMap.put("MOH_AGENCY_NAME",AppConsts.MOH_AGENCY_NAME);
                        try {
                            EmailDto emailDto = sendNotification(TEMPLATE_WITHDRAWAL_ID,msgInfoMap,h.getApplicationNo(),h.getLicenseeId());
                            sendInboxMessage(h.getApplicationNo(),h.getLicenseeId(),null,emailDto.getContent(),serviceId,emailDto.getSubject());
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
                e.printStackTrace();
            }
        });
        List<String> withdrawnList = cessationClient.saveWithdrawn(withdrawnDtoList).getEntity();
    }

    private void transformRfi(AppSubmissionDto appSubmissionDto, String licenseeId, ApplicationDto applicationDto) throws Exception {
        AppSubmissionRequestInformationDto appSubmissionRequestInformationDto = new AppSubmissionRequestInformationDto();
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto)CopyUtil.copyMutableObject(appSubmissionDto);
        appSubmissionRequestInformationDto.setOldAppSubmissionDto(oldAppSubmissionDto);
        Double amount = 0.0;
        AuditTrailDto internet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTERNET);
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
        appSubmissionDto.setStatus(applicationDto.getStatus());
        appSubmissionRequestInformationDto.setAppSubmissionDto(appSubmissionDto);
        applicationClient.saveRfcWithdrawSubmission(appSubmissionRequestInformationDto).getEntity();
    }

    @Override
    public WithdrawnDto getWithdrawAppInfo(String appNo) {
        return applicationClient.getWithdrawAppInfo(appNo).getEntity();
    }

    @Override
    public List<WithdrawApplicationDto> getCanWithdrawAppList(List<String[]> appTandS) {
        List<WithdrawApplicationDto> withdrawApplicationDtoList = applicationClient.getApplicationByAppTypesAndStatus(appTandS).getEntity();
        return withdrawApplicationDtoList;
    }

    private void sendInboxMessage(String applicationNo,String licenseeId,HashMap<String, String> maskParams,String templateMessageByContent,String serviceId,String subject){
        InterMessageDto interMessageDto = new InterMessageDto();
        HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
        interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
        interMessageDto.setSubject(subject);
        interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_NOTIFICATION);
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        String refNo = feEicGatewayClient.getMessageNo(signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
        interMessageDto.setRefNo(refNo);
        if(serviceDto != null){
            interMessageDto.setService_id(serviceDto.getSvcCode()+"@");
        }
        interMessageDto.setUserId(licenseeId);
        interMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        interMessageDto.setMsgContent(templateMessageByContent);
        interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        interMessageDto.setMaskParams(maskParams);
        feMessageClient.createInboxMessage(interMessageDto);
    }

    private EmailDto sendNotification(String msgId, Map<String, Object> msgInfoMap, String applicationNo, String licenseeId) throws IOException, TemplateException {
        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(msgId).getEntity();
        String templateMessageByContent = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), msgInfoMap);
        EmailDto emailDto=new EmailDto();
        emailDto.setClientQueryCode(applicationNo);
        emailDto.setSender(mailSender);
        emailDto.setContent(templateMessageByContent);
        emailDto.setSubject(msgTemplateDto.getTemplateName()+applicationNo);
        List<String> licenseeEmailAddrs = IaisEGPHelper.getLicenseeEmailAddrs(licenseeId);
        if(licenseeEmailAddrs!=null){
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            emailDto.setReceipts(licenseeEmailAddrs);
            try {
                EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.WithdrawalServiceImpl", "sendNotification",
                        "hcsa-licence-web-internet", InspRectificationSaveDto.class.getName(), JsonUtil.parseToJson(emailDto));
                String eicRefNo = eicRequestTrackingDto.getRefNo();
                feEicGatewayClient.feSendEmail(emailDto,signature.date(), signature.authorization(),
                        signature2.date(), signature2.authorization());
                //get eic record
                eicRequestTrackingDto = licEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
                //update eic record status
                eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
                eicRequestTrackingDtos.add(eicRequestTrackingDto);
                licEicClient.updateStatus(eicRequestTrackingDtos);
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }
        return emailDto;
    }

    private void transform(AppSubmissionDto appSubmissionDto,String licenseeId){
        AuditTrailDto auditTrailDto = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTERNET);
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