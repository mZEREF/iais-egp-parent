package com.ecquaria.cloud.moh.iais.service.impl;

/*
 *File Name: MessageServiceImpl
 *Creator: yichen
 *Creation time:2019/8/2 10:49
 *Describe:
 */

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageQueryDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.MessageService;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Autowired
    private SystemClient systemClient;

    @Autowired
    private EicGatewayClient eicGatewayClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @SearchTrack(catalog = "message", key = "search")
    @Override
    public SearchResult<MessageQueryDto> doQuery(SearchParam searchParam) {
        return systemClient.queryMessage(searchParam).getEntity();
    }

    @Override
    public void saveMessage(MessageDto messageDto) {
        messageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

        int statusCode = systemClient.saveMessage(messageDto).getStatusCode();
        if (statusCode == HttpStatus.SC_OK){
            try {
                MessageDto retMsg = systemClient.saveMessage(messageDto).getEntity();
                HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
                HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
                eicGatewayClient.syncMessageToFe(retMsg, signature.date(), signature.authorization(),
                        signature2.date(), signature2.authorization()).getEntity();
            }catch (IaisRuntimeException e){
                log.error("encounter failure when sync message to fe " + e.getMessage());

                //set message to eic event
            }
        }
    }

    @Override
    public MessageDto getMessageById(String id) {
        return systemClient.getMessageByRowguid(id).getEntity();
    }

}
