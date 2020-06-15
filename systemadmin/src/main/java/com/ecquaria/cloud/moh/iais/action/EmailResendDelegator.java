package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.AnnexDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailAttachMentDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ResendListDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.helper.WebValidationHelper;
import com.ecquaria.cloud.moh.iais.service.BlastManagementListService;
import com.ecquaria.cloud.moh.iais.service.DistributionListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 *File Name: MessageDelegator
 *Creator: guyin
 *Creation time:2019/12/26 19:08
 *Describe:
 */

@Delegator(value = "EmailResendDelegator")
@Slf4j
public class EmailResendDelegator {

//    private SearchParam searchParam;

    @Autowired
    BlastManagementListService blastManagementListService;

    @Autowired
    DistributionListService distributionListService;

    public void start(BaseProcessClass bpc){
        AuditTrailHelper.auditFunction("EmailResendDelegator", "EmailResendDelegator");
    }
    /**
     * doPrepare
     * @param bpc
     */
    public void prepare(BaseProcessClass bpc){
        SearchParam searchParam = (SearchParam)ParamUtil.getSessionAttr(bpc.request,"resendSearchParam");
        if(searchParam == null){
            searchParam = new SearchParam(ResendListDto.class.getName());
            searchParam.setPageSize(10);
            searchParam.setPageNo(1);
            searchParam.setSort("sent_time", SearchParam.ASCENDING);
        }
        QueryHelp.setMainSql("systemAdmin", "failEmail",searchParam);
        SearchResult<ResendListDto> searchResult = blastManagementListService.resendList(searchParam);
        ParamUtil.setRequestAttr(bpc.request,"resendSearchResult",searchResult);
        ParamUtil.setSessionAttr(bpc.request,"resendSearchParam",searchParam);

    }

    public void doPage(BaseProcessClass bpc){
        SearchParam searchParam = (SearchParam) ParamUtil.getSessionAttr(bpc.request,"resendSearchParam");
        CrudHelper.doPaging(searchParam,bpc.request);
    }

    /**
     * search
     * @param bpc
     */
    public void search(BaseProcessClass bpc){
        SearchParam searchParam = new SearchParam(ResendListDto.class.getName());
        searchParam.setPageSize(10);
        searchParam.setPageNo(1);
        searchParam.setSort("sent_time", SearchParam.ASCENDING);
        String start = ParamUtil.getRequestString(bpc.request,"start");
        String end = ParamUtil.getRequestString(bpc.request,"end");
        if(!StringUtil.isEmpty(start)){
            searchParam.addFilter("start", start,true);
        }
        if(!StringUtil.isEmpty(end)){
            searchParam.addFilter("end",  end,true);
        }
        if(!StringUtil.isEmpty(end) && !StringUtil.isEmpty(start)){
            try {
                Date startDate = Formatter.parseDate(start);
                Date endDate = Formatter.parseDate(end);
                int comparatorValue = endDate.compareTo(startDate);
                if (comparatorValue < 0){
                    Map<String, String> errorMap = new HashMap<>(1);
                    errorMap.put("date", "Sent Date From cannot be later than Sent Date End");
                    ParamUtil.setRequestAttr(bpc.request, IntranetUserConstant.ERRORMSG, WebValidationHelper.generateJsonStr(errorMap));
                }
            }catch (Exception e){
                log.error(e.getMessage(), e);
            }
        }
        ParamUtil.setSessionAttr(bpc.request,"resendSearchParam",searchParam);
        ParamUtil.setRequestAttr(bpc.request,"start",start);
        ParamUtil.setRequestAttr(bpc.request,"end",end);
    }

    public void resend(BaseProcessClass bpc){
        String id =  ParamUtil.getMaskedString(bpc.request, "emailId");
        EmailAttachMentDto emailAttachMentDto = blastManagementListService.getEmailById(id);
        EmailDto email = new EmailDto();
        email.setContent(emailAttachMentDto.getContent());
        email.setSender(emailAttachMentDto.getSender());
        email.setSubject(emailAttachMentDto.getSubject());
        email.setClientQueryCode(emailAttachMentDto.getClientQueryCode());
        String[] recipient = emailAttachMentDto.getRecipient().split("&");
        List<String> recipientList = IaisCommonUtils.genNewArrayList();
        for (String item:recipient
             ) {
            recipientList.add(item);
        }
        email.setReceipts(recipientList);
        email.setReqRefNum(emailAttachMentDto.getRequestRefNum());

        if(emailAttachMentDto.getAnnexDtos() != null){
            Map<String , byte[]> emailMap = IaisCommonUtils.genNewHashMap();
            for (AnnexDto item:emailAttachMentDto.getAnnexDtos()
                 ) {
                emailMap.put(item.getFileName(),item.getContent());
            }

            blastManagementListService.sendEmail(email,emailMap);
        }else{
            blastManagementListService.sendEmail(email,null);
        }
        blastManagementListService.setEmailResend(id);
    }

    /**
     * edit
     * @param bpc
     */
    public void edit(BaseProcessClass bpc){
        String id =  ParamUtil.getString(bpc.request, "editBlast");
        BlastManagementDto blastManagementDtoById = blastManagementListService.getBlastById(id);
        String schedule = Formatter.formatDate(blastManagementDtoById.getSchedule());
        Calendar cal = Calendar.getInstance();
        cal.setTime(blastManagementDtoById.getSchedule());
        int hour = cal.get(Calendar.HOUR);
        int minute = cal.get(Calendar.MINUTE);
        ParamUtil.setSessionAttr(bpc.request,"hour",hour);
        ParamUtil.setSessionAttr(bpc.request,"minutes",minute);
        ParamUtil.setSessionAttr(bpc.request,"edit",blastManagementDtoById);
        String status;
        if(blastManagementDtoById.getStatus().equals(AppConsts.COMMON_STATUS_ACTIVE)){
            blastManagementDtoById.setStatus("acitve");
        }else{
            blastManagementDtoById.setStatus("inacitve");
        }
        ParamUtil.setSessionAttr(bpc.request,"schedule",schedule);
    }

    /**
     * save
     * @param bpc
     */
    public void save(BaseProcessClass bpc){
        BlastManagementDto blastManagementDto = new BlastManagementDto();
        String id = ParamUtil.getString(bpc.request, "blastid");
        String date = ParamUtil.getString(bpc.request, "date");
        String HH = ParamUtil.getString(bpc.request, "HH");
        String MM = ParamUtil.getString(bpc.request, "MM");
        blastManagementDto.setId(id);
        SimpleDateFormat newformat =  new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT);
        Date schedule = new Date();
        if(!StringUtil.isEmpty(date)){
            try {
                schedule = newformat.parse(date);
                long time = schedule.getTime() + Long.parseLong(HH) * 60 * 60 * 1000 + Long.parseLong(MM) * 60 * 1000;
                schedule.setTime(time);
            } catch (ParseException e) {
                log.error(e.getMessage(), e);
            }
        }
        blastManagementDto.setSchedule(schedule);
        blastManagementListService.setSchedule(blastManagementDto);
    }

}
