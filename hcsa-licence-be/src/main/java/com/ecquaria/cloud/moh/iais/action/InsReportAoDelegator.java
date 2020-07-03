package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionReportConstants;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloudfeign.FeignException;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.validation.constraints.Null;

/**
 * @author weilu
 * @date 2019/12/5 13:16
 */
@Delegator(value = "insReportAo")
@Slf4j
public class InsReportAoDelegator {

    @Autowired
    private InsRepService insRepService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    private final static String RECOMMENDATION_DTO= "appPremisesRecommendationDto";
    private final static String OTHERS="Others";
    private final static String APPROVAL="Approval";
    private final static String REJECT="Reject";
    private final static String INSREPDTO="insRepDto";
    private final static String APPLICATIONVIEWDTO="applicationViewDto";
    private final static String TASKDTO="taskDto";


    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>report");
    }

    public void AoInit(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the inspectionReportInit start ...."));
        ParamUtil.setSessionAttr(bpc.request, INSREPDTO, null);
        ParamUtil.setSessionAttr(bpc.request, RECOMMENDATION_DTO, null);
        ParamUtil.setSessionAttr(bpc.request, APPLICATIONVIEWDTO, null);
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String taskId = ParamUtil.getMaskedString(bpc.request,"taskId");
        AuditTrailHelper.auditFunction("InspectionAO Report", "InspectionAO Report");
        TaskDto taskDto = taskService.getTaskById(taskId);
        String correlationId = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = insRepService.getApplicationViewDto(correlationId);
        AuditTrailHelper.auditFunctionWithAppNo("Inspection Report", "AO1 process Report",
                applicationViewDto.getApplicationDto().getApplicationNo());
        InspectionReportDto insRepDto = insRepService.getInsRepDto(taskDto,applicationViewDto,loginContext);
        InspectionReportDto inspectorAo = insRepService.getInspectorAo(taskDto, applicationViewDto);
        insRepDto.setInspectors(inspectorAo.getInspectors());
        initAoRecommendation(correlationId,bpc,applicationViewDto.getApplicationDto().getApplicationType());

        String infoClassTop = "active";
        String infoClassBelow = "tab-pane active";
        String reportClassBelow = "tab-pane";
        ParamUtil.setSessionAttr(bpc.request, "infoClassTop", infoClassTop);
        ParamUtil.setSessionAttr(bpc.request,"appType",null);
        ParamUtil.setSessionAttr(bpc.request, "reportClassTop", null);
        ParamUtil.setSessionAttr(bpc.request, "infoClassBelow", infoClassBelow);
        ParamUtil.setSessionAttr(bpc.request, "reportClassBelow", reportClassBelow);
        List<SelectOption> processingDe = getProcessingDecision(applicationViewDto.getApplicationDto().getStatus());
        ParamUtil.setSessionAttr(bpc.request, "processingDe", (Serializable) processingDe);
        ParamUtil.setSessionAttr(bpc.request, INSREPDTO, insRepDto);
        ParamUtil.setSessionAttr(bpc.request, APPLICATIONVIEWDTO, applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request, TASKDTO, taskDto);
    }

    public void AoReportPre(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>prepareReportData");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, APPLICATIONVIEWDTO);
        String applicationType = applicationViewDto.getApplicationDto().getApplicationType();
        ParamUtil.setSessionAttr(bpc.request,"appType",applicationType);
    }

    public void action(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the action start ...."));
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(bpc.request, INSREPDTO);
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, APPLICATIONVIEWDTO);
        TaskDto taskDto =  (TaskDto)ParamUtil.getSessionAttr(bpc.request, TASKDTO);
        ParamUtil.setSessionAttr(bpc.request, INSREPDTO, insRepDto);
        ParamUtil.setSessionAttr(bpc.request, APPLICATIONVIEWDTO, applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request, TASKDTO, taskDto);
    }

    public void back(BaseProcessClass bpc) throws Exception {
        log.debug(StringUtil.changeForLog("the back start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, APPLICATIONVIEWDTO);
        TaskDto taskDto =  (TaskDto)ParamUtil.getSessionAttr(bpc.request, TASKDTO);
        String historyRemarks = ParamUtil.getRequestString(bpc.request, "processRemarks");
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        AppPremisesRecommendationDto appPremisesRecommendationDto = (AppPremisesRecommendationDto)ParamUtil.getSessionAttr(bpc.request, RECOMMENDATION_DTO);
        log.debug(StringUtil.changeForLog("the saveAoRecommendation start ...."));
        ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.TRUE);
        insRepService.routBackTaskToInspector(taskDto,applicationDto,appPremisesCorrelationId,historyRemarks);
    }

    public void approve(BaseProcessClass bpc) throws Exception {
        log.debug(StringUtil.changeForLog("the inspectorReportAction start ...."));
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(bpc.request, INSREPDTO);
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, APPLICATIONVIEWDTO);
        AppPremisesRecommendationDto appPremisesRecommendationDto = (AppPremisesRecommendationDto)ParamUtil.getSessionAttr(bpc.request, RECOMMENDATION_DTO);
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        TaskDto taskDto =  (TaskDto)ParamUtil.getSessionAttr(bpc.request, TASKDTO);
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        String[] fastTracking =  ParamUtil.getStrings(bpc.request,"fastTracking");
        if(fastTracking!=null){
            applicationDto.setFastTracking(true);
        }
        if(ApplicationConsts.APPLICATION_STATUS_ROUTE_BACK.equals(applicationDto.getStatus())){
            insRepService.routTaskToRoutBack(bpc,taskDto, applicationDto, appPremisesCorrelationId,appPremisesRecommendationDto.getProcessRemarks());
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ISVALID, IntranetUserConstant.TRUE);
            return;
        }
        String historyRemarks = ParamUtil.getRequestString(bpc.request, "processRemarks");
        insRepService.routingTaskToAo2(taskDto,applicationDto,appPremisesCorrelationId,historyRemarks);
        ParamUtil.setSessionAttr(bpc.request, INSREPDTO, insRepDto);
        ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.TRUE);
    }

    private void initAoRecommendation(String correlationId,BaseProcessClass bpc,String appType){
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
        AppPremisesRecommendationDto engageRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_ENGAGE).getEntity();
        AppPremisesRecommendationDto followRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSPCTION_FOLLOW_UP_ACTION).getEntity();

        AppPremisesRecommendationDto initRecommendationDto = new AppPremisesRecommendationDto();
        if (appPremisesRecommendationDto != null) {
            String reportRemarks = appPremisesRecommendationDto.getRemarks();
            String recomDecision = appPremisesRecommendationDto.getRecomDecision();
            if(InspectionReportConstants.APPROVED.equals(recomDecision)||InspectionReportConstants.APPROVEDLTC.equals(recomDecision)){
                initRecommendationDto.setRemarks(reportRemarks);
                Integer recomInNumber = appPremisesRecommendationDto.getRecomInNumber();
                String recommendationOnlyShowStr = getRecommendationOnlyShowStr(recomInNumber);
                initRecommendationDto.setPeriod(recommendationOnlyShowStr);
            }
            if(InspectionReportConstants.REJECTED.equals(recomDecision)){
                initRecommendationDto.setPeriod(InspectionReportConstants.REJECTED);
            }
            if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
                String recommendation = appPremisesRecommendationDto.getRecomDecision();
                if(InspectionReportConstants.RFC_APPROVED.equals(recommendation)){
                    initRecommendationDto.setPeriod(InspectionReportConstants.APPROVED);
                }
                if(InspectionReportConstants.RFC_REJECTED.equals(recommendation)){
                    initRecommendationDto.setPeriod(InspectionReportConstants.REJECTED);
                }
            }
        }
        if (engageRecommendationDto != null) {
            String remarks = engageRecommendationDto.getRemarks();
            String engage ;
            if(StringUtil.isEmpty(remarks)){
                engage = "off";
            }else {
                engage = "on";
            }
            initRecommendationDto.setEngageEnforcement(engage);
            initRecommendationDto.setEngageEnforcementRemarks(remarks);
        }
        if (followRecommendationDto != null) {
            String followRemarks = followRecommendationDto.getRemarks();
            initRecommendationDto.setFollowUpAction(followRemarks);
        }
        ParamUtil.setSessionAttr(bpc.request, RECOMMENDATION_DTO, initRecommendationDto);
    }

    private List<SelectOption> getProcessingDecision(String status) {
        if(ApplicationConsts.APPLICATION_STATUS_ROUTE_BACK.equals(status)){
            List<SelectOption> riskLevelResult = IaisCommonUtils.genNewArrayList();
            SelectOption so1 = new SelectOption("submit", "Give Clarification");
            riskLevelResult.add(so1);
            return riskLevelResult;
        }

        List<SelectOption> riskLevelResult = IaisCommonUtils.genNewArrayList();
        SelectOption so1 = new SelectOption(APPROVAL, "Acknowledge Inspection Report");
        SelectOption so2 = new SelectOption(REJECT, "Revise Inspection Report");
        riskLevelResult.add(so1);
        riskLevelResult.add(so2);
        return riskLevelResult;
    }
    private String getRecommendationOnlyShowStr (Integer recomInNumber){
        String recommendationOnlyShow = "-";
        if(recomInNumber >= 12){
            if( recomInNumber % 12 == 0){
                if(recomInNumber / 12 == 1){
                    recommendationOnlyShow = "1 Year";
                }else {
                    recommendationOnlyShow = recomInNumber / 12 + " Year(s)";
                }
            }else {
                if(recomInNumber / 12 == 1) {
                    if(recomInNumber % 12 == 1){
                        recommendationOnlyShow = 1 + " Year " + 1 + " Month";
                    }else {
                        recommendationOnlyShow = 1 + " Year " + recomInNumber % 12 + " Month(s)";
                    }

                }else {
                    if(recomInNumber % 12 == 1){
                        recommendationOnlyShow = recomInNumber / 12 + " Year(s) " + 1 + " Month";
                    }else {
                        recommendationOnlyShow = recomInNumber / 12 + " Year(s) " + recomInNumber % 12 + " Month(s)";
                    }
                }
            }
        }else {
            if( recomInNumber == 1){
                recommendationOnlyShow = recomInNumber + " Month";
            }else {
                recommendationOnlyShow = recomInNumber + " Month(s)";
            }
        }
        return recommendationOnlyShow;
    }

}
