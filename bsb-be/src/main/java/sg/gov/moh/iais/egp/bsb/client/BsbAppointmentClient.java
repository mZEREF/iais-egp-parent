package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptInspectionDateDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.AppointmentReviewDataDto;
import sg.gov.moh.iais.egp.bsb.dto.appointment.SaveAppointmentDataDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.InspectionAppointmentDraftDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;

import java.util.List;

@FeignClient(value = "bsb-be-api", configuration = FeignConfiguration.class)
public interface BsbAppointmentClient {

    @GetMapping(path = "/inspection/appointment/officer-review/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AppointmentReviewDataDto> getOfficerReviewData(@PathVariable("appId") String applicationId);

    @GetMapping(path = "/inspection/appointment/{taskId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApptInspectionDateDto> getInspectionDateDto(@PathVariable("taskId") String taskId);

    @GetMapping(path = "/inspection/appointment/draft", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<List<InspectionAppointmentDraftDto>> getActiveAppointmentDraftData(@RequestParam("appNo") String appNo);

    @GetMapping(path = "/inspection/appointment/inspection-date/appId", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<AppointmentDto> getApptStartEndDateByAppId(@RequestParam("appId") String applicationId);

    @PostMapping(path = "/inspection/appointment/form-validation/main", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateAppointmentData(@RequestBody AppointmentReviewDataDto dto);

    @PostMapping(path = "/inspection/appointment/draft", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<List<InspectionAppointmentDraftDto>> saveAppointmentDraft(@RequestBody List<InspectionAppointmentDraftDto> dtos);

    @PostMapping(path = "/inspection/appointment",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveAppointment(@RequestBody SaveAppointmentDataDto dto);
}
