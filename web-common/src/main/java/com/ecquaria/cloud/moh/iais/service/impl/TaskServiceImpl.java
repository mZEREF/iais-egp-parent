package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemParameterConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.utils.TaskUtil;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.TaskApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskHcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskOrganizationClient;
import com.ecquaria.cloudfeign.FeignException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
            log.error(StringUtil.changeForLog("The applicationDto or stageId is null"));
            return result;
        }
        List<ApplicationDto> applicationDtos = new ArrayList<>();
        applicationDtos.add(applicationDto);
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos,statgId);
        hcsaSvcStageWorkingGroupDtos = this.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
        if(hcsaSvcStageWorkingGroupDtos!= null && hcsaSvcStageWorkingGroupDtos.size() > 0){
            String workGroupId = hcsaSvcStageWorkingGroupDtos.get(0).getGroupId();
            TaskDto taskScoreDto =new TaskDto();
            Date assignDate = new Date();
            if(SystemParameterConstants.COMMON_POOL.equals(hcsaSvcStageWorkingGroupDtos.get(0).getSchemeType())){
                assignDate = null;
            }else{
                taskScoreDto = getUserIdForWorkGroup(workGroupId);
            }

            int score =  getConfigScoreForService(hcsaSvcStageWorkingGroupDtos,applicationDto.getServiceId(),
                    statgId,applicationDto.getApplicationType());
            result = TaskUtil.getTaskDto(statgId,TaskConsts.TASK_TYPE_MAIN_FLOW,
                    correlationId,workGroupId,
                    taskScoreDto.getUserId(),assignDate,score,TaskConsts.TASK_PROCESS_URL_MAIN_FLOW,roleId,
                    IaisEGPHelper.getCurrentAuditTrailDto());
        }else{
            log.error(StringUtil.changeForLog("can not get the HcsaSvcStageWorkingGroupDto ..."));
        }
        log.debug(StringUtil.changeForLog("the do routingTask start ...."));
        return result;
    }


    @Override
    public List<OrgUserDto> getUsersByWorkGroupId(String workGroupId, String status) {
        return taskOrganizationClient.getUsersByWorkGroupName(workGroupId,status).getEntity();
    }

    @Override
    public List<TaskDto> getTaskDtoScoresByWorkGroupId(String workGroupId) {
        return taskOrganizationClient.getTaskScores(workGroupId).getEntity();
    }


