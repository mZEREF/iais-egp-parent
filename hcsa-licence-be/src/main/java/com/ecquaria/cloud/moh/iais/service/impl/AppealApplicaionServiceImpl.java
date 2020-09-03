package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppealApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.AppealApplicaionService;
import com.ecquaria.cloud.moh.iais.service.client.AppealClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.util.EicUtil;
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
        AppealApplicationDto appealApplicationDto = EicUtil.getObjectApp(appEicRequestTrackingDto,AppealApplicationDto.class);
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

}
