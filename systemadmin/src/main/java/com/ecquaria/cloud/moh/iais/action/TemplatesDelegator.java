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
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HalpSearchResultHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.TemplatesService;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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

    @Autowired
    private EicGatewayClient eicGatewayClient;

    private static final Map<String, String> processMap;
    static
    {
        processMap = IaisCommonUtils.genNewHashMap();

        processMap.put("AUD", "Audit");
        processMap.put("CHM", "Checklist Management");
        processMap.put("FEP", "Fees and Payments");
        processMap.put("RSM", "Risk Score Management");
        processMap.put("SM",  "Scheduled Maintenance");
        processMap.put("UEN", "UEN Management");

        processMap.put("APP", "Appeal");
        processMap.put("CES", "Cessation");
        processMap.put("CHM", "Checklist Management");
        processMap.put("INS", "Inspection");
        processMap.put("NAP", "New");
        processMap.put("MISC", "MISC");
        processMap.put("ONA", "Online Appointment");
        processMap.put("REN", "Renewal");
        processMap.put("RFC", "Request For Change");
        processMap.put("RFI", "Request for Information");
        processMap.put("REV", "Revocation");
        processMap.put("SUS", "Suspension");
        processMap.put("TRE", "Task Reassignment");
        processMap.put("WIT", "Withdrawal");
    }

    private final static List<String> processList = Arrays.asList(
            "AUD",
            "UEN",
            "APP",
            "CES",
            "CHM",
            "INS",
            "NAP",
            "MISC",
            "ONA",
            "REN",
            "RFC",
            "RFI",
            "REV",
            "SUS",
            "TRE",
            "WIT");
