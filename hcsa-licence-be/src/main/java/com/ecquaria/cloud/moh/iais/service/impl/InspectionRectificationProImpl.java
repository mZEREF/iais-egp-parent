package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.client.AppEicClient;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inbox.InterMessageDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspRectificationSaveDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspecUserRecUploadDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionPreTaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewService;
import com.ecquaria.cloud.moh.iais.service.InboxMsgService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.InspectionRectificationProService;
import com.ecquaria.cloud.moh.iais.service.LicenseeService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesCorrClient;
import com.ecquaria.cloud.moh.iais.service.client.AppPremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailClient;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.FillUpCheckListGetAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.InsRepClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2019/12/18 14:27
 **/
@Service
public class InspectionRectificationProImpl implements InspectionRectificationProService {

    @Autowired
    private AppPremisesRoutingHistoryClient appPremisesRoutingHistoryClient;

    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private AppEicClient appEicClient;

    @Autowired
    private TaskService taskService;

    @Autowired
    private InspEmailService inspEmailService;

    @Autowired
    private EmailClient emailClient;

    @Autowired
    private ApplicationViewService applicationViewService;

    @Autowired
    private AppPremisesCorrClient appPremisesCorrClient;

    @Autowired
    private AppInspectionStatusClient appInspectionStatusClient;

    @Autowired
    private InsRepClient insRepClient;

    @Autowired
    private FileRepoClient fileRepoClient;

    @Autowired
    private InboxMsgService inboxMsgService;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private FillUpCheckListGetAppClient fillUpCheckListGetAppClient;

    @Autowired
    private InspectionTaskClient inspectionTaskClient;

    @Autowired
    private HcsaChklClient hcsaChklClient;

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;

    @Autowired
    private MsgTemplateClient msgTemplateClient;

    @Autowired
    private LicenseeService licenseeService;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Value("${iais.email.sender}")
    private String mailSender;

    static String[] processDecArr = new String[]
            {InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION, InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION,
                    InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION_CONDITION};
    @Override
    public AppPremisesRoutingHistoryDto getAppHistoryByTask(String appNo, String stageId) {
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.getAppPremisesRoutingHistorysByAppNoAndStageId(appNo, stageId).getEntity();
        if(appPremisesRoutingHistoryDto == null){
            appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        }
        return appPremisesRoutingHistoryDto;
    }

    @Override
    public List<SelectOption> getProcessRecDecOption() {

        return MasterCodeUtil.retrieveOptionsByCodes(processDecArr);
    }

