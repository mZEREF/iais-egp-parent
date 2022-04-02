package com.ecquaria.cloud.moh.iais.service.impl;

/*
 *File Name: MessageServiceImpl
 *Creator: yichen
 *Creation time:2019/8/2 10:49
 *Describe:
 */

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageQueryDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.MessageService;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.Date;
import java.util.List;

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

    @Value("${spring.application.name}")
    private String currentApp;

    @Value("${iais.current.domain}")
    private String currentDomain;

    @Autowired
    private EicRequestTrackingHelper trackingHelper;

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

            EicRequestTrackingDto postSaveTrack = trackingHelper.clientSaveEicRequestTracking(EicClientConstant.SYSTEM_ADMIN_CLIENT, MessageServiceImpl.class.getName(),
                    "callEicCreateErrorMessage", currentApp + "-" + currentDomain,
                    MessageDto.class.getName(), JsonUtil.parseToJson(result.getEntity()));

            try {
                FeignResponseEntity<EicRequestTrackingDto> fetchResult = trackingHelper.getEicClient().getPendingRecordByReferenceNumber(postSaveTrack.getRefNo());
                if (HttpStatus.SC_OK == fetchResult.getStatusCode()) {
                    EicRequestTrackingDto entity = fetchResult.getEntity();
                    if (AppConsts.EIC_STATUS_PENDING_PROCESSING.equals(entity.getStatus())){
                        callEicCreateErrorMessage(result.getEntity());
                        entity.setProcessNum(1);
                        Date now = new Date();
                        entity.setFirstActionAt(now);
                        entity.setLastActionAt(now);
                        entity.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                        trackingHelper.getEicClient().saveEicTrack(entity);
                    }
                }

            }catch (Exception e){
                log.error(StringUtil.changeForLog("encounter failure when sync message to fe " + e.getMessage()), e);
            }

        }
    }


    public void callEicCreateErrorMessage(MessageDto msg){
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        MessageDto postSaveMsg = msg;
        eicGatewayClient.syncMessageToFe(postSaveMsg, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
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
