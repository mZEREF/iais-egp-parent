package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionSubPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.intranetDashboard.HcsaTaskAssignDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.SuperPoolTaskQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;

import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2019/11/14 18:04
 **/

public interface InspectionService {
    /**
     * @author: shicheng
     * @Date 2019/12/2
     * @Param: null
     * @return: List<SelectOption>
     * @Descripation: get App Type
     */
    List<SelectOption> getAppTypeOption();

    /**
     * @author: shicheng
     * @Date 2019/12/2
     * @Param: null
     * @return: List<SelectOption>
     * @Descripation: get App Status
     * @param loginContext
     */
    List<SelectOption> getAppStatusOption(LoginContext loginContext, String poolType);

    /**
     * @author: shicheng
     * @Date 2019/12/2
     * @Param: workGroupId
     * @return: List<TaskDto>
     * @Descripation: According to the group Id, get the work pool
     */
    List<TaskDto> getSupervisorPoolByGroupWordId(String workGroupId);

    /**
     * @author: shicheng
     * @Date 2019/12/2
     * @Param: inspectionTaskPoolListDtoList
     * @return: String[]
     * @Descripation: get Application No By InspectionTaskPoolListDto
     */
    List<String> getApplicationNoListByPool(List<TaskDto> commPools);

    /**
     * @author: shicheng
     * @Date 2019/12/3
     * @Param: searchParam
     * @return: SearchResult<InspectionSubPoolQueryDto>
     * @Descripation: get Sup Pool By Param
     */
    SearchResult<InspectionSubPoolQueryDto> getSupPoolByParam(SearchParam searchParam);

    /**
     * @author: shicheng
     * @Date 2019/12/4
     * @Param: nameValue, inspectionTaskPoolListDto
     * @return: List<SelectOption>
     * @Descripation: get check Inspector
     */
    List<SelectOption> getCheckInspector(String[] nameValue, InspectionTaskPoolListDto inspectionTaskPoolListDto);

    /**
     * @author: shicheng
     * @Date 2019/12/4
     * @Param: inspectionTaskPoolListDto, commPools, internalRemarks, applicationDto, taskDto, applicationViewDto
     * @return: void
     * @Descripation: Assign Task For Inspectors
     */
    String assignTaskForInspectors(InspectionTaskPoolListDto inspectionTaskPoolListDto, List<TaskDto> commPools,
                                 String internalRemarks, ApplicationDto applicationDto, TaskDto taskDto, ApplicationViewDto applicationViewDto);

    /**
     * @author: shicheng
     * @Date 2019/11/22
     * @Param: serviceId
     * @return: HcsaServiceDto
     * @Descripation: get HcsaServiceDto By Service Id
     */
    HcsaServiceDto getHcsaServiceDtoByServiceId(String serviceId);

    /**
     * @author: shicheng
     * @Date 2019/12/11
     * @Param: inspectionTaskPoolListDto, internalRemarks, commPools
     * @return: void
     * @Descripation: routing Task By Pool
     */
    String routingTaskByPool(InspectionTaskPoolListDto inspectionTaskPoolListDto, List<TaskDto> commPools, String internalRemarks);

    /**
     * @author: shicheng
     * @Date 2019/12/11
     * @Param: loginContext
     * @return: GroupRoleFieldDto
     * @Descripation: get Inspector Option By Login
     */
    GroupRoleFieldDto getInspectorOptionByLogin(LoginContext loginContext, List<String> workGroupIds, GroupRoleFieldDto groupRoleFieldDto);

    /**
     * @author: shicheng
     * @Date 2019/12/11
     * @Param: loginContext
     * @return: List<String>
     * @Descripation: get Work Group Ids By Login
     */
    List<String> getWorkGroupIdsByLogin(LoginContext loginContext);

    /**
     * @author: shicheng
     * @Date 2019/12/12
     * @Param: inspectionTaskPoolListDto, loginContext
     * @return: InspectionTaskPoolListDto
     * @Descripation: input Inspector Option
     */
    InspectionTaskPoolListDto inputInspectorOption(InspectionTaskPoolListDto inspectionTaskPoolListDto, LoginContext loginContext);

    /**
     * @author: weilu
     * @Date 2019/12/17
     * @Param: workGroupId
     * @return: List<TaskDto>
     * @Descripation: According to the group Id, get the reassign pool
     */
    List<TaskDto> getReassignPoolByGroupWordId(String workGroupId);

    /**
     * @author: weilu
     * @Date 2019/12/17
     * @Param: inspectionTaskPoolListDto taskId
     * @return: List<TaskDto>
     * @Descripation: get reassign task option
     */
    InspectionTaskPoolListDto reassignInspectorOption(InspectionTaskPoolListDto inspectionTaskPoolListDto, LoginContext loginContext,String taskId);

    /**
      * @author: shicheng
      * @Date 2020/3/23
      * @Param: userId
      * @return: String
      * @Descripation: get Members's task ref_No From Work Group By UserId
      */
    String getMemberValueByWorkGroupUserId(String userId);

    /**
      * @author: shicheng
      * @Date 2020/3/23
      * @Param: searchResult, loginContext, superPool
      * @return: SearchResult<InspectionSubPoolQueryDto>
      * @Descripation: getGroupLeadName
      */
    SearchResult<InspectionSubPoolQueryDto> getGroupLeadName(SearchResult<InspectionSubPoolQueryDto> searchResult, LoginContext loginContext);

    /**
      * @author: shicheng
      * @Date 2020/3/24
      * @Param: searchParam
      * @return: SearchResult<SuperPoolTaskQueryDto>
      * @Descripation: getSupPoolSecondByParam
      */
    SearchResult<SuperPoolTaskQueryDto> getSupPoolSecondByParam(SearchParam searchParam);

    /**
      * @author: shicheng
      * @Date 2020/3/24
      * @Param: 
      * @return: 
      * @Descripation: 
      */
    SearchResult<SuperPoolTaskQueryDto> getSecondSearchOtherData(SearchResult<SuperPoolTaskQueryDto> searchResult, HcsaTaskAssignDto hcsaTaskAssignDto);

    /**
      * @author: shicheng
      * @Date 2020/3/25
      * @Param: 
      * @return: 
      * @Descripation: 
      */
    InspectionTaskPoolListDto getDataForAssignTask(Map<String, SuperPoolTaskQueryDto> assignMap, InspectionTaskPoolListDto inspectionTaskPoolListDto, String taskId);

    /**
      * @author: shicheng
      * @Date 2020/9/8
      * @Param: workGrpId
      * @return: List<String>
      * @Descripation: getUserIdByWorkGrpId
      */
    List<String> getUserIdByWorkGrpId(String workGrpId);


    List<String> getWorkIdsByLogin(LoginContext loginContext);

    /**
      * @author: shicheng
      * @Date 2021/3/4
      * @Param: List<TaskDto> superPool
      * @return: List<TaskDto>
      * @Descripation: filter Common Pool Task
      */
    List<TaskDto> filterCommonPoolTask(List<TaskDto> superPool);

    /**
      * @author: shicheng
      * @Date 2021/11/22
      * @Param: appGroupIds
      * @return: HcsaTaskAssignDto
      * @Descripation: getHcsaTaskAssignDtoByAppGrp
      */
    HcsaTaskAssignDto getHcsaTaskAssignDtoByAppGrp(List<String> appGroupIds);

    List<String> getSuperPoolAppGrpIdByResult(SearchResult<InspectionSubPoolQueryDto> searchResult);
}
