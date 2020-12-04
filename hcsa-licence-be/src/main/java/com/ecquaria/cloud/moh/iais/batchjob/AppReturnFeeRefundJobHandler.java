package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * AppReturnFeeRefundJobHandler
 *
 * @author junyu
 * @date 2020/10/26
 */
@JobHandler(value="appReturnFeeRefundJobHandler")
@Component
@Slf4j
public class AppReturnFeeRefundJobHandler extends IJobHandler {
    @Autowired
    ApplicationClient applicationClient;
    @Autowired
    private ApplicationService applicationService;
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            AuditTrailHelper.setupBatchJobAuditTrail(this);
            List<AppReturnFeeDto> appReturnFeeDtoList= applicationClient.getAppReturnFeeByStatus("paying").getEntity();
            doRefunds(appReturnFeeDtoList);
            for (AppReturnFeeDto appFee:appReturnFeeDtoList
                 ) {
                applicationService.saveAppReturnFee(appFee);
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }

    private void doRefunds(List<AppReturnFeeDto> saveReturnFeeDtos){
        List<PaymentRequestDto> paymentRequestDtos= applicationService.eicFeStripeRefund(saveReturnFeeDtos);
        for (PaymentRequestDto refund:paymentRequestDtos
        ) {
            for (AppReturnFeeDto appreturn:saveReturnFeeDtos
            ) {
                if(appreturn.getApplicationNo().equals(refund.getReqRefNo())){
                    appreturn.setTriggerCount(appreturn.getTriggerCount()+1);
                    appreturn.setStatus(refund.getStatus());
                }
            }
        }
    }
}
