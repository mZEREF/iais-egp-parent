package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.eventbus.SubmitReq;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.feign.EventBusClient;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.service.AppSubmisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AppSubmisionServiceImpl
 *
 * @author suocheng
 * @date 11/6/2019
 */
@Service
public class AppSubmisionServiceImpl implements AppSubmisionService {
    @Autowired
    private EventBusClient eventBusClient;

    @Override
    public void submit(AppSubmissionDto appSubmissionDto) {
        int timeoutSec = 300;
        SubmitReq sr = new SubmitReq();
        sr.setProject("iais");
        sr.setService("appsubmit");
        sr.setData(JsonUtil.parseToJson(appSubmissionDto));
        sr.setWait(true);
        sr.setTotalWait(timeoutSec);
        eventBusClient.submit(sr);
    }
}