    @Override
    public void routingTaskToReport(TaskDto taskDto, InspectionPreTaskDto inspectionPreTaskDto, ApplicationViewDto applicationViewDto,
                                    LoginContext loginContext) throws IOException, TemplateException {
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        InspRectificationSaveDto inspRectificationSaveDto = new InspRectificationSaveDto();

        //get licenseeId
        ApplicationGroupDto applicationGroupDto = applicationViewDto.getApplicationGroupDto();
        String licenseeId = applicationGroupDto.getLicenseeId();
        //get application, task score
        ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
        List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
        applicationDtos.add(applicationDto);
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = generateHcsaSvcStageWorkingGroupDtos(applicationDtos,HcsaConsts.ROUTING_STAGE_INS);
        hcsaSvcStageWorkingGroupDtos = taskService.getTaskConfig(hcsaSvcStageWorkingGroupDtos);

        List<TaskDto> taskDtoList = organizationClient.getCurrTaskByRefNo(taskDto.getRefNo()).getEntity();
        AuditTrailDto auditTrailDto = IaisEGPHelper.getCurrentAuditTrailDto();
        for(TaskDto tDto : taskDtoList){
            if(tDto.getTaskStatus().equals(TaskConsts.TASK_STATUS_PENDING) || tDto.getTaskStatus().equals(TaskConsts.TASK_STATUS_READ)) {
                tDto.setSlaDateCompleted(new Date());
                tDto.setSlaRemainInDays(0);
                tDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
                tDto.setAuditTrailDto(auditTrailDto);
                taskService.updateTask(tDto);
            }
        }
        updateInspectionStatus(taskDto.getRefNo(), InspectionConstants.INSPECTION_STATUS_PENDING_PREPARE_REPORT);

        if(InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION.equals(inspectionPreTaskDto.getSelectValue())){
            createAppPremisesRoutingHistory(applicationDto.getApplicationNo(), applicationDto.getStatus(),taskDto.getTaskKey(),inspectionPreTaskDto.getInternalMarks(), InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION, RoleConsts.USER_ROLE_INSPECTIOR, HcsaConsts.ROUTING_STAGE_POT, taskDto.getWkGrpId());
            createTaskForReport(hcsaSvcStageWorkingGroupDtos, taskDto, loginContext);
            ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT);
            applicationService.updateFEApplicaiton(applicationDto1);
            applicationViewDto.setApplicationDto(applicationDto1);
            createAppPremisesRoutingHistory(applicationDto1.getApplicationNo(),applicationDto1.getStatus(), HcsaConsts.ROUTING_STAGE_INS,null, null, RoleConsts.USER_ROLE_INSPECTIOR, HcsaConsts.ROUTING_STAGE_POT, taskDto.getWkGrpId());

            inspRectificationSaveDto = getUpdateItemNcList(inspectionPreTaskDto, inspRectificationSaveDto);
            //save eic record
            EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.InspectionRectificationProImpl", "routingTaskToReport",
                    "hcsa-licence-web-intranet", InspRectificationSaveDto.class.getName(), JsonUtil.parseToJson(inspRectificationSaveDto));
            String eicRefNo = eicRequestTrackingDto.getRefNo();
            beEicGatewayClient.beCreateNcData(inspRectificationSaveDto, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());
            //get eic record
            eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
            //update eic record status
            eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
            eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
            eicRequestTrackingDtos.add(eicRequestTrackingDto);
            appEicClient.updateStatus(eicRequestTrackingDtos);
        } else if (InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION_CONDITION.equals(inspectionPreTaskDto.getSelectValue())){
            createAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),taskDto.getTaskKey(),inspectionPreTaskDto.getInternalMarks(), InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION_CONDITION, RoleConsts.USER_ROLE_INSPECTIOR, HcsaConsts.ROUTING_STAGE_POT, taskDto.getWkGrpId());
            createRecommendation(inspectionPreTaskDto, applicationViewDto);
            createTaskForReport(hcsaSvcStageWorkingGroupDtos, taskDto, loginContext);
            ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_REPORT);
            applicationService.updateFEApplicaiton(applicationDto1);
            applicationViewDto.setApplicationDto(applicationDto1);
            createAppPremisesRoutingHistory(applicationDto1.getApplicationNo(),applicationDto1.getStatus(), HcsaConsts.ROUTING_STAGE_INS,null, null, RoleConsts.USER_ROLE_INSPECTIOR, HcsaConsts.ROUTING_STAGE_POT, taskDto.getWkGrpId());

            inspRectificationSaveDto = getUpdateItemNcList(inspectionPreTaskDto, inspRectificationSaveDto);
            //save eic record
            EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.InspectionRectificationProImpl", "routingTaskToReport",
                    "hcsa-licence-web-intranet", InspRectificationSaveDto.class.getName(), JsonUtil.parseToJson(inspRectificationSaveDto));
            String eicRefNo = eicRequestTrackingDto.getRefNo();
            beEicGatewayClient.beCreateNcData(inspRectificationSaveDto, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());
            //get eic record
            eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
            //update eic record status
            eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
            eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
            eicRequestTrackingDtos.add(eicRequestTrackingDto);
            appEicClient.updateStatus(eicRequestTrackingDtos);
        } else if (InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION.equals(inspectionPreTaskDto.getSelectValue())){
            createAppPremisesRoutingHistory(applicationDto.getApplicationNo(),applicationDto.getStatus(),taskDto.getTaskKey(),inspectionPreTaskDto.getInternalMarks(), InspectionConstants.PROCESS_DECI_ACCEPTS_RECTIFICATION_CONDITION, RoleConsts.USER_ROLE_INSPECTIOR, HcsaConsts.ROUTING_STAGE_POT, taskDto.getWkGrpId());
            ApplicationDto applicationDto1 = updateApplication(applicationDto, ApplicationConsts.APPLICATION_STATUS_PENDING_NC_RECTIFICATION_RFI);
            applicationService.updateFEApplicaiton(applicationDto1);
            updateInspectionStatus(taskDto.getRefNo(), InspectionConstants.INSPECTION_STATUS_PENDING_REQUEST_FOR_INFORMATION);

            //create new version
            String version = getVersionAddOne(taskDto);
            //update rfi nc
            inspRectificationSaveDto = getRfiUpdateItemNcList(inspectionPreTaskDto, inspRectificationSaveDto);
            //save eic record
            EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.InspectionRectificationProImpl", "routingTaskToReport",
                    "hcsa-licence-web-intranet", InspRectificationSaveDto.class.getName(), JsonUtil.parseToJson(inspRectificationSaveDto));
            String eicRefNo = eicRequestTrackingDto.getRefNo();
            beEicGatewayClient.beCreateNcData(inspRectificationSaveDto, signature.date(), signature.authorization(),
                    signature2.date(), signature2.authorization());
            //get eic record
            eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
            //update eic record status
            eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
            eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
            eicRequestTrackingDtos.add(eicRequestTrackingDto);
            appEicClient.updateStatus(eicRequestTrackingDtos);
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(applicationDto.getServiceId());
            String serviceCode = hcsaServiceDto.getSvcCode();
            InterMessageDto interMessageDto = new InterMessageDto();
            interMessageDto.setSrcSystemId(AppConsts.MOH_IAIS_SYSTEM_INBOX_CLIENT_KEY);
            interMessageDto.setSubject(MessageConstants.MESSAGE_SUBJECT_REQUEST_FOR_INFORMATION);
            interMessageDto.setMessageType(MessageConstants.MESSAGE_TYPE_ACTION_REQUIRED);
            String mesNO = inboxMsgService.getMessageNo();
            interMessageDto.setRefNo(mesNO);
            interMessageDto.setService_id(serviceCode+"@");
            String url = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() +
                    MessageConstants.MESSAGE_INBOX_URL_USER_UPLOAD_RECTIFICATION +
                    taskDto.getRefNo() + "&recVersion=" + version;
            HashMap<String, String> maskParams = IaisCommonUtils.genNewHashMap();
            maskParams.put("appPremCorrId", taskDto.getRefNo());
            MsgTemplateDto mtd = msgTemplateClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_NC_RECTIFICATION).getEntity();
            Map<String, Object> params = IaisCommonUtils.genNewHashMap();
            params.put("process_url", url);
            LicenseeDto licDto = licenseeService.getLicenseeDtoById(applicationViewDto.getApplicationGroupDto().getLicenseeId());
            params.put("applicant_name", StringUtil.viewHtml(licDto.getName()));
            params.put("hci_code", StringUtil.viewHtml(applicationViewDto.getHciCode()));
            params.put("hci_name", StringUtil.viewHtml(applicationViewDto.getHciName()));
            params.put("service_name", StringUtil.viewHtml(HcsaServiceCacheHelper.getServiceNameById
                    (applicationViewDto.getApplicationDto().getServiceId())));
            params.put("application_number", applicationViewDto.getApplicationDto().getApplicationNo());
            String templateMessageByContent = MsgUtil.getTemplateMessageByContent(mtd.getMessageContent(), params);
            interMessageDto.setMsgContent(templateMessageByContent);
            interMessageDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
            interMessageDto.setUserId(licenseeId);
            interMessageDto.setMaskParams(maskParams);
            interMessageDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            inboxMsgService.saveInterMessage(interMessageDto);

            InspectionEmailTemplateDto inspectionEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_NC_RECTIFICATION);
            if(inspectionEmailTemplateDto != null) {
                EmailDto emailDto = new EmailDto();
                emailDto.setContent(templateMessageByContent);
                emailDto.setSubject(inspectionEmailTemplateDto.getSubject());
                emailDto.setSender(mailSender);
                emailDto.setReceipts(IaisEGPHelper.getLicenseeEmailAddrs(licenseeId));
                emailDto.setClientQueryCode(taskDto.getId());
                emailClient.sendNotification(emailDto);
            }
        }
    }

    private InspRectificationSaveDto getRfiUpdateItemNcList(InspectionPreTaskDto inspectionPreTaskDto, InspRectificationSaveDto inspRectificationSaveDto) {
        List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtos = IaisCommonUtils.genNewArrayList();
        List<String> checkRecRfiNcItems = inspectionPreTaskDto.getCheckRecRfiNcItems();
        for(InspecUserRecUploadDto inspecUserRecUploadDto : inspectionPreTaskDto.getInspecUserRecUploadDtos()){
            AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto = inspecUserRecUploadDto.getAppPremisesPreInspectionNcItemDto();
            if(appPremisesPreInspectionNcItemDto != null){
                appPremisesPreInspectionNcItemDto.setIsRecitfied(1);
                appPremisesPreInspectionNcItemDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                for(String checkRecRfiNcItem : checkRecRfiNcItems) {
                    if(checkRecRfiNcItem.equals(appPremisesPreInspectionNcItemDto.getId())) {
                        appPremisesPreInspectionNcItemDto.setIsRecitfied(0);
                        appPremisesPreInspectionNcItemDto.setFeRectifiedFlag(0);
                    }
                }
                appPremisesPreInspectionNcItemDtos.add(appPremisesPreInspectionNcItemDto);
            }
        }
        appPremisesPreInspectionNcItemDtos = fillUpCheckListGetAppClient.saveAppPreNcItem(appPremisesPreInspectionNcItemDtos).getEntity();
        inspRectificationSaveDto.setAppPremisesPreInspectionNcItemDtos(appPremisesPreInspectionNcItemDtos);
        inspRectificationSaveDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return inspRectificationSaveDto;
    }

    private String getVersionAddOne(TaskDto taskDto) {
        String appPremCorrId = taskDto.getRefNo();
        AppPremPreInspectionNcDto appPremPreInspectionNcDto = fillUpCheckListGetAppClient.getAppNcByAppCorrId(appPremCorrId).getEntity();
        String curVersionStr = appPremPreInspectionNcDto.getVersion();
        curVersionStr = curVersionStr.trim();
        int curVersion = Integer.parseInt(curVersionStr);
        String version = curVersion + 1 + "";

        appPremPreInspectionNcDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
        appPremPreInspectionNcDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        fillUpCheckListGetAppClient.updateAppPreNc(appPremPreInspectionNcDto).getEntity();

        return version;
    }

    private InspRectificationSaveDto getUpdateItemNcList(InspectionPreTaskDto inspectionPreTaskDto, InspRectificationSaveDto inspRectificationSaveDto) {
        List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtos = IaisCommonUtils.genNewArrayList();
        for(InspecUserRecUploadDto inspecUserRecUploadDto : inspectionPreTaskDto.getInspecUserRecUploadDtos()){
            AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto = inspecUserRecUploadDto.getAppPremisesPreInspectionNcItemDto();
            if(appPremisesPreInspectionNcItemDto != null){
                appPremisesPreInspectionNcItemDto.setIsRecitfied(1);
                appPremisesPreInspectionNcItemDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                appPremisesPreInspectionNcItemDtos.add(appPremisesPreInspectionNcItemDto);
            }
        }
        appPremisesPreInspectionNcItemDtos = fillUpCheckListGetAppClient.saveAppPreNcItem(appPremisesPreInspectionNcItemDtos).getEntity();
        inspRectificationSaveDto.setAppPremisesPreInspectionNcItemDtos(appPremisesPreInspectionNcItemDtos);
        inspRectificationSaveDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return inspRectificationSaveDto;
    }

    @Override
    public List<ChecklistItemDto> getQuesAndClause(String appPremCorrId) {
        List<ChecklistItemDto> checklistItemDtos = IaisCommonUtils.genNewArrayList();
        if(!(StringUtil.isEmpty(appPremCorrId))){
            List<String> itemIds = inspectionTaskClient.getItemIdsByAppNo(appPremCorrId).getEntity();
            checklistItemDtos = getCheckDtosByItemIds(itemIds);
        }
        return checklistItemDtos;
    }

    @Override
    public List<FileRepoDto> getFileByItemId(List<AppPremPreInspectionNcDocDto> appPremPreInspectionNcDocDtos) {
        if(!IaisCommonUtils.isEmpty(appPremPreInspectionNcDocDtos)){
            List<String> fileIds = IaisCommonUtils.genNewArrayList();
            for(AppPremPreInspectionNcDocDto appInspNcDoc : appPremPreInspectionNcDocDtos){
                fileIds.add(appInspNcDoc.getFileRepoId());
            }
            List<FileRepoDto> fileRepoDtos = fileRepoClient.getFilesByIds(fileIds).getEntity();
            return fileRepoDtos;
        }
        return null;
    }

    @Override
    public List<AppPremPreInspectionNcDocDto> getAppNcDocList(String id) {
        List<AppPremPreInspectionNcDocDto> appPremPreInspectionNcDocDtos = inspectionTaskClient.getFilesByItemId(id).getEntity();
        return appPremPreInspectionNcDocDtos;
    }

    @Override
    public byte[] downloadFile(String fileRepoId) {
        return fileRepoClient.getFileFormDataBase(fileRepoId).getEntity();
    }

    @Override
    public AppPremPreInspectionNcDto getAppPremPreInspectionNcDtoByCorrId(String refNo) {
        AppPremPreInspectionNcDto appPremPreInspectionNcDto = fillUpCheckListGetAppClient.getAppNcByAppCorrId(refNo).getEntity();
        return appPremPreInspectionNcDto;
    }

    @Override
    public Map<String, AppPremisesPreInspectionNcItemDto> getNcItemDtoMap(String id) {
        Map<String, AppPremisesPreInspectionNcItemDto> ncItemDtoMap = IaisCommonUtils.genNewHashMap();
        List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtos = fillUpCheckListGetAppClient.getAppNcItemByNcId(id).getEntity();
        if(!IaisCommonUtils.isEmpty(appPremisesPreInspectionNcItemDtos)){
            for(AppPremisesPreInspectionNcItemDto appPremisesPreInspectionNcItemDto : appPremisesPreInspectionNcItemDtos) {
                ncItemDtoMap.put(appPremisesPreInspectionNcItemDto.getId(), appPremisesPreInspectionNcItemDto);
            }
        }
        return ncItemDtoMap;
    }

    @Override
    public List<String> getInspectorLeadsByWorkGroupId(String workGroupId) {
        List<String> inspectorLeads = IaisCommonUtils.genNewArrayList();
        List<OrgUserDto> orgUserDtos = organizationClient.getUsersByWorkGroupName(workGroupId, AppConsts.COMMON_STATUS_ACTIVE).getEntity();
        List<String> leadIds = organizationClient.getInspectionLead(workGroupId).getEntity();
        for(String id : leadIds){
            for(OrgUserDto oDto : orgUserDtos){
                if(id.equals(oDto.getId())){
                    inspectorLeads.add(oDto.getDisplayName());
                }
            }
        }
        return inspectorLeads;
    }

    @Override
    public int getHowMuchNcByAppPremCorrId(String refNo) {
        int ncCount = 0;
        AppPremPreInspectionNcDto appPremPreInspectionNcDto = getAppPremPreInspectionNcDtoByCorrId(refNo);
        List<AppPremisesPreInspectionNcItemDto> appPremisesPreInspectionNcItemDtos = fillUpCheckListGetAppClient.getAppNcItemByNcId(appPremPreInspectionNcDto.getId()).getEntity();
        if(!IaisCommonUtils.isEmpty(appPremisesPreInspectionNcItemDtos)){
            ncCount = appPremisesPreInspectionNcItemDtos.size();
        }
        return ncCount;
    }

    @Override
    public FileRepoDto getFileReportById(String fileRepoId) {
        if(!StringUtil.isEmpty(fileRepoId)){
            List<String> fileIds = IaisCommonUtils.genNewArrayList();
            fileIds.add(fileRepoId);
            List<FileRepoDto> fileRepoDtos = fileRepoClient.getFilesByIds(fileIds).getEntity();
            if(!IaisCommonUtils.isEmpty(fileRepoDtos)) {
                FileRepoDto fileRepoDto = fileRepoDtos.get(0);
                return fileRepoDto;
            }
        }
        return null;
    }

    @Override
    public AdhocChecklistItemDto getAdhocChecklistItemById(String adhocItemId) {
        AdhocChecklistItemDto adhocChecklistItemDto = new AdhocChecklistItemDto();
        if(!StringUtil.isEmpty(adhocItemId)){
            adhocChecklistItemDto = applicationClient.getAdhocChecklistItemById(adhocItemId).getEntity();
        }
        return adhocChecklistItemDto;
    }

    @Override
    public ChecklistItemDto getChklItemById(String itemId) {
        ChecklistItemDto checklistItemDto = new ChecklistItemDto();
        if(!StringUtil.isEmpty(itemId)){
            checklistItemDto = hcsaChklClient.getChklItemById(itemId).getEntity();
        }
        return checklistItemDto;
    }

    private List<ChecklistItemDto> getCheckDtosByItemIds(List<String> itemIds) {
        List<ChecklistItemDto> checklistItemDtos = IaisCommonUtils.genNewArrayList();
        if(itemIds != null && !(itemIds.isEmpty())) {
            for (String itemId:itemIds) {
                ChecklistItemDto checklistItemDto = hcsaChklClient.getChklItemById(itemId).getEntity();
                if(checklistItemDto != null) {
                    checklistItemDtos.add(checklistItemDto);
                }
            }
        }
        return checklistItemDtos;
    }

    private List<HcsaSvcStageWorkingGroupDto> generateHcsaSvcStageWorkingGroupDtos(List<ApplicationDto> applicationDtos, String stageId){
        List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos = IaisCommonUtils.genNewArrayList();
        for(ApplicationDto applicationDto : applicationDtos){
            HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
            hcsaSvcStageWorkingGroupDto.setStageId(stageId);
            hcsaSvcStageWorkingGroupDto.setServiceId(applicationDto.getServiceId());
            hcsaSvcStageWorkingGroupDto.setType(applicationDto.getApplicationType());
            hcsaSvcStageWorkingGroupDtos.add(hcsaSvcStageWorkingGroupDto);
        }
        return hcsaSvcStageWorkingGroupDtos;
    }

    private void createRecommendation(InspectionPreTaskDto inspectionPreTaskDto, ApplicationViewDto applicationViewDto) {
        AppPremisesRecommendationDto appPremisesRecommendationDto = new AppPremisesRecommendationDto();
        AppPremisesCorrelationDto appPremisesCorrelationDto = getAppPreCorrDtoByAppDto(applicationViewDto.getApplicationDto());
        appPremisesRecommendationDto.setId(null);
        appPremisesRecommendationDto.setAppPremCorreId(appPremisesCorrelationDto.getId());
        appPremisesRecommendationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRecommendationDto.setBestPractice(null);
        appPremisesRecommendationDto.setRecomType(InspectionConstants.RECOM_TYPE_INSPECTYPE);
        appPremisesRecommendationDto.setChronoUnit(null);
        appPremisesRecommendationDto.setRecomDecision(inspectionPreTaskDto.getSelectValue());
        appPremisesRecommendationDto.setRecomInDate(new Date());
        appPremisesRecommendationDto.setVersion(1);
        appPremisesRecommendationDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        appPremisesRecommendationDto.setRemarks(inspectionPreTaskDto.getAccCondMarks());
        appPremisesRecommendationDto.setRecomInNumber(null);
        insRepClient.saveRecommendationData(appPremisesRecommendationDto);
    }

    private void createTaskForReport(List<HcsaSvcStageWorkingGroupDto> hcsaSvcStageWorkingGroupDtos, TaskDto td, LoginContext loginContext) {
        TaskDto taskDto = new TaskDto();
        List<TaskDto> taskDtos = IaisCommonUtils.genNewArrayList();
        taskDto.setId(null);
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_PENDING);
        taskDto.setPriority(td.getPriority());
        taskDto.setRefNo(td.getRefNo());
        taskDto.setScore(hcsaSvcStageWorkingGroupDtos.get(0).getCount());
        taskDto.setSlaAlertInDays(td.getSlaAlertInDays());
        taskDto.setSlaDateCompleted(null);
        taskDto.setSlaInDays(td.getSlaInDays());
        taskDto.setSlaRemainInDays(null);
        taskDto.setTaskKey(td.getTaskKey());
        taskDto.setTaskType(td.getTaskType());
        taskDto.setWkGrpId(td.getWkGrpId());
        taskDto.setUserId(loginContext.getUserId());
        taskDto.setDateAssigned(new Date());
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        taskDto.setProcessUrl(TaskConsts.TASK_PROCESS_URL_INSPECTION_REPORT);
        taskDto.setRoleId(RoleConsts.USER_ROLE_INSPECTIOR);
        taskDto.setApplicationNo(td.getApplicationNo());
        taskDtos.add(taskDto);
        taskService.createTasks(taskDtos);
    }
    private AppPremisesCorrelationDto getAppPreCorrDtoByAppDto(ApplicationDto applicationDto){
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtos =  appPremisesCorrClient.getAppPremisesCorrelationsByAppId(applicationDto.getId()).getEntity();
        AppPremisesCorrelationDto appPremisesCorrelationDto = new AppPremisesCorrelationDto();
        if(appPremisesCorrelationDtos != null && !(appPremisesCorrelationDtos.isEmpty())){
            appPremisesCorrelationDto = appPremisesCorrelationDtos.get(0);
        }
        return appPremisesCorrelationDto;
    }
    private void updateInspectionStatus(String appPremCorreId, String status) {
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(appPremCorreId).getEntity();
        appInspectionStatusDto.setStatus(status);
        appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appInspectionStatusClient.update(appInspectionStatusDto);
    }

    private ApplicationDto updateApplication(ApplicationDto applicationDto, String appStatus) {
        applicationDto.setStatus(appStatus);
        applicationDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return applicationViewService.updateApplicaiton(applicationDto);
    }

    private AppPremisesRoutingHistoryDto createAppPremisesRoutingHistory(String appNo, String appStatus, String stageId, String internalRemarks,
                                                                         String processDec, String roleId, String subStage, String workGroupId){
        AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto = new AppPremisesRoutingHistoryDto();
        appPremisesRoutingHistoryDto.setApplicationNo(appNo);
        appPremisesRoutingHistoryDto.setStageId(stageId);
        appPremisesRoutingHistoryDto.setInternalRemarks(internalRemarks);
        appPremisesRoutingHistoryDto.setAppStatus(appStatus);
        appPremisesRoutingHistoryDto.setActionby(IaisEGPHelper.getCurrentAuditTrailDto().getMohUserGuid());
        appPremisesRoutingHistoryDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appPremisesRoutingHistoryDto.setProcessDecision(processDec);
        appPremisesRoutingHistoryDto.setRoleId(roleId);
        appPremisesRoutingHistoryDto.setSubStage(subStage);
        appPremisesRoutingHistoryDto.setWrkGrpId(workGroupId);
        appPremisesRoutingHistoryDto = appPremisesRoutingHistoryClient.createAppPremisesRoutingHistory(appPremisesRoutingHistoryDto).getEntity();
        return appPremisesRoutingHistoryDto;
    }

}
