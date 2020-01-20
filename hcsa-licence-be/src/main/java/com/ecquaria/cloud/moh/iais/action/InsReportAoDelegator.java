package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloudfeign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.Serializable;
import java.util.*;

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
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;
    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private AppPremisesCorrClient appPremisesCorrClient;
    @Autowired
    AppInspectionStatusClient appInspectionStatusClient;
    @Autowired
    private AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient;

    private final String RECOMMENDATION_DTO= "appPremisesRecommendationDto";
    private final String RECOMMENDATION="recommendation";
    private final String CHRONO="chrono";
    private final String NUMBER="number";
    private final String OTHERS="Others";
    private final String APPROVAL="Approval";
    private final String REJECT="Reject";
    public void start(BaseProcessClass bpc) {

        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>report");
        AuditTrailHelper.auditFunction("Inspection Report", "Assign Report");
    }

    public void AoInit(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("the inspectionReportInit start ...."));
        AccessUtil.initLoginUserInfo(bpc.request);
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", null);
        ParamUtil.setSessionAttr(bpc.request, RECOMMENDATION_DTO, null);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", null);
    }


    public void AoReportPre(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>prepareReportData");
        LoginContext loginContext = (LoginContext) ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        String taskId;
        taskId = ParamUtil.getRequestString(bpc.request,"taskId");
        if(StringUtil.isEmpty(taskId)){
            taskId = "B7A46131-6637-EA11-BE7E-000C29F371DC";
        }
        TaskDto taskDto = taskService.getTaskById(taskId);
        String correlationId = taskDto.getRefNo();
        ApplicationViewDto applicationViewDto = insRepService.getApplicationViewDto(correlationId);
        InspectionReportDto insRepDto = insRepService.getInsRepDto(taskDto,applicationViewDto,loginContext);
        InspectionReportDto inspectorAo = insRepService.getInspectorAo(applicationViewDto);
        insRepDto.setInspectors(inspectorAo.getInspectors());
        insRepDto.setReportNoteBy(inspectorAo.getReportNoteBy());
        insRepDto.setReportedBy(inspectorAo.getReportedBy());

        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(correlationId, InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT).getEntity();
        String chronoUnit = appPremisesRecommendationDto.getChronoUnit();
        Integer recomInNumber = appPremisesRecommendationDto.getRecomInNumber();
        String option  = recomInNumber + chronoUnit;
        List<SelectOption> riskOption = insRepService.getRiskOption(applicationViewDto);
        SelectOption so1 = new SelectOption(APPROVAL,APPROVAL);
        SelectOption so2 = new SelectOption(REJECT,REJECT);
        riskOption.add(so1);
        riskOption.add(so2);
        List<SelectOption> chronoOption = getChronoOption();
        ParamUtil.setSessionAttr(bpc.request, "chronoOption", (Serializable) chronoOption);
        ParamUtil.setSessionAttr(bpc.request, "riskOption", (Serializable)riskOption);
        ParamUtil.setSessionAttr(bpc.request, "option", option);
        ParamUtil.setSessionAttr(bpc.request, RECOMMENDATION_DTO, appPremisesRecommendationDto);
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", insRepDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
    }

    public void action(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the action start ...."));
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(bpc.request, "insRepDto");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        TaskDto taskDto =  (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", insRepDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
    }

    public void back(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the back start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        TaskDto taskDto =  (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        insRepService.routBackTaskToInspector(taskDto,applicationDto,appPremisesCorrelationId);
    }
    public void approve(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the inspectorReportAction start ...."));
        InspectionReportDto insRepDto = (InspectionReportDto) ParamUtil.getSessionAttr(bpc.request, "insRepDto");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        TaskDto taskDto =  (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        String appPremisesCorrelationId = applicationViewDto.getAppPremisesCorrelationId();
        AppPremisesRecommendationDto preapreRecommendationDto = prepareRecommendation(bpc);
        ParamUtil.setSessionAttr(bpc.request, "preapreRecommendationDto", preapreRecommendationDto);
        ValidationResult validationResult = WebValidationHelper.validateProperty(preapreRecommendationDto, "edit");
        if (validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG,WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.FALSE);
            return;
        }
        AppPremisesRecommendationDto recommendationDto = prepareRecommendation(bpc, appPremisesCorrelationId);
        insRepService.updateRecommendation(recommendationDto);
        if(REJECT.equals(recommendationDto.getRecommendation())){
            insRepService.routBackTaskToInspector(taskDto,applicationDto,appPremisesCorrelationId);
            return;
        }
        insRepService.routingTaskToAo2(taskDto,applicationDto,appPremisesCorrelationId);
        ParamUtil.setSessionAttr(bpc.request, "insRepDto", insRepDto);
        ParamUtil.setRequestAttr(bpc.request,IntranetUserConstant.ISVALID,IntranetUserConstant.TRUE);
    }

    private AppPremisesRecommendationDto prepareRecommendation (BaseProcessClass bpc,String appPremisesCorrelationId){
        String recommendation = ParamUtil.getRequestString(bpc.request, RECOMMENDATION);
        String chrono = ParamUtil.getRequestString(bpc.request, CHRONO);
        String number = ParamUtil.getRequestString(bpc.request, NUMBER);
        ParamUtil.setSessionAttr(bpc.request, CHRONO, chrono);
        ParamUtil.setSessionAttr(bpc.request, NUMBER, number);
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        appPremisesRecommendationDto.setRecomInDate(new Date());
        appPremisesRecommendationDto.setRecomDecision(InspectionConstants.PROCESS_DECI_REVIEW_INSPECTION_REPORT);
        appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSEPCTION_REPORT);
        appPremisesRecommendationDto.setRecommendation(recommendation);
        if(OTHERS.equals(recommendation)&&!StringUtil.isEmpty(chrono)&&!StringUtil.isEmpty(number)){
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
            appPremisesRecommendationDto.setChronoUnit(chrono);
            appPremisesRecommendationDto.setRecomInNumber(Integer.parseInt(number));
        }else if(APPROVAL.equals(recommendation)){
            appPremisesRecommendationDto.setRecomDecision(ApplicationConsts.APPLICATION_STATUS_APPROVED);
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
        }else if(REJECT.equals(recommendation)){
            appPremisesRecommendationDto.setRecomDecision(ApplicationConsts.APPLICATION_STATUS_REJECTED);
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
        } else {
            String[] split_number = recommendation.split("\\D");
            String[] split_unit = recommendation.split("\\d");
            String chronoRe = split_unit[1];
            String numberRe = split_number[0];
            appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationId);
            appPremisesRecommendationDto.setChronoUnit(chronoRe);
            appPremisesRecommendationDto.setRecomInNumber(Integer.parseInt(numberRe));
            appPremisesRecommendationDto.setRecommendation(recommendation);
        }
        return appPremisesRecommendationDto;
    }
    private AppPremisesRecommendationDto prepareRecommendation (BaseProcessClass bpc){
        String recommendation = ParamUtil.getRequestString(bpc.request, RECOMMENDATION);
        String chrono = ParamUtil.getRequestString(bpc.request, CHRONO);
        String number = ParamUtil.getRequestString(bpc.request, NUMBER);
        ParamUtil.setSessionAttr(bpc.request, CHRONO, chrono);
        ParamUtil.setSessionAttr(bpc.request, NUMBER, number);
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        appPremisesRecommendationDto.setRecommendation(recommendation);
        return appPremisesRecommendationDto;
    }

    private List<SelectOption> getChronoOption() {
        List<SelectOption> ChronoResult = new ArrayList<>();
        SelectOption so1 = new SelectOption("Year", "Year");
        SelectOption so2 = new SelectOption("Month", "Month");
        SelectOption so3 = new SelectOption("Week", "Week");
        ChronoResult.add(so1);
        ChronoResult.add(so2);
        ChronoResult.add(so3);
        return ChronoResult;
    }

}
