package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.InspectionAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Process MohRemindDoPreInspTask
 *
 * @author Shicheng
 * @date 2020/7/29 17:49
 **/
@JobHandler(value="remindInspectorPreInspTaskJobHandler")
@Component
@Slf4j
public class RemindInspectorPreInspTaskJobHandler extends IJobHandler {

    @Autowired
    private InspectionAssignTaskService inspectionAssignTaskService;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            logAbout("Remind Inspector Do Pre Inspection");
            List<ApplicationDto> applicationDtos =
                    applicationClient.getApplicationByStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS).getEntity();
            if(!IaisCommonUtils.isEmpty(applicationDtos)){
                for(ApplicationDto applicationDto : applicationDtos){
                    try {
                        sendEmailByInspReadiness(applicationDto);
                    } catch (Exception e) {
                        JobLogger.log(e);
                        log.error(e.getMessage(), e);
                        continue;
                    }
                }
            }
            return ReturnT.SUCCESS;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }

    public void remindDoPreInspTaskStep(BaseProcessClass bpc) {
        logAbout("Remind Inspector Do Pre Inspection");
        List<ApplicationDto> applicationDtos =
                applicationClient.getApplicationByStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS).getEntity();
        if(!IaisCommonUtils.isEmpty(applicationDtos)){
            for(ApplicationDto applicationDto : applicationDtos){
                try {
                    sendEmailByInspReadiness(applicationDto);
                } catch (Exception e) {
                    JobLogger.log(e);
                    log.error(e.getMessage(), e);
                    continue;
                }
            }
        }
    }

    private void sendEmailByInspReadiness(ApplicationDto applicationDto) {
        int days = systemParamConfig.getPreInspTaskTeminder();
        log.info(StringUtil.changeForLog("System Inspection days = " + days));
        JobLogger.log(StringUtil.changeForLog("System Inspection days = " + days));
        String appNo = applicationDto.getApplicationNo();
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremCorrByAppNo(appNo).getEntity();
        String appPremCorrId = appPremisesCorrelationDto.getId();
        AppPremisesRecommendationDto appPremisesRecommendationDto = fillUpCheckListGetAppClient.getAppPremRecordByIdAndType(appPremCorrId, InspectionConstants.RECOM_TYPE_INSEPCTION_DATE).getEntity();
        Date inspDate = appPremisesRecommendationDto.getRecomInDate();
        log.info(StringUtil.changeForLog("Inspection Date = " + inspDate));
        JobLogger.log(StringUtil.changeForLog("Inspection Date = " + inspDate));
        List<Date> dayList = MiscUtil.getDateInPeriodByRecurrence(new Date(), inspDate);
        if(!IaisCommonUtils.isEmpty(dayList)){
            int nowDays = dayList.size();
            log.info(StringUtil.changeForLog("jobRemindMsgTrackingDto2 not null, nowDays = " + nowDays));
            JobLogger.log(StringUtil.changeForLog("jobRemindMsgTrackingDto2 not null, nowDays = " + nowDays));
            if(days == nowDays){
                Map<String, Object> templateMap = getEmailField(applicationDto, appPremisesCorrelationDto);
                EmailParam emailParam = new EmailParam();
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_REMIND_INSPECTOR_PRE_INSP_READY);
                emailParam.setTemplateContent(templateMap);
                emailParam.setQueryCode(appNo);
                emailParam.setReqRefNum(appNo);
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                emailParam.setRefId(appNo);
                notificationHelper.sendNotification(emailParam);
                EmailParam smsParam = new EmailParam();
                smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_REMIND_INSPECTOR_PRE_INSP_READY);
                smsParam.setQueryCode(appNo);
                smsParam.setReqRefNum(appNo);
                smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
                smsParam.setRefId(appNo);
                smsParam.setSubject("MOH HALP - [Internal] Inspection Task not ready for inspection");
                notificationHelper.sendNotification(smsParam);
                EmailParam msgParam = new EmailParam();
                msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_REMIND_INSPECTOR_PRE_INSP_READY);
                msgParam.setTemplateContent(templateMap);
                msgParam.setQueryCode(appNo);
                msgParam.setReqRefNum(appNo);
                msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                msgParam.setRefId(appNo);
                notificationHelper.sendNotification(msgParam);
            }
        }
    }

    private Map<String, Object> getEmailField(ApplicationDto applicationDto, AppPremisesCorrelationDto appPremisesCorrelationDto) {
        Map<String, Object> templateMap = IaisCommonUtils.genNewHashMap();
        AppGrpPremisesDto appGrpPremisesDto = inspectionAssignTaskService.getAppGrpPremisesDtoByAppGroId(appPremisesCorrelationDto.getId());
        String address = inspectionAssignTaskService.getAddress(appGrpPremisesDto);
        String hciName = appGrpPremisesDto.getHciName();
        String hciCode = appGrpPremisesDto.getHciCode();
        if(StringUtil.isEmpty(hciName)){
            hciName = "-";
        }
        if(StringUtil.isEmpty(hciCode)){
            hciCode = "-";
        }
        String appNo = applicationDto.getApplicationNo();
        String appType = applicationDto.getApplicationType();
        String serviceId = applicationDto.getServiceId();
        HcsaServiceDto hcsaServiceDto = hcsaConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
        String appGroupId = applicationDto.getAppGrpId();
        ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(appGroupId).getEntity();
        Date appDate = applicationGroupDto.getSubmitDt();
        if(appDate == null){
            appDate = new Date();
        }
        String appDateStr = Formatter.formatDateTime(appDate, "dd/MM/yyyy HH:mm:ss");
        templateMap.put("appNo", appNo);
        templateMap.put("hciName", hciName);
        templateMap.put("hciCode", hciCode);
        templateMap.put("hciAddress", address);
        templateMap.put("serviceName", hcsaServiceDto.getSvcName());
        templateMap.put("applicationNo", appNo);
        templateMap.put("appType", appType);
        templateMap.put("appDate", appDateStr);
        templateMap.put("officer_name", "officer_name");
        return templateMap;
    }

    private void logAbout(String methodName){
        log.debug(StringUtil.changeForLog("****The*****" + methodName +"******Start****"));
    }
}
