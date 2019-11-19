package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.PaymentRequestService;
import org.springframework.stereotype.Service;

/**
 * @author weilu
 * date 2019/11/9 14:00
 */
@Service
public class PaymentRequestServiceImpl implements PaymentRequestService {


    @Override
    public void savePaymentRequestDto(PaymentRequestDto paymentRequestDto) {
        RestApiUtil.save("iais-payment:8881/payment/duringThePayment",paymentRequestDto);
    }
}
