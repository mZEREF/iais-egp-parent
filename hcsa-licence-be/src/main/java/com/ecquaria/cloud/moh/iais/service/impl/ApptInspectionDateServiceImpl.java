package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ApptInspectionDateService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shicheng
 * @date 2020/2/11 11:32
 **/
@Service
@Slf4j
public class ApptInspectionDateServiceImpl implements ApptInspectionDateService {

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private AppointmentClient appointmentClient;

    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Autowired
    private TaskService taskService;

    @Override
    public ApptInspectionDateDto getInspectionDate(String taskId, ApptInspectionDateDto apptInspectionDateDto) {
        TaskDto taskDto = taskService.getTaskById(taskId);
        AppointmentDto appointmentDto = inspectionTaskClient.getApptStartEndDateByAppCorrId(taskDto.getRefNo()).getEntity();
        List<TaskDto> taskDtoList = organizationClient.getTaskByAppNo(taskDto.getRefNo()).getEntity();
        List<String> systemCorrIds = new ArrayList<>();
        if(!IaisCommonUtils.isEmpty(taskDtoList)){
            for(TaskDto tDto : taskDtoList){
                systemCorrIds = getSystemCorrIdByUserId(tDto.getUserId(), systemCorrIds);
            }
        }
        appointmentDto.setUserId(systemCorrIds);
        if(appointmentDto.getStartDate() == null && appointmentDto.getEndDate() == null){

        }
        List<List<ApptUserCalendarDto>> apptUserCalendarDtoList = appointmentClient.getUserCalendarByUserId(appointmentDto).getEntity();
        return apptInspectionDateDto;
    }

    private List<String> getSystemCorrIdByUserId(String userId, List<String> systemCorrIds) {
        List<String> systemCorrIdList = appointmentClient.getIdByAgencyUserId(userId).getEntity();
        if(!IaisCommonUtils.isEmpty(systemCorrIdList)){
            for(String sId : systemCorrIdList){
                systemCorrIds.add(sId);
            }
        }
        return systemCorrIds;
    }

    @Override
    public List<SelectOption> getProcessDecList() {
        String[] processDecArr = new String[]{InspectionConstants.PROCESS_DECI_ALLOW_SYSTEM_TO_PROPOSE_DATE, InspectionConstants.PROCESS_DECI_ASSIGN_SPECIFIC_DATE};
        List<SelectOption> processDecOption = MasterCodeUtil.retrieveOptionsByCodes(processDecArr);
        return processDecOption;
    }

    @Override
    public List<SelectOption> getInspectionDateHours() {
        return null;
    }

    @Override
    public List<SelectOption> getAmPmOption() {
        List<SelectOption> amPmOption = new ArrayList<>();
        SelectOption so1 = new SelectOption(Formatter.DAY_AM, "am");
        SelectOption so2 = new SelectOption(Formatter.DAY_PM, "pm");
        amPmOption.add(so1);
        amPmOption.add(so2);
        return amPmOption;
    }

    @Override
    public void saveLeadSpecificDate(ApptInspectionDateDto apptInspectionDateDto) {

    }

    @Override
    public void saveSystemInspectionDate(ApptInspectionDateDto apptInspectionDateDto) {

    }
}
