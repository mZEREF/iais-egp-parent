package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
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
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonWorkingDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspEmailFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspRectificationSaveDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.JobRemindMsgTrackingDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.ApptInspectionDateService;
import com.ecquaria.cloud.moh.iais.service.InsepctionNcCheckListService;
import com.ecquaria.cloud.moh.iais.service.InspectionRectificationProService;
import com.ecquaria.cloud.moh.iais.service.client.AppSvcVehicleBeClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.AppointmentClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemBeLicClient;
import com.ecquaria.cloud.moh.iais.util.WorkDayCalculateUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2019/12/19 19:37
 **/
@Delegator("inspectionSendRecDelegator")
@Slf4j
public class InspectionSendRecBatchjob {
    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    private HcsaChklClient hcsaChklClient;

    @Autowired
    private InspectionRectificationProService inspectionRectificationProService;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private SystemBeLicClient systemBeLicClient;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;

    @Autowired
    private ApptInspectionDateService apptInspectionDateService;

    @Autowired
    private AppointmentClient appointmentClient;

    @Autowired
    private InsepctionNcCheckListService insepctionNcCheckListService;

    @Autowired
    private AppSvcVehicleBeClient appSvcVehicleBeClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Autowired
    private InspectionSendRecBatchjob(SystemBeLicClient systemBeLicClient, SystemParamConfig systemParamConfig, FillUpCheckListGetAppClient fillUpCheckListGetAppClient){
        this.fillUpCheckListGetAppClient = fillUpCheckListGetAppClient;
        this.systemParamConfig = systemParamConfig;
        this.systemBeLicClient = systemBeLicClient;
    }
    /**
     * StartStep: mohInspecSendRectifiToUserStart
     *
     * @param bpc
     * @throws
     */
    public void mohInspecSendRectifiToUserStart(BaseProcessClass bpc){
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        log.debug(StringUtil.changeForLog("the mohInspecSendRectifiToUserStart start ...."));
    }

    /**
     * StartStep: mohInspecSendRectifiToUserPre
     *
     * @param bpc
     * @throws
     */
    public void mohInspecSendRectifiToUserPre(BaseProcessClass bpc) throws IOException, TemplateException {
        log.debug(StringUtil.changeForLog("the mohInspecSendRectifiToUserPre start ...."));
        List<ApplicationViewDto> mapApp =  fillUpCheckListGetAppClient.getApplicationDtoByNcItem().getEntity();
        if(IaisCommonUtils.isEmpty(mapApp)){
            return;
        }
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        List<AppPremPreInspectionNcDto> appPremPreInspectionNcDtos = IaisCommonUtils.genNewArrayList();
        List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtos = IaisCommonUtils.genNewArrayList();
        InspRectificationSaveDto inspRectificationSaveDto = new InspRectificationSaveDto();

        AuditTrailDto intranet = AuditTrailHelper.getCurrentAuditTrailDto();
        List<Date> holidays = appointmentClient.getHolidays().getEntity();
        for(ApplicationViewDto dto : mapApp){
            ApplicationDto aDto = dto.getApplicationDto();
            String appPremCorrId = dto.getAppPremisesCorrelationId();
            JobRemindMsgTrackingDto jobRemindMsgTrackingDto2 = systemBeLicClient.getJobRemindMsgTrackingDto(aDto.getId(), MessageConstants.JOB_REMIND_MSG_KEY_SEND_REC_TO_FE).getEntity();
            if(jobRemindMsgTrackingDto2 == null) {
                List<InspEmailFieldDto> inspEmailFieldDtos = getEmailFieldByAppId(aDto.getId());
                inspEmailFieldDtos = inspectionRectificationProService.sortInspEmailFieldDtoByCategory(inspEmailFieldDtos);
                String applicantId;
                if(ApplicationConsts.APPLICATION_TYPE_POST_INSPECTION.equals(aDto.getApplicationType()) ||
                        ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(aDto.getApplicationType())) {
                    applicantId = apptInspectionDateService.getAppSubmitByWithLicId(aDto.getOriginLicenceId());
                }else{
                    applicantId = dto.getApplicationGroupDto().getSubmitBy();
                }
                OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicantId).getEntity();
                String applicantName = orgUserDto.getDisplayName();
                String appNo = aDto.getApplicationNo();
                //get rec date
                List<ApptNonWorkingDateDto> nonWorkingDateListByWorkGroupId = inspectionRectificationProService.getApptNonWorkingDateByAppNo(appNo);
                AppPremisesRecommendationDto appPremisesRecommendationDto = insepctionNcCheckListService.getAppRecomDtoByAppCorrId(appPremCorrId, InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
                Date date = WorkDayCalculateUtil.getDate(appPremisesRecommendationDto.getRecomInDate(), systemParamConfig.getRectificateDay(), holidays, nonWorkingDateListByWorkGroupId);
                String strDate = Formatter.formatDateTime(date, "dd/MM/yyyy");
                //get url
                String url = HmacConstants.HTTPS +"://"+systemParamConfig.getInterServerName() +
                        MessageConstants.MESSAGE_INBOX_URL_USER_UPLOAD_RECTIFICATION + appNo;
                HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
                maskParams.put("applicationNo", appNo);
                //set map
                Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
                templateContent.put("applicant", applicantName);
                templateContent.put("date", strDate);
                templateContent.put("ncDtos", inspEmailFieldDtos);
                templateContent.put("systemLink", url);
                EmailParam emailParam = new EmailParam();
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_SEND_RECTIFICATION_MSG);
                emailParam.setTemplateContent(templateContent);
                emailParam.setQueryCode(appNo);
                emailParam.setReqRefNum(appNo);
                emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_ACTION_REQUIRED);
                emailParam.setRefId(appNo);
                emailParam.setMaskParams(maskParams);
                //set svc code
                List<String> serviceCodes = IaisCommonUtils.genNewArrayList();
                String serviceId = aDto.getServiceId();
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
                String serviceCode = hcsaServiceDto.getSvcCode();
                serviceCodes.add(serviceCode);
                emailParam.setSvcCodeList(serviceCodes);
                notificationHelper.sendNotification(emailParam);

                aDto.setAuditTrailDto(intranet);
                aDto.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION);
                applicationViewService.updateApplicaiton(aDto);
                applicationService.updateFEApplicaiton(aDto);

