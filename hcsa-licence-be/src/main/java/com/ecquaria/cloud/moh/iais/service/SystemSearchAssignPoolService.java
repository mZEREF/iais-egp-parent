package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.SystemAssignTaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.SuperPoolTaskQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.SystemAssignSearchQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;

import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2020/8/26 14:27
 **/
public interface SystemSearchAssignPoolService {
    /**
      * @author: shicheng
      * @Date 2020/8/26
      * @Param: null
      * @return: GroupRoleFieldDto
      * @Descripation: getSystemSearchStage
      */
    GroupRoleFieldDto getSystemSearchStage();

    /**
      * @author: shicheng
      * @Date 2020/8/27
      * @Param: 
      * @return: 
      * @Descripation: get System User Task Pool By User Id
      */
    List<TaskDto> getSystemTaskPool(String userId);

    /**
      * @author: shicheng
      * @Date 2020/8/31
      * @Param: 
      * @return: 
      * @Descripation: get System Group Pool By Param
      */
    SearchResult<SystemAssignSearchQueryDto> getSystemGroupPoolByParam(SearchParam searchParam);

    /**
      * @author: shicheng
      * @Date 2020/8/31
      * @Param: GroupRoleFieldDto groupRoleFieldDto
      * @return: List<SelectOption>
      * @Descripation: get App Status Option
      */
    List<SelectOption> getAppStatusOption(GroupRoleFieldDto groupRoleFieldDto);

    /**
      * @author: shicheng
      * @Date 2020/9/1
      * @Param: searchParam
      * @return: SearchResult<SuperPoolTaskQueryDto>
      * @Descripation: get System Pool Second By Param
      */
    SearchResult<SuperPoolTaskQueryDto> getSystemPoolSecondByParam(SearchParam searchParam);

    /**
      * @author: shicheng
      * @Date 2020/9/1
      * @Param: groupRoleFieldDto
      * @return: GroupRoleFieldDto
      * @Descripation: set Group Member Name
      */
    GroupRoleFieldDto setGroupMemberName(GroupRoleFieldDto groupRoleFieldDto);

    /**
      * @author: shicheng
      * @Date 2020/9/1
      * @Param: groupRoleFieldDto, systemAssignTaskDto
      * @return: SystemAssignTaskDto
      * @Descripation: set Work Group And Officer
      */
    SystemAssignTaskDto setWorkGroupAndOfficer(GroupRoleFieldDto groupRoleFieldDto, SystemAssignTaskDto systemAssignTaskDto);

    /**
      * @author: shicheng
      * @Date 2020/9/1
      * @Param: systemAssignMap, systemAssignTaskDto, taskDto, applicationViewDto
      * @return: SystemAssignTaskDto
      * @Descripation: getDataForSystemAssignTask
      */
    SystemAssignTaskDto getDataForSystemAssignTask(Map<String, SuperPoolTaskQueryDto> systemAssignMap, SystemAssignTaskDto systemAssignTaskDto,
                                                   TaskDto taskDto, ApplicationViewDto applicationViewDto);

    /**
      * @author: shicheng
      * @Date 2020/9/2
      * @Param: systemAssignTaskDto
      * @return: SystemAssignTaskDto
      * @Descripation: get Check Group Name And User Name
      */
    SystemAssignTaskDto getCheckGroupNameAndUserName(SystemAssignTaskDto systemAssignTaskDto);

    /**
      * @author: shicheng
      * @Date 2020/9/2
      * @Param: systemAssignTaskDto
      * @return: void
      * @Descripation: systemAssignTask
      */
    void systemAssignTask(SystemAssignTaskDto systemAssignTaskDto);

    /**
      * @author: shicheng
      * @Date 2021/3/8
      * @Param: groupRoleFieldDto
      * @return: String
      * @Descripation: getSysCurStageId
      */
    String getSysCurStageId(GroupRoleFieldDto groupRoleFieldDto);

    List<String> getSystemPoolAppGrpIdByResult(SearchResult<SystemAssignSearchQueryDto> searchResult);
}
