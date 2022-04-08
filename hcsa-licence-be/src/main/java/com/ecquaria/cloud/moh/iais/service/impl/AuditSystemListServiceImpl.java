package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptCalendarStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.SmsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionForAuditDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditCombinationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesAuditDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesAuditInspectorDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MessageTemplateUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.EventBusHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.ApptInspectionDateService;
import com.ecquaria.cloud.moh.iais.service.AuditSystemListService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.EgpUserClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.EventClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskHcsaConfigClient;
import com.ecquaria.cloud.role.Role;
import com.ecquaria.cloudfeign.FeignException;
import com.ecquaria.kafka.model.Submission;
import com.ecquaria.sz.commons.util.MsgUtil;
import com.google.common.collect.Maps;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2020/2/19 10:54
 */
@Slf4j
@Service
public class AuditSystemListServiceImpl implements AuditSystemListService {
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private BeEicGatewayClient beEicGatewayClient;
    @Autowired
    private GenerateIdClient generateIdClient;
    @Autowired
    private EventBusHelper eventBusHelper;
    @Autowired
    private EventClient eventClient;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private TaskHcsaConfigClient taskHcsaConfigClient;
    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    private SystemParamConfig systemParamConfig;
    @Autowired
    private InspectionTaskClient inspectionTaskClient;
    @Autowired
    private ApptInspectionDateService apptInspectionDateService;
    static String[] category = {"ADTYPE001", "ADTYPE002", "ADTYPE003"};
    @Autowired
    private AppointmentClient appointmentClient;
    @Autowired
    private EgpUserClient egpUserClient;

    @Value("${iais.email.sender}")
    private String mailSender;

    @Autowired
    private EmailClient emailClient;

    @Autowired
    private MsgTemplateClient msgTemplateClient;

    @Override
    public void sendMailForAuditPlanerForSms(String emailKey) {
        List<OrgUserDto> userDtoList = organizationClient. retrieveUserRoleByRoleId(RoleConsts.USER_ROLE_AUDIT_PLAN).getEntity();
        if( !IaisCommonUtils.isEmpty(userDtoList)){
            sendEmailToInsForSms(emailKey,null);
        }else {
            log.info("----------no audit plan user ---------");
        }
    }

    @Override
    public void sendMailForAuditPlaner(String emailKey) {
        List<OrgUserDto> userDtoList = organizationClient. retrieveUserRoleByRoleId(RoleConsts.USER_ROLE_AUDIT_PLAN).getEntity();
        if( !IaisCommonUtils.isEmpty(userDtoList)){
               sendEmailToIns(emailKey,null,null);
        }else {
            log.info("----------no audit plan user ---------");
        }
    }

    @Override
    public List<AuditTaskDataFillterDto> getInspectors(List<AuditTaskDataFillterDto> auditTaskDataDtos) {
        if (!IaisCommonUtils.isEmpty(auditTaskDataDtos)) {
            List<AuditTaskDataFillterDto> auditTaskDataFillterDtos = new ArrayList<>(5);
            Map<String,String> workGroupIds = getAllWorkGrpIds(auditTaskDataDtos);
            for(AuditTaskDataFillterDto temp : auditTaskDataDtos){
                String groupId = workGroupIds.get(temp.getSvcName());
                if( !StringUtil.isEmpty(groupId)){
                    temp.setWorkGroupId(groupId);
                    auditTaskDataFillterDtos.add(temp);
                }

            }
            Map<String, List<OrgUserDto>> map = getAllOrgUsersByAuditTaskDataFillterDtos(auditTaskDataFillterDtos);
            for (AuditTaskDataFillterDto temp : auditTaskDataFillterDtos) {
                getinspectorOp(map.get(temp.getWorkGroupId()), temp);
            }
            return auditTaskDataFillterDtos;
        }

      return auditTaskDataDtos;
    }

    private Map<String, List<OrgUserDto>> getAllOrgUsersByAuditTaskDataFillterDtos(List<AuditTaskDataFillterDto> auditTaskDataDtos){
         List<String> workGroupIds = new ArrayList<>(auditTaskDataDtos.size());
         for(AuditTaskDataFillterDto temp : auditTaskDataDtos){
             if(!workGroupIds.contains(temp.getWorkGroupId())){
                 workGroupIds.add(temp.getWorkGroupId());
             }
         }
        Map<String, List<OrgUserDto>> map = IaisCommonUtils.genNewHashMap();
         for(String workGroupId :  workGroupIds){
             map.put(workGroupId,getOrgDtos(workGroupId));
         }
         return  map;
    }

    private   List<OrgUserDto> getOrgDtos( String workGroupId){
        return organizationClient. getUsersByWorkGroupNameNotAvailable(workGroupId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
    }
    private Map<String,String> getAllWorkGrpIds(List<AuditTaskDataFillterDto> auditTaskDataDtos){
        List<String> svcNames = new ArrayList<>(auditTaskDataDtos.size());
        for(AuditTaskDataFillterDto auditTaskDataFillterDto : auditTaskDataDtos){
            if( !svcNames.contains(auditTaskDataFillterDto.getSvcName())){
                svcNames.add(auditTaskDataFillterDto.getSvcName());
            }
        }
        Map<String,String> map = IaisCommonUtils.genNewHashMap();
        for(String s : svcNames){
            String groupId = getWorkGroupIdBySvcName(s);
            if( !StringUtil.isEmpty(groupId)) {
                map.put(s, getWorkGroupIdBySvcName(s));
            }
        }
        return map;
    }

    private String  getWorkGroupIdBySvcName( String svcName){
        HcsaSvcStageWorkingGroupDto dto = new HcsaSvcStageWorkingGroupDto();
        try {
            HcsaServiceDto svcDto = hcsaConfigClient.getServiceDtoByName(svcName).getEntity();
            if(svcDto == null){
                log.info(StringUtil.changeForLog("dirty data Service Name is :" + svcName));
                return null;
            }
            dto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
            dto.setOrder(1);
            dto.setServiceId(svcDto.getId());
            dto.setType("APTY002");
            dto = hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(dto).getEntity();
            return  dto.getGroupId();
        }catch (Exception e){
            log.info(StringUtil.changeForLog("dirty data Service Name is " + svcName));
            return null;
        }
    }


    private void getinspectorOp(List<OrgUserDto> orgDtos, AuditTaskDataFillterDto temp) {
        List<SelectOption> ops = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(orgDtos)) {
            Map<String,String> userIdToEmails = Maps.newHashMapWithExpectedSize(orgDtos.size());
            for (OrgUserDto ou : orgDtos) {
                SelectOption op = new SelectOption();
                op.setText(ou.getDisplayName());
                op.setValue(ou.getId());
                ops.add(op);
                userIdToEmails.put(ou.getDisplayName(),ou.getEmail());
            }
            temp.setInspectors(ops);
            temp.setUserIdToEmails(userIdToEmails);
        }
    }

