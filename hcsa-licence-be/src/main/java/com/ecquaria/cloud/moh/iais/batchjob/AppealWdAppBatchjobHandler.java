package com.ecquaria.cloud.moh.iais.batchjob;


import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.handler.IJobHandler;
import com.ecquaria.cloud.job.executor.handler.annotation.JobHandler;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.action.HcsaApplicationDelegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.acra.AcraConsts;
import com.ecquaria.cloud.moh.iais.common.constant.application.AppServicesConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcStageWorkingGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AppInspectionStatusDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.TaskHistoryDto;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.AppPremisesRoutingHistoryService;
import com.ecquaria.cloud.moh.iais.service.ApplicationService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.AppInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaAppClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloudfeign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

@JobHandler(value="approveWithdrawalJobHandler")
@Component
@Slf4j
public class AppealWdAppBatchjobHandler extends IJobHandler {

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;

    @Autowired
    CessationClient cessationClient;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private OrganizationClient organizationClient;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private AppInspectionStatusClient appInspectionStatusClient;

    @Value("${iais.system.one.address}")
    private String systemAddressOne;

    @Autowired
    private HcsaAppClient hcsaAppClient;

    @Override
    public ReturnT<String> execute(String s) {
        try {
            AuditTrailHelper.setupBatchJobAuditTrail(this);
            jobRun();
        }catch (Exception e){
            log.error(e.getMessage(), e);
            JobLogger.log(e);
            return ReturnT.FAIL;
        }
        return ReturnT.SUCCESS;
    }

