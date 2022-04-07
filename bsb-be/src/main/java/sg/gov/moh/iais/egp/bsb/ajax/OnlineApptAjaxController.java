package sg.gov.moh.iais.egp.bsb.ajax;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.*;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sg.gov.moh.iais.egp.bsb.client.AppointmentClient;
import sg.gov.moh.iais.egp.bsb.client.BsbAppointmentClient;
import sg.gov.moh.iais.egp.bsb.client.OrganizationClient;
import sg.gov.moh.iais.egp.bsb.dto.appointment.AppointmentReviewDataDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionAppointmentDraftDto;
import sg.gov.moh.iais.egp.bsb.util.DateUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

import static sg.gov.moh.iais.egp.bsb.constant.AppointmentConstants.*;

@Slf4j
@Controller
@RequestMapping("/online-appt")
public class OnlineApptAjaxController {


    private final BsbAppointmentClient bsbAppointmentClient;
    private final OrganizationClient organizationClient;
    private final AppointmentClient appointmentClient;

    public OnlineApptAjaxController(BsbAppointmentClient bsbAppointmentClient, OrganizationClient organizationClient, AppointmentClient appointmentClient) {
        this.bsbAppointmentClient = bsbAppointmentClient;
        this.organizationClient = organizationClient;
        this.appointmentClient = appointmentClient;
    }

