package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemParameterConstant;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.SmsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.SendTaskTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskEmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.utils.TaskUtil;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.CommonEmailClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaAppClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskHcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskOrganizationClient;
import com.ecquaria.cloudfeign.FeignException;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * TaskServiceImpl
 *
 * @author suocheng
 * @date 11/20/2019
 */
@Service
@Slf4j
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskOrganizationClient taskOrganizationClient;

    @Autowired
    private TaskHcsaConfigClient taskHcsaConfigClient;

    @Autowired
    private TaskApplicationClient taskApplicationClient;

    @Autowired
    private HcsaAppClient hcsaAppClient;
    @Autowired
    private GenerateIdClient generateIdClient;
    @Value("${iais.email.sender}")
    private String mailSender;
    @Autowired
    private CommonEmailClient emailClient;
    @Autowired
    private SystemParamConfig systemParamConfig;



    @Override
    public List<TaskDto> createTasks(List<TaskDto> taskDtos) {
        return taskOrganizationClient.createTask(taskDtos).getEntity();
    }

    @Override
    public TaskDto updateTask(TaskDto taskDto) {
        return taskOrganizationClient.updateTask(taskDto).getEntity();
    }

    @Override
    public List<HcsaSvcStageWorkingGroupDto> getTaskConfig(List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos) {
        return taskHcsaConfigClient.getWrkGrp(hcsaSvcStageWorkingGroupDtos).getEntity();
    }

    @Override
    public TaskDto getTaskById(String taskId) {
        return taskOrganizationClient.getTaskById(taskId).getEntity();
    }

    @Override
    public TaskDto getRoutingTask(ApplicationDto applicationDto, String statgId,String roleId,String correlationId) throws FeignException {
        log.debug(StringUtil.changeForLog("the do routingTask start ...."));
        TaskDto result = null;
        if(applicationDto == null  || StringUtil.isEmpty(statgId)){
            log.debug(StringUtil.changeForLog("The applicationDto or stageId is null"));
            return result;
        }
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        applicationDtos.add(applicationDto);
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos,statgId);
        hcsaSvcStageWorkingGroupDtos = this.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
        if(hcsaSvcStageWorkingGroupDtos!= null && hcsaSvcStageWorkingGroupDtos.size() > 0){
            String workGroupId = hcsaSvcStageWorkingGroupDtos.get(0).getGroupId();
            log.info(StringUtil.changeForLog("work group id is [ "+workGroupId+" ]"));
            log.info(StringUtil.changeForLog("------"+JsonUtil.parseToJson(hcsaSvcStageWorkingGroupDtos)));
            TaskDto taskScoreDto =new TaskDto();
            log.info(StringUtil.changeForLog("The getRoutingTask getSchemeType() is -->:"+hcsaSvcStageWorkingGroupDtos.get(0).getSchemeType()));
            if(SystemParameterConstant.ROUND_ROBIN.equals(hcsaSvcStageWorkingGroupDtos.get(0).getSchemeType())){
                taskScoreDto = getUserIdForWorkGroup(workGroupId);
            }

            SendTaskTypeDto sendTaskTypeDto = new SendTaskTypeDto();
            sendTaskTypeDto.setApplicationDtos(applicationDtos);
            sendTaskTypeDto.setStage(statgId);
            String scheme = taskHcsaConfigClient.getSendTaskType(sendTaskTypeDto).getEntity();
            log.info(StringUtil.changeForLog("The getRoutingTask scheme is -->:"+scheme));
            String taskType = TaskConsts.TASK_TYPE_MAIN_FLOW;
            String userId = null;
            boolean isSystemAdmin = false;
            if(taskScoreDto != null){
                userId = taskScoreDto.getUserId();
            }else{
                log.info(StringUtil.changeForLog("The getRoutingTaskOneUserForSubmisison taskScoreDto is null"));
            }
            Date  assignDate = new Date();
            log.info(StringUtil.changeForLog("The getRoutingTask userId is -->:"+userId));
            switch (scheme){
                case TaskConsts.TASK_SCHEME_TYPE_COMMON :
                    userId = null;
                    assignDate = null;
                    break;
                case TaskConsts.TASK_SCHEME_TYPE_ASSIGN :
                    userId = null;
                    taskType = TaskConsts.TASK_TYPE_MAIN_FLOW_SUPER;
                    assignDate = null;
                    break;
                case TaskConsts.TASK_SCHEME_TYPE_ROUND :
                    if(StringUtil.isEmpty(userId)){
                        //0066643
                        List<OrgUserDto> orgUserDtos = taskOrganizationClient.retrieveOrgUserAccountByRoleId(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN).getEntity();
                        if(!IaisCommonUtils.isEmpty(orgUserDtos)){
                            userId = orgUserDtos.get(0).getId();
                            isSystemAdmin = true;
                            log.info(StringUtil.changeForLog("The getRoutingTask sendNoteToAdm "));
                            sendNoteToAdm(applicationDto.getApplicationNo(),correlationId,orgUserDtos.get(0));
                        }
                    }
                    break;
            }
            log.info(StringUtil.changeForLog("The getRoutingTask isSystemAdmin is -->:"+isSystemAdmin));
            log.info(StringUtil.changeForLog("The getRoutingTask taskType is -->:"+taskType));
            log.info(StringUtil.changeForLog("The getRoutingTask userId is -->:"+userId));
            log.info(StringUtil.changeForLog("The getRoutingTask assignDate is -->:"+assignDate));

            int score =  getConfigScoreForService(hcsaSvcStageWorkingGroupDtos,applicationDto.getServiceId(),
                    statgId,applicationDto.getApplicationType());
            //handle the taskUrl
            String TaskUrl = TaskConsts.TASK_PROCESS_URL_MAIN_FLOW;
            if(HcsaConsts.ROUTING_STAGE_INS.equals(statgId)){
                TaskUrl = TaskConsts.TASK_PROCESS_URL_APPT_INSPECTION_DATE;
            }
             result = TaskUtil.getTaskDto(applicationDto.getApplicationNo(),statgId,taskType,
                     correlationId,TaskConsts.TASK_STATUS_PENDING,isSystemAdmin?null:workGroupId,
                    userId, assignDate,null,score,TaskUrl,roleId,
                     IaisEGPHelper.getCurrentAuditTrailDto());
        }else{
            log.debug(StringUtil.changeForLog("can not get the HcsaSvcStageWorkingGroupDto ..."));
        }
        log.debug(StringUtil.changeForLog("the do routingTask start ...."));
        return result;
    }

    @Override
    public List<OrgUserDto> getUsersByWorkGroupId(String workGroupId, String status) {
        return taskOrganizationClient.getUsersByWorkGroupName(workGroupId,status).getEntity();
    }

    @Override
    public List<OrgUserDto> getUsersByWorkGroupIdExceptLeader(String workGroupId, String status) {
        return taskOrganizationClient.getUsersByWorkGroupNameExceptLeader(workGroupId,status).getEntity();
    }

    @Override
    public List<TaskDto> getTaskDtoScoresByWorkGroupId(String workGroupId) {
        return taskOrganizationClient.getTaskScores(workGroupId,String.valueOf(systemParamConfig.getWorkloadCalculation())).getEntity();
    }

    @Override
    public TaskHistoryDto getRoutingTaskOneUserForSubmisison(List<ApplicationDto> applicationDtos, String stageId, String roleId, AuditTrailDto auditTrailDto, String createHistoryRoleId,String createWorkGroupId) throws FeignException{
        return getRoutingTaskOneUserForSubmisison(applicationDtos,stageId,roleId,auditTrailDto,createHistoryRoleId,createWorkGroupId,false);
    }
    @Override
    public TaskHistoryDto getRoutingTaskOneUserForSubmisison(List<ApplicationDto> applicationDtos, String stageId,String roleId, AuditTrailDto auditTrailDto, String createHistoryRoleId,String createWorkGroupId,boolean isFEActionBy) throws FeignException {
        log.debug(StringUtil.changeForLog("the do getRoutingTaskOneUserForSubmisison start ...."));
        log.info(StringUtil.changeForLog("---------------"+ JsonUtil.parseToJson(applicationDtos) +"--------"+stageId));
        TaskHistoryDto result = new TaskHistoryDto();
        if(applicationDtos != null && applicationDtos.size() > 0){
            List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos,stageId);
            hcsaSvcStageWorkingGroupDtos = this.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
            if(hcsaSvcStageWorkingGroupDtos!= null && hcsaSvcStageWorkingGroupDtos.size() > 0){
                String workGroupId = hcsaSvcStageWorkingGroupDtos.get(0).getGroupId();
                TaskDto taskScoreDto = getUserIdForWorkGroup(workGroupId);
               // List<AppPremisesCorrelationDto> appPremisesCorrelationDtos = this.getAppPremisesCorrelationByAppGroupId(applicationDtos.get(0).getAppGrpId());
                List<TaskDto> taskDtos = IaisCommonUtils.genNewArrayList();
                List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = IaisCommonUtils.genNewArrayList();

                SendTaskTypeDto sendTaskTypeDto = new SendTaskTypeDto();
                sendTaskTypeDto.setApplicationDtos(applicationDtos);
                sendTaskTypeDto.setStage(stageId);
                String scheme = taskHcsaConfigClient.getSendTaskType(sendTaskTypeDto).getEntity();
                log.info(StringUtil.changeForLog("The getRoutingTaskOneUserForSubmisison scheme is -->:"+scheme));
                String taskType = TaskConsts.TASK_TYPE_MAIN_FLOW;
                String userId = null;
                if(taskScoreDto != null){
                    userId = taskScoreDto.getUserId();
                }else{
                    log.info(StringUtil.changeForLog("The getRoutingTaskOneUserForSubmisison taskScoreDto is null"));
                }
                Date  assignDate = new Date();
                log.info(StringUtil.changeForLog("The getRoutingTaskOneUserForSubmisison userId is -->:"+userId));
                OrgUserDto orgUserDto = null;
                boolean isSystemAdmin = false;
                switch (scheme){
                    case TaskConsts.TASK_SCHEME_TYPE_COMMON :
                        userId = null;
                        assignDate = null;
                        break;
                    case TaskConsts.TASK_SCHEME_TYPE_ASSIGN :
                        userId = null;
                        taskType = TaskConsts.TASK_TYPE_MAIN_FLOW_SUPER;
                        assignDate = null;
                        break;
                    case TaskConsts.TASK_SCHEME_TYPE_ROUND :
                        if(StringUtil.isEmpty(userId)){
                            //0066643
                            List<OrgUserDto> orgUserDtos = taskOrganizationClient.retrieveOrgUserAccountByRoleId(RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN).getEntity();
                            if(!IaisCommonUtils.isEmpty(orgUserDtos)){
                                orgUserDto = orgUserDtos.get(0);
                                userId = orgUserDto.getId();
                                isSystemAdmin = true;
                            }
                        }
                        break;
                }
                log.info(StringUtil.changeForLog("The getRoutingTaskOneUserForSubmisison isSystemAdmin is -->:"+isSystemAdmin));
                log.info(StringUtil.changeForLog("The getRoutingTaskOneUserForSubmisison taskType is -->:"+taskType));
                log.info(StringUtil.changeForLog("The getRoutingTaskOneUserForSubmisison userId is -->:"+userId));
                log.info(StringUtil.changeForLog("The getRoutingTaskOneUserForSubmisison assignDate is -->:"+assignDate));
                //handle the taskUrl
                String TaskUrl = TaskConsts.TASK_PROCESS_URL_MAIN_FLOW;
                if(HcsaConsts.ROUTING_STAGE_INS.equals(stageId)){
                    TaskUrl = TaskConsts.TASK_PROCESS_URL_APPT_INSPECTION_DATE;
                }
                for(ApplicationDto applicationDto : applicationDtos){
                    int score =  getConfigScoreForService(hcsaSvcStageWorkingGroupDtos,applicationDto.getServiceId(),
                            stageId,applicationDto.getApplicationType());
                    List<AppPremisesCorrelationDto> appPremisesCorrelations = getAppPremisesCorrelationId(applicationDto.getId());
                    if(!IaisCommonUtils.isEmpty(appPremisesCorrelations)){
                        for (AppPremisesCorrelationDto appPremisesCorrelationDto :appPremisesCorrelations ){
                            TaskDto taskDto = TaskUtil.getTaskDto(applicationDto.getApplicationNo(),stageId,taskType,
                                    appPremisesCorrelationDto.getId(),TaskConsts.TASK_STATUS_PENDING,isSystemAdmin?null:workGroupId,
                                    userId, assignDate,null,score,TaskUrl,roleId,
                                    auditTrailDto);
                            if(applicationDto.getStatus().equals(ApplicationConsts.APPLICATION_STATUS_WITHDRAWN)){
                                taskDto.setTaskStatus(TaskConsts.TASK_STATUS_REMOVE);
                            }
                            taskDtos.add(taskDto);
                            if(orgUserDto!=null){
                                log.info(StringUtil.changeForLog("The getRoutingTaskOneUserForSubmisison sendNoteToAdm "));
                                sendNoteToAdm(applicationDto.getApplicationNo(),appPremisesCorrelationDto.getId(),orgUserDto);
                            }
                            //create history
                            log.debug(StringUtil.changeForLog("the appPremisesCorrelationId is -->;"+appPremisesCorrelationDto.getId()));
                            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto =
                                    createAppPremisesRoutingHistory(applicationDto,applicationDto.getStatus(),
                                            stageId,null,createHistoryRoleId,auditTrailDto,isFEActionBy);
                            appPremisesRoutingHistoryDto.setWrkGrpId(createWorkGroupId==null?workGroupId:createWorkGroupId);
                            appPremisesRoutingHistoryDtos.add(appPremisesRoutingHistoryDto);
                        }
                    }
                }
                result.setTaskDtoList(taskDtos);
                result.setAppPremisesRoutingHistoryDtos(appPremisesRoutingHistoryDtos);
            }else{
                log.debug(StringUtil.changeForLog("can not get the HcsaSvcStageWorkingGroupDto ..."));
            }
        }else{
            log.info(StringUtil.changeForLog("The applicationDtos is null"));
        }

        log.debug(StringUtil.changeForLog("the do getRoutingTaskOneUserForSubmisison end ...."));
        return  result;
    }





