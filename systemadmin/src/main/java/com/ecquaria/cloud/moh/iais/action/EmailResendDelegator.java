package com.ecquaria.cloud.moh.iais.action;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.system.BlastManagementDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ResendListDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.ParamUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.CrudHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.BlastManagementListService;
import com.ecquaria.cloud.moh.iais.service.DistributionListService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/*
 *File Name: MessageDelegator
 *Creator: guyin
 *Creation time:2019/12/26 19:08
 *Describe:
 */

@Delegator(value = "EmailResendDelegator")
@Slf4j
public class EmailResendDelegator {

    private SearchParam searchParam;
    @Autowired
    BlastManagementListService blastManagementListService;

    @Autowired
    DistributionListService distributionListService;

    public void start(BaseProcessClass bpc){
        searchParam = new SearchParam(ResendListDto.class.getName());
        searchParam.setPageSize(10);
        searchParam.setPageNo(1);
        searchParam.setSort("sent_time", SearchParam.ASCENDING);
        AuditTrailHelper.auditFunction("EmailResendDelegator", "EmailResendDelegator");
    }
    /**
     * doPrepare
     * @param bpc
     */
    public void prepare(BaseProcessClass bpc){
        CrudHelper.doPaging(searchParam,bpc.request);
        QueryHelp.setMainSql("systemAdmin", "resend",searchParam);
        SearchResult<ResendListDto> searchResult = blastManagementListService.resendList(searchParam);
        ParamUtil.setRequestAttr(bpc.request,"resendSearchResult",searchResult);
        ParamUtil.setRequestAttr(bpc.request,"resendSearchParam",searchParam);

    }

    /**
     * search
     * @param bpc
     */
    public void search(BaseProcessClass bpc){
        String start = ParamUtil.getRequestString(bpc.request,"start");
        String end = ParamUtil.getRequestString(bpc.request,"end");
        searchParam.getParams().clear();
        searchParam.getFilters().clear();
        searchParam.setPageNo(1);
        if(!StringUtil.isEmpty(start)){
            searchParam.addFilter("start", start,true);
        }
        if(!StringUtil.isEmpty(end)){
            searchParam.addFilter("end",  end,true);
        }
        ParamUtil.setRequestAttr(bpc.request,"start",start);
        ParamUtil.setRequestAttr(bpc.request,"end",end);
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
             status = "acitve";
        }else{
            status = "inacitve";
        }
        ParamUtil.setSessionAttr(bpc.request,"status",status);
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
        SimpleDateFormat newformat =  new SimpleDateFormat("dd/MM/yyyy");
        Date schedule = new Date();
        if(!StringUtil.isEmpty(date)){
            try {
                schedule = newformat.parse(date);
                long time = schedule.getTime() + Long.parseLong(HH) * 60 * 60 * 1000 + Long.parseLong(MM) * 60 * 1000;
                schedule.setTime(time);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        blastManagementDto.setSchedule(schedule);
        blastManagementListService.setSchedule(blastManagementDto);
    }

}
