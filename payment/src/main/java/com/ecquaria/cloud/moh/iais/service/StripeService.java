package com.ecquaria.cloud.moh.iais.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;

import java.util.Map;

/**
 * StripeService
 *
 * @author junyu
 * @date 2020/7/21
 */
public interface StripeService {
    Account createAccount() throws StripeException;
    RequestOptions connectedAccounts(String id) ;
    void authentication();
    Charge createCharge(Map<String, Object> params);
    Charge retrieveCharge(String id);
}
