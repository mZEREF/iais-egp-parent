package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
@Slf4j
public class ApplicationViewServiceImp implements ApplicationViewService {
    @Override
    public ApplicationViewDto searchByAppNo(String AppNo) {
        Map<String,Object> map = new HashMap<>();
        map.put("AppNo",AppNo);
        return RestApiUtil.getByReqParam("iais-application:8883/iais-application/application/{AppNo}",map, ApplicationViewDto.class);

    }

    @Override
    public boolean isAllApplicationSubmit(String appNo) {
        boolean result = false;
        ApplicationDto applicationDto = getApplicaitonByAppNo(appNo);

        if(applicationDto != null){
            String appGroupId = applicationDto.getAppGrpId();
            //todo:judge the All Applicaitons Submit
        }else{
            log.error(StringUtil.changeForLog("The applicationDto is null"));
        }

        return result;
    }

    @Override
    public ApplicationDto getApplicaitonByAppNo(String appNo) {
        return   RestApiUtil.getByPathParam(RestApiUrlConsts.APPLICATION_APPLICATION_APPNO,appNo,ApplicationDto.class);
    }

    @Override
    public ApplicationDto updateApplicaiton(ApplicationDto applicationDto) {
        return RestApiUtil.update(RestApiUrlConsts.IAIS_APPLICATION_BE,applicationDto,ApplicationDto.class);
    }
}
