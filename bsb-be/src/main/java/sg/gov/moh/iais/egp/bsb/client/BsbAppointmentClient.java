package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptRequestDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.*;
import sg.gov.moh.iais.egp.bsb.dto.appointment.doreschedule.OfficerRescheduleDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;

import java.util.List;

@FeignClient(value = "bsb-api", configuration = FeignConfiguration.class)
public interface BsbAppointmentClient {
    @GetMapping(path = "/inspection/appointment/inspection-date/appId", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<BsbAppointmentDto> getApptStartEndDateByAppId(@RequestParam("appId") String applicationId);

    @PostMapping(path = "/inspection/appointment", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveAppointmentData(@RequestBody SaveAppointmentDataDto dto);

    @GetMapping("/inspection/appointment/reschedule/list")
    ResponseDto<SearchResultDto> searchRescheduleAppt(@SpringQueryMap ApptSearchDto searchDto);

    @GetMapping(path = "/inspection/appointment/reschedule/appId", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<OfficerRescheduleDto> getOfficerRescheduleReviewData(@RequestParam("appId") String applicationId);

    @PostMapping(value = "/datasync/appointment/reschedule/date", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApptRequestDto>> getRescheduleNewDateFromBE(@RequestBody AppointmentDto appointmentDto);

    @PostMapping(path = "/inspection/appointment/reschedule/form-validation/main", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateRescheduleData(@RequestBody OfficerRescheduleDto dto);
}
