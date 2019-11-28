package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.sample.DemoConstants;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
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

    public void start(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
    }

    public void prepareData(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String templateId = "7" ;
        InspectionEmailTemplateDto inspectionEmailTemplateDto = inspEmailService.getInsEmailTemplateDto(templateId);
        ParamUtil.setSessionAttr(request,"insEmailDto", inspectionEmailTemplateDto);
    }
    public void emailSubmitStep(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String applicantName = ParamUtil.getString(bpc.request, "applicantName");
        String addressee= ParamUtil.getString(bpc.request, "addressee");
        String applicationNumber= ParamUtil.getString(bpc.request, "applicationNumber");
        String hciCode= ParamUtil.getString(bpc.request, "hciCode");
        String hciNameOrAddress= ParamUtil.getString(bpc.request, "hciNameOrAddress");
        String serviceName= ParamUtil.getString(bpc.request, "serviceName");
        String sn= ParamUtil.getString(bpc.request, "sn");
        String checklistItem= ParamUtil.getString(bpc.request, "checklistItem");
        String regulationClause= ParamUtil.getString(bpc.request, "regulationClause");
        String remarks= ParamUtil.getString(bpc.request, "remarks");
        String BestPractices= ParamUtil.getString(bpc.request, "BestPractices");

//        if (applicantName.isEmpty()){
//            Map<String,String> errorMap = new HashMap<>();
//            ParamUtil.setRequestAttr(request, DemoConstants.ERRORMAP,errorMap);
//            ParamUtil.setRequestAttr(request,DemoConstants.ISVALID,"N");
//        }

        InspectionEmailTemplateDto inspectionEmailTemplateDto = (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(bpc.request,"insEmailDto");
        inspectionEmailTemplateDto.setApplicantName(applicantName);
        inspectionEmailTemplateDto.setAddressee(addressee);
        inspectionEmailTemplateDto.setApplicationNumber(applicationNumber);
        inspectionEmailTemplateDto.setHciCode(hciCode);
        inspectionEmailTemplateDto.setHciNameOrAddress(hciNameOrAddress);
        inspectionEmailTemplateDto.setServiceName(serviceName);
        inspectionEmailTemplateDto.setSn(sn);
        inspectionEmailTemplateDto.setChecklistItem(checklistItem);
        inspectionEmailTemplateDto.setRegulationClause(regulationClause);
        inspectionEmailTemplateDto.setRemarks(remarks);
        inspectionEmailTemplateDto.setBestPractices(BestPractices);

        String crudAction = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
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
        inspectionEmailTemplateDto.setId(id);

    }
    public void doRecallEmail(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        InspectionEmailTemplateDto inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,"insEmailDto");
        inspEmailService.recallEmailTemplate(inspectionEmailTemplateDto.getId());
    }

}
