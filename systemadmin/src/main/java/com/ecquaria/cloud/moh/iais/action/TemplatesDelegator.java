package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.MasterCodePair;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import sop.webflow.rt.api.BaseProcessClass;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.text.ParseException;
import java.util.*;

/**
 * @Author: Hc
 * @Program: iais-egp
 * @Create: 2019-12-23 14:59
 **/
@Slf4j
@Delegator(value = "templatesDelegator")
public class TemplatesDelegator {

    private static final String DELIVERY_MODE = "deliveryMode";
    private static final String EMM_ERR005 = "EMM_ERR005";

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
        String deliveyMode = ParamUtil.getString(request, MsgTemplateConstants.MSG_TEMPLATE_MSGTYPE);
        SearchParam searchParam = HalpSearchResultHelper.gainSearchParam(request,MsgTemplateConstants.MSG_SEARCH_PARAM,
                MsgTemplateQueryDto.class.getName(),MsgTemplateConstants.TEMPLATE_SORT_COLUM,SearchParam.ASCENDING,false);
        QueryHelp.setMainSql(MsgTemplateConstants.MSG_TEMPLATE_FILE, MsgTemplateConstants.MSG_TEMPLATE_SQL,searchParam);
        SearchResult<MsgTemplateQueryDto> searchResult = templatesService.getTemplateResults(searchParam);

