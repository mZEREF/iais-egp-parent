package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocNcCheckItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.CheckListDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.CheckListVadlidateDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
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
    @Autowired
    private ApplicationViewService applicationViewService;
    public InspectionNcCheckListDelegator(InsepctionNcCheckListService insepctionNcCheckListService){
        this.insepctionNcCheckListService = insepctionNcCheckListService;
    }

    public void start(BaseProcessClass bpc){
        Log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>emailRequest");
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,"adchklDto",null);
        ParamUtil.setSessionAttr(request,"fillCheckListDto",null);
        ParamUtil.setSessionAttr(request,"commonDto",null);
    }

    public void init(BaseProcessClass bpc){
        Log.info("=======>>>>>initStep>>>>>>>>>>>>>>>>initRequest");
        AuditTrailHelper.auditFunction("Checklist Management", "Checklist Config");
        HttpServletRequest request = bpc.request;
        //String taskId = ParamUtil.getString(request,"TaskId");
        String taskId = ParamUtil.getRequestString(request,"taskId");
        //String taskId = "38CDA333-BC2E-EA11-BE7D-000C29F371DC";
        if (StringUtil.isEmpty(taskId)) {
            taskId = "BA71A041-AD32-EA11-BE7D-000C29F371DC";
        }
        String serviceType = "Inspection";
/*        InspectionFillCheckListDto cDto = fillupChklistService.getInspectionFillCheckListDto(taskId,serviceType);
        String configId = cDto.getCheckList().get(0).getConfigId();
        AppPremisesPreInspectChklDto appPremPreCklDto = insepctionNcCheckListService.getAppPremChklDtoByTaskId(taskId,configId);
        InspectionFillCheckListDto insepectionNcCheckListDto = null;
        String appPremCorrId = appPremPreCklDto.getAppPremCorrId();
        AppPremisesRecommendationDto appPremisesRecommendationDto = insepctionNcCheckListService.getAppRecomDtoByAppCorrId(appPremCorrId,"tcu");
        List<AppPremisesPreInspectionNcItemDto> itemDtoList = insepctionNcCheckListService.getNcItemDtoByAppCorrId(appPremPreCklDto.getAppPremCorrId());
        insepectionNcCheckListDto = insepctionNcCheckListService.getNcCheckList(cDto,appPremPreCklDto,itemDtoList,appPremisesRecommendationDto);
        ChecklistConfigDto commonCheckListDto = fillupChklistService.getcommonCheckListDto("Inspection","New");
        InspectionFillCheckListDto commonDto  = fillupChklistService.transferToInspectionCheckListDto(commonCheckListDto,cDto.getCheckList().get(0).getAppPreCorreId());
        insepctionNcCheckListService.getCommonDto(commonDto,appPremPreCklDto,itemDtoList);
        AdCheckListShowDto adchklDto =insepctionNcCheckListService.getAdhocCheckListDto(appPremCorrId);
        ApplicationViewDto appViewDto = fillupChklistService.getAppViewDto(taskId);
        TaskDto  taskDto = fillupChklistService.getTaskDtoById(taskId);
        ParamUtil.setSessionAttr(request,"adchklDto",adchklDto);
        ParamUtil.setSessionAttr(request,"taskDto",taskDto);
        ParamUtil.setSessionAttr(request,"fillCheckListDto",insepectionNcCheckListDto);
        ParamUtil.setSessionAttr(request,"commonDto",commonDto);
        ParamUtil.setSessionAttr(request,"applicationViewDto",appViewDto);*/
        TaskDto taskDto = taskService.getTaskById(taskId);
        CheckListDraftDto checkListDraftDto = fillupChklistService.getDraftByTaskId(taskId,serviceType);
        ApplicationViewDto appViewDto = fillupChklistService.getAppViewDto(taskId);
        InspectionFillCheckListDto cDto = null;
        InspectionFillCheckListDto commonDto = null;
        if(checkListDraftDto!=null){
            cDto = checkListDraftDto.getGeneralDto();
            commonDto = checkListDraftDto.getComDto();
        }else{
            cDto = fillupChklistService.getInspectionFillCheckListDto(taskId,serviceType);
            ChecklistConfigDto commonCheckListDto = fillupChklistService.getcommonCheckListDto("Inspection","New");
            if(commonCheckListDto!=null){
                commonDto  = fillupChklistService.transferToInspectionCheckListDto(commonCheckListDto,cDto.getCheckList().get(0).getAppPreCorreId());
            }
        }
        AdCheckListShowDto adchklDto = null;
        adchklDto = fillupChklistService.getAdhocDraftByappCorrId(cDto.getCheckList().get(0).getAppPreCorreId());
        if(adchklDto==null){
            adchklDto = fillupChklistService.getAdhoc(cDto.getCheckList().get(0).getAppPreCorreId());
        }
        ParamUtil.setSessionAttr(request,"taskDto",taskDto);
        ParamUtil.setSessionAttr(request,"applicationViewDto",appViewDto);
        ParamUtil.setSessionAttr(request,"adchklDto",adchklDto);
        ParamUtil.setSessionAttr(request,"commonDto",commonDto);
        ParamUtil.setSessionAttr(request,"fillCheckListDto",cDto);
    }

    public void pre(BaseProcessClass bpc){
        Log.info("=======>>>>>initStep>>>>>>>>>>>>>>>>initRequest");
    }

    public void doNext(BaseProcessClass bpc){
        Log.info("=======>>>>>doNextStep>>>>>>>>>>>>>>>>doNextRequest");
        HttpServletRequest request = bpc.request;
        InspectionFillCheckListDto cDto = getDataFromPage(request);
        InspectionFillCheckListDto commonDto= getCommonDataFromPage(request);
        AdCheckListShowDto adchklDto = getAdhocDtoFromPage(request);
        ParamUtil.setSessionAttr(request,"adchklDto",adchklDto);
        ParamUtil.setSessionAttr(request,"commonDto",commonDto);
        ParamUtil.setSessionAttr(request,"fillCheckListDto",cDto);
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
        InspectionFillCheckListDto icDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,"fillCheckListDto");
        InspectionFillCheckListDto comDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,"commonDto");
        AdCheckListShowDto showDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(request,"adchklDto");
        fillupChklistService.merge(comDto,icDto);
        insepctionNcCheckListService.submit(icDto,showDto);
        ApplicationViewDto appViewDto = (ApplicationViewDto)ParamUtil.getSessionAttr(request,"applicationViewDto");
        AdCheckListShowDto showPageDto = new AdCheckListShowDto();
        try {
            showPageDto = (AdCheckListShowDto) CopyUtil.copyMutableObject(showDto);

        }catch (Exception e){
            e.printStackTrace();
        }
        ParamUtil.setSessionAttr(request,"adchklDto",showPageDto);
        try {
            TaskDto taskDto = (TaskDto) ParamUtil.getSessionAttr(bpc.request,"taskDto");
            fillupChklistService.routingTask(taskDto,null);

        }catch (Exception e){

        }
    }

    public InspectionFillCheckListDto getCommonDataFromPage(HttpServletRequest request){
        InspectionFillCheckListDto cDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,"commonDto");
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
        return cDto;
    }

    public InspectionFillCheckListDto getDataFromPage(HttpServletRequest request){
        InspectionFillCheckListDto cDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,"fillCheckListDto");
        List<InspectionCheckQuestionDto> checkListDtoList = cDto.getCheckList();
        for(InspectionCheckQuestionDto temp:checkListDtoList){
            String answer = ParamUtil.getString(request,temp.getSectionName()+temp.getItemId()+"rad");
            String remark = ParamUtil.getString(request,temp.getSectionName()+temp.getItemId()+"remark");
            String rectified = ParamUtil.getString(request,temp.getSectionName()+temp.getItemId()+"rec");
            if(!StringUtil.isEmpty(rectified)&&"No".equals(answer)){
                temp.setRectified(true);
            }else{
                temp.setRectified(false);
            }
            temp.setChkanswer(answer);
            temp.setRemark(remark);
        }
        String tcu = ParamUtil.getString(request,"tuc");
        String bestpractice = ParamUtil.getString(request,"bestpractice");
        String tcuremark = ParamUtil.getString(request,"tcuRemark");
        cDto.setTcuRemark(tcuremark);
        cDto.setTuc(tcu);
        cDto.setBestPractice(bestpractice);
        fillupChklistService.fillInspectionFillCheckListDto(cDto);
        return cDto;
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
