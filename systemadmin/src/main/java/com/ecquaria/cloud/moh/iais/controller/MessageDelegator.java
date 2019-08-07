package com.ecquaria.cloud.moh.iais.controller;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.dto.MessageDto;
import com.ecquaria.cloud.moh.iais.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.entity.Message;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.MsgService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sg.gov.moh.iais.common.utils.MiscUtil;
import sg.gov.moh.iais.common.utils.ParamUtil;
import sg.gov.moh.iais.common.utils.StringUtil;
import sg.gov.moh.iais.common.validation.ValidationUtils;
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

    private void preSelectOption(HttpServletRequest request){
        Map<String , List<String>> optMap = new HashMap<String , List<String>>(){{
            put("domainTypeSelect", new ArrayList<String>(){{
                add("Internet");
                add("Intranet");
            }});

            put("msgTypeSelect", new ArrayList<String>(){{
                add("Acknowledgement");
                add("Error");
            }});

            put("moduleTypeSelect", new ArrayList<String>(){{
                add("New");
                add("Renewal");
                add("Cessation");
                add("Amendment");
                add("Reinstate");
                add("Audit");
                add("Common");
                add("Others");
            }});
        }};

        IaisEGPHelper.setOptionToList(request, optMap);

    }

    public void startStep(BaseProcessClass bpc) throws IllegalAccessException {
        HttpServletRequest request = bpc.request;
        clearSessionAttr(request, MessageDelegator.class);
    }

    /**
     * prepare data to msg main page
     * @param bpc
     */
    public void prepareData(BaseProcessClass bpc){
        log.debug("errors and acknowledgements pre-setup data");
        HttpServletRequest request = bpc.request;

        preSelectOption(request);

        //default false, go to pre-data
        SearchParam param = getSearchParam(bpc);
        SearchResult result = msgService.doSearch(param, "messageSql", "search");

        ParamUtil.setSessionAttr(request, PARAM_MESSAGE_SEARCH, param);
        ParamUtil.setRequestAttr(request, PARAM_MESSAGE_SEARCH_RESULT, result);

        log.debug("The prepareData end ...");

    }

    public void doCreate(BaseProcessClass bpc){
        log.debug("errors and acknowledgements doCreate");

        log.debug("errors and acknowledgements doCreate");
    }

    public void doEdit(BaseProcessClass bpc){
        log.debug("errors and acknowledgements doEdit");
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request,"crud_action_type");
        if(!"doEdit".equals(type)){
            return;
        }
        MessageDto dto = (MessageDto) ParamUtil.getRequestAttr(request, MessageDto.MESSAGE_REQUEST_DTO);
        log.debug("errors and acknowledgements dto" + dto);
        ValidationResult validationResult = ValidationUtils.validateProperty(dto, "edit");
        if(validationResult.isHasErrors()){
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


        log.debug("errors and acknowledgements doEdit");
    }

    public void doDelete(BaseProcessClass bpc){
        log.debug("errors and acknowledgements delete-action start");
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

        log.debug("errors and acknowledgements delete-action end");
    }

    public void doSearch(BaseProcessClass bpc){
        log.debug("errors and acknowledgements doSearch start");
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request, "crud_action_type");
        if(!"doSearch".equals(type)){
            return;
        }

        String domainType = ParamUtil.getString(request, "domainType");
        String msgType = ParamUtil.getString(request, "msgType");
        String module = ParamUtil.getString(request, "module");

        log.debug("domainType =>>>>>>>>" + domainType);
        log.debug("msgType =>>>>>>>>" + msgType);
        log.debug("module =>>>>>>>>" + module);
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

        log.debug("errors and acknowledgements doSearch end");
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


    public void perpareEdit(BaseProcessClass bpc){
        log.debug("The perpareEdit start ...");
        HttpServletRequest request = bpc.request;
        String  action = ParamUtil.getString(bpc.request,"crud_action_type");
        String msgId = ParamUtil.getString(bpc.request,"crud_action_value");
        preSelectOption(request);
        if(!StringUtil.isEmpty(msgId)){
            try {
                Integer id = Integer.valueOf(msgId);
                Message msg = msgService.getMessageByMsgId(id);
                MessageDto dto = MiscUtil.transferEntityDto(msg, MessageDto.class);
                ParamUtil.setRequestAttr(request, MessageDto.MESSAGE_REQUEST_DTO, dto);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }

        }

        log.debug("*******************action-->: " + action);
        log.debug("The perpareEdit end ...");
    }

    private void clearSessionAttr(HttpServletRequest request, Class<?> clz) throws IllegalAccessException {
        if(request == null || clz == null){
            return;
        }

        log.debug("[clearSessionAttr Class Name] ==>>>" + clz.getName());

        Field[]  fields = clz.getFields();
        if(fields != null){
            for(Field f : fields){
                String fieldName = f.getName();
                if(fieldName.startsWith("PARAM_")){
                    log.debug("param clear session =====>>>>>>>>>>> " + fieldName);
                    ParamUtil.setSessionAttr(request, (String) f.get(fieldName), null);
                }
            }
        }
    }

    private SearchParam getSearchParam(BaseProcessClass bpc,boolean isNew){
        HttpServletRequest request = bpc.request;
        SearchParam param = (SearchParam) ParamUtil.getSessionAttr(request, PARAM_MESSAGE_SEARCH);
        log.debug("SearchParam param ======>>>>>>" + param);
        if(param == null || isNew){
            param = new SearchParam(Message.class);
            param.setPageSize(10);
            param.setPageNo(1);
            param.setSort("msg_id", SearchParam.ASCENDING);
            ParamUtil.setSessionAttr(request, PARAM_MESSAGE_SEARCH, param);
            log.debug("new param ===>>>>>>>>>>>>");
        }
        return param;
    }

    private SearchParam getSearchParam(BaseProcessClass bpc){
        return getSearchParam(bpc, false);
    }
}
