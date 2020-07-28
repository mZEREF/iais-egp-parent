package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.AdhocChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.*;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.CheckListVadlidateDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.util.LicenceUtil;
import com.ecquaria.cloud.moh.iais.validation.InspectionCheckListItemValidate;
import com.ecquaria.cloud.moh.iais.validation.InspectionCheckListValidation;
import com.ecquaria.sz.commons.util.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
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
public class InspectionNcCheckListDelegator {
    @Autowired
    FillupChklistService fillupChklistService;
    @Autowired
    InsepctionNcCheckListService insepctionNcCheckListService;
    @Autowired
    TaskService taskService;
    @Autowired
    AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;
    @Autowired
    InspectionAssignTaskService inspectionAssignTaskService;
    private static final String SERLISTDTO="serListDto";
    private static final String COMMONDTO="commonDto";
    private static final String ADHOCLDTO="adchklDto";
    private static final String TASKDTO="taskDto";
    private static final String APPLICATIONVIEWDTO = "applicationViewDto";
    private static final String CHECKLISTFILEDTO = "checkListFileDto";
    private static final String TASKDTOLIST = "InspectionNcCheckListDelegator_taskDtoList";
    private static final String INSPECTION_ADHOC_CHECKLIST_LIST_ATTR  = "inspection_adhoc_checklist_list_attr";
    private static final String INSPECTION_USERS = "inspectorsParticipant";
    private static final String INSPECTION_USER_FINISH = "inspectorUserFinishChecklistId";
    public InspectionNcCheckListDelegator(InsepctionNcCheckListService insepctionNcCheckListService){
        this.insepctionNcCheckListService = insepctionNcCheckListService;
    }

