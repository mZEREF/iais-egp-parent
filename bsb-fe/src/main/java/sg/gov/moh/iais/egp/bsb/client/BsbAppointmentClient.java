package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptRequestDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.AppointmentViewDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.InspectionDateDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.SaveRescheduleDataDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;

import java.util.List;

@FeignClient(value = "bsb-api", configuration = FeignConfiguration.class)
public interface BsbAppointmentClient {
    @PostMapping(path = "/appointment/form-validation/main", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateAppointmentData(@RequestBody InspectionDateDto dto);

    @PostMapping(path = "/appointment",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveInsDate(@RequestBody InspectionDateDto dto);

    @GetMapping("/appointment/reschedule/list")
    ResponseDto<List<AppointmentViewDto>> searchRescheduleAppt();

    @PostMapping(path = "/appointment/reschedule/form-validation/main", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateRescheduleAppointment(@RequestBody List<AppointmentViewDto> dtos);

    @PostMapping(value = "/appointment/reschedule/new-date", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<List<ApptRequestDto>> getRescheduleNewDateFromBE(@RequestBody AppointmentDto appointmentDto);

    @PostMapping(value = "/appointment/reschedule", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveRescheduleData(@RequestBody List<SaveRescheduleDataDto> rescheduleDataDtos);
}
