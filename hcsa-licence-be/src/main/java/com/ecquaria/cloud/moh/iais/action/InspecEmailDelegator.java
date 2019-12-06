package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.constant.sample.DemoConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
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
    InspectionService inspectionService;
    @Autowired
    private TaskService taskService;
    @Autowired
    InsRepService insRepService;
    @Autowired
    ApplicationViewService applicationViewService;

    public void start(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
    }

    public void prepareData(BaseProcessClass bpc) throws IOException, TemplateException {
        log.info("=======>>>>>prepareData>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String templateId="08BDA324-5D13-EA11-BE78-000C29D29DB0";
        String taskId="48512333-7A16-EA11-BE7D-000C29F371DC";
        TaskDto taskDto = taskService.getTaskById(taskId);
        String appNo = taskDto.getRefNo();
        //String licenseeName=insRepService.getInsRepDto(appNo).getLicenseeName();
        String licenseeName="lichen";
        ApplicationViewDto applicationViewDto = inspEmailService.getAppViewByNo(appNo);
        String appPremCorrId=applicationViewDto.getAppPremisesCorrelationId();
        ParamUtil.setSessionAttr(request,"appPremCorrId",appPremCorrId);
        InspectionEmailTemplateDto inspectionEmailTemplateDto = inspEmailService.loadingEmailTemplate(templateId);
        inspectionEmailTemplateDto.setAppPremCorrId(appPremCorrId);
        inspectionEmailTemplateDto.setApplicantName(licenseeName);
        inspectionEmailTemplateDto.setApplicationNumber(appNo);
        inspectionEmailTemplateDto.setHciCode(applicationViewDto.getHciCode());
        inspectionEmailTemplateDto.setHciNameOrAddress(applicationViewDto.getHciAddress());
        HcsaServiceDto hcsaServiceDto=inspectionService.getHcsaServiceDtoByServiceId(applicationViewDto.getApplicationDto().getServiceId());
        inspectionEmailTemplateDto.setServiceName(hcsaServiceDto.getSvcName());
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
        if(inspectionEmailTemplateDto.getSn().equals("No")){
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
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, "checkList");
    }
    public void emailSubmitStep(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        InspectionEmailTemplateDto inspectionEmailTemplateDto = (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(bpc.request,"insEmailDto");
        String crudAction = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        ParamUtil.setSessionAttr(request,"insEmailDto", inspectionEmailTemplateDto);
        log.debug("*******************crudAction-->:" + crudAction);
    }

    public void previewEmail(BaseProcessClass bpc){
        log.info("=======>>>>>previewEmail>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!"preview".equals(currentAction)){
            return;
        }
        String content=ParamUtil.getString(request,"messageContent");
        ParamUtil.setRequestAttr(request,"content", content);


    }

    public void sendEmail(BaseProcessClass bpc){

        log.info("=======>>>>>sendEmail>>>>>>>>>>>>>>>>emailRequest");
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
            applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());
        }
        else {
            applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
            applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());
        }
        String id= inspEmailService.insertEmailTemplate(inspectionEmailTemplateDto);
        ParamUtil.setSessionAttr(request,"templateId",id);
        ParamUtil.setSessionAttr(request,"insEmailDto", inspectionEmailTemplateDto);

    }
    public void doRecallEmail(BaseProcessClass bpc) {
        log.info("=======>>>>>doRecallEmail>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        ApplicationViewDto applicationViewDto= (ApplicationViewDto) ParamUtil.getSessionAttr(request,"applicationViewDto");
        applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_ROLL_BACK);
        applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());
        String id= (String) ParamUtil.getSessionAttr(request,"templateId");
        inspEmailService.recallEmailTemplate(id);
    }

