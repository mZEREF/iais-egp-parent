package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;

import java.util.List;

/**
 * @author Shicheng
 * @date 2019/11/22 10:19
 **/
public interface InspectionAssignTaskService {
    /**
     * @author: shicheng
     * @Date 2019/11/21
     * @Param: name
     * @return: List<TaskDto>
     * @Descripation: According to the group name, get the work pool
     */
    List<TaskDto> getCommPoolByGroupWordName(String name);

    /**
      * @author: shicheng
      * @Date 2019/11/22
      * @Param: taskDtoList
      * @return: List<InspectionTaskPoolListDto>
      * @Descripation: Display Common Pools
      */
    List<InspectionTaskPoolListDto> getPoolListByTaskDto(List<TaskDto> taskDtoList);
}
