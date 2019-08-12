package com.ecquaria.cloud.moh.iais.controller;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.constant.IaisEGPConstant;
import com.ecquaria.cloud.moh.iais.dto.MessageDto;
import com.ecquaria.cloud.moh.iais.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.entity.Message;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.MsgService;
import com.ecquaria.cloud.moh.iais.tags.SelectOption;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.common.utils.MiscUtil;
import sg.gov.moh.iais.common.utils.ParamUtil;
import sg.gov.moh.iais.common.utils.StringUtil;
import sg.gov.moh.iais.common.validation.dto.ValidationResult;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 *File Name: ErrorMessageController
 *Creator: yichen
 *Creation time:2019/8/2 9:08
 *Describe:
 */

@Delegator
@Slf4j
public class MessageDelegator {
    public static final String PARAM_MESSAGE_SEARCH = "msgSearchParam";
    public static final String PARAM_MESSAGE_SEARCH_RESULT = "msgSearchResult";

    public static final String PARAM_MSG_ID = "param_msg_id";
    public static final String PARAM_ROW_ID = "param_row_id";
    public static final String PARAM_CODE_KEY = "param_code_key";
    public static final String PARAM_DOMAIN_TYPE = "param_domain_type";
    public static final String PARAM_MSG_TYPE = "param_msg_type";
    public static final String PARAM_MODULE = "param_module";
    public static final String PARAM_DESCRIPTION = "param_description";
    public static final String PARAM_STATUS = "param_status";

    @Autowired
    private MsgService msgService;

    /**
     * setup option to web page
     * @param request
     */
    private void preSelectOption(HttpServletRequest request){
        List<SelectOption> domainOptionList = new ArrayList<>();
        domainOptionList.add(new SelectOption("Inter", "Internet"));
        domainOptionList.add(new SelectOption("Intra", "Intranet"));
        ParamUtil.setRequestAttr(request, "domainTypeSelect", domainOptionList);

        List<SelectOption> msgOptionList = new ArrayList<>();
        msgOptionList.add(new SelectOption("Acknowledgement", "Acknowledgement"));
        msgOptionList.add(new SelectOption("Error", "Error"));
        ParamUtil.setRequestAttr(request, "msgTypeSelect", msgOptionList);

        List<SelectOption> moduleList =  new ArrayList<>();
        moduleList.add(new SelectOption("New", "New"));
        moduleList.add(new SelectOption("Renewal", "Renewal"));
        moduleList.add(new SelectOption("Cessation", "Cessation"));
        moduleList.add(new SelectOption("Amendment", "Amendment"));
        moduleList.add(new SelectOption("Reinstate", "Reinstate"));
        moduleList.add(new SelectOption("Audit", "Audit"));
        moduleList.add(new SelectOption("Common", "Common"));
        moduleList.add(new SelectOption("Others", "Others"));
        ParamUtil.setRequestAttr(request, "moduleTypeSelect", moduleList);
    }

    public void startStep(BaseProcessClass bpc) throws IllegalAccessException {
        AuditTrailHelper.auditFunction("Error and Acknowledgement Message", "Function is used by MOH system administrator (users given the administrator rights and have the rights to modify the information");
        HttpServletRequest request = bpc.request;
        clearSessionAttr(request, MessageDelegator.class);
    }

    /**
     * prepare data to msg main page
     * @param bpc
     */
    public void prepareData(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;

        preSelectOption(request);

        //default false, go to pre-data
        SearchParam param = getSearchParam(bpc);
        SearchResult result = msgService.doSearch(param, "systemAdmin", "queryMessage");

        ParamUtil.setSessionAttr(request, PARAM_MESSAGE_SEARCH, param);
        ParamUtil.setRequestAttr(request, PARAM_MESSAGE_SEARCH_RESULT, result);

    }

    /**
     * INTRA INTER Internet Intranet
     * @param str
     * @return
     *//*
    private String convertDomainType(String str){
        if(str == null || str.length() == 0){
            return str;
        }

        int len = str.length() - 1;
        Integer uCode = Integer.valueOf(str.charAt(len));
        if(uCode == 116){
            return str.substring(0,5).toUpperCase();
        }else{
            return str.charAt(0) + str.substring(1,5).toLowerCase() + "net";
        }
    }*/


    /**
     * user do edit with message management
     * @param bpc
     */
    public void doEdit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if("cancel".equals(type)){
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
            return;
        }else if(!"doEdit".equals(type)){
            return;
        }

        String domainType = ParamUtil.getString(request, "domainType");
        String msgType = ParamUtil.getString(request, "msgType");
        String module = ParamUtil.getString(request, "module");
        String description = ParamUtil.getString(request, "description");

