package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecUserRecUploadDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFDtosDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
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
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InsRepService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.InspectionRectificationProService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2019/12/18 14:22
 **/
@Delegator("inspectionRectificationProDelegator")
@Slf4j
public class InspectionRectificationProDelegator {

    @Autowired
    private InspectionRectificationProService inspectionRectificationProService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private FillupChklistService fillupChklistService;

    @Autowired
    private InsepctionNcCheckListService insepctionNcCheckListService;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private InsRepService insRepService;

    private static final String SERLISTDTO="serListDto";
    private static final String COMMONDTO="commonDto";
    private static final String ADHOCLDTO="adchklDto";
    private static final String TASKDTO="taskDto";
    private static final String APPLICATIONVIEWDTO = "applicationViewDto";
    private static final String CHECKLISTFILEDTO = "checkListFileDto";

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
        log.debug(StringUtil.changeForLog("the inspectorProRectificationStart start ...."));
        AuditTrailHelper.auditFunction("Inspection Rectification Process", "Inspector Processing Rectification");
    }

    /**
     * StartStep: inspectorProRectificationInit
     *
     * @param bpc
     * @throws
     */
    public void inspectorProRectificationInit(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the inspectorProRectificationInit start ...."));
        AccessUtil.initLoginUserInfo(bpc.request);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", null);
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", null);
        ParamUtil.setSessionAttr(bpc.request, "processDecOption", null);
        ParamUtil.setSessionAttr(bpc.request, "inboxUrl", null);
        ParamUtil.setSessionAttr(bpc.request,SERLISTDTO,null);
        ParamUtil.setSessionAttr(bpc.request,ADHOCLDTO,null);
        ParamUtil.setSessionAttr(bpc.request,COMMONDTO,null);
        ParamUtil.setSessionAttr(bpc.request,TASKDTO,null);
        ParamUtil.setSessionAttr(bpc.request,APPLICATIONVIEWDTO,null);
        ParamUtil.setSessionAttr(bpc.request,CHECKLISTFILEDTO,null);
        ParamUtil.setSessionAttr(bpc.request, "inspectionReportDto", null);
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
            String taskId = ParamUtil.getRequestString(bpc.request, "taskId");
            taskDto = taskService.getTaskById(taskId);
            applicationViewDto = applicationViewService.getApplicationViewDtoByCorrId(taskDto.getRefNo());
            ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
            inspectionPreTaskDto.setAppStatus(applicationDto.getStatus());

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
                        iDto.setAppNo(applicationDto.getApplicationNo());
                        if (checklistItemDtos != null && !(checklistItemDtos.isEmpty())) {
                            iDto = setNcDataByItemId(iDto, itemId, checklistItemDtos);
                        }
                        if (!StringUtil.isEmpty(appPremisesPreInspectionNcItemDto.getRemarks())) {
                            iDto.setUploadRemarks(appPremisesPreInspectionNcItemDto.getRemarks());
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
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
        ParamUtil.setSessionAttr(bpc.request, "inspectionPreTaskDto", inspectionPreTaskDto);
        ParamUtil.setSessionAttr(bpc.request, "processDecOption", (Serializable) processDecOption);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
    }

    private InspecUserRecUploadDto setNcDataByItemId(InspecUserRecUploadDto iDto, String itemId, List<ChecklistItemDto> checklistItemDtos) {
        int index = 0;
        for(ChecklistItemDto checklistItemDto : checklistItemDtos){
            if(checklistItemDto.getItemId().equals(itemId)){
                iDto.setCheckClause(checklistItemDto.getRegulationClause());
                iDto.setCheckQuestion(checklistItemDto.getChecklistItem());
                iDto.setIndex(index++);
                iDto.setItemId(checklistItemDto.getItemId());
                //To prevent the repeat, because have the same item by different Config
                return iDto;
            }
        }
        return iDto;
    }

    @RequestMapping(value = "/file-repo-popup", method = RequestMethod.GET)
    public @ResponseBody
    void filePopUpDownload(HttpServletRequest request, HttpServletResponse response) throws IOException {
        log.debug(StringUtil.changeForLog("filePopUpDownload start ...."));
        String fileRepoName = ParamUtil.getRequestString(request, "fileRepoName");
        String maskFileRepoIdName = ParamUtil.getRequestString(request, "filerepo");
        String fileRepoId = ParamUtil.getMaskedString(request, maskFileRepoIdName);
        if (StringUtil.isEmpty(fileRepoId)) {
            log.debug(StringUtil.changeForLog("file-repo-popup id is empty"));
            return;
        }
        byte[] fileData = inspectionRectificationProService.downloadFile(fileRepoId);
        response.setContentType("application/OCTET-STREAM");
        response.addHeader("Content-Disposition", "attachment;filename=" + fileRepoName);
        response.addHeader("Content-Length", "" + fileData.length);
        OutputStream ops = new BufferedOutputStream(response.getOutputStream());
        ops.write(fileData);
        ops.close();
        ops.flush();
        log.debug(StringUtil.changeForLog("filePopUpDownload end ...."));
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

        if(InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION_CONDITION.equals(processDec)){
            inspectionPreTaskDto.setSelectValue(processDec);
            inspectionPreTaskDto.setInternalMarks(internalRemarks);
            inspectionPreTaskDto.setAccCondMarks(condRemarks);
        } else if(InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION.equals(processDec)){
            inspectionPreTaskDto.setSelectValue(processDec);
            inspectionPreTaskDto.setInternalMarks(internalRemarks);
        } else if(InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION.equals(processDec)) {
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
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
                ParamUtil.setRequestAttr(bpc.request, "validateShowPage", InspectionConstants.SWITCH_ACTION_ACK);
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
            }
        } else if(InspectionConstants.SWITCH_ACTION_REQUEST_INFORMATION.equals(actionValue)) {
            ValidationResult validationResult = WebValidationHelper.validateProperty(inspectionPreTaskDto,"request");
            if (validationResult.isHasErrors()) {
                Map<String, String> errorMap = validationResult.retrieveAll();
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                ParamUtil.setRequestAttr(bpc.request, "flag", AppConsts.FALSE);
                ParamUtil.setRequestAttr(bpc.request, "validateShowPage", InspectionConstants.SWITCH_ACTION_ACK);
            } else {
                ParamUtil.setRequestAttr(bpc.request,"flag",AppConsts.TRUE);
            }
        }else if(InspectionConstants.SWITCH_ACTION_BACK.equals(actionValue)){
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
    public void InspectorProRectificationView(BaseProcessClass bpc){
        log.debug(StringUtil.changeForLog("the InspectorProRectificationView start ...."));
        ApplicationViewDto applicationViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(bpc.request, "applicationViewDto");
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        InspectionReportDto inspectionReportDto = insRepService.getInsRepDto(taskDto, applicationViewDto, loginContext);
        List<String> inspectorLeads = inspectionRectificationProService.getInspectorLeadsByWorkGroupId(taskDto.getWkGrpId());
        inspectionReportDto.setInspectorLeads(inspectorLeads);
        int ncCount = inspectionRectificationProService.getHowMuchNcByAppPremCorrId(taskDto.getRefNo());
        inspectionReportDto.setNcCount(ncCount);
        ParamUtil.setSessionAttr(bpc.request, "inspectionReportDto", inspectionReportDto);
        ParamUtil.setSessionAttr(bpc.request, "applicationViewDto", applicationViewDto);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
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
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        String appPremCorrId = taskDto.getRefNo();
        String taskId = taskDto.getId();
        //draft start
        InspectionFillCheckListDto maxComChkDto = fillupChklistService.getMaxVersionComAppChklDraft(appPremCorrId);
        List<InspectionFillCheckListDto> allComChkDtoList = fillupChklistService.getAllVersionComAppChklDraft(appPremCorrId);
        List<InspectionFDtosDto> fdtosdraft= fillupChklistService.geAllVersionServiceDraftList(appPremCorrId);
        InspectionFDtosDto maxVersionfdtos = fillupChklistService.getMaxVersionServiceDraft(fdtosdraft);
        List<InspectionFDtosDto> otherVersionfdtos = fillupChklistService.getOtherVersionfdtos(fdtosdraft);
        List<AdCheckListShowDto> otherVersionAdhocDraftList = fillupChklistService.getOtherAdhocList(appPremCorrId);
        ParamUtil.setSessionAttr(bpc.request,"otherVersionAdhocDraftList",(Serializable) otherVersionAdhocDraftList);
        ParamUtil.setSessionAttr(bpc.request,"otherVersionfdtos",(Serializable) otherVersionfdtos);
        ParamUtil.setSessionAttr(bpc.request,"allComChkDtoList",(Serializable)allComChkDtoList);
        ParamUtil.setSessionAttr(bpc.request,"maxComChkDto", maxComChkDto);
        //draft end
        InspectionFillCheckListDto commonDto = null;
        List<InspectionFillCheckListDto> cDtoList = fillupChklistService.getInspectionFillCheckListDtoList(taskId,"service");
        List<InspectionFillCheckListDto> commonList = fillupChklistService.getInspectionFillCheckListDtoList(taskId,"common");
        if(commonList != null && !commonList.isEmpty()){
            commonDto = commonList.get(0);
        }
        InspectionFDtosDto serListDto =  fillupChklistService.getInspectionFDtosDto(appPremCorrId,taskDto,cDtoList);
        AdCheckListShowDto adchklDto = fillupChklistService.getAdhocDraftByappCorrId(appPremCorrId);
        if(adchklDto == null){
            adchklDto = fillupChklistService.getAdhoc(appPremCorrId);
        }

        ParamUtil.setSessionAttr(bpc.request,TASKDTO,taskDto);
        ParamUtil.setSessionAttr(bpc.request,ADHOCLDTO,adchklDto);
        if(maxComChkDto != null){
            List<InspectionFillCheckListDto> coms = fillupChklistService.getInspectionFillCheckListDtoListForReview(taskId,"common");
            if(!IaisCommonUtils.isEmpty(coms)){
                commonDto = coms.get(0);
            }
        }
        // change common data;
        insepctionNcCheckListService.getInspectionFillCheckListDtoForShow(commonDto);
        ParamUtil.setSessionAttr(bpc.request,COMMONDTO,commonDto);
        if(maxVersionfdtos != null && !IaisCommonUtils.isEmpty(maxVersionfdtos.getFdtoList())){
            List<InspectionFillCheckListDto> ftos = fillupChklistService.getInspectionFillCheckListDtoListForReview(taskId,"service");
            serListDto.setFdtoList(ftos);
        }
        //  change service checklist data
        if(serListDto != null){
            List<InspectionFillCheckListDto> fdtoList = serListDto.getFdtoList();
            if(fdtoList != null && fdtoList.size() > 0){
                for(InspectionFillCheckListDto inspectionFillCheckListDto : fdtoList) {
                    insepctionNcCheckListService.getInspectionFillCheckListDtoForShow(inspectionFillCheckListDto);
                }
            }
        }
        ParamUtil.setSessionAttr(bpc.request,SERLISTDTO,serListDto);
        ParamUtil.setSessionAttr(bpc.request, "taskDto", taskDto);
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
}
