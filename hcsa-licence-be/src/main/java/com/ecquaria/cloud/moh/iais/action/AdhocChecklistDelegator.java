package com.ecquaria.cloud.moh.iais.action;

/*
 *author: yichen
 *date time:12/10/2019 12:59 PM
 *description:
 */

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.AdhocChecklistConstants;
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
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AdhocChecklistService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.HcsaChklService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    public void startStep(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("Pre-Inspection",
                "Adhoc Checklist");

        ParamUtil.setSessionAttr(request, AdhocChecklistConstants.INSPECTION_CHECKLIST_LIST_ATTR, null);
        ParamUtil.setSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, null);
    }

    /**
     * AutoStep: initialize
     *
     * @param bpc
     * @throws
     */
    public void initialize(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction("Pre-Inspection",
                "Adhoc Checklist");

        /*boolean isIntranet = AccessUtil.isIntranet(IaisEGPHelper.getCurrentAuditTrailDto());
        if (!isIntranet){

            return;
        }

        String taskId = ParamUtil.getMaskedString(request, "taskId");*/

        String taskId = "4E032AF9-C110-EA11-BE7D-000C29F371DC";

        log.info("doInspectorSearchTaskAssign task id ====>>>>> " + taskId);
        if (taskId == null) {
            return;
        }

        TaskDto task = taskService.getTaskById(taskId);

        if (task != null){
            String appNo = task.getRefNo();
            ApplicationDto application = applicationViewService.getApplicaitonByAppNo(appNo);
            List<ChecklistConfigDto> inspectionChecklist = adhocChecklistService.getInspectionChecklist(application);

            log.info("inspectionChecklist info =====>>>>>>>>>>> " + inspectionChecklist.toString());
            ParamUtil.setSessionAttr(request, AdhocChecklistConstants.INSPECTION_CHECKLIST_LIST_ATTR, (Serializable) inspectionChecklist);
        }
    }


    /**
     * AutoStep: getNextStep
     *
     * @param bpc
     * @throws
     */
    public void getNextStep(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
    }

    /**
     * AutoStep: saveAdhocItem
     *
     * @param bpc
     * @throws
     */
    public void saveAdhocItem(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        AdhocCheckListConifgDto adhocCheckListConifgDto = (AdhocCheckListConifgDto) ParamUtil.getSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR);
        if (adhocCheckListConifgDto != null){
            adhocCheckListConifgDto.setPremCorreId(adhocChecklistService.getCurrentCorreId());
            adhocCheckListConifgDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            adhocCheckListConifgDto.setVersion(1);
            adhocCheckListConifgDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            adhocChecklistService.saveAdhocChecklist(adhocCheckListConifgDto);
        }
    }


    /**
     * AutoStep: validateAdhocData
     *
     * @param bpc
     * @throws
     */
    public void validateAdhocData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        boolean hasSampleItem = false;
        Set<String > hashSet = new HashSet<>();
        AdhocCheckListConifgDto adhocConfigObj = getAdhocConfigObj(request);
        List<AdhocChecklistItemDto> allAdhocItem = adhocConfigObj.getAllAdhocItem();
        for (AdhocChecklistItemDto adhocItem : allAdhocItem){
            String question = adhocItem.getQuestion();
            if (!hashSet.add(question)){
                hasSampleItem = true;
            }
        }

        if (hasSampleItem){
            Map<String, String> errorMap = new HashMap<>(1);
            errorMap.put("checklistItem", "can no add sample item!");
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
        }else {
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }
    }

    /**
     * AutoStep: doBack
     *
     * @param bpc
     * @throws
     */
    public void doBack(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

    }

    /**
     * AutoStep: doCancel
     *
     * @param bpc
     * @throws
     */
    public void doCancel(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
    }

    /**
     * AutoStep: removeAdhocItem
     *
     * @param bpc
     * @throws
     */
    public void removeAdhocItem(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        String value = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);

        AdhocCheckListConifgDto adhocConfigObj = getAdhocConfigObj(request);
        List<AdhocChecklistItemDto> allAdhocItem = adhocConfigObj.getAllAdhocItem();
        Iterator<AdhocChecklistItemDto> iterator = allAdhocItem.iterator();
        while (iterator.hasNext()){
            AdhocChecklistItemDto adhodItem = iterator.next();
            String question = adhodItem.getQuestion();
            if (value.equals(question)){
                iterator.remove();
                break;
            }
        }

        if(allAdhocItem.isEmpty()){
            ParamUtil.setSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, null);
        }else {
            ParamUtil.setSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, adhocConfigObj);
        }
    }

    /**
     * AutoStep: receiveItemPool
     *
     * @param bpc
     * @throws
     */
    public void receiveItemPool(BaseProcessClass bpc) {
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

    /**
     * AutoStep: receiveItemPool
     *
     * @param bpc
     * @throws
     */
    public void appendToTail(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        String[] checkBoxItemId = ParamUtil.getStrings(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX);
        if (checkBoxItemId == null || checkBoxItemId.length == 0) {
            Map<String, String> errorMap = new HashMap<>(1);
            errorMap.put("checklistItem", "please select item!");
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }

        List<ChecklistItemDto> selectItemList = hcsaChklService.listChklItemByItemId(Arrays.asList(checkBoxItemId));

        AdhocCheckListConifgDto adhocCheckListConifgDto = getAdhocConfigObj(request);
        List<AdhocChecklistItemDto> allAdhocItem = adhocCheckListConifgDto.getAllAdhocItem();

        ParamUtil.setRequestAttr(request, "selectItemList", selectItemList);

        selectItemList.stream().forEach(selectItem -> {
            AdhocChecklistItemDto adhocItem = new AdhocChecklistItemDto();
            String question = selectItem.getChecklistItem();
            String answerType = selectItem.getAnswerType();
            String riskLevel = selectItem.getRiskLevel();

            adhocItem.setAnswerType(answerType);
            adhocItem.setQuestion(question);
            adhocItem.setRiskLvl(riskLevel);
            adhocItem.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            allAdhocItem.add(adhocItem);

        });
        ParamUtil.setSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, adhocCheckListConifgDto);
        ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
    }

    private AdhocCheckListConifgDto getAdhocConfigObj(HttpServletRequest request) {
        AdhocCheckListConifgDto obj = (AdhocCheckListConifgDto) ParamUtil.getSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR);
        if (obj == null) {
            obj = new AdhocCheckListConifgDto();
            List<AdhocChecklistItemDto> allAdhocItem = new ArrayList<>();
            obj.setAllAdhocItem(allAdhocItem);
            return obj;
        } else {
            return obj;
        }
    }

    /**
     * AutoStep: customItem
     *
     * @param bpc
     * @throws
     */
    public void customItem(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        String action = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_TYPE);
        if (!"customItem".equals(action)){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            return;
        }

        String question = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM);
        String answerType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_ANSWER_TYPE);
        String riskLevel = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_RISK_LEVEL);

        ChecklistItemDto customDto = new ChecklistItemDto();
        customDto.setChecklistItem(question);
        customDto.setAnswerType(answerType);
        customDto.setRiskLevel(riskLevel);

        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM, customDto);
        ValidationResult validationResult = WebValidationHelper.validateProperty(customDto, "customAdd");
        if(validationResult != null && validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }

            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            AdhocCheckListConifgDto adhocCheckListConifgDto = getAdhocConfigObj(request);
            List<AdhocChecklistItemDto> allAdhocItem = adhocCheckListConifgDto.getAllAdhocItem();

            AdhocChecklistItemDto adhocItem = new AdhocChecklistItemDto();
            adhocItem.setAnswerType(answerType);
            adhocItem.setQuestion(question);
            adhocItem.setRiskLvl(riskLevel);
            adhocItem.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            allAdhocItem.add(adhocItem);
            adhocCheckListConifgDto.setAllAdhocItem(allAdhocItem);

            ParamUtil.setSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, adhocCheckListConifgDto);
       }

}
