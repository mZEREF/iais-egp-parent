package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayQueryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author guyin
 * @date 2019/12/28 10:45
 */
@FeignClient(name = "iais-appointment", configuration = FeignConfiguration.class, fallback = PublicHolidayClientFallback.class)
public interface PublicHolidayClient {


    @PostMapping(value = "/iais-publicHoliday/getAllHoliday",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<PublicHolidayQueryDto>> getAllHoliday(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/iais-publicHoliday/getHolidayById",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PublicHolidayDto> getHolidayById(@RequestParam("id") String id);

    @PostMapping(value = "/iais-publicHoliday/doSave", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PublicHolidayDto> doSave(@RequestBody PublicHolidayDto publicHolidayDto);

    @PostMapping(value = "/iais-publicHoliday/doSaves", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> doSaves(@RequestBody List<PublicHolidayDto> publicHolidayDtos);

    @PostMapping(value = "/iais-publicHoliday/doUpdate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PublicHolidayDto> doUpdate(@RequestBody PublicHolidayDto publicHolidayDto);

    @GetMapping(value = "/iais-publicHoliday/getPublicHolidayByDate", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PublicHolidayDto> getPublicHolidayBydate(@RequestParam("date") String date);

    @GetMapping(value = "/iais-publicHoliday/getPublicHolidayByDis", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PublicHolidayDto> getPublicHolidayByDis(@RequestParam("dis") String dis,@RequestParam("year") int year);

    @PostMapping(value = "/iais-publicHoliday/doDelete", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> doDelete(@RequestBody List<String> id);

    @PostMapping(value = "/iais-publicHoliday/getPublicHolidayInCalender", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<String> getPublicHolidayInCalender(@RequestParam("fromDate") String fromDate);

    @PostMapping(value = "/iais-publicHoliday/getScheduleInCalender", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getScheduleInCalender(@RequestParam("groupName") String groupName);

    @GetMapping(value = "/iais-publicHoliday/getAllYearList",produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<String>> getAllYearList();


}
