package com.ecquaria.cloud.moh.iais.action;

/*
 *author: yichen
 *date time:10/11/2019 2:16 PM
 *description:
 */

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.IaisApiStatusCode;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageCodeKey;
import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.CheckItemQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklSvcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageContent;
import com.ecquaria.cloud.moh.iais.common.exception.IaisRuntimeException;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.FileUtils;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelReader;
import com.ecquaria.cloud.moh.iais.helper.excel.ExcelWriter;
import com.ecquaria.cloud.moh.iais.service.HcsaChklService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Delegator(value = "hcsaChklItemDelegator")
@Slf4j
public class HcsaChklItemDelegator {

    private static final String REGULATION = "regulation";
    private static final String CHECKLIST_ITEM = "checklistItem";
    private static final String FILE_UPLOAD_ERROR = "fileUploadError";

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

    /**
     * StartStep: startStep
     * @param bpc
     * @throws IllegalAccessException
     */
    public void startStep(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction("Checklist Management", "Checklist Item");
        HttpServletRequest request = bpc.request;


        String currentValidateId = ParamUtil.getString(request, "currentValidateId");
        if (currentValidateId == null){
            ParamUtil.setSessionAttr(request, "currentValidateId", null);
        }

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
        String crudAction = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        log.debug("*******************crudAction-->:" + crudAction);
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
        String value = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_VALUE);

        if (!StringUtils.isEmpty(value)){
            ParamUtil.setSessionAttr(request, "switchUploadPage", value);
        }
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

    private Map<String, String> validationFile(HttpServletRequest request, MultipartFile file){
        Map<String, String> errorMap = new HashMap<>();
        if (file == null){
            errorMap.put(FILE_UPLOAD_ERROR, MessageCodeKey.GENERAL_ERR0004);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return errorMap;
        }

        String originalFileName = file.getOriginalFilename();
        if (!originalFileName.endsWith("." + ExcelReader.EXCEL_TYPE_XSSF)){
            errorMap.put(FILE_UPLOAD_ERROR, MessageCodeKey.GENERAL_ERR0005);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return errorMap;
        }

        double size = (file.getSize() / 0x400) / (double) 0x400;

        if (Math.ceil(size) > 0x10){
            errorMap.put(FILE_UPLOAD_ERROR, MessageCodeKey.GENERAL_ERR0004);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return errorMap;
        }

        return null;
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

        String value = (String) ParamUtil.getSessionAttr(request, "switchUploadPage");

        MultipartFile file = mulReq.getFile("selectedFile");

        Map<String, String> errorMap = validationFile(request, file);
        if (errorMap != null && !errorMap.isEmpty()){
            return;
        }

        String json = "";
        File toFile = FileUtils.multipartFileToFile(file);
        errorMap = new HashMap<>(1);
        errorMap.put(FILE_UPLOAD_ERROR, "Please remove the same data from Excel.");
        ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
        try {
            switch (value){
                case REGULATION:
                    List<HcsaChklSvcRegulationDto> regulationDtoList = FileUtils.transformToJavaBean(toFile, HcsaChklSvcRegulationDto.class);
                    json = hcsaChklService.submitUploadRegulation(regulationDtoList);
                    break;
                case CHECKLIST_ITEM:
                    List<ChecklistItemDto> checklistItemDtoList = FileUtils.transformToJavaBean(toFile, ChecklistItemDto.class);
                    checklistItemDtoList = checklistItemDtoList.stream().filter(i -> !StringUtil.isEmpty(i.getChecklistItem())).collect(Collectors.toList());
                    json  = hcsaChklService.submitUploadItem(checklistItemDtoList);
                    break;
                default:
            }
        }catch (IaisRuntimeException e){
            errorMap.put(FILE_UPLOAD_ERROR, MessageCodeKey.CHKL_ERR011);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            log.error(e.getMessage());
            return;
        }

        List<MessageContent> messageContentList = JsonUtil.parseToList(json, MessageContent.class);
        for (MessageContent messageContent : messageContentList){
            String msg = MessageUtil.getMessageDesc(messageContent.getResult());
            messageContent.setResult(msg);
        }

        ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
        ParamUtil.setRequestAttr(request, "messageContent", messageContentList);
        FileUtils.deleteTempFile(toFile);
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

        List<ChecklistItemDto> chklItemDtos = (List<ChecklistItemDto>) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_CLONE_SESSION_ATTR);
        if (chklItemDtos == null || chklItemDtos.isEmpty()){
            Map<String,String> errorMap = new HashMap<>(1);
            errorMap.put("cloneRecords", "Please add item to be clone.");
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }

