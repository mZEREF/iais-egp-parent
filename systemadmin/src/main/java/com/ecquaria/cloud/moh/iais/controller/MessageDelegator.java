package com.ecquaria.cloud.moh.iais.controller;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.dto.MessageDto;
import com.ecquaria.cloud.moh.iais.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.entity.Message;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.MsgService;
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
        List<String> domainOptionList =  new ArrayList<>();
        domainOptionList.add("INTER");
        domainOptionList.add("INTRA");

        List<String> msgOptionList =  new ArrayList<>();
        msgOptionList.add("Acknowledgement");
        msgOptionList.add("Error");

        List<String> moduleOptionList =  new ArrayList<>();
        moduleOptionList.add("New");
        moduleOptionList.add("Renewal");
        moduleOptionList.add("Cessation");
        moduleOptionList.add("Amendment");
        moduleOptionList.add("Reinstate");
        moduleOptionList.add("Audit");
        moduleOptionList.add("Common");
        moduleOptionList.add("Others");

        Map<String , List<String>> map = new HashMap<>();
        map.put("domainTypeSelect", domainOptionList);
        map.put("msgTypeSelect", msgOptionList);
        map.put("moduleTypeSelect", moduleOptionList);
        IaisEGPHelper.setOptionToList(request, map);
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
        SearchResult result = msgService.doSearch(param, "messageSql", "search");

        ParamUtil.setSessionAttr(request, PARAM_MESSAGE_SEARCH, param);
        ParamUtil.setRequestAttr(request, PARAM_MESSAGE_SEARCH_RESULT, result);

    }

    /**
     * user do edit with message management
     * @param bpc
     */
    public void doEdit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request,"crud_action_type");
        if(!"doEdit".equals(type)){
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
            ParamUtil.setRequestAttr(request,"errorMap",errorMap);
            ParamUtil.setRequestAttr(request,"isValid","N");
        }else {
            Map<String,String> successMap = new HashMap<>();
            successMap.put("edit message","suceess");
            Message message = MiscUtil.transferEntityDto(dto, Message.class);
            msgService.saveMessage(message);
            ParamUtil.setRequestAttr(request,"isValid","Y");
            ParamUtil.setRequestAttr(request,"successMap",successMap);
        }

    }

    /**
     * delete message
     * @param bpc
     */
    public void doDelete(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String msgId = ParamUtil.getString(bpc.request,"crud_action_value");
        if(!StringUtil.isEmpty(msgId)) {
            try {
                Integer id = Integer.valueOf(msgId);
                msgService.deleteMessageById(id);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }
        }
    }

    /**
     * search message
     * @param bpc
     */
    public void doSearch(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request, "crud_action_type");
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
        String action = ParamUtil.getString(bpc.request, "crud_action_type");
        log.debug("*******************action-->:" + action);
        log.debug("The prepareSwitch end ...");
    }


    /**
     * preparation before editing
     * @param bpc
     */
    public void perpareEdit(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String  action = ParamUtil.getString(bpc.request,"crud_action_type");
        String msgId = ParamUtil.getString(bpc.request,"crud_action_value");
        preSelectOption(request);
        if(!StringUtil.isEmpty(msgId)){
            try {
                Integer id = Integer.valueOf(msgId);
                Message msg = msgService.getMessageByMsgId(id);
                MessageDto dto = MiscUtil.transferEntityDto(msg, MessageDto.class);
                ParamUtil.setSessionAttr(request, MessageDto.MESSAGE_REQUEST_DTO, dto);
            }catch (NumberFormatException e){
                e.printStackTrace();
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
