package com.ecquaria.cloud.moh.iais.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * FillupChklistDelegator
 *
 * @author Guyin
 * @date 2019/11/21 15:07
 */

@Delegator("fillupChklistDelegator")
@Slf4j
public class FillupChklistDelegator {

    private static final String SERLISTDTO="serListDto";
    @Autowired
    FillupChklistService fillupChklistService;
    @Autowired
    InsepctionNcCheckListService insepctionNcCheckListService;
    @Autowired
    AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;
    @Autowired
    InspectionAssignTaskService inspectionAssignTaskService;
    @Autowired
    TaskService taskService;
    public FillupChklistDelegator(FillupChklistService fillupChklistService){
        this.fillupChklistService = fillupChklistService;
    }

    /**
     * StartStep: AssignedInspectionTask
     *
     * @param bpc
     * @throws
     */
    public void start(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request,"adchklDto",null);
        ParamUtil.setSessionAttr(request,"fillCheckListDto",null);
        ParamUtil.setSessionAttr(request,"commonDto",null);
        ParamUtil.setSessionAttr(request,"taskDto",null);
    }

    public void init(BaseProcessClass bpc){
        Log.info("=======>>>>>initStep>>>>>>>>>>>>>>>>initRequest");
        AuditTrailHelper.auditFunction("Checklist Management", "Checklist Config");
        HttpServletRequest request = bpc.request;
        String taskId = ParamUtil.getRequestString(request,"taskId");
        if (StringUtil.isEmpty(taskId)) {
            taskId = "F9359FD4-5934-EA11-BE7D-000C29F371DC";
        }
        String serviceType = "Inspection";
        TaskDto taskDto = taskService.getTaskById(taskId);
        String appPremCorrId = taskDto.getRefNo();
        CheckListDraftDto checkListDraftDto = fillupChklistService.getDraftByTaskId(taskId,serviceType);
        ApplicationViewDto appViewDto = fillupChklistService.getAppViewDto(taskId);
        //InspectionFillCheckListDto cDto = null;
        InspectionFillCheckListDto commonDto = null;
        List<InspectionFillCheckListDto> cDtoList = null;
        InspectionFDtosDto serListDto = new InspectionFDtosDto();
        List<InspectionFillCheckListDto> commonList = null;
        if(checkListDraftDto!=null){
            //cDto = checkListDraftDto.getGeneralDto();
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
        ParamUtil.setSessionAttr(request,"taskDto",taskDto);
        ParamUtil.setSessionAttr(request,"applicationViewDto",appViewDto);
        ParamUtil.setSessionAttr(request,"adchklDto",adchklDto);
        ParamUtil.setSessionAttr(request,"commonDto",commonDto);
        ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
    }
    /**
     * AutoStep: AssignedInspectionTask
     *
     * @param bpc
     * @throws
     */
    public void assignedInspectionTask(BaseProcessClass bpc) {
        AuditTrailHelper.auditFunction("Checklist Management", "Checklist Config");
        HttpServletRequest request = bpc.request;
        List<SelectOption> isTcuOption = new ArrayList<>();
        SelectOption op = new SelectOption("Yes","Yes");
        SelectOption op2 = new SelectOption("No","No");
        isTcuOption.add(op);
        isTcuOption.add(op2);
        ParamUtil.setRequestAttr(request,"isTcuOption",isTcuOption);
    }

    public CheckListVadlidateDto getValueFromPage(HttpServletRequest request) {
        CheckListVadlidateDto dto = new CheckListVadlidateDto();
        getDataFromPage(request);
        getCommonDataFromPage(request);
        getAdhocDtoFromPage(request);
        return dto;
    }


    /**
     * AutoStep: InspectionChecklist
     *
     * @param bpc
     * @throws
     */
    public void inspectionChecklist(BaseProcessClass bpc) {
        /*HttpServletRequest request = bpc.request;
        InspectionFillCheckListDto cDto = getDataFromPage(request);
        InspectionFillCheckListDto commonDto= getCommonDataFromPage(request);
        AdCheckListShowDto adchklDto = getAdhocDtoFromPage(request);
        InspectionCheckListValidation InspectionCheckListValidation = new InspectionCheckListValidation();
        ParamUtil.setSessionAttr(request,"adchklDto",adchklDto);
        ParamUtil.setSessionAttr(request,"fillCheckListDto",cDto);
        ParamUtil.setSessionAttr(request,"commonDto",commonDto);
        Map<String, String> errMap = InspectionCheckListValidation.validate(request);
        String draftFalg = ParamUtil.getString(request,"draftFalg");
        if(StringUtil.isEmpty(draftFalg)) {
            if (!errMap.isEmpty()) {
                ParamUtil.setRequestAttr(request, "isValid", "N");
                ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errMap));
            } else {
                ParamUtil.setRequestAttr(request, "isValid", "Y");
            }
        }else{
            fillupChklistService.saveDraft(cDto,commonDto,adchklDto);
            ParamUtil.setRequestAttr(request, "isValid", "N");
        }*/
        Log.info("=======>>>>>doNextStep>>>>>>>>>>>>>>>>doNextRequest");
        HttpServletRequest request = bpc.request;
        InspectionFDtosDto serListDto = getDataFromPage(request);
        InspectionFillCheckListDto commonDto= getCommonDataFromPage(request);
        AdCheckListShowDto adchklDto = getAdhocDtoFromPage(request);
        ParamUtil.setSessionAttr(request,SERLISTDTO,serListDto);
        ParamUtil.setSessionAttr(request,"adchklDto",adchklDto);
        ParamUtil.setSessionAttr(request,"commonDto",commonDto);
        InspectionCheckListValidation InspectionCheckListValidation = new InspectionCheckListValidation();
        Map<String, String> errMap = InspectionCheckListValidation.validate(request);
        String draftFalg = ParamUtil.getString(request,"draftFalg");
        if(StringUtil.isEmpty(draftFalg)) {

        }else{
            fillupChklistService.saveDraft(serListDto,commonDto,adchklDto);
        }
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request, "isValid", "N");
            ParamUtil.setRequestAttr(bpc.request, "errorMsg", WebValidationHelper.generateJsonStr(errMap));
        }else{
            ParamUtil.setRequestAttr(request, "isValid", "Y");
        }
    }



    /**
     * AutoStep: SubmitInspection
     *
     * @param bpc
     * @throws
     */
    public void submitInspection(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InspectionFillCheckListDto comDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,"commonDto");
        InspectionFillCheckListDto icDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,"fillCheckListDto");
        AdCheckListShowDto showDto = (AdCheckListShowDto)ParamUtil.getSessionAttr(request,"adchklDto");


        AdCheckListShowDto showPageDto = new AdCheckListShowDto();
        try {
            showPageDto = (AdCheckListShowDto)CopyUtil.copyMutableObject(showDto);
        }catch (Exception e){
            e.printStackTrace();
        }
        ParamUtil.setSessionAttr(request,"adchklDto",showPageDto);
        fillupChklistService.saveAdhocDto(showDto,icDto.getCheckList().get(0).getAppPreCorreId());
        fillupChklistService.merge(comDto,icDto);
        fillupChklistService.saveDto(icDto);
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
        ParamUtil.setSessionAttr(request,"commonDto",cDto);
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
        serListDto.setTcuRemark(tcuremark);
        serListDto.setTuc(tcu);
        serListDto.setBestPractice(bestpractice);
        return serListDto;
    }

    public void getServiceData(InspectionCheckQuestionDto temp,InspectionFillCheckListDto fdto,HttpServletRequest request){
        String answer = ParamUtil.getString(request,fdto.getSvcCode()+temp.getSectionName()+temp.getItemId()+"rad");
        String remark = ParamUtil.getString(request,fdto.getSvcCode()+temp.getSectionName()+temp.getItemId()+"remark");
        String rectified = ParamUtil.getString(request,fdto.getSvcCode()+temp.getSectionName()+temp.getItemId()+"rec");
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
