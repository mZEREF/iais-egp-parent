package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.acra.AcraConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.BroadcastApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.PaymentRequestDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaRiskScoreDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicInspectionGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremInspGrpCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.PostInsGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.BroadcastOrganizationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.UserGroupCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.ApplicationViewMainService;
import com.ecquaria.cloud.moh.iais.service.BeDashboardSupportService;
import com.ecquaria.cloud.moh.iais.service.InspectionMainService;
import com.ecquaria.cloud.moh.iais.service.MohHcsaBeDashboardService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusMainClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationMainClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationMainClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigMainClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.MsgTemplateMainClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import com.ecquaria.cloudfeign.FeignException;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2021/4/14 14:55
 **/
@Service
@Slf4j
public class BeDashboardSupportServiceImpl implements BeDashboardSupportService {

    @Autowired
    private MohHcsaBeDashboardService mohHcsaBeDashboardService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private BeDashboardSupportService beDashboardSupportService;

    @Autowired
    private InspectionMainService inspectionMainService;

    @Autowired
    private ApplicationViewMainService applicationViewService;

    @Autowired
    private GenerateIdClient generateIdClient;

    @Autowired
    private OrganizationMainClient organizationMainClient;

    @Autowired
    private CessationMainClient cessationClient;

    @Autowired
    private MsgTemplateMainClient msgTemplateMainClient;

    @Autowired
    private HcsaConfigMainClient hcsaConfigMainClient;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    private ApplicationMainClient applicationMainClient;

    @Value("${iais.email.sender}")
    private String mailSender;

    @Value("${iais.system.one.address}")
    private String systemAddressOne;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private LicenceClient licenceClient;

    @Autowired
    private AppInspectionStatusMainClient appInspectionStatusMainClient;

    @Override
    public void saveRejectReturnFee(List<ApplicationDto> applicationDtos, BroadcastApplicationDto broadcastApplicationDto){
        List<AppReturnFeeDto> saveReturnFeeDtos = IaisCommonUtils.genNewArrayList();
        //save return fee
        for(ApplicationDto applicationDto : applicationDtos){
            log.info(StringUtil.changeForLog("**** saveRejectReturnFee applicationDto ***** " + applicationDto.getApplicationNo()));
            log.info(StringUtil.changeForLog("**** saveRejectReturnFee applicationDto ***** " + applicationDto.getStatus()));
            if(ApplicationConsts.APPLICATION_STATUS_REJECTED.equals(applicationDto.getStatus())){
                AppReturnFeeDto appReturnFeeDto = new AppReturnFeeDto();
                Double returnFee = applicationDto.getReturnFee();
                if(returnFee==null || MiscUtil.doubleEquals(returnFee, 0d)){
                    continue;
                }
                appReturnFeeDto.setReturnAmount(returnFee);
                appReturnFeeDto.setApplicationNo(applicationDto.getApplicationNo());
                appReturnFeeDto.setReturnType(ApplicationConsts.APPLICATION_RETURN_FEE_REJECT);
                appReturnFeeDto.setStatus("paying");
                appReturnFeeDto.setTriggerCount(0);
                saveReturnFeeDtos.add(appReturnFeeDto);
                try {
                    doRefunds(saveReturnFeeDtos);
                }catch (Exception e){
                    log.info(e.getMessage(),e);
                }
                log.info(StringUtil.changeForLog("**** saveRejectReturnFee applicationDto ReturnFee***** " +applicationDto.getApplicationNo() + " and " + applicationDto.getReturnFee()));
            }
        }
        if(!IaisCommonUtils.isEmpty(saveReturnFeeDtos)){
            broadcastApplicationDto.setReturnFeeDtos(saveReturnFeeDtos);
            broadcastApplicationDto.setRollBackReturnFeeDtos(saveReturnFeeDtos);
        }
    }

    @Override
    public void doRefunds(List<AppReturnFeeDto> saveReturnFeeDtos){
        List<AppReturnFeeDto> saveReturnFeeDtosStripe=IaisCommonUtils.genNewArrayList();
        if(saveReturnFeeDtos!=null){
            for (AppReturnFeeDto appreturn:saveReturnFeeDtos
            ) {
                ApplicationDto applicationDto = applicationMainClient.getAppByNo(appreturn.getApplicationNo()).getEntity();
                ApplicationGroupDto applicationGroupDto = applicationMainClient.getAppById(applicationDto.getAppGrpId()).getEntity();
                if(applicationGroupDto.getPayMethod()!=null&&applicationGroupDto.getPayMethod().equals(ApplicationConsts.PAYMENT_METHOD_NAME_CREDIT)){
                    appreturn.setSysClientId(AppConsts.MOH_IAIS_SYSTEM_PAYMENT_CLIENT_KEY);
                    saveReturnFeeDtosStripe.add(appreturn);
                }
            }
            List<PaymentRequestDto> paymentRequestDtos = applicationViewService.eicFeStripeRefund(saveReturnFeeDtosStripe);
            for (PaymentRequestDto refund:paymentRequestDtos
            ) {
                for (AppReturnFeeDto appreturn:saveReturnFeeDtos
                ) {
                    if(appreturn.getApplicationNo().equals(refund.getReqRefNo())){
                        appreturn.setStatus(refund.getStatus());
                    }
                }
            }
        }
    }

