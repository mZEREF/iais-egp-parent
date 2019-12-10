package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppSupDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.utils.TaskUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.ApplicationGroupService;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.BroadcastService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloudfeign.FeignException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * HcsaApplicationDelegator
 *
 * @author suocheng
 * @date 10/17/2019
 */
@Delegator("hcsaApplicationDelegator")
@Slf4j
public class HcsaApplicationDelegator {
    @Autowired
    private TaskService taskService;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private ApplicationGroupService applicationGroupService;

    @Autowired
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;

    @Autowired
    private BroadcastService broadcastService;

    public void routingTask(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the do routingTask start ...."));
        AuditTrailHelper.auditFunction("hcsa-licence", "hcsa licence");
        List<ApplicationDto> applicationDtos = new ArrayList();
        ApplicationDto applicationDto0 = new ApplicationDto();
        applicationDto0.setApplicationNo("test applicaitonNo");
        applicationDto0.setServiceId("35F99D15-820B-EA11-BE7D-000C29F371DC");
        applicationDto0.setApplicationType(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION);
        applicationDtos.add(applicationDto0);
       // taskService.routingAdminScranTask(applicationDtos);
        taskService.routingTask(applicationDto0,HcsaConsts.ROUTING_STAGE_PSO);
        log.debug(StringUtil.changeForLog("the do routingTask end ...."));
    }

    /**
     * StartStep: doStart
     *
     * @param bpc
     * @throws
     */
    public void doStart(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do cleanSession start ...."));
        AuditTrailHelper.auditFunction("hcsa-licence", "hcsa licence");
        ParamUtil.setSessionAttr(bpc.request,"taskDto",null);
        ParamUtil.setSessionAttr(bpc.request,"applicationViewDto",null);
        log.debug(StringUtil.changeForLog("the do cleanSession end ...."));
    }