        if(!StringUtil.isEmpty(searchResult)){
            List<MsgTemplateQueryDto> msgTemplateQueryDtoList = searchResult.getRows();
            for (MsgTemplateQueryDto msgDto:msgTemplateQueryDtoList) {
                msgDto.setMessageType(MasterCodeUtil.getCodeDesc(msgDto.getMessageType()));
                msgDto.setDeliveryMode(MasterCodeUtil.getCodeDesc(msgDto.getDeliveryMode()));
                msgDto.setProcess(MasterCodeUtil.getCodeDesc(msgDto.getProcess()));

                msgDto.setBcc(changeStringFormat(msgDto.getBcc()));
                msgDto.setCc(changeStringFormat(msgDto.getCc()));
                msgDto.setRec(changeStringFormat(msgDto.getRec()));
                if(StringUtil.isEmpty(msgDto.getProcess())){
                    msgDto.setProcess("N/A");
                }
            }


            ParamUtil.setRequestAttr(request,MsgTemplateConstants.MSG_SEARCH_RESULT, searchResult);
        }
        ParamUtil.setSessionAttr(request,MsgTemplateConstants.MSG_SEARCH_PARAM, searchParam);
        List<SelectOption> messageTypeSelectList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_MSG_TEMPLATE_TYPE);
        ParamUtil.setRequestAttr(bpc.request, "msgType", messageTypeSelectList);

        if (StringUtil.isEmpty(deliveyMode)){
            List<SelectOption> deliveryModeSelectList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DELIVERY_MODE);
            ParamUtil.setRequestAttr(bpc.request, DELIVERY_MODE, deliveryModeSelectList);
        }else{
            List<SelectOption> deliveyModeList = getDeliveyMode(deliveyMode);
            ParamUtil.setRequestAttr(bpc.request, DELIVERY_MODE, deliveyModeList);
        }

        List<SelectOption> msgProcessList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_MSG_TEMPLATE_PROCESS);
        Collections.sort(msgProcessList,SelectOption::compareTo);
        ParamUtil.setRequestAttr(bpc.request, "tepProcess", msgProcessList);
    }

    private String changeStringFormat(String rec){
        if (StringUtil.isEmpty(rec)) {
            return "N/A";
        }
        StringBuilder bccDes = new StringBuilder(100);
        String[] BccList = rec.split(", ");
        for (String item:BccList) {
            String full = MasterCodeUtil.getCodeDesc(item);
            if(StringUtil.isEmpty(full)){
                full = item;
            }
            bccDes.append(full).append(",</br>");
        }
        return bccDes.substring(0,bccDes.toString().length()-6);
    }



    public void prepareSwitch(BaseProcessClass bpc){
        // nothing to do
    }

    public void editTemplate(BaseProcessClass bpc){
        HttpServletRequest request = bpc.request;
        String msgId = ParamUtil.getString(request,SystemAdminBaseConstants.CRUD_ACTION_VALUE);
        log.info(StringUtil.changeForLog("msgId ===:"+msgId));
        if (StringUtil.isNotEmpty(msgId)){
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

            List<SelectOption> messageTypeSelectList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_MSG_TEMPLATE_TYPE);
            messageTypeSelectList.add(new SelectOption(messageType, messageTypeTxt));
            ParamUtil.setRequestAttr(bpc.request, "msgType", messageTypeSelectList);

            List<SelectOption> deliveryModeSelectList = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_DELIVERY_MODE);
            deliveryModeSelectList.add(new SelectOption(deliveryMode, deliveryModeTxt));
            ParamUtil.setRequestAttr(bpc.request, DELIVERY_MODE, deliveryModeSelectList);

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
            ParamUtil.setRequestAttr(bpc.request, "confirm_err_msg",MsgTemplateConstants.MSG_TEMPLETE_DELIVERY_MODE_SMS.equals(msgTemplateDto.getDeliveryMode()) ? MessageUtil.getMessageDesc("EMM_ERR013") : MessageUtil.replaceMessage(EMM_ERR005,"8000","num"));
        }
    }

    public void doEdit(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        String type = ParamUtil.getString(request,SystemAdminBaseConstants.CRUD_ACTION_TYPE);
        Integer contentSize = ParamUtil.getInt(request,SystemAdminBaseConstants.TEMPLATE_CONTENT_SIZE);
        String delivery = ParamUtil.getString(request,SystemAdminBaseConstants.CRUD_ACTION_DELIVERY_MODE);
        if (!SystemAdminBaseConstants.EDIT_ACTION.equals(type)){
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
            return;
        }
        MsgTemplateDto msgTemplateDto = (MsgTemplateDto) ParamUtil.getSessionAttr(request, MsgTemplateConstants.MSG_TEMPLATE_DTO);
        if(msgTemplateDto==null){
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.YES);
            return;
        }
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
                errorMap.put("messageContent",MessageUtil.replaceMessage(EMM_ERR005,"8000","num"));
            }
            if (contentSize < 2) {
                errorMap.put("messageContent", MessageUtil.replaceMessage("GENERAL_ERR0006","Message Content","field"));
            }
            checkRecipient(request, msgTemplateDto);
            ParamUtil.setRequestAttr(request,SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errorMap));
            ParamUtil.setRequestAttr(request, SystemAdminBaseConstants.ISVALID, SystemAdminBaseConstants.NO);

            ParamUtil.setSessionAttr(request, MsgTemplateConstants.MSG_TEMPLATE_DTO,msgTemplateDto);
            ParamUtil.setRequestAttr(bpc.request, "confirm_err_msg",MsgTemplateConstants.MSG_TEMPLETE_DELIVERY_MODE_SMS.equals(msgTemplateDto.getDeliveryMode()) ? MessageUtil.getMessageDesc("EMM_ERR013") : MessageUtil.replaceMessage(EMM_ERR005,"8000","num"));
        } else {
            templatesService.updateMsgTemplate(msgTemplateDto);
            templatesService.syncTemplateFe(msgTemplateDto);
            ParamUtil.setRequestAttr(request,SystemAdminBaseConstants.ISVALID,SystemAdminBaseConstants.YES);
        }
    }

    private void checkRecipient(HttpServletRequest request, MsgTemplateDto msgTemplateDto) {
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
            ParamUtil.setSessionAttr(request,"recipientString", recipientString);
            ParamUtil.setSessionAttr(request,"ccrecipientString", ccrecipientString);
            ParamUtil.setSessionAttr(request,"bccrecipientString", bccrecipientString);
        }
    }

    public void searchTemplate(BaseProcessClass bpc) throws ParseException {
        HttpServletRequest request = bpc.request;
        String process = ParamUtil.getString(request, MsgTemplateConstants.MSG_TEMPLATE_MESSAGE_PROCESS);
        String msgType = ParamUtil.getString(request, MsgTemplateConstants.MSG_TEMPLATE_MSGTYPE);
        String deliveryMode = ParamUtil.getString(request, MsgTemplateConstants.MSG_TEMPLATE_DELIVERY_MODE);
        String templateName = ParamUtil.getString(request, MsgTemplateConstants.MSG_TEMPLATE_TEMPLATE_NAME);
        if(!StringUtil.isEmpty(templateName)){
            templateName = templateName.replace('\n',' ');
        }
        Date startDate = Formatter.parseDate(ParamUtil.getString(request, SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM));
        Date endDate = Formatter.parseDate(ParamUtil.getString(request, SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO));
        String templateStartDate = Formatter.formatDateTime(startDate,SystemAdminBaseConstants.DATE_FORMAT);
        String templateEndDate = Formatter.formatDateTime(endDate,SystemAdminBaseConstants.DATE_FORMAT);
        SearchParam searchParam = HalpSearchResultHelper.gainSearchParam(request,MsgTemplateConstants.MSG_SEARCH_PARAM,
                MsgTemplateQueryDto.class.getName(),MsgTemplateConstants.TEMPLATE_SORT_COLUM,SearchParam.ASCENDING,true);
        searchParam.addFilterOrRemove(MsgTemplateConstants.MSG_TEMPLATE_MESSAGE_PROCESS,process,true);
        searchParam.addFilterOrRemove(MsgTemplateConstants.MSG_TEMPLATE_MSGTYPE,msgType,true);
        searchParam.addFilterOrRemove(MsgTemplateConstants.MSG_TEMPLATE_DELIVERY_MODE,deliveryMode,true);
        searchParam.addFilterOrRemove(MsgTemplateConstants.MSG_TEMPLATE_TEMPLATE_NAME,templateName,true);

        if (startDate != null && endDate != null){
            if(startDate.before(endDate)){
                searchParam.addFilter(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM,templateStartDate,true);
                searchParam.addFilter(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO,templateEndDate,true);
            }else{
                searchParam.removeFilter(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM);
                searchParam.removeFilter(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO);
                ParamUtil.setRequestAttr(request,SystemAdminBaseConstants.TEMPLATE_DATE_ERR_MSG, MessageUtil.getMessageDesc("EMM_ERR004"));
            }
        }else{
            searchParam.addFilterOrRemove(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_FROM,templateStartDate,true);
            searchParam.addFilterOrRemove(SystemAdminBaseConstants.MASTER_CODE_EFFECTIVE_TO,templateEndDate,true);
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
        if(StringUtil.isNotEmpty(searchParam.getSortMap().get("PROCESS_DESC"))){
            List<SelectOption> inboxTypes = MasterCodeUtil.retrieveOptionsByCate(MasterCodeUtil.CATE_ID_MSG_TEMPLATE_PROCESS);
            MasterCodePair mcp = new MasterCodePair("process", "process_desc", inboxTypes);
            searchParam.addMasterCode(mcp);
            ParamUtil.setSessionAttr(bpc.request, MsgTemplateConstants.MSG_SEARCH_PARAM, searchParam);
        }
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

    private List<SelectOption> getDeliveyMode(String deliveyMode){
        String email = MsgTemplateConstants.MSG_TEMPLETE_DELIVERY_MODE_EMAIL;
        String sms = MsgTemplateConstants.MSG_TEMPLETE_DELIVERY_MODE_SMS;
        String msg = MsgTemplateConstants.MSG_TEMPLETE_DELIVERY_MODE_MSG;
        String na = MsgTemplateConstants.MSG_TEMPLETE_DELIVERY_MODE_NA;
        List<SelectOption> selectOptions = IaisCommonUtils.genNewArrayList();
        switch (deliveyMode){
            case MsgTemplateConstants.MSG_TEMPLATE_TYPE_BANNER_ALERT:
//                selectOptions.add(new SelectOption(na,MasterCodeUtil.getCodeDesc(na)));
//                break;
            case MsgTemplateConstants.MSG_TEMPLATE_TYPE_ALERT:
//                selectOptions.add(new SelectOption(na,MasterCodeUtil.getCodeDesc(na)));
//                break;
            case MsgTemplateConstants.MSG_TEMPLATE_TYPE_SCHEDULED_MAINTENANCE:
                selectOptions.add(new SelectOption(na,MasterCodeUtil.getCodeDesc(na)));
                break;
            case MsgTemplateConstants.MSG_TEMPLATE_TYPE_LETTER:
                selectOptions.add(new SelectOption(email,MasterCodeUtil.getCodeDesc(email)));
                selectOptions.add(new SelectOption(msg,MasterCodeUtil.getCodeDesc(msg)));
                break;
            case MsgTemplateConstants.MSG_TEMPLATE_TYPE_NOTIFICATION:
                selectOptions.add(new SelectOption(email,MasterCodeUtil.getCodeDesc(email)));
                selectOptions.add(new SelectOption(msg,MasterCodeUtil.getCodeDesc(msg)));
                selectOptions.add(new SelectOption(sms,MasterCodeUtil.getCodeDesc(sms)));
                break;
            default:
                selectOptions.add(new SelectOption(na,MasterCodeUtil.getCodeDesc(na)));
                break;
        }
        return selectOptions;
    }

}