    @Override
    public List<SelectOption> getAuditOp() {

        List<SelectOption> inpTypeOp = MasterCodeUtil.retrieveOptionsByCodes(category);
        return inpTypeOp;
    }

    @Override
    public void doSubmit(List<AuditTaskDataFillterDto> auditTaskDataDtos) {
        if (!IaisCommonUtils.isEmpty(auditTaskDataDtos)) {
            for (AuditTaskDataFillterDto temp : auditTaskDataDtos) {
                if (temp.isSelectedForAudit()) {
                    if(!StringUtil.isEmpty(temp.getAnnouncedFlag())) {
                        temp = setOrgUserDtoForSendMsgEmail(temp);
                    }
                    assignTask(temp);
                }
            }
        }
    }

    @Override
    public void setTcuAuditFlag(List<AuditTaskDataFillterDto> auditTaskDataDtos) {
        if (!IaisCommonUtils.isEmpty(auditTaskDataDtos)) {
            Iterator<AuditTaskDataFillterDto> itemDtoIterator = auditTaskDataDtos.iterator();
            while(itemDtoIterator.hasNext()){
                AuditTaskDataFillterDto temp  = itemDtoIterator.next();
                if(temp.isSelectedForAudit()){
                    temp.setTcuAudit(true);
                }else {
                    itemDtoIterator.remove();
                }
            }
        }
    }

    public AuditTaskDataFillterDto setOrgUserDtoForSendMsgEmail(AuditTaskDataFillterDto auditTaskDataFillterDto) {
        if(auditTaskDataFillterDto != null) {
            String licenceId = auditTaskDataFillterDto.getLicId();
            String submitById = apptInspectionDateService.getAppSubmitByWithLicId(licenceId);
            OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(submitById).getEntity();
            if(orgUserDto != null) {
                String applicantName = orgUserDto.getDisplayName();
                log.info(StringUtil.changeForLog("Tcu Audit Appt Preferred Date User Display Name : " + applicantName));
                auditTaskDataFillterDto.setOrgUserDto(orgUserDto);
            }
        }
        return auditTaskDataFillterDto;
    }

    private void assignTask(AuditTaskDataFillterDto temp) {
        String submitId = generateIdClient.getSeqId().getEntity();
        //create auditType data  and create grop info
        AuditCombinationDto auditCombinationDto = new AuditCombinationDto();
        auditCombinationDto.setAuditTaskDataFillterDto(temp);
        //create grop info
        // createInspectionGroupInfo(temp,submitId);
        // create app
        List<String> licIds = new ArrayList<>(1);
        if( !StringUtil.isEmpty(temp.getLicId())){
            createAudit(temp,submitId,auditCombinationDto);
            licIds.add(temp.getLicId());
            createAuditTaskApp(licIds,submitId,auditCombinationDto);
        }
    }

    private void sendEmailByCreateAuditTaskDataFillterDto(AuditTaskDataFillterDto temp,String eventNo){
        // send email
        if(!StringUtil.isEmpty(temp.getInspector()) &&  (temp.getUserIdToEmails() != null && temp.getUserIdToEmails().size() > 0)){
            sendEmailToIns(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_CREATE_TASK,eventNo,temp);
            sendEmailToInsForSms(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_CREATE_TASK_SMS,eventNo);
        }else {
            log.info("-----------Inspector id is null or UserIdToEmails is null");
        }
    }
    public void createTaskCallBack(String eventRefNum,String submissionId)throws FeignException {
        log.info("call back createTaskCallBack ===================>>>>>");
        List<Submission> submissionList = eventClient.getSubmission(submissionId).getEntity();
        AuditCombinationDto auditCombinationDto = null;
        if(submissionList!= null && submissionList.size() > 0){
            log.info(StringUtil.changeForLog(submissionList .size() +"submissionList .size()"));
            for(Submission submission : submissionList){
                if(EventBusConsts.SERVICE_NAME_APPSUBMIT.equals(submission.getSubmissionIdentity().getService())){
                    auditCombinationDto = JsonUtil.parseToObject(submission.getData(), AuditCombinationDto.class);
                    break;
                }
            }
            if(auditCombinationDto != null){
                //  Synchronize app to fe
                AppSubmissionForAuditDto appSubmissionForAuditDto = applicationClient.getAppSubmissionForAuditDto(eventRefNum).getEntity();
                if( appSubmissionForAuditDto == null )
                    log.info("========create applicationDtos is no success.");
                else {
                    appSubmissionForAuditDto.setAuditTrailDto(auditCombinationDto.getLicPremisesAuditDto().getAuditTrailDto());
                    saveAppForAuditToFe(appSubmissionForAuditDto,false);
                }
                log.info("========create TaskCall is start.");
                TaskDto taskDto = createTask(auditCombinationDto.getAuditTaskDataFillterDto(), submissionId, auditCombinationDto,eventRefNum);
                sendEmailByCreateAuditTaskDataFillterDto( auditCombinationDto.getAuditTaskDataFillterDto(),auditCombinationDto.getEventRefNo());
                AuditTaskDataFillterDto auditTaskDataFillterDto = auditCombinationDto.getAuditTaskDataFillterDto();
                try{
                    //tcu audit announced Appointment Pre date send msg email sms
                    sendTcuAuditApptEmailMsgSms(auditTaskDataFillterDto, taskDto, appSubmissionForAuditDto);
                } catch (Exception e) {
                    log.info(e.getMessage(), e);
                }
            }else {
                log.info(StringUtil.changeForLog("---------- auditCombinationDto is null, eventRefNum : " + eventRefNum +"-----------------------------"));
            }

        }else {
            log.info("========createTaskCallBack  submissionList is null.");
        }
    }

