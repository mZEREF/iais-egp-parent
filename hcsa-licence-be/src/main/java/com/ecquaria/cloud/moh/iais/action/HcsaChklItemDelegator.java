package com.ecquaria.cloud.moh.iais.action;

/*
 *author: yichen
 *date time:10/11/2019 2:16 PM
 *description:
 */

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.CheckItemQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.HcsaChklService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Delegator(value = "hcsaChklItemDelegator")
@Slf4j
public class HcsaChklItemDelegator {

    private HcsaChklService hcsaChklService;
    private FilterParameter filterParameter;

    @Autowired
    public HcsaChklItemDelegator(HcsaChklService hcsaChklService, FilterParameter filterParameter){
        this.hcsaChklService = hcsaChklService;
        this.filterParameter = filterParameter;
    }

    /**
     * StartStep: startStep
     * @param bpc
     * @throws IllegalAccessException
     */
    public void startStep(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction("Checklist Management", "Checklist Management");
        HttpServletRequest request = bpc.request;


        String currentValidateId = ParamUtil.getString(request, "currentValidateId");
        if (currentValidateId == null){
            ParamUtil.setSessionAttr(request, "currentValidateId", null);
        }

        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_CLONE_SESSION_ATTR, null);

    }

    /**
     * @AutoStep: prepareSwitch
     * @param:
     * @return:
     * @author: yichen
     */
    public void prepareSwitch(BaseProcessClass bpc) {
        log.debug("The prepareSwitch start ...");
        String crudAction = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        log.debug("*******************crudAction-->:" + crudAction);
        log.debug("The prepareSwitch end ...");
    }

    /**
     * @AutoStep: saveChecklistItem
     * @param:
     * @return:
     * @author: yichen
     */
    public void saveChecklistItem(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!HcsaChecklistConstants.ACTION_SAVE_ITEM.equals(currentAction)){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
            return;
        }

        try {
            doSubmitOrUpdate(request);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
        }catch (IaisRuntimeException e){
            throw new IaisRuntimeException(e.getMessage());
        }

    }

    /**
     * @AutoStep: submitCloneItem
     * @param:
     * @return:
     * @author: yichen
     */
    public void submitCloneItem(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        List<ChecklistItemDto> chklItemDtos = (List<ChecklistItemDto>) request.getSession().getAttribute("cloneItems");
        if (chklItemDtos == null || chklItemDtos.isEmpty()){
            Map<String,String> errorMap = new HashMap<>(1);
            errorMap.put("cloneRecords", "Please add item to be clone.");
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
        }else {
            hcsaChklService.submitCloneItem(chklItemDtos);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
        }

    }


    /**
     * for new item , it don't have item id , else update action
     * @param request
     */
    private void doSubmitOrUpdate(HttpServletRequest request){
        ChecklistItemDto itemDto =  requestChklItemDto(request);

        AuditTrailDto auditTrailDto = new AuditTrailDto();
        auditTrailDto.setMohUserId(AppConsts.USER_ID_ANONYMOUS);

        itemDto.setAuditTrailDto(auditTrailDto);

        ValidationResult validationResult = WebValidationHelper.validateProperty(itemDto, "save");
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
        }else {
            hcsaChklService.saveChklItem(itemDto);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");

        }

    }

    /**
     * setup option to web page
     * @param request
     */
    private void preSelectOption(HttpServletRequest request){
        List<SelectOption> clauseSelect = new ArrayList<>();


        List<String> strings = hcsaChklService.listRegulationClauseNo();
        for(String s : strings){
            clauseSelect.add(new SelectOption(s, s));
        }

        ParamUtil.setRequestAttr(request, "clauseSelect", clauseSelect);

    }

    /**
     * AutoStep: viewCloneData
     * @param bpc
     * @throws IllegalAccessException
     */
    public void viewCloneData(BaseProcessClass bpc) throws IllegalAccessException {
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);

        String[] checkBoxItemId = ParamUtil.getStrings(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX);
        if(checkBoxItemId == null || checkBoxItemId.length <= 0){
            return;
        }

        List<ChecklistItemDto> chklItemDtos = hcsaChklService.listChklItemByItemId(Arrays.asList(checkBoxItemId));
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_CLONE_SESSION_ATTR, (Serializable) chklItemDtos);

    }


    /**
     * AutoStep: prepareChecklistItemInfo
     * @param bpc
     * @throws IllegalAccessException
     */
    public void prepareChecklistItemInfo(BaseProcessClass bpc) throws IllegalAccessException {
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);

    }

    /**
     * AutoStep: addChecklistItemNextAction
     * @param bpc
     * @throws IllegalAccessException
     */
    public void addChecklistItemNextAction(BaseProcessClass bpc) throws IllegalAccessException {
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);

    }



    /**
    * @description: get request chkl item dto
    * @param: 
    * @return: 
    * @author: yichen 
    */
    private ChecklistItemDto requestChklItemDto(HttpServletRequest request){
        ChecklistItemDto itemDto = new ChecklistItemDto();
        String itemId = ParamUtil.getMaskedString(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_ID);
        String clause = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_REGULATION_CLAUSE);
        String desc = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_REGULATION_DESC);
        String regulationId = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CHKL_REGULATION_ID);
        String chklItem = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM);
        String riskLevel = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_RISK_LEVEL);
        String answerType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_ANSWER_TYPE);
        String status = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_STATUS);

        itemDto.setItemId(itemId);
        itemDto.setRegulationId(regulationId);
        itemDto.setRegulationClauseNo(clause);
        itemDto.setRegulationClause(desc);
        itemDto.setRiskLevel(riskLevel);
        itemDto.setStatus(status);
        itemDto.setChecklistItem(chklItem);
        itemDto.setAnswerType(answerType);

        return itemDto;
    }


    /**
     * AutoStep: editCloneItem
     * @param bpc
     * @throws IllegalAccessException
     */
    public void editCloneItem(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!HcsaChecklistConstants.ACTION_EDIT_CLONE_ITEM.equals(currentAction)){
            return;
        }

        ChecklistItemDto itemDto = requestChklItemDto(request);

        ValidationResult validationResult = WebValidationHelper.validateProperty(itemDto, "save");
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
        }else {
            Map<String,String> successMap = new HashMap<>(16);
            successMap.put("edit item","suceess");
            List<ChecklistItemDto> chklItemDtos = (List<ChecklistItemDto>) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_CLONE_SESSION_ATTR);

            for(ChecklistItemDto it : chklItemDtos){
                if (it.getItemId().equals(itemDto.getItemId())){
                    it.setAnswerType(itemDto.getAnswerType());
                    it.setChecklistItem(itemDto.getChecklistItem());
                    it.setRegulationClause(itemDto.getRegulationClause());
                    it.setRegulationId(itemDto.getRegulationId());
                    it.setRiskLevel(itemDto.getRiskLevel());
                    it.setStatus(itemDto.getStatus());
                    it.setRegulationClauseNo(itemDto.getRegulationClauseNo());
                }

            }

            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_CLONE_SESSION_ATTR, (Serializable) chklItemDtos);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMAP,successMap);
        }

    }

     /**
     * AutoStep: prepareItem
     * @param bpc
     * @throws IllegalAccessException
     */
    public void prepareChecklistItem(BaseProcessClass bpc) throws IllegalAccessException {
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);

        if(HcsaChecklistConstants.ACTION_CANCEL.equals(currentAction)){
            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR, null);
            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_CLONE_SESSION_ATTR, null);
            ParamUtil.setSessionAttr(request, "currentValidateId", null);
        }

        filterParameter.setClz(CheckItemQueryDto.class);
        filterParameter.setSearchAttr(HcsaChecklistConstants.PARAM_CHECKLIST_ITEM_SEARCH);
        filterParameter.setResultAttr(HcsaChecklistConstants.PARAM_CHECKLIST_ITEM_RESULT);
        filterParameter.setSortField("item_id");

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        QueryHelp.setMainSql("hcsaconfig", "listChklItem", searchParam);

        SearchResult searchResult = hcsaChklService.listChklItem(searchParam);

        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_HCSA_SERVICE_SEARCH, searchParam);
        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM_RESULT, searchResult);

    }

    /**
     * AutoStep: configToChecklist
     * @param bpc
     * @throws IllegalAccessException
     * description: Verify that the added id already exists for the same section
     */
    public void configToChecklist(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        ChecklistConfigDto currentConfig = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
        String[] checkBoxItemId = ParamUtil.getStrings(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX);
        if (checkBoxItemId == null || checkBoxItemId.length == 0){
            return;
        }

        List<ChecklistSectionDto> sectionDtos = currentConfig.getSectionDtos();
        String currentValidateId = (String) ParamUtil.getSessionAttr(request, "currentValidateId");
        for (ChecklistSectionDto currentSection : sectionDtos){
            if (currentValidateId.equals(currentSection.getId())){
                List<ChecklistItemDto> checklistItemDtos = currentSection.getChecklistItemDtos();
                if (checklistItemDtos == null){
                    break;
                }

                for (ChecklistItemDto chkl : checklistItemDtos){
                    for (String s : checkBoxItemId){
                        if (chkl.getItemId().equals(s)){
                            Map<String,String> errorMap = new HashMap<>();
                            errorMap.put("configCustomValidation", "CHKL_ERR007");

                            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID,"N");
                            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                            return;
                        }
                    }
                }

            }
        }

        ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID,"Y");
    }

    private void loadSingleItemData(HttpServletRequest request){
        String itemId = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_VALUE);
        if(!StringUtil.isEmpty(itemId)){
            ChecklistItemDto itemDto = hcsaChklService.getChklItemById(itemId);
            ParamUtil.setRequestAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR, itemDto);
        }
    }

    /**
     * AutoStep: prepareAddItem
     * @param bpc
     * @throws IllegalAccessException
     */
    public void prepareAddItem(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        //for jsp action button
        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.DISPLAY_BUTTON, "SubmitButton");
        preSelectOption(request);
    }

    /**
     * AutoStep: prepareEditItem
     * @param bpc
     * @throws IllegalAccessException
     */
    public void prepareEditItem(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        //for jsp action button
        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.DISPLAY_BUTTON, "UpdateButton");
        preSelectOption(request);
        loadSingleItemData(request);
    }

    /**
     * AutoStep: prepareCloneItem
     * @param bpc
     * @throws IllegalAccessException
     */
    public void prepareCloneItem(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        ParamUtil.setRequestAttr(request,"btnTag","CloneButton");
        preSelectOption(request);
        loadSingleItemData(request);
    }

    /**
     * AutoStep: switchClone
     * @param bpc
     * @throws IllegalAccessException
     */
    public void switchClone(BaseProcessClass bpc){
        // do nothing.
    }

    /**
     * AutoStep: cancelClone
     * @param bpc
     * @throws IllegalAccessException
     */
    public void cancelClone(BaseProcessClass bpc){
        // do nothing.
    }


    /**
     * AutoStep: deleteChecklistItem
     * @param bpc
     * @throws IllegalAccessException
     */
    public void deleteChecklistItem(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String itemId = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_VALUE);
        if(!StringUtil.isEmpty(itemId)){
            boolean canInactive = hcsaChklService.inActiveItem(itemId);
            if (!canInactive){
                Map<String,String> errorMap = new HashMap<>(1);
                errorMap.put("deleteItemMsg", "Delete Item faild, this item is being used.");
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            }
        }
    }

    /**
     * @AutoStep: doSearch
     * @param:
     * @return:
     * @author: yichen
     */
    public void doSearch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!HcsaChecklistConstants.ACTION_SEARCH.equals(currentAction)){
            return;
        }

        CheckItemQueryDto itemQueryDto = new CheckItemQueryDto();

        String clause = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_REGULATION_CLAUSE);
        String desc = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_REGULATION_DESC);
        String chklItem = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM);
        String status = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_STATUS);
        String answerType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_ANSWER_TYPE);
        String riskLevel = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_RISK_LEVEL);

        itemQueryDto.setRegulationClauseNo(clause);
        itemQueryDto.setRegulationClause(desc);
        itemQueryDto.setChecklistItem(chklItem);
        itemQueryDto.setStatus(status);
        itemQueryDto.setRiskLevel(riskLevel);
        itemQueryDto.setAnswerType(answerType);

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);

        if(!StringUtil.isEmpty(clause)){
            searchParam.addFilter(HcsaChecklistConstants.PARAM_REGULATION_CLAUSE, clause, true);
        }

        if(!StringUtil.isEmpty(desc)){
            searchParam.addFilter(HcsaChecklistConstants.PARAM_REGULATION_DESC, desc, true);
        }

        if(!StringUtil.isEmpty(chklItem)){
            searchParam.addFilter(HcsaChecklistConstants.PARAM_CHECKLIST_ITEM, chklItem, true);
        }

        if(!StringUtil.isEmpty(riskLevel)){
            searchParam.addFilter(HcsaChecklistConstants.PARAM_RISK_LEVEL, riskLevel, true);
        }

        if(!StringUtil.isEmpty(answerType)){
            searchParam.addFilter(HcsaChecklistConstants.PARAM_ANSWER_TYPE, answerType, true);
        }

        if(!StringUtil.isEmpty(status)){
            searchParam.addFilter(HcsaChecklistConstants.PARAM_STATUS, status, true);
        }

    }

}
