package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssQueryDto;
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
     * @Descripation: According to the group Id, get the work pool
     */
    List<TaskDto> getCommPoolByGroupWordId(String workGroupId);

    /**
      * @author: shicheng
      * @Date 2019/11/22
      * @Param: taskDtoList
      * @return: List<InspectionTaskPoolListDto>
      * @Descripation: Display Common Pools
      */
    List<InspectionTaskPoolListDto> getPoolListByTaskDto(List<TaskDto> taskDtoList);

    /**
      * @author: shicheng
      * @Date 2019/11/23
      * @Param: applicationNo
      * @return: InspecTaskCreAndAssQueryDto
      * @Descripation: Gets a single Common Pool of information for allocation
      */
    InspecTaskCreAndAssQueryDto getInspecTaskCreAndAssQueryDto(String applicationNo);
}
