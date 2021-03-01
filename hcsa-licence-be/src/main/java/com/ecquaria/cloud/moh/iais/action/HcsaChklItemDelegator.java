package com.ecquaria.cloud.moh.iais.action;

/*
 *author: yichen
 *date time:10/11/2019 2:16 PM
 *description:
 */

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.AuditTrailConsts;
import com.ecquaria.cloud.moh.iais.common.constant.IaisApiStatusCode;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.CheckItemQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ConfigExcelItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklSvcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ItemTemplate;
import com.ecquaria.cloud.moh.iais.common.dto.message.ErrorMsgContent;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MaskUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.ChecklistConstant;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.ChecklistHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.ecquaria.cloud.moh.iais.service.HcsaChklService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import sop.servlet.webflow.HttpHandler;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Delegator(value = "hcsaChklItemDelegator")
@Slf4j
public class HcsaChklItemDelegator {

    private HcsaChklService hcsaChklService;

    private FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(CheckItemQueryDto.class)
            .searchAttr(HcsaChecklistConstants.PARAM_CHECKLIST_ITEM_SEARCH)
            .resultAttr(HcsaChecklistConstants.PARAM_CHECKLIST_ITEM_RESULT)
            .sortField("status").sortType(SearchParam.ASCENDING).build();

    @Autowired
    public HcsaChklItemDelegator(HcsaChklService hcsaChklService){
        this.hcsaChklService = hcsaChklService;
    }

    @Autowired
    private SystemParamConfig paramConfig;

    /**
     * StartStep: startStep
     * @param bpc
     * @throws IllegalAccessException
     */
    public void startStep(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction(AuditTrailConsts.MODULE_CHECKLIST_MANAGEMENT, AuditTrailConsts.FUNCTION_CHECKLIST_ITEM);
        HttpServletRequest request = bpc.request;

        String curValhId = ParamUtil.getString(request, "currentValidateId");
        if (curValhId == null){
            ParamUtil.setSessionAttr(request, "routeToItemProcess", null);
            ParamUtil.setSessionAttr(request, "currentValidateId", null);
        }

        ParamUtil.setSessionAttr(request, "itemCheckboxReDisplay", null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_REGULATION_CLAUSE, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_REGULATION_DESC, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_STATUS, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_ANSWER_TYPE, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_RISK_LEVEL, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM_SEARCH, null);
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
        String crudAct = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        log.debug("The prepareSwitch end ...");
    }



