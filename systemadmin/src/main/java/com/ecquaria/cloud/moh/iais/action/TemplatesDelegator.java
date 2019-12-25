package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.mastercode.MasterCodeConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.common.validation.dto.ValidationResult;
import com.ecquaria.cloud.moh.iais.dto.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.*;
import com.ecquaria.cloud.moh.iais.service.MasterCodeService;
import com.ecquaria.cloud.moh.iais.service.TemplatesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-12-23 14:59
 **/
@Slf4j
@Delegator(value = "templatesDelegator")
public class TemplatesDelegator {

    private final FilterParameter filterParameter;
    private final TemplatesService templatesService;

    @Autowired
    private MasterCodeService masterCodeService;

    @Autowired
    private TemplatesDelegator(FilterParameter filterParameter, TemplatesService templatesService){
        this.filterParameter = filterParameter;
        this.templatesService = templatesService;
    }

    public void doStart(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction(MsgTemplateConstants.AUDIT_TRAIL_NAME,
                MsgTemplateConstants.AUDIT_TRAIL_NAME);
    }
    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;

        filterParameter.setClz(MsgTemplateQueryDto.class);
        filterParameter.setSearchAttr(MsgTemplateConstants.MSG_SEARCH_PARAM);
        filterParameter.setResultAttr(MsgTemplateConstants.MSG_SEARCH_RESULT);
        filterParameter.setSortField(MsgTemplateConstants.TEMPLATE_SORT_COLUM);
        SearchParam searchParam = SearchResultHelper.getSearchParam(request,true, filterParameter);
        QueryHelp.setMainSql(MsgTemplateConstants.MSG_TEMPLATE_FILE, MsgTemplateConstants.MSG_TEMPLATE_SQL,searchParam);
        SearchResult searchResult = templatesService.getTemplateResults(searchParam);
        List<MsgTemplateQueryDto> msgTemplateQueryDtoList = searchResult.getRows();
        for (MsgTemplateQueryDto msgDto:msgTemplateQueryDtoList
             ) {
            msgDto.setMessageType(MasterCodeUtil.getCodeDesc(msgDto.getMessageType()));
            msgDto.setDeliveryMode(MasterCodeUtil.getCodeDesc(msgDto.getDeliveryMode()));
        }

