package com.ecquaria.cloud.moh.iais.action;

/*
 *author: yichen
 *date time:12/10/2019 12:59 PM
 *description:
 */

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.CheckItemQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.AdhocChecklistService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.HcsaChklService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Delegator(value = "adhocChecklistDelegator")
@Slf4j
public class AdhocChecklistDelegator {

    @Autowired
    private TaskService taskService;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private AdhocChecklistService adhocChecklistService;

    @Autowired
    private HcsaChklService hcsaChklService;

    /**
     * StartStep: startStep
     *
     * @param bpc
     * @throws
     */
    public void startStep(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("Pre-Inspection",
                "Adhoc Checklist");
    }

    /**
     * AutoStep: initialize
     *
     * @param bpc
     * @throws
     */
    public void initialize(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("Pre-Inspection",
                "Adhoc Checklist");
        //String taskId = (String) ParamUtil.getRequestAttr(request, "taskId");
        String taskId = "7102C311-D10D-EA11-BE7D-000C29F371DC";
        TaskDto task = taskService.getTaskById(taskId);

        if (task != null){
            String appNo = task.getRefNo();
            ApplicationDto application = applicationViewService.getApplicaitonByAppNo(appNo);
            List<ChecklistConfigDto> inspectionChecklist = adhocChecklistService.getInspectionChecklist(application);
            log.info("inspectionChecklist info =====>>>>>>>>>>> " + inspectionChecklist.toString());
            ParamUtil.setSessionAttr(request, "inspectionChecklistAttr", (Serializable) inspectionChecklist);
        }

        AdhocCheckListConifgDto adhocCheckListConifgDto = new AdhocCheckListConifgDto();
        ParamUtil.setSessionAttr(request, "adhocCheckListAttr", (Serializable) adhocCheckListConifgDto);
    }


    /**
     * AutoStep: getNextStep
     *
     * @param bpc
     * @throws
     */
    public void getNextStep(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

    }

    /**
     * AutoStep: receiveItemPool
     *
     * @param bpc
     * @throws
     */
    public void receiveItemPool(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        FilterParameter filterParameter = new FilterParameter();

        filterParameter.setClz(CheckItemQueryDto.class);
        filterParameter.setSearchAttr(HcsaChecklistConstants.PARAM_CHECKLIST_ITEM_SEARCH);
        filterParameter.setResultAttr(HcsaChecklistConstants.PARAM_CHECKLIST_ITEM_RESULT);
        filterParameter.setSortField("item_id");

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        QueryHelp.setMainSql("hcsaconfig", "listChklItem", searchParam);

        SearchResult searchResult = hcsaChklService.listChklItem(searchParam);

        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM_RESULT, searchResult);
    }

    private boolean hasSampleItem(List<AdhocChecklistItemDto> allAdhocItem, List<ChecklistItemDto> selectItemList){
        long count = allAdhocItem.stream().map(adhocItem -> selectItemList.stream().filter(
                selectItem -> adhocItem.getQuestion().equals(selectItem.getChecklistItem())
        )).count();

        return count > 0 ? true : false;
    }


    /**
     * AutoStep: receiveItemPool
     *
     * @param bpc
     * @throws
     */
    public void appendToTail(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        String[] checkBoxItemId = ParamUtil.getStrings(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX);
        if(checkBoxItemId == null || checkBoxItemId.length <= 0){
            return;
        }

        List<ChecklistItemDto> selectItemList = hcsaChklService.listChklItemByItemId(Arrays.asList(checkBoxItemId));
        AdhocCheckListConifgDto adhocCheckListConifgDto = getSessionAdhocCheckListConifg(request);
        List<AdhocChecklistItemDto> allAdhocItem = adhocCheckListConifgDto.getAllAdhocItem();
        boolean hasSampleItem = hasSampleItem(allAdhocItem, selectItemList);
        if (hasSampleItem){
            Map<String,String> errorMap = new HashMap<>(1);
            errorMap.put("sampleItem", "Cannot add sample item to adhoc section!");
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMAP,errorMap);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
        }else {
            AdhocChecklistItemDto adhocItem = new AdhocChecklistItemDto();
            selectItemList.stream().forEach(selectItem -> {
                String question = selectItem.getChecklistItem();
                String answerType = selectItem.getAnswerType();
                String riskLevel = selectItem.getRiskLevel();
                adhocItem.setAnswerType(answerType);
                adhocItem.setQuestion(question);
                adhocItem.setRiskLvl(riskLevel);
                allAdhocItem.add(adhocItem);
            });
        }

        ParamUtil.setRequestAttr(request, "adhocCheckListAttr", adhocCheckListConifgDto);
    }

    private AdhocCheckListConifgDto getSessionAdhocCheckListConifg(HttpServletRequest request){
        AdhocCheckListConifgDto adhocCheckListConifgDto = (AdhocCheckListConifgDto) ParamUtil.getSessionAttr(request, "adhocCheckListAttr");
        if (adhocCheckListConifgDto == null){
            adhocCheckListConifgDto = new AdhocCheckListConifgDto();
            List<AdhocChecklistItemDto> allAdhocItem = new ArrayList<>();
            adhocCheckListConifgDto.setAllAdhocItem(allAdhocItem);
            return adhocCheckListConifgDto;
        }else {
            return adhocCheckListConifgDto;
        }
    }

    /**
     * AutoStep: customItem
     *
     * @param bpc
     * @throws
     */
    public void customItem(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String question = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM);
        String answerType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_ANSWER_TYPE);
        String riskLevel = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_RISK_LEVEL);

        Map<String,String> errorMap = new HashMap<>(3);
        if (StringUtils.isEmpty(question)){
            errorMap.put("question", "please input Question");
        }

        if (StringUtils.isEmpty(answerType)){
            errorMap.put("question", "please select answer type");
        }

        if (StringUtils.isEmpty(riskLevel)){
            errorMap.put("question", "please select risk level");
        }

        if (errorMap.size() > 0){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMAP,errorMap);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
        }else {
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");

            AdhocCheckListConifgDto adhocCheckListConifgDto = getSessionAdhocCheckListConifg(request);
            List<AdhocChecklistItemDto> allAdhocItem = adhocCheckListConifgDto.getAllAdhocItem();

            AdhocChecklistItemDto adhocItem = new AdhocChecklistItemDto();
            adhocItem.setAnswerType(answerType);
            adhocItem.setQuestion(question);
            adhocItem.setRiskLvl(riskLevel);

            allAdhocItem.add(adhocItem);
            adhocCheckListConifgDto.setAllAdhocItem(allAdhocItem);
            ParamUtil.setRequestAttr(request, "adhocCheckListAttr", adhocCheckListConifgDto);
        }

    }
}
