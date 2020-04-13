package com.ecquaria.cloud.moh.iais.ajax;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
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
                        appointmentUserDto.setLoginUserId(orgUserDto.getDisplayName());
                        appointmentUserDto.setWorkGrpName(tDto.getWkGrpId());
                        //get service id by task refno
                        String serviceId = corrIdServiceIdMap.get(tDto.getRefNo());
                        //get manHours by service and stage
                        int manHours = hcsaConfigClient.getManHour(serviceId, HcsaConsts.ROUTING_STAGE_INS).getEntity();
                        appointmentUserDto.setWorkHours(manHours);
                        appointmentUserDtos.add(appointmentUserDto);
                    }
                    appointmentDto.setUsers(appointmentUserDtos);
                    Map<String, Collection<String>> headers = appointmentClient.getUserCalendarByUserId(appointmentDto).getHeaders();
                    //Has it been blown up
                    if(headers != null && StringUtil.isEmpty(headers.get("fusing"))) {
                        Map<String, List<ApptUserCalendarDto>> inspectionDateMap = appointmentClient.getUserCalendarByUserId(appointmentDto).getEntity();
                        apptInspectionDateDto = getShowTimeStringList(inspectionDateMap, apptInspectionDateDto);
                        map.put("buttonFlag", AppConsts.TRUE);
                    }
                    ParamUtil.setSessionAttr(request, "apptInspectionDateDto", apptInspectionDateDto);
                }
            }
        }
        return map;
    }

    private ApptInspectionDateDto getShowTimeStringList(Map<String, List<ApptUserCalendarDto>> inspectionDateMap, ApptInspectionDateDto apptInspectionDateDto) {
        List<String> inspectionDates = IaisCommonUtils.genNewArrayList();
        if(inspectionDateMap != null){
            for(Map.Entry<String, List<ApptUserCalendarDto>> inspDateMap : inspectionDateMap.entrySet()){
                List<ApptUserCalendarDto> apptUserCalendarDtos = inspDateMap.getValue();
                String inspectionDate = apptDateToStringShow(apptUserCalendarDtos.get(0).getTimeSlot().get(0));
                inspectionDates.add(inspectionDate);
            }
            apptInspectionDateDto.setInspectionDate(inspectionDates);
            apptInspectionDateDto.setInspectionDateMap(inspectionDateMap);
        }
        return apptInspectionDateDto;
    }

    private String apptDateToStringShow(Date date) {
        String specificDate = Formatter.formatDateTime(date, "d MMM");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int curHour24 = cal.get(Calendar.HOUR_OF_DAY);
        if(curHour24 > 12){
            int hours = curHour24 - 12;
            specificDate = specificDate + " " + hours + ":00" + "PM";
        } else {
            specificDate = specificDate + " " + curHour24 + ":00" + "AM";
        }
        return specificDate;
    }

    private Map<String, String> getServiceIdsByCorrIdsFromPremises(List<String> premCorrIds) {
        Map<String, String> serviceIds = applicationClient.getServiceIdsByCorrIdsFromPremises(premCorrIds).getEntity();
        return serviceIds;
    }
}
