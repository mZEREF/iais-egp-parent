package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;

import java.util.List;

/**
 * @author Shicheng
 * @date 2019/11/14 18:04
 **/

public interface InspectionService {
    /**
     * @author: shicheng
     * @Date 2019/11/21
     * @Param: taskDtoList
     * @return: InspectionTaskPoolListDto
     * @Descripation: Shows unallocated Common pools
     */
    InspectionTaskPoolListDto getInspectionTaskPoolListDto(List<TaskDto> taskDtoList);

    /**
     * @author: shicheng
     * @Date 2019/11/21
     * @Param: taskDto
     * @return: InspecTaskCreAndAssQueryDto
     * @Descripation: Gets a single Common pool
     */
    InspecTaskCreAndAssQueryDto getInspecTaskCreAndAssQueryDtoByTask(TaskDto taskDto);
}
