package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloudfeign.FeignException;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * TaskService
 *
 * @author suocheng
 * @date 11/20/2019
 */
public interface TaskService {
    //create Tasks
    List<TaskDto> createTasks(List<TaskDto> taskDtos);
    TaskDto updateTask(TaskDto taskDto);
    //get TaskConfig
    List<HcsaSvcStageWorkingGroupDto> getTaskConfig(List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos);
    //get Task
    TaskDto getTaskById(String taskId);
    public TaskDto getRoutingTask(ApplicationDto applicationDto, String statgId,String roleId,String correlationId) throws FeignException;
    public TaskHistoryDto getRoutingTaskOneUserForSubmisison(List<ApplicationDto> applicationDtos, String stageId,String roleId, AuditTrailDto auditTrailDto) throws FeignException;
    //rounting Task
    // void routingTaskOneUserForSubmisison(List<ApplicationDto> applicationDtos, String stage,String roleId, AuditTrailDto auditTrailDto) throws FeignException;
     //TaskDto routingTask(ApplicationDto applicationDto, String statgId,String roleId) throws FeignException;
     //get users
    List<OrgUserDto> getUsersByWorkGroupId(String workGroupId, String status);
    //get Task Score
    List<TaskDto> getTaskDtoScoresByWorkGroupId(String workGroupId);
    TaskDto getLowestTaskScore(List<TaskDto> taskScoreDtos, List<OrgUserDto> users);

    //other method help task.
    public List<AppPremisesCorrelationDto> getAppPremisesCorrelationByAppGroupId(String appGroupId);
    public List<AppPremisesRoutingHistoryDto> createHistorys(List<AppPremisesRoutingHistoryDto>  appPremisesRoutingHistoryDtoList);
    /**
     * @author: shicheng
     * @Date 2019/11/21
     * @Param: name
     * @return: List<TaskDto>
     * @Descripation: According to the group Id, get the work pool
     */
    List<TaskDto> getCommPoolByGroupWordId(String workGroupId);
    
    /**
      * @author: shicheng
      * @Date 2019/12/9
      * @Param: 
      * @return: 
      * @Descripation: 
      */
    int remainDays(TaskDto taskDto);

    public Map<String, Object> getEmailNotifyList();

    List<String> getDistincTaskRefNumByCurrentGroup(String wrkGroupId);

    /**
      * @author: shicheng
      * @Date 2020/1/6
      * @Param:
      * @return:
      * @Descripation: get  lowest score User Id By workGroupId
      */
    TaskDto getUserIdForWorkGroup(String workGroupId) throws FeignException;


    /**
     *@Author :weilu on 2020/2/8 17:03
     *@param :
     *@return :
     *@Description :
     */
    Set<String> getInspectiors(String corrId , String status, String roleId);
}
