package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.job.executor.biz.model.ReturnT;
import com.ecquaria.cloud.job.executor.log.JobLogger;
import com.ecquaria.cloud.moh.iais.action.HcsaApplicationDelegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.constant.acra.AcraConsts;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.constant.task.TaskConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppReturnFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
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
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloudfeign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Delegator("approvewithdrawalDelegator")
@Slf4j
public class ApproveWdAppBatchJob {

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private TaskService taskService;

    @Autowired
    private AppPremisesRoutingHistoryService appPremisesRoutingHistoryService;

    @Autowired
    CessationClient cessationClient;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private OrganizationClient organizationClient;

    @Value("${iais.system.one.address}")
    private String systemAddressOne;


    public void startStep(BaseProcessClass bpc) {

    }

    public void approveWithdrawalStep(BaseProcessClass bpc) {
        try {
            AuditTrailHelper.setupBatchJobAuditTrail(this);
            List<ApplicationDto> withdrawApplicationDtoList = applicationClient.saveWithdrawn().getEntity();
            //get old application
            HcsaApplicationDelegator newApplicationDelegator = SpringContextHelper.getContext().getBean(HcsaApplicationDelegator.class);
            if (!IaisCommonUtils.isEmpty(withdrawApplicationDtoList)) {
                withdrawApplicationDtoList.forEach(h -> {
                    applicationService.updateFEApplicaiton(h);
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
                        String serviceName = HcsaServiceCacheHelper.getServiceById(serviceId).getSvcName();
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
                            if (!StringUtil.isEmpty(fee)){
                                boolean withdrawReturnFee = applicationService.isWithdrawReturnFee(h.getApplicationNo());
                                if (withdrawReturnFee){
                                    AppReturnFeeDto appReturnFeeDto = assembleReturn(h, fee);
                                    applicationService.saveAppReturnFee(appReturnFeeDto);
                                }
                            }
                        }catch (Exception e){
                            log.error("Withdraw application return is failed");
                            log.error(e.getMessage(), e);
                        }
                        Map<String, Object> msgInfoMap = IaisCommonUtils.genNewHashMap();
                        msgInfoMap.put("Applicant", applicantName);
                        msgInfoMap.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationType1));
                        msgInfoMap.put("ApplicationNumber", applicationNo);
                        msgInfoMap.put("reqAppNo", applicationNo);
                        msgInfoMap.put("S_LName", serviceName);
                        msgInfoMap.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);
                        msgInfoMap.put("ApplicationDate", Formatter.formatDateTime(new Date()));
                        if (StringUtil.isEmpty(paymentMethod) ||  StringUtil.isEmpty(fee) ||
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
                        msgInfoMap.put("adminFee", ApplicationConsts.PAYMRNT_ADMIN_FEE);
                        msgInfoMap.put("systemLink", loginUrl);
                        msgInfoMap.put("emailAddress", systemAddressOne);
                        try {
                            newApplicationDelegator.sendEmail(MsgTemplateConstants.MSG_TEMPLATE_WITHDRAWAL_APP_APPROVE_EMAIL, msgInfoMap, oldApplication);
                            newApplicationDelegator.sendSMS(oldApplication, MsgTemplateConstants.MSG_TEMPLATE_WITHDRAWAL_APP_APPROVE_SMS, msgInfoMap);
                            newApplicationDelegator.sendInboxMessage(oldApplication, serviceId, msgInfoMap, MsgTemplateConstants.MSG_TEMPLATE_WITHDRAWAL_APP_APPROVE_MESSAGE);
                        } catch (Exception e) {
                            log.info(e.getMessage(), e);
                        }
                    }
                });
                log.debug(StringUtil.changeForLog("**** The withdraw Application List size" + withdrawApplicationDtoList.size()));
                List<String> oldAppGroupExcuted = IaisCommonUtils.genNewArrayList();
                if (!IaisCommonUtils.isEmpty(withdrawApplicationDtoList)) {
                    for (ApplicationDto oldApplicationDto : withdrawApplicationDtoList) {
                        doWithdrawal(oldApplicationDto, oldAppGroupExcuted);
                    }
                }
                JobLogger.log(StringUtil.changeForLog("The withdraw Application List" + withdrawApplicationDtoList.size()));
            } else {
                JobLogger.log(StringUtil.changeForLog("The withdraw Application List is null *****"));
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            JobLogger.log(e);
        }
    }

    private AppReturnFeeDto assembleReturn(ApplicationDto applicationDto, Double returnFee){
        AppReturnFeeDto appReturnFeeDto = new AppReturnFeeDto();
        appReturnFeeDto.setStatus("paying");
        appReturnFeeDto.setTriggerCount(0);
        appReturnFeeDto.setApplicationNo(applicationDto.getApplicationNo());
        appReturnFeeDto.setReturnAmount(returnFee);
        appReturnFeeDto.setReturnType(ApplicationConsts.APPLICATION_RETURN_FEE_REJECT);
        return appReturnFeeDto;
    }

    private void doWithdrawal(ApplicationDto oldApplicationDto, List<String> oldAppGroupExcuted) throws FeignException {
        if (oldApplicationDto != null) {
            log.info(StringUtil.changeForLog("withdrawal old application id : " + oldApplicationDto.getId()));
            String oldAppGrpId = oldApplicationDto.getAppGrpId();
            String currentOldApplicationNo = oldApplicationDto.getApplicationNo();
            List<ApplicationDto> applicationDtoList = applicationService.getApplicaitonsByAppGroupId(oldAppGrpId);
            if (IaisCommonUtils.isEmpty(applicationDtoList) || applicationDtoList.size() == 1) {
                return;
            } else {
                if (!oldAppGroupExcuted.contains(oldAppGrpId)) {
                    List<ApplicationDto> ao1AppList = getStatusAppList(applicationDtoList, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01, RoleConsts.USER_ROLE_AO1, currentOldApplicationNo);
                    List<ApplicationDto> ao2AppList = getStatusAppList(applicationDtoList, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02, RoleConsts.USER_ROLE_AO2, currentOldApplicationNo);
                    List<ApplicationDto> ao3AppList = getStatusAppList(applicationDtoList, ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03, RoleConsts.USER_ROLE_AO3, currentOldApplicationNo);
                    if (!IaisCommonUtils.isEmpty(ao1AppList)) {
                        return;
                    } else {
                        //ao1 == null
                        if (!IaisCommonUtils.isEmpty(ao2AppList)) {
                            //create task
                            createTaskAndHistory(ao2AppList, HcsaConsts.ROUTING_STAGE_AO2, RoleConsts.USER_ROLE_AO2, oldAppGroupExcuted, oldAppGrpId);
                        } else {
                            if (!IaisCommonUtils.isEmpty(ao3AppList)) {
                                //create task
                                createTaskAndHistory(ao3AppList, HcsaConsts.ROUTING_STAGE_AO3, RoleConsts.USER_ROLE_AO3, oldAppGroupExcuted, oldAppGrpId);
                            } else {
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private void createTaskAndHistory(List<ApplicationDto> creatTaskApplicationList, String stageId, String roleId, List<String> oldAppGroupExcuted, String oldAppGrpId) throws FeignException {
        TaskHistoryDto taskHistoryDto = taskService.getRoutingTaskOneUserForSubmisison(creatTaskApplicationList,
                stageId, roleId, IaisEGPHelper.getCurrentAuditTrailDto(), RoleConsts.USER_ROLE_SYSTEM_USER_ADMIN, null);
        if (taskHistoryDto != null) {
            List<TaskDto> taskDtos = taskHistoryDto.getTaskDtoList();
            List<AppPremisesRoutingHistoryDto> appPremisesRoutingHistoryDtos = taskHistoryDto.getAppPremisesRoutingHistoryDtos();
            taskService.createTasks(taskDtos);
            appPremisesRoutingHistoryService.createHistorys(appPremisesRoutingHistoryDtos);
            oldAppGroupExcuted.add(oldAppGrpId);
        }
    }

    private List<ApplicationDto> getOldApplicationDtoList(List<ApplicationDto> withdrawalApplicationDtoList) {
        if (IaisCommonUtils.isEmpty(withdrawalApplicationDtoList)) {
            return null;
        }
        List<ApplicationDto> oldApplicationList = IaisCommonUtils.genNewArrayList();
        for (ApplicationDto withdrawalApplicationDto : withdrawalApplicationDtoList) {
            AppPremiseMiscDto premiseMiscDto = cessationClient.getAppPremiseMiscDtoByAppId(withdrawalApplicationDto.getId()).getEntity();
            if (premiseMiscDto != null) {
                String oldAppId = premiseMiscDto.getRelateRecId();
                log.info(StringUtil.changeForLog("withdrawal old application id : " + oldAppId));
                ApplicationDto oldApplication = applicationClient.getApplicationById(oldAppId).getEntity();
                if (oldApplication != null) {
                    oldApplicationList.add(oldApplication);
                }
            }
        }
        return oldApplicationList;
    }

    private List<ApplicationDto> getStatusAppList(List<ApplicationDto> applicationDtos, String status, String roleId, String currentAppNo) {
        if (IaisCommonUtils.isEmpty(applicationDtos) || StringUtil.isEmpty(status) || StringUtil.isEmpty(roleId) || StringUtil.isEmpty(currentAppNo)) {
            return null;
        }
        List<ApplicationDto> applicationDtoList = null;
        for (ApplicationDto applicationDto : applicationDtos) {
            if (currentAppNo.equals(applicationDto.getApplicationNo())) {
                continue;
            }
            //have uncompleted task
            boolean hasNoTask = false;
            List<TaskDto> pendingTaskDtos = organizationClient.getTaskByApplicationNoAndRoleIdAndStatus(applicationDto.getApplicationNo(), roleId, TaskConsts.TASK_STATUS_PENDING).getEntity();
            List<TaskDto> readTaskDtos = organizationClient.getTaskByApplicationNoAndRoleIdAndStatus(applicationDto.getApplicationNo(), roleId, TaskConsts.TASK_STATUS_READ).getEntity();
            if (IaisCommonUtils.isEmpty(pendingTaskDtos) && IaisCommonUtils.isEmpty(readTaskDtos)) {
                hasNoTask = true;
            }
            if (hasNoTask) {
                if (status.equals(applicationDto.getStatus())) {
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


}
