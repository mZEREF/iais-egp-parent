package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.dto.ApplicationDto;
import com.ecquaria.cloud.moh.iais.service.InboxTabService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-10-15 17:52
 **/
@Service
@Slf4j
public class InboxTabSericeImpl implements InboxTabService {
    private static final String URL="iais-application:8881/iais-application";
    @Override
    public List<ApplicationDto> selectAll() {
        log.info("---InboxTabSericeImpl---");
        ApplicationDto applicationDto = new ApplicationDto();
        List<ApplicationDto> applicationDtos = RestApiUtil.getByList(URL,applicationDto,List.class);
        return applicationDtos;
    }

}


