package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Shicheng
 * @date 2019/11/19 14:45
 **/
@Service
public class InspectionServiceImpl implements InspectionService {
    @Override
    public InspectionTaskPoolListDto getInspectionTaskPoolListDto(List<TaskDto> taskDtoList) {
        InspectionTaskPoolListDto inspectionTaskPoolListDto = new InspectionTaskPoolListDto();
        for (TaskDto taskDto:taskDtoList){
            InspecTaskCreAndAssQueryDto inspecTaskCreAndAssQueryDto = getInspecTaskCreAndAssQueryDtoByTask(taskDto);
        }
        return inspectionTaskPoolListDto;
    }

    @Override
    public InspecTaskCreAndAssQueryDto getInspecTaskCreAndAssQueryDtoByTask(TaskDto taskDto) {
        InspecTaskCreAndAssQueryDto inspecTaskCreAndAssQueryDto = new InspecTaskCreAndAssQueryDto();

        return inspecTaskCreAndAssQueryDto;
    }
}
