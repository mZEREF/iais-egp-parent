package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonAvailabilityDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonWorkingDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "iais-appointment", configuration = FeignConfiguration.class,
        fallback = AppointmentClientFallback.class)
public interface AppointmentClient {

    @GetMapping(value = "/iais-publicHoliday/getActiveHoliday")
    FeignResponseEntity<List<PublicHolidayDto>> getActiveHoliday();

    @GetMapping(value = "/blacked-out-date/{shortName}")
    FeignResponseEntity<List<ApptBlackoutDateDto>> getAllByShortName(@PathVariable("shortName") String shortName);

    @GetMapping(value = "/non-working-data/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApptNonWorkingDateDto>> getNonWorkingDateListByWorkGroupId(@PathVariable(name = "groupId") String groupId);

    @PostMapping(value = "/iais-appointment/appt-nonavac", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApptNonAvailabilityDateDto> createNonAvailability(@RequestBody ApptNonAvailabilityDateDto apptNonAvailabilityDateDto);

    @PutMapping(value = "/iais-appointment/appt-nonavau", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ApptNonAvailabilityDateDto> updateNonAvailability(@RequestBody ApptNonAvailabilityDateDto apptNonAvailabilityDateDto);

    @GetMapping(value = "/iais-appointment/appt-nonava/{id}")
    FeignResponseEntity<ApptNonAvailabilityDateDto> getNonAvailabilityById(@PathVariable("id") String id);

    @GetMapping(value = "/iais-appointment/appt-nonava-contain")
    FeignResponseEntity<String> dateIsContainNonWork(@RequestBody ApptNonAvailabilityDateDto apptNonAvailabilityDateDto);

    @GetMapping(value = "/iais-appointment/user-calendar/{userId}")
    FeignResponseEntity<List<String>> getIdByAgencyUserId(@PathVariable("userId") String userId);

    @PostMapping(value = "/iais-appointment/user-calendar",produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<List<ApptUserCalendarDto>>> getUserCalendarByUserId(@RequestBody AppointmentDto appointmentDto);

    @GetMapping(value = "/iais-appointment/ava-appt-date/{strSpecDate}")
    FeignResponseEntity<String> isAvailableAppointmentDates(@PathVariable("strSpecDate") String strSpecDate);
}
