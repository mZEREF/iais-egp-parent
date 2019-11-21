package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

/**
 * @author weilu
 * date 2019/11/20 17:15
 */


@Delegator(value = "insReport")
@Slf4j
public class InsReportDelegator {

    @Autowired
    private InsRepService insRepService;

    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>paymentRequest");
    }

    public void prepareData(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>paymentRequest");
        HttpServletRequest request = bpc.request;
        String appNo = "AN1911136061-01" ;
        InspectionReportDto insRepDto = insRepService.getInsRepDto(appNo);
        ParamUtil.setRequestAttr(request,"insRepDto", insRepDto);
    }
}
