package sg.gov.moh.iais.egp.bsb.service;

import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import sg.gov.moh.iais.egp.bsb.client.BsbAppointmentClient;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.AppointmentViewDto;

import java.text.ParseException;
import java.util.*;

/**
 * @author tangtang
 * @date 2022/3/31 14:43
 */
@Service
@Slf4j
public class BsbAppointmentService {
    private static final String PARAM_NEW_DATE = "newDate";
    private static final String ERROR_MSG_NO_DATE = "No suitable date available, please reschedule";

    private final BsbAppointmentClient bsbAppointmentClient;

    public BsbAppointmentService(BsbAppointmentClient bsbAppointmentClient) {
        this.bsbAppointmentClient = bsbAppointmentClient;
    }

    /**
     * call this method to get new inspection date
     */
    public void getNewInsDate(List<AppointmentViewDto> appointmentViewDtos, Map<String, String> errorMap, boolean needNewDate) throws ParseException {
        if (needNewDate) {
            for (AppointmentViewDto appointmentViewDto : appointmentViewDtos) {
                Date inspStDate = Formatter.parseDate(appointmentViewDto.getNewStartDate());
                Date inspEndDate = Formatter.parseDate(appointmentViewDto.getNewEndDate());

                List<String> appNoList = new ArrayList<>(1);
                appNoList.add(appointmentViewDto.getApplicationNo());
                AppointmentDto appointmentDto = new AppointmentDto();
                appointmentDto.setAppNoList(appNoList);
                appointmentDto.setStartDate(Formatter.formatDateTime(inspStDate, "dd/MM/yyyy HH:mm:ss"));
                appointmentDto.setEndDate(Formatter.formatDateTime(inspEndDate, "dd/MM/yyyy HH:mm:ss"));

                ResponseDto<List<ApptRequestDto>> responseDto = bsbAppointmentClient.getRescheduleNewDateFromBE(appointmentDto);
                if (responseDto != null) {
                    List<ApptRequestDto> apptRequestDtoList = responseDto.getEntity();

                    if (!CollectionUtils.isEmpty(apptRequestDtoList)) {
                        ApptRequestDto apptRequestDto = apptRequestDtoList.get(0);
                        ArrayList<ApptUserCalendarDto> userClandars = apptRequestDto.getUserClandars();
                        if (userClandars != null) {
                            appointmentViewDto.setInspNewDate(userClandars.get(0).getStartSlot().get(0));
                            appointmentViewDto.setUserCalendarDtos(userClandars);
                            appointmentViewDto.setApptRefNo(apptRequestDto.getApptRefNo());
                        } else {
                            appointmentViewDto.setInspNewDate(null);
                            appointmentViewDto.setUserCalendarDtos(null);
                            appointmentViewDto.setApptRefNo(null);
                            errorMap.put(PARAM_NEW_DATE + appointmentViewDto.getMaskedAppId(), ERROR_MSG_NO_DATE);
                        }
                    } else {
                        appointmentViewDto.setInspNewDate(null);
                        appointmentViewDto.setUserCalendarDtos(null);
                        appointmentViewDto.setApptRefNo(null);
                        errorMap.put(PARAM_NEW_DATE + appointmentViewDto.getMaskedAppId(), ERROR_MSG_NO_DATE);
                    }
                } else {
                    appointmentViewDto.setInspNewDate(null);
                    appointmentViewDto.setUserCalendarDtos(null);
                    appointmentViewDto.setApptRefNo(null);
                    errorMap.put(PARAM_NEW_DATE + appointmentViewDto.getMaskedAppId(), ERROR_MSG_NO_DATE);
                }
            }
        }
    }
}
