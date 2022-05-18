package sg.gov.moh.iais.egp.bsb.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.client.BsbAppointmentClient;
import sg.gov.moh.iais.egp.bsb.constant.ValidationConstants;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.AppointmentViewDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.SaveRescheduleDataDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.service.BsbAppointmentService;
import sg.gov.moh.iais.egp.bsb.util.MaskHelper;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

import static sg.gov.moh.iais.egp.bsb.constant.module.InspectionConstants.KEY_APPOINTMENT_LIST_DATA_LIST;

@Slf4j
@Delegator("rescheduleAppointmentDelegator")
public class RescheduleAppointmentDelegator {
    private static final String MODULE_NAME = "Inspection";
    private static final String FUNCTION_NAME_RESCHEDULE_APPOINTMENT = "Reschedule Appointments";
    private static final String KEY_APPOINTMENT_VIEW_LIST = "appointmentViewList";
    private static final String ACTION_TYPE = "action_type";
    private static final String ACTION_TYPE_NEXT = "next";
    private static final String ACTION_TYPE_BACK = "back";

    private final BsbAppointmentClient bsbAppointmentClient;
    private final BsbAppointmentService bsbAppointmentService;

    public RescheduleAppointmentDelegator(BsbAppointmentClient bsbAppointmentClient, BsbAppointmentService bsbAppointmentService) {
        this.bsbAppointmentClient = bsbAppointmentClient;
        this.bsbAppointmentService = bsbAppointmentService;
    }

    public void start(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction(MODULE_NAME, FUNCTION_NAME_RESCHEDULE_APPOINTMENT);
        request.getSession().removeAttribute(KEY_APPOINTMENT_VIEW_LIST);
    }

    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        List<AppointmentViewDto> appointmentDtoList = (List<AppointmentViewDto>) ParamUtil.getSessionAttr(request, KEY_APPOINTMENT_LIST_DATA_LIST);
        List<AppointmentViewDto> appointmentViewDtos = getAppointmentViewDtos(request);
        Map<String, AppointmentViewDto> viewDtoMap = sg.gov.moh.iais.egp.bsb.util.CollectionUtils.uniqueIndexMap(appointmentDtoList, AppointmentViewDto::getAppId);
        String[] maskedAppIds = ParamUtil.getStrings(request, "apptAppId");

