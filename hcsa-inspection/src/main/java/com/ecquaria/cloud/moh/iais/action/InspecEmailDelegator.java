package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
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
    public void emailSubmitStep(BaseProcessClass bpc){
        String action = (String) ParamUtil.getRequestAttr(bpc.request, "submitEmail");
        if (StringUtil.isEmpty(action)) {
            action = ParamUtil.getString(bpc.request, "submitEmail");
            if (StringUtil.isEmpty(action)) {
                //first
                action = "preview";
            }
        }
        ParamUtil.setRequestAttr(bpc.request, "submit_value", action);

    }

    public void previewEmail(BaseProcessClass bpc ,InspectionEmailTemplateDto inspectionEmailTemplateDto){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String context=inspEmailService.previewEmailTemplate(inspectionEmailTemplateDto);
        ParamUtil.setRequestAttr(request,"context", context);

    }

    public void validationEmail(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String flag="true";
        ParamUtil.setRequestAttr(request,"flag", flag);
    }
    public void sendEmail(BaseProcessClass bpc,InspectionEmailTemplateDto inspectionEmailTemplateDto){

        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        inspEmailService.insertEmailTemplate(inspectionEmailTemplateDto);
        ParamUtil.setRequestAttr(request,"reqRefNum", "ASD123");
    }
    public void doRecallEmail(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        inspEmailService.recallEmailTemplate(request.getParameter("id"));
    }

}
