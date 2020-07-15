package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
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
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.FilterParameter;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.SearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.TemplatesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
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
            if(!StringUtil.isEmpty(msgDto.getBcc())){
                msgDto.setBcc(changeStringFormat(msgDto.getBcc()));
            }else{
                msgDto.setBcc("N/A");
            }
            if(!StringUtil.isEmpty(msgDto.getCc())){
                msgDto.setCc(changeStringFormat(msgDto.getCc()));
            }else{
                msgDto.setCc("N/A");
            }
            if(!StringUtil.isEmpty(msgDto.getRec())){
                msgDto.setRec(changeStringFormat(msgDto.getRec()));
            }else{
                msgDto.setRec("N/A");
            }
            if(msgDto.getProcess() == null || msgDto.getProcess().isEmpty()){
                msgDto.setProcess("N/A");
            }
        }

        if(!StringUtil.isEmpty(searchResult)){
            ParamUtil.setSessionAttr(request,MsgTemplateConstants.MSG_SEARCH_PARAM, searchParam);
            ParamUtil.setRequestAttr(request,MsgTemplateConstants.MSG_SEARCH_RESULT, searchResult);
        }
        List<SelectOption> messageTypeSelectList = IaisCommonUtils.genNewArrayList();
        messageTypeSelectList.add(new SelectOption("", "Please Select"));
        messageTypeSelectList.add(new SelectOption("MTTP001", "Alert"));
        messageTypeSelectList.add(new SelectOption("MTTP003", "Letter"));
        messageTypeSelectList.add(new SelectOption("MTTP004", "Notification"));
        messageTypeSelectList.add(new SelectOption("MTTP002", "Banner Alert"));
        messageTypeSelectList.add(new SelectOption("MTTP005", "Scheduled Maintenance"));
        ParamUtil.setRequestAttr(bpc.request, "msgType", messageTypeSelectList);

        List<SelectOption> deliveryModeSelectList = IaisCommonUtils.genNewArrayList();
        deliveryModeSelectList.add(new SelectOption("", "Please Select"));
        deliveryModeSelectList.add(new SelectOption("DEMD001", "Mail"));
        deliveryModeSelectList.add(new SelectOption("DEMD002", "SMS"));
        deliveryModeSelectList.add(new SelectOption("DEMD003", "System Inbox"));
        ParamUtil.setRequestAttr(bpc.request, "deliveryMode", deliveryModeSelectList);

        List<SelectOption> msgProcessList = IaisCommonUtils.genNewArrayList();
        msgProcessList.add(new SelectOption("", "Please Select"));
        msgProcessList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION, "New"));
        msgProcessList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_APPEAL, "Renewal"));
        msgProcessList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE, "Request For Change"));
        msgProcessList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL, "Withdrawal"));
        msgProcessList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_CESSATION, "Cessation"));
        msgProcessList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_APPEAL, "Appeal"));
        msgProcessList.add(new SelectOption(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION, "Inspection"));
        ParamUtil.setRequestAttr(bpc.request, "tepProcess", msgProcessList);
    }

    private String changeStringFormat(String rec){
        StringBuilder bccDes = new StringBuilder(100);
        String[] BccList = rec.split(", ");
        for (String item:BccList
        ) {
            String str = "EM-" + (item);
            String full = MasterCodeUtil.getCodeDesc(str);
            bccDes.append(full).append("</br>");
        }
        return bccDes.toString();
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

            String recipientString = String.join("#", msgTemplateDto.getRecipient());
            String ccrecipientString = String.join("#", msgTemplateDto.getCcrecipient());
            String bccrecipientString = String.join("#", msgTemplateDto.getBccrecipient());

            List<SelectOption> messageTypeSelectList = IaisCommonUtils.genNewArrayList();
            messageTypeSelectList.add(new SelectOption(messageType, messageTypeTxt));
            messageTypeSelectList.add(new SelectOption("MTTP001", "Alert"));
            messageTypeSelectList.add(new SelectOption("MTTP002", "Banner Alert"));
            messageTypeSelectList.add(new SelectOption("MTTP003", "Letter"));
            messageTypeSelectList.add(new SelectOption("MTTP004", "Notification"));
            messageTypeSelectList.add(new SelectOption("MTTP005", "Scheduled Maintenance"));
            ParamUtil.setSessionAttr(bpc.request, "messageTypeSelect", (Serializable) messageTypeSelectList);

            List<SelectOption> deliveryModeSelectList = IaisCommonUtils.genNewArrayList();
            deliveryModeSelectList.add(new SelectOption(deliveryMode, deliveryModeTxt));
            deliveryModeSelectList.add(new SelectOption("DEMD001", "Email"));
            deliveryModeSelectList.add(new SelectOption("DEMD002", "SMS"));
            deliveryModeSelectList.add(new SelectOption("DEMD003", "System Inbox"));
            List<SelectOption> selectOptions = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_TEMPLATE_ROLE);
            for (SelectOption item:selectOptions
                 ) {
                item.setValue(item.getValue().substring(3));
            }
            ParamUtil.setSessionAttr(bpc.request,"recipient",(Serializable) selectOptions);
            ParamUtil.setSessionAttr(bpc.request,"recipientString", recipientString);
            ParamUtil.setSessionAttr(bpc.request,"ccrecipientString", ccrecipientString);
            ParamUtil.setSessionAttr(bpc.request,"bccrecipientString", bccrecipientString);
            ParamUtil.setSessionAttr(bpc.request, "deliveryModeSelect", (Serializable) deliveryModeSelectList);


        }
    }

    public void doEdit(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request,SystemAdminBaseConstants.CRUD_ACTION_TYPE);
        Integer contentSize = ParamUtil.getInt(request,SystemAdminBaseConstants.TEMPLATE_CONTENT_SIZE);
        if (!SystemAdminBaseConstants.EDIT_ACTION.equals(type)){
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
            return;
        }
        MsgTemplateDto msgTemplateDto = (MsgTemplateDto) ParamUtil.getSessionAttr(request, MsgTemplateConstants.MSG_TEMPLATE_DTO);
        msgTemplateDto.setProcess("test");
        getValueFromPage(msgTemplateDto, request);
        ValidationResult validationResult =WebValidationHelper.validateProperty(msgTemplateDto, "edit");
        if (msgTemplateDto.getEffectiveFrom() != null && msgTemplateDto.getEffectiveTo() !=null) {
            if (!msgTemplateDto.getEffectiveFrom().before(msgTemplateDto.getEffectiveTo())) {
                validationResult.setHasErrors(true);
            }
        }
        if (contentSize != -1 && contentSize>8000) {
            validationResult.setHasErrors(true);
        }
        if(validationResult != null && validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            if (msgTemplateDto.getEffectiveFrom() != null && msgTemplateDto.getEffectiveTo() !=null){
                if(!msgTemplateDto.getEffectiveFrom().before(msgTemplateDto.getEffectiveTo())){
                    validationResult.setHasErrors(true);
                    errorMap.put("effectiveTo","Effective Start Date cannot be later than Effective End Date");
                }
            }
            if (contentSize>8000) {
                errorMap.put("messageContent","The content should not exceed 8000 words");
            }
            String recipientString = "";
            String ccrecipientString = "";
            String bccrecipientString = "";
            if(msgTemplateDto.getRecipient() != null){
                recipientString = String.join("#", msgTemplateDto.getRecipient());
            }
            if(msgTemplateDto.getCcrecipient() != null){
                ccrecipientString = String.join("#", msgTemplateDto.getCcrecipient());
            }
            if(msgTemplateDto.getBccrecipient() != null){
                bccrecipientString = String.join("#", msgTemplateDto.getBccrecipient());
            }
            ParamUtil.setSessionAttr(bpc.request,"recipientString", recipientString);
            ParamUtil.setSessionAttr(bpc.request,"ccrecipientString", ccrecipientString);
            ParamUtil.setSessionAttr(bpc.request,"bccrecipientString", bccrecipientString);
            ParamUtil.setRequestAttr(request,SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.NO);
            ParamUtil.setSessionAttr(request, MsgTemplateConstants.MSG_TEMPLATE_DTO,msgTemplateDto);
            return;
        }else {
            templatesService.updateMsgTemplate(msgTemplateDto);
            ParamUtil.setRequestAttr(request,SystemAdminBaseConstants.ISVALID,SystemAdminBaseConstants.YES);
        }
    }

    public void searchTemplate(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        String process = ParamUtil.getString(request, MsgTemplateConstants.MSG_TEMPLATE_MESSAGE_PROCESS);
        String msgType = ParamUtil.getString(request, MsgTemplateConstants.MSG_TEMPLATE_MSGTYPE);
        String deliveryMode = ParamUtil.getString(request, MsgTemplateConstants.MSG_TEMPLATE_DELIVERY_MODE);
        String templateName = ParamUtil.getString(request, MsgTemplateConstants.MSG_TEMPLATE_TEMPLATE_NAME);
        Date startDate = Formatter.parseDate(ParamUtil.getString(request, SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM));
        Date endDate = Formatter.parseDate(ParamUtil.getString(request, SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO));
        String templateStartDate = Formatter.formatDateTime(startDate,SystemAdminBaseConstants.DATE_FORMAT);
        String templateEndDate = Formatter.formatDateTime(endDate,SystemAdminBaseConstants.DATE_FORMAT);
        Map<String,Object> templateMap = IaisCommonUtils.genNewHashMap();
        if (!StringUtil.isEmpty(process) && !"Please Select".equals(msgType)){
            templateMap.put(MsgTemplateConstants.MSG_TEMPLATE_MESSAGE_PROCESS,process);
        }else{
            templateMap.remove(MsgTemplateConstants.MSG_TEMPLATE_MESSAGE_PROCESS);
        }
        if (!StringUtil.isEmpty(msgType) && !"Please Select".equals(msgType)){
            templateMap.put(MsgTemplateConstants.MSG_TEMPLATE_MSGTYPE,msgType);
        }else{
            templateMap.remove(MsgTemplateConstants.MSG_TEMPLATE_MSGTYPE);
        }
        if(!StringUtil.isEmpty(deliveryMode) && !"Please Select".equals(msgType)){
            templateMap.put(MsgTemplateConstants.MSG_TEMPLATE_DELIVERY_MODE,deliveryMode);
        }else{
            templateMap.remove(MsgTemplateConstants.MSG_TEMPLATE_DELIVERY_MODE);
        }
        if(!StringUtil.isEmpty(templateName)){
            templateMap.put(MsgTemplateConstants.MSG_TEMPLATE_TEMPLATE_NAME,'%'+templateName+'%');
        }else{
            templateMap.remove(MsgTemplateConstants.MSG_TEMPLATE_TEMPLATE_NAME);
        }
        if (startDate != null && endDate != null){
            if(startDate.before(endDate)){
                templateMap.put(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM,templateStartDate);
                templateMap.put(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO,templateEndDate);
            }else{
                templateMap.remove(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM);
                templateMap.remove(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO);
                ParamUtil.setRequestAttr(request,SystemAdminBaseConstants.TEMPLATE_DATE_ERR_MSG, "Effective Start Date cannot be later than Effective End Date");
            }
        }else{
            if (!StringUtil.isEmpty(templateStartDate)){
                templateMap.put(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM,templateStartDate);
            }else{
                templateMap.remove(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM);
            }
            if (!StringUtil.isEmpty(templateEndDate)){
                templateMap.put(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO,templateEndDate);
            }else{
                templateMap.remove(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO);
            }
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
        String[] recipient = ParamUtil.getStrings(request,"recipient");
        String[] ccrecipient = ParamUtil.getStrings(request,"ccrecipient");
        String[] bccrecipient = ParamUtil.getStrings(request,"bccrecipient");
        if(recipient == null){
            msgTemplateDto.setRecipient(null);
        }else{
            List<String> recipientList = Arrays.asList(recipient);
            msgTemplateDto.setRecipient(recipientList);
        }
        if(ccrecipient == null){
            msgTemplateDto.setCcrecipient(null);
        }else{
            List<String> ccrecipientList = Arrays.asList(ccrecipient);
            msgTemplateDto.setCcrecipient(ccrecipientList);
        }
        if(bccrecipient == null){
            msgTemplateDto.setBccrecipient(null);
        }else{
            List<String> bccrecipientList = Arrays.asList(bccrecipient);
            msgTemplateDto.setBccrecipient(bccrecipientList);
        }

        msgTemplateDto.setTemplateName(ParamUtil.getString(request, MsgTemplateConstants.MSG_TEMPLATE_TEMPLATE_NAME));
        msgTemplateDto.setMessageContent(ParamUtil.getString(request, MsgTemplateConstants.MSG_TEMPLATE_MESSAGE_CONTENT));
        msgTemplateDto.setEffectiveFrom(Formatter.parseDate(ParamUtil.getString(request, SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM)));
        msgTemplateDto.setEffectiveTo(Formatter.parseDate(ParamUtil.getString(request, SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO)));



    }

}
