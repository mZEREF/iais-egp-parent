package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.sample.DemoConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.NcAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.impl.InsepctionNcCheckListImpl;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ValidateEmailDelegator
 *
 * @author junyu
 * @date 2019/12/3
 */
@Delegator("validateEmailDelegator")
@Slf4j
public class InspectEmailAo1Delegator {
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

    @Autowired
    InsepctionNcCheckListImpl insepctionNcCheckListService;

    public void start(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, "emailView");
    }


    public void prepareData(BaseProcessClass bpc) {
        log.info("=======>>>>>prepareData>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String crudAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        log.debug("*******************crudAction-->:" + crudAction);

    }
    public void emailSubmitStep(BaseProcessClass bpc){
        log.info("=======>>>>>emailSubmitStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        InspectionEmailTemplateDto inspectionEmailTemplateDto = (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(bpc.request,"insEmailDto");
        String content=ParamUtil.getString(request,"messageContent");
        ParamUtil.setSessionAttr(request,"content",content);
        ParamUtil.setSessionAttr(request,"insEmailDto", inspectionEmailTemplateDto);
    }

    public void previewEmail(BaseProcessClass bpc){
        log.info("=======>>>>>previewEmail>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!"preview".equals(currentAction)){
            return;
        }
        String content=ParamUtil.getString(request,"messageContent");
        ParamUtil.setSessionAttr(request,"content", content);


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
        if (decision.equals(InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_EMAIL_CONTENT)){
            applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
            applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());
        }
        else {
            applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_REJECTED);
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
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, "emailView");
    }

    public void preCheckList(BaseProcessClass bpc) {

        log.info("=======>>>>>preCheckList>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!"checkList".equals(currentAction)){
            return;
        }
    }
    public void checkListNext(BaseProcessClass bpc) throws IOException, TemplateException {
        log.info("=======>>>>>checkListNext>>>>>>>>>>>>>>>>emailRequest");

        HttpServletRequest request = bpc.request;
        if(false)
        {
            String templateId="08BDA324-5D13-EA11-BE78-000C29D29DB0";
            String taskId="47512333-7A16-EA11-BE7D-000C29F371DC";
            TaskDto taskDto = taskService.getTaskById(taskId);
            String appNo = taskDto.getRefNo();
            //String licenseeName=insRepService.getInsRepDto(appNo).getLicenseeName();
            String licenseeName="lichen";
            ApplicationViewDto applicationViewDto = inspEmailService.getAppViewByNo(appNo);
            String appPremCorrId=applicationViewDto.getAppPremisesCorrelationId();
            InspectionEmailTemplateDto inspectionEmailTemplateDto = inspEmailService.loadingEmailTemplate(templateId);
            inspectionEmailTemplateDto.setAppPremCorrId(appPremCorrId);
            inspectionEmailTemplateDto.setApplicantName(licenseeName);
            inspectionEmailTemplateDto.setApplicationNumber(appNo);
            inspectionEmailTemplateDto.setHciCode(applicationViewDto.getHciCode());
            inspectionEmailTemplateDto.setHciNameOrAddress(applicationViewDto.getHciAddress());
            HcsaServiceDto hcsaServiceDto=inspectionService.getHcsaServiceDtoByServiceId(applicationViewDto.getApplicationDto().getServiceId());
            inspectionEmailTemplateDto.setServiceName(hcsaServiceDto.getSvcName());
            String configId=inspEmailService.getcheckListQuestionDtoList(hcsaServiceDto.getSvcCode(),hcsaServiceDto.getSvcType()).get(1).getConfigId();
            List<NcAnswerDto>  ncAnswerDtos=insepctionNcCheckListService.getNcAnswerDtoList(configId,appPremCorrId);
            AppPremisesRecommendationDto appPreRecommentdationDto =insepctionNcCheckListService.getAppRecomDtoByAppCorrId(appPremCorrId,"tcu");
            inspectionEmailTemplateDto.setBestPractices(appPreRecommentdationDto.getBestPractice());

            Map<String,Object> map=new HashMap<>();
            map.put("APPLICANT_NAME",inspectionEmailTemplateDto.getApplicantName());
            map.put("APPLICATION_NUMBER",inspectionEmailTemplateDto.getApplicationNumber());
            map.put("HCI_CODE",inspectionEmailTemplateDto.getHciCode());
            map.put("HCI_NAME",inspectionEmailTemplateDto.getHciNameOrAddress());
            map.put("SERVICE_NAME",inspectionEmailTemplateDto.getServiceName());
            if(!ncAnswerDtos.isEmpty()){
                StringBuilder stringBuilder=new StringBuilder();
                for (NcAnswerDto ncAnswerDto:ncAnswerDtos
                ) {
                    stringBuilder.append("<tr><td>"+ncAnswerDto.getItemId());
                    stringBuilder.append("</td><td>"+ncAnswerDto.getItemQuestion());
                    stringBuilder.append("</td><td>"+ncAnswerDto.getClause());
                    stringBuilder.append("</td><td>"+ncAnswerDto.getRemark());
                    stringBuilder.append("</td><tr>");
                }
                map.put("NC_DETAILS",stringBuilder.toString());
            }
            if(inspectionEmailTemplateDto.getBestPractices()!=null){
                map.put("BEST_PRACTICE",inspectionEmailTemplateDto.getBestPractices());
            }
            map.put("MOH_NAME", AppConsts.MOH_AGENCY_NAME);
            String mesContext= MsgUtil.getTemplateMessageByContent(inspectionEmailTemplateDto.getMessageContent(),map);
            inspectionEmailTemplateDto.setMessageContent(mesContext);
            String draftEmailId= (String) ParamUtil.getSessionAttr(request,"draftEmailId");
            inspectionEmailTemplateDto.setId(draftEmailId);
            inspEmailService.insertEmailTemplate(inspectionEmailTemplateDto);
            ParamUtil.setSessionAttr(request,"applicationViewDto",applicationViewDto);
            ParamUtil.setSessionAttr(request,"insEmailDto", inspectionEmailTemplateDto);
        }
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, "emailView");
    }
    public void preEmailView(BaseProcessClass bpc) throws IOException, TemplateException {
        log.info("=======>>>>>preEmailView>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!"preEmailView".equals(currentAction)){
            return;
        }
        String taskId="48512333-7A16-EA11-BE7D-000C29F371DC";
        TaskDto taskDto = taskService.getTaskById(taskId);
        String appNo = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = inspEmailService.getAppViewByNo(appNo);
        String appPremCorrId=applicationViewDto.getAppPremisesCorrelationId();
        InspectionEmailTemplateDto inspectionEmailTemplateDto= inspEmailService.getInsertEmail(appPremCorrId);
        List<SelectOption> appTypeOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_EMAIL_CONTENT,InspectionConstants.PROCESS_DECI_REVISE_EMAIL_CONTENT});

        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setSessionAttr(request,"draftEmailId",inspectionEmailTemplateDto.getId());
        ParamUtil.setSessionAttr(request,"insEmailDto", inspectionEmailTemplateDto);
    }
    public void emailView(BaseProcessClass bpc) {
        log.info("=======>>>>>emailView>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, "emailView");
    }
}