    public void start(BaseProcessClass bpc) {
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, ADHOCLDTO, null);
        ParamUtil.setSessionAttr(request, COMMONDTO, null);
        ParamUtil.setSessionAttr(request, TASKDTO, null);
        ParamUtil.setSessionAttr(request, APPLICATIONVIEWDTO, null);
        ParamUtil.setSessionAttr(request, CHECKLISTFILEDTO, null);
        ParamUtil.setSessionAttr(request, TASKDTOLIST, null);
        ParamUtil.setSessionAttr(request, INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, null);
        ParamUtil.setSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, null);
        ParamUtil.setSessionAttr(request,INSPECTION_USERS,null);
        ParamUtil.setSessionAttr(request,INSPECTION_USER_FINISH,null);
    }

    public void successViewPre(BaseProcessClass bpc){
        log.info("=======>>>>>successViewPre>>>>>>>>>>>>>>>>successViewPre");
        HttpServletRequest request = bpc.request;
        InspectionFDtosDto serListDto  = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SERLISTDTO);
        serListDto.setCheckListTab("chkList");
        ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
    }

    public void successViewBack(BaseProcessClass bpc){
        log.info("=======>>>>>successViewBack>>>>>>>>>>>>>>>>successViewBack");
        HttpServletRequest request = bpc.request;

    }

    public void init(BaseProcessClass bpc){
        log.info("=======>>>>>initStep>>>>>>>>>>>>>>>>initRequest");
        HttpServletRequest request = bpc.request;
        String taskId = ParamUtil.getMaskedString(request,"taskId");
        AuditTrailHelper.auditFunction("Checklist Management", "Checklist Config");
        TaskDto taskDto = taskService.getTaskById(taskId);
        if( taskDto == null) return;
        String appPremCorrId = taskDto.getRefNo();
        List<TaskDto> taskDtos = fillupChklistService.getCurrTaskByRefNo(taskDto);
        ParamUtil.setSessionAttr(request,TASKDTOLIST ,(Serializable) taskDtos);
        ApplicationViewDto appViewDto = fillupChklistService.getAppViewDto(taskId);
        //draft start
        InspectionFillCheckListDto maxComChkDto = fillupChklistService.getMaxVersionComAppChklDraft(appPremCorrId);
        List<InspectionFillCheckListDto> allComChkDtoList = fillupChklistService.getAllVersionComAppChklDraft(appPremCorrId);
        List<InspectionFDtosDto> fdtosdraft= fillupChklistService.geAllVersionServiceDraftList(appPremCorrId);
        InspectionFDtosDto maxVersionfdtos = fillupChklistService.getMaxVersionServiceDraft(fdtosdraft);
        List<InspectionFDtosDto> otherVersionfdtos = fillupChklistService.getOtherVersionfdtos(fdtosdraft);
        List<AdCheckListShowDto> otherVersionAdhocDraftList = fillupChklistService.getOtherAdhocList(appPremCorrId);
        ParamUtil.setSessionAttr(request,"otherVersionAdhocDraftList",(Serializable) otherVersionAdhocDraftList);
        ParamUtil.setSessionAttr(request,"otherVersionfdtos",(Serializable) otherVersionfdtos);
        ParamUtil.setSessionAttr(request,"allComChkDtoList",(Serializable)allComChkDtoList);
        ParamUtil.setSessionAttr(request,"maxComChkDto", maxComChkDto);
        //draft end
        InspectionFillCheckListDto commonDto = null;
        List<InspectionFillCheckListDto>   cDtoList = fillupChklistService.getInspectionFillCheckListDtoList(taskId,"service");
        List<InspectionFillCheckListDto>   commonList = fillupChklistService.getInspectionFillCheckListDtoList(taskId,"common");
            if(commonList!=null&&!commonList.isEmpty()){
                commonDto = commonList.get(0);
            }
        InspectionFDtosDto serListDto =  fillupChklistService.getInspectionFDtosDto(appPremCorrId,taskDto,cDtoList);
        AdCheckListShowDto adchklDto = fillupChklistService.getAdhocDraftByappCorrId(appPremCorrId);
        if(adchklDto==null){
            adchklDto = fillupChklistService.getAdhoc(appPremCorrId);
        }
        List<OrgUserDto> orgUserDtoUsers = fillupChklistService.getOrgUserDtosByTaskDatos(taskDtos);
        ParamUtil.setSessionAttr(request,INSPECTION_USERS,(Serializable) orgUserDtoUsers);
        ParamUtil.setSessionAttr(request,INSPECTION_USER_FINISH, IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        Map<String,String> orgUserDtos = fillupChklistService.userIdNameMapByOrgUserDtos(orgUserDtoUsers);

        // get ah draft
        adchklDto = fillupChklistService.getAdCheckListShowDtoByAdCheckListShowDto(adchklDto,orgUserDtos);
        ParamUtil.setSessionAttr(request,TASKDTO,taskDto);
        ParamUtil.setSessionAttr(request,APPLICATIONVIEWDTO,appViewDto);
        ParamUtil.setSessionAttr(request,ADHOCLDTO,adchklDto);
        if(maxComChkDto!=null){
            List<InspectionFillCheckListDto> coms = fillupChklistService.getInspectionFillCheckListDtoListForReview(taskId,"common");
            if(!IaisCommonUtils.isEmpty(coms)){
                commonDto = coms.get(0);
            }
        }
        //get  commonDto draft
        fillupChklistService.getInspectionFillCheckListDtoByInspectionFillCheckListDto(commonDto,orgUserDtos);
        // change common data;
        insepctionNcCheckListService.getInspectionFillCheckListDtoForShow(commonDto);
        ParamUtil.setSessionAttr(request,COMMONDTO,commonDto);
        if(maxVersionfdtos != null && !IaisCommonUtils.isEmpty(maxVersionfdtos.getFdtoList())){
            List<InspectionFillCheckListDto> ftos = fillupChklistService.getInspectionFillCheckListDtoListForReview(taskId,"service");
            serListDto.setFdtoList(ftos);
        }

        List<InspectionFillCheckListDto> inspectionFillCheckListDtos = new ArrayList<>(2);
        if(commonDto != null){
            inspectionFillCheckListDtos.add(commonDto);
        }
        //  change service checklist data
       if(serListDto != null){
           List<InspectionFillCheckListDto> fdtoList = serListDto.getFdtoList();
            if(fdtoList != null && fdtoList.size() >0){
                inspectionFillCheckListDtos.addAll(fdtoList);
              for(InspectionFillCheckListDto inspectionFillCheckListDto : fdtoList){
                  //get Service draft
                  fillupChklistService.getInspectionFillCheckListDtoByInspectionFillCheckListDto(inspectionFillCheckListDto,orgUserDtos);
                  insepctionNcCheckListService.getInspectionFillCheckListDtoForShow(inspectionFillCheckListDto);
              }
            }
           serListDto.setOtherinspectionofficer(fillupChklistService.getOtherOffGropByInspectionFillCheckListDtos(inspectionFillCheckListDtos));
       }
        fillupChklistService.getRateOfCheckList(serListDto,adchklDto,commonDto);
        //comparative data for sef and check list nc
        fillupChklistService.changeDataForNc(inspectionFillCheckListDtos,appPremCorrId);
        ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
        //get selections dd hh
        ParamUtil.setSessionAttr(request,"hhSelections",(Serializable) IaisCommonUtils.getHHOrDDSelectOptions(true));
        ParamUtil.setSessionAttr(request,"ddSelections",(Serializable) IaisCommonUtils.getHHOrDDSelectOptions(false));
        ParamUtil.setSessionAttr(request,"frameworknOption",(Serializable) LicenceUtil.getIncludeRiskTypes());
    }

    public void pre(BaseProcessClass bpc){
        log.info("=======>>>>>initStep>>>>>>>>>>>>>>>>initRequest");
        HttpServletRequest request = bpc.request;
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(request,"taskDto");
        AdCheckListShowDto adchklDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(request,"adchklDto");
        InspectionFDtosDto serListDto  = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SERLISTDTO);
        InspectionFillCheckListDto commonDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,"commonDto");
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
        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        String crudActionValue = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE, crudActionValue);
        InspectionFDtosDto serListDto;
        String viewChkFlag = ParamUtil.getString(mulReq,"viewchk");
  /*      CommonsMultipartFile file = (CommonsMultipartFile) mulReq.getFile("selectedFile");
        CheckListFileDto fileDto = new CheckListFileDto();
        fileDto.setFileName(file.getOriginalFilename());;
        ParamUtil.setSessionAttr(mulReq,CHECKLISTFILEDTO,fileDto);*/

        if(!StringUtil.isEmpty(viewChkFlag)){
            /*serListDto = getDataFromPage(request);
            InspectionFillCheckListDto commonDto= getCommonDataFromPage(request);
            AdCheckListShowDto adchklDto = getAdhocDtoFromPage(request);
            serListDto.setCheckListTab("chkList");
            ParamUtil.setSessionAttr(request,ADHOCLDTO,adchklDto);
            ParamUtil.setSessionAttr(request,COMMONDTO,commonDto);*/
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }else if("listAhoc".equalsIgnoreCase(crudActionValue)){
            serListDto = getOtherInfo(mulReq);
            serListDto.setCheckListTab("chkList");
            ParamUtil.setSessionAttr(mulReq,SERLISTDTO,serListDto);
        } else{
            serListDto = getOtherInfo(mulReq);
            ParamUtil.setSessionAttr(mulReq,SERLISTDTO,serListDto);
            TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(mulReq,"taskDto");
            InspectionFillCheckListDto comDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(mulReq,"commonDto");
            AdCheckListShowDto showDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(mulReq,"adchklDto");
            InspectionCheckListValidation inspectionCheckListValidation = new InspectionCheckListValidation();
            Map<String, String> errMap = inspectionCheckListValidation.validate(request);
            if(!errMap.isEmpty()){
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                serListDto.setCheckListTab("chkList");
                ParamUtil.setSessionAttr(mulReq,SERLISTDTO,serListDto);
                ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
            }else{
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
                boolean flag = insepctionNcCheckListService.isHaveNcOrBestPractice(serListDto,comDto,showDto);
                insepctionNcCheckListService.submit(comDto,showDto,serListDto,taskDto.getRefNo());
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
                ParamUtil.setSessionAttr(request,"adchklDto",showPageDto);
                LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
                // SAVE OTHER TASKS
                fillupChklistService.saveOtherTasks((List<TaskDto>)ParamUtil.getSessionAttr(request,TASKDTOLIST),taskDto);
                fillupChklistService.routingTask(taskDto,ParamUtil.getString(request,"RemarksForHistory"),loginContext,flag);
            }
       }

    }

    private InspectionFDtosDto getOtherInfo(MultipartHttpServletRequest request) throws IOException {
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SERLISTDTO);
        String tcuflag = ParamUtil.getString(request,"tcuType");
        String tcu = null;
        if(!StringUtil.isEmpty(tcuflag)){
            tcu = ParamUtil.getString(request,"tuc");
        }
        String bestpractice = ParamUtil.getString(request,"bestpractice");
        String tcuremark = ParamUtil.getString(request,"tcuRemark");
        String otherOfficers = ParamUtil.getString(request,"otherinspector");

        //startHour   startHourMin  endHour endHourMin
        String inspectionDate = ParamUtil.getString(request,"inspectionDate");
        String startHour = ParamUtil.getString(request,"startHour");
        String startMin = ParamUtil.getString(request,"startHourMin");
        String endHour = ParamUtil.getString(request,"endHour");
        String endMin = ParamUtil.getString(request,"endHourMin");
        String startTime = startHour+" : "+startMin;
        String endTime =  endHour+" : "+endMin;
        serListDto.setStartTime(startTime);
        serListDto.setEndTime(endTime);
        serListDto.setStartHour(startHour);
        serListDto.setEndHour(endHour);
        serListDto.setStartMin(startMin);
        serListDto.setEndMin(endMin);
        serListDto.setInspectionDate(inspectionDate);
        serListDto.setOtherinspectionofficer(otherOfficers);
        serListDto.setTcuRemark(tcuremark);
        if(!StringUtil.isEmpty(tcuflag)){
            serListDto.setTcuFlag(true);
            serListDto.setTuc(tcu);
        }else{
            serListDto.setTcuFlag(false);
            serListDto.setTuc(null);
        }
        serListDto.setBestPractice(bestpractice);

        // set litter file
        String litterFile =  ParamUtil.getString(request,"litterFile" );
        if(!StringUtil.isEmpty(litterFile)){
            String litterFileId =  ParamUtil.getString(request,"litterFileId" );
            CommonsMultipartFile file= (CommonsMultipartFile) request.getFile("selectedFileView");
            if(StringUtil.isEmpty(litterFileId) && file != null && file.getSize() != 0){
                if (!StringUtil.isEmpty(file.getOriginalFilename())) {
                    file.getFileItem().setFieldName("selectedFile");
                    TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(request, TASKDTO);
                    String correlationId = taskDto.getRefNo();
                    AppPremisesSpecialDocDto appIntranetDocDto = new AppPremisesSpecialDocDto();
                    appIntranetDocDto.setDocName(litterFile);
                    appIntranetDocDto.setAppPremCorreId(correlationId);
                    appIntranetDocDto.setMd5Code(FileUtil.genMd5FileChecksum(file.getBytes()));
                    long size = file.getSize()/1024;
                    if(size <= Integer.MAX_VALUE ){
                        appIntranetDocDto.setDocSize((int)size);
                    }else {
                        appIntranetDocDto.setDocSize(Integer.MAX_VALUE);
                    }
                     //delete file
                    insepctionNcCheckListService.deleteInvalidFile(serListDto);
                    //save file
                    if( size <= 10240)
                    appIntranetDocDto.setFileRepoId(insepctionNcCheckListService.saveFiles(file));
                    serListDto.setAppPremisesSpecialDocDto(appIntranetDocDto);
                }
            }
        }else {
            //delete file
            insepctionNcCheckListService.deleteInvalidFile(serListDto);
            serListDto.setAppPremisesSpecialDocDto(null);
           // serListDto.setFile(null);
        }

        ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
        getAuditData(request);
        return serListDto;
    }

   private void  getAuditData(MultipartHttpServletRequest request)throws IOException {
       ApplicationViewDto appViewDto =(ApplicationViewDto) ParamUtil.getSessionAttr(request,APPLICATIONVIEWDTO);
       if (appViewDto != null && appViewDto.getLicPremisesAuditDto() != null){
           LicPremisesAuditDto licPremisesAuditDto =  appViewDto.getLicPremisesAuditDto();
           String framework = ParamUtil.getString(request,"framework");
           String periods = ParamUtil.getString(request,"periods");
           String frameworkRemarks = ParamUtil.getString(request,"frameworkRemarks");
           if( !StringUtil.isEmpty(framework) && framework.equalsIgnoreCase("0")){
               licPremisesAuditDto.setInRiskSocre(0);
               if(!StringUtil.isEmpty(periods)){
                   licPremisesAuditDto.setIncludeRiskType(periods);
                   if(periods.equalsIgnoreCase(ApplicationConsts.INCLUDE_RISK_TYPE_LEADERSHIP_KEY))
                       licPremisesAuditDto.setLgrRemarks(frameworkRemarks );
                   else
                       licPremisesAuditDto.setLgrRemarks(null);
               }else {
                   licPremisesAuditDto.setIncludeRiskType(null);
                   licPremisesAuditDto.setLgrRemarks(null);
               }
           }else {
               licPremisesAuditDto.setInRiskSocre(1);
               licPremisesAuditDto.setIncludeRiskType(null);
               licPremisesAuditDto.setLgrRemarks(null);
           }
         ParamUtil.setSessionAttr(request,APPLICATIONVIEWDTO,appViewDto);
       }
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
        TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SERLISTDTO);
        InspectionFillCheckListDto comDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,"commonDto");
        AdCheckListShowDto showDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(request,"adchklDto");
        boolean flag = insepctionNcCheckListService.isHaveNcOrBestPractice(serListDto,comDto,showDto);
        insepctionNcCheckListService.submit(comDto,showDto,serListDto,taskDto.getRefNo());
        AdCheckListShowDto showPageDto = new AdCheckListShowDto();
        try {
            showPageDto = (AdCheckListShowDto) CopyUtil.copyMutableObject(showDto);
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        ParamUtil.setSessionAttr(request,"adchklDto",showPageDto);
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        fillupChklistService.routingTask(taskDto,ParamUtil.getString(request,"RemarksForHistory"),loginContext,flag);
    }

    public InspectionFillCheckListDto getCommonDataFromPage(HttpServletRequest request){
        InspectionFillCheckListDto cDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,"commonDto");
        if(cDto!=null&&cDto.getCheckList()!=null&&!cDto.getCheckList().isEmpty()){
            List<InspectionCheckQuestionDto> checkListDtoList = cDto.getCheckList();
            for(InspectionCheckQuestionDto temp:checkListDtoList){
                    String answer = ParamUtil.getString(request,temp.getSectionNameShow()+temp.getItemId()+"comrad");
                    temp.setChkanswer(answer);
                String remark = ParamUtil.getString(request,temp.getSectionNameShow()+temp.getItemId()+"comremark");
                String rectified = ParamUtil.getString(request,temp.getSectionNameShow()+temp.getItemId()+"comrec");
                if(!StringUtil.isEmpty(rectified)&&"No".equals(answer)){
                    temp.setRectified(true);
                }else{
                    temp.setRectified(false);
                }
                temp.setRemark(remark);
            }
            fillupChklistService.fillInspectionFillCheckListDto(cDto);
        }
        return cDto;
    }

    public InspectionFDtosDto getServiceCheckListDataFormViewPage(HttpServletRequest request){
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SERLISTDTO);
        if(!IaisCommonUtils.isEmpty(serListDto.getFdtoList())){
            for(InspectionFillCheckListDto fdto:serListDto.getFdtoList()){
                if(fdto!=null&&!IaisCommonUtils.isEmpty(fdto.getCheckList())){
                    List<InspectionCheckQuestionDto> checkListDtoList = fdto.getCheckList();
                    for(InspectionCheckQuestionDto temp:checkListDtoList){
                        getServiceData(temp,fdto,request);
                    }
                    fillupChklistService.fillInspectionFillCheckListDto(fdto);
                }
            }
        }
        return serListDto;
    }

    public InspectionFDtosDto getDataFromPage(HttpServletRequest request){
        InspectionFDtosDto serListDto = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SERLISTDTO);
        /*if(!IaisCommonUtils.isEmpty(serListDto.getFdtoList())){
            for(InspectionFillCheckListDto fdto:serListDto.getFdtoList()){
                if(fdto!=null&&!IaisCommonUtils.isEmpty(fdto.getCheckList())){
                    List<InspectionCheckQuestionDto> checkListDtoList = fdto.getCheckList();
                    for(InspectionCheckQuestionDto temp:checkListDtoList){
                        getServiceData(temp,fdto,request);
                    }
                    fillupChklistService.fillInspectionFillCheckListDto(fdto);
                }
            }
        }*/
        String tcu = ParamUtil.getString(request,"tuc");
        String bestpractice = ParamUtil.getString(request,"bestpractice");
        String tcuremark = ParamUtil.getString(request,"tcuRemark");
        String otherOfficers = ParamUtil.getString(request,"otherinspector");
        String startTime = ParamUtil.getString(request,"startTime");
        String endTime =  ParamUtil.getString(request,"endTime");
        serListDto.setStartTime(startTime);
        serListDto.setEndTime(endTime);
        serListDto.setOtherinspectionofficer(otherOfficers);
        serListDto.setTcuRemark(tcuremark);
        serListDto.setTuc(tcu);
        serListDto.setBestPractice(bestpractice);
        ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
        return serListDto;
    }



    public void getServiceData(InspectionCheckQuestionDto temp,InspectionFillCheckListDto fdto,HttpServletRequest request){
        String answer = ParamUtil.getString(request,fdto.getSubName()+temp.getSectionNameShow()+temp.getItemId()+"rad");
            temp.setChkanswer(answer);
        String remark = ParamUtil.getString(request,fdto.getSubName()+temp.getSectionNameShow()+temp.getItemId()+"remark");
        String rectified = ParamUtil.getString(request,fdto.getSubName()+temp.getSectionNameShow()+temp.getItemId()+"rec");
        if(!StringUtil.isEmpty(rectified)&&"No".equals(answer)){
            temp.setRectified(true);
        }else{
            temp.setRectified(false);
        }
        temp.setRemark(remark);
    }

    public AdCheckListShowDto getAdhocDtoFromPage(HttpServletRequest request){
        AdCheckListShowDto showDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(request,"adchklDto");
        List<AdhocNcCheckItemDto> itemDtoList = showDto.getAdItemList();
        if(itemDtoList!=null && !itemDtoList.isEmpty()){
            for(AdhocNcCheckItemDto temp:itemDtoList){
                String answer = ParamUtil.getString(request,temp.getId()+"adhocrad");
                String remark = ParamUtil.getString(request,temp.getId()+"adhocremark");
                String rec = ParamUtil.getString(request,temp.getId()+"adhocrec");
                temp.setAdAnswer(answer);
                temp.setRemark(remark);
                if("No".equals(answer)&&!StringUtil.isEmpty(rec)){
                    temp.setRectified(Boolean.TRUE);
                }else{
                    temp.setRectified(Boolean.FALSE);
                }
            }
        }
        showDto.setAdItemList(itemDtoList);
        return showDto;
    }

    public void doCheckList(BaseProcessClass bpc){
        log.info("=======>>>>>doCheckList>>>>>>>>>>>>>>>>doCheckList");
        HttpServletRequest request = bpc.request;
        InspectionFDtosDto serListDto = getServiceCheckListDataFormViewPage(request);
        InspectionFillCheckListDto commonDto= getCommonDataFromPage(request);
        AdCheckListShowDto adchklDto = getAdhocDtoFromPage(request);
        ParamUtil.setSessionAttr(request,ADHOCLDTO,adchklDto);
        ParamUtil.setSessionAttr(request,COMMONDTO,commonDto);
        ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
        InspectionCheckListItemValidate inspectionCheckListItemValidate = new InspectionCheckListItemValidate();
        Map errMap =  inspectionCheckListItemValidate.validate(request);
        if(!errMap.isEmpty()){
            fillupChklistService.getRateOfCheckList(serListDto,adchklDto,commonDto);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            serListDto.setCheckListTab("chkList");
           // ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errMap));
        }else {
            serListDto.setCheckListTab("chkList");
            fillupChklistService.getRateOfCheckList(serListDto,adchklDto,commonDto);
            ParamUtil.setSessionAttr(request,ADHOCLDTO,adchklDto);
            ParamUtil.setSessionAttr(request,COMMONDTO,commonDto);
            ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }
    }

    public void preViewCheckList(BaseProcessClass bpc)throws IOException{
        log.info("=======>>>>>preViewCheckList>>>>>>>>>>>>>>>>preViewCheckList");
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        String crudActionValue = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE, crudActionValue);
        getOtherInfo(mulReq);

    }

    public void listAhocs(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        AdhocCheckListConifgDto adhocCheckListConifgDto = (AdhocCheckListConifgDto) ParamUtil.getSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR);
        if( adhocCheckListConifgDto == null){
            TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(request,TASKDTO);
            if(taskDto != null ){
                ParamUtil.setSessionAttr(request, INSPECTION_ADHOC_CHECKLIST_LIST_ATTR,fillupChklistService.getAdhocCheckListConifgDtoByCorId(taskDto.getRefNo()));
                ParamUtil.setSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR,fillupChklistService.getAdhocCheckListConifgDtoByCorId(taskDto.getRefNo()));
            }else {
                log.info("-----------taskdto is null-------");
            }
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
        AdhocCheckListConifgDto adhocCheckListConifgDto = (AdhocCheckListConifgDto) ParamUtil.getSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR);
        AdhocCheckListConifgDto adhocCheckListConifgDtoOld = (AdhocCheckListConifgDto)ParamUtil.getSessionAttr(request, INSPECTION_ADHOC_CHECKLIST_LIST_ATTR);
        boolean editAhoc = fillupChklistService.editAhocByAdhocCheckListConifgDtoAndOldAdhocCheckListConifgDto(adhocCheckListConifgDto,adhocCheckListConifgDtoOld);
        if(editAhoc){
            fillupChklistService.saveAdhocDto(adhocCheckListConifgDto);

            TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(request,TASKDTO);
            List<TaskDto> taskDtos = (List<TaskDto>) ParamUtil.getSessionAttr(request,TASKDTOLIST);
            String appPremCorrId = taskDto.getRefNo();
            AdCheckListShowDto adchklDto = fillupChklistService.getAdhocDraftByappCorrId(appPremCorrId);
            if(adchklDto==null){
                adchklDto = fillupChklistService.getAdhoc(appPremCorrId);
            }
            Map<String,String> orgUserDtos = fillupChklistService.userIdNameMapByOrgUserDtos(fillupChklistService.getOrgUserDtosByTaskDatos(taskDtos));
            // get ah draft
            adchklDto = fillupChklistService.getAdCheckListShowDtoByAdCheckListShowDto(adchklDto,orgUserDtos);
            ParamUtil.setSessionAttr(request,ADHOCLDTO,adchklDto);

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

    public void changeTab(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        log.info("----------changeTab --");
        String nowTabIn = ParamUtil.getString(request,"nowTabIn");
        ParamUtil.setRequestAttr(request, "nowTabIn",  nowTabIn);
        String nowComTabIn = ParamUtil.getString(request,"nowComTabIn");
        ParamUtil.setRequestAttr(request, "nowComTabIn",  nowComTabIn);
    }

    public void changeDataForCheckList(HttpServletRequest request){

    }
}
