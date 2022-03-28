package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesInspecApptDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import sg.gov.moh.iais.egp.bsb.client.AppointmentClient;
import sg.gov.moh.iais.egp.bsb.client.BsbAppointmentClient;
import sg.gov.moh.iais.egp.bsb.client.OrganizationClient;
import sg.gov.moh.iais.egp.bsb.constant.AppConstants;
import sg.gov.moh.iais.egp.bsb.dto.appointment.AppointmentReviewDataDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.SaveAppointmentDataDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.ApplicationDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionAppointmentDto;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static sg.gov.moh.iais.egp.bsb.constant.AppointmentConstants.*;

/**
 * @author tangtang
 * @date 2022/3/8 13:57
 */
@Service
@Slf4j
public class ApptInspectionDateService {
    private final BsbAppointmentClient bsbAppointmentClient;
    private final AppointmentClient appointmentClient;

    public ApptInspectionDateService(BsbAppointmentClient bsbAppointmentClient, AppointmentClient appointmentClient) {
        this.bsbAppointmentClient = bsbAppointmentClient;
        this.appointmentClient = appointmentClient;
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
            InspectionAppointmentDto inspectionAppointmentDto = new InspectionAppointmentDto();
            ApplicationDto applicationDto = new ApplicationDto();
            applicationDto.setId(applicationId);
            inspectionAppointmentDto.setApplication(applicationDto);
            inspectionAppointmentDto.setApptRefNo(apptRefNo);
            inspectionAppointmentDto.setSpecInsDate(null);
            inspectionAppointmentDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            AppointmentDto appointmentDto = apptInspectionDateDto.getAppointmentDto();
            if (appointmentDto.getStartDate() != null) {
                try {
                    inspectionAppointmentDto.setStartDate(Formatter.parseDateTime(appointmentDto.getStartDate(), AppConsts.DEFAULT_DATE_TIME_FORMAT));
                } catch (ParseException e) {
                    log.error(e.getMessage(), e);
                }
            }
            if (appointmentDto.getEndDate() != null) {
                try {
                    inspectionAppointmentDto.setEndDate(Formatter.parseDateTime(appointmentDto.getEndDate(), AppConsts.DEFAULT_DATE_TIME_FORMAT));
                } catch (ParseException e) {
                    log.error(e.getMessage(), e);
                }
            }
            appPremisesInspecApptDtoList.add(inspectionAppointmentDto);
            confirmRefNo.add(apptRefNo);
        }
        //cancel or confirm appointment date
        SaveAppointmentDataDto saveAppointmentDataDto = new SaveAppointmentDataDto();
        saveAppointmentDataDto.setAppointmentDtos(appPremisesInspecApptDtoList);
        saveAppointmentDataDto.setApptRefNos(confirmRefNo);
        saveAppointmentDataDto.setTaskDtos(apptInspectionDateDto.getTaskDtos());
        bsbAppointmentClient.saveAppointment(saveAppointmentDataDto);

        ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
        apptCalendarStatusDto.setConfirmRefNums(confirmRefNo);
        apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        cancelOrConfirmApptDate(apptCalendarStatusDto);
        //todo email
    }


    public void saveUserSpecificDate(ApptInspectionDateDto apptInspectionDateDto, String applicationId) {
        List<InspectionAppointmentDto> appPremisesInspecApptDtoList = new ArrayList<>(1);
        List<String> appPremCorrIds = apptInspectionDateDto.getRefNo();
        //get AppointmentDto
        AppointmentDto appointmentDtoSave = apptInspectionDateDto.getSpecificApptDto();
        //save and return apptRefNo
        List<String> confirmRefNo = new ArrayList<>(1);
        String apptRefNo = appointmentClient.saveManualUserCalendar(appointmentDtoSave).getEntity();
        confirmRefNo.add(apptRefNo);
        //save data
//        for(String appPremCorrId : appPremCorrIds) {

        InspectionAppointmentDto inspectionAppointmentDto = new InspectionAppointmentDto();
        ApplicationDto applicationDto = new ApplicationDto();
        applicationDto.setId(applicationId);
        inspectionAppointmentDto.setApplication(applicationDto);
        inspectionAppointmentDto.setApptRefNo(apptRefNo);
        inspectionAppointmentDto.setSpecInsDate(null);
        inspectionAppointmentDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);

        AppointmentDto appointmentDto = apptInspectionDateDto.getAppointmentDto();
        if (appointmentDto.getStartDate() != null) {
            try {
                inspectionAppointmentDto.setStartDate(Formatter.parseDateTime(appointmentDto.getStartDate(), AppConsts.DEFAULT_DATE_TIME_FORMAT));
            } catch (ParseException e) {
                log.error(e.getMessage(), e);
            }
        }
        if (appointmentDto.getEndDate() != null) {
            try {
                inspectionAppointmentDto.setEndDate(Formatter.parseDateTime(appointmentDto.getEndDate(), AppConsts.DEFAULT_DATE_TIME_FORMAT));
            } catch (ParseException e) {
                log.error(e.getMessage(), e);
            }
        }
        appPremisesInspecApptDtoList.add(inspectionAppointmentDto);

//        }

        Map<String, List<ApptUserCalendarDto>> inspectionDateMap = apptInspectionDateDto.getInspectionDateMap();
        List<String> cancelRefNo = new ArrayList<>();
        if (inspectionDateMap != null) {
            for (Map.Entry<String, List<ApptUserCalendarDto>> inspDateMap : inspectionDateMap.entrySet()) {
                String refNo = inspDateMap.getKey();
                cancelRefNo.add(refNo);
            }
        }

        SaveAppointmentDataDto saveAppointmentDataDto = new SaveAppointmentDataDto();
        saveAppointmentDataDto.setAppointmentDtos(appPremisesInspecApptDtoList);
        saveAppointmentDataDto.setApptRefNos(cancelRefNo);
        saveAppointmentDataDto.setTaskDtos(apptInspectionDateDto.getTaskDtos());
        bsbAppointmentClient.saveAppointment(saveAppointmentDataDto);

        //cancel or confirm appointment date
        ApptCalendarStatusDto apptCalendarStatusDto = new ApptCalendarStatusDto();
        apptCalendarStatusDto.setConfirmRefNums(confirmRefNo);
        apptCalendarStatusDto.setCancelRefNums(cancelRefNo);
        apptCalendarStatusDto.setSysClientKey(AppConsts.MOH_IAIS_SYSTEM_APPT_CLIENT_KEY);
        cancelOrConfirmApptDate(apptCalendarStatusDto);
        //todo email
    }
}
