package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.PaymentService;
import org.springframework.stereotype.Service;

/**
 * @author weilu
 * date 2019/11/9 16:01
 */
@Service
public class PaymentServiceImpl implements PaymentService {
    @Override
    public void savePayment(PaymentDto paymentDto) {
        RestApiUtil.save("iais-payment:8883/payment/tradingReply",paymentDto);
    }
}
