package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayQueryDto;

import java.util.List;

/**
 * @author guyin
 * @date 2019/12/28 10:40
 */
public interface PublicHolidayService {
    SearchResult<PublicHolidayQueryDto> getHoliday(SearchParam searchParam);
    PublicHolidayDto getHolidayById(String id);
    PublicHolidayDto createHoliday(PublicHolidayDto publicHolidayDto);
    void createHolidays(List<PublicHolidayDto> publicHolidayDtos);
    PublicHolidayDto updateHoliday(PublicHolidayDto publicHolidayDto);
    void deleteHoliday(List<String> id);
    String getPublicHolidayInCalender(String form);
    List<String> getScheduleInCalender(String groupName);
    List<String> getAllYearList();
}
