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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemExcel;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigExcel;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklSvcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.ErrorMsgContent;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

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

        String currentValidateId = ParamUtil.getString(request, "currentValidateId");
        if (currentValidateId == null){
            ParamUtil.setSessionAttr(request, "routeToItemProcess", null);
            ParamUtil.setSessionAttr(request, "currentValidateId", null);
        }

        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECK_BOX_REDISPLAY, null);
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
        MultipartFile mulReqFile = mulReq.getFile("selectedFile");

        boolean fileHasError = ChecklistHelper.validateFile(request, mulReqFile);
        if (fileHasError){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return;
        }

        File file = FileUtils.multipartFileToFile(mulReqFile);
        try {
            List<ChecklistItemExcel> itemTemplateList = FileUtils.transformToJavaBean(file, ChecklistItemExcel.class);
            String uploadMode = (String) ParamUtil.getSessionAttr(request, ChecklistConstant.ITEM_UPLOAD_MODE);
            List<ErrorMsgContent> emcList;
            if ("createData".equals(uploadMode)){
                emcList = hcsaChklService.submitUploadItems(itemTemplateList);
            }else{
                emcList = hcsaChklService.updateUploadItems(itemTemplateList);
            }

            ChecklistHelper.replaceErrorMsgContentMasterCode(emcList);
            FileUtils.deleteTempFile(file);
            ParamUtil.setRequestAttr(request, "messageContent", emcList);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
        }catch (Exception e){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(ChecklistConstant.FILE_UPLOAD_ERROR, "CHKL_ERR011"));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            log.error(e.getMessage(), e);
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
        String action = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!HcsaChecklistConstants.ACTION_SAVE_ITEM.equals(action)){
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

        List<ChecklistItemDto> cloneItem = (List<ChecklistItemDto>) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_CLONE_SESSION_ATTR);
        if (IaisCommonUtils.isEmpty(cloneItem)){
            Map<String,String> errorMap = new HashMap<>(1);
            errorMap.put("cloneRecords", "Please add item to be clone.");
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }

        boolean isDuplicate = hcsaChklService.submitCloneItem(cloneItem).booleanValue();
        if (isDuplicate){
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
        ChecklistItemDto item = (ChecklistItemDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR);
        requestChklItemDto(request, item);
        //Field calibration
        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR, item);
        ValidationResult vResult = WebValidationHelper.validateProperty(item, "save");
        if(vResult != null && vResult.isHasErrors()){
            Map<String,String> errorMap = vResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }

        //Business check
        IaisApiResult<ChecklistItemDto> result = hcsaChklService.saveChklItem(item);
        if(result.isHasError()){
            String errCode = result.getErrorCode();
            if (IaisApiStatusCode.DUPLICATE_RECORD.getStatusCode().equals(errCode)){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("messageContent", "CHKL_ERR016"));
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            }
        }else {
            boolean isCreate = StringUtil.isEmpty(item.getItemId()) ? true : false;
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
        HashSet<String> set = (HashSet<String>) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECK_BOX_REDISPLAY);
        if(IaisCommonUtils.isEmpty(set)){
            ParamUtil.setRequestAttr(request, HcsaChecklistConstants.CHECK_BOX_REDISPLAY, null);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("cloneItemMsg", "CHKL_ERR045"));
            return;
        }

        int maxNum = paramConfig.getCloneChecklistMaxNum();
        if(set.size() > maxNum){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("cloneItemMsg",
                    MessageUtil.replaceMessage("CHKL_ERR026", String.valueOf(maxNum), "number")));
            return;
        }

        ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
        List<ChecklistItemDto> item = hcsaChklService.listChklItemByItemId(new ArrayList<>(set));
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_CLONE_SESSION_ATTR, (Serializable) item);

    }

    /**
     * AutoStep: addChecklistItemNextAction
     * @param bpc
     * @throws IllegalAccessException
     */
    public void addChecklistItemNextAction(BaseProcessClass bpc) throws IllegalAccessException {
        //....
    }

    /**
    * @description: get request chkl item dto
    * @param: 
    * @return: 
    * @author: yichen 
    */
    private void requestChklItemDto(HttpServletRequest request, ChecklistItemDto item){
        item = Optional.ofNullable(item).orElseGet(() -> new ChecklistItemDto());

        String itemId = ParamUtil.getMaskedString(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_ID);
        String regulationId = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_REGULATION_CLAUSE);
        String cklItemStr = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM);
        String riskLevel = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_RISK_LEVEL);
        String answerType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_ANSWER_TYPE);
        String status = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_STATUS);
        item.setItemId(itemId);
        item.setRegulationId(regulationId);
        item.setRiskLevel(riskLevel);
        item.setStatus(status);
        item.setChecklistItem(cklItemStr);
        item.setAnswerType(answerType);
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

        ChecklistItemDto item = (ChecklistItemDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR);
        if (item == null){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }

        String cklItemStr = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM);
        String riskLevel = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_RISK_LEVEL);
        String answerType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_ANSWER_TYPE);

        item.setAnswerType(answerType);
        item.setRiskLevel(riskLevel);
        item.setChecklistItem(cklItemStr);
        item.setStatus(AppConsts.COMMON_STATUS_ACTIVE);

        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR, item);
        ValidationResult vResult = WebValidationHelper.validateProperty(item, "clone");
        if(vResult != null && vResult.isHasErrors()){
            Map<String,String> errorMap = vResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
        }else {
            List<ChecklistItemDto> itemList = (List<ChecklistItemDto>) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_CLONE_SESSION_ATTR);
            for(ChecklistItemDto it : itemList){
                if (it.getItemId().equals(item.getItemId())){
                    it.setAnswerType(item.getAnswerType());
                    it.setChecklistItem(item.getChecklistItem());
                    it.setRegulationClause(item.getRegulationClause());
                    it.setRegulationId(item.getRegulationId());
                    it.setRiskLevel(item.getRiskLevel());
                    it.setStatus(item.getStatus());
                    it.setRegulationClauseNo(item.getRegulationClauseNo());
                }
            }
            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_CLONE_SESSION_ATTR, (Serializable) itemList);
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
        String action = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);

        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_CLONE_SESSION_ATTR, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX, null);

        if(HcsaChecklistConstants.ACTION_CANCEL.equals(action)){
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
        String[] checked = ParamUtil.getStrings(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX);
        if (checked == null || checked.length == 0){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }
        ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
    }

    private void loadSingleItemData(HttpServletRequest request){
        String itemId = ParamUtil.getMaskedString(request, HcsaChecklistConstants.CURRENT_MASK_ID);
        if(StringUtil.isNotEmpty(itemId)){
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

        List<ChecklistItemDto> itemList = (List<ChecklistItemDto>) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_CLONE_SESSION_ATTR);
        log.info("HcsaChklItemDelegator [prepareCloneItem] START itemList {}", JsonUtil.parseToJson(itemList));
        Optional.ofNullable(itemList)
                 .orElseGet(() -> new ArrayList<>())
                .forEach(it -> {
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
        if(StringUtil.isNotEmpty(itemId)){
            boolean success = hcsaChklService.inActiveItem(itemId);
            if (success){
                AuditTrailHelper.callSaveAuditTrailByOperation(AuditTrailConsts.OPERATION_INACTIVE_RECORD);
            }else {
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("deleteItemMsg", "CHKL_ERR020"));
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
        String action = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!HcsaChecklistConstants.ACTION_SEARCH.equals(action)){
            return;
        }

        ChecklistItemDto checklistItem = new ChecklistItemDto();
        String clause = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_REGULATION_CLAUSE);
        String desc = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_REGULATION_DESC);
        String cklItemStr = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM);
        String status = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_STATUS);
        String answerType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_ANSWER_TYPE);
        String riskLevel = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_RISK_LEVEL);

        checklistItem.setRegulationClauseNo(clause);
        checklistItem.setRegulationClause(desc);
        checklistItem.setChecklistItem(cklItemStr);
        checklistItem.setStatus(status);
        checklistItem.setRiskLevel(riskLevel);
        checklistItem.setAnswerType(answerType);

        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_REGULATION_CLAUSE, clause);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_REGULATION_DESC, desc);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM, cklItemStr);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_STATUS, status);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_ANSWER_TYPE, answerType);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_RISK_LEVEL, riskLevel);

        ValidationResult vResult = WebValidationHelper.validateProperty(checklistItem, "search");
        if(vResult != null && vResult.isHasErrors()){
            Map<String,String> errorMap = vResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return;
        }

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);

        //if do search from config function, will filter
        filterItemInConfig(request, searchParam);

        if(StringUtil.isNotEmpty(clause)){
            searchParam.addFilter(HcsaChecklistConstants.PARAM_REGULATION_CLAUSE, clause, true);
        }

        if(StringUtil.isNotEmpty(desc)){
            searchParam.addFilter(HcsaChecklistConstants.PARAM_REGULATION_DESC, desc, true);
        }

        if(StringUtil.isNotEmpty(cklItemStr)){
            searchParam.addFilter(HcsaChecklistConstants.PARAM_CHECKLIST_ITEM, cklItemStr, true);
        }

        if(StringUtil.isNotEmpty(riskLevel)){
            searchParam.addFilter(HcsaChecklistConstants.PARAM_RISK_LEVEL, riskLevel, true);
        }

        if(StringUtil.isNotEmpty(answerType)){
            searchParam.addFilter(HcsaChecklistConstants.PARAM_ANSWER_TYPE, answerType, true);
        }

        if(StringUtil.isNotEmpty(status)){
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
        if (StringUtils.isNotEmpty(fromConfigPage)){
            searchParam.addFilter("itemStatus", "CMSTAT001", true);
        }
        if (IaisCommonUtils.isNotEmpty(selectedItemIdToConfig)){
            SqlHelper.builderNotInSql(searchParam, "item.id", HcsaChecklistConstants.SELECTED_ITEM_IN_CONFIG, selectedItemIdToConfig);
        }
    }

    @GetMapping(value = "checklist-item-clause")
    public @ResponseBody String getRegulationClauseById(HttpServletRequest request, HttpServletResponse response){
        String reglId = request.getParameter("regulationId");
        List<HcsaChklSvcRegulationDto> regulationList = (List<HcsaChklSvcRegulationDto>) ParamUtil.getSessionAttr(request, "regulationSelectDetail");
        if (StringUtil.isNotEmpty(reglId) && IaisCommonUtils.isNotEmpty(regulationList)){
            for (HcsaChklSvcRegulationDto i : regulationList){
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
        List<CheckItemQueryDto> itemQueryList = searchResult.getRows();
        for (CheckItemQueryDto cklItemQuery : itemQueryList){
            cklItemQuery.setAnswerType(MasterCodeUtil.getCodeDesc(cklItemQuery.getAnswerType()));
            String riskLvl = MasterCodeUtil.getCodeDesc(cklItemQuery.getRiskLevel());
            cklItemQuery.setRiskLevel("".equals(riskLvl) ? "-" : riskLvl);
            cklItemQuery.setStatus(MasterCodeUtil.getCodeDesc(cklItemQuery.getStatus()));
        }

        boolean blockExcel = false;
        if (IaisCommonUtils.isEmpty(itemQueryList)){
            blockExcel = true;
        }

        File file = null;
        try {
            file = ExcelWriter.writerToExcel(itemQueryList, CheckItemQueryDto.class, null, "Checklist_Items_Template", blockExcel, true);
        } catch (Exception e) {
            log.error("=======>fileHandler error >>>>>", e);
        }

        if(Optional.ofNullable(file).isPresent()){
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
        String[] checked =  request.getParameterValues("selectedCheckBoxItem");
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX, checked);
    }

    /**
     * AutoStep: clearCheckBox
     * @param bpc
     * @throws IllegalAccessException
     */
    public void clearCheckBox(BaseProcessClass bpc){
        ParamUtil.setSessionAttr(bpc.request, HcsaChecklistConstants.CHECK_BOX_REDISPLAY, null);
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
            Stream.of(checked).forEach(i -> ids.add(MaskUtil.unMaskValue(HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX, i)));

            if (inputFile.exists() && inputFile.isFile()) {
                List<ChecklistItemDto> item = hcsaChklService.listChklItemByItemId(ids);
                List<ChecklistConfigExcel> uploadTemplate = IaisCommonUtils.genNewArrayList();

                for (ChecklistItemDto i : item) {
                    ChecklistConfigExcel template = new ChecklistConfigExcel();
                    template.setItemId(i.getItemId());
                    template.setChecklistItem(i.getChecklistItem());
                    uploadTemplate.add(template);
                }

                Map<Integer, List<Integer>> unlockMap = IaisEGPHelper.generateUnlockMap(8, 8);

                File outputFile = ExcelWriter.writerToExcel(uploadTemplate, ChecklistConfigExcel.class, inputFile, "Checklist_Config_Upload_Template", true, false, unlockMap);

                FileUtils.writeFileResponseProcessContent(request, outputFile);

                FileUtils.deleteTempFile(outputFile);

                ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
            }

        } catch (Exception e) {
           log.error("exportItemToConfigTemplate has error ", e);
        }

    }
}
