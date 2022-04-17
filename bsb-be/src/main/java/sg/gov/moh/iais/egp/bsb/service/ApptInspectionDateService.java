package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.*;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.AppointmentClient;
import sg.gov.moh.iais.egp.bsb.client.BsbAppointmentClient;
import sg.gov.moh.iais.egp.bsb.client.OrganizationClient;
import sg.gov.moh.iais.egp.bsb.constant.AppConstants;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.AppointmentReviewDataDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.SaveAppointmentDataDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.doreschedule.ApptAppInfoDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.doreschedule.OfficerRescheduleDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.AppInspectorCorrelationDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.ApplicationDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionAppointmentDraftDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionAppointmentDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.util.DateUtil;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static sg.gov.moh.iais.egp.bsb.constant.AppointmentConstants.*;

@Service
@Slf4j
public class ApptInspectionDateService {
    private final BsbAppointmentClient bsbAppointmentClient;
    private final AppointmentClient appointmentClient;
    private final OrganizationClient organizationClient;

    public ApptInspectionDateService(BsbAppointmentClient bsbAppointmentClient, AppointmentClient appointmentClient, OrganizationClient organizationClient) {
        this.bsbAppointmentClient = bsbAppointmentClient;
        this.appointmentClient = appointmentClient;
        this.organizationClient = organizationClient;
    }

    public List<SelectOption> getInspectionDateStartHours() {
        List<SelectOption> hourOption = new ArrayList<>(8);
        SelectOption so1 = new SelectOption("1", "09:00");
        SelectOption so2 = new SelectOption("2", "10:00");
        SelectOption so3 = new SelectOption("3", "11:00");
        SelectOption so4 = new SelectOption("4", "12:00");
        SelectOption so5 = new SelectOption("5", "14:00");
        SelectOption so6 = new SelectOption("6", "15:00");
        SelectOption so7 = new SelectOption("7", "16:00");
        SelectOption so8 = new SelectOption("8", "17:00");
        hourOption.add(so1);
        hourOption.add(so2);
        hourOption.add(so3);
        hourOption.add(so4);
        hourOption.add(so5);
        hourOption.add(so6);
        hourOption.add(so7);
        hourOption.add(so8);
        return hourOption;
    }

    public List<SelectOption> getInspectionDateEndHours() {
        List<SelectOption> hourOption = new ArrayList<>(8);
        SelectOption so1 = new SelectOption("1", "10:00");
        SelectOption so2 = new SelectOption("2", "11:00");
        SelectOption so3 = new SelectOption("3", "12:00");
        SelectOption so4 = new SelectOption("4", "13:00");
        SelectOption so5 = new SelectOption("5", "15:00");
        SelectOption so6 = new SelectOption("6", "16:00");
        SelectOption so7 = new SelectOption("7", "17:00");
        SelectOption so8 = new SelectOption("8", "18:00");
        hourOption.add(so1);
        hourOption.add(so2);
        hourOption.add(so3);
        hourOption.add(so4);
        hourOption.add(so5);
        hourOption.add(so6);
        hourOption.add(so7);
        hourOption.add(so8);
        return hourOption;
    }

    public AppointmentDto getValidateValue(AppointmentReviewDataDto dto, HttpServletRequest request) {
        ApptInspectionDateDto apptInspectionDateDto = (ApptInspectionDateDto) ParamUtil.getSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DTO);
        AppointmentDto specificApptDto = apptInspectionDateDto.getSpecificApptDto();

        String specificStartDate = ParamUtil.getDate(request, KEY_SPECIFY_START_DATE);
        String specificEndDate = ParamUtil.getDate(request, KEY_SPECIFY_END_DATE);
        String startHour = ParamUtil.getRequestString(request, KEY_SPECIFY_START_HOUR);
        String endHour = ParamUtil.getRequestString(request, KEY_SPECIFY_END_HOUR);
        List<SelectOption> startHoursOption = (List<SelectOption>) ParamUtil.getSessionAttr(request, KEY_START_HOURS_OPTION);
        List<SelectOption> endHoursOption = (List<SelectOption>) ParamUtil.getSessionAttr(request, KEY_END_HOURS_OPTION);