        MessageDto dto = (MessageDto) ParamUtil.getSessionAttr(request, MessageDto.MESSAGE_REQUEST_DTO);
        dto.setDomainType(domainType);
        dto.setMsgType(msgType);
        dto.setModule(module);
        dto.setDescription(description);

        ParamUtil.setRequestAttr(request, MessageDto.MESSAGE_REQUEST_DTO, dto);
        ValidationResult validationResult = WebValidationHelper.validateProperty(dto, "edit");
        if(validationResult != null && validationResult.isHasErrors()){
            Map<String,String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMAP,errorMap);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"N");
        }else {
            Map<String,String> successMap = new HashMap<>();
            successMap.put("edit message","suceess");
            Message message = MiscUtil.transferEntityDto(dto, Message.class);
            msgService.saveMessage(message);
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ISVALID,"Y");
            ParamUtil.setRequestAttr(request,IaisEGPConstant.ERRORMAP,successMap);
        }

    }

    /**
     * delete message
     * @param bpc
     */
    public void doDelete(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String msgId = ParamUtil.getString(request,IaisEGPConstant.CRUD_ACTION_VALUE);
        if(!StringUtil.isEmpty(msgId)) {
            try {
                Integer id = Integer.valueOf(msgId);
                msgService.deleteMessageById(id);
            }catch (NumberFormatException e){
                log.debug(e.getMessage());
            }
        }
    }

    /**
     * search message
     * @param bpc
     */
    public void doSearch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request, IaisEGPConstant.CRUD_ACTION_TYPE);
        if(!"doSearch".equals(type)){
            return;
        }

        String domainType = ParamUtil.getString(request, "domainType");
        String msgType = ParamUtil.getString(request, "msgType");
        String module = ParamUtil.getString(request, "module");

        SearchParam param = getSearchParam(bpc, true);
        if(!StringUtil.isEmpty(domainType)){
            param.addFilter("domainType", domainType, true);
        }

        if(!StringUtil.isEmpty(msgType)){
            param.addFilter("msgType", msgType, true);
        }

        if(!StringUtil.isEmpty(module)){
            param.addFilter("module", module, true);
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
        String action = ParamUtil.getString(bpc.request, IaisEGPConstant.CRUD_ACTION_TYPE);
        log.debug("*******************action-->:" + action);
        log.debug("The prepareSwitch end ...");
    }


    /**
     * preparation before editing
     * @param bpc
     */
    public void perpareEdit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String msgId = ParamUtil.getString(bpc.request,IaisEGPConstant.CRUD_ACTION_VALUE);
        preSelectOption(request);
        if(!StringUtil.isEmpty(msgId)){
            try {
                Integer id = Integer.valueOf(msgId);
                Message msg = msgService.getMessageByMsgId(id);
                MessageDto dto = MiscUtil.transferEntityDto(msg, MessageDto.class);
                dto.setDomainType(dto.getDomainType());
                ParamUtil.setSessionAttr(request, MessageDto.MESSAGE_REQUEST_DTO, dto);
            }catch (NumberFormatException e){
                log.debug(e.getMessage());
            }

        }
    }

    public void doPaging(BaseProcessClass bpc){
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doPaging(searchParam,bpc.request);
    }

    public void doSorting(BaseProcessClass bpc){
        SearchParam searchParam = getSearchParam(bpc);
        CrudHelper.doSorting(searchParam,  bpc.request);
    }

    private void clearSessionAttr(HttpServletRequest request, Class<?> clz) throws IllegalAccessException {
        if(request == null || clz == null){
            return;
        }

        Field[]  fields = clz.getFields();
        if(fields != null){
            for(Field f : fields){
                String fieldName = f.getName();
                if(fieldName.startsWith("PARAM_")){
                    ParamUtil.setSessionAttr(request, (String) f.get(fieldName), null);
                }
            }
        }
    }

    private SearchParam getSearchParam(BaseProcessClass bpc,boolean isNew){
        HttpServletRequest request = bpc.request;
        SearchParam param = (SearchParam) ParamUtil.getSessionAttr(request, PARAM_MESSAGE_SEARCH);
        if(param == null || isNew){
            param = new SearchParam(Message.class);
            param.setPageSize(10);
            param.setPageNo(1);
            param.setSort("msg_id", SearchParam.ASCENDING);
            ParamUtil.setSessionAttr(request, PARAM_MESSAGE_SEARCH, param);
        }
        return param;
    }

    private SearchParam getSearchParam(BaseProcessClass bpc){
        return getSearchParam(bpc, false);
    }
}