        String json = hcsaChklService.submitCloneItem(chklItemDtos);
        List<MessageContent> messageContentList = JsonUtil.parseToList(json, MessageContent.class);
        if (messageContentList != null && ! messageContentList.isEmpty()){
            for (MessageContent messageContent : messageContentList){
                String msg = MessageUtil.getMessageDesc(messageContent.getResult());
                messageContent.setResult(msg);
            }

            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            ParamUtil.setRequestAttr(request, "messageContent", messageContentList);
            return;
        }
        ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.YES);

    }


    /**
     * for new item , it don't have item id , else update action
     * @param request
     */
    private void doSubmitOrUpdate(HttpServletRequest request){
        ChecklistItemDto itemDto =  requestChklItemDto(request);

        itemDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

        //Field calibration
        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR, itemDto);
        ValidationResult validationResult = WebValidationHelper.validateProperty(itemDto, "save");
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }

        //Business check
        IaisApiResult<ChecklistItemDto> itemDtoIaisApiResult = hcsaChklService.saveChklItem(itemDto);
        if(itemDtoIaisApiResult.isHasError()){
            Map<String, String> errorMap = new HashMap<>();
            int errorCode = itemDtoIaisApiResult.getErrorCode();
            if (IaisApiStatusCode.DUPLICATION_RECORD.getStatusCode() == errorCode){
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("messageContent", IaisApiStatusCode.DUPLICATION_RECORD.getMessage()));
                ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            }
        }else {
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
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

        String[] checkBoxItemId = ParamUtil.getStrings(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX);
        if(checkBoxItemId == null || checkBoxItemId.length <= 0){
            return;
        }

        if(checkBoxItemId.length > 0x10){
            return;
        }

        List<ChecklistItemDto> chklItemDtos = hcsaChklService.listChklItemByItemId(Arrays.asList(checkBoxItemId));
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_CLONE_SESSION_ATTR, (Serializable) chklItemDtos);

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
        ChecklistItemDto itemDto = (ChecklistItemDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR);
        if (itemDto == null){
            itemDto = new ChecklistItemDto();
        }
        String itemId = ParamUtil.getMaskedString(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_ID);
        String clause = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_REGULATION_CLAUSE);
        String desc = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_REGULATION_DESC);
        String chklItem = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM);
        String riskLevel = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_RISK_LEVEL);
        String answerType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_ANSWER_TYPE);
        String status = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_STATUS);

        itemDto.setItemId(itemId);
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
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.YES);
            return;
        }

        ChecklistItemDto itemDto = (ChecklistItemDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR);
        if (itemDto == null){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
            return;
        }

        String chklItem = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM);
        String riskLevel = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_RISK_LEVEL);
        String answerType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_ANSWER_TYPE);

        itemDto.setAnswerType(answerType);
        itemDto.setRiskLevel(riskLevel);
        itemDto.setChecklistItem(chklItem);
        itemDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);

        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR, itemDto);
        ValidationResult validationResult = WebValidationHelper.validateProperty(itemDto, "clone");
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
        }else {
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
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_CLONE_SESSION_ATTR, null);
        ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR, null);

        if(HcsaChecklistConstants.ACTION_CANCEL.equals(currentAction)){
            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR, null);
            ParamUtil.setSessionAttr(request, "currentValidateId", null);
        }

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
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

        ChecklistConfigDto currentConfig = (ChecklistConfigDto) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_CONFIG_SESSION_ATTR);
        String[] checkBoxItemId = ParamUtil.getStrings(request, HcsaChecklistConstants.PARAM_CHKL_ITEM_CHECKBOX);
        if (checkBoxItemId == null || checkBoxItemId.length == 0){
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
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
                            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.NO);
                            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr("configCustomValidation", "CHKL_ERR007"));
                            return;
                        }
                    }
                }

            }
        }

            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, IaisEGPConstant.YES);
    }

    private void loadSingleItemData(HttpServletRequest request){
        String itemId = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_VALUE);
        if(!StringUtil.isEmpty(itemId)){
            ChecklistItemDto itemDto = hcsaChklService.getChklItemById(itemId);
            if (itemDto.getStatus().equals(AppConsts.COMMON_STATUS_IACTIVE)){
                itemDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            }
            ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR, itemDto);



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
        List<ChecklistItemDto> chklItemDtos = (List<ChecklistItemDto>) ParamUtil.getSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_CLONE_SESSION_ATTR);
        chklItemDtos.stream().forEach(itemDto -> {
            if (itemDto.getItemId().equals(itemId)){
                ParamUtil.setSessionAttr(request, HcsaChecklistConstants.CHECKLIST_ITEM_REQUEST_ATTR, itemDto);
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
        String itemId = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_VALUE);
        if(!StringUtil.isEmpty(itemId)){
            boolean canInactive = hcsaChklService.inActiveItem(itemId);
            if (!canInactive){
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
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!HcsaChecklistConstants.ACTION_SEARCH.equals(currentAction)){
            return;
        }

        ChecklistItemDto itemDto = new ChecklistItemDto();

        String clause = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_REGULATION_CLAUSE);
        String desc = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_REGULATION_DESC);
        String chklItem = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM);
        String status = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_STATUS);
        String answerType = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_ANSWER_TYPE);
        String riskLevel = ParamUtil.getString(request, HcsaChecklistConstants.PARAM_RISK_LEVEL);

        itemDto.setRegulationClauseNo(clause);
        itemDto.setRegulationClause(desc);
        itemDto.setChecklistItem(chklItem);
        itemDto.setStatus(status);
        itemDto.setRiskLevel(riskLevel);
        itemDto.setAnswerType(answerType);

        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.PARAM_REGULATION_CLAUSE, clause);
        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.PARAM_REGULATION_DESC, desc);
        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.PARAM_CHECKLIST_ITEM, chklItem);
        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.PARAM_STATUS, status);
        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.PARAM_ANSWER_TYPE, answerType);
        ParamUtil.setRequestAttr(request, HcsaChecklistConstants.PARAM_RISK_LEVEL, riskLevel);

        ValidationResult validationResult = WebValidationHelper.validateProperty(itemDto, "search");
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,IaisEGPConstant.NO);
            return;
        }

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

    
    /**
    * @author: yichen 
    * @description: ajax call that the mehod download excel file by db genereate
    * @param: 
    * @return:
    */
    @GetMapping(value = "checklist-item-file")
	public @ResponseBody void fileHandler(HttpServletRequest request, HttpServletResponse response){
	    log.debug(StringUtil.changeForLog("fileHandler start ...."));
        String action = ParamUtil.getString(request, "action");
        File file = null;
        switch (action){
            case REGULATION:
                List<HcsaChklSvcRegulationDto> regulationList = hcsaChklService.getRegulationClauseListIsActive();
                if (regulationList != null){
                    file = ExcelWriter.exportExcel(regulationList, HcsaChklSvcRegulationDto.class, "Checklist_Regulations_Upload_Template");
                }
                break;
            case CHECKLIST_ITEM:
                SearchParam  searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
                QueryHelp.setMainSql("hcsaconfig", "listChklItem", searchParam);
                SearchResult searchResult = hcsaChklService.listChklItem(searchParam);
                if (!IaisCommonUtils.isEmpty(searchResult.getRows())){

                    //master code to description
                    List<CheckItemQueryDto> checkItemQueryDtoList = searchResult.getRows();
                    for (CheckItemQueryDto checkItemQueryDto : checkItemQueryDtoList){
                        checkItemQueryDto.setAnswerType(MasterCodeUtil.getCodeDesc(checkItemQueryDto.getAnswerType()));
                        String riskLvl = MasterCodeUtil.getCodeDesc(checkItemQueryDto.getRiskLevel());
                        checkItemQueryDto.setRiskLevel("".equals(riskLvl) ? "-" : riskLvl);
                        checkItemQueryDto.setStatus(MasterCodeUtil.getCodeDesc(checkItemQueryDto.getStatus()));
                    }

                    file = ExcelWriter.exportExcel(checkItemQueryDtoList, CheckItemQueryDto.class, "Checklist_Items_Upload_Template");
                }
                break;
            default:
        }

        if(file != null){
            try {
                FileUtils.writeFileResponeContent(response, file);
                FileUtils.deleteTempFile(file);
            } catch (IOException e) {
                log.debug(e.getMessage());
            }
        }

        log.debug(StringUtil.changeForLog("fileHandler end ...."));
    }
}
