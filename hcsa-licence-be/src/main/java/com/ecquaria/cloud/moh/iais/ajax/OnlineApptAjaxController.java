package com.ecquaria.cloud.moh.iais.ajax;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppPremInspApptDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAppInfoShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
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
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
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
import java.io.Serializable;
import java.text.ParseException;
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
    private HcsaLicenceClient hcsaLicenceClient;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private OrganizationClient organizationClient;

    @RequestMapping(value = "insp.date", method = RequestMethod.POST)
    public @ResponseBody
    Map<String, Object> appGroup(HttpServletRequest request) {
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        ApptInspectionDateDto apptInspectionDateDto = (ApptInspectionDateDto) ParamUtil.getSessionAttr(request, "apptInspectionDateDto");
        List<AppPremInspApptDraftDto> appPremInspApptDraftDtos = (List<AppPremInspApptDraftDto>) ParamUtil.getSessionAttr(request, "scheduledInspApptDraftDtos");
        boolean newInspDateFlag = getNewInspDateFlag(appPremInspApptDraftDtos);
        if(apptInspectionDateDto != null && IaisCommonUtils.isEmpty(apptInspectionDateDto.getInspectionDate())){
            ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(request, "applicationViewDto");
            ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
            String appType = applicationDto.getApplicationType();
            String actionButtonFlag = apptInspectionDateDto.getActionButtonFlag();
            //get inspection date
            if(!ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(appType)){
                if(AppConsts.SUCCESS.equals(actionButtonFlag)) {
                    TaskDto taskDto = apptInspectionDateDto.getTaskDto();
                    //get Applicant set start date and end date from appGroup
                    AppointmentDto appointmentDto = inspectionTaskClient.getApptStartEndDateByAppCorrId(taskDto.getRefNo()).getEntity();
                    //set system key
                    appointmentDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
                    List<String> premCorrIds = apptInspectionDateDto.getRefNo();
                    Map<String, String> corrIdServiceIdMap = getServiceIdsByCorrIdsFromPremises(premCorrIds);
                    List<String> serviceIds = IaisCommonUtils.genNewArrayList();
                    //get service data for get Date period
                    appointmentDto = getServiceDataForDatePeriod(appointmentDto, appType, corrIdServiceIdMap, serviceIds);
                    //get Start date and End date when group no date
                    if (appointmentDto.getStartDate() == null && appointmentDto.getEndDate() == null) {
                        appointmentDto.setServiceIds(serviceIds);
                        appointmentDto = hcsaConfigClient.getApptStartEndDateByService(appointmentDto).getEntity();
                    }
                    //get inspection date
                    List<TaskDto> taskDtoList = apptInspectionDateDto.getTaskDtos();
                    List<AppointmentUserDto> appointmentUserDtos = IaisCommonUtils.genNewArrayList();
                    //set manHours and inspectors
                    appointmentUserDtos = getManHoursInspectors(appointmentUserDtos, taskDtoList, corrIdServiceIdMap, appType);
                    //If one person is doing multiple services at the same time, The superposition of time
                    appointmentUserDtos = getOnePersonBySomeService(appointmentUserDtos);
                    appointmentDto.setUsers(appointmentUserDtos);
                    apptInspectionDateDto.setAppointmentDto(appointmentDto);
                    boolean dateFlag = getStartEndDateFlag(appointmentDto);
                    if(dateFlag && newInspDateFlag) {
                        //set Inspection date show, flag,
                        apptInspectionDateDto = getNewInspDateData(apptInspectionDateDto, appointmentDto, map, taskDtoList, request);
                    } else if(!newInspDateFlag) {
                        //get Inspection date Draft
                        apptInspectionDateDto = setInspDateDraftData(apptInspectionDateDto, appPremInspApptDraftDtos);
                        apptInspectionDateDto.setSysInspDateFlag(AppConsts.TRUE);
                        apptInspectionDateDto.setSysSpecDateFlag(AppConsts.TRUE);
                        map.put("buttonFlag", AppConsts.TRUE);
                        map.put("specButtonFlag", AppConsts.TRUE);
                        map.put("inspDateList", apptInspectionDateDto.getInspectionDate());
                    } else {
                        map.put("buttonFlag", AppConsts.FALSE);
                        map.put("specButtonFlag", AppConsts.TRUE);
                        map.put("inspDateList", null);
                    }
                    //specific date dto
                    AppointmentDto specificApptDto = new AppointmentDto();
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
                //set data for validate
                apptInspectionDateDto = setSomeDataForVali(apptInspectionDateDto, map, appType);
                ParamUtil.setSessionAttr(request, "apptInspectionDateDto", apptInspectionDateDto);
            }
        }
        return map;
    }

    private ApptInspectionDateDto setInspDateDraftData(ApptInspectionDateDto apptInspectionDateDto, List<AppPremInspApptDraftDto> appPremInspApptDraftDtos) {
        if(!IaisCommonUtils.isEmpty(appPremInspApptDraftDtos)) {
            log.info(StringUtil.changeForLog("Inspection Scheduling appPremInspApptDraftDtos Size ====" + appPremInspApptDraftDtos.size()));
            Map<String, List<ApptUserCalendarDto>> inspectionDateMap = new LinkedHashMap<>(4);
            for(AppPremInspApptDraftDto appPremInspApptDraftDto : appPremInspApptDraftDtos) {
                if(appPremInspApptDraftDto != null) {
                    String apptRefNo = appPremInspApptDraftDto.getApptRefNo();
                    List<ApptUserCalendarDto> apptUserCalendarDtos = inspectionDateMap.get(apptRefNo);
                    if (IaisCommonUtils.isEmpty(apptUserCalendarDtos)) {
                        apptUserCalendarDtos = IaisCommonUtils.genNewArrayList();
                    }
                    ApptUserCalendarDto apptUserCalendarDto = new ApptUserCalendarDto();
                    List<Date> startSlot = IaisCommonUtils.genNewArrayList();
                    List<Date> endSlot = IaisCommonUtils.genNewArrayList();
                    Date inspStartDate = appPremInspApptDraftDto.getStartDate();
                    startSlot.add(inspStartDate);
                    Date inspEndDate = appPremInspApptDraftDto.getEndDate();
                    endSlot.add(inspEndDate);
                    OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(appPremInspApptDraftDto.getUserId()).getEntity();
                    //set
                    apptUserCalendarDto.setAppNo(appPremInspApptDraftDto.getApplicationNo());
                    apptUserCalendarDto.setApptRefNo(apptRefNo);
                    apptUserCalendarDto.setStartSlot(startSlot);
                    apptUserCalendarDto.setEndSlot(endSlot);
                    apptUserCalendarDto.setLoginUserId(orgUserDto.getUserId());
                    apptUserCalendarDtos.add(apptUserCalendarDto);
                    inspectionDateMap.put(apptRefNo, apptUserCalendarDtos);
                }
            }
            //set insp date string show
            apptInspectionDateDto = setInspDateDraftStrShow(appPremInspApptDraftDtos, apptInspectionDateDto);
            apptInspectionDateDto.setInspectionDateMap(inspectionDateMap);
        }
        return apptInspectionDateDto;
    }

    private ApptInspectionDateDto setInspDateDraftStrShow(List<AppPremInspApptDraftDto> appPremInspApptDraftDtos, ApptInspectionDateDto apptInspectionDateDto) {
        AppPremInspApptDraftDto appPremInspApptDraftDto = appPremInspApptDraftDtos.get(0);
        Date inspStartDate = appPremInspApptDraftDto.getStartDate();
        Date inspEndDate = appPremInspApptDraftDto.getEndDate();
        //date to str
        String inspStartDateStr = apptDateToStringShow(inspStartDate);
        String inspEndDateStr = apptDateToStringShow(inspEndDate);
        String inspectionDate = inspStartDateStr + " - " + inspEndDateStr;
        List<String> inspectionDates = IaisCommonUtils.genNewArrayList();
        inspectionDates.add(inspectionDate);
        apptInspectionDateDto.setInspectionDate(inspectionDates);
        return apptInspectionDateDto;
    }

    private ApptInspectionDateDto setSomeDataForVali(ApptInspectionDateDto apptInspectionDateDto, Map<String, Object> map, String appType) {
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
            specificApptDto.setSvcIdLicDtMap(null);
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
        apptInspectionDateDto.setSysSpecDateFlag(AppConsts.TRUE);
        map.put("specButtonFlag", AppConsts.TRUE);
        return apptInspectionDateDto;
    }

    private ApptInspectionDateDto getNewInspDateData(ApptInspectionDateDto apptInspectionDateDto, AppointmentDto appointmentDto, Map<String, Object> map,
                                                     List<TaskDto> taskDtoList, HttpServletRequest request) {
        try {
            appointmentDto.setResultNum(1);
            FeignResponseEntity<List<ApptRequestDto>> result = appointmentClient.getUserCalendarByUserId(appointmentDto);
            Map<String, Collection<String>> headers = result.getHeaders();
            //Has it been blown up
            if (headers != null && StringUtil.isEmpty(headers.get("fusing"))) {
                List<ApptRequestDto> apptRequestDtos = result.getEntity();
                if (!IaisCommonUtils.isEmpty(apptRequestDtos)) {
                    Map<String, List<ApptUserCalendarDto>> inspectionDateMap = new LinkedHashMap<>(apptRequestDtos.size());
                    for (ApptRequestDto apptRequestDto : apptRequestDtos) {
                        inspectionDateMap.put(apptRequestDto.getApptRefNo(), apptRequestDto.getUserClandars());
                    }
                    apptInspectionDateDto.setSysInspDateFlag(AppConsts.TRUE);
                    apptInspectionDateDto.setSysSpecDateFlag(AppConsts.TRUE);
                    apptInspectionDateDto = getShowTimeStringList(inspectionDateMap, apptInspectionDateDto);
                    //save inspection draft
                    List<AppPremInspApptDraftDto> appPremInspApptDraftDtos = saveInspectionDateDraft(apptRequestDtos, taskDtoList);
                    ParamUtil.setSessionAttr(request, "scheduledInspApptDraftDtos", (Serializable) appPremInspApptDraftDtos);
                    map.put("buttonFlag", AppConsts.TRUE);
                    map.put("specButtonFlag", AppConsts.TRUE);
                    map.put("inspDateList", apptInspectionDateDto.getInspectionDate());
                } else {
                    map.put("buttonFlag", AppConsts.FALSE);
                    map.put("specButtonFlag", AppConsts.TRUE);
                    map.put("inspDateList", null);
                    apptInspectionDateDto.setSysSpecDateFlag(AppConsts.TRUE);
                }
            } else {
                map.put("buttonFlag", AppConsts.FALSE);
                map.put("specButtonFlag", AppConsts.TRUE);
                map.put("inspDateList", null);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return apptInspectionDateDto;
    }

    private List<AppPremInspApptDraftDto> saveInspectionDateDraft(List<ApptRequestDto> apptRequestDtos, List<TaskDto> taskDtoList) {
        List<AppPremInspApptDraftDto> appPremInspApptDraftDtos = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(taskDtoList) && !IaisCommonUtils.isEmpty(apptRequestDtos)) {
            List<String> appNoList = IaisCommonUtils.genNewArrayList();
            for(TaskDto taskDto : taskDtoList) {
                for(ApptRequestDto apptRequestDto : apptRequestDtos) {
                    if(taskDto != null && apptRequestDto != null) {
                        //get date
                        int endTimeSize = apptRequestDto.getUserClandars().get(0).getEndSlot().size();
                        Date inspStartDate = apptRequestDto.getUserClandars().get(0).getStartSlot().get(0);
                        Date inspEndDate = apptRequestDto.getUserClandars().get(0).getEndSlot().get(endTimeSize - 1);
                        //set data
                        if(!appNoList.contains(taskDto.getApplicationNo())) {
                            appNoList.add(taskDto.getApplicationNo());
                            AppPremInspApptDraftDto appPremInspApptDraftDto = new AppPremInspApptDraftDto();
                            appPremInspApptDraftDto.setApplicationNo(taskDto.getApplicationNo());
                            appPremInspApptDraftDto.setApptRefNo(apptRequestDto.getApptRefNo());
                            appPremInspApptDraftDto.setStartDate(inspStartDate);
                            appPremInspApptDraftDto.setEndDate(inspEndDate);
                            appPremInspApptDraftDto.setUserId(taskDto.getUserId());
                            appPremInspApptDraftDtos.add(appPremInspApptDraftDto);
                        }
                    }
                }
            }
            appPremInspApptDraftDtos = inspectionTaskClient.createAppPremisesInspecApptDto(appPremInspApptDraftDtos).getEntity();
        }
        return appPremInspApptDraftDtos;
    }

    private AppointmentDto getServiceDataForDatePeriod(AppointmentDto appointmentDto, String appType, Map<String, String> corrIdServiceIdMap, List<String> serviceIds) {
        if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
            Map<String, Date> svcIdLicDtMap = IaisCommonUtils.genNewHashMap();
            for (Map.Entry<String, String> mapDate : corrIdServiceIdMap.entrySet()) {
                if(!StringUtil.isEmpty(mapDate.getValue())){
                    svcIdLicDtMap = setSvcIdLicDtMapByApp(mapDate.getKey(), mapDate.getValue(), svcIdLicDtMap);
                    serviceIds.add(mapDate.getValue());
                }
            }
            appointmentDto.setSvcIdLicDtMap(svcIdLicDtMap);
        } else {
            for (Map.Entry<String, String> mapDate : corrIdServiceIdMap.entrySet()) {
                if(!StringUtil.isEmpty(mapDate.getValue())){
                    serviceIds.add(mapDate.getValue());
                }
            }
            appointmentDto.setSvcIdLicDtMap(null);
        }
        return appointmentDto;
    }

    private List<AppointmentUserDto> getManHoursInspectors(List<AppointmentUserDto> appointmentUserDtos, List<TaskDto> taskDtoList,
                                                           Map<String, String> corrIdServiceIdMap, String appType) {
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
        return appointmentUserDtos;
    }

    private boolean getNewInspDateFlag(List<AppPremInspApptDraftDto> appPremInspApptDraftDtos) {
        if(!IaisCommonUtils.isEmpty(appPremInspApptDraftDtos)) {
            return false;
        }
        return true;
    }

    private boolean getStartEndDateFlag(AppointmentDto appointmentDto) {
        Date today = new Date();
        String todayStr = Formatter.formatDateTime(today, AppConsts.DEFAULT_DATE_FORMAT);
        String startDateStr = appointmentDto.getStartDate();
        String endDateStr = appointmentDto.getEndDate();
        Date startDate = null;
        Date endDate = null;
        try {
            today = Formatter.parseDateTime(todayStr, AppConsts.DEFAULT_DATE_FORMAT);
            if(!StringUtil.isEmpty(startDateStr)) {
                startDate = Formatter.parseDateTime(startDateStr, AppConsts.DEFAULT_DATE_FORMAT);
            }
            if(!StringUtil.isEmpty(endDateStr)) {
                endDate = Formatter.parseDateTime(endDateStr, AppConsts.DEFAULT_DATE_FORMAT);
            }
        } catch (ParseException e) {
            log.info("Appt Date Error!!!!!");
            log.error(e.getMessage(), e);
        }
        if(endDate != null){
            if(endDate.before(today)){
                return false;
            } else {
                if(startDate == null){
                    return false;
                } else {
                    if(startDate.before(today)){
                        startDate = new Date();
                        appointmentDto.setStartDate(Formatter.formatDateTime(startDate, AppConsts.DEFAULT_DATE_TIME_FORMAT));
                    }
                }
            }
        } else {
            if(startDate == null){
                return false;
            } else {
                if(startDate.before(today)){
                    startDate = new Date();
                    appointmentDto.setStartDate(Formatter.formatDateTime(startDate, AppConsts.DEFAULT_DATE_TIME_FORMAT));
                }
            }
        }
        return true;
    }

    private Map<String, Date> setSvcIdLicDtMapByApp(String appPremCorrId, String serviceId, Map<String, Date> svcIdLicDtMap) {
        ApplicationDto appDto = inspectionTaskClient.getApplicationByCorreId(appPremCorrId).getEntity();
        String orgLicId = appDto.getOriginLicenceId();
        if(!StringUtil.isEmpty(orgLicId)) {
            LicenceDto licDto = hcsaLicenceClient.getLicDtoById(orgLicId).getEntity();
            if(licDto != null && licDto.getExpiryDate() != null){
                svcIdLicDtMap.put(serviceId, licDto.getExpiryDate());
            }
        }
        return svcIdLicDtMap;
    }

    private List<AppointmentUserDto> getOnePersonBySomeService(List<AppointmentUserDto> appointmentUserDtos) {
        List<AppointmentUserDto> appointmentUserDtoList = null;
        if (!IaisCommonUtils.isEmpty(appointmentUserDtos)) {
            appointmentUserDtoList = IaisCommonUtils.genNewArrayList();
            for (AppointmentUserDto appointmentUserDto : appointmentUserDtos) {
                if (appointmentUserDtoList.isEmpty()) {
                    appointmentUserDtoList.add(appointmentUserDto);
                } else {
                    filterRepetitiveUser(appointmentUserDto, appointmentUserDtoList);
                }
            }
        }
        return appointmentUserDtoList;
    }

    private List<AppointmentUserDto> filterRepetitiveUser(AppointmentUserDto appointmentUserDto, List<AppointmentUserDto> appointmentUserDtoList) {
        List<AppointmentUserDto> appointmentUserDtos = IaisCommonUtils.genNewArrayList();
        boolean sameUserFlag = false;
        for(AppointmentUserDto appointmentUserDto1 : appointmentUserDtoList){
            String loginUserId = appointmentUserDto.getLoginUserId();
            String curLoginUserId = appointmentUserDto1.getLoginUserId();
            if (loginUserId.equals(curLoginUserId)) {
                int hours = appointmentUserDto.getWorkHours();
                int curHours = appointmentUserDto1.getWorkHours();
                int allHours = hours + curHours;
                appointmentUserDto1.setWorkHours(allHours);
                sameUserFlag = true;
            }
        }
        if(!sameUserFlag) {
            appointmentUserDtos.add(appointmentUserDto);
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