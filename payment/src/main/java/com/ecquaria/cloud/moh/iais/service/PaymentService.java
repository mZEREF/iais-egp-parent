package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.service.impl.SoapiS2S;
import com.ecquaria.cloud.moh.iais.service.impl.SoapiS2SResponse;

/**
 * @author weilu
 * date 2019/11/9 15:59
 */
public interface PaymentService {



    void retrieveNetsPayment(PaymentRequestDto paymentRequestDto) throws Exception;
    SoapiS2SResponse sendTxnQueryReqToGW(String secretKey, String keyId, SoapiS2S soapiTxnQueryReq)throws Exception;

    void retrievePayNowPayment(PaymentRequestDto paymentRequestDto) ;

}
