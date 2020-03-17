package com.ecquaria.cloud.moh.iais.job;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.PaymentStatusService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

/**
 * @author weilu
 * @date 2020/1/14 14:23
 */
@Delegator("CheckPaymentStatus")
@Slf4j
public class CheckPaymentStatusDelegator {

    @Autowired
    private PaymentStatusService paymentStatusService;

    public void start(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do doStart start ...."));
    }

    public void action(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the do action start ...."));
        List<String> list = IaisCommonUtils.genNewArrayList();
        list.add("79EEF774-8927-EA11-BE78-000C298A32C2");
        paymentStatusService.checkPaymentStatusAndUpdateAppGrp();
    }
}
