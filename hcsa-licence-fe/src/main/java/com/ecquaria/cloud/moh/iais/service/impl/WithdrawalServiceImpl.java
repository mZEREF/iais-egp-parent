package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.client.LicEicClient;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
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
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.WithdrawalService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FeMessageClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
            String licenseeId = h.getLicenseeId();
            AppSubmissionDto appSubmissionDto = applicationClient.getAppSubmissionDto(h.getApplicationNo()).getEntity();
            transform(appSubmissionDto,licenseeId);
            AppSubmissionDto newAppSubmissionDto = applicationClient.saveSubmision(appSubmissionDto).getEntity();
            AppSubmissionDto appSubmissionDtoSave = applicationClient.saveApps(newAppSubmissionDto).getEntity();
            List<ApplicationDto> applicationDtoList = appSubmissionDtoSave.getApplicationDtos();
            String appId = applicationDtoList.get(0).getId();
            h.setApplicationId(appId);
        });
        List<String> withdrawnList = cessationClient.saveWithdrawn(withdrawnDtoList).getEntity();
        if (!IaisCommonUtils.isEmpty(withdrawnList)){
            withdrawnDtoList.forEach(h -> {
                AppSubmissionDto appSubmissionDto = applicationClient.getAppSubmissionDto(h.getApplicationNo()).getEntity();
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
                        } catch (IOException e) {
                            log.error(e.getMessage(), e);
                        } catch (TemplateException e) {
                            log.error(e.getMessage(), e);
                        }
                    }
                }
            });
        }
    }

    private void sendInboxMessage(String applicationNo,String licenseeId,HashMap<String, String> maskParams,String templateMessageByContent,String serviceId,String subject){
        InterMessageDto interMessageDto = new InterMessageDto();
        interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
        interMessageDto.setSubject(subject);
        interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_NOTIFICATION);
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        String refNo = feEicGatewayClient.getMessageNo(signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
        interMessageDto.setRefNo(refNo);
        interMessageDto.setService_id(serviceId);
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
        String grpNo = systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL).getEntity();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        String serviceId = appSvcRelatedInfoDtoList.get(0).getServiceId();
        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
        String svcId = hcsaServiceDto.getId();
        String svcCode = hcsaServiceDto.getSvcCode();
        appSvcRelatedInfoDtoList.get(0).setServiceId(svcId);
        appSvcRelatedInfoDtoList.get(0).setServiceCode(svcCode);
        appSubmissionDto.setAppGrpId(null);
        appSubmissionDto.setAppGrpNo(grpNo);
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
}