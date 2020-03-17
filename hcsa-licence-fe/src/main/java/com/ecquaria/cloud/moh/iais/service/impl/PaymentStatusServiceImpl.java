package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.PaymentStatusService;
import com.ecquaria.cloud.moh.iais.service.client.AppPaymentStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author weilu
 * @date 2020/1/14 17:12
 */
@Service
public class PaymentStatusServiceImpl implements PaymentStatusService {

    @Autowired
    private AppPaymentStatusClient appPaymentStatusClient;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private FeEicGatewayClient gatewayClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;


    @Override
    public void checkPaymentStatusAndUpdateAppGrp() {
        //get payment info
        List<PaymentDto> paymentDtos = appPaymentStatusClient.getPaymentDtosByReqRefNos().getEntity();
        if (paymentDtos != null && !paymentDtos.isEmpty()) {
            for (PaymentDto paymentDto : paymentDtos) {
                String reqRefNo = paymentDto.getReqRefNo();
                String pmtStatusPayment = paymentDto.getPmtStatus();
                List<String> appGrpIds = IaisCommonUtils.genNewArrayList();
                appGrpIds.add(reqRefNo);
                //get appGrp info
                List<ApplicationGroupDto> appGrps = applicationClient.getApplicationGroupsByIds(appGrpIds).getEntity();
                if (appGrps != null && !appGrps.isEmpty()) {
                    ApplicationGroupDto applicationGroupDto = appGrps.get(0);
                    String pmtStatus = applicationGroupDto.getPmtStatus();
                    if (!ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS.equals(pmtStatus) && !ApplicationConsts.PAYMENT_STATUS_GIRO_PAY_SUCCESS.equals(pmtStatus)) {
                        applicationGroupDto.setPmtStatus(pmtStatusPayment);
                        applicationClient.doUpDate(applicationGroupDto).getEntity();
                        //jiang appGrpDto chuan dao iais
                        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
                        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
                        gatewayClient.routePaymentStatus(applicationGroupDto, signature.date(), signature.authorization(), signature2.date(), signature2.authorization()).getEntity();
                    }
                }
            }
        }
    }
}