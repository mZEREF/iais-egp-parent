package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
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
import com.ecquaria.cloud.moh.iais.dto.CheckListVadlidateDto;
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
import sop.util.CopyUtil;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
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
    public InspectionNcCheckListDelegator(InsepctionNcCheckListService insepctionNcCheckListService){
        this.insepctionNcCheckListService = insepctionNcCheckListService;
    }

    public void start(BaseProcessClass bpc){
        Log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,ADHOCLDTO,null);
        ParamUtil.setSessionAttr(request,COMMONDTO,null);
        ParamUtil.setSessionAttr(request,TASKDTO,null);
    }

    public void init(BaseProcessClass bpc){
        Log.info("=======>>>>>initStep>>>>>>>>>>>>>>>>initRequest");
        AuditTrailHelper.auditFunction("Checklist Management", "Checklist Config");
        HttpServletRequest request = bpc.request;
        String taskId = ParamUtil.getRequestString(request,"taskId");
        if (StringUtil.isEmpty(taskId)) {
            taskId = "41095B98-6138-EA11-BE7E-000C29F371DC";
        }
        String serviceType = "Inspection";
        TaskDto taskDto = taskService.getTaskById(taskId);
        String appPremCorrId = taskDto.getRefNo();
        CheckListDraftDto checkListDraftDto = fillupChklistService.getDraftByTaskId(taskId,serviceType);
        ApplicationViewDto appViewDto = fillupChklistService.getAppViewDto(taskId);
        InspectionFillCheckListDto commonDto = null;
        List<InspectionFillCheckListDto> cDtoList = null;
        InspectionFDtosDto serListDto = new InspectionFDtosDto();
        List<InspectionFillCheckListDto> commonList = null;
        if(checkListDraftDto!=null){
            commonDto = checkListDraftDto.getComDto();
        }else{
            cDtoList = fillupChklistService.getInspectionFillCheckListDtoList(taskId,"service");
            serListDto.setFdtoList(cDtoList);
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
        List<String> inspeciotnOfficers  = fillupChklistService.getInspectiors(appPremCorrId);
        String inspectionleader = fillupChklistService.getInspectionLeader(appPremCorrId);

        serListDto.setInspectionDate(inspectionDate);
        serListDto.setInspectionStartDate("08:56");
        serListDto.setInspectionEndDate("18:07");
        serListDto.setInspectionofficer(inspeciotnOfficers);
        serListDto.setInspectionLeader(inspectionleader);
        ParamUtil.setSessionAttr(request,TASKDTO,taskDto);
        ParamUtil.setSessionAttr(request,"applicationViewDto",appViewDto);
        ParamUtil.setSessionAttr(request,ADHOCLDTO,adchklDto);
        ParamUtil.setSessionAttr(request,COMMONDTO,commonDto);
        ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);

    }

    public void pre(BaseProcessClass bpc){
        Log.info("=======>>>>>initStep>>>>>>>>>>>>>>>>initRequest");
        HttpServletRequest request = bpc.request;
        TaskDto taskDto = (TaskDto)ParamUtil.getSessionAttr(request,"taskDto");
        AdCheckListShowDto adchklDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(request,"adchklDto");
        InspectionFDtosDto serListDto  = (InspectionFDtosDto)ParamUtil.getSessionAttr(request,SERLISTDTO);
        InspectionFillCheckListDto commonDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,"commonDto");
        fillupChklistService.getRateOfCheckList(serListDto,adchklDto,commonDto,taskDto.getRefNo());
        ParamUtil.setSessionAttr(request,TASKDTO,taskDto);
        ParamUtil.setSessionAttr(request,ADHOCLDTO,adchklDto);
        ParamUtil.setSessionAttr(request,COMMONDTO,commonDto);
        ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
    }

    public void doNext(BaseProcessClass bpc){
        Log.info("=======>>>>>doNextStep>>>>>>>>>>>>>>>>doNextRequest");
        HttpServletRequest request = bpc.request;
        InspectionFDtosDto serListDto = getDataFromPage(request);
        InspectionFillCheckListDto commonDto= getCommonDataFromPage(request);
        AdCheckListShowDto adchklDto = getAdhocDtoFromPage(request);
        serListDto.setCheckListTab("chkList");
        ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
        ParamUtil.setSessionAttr(request,ADHOCLDTO,adchklDto);
        ParamUtil.setSessionAttr(request,COMMONDTO,commonDto);
        InspectionCheckListValidation InspectionCheckListValidation = new InspectionCheckListValidation();
        Map<String, String> errMap = InspectionCheckListValidation.validate(request);
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request, "isValid", "N");
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errMap));
        }else{
            ParamUtil.setRequestAttr(request, "isValid", "Y");
        }

    }

    public CheckListVadlidateDto getValueFromPage(HttpServletRequest request) {
        CheckListVadlidateDto dto = new CheckListVadlidateDto();
        getDataFromPage(request);
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
        insepctionNcCheckListService.submit(comDto,showDto,serListDto,taskDto.getRefNo());
        AdCheckListShowDto showPageDto = new AdCheckListShowDto();
        try {
            showPageDto = (AdCheckListShowDto) CopyUtil.copyMutableObject(showDto);
        }catch (Exception e){
            e.printStackTrace();
        }
        ParamUtil.setSessionAttr(request,"adchklDto",showPageDto);
        //fillupChklistService.routingTask(taskDto,null);

    }

    public InspectionFillCheckListDto getCommonDataFromPage(HttpServletRequest request){
        InspectionFillCheckListDto cDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,"commonDto");
        if(cDto!=null&&cDto.getCheckList()!=null&&!cDto.getCheckList().isEmpty()){
            List<InspectionCheckQuestionDto> checkListDtoList = cDto.getCheckList();
            for(InspectionCheckQuestionDto temp:checkListDtoList){
                String answer = ParamUtil.getString(request,temp.getSectionName()+temp.getItemId()+"comrad");
                String remark = ParamUtil.getString(request,temp.getSectionName()+temp.getItemId()+"comremark");
                String rectified = ParamUtil.getString(request,temp.getSectionName()+temp.getItemId()+"comrec");
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

    public InspectionFDtosDto getDataFromPage(HttpServletRequest request){
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
        String tcu = ParamUtil.getString(request,"tuc");
        String bestpractice = ParamUtil.getString(request,"bestpractice");
        String tcuremark = ParamUtil.getString(request,"tcuRemark");
        String otherOfficers = ParamUtil.getString(request,"otherinspector");
        serListDto.setOtherinspectionofficer(otherOfficers);
        serListDto.setTcuRemark(tcuremark);
        serListDto.setTuc(tcu);
        serListDto.setBestPractice(bestpractice);
        return serListDto;
    }

    public void getServiceData(InspectionCheckQuestionDto temp,InspectionFillCheckListDto fdto,HttpServletRequest request){
        String answer = ParamUtil.getString(request,fdto.getSubName()+temp.getSectionName()+temp.getItemId()+"rad");
        String remark = ParamUtil.getString(request,fdto.getSubName()+temp.getSectionName()+temp.getItemId()+"remark");
        String rectified = ParamUtil.getString(request,fdto.getSubName()+temp.getSectionName()+temp.getItemId()+"rec");
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
}
