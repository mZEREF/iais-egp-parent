package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        Stripe.apiKey = "sk_test_YGXYtjBWWLt6qhEqW34wu8Vg00iEFDMW4w";

        List<Object> requestedCapabilities =
                new ArrayList<>();
        requestedCapabilities.add("card_payments");
        requestedCapabilities.add("transfers");
        Map<String, Object> params = new HashMap<>();
        params.put("type", "custom");
        params.put("country", "US");
        params.put("email", "jenny.rosen@example.com");
        params.put(
                "requested_capabilities",
                requestedCapabilities
        );

        return Account.create(params);
    }

    @Override
    public RequestOptions connectedAccounts(String id) {
        return  RequestOptions.builder()
                .setStripeAccount(id)
                .build();
    }

    @Override
    public void authentication() {
        Stripe.apiKey = "sk_test_YGXYtjBWWLt6qhEqW34wu8Vg00iEFDMW4w";

        RequestOptions requestOptions = RequestOptions.builder()
                .setApiKey("sk_test_YGXYtjBWWLt6qhEqW34wu8Vg00iEFDMW4w")
                .build();

    }

    @Override
    public Charge createCharge(Map<String, Object> params) {
        Stripe.apiKey = "sk_test_YGXYtjBWWLt6qhEqW34wu8Vg00iEFDMW4w";

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
        Stripe.apiKey = "sk_test_YGXYtjBWWLt6qhEqW34wu8Vg00iEFDMW4w";

        Charge charge =
                null;
        try {
            charge = Charge.retrieve(id);
        } catch (StripeException e) {
            log.info(e.getMessage(),e);
        }
        return charge;
    }
}