        if(!StringUtil.isEmpty(searchResult)){
            ParamUtil.setSessionAttr(request,MsgTemplateConstants.MSG_SEARCH_PARAM, searchParam);
            ParamUtil.setRequestAttr(request,MsgTemplateConstants.MSG_SEARCH_RESULT, searchResult);
        }
    }

    public void prepareSwitch(BaseProcessClass bpc){

    }

    public void editTemplate(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String msgId = ParamUtil.getString(request,MasterCodeConstants.CRUD_ACTION_VALUE);
        prepareSelectOption(bpc);
        if (!msgId.isEmpty()){
            MsgTemplateDto msgTemplateDto = templatesService.getMsgTemplate(msgId);
            msgTemplateDto.setMessageType(MasterCodeUtil.getCodeDesc(msgTemplateDto.getMessageType()));
            msgTemplateDto.setDeliveryMode(MasterCodeUtil.getCodeDesc(msgTemplateDto.getDeliveryMode()));
            ParamUtil.setSessionAttr(request,MsgTemplateConstants.MSG_TEMPLATE_DTO, msgTemplateDto);
        }
    }

    public void doEdit(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request,MasterCodeConstants.CRUD_ACTION_TYPE);
        if (!MasterCodeConstants.EDIT_ACTION.equals(type)){
            ParamUtil.setRequestAttr(request, MasterCodeConstants.ISVALID, MasterCodeConstants.YES);
            return;
        }
        MsgTemplateDto msgTemplateDto = (MsgTemplateDto) ParamUtil.getSessionAttr(request, MsgTemplateConstants.MSG_TEMPLATE_DTO);
        getValueFromPage(msgTemplateDto, request);
        ValidationResult validationResult =WebValidationHelper.validateProperty(msgTemplateDto, "edit");
        if(validationResult != null && validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,MasterCodeConstants.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, "isValid", "N");
            return;
        }

        ParamUtil.setRequestAttr(request,MasterCodeConstants.ISVALID,MasterCodeConstants.YES);
    }


    public void searchTemplate(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        String msgType = ParamUtil.getString(request, "msgType");
        String deliveryMode = ParamUtil.getString(request, "deliveryMode");
        String templateName = ParamUtil.getString(request, "templateName");
        String templateStartDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_EFFECTIVE_FROM)),
                "yyyy-MM-dd");
        String templateEndDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_EFFECTIVE_TO)),
                "yyyy-MM-dd");

        Map<String,Object> templateMap = new HashMap<>();

        if (!StringUtil.isEmpty(msgType)){
            msgType = masterCodeService.findCodeKeyByDescription(msgType);
            templateMap.put("msgType",msgType);
        }
        if(!StringUtil.isEmpty(deliveryMode)){
            deliveryMode = masterCodeService.findCodeKeyByDescription(deliveryMode);
            templateMap.put("deliveryMode",deliveryMode);
        }
        if(!StringUtil.isEmpty(templateName)){
            templateMap.put("templateName",templateName);
        }
        if (!StringUtil.isEmpty(templateStartDate)){
            templateMap.put( MasterCodeConstants.MASTER_CODE_EFFECTIVE_FROM,templateStartDate);
        }
        if (!StringUtil.isEmpty(templateEndDate)){
            templateMap.put( MasterCodeConstants.MASTER_CODE_EFFECTIVE_TO,templateEndDate);
        }
        filterParameter.setFilters(templateMap);
    }

    public void preView(BaseProcessClass bpc){

        String msgId = ParamUtil.getString(bpc.request, MasterCodeConstants.CRUD_ACTION_VALUE);
        MsgTemplateDto msgTemplateDto = templatesService.getMsgTemplate(msgId);
        msgTemplateDto.setMessageType(MasterCodeUtil.getCodeDesc(msgTemplateDto.getMessageType()));
        msgTemplateDto.setDeliveryMode(MasterCodeUtil.getCodeDesc(msgTemplateDto.getDeliveryMode()));
        ParamUtil.setRequestAttr(bpc.request,MsgTemplateConstants.MSG_TEMPLATE_DTO, msgTemplateDto);

    }

    private void prepareSelectOption(BaseProcessClass bpc){
        List<SelectOption> messageTypeSelectList = new ArrayList<>();
        messageTypeSelectList.add(new SelectOption("MTTP001", "Alert"));
        messageTypeSelectList.add(new SelectOption("MTTP002", "Banner Alert"));
        messageTypeSelectList.add(new SelectOption("MTTP003", "Letter"));
        messageTypeSelectList.add(new SelectOption("MTTP004", "Notification"));
        ParamUtil.setRequestAttr(bpc.request, "messageTypeSelect", messageTypeSelectList);

        List<SelectOption> deliveryModeSelectList = new ArrayList<>();
        deliveryModeSelectList.add(new SelectOption("DEMD001", "Mail"));
        deliveryModeSelectList.add(new SelectOption("DEMD002", "SMS"));
        deliveryModeSelectList.add(new SelectOption("DEMD003", "System Inbox"));
        ParamUtil.setRequestAttr(bpc.request, "deliveryModeSelect", deliveryModeSelectList);
    }

    private void getValueFromPage(MsgTemplateDto msgTemplateDto, HttpServletRequest request) throws ParseException {
        msgTemplateDto.setMessageType(ParamUtil.getString(request, "messageType"));
        msgTemplateDto.setDeliveryMode(ParamUtil.getString(request, "deliveryMode"));
        msgTemplateDto.setTemplateName(ParamUtil.getString(request, "templateName"));
        msgTemplateDto.setMessageContent(ParamUtil.getString(request, "messageContent"));
        msgTemplateDto.setEffectiveFrom(Formatter.parseDate(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_EFFECTIVE_FROM)));
        msgTemplateDto.setEffectiveTo(Formatter.parseDate(ParamUtil.getString(request, MasterCodeConstants.MASTER_CODE_EFFECTIVE_TO)));
    }
}
