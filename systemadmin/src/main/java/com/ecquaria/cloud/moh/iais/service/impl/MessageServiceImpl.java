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
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.MessageService;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class MessageServiceImpl implements MessageService {

    @Autowired
    private SystemClient systemClient;

    @Autowired
    private EicGatewayClient eicGatewayClient;

    @SearchTrack(catalog = "systemAdmin", key = "queryMessage")
    @Override
    public SearchResult<MessageQueryDto> doQuery(SearchParam searchParam) {
        return systemClient.queryMessage(searchParam).getEntity();
    }

    @Override
    public void saveMessage(MessageDto messageDto) {
        messageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

        FeignResponseEntity<MessageDto> result = systemClient.saveMessage(messageDto);
        int statusCode = result.getStatusCode();
        if (statusCode == HttpStatus.SC_OK){
            //eicGatewayClient.callEicCreateErrorMessage(result.getEntity());
            eicGatewayClient.callEicWithTrack(result.getEntity(), eicGatewayClient::syncMessageToFe,
                    eicGatewayClient.getClass(), "syncMessageToFe");
        }
    }

    @Override
    public MessageDto getMessageById(String id) {
        return systemClient.getMessageByRowguid(id).getEntity();
    }

    @Override
    public List<String> listModuleTypes() {
        return IaisCommonUtils.getList(systemClient.listModuleTypes().getEntity());
    }

}
