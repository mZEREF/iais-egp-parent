package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.AppealApplicaionService;
import com.ecquaria.cloud.moh.iais.service.client.AppealClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * AppealApplicaionServiceImpl
 *
 * @author suocheng
 * @date 2/11/2020
 */
@Service
@Slf4j
public class AppealApplicaionServiceImpl implements AppealApplicaionService {
    @Autowired
    private BeEicGatewayClient beEicGatewayClient;

    @Autowired
    private AppealClient appealClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Override
    public AppealApplicationDto updateFEAppealApplicationDto(String eventRefNum,String submissionId) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        AppEicRequestTrackingDto appEicRequestTrackingDto = getAppEicRequestTrackingDtoByRefNo(eventRefNum);
        AppealApplicationDto appealApplicationDto = getObjectApp(appEicRequestTrackingDto,AppealApplicationDto.class);
        if(appealApplicationDto!=null){
            appealApplicationDto = beEicGatewayClient.updateAppealApplication(appealApplicationDto, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization()).getEntity();
        }else{
            log.error(StringUtil.changeForLog("This eventReo can not get the AppEicRequestTrackingDto -->:"+eventRefNum));
        }
        return appealApplicationDto;
    }

    @Override
    public AppEicRequestTrackingDto getAppEicRequestTrackingDtoByRefNo(String refNo) {
        return appealClient.getAppEicRequestTrackingDto(refNo).getEntity();
    }
    private <T> T getObjectApp(AppEicRequestTrackingDto appEicRequestTrackingDto, Class<T> cls){
        T result = null;
        if(appEicRequestTrackingDto!=null){
            ObjectMapper mapper = new ObjectMapper();
            try {
                result = mapper.readValue(appEicRequestTrackingDto.getDtoObj(), cls);
            } catch (IOException e) {
                log.error(StringUtil.changeForLog(e.getMessage()),e);
            }
        }
        return  result;
    }
}
