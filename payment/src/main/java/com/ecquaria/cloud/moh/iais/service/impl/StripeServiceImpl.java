package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.service.StripeService;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.net.RequestOptions;
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
}
