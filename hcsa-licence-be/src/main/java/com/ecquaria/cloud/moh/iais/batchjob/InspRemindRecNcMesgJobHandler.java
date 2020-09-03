package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspEmailFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.InspectionRectificationProService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Shicheng
 * @date 2020/4/29 13:59
 **/
@JobHandler(value="inspRemindRecNcMesgJobHandler")
@Component
@Slf4j
public class InspRemindRecNcMesgJobHandler extends IJobHandler {

    @Autowired
    private InspectionRectificationProService inspectionRectificationProService;

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private HcsaChklClient hcsaChklClient;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private SystemBeLicClient systemBeLicClient;


    @Override
    public ReturnT<String> execute(String s) throws Exception {
        try {
            logAbout("Remind Applicant Rectify N/C Do");
            List<ApplicationDto> applicationDtos = applicationClient.getApplicationByStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION).getEntity();
            if(IaisCommonUtils.isEmpty(applicationDtos)){
                return ReturnT.SUCCESS;
            }
            int weeks = systemParamConfig.getReminderRectification();
            int days = weeks * 7;
            log.info(StringUtil.changeForLog("System days = " + days));
            JobLogger.log(StringUtil.changeForLog("System days = " + days));
            AuditTrailDto intranet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTRANET);
            for(ApplicationDto applicationDto : applicationDtos){
                try {
                    ApplicationGroupDto applicationGroupDto = inspectionTaskClient.getApplicationGroupDtoByAppGroId(applicationDto.getAppGrpId()).getEntity();
                    String licenseeId = applicationGroupDto.getLicenseeId();
                    JobRemindMsgTrackingDto jobRemindMsgTrackingDto2 = systemBeLicClient.getJobRemindMsgTrackingDto(applicationDto.getApplicationNo(), MessageConstants.JOB_REMIND_MSG_KEY_REMIND_RECTIFICATION_EMAIL).getEntity();
                    if (jobRemindMsgTrackingDto2 == null) {
                        log.info(StringUtil.changeForLog("jobRemindMsgTrackingDto2 null"));
                        JobLogger.log(StringUtil.changeForLog("jobRemindMsgTrackingDto2 null"));
                        inspectionDateSendEmail(licenseeId, applicationDto);
                        createJobRemindMsgTrackingDto(intranet, applicationDto.getApplicationNo());
                    } else {
                        Date createDate = jobRemindMsgTrackingDto2.getCreateTime();
                        log.info(StringUtil.changeForLog("jobRemindMsgTrackingDto2 not null, CreateDate = " + createDate));
                        JobLogger.log(StringUtil.changeForLog("jobRemindMsgTrackingDto2 not null, CreateDate = " + createDate));
                        List<Date> dayList = MiscUtil.getDateInPeriodByRecurrence(createDate, new Date());
                        int nowDays = dayList.size();
                        log.info(StringUtil.changeForLog("jobRemindMsgTrackingDto2 not null, nowDays = " + nowDays));
                        JobLogger.log(StringUtil.changeForLog("jobRemindMsgTrackingDto2 not null, nowDays = " + nowDays));
                        if (nowDays > days) {
                            inspectionDateSendEmail(licenseeId, applicationDto);
                            jobRemindMsgTrackingDto2.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                            jobRemindMsgTrackingDto2.setAuditTrailDto(intranet);
                            systemBeLicClient.updateJobRemindMsgTrackingDto(jobRemindMsgTrackingDto2);
                            createJobRemindMsgTrackingDto(intranet, applicationDto.getApplicationNo());
                        }
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    JobLogger.log(e);
                    continue;
                }
            }
            return ReturnT.SUCCESS;
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
    }

    private void createJobRemindMsgTrackingDto(AuditTrailDto intranet, String applicationNo) {
        List<JobRemindMsgTrackingDto> jobRemindMsgTrackingDtos = IaisCommonUtils.genNewArrayList();
        JobRemindMsgTrackingDto jobRemindMsgTrackingDto = new JobRemindMsgTrackingDto();
        jobRemindMsgTrackingDto.setAuditTrailDto(intranet);
        jobRemindMsgTrackingDto.setMsgKey(MessageConstants.JOB_REMIND_MSG_KEY_REMIND_RECTIFICATION_EMAIL);
        jobRemindMsgTrackingDto.setRefNo(applicationNo);
        jobRemindMsgTrackingDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        jobRemindMsgTrackingDto.setId(null);
        jobRemindMsgTrackingDtos.add(jobRemindMsgTrackingDto);
        systemBeLicClient.createJobRemindMsgTrackingDtos(jobRemindMsgTrackingDtos);
    }

