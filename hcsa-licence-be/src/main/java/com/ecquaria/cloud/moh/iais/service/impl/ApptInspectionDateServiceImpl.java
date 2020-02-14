package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ApptInspectionDateService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Autowired
    private ApplicationClient applicationClient;

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
            appointmentDto = hcsaConfigClient.getApptStartEndDateByService(appointmentDto).getEntity();
        }
        List<List<ApptUserCalendarDto>> apptUserCalendarDtoList = appointmentClient.getUserCalendarByUserId(appointmentDto).getEntity();
        apptInspectionDateDto = getShowTimeStringList(apptUserCalendarDtoList, apptInspectionDateDto);
        apptInspectionDateDto.setTaskDto(taskDto);
        apptInspectionDateDto.setTaskDtos(taskDtoList);
        return apptInspectionDateDto;
    }

    private ApptInspectionDateDto getShowTimeStringList(List<List<ApptUserCalendarDto>> apptUserCalendarDtoList, ApptInspectionDateDto apptInspectionDateDto) {
        List<String> inspectionDates = new ArrayList<>();
        if(!IaisCommonUtils.isEmpty(apptUserCalendarDtoList)){
            List<ApptUserCalendarDto> apptUserCalendarDtoListAll = new ArrayList<>();
            for(List<ApptUserCalendarDto> apptUserCalendarDtos : apptUserCalendarDtoList){
                for(ApptUserCalendarDto  aDto : apptUserCalendarDtos){
                    apptUserCalendarDtoListAll.add(aDto);
                }
                if(!IaisCommonUtils.isEmpty(apptUserCalendarDtos)){
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(apptUserCalendarDtos.get(0).getTimeSlot());
                    int curHour24 = cal.get(Calendar.HOUR_OF_DAY);
                    String hours;
                    if(curHour24 > 12){
                        hours = (curHour24 - 12) + "pm";
                    } else {
                        hours = curHour24 + "am";
                    }
                    String[] weeks = {"Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};
                    int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
                    String week = weeks[w];
                    SimpleDateFormat format = new SimpleDateFormat("d");
                    String temp = format.format(apptUserCalendarDtos.get(0).getTimeSlot());
                    if(temp.endsWith("1") && !temp.endsWith("11")){
                        format = new SimpleDateFormat("dd'st'MMMM yyyy", Locale.ENGLISH);
                    }else if(temp.endsWith("2") && !temp.endsWith("12")){
                        format = new SimpleDateFormat("dd'nd'MMMM yyyy",Locale.ENGLISH);
                    }else if(temp.endsWith("3") && !temp.endsWith("13")){
                        format = new SimpleDateFormat("dd'rd'MMMM yyyy",Locale.ENGLISH);
                    }else{
                        format = new SimpleDateFormat("dd'th'MMMM yyyy",Locale.ENGLISH);
                    }
                    String englishDate = format.format(apptUserCalendarDtos.get(0).getTimeSlot());
                    String fullDate = week + ", " + englishDate + ", " + hours;
                    inspectionDates.add(fullDate);
                }
            }
            apptInspectionDateDto.setInspectionDate(inspectionDates);
            apptInspectionDateDto.setApptUserCalendarDtoListAll(apptUserCalendarDtoListAll);
        }
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
        List<SelectOption> hourOption = new ArrayList<>();
        for(int i = 1; i < 13; i++){
            SelectOption so = new SelectOption(i + "", i + "");
            hourOption.add(so);
        }
        return hourOption;
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
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = new ArrayList<>();
        AppPremisesInspecApptDto appPremisesInspecApptDto = new AppPremisesInspecApptDto();
        appPremisesInspecApptDto.setAppCorrId(apptInspectionDateDto.getTaskDto().getRefNo());
        appPremisesInspecApptDto.setApptRefNo(null);
        appPremisesInspecApptDto.setSpecificInspDate(apptInspectionDateDto.getSpecificDate());
        appPremisesInspecApptDto.setId(null);
        appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesInspecApptDtoList.add(appPremisesInspecApptDto);
        applicationClient.createAppPremisesInspecApptDto(appPremisesInspecApptDtoList);
    }

    @Override
    public void saveSystemInspectionDate(ApptInspectionDateDto apptInspectionDateDto) {
        List<AppPremisesInspecApptDto> appPremisesInspecApptDtoList = new ArrayList<>();
        for(ApptUserCalendarDto aucDto : apptInspectionDateDto.getApptUserCalendarDtoListAll()) {
            AppPremisesInspecApptDto appPremisesInspecApptDto = new AppPremisesInspecApptDto();
            appPremisesInspecApptDto.setAppCorrId(apptInspectionDateDto.getTaskDto().getRefNo());
            appPremisesInspecApptDto.setApptRefNo(aucDto.getApptRefNo());
            appPremisesInspecApptDto.setSpecificInspDate(null);
            appPremisesInspecApptDto.setId(null);
            appPremisesInspecApptDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appPremisesInspecApptDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            appPremisesInspecApptDtoList.add(appPremisesInspecApptDto);
        }
        applicationClient.createAppPremisesInspecApptDto(appPremisesInspecApptDtoList);
    }
}
