package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecUserRecUploadDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionPreTaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AppValidatorHelper;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.InspectionHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.ApptInspectionDateService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionPreTaskService;
import com.ecquaria.cloud.moh.iais.service.InspectionRectificationProService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppSvcVehicleBeClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloudfeign.FeignException;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2019/12/18 14:22
 **/
@Delegator("inspectionRectificationProDelegator")
@Slf4j
public class InspectionRectificationProDelegator extends InspectionCheckListCommonMethodDelegator{

    @Autowired
    private InspectionRectificationProService inspectionRectificationProService;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private InspectionPreTaskService inspectionPreTaskService;

    @Autowired
    private InsRepService insRepService;

    @Autowired
    private AppSvcVehicleBeClient appSvcVehicleBeClient;

    @Autowired
    InspEmailService inspEmailService;

    @Autowired
    private ApptInspectionDateService apptInspectionDateService;


    private static final String SERLISTDTO ="serListDto";
    private static final String COMMONDTO ="commonDto";
    private static final String ADHOCLDTO ="adchklDto";
    private static final String TASKDTO ="taskDto";
    private static final String APPLICATIONVIEWDTO = "applicationViewDto";
    private static final String CHECKLISTFILEDTO = "checkListFileDto";
    private static final String ROLL_BACK_OPTIONS = "rollBackOptions";
    private static final String ROLL_BACK_VALUE_MAP = "rollBackValueMap";

    @Autowired
    private InspectionRectificationProDelegator(ApplicationViewService applicationViewService, TaskService taskService, InspectionRectificationProService inspectionRectificationProService){
        this.inspectionRectificationProService = inspectionRectificationProService;
        this.taskService = taskService;
        this.applicationViewService = applicationViewService;
    }

    /**
     * StartStep: inspectorProRectificationStart
     *
     * @param bpc
     * @throws
     */
    public void inspectorProRectificationStart(BaseProcessClass bpc){
        clearSessionForStartCheckList(bpc.request);
        log.debug(StringUtil.changeForLog("the inspectorProRectificationStart start ...."));
        String taskId = verifyTaskId(bpc);
        if(StringUtil.isEmpty(taskId)){
            return;
        }
        TaskDto taskDto = taskService.getTaskById(taskId);
        AuditTrailHelper.auditFunctionWithAppNo(AuditTrailConsts.MODULE_INSPECTION,  AuditTrailConsts.FUNCTION_INSPECTION_RECTIFICATION, taskDto.getApplicationNo());
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
        SearchParam searchParamGroup = (SearchParam)ParamUtil.getSessionAttr(bpc.request, "backendinboxSearchParam");
        ParamUtil.setSessionAttr(bpc.request,"backSearchParamFromHcsaApplication",searchParamGroup);
    }

