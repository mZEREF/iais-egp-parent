package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonWorkingDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "hcsa-appointment", configuration = FeignConfiguration.class,
        fallback = AppointmentClientFallback.class)
public interface AppointmentClient {

    @GetMapping(value = "/iais-publicHoliday/getActiveHoliday")
    FeignResponseEntity<List<PublicHolidayDto>> getActiveHoliday();

    @GetMapping(value = "/blacked-out-date/{shortName}")
    FeignResponseEntity<List<ApptBlackoutDateDto>> getAllByShortName(@PathVariable("shortName") String shortName);

    @GetMapping(value = "/non-working-data/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApptNonWorkingDateDto>> getNonWorkingDateListByWorkGroupId(@PathVariable(name = "groupId") String groupId);
}
