package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2019/11/22 10:19
 **/
@Service
public class InspectionAssignTaskServiceImpl implements InspectionAssignTaskService {
    @Override
    public List<TaskDto> getCommPoolByGroupWordName(String name) {
        Map<String,Object> map = new HashMap<>();
        map.put("workGroupName", name);
        return RestApiUtil.getListByReqParam("hcsa-task:8886/iais-task/commpool/{workGroupName}",map,TaskDto.class);
    }

    @Override
    public List<InspectionTaskPoolListDto> getPoolListByTaskDto(List<TaskDto> taskDtoList) {
        List<InspectionTaskPoolListDto> inspectionTaskPoolListDtoList = new ArrayList<>();
        if(taskDtoList != null) {
            for(TaskDto td:taskDtoList){
                InspectionTaskPoolListDto inspectionTaskPoolListDto = getInspectionTaskPoolListDtoByTaskDto(td);
                inspectionTaskPoolListDtoList.add(inspectionTaskPoolListDto);
            }
        }
        return inspectionTaskPoolListDtoList;
    }

    /**
      * @author: shicheng
      * @Date 2019/11/22
      * @Param: taskDto
      * @return: InspectionTaskPoolListDto
      * @Descripation: Gets a single Common Pool
      */
    private InspectionTaskPoolListDto getInspectionTaskPoolListDtoByTaskDto(TaskDto taskDto){
        InspectionTaskPoolListDto inspectionTaskPoolListDto = new InspectionTaskPoolListDto();
        inspectionTaskPoolListDto.setApplicationNo(taskDto.getRefNo());
        inspectionTaskPoolListDto.setWorkGroupName(taskDto.getGroupShortName());
        inspectionTaskPoolListDto.setInspectionLead(taskDto.getInboxUserId());
        Map<String,Object> map2 = new HashMap<>();
        map2.put("applicationNo", taskDto.getRefNo());
        ApplicationDto applicationDto = RestApiUtil.getByReqParam("iais-application:8883/iais-inspection/one-of-inspection/{applicationNo}",map2,ApplicationDto.class); ;
        Map<String,Object> map = new HashMap<>();
        map.put("serviceId", applicationDto.getServiceId());
        HcsaServiceDto hcsaServiceDto =  RestApiUtil.getByReqParam("iais-hcsa-service:8878/iais-hcsa-service/one-of-hcsa-service/{serviceId}",map,HcsaServiceDto.class);
        inspectionTaskPoolListDto.setServiceName(hcsaServiceDto.getSvcName());
        return inspectionTaskPoolListDto;
    }
}
