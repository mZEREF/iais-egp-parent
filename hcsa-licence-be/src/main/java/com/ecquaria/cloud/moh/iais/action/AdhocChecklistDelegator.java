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
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.CheckItemQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AdhocChecklistService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.HcsaChklService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Delegator(value = "adhocChecklistDelegator")
@Slf4j
public class AdhocChecklistDelegator {

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private AdhocChecklistService adhocChecklistService;

    @Autowired
    private HcsaChklService hcsaChklService;

    private FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(CheckItemQueryDto.class)
            .searchAttr(HcsaChecklistConstants.PARAM_CHECKLIST_ITEM_SEARCH)
            .resultAttr(HcsaChecklistConstants.PARAM_CHECKLIST_ITEM_RESULT)
            .sortField("item_id").build();

    /**
     * StartStep: startStep
     *
     * @param bpc
     * @throws
     */
    public void startStep(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        ParamUtil.setSessionAttr(request, "adhocActionFlag", "Y");
    }

    /**
     * AutoStep: initialize
     *
     * @param bpc
     * @throws
     */
    public void initialize(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        List<ChecklistConfigDto> inspectionChecklist = (List<ChecklistConfigDto>)ParamUtil.getSessionAttr(request, AdhocChecklistConstants.INSPECTION_CHECKLIST_LIST_ATTR);
        TaskDto task = (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
        if (task != null) {
            String refNo = task.getRefNo();
            ApplicationViewDto applicationViewDto = applicationViewService.searchByCorrelationIdo(refNo);
            if (applicationViewDto != null) {
                ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
                AuditTrailHelper.auditFunctionWithAppNo("Pre-Inspection",
                        "Adhoc Checklist", applicationDto.getApplicationNo());
                if (inspectionChecklist == null) {
                    inspectionChecklist = adhocChecklistService.getInspectionChecklist(applicationDto);
                }
            }
        }
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM_SEARCH, null);
        ParamUtil.setSessionAttr(request, AdhocChecklistConstants.INSPECTION_CHECKLIST_LIST_ATTR, (Serializable) inspectionChecklist);
    }


    /**
     * AutoStep: getNextStep
     *
     * @param bpc
     * @throws
     */
    public void getNextStep(BaseProcessClass bpc) {
        log.debug("getNextStep");
    }

    /**
     * AutoStep: changePage
     *
     * @param bpc
     * @throws
     */
    public void changePage(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doPaging(searchParam,bpc.request);
    }

