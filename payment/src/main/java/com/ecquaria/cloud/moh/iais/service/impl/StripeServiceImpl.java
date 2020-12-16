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
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.RequestOptions;
import com.stripe.param.checkout.SessionCreateParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public void retrievePayment(PaymentRequestDto paymentRequestDto) throws StripeException {
        Session session=retrieveSession(paymentRequestDto.getQueryCode());
        PaymentIntent paymentIntent=retrievePaymentIntent(session.getPaymentIntent());

        PaymentDto paymentDto=paymentClient.getPaymentDtoByReqRefNo(paymentRequestDto.getReqRefNo()).getEntity();
        String appGrpNo=paymentRequestDto.getReqRefNo().substring(0,'_');
        ApplicationGroupDto applicationGroupDto=paymentAppGrpClient.paymentUpDateByGrpNo(appGrpNo).getEntity();
        if(paymentDto!=null){
            if(paymentIntent!=null && "succeeded".equals(paymentIntent.getStatus())){
                paymentDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_SUCCESS);
                paymentRequestDto.setStatus(PaymentTransactionEntity.TRANS_STATUS_SUCCESS);
                applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
            }else if(paymentIntent!=null && "canceled".equals(paymentIntent.getStatus())){
                paymentDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
                paymentRequestDto.setStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
                applicationGroupDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
            }
            paymentClient.saveHcsaPayment(paymentDto);
        }else{
            if(paymentIntent!=null && "succeeded".equals(paymentIntent.getStatus())){
                paymentRequestDto.setStatus(PaymentTransactionEntity.TRANS_STATUS_SUCCESS);
                applicationGroupDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_SUCCESS);
            }else {
                paymentRequestDto.setStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
                applicationGroupDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
            }
        }
        paymentClient.updatePaymentResquset(paymentRequestDto);
        paymentAppGrpClient.doUpDate(applicationGroupDto);
    }


}
