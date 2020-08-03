package com.ecquaria.cloud.moh.iais.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.checkout.SessionCreateParams;

import java.util.Map;

/**
 * StripeService
 *
 * @author junyu
 * @date 2020/7/21
 */
public interface StripeService {
    Account createAccount() throws StripeException;
    Session createSession(SessionCreateParams params) throws StripeException;
    Session retrieveSession(String csId) throws StripeException;
    RequestOptions connectedAccounts(String id) ;
    RequestOptions authentication();
    Charge createCharge(Map<String, Object> params);
    Charge retrieveCharge(String id);
    PaymentIntent createPaymentIntent( Map<String, Object> params);
    PaymentIntent retrievePaymentIntent(String pi);
    PaymentIntent confirmPaymentIntent(String pi,Map<String, Object> params);
    PaymentIntent capturePaymentIntent(String pi) throws StripeException;
    PaymentIntent cancelPaymentIntent(String pi) throws StripeException;
    PaymentMethod createPaymentMethod(Map<String, Object> params);
    PaymentMethod retrievePaymentMethod(String pm) throws StripeException;
}
