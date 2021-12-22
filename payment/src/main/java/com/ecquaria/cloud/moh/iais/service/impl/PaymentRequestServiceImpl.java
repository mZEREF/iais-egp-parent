package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.service.PaymentRequestService;
import com.ecquaria.cloud.moh.iais.service.client.PaymentClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author weilu
 * date 2019/11/9 14:00
 */
@Service
public class PaymentRequestServiceImpl implements PaymentRequestService {

    @Autowired
    private PaymentClient paymentClient;

    @Override
    public void savePaymentRequestDto(PaymentRequestDto paymentRequestDto) {
        paymentRequestDto.setSystemClientId(AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY);
        paymentClient.saveHcsaPaymentResquset(paymentRequestDto);
    }
}
