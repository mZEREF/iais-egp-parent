package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.AppGrpPaymentClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * ClientCheckNotResultPaymentHandler
 *
 * @author junyu
 * @date 2020/9/22
 */
@JobHandler(value = "clientCheckNotResultPaymentHandler")
@Component
@Slf4j
public class ClientCheckNotResultPaymentHandler extends IJobHandler {
    @Autowired
    ApplicationFeClient applicationFeClient;
    @Autowired
    AppGrpPaymentClient appGrpPaymentClient;
    @Autowired
    private ServiceConfigService serviceConfigService;
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            AuditTrailHelper.setupBatchJobAuditTrail(this);
            log.debug(StringUtil.changeForLog("the do job start ...."));
            List<ApplicationGroupDto> applicationGroupDtoList= applicationFeClient.getAppGrpDtoPaying().getEntity();
            AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
            for (ApplicationGroupDto appGrp :applicationGroupDtoList
            ) {
                try {
                    List<PaymentRequestDto> paymentRequestDtos= appGrpPaymentClient.getPaymentRequestDtoByReqRefNoLike(appGrp.getGroupNo()).getEntity();
                    for (PaymentRequestDto paymentRequestDto:paymentRequestDtos
                         ) {
                        PaymentDto paymentDto= appGrpPaymentClient.getPaymentDtoByReqRefNo(paymentRequestDto.getReqRefNo()).getEntity();
                        if(paymentDto!=null&&"success".equals(paymentDto.getPmtStatus())){
                            appGrp.setPmtRefNo(paymentDto.getReqRefNo());
                            appGrp.setPaymentDt(paymentDto.getTxnDt());
                            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                            appGrp.setPayMethod(paymentRequestDto.getPayMethod());
                            serviceConfigService.updatePaymentStatus(appGrp);
                        }
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
