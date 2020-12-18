package com.ecquaria.cloud.entity.sopprojectuserassignment;

import com.ecquaria.cloud.moh.iais.service.PaymentService;
import com.ecquaria.cloud.moh.iais.service.StripeService;
import com.ecquaria.cloud.moh.iais.service.client.PaymentClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * PaymentBaiduriProxyUtil
 *
 * @author junyu
 * @date 2020/7/10
 */
@Service
public class PaymentBaiduriProxyUtil {
    private static PaymentBaiduriProxyUtil paymentBaiduriProxy;
    @Autowired
    PaymentClient paymentClient;

    @Autowired
    StripeService stripeService;
    @Autowired
    PaymentService paymentService;

    @PostConstruct
    private void init(){
        paymentBaiduriProxy=this;
        paymentBaiduriProxy.paymentClient=this.paymentClient;
        paymentBaiduriProxy.stripeService=this.stripeService;
        paymentBaiduriProxy.paymentService=this.paymentService;
    }

    public static PaymentBaiduriProxyUtil getPaymentBaiduriProxy(){
        return paymentBaiduriProxy;
    }

    public static PaymentClient getPaymentClient(){
        return paymentBaiduriProxy.paymentClient;
    }

    public static StripeService getStripeService(){
        return paymentBaiduriProxy.stripeService;
    }

    public static PaymentService getPaymentService(){
        return paymentBaiduriProxy.paymentService;
    }

}
