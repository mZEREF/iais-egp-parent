package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.AdhocChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.*;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaLicenceBeConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.CheckListVadlidateDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.*;
import com.ecquaria.cloud.moh.iais.validation.InspectionCheckListItemValidate;
import com.ecquaria.cloud.moh.iais.validation.InspectionCheckListValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.util.CopyUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author: jiahao
 * @Date: 2019/12/5 9:45
 */
@Delegator(value = "inspectionNcCheckListDelegator")
@Slf4j
public class InspectionNcCheckListDelegator extends InspectionCheckListCommonMethodDelegator{

    private static final String SERLISTDTO="serListDto";
    private static final String COMMONDTO="commonDto";
    private static final String ADHOCLDTO="adchklDto";
    private static final String TASKDTO="taskDto";
    private static final String APPLICATIONVIEWDTO = "applicationViewDto";
    private static final String TASKDTOLIST = "InspectionNcCheckListDelegator_taskDtoList";
    private static final String INSPECTION_ADHOC_CHECKLIST_LIST_ATTR  = "inspection_adhoc_checklist_list_attr";
    private static final String INSPECTION_USERS = "inspectorsParticipant";
    private static final String INSPECTION_USER_FINISH = "inspectorUserFinishChecklistId";
    private static final String ACTION_ADHOC_OWN = "action_adhoc_own";
    private static final String BEFORE_FINISH_CHECK_LIST = "inspectionNcCheckListDelegator_before_finish_check_list";
    private static final String MOBILE_REMARK_GROUP = "mobile_remark_group";
    public InspectionNcCheckListDelegator(InsepctionNcCheckListService insepctionNcCheckListService){
        this.insepctionNcCheckListService = insepctionNcCheckListService;
    }

    public void start(BaseProcessClass bpc) {
        log.info("=======inspectionNcCheckListDelegator start=======");
        HttpServletRequest request = bpc.request;
        clearSessionForStartCheckList(request);
        SearchParam searchParamGroup = (SearchParam)ParamUtil.getSessionAttr(request, "backendinboxSearchParam");
        ParamUtil.setSessionAttr(request,"backSearchParamFromHcsaApplication",searchParamGroup);
    }