                AppPremPreInspectionNcDto appPremPreInspectionNcDto = fillUpCheckListGetAppClient.getAppNcByAppCorrId(appPremCorrId).getEntity();
                appPremPreInspectionNcDto.setApplicationNo(appNo);
                appPremPreInspectionNcDtos.add(appPremPreInspectionNcDto);
                List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtoList = fillUpCheckListGetAppClient.getAppNcItemByNcId(appPremPreInspectionNcDto.getId()).getEntity();
                if(!IaisCommonUtils.isEmpty(appPremisesPreInspectionNcItemDtoList)){
                    for(AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto : appPremisesPreInspectionNcItemDtoList){
                        appPremisesPreInspectionNcItemDtos.add(appPremisesPreInspectionNcItemDto);
                    }
                }

                List<JobRemindMsgTrackingDto> jobRemindMsgTrackingDtos = IaisCommonUtils.genNewArrayList();
                JobRemindMsgTrackingDto jobRemindMsgTrackingDto = new JobRemindMsgTrackingDto();
                jobRemindMsgTrackingDto.setAuditTrailDto(intranet);
                jobRemindMsgTrackingDto.setMsgKey(MessageConstants.JOB_REMIND_MSG_KEY_SEND_REC_TO_FE);
                jobRemindMsgTrackingDto.setRefNo(aDto.getId());
                jobRemindMsgTrackingDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
                jobRemindMsgTrackingDto.setId(null);
                jobRemindMsgTrackingDtos.add(jobRemindMsgTrackingDto);
                systemBeLicClient.createJobRemindMsgTrackingDtos(jobRemindMsgTrackingDtos);
            }
        }
        inspRectificationSaveDto.setAppPremPreInspectionNcDtos(appPremPreInspectionNcDtos);
        inspRectificationSaveDto.setAppPremisesPreInspectionNcItemDtos(appPremisesPreInspectionNcItemDtos);
        inspRectificationSaveDto.setAuditTrailDto(intranet);
        beEicGatewayClient.beCreateNcData(inspRectificationSaveDto, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization());
    }

    private List<InspEmailFieldDto> getEmailFieldByAppId(String appId) {
        List<InspEmailFieldDto> inspEmailFieldDtos = IaisCommonUtils.genNewArrayList();
        AppPremisesCorrelationDto appPremisesCorrelationDto = applicationClient.getAppPremisesCorrelationDtosByAppId(appId).getEntity();
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

    private InspEmailFieldDto setNcDataByItemId(List<ChecklistConfigDto> checklistConfigDtos, AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto,
                                                List<AppSvcVehicleDto> appSvcVehicleDtos) {
        InspEmailFieldDto inspEmailFieldDto = new InspEmailFieldDto();
        String itemId = appPremisesPreInspectionNcItemDto.getItemId();
        String beRemark = appPremisesPreInspectionNcItemDto.getBeRemarks();
        String findNcs = appPremisesPreInspectionNcItemDto.getNcs();
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
                    inspEmailFieldDto = setFieldByItem(inspEmailFieldDto, checklistConfigDto, checklistItemDtos, itemId, beRemark, findNcs, vehicleNo);
                    if(inspEmailFieldDto != null){
                        return inspEmailFieldDto;
                    }
                }
            }
        }
        //There are no matching items, search item from adhoc table
        String adhocItemId = itemId;
        AdhocChecklistItemDto adhocChecklistItemDto = inspectionRectificationProService.getAdhocChecklistItemById(adhocItemId);
        if(inspEmailFieldDto == null){
            inspEmailFieldDto = new InspEmailFieldDto();
        }
        //CR Regulation change -> Question
        //CR question change -> Findings/NCs
        if(adhocChecklistItemDto != null){
            String checkItemId = adhocChecklistItemDto.getItemId();
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
                                             String itemId, String beRemark, String findNcs, String vehicleNo) {
        boolean containFlag = false;
        for(ChecklistItemDto checklistItemDto : checklistItemDtos){
            if(itemId.equals(checklistItemDto.getItemId())){
                log.info(StringUtil.changeForLog("itemId Id = " + itemId));
                //get category value
                String category;
                if(!StringUtil.isEmpty(vehicleNo)) {
                    category = vehicleNo;
                } else {
                    category = getItemCategory(checklistConfigDto);
                }
                containFlag = true;
                ChecklistItemDto clItemDto = hcsaChklClient.getChklItemById(itemId).getEntity();
                if(clItemDto == null){
                    log.info(StringUtil.changeForLog("clItemDto == null"));
                    clItemDto = new ChecklistItemDto();
                    clItemDto.setRegulationClause("");
                    clItemDto.setChecklistItem("");
                }
                if(inspEmailFieldDto == null){
                    inspEmailFieldDto = new InspEmailFieldDto();
                }
                //CR Regulation change -> Question
                inspEmailFieldDto.setRegulation(clItemDto.getChecklistItem());
                //CR question change -> Findings/NCs
                inspEmailFieldDto.setQuestion(findNcs);
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
}