    private void sendTcuAuditApptEmailMsgSms(AuditTaskDataFillterDto auditTaskDataFillterDto, TaskDto taskDto, AppSubmissionForAuditDto appSubmissionForAuditDto) {
        if(auditTaskDataFillterDto != null && taskDto != null) {
            String appNo = taskDto.getApplicationNo();
            log.info(StringUtil.changeForLog("Tcu Audit Appt Date Send Email Start" + appNo));
            if (!StringUtil.isEmpty(auditTaskDataFillterDto.getAnnouncedFlag())) {
                OrgUserDto orgUserDto = auditTaskDataFillterDto.getOrgUserDto();
                if(orgUserDto != null) {
                    //set svc code
                    List<String> serviceCodes = getSvcCodeForEmail(appSubmissionForAuditDto.getApplicationDtos());
                    //FE Submit user name
                    String applicantName = orgUserDto.getDisplayName();
                    log.info(StringUtil.changeForLog("Tcu Audit Appt Date Send Email submitByName =======" + applicantName));
                    //create mask value map
                    HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
                    maskParams.put("applicationNo", appNo);
                    //fe url
                    String emailLoginUrl = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
                    String msgLoginUrl = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_TCU_AUDIT_APPT_PRE_DATE + appNo;
                    //set template value
                    Map<String ,Object> map = IaisCommonUtils.genNewHashMap();
                    map.put("appNo", appNo);
                    map.put("applicant", applicantName);
                    map.put("systemLink", emailLoginUrl);
                    sendTcuAuditApptEmailSms(orgUserDto, map, appNo);
                    //send message
                    map.put("systemLink", msgLoginUrl);
                    EmailParam msgParam = new EmailParam();
                    msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_PRE_DATE_MSG);
                    msgParam.setTemplateContent(map);
                    msgParam.setQueryCode(appNo);
                    msgParam.setReqRefNum(appNo);
                    msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_ACTION_REQUIRED);
                    msgParam.setRefId(appNo);
                    msgParam.setMaskParams(maskParams);
                    msgParam.setSvcCodeList(serviceCodes);
                    notificationHelper.sendNotification(msgParam);
                }
            }
        }
    }

    private void sendTcuAuditApptEmailSms(OrgUserDto orgUserDto, Map<String, Object> map, String appNo) {
        //email
        MsgTemplateDto msgTemplateDto = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_PRE_DATE_EMAIL).getEntity();
        if(msgTemplateDto != null){
            int emailFlag = systemParamConfig.getEgpEmailNotifications();
            if (0 == emailFlag) {
                log.info("Tcu Audit please turn on email param.......");
            } else {
                List<String> receiptEmail = IaisCommonUtils.genNewArrayList();
                String emailAddress = orgUserDto.getEmail();
                if(!StringUtil.isEmpty(emailAddress)) {
                    receiptEmail.add(emailAddress);
                }
                String emailTemplate = msgTemplateDto.getMessageContent();
                //replace num
                emailTemplate = MessageTemplateUtil.replaceNum(emailTemplate);
                //get mesContext
                String mesContext;
                try {
                    mesContext = MsgUtil.getTemplateMessageByContent(emailTemplate, map);
                } catch (IOException | TemplateException e) {
                    log.error(e.getMessage(), e);
                    throw new IaisRuntimeException(e);
                }
                if(!IaisCommonUtils.isEmpty(receiptEmail)) {
                    EmailDto emailDto = new EmailDto();
                    emailDto.setContent(mesContext);
                    emailDto.setSubject(msgTemplateDto.getTemplateName());
                    emailDto.setSender(mailSender);
                    emailDto.setReceipts(receiptEmail);
                    emailDto.setClientQueryCode(appNo);
                    emailDto.setReqRefNum(appNo);
                    emailClient.sendNotification(emailDto);
                } else {
                    log.info("Tcu Audit receiptEmail is null.......");
                }
            }
        }
        //sms
        int smsFlag = systemParamConfig.getEgpSmsNotifications();
        if (0 == smsFlag) {
            log.info("Tcu Audit please turn on sms param.......");
        } else {
            List<String> mobile = IaisCommonUtils.genNewArrayList();
            String phoneNo = orgUserDto.getMobileNo();
            if(!StringUtil.isEmpty(phoneNo)) {
                mobile.add(phoneNo);
            }
            if (!IaisCommonUtils.isEmpty(mobile)) {
                SmsDto smsDto = new SmsDto();
                smsDto.setSender(mailSender);
                smsDto.setContent("MOH HALP - Audit Inspection Preferred Date");
                smsDto.setOnlyOfficeHour(true);
                smsDto.setReceipts(mobile);
                smsDto.setReqRefNum(appNo);
                emailClient.sendSMS(mobile, smsDto, appNo);
            } else {
                log.info("Tcu Audit mobile is null.......");
            }
        }
    }

    private List<String> getSvcCodeForEmail(List<ApplicationDto> applicationDtos) {
        List<String> serviceCodes = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(applicationDtos)) {
            for(ApplicationDto applicationDto : applicationDtos) {
                String serviceId = applicationDto.getServiceId();
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
                if(hcsaServiceDto != null) {
                    String serviceCode = hcsaServiceDto.getSvcCode();
                    serviceCodes.add(serviceCode);
                }
            }
        }
        return serviceCodes;
    }

    public TaskDto createTask(AuditTaskDataFillterDto temp,String submitId,AuditCombinationDto auditCombinationDto,String eventRefNum){
        TaskDto taskDto = new TaskDto();
        taskDto.setId(generateIdClient.getSeqId().getEntity());
        taskDto.setUserId(temp.getInspectorId());
        taskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_APPT_INSPECTION_DATE );
        taskDto.setTaskType(TaskConsts.TASK_TYPE_INSPECTION_SUPER);
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        taskDto.setSlaDateCompleted(null);
        taskDto.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        taskDto.setWkGrpId(temp.getWorkGroupId());
        taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
        taskDto.setSlaAlertInDays(2);
        //taskDto.setSlaRemainInDays(3);
        taskDto.setSlaInDays(5);
        taskDto.setPriority(0);
        taskDto.setDateAssigned(new Date());
        taskDto.setApplicationNo(eventRefNum+"-01");
        List<ApplicationDto> postApps = applicationClient.getAppsByGrpNo(eventRefNum).getEntity();
        if(postApps != null && postApps.size() >0 ){
            ApplicationDto applicationDtoC = postApps.get(0);
            String appId = applicationDtoC.getId();
            createAuditAppLic(temp.getLicId(),appId ,auditCombinationDto.getLicPremisesAuditDto().getAuditTrailDto(),auditCombinationDto.getEventRefNo(),submitId, applicationDtoC.getAppPremisesCorrelationId(),temp.getId());
            String corrId = applicationClient.getCorrIdByAppId(appId).getEntity();
            taskDto.setRefNo(corrId);
            List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = new ArrayList(1);
            for(ApplicationDto applicationDto : postApps){
                HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
                hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
                hcsaSvcStageWorkingGroupDto.setServiceId(StringUtil.isNotEmpty(applicationDto.getRoutingServiceId()) ? applicationDto.getRoutingServiceId() : applicationDto.getServiceId());
                hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
                AppGrpPremisesEntityDto appGrpPremisesEntityDto = applicationClient.getPremisesByAppNo(taskDto.getApplicationNo()).getEntity();
                hcsaSvcStageWorkingGroupDto.setPremiseType(appGrpPremisesEntityDto.getPremisesType());
                hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
                hcsaSvcStageWorkingGroupDtos = taskHcsaConfigClient.getWrkGrp(hcsaSvcStageWorkingGroupDtos).getEntity();
                taskDto.setScore(getConfigScoreForService( hcsaSvcStageWorkingGroupDtos,applicationDto.getServiceId(),HcsaConsts.ROUTING_STAGE_INS,applicationDto.getApplicationType()));
            }
        }else {
            log.info("=============== group id is null.==========");
            return null;

        }
        List<TaskDto> createTaskDtoList = IaisCommonUtils.genNewArrayList();
        createTaskDtoList.add(taskDto);
        auditCombinationDto.setTaskDtos(createTaskDtoList);
        try {
            log.info("========================>>>>> create task !!!!");
            eventBusHelper.submitAsyncRequest(auditCombinationDto,submitId, EventBusConsts.SERVICE_NAME_ROUNTINGTASK,EventBusConsts.OPERATION_CREATE_AUDIT_TASK_CALL_BACK,auditCombinationDto.getEventRefNo(),null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return taskDto;
    }

    private void  createAuditAppLic(String licId, String appId , AuditTrailDto auditTrailDto,String eventRefNum,String submissionId,String corrId,String licPremId){
        try {
            log.info(StringUtil.changeForLog("========================>>>>> create appLicCorrelation : appid ---"+ appId + " licId ----" +licId+" ---"+"===== !!!!"));
            LicAppCorrelationDto licAppCorrelationDto = new LicAppCorrelationDto();
            licAppCorrelationDto.setLicenceId(licId);
            licAppCorrelationDto.setApplicationId(appId);
            licAppCorrelationDto.setAuditTrailDto(auditTrailDto);
            licAppCorrelationDto.setEventRefNo(eventRefNum);
            licAppCorrelationDto.setCorrId(corrId);
            licAppCorrelationDto.setLicPremId(licPremId);
            eventBusHelper.submitAsyncRequest(licAppCorrelationDto,submissionId, EventBusConsts.SERVICE_NAME_LICENCESAVE,EventBusConsts.OPERATION_CREATE_AUDIT_TASK_CALL_BACK,eventRefNum,null);
        }catch (Exception e){
            log.info("========================>>>>> create appLicCorrelation failed!!!!");
            log.error(e.getMessage(), e);
        }
    }
    private int getConfigScoreForService(List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos,String serviceId,
                                         String stageId,String appType){
        int result = 0;
        if(StringUtil.isEmpty(serviceId) || StringUtil.isEmpty(stageId) || StringUtil.isEmpty(appType)){
            return result;
        }
        for (HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto :hcsaSvcStageWorkingGroupDtos){
            if(serviceId.equals(hcsaSvcStageWorkingGroupDto.getServiceId())
                    && stageId.equals(hcsaSvcStageWorkingGroupDto.getStageId())
                    && appType.equals(hcsaSvcStageWorkingGroupDto.getType())){
                result = hcsaSvcStageWorkingGroupDto.getCount() == null ? 0 :hcsaSvcStageWorkingGroupDto.getCount();
            }
        }
        return result;
    }
    public void updateLicPremisesAuditDtoByEventBus(AuditTaskDataFillterDto temp,String status){
        String submitId = generateIdClient.getSeqId().getEntity();
        AuditCombinationDto auditCombinationDto = new AuditCombinationDto();
        auditCombinationDto.setAuditTaskDataFillterDto(temp);
        String  RefNo = applicationClient.getRefNoByLicId(temp.getLicId(),temp.getHclCode()).getEntity();
       if(StringUtil.isEmpty( RefNo)){
           log.info(StringUtil.changeForLog("It is a dirty data : licid" +  temp.getLicId()+ " hcicode :" + temp.getHclCode()));
           return;
       }
        auditCombinationDto.setEventRefNo(RefNo);
        // boolean haveApptData =
        setAppPremisesInspecApptDtos(auditCombinationDto,RefNo);
        updateLicenceSaveCancelTask(auditCombinationDto,status,submitId);
        updateAppCancelTaskByEventBus(auditCombinationDto,submitId);
        updateTaskCancelTaskByEventBus(auditCombinationDto,submitId);
        try{
            // sysn fe app
            log.info("========================>>>>>sysn fe app !!!!");
            String groupNo = applicationClient.getRefNoByRefNo(RefNo).getEntity();
            AppSubmissionForAuditDto appSubmissionForAuditDto = applicationClient.getAppSubmissionForAuditDto(groupNo).getEntity();
            appSubmissionForAuditDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            saveAppForAuditToFe(appSubmissionForAuditDto,true);
        //send email to insp
        if(!StringUtil.isEmpty(temp.getInspector()) &&  (temp.getUserIdToEmails() != null && temp.getUserIdToEmails().size() > 0)){
            sendEmailToIns(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_CANCELED_TASK,groupNo,temp);
            sendEmailToInsForSms(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_CANCELED_TASK_SMS,groupNo);
        }else {
            log.info("-----------Inspector id is null or UserIdToEmails is null");
        }

        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        //save fe appt
        /*if(haveApptData){
            ApptInspectionDateDto apptInspectionDateDto = new ApptInspectionDateDto();
            apptInspectionDateDto.setAppPremisesInspecApptDtos(auditCombinationDto.getAppPremisesInspecApptDtos());
            try{
                apptInspectionDateService.createFeAppPremisesInspecApptDto(apptInspectionDateDto);
            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
        }*/
    }
    private boolean setAppPremisesInspecApptDtos(AuditCombinationDto auditCombinationDto,String refNo){
        //get AppPremisesInspecApptDtos
        try{
            List<AppPremisesInspecApptDto>  appPremisesInspecApptDtos = inspectionTaskClient.getSystemDtosByAppPremCorrId(refNo).getEntity();
            if(!IaisCommonUtils.isEmpty(appPremisesInspecApptDtos)){
                for(AppPremisesInspecApptDto appPremisesInspecApptDto : appPremisesInspecApptDtos){
                    appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                }
                auditCombinationDto.setAppPremisesInspecApptDtos(appPremisesInspecApptDtos);
                return true;
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
        return false;
    }


    @Override
    public void sendEmailToIns( String emailKey, String appGroupNo,AuditTaskDataFillterDto auditTaskDataFillterDto) {
        if(!StringUtil.isEmpty(appGroupNo)){
            appGroupNo +="-01";
        }
        Map<String,Object> param = getParamByMesskey(emailKey,appGroupNo,auditTaskDataFillterDto);
        try {
            if(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_LIST_REMIND.equalsIgnoreCase(emailKey)
                    || MsgTemplateConstants.MSG_TEMPLATE_AUDIT_TCU_REMIND.equalsIgnoreCase(emailKey)){
                EmailParam emailParam = new EmailParam();
                emailParam.setTemplateId(emailKey);
                emailParam.setTemplateContent(param);
                emailParam.setQueryCode(emailKey);
                emailParam.setReqRefNum(emailKey);
                emailParam.setRefIdType(null);
                emailParam.setRefId(null);
                notificationHelper.sendNotification(emailParam);
            }else {
                EmailParam emailParam = new EmailParam();
                emailParam.setTemplateId(emailKey);
                emailParam.setTemplateContent(param);
                emailParam.setQueryCode(appGroupNo);
                emailParam.setReqRefNum(appGroupNo);
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                emailParam.setRefId(appGroupNo);
                notificationHelper.sendNotification(emailParam);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    @Override
    public void sendEmailToInsForSms(String emailKey,String appGroupNo) {
        if(!StringUtil.isEmpty(appGroupNo)){
            appGroupNo +="-01";
        }
        try{
            Map<String,Object> param = IaisCommonUtils.genNewHashMap();
            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(emailKey);
            emailParam.setTemplateContent(param);
            if(!StringUtil.isEmpty(appGroupNo)){
                emailParam.setQueryCode(appGroupNo);
                emailParam.setReqRefNum(appGroupNo);
                emailParam.setRefId(appGroupNo);
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
            }else {
                emailParam.setQueryCode(emailKey);
                emailParam.setReqRefNum(emailKey);
                emailParam.setRefId(null);
                emailParam.setRefIdType(null);
            }
            notificationHelper.sendNotification(emailParam);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    private Map<String,Object> getParamByMesskey(String key, String appNo, AuditTaskDataFillterDto auditTaskDataFillterDto){
        Map<String,Object> param =   IaisCommonUtils.genNewHashMap();
        String syName = "<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"<br/>"+AppConsts.MOH_AGENCY_NAME+"</b>";
        String newDateString = Formatter.formatDate(new Date());
        String  licenceDueDateString = "";
        if(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_CREATE_TASK.equalsIgnoreCase(key)){
            if(auditTaskDataFillterDto.getLicenceDueDate() != null){
                licenceDueDateString =  Formatter.formatDate(auditTaskDataFillterDto.getLicenceDueDate());
            }
            param.put("DDMMYYYY",newDateString);
            param.put("appno", appNo);
            String hclName =  auditTaskDataFillterDto.getHclName();
            param.put("hCIName", StringUtil.isEmpty(hclName) ? "N/A" : hclName);
            param.put("hCICode",  auditTaskDataFillterDto.getHclCode());
            param.put("hCIAddress",auditTaskDataFillterDto.getAddress());
            param.put("serviceName", auditTaskDataFillterDto.getSvcName());
            param.put("licenceDueDate", licenceDueDateString);
        }else if(MsgTemplateConstants. MSG_TEMPLATE_AUDIT_CANCELED_TASK .equalsIgnoreCase(key)){
            if(auditTaskDataFillterDto.getLicenceDueDate() != null){
                licenceDueDateString =  Formatter.formatDate(auditTaskDataFillterDto.getLicenceDueDate());
            }
            param.put("appno", appNo);
            param.put("hCIName",  auditTaskDataFillterDto.getHclName());
            param.put("hCICode",  auditTaskDataFillterDto.getHclCode());
            param.put("hCIAddress",auditTaskDataFillterDto.getAddress());
            param.put("serviceName", auditTaskDataFillterDto.getSvcName());
            param.put("licenceDueDate", licenceDueDateString);
        }else if(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_LIST_REMIND.equalsIgnoreCase(key)){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(InboxConst.URL_HTTPS).append(systemParamConfig.getIntraServerName()).append("/hcsa-licence-web/eservice/INTRANET/").append("MohAduitSystemList");
            String url = stringBuilder.toString();
            stringBuilder.delete(0,url.length());
            stringBuilder.append("<a href = '").append(url).append("'>").append(url).append("</a>");
            param.put("url", stringBuilder.toString());
        }else if(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_TCU_REMIND.equalsIgnoreCase(key)){
            param.put("ListNo","");//todo
            param.put("generatedDate",newDateString);
            param.put("byDate",newDateString);
        }
        param.put("officer_name","");
        param.put("syName",syName);
        return param;
    }

    public void saveAppForAuditToFe(AppSubmissionForAuditDto appSubmissionForAuditDto, boolean isCancel) {
        log.info("========================>>>>>sysn fe app start!!!!");
        appSubmissionForAuditDto.setIsCancel(isCancel);
        beEicGatewayClient.callEicWithTrack(appSubmissionForAuditDto, this::saveAppForAuditToFeAndCreateTrack,
                this.getClass(), "saveAppForAuditToFeAndCreateTrack");
    }

    public void saveAppForAuditToFeAndCreateTrack(AppSubmissionForAuditDto appSubmissionForAuditDto){
        //  Synchronize app to fe
        beEicGatewayClient.saveAppForAuditToFe(appSubmissionForAuditDto);
        log.info("========================>>>>>sysn fe app end!!!!");
    }

    public void updateAppCancelTaskByEventBus(AuditCombinationDto auditCombinationDto,String submitId){
        log.info("========================>>>>> canceled app !!!!");
        try {
            eventBusHelper.submitAsyncRequest(auditCombinationDto,submitId, EventBusConsts.SERVICE_NAME_APPSUBMIT,EventBusConsts. OPERATION__AUDIT_TASK_CANCELED,auditCombinationDto.getEventRefNo(),null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void updateTaskCancelTaskByEventBus(AuditCombinationDto auditCombinationDto,String submitId){
        log.info("========================>>>>> canceled task !!!!");
        try {
            List<TaskDto> taskDtos = organizationClient .getCurrTaskByRefNo(auditCombinationDto.getEventRefNo()).getEntity();
            auditCombinationDto.setTaskDtos(taskDtos);
            eventBusHelper.submitAsyncRequest(auditCombinationDto,submitId, EventBusConsts.SERVICE_NAME_ROUNTINGTASK,EventBusConsts. OPERATION__AUDIT_TASK_CANCELED,auditCombinationDto.getEventRefNo(),null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void updateLicenceSaveCancelTask( AuditCombinationDto auditCombinationDto,String status,String submitId){
       AuditTaskDataFillterDto temp = auditCombinationDto.getAuditTaskDataFillterDto();
        LicPremisesAuditDto licPremisesAuditDto = hcsaLicenceClient.getLicPremAuditByGuid(temp.getAuditId()).getEntity();
        licPremisesAuditDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        licPremisesAuditDto.setStatus(status);
        licPremisesAuditDto.setRemarks(temp.getCancelReason());
        auditCombinationDto.setLicPremisesAuditDto(licPremisesAuditDto);
        log.info("========================>>>>> canceled audit !!!!");
        try {
            eventBusHelper.submitAsyncRequest(auditCombinationDto,submitId, EventBusConsts.SERVICE_NAME_LICENCESAVE,EventBusConsts. OPERATION__AUDIT_TASK_CANCELED,auditCombinationDto.getEventRefNo(),null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void updateLicPremisesAuditDto(AuditTaskDataFillterDto temp,String status){
        LicPremisesAuditDto licPremisesAuditDto = hcsaLicenceClient.getLicPremAuditByGuid(temp.getAuditId()).getEntity();
        licPremisesAuditDto.setStatus(status);
        licPremisesAuditDto.setRemarks( temp.getCancelReason());
        hcsaLicenceClient.createLicPremAudit(licPremisesAuditDto);
    }

    private void createAudit(AuditTaskDataFillterDto temp,String submitId,AuditCombinationDto auditCombinationDto) {
        String grpNo = beEicGatewayClient.getAppNo(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK).getEntity();
        auditCombinationDto.setEventRefNo(grpNo);
        LicPremisesAuditDto licPremisesAuditDto = new LicPremisesAuditDto();
        licPremisesAuditDto.setId(generateIdClient.getSeqId().getEntity());
        licPremisesAuditDto.setAuditRiskType(temp.getRiskType());
        licPremisesAuditDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        licPremisesAuditDto.setRemarks(null);
        licPremisesAuditDto.setInRiskSocre(1);
        licPremisesAuditDto.setLicPremId(temp.getId());
        licPremisesAuditDto.setAuditType(temp.getAuditType());
        licPremisesAuditDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        // create audit event bus
        LicPremisesAuditInspectorDto audinspDto = new LicPremisesAuditInspectorDto();
        audinspDto.setInspectorId(temp.getInspectorId());
        audinspDto.setAuditId(licPremisesAuditDto.getId());
        audinspDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        auditCombinationDto.setLicPremisesAuditDto(licPremisesAuditDto);
        auditCombinationDto.setLicPremisesAuditInspectorDto(audinspDto);
    }

    public void releaseTimeForInsUserCallBack(String eventRefNum,String submissionId)throws FeignException{
        log.info("---releaseTimeForInsUserCallBack into---------------");
         if( !StringUtil.isEmpty(eventRefNum)){
             log.info(StringUtil.changeForLog("--------------- releaseTimeForInsUserCallBack eventRefNum :" + eventRefNum+ " submissionId :"+ submissionId +"--------------------"));
             List<AppPremisesInspecApptDto> appPremisesInspecApptDtos = inspectionTaskClient.getAllSystemDtosByAppPremCorrId(eventRefNum).getEntity();
             if(!IaisCommonUtils.isEmpty(appPremisesInspecApptDtos)){
                 ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
                 List<String> cancelRefNums = new ArrayList<>(1);
                 for(AppPremisesInspecApptDto appPremisesInspecApptDto : appPremisesInspecApptDtos){
                     cancelRefNums.add(appPremisesInspecApptDto.getApptRefNo());
                 }
                 apptCalendarStatusDto.setCancelRefNums(cancelRefNums);
                 apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
                 appointmentClient.updateUserCalendarStatus(apptCalendarStatusDto);
             }else {
                 log.info(StringUtil.changeForLog("------ eventRefNum :" + eventRefNum +" cannot exist appt"));
             }
         }
    }
    @Override
    public List<AuditTaskDataFillterDto> doRemove(List<AuditTaskDataFillterDto> auditTaskDataDtos) {
        List<AuditTaskDataFillterDto> removeList = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(auditTaskDataDtos)) {
            for (AuditTaskDataFillterDto temp : auditTaskDataDtos) {
                if (!temp.isSelectedForAudit()) {
                    removeList.add(temp);
                }
            }
        }
        return removeList;
    }

    @Override
    public void doCancel(List<AuditTaskDataFillterDto> auditTaskDataDtos) {
        if(!IaisCommonUtils.isEmpty(auditTaskDataDtos)){
            for(AuditTaskDataFillterDto temp: auditTaskDataDtos){
                if(temp.isSelectedForAudit()){
                    cancelTask(temp);
                }
            }
        }
    }

    @Override
    public void doRejectCancelTask(List<AuditTaskDataFillterDto> auditTaskDataDtos) {
        if(!IaisCommonUtils.isEmpty(auditTaskDataDtos)){
            for(AuditTaskDataFillterDto temp: auditTaskDataDtos){
                if(temp.isSelectedForAudit()) {
                    temp.setCancelReason(temp.getReasonForAO());
                    updateLicPremisesAuditDto(temp,AppConsts.COMMON_STATUS_ACTIVE);
                }
            }
        }
    }

    @Override
    public void doCanceledTask(List<AuditTaskDataFillterDto> auditTaskDataDtos) {
        if(!IaisCommonUtils.isEmpty(auditTaskDataDtos)){
            for(AuditTaskDataFillterDto temp: auditTaskDataDtos){
                if(temp.isSelectedForAudit()) {
                    temp.setCancelReason(temp.getReasonForAO());
                    updateLicPremisesAuditDtoByEventBus(temp,AppConsts.AUDIT_TASK_CANCELED_STATUS);
                }
            }
        }
    }
    private void cancelTask(AuditTaskDataFillterDto temp) {
        //update audit status
        updateLicPremisesAuditDto(temp,AppConsts.AUDIT_TASK_CANCEL_PENDING_STATUS);
    }

    @Override
    public List<HcsaServiceDto> getActiveHCIServices() {
        return hcsaConfigClient.getActiveServices().getEntity();
    }

    @Override
    public List<SelectOption> getCanViewAuditRoles(List<String> roleIds){
        List<SelectOption> selectOptionArrayList = IaisCommonUtils.genNewArrayList();
        List<Role> roles = getRolesByDomain(AppConsts.HALP_EGP_DOMAIN);
        add(roleIds,RoleConsts.USER_ROLE_AUDIT_PLAN,selectOptionArrayList,roles);
        //add(roleIds,RoleConsts.USER_ROLE_INSPECTIOR,selectOptionArrayList,roles);
        add(roleIds,RoleConsts.USER_ROLE_INSPECTION_LEAD,selectOptionArrayList,roles);
       return selectOptionArrayList;
    }

    private void add(List<String> roleIds,String roleId, List<SelectOption> selectOptionArrayList,List<Role> roles){
        for (String item : roleIds){
            if(roleId.equalsIgnoreCase(item)){
                selectOptionArrayList.add(getRoleSelectOption(roles,roleId));
                break;
            }
        }
    }
    @Override
    public  SelectOption getRoleSelectOption(List<Role> roles,String roleId){
       if(IaisCommonUtils.isEmpty(roles) || StringUtil.isEmpty(roleId)){
           return null;
       }
        for(Role role : roles){
            if(roleId.equalsIgnoreCase(role.getId())){
                return new SelectOption(role.getId(),role.getName());
            }
        }
        return  new SelectOption(roleId,roleId);
    }
    @Override
    public List<Role> getRolesByDomain(String domain) {
        Map<String, String> map = IaisCommonUtils.genNewHashMap();
        map.put("userDomains", domain);
        return egpUserClient.search(map).getEntity();
    }

    @Override
    public  List<SelectOption> getActiveHCIServicesByNameOrCode(List<HcsaServiceDto> hcsaServiceDtos,String type){
        List<SelectOption> activeHCISelections = IaisCommonUtils.genNewArrayList();
        if (hcsaServiceDtos != null && hcsaServiceDtos.size() > 0) {
            for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtos) {
                if (StringUtil.isEmpty(type)) {
                    activeHCISelections.add(new SelectOption(String.valueOf(hcsaServiceDto.getSvcCode()), String.valueOf(hcsaServiceDto.getSvcName())));
                } else if (HcsaLicenceBeConstant.GET_HCI_SERVICE_SELECTION_NAME_TAG.equalsIgnoreCase(type)) {
                    activeHCISelections.add(new SelectOption(String.valueOf(hcsaServiceDto.getSvcName()), String.valueOf(hcsaServiceDto.getSvcName())));
                } else {
                    activeHCISelections.add(new SelectOption(String.valueOf(hcsaServiceDto.getSvcName()), String.valueOf(hcsaServiceDto.getSvcCode())));
                }
            }
        }
        return activeHCISelections;
    }

    @Override
    public List<SelectOption> getActiveHCICode() {
        List<String> activeHcicode = hcsaLicenceClient.getActiveHCICode().getEntity();
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        if(activeHcicode != null) {
            for (String string : activeHcicode) {
                selectOptions.add(new SelectOption(string, string));
            }
        }
        return selectOptions;
    }

    public String createAuditTaskApp(List<String> licIds, String submissionId,AuditCombinationDto auditCombinationDto) {
        List<AppSubmissionDto> appSubmissionDtoList = hcsaLicenceClient.getAppSubmissionDtos(licIds).getEntity();
        String grpNo = auditCombinationDto.getEventRefNo();
        for(AppSubmissionDto entity : appSubmissionDtoList){
            filetDoc(entity);
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = entity.getAppSvcRelatedInfoDtoList();
            String serviceName = appSvcRelatedInfoDtoList.get(0).getServiceName();
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
            String svcId = hcsaServiceDto.getId();
            String svcCode = hcsaServiceDto.getSvcCode();
            appSvcRelatedInfoDtoList.get(0).setServiceId(svcId);
            appSvcRelatedInfoDtoList.get(0).setServiceCode(svcCode);
            appSvcRelatedInfoDtoList.get(0).setHciCode(auditCombinationDto.getAuditTaskDataFillterDto().getHclCode());
            entity.setAppGrpNo(grpNo);
            entity.setAppType(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK);
            entity.setAmount(0.0);
            entity.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            entity.setPreInspection(true);
            entity.setRequirement(true);
            entity.setStatus(ApplicationConsts.APPLICATION_STATUS_CREATE_AUDIT_TASK);
            entity.setEventRefNo(grpNo);
            entity.setLicenceId(auditCombinationDto.getAuditTaskDataFillterDto().getLicId());
            entity.setLicenseeId(auditCombinationDto.getAuditTaskDataFillterDto().getLicenseeId());
            setRiskToDto(entity);
        }
        auditCombinationDto.setAppSubmissionDtoList(appSubmissionDtoList);
        try {
            log.info("========================>>>>> create audit !!!!");
            eventBusHelper.submitAsyncRequest(auditCombinationDto,submissionId, EventBusConsts.SERVICE_NAME_LICENCESAVE,EventBusConsts.OPERATION_CREATE_AUDIT_TASK,auditCombinationDto.getEventRefNo(),null);
            log.info("========================>>>>> creat application!!!!");
            eventBusHelper.submitAsyncRequest(auditCombinationDto,submissionId, EventBusConsts.SERVICE_NAME_APPSUBMIT,EventBusConsts.OPERATION_CREATE_AUDIT_TASK,auditCombinationDto.getEventRefNo(),null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }
    @Override
    public void filetDoc(AppSubmissionDto appSubmissionDto){
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if(!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtoList)){
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList){
                List<AppSvcDocDto> appSvcDocDtoLit = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                if(!IaisCommonUtils.isEmpty(appSvcDocDtoLit)){
                    ListIterator<AppSvcDocDto> appSvcDocDtoListIterator = appSvcDocDtoLit.listIterator();
                    while (appSvcDocDtoListIterator.hasNext()){
                        AppSvcDocDto appSvcDocDto = appSvcDocDtoListIterator.next();
                        String fileRepoId = appSvcDocDto.getFileRepoId();
                        String svcDocId = appSvcDocDto.getSvcDocId();
                        if(StringUtil.isEmpty(fileRepoId)||StringUtil.isEmpty(svcDocId)){
                            appSvcDocDtoListIterator.remove();
                        }
                    }
                    setSvcDocsDupForPerson(appSvcDocDtoLit);
                }
            }
        }
        List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtos = appSubmissionDto.getAppGrpPrimaryDocDtos();
        if(!IaisCommonUtils.isEmpty(appGrpPrimaryDocDtos)){
            ListIterator<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoListIterator = appGrpPrimaryDocDtos.listIterator();
            while (appGrpPrimaryDocDtoListIterator.hasNext()){
                AppGrpPrimaryDocDto next = appGrpPrimaryDocDtoListIterator.next();
                String fileRepoId = next.getFileRepoId();
                String svcDocId = next.getSvcDocId();
                if(StringUtil.isEmpty(fileRepoId)||StringUtil.isEmpty(svcDocId)){
                    appGrpPrimaryDocDtoListIterator.remove();
                }
            }
        }
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

        List<RiskResultDto> riskResultDtoList = hcsaConfigClient.getRiskResult(riskAcceptiionDtoList).getEntity();

        for(AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos){
            String serviceCode = appSvcRelatedInfoDto.getServiceCode();
            RiskResultDto riskResultDto = getRiskResultDtoByServiceCode(riskResultDtoList,serviceCode);
            if(riskResultDto!= null){
                appSvcRelatedInfoDto.setScore(riskResultDto.getScore());
                appSvcRelatedInfoDto.setDoRiskDate(riskResultDto.getDoRiskDate());
            }
        }
    }


    private RiskResultDto getRiskResultDtoByServiceCode(List<RiskResultDto> riskResultDtoList,String serviceCode){
        RiskResultDto result = null;
        if(riskResultDtoList == null || StringUtil.isEmpty(serviceCode)){
            return result;
        }
        for(RiskResultDto riskResultDto : riskResultDtoList){
            if(serviceCode.equals(riskResultDto.getSvcCode())){
                result = riskResultDto ;
            }
        }
        return result;
    }

    @Override
    public boolean rightControlForRole(List<SelectOption> roleSels, String roleId) {
        if(IaisCommonUtils.isEmpty(roleSels) || StringUtil.isEmpty(roleId)){
            return false;
        }
        for(SelectOption role : roleSels){
            if(roleId.equalsIgnoreCase(role.getValue())){
                return true;
            }
        }
        return false;
    }

    private void  setSvcDocsDupForPerson(List<AppSvcDocDto> appSvcDocDtoLit){
        if(IaisCommonUtils.isNotEmpty(appSvcDocDtoLit)){
            appSvcDocDtoLit.forEach((v)->{
                if(StringUtil.isNotEmpty(v.getSvcDocId())){
                    v.setDupForPerson(hcsaConfigClient.getHcsaSvcDocConfigDtoById(v.getSvcDocId()).getEntity().getDupForPerson());
                }
            });
        }
    }
}
