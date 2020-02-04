package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.AppointmentDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptBlackoutDateQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonWorkingDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.InspectorCalendarQueryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author: yichen
 * @date time:12/28/2019 2:55 PM
 * @description:
 */


@FeignClient(name = "iais-appointment",  configuration = FeignConfiguration.class,
        fallback = OnlineApptClientFallback.class)
public interface OnlineApptClient {
    @PostMapping(value = "/iais-appointment/blacked-out-date/results", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<ApptBlackoutDateQueryDto>> doQuery(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/iais-appointment/blacked-out-date/", consumes = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<Boolean> createBlackedOutCalendar(@RequestBody ApptBlackoutDateDto blackoutDateDto);

	@PutMapping(value = "/iais-appointment/blacked-out-date/", consumes = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<Boolean> updateBlackedOutCalendar(@RequestBody ApptBlackoutDateDto blackoutDateDto);

    @PostMapping(value = "/iais-appointment/blacked-out-date/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Boolean> inActiveBlackedOutCalendar(@RequestBody ApptBlackoutDateDto blackoutDateDto);

	@GetMapping(value = "/iais-appointment/non-working-data/{groupId}", produces = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<List<ApptNonWorkingDateDto>> getNonWorkingDateListByWorkGroupId(@PathVariable(name = "groupId") String groupId);

	@PostMapping(value = "/iais-appointment/user-calendar/", produces = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<Map<String,List<Date>>> getUserCalendarByUserId(@RequestBody AppointmentDto appointmentDto);

	@PutMapping(value = "/iais-appointment/non-working-data/", consumes = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<ApptNonWorkingDateDto> updateNonWorkingDate(@RequestBody ApptNonWorkingDateDto nonWorkingDateDto);

	@GetMapping(value = "/user-id/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<List<String>> getIdByAgencyUserId(@PathVariable("userId") String userId);

	@PostMapping(value = "/iais-appointment/inspector-calendar/results", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	FeignResponseEntity<SearchResult<InspectorCalendarQueryDto>> queryInspectorCalendar(@RequestBody SearchParam searchParam);

}