    public void successViewPre(BaseProcessClass bpc){
        log.info("=======inspectionNcCheckListDelegator successViewPre=======");
        HttpServletRequest request = bpc.request;
        InspectionFDtosDto serListDto  = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SERLISTDTO);
        serListDto.setCheckListTab("chkList");
        ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
    }

    public void successViewBack(BaseProcessClass bpc){
        log.info("=======inspectionNcCheckListDelegator successViewBack=======");
        HttpServletRequest request = bpc.request;
        log.info(StringUtil.changeForLog(request.getRequestURI()));
    }

    private boolean setCsrf(BaseProcessClass bpc,TaskDto taskDto){
        if( taskDto == null || TaskConsts.TASK_STATUS_COMPLETED.equals(taskDto.getTaskStatus()) || TaskConsts.TASK_STATUS_REMOVE.equals(taskDto.getTaskStatus()) || taskDto.getUpdateCount() ==1) {
            try{
                IaisEGPHelper.redirectUrl(bpc.response, "https://"+bpc.request.getServerName()+"/hcsa-licence-web/CsrfErrorPage.jsp");
            } catch (IOException ioe){
                log.error(ioe.getMessage(),ioe);
            }
            return true;
        }else {
            return  false;
        }
    }
    public void init(BaseProcessClass bpc){
        log.info("======inspectionNcCheckListDelegator initRequest======");
        HttpServletRequest request = bpc.request;
        String taskId = verifyTaskId(bpc);
        if(StringUtil.isEmpty(taskId)){
            return;
        }

        TaskDto taskDto = taskService.getTaskById(taskId);
        if(setCsrf(bpc,taskDto)){
            return;
        }
        setCheckListUnFinishedTask(request,taskDto);
    }

    private void setCheckListUnFinishedTask(HttpServletRequest request,TaskDto taskDto){
        String appPremCorrId = taskDto.getRefNo();
        String taskId = taskDto.getId();
        List<TaskDto> taskDtos = fillupChklistService.getCurrTaskByRefNo(taskDto);
        ParamUtil.setSessionAttr(request,TASKDTOLIST ,(Serializable) taskDtos);
        ApplicationViewDto appViewDto = fillupChklistService.getAppViewDto(taskId);
        AuditTrailHelper.auditFunctionWithAppNo(AuditTrailConsts.MODULE_INSPECTION, AuditTrailConsts.FUNCTION_INSPECTION_CHECKLIST,appViewDto.getApplicationDto().getApplicationNo());
        boolean beforeFinishList =  fillupChklistService.isBeforeFinishCheckList(appPremCorrId);
        boolean needVehicleSeparation = checklistNeedVehicleSeparation(appViewDto);
        InspectionFillCheckListDto commonDto = null;
        List<InspectionFillCheckListDto>   cDtoList ;
        List<InspectionFillCheckListDto>   commonList;
        AdCheckListShowDto adchklDto;
        if( beforeFinishList){
            cDtoList =  fillupChklistService.getInspectionFillCheckListDtoListForReview(taskId,"service",false);
            commonList = fillupChklistService.getInspectionFillCheckListDtoListForReview(taskId,"common",false);
            adchklDto =  insepctionNcCheckListService.getAdhocCheckListDto(appPremCorrId);
        }else {
            cDtoList = fillupChklistService.getInspectionFillCheckListDtoList(taskId,"service",false);
            commonList = fillupChklistService.getInspectionFillCheckListDtoList(taskId,"common",false);
            adchklDto = fillupChklistService.getAdhocDraftByappCorrId(appPremCorrId);
            if(adchklDto==null){
                adchklDto = fillupChklistService.getAdhoc(appPremCorrId);
            }
        }
        if(IaisCommonUtils.isNotEmpty(commonList)){
            commonDto = commonList.get(0);
        }
        InspectionFDtosDto serListDto =  fillupChklistService.getInspectionFDtosDto(appPremCorrId,taskDto,cDtoList);
        List<OrgUserDto> orgUserDtoUsers = fillupChklistService.getOrgUserDtosByTaskDatos(taskDtos);
        // get ah draft
        adchklDto = fillupChklistService.getAdCheckListShowDtoByAdCheckListShowDto(adchklDto,orgUserDtoUsers);
        //get  commonDto draft
        fillupChklistService.getInspectionFillCheckListDtoByInspectionFillCheckListDto(commonDto,orgUserDtoUsers);
        // change common data;
        insepctionNcCheckListService.getInspectionFillCheckListDtoForShow(commonDto);
        List<InspectionFillCheckListDto> inspectionFillCheckListDtos = new ArrayList<>(2);
        if(commonDto != null){
            commonDto.setCommonConfig(true);
            inspectionFillCheckListDtos.add(commonDto);
        }
        //  change service checklist data
        if(serListDto != null){
            List<InspectionFillCheckListDto> fdtoList = serListDto.getFdtoList();
            if(fdtoList != null && fdtoList.size() >0){
                inspectionFillCheckListDtos.addAll(fdtoList);
                for(InspectionFillCheckListDto inspectionFillCheckListDto : fdtoList){
                    //get Service draft
                    fillupChklistService.getInspectionFillCheckListDtoByInspectionFillCheckListDto(inspectionFillCheckListDto,orgUserDtoUsers);
                    insepctionNcCheckListService.getInspectionFillCheckListDtoForShow(inspectionFillCheckListDto);
                }
            }
            serListDto.setOtherinspectionofficer(fillupChklistService.getOtherOffGropByInspectionFillCheckListDtos(inspectionFillCheckListDtos));
        }
        //comparative data for sef and check list nc
        fillupChklistService.changeDataForNc(inspectionFillCheckListDtos,appPremCorrId);
        boolean remarksIsNull = (serListDto != null && StringUtil.isEmpty(serListDto.getTcuRemark()));
        //get RemarksAndStartTimeAndEndTimeForCheckList BY mobile api
        fillupChklistService.setRemarksAndStartTimeAndEndTimeForCheckList(serListDto,commonDto,appPremCorrId);
        if(remarksIsNull){
            if(!beforeFinishList){
                String remarks = serListDto.getTcuRemark();
                if(!StringUtil.isEmpty(remarks)){
                    ParamUtil.setSessionAttr(request, MOBILE_REMARK_GROUP,remarks);
                }
            }else {
                AppPremisesRecommendationDto appPremisesRecommendationDto =  insepctionNcCheckListService.getAppRecomDtoByAppCorrId(appPremCorrId,InspectionConstants.RECOM_TYPE_INSP_FINISH_CHECKLIST_BEFORE);
                if(appPremisesRecommendationDto != null){
                    ParamUtil.setSessionAttr(request, MOBILE_REMARK_GROUP,appPremisesRecommendationDto.getRemarks());
                    serListDto.setTcuRemark(appPremisesRecommendationDto.getRemarks());
                }
            }
        }
        setSpecServiceCheckListData(request,fillupChklistService.getOriginalInspectionSpecServiceDtoByTaskId(needVehicleSeparation,beforeFinishList,taskId),adchklDto,beforeFinishList,orgUserDtoUsers,appViewDto);
        ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
        ParamUtil.setSessionAttr(request,TASKDTO,taskDto);
        ParamUtil.setSessionAttr(request,COMMONDTO,commonDto);
        ParamUtil.setSessionAttr(request,APPLICATIONVIEWDTO,appViewDto);
        ParamUtil.setSessionAttr(request,ADHOCLDTO,adchklDto);
        ParamUtil.setSessionAttr(request,INSPECTION_USERS,(Serializable) orgUserDtoUsers);
        ParamUtil.setSessionAttr(request,INSPECTION_USER_FINISH, IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        if(beforeFinishList){
            ParamUtil.setSessionAttr(request,BEFORE_FINISH_CHECK_LIST,AppConsts.YES);
        }
        //set num
        setRate(request);
        //get selections dd hh
        setSelectionsForDDMMAndAuditRiskSelect(request);
    }

    public void pre(BaseProcessClass bpc){
        log.info("=======>>>>>initStep>>>>>>>>>>>>>>>>initRequest");
        HttpServletRequest request = bpc.request;
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(request,TASKDTO);
        AdCheckListShowDto adchklDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(request,ADHOCLDTO);
        InspectionFDtosDto serListDto  = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SERLISTDTO);
        InspectionFillCheckListDto commonDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,COMMONDTO);
        //fillupChklistService.getRateOfCheckList(serListDto,adchklDto,commonDto);
        ParamUtil.setSessionAttr(request,TASKDTO,taskDto);
        ParamUtil.setSessionAttr(request,ADHOCLDTO,adchklDto);
        ParamUtil.setSessionAttr(request,COMMONDTO,commonDto);
        ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
    }

    public void doNext(BaseProcessClass bpc) throws IOException{

        log.info("=======>>>>>doNextStep>>>>>>>>>>>>>>>>doNextRequest");
        HttpServletRequest request = bpc.request;
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String crudActionType = setActionRequestFroMulReq(request,mulReq);
        InspectionFDtosDto serListDto;
        String viewChkFlag = ParamUtil.getString(mulReq,"viewchk");
        if(!StringUtil.isEmpty(viewChkFlag)){
            if(viewChkFlag.equalsIgnoreCase("uploadFileLetter")){
                serListDto = getOtherInfo(mulReq);
                serListDto.setCheckListTab("chkList");
                ParamUtil.setSessionAttr(mulReq,SERLISTDTO,serListDto);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            }else {
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            }
        }else if("listAhoc".equalsIgnoreCase(crudActionType)){
            serListDto = getOtherInfo(mulReq);
            serListDto.setCheckListTab("chkList");
            ParamUtil.setSessionAttr(mulReq,SERLISTDTO,serListDto);
            ParamUtil.setSessionAttr(mulReq,ACTION_ADHOC_OWN,IaisEGPConstant.YES);
        } else{
            serListDto = getOtherInfo(mulReq);
            ParamUtil.setSessionAttr(mulReq,SERLISTDTO,serListDto);
            TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(mulReq,TASKDTO);
            InspectionFillCheckListDto comDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(mulReq,COMMONDTO);
            AdCheckListShowDto showDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(mulReq,ADHOCLDTO);
            InspectionCheckListValidation inspectionCheckListValidation = new InspectionCheckListValidation();
            Map<String, String> errMap = inspectionCheckListValidation.validate(request);
            if(!errMap.isEmpty()){
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                serListDto.setCheckListTab("chkList");
                ParamUtil.setSessionAttr(mulReq,SERLISTDTO,serListDto);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            }else{
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
                String taskId = taskDto.getId();
                TaskDto searchTask = taskService.getTaskById(taskId);
                if(TaskConsts.TASK_STATUS_COMPLETED.equals(searchTask.getTaskStatus()) || TaskConsts.TASK_STATUS_REMOVE.equals(searchTask.getTaskStatus()) || searchTask.getUpdateCount() ==1){
                    ParamUtil.setRequestAttr(request,"errerMessageForNoTaskForUpdate","LOLEV_ACK039");
                    return;
                }else {
                    ParamUtil.setRequestAttr(request,"errerMessageForNoTaskForUpdate","LOLEV_ACK034");
                }
                boolean flag = insepctionNcCheckListService.isHaveNcOrBestPractice(serListDto,comDto,showDto);
                String beforeFinishYes = (String) ParamUtil.getSessionAttr(request,BEFORE_FINISH_CHECK_LIST);
                serListDto.setSpecServiceVehicle(AppConsts.YES.equalsIgnoreCase((String) ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DECIDE)));
                if(!AppConsts.YES.equalsIgnoreCase(beforeFinishYes)){
                    saveCheckList(request,comDto,showDto,serListDto,taskDto.getRefNo());
                }else {
                    insepctionNcCheckListService.saveOtherDataForPengingIns(serListDto,taskDto.getRefNo());
                }
                ApplicationViewDto appViewDto =(ApplicationViewDto) ParamUtil.getSessionAttr(request,APPLICATIONVIEWDTO);
                insepctionNcCheckListService.saveLicPremisesAuditDtoByApplicationViewDto(appViewDto);
                AdCheckListShowDto showPageDto = new AdCheckListShowDto();
                serListDto.setCheckListTab(null);
                ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
                try {
                    showPageDto = (AdCheckListShowDto) CopyUtil.copyMutableObject(showDto);
                }catch (Exception e){
                    log.error(e.getMessage(), e);
                }
                ParamUtil.setSessionAttr(request,ADHOCLDTO,showPageDto);
                LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
                // SAVE OTHER TASKS
                fillupChklistService.saveOtherTasks((List<TaskDto>)ParamUtil.getSessionAttr(request,TASKDTOLIST),taskDto);
                fillupChklistService.routingTask(taskDto,serListDto.getRemarksForHistory(),loginContext,flag);
            }
       }

    }

    public String setActionRequestFroMulReq(HttpServletRequest request,MultipartHttpServletRequest mulReq){
        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        String crudActionValue = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);

        ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);
        ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_VALUE, crudActionValue);
        return crudActionType;
    }
    public CheckListVadlidateDto getValueFromPage(HttpServletRequest request) throws IOException {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest)request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        CheckListVadlidateDto dto = new CheckListVadlidateDto();
        getDataFromPage(request); // GET  check list base data
        getOtherInfo(mulReq);      // Set tcu and hh mm
        getCommonDataFromPage(request); // Set common data
        getAdhocDtoFromPage(request);  // Set adh data
        return dto;
    }

    public void doSubmit(BaseProcessClass bpc){
        log.info("=======>>>>>doSubmitStep>>>>>>>>>>>>>>>>doSubmitRequest");
        HttpServletRequest request = bpc.request;
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,TASKDTO);
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SERLISTDTO);
        InspectionFillCheckListDto comDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,COMMONDTO);
        AdCheckListShowDto showDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(request,ADHOCLDTO);
        boolean flag = insepctionNcCheckListService.isHaveNcOrBestPractice(serListDto,comDto,showDto);
        insepctionNcCheckListService.submit(comDto,showDto,serListDto,taskDto.getRefNo());
        AdCheckListShowDto showPageDto = new AdCheckListShowDto();
        try {
            showPageDto = (AdCheckListShowDto) CopyUtil.copyMutableObject(showDto);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        ParamUtil.setSessionAttr(request,ADHOCLDTO,showPageDto);
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        fillupChklistService.routingTask(taskDto,ParamUtil.getString(request,"RemarksForHistory"),loginContext,flag);
    }


    public void doCheckList(BaseProcessClass bpc){
        log.info("=======>>>>>doCheckList>>>>>>>>>>>>>>>>doCheckList");
        HttpServletRequest request = bpc.request;
        String beforeFinishYes = (String) ParamUtil.getSessionAttr(request,BEFORE_FINISH_CHECK_LIST);
        InspectionFDtosDto serListDto = (InspectionFDtosDto) ParamUtil.getSessionAttr(request,SERLISTDTO);
        String doSubmitAction = ParamUtil.getString(request,"doSubmitAction");
        if(!AppConsts.YES.equalsIgnoreCase(beforeFinishYes)){
        saveCheckListAllSession(request);
        InspectionCheckListItemValidate inspectionCheckListItemValidate = new InspectionCheckListItemValidate();
        if(InspectionCheckListItemValidate.NEXT_ACTION.equalsIgnoreCase(doSubmitAction)) {
            Map errMap = inspectionCheckListItemValidate.validate(request);
            if (!errMap.isEmpty()) {
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                serListDto.setCheckListTab("chkList");
                ParamUtil.setSessionAttr(request, SERLISTDTO, serListDto);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            } else {
                serListDto.setCheckListTab("chkList");
                saveCheckListBefore(request);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            }
            setChangeTabForChecklist(request);
         }
        }
        if(!InspectionCheckListItemValidate.NEXT_ACTION.equalsIgnoreCase(doSubmitAction)){
            serListDto.setCheckListTab("chkList");
            if(!AppConsts.YES.equalsIgnoreCase(beforeFinishYes)){
                setRate(request);
            }
            ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }
    }

    private void saveCheckListBefore(HttpServletRequest request){
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(request,TASKDTO);
        String taskId = taskDto.getId();
        TaskDto searchTask = taskService.getTaskById(taskId);
        if(TaskConsts.TASK_STATUS_COMPLETED.equals(searchTask.getTaskStatus()) || TaskConsts.TASK_STATUS_REMOVE.equals(searchTask.getTaskStatus()) || searchTask.getUpdateCount() ==1){
            log.info(StringUtil.changeForLog("----saveCheckListBefore taskId : " + taskId + " ,the associated data has been submitted -------"));
            return;
        }
        InspectionFDtosDto serListDto = (InspectionFDtosDto) ParamUtil.getSessionAttr(request,SERLISTDTO);
        InspectionFillCheckListDto commonDto =  (InspectionFillCheckListDto) ParamUtil.getSessionAttr(request,COMMONDTO);
        AdCheckListShowDto showDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(request,ADHOCLDTO);
        log.info(" ----------- saveCheckListBefore start ------------");
        if(AppConsts.YES.equalsIgnoreCase((String) ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DECIDE))){
            List<InspectionSpecServiceDto> fDtosDtos =( List<InspectionSpecServiceDto>) ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DTOS);
            insepctionNcCheckListService.saveBeforeSubmitSpecCheckList(commonDto,fDtosDtos,serListDto,showDto,taskDto.getRefNo());
            ParamUtil.setSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DTOS,(Serializable) fDtosDtos);
        }else {
            insepctionNcCheckListService.saveBeforeSubmitCheckList(commonDto,showDto,serListDto,taskDto.getRefNo());
        }
        insepctionNcCheckListService.saveBeforeFinishCheckListRec(taskDto.getRefNo(),(String)ParamUtil.getSessionAttr(request, MOBILE_REMARK_GROUP));
        ParamUtil.setSessionAttr(request,BEFORE_FINISH_CHECK_LIST,AppConsts.YES);
        log.info(" ----------- saveCheckListBefore end ------------");

    }


    public void preViewCheckList(BaseProcessClass bpc)throws IOException{
        log.info("=======>>>>>preViewCheckList>>>>>>>>>>>>>>>>preViewCheckList");
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        setActionRequestFroMulReq(bpc.request,mulReq);
        getOtherInfo(mulReq);

    }

    public void listAhocs(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String actionAdhoc =(String) ParamUtil.getSessionAttr(request,ACTION_ADHOC_OWN);
        AdhocCheckListConifgDto adhocCheckListConifgDto = (AdhocCheckListConifgDto) ParamUtil.getSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR);
        if( IaisEGPConstant.YES.equalsIgnoreCase(actionAdhoc) && adhocCheckListConifgDto == null){
            TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(request,TASKDTO);
            if(taskDto != null ){
                ParamUtil.setSessionAttr(request, INSPECTION_ADHOC_CHECKLIST_LIST_ATTR,fillupChklistService.getAdhocCheckListConifgDtoByCorId(taskDto.getRefNo()));
                ParamUtil.setSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR,fillupChklistService.getAdhocCheckListConifgDtoByCorId(taskDto.getRefNo()));
            }else {
                log.info("-----------taskdto is null-------");
            }
            ParamUtil.setSessionAttr(request,ACTION_ADHOC_OWN,IaisEGPConstant.NO);
        }
    }

    public void addAhocs(BaseProcessClass bpc){
        AdhocCheckListConifgDto adhocCheckListConifgDto = (AdhocCheckListConifgDto) ParamUtil.getSessionAttr(bpc.request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR);
       if(adhocCheckListConifgDto == null){
           log.info("adhocCheckListConifgDto is null");
       }
    }

    public void saveAhocs(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(request,TASKDTO);
        TaskDto taskDtoSearch = taskService.getTaskById(taskDto.getId());
        if(setCsrf(bpc,taskDtoSearch)){
            return;
        }
        AdhocCheckListConifgDto adhocCheckListConifgDto = (AdhocCheckListConifgDto) ParamUtil.getSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR);
        AdhocCheckListConifgDto adhocCheckListConifgDtoOld = (AdhocCheckListConifgDto)ParamUtil.getSessionAttr(request, INSPECTION_ADHOC_CHECKLIST_LIST_ATTR);
        boolean editAhoc = fillupChklistService.editAhocByAdhocCheckListConifgDtoAndOldAdhocCheckListConifgDto(adhocCheckListConifgDto,adhocCheckListConifgDtoOld);
        if(editAhoc){
            fillupChklistService.saveAdhocDto(adhocCheckListConifgDto);
            if(adhocCheckListConifgDto == null && adhocCheckListConifgDtoOld != null){
                adhocCheckListConifgDtoOld.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                adhocChecklistService.saveAdhocChecklist(adhocCheckListConifgDtoOld);
            }
            List<TaskDto> taskDtos = (List<TaskDto>) ParamUtil.getSessionAttr(request,TASKDTOLIST);
            String appPremCorrId = taskDto.getRefNo();
            AdCheckListShowDto adchklDto = fillupChklistService.getAdhocDraftByappCorrId(appPremCorrId);
            if(adchklDto==null){
                adchklDto = fillupChklistService.getAdhoc(appPremCorrId);
            }
            // get ah draft
            adchklDto = fillupChklistService.getAdCheckListShowDtoByAdCheckListShowDto(adchklDto,fillupChklistService.getOrgUserDtosByTaskDatos(taskDtos));
            ParamUtil.setSessionAttr(request,ADHOCLDTO,adchklDto);
            setSpecAhoc(request,adchklDto);
            //get nc
            setRate(request);
            ApplicationViewDto appViewDto = (ApplicationViewDto) ParamUtil.getSessionAttr(request,APPLICATIONVIEWDTO);
            if( appViewDto != null){
                fillupChklistService.sendModifiedChecklistEmailToAOStage(appViewDto);
            }else {
                log.info("--------- appViewDto is null ,no can send email ao--------");
            }
        }
        if(adhocCheckListConifgDto == null){
            log.info("-----adhocCheckListConifgDto is null-----");
        }
        ParamUtil.setSessionAttr(request, INSPECTION_ADHOC_CHECKLIST_LIST_ATTR,null);
        ParamUtil.setSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, null);
    }

    public void setSpecAhoc(HttpServletRequest request, AdCheckListShowDto adchklDto){
        if(AppConsts.YES.equalsIgnoreCase((String) ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DECIDE))){
            List<InspectionSpecServiceDto> fDtosDtos =( List<InspectionSpecServiceDto>) ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DTOS);
            if(IaisCommonUtils.isNotEmpty(fDtosDtos)){
                for(InspectionSpecServiceDto inspectionSpecServiceDto : fDtosDtos){
                    try{
                        inspectionSpecServiceDto.setAdchklDto((AdCheckListShowDto)com.ecquaria.cloud.moh.iais.common.utils.CopyUtil.copyMutableObject(adchklDto));
                    }catch (Exception e){
                        log.error(e.getMessage());
                    }
                }
            }
        }
    }
    public void changeTab(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        log.info("----------changeTab --");
        String nowTabIn = ParamUtil.getString(request,"nowTabIn");
        ParamUtil.setRequestAttr(request, "nowTabIn",  nowTabIn);
        String nowComTabIn = ParamUtil.getString(request,HcsaLicenceBeConstant.CHECK_LIST_COM_TAB_NAME);
        ParamUtil.setRequestAttr(request, HcsaLicenceBeConstant.CHECK_LIST_COM_TAB_NAME,  nowComTabIn);
        changeDataForCheckList(request);
    }

    public void changeDataForCheckList(HttpServletRequest request){
        getCommonCheckListMoreData(request);
        getServiceCheckListMoreData(request);
        getAhocCheckListMoreData(request);
        if(AppConsts.YES.equalsIgnoreCase((String) ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DECIDE))){
            getSpecServiceCheckListMoreData(request);
        }
        setRate(request);
    }


    public void getCommonCheckListMoreData(HttpServletRequest request){
        InspectionFillCheckListDto comDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,COMMONDTO);
         String userId = (String)ParamUtil.getSessionAttr(request, INSPECTION_USER_FINISH);
        if(comDto != null && comDto.getCheckList()!=null&& !comDto.getCheckList().isEmpty()){
            List<InspectionCheckQuestionDto> checkList = comDto.getCheckList();
            for(InspectionCheckQuestionDto inspectionCheckQuestionDto : checkList){
                String prefix = inspectionCheckQuestionDto.getSectionNameShow()+inspectionCheckQuestionDto.getItemId();

                List<AnswerForDifDto> answerForDifDtos = inspectionCheckQuestionDto.getAnswerForDifDtos();
                int index = 0;
                for(AnswerForDifDto answerForDifDto : answerForDifDtos){
                    if(userId.equalsIgnoreCase(answerForDifDto.getSubmitId())){
                        String answer =  ParamUtil.getString(request,prefix +"comradIns" + index);
                        String remark = ParamUtil.getString(request,prefix +"comremarkIns" + index);
                        String raf =  ParamUtil.getString(request,prefix +"comrecIns" + index);
                        String ncs  = ParamUtil.getString(request,prefix +"comFindNcsIns" + index);
                        answerForDifDto.setNcs(ncs);
                        answerForDifDto.setAnswer(answer);
                        answerForDifDto.setRemark(remark);
                        if("No".equalsIgnoreCase(answer) && "rec".equalsIgnoreCase(raf)){
                            answerForDifDto.setIsRec("1");
                        }else {
                            answerForDifDto.setIsRec("0");
                        }
                        break;
                    }
                 index++;
                }
                fillupChklistService.setInspectionCheckQuestionDtoByAnswerForDifDtosAndDeconflict(inspectionCheckQuestionDto,answerForDifDtos,ParamUtil.getString(request,prefix +"Deconflict"));
            }
            ParamUtil.setSessionAttr(request,COMMONDTO,comDto);
        }
    }


    public void getServiceCheckListMoreData(HttpServletRequest request){
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SERLISTDTO);
        String userId = (String)ParamUtil.getSessionAttr(request, INSPECTION_USER_FINISH);
        List<InspectionFillCheckListDto> fdtoList = serListDto.getFdtoList();
        if(fdtoList != null && fdtoList.size() >0){
            getOneServiceMoreData( request,fdtoList,userId);
            ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
        }
    }

    public void getAhocCheckListMoreData(HttpServletRequest request){
        AdCheckListShowDto adchklDto  = (AdCheckListShowDto) ParamUtil.getSessionAttr(request,ADHOCLDTO);
        String userId = (String)ParamUtil.getSessionAttr(request, INSPECTION_USER_FINISH);
        List<AdhocNcCheckItemDto> adItemList = adchklDto.getAdItemList();
        if( !IaisCommonUtils.isEmpty( adItemList)){
            getAhocCheckListMoreData(request,adchklDto,"",userId);
            ParamUtil.setSessionAttr(request,ADHOCLDTO,adchklDto);
        }
    }

    public void saveDraftChecklist(BaseProcessClass bpc){
        log.info("----------saveDraftChecklist start--------");
        HttpServletRequest request = bpc.request;
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(request,TASKDTO);
        TaskDto taskDtoSearch = taskService.getTaskById(taskDto.getId());
        if(setCsrf(bpc,taskDtoSearch)){
            return;
        }
        saveCheckListAllSession(request);
       /*  InspectionCheckListItemValidate inspectionCheckListItemValidate = new InspectionCheckListItemValidate();
        Map<String, String> errMap = inspectionCheckListItemValidate.validate(request);
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
        }else {
            InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SERLISTDTO);
            InspectionFillCheckListDto commonDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,COMMONDTO);
            AdCheckListShowDto adchklDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(request,ADHOCLDTO);
            insepctionNcCheckListService.saveDraftChecklist(commonDto,adchklDto,serListDto,taskDto.getRefNo());
        }*/
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SERLISTDTO);
        InspectionFillCheckListDto commonDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,COMMONDTO);
        AdCheckListShowDto adchklDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(request,ADHOCLDTO);
        if(AppConsts.YES.equalsIgnoreCase((String) ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DECIDE))){
            List<InspectionSpecServiceDto> fDtosDtos =( List<InspectionSpecServiceDto>) ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DTOS);
            insepctionNcCheckListService.saveDraftSpecChecklist(commonDto,taskDto.getRefNo(),fDtosDtos,adchklDto,serListDto);
            ParamUtil.setSessionAttr(request,HcsaLicenceBeConstant.SPECIAL_SERVICE_FOR_CHECKLIST_DTOS,(Serializable) fDtosDtos);
        }else {
            insepctionNcCheckListService.saveDraftChecklist(commonDto,adchklDto,serListDto,taskDto.getRefNo());
        }
        setRate(request);
        String nowTabIn = ParamUtil.getString(request,"nowTabIn");
        ParamUtil.setRequestAttr(request, "nowTabIn",  nowTabIn);
        String nowComTabIn = ParamUtil.getString(request,HcsaLicenceBeConstant.CHECK_LIST_COM_TAB_NAME);
        ParamUtil.setRequestAttr(request, HcsaLicenceBeConstant.CHECK_LIST_COM_TAB_NAME,  nowComTabIn);
       /* String errTab = (String) ParamUtil.getSessionAttr(request,HcsaLicenceBeConstant.CHECK_LIST_ERROR_TAB_NAME);
        if( !StringUtil.isEmpty(errTab)){
            ParamUtil.setSessionAttr(request,HcsaLicenceBeConstant.CHECK_LIST_ERROR_TAB_NAME,null);
            ParamUtil.setRequestAttr(request, HcsaLicenceBeConstant.CHECK_LIST_COM_TAB_NAME,  errTab);
        }*/
        log.info("----------saveDraftChecklist end--------");
    }

    public void saveCheckListAllSession(HttpServletRequest request){
        log.info("---------- saveCheckListAllSession start --------------");
        List<OrgUserDto> orgUserDtoUsers = (List<OrgUserDto>) ParamUtil.getSessionAttr(request,INSPECTION_USERS);
        if( !IaisCommonUtils.isEmpty(orgUserDtoUsers) && orgUserDtoUsers.size() >1){
            changeDataForCheckList(request);
        }else {
            setCheckListData(request);
        }
        log.info("---------- saveCheckListAllSession end --------------");
    }
}
