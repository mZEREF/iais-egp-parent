package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.PaymentService;
import com.ecquaria.cloud.moh.iais.service.StripeService;
import com.ecquaria.cloud.moh.iais.service.client.PaymentAppGrpClient;
import com.ecquaria.cloud.moh.iais.service.client.PaymentClient;
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

    public void action(BaseProcessClass bpc)  {
        log.debug(StringUtil.changeForLog("the do action start ...."));
        List<PaymentRequestDto> paymentRequestDtos=paymentClient.getAllPayingPaymentRequestDto().getEntity();
        for (PaymentRequestDto payReq:paymentRequestDtos
             ) {
            try {
                PaymentRequestDto paymentRequestDto=paymentClient.getPaymentRequestDtoByReqRefNo(payReq.getReqRefNo()).getEntity();
                if("stripe".equals(paymentRequestDto.getPayMethod())){
                    stripeService.retrievePayment(paymentRequestDto);
                }
                if("eNets".equals(paymentRequestDto.getPayMethod())){
                    paymentService.retrieveNetsPayment(paymentRequestDto);
                }
            }catch (Exception e){
                     log.info(e.getMessage(),e);
            }
        }

    }
}
