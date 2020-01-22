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

/**
 * @author guyin
 * @date 2019/12/28 10:45
 */
@FeignClient(name = "iais-appointment", configuration = FeignConfiguration.class, fallback = IntranetUserClientFallback.class)
public interface PublicHolidayClient {


    @PostMapping(value = "/iais-publicHoliday/getAllHoliday",consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<SearchResult<PublicHolidayQueryDto>> getAllHoliday(@RequestBody SearchParam searchParam);

    @PostMapping(value = "/iais-publicHoliday/doSave", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PublicHolidayDto> doSave(@RequestBody PublicHolidayDto publicHolidayDto);

    @PostMapping(value = "/iais-publicHoliday/doUpdate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PublicHolidayDto> doUpdate(@RequestBody PublicHolidayDto publicHolidayDto);

    @GetMapping(value = "/iais-publicHoliday/doDelete", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> doDelete(@RequestBody String id);
}
