package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
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

    private final FilterParameter filterParameter = new FilterParameter.Builder()
            .clz(MsgTemplateQueryDto.class)
            .searchAttr(MsgTemplateConstants.MSG_SEARCH_PARAM)
            .resultAttr(MsgTemplateConstants.MSG_SEARCH_RESULT)
            .sortField(MsgTemplateConstants.TEMPLATE_SORT_COLUM).sortType(SearchParam.ASCENDING).build();

    private final TemplatesService templatesService;

    @Autowired
    private MasterCodeService masterCodeService;

    @Autowired
    private TemplatesDelegator(TemplatesService templatesService){
        this.templatesService = templatesService;
    }

    public void doStart(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        AuditTrailHelper.auditFunction(MsgTemplateConstants.AUDIT_TRAIL_NAME,
                MsgTemplateConstants.AUDIT_TRAIL_NAME);
        ParamUtil.setSessionAttr(request, MsgTemplateConstants.MSG_SEARCH_PARAM, null);
        ParamUtil.setSessionAttr(request, MsgTemplateConstants.MSG_SEARCH_RESULT, null);
        ParamUtil.setSessionAttr(request, MsgTemplateConstants.MSG_TEMPLATE_DTO, null);
    }
    public void prepareData(BaseProcessClass bpc) {
        HttpServletRequest request = bpc.request;
        SearchParam searchParam = SearchResultHelper.getSearchParam(request, filterParameter,true);
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
        List<SelectOption> messageTypeSelectList = IaisCommonUtils.genNewArrayList();
        messageTypeSelectList.add(new SelectOption("", "Select"));
        messageTypeSelectList.add(new SelectOption("MTTP001", "Alert"));
        messageTypeSelectList.add(new SelectOption("MTTP002", "Banner Alert"));
        messageTypeSelectList.add(new SelectOption("MTTP003", "Letter"));
        messageTypeSelectList.add(new SelectOption("MTTP004", "Notification"));
        ParamUtil.setRequestAttr(bpc.request, "msgType", messageTypeSelectList);

        List<SelectOption> deliveryModeSelectList = IaisCommonUtils.genNewArrayList();
        deliveryModeSelectList.add(new SelectOption("", "Select"));
        deliveryModeSelectList.add(new SelectOption("DEMD001", "Mail"));
        deliveryModeSelectList.add(new SelectOption("DEMD002", "SMS"));
        deliveryModeSelectList.add(new SelectOption("DEMD003", "System Inbox"));
        ParamUtil.setRequestAttr(bpc.request, "deliveryMode", deliveryModeSelectList);
    }

    public void prepareSwitch(BaseProcessClass bpc){

    }

    public void editTemplate(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String msgId = ParamUtil.getString(request,SystemAdminBaseConstants.CRUD_ACTION_VALUE);
        if (!msgId.isEmpty()){
            MsgTemplateDto msgTemplateDto = templatesService.getMsgTemplate(msgId);
            String messageType = msgTemplateDto.getMessageType();
            String deliveryMode = msgTemplateDto.getDeliveryMode();
            String messageTypeTxt = MasterCodeUtil.getCodeDesc(msgTemplateDto.getMessageType());
            String deliveryModeTxt = MasterCodeUtil.getCodeDesc(msgTemplateDto.getDeliveryMode());
            msgTemplateDto.setMessageType(messageType);
            msgTemplateDto.setDeliveryMode(deliveryMode);
            ParamUtil.setSessionAttr(request,MsgTemplateConstants.MSG_TEMPLATE_DTO, msgTemplateDto);


            List<SelectOption> messageTypeSelectList = IaisCommonUtils.genNewArrayList();
            messageTypeSelectList.add(new SelectOption(messageType, messageTypeTxt));
            messageTypeSelectList.add(new SelectOption("MTTP001", "Alert"));
            messageTypeSelectList.add(new SelectOption("MTTP002", "Banner Alert"));
            messageTypeSelectList.add(new SelectOption("MTTP003", "Letter"));
            messageTypeSelectList.add(new SelectOption("MTTP004", "Notification"));
            messageTypeSelectList.add(new SelectOption("MTTP005", "Scheduled Maintenance"));
            ParamUtil.setRequestAttr(bpc.request, "messageTypeSelect", messageTypeSelectList);

            List<SelectOption> deliveryModeSelectList = IaisCommonUtils.genNewArrayList();
            deliveryModeSelectList.add(new SelectOption(deliveryMode, deliveryModeTxt));
            deliveryModeSelectList.add(new SelectOption("DEMD001", "Mail"));
            deliveryModeSelectList.add(new SelectOption("DEMD002", "SMS"));
            deliveryModeSelectList.add(new SelectOption("DEMD003", "System Inbox"));
            ParamUtil.setRequestAttr(bpc.request, "deliveryModeSelect", deliveryModeSelectList);
        }
    }

    public void doEdit(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request,SystemAdminBaseConstants.CRUD_ACTION_TYPE);
        if (!SystemAdminBaseConstants.EDIT_ACTION.equals(type)){
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
            return;
        }
        MsgTemplateDto msgTemplateDto = (MsgTemplateDto) ParamUtil.getSessionAttr(request, MsgTemplateConstants.MSG_TEMPLATE_DTO);

        ValidationResult validationResult =WebValidationHelper.validateProperty(msgTemplateDto, "edit");
        if(validationResult != null && validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            ParamUtil.setRequestAttr(request,SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.NO);
            return;
        }else {
            getValueFromPage(msgTemplateDto, request);
            templatesService.updateMsgTemplate(msgTemplateDto);
            ParamUtil.setRequestAttr(request,SystemAdminBaseConstants.ISVALID,SystemAdminBaseConstants.YES);
        }
    }


    public void searchTemplate(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        String msgType = ParamUtil.getString(request, MsgTemplateConstants.MSG_TEMPLATE_MSGTYPE);
        String deliveryMode = ParamUtil.getString(request, MsgTemplateConstants.MSG_TEMPLATE_DELIVERY_MODE);
        String templateName = ParamUtil.getString(request, MsgTemplateConstants.MSG_TEMPLATE_TEMPLATE_NAME);
        String templateStartDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM)),
                SystemAdminBaseConstants.DATE_FORMAT);
        String templateEndDate = Formatter.formatDateTime(Formatter.parseDate(ParamUtil.getString(request, SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO)),
                SystemAdminBaseConstants.DATE_FORMAT);
        Map<String,Object> templateMap = IaisCommonUtils.genNewHashMap();
        if (!StringUtil.isEmpty(msgType)){
            templateMap.put(MsgTemplateConstants.MSG_TEMPLATE_MSGTYPE,msgType);
        }
        if(!StringUtil.isEmpty(deliveryMode)){
            templateMap.put(MsgTemplateConstants.MSG_TEMPLATE_DELIVERY_MODE,deliveryMode);
        }
        if(!StringUtil.isEmpty(templateName)){
            templateMap.put(MsgTemplateConstants.MSG_TEMPLATE_TEMPLATE_NAME,templateName);
        }
        if (!StringUtil.isEmpty(templateStartDate)){
            templateMap.put( SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM,templateStartDate);
        }
        if (!StringUtil.isEmpty(templateEndDate)){
            templateMap.put( SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO,templateEndDate);
        }
        filterParameter.setFilters(templateMap);
    }

    public void doPage(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchResultHelper.doPage(request,filterParameter);
    }

    public void doSort(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        SearchResultHelper.doSort(request,filterParameter);
    }

    public void preView(BaseProcessClass bpc){

        String msgId = ParamUtil.getString(bpc.request, SystemAdminBaseConstants.CRUD_ACTION_VALUE);
        MsgTemplateDto msgTemplateDto = templatesService.getMsgTemplate(msgId);
        msgTemplateDto.setMessageType(MasterCodeUtil.getCodeDesc(msgTemplateDto.getMessageType()));
        msgTemplateDto.setDeliveryMode(MasterCodeUtil.getCodeDesc(msgTemplateDto.getDeliveryMode()));
        ParamUtil.setRequestAttr(bpc.request,MsgTemplateConstants.MSG_TEMPLATE_DTO, msgTemplateDto);

    }

    private void getValueFromPage(MsgTemplateDto msgTemplateDto, HttpServletRequest request) throws ParseException {
        msgTemplateDto.setMessageType(ParamUtil.getString(request, MsgTemplateConstants.MSG_TEMPLATE_MSGTYPE));
        msgTemplateDto.setDeliveryMode(ParamUtil.getString(request, MsgTemplateConstants.MSG_TEMPLATE_DELIVERY_MODE));
        msgTemplateDto.setTemplateName(ParamUtil.getString(request, MsgTemplateConstants.MSG_TEMPLATE_TEMPLATE_NAME));
        msgTemplateDto.setMessageContent(ParamUtil.getString(request, MsgTemplateConstants.MSG_TEMPLATE_MESSAGE_CONTENT));
        msgTemplateDto.setEffectiveFrom(Formatter.parseDate(ParamUtil.getString(request, SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM)));
        msgTemplateDto.setEffectiveTo(Formatter.parseDate(ParamUtil.getString(request, SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO)));
    }
}
