package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocNcCheckItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.CheckListDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFDtosDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.CheckListVadlidateDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.AccessUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.validation.InspectionCheckListValidation;
import com.esotericsoftware.minlog.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.util.CopyUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
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
    public InspectionNcCheckListDelegator(InsepctionNcCheckListService insepctionNcCheckListService){
        this.insepctionNcCheckListService = insepctionNcCheckListService;
    }

    public void start(BaseProcessClass bpc){
        Log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,ADHOCLDTO,null);
        ParamUtil.setSessionAttr(request,COMMONDTO,null);
        ParamUtil.setSessionAttr(request,TASKDTO,null);
        ParamUtil.setSessionAttr(request,APPLICATIONVIEWDTO,null);
        ParamUtil.setSessionAttr(request,CHECKLISTFILEDTO,null);
    }

    public void successViewPre(BaseProcessClass bpc){
        Log.info("=======>>>>>successViewPre>>>>>>>>>>>>>>>>successViewPre");
        HttpServletRequest request = bpc.request;
        InspectionFDtosDto serListDto  = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SERLISTDTO);
        serListDto.setCheckListTab("chkList");
        ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
    }

    public void successViewBack(BaseProcessClass bpc){
        Log.info("=======>>>>>successViewBack>>>>>>>>>>>>>>>>successViewBack");
        HttpServletRequest request = bpc.request;

    }

    public void init(BaseProcessClass bpc){
        Log.info("=======>>>>>initStep>>>>>>>>>>>>>>>>initRequest");
        AccessUtil.initLoginUserInfo(bpc.request);
        AuditTrailHelper.auditFunction("Checklist Management", "Checklist Config");
        HttpServletRequest request = bpc.request;
        String taskId = ParamUtil.getRequestString(request,"taskId");
        if (StringUtil.isEmpty(taskId)) {
            taskId = "B3A5C76D-9C3A-EA11-BE7E-000C29F371DC";
        }
        String serviceType = "Inspection";
        TaskDto taskDto = taskService.getTaskById(taskId);
        String appPremCorrId = taskDto.getRefNo();
        //fillupChklistService.getDraftByTaskId(taskId,serviceType);
        //fillupChklistService.getAllAppChklDraftList(appPremCorrId);
        CheckListDraftDto checkListDraftDto = null;
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
        List<InspectionFillCheckListDto> cDtoList = null;
        InspectionFDtosDto serListDto = new InspectionFDtosDto();
        List<InspectionFillCheckListDto> commonList = null;
        if(checkListDraftDto!=null){
            commonDto = checkListDraftDto.getComDto();
        }else{
            cDtoList = fillupChklistService.getInspectionFillCheckListDtoList(taskId,"service");
            serListDto.setFdtoList(cDtoList);
            fillupChklistService.getSvcName(serListDto);
            serListDto.setBestPractice("");
            serListDto.setTcuRemark("");
            serListDto.setTuc("");
            commonList = fillupChklistService.getInspectionFillCheckListDtoList(taskId,"common");
            if(commonList!=null&&!commonList.isEmpty()){
                commonDto = commonList.get(0);
            }
        }
        AdCheckListShowDto adchklDto = null;
        adchklDto = fillupChklistService.getAdhocDraftByappCorrId(appPremCorrId);
        if(adchklDto==null){
            adchklDto = fillupChklistService.getAdhoc(appPremCorrId);
        }
        String inspectionDate = fillupChklistService.getInspectionDate(appPremCorrId);
        List<String> inspeciotnOfficers  = fillupChklistService.getInspectiors(taskDto);
        String inspectionleader = fillupChklistService.getInspectionLeader(taskDto);
        serListDto.setInspectionDate(inspectionDate);
        serListDto.setInspectionofficer(inspeciotnOfficers);
        serListDto.setInspectionLeader(inspectionleader);
        ParamUtil.setSessionAttr(request,TASKDTO,taskDto);
        ParamUtil.setSessionAttr(request,APPLICATIONVIEWDTO,appViewDto);
        ParamUtil.setSessionAttr(request,ADHOCLDTO,adchklDto);
        if(maxComChkDto!=null){
            List<InspectionFillCheckListDto> coms = fillupChklistService.getInspectionFillCheckListDtoListForReview(taskId,"common");
            if(!IaisCommonUtils.isEmpty(coms)){
                commonDto = coms.get(0);
            }
        }
        ParamUtil.setSessionAttr(request,COMMONDTO,commonDto);
        if(maxVersionfdtos != null && !IaisCommonUtils.isEmpty(maxVersionfdtos.getFdtoList())){
            List<InspectionFillCheckListDto> ftos = fillupChklistService.getInspectionFillCheckListDtoListForReview(taskId,"service");
            serListDto.setFdtoList(ftos);
        }
        ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);

    }

    public void pre(BaseProcessClass bpc){
        Log.info("=======>>>>>initStep>>>>>>>>>>>>>>>>initRequest");
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

    public void doNext(BaseProcessClass bpc){

        Log.info("=======>>>>>doNextStep>>>>>>>>>>>>>>>>doNextRequest");
        HttpServletRequest request = bpc.request;
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        String crudActionValue = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE, crudActionValue);
        InspectionFDtosDto serListDto = null;
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
            ParamUtil.setRequestAttr(request, "isValid", "Y");
        }else{
            serListDto = getOtherInfo(mulReq);
            ParamUtil.setSessionAttr(mulReq,SERLISTDTO,serListDto);
            TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(mulReq,"taskDto");
            InspectionFillCheckListDto comDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(mulReq,"commonDto");
            AdCheckListShowDto showDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(mulReq,"adchklDto");
            InspectionCheckListValidation InspectionCheckListValidation = new InspectionCheckListValidation();
            Map<String, String> errMap = InspectionCheckListValidation.validate(request);
            if(!errMap.isEmpty()){
                ParamUtil.setRequestAttr(request, "isValid", "N");
                serListDto.setCheckListTab("chkList");
                ParamUtil.setSessionAttr(mulReq,SERLISTDTO,serListDto);
                ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errMap));
            }else{
                ParamUtil.setRequestAttr(request, "isValid", "Y");
                boolean flag = insepctionNcCheckListService.isHaveNcOrBestPractice(serListDto,comDto,showDto);
                insepctionNcCheckListService.submit(comDto,showDto,serListDto,taskDto.getRefNo());
                AdCheckListShowDto showPageDto = new AdCheckListShowDto();
                serListDto.setCheckListTab(null);
                ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
                try {
                    showPageDto = (AdCheckListShowDto) CopyUtil.copyMutableObject(showDto);
                }catch (Exception e){
                    e.printStackTrace();
                }
                ParamUtil.setSessionAttr(request,"adchklDto",showPageDto);
                LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
                fillupChklistService.routingTask(taskDto,null,loginContext,flag);
            }
       }

    }

    private InspectionFDtosDto getOtherInfo(MultipartHttpServletRequest request) {
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
        }
        serListDto.setBestPractice(bestpractice);
        ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
        return serListDto;
    }


    public CheckListVadlidateDto getValueFromPage(HttpServletRequest request) {
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest)request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        CheckListVadlidateDto dto = new CheckListVadlidateDto();
        getDataFromPage(request);
        getOtherInfo(mulReq);
        getCommonDataFromPage(request);
        getAdhocDtoFromPage(request);
        return dto;
    }

    public void doSubmit(BaseProcessClass bpc){
        Log.info("=======>>>>>doSubmitStep>>>>>>>>>>>>>>>>doSubmitRequest");
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
            e.printStackTrace();
        }
        ParamUtil.setSessionAttr(request,"adchklDto",showPageDto);
        LoginContext loginContext = (LoginContext)ParamUtil.getSessionAttr(bpc.request, AppConsts.SESSION_ATTR_LOGIN_USER);
        fillupChklistService.routingTask(taskDto,null,loginContext,flag);
    }

    public InspectionFillCheckListDto getCommonDataFromPage(HttpServletRequest request){
        InspectionFillCheckListDto cDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,"commonDto");
        if(cDto!=null&&cDto.getCheckList()!=null&&!cDto.getCheckList().isEmpty()){
            List<InspectionCheckQuestionDto> checkListDtoList = cDto.getCheckList();
            for(InspectionCheckQuestionDto temp:checkListDtoList){
                String answer = ParamUtil.getString(request,temp.getSectionNameSub()+temp.getItemId()+"comrad");
                String remark = ParamUtil.getString(request,temp.getSectionNameSub()+temp.getItemId()+"comremark");
                String rectified = ParamUtil.getString(request,temp.getSectionNameSub()+temp.getItemId()+"comrec");
                if(!StringUtil.isEmpty(rectified)&&"No".equals(answer)){
                    temp.setRectified(true);
                }else{
                    temp.setRectified(false);
                }
                temp.setChkanswer(answer);
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
        String answer = ParamUtil.getString(request,fdto.getSubName()+temp.getSectionNameSub()+temp.getItemId()+"rad");
        String remark = ParamUtil.getString(request,fdto.getSubName()+temp.getSectionNameSub()+temp.getItemId()+"remark");
        String rectified = ParamUtil.getString(request,fdto.getSubName()+temp.getSectionNameSub()+temp.getItemId()+"rec");
        if(!StringUtil.isEmpty(rectified)&&"No".equals(answer)){
            temp.setRectified(true);
        }else{
            temp.setRectified(false);
        }
        temp.setChkanswer(answer);
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
                    temp.setRectified(true);
                }else{
                    temp.setRectified(false);
                }
            }
        }
        showDto.setAdItemList(itemDtoList);
        return showDto;
    }

    public void doCheckList(BaseProcessClass bpc){
        Log.info("=======>>>>>doCheckList>>>>>>>>>>>>>>>>doCheckList");
        HttpServletRequest request = bpc.request;
        InspectionFDtosDto serListDto = null;
        serListDto = getServiceCheckListDataFormViewPage(request);
        InspectionFillCheckListDto commonDto= getCommonDataFromPage(request);
        AdCheckListShowDto adchklDto = getAdhocDtoFromPage(request);
        serListDto.setCheckListTab("chkList");
        fillupChklistService.getRateOfCheckList(serListDto,adchklDto,commonDto);
        if(serListDto!=null){
            int totalNcNum = serListDto.getGeneralNc()+serListDto.getServiceNc()+serListDto.getAdhocNc();
            serListDto.setTotalNcNum(totalNcNum);
        }
        ParamUtil.setSessionAttr(request,ADHOCLDTO,adchklDto);
        ParamUtil.setSessionAttr(request,COMMONDTO,commonDto);
        ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
    }

    public void preViewCheckList(BaseProcessClass bpc){
        Log.info("=======>>>>>preViewCheckList>>>>>>>>>>>>>>>>preViewCheckList");
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) bpc.request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        String crudActionType = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        String crudActionValue = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_VALUE);

        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE, crudActionType);
        ParamUtil.setRequestAttr(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE, crudActionValue);
        HttpServletRequest request = bpc.request;
        getOtherInfo(mulReq);


    }
}
