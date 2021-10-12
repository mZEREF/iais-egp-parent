package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonWorkingDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspEmailFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.ApptInspectionDateService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.InspectionRectificationProService;
import com.ecquaria.cloud.moh.iais.service.client.AppSvcVehicleBeClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import com.ecquaria.cloud.moh.iais.util.WorkDayCalculateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Process MohRemindRecNcMesg
 *
 * @author Shicheng
 * @date 2020/4/29 13:59
 **/
@Delegator("remindRecNcMesgBatchJob")
@Slf4j
public class InspRemindRecNcMesgBatchJob {

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

    @Autowired
    private AppointmentClient appointmentClient;

    @Autowired
    private InsepctionNcCheckListService insepctionNcCheckListService;

    @Autowired
    private ApptInspectionDateService apptInspectionDateService;

    @Autowired
    private AppSvcVehicleBeClient appSvcVehicleBeClient;

    /**
     * StartStep: remindRecNcMesgStart
     *
     * @param bpc
     * @throws
     */
    public void remindRecNcMesgStart(BaseProcessClass bpc){
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        logAbout("Remind Applicant Rectify N/C");
    }

    /**
     * StartStep: remindRecNcMesgStep
     *
     * @param bpc
     * @throws
     */
    public void remindRecNcMesgStep(BaseProcessClass bpc){
        logAbout("Remind Applicant Rectify N/C Do");
        List<ApplicationDto> applicationDtos = applicationClient.getApplicationByStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION).getEntity();
        if(IaisCommonUtils.isEmpty(applicationDtos)){
            return;
        }
        int weeks = systemParamConfig.getReminderRectification();
        int days = weeks * 7;
        log.info(StringUtil.changeForLog("System days = " + days));
        JobLogger.log(StringUtil.changeForLog("System days = " + days));
        AuditTrailDto intranet = AuditTrailHelper.getCurrentAuditTrailDto();
        List<Date> holidays = appointmentClient.getHolidays().getEntity();
        for(ApplicationDto applicationDto : applicationDtos){
            try {
                ApplicationGroupDto applicationGroupDto = inspectionTaskClient.getApplicationGroupDtoByAppGroId(applicationDto.getAppGrpId()).getEntity();
                String applicantId;
                if(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(applicationDto.getApplicationType()) ||
                        ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(applicationDto.getApplicationType())) {
                    applicantId = apptInspectionDateService.getAppSubmitByWithLicId(applicationDto.getOriginLicenceId());
                }else{
                    applicantId = applicationGroupDto.getSubmitBy();
                }
                OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicantId).getEntity();
                String applicantName = orgUserDto.getDisplayName();
                JobRemindMsgTrackingDto jobRemindMsgTrackingDto2 = systemBeLicClient.getJobRemindMsgTrackingDto(applicationDto.getApplicationNo(), MessageConstants.JOB_REMIND_MSG_KEY_REMIND_RECTIFICATION_EMAIL).getEntity();
                if(jobRemindMsgTrackingDto2 == null) {
                    log.info(StringUtil.changeForLog("jobRemindMsgTrackingDto2 null"));
                    JobLogger.log(StringUtil.changeForLog("jobRemindMsgTrackingDto2 null"));
                    inspectionDateSendEmail(applicantName, applicationDto, holidays);
                    createJobRemindMsgTrackingDto(intranet, applicationDto.getApplicationNo());
                } else {
                    Date createDate = jobRemindMsgTrackingDto2.getCreateTime();
                    log.info(StringUtil.changeForLog("jobRemindMsgTrackingDto2 not null, CreateDate = " + createDate));
                    JobLogger.log(StringUtil.changeForLog("jobRemindMsgTrackingDto2 not null, CreateDate = " + createDate));
                    List<Date> dayList = MiscUtil.getDateInPeriodByRecurrence(createDate, new Date());
                    int nowDays = dayList.size();
                    log.info(StringUtil.changeForLog("jobRemindMsgTrackingDto2 not null, nowDays = " + nowDays));
                    JobLogger.log(StringUtil.changeForLog("jobRemindMsgTrackingDto2 not null, nowDays = " + nowDays));
                    if(nowDays > days){
                        inspectionDateSendEmail(applicantName, applicationDto, holidays);
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

    private void inspectionDateSendEmail(String applicantName, ApplicationDto applicationDto, List<Date> holidays) {
        String appId = applicationDto.getId();
        String appNo = applicationDto.getApplicationNo();
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(appId).getEntity();
        List<InspEmailFieldDto> inspEmailFieldDtos = getEmailFieldByAppId(appPremisesCorrelationDto);
        inspEmailFieldDtos = inspectionRectificationProService.sortInspEmailFieldDtoByCategory(inspEmailFieldDtos);
        //get rec date
        List<ApptNonWorkingDateDto> nonWorkingDateListByWorkGroupId = inspectionRectificationProService.getApptNonWorkingDateByAppNo(appNo);
        AppPremisesRecommendationDto appPremisesRecommendationDto = insepctionNcCheckListService.getAppRecomDtoByAppCorrId(appPremisesCorrelationDto.getId(), InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
        Date date = WorkDayCalculateUtil.getDate(appPremisesRecommendationDto.getRecomInDate(), systemParamConfig.getRectificateDay(), holidays, nonWorkingDateListByWorkGroupId);
        String strDate = Formatter.formatDateTime(date, "dd/MM/yyyy");
        //get url
        String url = HmacConstants.HTTPS +"://"+systemParamConfig.getInterServerName() +
                MessageConstants.MESSAGE_INBOX_URL_USER_UPLOAD_RECTIFICATION + appNo;
        String emailUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
        HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
        maskParams.put("applicationNo", appNo);
        Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
        templateContent.put("applicant", applicantName);
        templateContent.put("date", strDate);
        templateContent.put("ncDtos", inspEmailFieldDtos);
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_REMIND_NC_RECTIFICATION);
        emailParam.setTemplateContent(templateContent);
        emailParam.setQueryCode(appNo);
        emailParam.setReqRefNum(appNo);
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        emailParam.setRefId(appNo);
        emailParam.setMaskParams(maskParams);
        templateContent.put("systemLink", emailUrl);
        notificationHelper.sendNotification(emailParam);
        EmailParam smsParam = new EmailParam();
        smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_REMIND_NC_RECTIFICATION_SMS);
        smsParam.setQueryCode(appNo);
        smsParam.setReqRefNum(appNo);
        smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
        smsParam.setRefId(appNo);
        smsParam.setSubject("MOH HALP - Reminder to submit documentary proof of rectification");
        notificationHelper.sendNotification(smsParam);
        EmailParam msgParam = new EmailParam();
        msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_REMIND_NC_RECTIFICATION_MSG);
        msgParam.setTemplateContent(templateContent);
        msgParam.setQueryCode(appNo);
        msgParam.setReqRefNum(appNo);
        msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_ACTION_REQUIRED);
        msgParam.setRefId(appNo);
        msgParam.setMaskParams(maskParams);
        templateContent.put("systemLink", url);
        //set svc code
        List<String> serviceCodes = IaisCommonUtils.genNewArrayList();
        String serviceId = applicationDto.getServiceId();
        HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
        String serviceCode = hcsaServiceDto.getSvcCode();
        serviceCodes.add(serviceCode);
        msgParam.setSvcCodeList(serviceCodes);
        notificationHelper.sendNotification(msgParam);
    }