//    private final FilterParameter filterParameter = new FilterParameter.Builder()
//            .clz(MsgTemplateQueryDto.class)
//            .searchAttr(MsgTemplateConstants.MSG_SEARCH_PARAM)
//            .resultAttr(MsgTemplateConstants.MSG_SEARCH_RESULT)
//            .sortField(MsgTemplateConstants.TEMPLATE_SORT_COLUM).sortType(SearchParam.ASCENDING).build();

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
        SearchParam searchParam = HalpSearchResultHelper.gainSearchParam(request,MsgTemplateConstants.MSG_SEARCH_PARAM,
                MsgTemplateQueryDto.class.getName(),MsgTemplateConstants.TEMPLATE_SORT_COLUM,SearchParam.ASCENDING,false);
        QueryHelp.setMainSql(MsgTemplateConstants.MSG_TEMPLATE_FILE, MsgTemplateConstants.MSG_TEMPLATE_SQL,searchParam);
        SearchResult<MsgTemplateQueryDto> searchResult = templatesService.getTemplateResults(searchParam);
        if(!StringUtil.isEmpty(searchResult)){
            List<MsgTemplateQueryDto> msgTemplateQueryDtoList = searchResult.getRows();
            for (MsgTemplateQueryDto msgDto:msgTemplateQueryDtoList
             ) {
                msgDto.setMessageType(MasterCodeUtil.getCodeDesc(msgDto.getMessageType()));
                msgDto.setDeliveryMode(MasterCodeUtil.getCodeDesc(msgDto.getDeliveryMode()));
                msgDto.setProcess(processMap.get(msgDto.getProcess()));

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

            ParamUtil.setSessionAttr(request,MsgTemplateConstants.MSG_SEARCH_PARAM, searchParam);
            ParamUtil.setRequestAttr(request,MsgTemplateConstants.MSG_SEARCH_RESULT, searchResult);
        }
        List<SelectOption> messageTypeSelectList = IaisCommonUtils.genNewArrayList();
        messageTypeSelectList.add(new SelectOption("", "Please Select"));
        messageTypeSelectList.add(new SelectOption(MsgTemplateConstants.MSG_TEMPLATE_TYPE_ALERT, "Alert"));
        messageTypeSelectList.add(new SelectOption(MsgTemplateConstants.MSG_TEMPLATE_TYPE_LETTER, "Letter"));
        messageTypeSelectList.add(new SelectOption(MsgTemplateConstants.MSG_TEMPLATE_TYPE_NOTIFICATION, "Notification"));
        messageTypeSelectList.add(new SelectOption(MsgTemplateConstants.MSG_TEMPLATE_TYPE_BANNER_ALERT, "Banner Alert"));
        messageTypeSelectList.add(new SelectOption(MsgTemplateConstants.MSG_TEMPLATE_TYPE_SCHEDULED_MAINTENANCE, "Scheduled Maintenance"));
        ParamUtil.setRequestAttr(bpc.request, "msgType", messageTypeSelectList);

        List<SelectOption> deliveryModeSelectList = IaisCommonUtils.genNewArrayList();
        deliveryModeSelectList.add(new SelectOption("", "Please Select"));
        deliveryModeSelectList.add(new SelectOption(MsgTemplateConstants.MSG_TEMPLETE_DELIVERY_MODE_EMAIL, "Email"));
        deliveryModeSelectList.add(new SelectOption(MsgTemplateConstants.MSG_TEMPLETE_DELIVERY_MODE_SMS, "SMS"));
        deliveryModeSelectList.add(new SelectOption(MsgTemplateConstants.MSG_TEMPLETE_DELIVERY_MODE_MSG, "System Inbox"));
        ParamUtil.setRequestAttr(bpc.request, "deliveryMode", deliveryModeSelectList);

        List<SelectOption> msgProcessList = IaisCommonUtils.genNewArrayList();
        msgProcessList.add(new SelectOption("", "Please Select"));
        for (String item:processList
             ) {
            msgProcessList.add(new SelectOption(item,processMap.get(item)));
        }
        ParamUtil.setRequestAttr(bpc.request, "tepProcess", msgProcessList);
    }

    private String changeStringFormat(String rec){
        StringBuilder bccDes = new StringBuilder(100);
        String[] BccList = rec.split(", ");
        for (String item:BccList
        ) {
            String str = (item);
            String full = MasterCodeUtil.getCodeDesc(str);
            bccDes.append(full).append(",</br>");
        }
        return bccDes.toString().substring(0,bccDes.toString().length()-6);
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

            Boolean needRecipient = Boolean.TRUE;
            if(MsgTemplateConstants.MSG_TEMPLATE_TYPE_BANNER_ALERT.equals(messageType) || MsgTemplateConstants.MSG_TEMPLATE_TYPE_SCHEDULED_MAINTENANCE.equals(messageType)){
                needRecipient = Boolean.FALSE;
            }
            ParamUtil.setSessionAttr(bpc.request,"needRecipient",needRecipient);
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
        if (contentSize < 2 || contentSize > 8000) {
            validationResult.setHasErrors(true);
        }
        if(validationResult != null && validationResult.isHasErrors()) {
            Map<String, String> errorMap = validationResult.retrieveAll();
            if (msgTemplateDto.getEffectiveFrom() != null && msgTemplateDto.getEffectiveTo() !=null){
                if(!msgTemplateDto.getEffectiveFrom().before(msgTemplateDto.getEffectiveTo())){
                    validationResult.setHasErrors(true);
                    errorMap.put("effectiveTo",MessageUtil.getMessageDesc("EMM_ERR004"));
                }
            }
            if (contentSize > 8000) {
                errorMap.put("messageContent",MessageUtil.replaceMessage("EMM_ERR005","8000","num"));
            }
            if (contentSize < 2) {
                errorMap.put("messageContent", MessageUtil.getMessageDesc("EMM_ERR0010"));
            }
            Boolean needRecipient = (Boolean) ParamUtil.getSessionAttr(request,"needRecipient");
            if(needRecipient) {
                String recipientString = "";
                String ccrecipientString = "";
                String bccrecipientString = "";
                if (msgTemplateDto.getRecipient() != null) {
                    recipientString = String.join("#", msgTemplateDto.getRecipient());
                }
                if (msgTemplateDto.getCcrecipient() != null) {
                    ccrecipientString = String.join("#", msgTemplateDto.getCcrecipient());
                }
                if (msgTemplateDto.getBccrecipient() != null) {
                    bccrecipientString = String.join("#", msgTemplateDto.getBccrecipient());
                }
                ParamUtil.setSessionAttr(bpc.request,"recipientString", recipientString);
                ParamUtil.setSessionAttr(bpc.request,"ccrecipientString", ccrecipientString);
                ParamUtil.setSessionAttr(bpc.request,"bccrecipientString", bccrecipientString);
            }
            ParamUtil.setRequestAttr(request,SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.NO);
            ParamUtil.setSessionAttr(request, MsgTemplateConstants.MSG_TEMPLATE_DTO,msgTemplateDto);
            return;
        }else {
            templatesService.updateMsgTemplate(msgTemplateDto);
            eicGatewayClient.syncTemplateFe(msgTemplateDto);
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
        SearchParam searchParam = HalpSearchResultHelper.gainSearchParam(request,MsgTemplateConstants.MSG_SEARCH_PARAM,
                MsgTemplateQueryDto.class.getName(),MsgTemplateConstants.TEMPLATE_SORT_COLUM,SearchParam.ASCENDING,true);
        if (!StringUtil.isEmpty(process) && !"Please Select".equals(msgType)){
            searchParam.addFilter(MsgTemplateConstants.MSG_TEMPLATE_MESSAGE_PROCESS,process,true);
        }else{
            searchParam.removeFilter(MsgTemplateConstants.MSG_TEMPLATE_MESSAGE_PROCESS);
        }
        if (!StringUtil.isEmpty(msgType) && !"Please Select".equals(msgType)){
            searchParam.addFilter(MsgTemplateConstants.MSG_TEMPLATE_MSGTYPE,msgType,true);
        }else{
            searchParam.removeFilter(MsgTemplateConstants.MSG_TEMPLATE_MSGTYPE);
        }
        if(!StringUtil.isEmpty(deliveryMode) && !"Please Select".equals(deliveryMode)){
            searchParam.addFilter(MsgTemplateConstants.MSG_TEMPLATE_DELIVERY_MODE,deliveryMode,true);
        }else{
            searchParam.removeFilter(MsgTemplateConstants.MSG_TEMPLATE_DELIVERY_MODE);
        }
        if(!StringUtil.isEmpty(templateName)){
            searchParam.addFilter(MsgTemplateConstants.MSG_TEMPLATE_TEMPLATE_NAME,'%'+templateName+'%',true);
        }else{
            searchParam.removeFilter(MsgTemplateConstants.MSG_TEMPLATE_TEMPLATE_NAME);
        }
        if (startDate != null && endDate != null){
            if(startDate.before(endDate)){
                searchParam.addFilter(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM,templateStartDate,true);
                searchParam.addFilter(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO,templateEndDate,true);
            }else{
                searchParam.removeFilter(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM);
                searchParam.removeFilter(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO);
                ParamUtil.setRequestAttr(request,SystemAdminBaseConstants.TEMPLATE_DATE_ERR_MSG, "Effective Start Date cannot be later than Effective End Date");
            }
        }else{
            if (!StringUtil.isEmpty(templateStartDate)){
                searchParam.addFilter(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM,templateStartDate,true);
            }else{
                searchParam.removeFilter(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM);
            }
            if (!StringUtil.isEmpty(templateEndDate)){
                searchParam.addFilter(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO,templateEndDate,true);
            }else{
                searchParam.removeFilter(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO);
            }
        }
    }

    public void doPage(BaseProcessClass bpc){
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, MsgTemplateConstants.MSG_SEARCH_PARAM);
        HalpSearchResultHelper.doPage(bpc.request,searchParam);
    }

    @GetMapping(value = "suggest-template-description")
    public @ResponseBody
    List<String> suggerTemplateMasterCode(HttpServletRequest request, HttpServletResponse response) {
        log.debug(StringUtil.changeForLog("fileHandler start ...."));
        String codeDescription = request.getParameter("description");
        List<String> codeDescriptionList = IaisCommonUtils.genNewArrayList();
        if (!StringUtil.isEmpty(codeDescription)){
            codeDescriptionList = templatesService.suggestTemplateCodeDescription(codeDescription);
        }
        return codeDescriptionList;
    }

    public void doSort(BaseProcessClass bpc){
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request, MsgTemplateConstants.MSG_SEARCH_PARAM);
        HalpSearchResultHelper.doSort(bpc.request,searchParam);
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
