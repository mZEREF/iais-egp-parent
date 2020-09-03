package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.EventApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.RequestInformationSubmitDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.SelfAssMtEmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MessageTemplateUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.AppealApplicaionService;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.AppealClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.EicClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskOrganizationClient;
import com.ecquaria.cloud.moh.iais.util.EicUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    private InspEmailService inspEmailService;

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
    private SystemParamConfig systemParamConfig;

    @Autowired
    private EicClient eicClient;

    @Autowired
    private AppealApplicaionService appealApplicaionService;
    @Autowired
    private AppealClient appealClient;

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
    public ApplicationDto updateFEApplicaiton(ApplicationDto applicationDto) {
        log.info(StringUtil.changeForLog("The updateFEApplicaiton start ..."));
        String moduleName = currentApp + "-" + currentDomain;
        EicRequestTrackingDto dto = new EicRequestTrackingDto();
        dto.setStatus(AppConsts.EIC_STATUS_PENDING_PROCESSING);
        dto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
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

    private void sendSelfDecl(String queryCode, String msgTemplateId, List<SelfAssMtEmailDto> allAssLt){
        Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();

        for (SelfAssMtEmailDto i : allAssLt) {
            String reqRefNum;
            String refType;
            String tlGroupNumber = "-";
            String tlAppType = "-";
            String tlSvcName = "-";
            List<ApplicationDto> appList;
            String randomStr = IaisEGPHelper.generateRandomString(26);
            int msgTrackRefNumType = i.getMsgTrackRefNumType();

            LicenseeDto licenseeDto= inspEmailService.getLicenseeDtoById(i.getLicenseeId());
            if (licenseeDto == null){
                continue;
            }


            List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
            log.info(StringUtil.changeForLog("do send email licensee name" + licenseeDto.getName()));
            //pending submit self ass mt
            if (msgTrackRefNumType == 1) {
                refType = NotificationHelper.RECEIPT_TYPE_APP_GRP;
                reqRefNum = i.getGroupId();
                appList = i.getAppList();
                boolean flag = false;
                List<String> svcNames = IaisCommonUtils.genNewArrayList();
                if (!IaisCommonUtils.isEmpty(appList)) {
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
                        if (serviceDto != null) {
                            String svcName = serviceDto.getSvcName();
                            String svcCode = serviceDto.getSvcCode();
                            tlSvcName = svcName;

                            svcCodeList.add(svcCode);
                            svcNames.add(svcName);
                        }
                    }

                    templateContent.put("serviceNames", svcNames.stream().distinct().collect(Collectors.toList()));
                }
            } else {
                // uncompleted self ass mt
                refType = NotificationHelper.RECEIPT_TYPE_APP;
                appList = i.getAppList();
                //never null
                ApplicationDto app = appList.get(0);

                tlGroupNumber = app.getApplicationNo();
                tlAppType = MasterCodeUtil.getCodeDesc(app.getApplicationType());

                reqRefNum = String.valueOf(app.getEndDate().getTime());
                templateContent.put("applicationNumber", reqRefNum);
                templateContent.put("applicationType", MasterCodeUtil.getCodeDesc(app.getApplicationType()));
                templateContent.put("applicationDate", Formatter.formatDate(app.getStartDate()));

                List<String> svcNames = IaisCommonUtils.genNewArrayList();
                HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceById(app.getServiceId());
                if (serviceDto != null) {
                    String svcName = serviceDto.getSvcName();
                    String svcCode = serviceDto.getSvcCode();
                    tlSvcName = svcName;

                    svcCodeList.add(svcCode);
                    svcNames.add(svcName);
                }
                templateContent.put("serviceNames", svcNames);
            }

            String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getIntraServerName() + "/main-web/";
            //EN-NAP-008
            String today = Formatter.formatDate(new Date());
            String applicantName = licenseeDto.getName();
            templateContent.put("ApplicantName", StringUtil.viewHtml(applicantName));
            templateContent.put("MOH_AGENCY_NAME", "-");
            templateContent.put("emailAddress", systemParamConfig.getSystemAddressOne());
            templateContent.put("tatTime", today);
            templateContent.put("reminderDate", today);
            templateContent.put("systemLink", loginUrl);
            JobRemindMsgTrackingDto jobRemindMsgTrackingDto = new JobRemindMsgTrackingDto();
            jobRemindMsgTrackingDto.setMsgKey(i.getMsgTrackKey());
            jobRemindMsgTrackingDto.setRefNo(reqRefNum);
            jobRemindMsgTrackingDto.setCreateTime(new Date());
            jobRemindMsgTrackingDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);


            HashMap<String, String> subjectParams = IaisCommonUtils.genNewHashMap();
            subjectParams.put("[applicationNo]", tlGroupNumber);
            subjectParams.put("[appType]", tlAppType);
            subjectParams.put("[serviceName]", tlSvcName);
            EmailParam emailParam = new EmailParam();
            emailParam.setSubjectParams(subjectParams);
            emailParam.setTemplateId(msgTemplateId);
            emailParam.setTemplateContent(templateContent);
            emailParam.setQueryCode(queryCode);
            emailParam.setReqRefNum(randomStr);
            emailParam.setRefIdType(refType);
            emailParam.setRefId(reqRefNum);
            emailParam.setSvcCodeList(svcCodeList);

            emailParam.setJobRemindMsgTrackingDto(jobRemindMsgTrackingDto);
            notificationHelper.sendNotification(emailParam);

            if (!IaisCommonUtils.isEmpty(appList)){
                ApplicationDto applicationDto = appList.get(0);
                emailParam.setRefId(applicationDto.getApplicationNo());
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                notificationHelper.sendNotification(emailParam);
            }
            log.info("===>>>>alertSelfDeclNotification end");
        }
    }

    @Override
    public void alertSelfDeclNotification() {
        log.info("===>>>>alertSelfDeclNotification start");
        List<SelfAssMtEmailDto> email_008 = applicationClient.getPendingSubmitSelfAss(HcsaChecklistConstants.SELF_ASS_MT_REMINDER__MSG_KEY).getEntity();
        sendSelfDecl(HcsaChecklistConstants.SELF_ASS_MT_REMINDER__MSG_KEY, MsgTemplateConstants.MSG_TEMPLATE_REMINDER_SELF_ASS_MT, email_008);

        List<SelfAssMtEmailDto> email_001 = applicationClient.getPendingSubmitSelfAss(HcsaChecklistConstants.SELF_ASS_MT_REMINDER__MSG_KEY_FIR).getEntity();
        sendSelfDecl(HcsaChecklistConstants.SELF_ASS_MT_REMINDER__MSG_KEY_FIR, MsgTemplateConstants.MSG_TEMPLATE_SELF_ASS_MT_REMINDER_FIR, email_001);

        List<SelfAssMtEmailDto> email_002 = applicationClient.getPendingSubmitSelfAss(HcsaChecklistConstants.SELF_ASS_MT_REMINDER__MSG_KEY_SEC).getEntity();
        sendSelfDecl(HcsaChecklistConstants.SELF_ASS_MT_REMINDER__MSG_KEY_SEC, MsgTemplateConstants.MSG_TEMPLATE_SELF_ASS_MT_REMINDER_SEC, email_002);
    }

    @Override
    public void applicationRfiAndEmail(ApplicationViewDto applicationViewDto, ApplicationDto applicationDto, String licenseeId, LicenseeDto licenseeDto,
                                       LoginContext loginContext, String externalRemarks) throws IOException, TemplateException {
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
        MsgTemplateDto autoEntity = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_RFI).getEntity();
        Map<String ,Object> map=IaisCommonUtils.genNewHashMap();
        map.put("APPLICANT_NAME",licenseeDto.getName());
        map.put("APPLICATION_NUMBER",StringUtil.viewHtml(applicationNo));
        map.put("DETAILS","");
        map.put("COMMENTS",StringUtil.viewHtml(externalRemarks));
        map.put("EDITSELECT",editSelect);
        map.put("A_HREF",url);
        map.put("MOH_NAME",AppConsts.MOH_AGENCY_NAME);
        String templateMessageByContent = MsgUtil.getTemplateMessageByContent(autoEntity.getMessageContent(), map);
        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(applicationDto.getServiceId());
        if(hcsaServiceDto!=null && !ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)){
            InterMessageDto interMessageDto = MessageTemplateUtil.getInterMessageDto(MessageConstants.MESSAGE_SUBJECT_REQUEST_FOR_INFORMATION+applicationNo,MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED,
                    messageNo,hcsaServiceDto.getSvcCode()+"@",templateMessageByContent, applicationViewDto.getApplicationGroupDto().getLicenseeId(),IaisEGPHelper.getCurrentAuditTrailDto());
            HashMap<String,String> mapParam = IaisCommonUtils.genNewHashMap();
            mapParam.put("appNo",applicationDto.getApplicationNo());
            interMessageDto.setMaskParams(mapParam);
            inboxMsgService.saveInterMessage(interMessageDto);
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
            AppEicRequestTrackingDto appEicRequestTrackingDto = appealApplicaionService.getAppEicRequestTrackingDtoByRefNo(eventRefNum);
            if(appEicRequestTrackingDto!=null){
                 eventApplicationGroupDto = EicUtil.getObjectApp(appEicRequestTrackingDto,EventApplicationGroupDto.class);
                if(eventApplicationGroupDto!= null){
                    eicCallFeApplication(eventApplicationGroupDto);
                }else{
                    log.error(StringUtil.changeForLog("This eventReo can not get the EventApplicationGroupDto -->:"+eventRefNum));
                }
                appEicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                appealClient.updateAppEicRequestTracking(appEicRequestTrackingDto);
            }else{
                log.error(StringUtil.changeForLog("This eventReo can not get the AppEicRequestTrackingDto -->:"+eventRefNum));
            }
        }
        log.info(StringUtil.changeForLog("The updateFEApplicationStatus end ..."));
        return eventApplicationGroupDto;
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
        }else{
            log.error(StringUtil.changeForLog("This applicationDto is null "));
        }
        log.info(StringUtil.changeForLog("The eicCallFeApplication end ..."));

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
