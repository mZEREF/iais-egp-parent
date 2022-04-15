package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.service.client.EicClient;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import java.util.Date;
import java.util.List;
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

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;

    @Autowired
    private SystemBeLicClient systemBeLicClient;

    @Override
    public InterMessageDto saveInterMessage(InterMessageDto interMessageDto) {
        beEicGatewayClient.callEicWithTrack(interMessageDto, this::callEicInterMsg, this.getClass(),
                "callEicInterMsg", EicClientConstant.SYSTEM_ADMIN_CLIENT);
        return interMessageDto;
    }

    public void callEicInterMsg(InterMessageDto interMessageDto) {
        beEicGatewayClient.saveInboxMessage(interMessageDto).getEntity();
    }

    @Override
    public String getMessageNo() {
        return systemBeLicClient.messageID().getEntity();
    }
}
