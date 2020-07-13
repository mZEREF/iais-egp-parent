package com.ecquaria.cloud.moh.iais.ajax;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAppInfoShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2020/4/13 15:57
 **/
@Slf4j
@Controller
@RequestMapping("/online-appt")
public class OnlineApptAjaxController {

    @Autowired
    private AppointmentClient appointmentClient;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private OrganizationClient organizationClient;

    @RequestMapping(value = "insp.date", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> appGroup(HttpServletRequest request) {
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        ApptInspectionDateDto apptInspectionDateDto = (ApptInspectionDateDto) ParamUtil.getSessionAttr(request, "apptInspectionDateDto");
        if(apptInspectionDateDto != null && IaisCommonUtils.isEmpty(apptInspectionDateDto.getInspectionDate())){
            ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(request, "applicationViewDto");
            ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
            String appType = applicationDto.getApplicationType();
            String actionButtonFlag = apptInspectionDateDto.getActionButtonFlag();

            if(!ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(appType)){
                if(AppConsts.SUCCESS.equals(actionButtonFlag)) {
                    TaskDto taskDto = apptInspectionDateDto.getTaskDto();
                    //get Applicant set start date and end date from appGroup
                    AppointmentDto appointmentDto = inspectionTaskClient.getApptStartEndDateByAppCorrId(taskDto.getRefNo()).getEntity();
                    //specific date dto
                    AppointmentDto specificApptDto = new AppointmentDto();
                    appointmentDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
                    List<String> premCorrIds = apptInspectionDateDto.getRefNo();
                    Map<String, String> corrIdServiceIdMap = getServiceIdsByCorrIdsFromPremises(premCorrIds);
                    List<String> serviceIds = IaisCommonUtils.genNewArrayList();
                    for (Map.Entry<String, String> mapDate : corrIdServiceIdMap.entrySet()) {
                        if(!StringUtil.isEmpty(mapDate.getValue())){
                            serviceIds.add(mapDate.getValue());
                        }
                    }
                    //get Start date and End date when group no date
                    if (appointmentDto.getStartDate() == null && appointmentDto.getEndDate() == null) {
                        appointmentDto.setServiceIds(serviceIds);
                        appointmentDto = hcsaConfigClient.getApptStartEndDateByService(appointmentDto).getEntity();
                    }
                    //get inspection date
                    List<TaskDto> taskDtoList = apptInspectionDateDto.getTaskDtos();
                    List<AppointmentUserDto> appointmentUserDtos = IaisCommonUtils.genNewArrayList();
                    for (TaskDto tDto : taskDtoList) {
                        AppointmentUserDto appointmentUserDto = new AppointmentUserDto();
                        OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(tDto.getUserId()).getEntity();
                        appointmentUserDto.setLoginUserId(orgUserDto.getUserId());
                        String workGroupId = tDto.getWkGrpId();
                        WorkingGroupDto workingGroupDto = organizationClient.getWrkGrpById(workGroupId).getEntity();
                        appointmentUserDto.setWorkGrpName(workingGroupDto.getGroupName());
                        //get service id by task refno
                        String serviceId = corrIdServiceIdMap.get(tDto.getRefNo());
                        //get manHours by service and stage
                        ApptAppInfoShowDto apptAppInfoShowDto = new ApptAppInfoShowDto();
                        apptAppInfoShowDto.setApplicationType(appType);
                        apptAppInfoShowDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
                        apptAppInfoShowDto.setServiceId(serviceId);
                        int manHours = getServiceManHours(tDto.getRefNo(), apptAppInfoShowDto);
                        //Divide the time according to the number of people
                        List<TaskDto> sizeTask = organizationClient.getCurrTaskByRefNo(tDto.getRefNo()).getEntity();
                        double hours = manHours;
                        double peopleCount = sizeTask.size();
                        int peopleHours = (int) Math.ceil(hours/peopleCount);
                        appointmentUserDto.setWorkHours(peopleHours);
                        appointmentUserDtos.add(appointmentUserDto);
                    }
                    //If one person is doing multiple services at the same time, The superposition of time
                    appointmentUserDtos = getOnePersonBySomeService(appointmentUserDtos);
                    appointmentDto.setUsers(appointmentUserDtos);
                    apptInspectionDateDto.setAppointmentDto(appointmentDto);
                    try {
                        FeignResponseEntity<List<ApptRequestDto>> result = appointmentClient.getUserCalendarByUserId(appointmentDto);
                        Map<String, Collection<String>> headers = result.getHeaders();
                        //Has it been blown up
                        if(headers != null && StringUtil.isEmpty(headers.get("fusing"))) {
                            List<ApptRequestDto> apptRequestDtos = result.getEntity();
                            Map<String, List<ApptUserCalendarDto>> inspectionDateMap = new LinkedHashMap<>(apptRequestDtos.size());
                            if(!IaisCommonUtils.isEmpty(apptRequestDtos)){
                                for(ApptRequestDto apptRequestDto : apptRequestDtos){
                                    inspectionDateMap.put(apptRequestDto.getApptRefNo(), apptRequestDto.getUserClandars());
                                }
                            }
                            apptInspectionDateDto = getShowTimeStringList(inspectionDateMap, apptInspectionDateDto);
                            map.put("buttonFlag", AppConsts.TRUE);
                            map.put("inspDateList", apptInspectionDateDto.getInspectionDate());
                            apptInspectionDateDto.setSysInspDateFlag(AppConsts.TRUE);
                        }
                    } catch (Exception e){
                        log.error(e.getMessage(), e);
                    }
                    specificApptDto.setSubmitDt(appointmentDto.getSubmitDt());
                    specificApptDto.setUsers(appointmentDto.getUsers());
                    specificApptDto.setSysClientKey(appointmentDto.getSysClientKey());
                    specificApptDto.setServiceIds(appointmentDto.getServiceIds());
                    specificApptDto.setServiceId(appointmentDto.getServiceId());
                    apptInspectionDateDto.setSpecificApptDto(specificApptDto);
                    ParamUtil.setSessionAttr(request, "apptInspectionDateDto", apptInspectionDateDto);
                }
            //Audit Task
            } else {
                TaskDto taskDto = apptInspectionDateDto.getTaskDto();
                //get Applicant set start date and end date from appGroup
                AppointmentDto specificApptDto = inspectionTaskClient.getApptStartEndDateByAppCorrId(taskDto.getRefNo()).getEntity();
                specificApptDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
                List<String> premCorrIds = apptInspectionDateDto.getRefNo();
                Map<String, String> corrIdServiceIdMap = getServiceIdsByCorrIdsFromPremises(premCorrIds);
                List<String> serviceIds = IaisCommonUtils.genNewArrayList();
                for (Map.Entry<String, String> mapDate : corrIdServiceIdMap.entrySet()) {
                    if(!StringUtil.isEmpty(mapDate.getValue())){
                        serviceIds.add(mapDate.getValue());
                    }
                }
                //get Start date and End date when group no date
                if (specificApptDto.getStartDate() == null && specificApptDto.getEndDate() == null) {
                    specificApptDto.setServiceIds(serviceIds);
                    specificApptDto = hcsaConfigClient.getApptStartEndDateByService(specificApptDto).getEntity();
                }
                //set data to validate
                List<TaskDto> taskDtoList = apptInspectionDateDto.getTaskDtos();
                List<AppointmentUserDto> appointmentUserDtos = IaisCommonUtils.genNewArrayList();
                for (TaskDto tDto : taskDtoList) {
                    AppointmentUserDto appointmentUserDto = new AppointmentUserDto();
                    OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(tDto.getUserId()).getEntity();
                    appointmentUserDto.setLoginUserId(orgUserDto.getUserId());
                    String workGroupId = tDto.getWkGrpId();
                    WorkingGroupDto workingGroupDto = organizationClient.getWrkGrpById(workGroupId).getEntity();
                    appointmentUserDto.setWorkGrpName(workingGroupDto.getGroupName());
                    //get service id by task refno
                    String serviceId = corrIdServiceIdMap.get(tDto.getRefNo());
                    //get manHours by service and stage
                    ApptAppInfoShowDto apptAppInfoShowDto = new ApptAppInfoShowDto();
                    apptAppInfoShowDto.setApplicationType(appType);
                    apptAppInfoShowDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
                    apptAppInfoShowDto.setServiceId(serviceId);
                    int manHours = getServiceManHours(tDto.getRefNo(), apptAppInfoShowDto);
                    //Divide the time according to the number of people
                    List<TaskDto> sizeTask = organizationClient.getCurrTaskByRefNo(tDto.getRefNo()).getEntity();
                    double hours = manHours;
                    double peopleCount = sizeTask.size();
                    int peopleHours = (int) Math.ceil(hours/peopleCount);
                    appointmentUserDto.setWorkHours(peopleHours);
                    appointmentUserDtos.add(appointmentUserDto);
                }
                //If one person is doing multiple services at the same time, The superposition of time
                appointmentUserDtos = getOnePersonBySomeService(appointmentUserDtos);
                specificApptDto.setUsers(appointmentUserDtos);
                apptInspectionDateDto.setSpecificApptDto(specificApptDto);
                ParamUtil.setSessionAttr(request, "apptInspectionDateDto", apptInspectionDateDto);
            }
        }
        return map;
    }

    private List<AppointmentUserDto> getOnePersonBySomeService(List<AppointmentUserDto> appointmentUserDtos) {
        List<AppointmentUserDto> appointmentUserDtoList = null;
        if(!IaisCommonUtils.isEmpty(appointmentUserDtos)){
            for(AppointmentUserDto appointmentUserDto : appointmentUserDtos){
                if(IaisCommonUtils.isEmpty(appointmentUserDtoList)){
                    appointmentUserDtoList = IaisCommonUtils.genNewArrayList();
                    appointmentUserDtoList.add(appointmentUserDto);
                } else {
                    appointmentUserDtoList = filterRepetitiveUser(appointmentUserDto, appointmentUserDtoList);
                }
            }
        }
        return appointmentUserDtoList;
    }

    private List<AppointmentUserDto> filterRepetitiveUser(AppointmentUserDto appointmentUserDto, List<AppointmentUserDto> appointmentUserDtoList) {
        List<AppointmentUserDto> appointmentUserDtos = IaisCommonUtils.genNewArrayList();
        for(AppointmentUserDto appointmentUserDto1 : appointmentUserDtoList){
            String loginUserId = appointmentUserDto.getLoginUserId();
            String curLoginUserId = appointmentUserDto1.getLoginUserId();
            if (loginUserId.equals(curLoginUserId)) {
                int hours = appointmentUserDto.getWorkHours();
                int curHours = appointmentUserDto1.getWorkHours();
                int allHours = hours + curHours;
                appointmentUserDto1.setWorkHours(allHours);
            } else {
                appointmentUserDtos.add(appointmentUserDto);
            }
        }
        if(!IaisCommonUtils.isEmpty(appointmentUserDtos)){
            for(AppointmentUserDto auDto : appointmentUserDtos){
                if(auDto != null){
                    appointmentUserDtoList.add(auDto);
                }
            }
        }
        return appointmentUserDtoList;
    }

    private int getServiceManHours(String refNo, ApptAppInfoShowDto apptAppInfoShowDto) {
        int manHours;
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(refNo, InspectionConstants.RECOM_TYPE_INSP_MAN_HOUR).getEntity();
        if(appPremisesRecommendationDto != null){
            String hours = appPremisesRecommendationDto.getRecomDecision();
            if(!StringUtil.isEmpty(hours)){
                manHours = Integer.parseInt(hours);
            } else {
                manHours = hcsaConfigClient.getManHour(apptAppInfoShowDto).getEntity();
            }
        } else {
            manHours = hcsaConfigClient.getManHour(apptAppInfoShowDto).getEntity();
        }
        return manHours;
    }

    private ApptInspectionDateDto getShowTimeStringList(Map<String, List<ApptUserCalendarDto>> inspectionDateMap, ApptInspectionDateDto apptInspectionDateDto) {
        List<String> inspectionDates = IaisCommonUtils.genNewArrayList();
        if(inspectionDateMap != null){
            for(Map.Entry<String, List<ApptUserCalendarDto>> inspDateMap : inspectionDateMap.entrySet()){
                List<ApptUserCalendarDto> apptUserCalendarDtos = inspDateMap.getValue();
                int endTimeSize = apptUserCalendarDtos.get(0).getEndSlot().size();
                String inspStartDate = apptDateToStringShow(apptUserCalendarDtos.get(0).getStartSlot().get(0));
                String inspEndDate = apptDateToStringShow(apptUserCalendarDtos.get(0).getEndSlot().get(endTimeSize - 1));
                String inspectionDate = inspStartDate + " - " + inspEndDate;
                inspectionDates.add(inspectionDate);
            }
            apptInspectionDateDto.setInspectionDate(inspectionDates);
            apptInspectionDateDto.setInspectionDateMap(inspectionDateMap);
        } else {
            apptInspectionDateDto.setInspectionDate(null);
            apptInspectionDateDto.setSysInspDateFlag(AppConsts.FALSE);
        }
        return apptInspectionDateDto;
    }

    private String apptDateToStringShow(Date date) {
        String specificDate = Formatter.formatDateTime(date, "dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int minutes = cal.get(Calendar.MINUTE);
        if(minutes > 0){
            cal.add(Calendar.HOUR_OF_DAY, 1);
        }
        int curHour24 = cal.get(Calendar.HOUR_OF_DAY);
        String hoursShow = "";
        if(curHour24 < 10){
            hoursShow = "0";
        }
        specificDate = specificDate + " " + hoursShow + curHour24 + ":00";
        return specificDate;
    }

    private Map<String, String> getServiceIdsByCorrIdsFromPremises(List<String> premCorrIds) {
        Map<String, String> serviceIds = applicationClient.getServiceIdsByCorrIdsFromPremises(premCorrIds).getEntity();
        return serviceIds;
    }
}
