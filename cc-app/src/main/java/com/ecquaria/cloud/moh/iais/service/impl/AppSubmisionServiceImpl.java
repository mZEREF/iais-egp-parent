package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.eventbus.SubmitReq;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.feign.EventBusClient;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.stereotype.Service;

/**
 * AppSubmisionServiceImpl
 *
 * @author suocheng
 * @date 11/6/2019
 */
@Service
public class AppSubmisionServiceImpl implements AppSubmissionService {
    @Autowired
    private EventBusClient eventBusClient;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

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

    @Override
    public AppSubmissionDto doSaveDraft(AppSubmissionDto appSubmissionDto) {
        return null;
    }
}