    /**
     * StartStep: prepareData
     *
     * @param bpc
     * @throws
     */
    public void prepareData(BaseProcessClass bpc) throws Exception{
        log.debug(StringUtil.changeForLog("the do prepareData start ..."));
        //get the task
       String  taskId = ParamUtil.getString(bpc.request,"taskId");
        //String taskId="12848A70-820B-EA11-BE7D-000C29F371DC";
        TaskDto taskDto = taskService.getTaskById(taskId);
//        String loginID=(String) ParamUtil.getSessionAttr(bpc.request,"loginID");
//        if(!(loginID.equals(taskDto.getUserId()))){
//            throw new Exception();
//        }
//        if(TaskConsts.TASK_STATUS_COMPLETED.equals(taskDto.getTaskStatus())){
//            throw new Exception();
//        }

        String appNo = taskDto.getRefNo();
//        get routing stage dropdown send to page.
        ApplicationViewDto applicationViewDto = applicationViewService.searchByAppNo(appNo);
        List<HcsaSvcDocConfigDto> docTitleList=applicationViewService.getTitleById(applicationViewDto.getTitleIdList());
        List<OrgUserDto> userNameList=applicationViewService.getUserNameById(applicationViewDto.getUserIdList());
        List<AppSupDocDto> appSupDocDtos=applicationViewDto.getAppSupDocDtoList();
        for (int i = 0; i <appSupDocDtos.size(); i++) {
            for (int j = 0; j <docTitleList.size() ; j++) {
                if ((appSupDocDtos.get(i).getFile()).equals(docTitleList.get(j).getId())){
                    appSupDocDtos.get(i).setFile(docTitleList.get(j).getDocTitle());
                }
            }
            for (int j = 0; j <userNameList.size() ; j++) {
                if ((appSupDocDtos.get(i).getSubmittedBy()).equals(userNameList.get(j).getId())){
                    appSupDocDtos.get(i).setSubmittedBy(userNameList.get(j).getUserName());
                }
            }
        }
        String applicationType=MasterCodeUtil.getCodeDesc(applicationViewDto.getApplicationType());
        applicationViewDto.setApplicationType(applicationType);
        String serviceType = MasterCodeUtil.getCodeDesc(applicationViewDto.getApplicationDto().getServiceId());
        applicationViewDto.setServiceType(serviceType);
        String status = MasterCodeUtil.getCodeDesc(applicationViewDto.getApplicationDto().getStatus());
        applicationViewDto.setCurrentStatus(status);
        List<HcsaSvcRoutingStageDto> hcsaSvcRoutingStageDtoList=applicationViewService.getStage(applicationViewDto.getApplicationDto().getServiceId(),taskDto.getTaskKey());
        Map<String,String> routingStage=new HashMap<>();

        for (HcsaSvcRoutingStageDto hcsaSvcRoutingStage:hcsaSvcRoutingStageDtoList
             ) {
            routingStage.put(hcsaSvcRoutingStage.getStageCode(),hcsaSvcRoutingStage.getStageName());
        }
           if(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03.equals(applicationViewDto.getApplicationDto().getStatus())){
               routingStage.put(ApplicationConsts.PROCESSING_DECISION_AO3_BROADCAST_QUERY,"Broadcast Query For Internal");
           }
//        if("AO3".equals(applicationViewDto.getCurrentStatus())){
//            routingStage.put("APST000","RollBack");
//            routingStage.put("APST002","Pending Approval");
//            routingStage.put("APST006","Reject");
//            routingStage.put("APST0013","Internal Enquiry");
//            routingStage.put("APST0014","Route To DMS");
//        }else if("AO2".equals(applicationViewDto.getCurrentStatus())
//                ||"AO1".equals(applicationViewDto.getCurrentStatus())){
//            routingStage.put("APST006","Reject");
//            routingStage.put("APST000","RollBack");
//            routingStage.put("APST0015","Support");
//        }else if("PSO".equals(applicationViewDto.getCurrentStatus())){
//            routingStage.put("APST0016","Verified");
//            routingStage.put("APST0017","Request For Information");
//        }else if("ASO".equals(applicationViewDto.getCurrentStatus())) {
//            routingStage.put("APST0016", "Verified");
//            routingStage.put( "APST0017","Request For Information");
//            routingStage.put("APST0018", "Licence Start Date");
//        }
        applicationViewDto.setRoutingStage(routingStage);
        ParamUtil.setSessionAttr(bpc.request,"applicationViewDto", applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request,"taskDto", taskDto);
        log.debug(StringUtil.changeForLog("the do prepareData end ...."));
    }

    /**
     * StartStep: chooseStage
     *
     * @param bpc
     * @throws
     */
    public void chooseStage(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do chooseStage start ...."));
        String nextStage = ParamUtil.getString(bpc.request,"nextStage");
        log.debug(StringUtil.changeForLog("the nextStage is -->:"+nextStage));
        ParamUtil.setRequestAttr(bpc.request, "crud_action_type", nextStage);
        log.debug(StringUtil.changeForLog("the do chooseStage end ...."));
    }

    /**
     * StartStep: rontingTaskToPSO
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToPSO(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToPSO start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_PSO,ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING);
        log.debug(StringUtil.changeForLog("the do rontingTaskToPSO end ...."));
    }


    /**
     * StartStep: rontingTaskToINS
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToINS(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToINS start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_INS,ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION);
        log.debug(StringUtil.changeForLog("the do rontingTaskToINS end ...."));
    }


    /**
     * StartStep: rontingTaskToASO
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToASO(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToASO start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_ASO,ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING);
        log.debug(StringUtil.changeForLog("the do rontingTaskToASO end ...."));
    }

    /**
     * StartStep: rontingTaskToAO1
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToAO1(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO1 start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_AO1,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01);
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO1 end ...."));
    }



    /**
     * StartStep: rontingTaskToAO2
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToAO2(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO2 start ...."));
        routingTask(bpc,HcsaConsts.ROUTING_STAGE_AO2,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02);
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO2 end ...."));
    }


    /**
     * StartStep: rontingTaskToAO3
     *
     * @param bpc
     * @throws
     */
    public void rontingTaskToAO3(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO3 start ...."));
        routingTask(bpc,null,ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03);
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        List<ApplicationDto> applicationDtoList = applicationService.getApplicaitonsByAppGroupId(applicationDto.getAppGrpId());
        boolean isAllSubmit = applicationService.isOtherApplicaitonSubmit(applicationDtoList,applicationDto.getId(),
                ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03);
        if(isAllSubmit){
            // send the task to Ao3
           taskService.routingTaskOneUserForSubmisison(applicationDtoList,HcsaConsts.ROUTING_STAGE_AO3);
        }
        log.debug(StringUtil.changeForLog("the do rontingTaskToAO3 end ...."));
    }


