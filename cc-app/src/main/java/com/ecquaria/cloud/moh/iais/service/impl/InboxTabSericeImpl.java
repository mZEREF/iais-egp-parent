package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
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
        log.info("The method is selectAll");
        ApplicationDto applicationDto = new ApplicationDto();
        return RestApiUtil.getByList(URL,applicationDto,List.class);
    }

    @Override
    public List<ApplicationDto> searchByAppType(String type) {
        log.info("The method is searchByAppType");
        ApplicationDto applicationDto = new ApplicationDto();
        return RestApiUtil.getByList(URL,applicationDto,List.class);
    }

    @Override
    public SearchResult<ApplicationDto> doQuery(SearchParam param) {
        return RestApiUtil.query(URL+"/param",param);
    }

    @Override
    public List<ApplicationDto> searchByAppStatus(String status) {
        log.info("The method is searchByAppType");
        ApplicationDto applicationDto = new ApplicationDto();
        return RestApiUtil.getByList(URL,applicationDto,List.class);
    }


}


