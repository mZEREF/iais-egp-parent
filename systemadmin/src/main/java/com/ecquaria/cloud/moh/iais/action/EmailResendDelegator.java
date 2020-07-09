package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.intranetUser.IntranetUserConstant;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.SystemAdminBaseConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
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
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    @Value("${iais.email.sender}")
    private String mailSender;

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
        //search mass email
        List<BlastManagementDto> blastManagementDtos = blastManagementListService.getSendedBlast();
        SearchParam searchParam = (SearchParam)ParamUtil.getSessionAttr(bpc.request,"resendSearchParam");
        if(searchParam == null){
            searchParam = new SearchParam(ResendListDto.class.getName());
            searchParam.setPageSize(10);
            searchParam.setPageNo(1);
            searchParam.setSort("sent_time", SearchParam.ASCENDING);
        }

        StringBuilder sb = new StringBuilder("(");
        int i =0;
        for (BlastManagementDto item: blastManagementDtos) {
            sb.append(":itemKey").append(i).append(',');
            i++;
        }
        String inSql = sb.substring(0, sb.length() - 1) + ")";
        searchParam.addParam("msg_id_in", inSql);
        i = 0;
        for (BlastManagementDto item: blastManagementDtos) {
            searchParam.addFilter("itemKey" + i,
                    item.getMessageId());
            i ++;
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
                    Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
                    errorMap.put("later", "Sent Date From cannot be later than Sent Date To");
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
        BlastManagementDto blastManagementDtoById = new BlastManagementDto();
        if(id == null || id.isEmpty()){
            id = (String)ParamUtil.getSessionAttr(bpc.request,"BlastMsgId");
            blastManagementDtoById = (BlastManagementDto) ParamUtil.getSessionAttr(bpc.request,"resendBlastedit");
        }else{
            blastManagementDtoById = blastManagementListService.getBlastByMsgId(id);
            String schedule = Formatter.formatDate(blastManagementDtoById.getSchedule());
            Calendar cal = Calendar.getInstance();
            cal.setTime(blastManagementDtoById.getSchedule());
            int hour = cal.get(Calendar.HOUR);
            int minute = cal.get(Calendar.MINUTE);
            ParamUtil.setSessionAttr(bpc.request,"hour",hour);
            ParamUtil.setSessionAttr(bpc.request,"minutes",minute);
            ParamUtil.setSessionAttr(bpc.request,"schedule",schedule);
        }
        ParamUtil.setSessionAttr(bpc.request,"BlastMsgId",id);

        if(blastManagementDtoById.getStatus().equals(AppConsts.COMMON_STATUS_ACTIVE)){
            blastManagementDtoById.setStatus("acitve");
        }else{
            blastManagementDtoById.setStatus("inacitve");
        }

        ParamUtil.setSessionAttr(bpc.request,"resendBlastedit",blastManagementDtoById);
        ParamUtil.setSessionAttr(bpc.request,"blastResendEmailId",id);
    }

    /**
     * edit
     * @param bpc
     */
    public void send(BaseProcessClass bpc){

        BlastManagementDto blastManagementDto = (BlastManagementDto)ParamUtil.getSessionAttr(bpc.request,"resendBlastedit");
        String date = ParamUtil.getString(bpc.request, "date");
        String HH = ParamUtil.getString(bpc.request, "HH");
        String MM = ParamUtil.getString(bpc.request, "MM");
        SimpleDateFormat newformat =  new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT);
        Date schedule = new Date();
        Map<String, String> errMap = IaisCommonUtils.genNewHashMap();

        if(HH == null){
            blastManagementDto.setHH(null);
            errMap.put("HH","The field is mandatory.");
        }else if(!(StringUtils.isNumeric(HH) &&  Integer.parseInt(HH) < 24)){
            blastManagementDto.setHH(HH);
            errMap.put("HH","Field format is wrong");
        }
        if(MM == null){
            blastManagementDto.setMM(null);

            errMap.put("HH","The field is mandatory.");
        }else if(!(StringUtils.isNumeric(MM) &&  Integer.parseInt(MM) < 60)){
            blastManagementDto.setMM(MM);
            errMap.put("HH","Field format is wrong");
        }
        if(errMap.isEmpty()){
            if(!StringUtil.isEmpty(date)){
                try {
                    schedule = newformat.parse(date);
                    long time = schedule.getTime() + Long.parseLong(HH) * 60 * 60 * 1000 + Long.parseLong(MM) * 60 * 1000;
                    schedule.setTime(time);
                    blastManagementDto.setSchedule(schedule);
                } catch (ParseException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
        if(blastManagementDto.getSchedule() != null && HH != null && MM != null) {
            SimpleDateFormat newfor = new SimpleDateFormat(AppConsts.DEFAULT_DATE_FORMAT);
            Date sch = new Date();
            sch = blastManagementDto.getSchedule();
            Date now = new Date();
            if (sch.compareTo(now) < 0) {
                errMap.put("date", "Send date and time cannot be earlier than now");
            }
        }
        if(errMap.isEmpty()){
            blastManagementDto.setActual(null);
            blastManagementListService.setSchedule(blastManagementDto);
            ParamUtil.setRequestAttr(bpc.request,"crud_action","suc");
        }else{
            ParamUtil.setSessionAttr(bpc.request,"hour",HH);
            ParamUtil.setSessionAttr(bpc.request,"minutes",MM);
            ParamUtil.setSessionAttr(bpc.request,"schedule",date);
            ParamUtil.setSessionAttr(bpc.request,"resendBlastedit",blastManagementDto);
            ParamUtil.setRequestAttr(bpc.request, SystemAdminBaseConstants.ERROR_MSG, WebValidationHelper.generateJsonStr(errMap));
            ParamUtil.setRequestAttr(bpc.request,"crud_action","err");
        }

        String id = (String) ParamUtil.getSessionAttr(bpc.request,"blastResendEmailId");
        blastManagementListService.setEmailResend(id);

    }

    /**
     * backToResend
     * @param bpc
     */
    public void backToResend(BaseProcessClass bpc){

    }
}
