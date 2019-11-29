package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemParameterConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.utils.TaskUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloudfeign.FeignException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
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


    @Override
    public List<TaskDto> createTasks(List<TaskDto> taskDtos) {
        return RestApiUtil.save( RestApiUrlConsts.IAIS_TASK,taskDtos,List.class);
    }

    @Override
    public TaskDto updateTask(TaskDto taskDto) {
        return RestApiUtil.update(RestApiUrlConsts.IAIS_TASK,taskDto,TaskDto.class);
    }

    @Override
    public List<HcsaSvcStageWorkingGroupDto> getTaskConfig(List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos) {
        return RestApiUtil.postGetList(RestApiUrlConsts.GET_HCSA_WORK_GROUP,hcsaSvcStageWorkingGroupDtos,HcsaSvcStageWorkingGroupDto.class);
    }

    @Override
    public TaskDto getTaskById(String taskId) {
        return RestApiUtil.getByPathParam(RestApiUrlConsts.TASK_TASKID,taskId,TaskDto.class);
    }


    @Override
    public List<OrgUserDto> getUsersByWorkGroupId(String workGroupId, String status) {
        Map<String,Object> params = new HashMap<>();
        params.put("workGroupId",workGroupId);
        params.put("status",status);
        return RestApiUtil.getListByReqParam(RestApiUrlConsts.USER_WORKGROUPID_STATUS,params,OrgUserDto.class);
    }

    @Override
    public List<TaskDto> getTaskDtoScoresByWorkGroupId(String workGroupId) {
        Map<String,Object> params = new HashMap<>();
        params.put("workGroupId",workGroupId);
        return RestApiUtil.getListByReqParam(RestApiUrlConsts.TASKSCORES_WORKGROUPNAME,params,TaskDto.class);
    }

    @Override
    public void routingTask(ApplicationDto applicationDto, String stageId) throws FeignException {
        log.debug(StringUtil.changeForLog("the do routingTask start ...."));
        if(applicationDto == null  || StringUtil.isEmpty(stageId)){
            log.error(StringUtil.changeForLog("The applicationDto or stageId is null"));
            return;
        }
        List<ApplicationDto> applicationDtos = new ArrayList<>();
        applicationDtos.add(applicationDto);
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos,stageId);
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
            List<TaskDto> taskDtos = new ArrayList<>();
            int score =  getConfigScoreForService(hcsaSvcStageWorkingGroupDtos,applicationDto.getServiceId(),
                    stageId,applicationDto.getApplicationType());
            TaskDto taskDto = TaskUtil.getTaskDto(stageId,TaskConsts.TASK_TYPE_MAIN_FLOW,
                    applicationDto.getApplicationNo(),workGroupId,
                    taskScoreDto.getUserId(),assignDate,score,
                    IaisEGPHelper.getCurrentAuditTrailDto());
            taskDtos.add(taskDto);
            this.createTasks(taskDtos);
        }else{
            log.error(StringUtil.changeForLog("can not get the HcsaSvcStageWorkingGroupDto ..."));
        }

        log.debug(StringUtil.changeForLog("the do routingTask start ...."));
    }

    @Override
    public void routingTaskOneUserForSubmisison(List<ApplicationDto> applicationDtos,String stageId) throws FeignException {
        log.debug(StringUtil.changeForLog("the do routingTaskOneUserForSubmisison start ...."));
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
                    TaskDto taskDto = TaskUtil.getUserTaskDto(stageId,
                            applicationDto.getApplicationNo(),workGroupId,
                            taskScoreDto.getUserId(),score,
                            applicationDto.getAuditTrailDto());
                    taskDtos.add(taskDto);
                    //create history
                    String appPremisesCorrelationId = getAppPremisesCorrelationId(appPremisesCorrelationDtos,applicationDto.getId());
                    log.debug(StringUtil.changeForLog("the appPremisesCorrelationId is -->;"+appPremisesCorrelationId));
                    AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto =
                            createAppPremisesRoutingHistory(appPremisesCorrelationId,applicationDto.getStatus(),
                            stageId,null );
                    appPremisesRoutingHistoryDtos.add(appPremisesRoutingHistoryDto);
                }
                this.createTasks(taskDtos);
                this.createHistorys(appPremisesRoutingHistoryDtos);
            }else{
                log.error(StringUtil.changeForLog("can not get the HcsaSvcStageWorkingGroupDto ..."));
            }
        }else{
            log.error(StringUtil.changeForLog("The applicationDtos is null"));
        }

        log.debug(StringUtil.changeForLog("the do routingTaskOneUserForSubmisison end ...."));
    }



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
        if(result == null){
            result = taskScoreDtos.get(0);
           // result.setScore(0);
        }
        return result;
    }

    @Override
    public List<AppPremisesCorrelationDto> getAppPremisesCorrelationByAppGroupId(String appGroupId) {
        return RestApiUtil.getListByPathParam(RestApiUrlConsts.APPLICATION_APPPREMISESCORRELATIONS_APPGROPID,appGroupId,AppPremisesCorrelationDto.class);
    }
    @Override
    public List<AppPremisesRoutingHistoryDto> createHistorys(List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtoList) {
        return RestApiUtil.save(RestApiUrlConsts.APPLICATION_HISTORYS,appPremisesRoutingHistoryDtoList,List.class);
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
    private String getAppPremisesCorrelationId(List<AppPremisesCorrelationDto> appPremisesCorrelationDtos,String appId){
        String result = null;
        for (AppPremisesCorrelationDto appPremisesCorrelationDto : appPremisesCorrelationDtos){
            if(appId.equals(appPremisesCorrelationDto.getApplicationId())){
                result = appPremisesCorrelationDto.getId();
                break;
            }
        }
        return  result;
    }
    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appPremisesCorrelationId, String appStatus,
                                                                         String stageId, String internalRemarks){
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setAppPremCorreId(appPremisesCorrelationId);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return appPremisesRoutingHistoryDto;
    }

}
