package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayQueryDto;

/**
 * @author guyin
 * @date 2019/12/28 10:40
 */
public interface PublicHolidayService {
    SearchResult<PublicHolidayQueryDto> getHoliday(SearchParam searchParam);
    PublicHolidayDto createHoliday(PublicHolidayDto publicHolidayDto);
    PublicHolidayDto updateHoliday(PublicHolidayDto publicHolidayDto);
    void deleteHoliday(String id);
}
