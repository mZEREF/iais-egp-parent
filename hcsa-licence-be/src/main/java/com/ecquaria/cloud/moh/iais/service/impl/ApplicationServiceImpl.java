package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppFeeDetailsDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryExtDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.EventApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RequestInformationSubmitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.SelfAssMtEmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MessageTemplateUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.utils.TaskUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.AppealApplicaionService;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.BroadcastService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.EicClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskOrganizationClient;
import com.ecquaria.cloud.moh.iais.util.EicUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sop.util.CopyUtil;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * ApplicationServiceImpl
 *
 * @author suocheng
 * @date 11/28/2019
 */
@Service
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private AppInspectionStatusClient appInspectionStatusClient;

    @Autowired
    private AppPremisesCorrClient appPremisesCorrClient;

    @Autowired
    private EmailClient emailClient;

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;

    @Autowired
    private InboxMsgService inboxMsgService;

    @Autowired
    CessationClient cessationClient;

    @Autowired
    private TaskOrganizationClient taskOrganizationClient;

    @Autowired
    MsgTemplateClient msgTemplateClient;

    @Autowired
    private BroadcastService broadcastService;

    @Autowired
    private GenerateIdClient generateIdClient;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private EicClient eicClient;

    @Autowired
    private AppealApplicaionService appealApplicaionService;
    @Autowired
    private OrganizationClient organizationClient;

    @Value("${spring.application.name}")
    private String currentApp;
    @Value("${iais.current.domain}")
    private String currentDomain;

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

    @Autowired
    private NotificationHelper notificationHelper;

    @Override
    public List<ApplicationDto> getApplicaitonsByAppGroupId(String appGroupId) {
        return applicationClient.getGroupAppsByNo(appGroupId).getEntity();
    }

    @Override
    public boolean isOtherApplicaitonSubmit(List<ApplicationDto> applicationDtoList,String appNo,String status) {
        if(IaisCommonUtils.isEmpty(applicationDtoList)|| StringUtil.isEmpty(appNo) || StringUtil.isEmpty(status)){
            return  false;
        }
        boolean result = true;
        Map<String,List<ApplicationDto>> applicationMap = tidyApplicationDto(applicationDtoList);
        if(applicationMap!=null && applicationMap.size()>0){
          for (Map.Entry<String,List<ApplicationDto>> entry : applicationMap.entrySet()){
              String key = entry.getKey();
              List<ApplicationDto> value = entry.getValue();
              if(appNo.equals(key)){
                  continue;
              }else if(!containStatus(value,status)){
                  result = false;
                  break;
              }

          }
          for(ApplicationDto applicationDto : applicationDtoList){
              applicationDto.setStatus(status);
          }
        }
        return result;
    }

    @Override
    public boolean isOtherApplicaitonSubmit(List<ApplicationDto> applicationDtoList,String appNo,String status, boolean updateStauts) {
        if(IaisCommonUtils.isEmpty(applicationDtoList)|| StringUtil.isEmpty(appNo) || StringUtil.isEmpty(status)){
            return  false;
        }
        boolean result = true;
        Map<String,List<ApplicationDto>> applicationMap = tidyApplicationDto(applicationDtoList);
        if(applicationMap!=null && applicationMap.size()>0){
            for (Map.Entry<String,List<ApplicationDto>> entry : applicationMap.entrySet()){
                String key = entry.getKey();
                List<ApplicationDto> value = entry.getValue();
                if(appNo.equals(key)){
                    continue;
                }else if(!containStatus(value,status)){
                    result = false;
                    break;
                }

            }
            if(updateStauts){
                for(ApplicationDto applicationDto : applicationDtoList){
                    applicationDto.setStatus(status);
                }
            }
        }
        return result;
    }

    @Override
    public boolean isOtherApplicaitonSubmit(List<ApplicationDto> applicationDtoList,String appNo,String status1, String status2) {
        if(IaisCommonUtils.isEmpty(applicationDtoList)|| StringUtil.isEmpty(appNo) || StringUtil.isEmpty(status1) || StringUtil.isEmpty(status2)){
            return  false;
        }
        boolean result = true;
        Map<String,List<ApplicationDto>> applicationMap = tidyApplicationDto(applicationDtoList);
        if(applicationMap!=null && applicationMap.size()>0){
            for (Map.Entry<String,List<ApplicationDto>> entry : applicationMap.entrySet()){
                String key = entry.getKey();
                List<ApplicationDto> value = entry.getValue();
                if(appNo.equals(key)){
                    continue;
                }else if(!(containStatus(value,status1) || containStatus(value,status2))){
                    result = false;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public List<AppPremisesCorrelationDto> getAppPremisesCorrelationByAppGroupId(String appGroupId) {
        return appPremisesCorrClient.getGroupAppsByNo(appGroupId).getEntity();
    }

    @Override
    public Integer getAppBYGroupIdAndStatus(String appGroupId, String status) {
        return applicationClient.getAppCountByGroupIdAndStatus(appGroupId,status).getEntity();
    }

    @Override
    public ApplicationDto updateBEApplicaiton(ApplicationDto applicationDto) {
        return applicationClient.updateApplication(applicationDto).getEntity();
    }

    public ApplicationDto callEicInterApplication(ApplicationDto applicationDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return beEicGatewayClient.updateApplication(applicationDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
    }

    @Override
    public List<ApplicationDto> updateFEApplicaitons(List<ApplicationDto> applicationDtos){
        for(ApplicationDto applicationdto : applicationDtos){
            updateFEApplicaiton(applicationdto);
        }
        return applicationDtos;
    }

    @Override
    public List<PaymentRequestDto> eicFeStripeRefund(List<AppReturnFeeDto> appReturnFeeDtos) {
        log.info(StringUtil.changeForLog("The updateFEPaymentRefund start ..."));
        String moduleName = currentApp + "-" + currentDomain;
        EicRequestTrackingDto dto = new EicRequestTrackingDto();
        dto.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
        dto.setActionClsName(this.getClass().getName());
        dto.setActionMethod("callEicInterPaymentRefund");
        dto.setDtoClsName(List.class.getName());
        dto.setDtoObject(JsonUtil.parseToJson(appReturnFeeDtos));
        String refNo = String.valueOf(System.currentTimeMillis());
        log.info(StringUtil.changeForLog("The updateFEPaymentRefund refNo is  -- >:"+refNo));
        dto.setRefNo(refNo);
        dto.setModuleName(moduleName);
        eicClient.saveEicTrack(dto);
        List<PaymentRequestDto> paymentRequestDtos=callEicInterPaymentRefund(appReturnFeeDtos);
        dto = eicClient.getPendingRecordByReferenceNumber(refNo).getEntity();
        Date now = new Date();
        dto.setProcessNum(1);
        dto.setFirstActionAt(now);
        dto.setLastActionAt(now);
        dto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        List<EicRequestTrackingDto> list = IaisCommonUtils.genNewArrayList(1);
        list.add(dto);
        eicClient.updateStatus(list);
        log.info(StringUtil.changeForLog("The updateFEPaymentRefund end ..."));
        return paymentRequestDtos;
    }

    private List<PaymentRequestDto> callEicInterPaymentRefund(List<AppReturnFeeDto> appReturnFeeDtos) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return beEicGatewayClient.doStripeRefunds(appReturnFeeDtos, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
    }


    @Override
    public ApplicationDto updateFEApplicaiton(ApplicationDto applicationDto) {
        log.info(StringUtil.changeForLog("The updateFEApplicaiton start ..."));
        String moduleName = currentApp + "-" + currentDomain;
        EicRequestTrackingDto dto = new EicRequestTrackingDto();
        dto.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
        dto.setActionClsName(this.getClass().getName());
        dto.setActionMethod("callEicInterApplication");
        dto.setDtoClsName(applicationDto.getClass().getName());
        dto.setDtoObject(JsonUtil.parseToJson(applicationDto));
        String refNo = String.valueOf(System.currentTimeMillis());
        log.info(StringUtil.changeForLog("The updateFEApplicaiton refNo is  -- >:"+refNo));
        dto.setRefNo(refNo);
        dto.setModuleName(moduleName);
        eicClient.saveEicTrack(dto);
        callEicInterApplication(applicationDto);
        dto = eicClient.getPendingRecordByReferenceNumber(refNo).getEntity();
        Date now = new Date();
        dto.setProcessNum(1);
        dto.setFirstActionAt(now);
        dto.setLastActionAt(now);
        dto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
        List<EicRequestTrackingDto> list = IaisCommonUtils.genNewArrayList(1);
        list.add(dto);
        eicClient.updateStatus(list);
        log.info(StringUtil.changeForLog("The updateFEApplicaiton end ..."));
        return applicationDto;
    }

    @Override
    public List<RequestInformationSubmitDto> getRequestInformationSubmitDtos(List<ApplicationDto> applicationDtos) {
        return  applicationClient.getRequestInformationSubmitDto(applicationDtos).getEntity();
    }

    @Override
    public List<AppEditSelectDto> getAppEditSelectDtos(String appId, String changeType) {
        return applicationClient.getAppEditSelectDto(appId, changeType).getEntity();
    }

    @Override
    public void postInspectionBack() {
        String appType = ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION;
        String appStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPOINTMENT_SCHEDULING;
        List<ApplicationDto> postApps = applicationClient.getPostApplication(appType, appStatus).getEntity();
        //ApplicationDto applicationDto, String statgId,String roleId,String correlationId
    }

    private void sendChecklistReminder(String queryCode, String emailId, String noticeId, String smsId, List<SelfAssMtEmailDto> allAssLt){
        for (SelfAssMtEmailDto i : allAssLt) {
            Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
            String reqRefNum;
            String refType;
            String msgReqRefNum;
            String tlGroupNumber = "-";
            String tlAppType = "-";
            String tlSvcName = "-";
            String licenseeId = i.getLicenseeId();
            String inspDate = Formatter.formatDate(i.getInspectionDate());
            List<ApplicationDto> appList;
            String randomStr = IaisEGPHelper.generateRandomString(26);
            int msgTrackRefNumType = i.getMsgTrackRefNumType();

            List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
            //pending submit self ass mt
            String appGrpId;
            if (msgTrackRefNumType == 1) {
                refType = NotificationHelper.RECEIPT_TYPE_APP_GRP;
                msgReqRefNum = i.getGroupId();
                reqRefNum = i.getGroupId();
                appGrpId = i.getGroupId();
                appList = i.getAppList();
                boolean flag = false;
                List<String> svcNames = IaisCommonUtils.genNewArrayList();
                if (IaisCommonUtils.isNotEmpty(appList)) {
                    for (ApplicationDto app : appList) {
                        if (!flag) {
                            String appNum = app.getApplicationNo();
                            String[] split = appNum.split("-");

                            String appType = MasterCodeUtil.getCodeDesc(app.getApplicationType());
                            Date appSubmitDate = app.getStartDate();

                            tlGroupNumber = split[0];
                            tlAppType = appType;
                            templateContent.put("applicationNumber", StringUtil.viewHtml(tlGroupNumber));
                            templateContent.put("applicationType", StringUtil.viewHtml(appType));
                            templateContent.put("applicationDate", StringUtil.viewHtml(Formatter.formatDate(appSubmitDate)));
                            flag = true;
                        }

                        String svcId = app.getServiceId();
                        HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceById(svcId);
                        if (Optional.ofNullable(serviceDto).isPresent()) {
                            String svcName = serviceDto.getSvcName();
                            String svcCode = serviceDto.getSvcCode();
                            tlSvcName = svcName;

                            if (!svcCodeList.contains(svcCode)){
                                svcCodeList.add(svcCode);
                            }

                            if (!svcNames.contains(svcName)){
                                svcNames.add(svcName);
                            }
                        }
                    }

                    templateContent.put("serviceNames", svcNames);
                }
            } else {
                // uncompleted self ass mt
                refType = NotificationHelper.RECEIPT_TYPE_APP;
                appList = i.getAppList();
                //never null
                ApplicationDto app = appList.get(0);

                appGrpId = app.getAppGrpId();

                tlGroupNumber = app.getApplicationNo();
                tlAppType = MasterCodeUtil.getCodeDesc(app.getApplicationType());

                reqRefNum = app.getApplicationNo();
                msgReqRefNum = String.valueOf(app.getEndDate().getTime());
                templateContent.put("applicationNumber", reqRefNum);
                templateContent.put("applicationType", MasterCodeUtil.getCodeDesc(app.getApplicationType()));
                templateContent.put("applicationDate", Formatter.formatDate(app.getStartDate()));

                List<String> svcNames = IaisCommonUtils.genNewArrayList();
                HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceById(app.getServiceId());
                if (Optional.ofNullable(serviceDto).isPresent()) {
                    String svcName = serviceDto.getSvcName();
                    String svcCode = serviceDto.getSvcCode();
                    tlSvcName = svcName;

                    if (!svcCodeList.contains(svcCode)){
                        svcCodeList.add(svcCode);
                    }

                    if (!svcNames.contains(svcName)){
                        svcNames.add(svcName);
                    }

                }
                templateContent.put("serviceNames", svcNames);
            }

            String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + "/main-web/";
            //EN-NAP-008
            String today = Formatter.formatDate(new Date());

            ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(appGrpId).getEntity();
            if (Optional.ofNullable(applicationGroupDto).isPresent()){
                OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
                if (Optional.ofNullable(orgUserDto).isPresent()){
                    templateContent.put("ApplicantName", StringUtil.viewHtml(orgUserDto.getDisplayName()));
                }
            }

            templateContent.put("MOH_AGENCY_NAM_GROUP",AppConsts.MOH_AGENCY_NAM_GROUP);
            templateContent.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);
            templateContent.put("emailAddress", systemParamConfig.getSystemAddressOne());
            templateContent.put("tatTime", inspDate);
            templateContent.put("reminderDate", today);
            templateContent.put("systemLink", loginUrl);
            templateContent.put("officer_name", "officer_name");
            templateContent.put("telPhone", systemParamConfig.getSystemPhoneNumber());

            if (StringUtil.isNotEmpty(licenseeId)){
                LicenseeDto licensee = organizationClient.getLicenseeDtoById(licenseeId).getEntity();
                if (Optional.ofNullable(licensee).isPresent()){
                    templateContent.put("licenseeEmailAddress", licensee.getEmilAddr());
                    templateContent.put("licenseeTelPhone", licensee.getOfficeTelNo());
                }
            }


            JobRemindMsgTrackingDto jobRemindMsgTrackingDto = new JobRemindMsgTrackingDto();
            jobRemindMsgTrackingDto.setMsgKey(i.getMsgTrackKey());
            jobRemindMsgTrackingDto.setRefNo(msgReqRefNum);
            jobRemindMsgTrackingDto.setCreateTime(new Date());
            jobRemindMsgTrackingDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            HashMap<String, String> subjectParams = IaisCommonUtils.genNewHashMap();
            subjectParams.put("{applicationNo}", tlGroupNumber);
            subjectParams.put("{appType}", tlAppType);
            subjectParams.put("{serviceName}", tlSvcName);
            EmailParam emailParam = new EmailParam();
            emailParam.setJobRemindMsgTrackingDto(jobRemindMsgTrackingDto);
            emailParam.setSubjectParams(subjectParams);
            emailParam.setTemplateId(emailId);
            emailParam.setQueryCode(queryCode);
            emailParam.setReqRefNum(randomStr);
            emailParam.setRefIdType(refType);
            emailParam.setRefId(reqRefNum);
            emailParam.setSvcCodeList(svcCodeList);

            //send to inspector
            if (HcsaChecklistConstants.SELF_ASS_MT_EMAIL_TO_CURRENT_INSPECTOR_FOR_BATCH_JOB.equals(queryCode)){
                emailParam.setModuleType(NotificationHelper.OFFICER_MODULE_TYPE_INSPECTOR_BY_CURRENT_TASK);
                JobRemindMsgTrackingDto firReminderRecord = msgTemplateClient.getJobRemindMsgTrackingDto(msgReqRefNum, HcsaChecklistConstants.SELF_ASS_MT_REMINDER__MSG_KEY_FIR).getEntity();
                JobRemindMsgTrackingDto secReminderRecord = msgTemplateClient.getJobRemindMsgTrackingDto(msgReqRefNum, HcsaChecklistConstants.SELF_ASS_MT_REMINDER__MSG_KEY_SEC).getEntity();if (Optional.ofNullable(firReminderRecord).isPresent() && Optional.ofNullable(secReminderRecord).isPresent()){
                    if (Optional.ofNullable(firReminderRecord).isPresent() && Optional.ofNullable(secReminderRecord).isPresent()){
                        templateContent.put("inspReminderStartDate", Formatter.formatDate(firReminderRecord.getCreateTime()));
                        templateContent.put("inspReminderEndDate", Formatter.formatDate(secReminderRecord.getCreateTime()));
                    }else {
                        log.info("break send reminder email to inspector , because the applicant has not been reminded");
                        continue;
                    }
                }
            }

            emailParam.setTemplateContent(templateContent);
            notificationHelper.sendNotification(emailParam);

            //send notification and SMS
            if (IaisCommonUtils.isNotEmpty(appList)){
                ApplicationDto applicationDto = appList.get(0);
                if (StringUtil.isNotEmpty(noticeId)){
                    EmailParam smsParam = MiscUtil.transferEntityDto(emailParam, EmailParam.class);
                    smsParam.setTemplateId(noticeId);
                    smsParam.setRefId(applicationDto.getApplicationNo());
                    smsParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                    notificationHelper.sendNotification(smsParam);
                }

                if (StringUtil.isNotEmpty(smsId)){
                    EmailParam msgParam = MiscUtil.transferEntityDto(emailParam, EmailParam.class);
                    msgParam.setTemplateId(smsId);
                    msgParam.setRefId(applicationDto.getApplicationNo());
                    msgParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
                    notificationHelper.sendNotification(msgParam);
                }
            }

            log.info("===>>>>alertSelfDeclNotification end");
        }
    }

    @Override
    public void alertSelfDeclNotification() {
        //These emails will only be reminded three times at different times, see database table -> smemail.notification
        List<SelfAssMtEmailDto> email_008 = applicationClient.getPendingSubmitSelfAss(HcsaChecklistConstants.SELF_ASS_MT_REMINDER__MSG_KEY).getEntity();
        sendChecklistReminder(HcsaChecklistConstants.SELF_ASS_MT_REMINDER__MSG_KEY, MsgTemplateConstants.MSG_TEMPLATE_REMINDER_SELF_ASS_MT, MsgTemplateConstants.MSG_TEMPLATE_REMINDER_SELF_ASS_MT_NOTIC, MsgTemplateConstants.MSG_TEMPLATE_REMINDER_SELF_ASS_MT_SMS , email_008);

        List<SelfAssMtEmailDto> email_001 = applicationClient.getPendingSubmitSelfAss(HcsaChecklistConstants.SELF_ASS_MT_REMINDER__MSG_KEY_FIR).getEntity();
        sendChecklistReminder(HcsaChecklistConstants.SELF_ASS_MT_REMINDER__MSG_KEY_FIR, MsgTemplateConstants.MSG_TEMPLATE_SELF_ASS_MT_REMINDER_FIR, MsgTemplateConstants.MSG_TEMPLATE_SELF_ASS_MT_REMINDER_FIR_NOTICE, MsgTemplateConstants.MSG_TEMPLATE_SELF_ASS_MT_REMINDER_FIR_SMS, email_001);

        List<SelfAssMtEmailDto> email_002 = applicationClient.getPendingSubmitSelfAss(HcsaChecklistConstants.SELF_ASS_MT_REMINDER__MSG_KEY_SEC).getEntity();
        sendChecklistReminder(HcsaChecklistConstants.SELF_ASS_MT_REMINDER__MSG_KEY_SEC, MsgTemplateConstants.MSG_TEMPLATE_SELF_ASS_MT_REMINDER_SEC, MsgTemplateConstants.MSG_TEMPLATE_SELF_ASS_MT_REMINDER_SEC_NOTICE, MsgTemplateConstants.MSG_TEMPLATE_SELF_ASS_MT_REMINDER_SEC_SMS, email_002);

        List<SelfAssMtEmailDto> email_to_inspecotr_004 = applicationClient.getPendingSubmitSelfAss(HcsaChecklistConstants.SELF_ASS_MT_EMAIL_TO_CURRENT_INSPECTOR_FOR_BATCH_JOB).getEntity();
         sendChecklistReminder(HcsaChecklistConstants.SELF_ASS_MT_EMAIL_TO_CURRENT_INSPECTOR_FOR_BATCH_JOB, MsgTemplateConstants.MSG_TEMPLATE_SELF_ASS_MT_REMINDER_TO_INSPECTOR, "", MsgTemplateConstants.MSG_TEMPLATE_SELF_ASS_MT_REMINDER_TO_INSPECTOR_SMS, email_to_inspecotr_004);
    }

    @Override
    public void appealRfiAndEmail(ApplicationViewDto applicationViewDto,ApplicationDto applicationDto,HashMap<String, String> maskParams,String linkURL,String externalRemarks)throws Exception{
        MsgTemplateDto autoEntity = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI).getEntity();
        Map<String ,Object> map=IaisCommonUtils.genNewHashMap();
        String applicationNo = applicationDto.getApplicationNo();
        String applicantName = "";
        int rfiDueDate = systemParamConfig.getRfiDueDate();
        LocalDate tatTime = LocalDate.now().plusDays(rfiDueDate);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Formatter.DATE);
        String tatTimeStr = tatTime.format(dtf);
        ApplicationGroupDto applicationGroupDto = applicationViewDto.getApplicationGroupDto();
        if(applicationGroupDto != null){
            OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
            if(orgUserDto != null){
                applicantName = orgUserDto.getDisplayName();
            }
        }
        String messageNo = inboxMsgService.getMessageNo();
        map.put("ApplicantName",applicantName);
        map.put("ApplicationType",MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
        map.put("ApplicationNumber",StringUtil.viewHtml(applicationNo));
        map.put("ApplicationDate",Formatter.formatDateTime( new Date(), Formatter.DATE));
        map.put("Remarks","");
        map.put("COMMENTS",externalRemarks);
        map.put("systemLink",linkURL);
        map.put("TATtime",tatTimeStr);
        map.put("email",systemParamConfig.getSystemAddressOne());
        map.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
        map.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
        String templateMessageByContent = MsgUtil.getTemplateMessageByContent(autoEntity.getMessageContent(), map);
        templateMessageByContent = MessageTemplateUtil.replaceNum(templateMessageByContent);
        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(applicationDto.getServiceId());
        Map<String, Object> subjectMap = IaisCommonUtils.genNewHashMap();
        subjectMap.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
        subjectMap.put("ApplicationNumber", applicationDto.getApplicationNo());
        String subject = MsgUtil.getTemplateMessageByContent(autoEntity.getTemplateName(),subjectMap);
        if(hcsaServiceDto!=null ){
            InterMessageDto interMessageDto = MessageTemplateUtil.getInterMessageDto(subject,MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED,
                    messageNo,hcsaServiceDto.getSvcCode()+"@",templateMessageByContent, applicationViewDto.getApplicationGroupDto().getLicenseeId(),IaisEGPHelper.getCurrentAuditTrailDto());
            interMessageDto.setMaskParams(maskParams);
            inboxMsgService.saveInterMessage(interMessageDto);
            //send email
            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_APP_RFI_EMAIL);
            String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
            map.put("systemLink",loginUrl);
            emailParam.setTemplateContent(map);
            emailParam.setQueryCode(applicationNo);
            emailParam.setReqRefNum(applicationNo);
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
            emailParam.setRefId(applicationNo);
            emailParam.setSubject(subject);
            log.info(StringUtil.changeForLog("send rfi application email start"));
            notificationHelper.sendNotification(emailParam);
            log.info(StringUtil.changeForLog("send rfi application email end"));
            //send sms
            EmailParam smsParam = new EmailParam();
            Map<String,Object> smsMap = IaisCommonUtils.genNewHashMap();
            smsMap.put("ApplicationType",MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
            smsMap.put("ApplicationNumber",StringUtil.viewHtml(applicationNo));
            smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_ADHOC_RFI_SMS);
            smsParam.setTemplateContent(smsMap);
            smsParam.setSubject(subject);
            smsParam.setQueryCode(applicationNo);
            smsParam.setReqRefNum(applicationNo);
            smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
            smsParam.setRefId(applicationNo);
            log.info(StringUtil.changeForLog("send rfi application sms start"));
            notificationHelper.sendNotification(smsParam);
            log.info(StringUtil.changeForLog("send rfi application sms end"));
        }
    }

    @Override
    public void applicationRfiAndEmail(ApplicationViewDto applicationViewDto, ApplicationDto applicationDto,
                                       LoginContext loginContext, String externalRemarks) throws IOException, TemplateException {
        String applicantName = "";
        ApplicationGroupDto applicationGroupDto = applicationViewDto.getApplicationGroupDto();
        if(applicationGroupDto != null){
            OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
            if(orgUserDto != null){
                applicantName = orgUserDto.getDisplayName();
            }
        }
        //send message to FE user.
        String messageNo = inboxMsgService.getMessageNo();
        String applicationType = applicationDto.getApplicationType();
        String url = HmacConstants.HTTPS +"://"+systemParamConfig.getInterServerName()+ MessageConstants.MESSAGE_CALL_BACK_URL_NEWAPPLICATION+applicationDto.getApplicationNo();
        String editSelect = "";
        //judge premises amend or licence amend
        AppEditSelectDto appEditSelectDto = applicationViewDto.getAppEditSelectDto();
        //Request For Change
        boolean rfcFlag = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationDto.getApplicationType());
        if((appEditSelectDto != null) && rfcFlag){
            if(appEditSelectDto.isPremisesListEdit()){
                url = HmacConstants.HTTPS +"://"+systemParamConfig.getInterServerName()+MessageConstants.MESSAGE_CALL_BACK_URL_PREMISES_LIST+applicationDto.getApplicationNo();
            }
        }
        //0065135
        if(appEditSelectDto != null){
            if(appEditSelectDto.isPremisesEdit()){
                editSelect = editSelect + "Premises";
            }
            if(appEditSelectDto.isDocEdit()){
                editSelect = editSelect +(StringUtil.isEmpty(editSelect)?"":", ") +"Primary Documents";
            }
            if(appEditSelectDto.isServiceEdit()){
                editSelect = editSelect + (StringUtil.isEmpty(editSelect)?"":", ") +"Service Related Information - " + applicationViewDto.getServiceType();
            }
            if(appEditSelectDto.isPoEdit()){
                editSelect = editSelect +(StringUtil.isEmpty(editSelect)?"":", ") + "PO";
            }
            if(appEditSelectDto.isDpoEdit()){
                editSelect = editSelect +(StringUtil.isEmpty(editSelect)?"":", ") + "DPO";
            }
            if(appEditSelectDto.isMedAlertEdit()){
                editSelect = editSelect +(StringUtil.isEmpty(editSelect)?"":", ") + "medAlert";
            }
        }
        String applicationNo = applicationViewDto.getApplicationDto().getApplicationNo();
        MsgTemplateDto autoEntity = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_APP_RFI_MSG).getEntity();
        Date now = new Date();
        if(applicationViewDto.getApplicationGroupDto() != null && applicationViewDto.getApplicationGroupDto().getSubmitDt() != null){
            now = applicationViewDto.getApplicationGroupDto().getSubmitDt();
        }
        int rfiDueDate = systemParamConfig.getRfiDueDate();
        LocalDate tatTime = LocalDate.now().plusDays(rfiDueDate);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(Formatter.DATE);
        String tatTimeStr = tatTime.format(dtf);
        String remarks = "<br/>Sections Allowed for Change : "+editSelect;
        Map<String ,Object> map=IaisCommonUtils.genNewHashMap();
        map.put("ApplicantName",applicantName);
        map.put("ApplicationType",MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
        map.put("ApplicationNumber",StringUtil.viewHtml(applicationNo));
        map.put("ApplicationDate",Formatter.formatDateTime(now, Formatter.DATE));
        map.put("Remarks",remarks);
        map.put("systemLink",url);
        map.put("COMMENTS",externalRemarks);
        map.put("TATtime",tatTimeStr);
        map.put("email",systemParamConfig.getSystemAddressOne());
        map.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
        map.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
        String templateMessageByContent = MsgUtil.getTemplateMessageByContent(autoEntity.getMessageContent(), map);
        //replace num
        templateMessageByContent = MessageTemplateUtil.replaceNum(templateMessageByContent);
        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(applicationDto.getServiceId());
        boolean isSend = !ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType) && !ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL.equals(applicationType) && !ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(applicationType);
        Map<String,Object> subjectMap = IaisCommonUtils.genNewHashMap();
        subjectMap.put("ApplicationType",MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
        subjectMap.put("ApplicationNumber",StringUtil.viewHtml(applicationNo));
        String msgSubject = MsgUtil.getTemplateMessageByContent(autoEntity.getTemplateName(),subjectMap);
        //autoEntity"MOH IAIS - Request for information for your "+ MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()) + ", " + StringUtil.viewHtml(applicationNo);
        if(hcsaServiceDto!=null && isSend){
            InterMessageDto interMessageDto = MessageTemplateUtil.getInterMessageDto(msgSubject,MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED,
                    messageNo,hcsaServiceDto.getSvcCode()+"@",templateMessageByContent, applicationViewDto.getApplicationGroupDto().getLicenseeId(),IaisEGPHelper.getCurrentAuditTrailDto());
            HashMap<String,String> mapParam = IaisCommonUtils.genNewHashMap();
            mapParam.put("appNo",applicationDto.getApplicationNo());
            interMessageDto.setMaskParams(mapParam);
            inboxMsgService.saveInterMessage(interMessageDto);
            //send email
            MsgTemplateDto emailTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_APP_RFI_EMAIL).getEntity();
            String emailSubject = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getTemplateName(),subjectMap);
            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_APP_RFI_EMAIL);
            String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
            map.put("systemLink",loginUrl);
            emailParam.setTemplateContent(map);
            emailParam.setQueryCode(applicationNo);
            emailParam.setReqRefNum(applicationNo);
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
            emailParam.setRefId(applicationNo);
            emailParam.setSubject(emailSubject);
            log.info(StringUtil.changeForLog("send rfi application email start"));
            notificationHelper.sendNotification(emailParam);
            log.info(StringUtil.changeForLog("send rfi application email end"));
            //send sms
            MsgTemplateDto smsTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_APP_RFI_EMAIL).getEntity();
            String smsSubject = MsgUtil.getTemplateMessageByContent(smsTemplateDto.getTemplateName(),subjectMap);
            EmailParam smsParam = new EmailParam();
            Map<String,Object> smsMap = IaisCommonUtils.genNewHashMap();
            smsMap.put("ApplicationType",MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
            smsMap.put("ApplicationNumber",StringUtil.viewHtml(applicationNo));
            smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_APP_RFI_SMS);
            smsParam.setTemplateContent(smsMap);
            smsParam.setSubject(smsSubject);
            smsParam.setQueryCode(applicationNo);
            smsParam.setReqRefNum(applicationNo);
            smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
            smsParam.setRefId(applicationNo);
            log.info(StringUtil.changeForLog("send rfi application sms start"));
            notificationHelper.sendNotification(smsParam);
            log.info(StringUtil.changeForLog("send rfi application sms end"));
        }
    }

    //send email helper
    private String sendEmailHelper(Map<String ,Object> tempMap,String msgTemplateId,String subject,String licenseeId,String clientQueryCode){
        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(msgTemplateId).getEntity();
        if(tempMap == null || tempMap.isEmpty() || msgTemplateDto == null
                || StringUtil.isEmpty(msgTemplateId)
                || StringUtil.isEmpty(subject)
                || StringUtil.isEmpty(licenseeId)
                || StringUtil.isEmpty(clientQueryCode)){
            return null;
        }
        String mesContext = null;
        try {
            mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), tempMap);
        } catch (IOException | TemplateException e) {
            log.error(e.getMessage(),e);
        }
        EmailDto emailDto = new EmailDto();
        emailDto.setContent(mesContext);
        emailDto.setSubject(" " + msgTemplateDto.getTemplateName() + " " + subject);
        emailDto.setSender(mailSender);
        emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
        emailDto.setClientQueryCode(clientQueryCode);
        //send
        emailClient.sendNotification(emailDto).getEntity();

        return mesContext;
    }

    private boolean containStatus(List<ApplicationDto> applicationDtos,String status){
        boolean result = false;
        if(!IaisCommonUtils.isEmpty(applicationDtos) && !StringUtil.isEmpty(status)){
            for(ApplicationDto applicationDto : applicationDtos){
                if(status.equals(applicationDto.getStatus())
                        || ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED.equals(applicationDto.getStatus())
                        || ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(applicationDto.getStatus())
                        || ApplicationConsts.APPLICATION_STATUS_WITHDRAWN.equals(applicationDto.getStatus())
                        || ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(applicationDto.getStatus())){
                    result = true;
                    break;
                }
            }
        }
        return  result;
    }

    private Map<String,List<ApplicationDto>> tidyApplicationDto(List<ApplicationDto> applicationDtoList){
        Map<String,List<ApplicationDto>> result = null;
        if(!IaisCommonUtils.isEmpty(applicationDtoList)){
            result = IaisCommonUtils.genNewHashMap();
            for(ApplicationDto applicationDto : applicationDtoList){
                String appNo = applicationDto.getApplicationNo();
                List<ApplicationDto> applicationDtos = result.get(appNo);
                if(applicationDtos ==null){
                    applicationDtos = IaisCommonUtils.genNewArrayList();
                }
                applicationDtos.add(applicationDto);
                result.put(appNo,applicationDtos);
            }
        }
        return result;
    }

    @Override
    public ApplicationDto getApplicationBytaskId(String ref){
        return applicationClient.getApplicationBytaskId(ref).getEntity();
    }

    @Override
    public AppFeeDetailsDto getAppFeeDetailsDtoByApplicationNo(String applicationNo) {
        return applicationClient.getAppFeeDetailsDtoByApplicationNo(applicationNo).getEntity();
    }

    @Override
    public AppReturnFeeDto saveAppReturnFee(AppReturnFeeDto appReturnFeeDto) {
        return applicationClient.saveAppReturnFee(appReturnFeeDto).getEntity();
    }

    @Override
    public List<ApplicationDto> getApplicationDtosByApplicationNo(String applicationNo) {
        List<ApplicationDto> entity = applicationClient.getApplicationDtosByApplicationNo(applicationNo).getEntity();
        return entity;
    }

    @Override
    public List<AppEditSelectDto> getAppEditSelectDtosByAppIds(List<String> applicationIds) {
        List<AppEditSelectDto> entity = applicationClient.getAppEditSelectDtosByAppIds(applicationIds).getEntity();
        return entity;
    }

    @Override
    public boolean isWithdrawReturnFee(String appNo) {
        boolean result = false;
        AppReturnFeeDto appReturnFeeDto = applicationClient.getReturnFeeByAppNo(appNo,ApplicationConsts.APPLICATION_RETURN_FEE_TYPE_WITHDRAW).getEntity();
        if (appReturnFeeDto == null){
            result = true;
        }
        return result;
    }

    @Override
    public boolean closeTaskWhenWhAppApprove(String appId) {
        boolean result = false;
        AppPremiseMiscDto premiseMiscDto = cessationClient.getAppPremiseMiscDtoByAppId(appId).getEntity();
        if (premiseMiscDto != null){
            String oldApplicationId = premiseMiscDto.getRelateRecId();
            if (!StringUtil.isEmpty(oldApplicationId)){
                List<String> applicationList = IaisCommonUtils.genNewArrayList();
                applicationList.add(oldApplicationId);
                ApplicationDto applicationDto = applicationClient.getApplicationById(oldApplicationId).getEntity();
                if (applicationDto != null){
                    List<TaskDto> taskDtoList = taskOrganizationClient.getTaskbyApplicationNo(applicationDto.getApplicationNo()).getEntity();
                    if (taskDtoList != null && taskDtoList.size()>0){
                        taskDtoList.forEach(c -> {
                            c.setTaskStatus(TaskConsts.TASK_STATUS_REMOVE);
                            taskOrganizationClient.updateTask(c).getEntity();
                        });
                    }
                    result = true;
                }
            }
        }
        return result;
    }

    @Override
    public EventApplicationGroupDto updateFEApplicationStatus(String eventRefNum, String submissionId) {
        log.info(StringUtil.changeForLog("The updateFEApplicationStatus start ..."));
        log.info(StringUtil.changeForLog("The updateFEApplicationStatus eventRefNum is -->:"+eventRefNum));
        EventApplicationGroupDto eventApplicationGroupDto = null;
        if(!StringUtil.isEmpty(eventRefNum)){
            EicRequestTrackingDto appEicRequestTrackingDto = appealApplicaionService.getAppEicRequestTrackingDtoByRefNo(eventRefNum);
            if(appEicRequestTrackingDto!=null){
                 eventApplicationGroupDto = EicUtil.getObjectApp(appEicRequestTrackingDto,EventApplicationGroupDto.class);
                if(eventApplicationGroupDto!= null){
                    eicCallFeApplication(eventApplicationGroupDto);
                }else{
                    log.debug(StringUtil.changeForLog("This eventReo can not get the EventApplicationGroupDto -->:"+eventRefNum));
                }
                appEicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                applicationClient.updateAppEicRequestTracking(appEicRequestTrackingDto);
            }else{
                log.debug(StringUtil.changeForLog("This eventReo can not get the AppEicRequestTrackingDto -->:"+eventRefNum));
            }
        }
        log.info(StringUtil.changeForLog("The updateFEApplicationStatus end ..."));
        return eventApplicationGroupDto;
    }

    //Send EN_RFC_005_CLARIFICATION
    @Override
    public void sendRfcClarificationEmail(String licenseeId, ApplicationViewDto applicationViewDto, String internalRemarks, String recipientRole,String recipientUserId) throws Exception {
        String licenseeName = null;
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(licenseeId).getEntity();
        if (licenseeDto != null) {
            licenseeName = licenseeDto.getName();
        }
        String loginUrl = HmacConstants.HTTPS + "://" + systemParamConfig.getIntraServerName() + "/main-web";
        Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
        emailMap.put("officer_name", "");
        emailMap.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationDto.getApplicationType()}).get(0).getText());
        emailMap.put("ApplicationNumber", applicationDto.getApplicationNo());
        emailMap.put("TAT_time", StringUtil.viewHtml(Formatter.formatDateTime(new Date(), Formatter.DATE)));
        StringBuilder stringBuffer = new StringBuilder();
        if (applicationViewDto.getHciName() != null) {
            stringBuffer.append("HCI Name : ").append(applicationViewDto.getHciName()).append("<br>");
        }

        if (applicationViewDto.getHciAddress() != null) {
            stringBuffer.append("HCI Address : ").append(applicationViewDto.getHciAddress()).append("<br>");
        }
        if (licenseeName != null) {
            stringBuffer.append("Licensee Name : ").append(licenseeName).append("<br>");
        }
        if (applicationViewDto.getSubmissionDate() != null) {
            stringBuffer.append("Submission Date : ").append(Formatter.formatDate(Formatter.parseDate(applicationViewDto.getSubmissionDate()))).append("<br>");
        }
        if (internalRemarks != null) {
            stringBuffer.append("Comment : ").append(StringUtil.viewHtml(internalRemarks)).append("<br>");
        }
        emailMap.put("details", stringBuffer.toString());
        emailMap.put("systemLink", loginUrl);
        emailMap.put("MOH_AGENCY_NAM_GROUP", "<b>" + AppConsts.MOH_AGENCY_NAM_GROUP + "</b>");
        emailMap.put("MOH_AGENCY_NAME", "<b>" + AppConsts.MOH_AGENCY_NAME + "</b>");
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_005_CLARIFICATION);
        emailParam.setTemplateContent(emailMap);
        emailParam.setQueryCode(applicationDto.getApplicationNo());
        emailParam.setReqRefNum(applicationDto.getApplicationNo());
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        emailParam.setRefId(applicationDto.getApplicationNo());
        emailParam.setRecipientType(recipientRole);
        emailParam.setRecipientUserId(recipientUserId);
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        MsgTemplateDto rfiEmailTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_005_CLARIFICATION).getEntity();
        map.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{applicationDto.getApplicationType()}).get(0).getText());
        map.put("ApplicationNumber", applicationDto.getApplicationNo());
        String subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
        emailParam.setSubject(subject);
        //email
        notificationHelper.sendNotification(emailParam);

        //sms
        rfiEmailTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_005_CLARIFICATION_SMS).getEntity();
        subject = null;
        try {
            subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
        } catch (TemplateException e) {
            log.info(e.getMessage(), e);
        }
        EmailParam smsParam = new EmailParam();
        smsParam.setQueryCode(applicationDto.getApplicationNo());
        smsParam.setReqRefNum(applicationDto.getApplicationNo());
        smsParam.setRefId(applicationDto.getApplicationNo());
        smsParam.setTemplateContent(emailMap);
        smsParam.setSubject(subject);
        smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_005_CLARIFICATION_SMS);
        smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
        notificationHelper.sendNotification(smsParam);
    }

    @Override
    public void rollBackInspAo1InspLead(BaseProcessClass bpc, String roleId, String appStatus, String wrkGpId, String userId) throws CloneNotSupportedException {
        //get the user for this applicationNo
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        String internalRemarks = ParamUtil.getString(bpc.request, "internalRemarks");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        BroadcastOrganizationDto broadcastOrganizationDto = new BroadcastOrganizationDto();
        BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();

        //complated this task and create the history
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        broadcastOrganizationDto.setRollBackComplateTask((TaskDto) CopyUtil.copyMutableObject(taskDto));
        //set / get completedTask
        taskDto = completedTask(taskDto);
        broadcastOrganizationDto.setComplateTask(taskDto);
        String processDecision = ParamUtil.getString(bpc.request, "nextStage");
        String nextStageReplys = ParamUtil.getString(bpc.request, "nextStageReplys");
        if (!StringUtil.isEmpty(nextStageReplys) && StringUtil.isEmpty(processDecision)) {
            processDecision = nextStageReplys;
        }
        //save appPremisesRoutingHistoryExtDto
        String routeBackReview = (String) ParamUtil.getSessionAttr(bpc.request, "routeBackReview");
        if ("canRouteBackReview".equals(routeBackReview)) {
            AppPremisesRoutingHistoryExtDto appPremisesRoutingHistoryExtDto = new AppPremisesRoutingHistoryExtDto();
            appPremisesRoutingHistoryExtDto.setComponentName(ApplicationConsts.APPLICATION_ROUTE_BACK_REVIEW);
            String[] routeBackReviews = ParamUtil.getStrings(bpc.request, "routeBackReview");
            if (routeBackReviews != null) {
                appPremisesRoutingHistoryExtDto.setComponentValue("Y");
            } else {
                appPremisesRoutingHistoryExtDto.setComponentValue("N");
                //route back and route task processing
                processDecision = ApplicationConsts.PROCESSING_DECISION_ROUTE_BACK_AND_ROUTE_TASK;
            }
            broadcastApplicationDto.setNewTaskHistoryExt(appPremisesRoutingHistoryExtDto);
        }

        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(),
                applicationDto.getStatus(), taskDto.getTaskKey(), HcsaConsts.ROUTING_STAGE_POT, taskDto.getWkGrpId(), internalRemarks, null, processDecision, taskDto.getRoleId());
        broadcastApplicationDto.setComplateTaskHistory(appPremisesRoutingHistoryDto);
        //update application status
        broadcastApplicationDto.setRollBackApplicationDto((ApplicationDto) CopyUtil.copyMutableObject(applicationDto));
        applicationDto.setStatus(appStatus);
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        broadcastApplicationDto.setApplicationDto(applicationDto);
        String taskType = TaskConsts.TASK_TYPE_MAIN_FLOW;
        String TaskUrl = TaskConsts.TASK_PROCESS_URL_MAIN_FLOW;
        String subStageId = HcsaConsts.ROUTING_STAGE_POT;
        //update inspector status
        updateInspectionStatus(applicationViewDto.getAppPremisesCorrelationId(), InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT);
        TaskDto newTaskDto = TaskUtil.getTaskDto(applicationDto.getApplicationNo(), HcsaConsts.ROUTING_STAGE_INS, taskType,
                taskDto.getRefNo(), wrkGpId, userId, new Date(), 0, TaskUrl, roleId,
                IaisEGPHelper.getCurrentAuditTrailDto());
        broadcastOrganizationDto.setCreateTask(newTaskDto);
        //create new history
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew = getAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(), HcsaConsts.ROUTING_STAGE_INS, subStageId,
                taskDto.getWkGrpId(), null, null, null, roleId);
        broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);

        //save the broadcast
        broadcastOrganizationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        broadcastApplicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        String evenRefNum = String.valueOf(System.currentTimeMillis());
        broadcastOrganizationDto.setEventRefNo(evenRefNum);
        broadcastApplicationDto.setEventRefNo(evenRefNum);
        String submissionId = generateIdClient.getSeqId().getEntity();
        log.info(StringUtil.changeForLog(submissionId));
        broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto, bpc.process, submissionId);
        broadcastApplicationDto = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto, bpc.process, submissionId);

        //0062460 update FE  application status.
        applicationService.updateFEApplicaiton(broadcastApplicationDto.getApplicationDto());
    }

    @Override
    public void updateInspectionStatusByAppNo(String appId, String inspectionStatus) {
        if(!StringUtil.isEmpty(appId)) {
            AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(appId).getEntity();
            if(appPremisesCorrelationDto != null){
                updateInspectionStatus(appPremisesCorrelationDto.getId(), inspectionStatus);
            }
        }
    }

    private TaskDto completedTask(TaskDto taskDto) {
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        //taskDto.setSlaRemainInDays(remainDays(taskDto));
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return taskDto;
    }

    private AppPremisesRoutingHistoryDto getAppPremisesRoutingHistory(String appNo, String appStatus,
                                                                      String stageId, String subStageId, String wrkGrpId, String internalRemarks, String externalRemarks, String processDecision,
                                                                      String roleId) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setSubStage(subStageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setExternalRemarks(externalRemarks);
        appPremisesRoutingHistoryDto.setProcessDecision(processDecision);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setWrkGrpId(wrkGrpId);
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return appPremisesRoutingHistoryDto;
    }

    private void updateInspectionStatus(String appPremisesCorrelationId, String status) {
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremisesCorrelationId).getEntity();
        if (appInspectionStatusDto != null) {
            appInspectionStatusDto.setStatus(status);
            appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appInspectionStatusClient.update(appInspectionStatusDto);
        }
    }

    public void eicCallFeApplication(EventApplicationGroupDto eventApplicationGroupDto) {
        log.info(StringUtil.changeForLog("The eicCallFeApplication start ..."));
        List<ApplicationDto> applicationDto = eventApplicationGroupDto.getApplicationDto();
        if(!IaisCommonUtils.isEmpty(applicationDto)){
            for (ApplicationDto applicationDto1 : applicationDto){
                HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
                HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
                beEicGatewayClient.updateApplication(applicationDto1, signature.date(), signature.authorization(),
                        signature2.date(), signature2.authorization()).getEntity();
            }
            updateAppealApplicationStatus(applicationDto);
        }else{
            log.debug(StringUtil.changeForLog("This applicationDto is null "));
        }
        log.info(StringUtil.changeForLog("The eicCallFeApplication end ..."));

    }

    private void updateAppealApplicationStatus(List<ApplicationDto> applicationDtos){
        if(applicationDtos!=null){
            List<String> appId=new ArrayList<>(applicationDtos.size());
            for (ApplicationDto applicationDto : applicationDtos){
                appId.add(applicationDto.getId());
            }
            List<ApplicationDto> applicationDtoList = applicationClient.getAppealApplicationByApplicationIds(appId).getEntity();
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            for(ApplicationDto applicationDto : applicationDtoList){
                if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationDto.getApplicationType())){
                    applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED);
                    applicationClient.updateApplication(applicationDto);
                    beEicGatewayClient.updateApplication(applicationDto,signature.date(), signature.authorization(),
                            signature2.date(), signature2.authorization());
                }
            }
        }
    }

    @Override
    public ApplicationDto getApplicationDtoByGroupIdAndStatus(String appGroupId, String status) {
        log.info(StringUtil.changeForLog("The containStatus is start ..."));
        log.info(StringUtil.changeForLog("The containStatus appGrpId is -->:"+appGroupId));
        log.info(StringUtil.changeForLog("The containStatus status is -->:"+status));
        ApplicationDto result = null;
        if(!StringUtil.isEmpty(appGroupId)){
            List<ApplicationDto> applicationDtoList = getApplicaitonsByAppGroupId(appGroupId);
            if(!IaisCommonUtils.isEmpty(applicationDtoList)){
                log.info(StringUtil.changeForLog("The containStatus applicationDtoList.size() is -->:"+applicationDtoList.size()));
                for (ApplicationDto applicationDto : applicationDtoList){
                    if(status.equals(applicationDto.getStatus())){
                        log.info(StringUtil.changeForLog("The containStatus had approved ApplicationNo is -->:"+applicationDto.getApplicationNo()));
                        result = applicationDto;
                        break;
                    }
                }
            }
        }

        //log.info(StringUtil.changeForLog("The containStatus result is -->:" + result));
        log.info(StringUtil.changeForLog("The containStatus is end ..."));
        return result;
    }
}
