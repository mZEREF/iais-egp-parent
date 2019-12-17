package com.ecquaria.cloud.moh.iais.action;


import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.client.task.TaskService;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocNcCheckItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionCheckQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.CheckListVadlidateDto;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.validation.InspectionCheckListValidation;
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
        if(StringUtil.isEmpty(taskId)){
            taskId = "DF1C07EE-191E-EA11-BE7D-000C29F371DC";
        }
        String serviceCode ="BLB";
        String serviceType = "Inspection";
        InspectionFillCheckListDto cDto = fillupChklistService.getInspectionFillCheckListDto(taskId,serviceType);
        ChecklistConfigDto commonCheckListDto = fillupChklistService.getcommonCheckListDto("Inspection","New");
        InspectionFillCheckListDto commonDto  = fillupChklistService.transferToInspectionCheckListDto(commonCheckListDto,cDto.getCheckList().get(0).getAppPreCorreId());
        AdCheckListShowDto adchklDto = fillupChklistService.getAdhoc(cDto.getCheckList().get(0).getAppPreCorreId());
        ParamUtil.setSessionAttr(request,"adchklDto",adchklDto);
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
        HttpServletRequest request = bpc.request;
        InspectionFillCheckListDto cDto = getDataFromPage(request);
        InspectionFillCheckListDto commonDto= getCommonDataFromPage(request);
        AdCheckListShowDto adchklDto = getAdhocDtoFromPage(request);
        InspectionCheckListValidation InspectionCheckListValidation = new InspectionCheckListValidation();
        ParamUtil.setSessionAttr(request,"adchklDto",adchklDto);
        ParamUtil.setSessionAttr(request,"fillCheckListDto",cDto);
        ParamUtil.setSessionAttr(request,"commonDto",commonDto);
        Map<String, String> errMap = InspectionCheckListValidation.validate(request);
        List<SelectOption> isTcuOption = new ArrayList<>();
        SelectOption op = new SelectOption("Yes","Yes");
        SelectOption op2 = new SelectOption("No","No");
        isTcuOption.add(op);
        isTcuOption.add(op2);
        ParamUtil.setRequestAttr(request,"isTcuOption",isTcuOption);
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
