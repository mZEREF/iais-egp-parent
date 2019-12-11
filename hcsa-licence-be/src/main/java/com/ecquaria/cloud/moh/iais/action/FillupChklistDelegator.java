package com.ecquaria.cloud.moh.iais.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.client.task.TaskService;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.constant.sample.DemoConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.validation.InspectionCheckListValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
    private TaskService taskService;
    private FillupChklistService fillupChklistService;
    private FilterParameter filterParameter;


    @Autowired
    public FillupChklistDelegator(FillupChklistService fillupChklistService, FilterParameter filterParameter){
        this.fillupChklistService = fillupChklistService;
        this.filterParameter = filterParameter;
    }

    /**
     * StartStep: AssignedInspectionTask
     *
     * @param bpc
     * @throws
     */
    public void start(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction("Checklist Management", "Checklist Config");
        HttpServletRequest request = bpc.request;

        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR, null);
    }

    public void init(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction("Checklist Management", "Checklist Config");
        HttpServletRequest request = bpc.request;
        String taskId = ParamUtil.getString(request,"TaskId");
        String serviceCode ="BLB";
        String serviceType = "Inspection";
        InspectionFillCheckListDto cDto = fillupChklistService.getInspectionFillCheckListDto(taskId,serviceCode,serviceType);
        ChecklistConfigDto commonCheckListDto = fillupChklistService.getcommonCheckListDto("Inspection","New");
        InspectionFillCheckListDto commonDto  = fillupChklistService.transferToInspectionCheckListDto(commonCheckListDto,cDto.getCheckList().get(0).getAppPreCorreId());

        ParamUtil.setSessionAttr(request,"commonDto",commonDto);
        ParamUtil.setSessionAttr(request,"fillCheckListDto",cDto);
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



    /**
     * AutoStep: InspectionChecklist
     *
     * @param bpc
     * @throws
     */
    public void inspectionChecklist(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        InspectionFillCheckListDto cDto = getDataFromPage(request);
        InspectionFillCheckListDto comcDto= getCommonDataFromPage(request);
        InspectionCheckListValidation InspectionCheckListValidation = new InspectionCheckListValidation();
        ParamUtil.setSessionAttr(request,"fillCheckListDto",cDto);
        ParamUtil.setSessionAttr(request,"comcDto",cDto);
        Map<String, String> errMap = InspectionCheckListValidation.validate(request);
        List<SelectOption> isTcuOption = new ArrayList<>();
        SelectOption op = new SelectOption("Yes","Yes");
        SelectOption op2 = new SelectOption("No","No");
        isTcuOption.add(op);
        isTcuOption.add(op2);
        ParamUtil.setRequestAttr(request,"isTcuOption",isTcuOption);
        if(!errMap.isEmpty()){
            ParamUtil.setRequestAttr(request, "isValid", "N");
            ParamUtil.setRequestAttr(request, DemoConstants.ERRORMAP,errMap);
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
        ChecklistConfigDto commonCheckListDto = (ChecklistConfigDto)ParamUtil.getSessionAttr(request,"commonCheckListDto");
        InspectionFillCheckListDto icDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,"fillCheckListDto");
        fillupChklistService.saveDto(icDto);
        fillupChklistService.saveCommonDto(commonCheckListDto,icDto.getCheckList().get(0).getAppPreCorreId());
    }
    public InspectionFillCheckListDto getCommonDataFromPage(HttpServletRequest request){
        InspectionFillCheckListDto cDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,"commonDto");
        List<InspectionCheckQuestionDto> checkListDtoList = cDto.getCheckList();
        for(InspectionCheckQuestionDto temp:checkListDtoList){
            String answer = ParamUtil.getString(request,temp.getSectionName()+temp.getItemId()+"comrad");
            String remark = ParamUtil.getString(request,temp.getSectionName()+temp.getItemId()+"comremark");
            String rectified = ParamUtil.getString(request,temp.getSectionName()+temp.getItemId()+"comrec");
            if(!StringUtil.isEmpty(rectified)){
                temp.setRectified(true);
            }else{
                temp.setRectified(false);
            }
            temp.setChkanswer(answer);
            temp.setRemark(remark);
        }
        return cDto;
    }



    public InspectionFillCheckListDto getDataFromPage(HttpServletRequest request){
        InspectionFillCheckListDto cDto = (InspectionFillCheckListDto)ParamUtil.getSessionAttr(request,"fillCheckListDto");
        List<InspectionCheckQuestionDto> checkListDtoList = cDto.getCheckList();
        for(InspectionCheckQuestionDto temp:checkListDtoList){
            String answer = ParamUtil.getString(request,temp.getSectionName()+temp.getItemId()+"rad");
            String remark = ParamUtil.getString(request,temp.getSectionName()+temp.getItemId()+"remark");
            String rectified = ParamUtil.getString(request,temp.getSectionName()+temp.getItemId()+"rec");
            if(!StringUtil.isEmpty(rectified)){
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

}
