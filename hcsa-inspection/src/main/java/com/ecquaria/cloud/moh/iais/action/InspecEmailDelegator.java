package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.sample.DemoConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeIndividualDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

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
    @Autowired
    InsRepService insRepService;

    public void start(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
    }

    public void prepareData(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String templateId = "BF0EFC2A-250C-EA11-BE78-000C29D29DB0" ;
        InspectionEmailTemplateDto inspectionEmailTemplateDto = inspEmailService.getInsEmailTemplateDto(templateId);
//        InspectionReportDto inspectionReportDto=insRepService.getInsRepDto("AN1911153344-01");
//        InspectionEmailTemplateDto callInspectionEmailTemplate2 = inspEmailService.getInsEmailTemplateDto(templateId);
//        inspectionEmailTemplateDto.setApplicantName(inspectionReportDto.getLicenseeName());
//        inspectionEmailTemplateDto.setAddressee(inspectionReportDto.getLicenseeEmailAddr());
//        inspectionEmailTemplateDto.setApplicationNumber("AN1911153344-01");
//        inspectionEmailTemplateDto.setHciCode(inspectionReportDto.getHciCode());
//        inspectionEmailTemplateDto.setHciNameOrAddress(inspectionReportDto.getHciName()+"/"+inspectionReportDto.getHciAddress());
//        inspectionEmailTemplateDto.setServiceName(inspectionReportDto.getServiceName());
//  没写
//        inspectionEmailTemplateDto.setSn(sn);
//        inspectionEmailTemplateDto.setChecklistItem(checklistItem);
//        inspectionEmailTemplateDto.setRegulationClause(regulationClause);
//        inspectionEmailTemplateDto.setRemarks(remarks);
//        inspectionEmailTemplateDto.setRecipient(AO1_email);

        String context=inspEmailService.previewEmailTemplate(inspectionEmailTemplateDto);
        inspectionEmailTemplateDto.setMessageContent(context);
        ParamUtil.setSessionAttr(request,"insEmailDto", inspectionEmailTemplateDto);
    }
    public void emailSubmitStep(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String BestPractices= ParamUtil.getString(bpc.request, "BestPractices");

        InspectionEmailTemplateDto inspectionEmailTemplateDto = (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(bpc.request,"insEmailDto");

        inspectionEmailTemplateDto.setBestPractices(BestPractices);
        String crudAction = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        ParamUtil.setSessionAttr(request,"insEmailDto", inspectionEmailTemplateDto);
        log.debug("*******************crudAction-->:" + crudAction);
    }

    public void previewEmail(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!"preview".equals(currentAction)){
            return;
        }
        InspectionEmailTemplateDto inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,"insEmailDto");
        String context=inspEmailService.previewEmailTemplate(inspectionEmailTemplateDto);
        ParamUtil.setRequestAttr(request,"context", context);


    }

    public void validationEmail(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!"validation".equals(currentAction)){
            return;
        }
        InspectionEmailTemplateDto inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,"insEmailDto");
        //假的
        inspectionEmailTemplateDto.setRecipient("AO1");
        String id= inspEmailService.applyInspection(inspectionEmailTemplateDto);
        ParamUtil.setSessionAttr(request,"templateId",id);
        String flag="Y";
        ParamUtil.setRequestAttr(request,"flag", flag);
    }
    public void sendEmail(BaseProcessClass bpc){

        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!"send".equals(currentAction)){
            return;
        }
        InspectionEmailTemplateDto inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,"insEmailDto");
        String id= inspEmailService.insertEmailTemplate(inspectionEmailTemplateDto);
        ParamUtil.setSessionAttr(request,"templateId",id);

    }
    public void doRecallEmail(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String id= (String) ParamUtil.getSessionAttr(request,"templateId");
        inspEmailService.recallEmailTemplate(id);
    }

}
