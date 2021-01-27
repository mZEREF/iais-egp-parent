package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.service.StripeService;
import com.ecquaria.cloud.moh.iais.service.client.PaymentAppGrpClient;
import com.ecquaria.cloud.moh.iais.service.client.PaymentClient;
import com.ecquaria.cloud.payment.PaymentTransactionEntity;
import com.ecquaria.egp.core.payment.api.config.GatewayConfig;
import com.stripe.Stripe;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.ApiResource;
import com.stripe.net.FormEncoder;
import com.stripe.net.HttpContent;
import com.stripe.net.RequestOptions;
import com.stripe.param.checkout.SessionCreateParams;
import com.stripe.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * StripeServiceImpl
 *
 * @author junyu
 * @date 2020/7/21
 */
@Service
@Slf4j
public class StripeServiceImpl implements StripeService {
    @Autowired
    PaymentClient paymentClient;
    @Autowired
    PaymentAppGrpClient paymentAppGrpClient;

    @Value("${iais.inter.gateway.url}")
    private String gateWayUrl;

    @Qualifier(value = "iaisRestTemplate")
    private RestTemplate restTemplate=new RestTemplate();

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
    public void retrievePayment(PaymentRequestDto paymentRequestDto) throws AuthenticationException, InvalidRequestException {
        Session session=retrieveEicSession(paymentRequestDto.getQueryCode());
        PaymentIntent paymentIntent=retrieveEicPaymentIntent(session.getPaymentIntent());

        PaymentDto paymentDto=paymentClient.getPaymentDtoByReqRefNo(paymentRequestDto.getReqRefNo()).getEntity();
        String appGrpNo;
        try{
            appGrpNo=paymentRequestDto.getReqRefNo().substring(0,'_');
        }catch (Exception e){
            appGrpNo=paymentRequestDto.getReqRefNo();
        }
        ApplicationGroupDto applicationGroupDto=paymentAppGrpClient.paymentUpDateByGrpNo(appGrpNo).getEntity();
        if(paymentDto!=null){
            if(paymentIntent!=null && "succeeded".equals(paymentIntent.getStatus())){
                paymentDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_SUCCESS);
                paymentRequestDto.setStatus(PaymentTransactionEntity.TRANS_STATUS_SUCCESS);
                applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
            }else if(paymentIntent!=null && "canceled".equals(paymentIntent.getStatus())){
                paymentDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
                paymentRequestDto.setStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
                //applicationGroupDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
            }
            paymentClient.saveHcsaPayment(paymentDto);
        }else{
            if(paymentIntent!=null && "succeeded".equals(paymentIntent.getStatus())){
                paymentRequestDto.setStatus(PaymentTransactionEntity.TRANS_STATUS_SUCCESS);
                applicationGroupDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_SUCCESS);
            }else {
                paymentRequestDto.setStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
                //applicationGroupDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
            }
        }
        paymentClient.updatePaymentResquset(paymentRequestDto);
        paymentAppGrpClient.doUpDate(applicationGroupDto);
    }

    @Override
    public PaymentIntent retrieveEicPaymentIntent(String pi) throws AuthenticationException, InvalidRequestException {
        Stripe.apiKey = GatewayConfig.stripeKey;
        String strGWPostURL= gateWayUrl+"/v1/stripe";
        String url = String.format("%s%s", strGWPostURL, String.format("/v1/payment_intents/%s", ApiResource.urlEncodeId(pi)));


        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        ApiResource.RequestMethod method=ApiResource.RequestMethod.GET;
        RequestOptions options=RequestOptions.getDefault();
        // Accept
        headers.set("Accept", "application/json");
        // Accept-Charset
        headers.set("Accept-Charset", ApiResource.CHARSET.name());
        // Authorization
        String apiKey = GatewayConfig.stripeKey;
        if (apiKey == null) {
            throw new AuthenticationException("No API key provided. Set your API key using `Stripe.apiKey = \"<API-KEY>\"`. You can generate API keys from the Stripe Dashboard. See https://stripe.com/docs/api/authentication for details or contact support at https://support.stripe.com/email if you have any questions.", null, null, 0);
        } else if (apiKey.isEmpty()) {
            throw new AuthenticationException("Your API key is invalid, as it is an empty string. You can double-check your API key from the Stripe Dashboard. See https://stripe.com/docs/api/authentication for details or contact support at https://support.stripe.com/email if you have any questions.", null, null, 0);
        } else if (StringUtils.containsWhitespace(apiKey)) {
            throw new AuthenticationException("Your API key is invalid, as it contains whitespace. You can double-check your API key from the Stripe Dashboard. See https://stripe.com/docs/api/authentication for details or contact support at https://support.stripe.com/email if you have any questions.", null, null, 0);
        }
        headers.set("Authorization", String.format("Bearer %s", apiKey));
        // Stripe-Version
        if (options.getStripeVersionOverride() != null) {
            headers.set("Stripe-Version", options.getStripeVersionOverride());
        } else if (options.getStripeVersion() != null) {
            headers.set("Stripe-Version", options.getStripeVersion());
        } else {
            throw new IllegalStateException("Either `stripeVersion` or `stripeVersionOverride` value must be set.");
        }
        // Stripe-Account
        if (options.getStripeAccount() != null) {
            headers.set("Stripe-Account",
                    options.getStripeAccount());
        }
        // Idempotency-Key
        if (options.getIdempotencyKey() != null) {
            headers.set("Idempotency-Key", options.getIdempotencyKey());
        } else if (method == ApiResource.RequestMethod.POST) {
            headers.set("Idempotency-Key", UUID.randomUUID().toString());
        }

        HttpEntity<byte[]> entity = new HttpEntity<byte[]>(null,
                headers);
        ResponseEntity<String> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        PaymentIntent session=ApiResource.GSON.fromJson(response.getBody(), PaymentIntent.class);
        return session;
    }

    @Override
    public Session createEicSession(SessionCreateParams params) throws AuthenticationException, IOException {
        Stripe.apiKey = GatewayConfig.stripeKey;
        String strGWPostURL= gateWayUrl+"/v1/stripe";
        String url = String.format("%s%s", strGWPostURL, "/v1/checkout/sessions");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ApiResource.RequestMethod method=ApiResource.RequestMethod.POST;
        RequestOptions options=RequestOptions.getDefault();
        // Accept
        headers.set("Accept", "application/json");
        // Accept-Charset
        headers.set("Accept-Charset", ApiResource.CHARSET.name());
        // Authorization
        String apiKey = GatewayConfig.stripeKey;
        if (apiKey == null) {
            throw new AuthenticationException("No API key provided. Set your API key using `Stripe.apiKey = \"<API-KEY>\"`. You can generate API keys from the Stripe Dashboard. See https://stripe.com/docs/api/authentication for details or contact support at https://support.stripe.com/email if you have any questions.", null, null, 0);
        } else if (apiKey.isEmpty()) {
            throw new AuthenticationException("Your API key is invalid, as it is an empty string. You can double-check your API key from the Stripe Dashboard. See https://stripe.com/docs/api/authentication for details or contact support at https://support.stripe.com/email if you have any questions.", null, null, 0);
        } else if (StringUtils.containsWhitespace(apiKey)) {
            throw new AuthenticationException("Your API key is invalid, as it contains whitespace. You can double-check your API key from the Stripe Dashboard. See https://stripe.com/docs/api/authentication for details or contact support at https://support.stripe.com/email if you have any questions.", null, null, 0);
        }
        headers.set("Authorization", String.format("Bearer %s", apiKey));
        // Stripe-Version
        if (options.getStripeVersionOverride() != null) {
            headers.set("Stripe-Version", options.getStripeVersionOverride());
        } else if (options.getStripeVersion() != null) {
            headers.set("Stripe-Version", options.getStripeVersion());
        } else {
            throw new IllegalStateException("Either `stripeVersion` or `stripeVersionOverride` value must be set.");
        }
        // Stripe-Account
        if (options.getStripeAccount() != null) {
            headers.set("Stripe-Account",
                    options.getStripeAccount());
        }
        // Idempotency-Key
        if (options.getIdempotencyKey() != null) {
            headers.set("Idempotency-Key", options.getIdempotencyKey());
        } else if (method == ApiResource.RequestMethod.POST) {
            headers.set("Idempotency-Key", UUID.randomUUID().toString());
        }
        HttpContent httpContent= FormEncoder.createHttpContent(params.toMap());
        HttpEntity<byte[]> entity = new HttpEntity<byte[]>(httpContent.byteArrayContent(),
                headers);

        ResponseEntity<String> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        Session session=ApiResource.GSON.fromJson(response.getBody(), Session.class);
        return session;
    }

    @Override
    public Session retrieveEicSession(String csId) throws AuthenticationException, InvalidRequestException {
        Stripe.apiKey = GatewayConfig.stripeKey;
        String strGWPostURL= gateWayUrl+"/v1/stripe/";
        String url = String.format("%s%s", strGWPostURL, String.format("/v1/checkout/sessions/%s", ApiResource.urlEncodeId(csId)));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        ApiResource.RequestMethod method=ApiResource.RequestMethod.GET;
        RequestOptions options=RequestOptions.getDefault();
        // Accept
        headers.set("Accept", "application/json");
        // Accept-Charset
        headers.set("Accept-Charset", ApiResource.CHARSET.name());
        // Authorization
        String apiKey = GatewayConfig.stripeKey;
        if (apiKey == null) {
            throw new AuthenticationException("No API key provided. Set your API key using `Stripe.apiKey = \"<API-KEY>\"`. You can generate API keys from the Stripe Dashboard. See https://stripe.com/docs/api/authentication for details or contact support at https://support.stripe.com/email if you have any questions.", null, null, 0);
        } else if (apiKey.isEmpty()) {
            throw new AuthenticationException("Your API key is invalid, as it is an empty string. You can double-check your API key from the Stripe Dashboard. See https://stripe.com/docs/api/authentication for details or contact support at https://support.stripe.com/email if you have any questions.", null, null, 0);
        } else if (StringUtils.containsWhitespace(apiKey)) {
            throw new AuthenticationException("Your API key is invalid, as it contains whitespace. You can double-check your API key from the Stripe Dashboard. See https://stripe.com/docs/api/authentication for details or contact support at https://support.stripe.com/email if you have any questions.", null, null, 0);
        }
        headers.set("Authorization", String.format("Bearer %s", apiKey));
        // Stripe-Version
        if (options.getStripeVersionOverride() != null) {
            headers.set("Stripe-Version", options.getStripeVersionOverride());
        } else if (options.getStripeVersion() != null) {
            headers.set("Stripe-Version", options.getStripeVersion());
        } else {
            throw new IllegalStateException("Either `stripeVersion` or `stripeVersionOverride` value must be set.");
        }
        // Stripe-Account
        if (options.getStripeAccount() != null) {
            headers.set("Stripe-Account",
                    options.getStripeAccount());
        }
        // Idempotency-Key
        if (options.getIdempotencyKey() != null) {
            headers.set("Idempotency-Key", options.getIdempotencyKey());
        } else if (method == ApiResource.RequestMethod.POST) {
            headers.set("Idempotency-Key", UUID.randomUUID().toString());
        }
        HttpEntity<byte[]> entity = new HttpEntity<byte[]>(null,
                headers);
        ResponseEntity<String> response =
                restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        Session session=ApiResource.GSON.fromJson(response.getBody(), Session.class);
        return session;
    }
    private static com.stripe.net.HttpHeaders buildHeaders(ApiResource.RequestMethod method, RequestOptions options) throws AuthenticationException {
        Map<String, List<String>> headerMap = new HashMap<String, List<String>>();
        // Accept
        headerMap.put("Accept", Arrays.asList("application/json"));
        // Accept-Charset
        headerMap.put("Accept-Charset", Arrays.asList(ApiResource.CHARSET.name()));
        // Authorization
        String apiKey = options.getApiKey();
        if (apiKey == null) {
            throw new AuthenticationException("No API key provided. Set your API key using `Stripe.apiKey = \"<API-KEY>\"`. You can generate API keys from the Stripe Dashboard. See https://stripe.com/docs/api/authentication for details or contact support at https://support.stripe.com/email if you have any questions.", null, null, 0);
        } else if (apiKey.isEmpty()) {
            throw new AuthenticationException("Your API key is invalid, as it is an empty string. You can double-check your API key from the Stripe Dashboard. See https://stripe.com/docs/api/authentication for details or contact support at https://support.stripe.com/email if you have any questions.", null, null, 0);
        } else if (StringUtils.containsWhitespace(apiKey)) {
            throw new AuthenticationException("Your API key is invalid, as it contains whitespace. You can double-check your API key from the Stripe Dashboard. See https://stripe.com/docs/api/authentication for details or contact support at https://support.stripe.com/email if you have any questions.", null, null, 0);
        }
        headerMap.put("Authorization", Arrays.asList(String.format("Bearer %s", apiKey)));
        // Stripe-Version
        if (options.getStripeVersionOverride() != null) {
            headerMap.put("Stripe-Version", Arrays.asList(options.getStripeVersionOverride()));
        } else if (options.getStripeVersion() != null) {
            headerMap.put("Stripe-Version", Arrays.asList(options.getStripeVersion()));
        } else {
            throw new IllegalStateException("Either `stripeVersion` or `stripeVersionOverride` value must be set.");
        }
        // Stripe-Account
        if (options.getStripeAccount() != null) {
            headerMap.put("Stripe-Account", Arrays.asList(options.getStripeAccount()));
        }
        // Idempotency-Key
        if (options.getIdempotencyKey() != null) {
            headerMap.put("Idempotency-Key", Arrays.asList(options.getIdempotencyKey()));
        } else if (method == ApiResource.RequestMethod.POST) {
            headerMap.put("Idempotency-Key", Arrays.asList(UUID.randomUUID().toString()));
        }
        return com.stripe.net.HttpHeaders.of(headerMap);
    }

}