        Map<String, SelectOption> startHoursOptionMap = sg.gov.moh.iais.egp.bsb.util.CollectionUtils.uniqueIndexMap(startHoursOption, SelectOption::getValue);
        Map<String, SelectOption> endHoursOptionMap = sg.gov.moh.iais.egp.bsb.util.CollectionUtils.uniqueIndexMap(endHoursOption, SelectOption::getValue);
        if (containValueInList(startHour, startHoursOption)) {
            dto.setSpecifyStartHour(startHour);
        } else {
            dto.setSpecifyStartHour(null);
        }
        if (containValueInList(endHour, endHoursOption)) {
            dto.setSpecifyEndHour(endHour);
        } else {
            dto.setSpecifyEndHour(null);
        }
        if (StringUtils.hasLength(specificStartDate)) {
            dto.setSpecifyStartDate(specificStartDate);
        }
        if (StringUtils.hasLength(specificEndDate)) {
            dto.setSpecifyEndDate(specificEndDate);
        }
        String startDate = getSpecificDate(specificStartDate, startHoursOptionMap, startHour);
        String endDate = getSpecificDate(specificEndDate, endHoursOptionMap, endHour);
        if (startDate != null) {
            specificApptDto.setStartDate(startDate);
        }
        if (endDate != null) {
            specificApptDto.setEndDate(endDate);
        }
        apptInspectionDateDto.setSpecificApptDto(specificApptDto);
        ParamUtil.setSessionAttr(request, APPOINTMENT_INSPECTION_DATE_DTO, apptInspectionDateDto);
        return specificApptDto;
    }

    public String getSpecificDate(String specificDate1, Map<String, SelectOption> optionMap, String hours) {
        if (specificDate1 != null) {
            StringBuilder subDate = new StringBuilder();
            subDate.append(specificDate1);
            if (StringUtils.hasLength(hours)) {
                SelectOption selectOption = optionMap.get(hours);
                subDate.append(' ').append(selectOption.getText()).append(":00");
            } else {
                subDate.append(' ').append(":00:00");
            }
            return subDate.toString();
        }
        return null;
    }

    public boolean containValueInList(String str, List<SelectOption> optionList) {
        if (StringUtils.hasLength(str) && !CollectionUtils.isEmpty(optionList)) {
            for (SelectOption so : optionList) {
                if (str.equals(so.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Map<String, String> validateDateFromUserCalendar(AppointmentDto specificApptDto) {
        Map<String, String> errMap = Maps.newHashMapWithExpectedSize(1);
        SimpleDateFormat sdf3 = new SimpleDateFormat(AppConstants.DEFAULT_DATE_TIME_FORMAT);
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = sdf3.parse(specificApptDto.getStartDate());
        } catch (ParseException e) {
            log.info("startDate Conversion failure");
        }
        try {
            endDate = sdf3.parse(specificApptDto.getStartDate());
        } catch (ParseException e) {
            log.info("endDate Conversion failure");
        }
        //This cannot be empty
        assert endDate != null;
        if (endDate.before(startDate)) {
            errMap.put(KEY_ERROR_SPECIFIC_DATE, ERROR_MSG_END_DATE_BEFORE_START_DATE);
            return errMap;
        }

        try {
            appointmentClient.validateUserCalendar(specificApptDto).getStatusCode();
        } catch (Exception e) {
            errMap.put(KEY_ERROR_SPECIFIC_DATE, ERROR_MSG_COLLISION_DATE);
        }
        return errMap;
    }

    public String ensureValResult(String actionValue, ValidationResultDto validationResultDto, HttpServletRequest request, AppointmentDto specificApptDto) {
        String actionType;
        if ("back".equals(actionValue)) {
            actionType = "back";
        } else {
            if (!validationResultDto.isPass()) {
                ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
                actionType = "prepare";
            } else {
                actionType = "next";
                Map<String, String> errMap = validateDateFromUserCalendar(specificApptDto);
                if (!CollectionUtils.isEmpty(errMap)) {
                    validationResultDto.setPass(false);
                    validationResultDto.setErrorMap((HashMap<String, String>) errMap);
                    ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
                    actionType = "prepare";
                }
            }
        }
        return actionType;
    }

    private void cancelOrConfirmApptDate(ApptCalendarStatusDto apptCalendarStatusDto) {
        appointmentClient.updateUserCalendarStatus(apptCalendarStatusDto);
    }

    public void saveSystemInspectionDate(ApptInspectionDateDto apptInspectionDateDto, String applicationId) {
        Map<String, List<ApptUserCalendarDto>> inspectionDateMap = apptInspectionDateDto.getInspectionDateMap();
        List<InspectionAppointmentDto> appPremisesInspecApptDtoList = new ArrayList<>(inspectionDateMap.size());
        //save AppPremisesInspecApptDto
        List<String> confirmRefNo = new ArrayList<>();
        for (Map.Entry<String, List<ApptUserCalendarDto>> inspDateMap : inspectionDateMap.entrySet()) {
            String apptRefNo = inspDateMap.getKey();
            ResponseDto<InspectionAppointmentDto> responseDto = bsbAppointmentClient.getInspectionAppointmentByAppId(applicationId);
            InspectionAppointmentDto inspectionAppointmentDto;
            if (responseDto.ok()) {
                inspectionAppointmentDto = responseDto.getEntity();
            } else {
                inspectionAppointmentDto = new InspectionAppointmentDto();
            }
            ApplicationDto applicationDto = new ApplicationDto();
            applicationDto.setId(applicationId);
            inspectionAppointmentDto.setApplication(applicationDto);
            inspectionAppointmentDto.setApptRefNo(apptRefNo);
            inspectionAppointmentDto.setSpecInsDate(null);
            inspectionAppointmentDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);

            List<ApptUserCalendarDto> userCalendarDtos = inspDateMap.getValue();
            ApptUserCalendarDto userCalendarDto = userCalendarDtos.get(0);
            if (userCalendarDto.getStartSlot().get(0) != null) {
                inspectionAppointmentDto.setStartDate(userCalendarDto.getStartSlot().get(0));
            }
            if (userCalendarDto.getEndSlot().get(0) != null) {
                inspectionAppointmentDto.setEndDate(userCalendarDto.getEndSlot().get(0));
            }
            appPremisesInspecApptDtoList.add(inspectionAppointmentDto);
            confirmRefNo.add(apptRefNo);
        }
        List<TaskDto> taskDtos = apptInspectionDateDto.getTaskDtos();
        //create application and inspectors correlation data
        List<AppInspectorCorrelationDto> appInspectorCorrelationDtos = fillAppInspectorCorrelationDtos(taskDtos);

        SaveAppointmentDataDto saveAppointmentDataDto = new SaveAppointmentDataDto();
        saveAppointmentDataDto.setAppointmentDtos(appPremisesInspecApptDtoList);
        saveAppointmentDataDto.setApptRefNos(confirmRefNo);
        saveAppointmentDataDto.setTaskDtos(taskDtos);
        saveAppointmentDataDto.setAppInspCorrelationDtos(appInspectorCorrelationDtos);
        bsbAppointmentClient.saveAppointment(saveAppointmentDataDto);

        ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
        apptCalendarStatusDto.setConfirmRefNums(confirmRefNo);
        apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        cancelOrConfirmApptDate(apptCalendarStatusDto);
        //todo email
    }


    public void saveUserSpecificDate(ApptInspectionDateDto apptInspectionDateDto, String applicationId) {
        List<InspectionAppointmentDto> appPremisesInspecApptDtoList = new ArrayList<>(1);
        //get AppointmentDto
        AppointmentDto appointmentDtoSave = apptInspectionDateDto.getSpecificApptDto();
        //save and return apptRefNo
        List<String> confirmRefNo = new ArrayList<>(1);
        String apptRefNo = appointmentClient.saveManualUserCalendar(appointmentDtoSave).getEntity();
        confirmRefNo.add(apptRefNo);
        //save data
        ResponseDto<InspectionAppointmentDto> responseDto = bsbAppointmentClient.getInspectionAppointmentByAppId(applicationId);
        InspectionAppointmentDto inspectionAppointmentDto;
        if (responseDto.ok()) {
            inspectionAppointmentDto = responseDto.getEntity();
        } else {
            inspectionAppointmentDto = new InspectionAppointmentDto();
        }
        ApplicationDto applicationDto = new ApplicationDto();
        applicationDto.setId(applicationId);
        inspectionAppointmentDto.setApplication(applicationDto);
        inspectionAppointmentDto.setApptRefNo(apptRefNo);
        inspectionAppointmentDto.setSpecInsDate(null);
        inspectionAppointmentDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);

        if (appointmentDtoSave.getStartDate() != null) {
            try {
                inspectionAppointmentDto.setStartDate(Formatter.parseDateTime(appointmentDtoSave.getStartDate(), AppConsts.DEFAULT_DATE_TIME_FORMAT));
            } catch (ParseException e) {
                log.error(e.getMessage(), e);
            }
        }
        if (appointmentDtoSave.getEndDate() != null) {
            try {
                inspectionAppointmentDto.setEndDate(Formatter.parseDateTime(appointmentDtoSave.getEndDate(), AppConsts.DEFAULT_DATE_TIME_FORMAT));
            } catch (ParseException e) {
                log.error(e.getMessage(), e);
            }
        }
        appPremisesInspecApptDtoList.add(inspectionAppointmentDto);

        Map<String, List<ApptUserCalendarDto>> inspectionDateMap = apptInspectionDateDto.getInspectionDateMap();
        List<String> cancelRefNo = new ArrayList<>();
        if (inspectionDateMap != null) {
            for (Map.Entry<String, List<ApptUserCalendarDto>> inspDateMap : inspectionDateMap.entrySet()) {
                String refNo = inspDateMap.getKey();
                cancelRefNo.add(refNo);
            }
        }
        List<TaskDto> taskDtos = apptInspectionDateDto.getTaskDtos();
        //create application and inspectors correlation data
        List<AppInspectorCorrelationDto> appInspectorCorrelationDtos = fillAppInspectorCorrelationDtos(taskDtos);

        SaveAppointmentDataDto saveAppointmentDataDto = new SaveAppointmentDataDto();
        saveAppointmentDataDto.setAppointmentDtos(appPremisesInspecApptDtoList);
        saveAppointmentDataDto.setApptRefNos(cancelRefNo);
        saveAppointmentDataDto.setTaskDtos(taskDtos);
        saveAppointmentDataDto.setAppInspCorrelationDtos(appInspectorCorrelationDtos);
        bsbAppointmentClient.saveAppointment(saveAppointmentDataDto);

        //cancel or confirm appointment date
        ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
        apptCalendarStatusDto.setConfirmRefNums(confirmRefNo);
        apptCalendarStatusDto.setCancelRefNums(cancelRefNo);
        apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        cancelOrConfirmApptDate(apptCalendarStatusDto);
        //todo email
    }

    public List<AppInspectorCorrelationDto> fillAppInspectorCorrelationDtos(List<TaskDto> taskDtos) {
        List<AppInspectorCorrelationDto> appInspCorrelationDtos = new ArrayList<>(taskDtos.size());
        for (TaskDto taskDto : taskDtos) {
            AppInspectorCorrelationDto appInspectorCorrelationDto = new AppInspectorCorrelationDto();
            appInspectorCorrelationDto.setUserId(taskDto.getUserId());
            appInspectorCorrelationDto.setApplicationNo(taskDto.getApplicationNo());
            appInspectorCorrelationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            appInspCorrelationDtos.add(appInspectorCorrelationDto);
        }
        return appInspCorrelationDtos;
    }

    public void setStartEndDateNull(AppointmentDto appointmentDto) {
        String startDateStr = appointmentDto.getStartDate();
        //Compares user specified time with the current time
        if (StringUtils.hasLength(startDateStr)) {
            Date today = new Date();
            String todayStr = Formatter.formatDateTime(today, AppConsts.DEFAULT_DATE_FORMAT);
            try {
                today = Formatter.parseDateTime(todayStr, AppConsts.DEFAULT_DATE_FORMAT);
                Date startDate = Formatter.parseDateTime(startDateStr, AppConsts.DEFAULT_DATE_FORMAT);
                if (startDate.before(today)) {
                    appointmentDto.setStartDate(null);
                    appointmentDto.setEndDate(null);
                }
            } catch (ParseException e) {
                log.info("AppointmentDto: start date conversion Error!!!!!");
                log.error(e.getMessage(), e);
            }
        }
    }

    /**************************************************** reschedule ***************************************************/
    public OfficerRescheduleDto getReScheduleNewDateInfo(OfficerRescheduleDto dto) {
        //get start and end date from application4
        AppointmentDto appointmentDto = bsbAppointmentClient.getApptStartEndDateByAppId(dto.getAppId()).getEntity();
        //if start date before today,set start and end date null
        setStartEndDateNull(appointmentDto);
        //set start date and end date when application no date
        setStartDtAndEndDt(appointmentDto);
        //set display info dto
        dto = bsbAppointmentClient.getOfficerRescheduleReviewData(dto.getAppId()).getEntity();
        //get start date and end date by Service and appShow info
        List<String> appNoList = new ArrayList<>(1);
        appNoList.add(dto.getApptAppInfoDto().getAppNo());
        //set application no list
        appointmentDto.setAppNoList(appNoList);
        //set insp date draft
        List<InspectionAppointmentDraftDto> draftDtos = setInspApptDraftDto(dto.getApptAppInfoDto().getAppNo());
        boolean dateFlag = getStartEndDateFlag(appointmentDto);
        dto.setAppointmentDto(appointmentDto);
        if (CollectionUtils.isEmpty(draftDtos) && dateFlag) {
            //set app data to show ,and set userId correlation app No. to save
            setInfoByDateAndUserIdToSave(dto);
        } else if (!CollectionUtils.isEmpty(draftDtos)) {
            //set app data to show ,and set userId correlation app No.By Inspection Date Draft
            setInfoByDateAndUserIdByDraft(draftDtos, dto);
        }
        return dto;
    }

    public List<InspectionAppointmentDraftDto> setInspApptDraftDto(String appNo) {
        return bsbAppointmentClient.getActiveAppointmentDraftData(appNo).getEntity();
    }

    public boolean getStartEndDateFlag(AppointmentDto appointmentDto) {
        Date today = new Date();
        String todayStr = Formatter.formatDateTime(today, AppConsts.DEFAULT_DATE_FORMAT);
        String startDateStr = appointmentDto.getStartDate();
        String endDateStr = appointmentDto.getEndDate();
        Date startDate = null;
        Date endDate = null;
        try {
            today = Formatter.parseDateTime(todayStr, AppConsts.DEFAULT_DATE_FORMAT);
            if (!StringUtils.isEmpty(startDateStr)) {
                startDate = Formatter.parseDateTime(startDateStr, AppConsts.DEFAULT_DATE_FORMAT);
            }
            if (!StringUtils.isEmpty(endDateStr)) {
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

    public void setStartDtAndEndDt(AppointmentDto appointmentDto) {
        if (!StringUtils.hasLength(appointmentDto.getStartDate()) && !StringUtils.hasLength(appointmentDto.getEndDate())) {
            Calendar startCal = Calendar.getInstance();
            startCal.setTime(new Date());
            startCal.add(Calendar.DAY_OF_YEAR, 7);
            if (startCal.get(Calendar.HOUR_OF_DAY) > 9) {
                startCal.set(Calendar.HOUR_OF_DAY, 14);
            } else {
                startCal.set(Calendar.HOUR_OF_DAY, 9);
            }
            startCal.set(Calendar.MINUTE, 0);
            startCal.set(Calendar.SECOND, 0);
            startCal.set(Calendar.MILLISECOND, 0);
            appointmentDto.setStartDate(DateUtil.convertToString(startCal.getTime(), null));

            Calendar endCal = Calendar.getInstance();
            endCal.setTime(startCal.getTime());
            endCal.add(Calendar.DATE, 2);
            if (endCal.get(Calendar.HOUR_OF_DAY) > 12) {
                endCal.set(Calendar.HOUR_OF_DAY, 18);
            } else {
                endCal.set(Calendar.HOUR_OF_DAY, 13);
            }
            endCal.set(Calendar.MINUTE, 0);
            endCal.set(Calendar.SECOND, 0);
            endCal.set(Calendar.MILLISECOND, 0);
            appointmentDto.setEndDate(DateUtil.convertToString(endCal.getTime(), null));
        }
    }

    private void setInfoByDateAndUserIdByDraft(List<InspectionAppointmentDraftDto> draftDtoList, OfficerRescheduleDto dto) {
        ApptAppInfoDto apptAppInfoDto = dto.getApptAppInfoDto();
        for (InspectionAppointmentDraftDto draftDto : draftDtoList) {
            if (draftDto != null) {
                //set new inspection date string to show
                getShowDraftDateTimeStringList(draftDto, dto);
            }
        }
        setInsOfficers(draftDtoList, dto);
    }

    public void setInfoByDateAndUserIdToSave(OfficerRescheduleDto dto) {
        try {
            AppointmentDto appointmentDto = dto.getAppointmentDto();
            if (appointmentDto != null) {
                FeignResponseEntity<List<ApptRequestDto>> result = bsbAppointmentClient.getRescheduleNewDateFromBE(appointmentDto);
                Map<String, Collection<String>> headers = result.getHeaders();
                //Has it been blown up
                if (headers != null && StringUtils.isEmpty(headers.get("fusing"))) {
                    List<ApptRequestDto> apptRequestDtos = result.getEntity();
                    if (!CollectionUtils.isEmpty(apptRequestDtos)) {
                        for (ApptRequestDto apptRequestDto : apptRequestDtos) {
                            //set new inspection date string to show
                            getShowDateTimeStringList(apptRequestDto, dto);
                        }
                        //There's only one piece of data now
                        ApptRequestDto apptReDto = apptRequestDtos.get(0);
                        //set user with appNo and save inspection date draft
                        setUserWithAppNo(apptReDto, dto.getApptAppInfoDto());
                    } else {
                        dto.setAvailableDate(null);
                    }
                } else {
                    dto.setAvailableDate(null);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    public void setInsOfficers(List<InspectionAppointmentDraftDto> draftDtoList, OfficerRescheduleDto officerRescheduleDto) {
        if (!CollectionUtils.isEmpty(draftDtoList)) {
            List<String> userIdList = draftDtoList.stream().map(InspectionAppointmentDraftDto::getUserId).distinct().collect(Collectors.toList());
            List<OrgUserDto> userDtoList = organizationClient.retrieveOrgUserAccount(userIdList).getEntity();
            List<String> officers = userDtoList.stream().map(OrgUserDto::getDisplayName).collect(Collectors.toList());
            officerRescheduleDto.getApptAppInfoDto().setOfficers(officers);
        }
    }

    private void getShowDraftDateTimeStringList(InspectionAppointmentDraftDto draftDto, OfficerRescheduleDto officerRescheduleDto) {
        List<String> newInspDates = new ArrayList<>(1);
        if (draftDto != null) {
            String inspStartDate = apptDateToStringShow(draftDto.getStartDate());
            String inspEndDate = apptDateToStringShow(draftDto.getEndDate());
            String inspectionDate = inspStartDate + " - " + inspEndDate;
            newInspDates.add(inspectionDate);
        }
        officerRescheduleDto.setAvailableDate(newInspDates);
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

    private void getShowDateTimeStringList(ApptRequestDto apptRequestDto, OfficerRescheduleDto dto) {
        List<String> newInspDates;
        if (IaisCommonUtils.isEmpty(dto.getAvailableDate())) {
            newInspDates = new ArrayList<>();
        } else {
            newInspDates = dto.getAvailableDate();
        }
        if (apptRequestDto != null) {
            List<ApptUserCalendarDto> userClandars = apptRequestDto.getUserClandars();
            if (!CollectionUtils.isEmpty(userClandars)) {
                int endTimeSize = userClandars.get(0).getEndSlot().size();
                String inspStartDate = apptDateToStringShow(userClandars.get(0).getStartSlot().get(0));
                String inspEndDate = apptDateToStringShow(userClandars.get(0).getEndSlot().get(endTimeSize - 1));
                String inspectionDate = inspStartDate + " - " + inspEndDate;
                newInspDates.add(inspectionDate);
            }
        }
        dto.setAvailableDate(newInspDates);
    }

    private void setUserWithAppNo(ApptRequestDto apptReDto, ApptAppInfoDto apptAppInfoDto) {
        if (apptReDto != null && apptAppInfoDto != null) {
            List<String> apptRefNos = new ArrayList<>();
            String apptRefNo = apptReDto.getApptRefNo();
            apptRefNos.add(apptRefNo);
            List<ApptUserCalendarDto> userClandars = apptReDto.getUserClandars();
            //set user with App No.
            if (!CollectionUtils.isEmpty(userClandars)) {
                Date inspDate = userClandars.get(0).getStartSlot().get(0);
                int endTimeSize = userClandars.get(0).getEndSlot().size();
                Date inspEndDate = userClandars.get(0).getEndSlot().get(endTimeSize - 1);
                Map<String, List<String>> appNoUserLoginId = getAppNoUserLoginIdByUserClandars(userClandars);
                //set appointment ref NO.
                apptAppInfoDto.setApptRefNo(apptRefNos);
                List<String> userLoginIds = appNoUserLoginId.get(apptAppInfoDto.getAppNo());
                //sort for show
                Collections.sort(userLoginIds);
                //set user
                setUserIdByLoginIds(apptAppInfoDto, userLoginIds);
                apptAppInfoDto.setInspDate(inspDate);
                apptAppInfoDto.setInspEndDate(inspEndDate);
                //create inspection date Draft
                List<String> userIds = apptAppInfoDto.getUserIdList();
                saveInspecDateDraftList(userIds, apptReDto, apptAppInfoDto);
            }
        }
    }

    private Map<String, List<String>> getAppNoUserLoginIdByUserClandars(List<ApptUserCalendarDto> userClandars) {
        Map<String, List<String>> appNoUserLoginId = new HashMap<>();
        if (!CollectionUtils.isEmpty(userClandars)) {
            for (ApptUserCalendarDto apptUserCalendarDto : userClandars) {
                if (apptUserCalendarDto != null) {
                    String appNo = apptUserCalendarDto.getAppNo();
                    if (!StringUtils.isEmpty(appNo)) {
                        List<String> userLoginId = appNoUserLoginId.get(appNo);
                        if (IaisCommonUtils.isEmpty(userLoginId)) {
                            userLoginId = new ArrayList<>();
                        }
                        userLoginId.add(apptUserCalendarDto.getLoginUserId());
                        appNoUserLoginId.put(appNo, userLoginId);
                    }
                }
            }
        }
        return appNoUserLoginId;
    }

    private void setUserIdByLoginIds(ApptAppInfoDto apptAppInfoDto, List<String> userLoginIds) {
        if (!CollectionUtils.isEmpty(userLoginIds)) {
            List<String> userIdList = new ArrayList<>(userLoginIds.size());
            for (String userLoginId : userLoginIds) {
                if (!StringUtils.isEmpty(userLoginId)) {
                    OrgUserDto orgUserDto = organizationClient.retrieveOneOrgUserAccount(userLoginId).getEntity();
                    if (orgUserDto != null) {
                        userIdList.add(orgUserDto.getId());
                    }
                }
            }
            Set<String> userIdSet = new HashSet<>(userIdList);
            userIdList = new ArrayList<>(userIdSet);
            Collections.sort(userIdList);
            apptAppInfoDto.setUserIdList(userIdList);
        }
    }

    private List<InspectionAppointmentDraftDto> saveInspecDateDraftList(List<String> userIds, ApptRequestDto apptReDto, ApptAppInfoDto apptAppInfoDto) {
        List<InspectionAppointmentDraftDto> draftDtos = new ArrayList<>();
        //get date
        int endTimeSize = apptReDto.getUserClandars().get(0).getEndSlot().size();
        Date inspStartDate = apptReDto.getUserClandars().get(0).getStartSlot().get(0);
        Date inspEndDate = apptReDto.getUserClandars().get(0).getEndSlot().get(endTimeSize - 1);
        if (!CollectionUtils.isEmpty(userIds)) {
            for (String userId : userIds) {
                if (!StringUtils.isEmpty(userId)) {
                    //set data
                    InspectionAppointmentDraftDto draftDto = new InspectionAppointmentDraftDto();
                    draftDto.setApplicationNo(apptAppInfoDto.getAppNo());
                    draftDto.setApptRefNo(apptReDto.getApptRefNo());
                    draftDto.setStartDate(inspStartDate);
                    draftDto.setEndDate(inspEndDate);
                    draftDto.setUserId(userId);
                    draftDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                    draftDtos.add(draftDto);
                }
            }
            draftDtos = bsbAppointmentClient.saveAppointmentDraft(draftDtos).getEntity();
        }
        return draftDtos;
    }

    public AppointmentDto getRescheduleValidateValue(OfficerRescheduleDto rescheduleDto, HttpServletRequest request) {
        AppointmentReviewDataDto reviewDataDto = new AppointmentReviewDataDto();
        AppointmentDto specificApptDto = rescheduleDto.getAppointmentDto();

        String specificStartDate = ParamUtil.getDate(request, KEY_SPECIFY_START_DATE);
        String specificEndDate = ParamUtil.getDate(request, KEY_SPECIFY_END_DATE);
        String startHour = ParamUtil.getRequestString(request, KEY_SPECIFY_START_HOUR);
        String endHour = ParamUtil.getRequestString(request, KEY_SPECIFY_END_HOUR);
        List<SelectOption> startHoursOption = (List<SelectOption>) ParamUtil.getSessionAttr(request, KEY_START_HOURS_OPTION);
        List<SelectOption> endHoursOption = (List<SelectOption>) ParamUtil.getSessionAttr(request, KEY_END_HOURS_OPTION);

        Map<String, SelectOption> startHoursOptionMap = sg.gov.moh.iais.egp.bsb.util.CollectionUtils.uniqueIndexMap(startHoursOption, SelectOption::getValue);
        Map<String, SelectOption> endHoursOptionMap = sg.gov.moh.iais.egp.bsb.util.CollectionUtils.uniqueIndexMap(endHoursOption, SelectOption::getValue);
        if (containValueInList(startHour, startHoursOption)) {
            rescheduleDto.setSpecifyStartHour(startHour);
            reviewDataDto.setSpecifyStartHour(startHour);
        } else {
            rescheduleDto.setSpecifyStartHour(null);
            reviewDataDto.setSpecifyStartHour(null);
        }
        if (containValueInList(endHour, endHoursOption)) {
            rescheduleDto.setSpecifyEndHour(endHour);
            reviewDataDto.setSpecifyEndHour(endHour);
        } else {
            rescheduleDto.setSpecifyEndHour(null);
            reviewDataDto.setSpecifyEndHour(null);
        }
        if (StringUtils.hasLength(specificStartDate)) {
            rescheduleDto.setSpecifyStartDate(specificStartDate);
            reviewDataDto.setSpecifyStartDate(specificStartDate);
        }
        if (StringUtils.hasLength(specificEndDate)) {
            rescheduleDto.setSpecifyEndDate(specificEndDate);
            reviewDataDto.setSpecifyEndDate(specificEndDate);
        }
        String startDate = getSpecificDate(specificStartDate, startHoursOptionMap, startHour);
        String endDate = getSpecificDate(specificEndDate, endHoursOptionMap, endHour);
        if (startDate != null) {
            specificApptDto.setStartDate(startDate);
        }
        if (endDate != null) {
            specificApptDto.setEndDate(endDate);
        }
        ParamUtil.setSessionAttr(request, APPOINTMENT_REVIEW_DATA, reviewDataDto);
        return specificApptDto;
    }

    public void saveRescheduleSpecificDate(OfficerRescheduleDto dto) {
        List<InspectionAppointmentDto> inspectionAppointmentDtos = new ArrayList<>(1);
        //get AppointmentDto
        AppointmentDto appointmentDtoSave = dto.getAppointmentDto();
        //save and return apptRefNo
        List<String> confirmRefNos = new ArrayList<>(1);
        String apptRefNo = appointmentClient.saveManualUserCalendar(appointmentDtoSave).getEntity();
        confirmRefNos.add(apptRefNo);
        //save data

        ResponseDto<InspectionAppointmentDto> responseDto = bsbAppointmentClient.getInspectionAppointmentByAppId(dto.getAppId());
        InspectionAppointmentDto inspectionAppointmentDto;
        if (responseDto.ok()) {
            inspectionAppointmentDto = responseDto.getEntity();
        } else {
            inspectionAppointmentDto = new InspectionAppointmentDto();
        }
        ApplicationDto applicationDto = new ApplicationDto();
        applicationDto.setId(dto.getAppId());
        inspectionAppointmentDto.setApplication(applicationDto);
        inspectionAppointmentDto.setApptRefNo(apptRefNo);
        inspectionAppointmentDto.setSpecInsDate(null);
        inspectionAppointmentDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);

        if (appointmentDtoSave.getStartDate() != null) {
            try {
                inspectionAppointmentDto.setStartDate(Formatter.parseDateTime(appointmentDtoSave.getStartDate(), AppConsts.DEFAULT_DATE_TIME_FORMAT));
            } catch (ParseException e) {
                log.error(e.getMessage(), e);
            }
        }
        if (appointmentDtoSave.getEndDate() != null) {
            try {
                inspectionAppointmentDto.setEndDate(Formatter.parseDateTime(appointmentDtoSave.getEndDate(), AppConsts.DEFAULT_DATE_TIME_FORMAT));
            } catch (ParseException e) {
                log.error(e.getMessage(), e);
            }
        }
        inspectionAppointmentDtos.add(inspectionAppointmentDto);

        List<String> cancelRefNos = new ArrayList<>();
        cancelRefNos.add(dto.getCancelApptRefNo());
        //Do not delete task-related code
//        List<TaskDto> taskDtos = organizationClient.getCurrTaskByRefNo(dto.getRefNo()).getEntity();
        //create application and inspectors correlation data
//        List<AppInspectorCorrelationDto> appInspectorCorrelationDtos = fillAppInspectorCorrelationDtos(taskDtos);

        SaveAppointmentDataDto saveAppointmentDataDto = new SaveAppointmentDataDto();
        saveAppointmentDataDto.setAppointmentDtos(inspectionAppointmentDtos);
        saveAppointmentDataDto.setApptRefNos(cancelRefNos);
//        saveAppointmentDataDto.setTaskDtos(taskDtos);
//        saveAppointmentDataDto.setAppInspCorrelationDtos(appInspectorCorrelationDtos);
        bsbAppointmentClient.saveAppointment(saveAppointmentDataDto);

        //cancel or confirm appointment date
        ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
        apptCalendarStatusDto.setConfirmRefNums(confirmRefNos);
        apptCalendarStatusDto.setCancelRefNums(cancelRefNos);
        apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        cancelOrConfirmApptDate(apptCalendarStatusDto);
        //todo email
    }
}