    /**
     * @AutoStep: preUploadData
     * @param:
     * @return:
     * @author: yichen
     */
    public void preUploadData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        ParamUtil.setSessionAttr(request, "switchUploadPage", "Checklist Item Upload");
        String value = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_VALUE);
        ParamUtil.setSessionAttr(request, ChecklistConstant.ITEM_UPLOAD_MODE, value);
    }

    /**
     * @AutoStep: step3
     * @param:
     * @return:
     * @author: yichen
     */
    public void step3(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);

        String currentAction = mulReq.getParameter(IaisEGPConstant.CRUD_ACTION_TYPE);
        ParamUtil.setRequestAttr(request, IaisEGPConstant.CRUD_ACTION_TYPE, currentAction);
    }



    /**
     * @AutoStep: submitUploadData
     * @param:
     * @return:
     * @author: yichen
     */
    public void submitUploadData(BaseProcessClass bpc) throws Exception {
        HttpServletRequest request = bpc.request;
        MultipartHttpServletRequest mulReq = (MultipartHttpServletRequest) request.getAttribute(HttpHandler.SOP6_MULTIPART_REQUEST);
        MultipartFile file = mulReq.getFile("selectedFile");

        boolean fileHasError = ChecklistHelper.validateFile(request, file);
        if (fileHasError){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return;
        }

        File toFile = FileUtils.multipartFileToFile(file);
        try {
            List<ItemTemplate> itemTemplateList = FileUtils.transformToJavaBean(toFile, ItemTemplate.class);

            String uploadMode = (String) ParamUtil.getSessionAttr(request, ChecklistConstant.ITEM_UPLOAD_MODE);
            List<ErrorMsgContent> errorMsgContentList;
            if ("createData".equals(uploadMode)){
                errorMsgContentList = hcsaChklService.submitUploadItems(itemTemplateList);
            }else{
                errorMsgContentList = hcsaChklService.updateUploadItems(itemTemplateList);
            }

            ChecklistHelper.replaceErrorMsgContentMasterCode(errorMsgContentList);
            FileUtils.deleteTempFile(toFile);
            ParamUtil.setRequestAttr(request, "messageContent", errorMsgContentList);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
        }catch (Exception e){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "CHKL_ERR011"));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            log.error(e.getMessage(), e);
            return;
        }
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
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
            return;
        }

        try {
            doSubmitOrUpdate(request);
        }catch (IaisRuntimeException e){
           log.error(e.getMessage(), e);
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

        List<ChecklistItemDto> cklItem = (List<ChecklistItemDto>) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_CLONE_SESSION_ATTR);
        if (cklItem == null || cklItem.isEmpty()){
            Map<String,String> errorMap = new HashMap<>(1);
            errorMap.put("cloneRecords", "Please add item to be clone.");
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }

        cklItem.get(0).setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        boolean duplicationRecord = hcsaChklService.submitCloneItem(cklItem).booleanValue();
        if (duplicationRecord){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("cloneItemMsg", "CHKL_ERR007"));
            return;
        }

        ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.YES);

    }


    /**
     * for new item , it don't have item id , else update action
     * @param request
     */
    private void doSubmitOrUpdate(HttpServletRequest request){
        ChecklistItemDto cklItem =  requestChklItemDto(request);

        cklItem.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

        //Field calibration
        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR, cklItem);
        ValidationResult validationResult = WebValidationHelper.validateProperty(cklItem, "save");
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }

        //Business check
        cklItem.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        IaisApiResult<ChecklistItemDto> apiResult = hcsaChklService.saveChklItem(cklItem);
        if(apiResult.isHasError()){
            String errCode = apiResult.getErrorCode();
            if (IaisApiStatusCode.DUPLICATE_RECORD.getStatusCode().equals(errCode)){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("messageContent", "CHKL_ERR016"));
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            }
        }else {
            boolean isCreate = StringUtil.isEmpty(cklItem.getItemId()) ? true : false;
            if (isCreate){
                ParamUtil.setRequestAttr(request,"ackMsg", MessageUtil.dateIntoMessage("CHKL_ACK002"));
            }else {
                ParamUtil.setRequestAttr(request,"ackMsg", MessageUtil.dateIntoMessage("CHKL_ACK005"));
            }

            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }
    }

    /**
     * setup option to web page
     * @param request
     */
    private void preSelectOption(HttpServletRequest request){
        List<SelectOption> clauseSelect = IaisCommonUtils.genNewArrayList();

        List<HcsaChklSvcRegulationDto> regulations = hcsaChklService.listRegulationClause();
        for(HcsaChklSvcRegulationDto i : regulations){
            clauseSelect.add(new SelectOption(i.getId(), i.getClauseNo()));
        }

        ParamUtil.setSessionAttr(request, "regulationSelectDetail", (Serializable) regulations);
        ParamUtil.setRequestAttr(request, "clauseSelect", clauseSelect);
    }

    /**
     * AutoStep: viewCloneData
     * @param bpc
     * @throws IllegalAccessException
     */
    public void viewCloneData(BaseProcessClass bpc) throws IllegalAccessException {
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.setCheckboxStatus(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX, "itemCheckboxReDisplay");
        HashMap<String,String> checkedMap = (HashMap<String, String>) ParamUtil.getSessionAttr(request, "itemCheckboxReDisplay");
        if(checkedMap == null || checkedMap.size() <= 0){
            ParamUtil.setRequestAttr(request, "itemCheckboxReDisplay", null);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("cloneItemMsg", "CHKL_ERR045"));
            return;
        }

        int maxNum = paramConfig.getCloneChecklistMaxNum();
        if(checkedMap.size() > maxNum){
            ParamUtil.setSessionAttr(request, "itemCheckboxReDisplay", null);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("cloneItemMsg",
                    MessageUtil.replaceMessage("CHKL_ERR026", String.valueOf(maxNum), "number")));
            return;
        }

        ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        List<ChecklistItemDto> item = hcsaChklService.listChklItemByItemId(new ArrayList<>(checkedMap.keySet()));
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_CLONE_SESSION_ATTR, (Serializable) item);

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
        ChecklistItemDto cklItem = (ChecklistItemDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR);
        if (cklItem == null){
            cklItem = new ChecklistItemDto();
        }
        String itemId = ParamUtil.getMaskedString(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_ID);
        String regulationId = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_REGULATION_CLAUSE);
        String cklItemStr = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM);
        String riskLevel = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_RISK_LEVEL);
        String answerType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_ANSWER_TYPE);
        String status = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_STATUS);

        cklItem.setItemId(itemId);
        cklItem.setRegulationId(regulationId);
        cklItem.setRiskLevel(riskLevel);
        cklItem.setStatus(status);
        cklItem.setChecklistItem(cklItemStr);
        cklItem.setAnswerType(answerType);

        return cklItem;
    }

    /**
     * AutoStep: editCloneItem
     * @param bpc
     * @throws IllegalAccessException
     */
    public void editCloneItem(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        String curAct = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!HcsaChecklistConstants.ACTION_EDIT_CLONE_ITEM.equals(curAct)){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
            return;
        }

        ChecklistItemDto cklItem = (ChecklistItemDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR);
        if (cklItem == null){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }

        String cklItemStr = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM);
        String riskLevel = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_RISK_LEVEL);
        String answerType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_ANSWER_TYPE);

        cklItem.setAnswerType(answerType);
        cklItem.setRiskLevel(riskLevel);
        cklItem.setChecklistItem(cklItemStr);
        cklItem.setStatus(AppConsts.COMMON_STATUS_ACTIVE);

        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR, cklItem);
        ValidationResult validationResult = WebValidationHelper.validateProperty(cklItem, "clone");
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
        }else {
            List<ChecklistItemDto> chklItemDtos = (List<ChecklistItemDto>) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_CLONE_SESSION_ATTR);

            for(ChecklistItemDto it : chklItemDtos){
                if (it.getItemId().equals(cklItem.getItemId())){
                    it.setAnswerType(cklItem.getAnswerType());
                    it.setChecklistItem(cklItem.getChecklistItem());
                    it.setRegulationClause(cklItem.getRegulationClause());
                    it.setRegulationId(cklItem.getRegulationId());
                    it.setRiskLevel(cklItem.getRiskLevel());
                    it.setStatus(cklItem.getStatus());
                    it.setRegulationClauseNo(cklItem.getRegulationClauseNo());
                }
            }

            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_CLONE_SESSION_ATTR, (Serializable) chklItemDtos);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        }
    }

     /**
     * AutoStep: prepareItem
     * @param bpc
     * @throws IllegalAccessException
     */
    public void prepareChecklistItem(BaseProcessClass bpc) throws IllegalAccessException {
        HttpServletRequest request = bpc.request;
        String curAct = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);

        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_CLONE_SESSION_ATTR, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX, null);

        if(HcsaChecklistConstants.ACTION_CANCEL.equals(curAct)){
            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR, null);
            ParamUtil.setSessionAttr(request, "currentValidateId", null);
        }

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        //when configuring config, the same checklist item is not allowed
        filterItemInConfig(request, searchParam);

        QueryHelp.setMainSql("hcsaconfig", "listChklItem", searchParam);

        SearchResult searchResult = hcsaChklService.listChklItem(searchParam);
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

        ChecklistConfigDto curConf = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
        String[] ckbItemId = ParamUtil.getStrings(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX);
        if (ckbItemId == null || ckbItemId.length == 0){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }

        ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
    }

    private void loadSingleItemData(HttpServletRequest request){
        String itemId = ParamUtil.getMaskedString(request, HcsaChecklistConstants.CURRENT_MASK_ID);
        if(!StringUtil.isEmpty(itemId)){
            ChecklistItemDto cklItem = hcsaChklService.getChklItemById(itemId);

            if (cklItem.getStatus().equals(AppConsts.COMMON_STATUS_IACTIVE)){
                cklItem.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            }

            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR, cklItem);
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
     * AutoStep: changePage
     * @param bpc
     * @throws IllegalAccessException
     */
    public void changePage(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doPaging(searchParam, request);
        IaisEGPHelper.setCheckboxStatus(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX, "itemCheckboxReDisplay");
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM_SEARCH, searchParam);
    }



    /**
     * AutoStep: sortRecords
     * @param bpc
     * @throws IllegalAccessException
     */
    public void sortRecords(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doSorting(searchParam,bpc.request);
        IaisEGPHelper.setCheckboxStatus(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX, "itemCheckboxReDisplay");
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM_SEARCH, searchParam);
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

        String itemId = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_VALUE);
        List<ChecklistItemDto> cklItemList = (List<ChecklistItemDto>) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_CLONE_SESSION_ATTR);
        cklItemList.forEach(it -> {
            if (it.getItemId().equals(itemId)){
                ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR, it);
            }
        });
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
        String itemId = ParamUtil.getMaskedString(request, HcsaChecklistConstants.CURRENT_MASK_ID);
        if(!StringUtil.isEmpty(itemId)){
            boolean canInactive = hcsaChklService.inActiveItem(itemId);
            if (!canInactive){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("deleteItemMsg", "CHKL_ERR020"));
            }else {
                AuditTrailHelper.callSaveAuditTrailByOperation(AuditTrailConsts.OPERATION_INACTIVE_RECORD);
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
        String curAct = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!HcsaChecklistConstants.ACTION_SEARCH.equals(curAct)){
            return;
        }

        ChecklistItemDto cklItem = new ChecklistItemDto();
        String clause = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_REGULATION_CLAUSE);
        String desc = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_REGULATION_DESC);
        String cklItemStr = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM);
        String status = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_STATUS);
        String answerType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_ANSWER_TYPE);
        String riskLevel = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_RISK_LEVEL);

        cklItem.setRegulationClauseNo(clause);
        cklItem.setRegulationClause(desc);
        cklItem.setChecklistItem(cklItemStr);
        cklItem.setStatus(status);
        cklItem.setRiskLevel(riskLevel);
        cklItem.setAnswerType(answerType);

        IaisEGPHelper.setCheckboxStatus(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX, "itemCheckboxReDisplay");
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_REGULATION_CLAUSE, clause);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_REGULATION_DESC, desc);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM, cklItemStr);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_STATUS, status);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_ANSWER_TYPE, answerType);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_RISK_LEVEL, riskLevel);

        ValidationResult validationResult = WebValidationHelper.validateProperty(cklItem, "search");
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return;
        }

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);

        //if do search from config function, will filter
        filterItemInConfig(request, searchParam);

        if(!StringUtil.isEmpty(clause)){
            searchParam.addFilter(HcsaChecklistConstants.PARAM_REGULATION_CLAUSE, clause, true);
        }

        if(!StringUtil.isEmpty(desc)){
            searchParam.addFilter(HcsaChecklistConstants.PARAM_REGULATION_DESC, desc, true);
        }

        if(!StringUtil.isEmpty(cklItemStr)){
            searchParam.addFilter(HcsaChecklistConstants.PARAM_CHECKLIST_ITEM, cklItemStr, true);
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

    /**
     * @Author yichen
     * @Description: when create config and update config , the need filter selected item
     * @Date: 17:28 2020/4/2
     * @Param: []
     * @return:
     **/
    private void filterItemInConfig(HttpServletRequest request, SearchParam searchParam){
        String fromConfigPage = (String) ParamUtil.getSessionAttr(request, "routeToItemProcess");
        List<String> selectedItemIdToConfig = (List<String>) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.SELECTED_ITEM_IN_CONFIG);

        if (!StringUtils.isEmpty(fromConfigPage)){
            searchParam.addFilter("itemStatus", "CMSTAT001", true);
        }

        if (!IaisCommonUtils.isEmpty(selectedItemIdToConfig)){
            SqlHelper.builderNotInSql(searchParam, "item.id", HcsaChecklistConstants.SELECTED_ITEM_IN_CONFIG, selectedItemIdToConfig);
        }

    }

    @GetMapping(value = "checklist-item-clause")
    public @ResponseBody String getRegulationClauseById(HttpServletRequest request, HttpServletResponse response){
        String reglId = request.getParameter("regulationId");
        List<HcsaChklSvcRegulationDto> reglList = (List<HcsaChklSvcRegulationDto>) ParamUtil.getSessionAttr(request, "regulationSelectDetail");
        if (!StringUtil.isEmpty(reglId) && !IaisCommonUtils.isEmpty(reglList)){
            for (HcsaChklSvcRegulationDto i : reglList){
                if (reglId.equals(i.getId())){
                    return i.getClause();
                }
            }
        }
        return "-";
    }



    /**
    * @author: yichen 
    */
    @GetMapping(value = "checklist-item-file")
	public @ResponseBody void fileHandler(HttpServletRequest request, HttpServletResponse response){
	    log.debug(StringUtil.changeForLog("fileHandler start ...."));

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        searchParam.setPageNo(0);
        searchParam.setPageSize(Integer.MAX_VALUE);
        QueryHelp.setMainSql("hcsaconfig", "listChklItem", searchParam);
        SearchResult searchResult = hcsaChklService.listChklItem(searchParam);

        //master code to description
        List<CheckItemQueryDto> cklItemQueryList = searchResult.getRows();
        for (CheckItemQueryDto cklItemQuery : cklItemQueryList){
            cklItemQuery.setAnswerType(MasterCodeUtil.getCodeDesc(cklItemQuery.getAnswerType()));
            String riskLvl = MasterCodeUtil.getCodeDesc(cklItemQuery.getRiskLevel());
            cklItemQuery.setRiskLevel("".equals(riskLvl) ? "-" : riskLvl);
            cklItemQuery.setStatus(MasterCodeUtil.getCodeDesc(cklItemQuery.getStatus()));
        }

        boolean blockExcel = false;
        if (IaisCommonUtils.isEmpty(cklItemQueryList)){
            blockExcel = true;
        }

        File file = null;
        try {
            file = ExcelWriter.writerToExcel(cklItemQueryList, CheckItemQueryDto.class, null, "Checklist_Items_Template", blockExcel, true);
        } catch (Exception e) {
            log.error("=======>fileHandler error >>>>>", e);
        }

        if(file != null){
            try {
                FileUtils.writeFileResponseContent(response, file);
            } catch (IOException e) {
                log.debug(e.getMessage());
            }finally {
                FileUtils.deleteTempFile(file);
            }
        }

        log.debug(StringUtil.changeForLog("fileHandler end ...."));
    }

    @RequestMapping(value = "/checklist-item/setup-checkbox", method = RequestMethod.POST)
    public @ResponseBody void setUpCheckBox(HttpServletRequest request) {
        String[] sCkBox =  request.getParameterValues("selectedCheckBoxItem");
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX, sCkBox);
    }

    /**
     * @Author yichen
     * @Date: 10:36 2020/6/4
     **/
    public void exportItemToConfigTemplate(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        log.debug(StringUtil.changeForLog("exportConfigTemplate start ...."));

        try {
            File inputFile = ResourceUtils.getFile("classpath:template/Checklist_Config_Upload_Template.xlsx");
            String[] checked = (String[]) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX);
            if (checked == null || checked.length <= 0) {
                FileUtils.writeFileResponseProcessContent(request, inputFile);
                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
                return;
            }

            List<String> ids = IaisCommonUtils.genNewArrayList();
            for (String i : checked){
                ids.add(MaskUtil.unMaskValue(HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX, i));
            }

            if (inputFile.exists() && inputFile.isFile()) {
                List<ChecklistItemDto> item = hcsaChklService.listChklItemByItemId(ids);
                List<ConfigExcelItemDto> uploadTemplate = IaisCommonUtils.genNewArrayList();

                for (ChecklistItemDto i : item) {
                    ConfigExcelItemDto template = new ConfigExcelItemDto();
                    template.setItemId(i.getItemId());
                    template.setChecklistItem(i.getChecklistItem());
                    uploadTemplate.add(template);
                }

                Map<Integer, List<Integer>> unlockMap = IaisEGPHelper.generateUnlockMap(8, 8);

                File outputFile = ExcelWriter.writerToExcel(uploadTemplate, ConfigExcelItemDto.class, inputFile, "Checklist_Config_Upload_Template", true, false, unlockMap);

                FileUtils.writeFileResponseProcessContent(request, outputFile);

                FileUtils.deleteTempFile(outputFile);

                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            }

        } catch (Exception e) {
           log.error("exportItemToConfigTemplate has error ", e);
        }

    }
}
