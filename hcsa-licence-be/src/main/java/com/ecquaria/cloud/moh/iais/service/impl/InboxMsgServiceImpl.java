package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.client.FEEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * InboxMsgServiceImpl
 *
 * @author suocheng
 * @date 12/17/2019
 */
@Service
public class InboxMsgServiceImpl implements InboxMsgService {

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;

    @Autowired
    private FEEicGatewayClient feEicGatewayClient;

    @Autowired
    private SystemBeLicClient systemBeLicClient;

    @Override
    public InterMessageDto saveInterMessage(InterMessageDto interMessageDto) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        return feEicGatewayClient.saveInboxMessage(interMessageDto, signature.date(), signature.authorization()).getEntity();
    }

    @Override
    public String getMessageNo() {
        return systemBeLicClient.messageID().getEntity();
    }
}
