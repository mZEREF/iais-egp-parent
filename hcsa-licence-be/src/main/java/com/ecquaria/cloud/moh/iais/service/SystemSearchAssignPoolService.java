package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.organization.GroupRoleFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.SuperPoolTaskQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.SystemAssignSearchQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;

import java.util.List;

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
      * @Param: curStage
      * @return: List<SelectOption>
      * @Descripation: get App Status Option
      */
    List<SelectOption> getAppStatusOption(String curStage);

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
}
