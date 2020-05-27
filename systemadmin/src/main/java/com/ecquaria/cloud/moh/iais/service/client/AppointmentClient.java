package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAgencyUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptAppInfoShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptCalendarStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonAvailabilityDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonWorkingDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserCalendarDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptUserSystemCorrelationDto;
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

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author ShiCheng_Xu
 */
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

    @PostMapping(value = "/iais-appointment/appt-nonava-contain", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> dateIsContainNonWork(@RequestBody ApptNonAvailabilityDateDto apptNonAvailabilityDateDto);

    @GetMapping(value = "/iais-appointment/user-calendar/{userId}")
    FeignResponseEntity<List<String>> getIdByAgencyUserId(@PathVariable("userId") String userId);

    @PostMapping(value = "/iais-appointment/user-calendar",produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, List<ApptUserCalendarDto>>> getUserCalendarByUserId(@RequestBody AppointmentDto appointmentDto);

    @GetMapping(value = "/iais-appointment/ava-appt-date/{strSpecDate}")
    FeignResponseEntity<String> isAvailableAppointmentDates(@PathVariable("strSpecDate") String strSpecDate);

    @PostMapping(value = "/iais-appointment/appt-nonava/daylist", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<Integer, Integer>> getWorkAndNonMap(@RequestBody List<Date> dates);

    @PostMapping(value = "/iais-appointment/user-calendar-validation",produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Map<String, List<String>>> validateUserCalendar(@RequestBody AppointmentDto appointmentDto);

    @PostMapping(value = "/iais-appointment/manual-calendar-appointment",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> saveManualUserCalendar(@RequestBody AppointmentDto appointmentDto);

    @PutMapping(value = "/iais-appointment/user-calendar/status",consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateUserCalendarStatus(@RequestBody ApptCalendarStatusDto apptCalDto);

    @GetMapping(value = "/iais-appointment/appt-calendar/{sysUserCorrId}/{status}")
    FeignResponseEntity<ApptUserCalendarDto> getCalenderBySysUserCorrIdAndStatus(@PathVariable("sysUserCorrId") String sysUserCorrId, @PathVariable("status") String status);

    @PutMapping(value = "/iais-appointment/appt-u-calendar",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApptUserCalendarDto>> updateApptUserCalendarDtoList(@RequestBody List<ApptUserCalendarDto> apptUserCalendarDtos);

    @PostMapping(value = "/iais-appointment/appt-c-calendar",produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApptUserCalendarDto>> createApptUserCalendarDtoList(@RequestBody List<ApptUserCalendarDto> apptUserCalendarDtos);

    @GetMapping(value = "/iais-appointment/appt-sys-corr/{srcSysId}/{userAgencyId}")
    FeignResponseEntity<List<ApptUserSystemCorrelationDto>> getApptUserSystemCorrelationDtos(@PathVariable("srcSysId") String srcSysId, @PathVariable("userAgencyId") String userAgencyId);

    @GetMapping(value = "/iais-appointment/appt-sys-corr/{userLoginId}")
    FeignResponseEntity<ApptAgencyUserDto> getApptAgencyUserDtoLogin(@PathVariable("userLoginId") String userLoginId);

    @PostMapping(value = "/iais-appointment/appt-corr-calendar", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApptUserSystemCorrelationDto>> createApptUserInfo(@RequestBody ApptAppInfoShowDto apptAppInfoShowDto);

    @PutMapping(value = "/iais-appointment/appt-nonavau-refno", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApptNonAvailabilityDateDto>> cancelNonAvailabilityByApptRefNo(@RequestBody ApptNonAvailabilityDateDto apptNonAvailabilityDateDto);

    @PutMapping(value = "/iais-appointment/appt-calendar-refno", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ApptUserCalendarDto>> cancelCalenderByApptRefNoAndStatus(ApptUserCalendarDto apptUserCalendarDto);
}
