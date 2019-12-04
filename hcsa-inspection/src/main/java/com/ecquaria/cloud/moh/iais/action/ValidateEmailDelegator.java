package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.client.InsEmailClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * ValidateEmailDelegator
 *
 * @author junyu
 * @date 2019/12/3
 */
@Delegator("validateEmailDelegator")
@Slf4j
public class ValidateEmailDelegator {
    @Autowired
    InspEmailService inspEmailService;

    public void start(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
    }

    public void prepareEmail(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        InspectionEmailTemplateDto inspectionEmailTemplateDto= inspEmailService.getInsertEmail("B14E19EA-E4F8-4298-935A-8C50346BD01F");
        ParamUtil.setSessionAttr(request,"insEmailDto", inspectionEmailTemplateDto);
    }

    public void doValidate(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String decision=ParamUtil.getRequestString(request,"decision");
        InspectionEmailTemplateDto inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,"insEmailDto");

        if (decision.equals("Acknowledge email/Letter Content")){
            ;
        }else {

        }
        inspEmailService.insertEmailTemplate(inspectionEmailTemplateDto);
    }

    public void step3(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
    }

}
