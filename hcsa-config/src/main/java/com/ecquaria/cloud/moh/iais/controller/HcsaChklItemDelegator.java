package com.ecquaria.cloud.moh.iais.controller;

/*
 *author: yichen
 *date time:10/11/2019 2:16 PM
 *description:
 */

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChklConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklItemQueryDto;
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
import javax.servlet.http.HttpServletResponse;
import java.util.*;

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
        log.info("=======>>>>>startStep>>>>>>>>>>>>>>>>ChecklistDelegator");
        AuditTrailHelper.auditFunction("Checklist Management", "Checklist Management");
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, HcsaChklConstants.class);
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
        if(!HcsaChklConstants.ACTION_SAVE_ITEM.equals(currentAction)){
            return;
        }
/*
        String itemId = ParamUtil.getMaskedString(request, HcsaChklConstants.PARAM_CHKL_ITEM_ID);*/
        String itemId = ParamUtil.getString(request, HcsaChklConstants.PARAM_CHKL_ITEM_ID);
        String clause = ParamUtil.getString(request, HcsaChklConstants.PARAM_REGULATION_CLAUSE);
        String desc = ParamUtil.getString(request, HcsaChklConstants.PARAM_REGULATION_DESC);
        String regulationId = ParamUtil.getString(request, HcsaChklConstants.PARAM_CHKL_REGULATION_ID);
        String chklItem = ParamUtil.getString(request, HcsaChklConstants.PARAM_CHECKLIST_ITEM);
        String riskLevel = ParamUtil.getString(request, HcsaChklConstants.PARAM_RISK_LEVEL);
        String answerType = ParamUtil.getString(request, HcsaChklConstants.PARAM_ANSWER_TYPE);
        String status = ParamUtil.getString(request, HcsaChklConstants.PARAM_STATUS);

        /*AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();*/

        AuditTrailDto auditTrailDto = new AuditTrailDto();
        auditTrailDto.setMohUserId(AppConsts.USER_ID_ANONYMOUS);
        HcsaChklItemDto itemDto = new HcsaChklItemDto();

        if(itemId == null || itemId.isEmpty()){
            itemDto.setUpdate(false);
            log.info("Can not find item id, it will go to insert reocrd" + itemId);
        }else{
            log.info("item id, it will go to update reocrd" + itemId);
            itemDto.setUpdate(true);
        }

        itemDto.setItemId(itemId);
        itemDto.setRegulationId(regulationId);
        itemDto.setRegulationClauseNo(clause);
        itemDto.setRegulationClause(desc);
        itemDto.setRiskLevel(riskLevel);
        itemDto.setStatus(status);
        itemDto.setChecklistItem(chklItem);
        itemDto.setAnswerType(answerType);
        itemDto.setAuditTrailDto(auditTrailDto);

        ValidationResult validationResult = WebValidationHelper.validateProperty(itemDto, "save");
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMAP,errorMap);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
        }else {
            Map<String,String> successMap = new HashMap<>();
            successMap.put("save item","suceess");
            hcsaChklService.saveChklItem(itemDto);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMAP,successMap);
        }
    }



    /**
     * setup option to web page
     * @param request
     */
    private void preSelectOption(HttpServletRequest request){
        List<SelectOption> clauseSelect = new ArrayList<>();
        clauseSelect.add(new SelectOption("R10(2)G.19", "R10(2)G.19"));
        ParamUtil.setRequestAttr(request, "clauseSelect", clauseSelect);

    }

    /**
     * AutoStep: loadCloneChklItems
     * @param bpc
     * @throws IllegalAccessException
     */
    public void loadCloneChklItems(BaseProcessClass bpc) throws IllegalAccessException {
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(HcsaChklConstants.ACTION_CANCEL.equals(currentAction)){
            IaisEGPHelper.clearSessionAttr(request, HcsaChklItemQueryDto.class);
        }

        String[] values = ParamUtil.getStrings(request, "checkboxItemId");
        if (values == null | values.length == 0){
            return;
        }

        List<String> itemIds = Arrays.asList(values);
        List<HcsaChklItemDto> chklItemDtos = hcsaChklService.listChklItemByItemId(itemIds);

        ParamUtil.setRequestAttr(request, HcsaChklConstants.PARAM_CHECKLIST_ITEM_RESULT, chklItemDtos);
    }

    /**
     * AutoStep: prepareEditCloneChklItem
     * @param bpc
     * @throws IllegalAccessException
     */
    public void prepareEditCloneChklItem(BaseProcessClass bpc) throws IllegalAccessException {
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!HcsaChklConstants.ACTION_PREPARE_EDIT.equals(currentAction)){
            return;
        }
        preSelectOption(request);

        List<HcsaChklItemDto> chklItemDtos = (List<HcsaChklItemDto>) ParamUtil.getRequestAttr(request, HcsaChklConstants.PARAM_CHECKLIST_ITEM_RESULT);
        String needEditId = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
        chklItemDtos.stream().forEach(i -> {
            if(i.getItemId().equals(needEditId)){
                ParamUtil.setRequestAttr(request, HcsaChklConstants.CHECKLIST_ITEM_REQUEST_DTO, i);
            };
        });
    }

    /**
     * AutoStep: editCloneChklItem
     * @param bpc
     * @throws IllegalAccessException
     */
    public void editCloneChklItem(BaseProcessClass bpc) throws IllegalAccessException {
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!HcsaChklConstants.ACTION_PREPARE_EDIT.equals(currentAction)){
            return;
        }
        preSelectOption(request);

    }

    /**
     * AutoStep: prepareItem
     * @param bpc
     * @throws IllegalAccessException
     */
    public void prepareItem(BaseProcessClass bpc) throws IllegalAccessException {
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(HcsaChklConstants.ACTION_CANCEL.equals(currentAction)){
            IaisEGPHelper.clearSessionAttr(request, HcsaChklItemQueryDto.class);
        }

        filterParameter.setClz(HcsaChklItemQueryDto.class);
        filterParameter.setSearchAttr(HcsaChklConstants.PARAM_CHECKLIST_ITEM_SEARCH);
        filterParameter.setResultAttr(HcsaChklConstants.PARAM_CHECKLIST_ITEM_RESULT);
        filterParameter.setSortField("item_id");

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        QueryHelp.setMainSql("hcsaconfig", "listChklItem", searchParam);

        SearchResult searchResult = hcsaChklService.listChklItem(searchParam);

        ParamUtil.setSessionAttr(request, HcsaChklConstants.PARAM_HCSA_SERVICE_SEARCH, searchParam);
        ParamUtil.setRequestAttr(request, HcsaChklConstants.PARAM_CHECKLIST_ITEM_RESULT, searchResult);
    }

    /**
     * AutoStep: prepareAdd
     * @param bpc
     * @throws IllegalAccessException
     */
    public void prepareAdd(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!HcsaChklConstants.ACTION_PREPARE_ADD.equals(currentAction)){
            return;
        }
        preSelectOption(request);
        ParamUtil.setRequestAttr(request,"btnTag","Y");
    }




    /**
     * AutoStep: prepareEdit
     * @param bpc
     * @throws IllegalAccessException
     */
    public void prepareEdit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        HttpServletResponse response = bpc.response;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!HcsaChklConstants.ACTION_PREPARE_EDIT.equals(currentAction)){
            return;
        }
        preSelectOption(request);
        String itemId = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
        ParamUtil.setRequestAttr(request,"btnTag","N");
        if(!StringUtil.isEmpty(itemId)){
            HcsaChklItemDto itemDto = hcsaChklService.getChklItemById(itemId);
            ParamUtil.setRequestAttr(request, HcsaChklConstants.CHECKLIST_ITEM_REQUEST_DTO, itemDto);
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
        if(!HcsaChklConstants.ACTION_SEARCH.equals(currentAction)){
            return;
        }

        HcsaChklItemQueryDto itemQueryDto = new HcsaChklItemQueryDto();

        String clause = ParamUtil.getString(request, HcsaChklConstants.PARAM_REGULATION_CLAUSE);
        String desc = ParamUtil.getString(request, HcsaChklConstants.PARAM_REGULATION_DESC);
        String chklItem = ParamUtil.getString(request, HcsaChklConstants.PARAM_CHECKLIST_ITEM);
        String status = ParamUtil.getString(request, HcsaChklConstants.PARAM_STATUS);
        String answerType = ParamUtil.getString(request, HcsaChklConstants.PARAM_ANSWER_TYPE);
        String riskLevel = ParamUtil.getString(request, HcsaChklConstants.PARAM_RISK_LEVEL);

        itemQueryDto.setRegulationClauseNo(clause);
        itemQueryDto.setRegulationClause(desc);
        itemQueryDto.setChecklistItem(chklItem);
        itemQueryDto.setStatus(status);
        itemQueryDto.setRiskLevel(riskLevel);
        itemQueryDto.setAnswerType(answerType);

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);

        if(!StringUtil.isEmpty(clause)){
            searchParam.addFilter(HcsaChklConstants.PARAM_REGULATION_CLAUSE, clause, true);
        }

        if(!StringUtil.isEmpty(desc)){
            searchParam.addFilter(HcsaChklConstants.PARAM_REGULATION_DESC, desc, true);
        }

        if(!StringUtil.isEmpty(chklItem)){
            searchParam.addFilter(HcsaChklConstants.PARAM_CHECKLIST_ITEM, chklItem, true);
        }

        if(!StringUtil.isEmpty(riskLevel)){
            searchParam.addFilter(HcsaChklConstants.PARAM_RISK_LEVEL, riskLevel, true);
        }

        if(!StringUtil.isEmpty(answerType)){
            searchParam.addFilter(HcsaChklConstants.PARAM_ANSWER_TYPE, answerType, true);
        }

        if(!StringUtil.isEmpty(status)){
            searchParam.addFilter(HcsaChklConstants.PARAM_STATUS, status, true);
        }

    }

}
