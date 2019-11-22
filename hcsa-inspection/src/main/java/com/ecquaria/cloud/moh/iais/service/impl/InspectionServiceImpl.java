package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2019/11/19 14:45
 **/
@Service
public class InspectionServiceImpl implements InspectionService {

    @Override
    public List<TaskDto> getCommPoolByGroupWordName(String name) {
        Map<String,Object> map = new HashMap<>();
        map.put("workGroupName", name);
        return RestApiUtil.getListByReqParam("hcsa-task:8886/iais-task/commpool/{workGroupName}",map,TaskDto.class);
    }

    @Override
    public InspectionTaskPoolListDto getInspectionTaskPoolListDto(List<TaskDto> taskDtoList) {
        InspectionTaskPoolListDto inspectionTaskPoolListDto = new InspectionTaskPoolListDto();
        inspectionTaskPoolListDto.setInspecTaskCreAndAssQueryDtoList(new ArrayList());
        for (TaskDto taskDto:taskDtoList){
            InspecTaskCreAndAssQueryDto inspecTaskCreAndAssQueryDto = getInspecTaskCreAndAssQueryDtoByTask(taskDto);
            inspectionTaskPoolListDto.getInspecTaskCreAndAssQueryDtoList().add(inspecTaskCreAndAssQueryDto);
        }
        return inspectionTaskPoolListDto;
    }

    @Override
    public InspecTaskCreAndAssQueryDto getInspecTaskCreAndAssQueryDtoByTask(TaskDto taskDto) {
        InspecTaskCreAndAssQueryDto inspecTaskCreAndAssQueryDto = new InspecTaskCreAndAssQueryDto();
        
        return inspecTaskCreAndAssQueryDto;
    }
}
