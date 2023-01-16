package com.ecquaria.cloud.moh.iais.action;

/*
 *author: yichen
 *date time:12/10/2019 12:59 PM
 *description:
 */

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
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
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.ChecklistConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.AdhocChecklistService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import com.ecquaria.cloud.moh.iais.service.HcsaChklService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Delegator(value = "adhocChecklistDelegator")
@Slf4j
public class AdhocChecklistDelegator {

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private AdhocChecklistService adhocChecklistService;

    @Autowired
    private HcsaChklService hcsaChklService;
    @Autowired
    private FillupChklistService fillupChklistService;

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
        ParamUtil.setSessionAttr(request, ChecklistConstant.ADHOC_ITEM_ACTION_FLAG, "Y");
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
        if (Optional.ofNullable(task).isPresent()) {
            String refNo = task.getRefNo();
            ApplicationViewDto appView = applicationViewService.getApplicationViewDtoByCorrId(refNo);
            if (Optional.ofNullable(appView).isPresent()) {
                ApplicationDto applicationDto = appView.getApplicationDto();
                AuditTrailHelper.auditFunctionWithAppNo(AuditTrailConsts.MODULE_INSPECTION,
                        AuditTrailConsts.FUNCTION_ADHOC_CHECKLIST, applicationDto.getApplicationNo());
                boolean needVehicle = fillupChklistService.checklistNeedVehicleSeparation(appView);
                inspectionChecklist = Optional.ofNullable(inspectionChecklist).orElseGet(() -> adhocChecklistService.getInspectionChecklist(applicationDto,
                        needVehicle));
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
        //log.debug("getNextStep" + bpc.getClass().getName());
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

        AdhocCheckListConifgDto adhocConfig = (AdhocCheckListConifgDto) ParamUtil.getSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR);
        if (Optional.ofNullable(adhocConfig).isPresent()){
            TaskDto task = (TaskDto)ParamUtil.getSessionAttr(bpc.request, "taskDto");
            if (Optional.ofNullable(task).isPresent()) {
                String correId = task.getRefNo();
                adhocConfig.setPremCorreId(correId);
            }

            adhocConfig.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            adhocConfig.setVersion(1);
            adhocConfig.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

            // Waiting for the event bus save
             ParamUtil.setSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, adhocConfig);

            ParamUtil.setRequestAttr(bpc.request, "preInspInitFlag", InspectionConstants.SWITCH_ACTION_BACK);

            ParamUtil.setSessionAttr(bpc.request, ChecklistConstant.ADHOC_ITEM_ACTION_FLAG, "N");
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
        AdhocCheckListConifgDto adhocConfig = getAdhocChecklistConfigInSession(request);
        boolean hasSampleItem = adhocChecklistService.hasSampleChecklistItem(adhocConfig);
        if (hasSampleItem){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("checklistItem", MessageUtil.getMessageDesc("CHKL_ERR047")));
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
        ParamUtil.setSessionAttr(bpc.request, ChecklistConstant.ADHOC_ITEM_ACTION_FLAG, "N");
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
        String value = ParamUtil.getMaskedString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);
        AdhocCheckListConifgDto config = getAdhocChecklistConfigInSession(request);
        List<AdhocChecklistItemDto> allAdhocItem = config.getAllAdhocItem();
        int idx = Integer.parseInt(value);
        adhocChecklistService.removeAdhocItem(allAdhocItem, idx);
        if(allAdhocItem.isEmpty()){
            ParamUtil.setSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, null);
        }else {
            ParamUtil.setSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, config);
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
        AdhocCheckListConifgDto config = getAdhocChecklistConfigInSession(request);
        adhocChecklistService.filterAdhocItem(searchParam, config);
        QueryHelp.setMainSql("hcsaconfig", "queryChecklistItem", searchParam);
        SearchResult<CheckItemQueryDto> searchResult = hcsaChklService.listChklItem(searchParam);
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

        List<ChecklistItemDto> itemList = hcsaChklService.listChklItemByItemId(Arrays.asList(checkBoxItemId));
        AdhocCheckListConifgDto config = getAdhocChecklistConfigInSession(request);
        adhocChecklistService.addSelectedChecklistItemToAdhocConfig(itemList, config);
        ParamUtil.setSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, config);
        ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
    }

    private AdhocCheckListConifgDto getAdhocChecklistConfigInSession(HttpServletRequest request) {
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

        AdhocCheckListConifgDto config = getAdhocChecklistConfigInSession(request);
        List<AdhocChecklistItemDto> itemList = config.getAllAdhocItem();
        Optional<AdhocChecklistItemDto> optional =
                itemList.stream().
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
            itemList.add(adhocItem);
            config.setAllAdhocItem(itemList);
            ParamUtil.setSessionAttr(request, AdhocChecklistConstants.INSPECTION_ADHOC_CHECKLIST_LIST_ATTR, config);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }else {
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("customItemError", MessageUtil.getMessageDesc("CHKL_ERR047")));
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
        }
       }
}