    /**
     * AutoStep: doSort
     *
     * @param bpc
     * @throws
     */
    public void doSort(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doSorting(searchParam,bpc.request);
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
            TaskDto task = (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
            if (task != null) {
                String correId = task.getRefNo();
                adhocCheckListConifgDto.setPremCorreId(correId);
            }

            adhocCheckListConifgDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            adhocCheckListConifgDto.setVersion(1);
            adhocCheckListConifgDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

            // Waiting for the event bus save
             ParamUtil.setSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, adhocCheckListConifgDto);

            ParamUtil.setRequestAttr(bpc.request, "preInspInitFlag", InspectionConstants.SWITCH_ACTION_BACK);

            ParamUtil.setSessionAttr(bpc.request, "adhocActionFlag", "N");
            /*adhocChecklistService.saveAdhocChecklist(adhocCheckListConifgDto);*/
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
        Set<String > hashSet = IaisCommonUtils.genNewHashSet();
        AdhocCheckListConifgDto adhocConfigObj = getAdhocItemBySession(request);
        List<AdhocChecklistItemDto> allAdhocItem = adhocConfigObj.getAllAdhocItem();
        for (AdhocChecklistItemDto item : allAdhocItem){
            String question = item.getQuestion();
            if (!hashSet.add(question)){
                hasSampleItem = true;
            }
        }

        if (hasSampleItem){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("checklistItem", "can no add sample item!"));
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
        ParamUtil.setSessionAttr(bpc.request, "adhocActionFlag", "N");
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

        AdhocCheckListConifgDto adhocCheckListConifgDto = getAdhocItemBySession(request);
        List<AdhocChecklistItemDto> allAdhocItem = adhocCheckListConifgDto.getAllAdhocItem();
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
            ParamUtil.setSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, adhocCheckListConifgDto);
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
        log.debug("receive item by item pool ==>>> start");

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);

        // remove has select item in the pool, you can modify my methods with SQL parameters
        AdhocCheckListConifgDto adhocCheckListConifgDto = getAdhocItemBySession(request);
        if (adhocCheckListConifgDto != null){
            List<AdhocChecklistItemDto> allAdhocItem = adhocCheckListConifgDto.getAllAdhocItem();
            log.debug("indicates that a record has been selected ");
            if (!IaisCommonUtils.isEmpty(allAdhocItem)){
                allAdhocItem.removeIf(i -> StringUtil.isEmpty(i.getItemId()));

                SqlHelper.builderNotInSql(searchParam, "item.id", "adhocItemId",
                        allAdhocItem.stream().map(AdhocChecklistItemDto::getItemId).collect(Collectors.toList()));

            }
        }

        QueryHelp.setMainSql("hcsaconfig", "queryChecklistItem", searchParam);
        SearchResult searchResult = hcsaChklService.listChklItem(searchParam);
        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM_RESULT, searchResult);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM_SEARCH, searchParam);
        log.debug("receive item by item pool ==>>> start");
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
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("checklistItem", "please select item!"));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }
        List<ChecklistItemDto> selectItemList = hcsaChklService.listChklItemByItemId(Arrays.asList(checkBoxItemId));
        AdhocCheckListConifgDto adhocCheckListConifgDto = getAdhocItemBySession(request);
        List<AdhocChecklistItemDto> allAdhocItem = adhocCheckListConifgDto.getAllAdhocItem();

        selectItemList.forEach(selectItem -> {
            AdhocChecklistItemDto adhocItem = new AdhocChecklistItemDto();
            String question = selectItem.getChecklistItem();
            String answerType = selectItem.getAnswerType();
            String riskLevel = selectItem.getRiskLevel();

            adhocItem.setItemId(selectItem.getItemId());
            adhocItem.setAnswerType(answerType);
            adhocItem.setQuestion(question);
            adhocItem.setRiskLvl(riskLevel);
            adhocItem.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            allAdhocItem.add(adhocItem);
        });
        ParamUtil.setSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, adhocCheckListConifgDto);
        ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
    }

    private AdhocCheckListConifgDto getAdhocItemBySession(HttpServletRequest request) {
        AdhocCheckListConifgDto obj = (AdhocCheckListConifgDto) ParamUtil.getSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR);
        if (obj == null) {
            obj = new AdhocCheckListConifgDto();
            List<AdhocChecklistItemDto> allAdhocItem = IaisCommonUtils.genNewArrayList();
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

        AdhocCheckListConifgDto adhocCheckListConifgDto = getAdhocItemBySession(request);
        List<AdhocChecklistItemDto> allAdhocItem = adhocCheckListConifgDto.getAllAdhocItem();

        Optional<AdhocChecklistItemDto> optional =
        allAdhocItem.stream().
                filter(i -> i.getAnswerType().equals(answerType)
                        && i.getQuestion().equals(question)
                        && i.getRiskLvl().equals(riskLevel))
                .findFirst();

        if (!optional.isPresent()){
            AdhocChecklistItemDto adhocItem = new AdhocChecklistItemDto();
            adhocItem.setAnswerType(answerType);
            adhocItem.setQuestion(question);
            adhocItem.setRiskLvl(riskLevel);
            adhocItem.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            allAdhocItem.add(adhocItem);

            adhocCheckListConifgDto.setAllAdhocItem(allAdhocItem);
            ParamUtil.setSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, adhocCheckListConifgDto);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }else {
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("customItemError", "can no add sample item!"));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
        }
       }

}