    private List<InspEmailFieldDto> getEmailFieldByAppId(AppPremisesCorrelationDto appPremisesCorrelationDto) {
        List<InspEmailFieldDto> inspEmailFieldDtos = IaisCommonUtils.genNewArrayList();
        String appPremCorrId = appPremisesCorrelationDto.getId();
        //get vehicle no
        List<AppSvcVehicleDto> appSvcVehicleDtos = appSvcVehicleBeClient.getAppSvcVehicleDtoListByCorrId(appPremCorrId).getEntity();
        List<ChecklistConfigDto> checklistConfigDtos = getAllCheckListByAppPremCorrId(appPremCorrId);
        AppPremPreInspectionNcDto appPremPreInspectionNcDto = fillUpCheckListGetAppClient.getAppNcByAppCorrId(appPremCorrId).getEntity();
        List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtos = fillUpCheckListGetAppClient.getAppNcItemByNcId(appPremPreInspectionNcDto.getId()).getEntity();
        if(!IaisCommonUtils.isEmpty(appPremisesPreInspectionNcItemDtos)){
            for(AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto : appPremisesPreInspectionNcItemDtos){
                int recFlag = appPremisesPreInspectionNcItemDto.getIsRecitfied();
                if(0 == recFlag){
                    InspEmailFieldDto inspEmailFieldDto = setNcDataByItemId(checklistConfigDtos, appPremisesPreInspectionNcItemDto, appSvcVehicleDtos);
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

    private InspEmailFieldDto setNcDataByItemId(List<ChecklistConfigDto> checklistConfigDtos, AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto, List<AppSvcVehicleDto> appSvcVehicleDtos) {
        InspEmailFieldDto inspEmailFieldDto = new InspEmailFieldDto();
        String itemId = appPremisesPreInspectionNcItemDto.getItemId();
        String beRemark = appPremisesPreInspectionNcItemDto.getBeRemarks();
        String findNcs = appPremisesPreInspectionNcItemDto.getNcs();
        String checkListConfigId = appPremisesPreInspectionNcItemDto.getCheckListConfigId();
        String vehicleNo = inspectionRectificationProService.getVehicleShowName(appPremisesPreInspectionNcItemDto.getVehicleName(), appSvcVehicleDtos);
        if(StringUtil.isEmpty(beRemark)){
            beRemark = "";
        }
        if(StringUtil.isEmpty(findNcs)){
            findNcs = "";
        }
        if(StringUtil.isEmpty(vehicleNo)){
            vehicleNo = "";
        }
        if(!IaisCommonUtils.isEmpty(checklistConfigDtos)) {
            for (ChecklistConfigDto checklistConfigDto : checklistConfigDtos) {
                List<ChecklistItemDto> checklistItemDtos = getCurrentSvcAllItems(checklistConfigDto);
                if(!IaisCommonUtils.isEmpty(checklistItemDtos)){
                    inspEmailFieldDto = setFieldByItem(inspEmailFieldDto, checklistConfigDto, checklistItemDtos, itemId, beRemark, findNcs, vehicleNo, checkListConfigId);
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
            //CR Regulation change -> Question
            //CR question change -> Findings/NCs
            if(!StringUtil.isEmpty(checkItemId)){
                ChecklistItemDto checklistItemDto = inspectionRectificationProService.getChklItemById(checkItemId);
                inspEmailFieldDto.setRegulation(checklistItemDto.getChecklistItem());
                inspEmailFieldDto.setQuestion(findNcs);
            } else {
                inspEmailFieldDto.setRegulation(adhocChecklistItemDto.getQuestion());
                inspEmailFieldDto.setQuestion(findNcs);
            }
        }
        inspEmailFieldDto.setBeNcRemark(beRemark);
        inspEmailFieldDto.setServiceName(vehicleNo);
        return inspEmailFieldDto;
    }

    private InspEmailFieldDto setFieldByItem(InspEmailFieldDto inspEmailFieldDto, ChecklistConfigDto checklistConfigDto, List<ChecklistItemDto> checklistItemDtos,
                                             String itemId, String beRemark, String findNcs, String vehicleNo, String checkListConfigId) {
        if(inspEmailFieldDto == null){
            inspEmailFieldDto = new InspEmailFieldDto();
        }
        boolean containFlag = false;
        if(checklistConfigDto != null && !StringUtil.isEmpty(checkListConfigId)) {
            for (ChecklistItemDto checklistItemDto : checklistItemDtos) {
                if (itemId.equals(checklistItemDto.getItemId()) && checkListConfigId.equals(checklistConfigDto.getId())) {
                    //get category value
                    String category;
                    if (!StringUtil.isEmpty(vehicleNo)) {
                        category = vehicleNo;
                    } else {
                        category = getItemCategory(checklistConfigDto);
                    }
                    containFlag = true;
                    ChecklistItemDto clItemDto = hcsaChklClient.getChklItemById(itemId).getEntity();
                    //CR Regulation change -> Question
                    inspEmailFieldDto.setRegulation(clItemDto.getChecklistItem());
                    //CR question change -> Findings/NCs
                    inspEmailFieldDto.setQuestion(findNcs);
                    inspEmailFieldDto.setBeNcRemark(beRemark);
                    inspEmailFieldDto.setServiceName(category);
                }
            }
        }
        if(!containFlag){
            inspEmailFieldDto = null;
        }
        return inspEmailFieldDto;
    }

    private String getItemCategory(ChecklistConfigDto checklistConfigDto) {
        String category = "";
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
        log.debug(StringUtil.changeForLog("****The*****" + methodName +"******Start****"));
    }
}
