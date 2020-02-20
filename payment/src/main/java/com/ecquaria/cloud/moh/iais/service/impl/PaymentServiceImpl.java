package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.service.PaymentService;
import com.ecquaria.cloud.moh.iais.service.client.PaymentClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author weilu
 * date 2019/11/9 16:01
 */
@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentClient paymentClient;

    @Override
    public PaymentDto savePayment(PaymentDto paymentDto) {
        PaymentDto paymentDtoSave = paymentClient.saveHcsaPayment(paymentDto).getEntity();
        return paymentDtoSave;
    }
}
