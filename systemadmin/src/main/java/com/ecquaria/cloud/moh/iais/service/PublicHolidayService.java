package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayQueryDto;

import java.util.Date;
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
    PublicHolidayDto publicHoliday(Date date);
    PublicHolidayDto publicHolidayByDis(String phCode,int year);
    List<String> getScheduleInCalender(String groupName);
    List<String> getAllYearList();
    /**
      * @author: shicheng
      * @Date 2021/2/25
      * @return: List<PublicHolidayDto>
      * @Descripation: get all public holiday by status
      */
    List<PublicHolidayDto> getAllActivePubHoliDay();

    /**
      * @author: shicheng
      * @Date 2021/2/25
      * @Param: publicHolidayDtos, allActivePubHolDays
      * @return: List<PublicHolidayDto>
      * @Descripation: filter holidays(To prevent the repeat)
      */
    List<PublicHolidayDto> filterPreventDays(List<PublicHolidayDto> publicHolidayDtos, List<PublicHolidayDto> allActivePubHolDays, List<PublicHolidayDto> duplicateDate);

    /**
      * @author: shicheng
      * @Date 2021/3/2
      * @Param: duplicateDate
      * @return: List<String>
      * @Descripation: get Duplicate Date Str
      */
    List<String> getDuplicateDateStr(List<PublicHolidayDto> duplicateDate);
}