//    public void preCheckList(BaseProcessClass bpc) {
//
//        log.info("=======>>>>>preCheckList>>>>>>>>>>>>>>>>emailRequest");
//    }
//    public void checkListNext(BaseProcessClass bpc) {
//        log.info("=======>>>>>checkListNext>>>>>>>>>>>>>>>>emailRequest");
//        HttpServletRequest request = bpc.request;
//    }
//    public void preEmailView(BaseProcessClass bpc) throws IOException, TemplateException {
//        log.info("=======>>>>>preEmailView>>>>>>>>>>>>>>>>emailRequest");
//        HttpServletRequest request = bpc.request;
//        String templateId="08BDA324-5D13-EA11-BE78-000C29D29DB0";
//        String appNo = "AN1911136061-01";
//        ApplicationViewDto applicationViewDto = inspEmailService.getAppViewByNo(appNo);
//        String appPremCorrId=applicationViewDto.getAppPremisesCorrelationId();
//        ParamUtil.setSessionAttr(request,"appPremCorrId",appPremCorrId);
//        InspectionEmailTemplateDto inspectionEmailTemplateDto = inspEmailService.loadingEmailTemplate(templateId);
//        inspectionEmailTemplateDto.setAppPremCorrId(appPremCorrId);
//        inspectionEmailTemplateDto.setApplicantName("li cen");
//        inspectionEmailTemplateDto.setApplicationNumber(appNo);
//        inspectionEmailTemplateDto.setHciCode("HCI123");
//        inspectionEmailTemplateDto.setHciNameOrAddress(applicationViewDto.getHciAddress());
//        inspectionEmailTemplateDto.setServiceName("cosmetic surgery");
//        inspectionEmailTemplateDto.setSn("No");
//        inspectionEmailTemplateDto.setChecklistItem("checklistItem");
//        inspectionEmailTemplateDto.setRegulationClause("regulationClause");
//        inspectionEmailTemplateDto.setRemarks("no remarks");
//        inspectionEmailTemplateDto.setBestPractices("GOOD");
//
//        Map<String,Object> map=new HashMap<>();
//        map.put("APPLICANT_NAME",inspectionEmailTemplateDto.getApplicantName());
//        map.put("APPLICATION_NUMBER",inspectionEmailTemplateDto.getApplicationNumber());
//        map.put("HCI_CODE",inspectionEmailTemplateDto.getHciCode());
//        map.put("HCI_NAME",inspectionEmailTemplateDto.getHciNameOrAddress());
//        map.put("SERVICE_NAME",inspectionEmailTemplateDto.getServiceName());
//        if(inspectionEmailTemplateDto.getSn().equals("No")){
//            StringBuilder stringBuilder=new StringBuilder();
//            stringBuilder.append("<tr><td>"+inspectionEmailTemplateDto.getSn());
//            stringBuilder.append("</td><td>"+inspectionEmailTemplateDto.getChecklistItem());
//            stringBuilder.append("</td><td>"+inspectionEmailTemplateDto.getRegulationClause());
//            stringBuilder.append("</td><td>"+inspectionEmailTemplateDto.getRemarks());
//            stringBuilder.append("</td><tr>");
//            map.put("NC_DETAILS",stringBuilder.toString());
//        }
//        if(inspectionEmailTemplateDto.getBestPractices()!=null){
//            map.put("BEST_PRACTICE",inspectionEmailTemplateDto.getBestPractices());
//        }
//        map.put("MOH_NAME", AppConsts.MOH_AGENCY_NAME);
//        String mesContext= MsgUtil.getTemplateMessageByContent(inspectionEmailTemplateDto.getMessageContent(),map);
//        inspectionEmailTemplateDto.setMessageContent(mesContext);
//        ParamUtil.setSessionAttr(request,"mesContext", mesContext);
//        ParamUtil.setSessionAttr(request,"applicationViewDto",applicationViewDto);
//        ParamUtil.setSessionAttr(request,"insEmailDto", inspectionEmailTemplateDto);
//    }
//    public void emailView(BaseProcessClass bpc) {
//        log.info("=======>>>>>emailView>>>>>>>>>>>>>>>>emailRequest");
//    }

}
