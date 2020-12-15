package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.service.PaymentService;
import com.ecquaria.cloud.moh.iais.service.StripeService;
import com.ecquaria.cloud.moh.iais.service.client.PaymentClient;
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
            return ReturnT.SUCCESS;
        }catch (Throwable e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }
}
