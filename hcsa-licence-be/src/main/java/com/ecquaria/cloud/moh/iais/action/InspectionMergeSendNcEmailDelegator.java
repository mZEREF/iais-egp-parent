package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.sample.DemoConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionPreTaskService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * InspectionMergeSendNcEmailDelegator
 *
 * @author junyu
 * @date 2019/12/17
 */
@Delegator("inspectionMergeSendNcEmailDelegator")
@Slf4j
public class InspectionMergeSendNcEmailDelegator {
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    InspectionService inspectionService;
    @Autowired
    private TaskService taskService;
    @Autowired
    InspectionPreTaskService inspectionPreTaskService;
    @Autowired
    ApplicationService applicationService;
    @Autowired
    ApplicationViewService applicationViewService;


    public void start(BaseProcessClass bpc){
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
    }

    public void prepareData(BaseProcessClass bpc) throws IOException, TemplateException {
        log.info("=======>>>>>prepareData>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        String taskId = ParamUtil.getString(request,"TaskId");
        if(StringUtil.isEmpty(taskId)){
            taskId = "DF1C07EE-191E-EA11-BE7D-000C29F371DC";
        }
        TaskDto taskDto = taskService.getTaskById(taskId);
        String appNo = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = inspEmailService.getAppViewByNo(appNo);
        Map<String,String> appPremCorrIds=inspEmailService.getAppPremisesCorrelationsByAppId(applicationViewDto.getApplicationDto().getId());
        StringBuilder mesContext=new StringBuilder();
        String oneEmail=inspEmailService.getInsertEmail(appPremCorrIds.entrySet().iterator().next().getValue()).getMessageContent();
        mesContext.append(oneEmail.substring(0,oneEmail.indexOf("Below are the review outcome of the Submitted Non-Compliance")));
        List<String> appIds=new ArrayList<>();
        for (Map.Entry<String, String> appPremCorr:appPremCorrIds.entrySet()
                ) {
            String ncEmail= inspEmailService.getInsertEmail(appPremCorr.getValue()).getMessageContent();
            appIds.add(appPremCorr.getKey());
            mesContext.append(ncEmail.substring(ncEmail.indexOf("Below are the review outcome of the Submitted Non-Compliance"),ncEmail.indexOf("<p>Thank you</p><br><p>Regards</p><br><p>Ministry of Health</p></body></html>")));
        }
        mesContext.append(oneEmail.substring(oneEmail.indexOf("<p>Thank you</p><br><p>Regards</p><br><p>Ministry of Health</p></body></html>")));
        InspectionEmailTemplateDto inspectionEmailTemplateDto= new InspectionEmailTemplateDto();
        inspectionEmailTemplateDto.setMessageContent(mesContext.toString());
        inspectionEmailTemplateDto.setSubject("Inspection NC / BP Outcome");

        List<SelectOption> appTypeOption = MasterCodeUtil.retrieveOptionsByCodes(new String[]{InspectionConstants.PROCESS_DECI_REVISE_EMAIL_CONTENT,InspectionConstants.PROCESS_DECI_SENDS_EMAIL_APPLICANT});

        ParamUtil.setSessionAttr(request,"appIds", (Serializable) appIds);
        ParamUtil.setRequestAttr(request,"appTypeOption", appTypeOption);
        ParamUtil.setSessionAttr(request,"mesContext", mesContext.toString());
        ParamUtil.setSessionAttr(request,"applicationViewDto",applicationViewDto);
        ParamUtil.setSessionAttr(request,"insEmailDto", inspectionEmailTemplateDto);
    }

    public void emailSubmitStep(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        InspectionEmailTemplateDto inspectionEmailTemplateDto = (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(bpc.request,"insEmailDto");
        ParamUtil.setSessionAttr(request,"insEmailDto", inspectionEmailTemplateDto);
        String crudAction = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
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
        InspectionEmailTemplateDto inspectionEmailTemplateDto= (InspectionEmailTemplateDto) ParamUtil.getSessionAttr(request,"insEmailDto");
        inspectionEmailTemplateDto.setSubject(ParamUtil.getString(request,"subject"));
        inspectionEmailTemplateDto.setMessageContent(ParamUtil.getString(request,"messageContent"));
        ApplicationViewDto applicationViewDto= (ApplicationViewDto) ParamUtil.getSessionAttr(request,"applicationViewDto");
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!"send".equals(currentAction)){
            return;
        }
        String decision=ParamUtil.getString(request,"decision");
        List<String>appIds= (List<String>) ParamUtil.getSessionAttr(request,"appIds");
        for(int i=1;i<=appIds.size();i++){
            String param="revise"+String.valueOf(i);
            if(ParamUtil.getString(request, param).equals(null)){
                appIds.set(i,"");
            }
        }
        List<ApplicationDto> applicationDtos= applicationService.getApplicaitonsByAppGroupId(applicationViewDto.getApplicationDto().getAppGrpId());


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
        if (decision.equals(InspectionConstants.PROCESS_DECI_REVISE_EMAIL_CONTENT)){
            applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_ROLL_BACK);
            applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());

            for(int i=0;i<appIds.size();i++){
                if(appIds.get(i).equals(applicationDtos.get(i))){
                    applicationDtos.get(i).setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_ROLL_BACK);
                    applicationViewService.updateApplicaiton(applicationDtos.get(i));
                }
            }
        }
        else {
            applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
            applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());
            String id= inspEmailService.insertEmailTemplate(inspectionEmailTemplateDto);
            ParamUtil.setSessionAttr(request,"templateId",id);
        }



        ParamUtil.setSessionAttr(request,"insEmailDto", inspectionEmailTemplateDto);

    }
    public void doRecallEmail(BaseProcessClass bpc) {
        log.info("=======>>>>>doRecallEmail>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        ApplicationViewDto applicationViewDto= (ApplicationViewDto) ParamUtil.getSessionAttr(request,"applicationViewDto");
        applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_ROLL_BACK);
        applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());
        String id= (String) ParamUtil.getSessionAttr(request,"templateId");
        if (!StringUtil.isEmpty(id)){
        inspEmailService.recallEmailTemplate(id);}
    }

}
