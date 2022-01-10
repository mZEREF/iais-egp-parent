package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.service.PaymentService;
import com.ecquaria.cloud.moh.iais.service.StripeService;
import com.ecquaria.cloud.moh.iais.service.client.PaymentClient;
import com.ecquaria.cloud.payment.PaymentTransactionEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * PaymentCheckNotResultFromBankJobHandler
 *
 * @author junyu
 * @date 2020/9/14
 */
@JobHandler(value = "paymentCheckNotResultFromBankJobHandler")
@Component
@Slf4j
public class PaymentCheckNotResultFromBankJobHandler extends IJobHandler {

    @Autowired
    PaymentClient paymentClient;
    @Autowired
    StripeService stripeService;
    @Autowired
    PaymentService paymentService;
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {

            List<PaymentRequestDto> paymentRequestDtos=paymentClient
                    .getAllPayingPaymentRequestDto(AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY).getEntity();
            for (PaymentRequestDto payReq:paymentRequestDtos
            ) {
                try {
                    if(("stripe".equals(payReq.getPayMethod())|| ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT.equals(payReq.getPayMethod()))&&payReq.getQueryCode()!=null&&payReq.getQueryCode().contains("cs_")){
                        stripeService.retrievePayment(payReq);
                    }else
                    if(("eNets".equals(payReq.getPayMethod())|| ApplicationConsts.PAYMENT_METHOD_NAME_NETS.equals(payReq.getPayMethod()))&&payReq.getMerchantTxnRef()!=null){
                        paymentService.retrieveNetsPayment(payReq);
                    }else if("PayNow".equals(payReq.getPayMethod())|| ApplicationConsts.PAYMENT_METHOD_NAME_PAYNOW.equals(payReq.getPayMethod())){
                        paymentService.retrievePayNowPayment(payReq);
                    }else {
                        payReq.setStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
                        payReq.setSystemClientId(AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY);
                        paymentClient.updatePaymentResquset(payReq);
                    }
                }catch (Exception e){
                    log.info(e.getMessage(),e);
                    payReq.setStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
                    payReq.setSystemClientId(AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY);
                    paymentClient.updatePaymentResquset(payReq);
                }
            }
            return ReturnT.SUCCESS;
        }catch (Throwable e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }
}
