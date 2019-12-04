package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.constant.sample.DemoConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.client.InsEmailClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
    @Autowired
    InsEmailClient insEmailClient;
    public void start(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
    }

    public void prepareData(BaseProcessClass bpc) throws IOException, TemplateException {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String templateId="08BDA324-5D13-EA11-BE78-000C29D29DB0";
        String appNo = "AN1911136061-01";
        ApplicationViewDto applicationViewDto = insEmailClient.getAppViewByNo(appNo).getEntity();
        String appPremCorrId=applicationViewDto.getAppPremisesCorrelationId();
        ParamUtil.setSessionAttr(request,"appPremCorrId",appPremCorrId);
        InspectionEmailTemplateDto inspectionEmailTemplateDto = RestApiUtil.postGetObject("system-admin:8886/iais-messageTemplate",templateId, InspectionEmailTemplateDto.class);
        inspectionEmailTemplateDto.setAppPremCorrId(appPremCorrId);
        inspectionEmailTemplateDto.setApplicantName("li cen");
        inspectionEmailTemplateDto.setApplicationNumber(appNo);
        inspectionEmailTemplateDto.setHciCode("HCI123");
        inspectionEmailTemplateDto.setHciNameOrAddress(applicationViewDto.getHciAddress());
        inspectionEmailTemplateDto.setServiceName("cosmetic surgery");
        inspectionEmailTemplateDto.setSn("No");
        inspectionEmailTemplateDto.setChecklistItem("checklistItem");
        inspectionEmailTemplateDto.setRegulationClause("regulationClause");
        inspectionEmailTemplateDto.setRemarks("no remarks");
        inspectionEmailTemplateDto.setBestPractices("GOOD");

        Map<String,Object> map=new HashMap<>();
        map.put("APPLICANT_NAME",inspectionEmailTemplateDto.getApplicantName());
        map.put("APPLICATION_NUMBER",inspectionEmailTemplateDto.getApplicationNumber());
        map.put("HCI_CODE",inspectionEmailTemplateDto.getHciCode());
        map.put("HCI_NAME",inspectionEmailTemplateDto.getHciNameOrAddress());
        map.put("SERVICE_NAME",inspectionEmailTemplateDto.getServiceName());
        if(!inspectionEmailTemplateDto.getSn().equals("Yes")){
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append("<tr><td>"+inspectionEmailTemplateDto.getSn());
            stringBuilder.append("</td><td>"+inspectionEmailTemplateDto.getChecklistItem());
            stringBuilder.append("</td><td>"+inspectionEmailTemplateDto.getRegulationClause());
            stringBuilder.append("</td><td>"+inspectionEmailTemplateDto.getRemarks());
            stringBuilder.append("</td><tr>");
            map.put("NC_DETAILS",stringBuilder.toString());
        }
        if(inspectionEmailTemplateDto.getBestPractices()!=null){
            map.put("BEST_PRACTICE",inspectionEmailTemplateDto.getBestPractices());
        }
        map.put("MOH_NAME", AppConsts.MOH_AGENCY_NAME);
        String mesContext= MsgUtil.getTemplateMessageByContent(inspectionEmailTemplateDto.getMessageContent(),map);
        inspectionEmailTemplateDto.setMessageContent(mesContext);
        ParamUtil.setSessionAttr(request,"mesContext", mesContext);
        ParamUtil.setSessionAttr(request,"applicationViewDto",applicationViewDto);
        ParamUtil.setSessionAttr(request,"insEmailDto", inspectionEmailTemplateDto);
    }
    public void emailSubmitStep(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        InspectionEmailTemplateDto inspectionEmailTemplateDto = (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(bpc.request,"insEmailDto");
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
        String context=ParamUtil.getString(request,"messageContent");
        ParamUtil.setRequestAttr(request,"context", context);


    }

    public void sendEmail(BaseProcessClass bpc){

        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!"send".equals(currentAction)){
            return;
        }
        String decision=ParamUtil.getRequestString(request,"decision");

        InspectionEmailTemplateDto inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,"insEmailDto");
        inspectionEmailTemplateDto.setSubject(ParamUtil.getString(request,"subject"));
        inspectionEmailTemplateDto.setMessageContent(ParamUtil.getString(request,"messageContent"));
        ApplicationViewDto applicationViewDto= (ApplicationViewDto) ParamUtil.getSessionAttr(request,"applicationViewDto");
        if (inspectionEmailTemplateDto.getSubject().isEmpty()){
            Map<String,String> errorMap = new HashMap<>();
            ParamUtil.setRequestAttr(request, DemoConstants.ERRORMAP,errorMap);
            ParamUtil.setRequestAttr(request,DemoConstants.ISVALID,"N");
        }
        if (inspectionEmailTemplateDto.getMessageContent().isEmpty()){
            Map<String,String> errorMap = new HashMap<>();
            ParamUtil.setRequestAttr(request, DemoConstants.ERRORMAP,errorMap);
            ParamUtil.setRequestAttr(request,DemoConstants.ISVALID,"N");
        }
        if (decision.equals("Route email/letter to AO1 for review")){
            applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01);
            RestApiUtil.update(RestApiUrlConsts.IAIS_APPLICATION_BE,applicationViewDto.getApplicationDto(), ApplicationDto.class);
        }
        else {
            applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
            RestApiUtil.update(RestApiUrlConsts.IAIS_APPLICATION_BE,applicationViewDto.getApplicationDto(), ApplicationDto.class);

        }
        String id= inspEmailService.insertEmailTemplate(inspectionEmailTemplateDto);
        ParamUtil.setSessionAttr(request,"templateId",id);
        ParamUtil.setSessionAttr(request,"insEmailDto", inspectionEmailTemplateDto);

    }
    public void doRecallEmail(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String id= (String) ParamUtil.getSessionAttr(request,"templateId");
        inspEmailService.recallEmailTemplate(id);
    }

}