    /**
     * StartStep: approve
     *
     * @param bpc
     * @throws
     */
    public void approve(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the do approve start ...."));
        routingTask(bpc,null,ApplicationConsts.APPLICATION_STATUS_APPROVED);
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        List<ApplicationDto> applicationDtoList = applicationService.getApplicaitonsByAppGroupId(applicationDto.getAppGrpId());
        boolean isAllSubmit = applicationService.isOtherApplicaitonSubmit(applicationDtoList,applicationDto.getId(),
                ApplicationConsts.APPLICATION_STATUS_APPROVED);
        if(isAllSubmit){
            //update application Group status
            ApplicationGroupDto applicationGroupDto = applicationGroupService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
            applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_APPROVED);
            applicationGroupDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            applicationGroupService.updateApplicationGroup(applicationGroupDto);
        }
        log.debug(StringUtil.changeForLog("the do approve end ...."));
    }

    /**
     * StartStep: routeToDMS
     *
     * @param bpc
     * @throws
     */
    public void routeToDMS(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do routeToDMS start ...."));

        log.debug(StringUtil.changeForLog("the do routeToDMS end ...."));
    }

    /**
     * StartStep: routeBack
     *
     * @param bpc
     * @throws
     */
    public void routeBack(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do routeBack start ...."));

        log.debug(StringUtil.changeForLog("the do routeBack end ...."));
    }

    /**
     * StartStep: internalEnquiry
     *
     * @param bpc
     * @throws
     */
    public void internalEnquiry(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do internalEnquiry start ...."));

        log.debug(StringUtil.changeForLog("the do internalEnquiry end ...."));
    }


    /**
     * StartStep: broadcast
     *
     * @param bpc
     * @throws
     */
    public void broadcast(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the do broadcast start ...."));
        //get the user for this applicationNo
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
          List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = appPremisesRoutingHistoryService.
                  getAppPremisesRoutingHistoryDtosByAppId(applicationDto.getId());
        List<String> userIds = getUserIds(appPremisesRoutingHistoryDtos);
        if(userIds != null && userIds.size() > 0){
            BroadcastOrganizationDto broadcastOrganizationDto = new BroadcastOrganizationDto();
            BroadcastApplicationDto broadcastApplicationDto = new BroadcastApplicationDto();
            //create workgroup
            WorkingGroupDto workingGroupDto = new WorkingGroupDto();
            workingGroupDto.setGroupName(applicationDto.getApplicationNo());
            workingGroupDto.setGroupDomain(AppConsts.DOMAIN_TEMPORARY);
            broadcastOrganizationDto.setWorkingGroupDto(workingGroupDto);
            //add this user to this workgroup
            List<UserGroupCorrelationDto> userGroupCorrelationDtoList = new ArrayList<>();
            for(String id : userIds) {
                UserGroupCorrelationDto userGroupCorrelationDto = new UserGroupCorrelationDto();
                userGroupCorrelationDto.setUserId(id);
                userGroupCorrelationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                userGroupCorrelationDtoList.add(userGroupCorrelationDto);
            }
            broadcastOrganizationDto.setUserGroupCorrelationDtoList(userGroupCorrelationDtoList);

            //complated this task and create the history
            TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
            taskDto =  completedTask(taskDto);
            broadcastOrganizationDto.setComplateTask(taskDto);
            String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
            String processDecision = ParamUtil.getString(bpc.request,"nextStage");
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = getAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),
                    applicationDto.getStatus(),taskDto.getTaskKey(), taskDto.getWkGrpId(),internalRemarks,processDecision);
            broadcastApplicationDto.setComplateTaskHistory(appPremisesRoutingHistoryDto);
            //update application status
            applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_BROADCAST);
            broadcastApplicationDto.setApplicationDto(applicationDto);
            //create the new task and create the history
            TaskDto taskDtoNew = TaskUtil.getTaskDto(taskDto.getTaskKey(),TaskConsts.TASK_TYPE_MAIN_FLOW, applicationDto.getApplicationNo(),null,
                    null,null,0, IaisEGPHelper.getCurrentAuditTrailDto());
            broadcastOrganizationDto.setCreateTask(taskDtoNew);
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew =getAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),applicationDto.getStatus(),
                    taskDto.getTaskKey(), taskDto.getWkGrpId(),null,null);
            broadcastApplicationDto.setNewTaskHistory(appPremisesRoutingHistoryDtoNew);
            //save the broadcast
            broadcastOrganizationDto = broadcastService.svaeBroadcastOrganization(broadcastOrganizationDto);
            broadcastApplicationDto  = broadcastService.svaeBroadcastApplicationDto(broadcastApplicationDto);

        }

        log.debug(StringUtil.changeForLog("the do broadcast end ...."));
    }



    private List<String> getUserIds(List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos){
        Set<String> set = new HashSet<>();
        if(appPremisesRoutingHistoryDtos == null || appPremisesRoutingHistoryDtos.size() ==0){
           return  null;
        }
        for(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto :appPremisesRoutingHistoryDtos ){
            set.add(appPremisesRoutingHistoryDto.getId());
        }
        return  new ArrayList(set);


    }

    //***************************************
    //private methods
    //*************************************

    private void routingTask(BaseProcessClass bpc,String stageId,String appStatus ) throws FeignException {
        //completedTask
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
        taskDto =  completedTask(taskDto);
        taskDto = taskService.updateTask(taskDto);
        //add history for this stage complate
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request,"applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
        String processDecision = ParamUtil.getString(bpc.request,"nextStage");
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto =getAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),
                applicationDto.getStatus(),taskDto.getTaskKey(), taskDto.getWkGrpId(),internalRemarks,processDecision);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryService.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto);
        //updateApplicaiton
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        updateApplicaiton(applicationDto,appStatus);
        applicationViewDto.setApplicationDto(applicationDto);
        // send the task
        if(!StringUtil.isEmpty(stageId)){
            taskDto = taskService.routingTask(applicationDto,stageId);
            //add history for next stage start
            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDtoNew =getAppPremisesRoutingHistory(applicationViewDto.getAppPremisesCorrelationId(),applicationDto.getStatus(),stageId,
                    taskDto.getWkGrpId(),null,null);
            appPremisesRoutingHistoryDtoNew = appPremisesRoutingHistoryService.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDtoNew);
        }
    }
    private AppPremisesRoutingHistoryDto getAppPremisesRoutingHistory(String appPremisesCorrelationId, String appStatus,
                                                                         String stageId,String wrkGrpId, String internalRemarks,String processDecision){
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setAppPremCorreId(appPremisesCorrelationId);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setProcessDecision(processDecision);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setWrkGrpId(wrkGrpId);
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return appPremisesRoutingHistoryDto;
    }
    private int remainDays(TaskDto taskDto){
       int result = 0;
       //todo: wait count kpi
       String  resultStr = DurationFormatUtils.formatPeriod(taskDto.getDateAssigned().getTime(),taskDto.getSlaDateCompleted().getTime(), "d");
      log.debug(StringUtil.changeForLog("The resultStr is -->:")+resultStr);
      return  result;
    }


    private TaskDto completedTask(TaskDto taskDto){
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setSlaRemainInDays(remainDays(taskDto));
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return taskDto;
    }
    private ApplicationDto updateApplicaiton(ApplicationDto applicationDto,String appStatus){
        applicationDto.setStatus(appStatus);
        return applicationViewService.updateApplicaiton(applicationDto);
    }





}
