package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.entity.sopprojectuserassignment.PaymentBaiduriProxyUtil;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.StripeService;
import com.ecquaria.cloud.moh.iais.service.client.PaymentAppGrpClient;
import com.ecquaria.cloud.moh.iais.service.client.PaymentClient;
import com.ecquaria.cloud.payment.PaymentTransactionEntity;
import com.stripe.model.Charge;
import com.stripe.net.RequestOptions;
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

    public void action(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do action start ...."));
        List<PaymentRequestDto> paymentRequestDtos=paymentClient.getAllPayingPaymentRequestDto().getEntity();
        for (PaymentRequestDto payReq:paymentRequestDtos
             ) {
            PaymentRequestDto paymentRequestDto=paymentClient.getPaymentRequestDtoByReqRefNo(payReq.getReqRefNo()).getEntity();
            PaymentBaiduriProxyUtil.getStripeService().authentication();

            RequestOptions requestOptions=stripeService.connectedAccounts(paymentRequestDto.getSrcSystemConfDto().getClientKey());

            PaymentDto paymentDto=paymentClient.getPaymentDtoByReqRefNo(paymentRequestDto.getReqRefNo()).getEntity();
            ApplicationGroupDto applicationGroupDto=paymentAppGrpClient.paymentUpDateByGrpNo(paymentRequestDto.getReqRefNo()).getEntity();
            if(paymentDto!=null){
                Charge charge=stripeService.retrieveCharge(paymentDto.getInvoiceNo());
                if(charge!=null && "succeeded".equals(charge.getStatus())){
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
            }else {
                applicationGroupDto.setPmtStatus(PaymentTransactionEntity.TRANS_STATUS_FAILED);
            }
            paymentAppGrpClient.doUpDate(applicationGroupDto);

        }

    }
}
