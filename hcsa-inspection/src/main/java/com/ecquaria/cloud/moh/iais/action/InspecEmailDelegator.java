package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;

/**
 * InspecEmailDelegator
 *
 * @author junyu
 * @date 2019/11/22
 */
@Delegator("inspEmailDelegator")
@Slf4j
public class InspecEmailDelegator {
    @Autowired
    InspEmailService inspEmailService;
    public void start(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
    }

    public void prepareData(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String templateId = "7" ;
        InspectionEmailTemplateDto inspectionEmailTemplateDto = inspEmailService.getInsEmailTemplateDto(templateId);
        ParamUtil.setRequestAttr(request,"insEmailDto", inspectionEmailTemplateDto);
    }
}
