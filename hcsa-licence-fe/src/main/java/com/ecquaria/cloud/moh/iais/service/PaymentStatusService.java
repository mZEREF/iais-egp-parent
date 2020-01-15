package com.ecquaria.cloud.moh.iais.service;

import java.util.List;

/**
 * @author weilu
 * @date 2020/1/14 14:52
 */
public interface PaymentStatusService {

    void checkPaymentStatus(List<String> reqRefNos);
    void sendEmail(List<String> appNos);
}
