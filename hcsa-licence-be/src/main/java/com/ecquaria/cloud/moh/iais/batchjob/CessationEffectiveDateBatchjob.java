package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSubSvcRelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcBusinessDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcOtherInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionEmailTemplateDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.service.OrganizationService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailSmsClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskOrganizationClient;
import com.ecquaria.cloud.moh.iais.util.DealSessionUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author weilu
 * @date 2020/2/13 9:42
 * Check for Cessation Effective Date  when cessation date == new date make licence inactive
 */
@Delegator("CessationEffectiveDateBatchjob")
@Slf4j
public class CessationEffectiveDateBatchjob {
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private OrganizationClient organizationClient;
    @Autowired
    SystemParamConfig systemParamConfig;
    @Autowired
    private CessationClient cessationClient;
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    private BeEicGatewayClient gatewayClient;
    @Autowired
    private LicCommService licCommService;
    @Autowired
    protected OrganizationService organizationService;
    @Autowired
    private TaskOrganizationClient taskOrganizationClient;
    @Value("${iais.email.sender}")
    private String mailSender;
    @Autowired
    private EmailSmsClient emailSmsClient;
    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("The CessationEffectiveDateBatchjob is start ..."));
    }

    public void doBatchJob(BaseProcessClass bpc) {
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        jobExecute();
    }

    public void jobExecute() {
        {
            Date date = new Date();
            //licence
            log.debug(StringUtil.changeForLog("The CessationLicenceBatchJob is doBatchJob ..."));
            Set<LicenceDto> licenceDtos = IaisCommonUtils.genNewHashSet();
            Set<LicenceDto> licNeedNew = IaisCommonUtils.genNewHashSet();
            List<String> filterLicenceId = IaisCommonUtils.genNewArrayList();
            Map<String, String> licGrpMap = IaisCommonUtils.genNewHashMap();
            List<ApplicationGroupDto> applicationGroupDtos = cessationClient.listAppGrpForCess().getEntity();
            List<ApplicationGroupDto> applicationGroupDtosCesead = IaisCommonUtils.genNewArrayList();
            if (!IaisCommonUtils.isEmpty(applicationGroupDtos)) {
                for (ApplicationGroupDto applicationGroupDto : applicationGroupDtos) {
                    try {
                        String appGrpId = applicationGroupDto.getId();
                        log.info(StringUtil.changeForLog("====================appGrpId==================" + appGrpId));
                        List<ApplicationDto> applicationDtos = applicationClient.getAppDtosByAppGrpId(appGrpId).getEntity();
                        boolean grpLic = applicationDtos.get(0).isGrpLic();
                        String originLicenceId = applicationDtos.get(0).getOriginLicenceId();
                        LicenceDto licenceDto = hcsaLicenceClient.getLicDtoById(originLicenceId).getEntity();
                        /*boolean flg=licenceDto != null && ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(licenceDto.getStatus())|| licenceDto != null && IaisEGPHelper.isActiveMigrated()&&licenceDto.getMigrated()!=0&&ApplicationConsts.LICENCE_STATUS_APPROVED.equals(licenceDto.getStatus());*/
                        if (grpLic) {/*
                            Set<String> statusSet = IaisCommonUtils.genNewHashSet();
                            for (ApplicationDto applicationDto : applicationDtos) {
                                String status = applicationDto.getStatus();
                                statusSet.add(status);
                            }
                            if (statusSet.size() == 1 && statusSet.contains(ApplicationConsts.APPLICATION_STATUS_CESSATION_NOT_LICENCE)) {
                                if (flg) {
                                    if (!filterLicenceId.contains(licenceDto.getId())) {
                                        licenceDtos.add(licenceDto);
                                        filterLicenceId.add(licenceDto.getId());
                                        licGrpMap.put(originLicenceId, appGrpId);
                                    }
                                }
                                continue;
                            }
                            for (ApplicationDto applicationDto : applicationDtos) {
                                try {
                                    String appId = applicationDto.getId();
                                    String status = applicationDto.getStatus();
                                    boolean needNewLicNo = applicationDto.isNeedNewLicNo();
                                    AppPremiseMiscDto appPremiseMiscDto = cessationClient.getAppPremiseMiscDtoByAppId(appId).getEntity();
                                    if ((ApplicationConsts.APPLICATION_STATUS_CESSATION_NEED_LICENCE.equals(status) || ApplicationConsts.APPLICATION_STATUS_CESSATION_TEMPORARY_LICENCE.equals(status))) {
                                        if (appPremiseMiscDto != null) {
                                            Date effectiveDate = appPremiseMiscDto.getEffectiveDate();
                                            if (effectiveDate.compareTo(date) <= 0) {
                                                String originLicenceIdCes = applicationDto.getOriginLicenceId();
                                                LicenceDto licenceDtoCes = hcsaLicenceClient.getLicDtoById(originLicenceIdCes).getEntity();
                                                if (licenceDtoCes != null) {
                                                    if (!filterLicenceId.contains(licenceDtoCes.getId())) {
                                                        filterLicenceId.add(licenceDtoCes.getId());
                                                        licenceDtos.add(licenceDtoCes);
                                                        if(needNewLicNo){
                                                            licNeedNew.add(licenceDtoCes);
                                                        }
                                                        applicationGroupDtosCesead.add(applicationGroupDto);
                                                        licGrpMap.put(originLicenceIdCes, appGrpId);
                                                    }
                                                }
                                                break;
                                            }
                                        } else {
                                            String originLicenceIdCes = applicationDto.getOriginLicenceId();
                                            LicenceDto licenceDtoCes = hcsaLicenceClient.getLicDtoById(originLicenceIdCes).getEntity();
                                            if (licenceDtoCes != null) {
                                                if (!filterLicenceId.contains(licenceDtoCes.getId())) {
                                                    filterLicenceId.add(licenceDtoCes.getId());
                                                    licenceDtos.add(licenceDtoCes);
                                                    applicationGroupDtosCesead.add(applicationGroupDto);
                                                    licGrpMap.put(originLicenceIdCes, appGrpId);
                                                    if(needNewLicNo){
                                                        licNeedNew.add(licenceDtoCes);
                                                    }
                                                    break;
                                                }
                                            }
                                        }

                                    }
                                } catch (Exception e) {
                                    log.error(e.getMessage(), e);
                                    continue;
                                }
                            }
                            //create grp licence and ceased old licence
                            List<String> grpLicIds = IaisCommonUtils.genNewArrayList();
                            grpLicIds.add(appGrpId);
                            List<ApplicationLicenceDto> applicationLicenceDtos = applicationClient.getCessGroup(grpLicIds).getEntity();
                            List<String> serviceIds = licenceApproveBatchjob.getAllServiceId(applicationLicenceDtos);
                            List<HcsaServiceDto> hcsaServiceDtos = licenceService.getHcsaServiceById(serviceIds);
                            if (hcsaServiceDtos == null || hcsaServiceDtos.size() == 0) {
                                log.debug(StringUtil.changeForLog("This serviceIds can not get the HcsaServiceDto -->:" + serviceIds));
                                continue;
                            }
                            for (ApplicationLicenceDto applicationLicenceDto : applicationLicenceDtos) {
                                List<ApplicationListDto> newApplicationListDtoLists = IaisCommonUtils.genNewArrayList();
                                List<ApplicationListDto> oldApplicationListDtoList = applicationLicenceDto.getApplicationListDtoList();
                                for (ApplicationListDto applicationListDto : oldApplicationListDtoList) {
                                    ApplicationDto applicationDto = applicationListDto.getApplicationDto();
                                    if (applicationDto.isNeedNewLicNo()) {
                                        newApplicationListDtoLists.add(applicationListDto);
                                    }
                                }
                                log.info(StringUtil.changeForLog("====================newApplicationListDtoLists  size==================" + newApplicationListDtoLists.size()));
                                applicationLicenceDto.setApplicationListDtoList(newApplicationListDtoLists);
                                LicenceApproveBatchjob.GenerateResult groupGenerateResult = licenceApproveBatchjob.generateGroupLicence(applicationLicenceDto, hcsaServiceDtos);
                                licenceApproveBatchjob.createCessLicence(applicationGroupDto, null, groupGenerateResult);
                            }*/
                        } else {
                            for (ApplicationDto applicationDto : applicationDtos) {
                                String appId = applicationDto.getId();
                                AppPremiseMiscDto appPremiseMiscDto = cessationClient.getAppPremiseMiscDtoByAppId(appId).getEntity();
                                if (appPremiseMiscDto != null&&originLicenceId!=null) {
                                    Date effectiveDate = appPremiseMiscDto.getEffectiveDate();
                                    if (effectiveDate.compareTo(date) <= 0) {
                                        //if (flg) {
                                            applicationGroupDtosCesead.add(applicationGroupDto);
                                            licenceDtos.add(licenceDto);
                                            licGrpMap.put(originLicenceId, appGrpId);
                                       // }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error(e.getMessage(), e);
                        continue;
                    }
                }
                try {
                    //update and send email
                    updateLicencesStatusAndSendMails(licenceDtos, date, licGrpMap,licNeedNew);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
                //update appGrp
                updateAppGroups(applicationGroupDtosCesead);
            }
        }
    }

    private void updateLicencesStatusAndSendMails(Set<LicenceDto> licenceDtos, Date date, Map<String, String> licGrpMap,Set<LicenceDto> licNeedNew) {
        List<LicenceDto> updateLicenceDtos = IaisCommonUtils.genNewArrayList();
        AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
        //List<String> specLicIdsAll = IaisCommonUtils.genNewArrayList();
        /*for (LicenceDto licenceDto : licenceDtos) {
            String licId = licenceDto.getId();
            List<String> specLicIds = hcsaLicenceClient.getActSpecIdByActBaseId(licId).getEntity();
            if (!IaisCommonUtils.isEmpty(specLicIds)) {
                specLicIdsAll.addAll(specLicIds);
            }
        }*/
        for (LicenceDto licenceDto : licenceDtos) {
            if (licenceDto == null) {
                continue;
            }
            try {
                List<String> serviceCodes = IaisCommonUtils.genNewArrayList();
                licenceDto.setAuditTrailDto(auditTrailDto);
                licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_CEASED);
                licenceDto.setEndDate(date);
                updateLicenceDtos.add(licenceDto);
                /*if (specLicIdsAll.contains(licenceDto.getId())) {
                    continue;
                }*/
                String svcName = licenceDto.getSvcName();
                HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(svcName);
                serviceCodes.add(hcsaServiceDto.getSvcCode());
                String licenceNo = licenceDto.getLicenceNo();
                String licId = licenceDto.getId();
                Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
                StringBuilder svcNameLicNo = new StringBuilder();
                svcNameLicNo.append(svcName).append(" : ").append(licenceNo);
                /*List<String> specLicIds = hcsaLicenceClient.getActSpecIdByActBaseId(licId).getEntity();
                if (!IaisCommonUtils.isEmpty(specLicIds)) {
                    for (String specLicId : specLicIds) {
                        LicenceDto specLicDto = hcsaLicenceClient.getLicDtoById(specLicId).getEntity();
                        String svcName1 = specLicDto.getSvcName();
                        String licenceNo1 = specLicDto.getLicenceNo();
                        serviceCodes.add("<br/>");
                        svcNameLicNo.append("<br/>");
                        svcNameLicNo.append(svcName1).append(" : ").append(licenceNo1);
                        HcsaServiceDto hcsaServiceDto1 = HcsaServiceCacheHelper.getServiceByServiceName(svcName1);
                        serviceCodes.add(hcsaServiceDto1.getSvcCode());
                    }
                }*/
                String groupId = licGrpMap.get(licId);
                StringBuilder appNos = new StringBuilder();
                OrgUserDto orgUserDto = null;
                if (!StringUtil.isEmpty(groupId)) {
                    ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(groupId).getEntity();
                    String groupNo = applicationGroupDto.getGroupNo();
                    groupNo = groupNo + "-01";
                    boolean isBeCessationFlow = false;
                    List<AppPremisesRoutingHistoryDto> temp = applicationClient.getHistoryByAppNoAndDecision(groupNo, ApplicationConsts.APPLICATION_STATUS_CESSATION_BE_DECISION).getEntity();
                    if (!IaisCommonUtils.isEmpty(temp) && temp.size() < 2) {
                        isBeCessationFlow = true;
                    }
                    if(isBeCessationFlow){
                        String userId = applicationClient.getUserIdBylicenseeId(applicationGroupDto.getLicenseeId()).getEntity();
                        orgUserDto = organizationClient.retrieveOrgUserAccountById(userId).getEntity();
                    }else {
                        orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
                    }
                    List<ApplicationDto> applicationDtos = applicationClient.getAppDtosByAppGrpId(groupId).getEntity();
                    for (ApplicationDto applicationDto : applicationDtos) {
                        String applicationNo = applicationDto.getApplicationNo();
                        String status = applicationDto.getStatus();
                        if (ApplicationConsts.APPLICATION_STATUS_CESSATION_NOT_LICENCE.equals(status)) {
                            appNos.append(' ');
                            appNos.append(applicationNo);
                        }
                    }
                }
                if (orgUserDto != null) {
                    emailMap.put("ApplicantName", orgUserDto.getDisplayName());
                } else {
                    emailMap.put("ApplicantName", "");
                    log.info(StringUtil.changeForLog("updateLicencesStatusAndSendMails Function: orgUserDto is null------->LicenceId =" + licenceDto.getId()));
                }
                emailMap.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{ApplicationConsts.APPLICATION_TYPE_CESSATION}).get(0).getText());
                emailMap.put("ServiceLicenceName", svcNameLicNo.toString());
                emailMap.put("ApplicationNumber", appNos.toString());
                emailMap.put("CessationDate", DateFormatUtils.format(date, "dd/MM/yyyy"));
                emailMap.put("email", systemParamConfig.getSystemAddressOne());
                emailMap.put("MOH_AGENCY_NAM_GROUP", "<b>" + AppConsts.MOH_AGENCY_NAM_GROUP + "</b>");
                emailMap.put("MOH_AGENCY_NAME", "<b>" + AppConsts.MOH_AGENCY_NAME + "</b>");
                EmailParam emailParam = new EmailParam();
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_JOB_CEASE_EFFECTIVE_DATE);
                emailParam.setTemplateContent(emailMap);
                emailParam.setQueryCode(licenceNo);
                emailParam.setReqRefNum(licenceNo);
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
                emailParam.setRefId(licId);
                Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_JOB_CEASE_EFFECTIVE_DATE);
                map.put("ApplicationType", MasterCodeUtil.retrieveOptionsByCodes(new String[]{ApplicationConsts.APPLICATION_TYPE_CESSATION}).get(0).getText());
                map.put("ApplicationNumber", appNos.toString());
                String subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(), map);
                emailParam.setSubject(subject);
                //email
                notificationHelper.sendNotification(emailParam);
                //sms
                rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_JOB_CEASE_EFFECTIVE_DATE_SMS);
                subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(), map);
                EmailParam smsParam = new EmailParam();
                smsParam.setQueryCode(licenceNo);
                smsParam.setReqRefNum(licenceNo);
                smsParam.setRefId(licId);
                smsParam.setTemplateContent(emailMap);
                smsParam.setSubject(subject);
                smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_JOB_CEASE_EFFECTIVE_DATE_SMS);
                smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
                notificationHelper.sendNotification(smsParam);
                //msg
                rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_JOB_CEASE_EFFECTIVE_DATE_MSG);
                subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(), map);
                EmailParam msgParam = new EmailParam();
                msgParam.setQueryCode(licenceNo);
                msgParam.setReqRefNum(licenceNo);
                msgParam.setRefId(licId);
                msgParam.setTemplateContent(emailMap);
                msgParam.setSubject(subject);
                msgParam.setSvcCodeList(serviceCodes);
                msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_JOB_CEASE_EFFECTIVE_DATE_MSG);
                msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                msgParam.setRefId(licenceDto.getId());
                notificationHelper.sendNotification(msgParam);

                AppSubmissionDto appSubmissionDto = licCommService.viewAppSubmissionDto(licId);
                DealSessionUtil.initView(appSubmissionDto);
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
                boolean hasTopYf=false;
                if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
                    for (AppSvcRelatedInfoDto appSvcRelatedInfoDto:appSvcRelatedInfoDtos
                    ) {
                        if(IaisCommonUtils.isNotEmpty(appSvcRelatedInfoDto.getAppSvcOtherInfoList())){
                            for (AppSvcOtherInfoDto otherInfo :appSvcRelatedInfoDto.getAppSvcOtherInfoList()
                            ) {
                                if(IaisCommonUtils.isNotEmpty(otherInfo.getAppPremSubSvcRelDtoList())){
                                    for (AppPremSubSvcRelDto otherRelatedInfoDto:otherInfo.getAppPremSubSvcRelDtoList()
                                    ) {
                                        if("O02".equals(otherRelatedInfoDto.getSvcCode())
                                                ||"O03".equals(otherRelatedInfoDto.getSvcCode())
                                                ||"O04".equals(otherRelatedInfoDto.getSvcCode())
                                                ||"O07".equals(otherRelatedInfoDto.getSvcCode())){
                                            hasTopYf=true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if(hasTopYf){
                    List<OrgUserDto> orgUserDtos = taskOrganizationClient.retrieveOrgUserAccountByRoleId(RoleConsts.USER_ROLE_ASO).getEntity();
                    SubLicenseeDto orgLicensee = organizationService.getSubLicenseeByLicenseeId(licenceDto.getLicenseeId());
                    MsgTemplateDto msgTemplateDto = notificationHelper.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_CEASE_EMAIL_005_TOP_YF);
                    Map<String, Object> mapTop = IaisCommonUtils.genNewHashMap();
                    mapTop.put("ApplicationNumber", appNos.toString());
                    subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(), mapTop);
                    for (OrgUserDto aso:orgUserDtos
                         ) {
                        List<AppSvcBusinessDto> appSvcBusinessDtoList=appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getAppSvcBusinessDtoList();
                        Map<String, Object> emailMap1 = IaisCommonUtils.genNewHashMap();
                        emailMap1.put("aso_officer_name", aso.getDisplayName());
                        emailMap1.put("licenceNumber", licenceNo);
                        emailMap1.put("LicenseeName", orgLicensee.getLicenseeName());
                        emailMap1.put("LicenceStartDateEndDate", DateFormatUtils.format(licenceDto.getStartDate(), "dd/MM/yyyy")+" - "+DateFormatUtils.format(date, "dd/MM/yyyy"));
                        emailMap1.put("MOSD", appSubmissionDto.getAppGrpPremisesDtoList().get(0).getAddress());
                        emailMap1.put("BusinessName", "-");
                        if(IaisCommonUtils.isNotEmpty(appSvcBusinessDtoList)){
                            emailMap1.put("BusinessName", appSvcBusinessDtoList.get(0).getBusinessName());
                        }
                        emailMap1.put("ServiceName", svcName);
                        emailMap1.put("MOH_AGENCY_NAME", "<b>" + AppConsts.MOH_AGENCY_NAME + "</b>");
                        //email
                        EmailDto emailDto = new EmailDto();
                        List<String> receiptEmail=IaisCommonUtils.genNewArrayList();
                        receiptEmail.add(aso.getEmail());
                        Set<String> set = IaisCommonUtils.genNewHashSet();
                        set.addAll(receiptEmail);
                        receiptEmail.clear();
                        receiptEmail.addAll(set);
                        emailDto.setReceipts(receiptEmail);
                        String mesContext = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getMessageContent(), emailMap1);
                        emailDto.setContent(mesContext);
                        emailDto.setSubject(subject);
                        emailDto.setSender(this.mailSender);
                        emailDto.setClientQueryCode(licenceNo);
                        emailDto.setReqRefNum(licenceNo);
                        int emailFlag = systemParamConfig.getEgpEmailNotifications();
                        if (0 == emailFlag) {
                            log.info("please turn on email param.......");
                        }else {
                            emailSmsClient.sendEmail(emailDto, null);
                        }
                    }

                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                continue;
            }
        }

        if (!IaisCommonUtils.isEmpty(updateLicenceDtos)) {
            if(!IaisCommonUtils.isEmpty(licNeedNew)){
                updateLicenceDtos.removeAll(licNeedNew);
            }
            hcsaLicenceClient.updateLicences(updateLicenceDtos).getEntity();
            callEicSync(updateLicenceDtos);
        }
    }

    public void callEicSync(List<LicenceDto> updateLicenceDtos) {
        gatewayClient.callEicWithTrack(updateLicenceDtos, gatewayClient::updateFeLicDto, gatewayClient.getClass(),
                "updateFeLicDto", EicClientConstant.LICENCE_CLIENT);
    }

    private void updateAppGroups(List<ApplicationGroupDto> applicationGroupDtos) {
        if (IaisCommonUtils.isEmpty(applicationGroupDtos)) {
            return;
        }
        AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
        for (ApplicationGroupDto applicationGroupDto : applicationGroupDtos) {
            applicationGroupDto.setAuditTrailDto(auditTrailDto);
            applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_LICENCE_GENERATED);
        }
        applicationClient.updateApplications(applicationGroupDtos);
    }
}
