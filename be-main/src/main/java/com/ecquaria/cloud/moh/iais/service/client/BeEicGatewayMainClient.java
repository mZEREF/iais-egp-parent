package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * EicGatewayClient
 *
 * @author suocheng
 * @date 2019/12/14 17:33
 */
@Component
public class BeEicGatewayMainClient {
    @Value("${iais.intra.gateway.url}")
    private String gateWayUrl;
    @Value("${iais.hmac.keyId}")
    private String keyId;

    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;

    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    public FeignResponseEntity<ApplicationDto> updateApplication(ApplicationDto applicationDto,
                                                          String date, String authorization, String dateSec,
                                                          String authorizationSec) {
         return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/iais-application", HttpMethod.PUT, applicationDto,
                 MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, ApplicationDto.class);
     }

    public FeignResponseEntity<InterMessageDto> saveInboxMessage(InterMessageDto interInboxDto,
                                                          String date, String authorization, String dateSec,
                                                          String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBody(gateWayUrl + "/v1/iais-inter-inbox-message", HttpMethod.POST, interInboxDto,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, InterMessageDto.class);
    }

    public FeignResponseEntity<List> doStripeRefunds(List<AppReturnFeeDto> appReturnFeeDtos,
                                                     String date, String authorization, String dateSec,
                                                     String authorizationSec) {
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/stripe-refund", HttpMethod.POST, appReturnFeeDtos,
                MediaType.APPLICATION_JSON, date, authorization, dateSec, authorizationSec, PaymentRequestDto.class);
    }

    public FeignResponseEntity<List> updateFeApplicationGroupStatus(List<ApplicationGroupDto> applicationGroupDtos) {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        return IaisEGPHelper.callEicGatewayWithBodyForList(gateWayUrl + "/v1/app-grp-status", HttpMethod.PUT, applicationGroupDtos,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(), signature2.date(), signature2.authorization(),
                ApplicationGroupDto.class);
    }
}
