package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * LicenceViewServiceImpl
 *
 * @author suocheng
 * @date 12/16/2019
 */
@Service
@Slf4j
public class LicenceViewServiceImpl implements LicenceViewService {

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Override
    public AppSubmissionDto getAppSubmissionByAppId(String appId) {

        return  applicationClient.getAppSubmissionByAppId(appId).getEntity();
    }

    @Override
    public AppEditSelectDto saveAppEditSelect(AppEditSelectDto appEditSelectDto) {
        return applicationClient.createAppEditSelectDto(appEditSelectDto).getEntity();
    }

    @Override
    public AppEditSelectDto saveAppEditSelectToFe(AppEditSelectDto appEditSelectDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return beEicGatewayClient.createAppEditSelectDto(appEditSelectDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
    }
}
