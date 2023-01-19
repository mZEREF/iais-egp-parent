package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.constant.dataSubmission.DataSubmissionConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.SearchNotificationCycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class NotificationCycleOvertimeService {

    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;

    @Autowired
    private SystemBeLicClient systemBeLicClient;

    @Autowired
    private NotificationHelper notificationHelper;

    public void firstNotification(){
        SearchResult<SearchNotificationCycleDto> res = searchFirst();
        sendEmailFirst(res);
    }

    public void repeatNotification(){
        SearchResult<SearchNotificationCycleDto> res = searchRepeat();
        sendEmailRepeat(res);
    }

    public SearchResult<SearchNotificationCycleDto> searchFirst() {
        SearchParam searchParam = new SearchParam(SearchNotificationCycleDto.class.getName());
//        searchParam.setPageSize(SystemParamUtil.getDefaultPageSize());
        searchParam.setPageNo(1);
        searchParam.setSortField("ID");

        Date current = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(current);
        cal.add(Calendar.DAY_OF_MONTH, -30);
        searchParam.addParam("dateFilter", Formatter.formatDate(cal.getTime()));
        searchParam.addFilter("dateFilter", Formatter.formatDate(cal.getTime()));
        QueryHelp.setMainSql("notificationCycleOvertime","searchCycleOvertime", searchParam);

        return hcsaLicenceClient.searchNotificationCycleFirst(searchParam).getEntity();
    }

    public SearchResult<SearchNotificationCycleDto> searchRepeat() {
        SearchParam searchParam = new SearchParam(SearchNotificationCycleDto.class.getName());
//        searchParam.setPageSize(SystemParamUtil.getDefaultPageSize());
        searchParam.setPageNo(1);
        searchParam.setSortField("ID");

        Date current = new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(current);
        cal.add(Calendar.DAY_OF_MONTH, -3);
        searchParam.addParam("dateFilter", Formatter.formatDate(cal.getTime()));
        searchParam.addFilter("dateFilter", Formatter.formatDate(cal.getTime()));
        QueryHelp.setMainSql("notificationCycleOvertime","searchCycleOvertimeRepeat", searchParam);

        return hcsaLicenceClient.searchNotificationCycleFirst(searchParam).getEntity();
    }


    public void sendEmailFirst(SearchResult<SearchNotificationCycleDto> result){
        List<SearchNotificationCycleDto> res = result.getRows();
        for(SearchNotificationCycleDto item : res){
            EmailParam emailParamEmail = new EmailParam();
            Map<String, Object> msgSubjectMap = IaisCommonUtils.genNewHashMap();
            msgSubjectMap.put("submissionId", item.getSubmissionId());
            msgSubjectMap.put("patient_name", item.getName());
            msgSubjectMap.put("officer_name", "officer_name");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            msgSubjectMap.put("date",sdf.format(item.getSubmitDate()));
            emailParamEmail.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NOTIFICATION_CYCLE_OVERTIME_FIRST);
            emailParamEmail.setTemplateContent(msgSubjectMap);
            emailParamEmail.setQueryCode(IaisEGPHelper.generateRandomString(26));
            emailParamEmail.setReqRefNum(IaisEGPHelper.generateRandomString(26));
            emailParamEmail.setServiceTypes(DataSubmissionConsts.DS_AR_NEW);
            emailParamEmail.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENSEE_ID);
            emailParamEmail.setRefId(item.getLicenseeId());
            notificationHelper.sendNotification(emailParamEmail);
            saveJobRemindTracking(item.getId());
        }
    }

    public void sendEmailRepeat(SearchResult<SearchNotificationCycleDto> result){
        List<SearchNotificationCycleDto> res = result.getRows();
        for(SearchNotificationCycleDto item : res){
            EmailParam emailParamEmail = new EmailParam();
            Map<String, Object> msgSubjectMap = IaisCommonUtils.genNewHashMap();
            msgSubjectMap.put("submissionId", item.getSubmissionId());
            msgSubjectMap.put("patient_name", item.getName());
            msgSubjectMap.put("officer_name", "officer_name");
            emailParamEmail.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NOTIFICATION_CYCLE_OVERTIME_REPEAT);
            emailParamEmail.setTemplateContent(msgSubjectMap);
            emailParamEmail.setQueryCode(IaisEGPHelper.generateRandomString(26));
            emailParamEmail.setReqRefNum(IaisEGPHelper.generateRandomString(26));
            emailParamEmail.setServiceTypes(DataSubmissionConsts.DS_AR_NEW);
            emailParamEmail.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENSEE_ID);
            emailParamEmail.setRefId(item.getLicenseeId());
            notificationHelper.sendNotification(emailParamEmail);
            saveJobRemindTracking(item.getId());
        }
    }

    public void saveJobRemindTracking(String id){
        JobRemindMsgTrackingDto jobRemindMsgTrackingDto = new JobRemindMsgTrackingDto();
        jobRemindMsgTrackingDto.setMsgKey("notificationCycleOvertime");
        jobRemindMsgTrackingDto.setCreateTime(new Date());
        jobRemindMsgTrackingDto.setRefNo(id);
        jobRemindMsgTrackingDto.setStatus("CMSTAT001");
        jobRemindMsgTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());

        systemBeLicClient.saveSendMailJob(jobRemindMsgTrackingDto);
    }

}