    @Override
    public void inspectorAo1(LoginContext loginContext, ApplicationViewDto applicationViewDto, TaskDto taskDto){
        String userId = loginContext.getUserId();
        applicationViewDto.getApplicationDto().setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING);
        applicationViewService.updateApplicaiton(applicationViewDto.getApplicationDto());
        AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusMainClient.getAppInspectionStatusByPremId(applicationViewDto.getAppPremisesCorrelationId()).getEntity();
        appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_JOB_CREATE_TASK_TO_LEADER);
        appInspectionStatusDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        appInspectionStatusMainClient.update(appInspectionStatusDto);
        taskDto.setTaskKey(HcsaConsts.ROUTING_STAGE_INS);
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        taskService.updateTask(taskDto);

        mohHcsaBeDashboardService.createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW,InspectionConstants.PROCESS_DECI_ACKNOWLEDGE_EMAIL_CONTENT, taskDto, userId,"",HcsaConsts.ROUTING_STAGE_POT);
        mohHcsaBeDashboardService.createAppPremisesRoutingHistory(applicationViewDto.getApplicationDto().getApplicationNo(), ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_REVIEW,ApplicationConsts.APPLICATION_STATUS_PENDING_EMAIL_SENDING, taskDto, userId,"",HcsaConsts.ROUTING_STAGE_POT);

        //update FE  application status.
        applicationViewService.updateFEApplicaiton(applicationViewDto.getApplicationDto());
    }

    @Override
    public List<ApplicationDto> getStatusAppList(List<ApplicationDto> applicationDtos, String status){
        if(IaisCommonUtils.isEmpty(applicationDtos) || StringUtil.isEmpty(status)){
            return null;
        }
        List<ApplicationDto> applicationDtoList = null;
        for(ApplicationDto applicationDto : applicationDtos){
            if(status.equals(applicationDto.getStatus())){
                if(applicationDtoList == null){
                    applicationDtoList = IaisCommonUtils.genNewArrayList();
                    applicationDtoList.add(applicationDto);
                }else{
                    applicationDtoList.add(applicationDto);
                }
            }
        }

        return applicationDtoList;
    }

    @Override
    public List<ApplicationDto> removeFastTracking(List<ApplicationDto> applicationDtos){
        List<ApplicationDto> result = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(applicationDtos)){
            for (ApplicationDto applicationDto : applicationDtos){
                if(ApplicationConsts.APPLICATION_STATUS_TRANSFER_ORIGIN.equals(applicationDto.getStatus())){
                    continue;
                }
                if(!applicationDto.isFastTracking()){
                    result.add(applicationDto);
                }
            }
        }
        return  result;
    }

    @Override
    public void updateCurrentApplicationStatus(List<ApplicationDto> applicationDtos, String applicationId, String status){
        if(!IaisCommonUtils.isEmpty(applicationDtos) && !StringUtil.isEmpty(applicationId)){
            for (ApplicationDto applicationDto : applicationDtos){
                if(applicationId.equals(applicationDto.getId())){
                    applicationDto.setStatus(status);
                }
            }
        }
    }

    @Override
    public void updateCurAppStatusByLicensee(Map<String, String> returnFee, List<ApplicationDto> applicationDtos, String licenseeId) {
        if (!IaisCommonUtils.isEmpty(returnFee) && !IaisCommonUtils.isEmpty(applicationDtos)) {
            String entityType = "";
            LicenseeDto licenseeDto = organizationMainClient.getLicenseeDtoById(licenseeId).getEntity();
            if (licenseeDto != null) {
                LicenseeEntityDto licenseeEntityDto = licenseeDto.getLicenseeEntityDto();
                if (licenseeEntityDto != null) {
                    entityType = licenseeEntityDto.getEntityType();
                }
            }
            boolean isCharity;
            if (AcraConsts.ENTITY_TYPE_CHARITIES.equals(entityType)) {
                isCharity = true;
            } else {
                isCharity = false;
            }
            for (ApplicationDto applicationDto : applicationDtos) {
                log.info(StringUtil.changeForLog("****application return fee applicationDto***** " + applicationDto.getApplicationNo()));
                applicationDto.setIsCharity(isCharity);
                applicationDto.setReturnType(ApplicationConsts.APPLICATION_RETURN_FEE_REJECT);
                for (Map.Entry<String, String> entry : returnFee.entrySet()) {
                    log.info(StringUtil.changeForLog("****application return fee returnFee***** " + entry.getKey()));
                    if (entry.getKey().equals(applicationDto.getApplicationNo())) {
                        log.info(StringUtil.changeForLog("****application return fee***** " + entry.getKey() + " *****" + entry.getValue()));
                        applicationDto.setStatus(entry.getValue());
                    }
                }
            }
        }
    }

    @Override
    public List<ApplicationDto> removeCurrentApplicationDto(List<ApplicationDto> applicationDtoList, String currentId){
        List<ApplicationDto> result = null;
        if(!IaisCommonUtils.isEmpty(applicationDtoList) && !StringUtil.isEmpty(currentId)){
            result = IaisCommonUtils.genNewArrayList();
            for(ApplicationDto applicationDto : applicationDtoList){
                if(currentId.equals(applicationDto.getId())){
                    continue;
                }
                result.add(applicationDto);
            }
        }
        return result;
    }

    @Override
    public TaskDto completedTask(TaskDto taskDto){
        taskDto.setTaskStatus(TaskConsts.TASK_STATUS_COMPLETED);
        taskDto.setSlaDateCompleted(new Date());
        taskDto.setSlaRemainInDays(0);
        taskDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return taskDto;
    }

    @Override
    public WorkingGroupDto changeStatusWrokGroup(WorkingGroupDto workingGroupDto, String status){
        if(workingGroupDto!= null && !StringUtil.isEmpty(status)){
            workingGroupDto.setStatus(status);
        }
        return workingGroupDto;
    }

    @Override
    public List<UserGroupCorrelationDto> changeStatusUserGroupCorrelationDtos(List<UserGroupCorrelationDto> userGroupCorrelationDtos, String status){
        List<UserGroupCorrelationDto> result = IaisCommonUtils.genNewArrayList();
        if(userGroupCorrelationDtos!= null && userGroupCorrelationDtos.size() >0){
            for (UserGroupCorrelationDto userGroupCorrelationDto : userGroupCorrelationDtos){
                userGroupCorrelationDto.setStatus(status);
                result.add(userGroupCorrelationDto);
            }
        }
        return  result;
    }

    @Override
    public List<ApplicationDto> removeFastTrackingAndTransfer(List<ApplicationDto> applicationDtos){
        List<ApplicationDto> result = IaisCommonUtils.genNewArrayList();
        if(!IaisCommonUtils.isEmpty(applicationDtos)){
            for (ApplicationDto applicationDto : applicationDtos){
                if(ApplicationConsts.APPLICATION_STATUS_TRANSFER_ORIGIN.equals(applicationDto.getStatus())){
                    continue;
                }
                if(!applicationDto.isFastTracking()){
                    result.add(applicationDto);
                }
            }
        }
        return  result;
    }

    @Override
    public void changePostInsForTodoAudit(ApplicationViewDto applicationViewDto){
        if(applicationViewDto.getLicPremisesAuditDto() != null && ApplicationConsts.INCLUDE_RISK_TYPE_INSPECTION_KEY.equalsIgnoreCase(applicationViewDto.getLicPremisesAuditDto().getIncludeRiskType())){
            ApplicationDto applicationDto = applicationViewDto.getApplicationDto();
            AppPremisesCorrelationDto newAppPremisesCorrelationDto = applicationViewDto.getNewAppPremisesCorrelationDto();
            if(applicationDto.getApplicationType().equalsIgnoreCase(ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK)){
                PostInsGroupDto postInsGroupDto =licenceClient.getPostInsGroupDto(applicationDto.getOriginLicenceId(),newAppPremisesCorrelationDto.getId()).getEntity();
                if(postInsGroupDto != null && postInsGroupDto.getLicInspectionGroupDto() != null && postInsGroupDto.getLicPremInspGrpCorrelationDto()!= null){
                    LicInspectionGroupDto licInspectionGroupDto = postInsGroupDto.getLicInspectionGroupDto();
                    licInspectionGroupDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    LicPremInspGrpCorrelationDto licPremInspGrpCorrelationDto = postInsGroupDto.getLicPremInspGrpCorrelationDto();
                    licPremInspGrpCorrelationDto.setStatus(AppConsts.COMMON_STATUS_IACTIVE);
                    postInsGroupDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    licenceClient.savePostInsGroupDto(postInsGroupDto);
                }
            }
        }
    }

    @Override
    public String checkAllStatus(List<ApplicationDto> applicationDtoList,List<String> appList){
        String status = ApplicationConsts.APPLICATION_STATUS_REJECTED;
        for(ApplicationDto applicationDto : applicationDtoList){
            int needChange = 1;
            for (String item : appList) {
                if(item.equals(applicationDto.getApplicationNo())){
                    needChange = 0;
                    break;
                }
            }
            if(needChange == 1){
                if(ApplicationConsts.APPLICATION_STATUS_APPROVED.equals(applicationDto.getStatus())){
                    status = ApplicationConsts.APPLICATION_STATUS_APPROVED;
                }
            }
        }
        return status;
    }

    @Override
    public void doAo1Ao2Approve(BroadcastOrganizationDto broadcastOrganizationDto, BroadcastApplicationDto broadcastApplicationDto, ApplicationDto applicationDto, List<String> appNo, TaskDto taskDto, String newCorrelationId) throws FeignException {
        String appGrpId = applicationDto.getAppGrpId();
        String status = applicationDto.getStatus();
        String appId = applicationDto.getId();
        List<ApplicationDto> applicationDtoList = applicationViewService.getApplicaitonsByAppGroupId(appGrpId);
        applicationDtoList = beDashboardSupportService.removeFastTrackingAndTransfer(applicationDtoList);
        applicationDtoList = beDashboardSupportService.removeCurrentApplicationDto(applicationDtoList,appId);
        if (IaisCommonUtils.isEmpty(applicationDtoList)) {
            return;
        } else {
            boolean flag = taskService.checkCompleteTaskByApplicationNo(applicationDtoList,newCorrelationId);
            if(flag) {
                String stageId = HcsaConsts.ROUTING_STAGE_AO3;
                String roleId = RoleConsts.USER_ROLE_AO3;
                beDashboardSupportService.updateCurrentApplicationStatus(applicationDtoList, appId, status);
                List<ApplicationDto> ao2AppList = beDashboardSupportService.getStatusAppList(applicationDtoList, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02);
                List<ApplicationDto> ao3AppList = beDashboardSupportService.getStatusAppList(applicationDtoList, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03);
                List<ApplicationDto> creatTaskApplicationList = ao2AppList;
                if (IaisCommonUtils.isEmpty(ao2AppList) && !IaisCommonUtils.isEmpty(ao3AppList)) {
                    creatTaskApplicationList = ao3AppList;
                } else {
                    stageId = HcsaConsts.ROUTING_STAGE_AO2;
                    roleId = RoleConsts.USER_ROLE_AO2;
                }
                // send the task to Ao2  or Ao3
                TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(creatTaskApplicationList,
                        stageId, roleId, IaisEGPHelper.getCurrentAuditTrailDto(),taskDto.getRoleId(), taskDto.getWkGrpId());
                List<TaskDto> taskDtos = taskHistoryDto.getTaskDtoList();
                List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = taskHistoryDto.getAppPremisesRoutingHistoryDtos();
                broadcastOrganizationDto.setOneSubmitTaskList(taskDtos);
                if(!IaisCommonUtils.isEmpty(taskDtos)) {
                    log.info(StringUtil.changeForLog("Dashboard -------- doAo1Ao2Approve task size "+ taskDtos.size()));
                    for(TaskDto dto : taskDtos) {
                        log.info(StringUtil.changeForLog("Dashboard -------- doAo1Ao2Approve task Id "+ dto.getId()));
                    }
                }
                broadcastApplicationDto.setOneSubmitTaskHistoryList(appPremisesRoutingHistoryDtos);
            }
        }
    }

    @Override
    public void setRiskScore(ApplicationDto applicationDto,String newCorrelationId){
        log.debug(StringUtil.changeForLog("correlationId : " + newCorrelationId));
        try {
            if(applicationDto != null && !StringUtil.isEmpty(newCorrelationId)){
                AppPremisesRecommendationDto appPreRecommentdationDtoInspectionDate = inspectionMainService.getAppRecomDtoByAppCorrId(newCorrelationId,InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
                if(appPreRecommentdationDtoInspectionDate != null){
                    HcsaRiskScoreDto hcsaRiskScoreDto = new HcsaRiskScoreDto();
                    hcsaRiskScoreDto.setAppType(applicationDto.getApplicationType());
                    hcsaRiskScoreDto.setLicId(applicationDto.getOriginLicenceId());
                    List<ApplicationDto> applicationDtos = new ArrayList<>(1);
                    applicationDtos.add(applicationDto);
                    hcsaRiskScoreDto.setApplicationDtos(applicationDtos);
                    hcsaRiskScoreDto.setServiceId(applicationDto.getServiceId());
                    HcsaRiskScoreDto entity = hcsaConfigMainClient.getHcsaRiskScoreDtoByHcsaRiskScoreDto(hcsaRiskScoreDto).getEntity();
                    String riskLevel = entity.getRiskLevel();
                    log.debug(StringUtil.changeForLog("riskLevel : " + riskLevel));
                    applicationDto.setRiskLevel(riskLevel);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
        }
    }

    @Override
    public void rejectSendNotification(ApplicationDto applicationDto) {
        String applicationNo = applicationDto.getApplicationNo();
        Date date = new Date();
        String appDate = Formatter.formatDateTime(date, "dd/MM/yyyy");
        String MohName = AppConsts.MOH_AGENCY_NAME;
        String applicationType = applicationDto.getApplicationType();
        String applicationTypeShow = MasterCodeUtil.getCodeDesc(applicationType);
        HcsaServiceDto svcDto = hcsaConfigMainClient.getHcsaServiceDtoByServiceId(applicationDto.getServiceId()).getEntity();
        List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
        if(svcDto != null){
            svcCodeList.add(svcDto.getSvcCode());
        }
        if(ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(applicationType)){
            renewalSendNotification(applicationTypeShow,applicationNo,appDate,MohName,applicationDto,svcCodeList);
        }else if(ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(applicationType)){
            newAppSendNotification(applicationTypeShow,applicationNo,appDate,MohName,applicationDto,svcCodeList);
        }else if(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType)){
            rfcSendRejectNotification(applicationTypeShow,applicationNo,appDate,MohName,applicationDto,svcCodeList);
        }else if(ApplicationConsts.APPLICATION_TYPE_APPEAL.equals(applicationType)){
            log.info("ao1 or ao2 send application reject email");
            try {
                sendAppealReject(applicationDto,MohName);
                log.info("reject email success");
            }catch (Exception e){
                log.error(e.getMessage()+"error",e);
            }
        }
    }

    private void sendAppealReject(ApplicationDto applicationDto,String MohName) throws IOException, TemplateException {
        log.info("start send email sms and msg");
        log.info(StringUtil.changeForLog("appNo: " + applicationDto.getApplicationNo()));
        String applicantName = "";
        Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
        ApplicationGroupDto applicationGroupDto =  applicationMainClient.getAppById(applicationDto.getAppGrpId()).getEntity();
        OrgUserDto orgUserDto = organizationMainClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
        if(orgUserDto != null){
            applicantName = orgUserDto.getDisplayName();
        }
        List<AppPremiseMiscDto> premiseMiscDtoList = cessationClient.getAppPremiseMiscDtoListByAppId(applicationDto.getId()).getEntity();
        String appType = "Licence";
        String appealNo = "-";
        if(premiseMiscDtoList != null){
            AppPremiseMiscDto premiseMiscDto = premiseMiscDtoList.get(0);
            if(premiseMiscDto != null){
                String oldAppId = premiseMiscDto.getRelateRecId();
                ApplicationDto oldApplication = applicationMainClient.getApplicationById(oldAppId).getEntity();
                if(oldApplication != null){
                    appType =  MasterCodeUtil.getCodeDesc(oldApplication.getApplicationType());
                    appealNo = oldApplication.getApplicationNo();
                }
                String appealType = premiseMiscDto.getAppealType();
                if(ApplicationConsts.APPEAL_TYPE_LICENCE.equals(appealType)){
                    LicenceDto licenceDto = licenceClient.getLicDtoById(premiseMiscDto.getRelateRecId()).getEntity();
                    if(licenceDto != null){
                        appealNo = licenceDto.getLicenceNo();
                        appType = "Licence";
                    }
                }
            }
        }
        if(StringUtil.isEmpty(appType)){
            appType = "Licence";
        }
        templateContent.put("ApplicantName", applicantName);
        templateContent.put("ApplicationType",  appType);
        templateContent.put("ApplicationNo", appealNo);
        templateContent.put("ApplicationDate", Formatter.formatDateTime(new Date(),"dd/MM/yyyy"));
        templateContent.put("MOH_AGENCY_NAME", MohName);
        templateContent.put("emailAddress", systemParamConfig.getSystemAddressOne());


        MsgTemplateDto emailTemplateDto = msgTemplateMainClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_REJECT_EMAIL).getEntity();
        MsgTemplateDto smsTemplateDto = msgTemplateMainClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_REJECT_SMS).getEntity();
        MsgTemplateDto msgTemplateDto = msgTemplateMainClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_REJECT_MSG).getEntity();
        Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
        subMap.put("ApplicationType", appType);
        subMap.put("ApplicationNo", appealNo);
        String emailSubject = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getTemplateName(),subMap);
        String smsSubject = MsgUtil.getTemplateMessageByContent(smsTemplateDto.getTemplateName(),subMap);
        String msgSubject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(),subMap);

        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_REJECT_EMAIL);
        emailParam.setTemplateContent(templateContent);
        emailParam.setSubject(emailSubject);
        emailParam.setQueryCode(applicationDto.getApplicationNo());
        emailParam.setReqRefNum(applicationDto.getApplicationNo());
        emailParam.setRefId(applicationDto.getApplicationNo());
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);

        notificationHelper.sendNotification(emailParam);
        EmailParam smsParam = new EmailParam();
        smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_REJECT_SMS);
        smsParam.setSubject(smsSubject);
        smsParam.setTemplateContent(templateContent);
        smsParam.setQueryCode(applicationDto.getApplicationNo());
        smsParam.setReqRefNum(applicationDto.getApplicationNo());
        smsParam.setRefId(applicationDto.getApplicationNo());
        smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
        notificationHelper.sendNotification(smsParam);

        EmailParam msgParam = new EmailParam();
        msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_APPEAL_REJECT_MSG);
        msgParam.setTemplateContent(templateContent);
        msgParam.setSubject(msgSubject);
        msgParam.setQueryCode(applicationDto.getApplicationNo());
        msgParam.setReqRefNum(applicationDto.getApplicationNo());
        List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
        HcsaServiceDto svcDto = hcsaConfigMainClient.getHcsaServiceDtoByServiceId(applicationDto.getServiceId()).getEntity();
        svcCodeList.add(svcDto.getSvcCode());
        msgParam.setSvcCodeList(svcCodeList);
        msgParam.setRefId(applicationDto.getApplicationNo());
        msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        notificationHelper.sendNotification(msgParam);
        log.info("end send email sms and msg");
    }

    private void rfcLicenseeSendRejectNotification(String applicationTypeShow, String applicationNo, String appDate, String MohName, ApplicationDto applicationDto,
                                          List<String> svcCodeList){
        ApplicationGroupDto applicationGroupDto = applicationViewService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
        String applicantName = "";
        OrgUserDto orgUserDto = organizationMainClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
        if(orgUserDto != null){
            applicantName = orgUserDto.getDisplayName();
        }
        Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
        LicenseeDto existingLicensee= organizationMainClient.getLicenseeDtoById(applicationGroupDto.getLicenseeId()).getEntity();
        emailMap.put("ExistingLicensee", existingLicensee.getName());
        LicenseeDto transfereeLicensee= organizationMainClient.getLicenseeDtoById(applicationGroupDto.getNewLicenseeId()).getEntity();
        emailMap.put("TransfereeLicensee", transfereeLicensee.getName());
        emailMap.put("ApplicantName", applicantName);
        emailMap.put("ApplicationType", applicationTypeShow);
        emailMap.put("ApplicationNumber", applicationNo);
        emailMap.put("ApplicationDate", appDate);
        emailMap.put("email_address", systemParamConfig.getSystemAddressOne());
        emailMap.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
        emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_011_LICENSEE_REJECTED);
        emailParam.setTemplateContent(emailMap);
        emailParam.setQueryCode(applicationNo);
        emailParam.setReqRefNum(applicationNo);
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        emailParam.setRefId(applicationNo);
        Map<String,Object> map=IaisCommonUtils.genNewHashMap();
        MsgTemplateDto rfiEmailTemplateDto = generateIdClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_011_LICENSEE_REJECTED).getEntity();
        map.put("ApplicationType", applicationTypeShow);
        map.put("ApplicationNumber", applicationNo);
        String subject= null;
        try {
            subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(),map);
        } catch (IOException | TemplateException e) {
            log.info(e.getMessage(),e);
        }
        emailParam.setSubject(subject);
        //email
        log.info(StringUtil.changeForLog("send RFC Reject email send"));
        notificationHelper.sendNotification(emailParam);
        log.info(StringUtil.changeForLog("send RFC Reject email end"));
        //msg
        rfiEmailTemplateDto = generateIdClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_011_LICENSEE_REJECTED_MSG).getEntity();
        subject = null;
        try {
            subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
        } catch (IOException |TemplateException e) {
            log.info(e.getMessage(),e);
        }
        EmailParam msgParam = new EmailParam();
        msgParam.setQueryCode(applicationNo);
        msgParam.setReqRefNum(applicationNo);
        msgParam.setTemplateContent(emailMap);
        msgParam.setSubject(subject);
        msgParam.setSvcCodeList(svcCodeList);
        msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_011_LICENSEE_REJECTED_MSG);
        msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        msgParam.setRefId(applicationNo);
        log.info(StringUtil.changeForLog("send RFC Reject msg send"));
        notificationHelper.sendNotification(msgParam);
        log.info(StringUtil.changeForLog("send RFC Reject msg end"));
        //sms
        EmailParam smsParam = new EmailParam();
        smsParam.setQueryCode(applicationNo);
        smsParam.setReqRefNum(applicationNo);
        smsParam.setRefId(applicationNo);
        smsParam.setTemplateContent(emailMap);
        rfiEmailTemplateDto = generateIdClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_011_LICENSEE_REJECTED_SMS).getEntity();
        subject = null;
        try {
            subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
        } catch (IOException |TemplateException e) {
            log.info(e.getMessage(),e);
        }
        smsParam.setSubject(subject);
        smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_011_LICENSEE_REJECTED_SMS);
        smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
        log.info(StringUtil.changeForLog("send RFC Reject sms send"));
        notificationHelper.sendNotification(smsParam);
        log.info(StringUtil.changeForLog("send RFC Reject sms end"));
    }
    @Override
    public void rfcSendRejectNotification(String applicationTypeShow, String applicationNo, String appDate, String MohName, ApplicationDto applicationDto,
                                          List<String> svcCodeList){
        ApplicationGroupDto applicationGroupDto = applicationViewService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
        if(StringUtil.isEmpty(applicationGroupDto.getNewLicenseeId())){
            String applicantName = "";
            OrgUserDto orgUserDto = organizationMainClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
            if(orgUserDto != null){
                applicantName = orgUserDto.getDisplayName();
            }
            Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
            emailMap.put("ApplicantName", applicantName);
            emailMap.put("ApplicationType", applicationTypeShow);
            emailMap.put("ApplicationNumber", applicationNo);
            emailMap.put("ApplicationDate", appDate);
            emailMap.put("email_address", systemParamConfig.getSystemAddressOne());
            emailMap.put("MOH_AGENCY_NAM_GROUP","<b>"+AppConsts.MOH_AGENCY_NAM_GROUP+"</b>");
            emailMap.put("MOH_AGENCY_NAME", "<b>"+AppConsts.MOH_AGENCY_NAME+"</b>");
            EmailParam emailParam = new EmailParam();
            emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_004_REJECTED);
            emailParam.setTemplateContent(emailMap);
            emailParam.setQueryCode(applicationNo);
            emailParam.setReqRefNum(applicationNo);
            emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
            emailParam.setRefId(applicationNo);
            Map<String,Object> map=IaisCommonUtils.genNewHashMap();
            MsgTemplateDto rfiEmailTemplateDto = generateIdClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_004_REJECTED).getEntity();
            map.put("ApplicationType", applicationTypeShow);
            map.put("ApplicationNumber", applicationNo);
            String subject= null;
            try {
                subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(),map);
            } catch (IOException | TemplateException e) {
                log.info(e.getMessage(),e);
            }
            emailParam.setSubject(subject);
            //email
            log.info(StringUtil.changeForLog("send RFC Reject email send"));
            notificationHelper.sendNotification(emailParam);
            log.info(StringUtil.changeForLog("send RFC Reject email end"));
            //msg
            rfiEmailTemplateDto = generateIdClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_004_REJECTED_MSG).getEntity();
            subject = null;
            try {
                subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
            } catch (IOException |TemplateException e) {
                log.info(e.getMessage(),e);
            }
            EmailParam msgParam = new EmailParam();
            msgParam.setQueryCode(applicationNo);
            msgParam.setReqRefNum(applicationNo);
            msgParam.setTemplateContent(emailMap);
            msgParam.setSubject(subject);
            msgParam.setSvcCodeList(svcCodeList);
            msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_004_REJECTED_MSG);
            msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
            msgParam.setRefId(applicationNo);
            log.info(StringUtil.changeForLog("send RFC Reject msg send"));
            notificationHelper.sendNotification(msgParam);
            log.info(StringUtil.changeForLog("send RFC Reject msg end"));
            //sms
            EmailParam smsParam = new EmailParam();
            smsParam.setQueryCode(applicationNo);
            smsParam.setReqRefNum(applicationNo);
            smsParam.setRefId(applicationNo);
            smsParam.setTemplateContent(emailMap);
            rfiEmailTemplateDto = generateIdClient.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_004_REJECTED_SMS).getEntity();
            subject = null;
            try {
                subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getTemplateName(), map);
            } catch (IOException |TemplateException e) {
                log.info(e.getMessage(),e);
            }
            smsParam.setSubject(subject);
            smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_EN_RFC_004_REJECTED_SMS);
            smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
            log.info(StringUtil.changeForLog("send RFC Reject sms send"));
            notificationHelper.sendNotification(smsParam);
            log.info(StringUtil.changeForLog("send RFC Reject sms end"));
        }
        else {
            rfcLicenseeSendRejectNotification(applicationTypeShow,applicationNo,appDate,MohName,applicationDto,svcCodeList);
        }
    }
    @Override
    public void newAppSendNotification(String applicationTypeShow, String applicationNo, String appDate, String MohName, ApplicationDto applicationDto,
                                       List<String> svcCodeList){
        log.info(StringUtil.changeForLog("send new application notification start"));
        //send email
        ApplicationGroupDto applicationGroupDto = applicationViewService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
        if(applicationGroupDto != null) {
            String groupLicenseeId = applicationGroupDto.getLicenseeId();
            String aubmitBy = applicationGroupDto.getSubmitBy();
            log.info(StringUtil.changeForLog("send new application notification groupLicenseeId : " + groupLicenseeId));

            Map<String, Object> map = IaisCommonUtils.genNewHashMap();

            OrgUserDto orgUserDto = organizationMainClient.retrieveOrgUserAccountById(aubmitBy).getEntity();
            if (orgUserDto != null){
                map.put("ApplicantName", orgUserDto.getDisplayName());
            }

            map.put("applicationType", applicationTypeShow);
            map.put("applicationNumber", applicationNo);
            map.put("applicationDate", appDate);
            map.put("emailAddress", systemAddressOne);
            map.put("MOH_AGENCY_NAME", MohName);
            try {
//                String subject = "MOH HALP - Your "+ applicationTypeShow + ", "+ applicationNo +" is rejected ";
                Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
                subMap.put("ApplicationType", applicationTypeShow);
                subMap.put("ApplicationNumber", applicationNo);
                String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_REJECTED_ID,subMap);
                String smsSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_REJECTED_SMS_ID,subMap);
                String messageSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_REJECTED_MESSAGE_ID,subMap);
                log.debug(StringUtil.changeForLog("emailSubject : " + emailSubject));
                log.debug(StringUtil.changeForLog("smsSubject : " + smsSubject));
                log.debug(StringUtil.changeForLog("messageSubject : " + messageSubject));
                EmailParam emailParam = new EmailParam();
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_REJECTED_ID);
                emailParam.setTemplateContent(map);
                emailParam.setQueryCode(applicationNo);
                emailParam.setReqRefNum(applicationNo);
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                emailParam.setRefId(applicationNo);
                emailParam.setSubject(emailSubject);
                //send email
                log.info(StringUtil.changeForLog("send new application email"));
                notificationHelper.sendNotification(emailParam);
                //send sms
                EmailParam smsParam = new EmailParam();
                smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_REJECTED_SMS_ID);
                smsParam.setSubject(smsSubject);
                smsParam.setQueryCode(applicationNo);
                smsParam.setReqRefNum(applicationNo);
                smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
                smsParam.setRefId(applicationNo);
                log.info(StringUtil.changeForLog("send new application sms"));
                notificationHelper.sendNotification(smsParam);
                //send message
                EmailParam messageParam = new EmailParam();
                messageParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_NEW_APP_REJECTED_MESSAGE_ID);
                messageParam.setTemplateContent(map);
                messageParam.setQueryCode(applicationNo);
                messageParam.setReqRefNum(applicationNo);
                messageParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                messageParam.setRefId(applicationNo);
                messageParam.setSubject(messageSubject);
                messageParam.setSvcCodeList(svcCodeList);
                log.info(StringUtil.changeForLog("send new application message"));
                notificationHelper.sendNotification(messageParam);
                log.info(StringUtil.changeForLog("send new application notification end"));
            }catch (Exception e){
                log.error(e.getMessage(), e);
            }

        }
    }
    @Override
    public void renewalSendNotification(String applicationTypeShow, String applicationNo, String appDate, String MohName, ApplicationDto applicationDto,
                                        List<String> svcCodeList){
        log.info(StringUtil.changeForLog("send renewal application notification start"));
        //send email
        ApplicationGroupDto applicationGroupDto = applicationViewService.getApplicationGroupDtoById(applicationDto.getAppGrpId());
        if(applicationGroupDto != null){
            String groupLicenseeId = applicationGroupDto.getLicenseeId();
            log.info(StringUtil.changeForLog("send renewal application notification groupLicenseeId : " + groupLicenseeId));
            LicenseeDto licenseeDto = organizationMainClient.getLicenseeDtoById(groupLicenseeId).getEntity();
            if(licenseeDto != null){
                String applicantName = "";
                OrgUserDto orgUserDto = organizationMainClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
                if(orgUserDto != null){
                    applicantName = orgUserDto.getDisplayName();
                }
                log.info(StringUtil.changeForLog("send renewal application notification applicantName : " + applicantName));
                Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                map.put("ApplicantName", applicantName);
                map.put("ApplicationType", applicationTypeShow);
                map.put("ApplicationNumber", applicationNo);
                map.put("ApplicationDate", appDate);
                map.put("emailAddress", systemAddressOne);
                map.put("MOH_AGENCY_NAME", MohName);
                try {
                    Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
                    subMap.put("ApplicationType", applicationTypeShow);
                    subMap.put("ApplicationNumber", applicationNo);
                    String emailSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_REJECT,subMap);
                    String smsSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_REJECT_SMS,subMap);
                    String messageSubject = getEmailSubject(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_REJECT_MESSAGE,subMap);
                    log.debug(StringUtil.changeForLog("emailSubject : " + emailSubject));
                    log.debug(StringUtil.changeForLog("smsSubject : " + smsSubject));
                    log.debug(StringUtil.changeForLog("messageSubject : " + messageSubject));
                    EmailParam emailParam = new EmailParam();
                    emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_REJECT);
                    emailParam.setTemplateContent(map);
                    emailParam.setQueryCode(applicationNo);
                    emailParam.setReqRefNum(applicationNo);
                    emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                    emailParam.setRefId(applicationNo);
                    emailParam.setSubject(emailSubject);
                    //send email
                    log.info(StringUtil.changeForLog("send renewal application email"));
                    notificationHelper.sendNotification(emailParam);
                    //send sms
                    EmailParam smsParam = new EmailParam();
                    smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_REJECT_SMS);
                    smsParam.setSubject(smsSubject);
                    smsParam.setQueryCode(applicationNo);
                    smsParam.setReqRefNum(applicationNo);
                    smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
                    smsParam.setRefId(applicationNo);
                    log.info(StringUtil.changeForLog("send renewal application sms"));
                    notificationHelper.sendNotification(smsParam);
                    //send message
                    EmailParam messageParam = new EmailParam();
                    messageParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_REJECT_MESSAGE);
                    messageParam.setTemplateContent(map);
                    messageParam.setQueryCode(applicationNo);
                    messageParam.setReqRefNum(applicationNo);
                    messageParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                    messageParam.setRefId(applicationNo);
                    messageParam.setSubject(messageSubject);
                    messageParam.setSvcCodeList(svcCodeList);
                    log.info(StringUtil.changeForLog("send renewal application message"));
                    notificationHelper.sendNotification(messageParam);
                    log.info(StringUtil.changeForLog("send renewal application notification end"));
                }catch (Exception e){
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    private String getEmailSubject(String templateId,Map<String, Object> subMap){
        String subject = "-";
        if(!StringUtil.isEmpty(templateId)){
            MsgTemplateDto emailTemplateDto = msgTemplateMainClient.getMsgTemplate(templateId).getEntity();
            if(emailTemplateDto != null){
                try {
                    if(!IaisCommonUtils.isEmpty(subMap)){
                        subject = MsgUtil.getTemplateMessageByContent(emailTemplateDto.getTemplateName(),subMap);
                    }else{
                        subject = emailTemplateDto.getTemplateName();
                    }
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }
        return subject;
    }
}
