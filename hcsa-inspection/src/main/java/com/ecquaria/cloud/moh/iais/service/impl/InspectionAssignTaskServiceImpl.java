package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssQueryDto;
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
    public List<TaskDto> getCommPoolByGroupWordId(String workGroupId) {
        Map<String,Object> map = new HashMap<>();
        map.put("workGroupId", workGroupId);
        return RestApiUtil.getListByReqParam("hcsa-task:8880/iais-task/commpool/{workGroupId}",map,TaskDto.class);
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

    @Override
    public InspecTaskCreAndAssQueryDto getInspecTaskCreAndAssQueryDto(String applicationNo) {
        ApplicationDto applicationDto = getApplicationDtoByAppNo(applicationNo);
        AppGrpPremisesDto appGrpPremisesDto = getAppGrpPremisesDtoByAppGroId(applicationDto.getId());
        HcsaServiceDto hcsaServiceDto = getHcsaServiceDtoByServiceId(applicationDto.getServiceId());
        ApplicationGroupDto applicationGroupDto = getApplicationGroupDtoByAppGroId(applicationDto.getAppGrpId());

        InspecTaskCreAndAssQueryDto inspecTaskCreAndAssQueryDto = new InspecTaskCreAndAssQueryDto();
        inspecTaskCreAndAssQueryDto.setApplicationNo(applicationNo);
        inspecTaskCreAndAssQueryDto.setApplicationType(applicationDto.getApplicationType());
        inspecTaskCreAndAssQueryDto.setApplicationStatus(applicationDto.getStatus());
        inspecTaskCreAndAssQueryDto.setHciName(appGrpPremisesDto.getHciName() + " / " + appGrpPremisesDto.getAddress());
        inspecTaskCreAndAssQueryDto.setHciCode(appGrpPremisesDto.getHciCode());
        inspecTaskCreAndAssQueryDto.setServiceName(hcsaServiceDto.getSvcName());
        inspecTaskCreAndAssQueryDto.setInspectionTypeName(applicationGroupDto.getIsPreInspection() == 0? "Post":"Pre");
        inspecTaskCreAndAssQueryDto.setInspectionType(applicationGroupDto.getIsPreInspection());
        inspecTaskCreAndAssQueryDto.setSubmitDt(applicationGroupDto.getSubmitDt());
        //get inspector lead
        //get inspector checkbox list
        return inspecTaskCreAndAssQueryDto;
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
        inspectionTaskPoolListDto.setInspectionLead(taskDto.getUserId());
        ApplicationDto applicationDto = getApplicationDtoByAppNo(taskDto.getRefNo());
        HcsaServiceDto hcsaServiceDto = getHcsaServiceDtoByServiceId(applicationDto.getServiceId());
        inspectionTaskPoolListDto.setServiceName(hcsaServiceDto.getSvcName());
        return inspectionTaskPoolListDto;
    }

    /**
      * @author: shicheng
      * @Date 2019/11/22
      * @Param: appNo
      * @return: ApplicationDto
      * @Descripation: get ApplicationDto By Application No.
      */
    public ApplicationDto getApplicationDtoByAppNo(String appNo){
        Map<String,Object> map2 = new HashMap<>();
        map2.put("applicationNo", appNo);
        ApplicationDto applicationDto = RestApiUtil.getByReqParam("iais-application:8883/iais-inspection/one-of-inspection/{applicationNo}",map2,ApplicationDto.class); ;
        return applicationDto;
    }

    /**
      * @author: shicheng
      * @Date 2019/11/22
      * @Param: serviceId
      * @return: HcsaServiceDto
      * @Descripation: get HcsaServiceDto By Service Id
      */
    public HcsaServiceDto getHcsaServiceDtoByServiceId(String serviceId){
        Map<String,Object> map = new HashMap<>();
        map.put("serviceId", serviceId);
        return RestApiUtil.getByReqParam("iais-hcsa-service:8878/iais-hcsa-service/one-of-hcsa-service/{serviceId}",map,HcsaServiceDto.class);
    }

    /**
      * @author: shicheng
      * @Date 2019/11/23
      * @Param: appGroupId
      * @return: AppGrpPremisesDto
      * @Descripation: get Application Group Premises By Application Id
      */
    public AppGrpPremisesDto getAppGrpPremisesDtoByAppGroId(String applicationId){
        Map<String,Object> map = new HashMap<>();
        map.put("applicationId", applicationId);
        return RestApiUtil.getByReqParam("iais-application:8883/iais-application/application-premises-by-app-id/{applicationId}",map,AppGrpPremisesDto.class);
    }

    /**
      * @author: shicheng
      * @Date 2019/11/23
      * @Param: appGroupId
      * @return: ApplicationGroupDto
      * @Descripation: get ApplicationGroup By Application Group Id
      */
    public ApplicationGroupDto getApplicationGroupDtoByAppGroId(String appGroupId){
        Map<String,Object> map = new HashMap<>();
        map.put("appGroId", appGroupId);
        return RestApiUtil.getByReqParam("iais-application:8883/iais-inspection/appGroup-of-inspection/{appGroId}",map,ApplicationGroupDto.class);
    }
}
