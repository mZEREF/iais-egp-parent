package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.StripeService;
import com.ecquaria.cloud.moh.iais.service.client.PaymentAppGrpClient;
import com.ecquaria.cloud.moh.iais.service.client.PaymentClient;
import com.ecquaria.cloud.payment.PaymentTransactionEntity;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
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

    public void start(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doStart start ...."));
    }

    public void action(BaseProcessClass bpc) throws StripeException {
        log.debug(StringUtil.changeForLog("the do action start ...."));
        List<PaymentRequestDto> paymentRequestDtos=paymentClient.getAllPayingPaymentRequestDto().getEntity();
        for (PaymentRequestDto payReq:paymentRequestDtos
             ) {
            try {
                PaymentRequestDto paymentRequestDto=paymentClient.getPaymentRequestDtoByReqRefNo(payReq.getReqRefNo()).getEntity();

                Session session=stripeService.retrieveSession(paymentRequestDto.getSrcSystemConfDto().getClientKey());
                PaymentIntent paymentIntent=stripeService.retrievePaymentIntent(session.getPaymentIntent());

                PaymentDto paymentDto=paymentClient.getPaymentDtoByReqRefNo(paymentRequestDto.getReqRefNo()).getEntity();
                ApplicationGroupDto applicationGroupDto=paymentAppGrpClient.paymentUpDateByGrpNo(paymentRequestDto.getReqRefNo()).getEntity();
                if(paymentDto!=null){
                    if(paymentIntent!=null && "succeeded".equals(paymentIntent.getStatus())){
                        paymentDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_SUCCESS);
                        paymentRequestDto.setStatus(PaymentTransactionEntity.TRANS_STATUS_SUCCESS);
                        applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                    }else {
                        paymentDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
                        paymentRequestDto.setStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
                        applicationGroupDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
                    }
                    paymentClient.saveHcsaPayment(paymentDto);
                    paymentClient.updatePaymentResquset(paymentRequestDto);
                }else if(paymentIntent!=null && "succeeded".equals(paymentIntent.getStatus())){
                    applicationGroupDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_SUCCESS);
                }else {
                    applicationGroupDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
                }
                paymentAppGrpClient.doUpDate(applicationGroupDto);
            }catch (Exception e){
                     log.info(e.getMessage(),e);
            }
        }

    }
}
