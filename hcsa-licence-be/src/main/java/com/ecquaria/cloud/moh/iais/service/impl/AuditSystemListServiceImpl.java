package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.EventBusConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inbox.InboxConst;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionForAuditDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditCombinationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicInspectionGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremInspGrpCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesAuditDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesAuditInspectorDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.AuditSystemListService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import com.ecquaria.cloudfeign.FeignException;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import com.ecquaria.kafka.model.Submission;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import java.util.*;

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
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Value("${spring.application.name}")
    private String currentApp;
    @Value("${iais.current.domain}")
    private String currentDomain;
    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;
    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    private SystemParamConfig systemParamConfig;

    static String[] category = {"ADTYPE001", "ADTYPE002", "ADTYPE003"};

    @Override
    public void sendMailForAuditPlanerForSms(String emailKey) {
        List<OrgUserDto> userDtoList = organizationClient. retrieveUserRoleByRoleId(RoleConsts.USER_ROLE_AUDIT_PLAN).getEntity();
        if( !IaisCommonUtils.isEmpty(userDtoList)){
            sendEmailToInsForSms(emailKey);//NOSONAR
        }else {
            log.info("----------no audit plan user ---------");
        }
    }

    @Override
    public void sendMailForAuditPlaner(String emailKey) {
        List<OrgUserDto> userDtoList = organizationClient. retrieveUserRoleByRoleId(RoleConsts.USER_ROLE_AUDIT_PLAN).getEntity();
        if( !IaisCommonUtils.isEmpty(userDtoList)){
               sendEmailToIns(emailKey,null,null);//NOSONAR
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
        return organizationClient.getUsersByWorkGroupName(workGroupId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
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
                    assignTask(temp);
                }
            }
        }
    }

    private void assignTask(AuditTaskDataFillterDto temp) {
        String submitId = generateIdClient.getSeqId().getEntity();
        //create auditType data  and create grop info
        AuditCombinationDto auditCombinationDto = new AuditCombinationDto();
        auditCombinationDto.setAuditTaskDataFillterDto(temp);
        createAudit(temp,submitId,auditCombinationDto);
        //create grop info
        // createInspectionGroupInfo(temp,submitId);

        // create app
        List<String> licIds = new ArrayList<>(1);
        if( !StringUtil.isEmpty(temp.getLicId())){
            licIds.add(temp.getLicId());
            createAuditTaskApp(licIds,submitId,auditCombinationDto);
        }

       // send email
        if(!StringUtil.isEmpty(temp.getInspector()) &&  (temp.getUserIdToEmails() != null && temp.getUserIdToEmails().size() > 0)){
            sendEmailToIns(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_CREATE_TASK,auditCombinationDto.getEventRefNo(),temp);
            sendEmailToInsForSms(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_CREATE_TASK_SMS);
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
                createTask(auditCombinationDto.getAuditTaskDataFillterDto(), submissionId, auditCombinationDto,eventRefNum);
            }

        }else {
            log.info("========createTaskCallBack  submissionList is null.");
        }
    }
    public void createTask(AuditTaskDataFillterDto temp,String submitId,AuditCombinationDto auditCombinationDto,String eventRefNum){
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
            String corrId = applicationClient.getCorrIdByAppId(postApps.get(0).getId()).getEntity();
            taskDto.setRefNo(corrId);
            List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = new ArrayList(1);
            for(ApplicationDto applicationDto : postApps){
                HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
                hcsaSvcStageWorkingGroupDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
                hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
                hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
                hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
                hcsaSvcStageWorkingGroupDtos = taskHcsaConfigClient.getWrkGrp(hcsaSvcStageWorkingGroupDtos).getEntity();
                taskDto.setScore(getConfigScoreForService( hcsaSvcStageWorkingGroupDtos,applicationDto.getServiceId(),HcsaConsts.ROUTING_STAGE_INS,applicationDto.getApplicationType()));
            }
        }else {
            log.info("=============== group id is null.==========");
            return;

        }
        List<TaskDto> createTaskDtoList = IaisCommonUtils.genNewArrayList();
        createTaskDtoList.add(taskDto);
        auditCombinationDto.setTaskDtos(createTaskDtoList);
        //taskService.createTasks(createTaskDtoList);
        try {
            log.info("========================>>>>> create task !!!!");
            eventBusHelper.submitAsyncRequest(auditCombinationDto,submitId, EventBusConsts.SERVICE_NAME_ROUNTINGTASK,EventBusConsts.OPERATION_CREATE_AUDIT_TASK_CALL_BACK,auditCombinationDto.getEventRefNo(),null);
        } catch (Exception e) {
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
        auditCombinationDto.setEventRefNo(RefNo);
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
            sendEmailToInsForSms(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_CANCELED_TASK_SMS);
        }else {
            log.info("-----------Inspector id is null or UserIdToEmails is null");
        }

        }catch (Exception e){
            log.error(e.getMessage(),e);
        }

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
    public void sendEmailToInsForSms(String emailKey) {
        try{
            Map<String,Object> param = IaisCommonUtils.genNewHashMap();
            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(emailKey);
            emailParam.setTemplateContent(param);
            emailParam.setQueryCode(emailKey);
            emailParam.setReqRefNum(emailKey);
            emailParam.setRefIdType(null);
            emailParam.setRefId(null);
            notificationHelper.sendNotification(emailParam);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    private Map<String,Object> getParamByMesskey(String key, String appNo, AuditTaskDataFillterDto auditTaskDataFillterDto){
        Map<String,Object> param =   IaisCommonUtils.genNewHashMap();
        String syName = AppConsts.MOH_AGENCY_NAM_GROUP+"<br/>"+AppConsts.MOH_AGENCY_NAME;
        String newDateString = Formatter.formatDate(new Date());
        String licenceDueDateString;
        if(auditTaskDataFillterDto.getLicenceDueDate() != null){
            licenceDueDateString =  Formatter.formatDate(auditTaskDataFillterDto.getLicenceDueDate());
        }else {
            licenceDueDateString = "";
        }
        if(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_CREATE_TASK.equalsIgnoreCase(key)){
            param.put("DDMMYYYY",newDateString);
            param.put("appno", appNo);
            param.put("hCIName",  auditTaskDataFillterDto.getHclName());
            param.put("hCICode",  auditTaskDataFillterDto.getHclCode());
            param.put("hCIAddress",auditTaskDataFillterDto.getAddress());
            param.put("serviceName", auditTaskDataFillterDto.getSvcName());
            param.put("licenceDueDate", licenceDueDateString);
        }else if(MsgTemplateConstants. MSG_TEMPLATE_AUDIT_CANCELED_TASK .equalsIgnoreCase(key)){
            param.put("appno", appNo);
            param.put("hCIName",  auditTaskDataFillterDto.getHclName());
            param.put("hCICode",  auditTaskDataFillterDto.getHclCode());
            param.put("hCIAddress",auditTaskDataFillterDto.getAddress());
            param.put("serviceName", auditTaskDataFillterDto.getSvcName());
            param.put("licenceDueDate", licenceDueDateString);
        }else if(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_LIST_REMIND.equalsIgnoreCase(key)){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(InboxConst.URL_HTTPS).append(systemParamConfig.getIntraServerName()).append(InboxConst.URL_LICENCE_WEB_MODULE).append("MohAduitSystemList");
            String url = stringBuilder.toString();
            param.put("url", url);
        }else if(MsgTemplateConstants.MSG_TEMPLATE_AUDIT_TCU_REMIND.equalsIgnoreCase(key)){
            param.put("List No","");//todo
            param.put("generatedDate",newDateString);
            param.put("byDate",newDateString);
        }
        param.put("syName",syName);
        return param;
    }

    public  void saveAppForAuditToFe(AppSubmissionForAuditDto appSubmissionForAuditDto, boolean isCancel){
        log.info("========================>>>>>sysn fe app start!!!!");
        appSubmissionForAuditDto.setIsCancel(isCancel);
        EicRequestTrackingDto postSaveTrack = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.LICENCE_CLIENT, AuditSystemListServiceImpl.class.getName(),
                "saveAppForAuditToFeAndCreateTrack", currentApp + "-" + currentDomain,
                AppSubmissionForAuditDto.class.getName(), JsonUtil.parseToJson(appSubmissionForAuditDto));
        FeignResponseEntity<EicRequestTrackingDto> fetchResult = eicRequestTrackingHelper.getLicEicClient().getPendingRecordByReferenceNumber(postSaveTrack.getRefNo());
        try{
            if (HttpStatus.SC_OK == fetchResult.getStatusCode()) {
                EicRequestTrackingDto entity = fetchResult.getEntity();
                if (AppConsts.EIC_STATUS_PENDING_PROCESSING.equals(entity.getStatus())){
                    saveAppForAuditToFeAndCreateTrack(appSubmissionForAuditDto);
                    entity.setProcessNum(1);
                    Date now = new Date();
                    entity.setFirstActionAt(now);
                    entity.setLastActionAt(now);
                    entity.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                    eicRequestTrackingHelper.getLicEicClient().saveEicTrack(entity);
                }
            }
        }catch (Exception e){
            log.error(StringUtil.changeForLog(e.getMessage()));
        }
    }
    public  void saveAppForAuditToFeAndCreateTrack(AppSubmissionForAuditDto appSubmissionForAuditDto){
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        //  Synchronize app to fe
        beEicGatewayClient.saveAppForAuditToFe(appSubmissionForAuditDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
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
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        String grpNo = beEicGatewayClient.getAppNo(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK,signature.date(), signature.authorization(), signature2.date(), signature2.authorization()).getEntity();
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
        //licPremisesAuditDto = hcsaLicenceClient.createLicPremAudit(licPremisesAuditDto).getEntity();
        LicPremisesAuditInspectorDto audinspDto = new LicPremisesAuditInspectorDto();
        audinspDto.setInspectorId(temp.getInspectorId());
        audinspDto.setAuditId(licPremisesAuditDto.getId());
        audinspDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
         //hcsaLicenceClient.createLicPremisesAuditInspector(audinspDto);
        LicInspectionGroupDto dto = new LicInspectionGroupDto();
        dto.setId(generateIdClient.getSeqId().getEntity());
        dto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        dto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        LicPremInspGrpCorrelationDto dtocorre = new LicPremInspGrpCorrelationDto();
        dtocorre.setInsGrpId(dto.getId());
        dtocorre.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        dtocorre.setLicPremId(temp.getId());
        dtocorre.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        auditCombinationDto.setLicPremisesAuditDto(licPremisesAuditDto);
        auditCombinationDto.setLicPremisesAuditInspectorDto(audinspDto);
        auditCombinationDto.setLicInspectionGroupDto(dto);
        auditCombinationDto.setLicPremInspGrpCorrelationDto(dtocorre);
        log.info("========================>>>>> create audit !!!!");
        try {
            eventBusHelper.submitAsyncRequest(auditCombinationDto,submitId, EventBusConsts.SERVICE_NAME_LICENCESAVE,EventBusConsts.OPERATION_CREATE_AUDIT_TASK,auditCombinationDto.getEventRefNo(),null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
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
        log.info("========================>>>>> creat application!!!!");
        try {
            eventBusHelper.submitAsyncRequest(auditCombinationDto,submissionId, EventBusConsts.SERVICE_NAME_APPSUBMIT,EventBusConsts.OPERATION_CREATE_AUDIT_TASK,auditCombinationDto.getEventRefNo(),null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    private void setRiskToDto(AppSubmissionDto appSubmissionDto) {
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
}
