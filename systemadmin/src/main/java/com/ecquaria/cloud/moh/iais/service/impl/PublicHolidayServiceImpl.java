package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.PublicHolidayQueryDto;
import com.ecquaria.cloud.moh.iais.service.PublicHolidayService;
import com.ecquaria.cloud.moh.iais.service.client.PublicHolidayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author guyin
 * @date 2019/12/28 10:41
 */
@Service
@Slf4j
public class PublicHolidayServiceImpl implements PublicHolidayService {

    @Autowired
    private PublicHolidayClient publicHolidayClient ;

    @Override
    public SearchResult<PublicHolidayQueryDto> getHoliday(SearchParam searchParam) {
        return publicHolidayClient.getAllHoliday(searchParam).getEntity();
    }

    @Override
    public PublicHolidayDto createHoliday(PublicHolidayDto publicHolidayDto){
        return publicHolidayClient.doSave(publicHolidayDto).getEntity();
    }

    @Override
    public PublicHolidayDto updateHoliday(PublicHolidayDto publicHolidayDto){
        return publicHolidayClient.doUpdate(publicHolidayDto).getEntity();
    }

    @Override
    public void deleteHoliday(String id){
        publicHolidayClient.doDelete(id).getEntity();
    }

}