//    public TaskDto routingTask(ApplicationDto applicationDto, String stageId,String roleId) throws FeignException {
//        log.debug(StringUtil.changeForLog("the do routingTask start ...."));
//        TaskDto result = null;
//        if(applicationDto == null  || StringUtil.isEmpty(stageId)){
//            log.error(StringUtil.changeForLog("The applicationDto or stageId is null"));
//            return result;
//        }
//        List<TaskDto> taskDtos = new ArrayList<>();
//        result = this.getRoutingTask(applicationDto,stageId,roleId);
//        taskDtos.add(result);
//        this.createTasks(taskDtos);
//        log.debug(StringUtil.changeForLog("the do routingTask start ...."));
//        return result;
//    }

    @Override
    public TaskHistoryDto getRoutingTaskOneUserForSubmisison(List<ApplicationDto> applicationDtos, String stageId,String roleId, AuditTrailDto auditTrailDto) throws FeignException {
        log.debug(StringUtil.changeForLog("the do getRoutingTaskOneUserForSubmisison start ...."));
        log.info("---------------"+applicationDtos+"--------"+stageId);
        TaskHistoryDto result = new TaskHistoryDto();
        if(applicationDtos != null && applicationDtos.size() > 0){
            List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos,stageId);
            hcsaSvcStageWorkingGroupDtos = this.getTaskConfig(hcsaSvcStageWorkingGroupDtos);
            if(hcsaSvcStageWorkingGroupDtos!= null && hcsaSvcStageWorkingGroupDtos.size() > 0){
                String workGroupId = hcsaSvcStageWorkingGroupDtos.get(0).getGroupId();
                TaskDto taskScoreDto = getUserIdForWorkGroup(workGroupId);
                List<AppPremisesCorrelationDto> appPremisesCorrelationDtos =
                        this.getAppPremisesCorrelationByAppGroupId(applicationDtos.get(0).getAppGrpId());
                List<TaskDto> taskDtos = new ArrayList<>();
                List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = new ArrayList<>();
                for(ApplicationDto applicationDto : applicationDtos){
                    int score =  getConfigScoreForService(hcsaSvcStageWorkingGroupDtos,applicationDto.getServiceId(),
                            stageId,applicationDto.getApplicationType());
                    List<String> appPremisesCorrelationIds = getAppPremisesCorrelationId(appPremisesCorrelationDtos,applicationDto.getId());
                    if(!IaisCommonUtils.isEmpty(appPremisesCorrelationIds)){
                        for (String id :appPremisesCorrelationIds ){
                            TaskDto taskDto = TaskUtil.getUserTaskDto(stageId,
                                    id,workGroupId,
                                    taskScoreDto.getUserId(),score,TaskConsts.TASK_PROCESS_URL_MAIN_FLOW,roleId,
                                    auditTrailDto);
                            taskDtos.add(taskDto);
                            //create history
                            log.debug(StringUtil.changeForLog("the appPremisesCorrelationId is -->;"+id));
                            AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto =
                                    createAppPremisesRoutingHistory(id,applicationDto.getStatus(),
                                            stageId,null,roleId,auditTrailDto);
                            appPremisesRoutingHistoryDtos.add(appPremisesRoutingHistoryDto);
                        }
                    }
                }
                result.setTaskDtoList(taskDtos);
                result.setAppPremisesRoutingHistoryDtos(appPremisesRoutingHistoryDtos);
            }else{
                log.error(StringUtil.changeForLog("can not get the HcsaSvcStageWorkingGroupDto ..."));
            }
        }else{
            log.error(StringUtil.changeForLog("The applicationDtos is null"));
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
//            log.error(StringUtil.changeForLog("The taksDto is null !!!"));
//        }
//
//        log.debug(StringUtil.changeForLog("the do routingTaskOneUserForSubmisison end ...."));
//    }



    @Override
    public TaskDto getLowestTaskScore(List<TaskDto> taskScoreDtos, List<OrgUserDto> users) {
        TaskDto result = null;
        //There is not user in this workgroup return null
        if(users == null || users.size() ==0){
            return  result;
        }
        //There is not taskScoreDtos ,return the users first.
        if(taskScoreDtos == null || taskScoreDtos.size() == 0){
            result = new TaskDto();
            result.setUserId(users.get(0).getId());
            result.setScore(0);
        }else{
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
                result = taskDto;
                break;
            }
           // result.setScore(0);
        }
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
        String  resultStr = DurationFormatUtils.formatPeriod(taskDto.getDateAssigned().getTime(),taskDto.getSlaDateCompleted().getTime(), "d");
        log.debug(StringUtil.changeForLog("The resultStr is -->:")+resultStr);
        return  result;
    }

    private List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId){
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = new ArrayList();
        for(ApplicationDto applicationDto : applicationDtos){
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setStageId(stageId);
            hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
            hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
            hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
        }
        return hcsaSvcStageWorkingGroupDtos;
    }
    private TaskDto getUserIdForWorkGroup(String workGroupId) throws FeignException {
        log.debug(StringUtil.changeForLog("the do getUserIdForWorkGroup start ...."));
        TaskDto result = null;
        if(StringUtil.isEmpty(workGroupId)){
            return result;
        }
        List<OrgUserDto> orgUserDtos =getUsersByWorkGroupId(workGroupId,AppConsts.COMMON_STATUS_ACTIVE);
        List<TaskDto> taskScoreDtos = this.getTaskDtoScoresByWorkGroupId(workGroupId);
        result = this.getLowestTaskScore(taskScoreDtos,orgUserDtos);
        if(result != null && StringUtil.isEmpty(result.getWkGrpId())){
            result.setWkGrpId(workGroupId);
        }
        log.debug(StringUtil.changeForLog("the do getUserIdForWorkGroup end ...."));
        return result;
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
    private List<String> getAppPremisesCorrelationId(List<AppPremisesCorrelationDto> appPremisesCorrelationDtos,String appId){
        List<String> result = new ArrayList<>();
        for (AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtos){
            if(appId.equals(appPremisesCorrelationDto.getApplicationId())){
                result.add(appPremisesCorrelationDto.getId());
            }
        }
        return  result;
    }
    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appPremisesCorrelationId, String appStatus,
                                                                         String stageId, String internalRemarks,String roleId,
                                                                         AuditTrailDto auditTrailDto){
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setAppPremCorreId(appPremisesCorrelationId);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(auditTrailDto.getMohUserGuid());
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setAuditTrailDto(auditTrailDto);
        return appPremisesRoutingHistoryDto;
    }

    @Override
    public Map<String, Object> getEmailNotifyList(){
        return taskOrganizationClient.getEmailNotifyList().getEntity();
    }

}
