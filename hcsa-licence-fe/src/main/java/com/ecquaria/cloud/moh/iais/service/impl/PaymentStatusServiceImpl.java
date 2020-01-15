package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.PaymentStatusService;
import com.ecquaria.cloud.moh.iais.service.client.AppPaymentStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
    public void checkPaymentStatus(List<String> reqRefNos) {
        List<PaymentDto> paymentDtos = appPaymentStatusClient.getPaymentDtoByReqRefNos(reqRefNos).getEntity();
        if (paymentDtos.isEmpty() && paymentDtos != null) {
            return;
        }
        for (PaymentDto paymentDto : paymentDtos) {
            String status = paymentDto.getPmtStatus();
            String reqRefNo = paymentDto.getReqRefNo();
            ApplicationGroupDto applicationGroupDto = applicationClient.getApplicationGroup(reqRefNo).getEntity();
            if (applicationGroupDto == null) {
                return;
            }
            String pmtStatus = applicationGroupDto.getPmtStatus();
            if (!"success".equals(pmtStatus)) {
                //update applicationGroup
                applicationGroupDto.setPmtStatus(status);
                applicationClient.doUpDate(applicationGroupDto).getEntity();
                //update application
                List<ApplicationDto> applicationDtos = applicationClient.listApplicationByGroupId(reqRefNo).getEntity();
                if (applicationDtos.isEmpty() && applicationDtos == null) {
                    return;
                }
                List<String> appNos = new ArrayList<>();
                for(ApplicationDto applicationDto :applicationDtos){
                    appNos.add(applicationDto.getApplicationNo());
                    ApplicationDto updateApplicationDto = applicationClient.updateApplication(applicationDto).getEntity();
                    HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
                    HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
                    gatewayClient.routePaymentStatus(updateApplicationDto, signature.date(), signature.authorization(), signature2.date(), signature2.authorization()).getEntity();
                }

            }
        }
    }

    @Override
    public void sendEmail(List<String> appNos) {


    }
}