//    public void routingTaskOneUserForSubmisison(List<ApplicationDto> applicationDtos,String stageId,String roleId,AuditTrailDto auditTrailDto) throws FeignException {
//        log.debug(StringUtil.changeForLog("the do routingTaskOneUserForSubmisison start ...."));
//        TaskHistoryDto taskHistoryDto = getRoutingTaskOneUserForSubmisison(applicationDtos,stageId,roleId,auditTrailDto);
//        List<TaskDto> taskDtos = taskHistoryDto.getTaskDtoList();
//        List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = taskHistoryDto.getAppPremisesRoutingHistoryDtos();
//        if(taskDtos!=null && taskDtos.size() >0 && appPremisesRoutingHistoryDtos !=null && appPremisesRoutingHistoryDtos.size()>0){
//            this.createTasks(taskDtos);
//            this.createHistorys(appPremisesRoutingHistoryDtos);
//        }else {
//            log.debug(StringUtil.changeForLog("The taksDto is null !!!"));
//        }
//
//        log.debug(StringUtil.changeForLog("the do routingTaskOneUserForSubmisison end ...."));
//    }



    @Override
    public TaskDto getLowestTaskScore(List<TaskDto> taskScoreDtos, List<OrgUserDto> users) {
        log.debug(StringUtil.changeForLog("the do getLowestTaskScore start ...."));
        TaskDto result = null;
        //There is not user in this workgroup return null
        if(IaisCommonUtils.isEmpty(users)){
            return  result;
        }else{
            log.debug(StringUtil.changeForLog("the do getLowestTaskScore users.size() is-->:"+users.size()));
        }
        //There is not taskScoreDtos ,return the users first.
        if(IaisCommonUtils.isEmpty(taskScoreDtos)){
            result = new TaskDto();
            result.setUserId(users.get(0).getId());
            result.setScore(0);
        }else{
            log.debug(StringUtil.changeForLog("the do getLowestTaskScore taskScoreDtos.size() is-->:"+taskScoreDtos.size()));
            //if user do not Exist in the taskScoreDtos, return this user
            for(OrgUserDto user : users){
                if(!StringUtil.isEmpty(user.getId())){
                    boolean isExist = isExist(taskScoreDtos,user.getId());
                    if(!isExist){
                        result = new TaskDto();
                        result.setUserId(user.getId());
                        result.setScore(0);
                        break;
                    }
                }
            }
        }
        //there is not new , return the Lowest Score taskScoreDtos. because there is sort in the SQL side
        //public
        if(result == null){
            for(TaskDto taskDto : taskScoreDtos){
               boolean isExist = isExistUser(users,taskDto.getUserId());
               if(isExist){
                   result = taskDto;
                   break;
               }
            }
           // result.setScore(0);
        }
        log.debug(StringUtil.changeForLog("the do getLowestTaskScore end ...."));
        return result;
    }

    @Override
    public List<AppPremisesCorrelationDto> getAppPremisesCorrelationByAppGroupId(String appGroupId) {
        return taskApplicationClient.getGroupAppsByNo(appGroupId).getEntity();
    }
    @Override
    public List<AppPremisesRoutingHistoryDto> createHistorys(List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtoList) {
        return taskApplicationClient.createAppPremisesRoutingHistorys(appPremisesRoutingHistoryDtoList).getEntity();
    }

    @Override
    public List<TaskDto> getCommPoolByGroupWordId(String workGroupId) {
        return taskOrganizationClient.getCommPoolTaskByWorkGroupId(workGroupId).getEntity();
    }

    @Override
    public int remainDays(TaskDto taskDto) {
        int result = 0;
        //todo: wait count kpi
        /*String  resultStr = DurationFormatUtils.formatPeriod(taskDto.getDateAssigned().getTime(),taskDto.getSlaDateCompleted().getTime(), "d");
        log.debug(StringUtil.changeForLog("The resultStr is -->:" + resultStr));*/
        return  result;
    }

    private List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId){
        log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos start ...."));
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = IaisCommonUtils.genNewArrayList();
        log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos stageId -->:"+stageId));
        for(ApplicationDto applicationDto : applicationDtos){
            AppGrpPremisesEntityDto appGrpPremisesEntityDto = hcsaAppClient.getPremisesByAppNo(applicationDto.getApplicationNo()).getEntity();
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setStageId(stageId);
            hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
            hcsaSvcStageWorkingGroupDto.setBaseServiceId(applicationDto.getRoutingServiceId());
            hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
            if(appGrpPremisesEntityDto != null){
                hcsaSvcStageWorkingGroupDto.setPremiseType(appGrpPremisesEntityDto.getPremisesType());
            }else{
                log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos this APP do not have the premise :"+applicationDto.getApplicationNo()));
            }
            hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
        }
        log.debug(StringUtil.changeForLog("the do generateHcsaSvcStageWorkingGroupDtos end ...."));
        return hcsaSvcStageWorkingGroupDtos;
    }

    @Override
    public TaskDto getUserIdForWorkGroup(String workGroupId) throws FeignException {
        return getUserIdForWorkGroup(workGroupId,null);
    }

    @Override
    public TaskDto getUserIdForWorkGroup(String workGroupId,String excpetUserId) throws FeignException {
        log.debug(StringUtil.changeForLog("the do getUserIdForWorkGroup start ...."));
        TaskDto result = null;
        if(StringUtil.isEmpty(workGroupId)){
            return result;
        }
        log.debug(StringUtil.changeForLog("the do getUserIdForWorkGroup workGroupId is -->:"+workGroupId));
        List<OrgUserDto> orgUserDtos = getUsersByWorkGroupIdExceptLeader(workGroupId,AppConsts.COMMON_STATUS_ACTIVE);
        orgUserDtos = removeUnavailableUser(orgUserDtos,excpetUserId);
        List<TaskDto> taskScoreDtos = this.getTaskDtoScoresByWorkGroupId(workGroupId);
        result = this.getLowestTaskScore(taskScoreDtos,orgUserDtos);
        if(result != null && StringUtil.isEmpty(result.getWkGrpId())){
            result.setWkGrpId(workGroupId);
        }
        log.debug(StringUtil.changeForLog("the do getUserIdForWorkGroup end ...."));
        return result;
    }

    @Override
    public Set<String> getInspectiors(String appNo , String processUrl, String roleId) {
        Set<String> entity = taskOrganizationClient.getInspectors(appNo, processUrl, roleId).getEntity();
        return entity;
    }

    @Override
    public List<TaskDto> getTaskDtoByDate(String roundDate,boolean isRouting) {
        return taskOrganizationClient.getTaskDtoByDate(roundDate,isRouting).getEntity();
    }

    @Override
    public List<TaskDto> getTaskbyApplicationNo(String applicationNo) {

        return  taskOrganizationClient.getTaskbyApplicationNo(applicationNo).getEntity();
    }

    @Override
    public List<TaskDto> getTaskByUrlAndRefNo(String refNo, String processUrl) {
        return taskOrganizationClient.getTaskByUrlAndRefNo(refNo,processUrl).getEntity();
    }

    @Override
    public List<TaskDto> getTaskRfi(String applicationNo) {
        return  taskOrganizationClient.getTaskRfi(applicationNo).getEntity();
    }

    @Override
    public void sendNoteToAdm(String appNo,String refNo, OrgUserDto orgUserDto) {
        try {
            MsgTemplateDto msgTemplateDto = generateIdClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_SYSTEM_ADMIN_REMINDER).getEntity();
            String emailTemplate = msgTemplateDto.getMessageContent();
            Map<String, Object> templateMap = IaisCommonUtils.genNewHashMap();
            List<String> receiptEmail = IaisCommonUtils.genNewArrayList();
            receiptEmail.add(orgUserDto.getEmail());
            templateMap.put("appNo",appNo);
            String mesContext;
            try {
                mesContext = MsgUtil.getTemplateMessageByContent(emailTemplate, templateMap);
            } catch (IOException | TemplateException e) {
                log.error(e.getMessage(), e);
                throw new IaisRuntimeException(e);
            }
            //send email
            EmailDto emailDto = new EmailDto();
            emailDto.setContent(mesContext);
            emailDto.setSubject("MOH HALP - "+appNo+" for your action");
            emailDto.setSender(mailSender);
            emailDto.setReceipts(receiptEmail);
            emailDto.setClientQueryCode(refNo);
            emailDto.setReqRefNum(refNo);
            emailClient.sendNotification(emailDto);

            //send sms
            List<String> mobile = IaisCommonUtils.genNewArrayList();
            String phoneNo = orgUserDto.getMobileNo();
            if(!StringUtil.isEmpty(phoneNo)) {
                mobile.add(phoneNo);
            }
            SmsDto smsDto = new SmsDto();
            smsDto.setSender(mailSender);
            smsDto.setContent("MOH HALP - "+appNo+" for your action");
            smsDto.setOnlyOfficeHour(true);
            smsDto.setReceipts(mobile);
            smsDto.setReqRefNum(appNo);
            emailClient.sendSMS(mobile, smsDto, appNo);
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    @Override
    public boolean checkCompleteTaskByApplicationNo(List<ApplicationDto> applicationDtoList,String correlationId) {
        if(IaisCommonUtils.isEmpty(applicationDtoList)){
            return true;
        }
        boolean flag = true;
        for(ApplicationDto applicationDto : applicationDtoList){
            List<TaskDto> taskList = taskOrganizationClient.getTaskbyApplicationNo(applicationDto.getApplicationNo()).getEntity();
            String status = applicationDto.getStatus();
            if(!IaisCommonUtils.isEmpty(taskList) || !checkTaskAppStatus(status,correlationId)){
                log.debug(StringUtil.changeForLog("task list are not null,application no : " + applicationDto.getApplicationNo()));
                flag = false;
                break;
            }
        }
        log.debug(StringUtil.changeForLog("checkCompleteTaskByApplicationNo flag : " + flag));
        return flag;
    }

    private boolean checkTaskAppStatus(String status,String correlationId){
        boolean inspectionFlowOver = true;
        boolean pendingRfi = ApplicationConsts.APPLICATION_STATUS_REQUEST_INFORMATION.equals(status);
        boolean appWithdraw = ApplicationConsts.APPLICATION_STATUS_WITHDRAWN.equals(status);
        boolean appStatusFlag = false;
        if(!pendingRfi || appWithdraw) {
            appStatusFlag = true;
        }
        log.info(StringUtil.changeForLog("checkTaskAppStatus pendingRfi : " + pendingRfi));
        AppInspectionStatusDto inspectionStatusDto = hcsaAppClient.getAppInspectionStatusByPremId(correlationId).getEntity();
        //in inspection && not the end
        if(inspectionStatusDto != null){
            inspectionFlowOver = InspectionConstants.INSPECTION_STATUS_PENDING_AO2_RESULT.equals(inspectionStatusDto.getStatus());
        }
        log.info(StringUtil.changeForLog("checkTaskAppStatus inInspection : " + inspectionFlowOver));
        log.info(StringUtil.changeForLog("checkTaskAppStatus status : " + status));
        log.info(StringUtil.changeForLog("checkTaskAppStatus inInspection : " + correlationId));
        boolean flag = appStatusFlag && inspectionFlowOver;
        log.debug(StringUtil.changeForLog("checkTaskAppStatus flag : " + flag));
        return flag;
    }

    @Override
    public List<TaskEmailDto> getEmailNotifyList(){
        return taskOrganizationClient.getEmailNotifyList().getEntity();
    }

    @Override
    public List<OrgUserDto> getEmailNotifyLeader(String taskId){
        return taskOrganizationClient.getEmailNotifyLeader(taskId).getEntity();
    }

    @Override
    public List<String> getDistincTaskRefNumByCurrentGroup(String wrkGroupId) {
        List<TaskDto> commPoolByGroupWordId =  getCommPoolByGroupWordId(wrkGroupId);
        List<String> taskRefNumList = IaisCommonUtils.genNewArrayList();
        commPoolByGroupWordId.forEach(i -> taskRefNumList.add(i.getRefNo()));
        return taskRefNumList.stream().distinct().collect(Collectors.toList());
    }

    @Override
    public Map<String, List<String>> getAllWorkGroupMembers(List<String> groupIdList){
        return taskOrganizationClient.getAllWorkGroupMembers(groupIdList).getEntity();
    }
    private boolean isExist(List<TaskDto> taskScoreDtos,String userId){
        boolean result = false;
        for (TaskDto taskScoreDto : taskScoreDtos){
            if(userId.equals(taskScoreDto.getUserId()) ){
                result = true;
                break;
            }
        }
        return result;
    }
    private boolean isExistUser(List<OrgUserDto> users,String userId){
        boolean result = false;
        for (OrgUserDto orgUserDto : users){
            if(orgUserDto.getId().equals(userId)){
                result = true;
                break;
            }
        }
        return result;
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
    private List<AppPremisesCorrelationDto> getAppPremisesCorrelationId(String appId){
        return  taskApplicationClient.getAppPremisesCorrelationsByAppId(appId).getEntity();
    }
    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(ApplicationDto applicationDto, String appStatus,
                                                                         String stageId, String internalRemarks,String roleId,
                                                                         AuditTrailDto auditTrailDto,boolean isFEActionBy){
        log.info(StringUtil.changeForLog("The isFEActionBy is -->:"+isFEActionBy));
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        String actionBy = AppConsts.USER_ID_SYSTEM;
        if(isFEActionBy){
            ApplicationGroupDto entity = hcsaAppClient.getAppGrpById(applicationDto.getAppGrpId()).getEntity();
            if(entity!=null && !StringUtil.isEmpty(entity.getSubmitBy())){
                actionBy =  entity.getSubmitBy();
            }
        }else{
            if(auditTrailDto != null && !StringUtil.isEmpty(auditTrailDto.getMohUserGuid())){
                actionBy = auditTrailDto.getMohUserGuid();
            }
        }
        log.info(StringUtil.changeForLog("The actionBy is -->:"+actionBy));
        appPremisesRoutingHistoryDto.setApplicationNo(applicationDto.getApplicationNo());
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(actionBy);
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setAuditTrailDto(auditTrailDto);
        return appPremisesRoutingHistoryDto;
    }
    private  List<OrgUserDto> removeUnavailableUser(List<OrgUserDto> orgUserDtos,String excpetUserId){
        log.debug(StringUtil.changeForLog("the do removeUnavailableUser start ...."));
        List<OrgUserDto> result = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(orgUserDtos)){
            log.debug(StringUtil.changeForLog("the do removeUnavailableUser orgUserDtos.size() -->:"+orgUserDtos.size()));
            for (OrgUserDto orgUserDto : orgUserDtos){
                if(orgUserDto.getAvailable()){
                    if(StringUtil.isEmpty(excpetUserId)||!excpetUserId.equals(orgUserDto.getId())){
                        result.add(orgUserDto);
                    }else{
                        log.debug(StringUtil.changeForLog("This user id is the excpetUserId -->:"+excpetUserId));
                    }
                }else{
                    log.debug(StringUtil.changeForLog("the do removeUnavailableUser is not Available-->:"+orgUserDto.getUserId()));
                }
            }
        }else{
            log.debug(StringUtil.changeForLog("the do removeUnavailableUser orgUserDtos is null"));
        }
        log.debug(StringUtil.changeForLog("the do removeUnavailableUser end ...."));
        return result;
    }
}
