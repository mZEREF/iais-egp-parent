package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.service.RescheduleService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * RescheduleServiceImpl
 *
 * @author junyu
 * @date 2020/6/18
 */
@Slf4j
@Service
public class RescheduleServiceImpl implements RescheduleService {
    @Autowired
    ApplicationClient applicationClient;
    @Autowired
    private FeEicGatewayClient feEicGatewayClient;


    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Override
    public void updateAppStatusCommPool(String[] appIds) {
        for (String appId:appIds
             ) {
            ApplicationDto applicationDto=applicationClient.getApplicationById(appId).getEntity();
            applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_RE_SCHEDULING_COMMON_POOL);
            applicationClient.updateApplication(applicationDto);
            //eic update to be
        }
    }
}
