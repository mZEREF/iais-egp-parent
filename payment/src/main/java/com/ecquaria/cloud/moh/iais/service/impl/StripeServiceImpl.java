package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.service.StripeService;
import com.ecquaria.egp.core.payment.api.config.GatewayConfig;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.PaymentMethod;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.AccountCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * StripeServiceImpl
 *
 * @author junyu
 * @date 2020/7/21
 */
@Service
@Slf4j
public class StripeServiceImpl implements StripeService {
    @Override
    public Account createAccount() throws StripeException {
        Stripe.apiKey = GatewayConfig.stripeKey;
        AccountCreateParams params =
                AccountCreateParams.builder()
                        .setCountry("US")
                        .setType(AccountCreateParams.Type.CUSTOM)
                        .addRequestedCapability(AccountCreateParams.RequestedCapability.CARD_PAYMENTS)
                        .addRequestedCapability(AccountCreateParams.RequestedCapability.TRANSFERS)
                        .build();
        return Account.create(params);
    }

    @Override
    public Session createSession(SessionCreateParams params) throws StripeException {
        // Set your secret key. Remember to switch to your live secret key in production!
// See your keys here: https://dashboard.stripe.com/account/apikeys
        Stripe.apiKey = GatewayConfig.stripeKey;


        Session session = Session.create(params);
        return session;

    }

    @Override
    public Session retrieveSession(String csId) throws StripeException {
        Stripe.apiKey = GatewayConfig.stripeKey;

        Session session =
                Session.retrieve(
                        csId
                );
        return session;
    }

    @Override
    public RequestOptions connectedAccounts(String id) {
        return  RequestOptions.builder()
                .setStripeAccount(id)
                .build();
    }

    @Override
    public RequestOptions authentication() {
        Stripe.apiKey = GatewayConfig.stripeKey;

        RequestOptions requestOptions = RequestOptions.builder()
                .setApiKey(GatewayConfig.stripeKey)
                .build();
        return requestOptions;

    }

    @Override
    public Charge createCharge(Map<String, Object> params) {
        Stripe.apiKey = GatewayConfig.stripeKey;

        Charge charge =null;
        try {
             charge = Charge.create(params);
        } catch (StripeException e) {
        log.info(e.getMessage(),e);
        }
        return charge;
    }

    @Override
    public Charge retrieveCharge(String id) {
        Stripe.apiKey = GatewayConfig.stripeKey;

        Charge charge =
                null;
        try {
            charge = Charge.retrieve(id);
        } catch (StripeException e) {
            log.info(e.getMessage(),e);
        }
        return charge;
    }

    @Override
    public PaymentIntent createPaymentIntent( Map<String, Object> params) {
        Stripe.apiKey = GatewayConfig.stripeKey;

//        List<Object> paymentMethodTypes =
//                new ArrayList<>();
//        paymentMethodTypes.add("card");
//        Map<String, Object> params = new HashMap<>();
//        params.put("amount", 2000);
//        params.put("currency", "sgd");
//        params.put(
//                "payment_method_types",
//                paymentMethodTypes
//        );

        PaymentIntent paymentIntent =
                null;
        try {
            paymentIntent = PaymentIntent.create(params);
        } catch (StripeException e) {
            log.info(e.getMessage(),e);
        }

        return paymentIntent;
    }

    @Override
    public PaymentIntent retrievePaymentIntent(String pi) {
        Stripe.apiKey = GatewayConfig.stripeKey;

        PaymentIntent paymentIntent =
                null;
        try {
            paymentIntent = PaymentIntent.retrieve(
                    pi
            );
        } catch (StripeException e) {
            log.info(e.getMessage(),e);
        }
        return paymentIntent;
    }

    @Override
    public PaymentIntent confirmPaymentIntent(String pi, Map<String, Object> params) {
        Stripe.apiKey = GatewayConfig.stripeKey;

// To create a PaymentIntent for confirmation, see our guide at: https://stripe.com/docs/payments/payment-intents/creating-payment-intents#creating-for-automatic
        PaymentIntent paymentIntent =
                null;
        try {
            paymentIntent = PaymentIntent.retrieve(
                    pi
            );
        } catch (StripeException e) {
            log.info(e.getMessage(),e);
        }

//        Map<String, Object> params = new HashMap<>();
//        params.put("payment_method", "pm_card_visa");

        PaymentIntent updatedPaymentIntent =
                null;
        try {
            updatedPaymentIntent = paymentIntent.confirm(params);
        } catch (StripeException e) {
            log.info(e.getMessage(),e);
        }
        return updatedPaymentIntent;
    }

    @Override
    public PaymentIntent capturePaymentIntent(String pi) throws StripeException {
        Stripe.apiKey = GatewayConfig.stripeKey;

        PaymentIntent paymentIntent =
                PaymentIntent.retrieve(
                        pi
                );

        PaymentIntent updatedPaymentIntent =
                paymentIntent.capture();
        return updatedPaymentIntent;
    }

    @Override
    public PaymentIntent cancelPaymentIntent(String pi) throws StripeException {
        Stripe.apiKey = GatewayConfig.stripeKey;

        PaymentIntent paymentIntent =
                PaymentIntent.retrieve(
                        pi
                );

        PaymentIntent updatedPaymentIntent =
                paymentIntent.cancel();
        return updatedPaymentIntent;
    }

    @Override
    public PaymentMethod createPaymentMethod(Map<String, Object> params) {
        Stripe.apiKey = GatewayConfig.stripeKey;

        PaymentMethod paymentMethod=null;
        try {
            paymentMethod=
                    PaymentMethod.create(params);
        } catch (StripeException e) {
            log.info(e.getMessage(),e);
        }
        return paymentMethod;
    }

    @Override
    public PaymentMethod retrievePaymentMethod(String pm) throws StripeException {
        Stripe.apiKey = GatewayConfig.stripeKey;

        PaymentMethod paymentMethod =
                PaymentMethod.retrieve(
                        "pm_1EUn5VJnvmXwwenzTDs9xSu5"
                );
        return paymentMethod;
    }
}
