package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecTaskCreAndAssDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCommonPoolQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaServiceClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.hazelcast.aws.utility.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Autowired
    private HcsaServiceClient hcsaServiceClient;

    /*@Autowired
    private CommonPoolTaskClient commonPoolTaskClient;*/

    @Override
    public List<TaskDto> getCommPoolByGroupWordId(String workGroupId) {
        //return commonPoolTaskClient.getCommPoolByGroupWordId(workGroupId).getEntity();
        Map<String,Object> map = new HashMap<>();
        map.put("workGroupId", workGroupId);
        return RestApiUtil.getListByReqParam("iais-organization:8879/iais-task/commpool/{workGroupId}",map,TaskDto.class);
    }

    @Override
    public List<InspectionTaskPoolListDto> getPoolListByTaskDto(List<TaskDto> taskDtoList) {
        List<InspectionTaskPoolListDto> inspectionTaskPoolListDtoList = new ArrayList<>();
        if(taskDtoList != null || taskDtoList.size() > 0) {
            for(TaskDto td:taskDtoList){
                InspectionTaskPoolListDto inspectionTaskPoolListDto = getInspectionTaskPoolListDtoByTaskDto(td);
                inspectionTaskPoolListDtoList.add(inspectionTaskPoolListDto);
            }
        }
        return inspectionTaskPoolListDtoList;
    }

    @Override
    public InspecTaskCreAndAssDto getInspecTaskCreAndAssDto(String applicationNo) {
        ApplicationDto applicationDto = getApplicationDtoByAppNo(applicationNo);
        AppGrpPremisesDto appGrpPremisesDto = getAppGrpPremisesDtoByAppGroId(applicationDto.getId());
        HcsaServiceDto hcsaServiceDto = getHcsaServiceDtoByServiceId(applicationDto.getServiceId());
        ApplicationGroupDto applicationGroupDto = getApplicationGroupDtoByAppGroId(applicationDto.getAppGrpId());

        InspecTaskCreAndAssDto inspecTaskCreAndAssQueryDto = new InspecTaskCreAndAssDto();
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

    @Override
    @SearchTrack(catalog = "inspectionQuery",key = "assignInspector")
    public SearchResult<InspectionCommonPoolQueryDto> getSearchResultByParam(SearchParam searchParam) {
        return inspectionTaskClient.searchInspectionPool(searchParam).getEntity();
    }

    @Override
    public String[] getApplicationNoListByPool(List<InspectionTaskPoolListDto> inspectionTaskPoolListDtoList) {
        String[] appNoList = new String[inspectionTaskPoolListDtoList.size()];
        if(inspectionTaskPoolListDtoList != null || inspectionTaskPoolListDtoList.size() > 0) {
            for (int i = 0; i < inspectionTaskPoolListDtoList.size(); i++) {
                appNoList[i] = inspectionTaskPoolListDtoList.get(i).getApplicationNo();
            }
        }
        return appNoList;
    }

    @Override
    public List<SelectOption> getAppTypeOption() {
        List<SelectOption> appTypeOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{"APTY002","APTY004","APTY005"});
        return appTypeOption;
    }

    @Override
    public List<SelectOption> getAppStatusOption() {
        List<SelectOption> appStatusOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{"APST001"});
        return appStatusOption;
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
        ApplicationDto applicationDto = null;
        HcsaServiceDto hcsaServiceDto = null;
        if(!StringUtil.isEmpty(taskDto.getRefNo())) {
            applicationDto = getApplicationDtoByAppNo(taskDto.getRefNo());
        }
        if(!StringUtil.isEmpty(taskDto.getRefNo())) {
            hcsaServiceDto = getHcsaServiceDtoByServiceId(applicationDto.getServiceId());
        }
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
        return null;
    }

    /**
      * @author: shicheng
      * @Date 2019/11/22
      * @Param: serviceId
      * @return: HcsaServiceDto
      * @Descripation: get HcsaServiceDto By Service Id
      */
    public HcsaServiceDto getHcsaServiceDtoByServiceId(String serviceId){
        return hcsaServiceClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
    }

    /**
      * @author: shicheng
      * @Date 2019/11/23
      * @Param: appGroupId
      * @return: AppGrpPremisesDto
      * @Descripation: get Application Group Premises By Application Id
      */
    public AppGrpPremisesDto getAppGrpPremisesDtoByAppGroId(String applicationId){
        return null;
    }

    /**
      * @author: shicheng
      * @Date 2019/11/23
      * @Param: appGroupId
      * @return: ApplicationGroupDto
      * @Descripation: get ApplicationGroup By Application Group Id
      */
    public ApplicationGroupDto getApplicationGroupDtoByAppGroId(String appGroupId){
        return null;
    }
}