        if (CollectionUtils.isEmpty(appointmentViewDtos) && maskedAppIds != null && maskedAppIds.length != 0) {
            appointmentViewDtos = new ArrayList<>(maskedAppIds.length);
            for (String maskedAppId : maskedAppIds) {
                String apptAppId = MaskHelper.unmask("maskedApptAppId", maskedAppId);
                AppointmentViewDto appointmentViewDto = viewDtoMap.get(apptAppId);
                appointmentViewDto.setMaskedAppId(maskedAppId);
                appointmentViewDtos.add(appointmentViewDto);
            }
            ParamUtil.setSessionAttr(request, KEY_APPOINTMENT_VIEW_LIST, (Serializable) appointmentViewDtos);
        }
    }

    public void valFormData(BaseProcessClass bpc) throws ParseException {
        log.info("start validate...");
        HttpServletRequest request = bpc.request;
        List<AppointmentViewDto> appointmentViewDtos = getAppointmentViewDtos(request);
        //Compare the first specified time with the possible second specified time to determine whether to query the time again
        boolean needNewDate = bindParam(request, appointmentViewDtos);
        String actionType = "";
        ValidationResultDto validationResultDto = bsbAppointmentClient.validateRescheduleAppointment(appointmentViewDtos);
        if (!validationResultDto.isPass()) {
            ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
            actionType = ACTION_TYPE_BACK;
        } else {
            actionType = ACTION_TYPE_NEXT;
            //Verify that the appropriate time can be generated
            HashMap<String, String> errorMap = validationResultDto.getErrorMap();
            bsbAppointmentService.getNewInsDate(appointmentViewDtos, errorMap, needNewDate);
            if (!CollectionUtils.isEmpty(errorMap)) {
                ParamUtil.setRequestAttr(request, ValidationConstants.KEY_VALIDATION_ERRORS, validationResultDto.toErrorMsg());
                actionType = ACTION_TYPE_BACK;
            }
        }
        ParamUtil.setRequestAttr(request, ACTION_TYPE, actionType);
        ParamUtil.setSessionAttr(request, KEY_APPOINTMENT_VIEW_LIST, (Serializable) appointmentViewDtos);
    }

    //
    public void submitData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        List<AppointmentViewDto> appointmentViewDtos = getAppointmentViewDtos(request);
        List<SaveRescheduleDataDto> rescheduleDataDtos = new ArrayList<>(appointmentViewDtos.size());
        for (AppointmentViewDto viewDto : appointmentViewDtos) {
            List<ApptUserCalendarDto> userCalendarDtos = viewDto.getUserCalendarDtos();
            ApptUserCalendarDto calendarDto = userCalendarDtos.get(0);
            SaveRescheduleDataDto dto = new SaveRescheduleDataDto();
            dto.setSpecInsDate(viewDto.getInspNewDate());
            dto.setStartDate(calendarDto.getStartSlot().get(0));
            dto.setEndDate(calendarDto.getEndSlot().get(0));
            dto.setReason(viewDto.getReason());
            dto.setAppId(viewDto.getAppId());
            dto.setAppNo(viewDto.getApplicationNo());
            dto.setApptRefNo(viewDto.getApptRefNo());
            dto.setUserCalendarDtos(userCalendarDtos);
            rescheduleDataDtos.add(dto);
        }
        ResponseDto<String> responseDto = bsbAppointmentClient.saveRescheduleData(rescheduleDataDtos);
        if (responseDto.ok()) {
            ParamUtil.setRequestAttr(request,"ackMsg","You have successfully reschedule inspection");
        } else {
            ParamUtil.setRequestAttr(request,"ackMsg","You are failed to reschedule inspection");
        }
        ParamUtil.setRequestAttr(request,"backUrl","/bsb-web/eservice/INTERNET/MohBsbRescheduleApptList");
    }

    private List<AppointmentViewDto> getAppointmentViewDtos(HttpServletRequest request) {
        List<AppointmentViewDto> appointmentViewDtos = (List<AppointmentViewDto>) ParamUtil.getSessionAttr(request, KEY_APPOINTMENT_VIEW_LIST);
        return appointmentViewDtos == null ? getDefaultDto() : appointmentViewDtos;
    }

    private List<AppointmentViewDto> getDefaultDto() {
        return new ArrayList<>();
    }

    private boolean bindParam(HttpServletRequest request, List<AppointmentViewDto> appointmentViewDtos) {
        boolean needNewDate = false;
        for (AppointmentViewDto appointmentViewDto : appointmentViewDtos) {
            //Gets the last specified time and compares it to the new time to determine whether the generation time is required
            String newStartDate = appointmentViewDto.getNewStartDate();
            String newEndDate = appointmentViewDto.getNewEndDate();

            String maskedAppId = appointmentViewDto.getMaskedAppId();
            String reason = ParamUtil.getString(request, "reason" + maskedAppId);
            String startDateStr = ParamUtil.getString(request, "newStartDate" + maskedAppId);
            String endDateStr = ParamUtil.getString(request, "newEndDate" + maskedAppId);
            String newDateStr = ParamUtil.getString(request, "newDate" + maskedAppId);

            appointmentViewDto.setReason(reason);
            appointmentViewDto.setNewStartDate(startDateStr);
            appointmentViewDto.setNewEndDate(endDateStr);
            appointmentViewDto.setModule("rescheduleAppointment");

            boolean changeStartDate = false;
            if (newStartDate != null) {
                changeStartDate = !newStartDate.equals(startDateStr);
            }
            boolean changeEndDate = false;
            if (newEndDate != null) {
                changeEndDate = !newEndDate.equals(endDateStr);
            }
            boolean hasNewDate = newDateStr == null;
            //
            if (changeStartDate || changeEndDate || hasNewDate) {
                needNewDate = true;
            }
        }
        return needNewDate;
    }
}
