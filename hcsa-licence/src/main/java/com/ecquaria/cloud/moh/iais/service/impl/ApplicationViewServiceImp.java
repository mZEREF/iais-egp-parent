package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;

import java.util.HashMap;
import java.util.Map;

public class ApplicationViewServiceImp implements ApplicationViewService {
    @Override
    public ApplicationViewDto searchByAppNo(String AppNo) {
        Map<String,Object> map = new HashMap<>();
        map.put("AppNo",AppNo);
        return RestApiUtil.getByReqParam("iais-application:8883/iais-application/application/{AppNo}",map, ApplicationViewDto.class);
    }
}
