package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.mastercode.MasterCodeConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.mastercode.MasterCodeQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.MessageQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
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
    private  final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(MessageQueryDto.class)
            .searchAttr(MessageConstants.PARAM_MESSAGE_SEARCH)
            .resultAttr(MessageConstants.PARAM_MESSAGE_SEARCH_RESULT)
            .sortField("msg_id").build();

    private  final MessageService messageService;

    @Autowired
    public MessageDelegator(MessageService messageService){
        this.messageService = messageService;
    }

    /**
     * setup option to web page
     * @param request
     */
    private void preSelectOption(HttpServletRequest request){
        List<SelectOption> domainOptionList = new ArrayList<>();
        domainOptionList.add(new SelectOption("Internet", "Internet"));
        domainOptionList.add(new SelectOption("Intranet", "Intranet"));
        ParamUtil.setRequestAttr(request, "domainTypeSelect", domainOptionList);

        List<SelectOption> msgOptionList = new ArrayList<>();
        msgOptionList.add(new SelectOption("Ack", "Acknowledgement"));
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
        IaisEGPHelper.clearSessionAttr(request, MessageConstants.class);
    }


    /**
     *  AutoStep: prepareData
     * @param bpc
     */
    public void prepareData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        preSelectOption(request);

        SearchParam param = IaisEGPHelper.getSearchParam(request, filterParameter);
        QueryHelp.setMainSql("systemAdmin", "queryMessage", param);
        SearchResult searchResult = messageService.doQuery(param);
        ParamUtil.setSessionAttr(request, MessageConstants.PARAM_MESSAGE_SEARCH, param);
        ParamUtil.setRequestAttr(request, MessageConstants.PARAM_MESSAGE_SEARCH_RESULT, searchResult);

    }


    /**
     * AutoStep: doEdit
     * user do edit with message management
     * @param bpc
     */
    public void doEdit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String currentAction = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(MessageConstants.ACTION_CANCEL.equals(currentAction)){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
            return;
        }else if(!MessageConstants.ACTION_EDIT.equals(currentAction)){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
            return;
        }

        String domainType = ParamUtil.getString(request, MessageConstants.PARAM_DOMAIN_TYPE);
        String msgType = ParamUtil.getString(request, MessageConstants.PARAM_MSG_TYPE);
        String module = ParamUtil.getString(request, MessageConstants.PARAM_MODULE);
        String description = ParamUtil.getString(request, MessageConstants.PARAM_DESCRIPTION);
        String message = ParamUtil.getString(request, MessageConstants.PARAM_MESSAGE);


        MessageDto editDto = (MessageDto) ParamUtil.getSessionAttr(request, MessageConstants.MESSAGE_REQUEST_DTO);
        editDto.setDomainType(domainType);
        editDto.setMsgType(msgType);
        editDto.setModule(module);
        editDto.setDescription(description);
        editDto.setMessage(message);

        ValidationResult validationResult = WebValidationHelper.validateProperty(editDto, "edit");
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
        }else {
            messageService.saveMessage(editDto);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
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
            messageDto.setStatus(AppConsts.COMMON_STATUS_DELETED);
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
        if(!MessageConstants.ACTION_SEARCH.equals(currentAction)){
            return;
        }
        String domainType = ParamUtil.getString(request, MessageConstants.PARAM_DOMAIN_TYPE);
        String msgType = ParamUtil.getString(request, MessageConstants.PARAM_MSG_TYPE);
        String module = ParamUtil.getString(request, MessageConstants.PARAM_MODULE);


        SearchParam searchParam = IaisEGPHelper.getSearchParam(request, true, filterParameter);

        if (!StringUtil.isEmpty(domainType)){
            searchParam.addFilter(MessageConstants.PARAM_DOMAIN_TYPE, domainType, true);
        }


        if(!StringUtil.isEmpty(msgType)){
            searchParam.addFilter(MessageConstants.PARAM_MSG_TYPE, msgType, true);
        }

        if(!StringUtil.isEmpty(module)){
            searchParam.addFilter(MessageConstants.PARAM_MODULE, module, true);
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
            ParamUtil.setSessionAttr(request, MessageConstants.MESSAGE_REQUEST_DTO, messageDto);
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