    /**
     * StartStep: inspectorProRectificationInit
     *
     * @param bpc
     * @throws
     */
    public void inspectorProRectificationInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectorProRectificationInit start ...."));
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", null);
        ParamUtil.setSessionAttr(bpc.request, "processDecOption", null);
        ParamUtil.setSessionAttr(bpc.request, "inboxUrl", null);
        ParamUtil.setSessionAttr(bpc.request,SERLISTDTO,null);
        ParamUtil.setSessionAttr(bpc.request,ADHOCLDTO,null);
        ParamUtil.setSessionAttr(bpc.request,COMMONDTO,null);
        ParamUtil.setSessionAttr(bpc.request,APPLICATIONVIEWDTO,null);
        ParamUtil.setSessionAttr(bpc.request,CHECKLISTFILEDTO,null);
        ParamUtil.setSessionAttr(bpc.request, "inspectionReportDto", null);
        ParamUtil.setSessionAttr(bpc.request, "fileRepoDto", null);
        ParamUtil.setSessionAttr(bpc.request,"licenceDto", null);
        ParamUtil.setSessionAttr(bpc.request,ROLL_BACK_OPTIONS, null);
        ParamUtil.setSessionAttr(bpc.request,ROLL_BACK_VALUE_MAP, null);
        ParamUtil.setSessionAttr(bpc.request, "lrSelect", null);
    }

    /**
     * StartStep: inspectorProRectificationPre
     *
     * @param bpc
     * @throws
     */
    public void inspectorProRectificationPre(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectorProRectificationPre start ...."));
        InspectionPreTaskDto inspectionPreTaskDto = (InspectionPreTaskDto)ParamUtil.getSessionAttr(bpc.request, "inspectionPreTaskDto");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        if(inspectionPreTaskDto == null){
            inspectionPreTaskDto = new InspectionPreTaskDto();
            taskDto = taskService.getTaskById(taskDto.getId());
            applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(taskDto.getRefNo(), taskDto.getRoleId());
            ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
            inspectionPreTaskDto.setAppStatus(applicationDto.getStatus());
            //get vehicle no
            List<AppSvcVehicleDto> appSvcVehicleDtos = appSvcVehicleBeClient.getAppSvcVehicleDtoListByCorrId(taskDto.getRefNo()).getEntity();
            LicenceDto licenceDto = inspectionPreTaskService.getLicenceDtoByLicenceId(applicationDto.getOriginLicenceId());
            ParamUtil.setSessionAttr(bpc.request,"licenceDto", licenceDto);
            List<InspecUserRecUploadDto> inspecUserRecUploadDtos = IaisCommonUtils.genNewArrayList();
            List<ChecklistItemDto> checklistItemDtos = inspectionRectificationProService.getQuesAndClause(taskDto.getRefNo());
            AppPremPreInspectionNcDto appPremPreInspectionNcDto = inspectionRectificationProService.getAppPremPreInspectionNcDtoByCorrId(taskDto.getRefNo());
            Map<String, AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtoMap = inspectionRectificationProService.getNcItemDtoMap(appPremPreInspectionNcDto.getId());
            if(appPremisesPreInspectionNcItemDtoMap != null) {
                for (Map.Entry<String, AppPremisesPreInspectionNcItemDto> map : appPremisesPreInspectionNcItemDtoMap.entrySet()) {
                    //get AppPremisesPreInspectionNcItemDto
                    AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto = map.getValue();
                    String itemId = appPremisesPreInspectionNcItemDto.getItemId();
                    int feRecFlag = appPremisesPreInspectionNcItemDto.getFeRectifiedFlag();
                    int recFlag = appPremisesPreInspectionNcItemDto.getIsRecitfied();
                    //filter need show rectification nc
                    if (1 == feRecFlag && 0 == recFlag) {
                        InspecUserRecUploadDto iDto = new InspecUserRecUploadDto();
                        iDto.setAppPremisesPreInspectionNcItemDto(appPremisesPreInspectionNcItemDto);
                        //set Vehicle No. To Show
                        String vehicleNo = inspectionRectificationProService.getVehicleShowName(appPremisesPreInspectionNcItemDto.getVehicleName(), appSvcVehicleDtos);
                        iDto.setVehicleNo(vehicleNo);
                        iDto.setAppNo(applicationDto.getApplicationNo());
                        if (checklistItemDtos != null && !(checklistItemDtos.isEmpty())) {
                            iDto = setNcDataByItemId(iDto, itemId, checklistItemDtos);
                        }
                        if (!StringUtil.isEmpty(appPremisesPreInspectionNcItemDto.getFeRemarks())) {
                            iDto.setUploadRemarks(appPremisesPreInspectionNcItemDto.getFeRemarks());
                        } else {
                            iDto.setUploadRemarks(HcsaConsts.HCSA_PREMISES_HCI_NULL);
                        }
                        iDto.setAppPremisesPreInspectionNcItemDto(appPremisesPreInspectionNcItemDto);
                        List<AppPremPreInspectionNcDocDto> appPremPreInspectionNcDocDtos = inspectionRectificationProService.getAppNcDocList(appPremisesPreInspectionNcItemDto.getId());
                        List<FileRepoDto> fileRepoDtos = inspectionRectificationProService.getFileByItemId(appPremPreInspectionNcDocDtos);
                        iDto.setAppPremPreInspectionNcDocDtos(appPremPreInspectionNcDocDtos);
                        iDto.setFileRepoDtos(fileRepoDtos);
                        inspecUserRecUploadDtos.add(iDto);
                    }
                }
            }

            inspectionPreTaskDto.setInspecUserRecUploadDtos(inspecUserRecUploadDtos);
        }
        List<SelectOption> processDecOption = inspectionRectificationProService.getProcessRecDecOption();
        String appType = applicationViewDto.getApplicationDto().getApplicationType();
        if (!(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(appType) || ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(appType)  || ApplicationConsts.APPLICATION_TYPE_CESSATION.equals(appType))) {
            processDecOption.addAll(MasterCodeUtil.retrieveOptionsByCodes(new String[]{InspectionConstants.PROCESS_DECI_ROLL_BACK}));
        }
        processDecOption.addAll(MasterCodeUtil.retrieveOptionsByCodes(new String[]{ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY}));
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", inspectionPreTaskDto);
        ParamUtil.setSessionAttr(bpc.request, "processDecOption", (Serializable) processDecOption);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        //init roll back params
        Map<String, AppPremisesRoutingHistoryDto> historyDtoMap = IaisCommonUtils.genNewHashMap();
        ParamUtil.setSessionAttr(bpc.request,ROLL_BACK_OPTIONS,(Serializable) inspectionService.getRollBackSelectOptions(applicationViewDto.getRollBackHistroyList(), historyDtoMap, taskDto.getRoleId()));
        ParamUtil.setSessionAttr(bpc.request,ROLL_BACK_VALUE_MAP, (Serializable) historyDtoMap);
        //Can edit application
        InspectionHelper.checkForEditingApplication(bpc.request);
    }

    private InspecUserRecUploadDto setNcDataByItemId(InspecUserRecUploadDto iDto, String itemId, List<ChecklistItemDto> checklistItemDtos) {
        int index = 0;
        for(ChecklistItemDto checklistItemDto : checklistItemDtos){
            if(itemId.equals(checklistItemDto.getItemId())){
                iDto.setCheckClause(checklistItemDto.getRegulationClause());
                iDto.setCheckQuestion(checklistItemDto.getChecklistItem());
                iDto.setIndex(index++);
                iDto.setItemId(checklistItemDto.getItemId());
                //To prevent the repeat, because have the same item by different Config
                return iDto;
            }
        }
        //There are no matching items, search item from adhoc table
        String adhocItemId = itemId;
        AdhocChecklistItemDto adhocChecklistItemDto = inspectionRectificationProService.getAdhocChecklistItemById(adhocItemId);
        if(adhocChecklistItemDto != null){
            String checkItemId = adhocChecklistItemDto.getItemId();
            if(!StringUtil.isEmpty(checkItemId)){
                ChecklistItemDto checklistItemDto = inspectionRectificationProService.getChklItemById(checkItemId);
                iDto.setCheckClause("-");
                iDto.setCheckQuestion(checklistItemDto.getChecklistItem());
                iDto.setItemId(adhocItemId);
            } else {
                iDto.setCheckClause("-");
                iDto.setCheckQuestion(adhocChecklistItemDto.getQuestion());
                iDto.setItemId(adhocItemId);
            }
        }
        return iDto;
    }

    /**
     * StartStep: inspectorProRectificationValid
     *
     * @param bpc
     * @throws
     */
    public void inspectorProRectificationValid(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectorProRectificationValid start ...."));
        InspectionPreTaskDto inspectionPreTaskDto = (InspectionPreTaskDto)ParamUtil.getSessionAttr(bpc.request, "inspectionPreTaskDto");
        String actionValue = ParamUtil.getRequestString(bpc.request, "actionValue");
        String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
        String condRemarks = ParamUtil.getString(bpc.request,"condRemarks");
        String processDec = ParamUtil.getRequestString(bpc.request,"processDec");
        String[] ncItemId = ParamUtil.getStrings(bpc.request,"ncItemCheck");
        List<String> ncItemIdList = IaisCommonUtils.genNewArrayList();
        if(ncItemId != null && ncItemId.length > 0){
            for(int i = 0; i < ncItemId.length; i++){
                if(!StringUtil.isEmpty(ncItemId[i])){
                    ncItemIdList.add(ncItemId[i]);
                }
            }
        } else {
            ncItemIdList = null;
        }
        inspectionPreTaskDto.setCheckRecRfiNcItems(ncItemIdList);
        inspectionPreTaskDto.setInternalMarks(internalRemarks);
        if(InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION_CONDITION.equals(processDec)){
            inspectionPreTaskDto.setSelectValue(processDec);
            inspectionPreTaskDto.setAccCondMarks(condRemarks);
        } else if(InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION.equals(processDec)){
            inspectionPreTaskDto.setSelectValue(processDec);
        } else if(InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION.equals(processDec)) {
            inspectionPreTaskDto.setSelectValue(processDec);
        } else if(InspectionConstants.PROCESS_DECI_ROLL_BACK.equals(processDec)){
            inspectionPreTaskDto.setSelectValue(processDec);
        } else if(ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY.equals(processDec)){
            inspectionPreTaskDto.setSelectValue(processDec);
        } else {
            inspectionPreTaskDto.setSelectValue(null);
        }
        doValidateByInspPreTaskDto(inspectionPreTaskDto, actionValue, bpc);
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", inspectionPreTaskDto);
    }

    private void doValidateByInspPreTaskDto(InspectionPreTaskDto inspectionPreTaskDto, String actionValue, BaseProcessClass bpc) {
        if(InspectionConstants.SWITCH_ACTION_ACCEPTS.equals(actionValue)){
            ValidationResult validationResult = WebValidationHelper.validateProperty(inspectionPreTaskDto,"prorecr");
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
                ParamUtil.setRequestAttr(bpc.request, "validateShowPage", InspectionConstants.SWITCH_ACTION_ACK);
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
            }
        } else if(InspectionConstants.SWITCH_ACTION_ACCEPTS_CONDITION.equals(actionValue)){
            ValidationResult validationResult = WebValidationHelper.validateProperty(inspectionPreTaskDto,"procond");
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
                ParamUtil.setRequestAttr(bpc.request, "validateShowPage", InspectionConstants.SWITCH_ACTION_ACK);
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
            }
        } else if(InspectionConstants.SWITCH_ACTION_REQUEST_INFORMATION.equals(actionValue)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(inspectionPreTaskDto,"recrfi");
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
                if(!StringUtil.isEmpty(inspectionPreTaskDto.getSelectValue()) && IaisCommonUtils.isEmpty(inspectionPreTaskDto.getCheckRecRfiNcItems())) {
                    ParamUtil.setRequestAttr(bpc.request, "validateShowPage", InspectionConstants.SWITCH_ACTION_REQUEST_INFORMATION);
                } else {
                    ParamUtil.setRequestAttr(bpc.request, "validateShowPage", InspectionConstants.SWITCH_ACTION_ACK);
                }
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
            }
        } else if("rollBack".equals(actionValue)){
            String rollBackTo = ParamUtil.getRequestString(bpc.request, "rollBackTo");
            if(StringUtil.isEmpty(rollBackTo)){
                Map<String, String> errorMap = IaisCommonUtils.genNewHashMap(1);
                errorMap.put("rollBackTo", "GENERAL_ERR0006");
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
                ParamUtil.setRequestAttr(bpc.request, "validateShowPage", InspectionConstants.SWITCH_ACTION_ACK);
            }else {
                ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
            }
        } else if("routeLaterally".equals(actionValue)){
            ParamUtil.setSessionAttr(bpc.request, "lrSelect", null);
            String lrSelect = ParamUtil.getRequestString(bpc.request, "lrSelect");
            ParamUtil.setSessionAttr(bpc.request, "lrSelect", lrSelect);
            Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
            if (StringUtil.isEmpty(inspectionPreTaskDto.getInternalMarks())) {
                errorMap.put("internalRemarks1", "GENERAL_ERR0006");
            }else {
                if (inspectionPreTaskDto.getInternalMarks().length() > 300) {
                    errorMap.put("internalRemarks1", AppValidatorHelper.repLength("Remarks", "300"));
                }
            }
            if (StringUtil.isEmpty(lrSelect)) {
                errorMap.put("lrSelectIns", "GENERAL_ERR0006");
            }
            if (!errorMap.isEmpty()){
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                WebValidationHelper.saveAuditTrailForNoUseResult(errorMap);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
                ParamUtil.setRequestAttr(bpc.request, "validateShowPage", InspectionConstants.SWITCH_ACTION_ACK);
            } else {
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.TRUE);
            }

        }else if(InspectionConstants.SWITCH_ACTION_BACK.equals(actionValue) || InspectionConstants.SWITCH_ACTION_VIEW.equals(actionValue) ||
                InspectionConstants.SWITCH_ACTION_INSPECTOR_CHECKLIST.equals(actionValue)){
            ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
        }
    }

    /**
     * StartStep: inspectorProRectificationReq
     *
     * @param bpc
     * @throws
     */
    public void inspectorProRectificationReq(BaseProcessClass bpc) throws IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the inspectorProRectificationReq start ...."));
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        InspectionPreTaskDto inspectionPreTaskDto = (InspectionPreTaskDto)ParamUtil.getSessionAttr(bpc.request, "inspectionPreTaskDto");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        inspectionRectificationProService.routingTaskToReport(taskDto, inspectionPreTaskDto, applicationViewDto, loginContext);
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", inspectionPreTaskDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
    }

    /**
     * StartStep: inspectorProRectificationAcc
     *
     * @param bpc
     * @throws
     */
    public void inspectorProRectificationAcc(BaseProcessClass bpc) throws IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the inspectorProRectificationAcc start ...."));
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        InspectionPreTaskDto inspectionPreTaskDto = (InspectionPreTaskDto)ParamUtil.getSessionAttr(bpc.request, "inspectionPreTaskDto");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        inspectionRectificationProService.routingTaskToReport(taskDto, inspectionPreTaskDto, applicationViewDto, loginContext);
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", inspectionPreTaskDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
    }

    /**
     * StartStep: inspectorProRectificationAccCond
     *
     * @param bpc
     * @throws
     */
    public void inspectorProRectificationAccCond(BaseProcessClass bpc) throws IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the inspectorProRectificationAccCond start ...."));
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        InspectionPreTaskDto inspectionPreTaskDto = (InspectionPreTaskDto)ParamUtil.getSessionAttr(bpc.request, "inspectionPreTaskDto");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        inspectionRectificationProService.routingTaskToReport(taskDto, inspectionPreTaskDto, applicationViewDto, loginContext);
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", inspectionPreTaskDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER, loginContext);
    }

    /**
     * StartStep: InspectorProRectificationView
     *
     * @param bpc
     * @throws
     */
    public void InspectorProRectificationView(BaseProcessClass bpc) throws FeignException {
        log.debug(StringUtil.changeForLog("the InspectorProRectificationView start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        InspectionReportDto inspectionReportDto = insRepService.getInsRepDto(taskDto, applicationViewDto, loginContext);
        //get fileReportId
        AppPremisesSpecialDocDto appPremisesSpecialDocDto = fillupChklistService.getAppPremisesSpecialDocDtoByRefNo(taskDto.getRefNo());
        //get file report
        FileRepoDto fileRepoDto = null;
        if(appPremisesSpecialDocDto != null && !StringUtil.isEmpty(appPremisesSpecialDocDto.getFileRepoId())) {
            fileRepoDto = inspectionRectificationProService.getFileReportById(appPremisesSpecialDocDto.getFileRepoId());
            fileRepoDto = inspectionRectificationProService.getCheckListFileRealName(fileRepoDto, taskDto.getRefNo(), AppConsts.COMMON_STATUS_ACTIVE, ApplicationConsts.APP_DOC_TYPE_CHECK_LIST);
            inspectionReportDto.setPracticesFileId(appPremisesSpecialDocDto.getFileRepoId());
        }
        //set show data for view checklist
        inspectionReportDto = setViewCheckListData(taskDto, inspectionReportDto, loginContext);

        ParamUtil.setSessionAttr(bpc.request, "inspectionReportDto", inspectionReportDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
        ParamUtil.setSessionAttr(bpc.request, "fileRepoDto", fileRepoDto);
    }

    private InspectionReportDto setViewCheckListData(TaskDto taskDto, InspectionReportDto inspectionReportDto, LoginContext loginContext) {
        if(inspectionReportDto != null) {
            //get inspector lead
            List<String> inspectorLeads = inspectionRectificationProService.getInspectorLeadsByWorkGroupId(taskDto.getWkGrpId());
            String inspectorLeadShow = getInspectorLeadShowByList(inspectorLeads);
            //get inspectors
            InspectionReportDto inspectorUser = insRepService.getInspectorUser(taskDto, loginContext);
            //get nc count
            int ncCount = inspectionRectificationProService.getHowMuchNcByAppPremCorrId(taskDto.getRefNo());
            //set best Practice
            AppPremisesRecommendationDto ncRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(taskDto.getRefNo(), InspectionConstants.RECOM_TYPE_TCU).getEntity();
            String bestPractice = "-";
            if (!StringUtil.isEmpty(ncRecommendationDto.getBestPractice())) {
                bestPractice = ncRecommendationDto.getBestPractice();
            }
            //get Observation
            String observation = fillupChklistService.getObservationByAppPremCorrId(taskDto.getRefNo());
            if(StringUtil.isEmpty(observation)) {
                observation = "-";
            }
            //get task Remarks
            String taskRemarks = "-";
            if (!StringUtil.isEmpty(ncRecommendationDto.getRemarks())) {
                taskRemarks = ncRecommendationDto.getRemarks();
            }
            inspectionReportDto.setObservation(observation);
            inspectionReportDto.setBestPractice(bestPractice);
            inspectionReportDto.setTaskRemarks(taskRemarks);
            inspectionReportDto.setInspectors(inspectorUser.getInspectors());
            inspectionReportDto.setInspectorLeadStr(inspectorLeadShow);
            inspectionReportDto.setInspectorLeads(inspectorLeads);
            inspectionReportDto.setNcCount(ncCount);
        }
        return inspectionReportDto;
    }

    private String getInspectorLeadShowByList(List<String> inspectorLeads) {
        StringBuilder leadStrBu = new StringBuilder();
        if(!IaisCommonUtils.isEmpty(inspectorLeads)) {
            Collections.sort(inspectorLeads);
            for (String strLeadName : inspectorLeads) {
                if (StringUtil.isEmpty(leadStrBu.toString())) {
                    leadStrBu.append(strLeadName);
                } else {
                    leadStrBu.append(',');
                    leadStrBu.append(' ');
                    leadStrBu.append(strLeadName);
                }
            }
        } else {
            leadStrBu.append('-');
        }
        return leadStrBu.toString();
    }

    /**
     * StartStep: InspectorProRectificationStep
     *
     * @param bpc
     * @throws
     */
    public void InspectorProRectificationStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the InspectorProRectificationStep start ...."));
    }

    /**
     * StartStep: InspectorProRectificationChList
     *
     * @param bpc
     * @throws
     */
    public void InspectorProRectificationChList(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the InspectorProRectificationChList start ...."));
        HttpServletRequest request = bpc.request;
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(request, "taskDto");
        setCheckDataHaveFinished(request,taskDto);
    }

    /**
     * StartStep: InspectorProRecCheckListStep
     *
     * @param bpc
     * @throws
     */
    public void InspectorProRecCheckListStep(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the InspectorProRecCheckListStep start ...."));
    }

    /**
     * StartStep: RollBack
     *
     * @param bpc
     * @throws
     */
    public void rollBack(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the rollBack start ...."));
        HttpServletRequest request = bpc.request;
        String internalRemarks = ParamUtil.getString(bpc.request,"internalRemarks");
        String rollBackTo = ParamUtil.getRequestString(bpc.request, "rollBackTo");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(request, "taskDto");
        Map<String, AppPremisesRoutingHistoryDto> historyDtoMap = (Map<String, AppPremisesRoutingHistoryDto>) ParamUtil.getSessionAttr(request, ROLL_BACK_VALUE_MAP);

        inspectionService.rollBack(bpc, taskDto, applicationViewDto, historyDtoMap.get(rollBackTo), internalRemarks);
        ParamUtil.setRequestAttr(bpc.request, "isRollBack",AppConsts.TRUE);
    }


    /**
     * StartStep: routeLaterally
     *
     * @param bpc
     * @throws
     */
    public void routeLaterally(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the routeLaterally start ...."));
        HttpServletRequest request = bpc.request;
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request, "taskDto");
        String lrSelect = ParamUtil.getRequestString(request, "lrSelect");
        ApplicationViewDto applicationViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        InspectionPreTaskDto inspectionPreTaskDto = (InspectionPreTaskDto)ParamUtil.getSessionAttr(bpc.request, "inspectionPreTaskDto");

        log.info(StringUtil.changeForLog("The lrSelect is -->:"+lrSelect));
        String currentUserId = AccessUtil.getLoginUser(request).getUserId();
        String[] lrSelects =  lrSelect.split("_");
        String workGroupId = lrSelects[0];
        String userId = lrSelects[1];
        inspEmailService.completedTask(taskDto);
        List<TaskDto> taskDtos = IaisCommonUtils.genNewArrayList();
        taskDto.setUserId(userId);
        taskDto.setDateAssigned(new Date());
        taskDto.setId(null);
        taskDto.setWkGrpId(workGroupId);
        taskDto.setSlaDateCompleted(null);
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        taskDtos.add(taskDto);
        taskService.createTasks(taskDtos);
        apptInspectionDateService.createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW, ApplicationConsts.PROCESSING_DECISION_ROUTE_LATERALLY,taskDto,currentUserId,inspectionPreTaskDto.getInternalMarks(), HcsaConsts.ROUTING_STAGE_INS);
        ParamUtil.setRequestAttr(bpc.request, "isRollBack", "laterally");
        ParamUtil.setRequestAttr(bpc.request, "routeLaterally",AppConsts.TRUE);
    }
}