    public void jobRun() throws FeignException {
        List<ApplicationDto> withdrawApplicationDtoList = applicationClient.saveWithdrawn().getEntity();
        //get old application
        HcsaApplicationDelegator newApplicationDelegator = SpringContextHelper.getContext().getBean(HcsaApplicationDelegator.class);
        if (!IaisCommonUtils.isEmpty(withdrawApplicationDtoList)){
            withdrawApplicationDtoList.forEach(h -> {
                boolean isCharity = false;
                String applicantName = "";
                String oldAppId = h.getId();
                if (!StringUtil.isEmpty(oldAppId)) {
                    ApplicationDto oldApplication = applicationClient.getApplicationById(oldAppId).getEntity();
                    String serviceId = oldApplication.getServiceId();
                    String applicationNo = oldApplication.getApplicationNo();
                    String applicationType1 = oldApplication.getApplicationType();
                    String loginUrl = HmacConstants.HTTPS + "://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
                    ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(oldApplication.getAppGrpId()).getEntity();
                    OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
                    if (orgUserDto != null) {
                        applicantName = orgUserDto.getDisplayName();
                    }
                    String licenseeId = applicationGroupDto.getLicenseeId();
                    String paymentMethod = applicationGroupDto.getPayMethod();
                    HcsaServiceDto hcsaServiceDto=HcsaServiceCacheHelper.getServiceById(serviceId);
                    String serviceName = hcsaServiceDto.getSvcName();
                    Double fee = 0.0;
                    applicationService.closeTaskWhenWhAppApprove(h.getId());
                    List<ApplicationDto> applicationDtoList = IaisCommonUtils.genNewArrayList();
                    applicationDtoList.add(oldApplication);
                    String entityType = "";
                    LicenseeDto licenseeDto = organizationClient.getLicenseeDtoById(licenseeId).getEntity();
                    if (licenseeDto != null) {
                        LicenseeEntityDto licenseeEntityDto = licenseeDto.getLicenseeEntityDto();
                        if (licenseeEntityDto != null) {
                            entityType = licenseeEntityDto.getEntityType();
                        }
                    }
                    if (AcraConsts.ENTITY_TYPE_CHARITIES.equals(entityType)) {
                        isCharity = true;
                    }
                    for (ApplicationDto applicationDto1 : applicationDtoList) {
                        applicationDto1.setStatus(ApplicationConsts.APPLICATION_STATUS_REJECTED);
                        applicationDto1.setIsCharity(isCharity);
                    }
                    List<ApplicationDto> applicationDtoList2 = hcsaConfigClient.returnFee(applicationDtoList).getEntity();
                    if (!IaisCommonUtils.isEmpty(applicationDtoList2)) {
                        fee = applicationDtoList2.get(0).getReturnFee();
                    }
                    try {
                        boolean withdrawReturnFee = applicationService.isWithdrawReturnFee(h.getApplicationNo(),h.getAppGrpId());
                        if (withdrawReturnFee&&fee!=null&& !MiscUtil.doubleEquals(fee, 0.0)){
                            AppReturnFeeDto appReturnFeeDto = assembleReturn(h, fee);
                            applicationService.saveAppReturnFee(appReturnFeeDto);
                        }
                    }catch (Exception e){
                        log.error("Withdraw application return is failed");
                        log.error(e.getMessage(), e);
                    }
                    h.setStatus(ApplicationConsts.APPLICATION_STATUS_WITHDRAWN);
                    applicationService.updateBEApplicaiton(h);
                    applicationService.updateFEApplicaiton(h);
                    try {
                        List<ApplicationDto> applicationDtoAllList = applicationService.getApplicaitonsByAppGroupId(oldApplication.getAppGrpId());

                        applicationDtoAllList = removeFastTrackingAndTransfer(applicationDtoAllList);

                        boolean needUpdateGroupStatus = applicationService.isOtherApplicaitonSubmit(applicationDtoAllList, oldApplication.getApplicationNo(),
                                ApplicationConsts.APPLICATION_STATUS_APPROVED, ApplicationConsts.APPLICATION_STATUS_REJECTED);
                        if (needUpdateGroupStatus ) {
                            //update application Group status
                            boolean appStatusIsAllRejected = checkAllStatus(applicationDtoAllList, ApplicationConsts.APPLICATION_STATUS_REJECTED);
                            if (appStatusIsAllRejected) {
                                applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_REJECT);
                            } else {
                                applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_APPROVED);
                            }
                            applicationClient.updateApplication(applicationGroupDto);
                        }
                    }catch (Exception e){
                        log.error(e.getMessage());
                    }
                    try {
                        Map<String, Object> msgInfoMap = IaisCommonUtils.genNewHashMap();
                        msgInfoMap.put("Applicant", applicantName);
                        msgInfoMap.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationType1));
                        msgInfoMap.put("ApplicationNumber", applicationNo);
                        msgInfoMap.put("reqAppNo", applicationNo);
                        msgInfoMap.put("S_LName", serviceName);
                        msgInfoMap.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);
                        msgInfoMap.put("ApplicationDate", Formatter.formatDateTime(new Date()));
                        msgInfoMap.put("adminFee", ApplicationConsts.PAYMRNT_ADMIN_FEE);
                        if(hcsaServiceDto.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_EMERGENCY_AMBULANCE_SERVICE)||hcsaServiceDto.getSvcCode().equals(AppServicesConsts.SERVICE_CODE_MEDICAL_TRANSPORT_SERVICE)){
                            ApplicationDto applicationDto =hcsaAppClient.getBundledAppDtosByAppDto(h).getEntity();
                            if(applicationDto!=null&&!StringUtil.isEmpty(fee)&&!MiscUtil.doubleEquals(fee, 0.0)){
                                msgInfoMap.put("adminFee", "200");
                            }
                        }
                        if (StringUtil.isEmpty(paymentMethod) || StringUtil.isEmpty(fee) ||MiscUtil.doubleEquals(fee, 0.0)||
                                ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(applicationType1) || isCharity) {
                            msgInfoMap.put("paymentType", "2");
                            msgInfoMap.put("paymentMode", "");
                            msgInfoMap.put("returnMount", 0.0);
                        } else {
                            msgInfoMap.put("returnMount", fee);
                            if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(paymentMethod)) {
                                msgInfoMap.put("paymentType", "0");
                                msgInfoMap.put("paymentMode", MasterCodeUtil.getCodeDesc(ApplicationConsts.PAYMENT_METHOD_NAME_GIRO));
                            } else {
                                msgInfoMap.put("paymentType", "1");
                                msgInfoMap.put("paymentMode", MasterCodeUtil.getCodeDesc(paymentMethod));
                            }
                        }

                        msgInfoMap.put("systemLink", loginUrl);
                        msgInfoMap.put("emailAddress", systemAddressOne);

                        newApplicationDelegator.sendEmail(MsgTemplateConstants.MSG_TEMPLATE_WITHDRAWAL_APP_APPROVE_EMAIL, msgInfoMap, oldApplication);
                        newApplicationDelegator.sendSMS(oldApplication, MsgTemplateConstants.MSG_TEMPLATE_WITHDRAWAL_APP_APPROVE_SMS, msgInfoMap);
                        newApplicationDelegator.sendInboxMessage(oldApplication, serviceId, msgInfoMap, MsgTemplateConstants.MSG_TEMPLATE_WITHDRAWAL_APP_APPROVE_MESSAGE);
                    } catch (Exception e) {
                        log.info(e.getMessage(),e);
                    }
                }
            });
            log.debug(StringUtil.changeForLog("**** The withdraw Application List size"+withdrawApplicationDtoList.size()));
            List<String> oldAppGroupExcuted = IaisCommonUtils.genNewArrayList();
            if(!IaisCommonUtils.isEmpty(withdrawApplicationDtoList)){
                for(ApplicationDto oldApplicationDto : withdrawApplicationDtoList){
                    doWithdrawal(oldApplicationDto,oldAppGroupExcuted);
                }
            }
            JobLogger.log(StringUtil.changeForLog("The withdraw Application List" + withdrawApplicationDtoList.size()));
        }else{
            JobLogger.log(StringUtil.changeForLog("The withdraw Application List is null *****"));
        }
    }
    private void doWithdrawal(ApplicationDto oldApplicationDto, List<String> oldAppGroupExcuted) throws FeignException {
        if(oldApplicationDto != null){
            log.info(StringUtil.changeForLog("withdrawal old application id : " + oldApplicationDto.getId()));
            String oldAppGrpId = oldApplicationDto.getAppGrpId();

            if(oldApplicationDto.getApplicationType().equals(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE)){
                List<AppEditSelectDto> appEditSelectDtos = applicationService.getAppEditSelectDtos(oldApplicationDto.getId(), ApplicationConsts.APPLICATION_EDIT_TYPE_RFC);
                boolean changePrem=false;
                for (AppEditSelectDto edit:appEditSelectDtos
                     ) {
                    if(edit.isPremisesEdit()||edit.isPremisesListEdit()){
                        changePrem=true;
                    }
                }
                if(changePrem){
                    List<ApplicationDto> apps=IaisCommonUtils.genNewArrayList();
                    apps.add(oldApplicationDto);
                    applicationClient.clearHclcodeByAppIds(apps);
                }
            }
            String currentOldApplicationNo = oldApplicationDto.getApplicationNo();
            List<ApplicationDto> applicationDtoList = applicationService.getApplicaitonsByAppGroupId(oldAppGrpId);
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtos=applicationService.getAppPremisesCorrelationByAppGroupId(oldAppGrpId);
            for (AppPremisesCorrelationDto apc:appPremisesCorrelationDtos
                 ) {
                if(apc.getApplicationId().equals(oldApplicationDto.getId())){
                    AppInspectionStatusDto appInspectionStatusDto = appInspectionStatusClient.getAppInspectionStatusByPremId(apc.getId()).getEntity();
                    if(appInspectionStatusDto!=null){
                        appInspectionStatusDto.setStatus(InspectionConstants.INSPECTION_STATUS_PENDING_AO2_RESULT);
                        appInspectionStatusClient.update(appInspectionStatusDto);
                    }
                }else if(taskService.checkCompleteTaskByApplicationNo(applicationDtoList,apc.getId())){

                    String appId = apc.getApplicationId();
                    ApplicationDto otherApp=applicationClient.getApplicationById(appId).getEntity();
                    String stageId = HcsaConsts.ROUTING_STAGE_AO3;
                    String roleId = RoleConsts.USER_ROLE_AO3;
                    updateCurrentApplicationStatus(applicationDtoList, appId, otherApp.getStatus());
                    List<ApplicationDto> ao2AppList = getStatusAppList(applicationDtoList, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02);
                    List<ApplicationDto> ao3AppList = getStatusAppList(applicationDtoList, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03);
                    List<ApplicationDto> creatTaskApplicationList = ao2AppList;
                    if (IaisCommonUtils.isEmpty(ao2AppList) && !IaisCommonUtils.isEmpty(ao3AppList)) {
                        creatTaskApplicationList = ao3AppList;
                    } else {
                        stageId = HcsaConsts.ROUTING_STAGE_AO2;
                        roleId = RoleConsts.USER_ROLE_AO2;
                    }
                    // send the task to Ao2  or Ao3
                    HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto = new HcsaSvcStageWorkingGroupDto();
                    hcsaSvcStageWorkingGroupDto.setServiceId(otherApp.getServiceId());
                    hcsaSvcStageWorkingGroupDto.setType(otherApp.getApplicationType());
                    hcsaSvcStageWorkingGroupDto.setStageId(stageId);
                    hcsaSvcStageWorkingGroupDto.setOrder(1);
                    HcsaSvcStageWorkingGroupDto hcsaSvcStageWorkingGroupDto1=hcsaConfigClient.getHcsaSvcStageWorkingGroupDto(hcsaSvcStageWorkingGroupDto).getEntity();

                    TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(creatTaskApplicationList,
                            stageId, roleId, IaisEGPHelper.getCurrentAuditTrailDto(),RoleConsts.USER_ROLE_AO3, hcsaSvcStageWorkingGroupDto1.getGroupId());
                    List<TaskDto> taskDtos = taskHistoryDto.getTaskDtoList();
                    List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = taskHistoryDto.getAppPremisesRoutingHistoryDtos();
                    taskService.createTasks(taskDtos);
                    appPremisesRoutingHistoryService.createHistorys(appPremisesRoutingHistoryDtos);
                }
            }

            if(IaisCommonUtils.isEmpty(applicationDtoList) || applicationDtoList.size() == 1){
                return;
            }else{
                if (!oldAppGroupExcuted.contains(oldAppGrpId)) {
                    List<ApplicationDto> ao1AppList = getStatusAppList(applicationDtoList, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01, RoleConsts.USER_ROLE_AO1,currentOldApplicationNo);
                    List<ApplicationDto> ao2AppList = getStatusAppList(applicationDtoList, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02,RoleConsts.USER_ROLE_AO2,currentOldApplicationNo);
                    List<ApplicationDto> ao3AppList = getStatusAppList(applicationDtoList, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03,RoleConsts.USER_ROLE_AO3,currentOldApplicationNo);
                    if(!IaisCommonUtils.isEmpty(ao1AppList)){
                        return;
                    }else{
                        //ao1 == null
                        if(!IaisCommonUtils.isEmpty(ao2AppList)){
                            //create task
                            createTaskAndHistory(ao2AppList, HcsaConsts.ROUTING_STAGE_AO2,RoleConsts.USER_ROLE_AO2,oldAppGroupExcuted,oldAppGrpId);
                        }else{
                            if(!IaisCommonUtils.isEmpty(ao3AppList)) {
                                //create task
                                createTaskAndHistory(ao3AppList,HcsaConsts.ROUTING_STAGE_AO3,RoleConsts.USER_ROLE_AO3,oldAppGroupExcuted,oldAppGrpId);
                            }else{
                                return;
                            }
                        }
                    }
                }
            }
        }
    }
    private List<ApplicationDto> getStatusAppList(List<ApplicationDto> applicationDtos, String status) {
        if (IaisCommonUtils.isEmpty(applicationDtos) || StringUtil.isEmpty(status)) {
            return null;
        }
        List<ApplicationDto> applicationDtoList = null;
        for (ApplicationDto applicationDto : applicationDtos) {
            if (status.equals(applicationDto.getStatus())) {
                if (applicationDtoList == null) {
                    applicationDtoList = IaisCommonUtils.genNewArrayList();
                    applicationDtoList.add(applicationDto);
                } else {
                    applicationDtoList.add(applicationDto);
                }
            }
        }

        return applicationDtoList;
    }
    private void updateCurrentApplicationStatus(List<ApplicationDto> applicationDtos, String applicationId, String status) {
        if (!IaisCommonUtils.isEmpty(applicationDtos) && !StringUtil.isEmpty(applicationId)) {
            for (ApplicationDto applicationDto : applicationDtos) {
                if (applicationId.equals(applicationDto.getId())) {
                    applicationDto.setStatus(status);
                }
            }
        }
    }

    private void createTaskAndHistory( List<ApplicationDto> creatTaskApplicationList, String stageId, String roleId, List<String> oldAppGroupExcuted, String oldAppGrpId) throws FeignException {
        TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(creatTaskApplicationList,
                stageId, roleId, IaisEGPHelper.getCurrentAuditTrailDto(),RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN, null);
        if(taskHistoryDto != null){
            List<TaskDto> taskDtos = taskHistoryDto.getTaskDtoList();
            List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = taskHistoryDto.getAppPremisesRoutingHistoryDtos();
            taskService.createTasks(taskDtos);
            appPremisesRoutingHistoryService.createHistorys(appPremisesRoutingHistoryDtos);
            oldAppGroupExcuted.add(oldAppGrpId);
        }
    }

    private List<ApplicationDto> getOldApplicationDtoList(List<ApplicationDto> withdrawalApplicationDtoList){
        if(IaisCommonUtils.isEmpty(withdrawalApplicationDtoList)){
            return null;
        }
        List<ApplicationDto> oldApplicationList = IaisCommonUtils.genNewArrayList();
        for(ApplicationDto withdrawalApplicationDto : withdrawalApplicationDtoList){
            AppPremiseMiscDto premiseMiscDto = cessationClient.getAppPremiseMiscDtoByAppId(withdrawalApplicationDto.getId()).getEntity();
            if(premiseMiscDto != null) {
                String oldAppId = premiseMiscDto.getRelateRecId();
                log.info(StringUtil.changeForLog("withdrawal old application id : " + oldAppId));
                ApplicationDto oldApplication = applicationClient.getApplicationById(oldAppId).getEntity();
                if(oldApplication != null){
                    oldApplicationList.add(oldApplication);
                }
            }
        }
        return oldApplicationList;
    }

    private AppReturnFeeDto assembleReturn(ApplicationDto applicationDto,Double returnFee){
        AppReturnFeeDto appReturnFeeDto = new AppReturnFeeDto();
        appReturnFeeDto.setStatus("paying");
        appReturnFeeDto.setTriggerCount(0);
        appReturnFeeDto.setApplicationNo(applicationDto.getApplicationNo());
        appReturnFeeDto.setReturnAmount(returnFee);
        appReturnFeeDto.setReturnType(ApplicationConsts.APPLICATION_RETURN_FEE_TYPE_WITHDRAW);
        return appReturnFeeDto;
    }

    private List<ApplicationDto> getStatusAppList(List<ApplicationDto> applicationDtos, String status, String roleId, String currentAppNo){
        if(IaisCommonUtils.isEmpty(applicationDtos) || StringUtil.isEmpty(status) || StringUtil.isEmpty(roleId) || StringUtil.isEmpty(currentAppNo)){
            return null;
        }
        List<ApplicationDto> applicationDtoList = null;
        for(ApplicationDto applicationDto : applicationDtos){
            if(currentAppNo.equals(applicationDto.getApplicationNo())){
                continue;
            }
            //have uncompleted task
            boolean hasNoTask = false;
            List<TaskDto> pendingTaskDtos = organizationClient.getTaskByApplicationNoAndRoleIdAndStatus(applicationDto.getApplicationNo(), roleId, TaskConsts.TASK_STATUS_PENDING).getEntity();
            List<TaskDto> readTaskDtos = organizationClient.getTaskByApplicationNoAndRoleIdAndStatus(applicationDto.getApplicationNo(), roleId, TaskConsts.TASK_STATUS_READ).getEntity();
            if(IaisCommonUtils.isEmpty(pendingTaskDtos) && IaisCommonUtils.isEmpty(readTaskDtos)){
                hasNoTask = true;
            }
            if(hasNoTask){
                if(status.equals(applicationDto.getStatus())) {
                    if (applicationDtoList == null) {
                        applicationDtoList = IaisCommonUtils.genNewArrayList();
                        applicationDtoList.add(applicationDto);
                    } else {
                        applicationDtoList.add(applicationDto);
                    }
                }
            }
        }
        return applicationDtoList;
    }
    private boolean checkAllStatus(List<ApplicationDto> applicationDtoList, String status) {
        boolean flag = false;
        if (!IaisCommonUtils.isEmpty(applicationDtoList) && !StringUtil.isEmpty(status)) {
            int index = 0;
            for (ApplicationDto applicationDto : applicationDtoList) {
                if (status.equals(applicationDto.getStatus())) {
                    index++;
                }
            }
            if (index == applicationDtoList.size()) {
                flag = true;
            }
        }

        return flag;
    }
    private List<ApplicationDto> removeFastTrackingAndTransfer(List<ApplicationDto> applicationDtos) {
        List<ApplicationDto> result = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(applicationDtos)) {
            for (ApplicationDto applicationDto : applicationDtos) {
                if (ApplicationConsts.APPLICATION_STATUS_TRANSFER_ORIGIN.equals(applicationDto.getStatus())) {
                    continue;
                }
                if (!applicationDto.isFastTracking()) {
                    result.add(applicationDto);
                }
            }
        }
        return result;
    }
}
