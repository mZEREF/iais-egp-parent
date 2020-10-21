package com.ecquaria.cloud.moh.iais.job;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.client.AppGrpPaymentClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
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
    ApplicationClient applicationClient;
    @Autowired
    AppGrpPaymentClient appGrpPaymentClient;
    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            log.debug(StringUtil.changeForLog("the do job start ...."));
            List<ApplicationGroupDto> applicationGroupDtoList= applicationClient.getAppGrpDtoPaying().getEntity();
            AuditTrailDto auditTrailDto = AuditTrailHelper.getBatchJobAuditTrail(AppConsts.DOMAIN_INTRANET);
            for (ApplicationGroupDto appGrp :applicationGroupDtoList
            ) {
                try {
                    PaymentDto paymentDto= appGrpPaymentClient.getPaymentDtoByReqRefNo(appGrp.getGroupNo()).getEntity();
                    paymentDto.setAuditTrailDto(auditTrailDto);
                    if(paymentDto!=null&&"success".equals(paymentDto.getPmtStatus())){
                        appGrp.setPmtRefNo(paymentDto.getReqRefNo());
                        appGrp.setPaymentDt(paymentDto.getTxnDt());
                        appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                        applicationClient.doPaymentUpDate(appGrp).getEntity();
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