    @PostMapping(value = "insp.date")
    public @ResponseBody
    Map<String, Object> getInspectionDate(HttpServletRequest request) {
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(request, AppConsts.SESSION_ATTR_LOGIN_USER);
        Map<String, Object> map = Maps.newHashMapWithExpectedSize(3);
        ApptInspectionDateDto apptInspectionDateDto = (ApptInspectionDateDto) ParamUtil.getSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DTO);
        List<InspectionAppointmentDraftDto> appPremInspApptDraftDtos = (List<InspectionAppointmentDraftDto>) ParamUtil.getSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DRAFT_DTO);
        boolean newInspDateFlag = getNewInspDateFlag(appPremInspApptDraftDtos);
        if (apptInspectionDateDto != null && CollectionUtils.isEmpty(apptInspectionDateDto.getInspectionDate())) {
            AppointmentReviewDataDto dto = (AppointmentReviewDataDto) ParamUtil.getSessionAttr(request, APPOINTMENT_REVIEW_DATA);
            String appType = dto.getApplicationType();
            String actionButtonFlag = apptInspectionDateDto.getActionButtonFlag();
            //get inspection date
            if (AppConsts.SUCCESS.equals(actionButtonFlag)) {
                //todo get inspection start date and end date from application
                AppointmentDto appointmentDto = bsbAppointmentClient.getApptStartEndDateByAppId(dto.getApplicationId()).getEntity();
                List<AppointmentUserDto> appointmentUserDtos = new ArrayList<>();
                AppointmentUserDto appointmentUserDto = new AppointmentUserDto();
                appointmentUserDto.setLoginUserId(loginContext.getLoginId());
                appointmentUserDto.setWorkHours(8);
                //
                appointmentUserDto.setWorkGrpName("BSB-Inspect");
                appointmentUserDtos.add(appointmentUserDto);
                //set system key
                appointmentDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
                //get Start date and End date when application no date
                if (appointmentDto.getStartDate() == null && appointmentDto.getEndDate() == null) {
                    //todo get startDate and endDate from service table,no table now,set time as below
                    Calendar startCal = Calendar.getInstance();
                    //todo the startCal is submitDt + date
                    startCal.setTime(new Date());
                    startCal.add(Calendar.DAY_OF_YEAR, 7);
                    appointmentDto.setStartDate(DateUtil.convertToString(startCal.getTime(), null));

                    Calendar endCal = Calendar.getInstance();
                    //todo the endCal is expiryDt + (-date)
                    endCal.setTime(startCal.getTime());
                    endCal.add(Calendar.DATE, 2);
                    appointmentDto.setEndDate(DateUtil.convertToString(endCal.getTime(), null));
                }
//                    List<String> premCorrIds = apptInspectionDateDto.getRefNo();
                //todo no service in application
//               Map<String, String> appIdServiceIdMap = getappIdServiceIdMapByRefNo(premCorrIds);

//                    List<String> serviceIds = new ArrayList<>(appIdServiceIdMap.size());
                //get service data for get Date period
//                    appointmentDto = getServiceDataForDatePeriod(appointmentDto, appType, appIdServiceIdMap, serviceIds);
                //get inspection date
                //todo get task dto list by refNo
                List<TaskDto> taskDtoList = apptInspectionDateDto.getTaskDtos();
                //set manHours and inspectors
                // todo use this method when ref_no is not null
                getManHoursInspectors(appointmentUserDtos, taskDtoList, null, appType);
                //If one person is doing multiple services at the same time, The superposition of time
                appointmentUserDtos = getOnePersonBySomeService(appointmentUserDtos);
                appointmentDto.setUsers(appointmentUserDtos);
                apptInspectionDateDto.setAppointmentDto(appointmentDto);
                boolean dateFlag = getStartEndDateFlag(appointmentDto);
                if (dateFlag && newInspDateFlag) {
                    //set Inspection date show, flag,
                    getNewInspDateData(apptInspectionDateDto, appointmentDto, map, request, taskDtoList);
                } else if (!newInspDateFlag) {
                    //get Inspection date Draft
                    setInspDateDraftData(apptInspectionDateDto, appPremInspApptDraftDtos);
                    apptInspectionDateDto.setSysInspDateFlag(AppConsts.TRUE);
                    apptInspectionDateDto.setSysSpecDateFlag(AppConsts.TRUE);
                    map.put(BUTTON_FLAG, AppConsts.TRUE);
                    map.put(SPEC_BUTTON_FLAG, AppConsts.TRUE);
                    map.put(INSP_DATE_LIST, apptInspectionDateDto.getInspectionDate());
                } else {
                    map.put(BUTTON_FLAG, AppConsts.FALSE);
                    map.put(SPEC_BUTTON_FLAG, AppConsts.TRUE);
                    map.put(INSP_DATE_LIST, null);
                }
                //specific date dto
                AppointmentDto specificApptDto = new AppointmentDto();
                specificApptDto.setSubmitDt(appointmentDto.getSubmitDt());
                specificApptDto.setUsers(appointmentDto.getUsers());
                specificApptDto.setSysClientKey(appointmentDto.getSysClientKey());
                specificApptDto.setServiceIds(appointmentDto.getServiceIds());
                specificApptDto.setServiceId(appointmentDto.getServiceId());
                apptInspectionDateDto.setSpecificApptDto(specificApptDto);
                ParamUtil.setSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DTO, apptInspectionDateDto);
            }
        }
        return map;
    }

    private void setInspDateDraftData(ApptInspectionDateDto apptInspectionDateDto, List<InspectionAppointmentDraftDto> appPremInspApptDraftDtos) {
        if (!IaisCommonUtils.isEmpty(appPremInspApptDraftDtos)) {
            log.info(StringUtil.changeForLog("Inspection Scheduling appPremInspApptDraftDtos Size ====" + appPremInspApptDraftDtos.size()));
            Map<String, List<ApptUserCalendarDto>> inspectionDateMap = new LinkedHashMap<>(4);
            for (InspectionAppointmentDraftDto appPremInspApptDraftDto : appPremInspApptDraftDtos) {
                if (appPremInspApptDraftDto != null) {
                    String apptRefNo = appPremInspApptDraftDto.getApptRefNo();
                    List<ApptUserCalendarDto> apptUserCalendarDtos = inspectionDateMap.computeIfPresent(apptRefNo, (s, apptUserCalendarDtos1) -> new ArrayList<>(1));
                    if (CollectionUtils.isEmpty(apptUserCalendarDtos)) {
                        apptUserCalendarDtos = new ArrayList<>();
                    }
                    ApptUserCalendarDto apptUserCalendarDto = new ApptUserCalendarDto();
                    List<Date> startSlot = new ArrayList<>(1);
                    List<Date> endSlot = new ArrayList<>(1);
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
            setInspDateDraftStrShow(appPremInspApptDraftDtos, apptInspectionDateDto);
            apptInspectionDateDto.setInspectionDateMap(inspectionDateMap);
        }
    }

    private void setInspDateDraftStrShow(List<InspectionAppointmentDraftDto> appPremInspApptDraftDtos, ApptInspectionDateDto apptInspectionDateDto) {
        InspectionAppointmentDraftDto appPremInspApptDraftDto = appPremInspApptDraftDtos.get(0);
        Date inspStartDate = appPremInspApptDraftDto.getStartDate();
        Date inspEndDate = appPremInspApptDraftDto.getEndDate();
        //date to str
        String inspStartDateStr = apptDateToStringShow(inspStartDate);
        String inspEndDateStr = apptDateToStringShow(inspEndDate);
        String inspectionDate = inspStartDateStr + " - " + inspEndDateStr;
        List<String> inspectionDates = new ArrayList<>(1);
        inspectionDates.add(inspectionDate);
        apptInspectionDateDto.setInspectionDate(inspectionDates);
    }

    private void setSomeDataForVali(ApptInspectionDateDto apptInspectionDateDto, Map<String, Object> map, String appType) {
//        TaskDto taskDto = apptInspectionDateDto.getTaskDto();
//        //get Applicant set start date and end date from appGroup
//        AppointmentDto specificApptDto = inspectionTaskClient.getApptStartEndDateByAppCorrId(taskDto.getRefNo()).getEntity();
//        specificApptDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
//        List<String> premCorrIds = apptInspectionDateDto.getRefNo();
//        Map<String, String> corrIdServiceIdMap = getappIdServiceIdMapByApplicationIds(premCorrIds);
//        List<String> serviceIds = IaisCommonUtils.genNewArrayList();
//        for (Map.Entry<String, String> mapDate : corrIdServiceIdMap.entrySet()) {
//            if(!StringUtil.isEmpty(mapDate.getValue())){
//                serviceIds.add(mapDate.getValue());
//            }
//        }
//        //get Start date and End date when group no date
//        if (specificApptDto.getStartDate() == null && specificApptDto.getEndDate() == null) {
//            specificApptDto.setServiceIds(serviceIds);
//            specificApptDto.setSvcIdLicDtMap(null);
//            specificApptDto = hcsaConfigClient.getApptStartEndDateByService(specificApptDto).getEntity();
//        }
//        //set data to validate
//        List<TaskDto> taskDtoList = apptInspectionDateDto.getTaskDtos();
//        List<AppointmentUserDto> appointmentUserDtos = IaisCommonUtils.genNewArrayList();
//        for (TaskDto tDto : taskDtoList) {
//            AppointmentUserDto appointmentUserDto = new AppointmentUserDto();
//            OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(tDto.getUserId()).getEntity();
//            appointmentUserDto.setLoginUserId(orgUserDto.getUserId());
//            String workGroupId = tDto.getWkGrpId();
//            WorkingGroupDto workingGroupDto = organizationClient.getWrkGrpById(workGroupId).getEntity();
//            appointmentUserDto.setWorkGrpName(workingGroupDto.getGroupName());
//            //get service id by task refno
//            String serviceId = corrIdServiceIdMap.get(tDto.getRefNo());
//            //get manHours by service and stage
//            ApptAppInfoShowDto apptAppInfoShowDto = new ApptAppInfoShowDto();
//            apptAppInfoShowDto.setApplicationType(appType);
//            apptAppInfoShowDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
//            apptAppInfoShowDto.setServiceId(serviceId);
//            int manHours = getServiceManHours(tDto.getRefNo(), apptAppInfoShowDto);
//            //Divide the time according to the number of people
//            List<TaskDto> sizeTask = organizationClient.getCurrTaskByRefNo(tDto.getRefNo()).getEntity();
//            double hours = manHours;
//            double peopleCount = sizeTask.size();
//            int peopleHours = (int) Math.ceil(hours/peopleCount);
//            appointmentUserDto.setWorkHours(peopleHours);
//            appointmentUserDtos.add(appointmentUserDto);
//        }
//        //If one person is doing multiple services at the same time, The superposition of time
//        appointmentUserDtos = getOnePersonBySomeService(appointmentUserDtos);
//        specificApptDto.setUsers(appointmentUserDtos);
//        apptInspectionDateDto.setSpecificApptDto(specificApptDto);
//        apptInspectionDateDto.setSysSpecDateFlag(AppConsts.TRUE);
//        map.put(SPEC_BUTTON_FLAG, AppConsts.TRUE);
    }

    private void getNewInspDateData(ApptInspectionDateDto apptInspectionDateDto, AppointmentDto appointmentDto, Map<String, Object> map,
                                    HttpServletRequest request, List<TaskDto> taskDtoList) {
        try {
            appointmentDto.setResultNum(1);
            FeignResponseEntity<List<ApptRequestDto>> result = appointmentClient.getUserCalendarByUserId(appointmentDto);
            Map<String, Collection<String>> headers = result.getHeaders();
            //Has it been blown up
            if (headers != null && StringUtils.isEmpty(headers.get("fusing"))) {
                List<ApptRequestDto> apptRequestDtos = result.getEntity();
                if (!CollectionUtils.isEmpty(apptRequestDtos)) {
                    Map<String, List<ApptUserCalendarDto>> inspectionDateMap = new LinkedHashMap<>(apptRequestDtos.size());
                    for (ApptRequestDto apptRequestDto : apptRequestDtos) {
                        inspectionDateMap.put(apptRequestDto.getApptRefNo(), apptRequestDto.getUserClandars());
                    }
                    apptInspectionDateDto.setSysInspDateFlag(AppConsts.TRUE);
                    apptInspectionDateDto.setSysSpecDateFlag(AppConsts.TRUE);
                    getShowTimeStringList(inspectionDateMap, apptInspectionDateDto);
                    //save inspection draft
                    List<InspectionAppointmentDraftDto> appPremInspApptDraftDtos = saveInspectionDateDraft(apptRequestDtos, taskDtoList);
                    ParamUtil.setSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DRAFT_DTO, (Serializable) appPremInspApptDraftDtos);
                    map.put(BUTTON_FLAG, AppConsts.TRUE);
                    map.put(SPEC_BUTTON_FLAG, AppConsts.TRUE);
                    map.put(INSP_DATE_LIST, apptInspectionDateDto.getInspectionDate());
                } else {
                    map.put(BUTTON_FLAG, AppConsts.FALSE);
                    map.put(SPEC_BUTTON_FLAG, AppConsts.TRUE);
                    map.put(INSP_DATE_LIST, null);
                    apptInspectionDateDto.setSysSpecDateFlag(AppConsts.TRUE);
                }
            } else {
                map.put(BUTTON_FLAG, AppConsts.FALSE);
                map.put(SPEC_BUTTON_FLAG, AppConsts.TRUE);
                map.put(INSP_DATE_LIST, null);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    private List<InspectionAppointmentDraftDto> saveInspectionDateDraft(List<ApptRequestDto> apptRequestDtos, List<TaskDto> taskDtoList) {
        List<InspectionAppointmentDraftDto> appPremInspApptDraftDtos = new ArrayList<>();
        if (!IaisCommonUtils.isEmpty(taskDtoList) && !IaisCommonUtils.isEmpty(apptRequestDtos)) {
            appPremInspApptDraftDtos = new ArrayList<>(taskDtoList.size());
            List<String> appNoList = new ArrayList<>(taskDtoList.size());
            for (TaskDto taskDto : taskDtoList) {
                for (ApptRequestDto apptRequestDto : apptRequestDtos) {
                    if (taskDto != null && apptRequestDto != null) {
                        //get date
                        int endTimeSize = apptRequestDto.getUserClandars().get(0).getEndSlot().size();
                        Date inspStartDate = apptRequestDto.getUserClandars().get(0).getStartSlot().get(0);
                        Date inspEndDate = apptRequestDto.getUserClandars().get(0).getEndSlot().get(endTimeSize - 1);
                        //set data
                        if (!appNoList.contains(taskDto.getApplicationNo())) {
                            appNoList.add(taskDto.getApplicationNo());
                            InspectionAppointmentDraftDto draftDto = new InspectionAppointmentDraftDto();
                            draftDto.setApplicationNo(taskDto.getApplicationNo());
                            draftDto.setApptRefNo(apptRequestDto.getApptRefNo());
                            draftDto.setStartDate(inspStartDate);
                            draftDto.setEndDate(inspEndDate);
                            draftDto.setUserId(taskDto.getUserId());
                            draftDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                            appPremInspApptDraftDtos.add(draftDto);
                        }
                    }
                }
            }
            appPremInspApptDraftDtos = bsbAppointmentClient.saveAppointmentDraft(appPremInspApptDraftDtos).getEntity();
        }
        return appPremInspApptDraftDtos;
    }

    private AppointmentDto getServiceDataForDatePeriod(AppointmentDto appointmentDto, String appType, Map<String, String> corrIdServiceIdMap, List<String> serviceIds) {
        if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)) {
            Map<String, Date> svcIdLicDtMap = Maps.newHashMapWithExpectedSize(corrIdServiceIdMap.size());
            for (Map.Entry<String, String> mapDate : corrIdServiceIdMap.entrySet()) {
                if (StringUtils.hasLength(mapDate.getValue())) {
                    setSvcIdLicDtMapByApp(mapDate.getKey(), mapDate.getValue(), svcIdLicDtMap);
                    serviceIds.add(mapDate.getValue());
                }
            }
            appointmentDto.setSvcIdLicDtMap(svcIdLicDtMap);
        } else {
            for (Map.Entry<String, String> mapDate : corrIdServiceIdMap.entrySet()) {
                if (StringUtils.hasLength(mapDate.getValue())) {
                    serviceIds.add(mapDate.getValue());
                }
            }
            appointmentDto.setSvcIdLicDtMap(null);
        }
        return appointmentDto;
    }

    private void getManHoursInspectors(List<AppointmentUserDto> appointmentUserDtos, List<TaskDto> taskDtoList,
                                       Map<String, String> corrIdServiceIdMap, String appType) {
        for (TaskDto tDto : taskDtoList) {
            //todo the if will be deleted future
            if (StringUtils.hasLength(tDto.getRefNo())) {

                AppointmentUserDto appointmentUserDto = new AppointmentUserDto();
                OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(tDto.getUserId()).getEntity();
                appointmentUserDto.setLoginUserId(orgUserDto.getUserId());
                //todo working group
                String workGroupId = tDto.getWkGrpId();
//            WorkingGroupDto workingGroupDto = organizationClient.getWrkGrpById(workGroupId).getEntity();
//            appointmentUserDto.setWorkGrpName(workingGroupDto.getGroupName());
                appointmentUserDto.setWorkGrpName("BSB-Inspect");
                //get service id by task refno
//            String serviceId = corrIdServiceIdMap.get(tDto.getRefNo());
//            //get manHours by service and stage
//            ApptAppInfoShowDto apptAppInfoShowDto = new ApptAppInfoShowDto();
//            apptAppInfoShowDto.setApplicationType(appType);
//            apptAppInfoShowDto.setStageId(HcsaConsts.ROUTING_STAGE_INS);
//            apptAppInfoShowDto.setServiceId(serviceId);
                //todo get manHours
//            int manHours = getServiceManHours(tDto.getRefNo(), apptAppInfoShowDto);
                int manHours = 2;
                //Divide the time according to the number of people
                //get the tasks where status in (pending,read)
                List<TaskDto> sizeTask = organizationClient.getCurrTaskByRefNo(tDto.getRefNo()).getEntity();
                double peopleCount = sizeTask.size();
                int peopleHours = (int) Math.ceil((double) manHours / peopleCount);
                appointmentUserDto.setWorkHours(peopleHours);
                appointmentUserDtos.add(appointmentUserDto);
            }
        }
    }

    private boolean getNewInspDateFlag(List<InspectionAppointmentDraftDto> appPremInspApptDraftDtos) {
        boolean newInspDateFlag = true;
        if (!CollectionUtils.isEmpty(appPremInspApptDraftDtos)) {
            newInspDateFlag = false;
        }
        return newInspDateFlag;
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
            if (StringUtils.hasLength(startDateStr)) {
                startDate = Formatter.parseDateTime(startDateStr, AppConsts.DEFAULT_DATE_FORMAT);
            }
            if (StringUtils.hasLength(endDateStr)) {
                endDate = Formatter.parseDateTime(endDateStr, AppConsts.DEFAULT_DATE_FORMAT);
            }
        } catch (ParseException e) {
            log.info("Appt Date Error!!!!!");
            log.error(e.getMessage(), e);
        }
        if (endDate != null) {
            if (endDate.before(today)) {
                return false;
            } else {
                if (startDate == null) {
                    return false;
                } else {
                    if (startDate.before(today)) {
                        startDate = new Date();
                        appointmentDto.setStartDate(Formatter.formatDateTime(startDate, AppConsts.DEFAULT_DATE_TIME_FORMAT));
                    }
                }
            }
        } else {
            if (startDate == null) {
                return false;
            } else {
                if (startDate.before(today)) {
                    startDate = new Date();
                    appointmentDto.setStartDate(Formatter.formatDateTime(startDate, AppConsts.DEFAULT_DATE_TIME_FORMAT));
                }
            }
        }
        return true;
    }

    private void setSvcIdLicDtMapByApp(String appPremCorrId, String serviceId, Map<String, Date> svcIdLicDtMap) {
        //error method
//        Date date = bsbAppointmentClient.getExpiryDate(appPremCorrId).getEntity();
        Date date = new Date();
        svcIdLicDtMap.put(serviceId, date);
    }

    private List<AppointmentUserDto> getOnePersonBySomeService(List<AppointmentUserDto> appointmentUserDtos) {
        List<AppointmentUserDto> appointmentUserDtoList = null;
        if (!IaisCommonUtils.isEmpty(appointmentUserDtos)) {
            appointmentUserDtoList = new ArrayList<>(appointmentUserDtos.size());
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

    private void filterRepetitiveUser(AppointmentUserDto appointmentUserDto, List<AppointmentUserDto> appointmentUserDtoList) {
        List<AppointmentUserDto> appointmentUserDtos = new ArrayList<>(appointmentUserDtoList.size());
        boolean sameUserFlag = false;
        for (AppointmentUserDto appointmentUserDto1 : appointmentUserDtoList) {
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
        if (!sameUserFlag) {
            appointmentUserDtos.add(appointmentUserDto);
        }
        if (!CollectionUtils.isEmpty(appointmentUserDtos)) {
            for (AppointmentUserDto auDto : appointmentUserDtos) {
                if (auDto != null) {
                    appointmentUserDtoList.add(auDto);
                }
            }
        }
    }

    private int getServiceManHours(String refNo, ApptAppInfoShowDto apptAppInfoShowDto) {
        int manHours = 0;
//        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(refNo, InspectionConstants.RECOM_TYPE_INSP_MAN_HOUR).getEntity();
//        if(appPremisesRecommendationDto != null){
//            String hours = appPremisesRecommendationDto.getRecomDecision();
//            if(StringUtils.hasLength(hours)){
//                manHours = Integer.parseInt(hours);
//            } else {
//                manHours = hcsaConfigClient.getManHour(apptAppInfoShowDto).getEntity();
//            }
//        } else {
//            manHours = hcsaConfigClient.getManHour(apptAppInfoShowDto).getEntity();
//        }
        return manHours;
    }

    private void getShowTimeStringList(Map<String, List<ApptUserCalendarDto>> inspectionDateMap, ApptInspectionDateDto apptInspectionDateDto) {
        if (inspectionDateMap != null) {
            List<String> inspectionDates = new ArrayList<>(inspectionDateMap.size());
            for (Map.Entry<String, List<ApptUserCalendarDto>> inspDateMap : inspectionDateMap.entrySet()) {
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
    }

    private String apptDateToStringShow(Date date) {
        String specificDate = Formatter.formatDateTime(date, "dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int minutes = cal.get(Calendar.MINUTE);
        if (minutes > 0) {
            cal.add(Calendar.HOUR_OF_DAY, 1);
        }
        int curHour24 = cal.get(Calendar.HOUR_OF_DAY);
        String hoursShow = "";
        if (curHour24 < 10) {
            hoursShow = "0";
        }
        specificDate = specificDate + " " + hoursShow + curHour24 + ":00";
        return specificDate;
    }

    private Map<String, String> getappIdServiceIdMapByRefNo(List<String> refNos) {
        //error method
//        return bsbAppointmentClient.getServiceIdsByRefNo(refNos).getEntity();
        return null;
    }
}