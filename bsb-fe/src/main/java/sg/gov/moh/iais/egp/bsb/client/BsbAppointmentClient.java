package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.appointment.InspectionDateDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;

@FeignClient(value = "bsb-fe-api", configuration = FeignConfiguration.class)
public interface BsbAppointmentClient {
    @PostMapping(path = "/inspection/appointment/form-validation/main", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateAppointmentData(@RequestBody InspectionDateDto dto);

    @PostMapping(path = "/inspection/appointment",consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveInsDate(@RequestBody InspectionDateDto dto);
}
