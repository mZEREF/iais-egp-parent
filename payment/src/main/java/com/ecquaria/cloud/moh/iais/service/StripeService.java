package com.ecquaria.cloud.moh.iais.service;

import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
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
    RequestOptions connectedAccounts(String id) ;
    RequestOptions authentication();
    PaymentIntent retrievePaymentIntent(String pi);
    Refund createRefund(String pi,Long amount) throws StripeException;
    Refund retrieveRefund(String ri) throws StripeException;
}

