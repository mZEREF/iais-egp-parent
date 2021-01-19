package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;

/**
 * @author weilu
 * date 2019/11/9 14:00
 */
public interface PaymentRequestService {
    /**
     * save paymentRequestDto
     * @param paymentRequestDto
     */
    void savePaymentRequestDto (PaymentRequestDto paymentRequestDto);

}
