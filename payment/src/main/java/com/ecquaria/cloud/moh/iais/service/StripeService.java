package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

/**
 * StripeService
 *
 * @author junyu
 * @date 2020/7/21
 */
public interface StripeService {
    Session createSession(SessionCreateParams params) throws StripeException;
    Session retrieveSession(String csId) throws StripeException;
    PaymentIntent retrievePaymentIntent(String pi);
    void retrievePayment(PaymentRequestDto paymentRequestDto) throws StripeException;

    PaymentIntent retrieveEicPaymentIntent(String pi);
    Session createEicSession(SessionCreateParams params) ;
    Session retrieveEicSession(String csId) ;
}

