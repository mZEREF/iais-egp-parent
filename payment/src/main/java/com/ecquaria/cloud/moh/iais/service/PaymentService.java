package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;

/**
 * @author weilu
 * date 2019/11/9 15:59
 */
public interface PaymentService {



    void retrieveNetsPayment(PaymentRequestDto paymentRequestDto) throws Exception;
}
