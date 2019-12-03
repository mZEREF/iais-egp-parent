package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionTaskPoolListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.client.CommonPoolTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaServiceClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shicheng
 * @date 2019/11/19 14:45
 **/
@Service
@Slf4j
public class InspectionServiceImpl implements InspectionService {
    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Autowired
    private HcsaServiceClient hcsaServiceClient;

    @Autowired
    private CommonPoolTaskClient commonPoolTaskClient;

    @Override
    public List<SelectOption> getAppTypeOption() {
        List<SelectOption> appTypeOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION, ApplicationConsts.APPLICATION_TYPE_RENEWAL, ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE});
        return appTypeOption;
    }

    @Override
    public List<SelectOption> getAppStatusOption() {
        List<SelectOption> appStatusOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION});
        return appStatusOption;
    }

    @Override
    public List<TaskDto> getCommPoolByGroupWordId(String workGroupId) {
        return commonPoolTaskClient.getCommPoolByGroupWordId(workGroupId).getEntity();
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
     * @Param: serviceId
     * @return: HcsaServiceDto
     * @Descripation: get HcsaServiceDto By Service Id
     */
    public HcsaServiceDto getHcsaServiceDtoByServiceId(String serviceId){
        return hcsaServiceClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
    }

    /**
     * @author: shicheng
     * @Date 2019/11/22
     * @Param: appNo
     * @return: ApplicationDto
     * @Descripation: get ApplicationDto By Application No.
     */
    public ApplicationDto getApplicationDtoByAppNo(String appNo){
        return inspectionTaskClient.getApplicationDtoByAppNo(appNo).getEntity();
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
}
