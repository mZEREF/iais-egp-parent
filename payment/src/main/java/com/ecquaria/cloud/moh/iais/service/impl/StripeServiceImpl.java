package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.StripeService;
import com.ecquaria.cloud.moh.iais.service.client.PaymentAppGrpClient;
import com.ecquaria.cloud.moh.iais.service.client.PaymentClient;
import com.ecquaria.cloud.payment.PaymentTransactionEntity;
import com.ecquaria.egp.core.payment.api.config.GatewayConfig;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
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

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.inter.gateway.url}")
    private String gateWayUrl;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;

    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

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
    public void retrievePayment(PaymentRequestDto paymentRequestDto)  {
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
    public PaymentIntent retrieveEicPaymentIntent(String pi) {
        Stripe.apiKey = GatewayConfig.stripeKey;
        String strGWPostURL= gateWayUrl+"/v1/stripe/v1/payment_intents";

        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.set("date", signature.date());
        headers.set("authorization", signature.authorization());
        headers.set("date-Secondary", signature2.date());
        headers.set("authorization-Secondary", signature2.authorization());
        HttpEntity<String> entity = new HttpEntity<String>(pi,
                headers);
        ResponseEntity<PaymentIntent> response =
                restTemplate.exchange(strGWPostURL, HttpMethod.POST, entity, PaymentIntent.class);
        return response.getBody();
    }

    @Override
    public Session createEicSession(SessionCreateParams params) {
        Stripe.apiKey = GatewayConfig.stripeKey;
        String strGWPostURL= gateWayUrl+"/v1/stripe/v1/checkout/sessions";

        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.set("date", signature.date());
        headers.set("authorization", signature.authorization());
        headers.set("date-Secondary", signature2.date());
        headers.set("authorization-Secondary", signature2.authorization());
        HttpEntity<SessionCreateParams> entity = new HttpEntity<SessionCreateParams>(params,
                headers);
        ResponseEntity<Session> response =
                restTemplate.exchange(strGWPostURL, HttpMethod.POST, entity, Session.class);
        return response.getBody();
    }

    @Override
    public Session retrieveEicSession(String csId) {
        Stripe.apiKey = GatewayConfig.stripeKey;
        String strGWPostURL= gateWayUrl+"/v1/stripe/v1/checkout/sessions";
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.set("date", signature.date());
        headers.set("authorization", signature.authorization());
        headers.set("date-Secondary", signature2.date());
        headers.set("authorization-Secondary", signature2.authorization());
        HttpEntity<String> entity = new HttpEntity<String>(csId,
                headers);
        ResponseEntity<Session> response =
                restTemplate.exchange(strGWPostURL, HttpMethod.GET, entity, Session.class);
        return response.getBody();
    }


}
