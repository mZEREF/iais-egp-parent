package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.sample.DemoConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.NcAnswerDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.impl.InsepctionNcCheckListImpl;
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


    @Autowired
    FillupChklistService fillupChklistService;
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
        String decision=ParamUtil.getString(request,"decision");

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
        String taskId = ParamUtil.getString(request,"TaskId");
        String serviceCode ="BLB";
        String serviceType = "Inspection";
        InspectionFillCheckListDto cDto = fillupChklistService.getInspectionFillCheckListDto(taskId,serviceCode,serviceType);
        String configId = cDto.getCheckList().get(0).getConfigId();
        AppPremisesPreInspectChklDto appPremPreCklDto = insepctionNcCheckListService.getAppPremChklDtoByTaskId(taskId,configId);
        InspectionFillCheckListDto insepectionNcCheckListDto = null;
        String appPremCorrId = appPremPreCklDto.getAppPremCorrId();
        List<NcAnswerDto> acDto = insepctionNcCheckListService.getNcAnswerDtoList(configId,appPremCorrId);
        AppPremisesRecommendationDto appPremisesRecommendationDto = insepctionNcCheckListService.getAppRecomDtoByAppCorrId(appPremCorrId,"tcu");
        List<AppPremisesPreInspectionNcItemDto> itemDtoList = insepctionNcCheckListService.getNcItemDtoByAppCorrId(appPremPreCklDto.getAppPremCorrId());
        insepectionNcCheckListDto = insepctionNcCheckListService.getNcCheckList(cDto,appPremPreCklDto,itemDtoList,appPremisesRecommendationDto);
        ChecklistConfigDto commonCheckListDto = fillupChklistService.getcommonCheckListDto("Inspection","New");
        InspectionFillCheckListDto commonDto  = fillupChklistService.transferToInspectionCheckListDto(commonCheckListDto,cDto.getCheckList().get(0).getAppPreCorreId());
        insepctionNcCheckListService.getCommonDto(commonDto,appPremPreCklDto,itemDtoList);

        ApplicationViewDto appViewDto = fillupChklistService.getAppViewDto("7102C311-D10D-EA11-BE7D-000C29F371DC");
        TaskDto  taskDto = fillupChklistService.getTaskDtoById("7102C311-D10D-EA11-BE7D-000C29F371DC");
        ParamUtil.setSessionAttr(request,"taskDto",taskDto);
        ParamUtil.setSessionAttr(request,"fillCheckListDto",insepectionNcCheckListDto);
        ParamUtil.setSessionAttr(request,"commonDto",commonDto);
        ParamUtil.setSessionAttr(request,"applicationViewDto",appViewDto);
    }
    public void checkListNext(BaseProcessClass bpc) throws IOException, TemplateException {
        log.info("=======>>>>>checkListNext>>>>>>>>>>>>>>>>emailRequest");

        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,"isValid", "Y");

        request.setAttribute(IaisEGPConstant.CRUD_ACTION_TYPE, "emailView");
    }
    public void preEmailView(BaseProcessClass bpc) throws IOException, TemplateException {
        log.info("=======>>>>>preEmailView>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
//        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
//        if(!"preEmailView".equals(currentAction)){
//            return;
//        }
        String taskId="7102C311-D10D-EA11-BE7D-000C29F371DC";
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
