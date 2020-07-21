package com.ecquaria.cloud.moh.iais.service;

import com.stripe.exception.StripeException;
import com.stripe.model.Account;
import com.stripe.net.RequestOptions;

/**
 * StripeService
 *
 * @author junyu
 * @date 2020/7/21
 */
public interface StripeService {
    Account createAccount() throws StripeException;
    RequestOptions connectedAccounts(String id) ;
}
