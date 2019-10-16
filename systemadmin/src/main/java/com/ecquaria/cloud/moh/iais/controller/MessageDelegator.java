package com.ecquaria.cloud.moh.iais.controller;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.constant.MessageConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.dto.MessageDto;
import com.ecquaria.cloud.moh.iais.dto.MessageQueryDto;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *File Name: MessageDelegator
 *Creator: yichen
 *Creation time:2019/8/2 9:08
 *Describe:
 */

@Delegator
@Slf4j
public class MessageDelegator {
    private  final FilterParameter filterParameter;
    private  final MessageService messageService;

    @Autowired
    public MessageDelegator(FilterParameter filterParameter, MessageService messageService){
        this.filterParameter = filterParameter;
        this.messageService = messageService;
    }

    /**
     * setup option to web page
     * @param request
     */
    private void preSelectOption(HttpServletRequest request){
        List<SelectOption> domainOptionList = new ArrayList<>();
        domainOptionList.add(new SelectOption("INTER", "Internet"));
        domainOptionList.add(new SelectOption("INTRA", "Intranet"));
        ParamUtil.setRequestAttr(request, "domainTypeSelect", domainOptionList);

        List<SelectOption> msgOptionList = new ArrayList<>();
        msgOptionList.add(new SelectOption("Acknowledgement", "Acknowledgement"));
        msgOptionList.add(new SelectOption("Error", "Error"));
        ParamUtil.setRequestAttr(request, "msgTypeSelect", msgOptionList);

        List<SelectOption> moduleList =  new ArrayList<>();
        moduleList.add(new SelectOption("New", "New"));
        moduleList.add(new SelectOption("Renewal", "Renewal"));
        moduleList.add(new SelectOption("Request For Change", "Request For Change"));
        moduleList.add(new SelectOption("Withdrawal", "Withdrawal"));
        moduleList.add(new SelectOption("Suspension", "Suspension"));
        moduleList.add(new SelectOption("Revocation", "Revocation"));
        moduleList.add(new SelectOption("Reinstatement", "Reinstatement"));
        moduleList.add(new SelectOption("Appeal", "Appeal"));
        ParamUtil.setRequestAttr(request, "moduleTypeSelect", moduleList);
    }

    /**
     * StartStep: startStep
     * @param bpc
     * @throws IllegalAccessException
     */
    public void startStep(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction("Error and Acknowledgement Message",
                "Function is used by MOH system administrator (users given " +
                        "the administrator rights and have the rights to modify the information");
        HttpServletRequest request = bpc.request;
        IaisEGPHelper.clearSessionAttr(request, MessageConstant.class);
    }


    /**
     *  AutoStep: prepareData
     * @param bpc
     */
    public void prepareData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        preSelectOption(request);

        //setting of query default value
        filterParameter.setClz(MessageQueryDto.class);
        filterParameter.setSortField("msg_id");
        filterParameter.setSearchAttr(MessageConstant.PARAM_MESSAGE_SEARCH);
        filterParameter.setResultAttr(MessageConstant.PARAM_MESSAGE_SEARCH_RESULT);

        SearchParam param = IaisEGPHelper.getSearchParam(request, filterParameter);
        QueryHelp.setMainSql("systemAdmin", "queryMessage", param);
        SearchResult searchResult = messageService.doQuery(param);
        ParamUtil.setSessionAttr(request, MessageConstant.PARAM_MESSAGE_SEARCH, param);
        ParamUtil.setRequestAttr(request, MessageConstant.PARAM_MESSAGE_SEARCH_RESULT, searchResult);

    }


    /**
     * AutoStep: doEdit
     * user do edit with message management
     * @param bpc
     */
    public void doEdit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if("cancel".equals(currentAction)){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
            return;
        }else if(!"doEdit".equals(currentAction)){
            return;
        }

        String domainType = ParamUtil.getString(request, MessageConstant.PARAM_DOMAIN_TYPE);
        String msgType = ParamUtil.getString(request, MessageConstant.PARAM_MSG_TYPE);
        String module = ParamUtil.getString(request, MessageConstant.PARAM_MODULE);
        String description = ParamUtil.getString(request, MessageConstant.PARAM_DESCRIPTION);
        String message = ParamUtil.getString(request, MessageConstant.PARAM_MESSAGE);


        MessageDto editDto = (MessageDto) ParamUtil.getSessionAttr(request, MessageConstant.MESSAGE_REQUEST_DTO);
        editDto.setDomainType(domainType);
        editDto.setMsgType(msgType);
        editDto.setModule(module);
        editDto.setDescription(description);
        editDto.setMessage(message);

        ValidationResult validationResult = WebValidationHelper.validateProperty(editDto, "edit");
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMAP,errorMap);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
        }else {
            Map<String,String> successMap = new HashMap<>();
            successMap.put("edit message","suceess");
            messageService.saveMessage(editDto);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMAP,successMap);
        }

    }

    /**
     * AutoStep: disableStatus
     * @param bpc
     */
    public void disableStatus(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String msgId = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_VALUE);
        if(!StringUtil.isEmpty(msgId)) {
            MessageDto messageDto = messageService.getMessageById(msgId);
            messageDto.setStatus(MessageConstant.STATUS_DEACTIVATED);
            messageService.saveMessage(messageDto);
        }
    }

    /**
     * AutoStep: doSearch
     * @param bpc
     */
    public void doSearch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!"doSearch".equals(currentAction)){
            return;
        }
        MessageQueryDto queryDto = new MessageQueryDto();
        String domainType = ParamUtil.getString(request, MessageConstant.PARAM_DOMAIN_TYPE);
        String msgType = ParamUtil.getString(request, MessageConstant.PARAM_MSG_TYPE);
        String module = ParamUtil.getString(request, MessageConstant.PARAM_MODULE);

        queryDto.setDomainType(domainType);
        queryDto.setMsgType(msgType);
        queryDto.setModule(module);

        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);
        ValidationResult validationResult = WebValidationHelper.validateProperty(queryDto, "search");
        if(validationResult != null && validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ERRORMAP, errorMap);
            ParamUtil.setRequestAttr(request, IaisEGPConstant.ISVALID, "N");
        }else {
            searchParam.addFilter(MessageConstant.PARAM_DOMAIN_TYPE, domainType, true);

            if(!StringUtil.isEmpty(msgType)){
                searchParam.addFilter(MessageConstant.PARAM_MSG_TYPE, msgType, true);
            }

            if(!StringUtil.isEmpty(module)){
                searchParam.addFilter(MessageConstant.PARAM_MODULE, module, true);
            }
        }
    }

    /**
     * AutoStep: PrepareSwitch
     *
     * @param bpc
     * @throws
     */
    public void prepareSwitch(BaseProcessClass bpc) {
        log.debug("The prepareSwitch start ...");
        String nextAction = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        log.debug("*******************nextAction-->:" + nextAction);
        log.debug("The prepareSwitch end ...");
    }

    /**
     * AutoStep: editBefore
     * @param bpc
     */
    public void prepareEdit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String msgId = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
        preSelectOption(request);
        if(!StringUtil.isEmpty(msgId)){
            MessageDto messageDto = messageService.getMessageById(msgId);
            ParamUtil.setSessionAttr(request, MessageConstant.MESSAGE_REQUEST_DTO, messageDto);
        }
    }

    /**
     * AutoStep: changePage
     * @param bpc
     */
    public void doPaging(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doPaging(searchParam,bpc.request);
    }

    /**
     * AutoStep: sortRecords
     * @param bpc
     */
    public void doSorting(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, filterParameter);
        CrudHelper.doSorting(searchParam,  bpc.request);
    }

}