    private void inspectionDateSendEmail(String licenseeId, ApplicationDto applicationDto) {
        String appId = applicationDto.getId();
        String appNo = applicationDto.getApplicationNo();
        List<InspEmailFieldDto> inspEmailFieldDtos = getEmailFieldByAppId(appId);
        LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(licenseeId).getEntity();
        String licName = licenseeDto.getName();
        Date date = new Date();
        String strDate = Formatter.formatDateTime(date, "dd/MM/yyyy");
        Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
        templateContent.put("applicant", licName);
        templateContent.put("date", strDate);
        templateContent.put("ncDtos", inspEmailFieldDtos);
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_REMIND_NC_RECTIFICATION);
        emailParam.setTemplateContent(templateContent);
        emailParam.setQueryCode(appNo);
        emailParam.setReqRefNum(appNo);
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        emailParam.setRefId(appNo);
        notificationHelper.sendNotification(emailParam);
        EmailParam smsParam = new EmailParam();
        smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_REMIND_NC_RECTIFICATION);
        smsParam.setQueryCode(appNo);
        smsParam.setReqRefNum(appNo);
        smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        smsParam.setRefId(appNo);
        smsParam.setSubject("MOH HALP - Reminder to submit documentary proof of rectification");
        notificationHelper.sendNotification(smsParam);
        EmailParam msgParam = new EmailParam();
        msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_REMIND_NC_RECTIFICATION);
        msgParam.setTemplateContent(templateContent);
        msgParam.setQueryCode(appNo);
        msgParam.setReqRefNum(appNo);
        msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        msgParam.setRefId(appNo);
        //set svc code
        List<String> serviceCodes = IaisCommonUtils.genNewArrayList();
        String serviceId = applicationDto.getServiceId();
        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
        String serviceCode = hcsaServiceDto.getSvcCode();
        serviceCodes.add(serviceCode);
        msgParam.setSvcCodeList(serviceCodes);
        notificationHelper.sendNotification(msgParam);
    }

    private List<InspEmailFieldDto> getEmailFieldByAppId(String appId) {
        List<InspEmailFieldDto> inspEmailFieldDtos = IaisCommonUtils.genNewArrayList();
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(appId).getEntity();
        String appPremCorrId = appPremisesCorrelationDto.getId();
        List<ChecklistConfigDto> checklistConfigDtos = getAllCheckListByAppPremCorrId(appPremCorrId);
        AppPremPreInspectionNcDto appPremPreInspectionNcDto = fillUpCheckListGetAppClient.getAppNcByAppCorrId(appPremCorrId).getEntity();
        List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtos = fillUpCheckListGetAppClient.getAppNcItemByNcId(appPremPreInspectionNcDto.getId()).getEntity();
        if(!IaisCommonUtils.isEmpty(appPremisesPreInspectionNcItemDtos)){
            for(AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto : appPremisesPreInspectionNcItemDtos){
                int recFlag = appPremisesPreInspectionNcItemDto.getIsRecitfied();
                if(0 == recFlag){
                    InspEmailFieldDto inspEmailFieldDto = setNcDataByItemId(checklistConfigDtos, appPremisesPreInspectionNcItemDto);
                    if(inspEmailFieldDto != null) {
                        inspEmailFieldDtos.add(inspEmailFieldDto);
                    }
                }
            }
        }
        return inspEmailFieldDtos;
    }

    private List<ChecklistConfigDto> getAllCheckListByAppPremCorrId(String appPremCorrId) {
        List<ChecklistConfigDto> checklistConfigDtos = IaisCommonUtils.genNewArrayList();
        List<AppPremisesPreInspectChklDto> chkList = fillUpCheckListGetAppClient.getPremInsChklList(appPremCorrId).getEntity();
        if(!IaisCommonUtils.isEmpty(chkList)){
            for(AppPremisesPreInspectChklDto appPremisesPreInspectChklDto : chkList){
                String configId = appPremisesPreInspectChklDto.getChkLstConfId();
                if(!StringUtil.isEmpty(configId)) {
                    ChecklistConfigDto checklistConfigDto = hcsaConfigClient.getChecklistConfigById(configId).getEntity();
                    if(checklistConfigDto != null){
                        checklistConfigDtos.add(checklistConfigDto);
                    }
                }
            }
        }
        return checklistConfigDtos;
    }

    private InspEmailFieldDto setNcDataByItemId(List<ChecklistConfigDto> checklistConfigDtos, AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto) {
        InspEmailFieldDto inspEmailFieldDto = new InspEmailFieldDto();
        String itemId = appPremisesPreInspectionNcItemDto.getItemId();
        String beRemark = appPremisesPreInspectionNcItemDto.getBeRemarks();
        if(!IaisCommonUtils.isEmpty(checklistConfigDtos)) {
            for (ChecklistConfigDto checklistConfigDto : checklistConfigDtos) {
                List<ChecklistItemDto> checklistItemDtos = getCurrentSvcAllItems(checklistConfigDto);
                if(!IaisCommonUtils.isEmpty(checklistItemDtos)){
                    inspEmailFieldDto = setFieldByItem(inspEmailFieldDto, checklistConfigDto, checklistItemDtos, itemId, beRemark);
                    if(inspEmailFieldDto != null){
                        return inspEmailFieldDto;
                    }
                }
            }
        }
        if( inspEmailFieldDto == null){
            inspEmailFieldDto = new InspEmailFieldDto();
        }
        //There are no matching items, search item from adhoc table
        String adhocItemId = itemId;
        AdhocChecklistItemDto adhocChecklistItemDto = inspectionRectificationProService.getAdhocChecklistItemById(adhocItemId);
        if(adhocChecklistItemDto != null){
            String checkItemId = adhocChecklistItemDto.getItemId();
            if(!StringUtil.isEmpty(checkItemId)){
                ChecklistItemDto checklistItemDto = inspectionRectificationProService.getChklItemById(checkItemId);
                inspEmailFieldDto.setRegulation("-");
                inspEmailFieldDto.setQuestion(checklistItemDto.getChecklistItem());
            } else {
                inspEmailFieldDto.setRegulation("-");
                inspEmailFieldDto.setQuestion(adhocChecklistItemDto.getQuestion());
            }
        }
        inspEmailFieldDto.setBeNcRemark(beRemark);
        inspEmailFieldDto.setServiceName("-");
        return inspEmailFieldDto;
    }

    private InspEmailFieldDto setFieldByItem(InspEmailFieldDto inspEmailFieldDto, ChecklistConfigDto checklistConfigDto, List<ChecklistItemDto> checklistItemDtos,
                                             String itemId, String beRemark) {
        boolean containFlag = false;
        for(ChecklistItemDto checklistItemDto : checklistItemDtos){
            if(itemId.equals(checklistItemDto.getItemId())){
                String category = getItemCategory(checklistConfigDto);
                containFlag = true;
                ChecklistItemDto clItemDto = hcsaChklClient.getChklItemById(itemId).getEntity();
                inspEmailFieldDto.setRegulation(clItemDto.getRegulationClause());
                inspEmailFieldDto.setQuestion(clItemDto.getChecklistItem());
                inspEmailFieldDto.setBeNcRemark(beRemark);
                inspEmailFieldDto.setServiceName(category);
            }
        }
        if(!containFlag){
            inspEmailFieldDto = null;
        }
        return inspEmailFieldDto;
    }

    private String getItemCategory(ChecklistConfigDto checklistConfigDto) {
        String category = "-";
        boolean commonFlag = checklistConfigDto.isCommon();
        if(!commonFlag){
            String subSvc = checklistConfigDto.getSvcSubType();
            if(!StringUtil.isEmpty(subSvc)){
                category = subSvc;
            } else {
                category = checklistConfigDto.getSvcName();
            }
        }
        return category;
    }

    private List<ChecklistItemDto> getCurrentSvcAllItems(ChecklistConfigDto checklistConfigDto) {
        List<ChecklistItemDto> checklistItemDtos = IaisCommonUtils.genNewArrayList();
        List<ChecklistSectionDto> sectionDtos = checklistConfigDto.getSectionDtos();
        if(!IaisCommonUtils.isEmpty(sectionDtos)){
            for(ChecklistSectionDto checklistSectionDto : sectionDtos){
                List<ChecklistItemDto> checklistItemDtoList = checklistSectionDto.getChecklistItemDtos();
                if(!IaisCommonUtils.isEmpty(checklistItemDtoList)){
                    for(ChecklistItemDto checklistItemDto : checklistItemDtoList){
                        if(checklistItemDto != null){
                            checklistItemDtos.add(checklistItemDto);
                        }
                    }
                }
            }
        }
        return checklistItemDtos;
    }

    private void logAbout(String methodName){
        log.info(StringUtil.changeForLog("****The****" + methodName +" *****Start****"));
        JobLogger.log(StringUtil.changeForLog("****The****" + methodName +" *****Start****"));
    }
}
