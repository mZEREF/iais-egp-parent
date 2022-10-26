package sg.gov.moh.iais.egp.bsb.client;


import com.ecquaria.cloud.moh.iais.common.dto.appointment.*;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@FeignClient(name = "iais-appointment", configuration = FeignConfiguration.class)
public interface AppointmentClient {

    @PostMapping(value = "/iais-appointment/user-calendar",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApptRequestDto>> getUserCalendarByUserId(@RequestBody AppointmentDto appointmentDto);

    @PostMapping(value = "/iais-appointment/user-calendar-validation",produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, List<String>>> validateUserCalendar(@RequestBody AppointmentDto appointmentDto);

    @PostMapping(value = "/iais-appointment/manual-calendar-appointment",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveManualUserCalendar(@RequestBody AppointmentDto appointmentDto);

    @PutMapping(value = "/iais-appointment/user-calendar/status",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateUserCalendarStatus(@RequestBody ApptCalendarStatusDto apptCalDto);

}
