package com.ecquaria.cloud.moh.iais.validation;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.validation.interfaces.CustomizeValidator;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2020/2/15 10:34
 **/
@Component
public class ApptInspectionDateValidate implements CustomizeValidator {

    @Autowired
    private AppointmentClient appointmentClient;

    @Override
    public Map<String, String> validate(HttpServletRequest request) {
        ApptInspectionDateDto apptInspectionDateDto = (ApptInspectionDateDto) ParamUtil.getSessionAttr(request, "apptInspectionDateDto");
        AppointmentDto specificApptDto = apptInspectionDateDto.getSpecificApptDto();
        Date specificStartDate = apptInspectionDateDto.getSpecificStartDate();
        Date specificEndDate = apptInspectionDateDto.getSpecificEndDate();
        if(specificStartDate == null || specificEndDate == null){
            return null;
        }
        if(apptInspectionDateDto.getStartHours() == null || apptInspectionDateDto.getEndHours() == null){
            return null;
        }
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();
        if(specificEndDate.before(specificStartDate)){
            errMap.put("specificDate", "UC_INSP_ERR0007");
            return errMap;
        }
        specificApptDto.setStartDate(Formatter.formatDateTime(specificStartDate, AppConsts.DEFAULT_DATE_TIME_FORMAT));
        specificApptDto.setEndDate(Formatter.formatDateTime(specificEndDate, AppConsts.DEFAULT_DATE_TIME_FORMAT));
        //get date flag, is System date(First get)
        Map<String, List<ApptUserCalendarDto>> inspectionDateMap = apptInspectionDateDto.getInspectionDateMap();
        boolean containsFlag;
        if(inspectionDateMap != null){
            for (Map.Entry<String, List<ApptUserCalendarDto>> inspDateMap : inspectionDateMap.entrySet()) {
                List<ApptUserCalendarDto> apptUserCalendarDtos = inspDateMap.getValue();
                containsFlag = getContainsFlagByTimeList(apptUserCalendarDtos, specificStartDate, specificEndDate);//NOSONAR
                if(containsFlag){
                    return errMap;
                }
            }
        }
        //key userId value date
        try {
            appointmentClient.validateUserCalendar(specificApptDto).getStatusCode();
        } catch (Exception e) {
            errMap.put("specificDate", "UC_INSP_ERR0007");
            apptInspectionDateDto.setSpecificApptDto(specificApptDto);
            ParamUtil.setSessionAttr(request, "apptInspectionDateDto", apptInspectionDateDto);
            return errMap;
        }
        apptInspectionDateDto.setSpecificApptDto(specificApptDto);
        ParamUtil.setSessionAttr(request, "apptInspectionDateDto", apptInspectionDateDto);
        return errMap;
    }

    private boolean getContainsFlagByTimeList(List<ApptUserCalendarDto> apptUserCalendarDtos, Date specificStartDate, Date specificEndDate) {
        int endTimeSize = apptUserCalendarDtos.get(0).getEndSlot().size();
        Date inspStartDate = apptUserCalendarDtos.get(0).getStartSlot().get(0);
        Date inspEndDate = apptUserCalendarDtos.get(0).getEndSlot().get(endTimeSize - 1);
        Calendar inspEndCal = Calendar.getInstance();
        inspEndCal.setTime(inspEndDate);
        inspEndCal.add(Calendar.SECOND, 1);
        Date endDate = inspEndCal.getTime();
        String specificStartDateStr = Formatter.formatDateTime(specificStartDate, "dd/MM/yyyy HH:mm:ss");
        String specificEndDateStr = Formatter.formatDateTime(specificEndDate, "dd/MM/yyyy HH:mm:ss");
        String inspStartDateStr = Formatter.formatDateTime(inspStartDate, "dd/MM/yyyy HH:mm:ss");
        String endDateStr = Formatter.formatDateTime(endDate, "dd/MM/yyyy HH:mm:ss");
        if(specificStartDateStr.equals(inspStartDateStr) && specificEndDateStr.equals(endDateStr)){
            return true;
        } else {
            return false;
        }
    }
}
