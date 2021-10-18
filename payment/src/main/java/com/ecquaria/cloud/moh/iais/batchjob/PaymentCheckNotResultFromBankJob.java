package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.PaymentService;
import com.ecquaria.cloud.moh.iais.service.StripeService;
import com.ecquaria.cloud.moh.iais.service.client.PaymentAppGrpClient;
import com.ecquaria.cloud.moh.iais.service.client.PaymentClient;
import com.ecquaria.cloud.payment.PaymentTransactionEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

/**
 * PaymentCheckNotResultFromBankJob
 *
 * @author junyu
 * @date 2020/7/21
 */
@Delegator(value = "paymentCheckNotResultFromBankJob")
@Slf4j
public class PaymentCheckNotResultFromBankJob {
    @Autowired
    PaymentAppGrpClient paymentAppGrpClient;
    @Autowired
    PaymentClient paymentClient;
    @Autowired
    StripeService stripeService;
    @Autowired
    PaymentService paymentService;
    public void start(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doStart start ...."));
    }

    public void endStep(BaseProcessClass bpc)  {
        log.debug(StringUtil.changeForLog("the do action start ...."));
        List<PaymentRequestDto> paymentRequestDtos=paymentClient.getAllPayingPaymentRequestDto().getEntity();
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
                    paymentClient.updatePaymentResquset(payReq);
                }
            }catch (Exception e){
                log.info(e.getMessage(),e);
                payReq.setStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
                try {
                    paymentClient.updatePaymentResquset(payReq);
                }catch (Exception e1){
                    log.info(e.getMessage(),e1);
                }
            }
        }

    }
}
