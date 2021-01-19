package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.service.PublicHolidayService;
import com.ecquaria.cloud.moh.iais.service.client.PublicHolidayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author guyin
 * @date 2019/12/28 10:41
 */
@Slf4j
@Service
public class PublicHolidayServiceImpl implements PublicHolidayService {

    @Autowired
    private PublicHolidayClient publicHolidayClient ;

    @Override
    @SearchTrack(catalog = "systemAdmin", key = "getHolidayList")
    public SearchResult<PublicHolidayQueryDto> getHoliday(SearchParam searchParam) {
        return publicHolidayClient.getAllHoliday(searchParam).getEntity();
    }

    @Override
    public PublicHolidayDto getHolidayById(String id){
        return publicHolidayClient.getHolidayById(id).getEntity();
    }

    @Override
    public PublicHolidayDto createHoliday(PublicHolidayDto publicHolidayDto){
        return publicHolidayClient.doSave(publicHolidayDto).getEntity();
    }

    @Override
    public void createHolidays(List<PublicHolidayDto> publicHolidayDtos){
        publicHolidayClient.doSaves(publicHolidayDtos).getEntity();
    }

    @Override
    public PublicHolidayDto updateHoliday(PublicHolidayDto publicHolidayDto){
        return publicHolidayClient.doUpdate(publicHolidayDto).getEntity();
    }

    @Override
    public void deleteHoliday(List<String> id){
        publicHolidayClient.doDelete(id).getEntity();
    }

    @Override
    public String getPublicHolidayInCalender(String from){
        return publicHolidayClient.getPublicHolidayInCalender(from).getEntity();
    }

    @Override
    public PublicHolidayDto publicHoliday(Date date){
        return publicHolidayClient.getPublicHolidayBydate(Formatter.formatDateTime(date)).getEntity();
    }
    @Override
    public PublicHolidayDto publicHolidayByDis(String phCode, int year){
        return publicHolidayClient.getPublicHolidayByDis(phCode,year).getEntity();
    }

    @Override
    public List<String> getScheduleInCalender(String groupName){
        return publicHolidayClient.getScheduleInCalender(groupName).getEntity();
    }

    @Override
    public List<String> getAllYearList(){
        return publicHolidayClient.getAllYearList().getEntity();
    }

}
