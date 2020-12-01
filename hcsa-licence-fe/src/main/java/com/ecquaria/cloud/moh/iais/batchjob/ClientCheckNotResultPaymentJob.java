package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.service.client.AppGrpPaymentClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.List;

/**
 * ClientCheckNotResultPaymentJob
 *
 * @author junyu
 * @date 2020/7/14
 */
@Delegator("clientCheckNotResultPaymentJob")
@Slf4j
public class ClientCheckNotResultPaymentJob {
    @Autowired
    ApplicationFeClient applicationFeClient;
    @Autowired
    AppGrpPaymentClient appGrpPaymentClient;


    public void start(BaseProcessClass bpc){
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        log.debug(StringUtil.changeForLog("the do doStart start ...."));
    }

    public void endStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do job start ...."));
        List<ApplicationGroupDto> applicationGroupDtoList= applicationFeClient.getAppGrpDtoPaying().getEntity();

        for (ApplicationGroupDto appGrp :applicationGroupDtoList
                ) {
            try {
                PaymentDto paymentDto= appGrpPaymentClient.getPaymentDtoByReqRefNo(appGrp.getPmtRefNo()).getEntity();
                PaymentRequestDto paymentRequestDto= appGrpPaymentClient.getPaymentRequestDtoByReqRefNoLike(appGrp.getPmtRefNo()).getEntity();
                if(paymentDto!=null&&"success".equals(paymentDto.getPmtStatus())){
                    appGrp.setPmtRefNo(paymentDto.getReqRefNo());
                    appGrp.setPaymentDt(paymentDto.getTxnDt());
                    appGrp.setPmtStatus(NewApplicationHelper.getPmtStatus(paymentRequestDto.getPayMethod()));
                    applicationFeClient.doPaymentUpDate(appGrp).getEntity();
                }
            }catch (Exception e){
                log.info(e.getMessage(),e);
            }
        }

    }
}
