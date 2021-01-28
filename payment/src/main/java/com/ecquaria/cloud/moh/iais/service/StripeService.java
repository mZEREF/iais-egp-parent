package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import java.io.IOException;

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

    PaymentIntent retrieveEicPaymentIntent(String pi) throws AuthenticationException, InvalidRequestException;
    Session createEicSession(SessionCreateParams params) throws AuthenticationException, IOException;
    Session retrieveEicSession(String csId) throws AuthenticationException, InvalidRequestException;
}

