package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.ServiceConfigService;
import com.ecquaria.cloud.moh.iais.service.client.AppGrpPaymentClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.Date;
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
    @Autowired
    private ServiceConfigService serviceConfigService;

    public void start(BaseProcessClass bpc){
        //AuditTrailHelper.setupBatchJobAuditTrail(this);
        log.debug(StringUtil.changeForLog("the do doStart start ...."));
    }

    public void endStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do job start ...."));
        List<ApplicationGroupDto> applicationGroupDtoList= applicationFeClient.getAppGrpDtoPaying().getEntity();

        for (ApplicationGroupDto appGrp :applicationGroupDtoList
                ) {
            try {
                List<PaymentRequestDto> paymentRequestDtos= appGrpPaymentClient
                        .getPaymentRequestDtoByReqRefNoLike(AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY, appGrp.getGroupNo()).getEntity();
                if(paymentRequestDtos!=null){
                    for (PaymentRequestDto paymentRequestDto:paymentRequestDtos
                    ) {
                        if("success".equals(paymentRequestDto.getStatus())){
                            appGrp.setPmtRefNo(paymentRequestDto.getReqRefNo());
                            appGrp.setPaymentDt(new Date());
                            appGrp.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS);
                            appGrp.setPayMethod(paymentRequestDto.getPayMethod());
                        }
                    }
                }
                if(appGrp.getPmtStatus().equals(ApplicationConsts.PAYMENT_STATUS_PAY_SUCCESS)){
                    serviceConfigService.updatePaymentStatus(appGrp);
                }else {
                    boolean hasPaying=false;
                    if(paymentRequestDtos!=null){
                        for (PaymentRequestDto paymentRequestDto:paymentRequestDtos
                        ) {
                            if("paying".equals(paymentRequestDto.getStatus())){
                                hasPaying=true;break;
                            }
                        }
                    }
                    if(!hasPaying){
                        List<ApplicationGroupDto> groupDtoList= IaisCommonUtils.genNewArrayList();
                        appGrp.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
                        groupDtoList.add(appGrp);
                        applicationFeClient.updateFeApplicationGroupStatus(groupDtoList);
                    }
                }

            }catch (Exception e){
                log.info(e.getMessage(),e);
            }
        }

    }
}
