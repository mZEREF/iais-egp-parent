package com.ecquaria.cloud.moh.iais.batchjob;

import com.ecquaria.cloud.annotation.Delegator;
import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.emailsms.EmailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSubSvcRelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
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
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.CessationBeService;
import com.ecquaria.cloud.moh.iais.service.InspEmailService;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.service.OrganizationService;
import com.ecquaria.cloud.moh.iais.service.client.AppCommClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.EmailSmsClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskOrganizationClient;
import com.ecquaria.cloud.moh.iais.util.DealSessionUtil;
import com.ecquaria.sz.commons.util.MsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import sop.util.DateUtil;
import sop.webflow.rt.api.BaseProcessClass;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author weilu
 * @Date 2020/4/27 8:27
 */
@Delegator("licenceExpiredBatchJob")
@Slf4j
public class LicenceExpiredBatchJob {
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private CessationBeService cessationBeService;
    @Autowired
    ApplicationClient applicationClient;
    @Autowired
    private BeEicGatewayClient gatewayClient;
    @Autowired
    SystemParamConfig systemParamConfig;
    @Autowired
    InspEmailService inspEmailService;
    @Autowired
    private NotificationHelper notificationHelper;
    @Autowired
    HcsaConfigClient hcsaConfigClient;
    @Autowired
    OrganizationClient organizationClient;
    @Autowired
    private LicCommService licCommService;
    @Autowired
    private AppCommClient appCommClient;
    @Autowired
    protected OrganizationService organizationService;
    @Autowired
    private TaskOrganizationClient taskOrganizationClient;
    @Value("${iais.email.sender}")
    private String mailSender;

    @Autowired
    private EmailSmsClient emailSmsClient;

    public void start(BaseProcessClass bpc) {
        log.debug(StringUtil.changeForLog("The licenceExpiredBatchJob is start ..."));
    }

    public void doBatchJob(BaseProcessClass bpc) {
        AuditTrailHelper.setupBatchJobAuditTrail(this);
        jobExecute();
    }

    public void jobExecute() {
        //licence
        log.debug(StringUtil.changeForLog("The CessationLicenceBatchJob is doBatchJob ..."));
        Date date = new Date();
        String dateStr = DateUtil.formatDate(date, "yyyy-MM-dd");
        //get expired date + 1 = today de licence
        List<LicenceDto> licenceDtos = hcsaLicenceClient.cessationLicenceDtos(ApplicationConsts.LICENCE_STATUS_ACTIVE,
                dateStr).getEntity();
        Date expiredDate= MiscUtil.todayAddDays(30);
        String dateStr30 = DateUtil.formatDate(expiredDate, "yyyy-MM-dd");
        List<LicenceDto> licenceDtosForRenewEmail = hcsaLicenceClient.cessationLicenceDtos(ApplicationConsts.LICENCE_STATUS_ACTIVE,
                dateStr30).getEntity();
        List<LicenceDto> licenceDtosForSave = IaisCommonUtils.genNewArrayList();
        List<String> ids = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(licenceDtos)) {
            for (LicenceDto licenceDto : licenceDtos) {
                //shi fou you qi ta de app zai zuo
                String licId = licenceDto.getId();
                ids.clear();
                ids.add(licId);
                Map<String, Boolean> stringBooleanMap = cessationBeService.listResultCeased(ids);
                if (stringBooleanMap.get(licId)) {
                    licenceDtosForSave.add(licenceDto);
                }
            }
            updateLicenceStatus(licenceDtosForSave, date);
        }
        if (!IaisCommonUtils.isEmpty(licenceDtosForRenewEmail)) {
            for (LicenceDto licenceDto : licenceDtosForRenewEmail) {
                try {
                    AppSubmissionDto appSubmissionDto = licCommService.viewAppSubmissionDto(licenceDto.getId());
                    Date expiryDate=licenceDto.getExpiryDate();

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
                        MsgTemplateDto msgTemplateDto = notificationHelper.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_EXPIRED_TOP_YF);
                        String svcName = licenceDto.getSvcName();
                        String licenceNo = licenceDto.getLicenceNo();

                        for (OrgUserDto aso:orgUserDtos
                        ) {
                            List<AppSvcBusinessDto> appSvcBusinessDtoList=appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getAppSvcBusinessDtoList();
                            Map<String, Object> emailMap1 = IaisCommonUtils.genNewHashMap();
                            emailMap1.put("aso_officer_name", aso.getDisplayName());
                            emailMap1.put("licenceNumber", licenceNo);
                            emailMap1.put("LicenseeName", orgLicensee.getLicenseeName());
                            emailMap1.put("LicenceStartDateEndDate", DateFormatUtils.format(licenceDto.getStartDate(), "dd/MM/yyyy")+" - "+DateFormatUtils.format(expiryDate, "dd/MM/yyyy"));
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
                            emailDto.setSubject(msgTemplateDto.getTemplateName());
                            emailDto.setSender(this.mailSender);
                            emailDto.setClientQueryCode(licenceNo);
                            emailDto.setReqRefNum(licenceNo);
                            emailSmsClient.sendEmail(emailDto, null);
                        }
                    }
                }catch (Exception e ){
                    log.info(e.getMessage(),e);
                }
            }
        }
        //effective Date
        List<LicenceDto> effectLicDtos = hcsaLicenceClient.getLicByEffDate().getEntity();
        if (!IaisCommonUtils.isEmpty(effectLicDtos)) {
            updateLicenceStatusEffect(effectLicDtos, date);
        }
        //approved licence
        List<LicenceDto> approvedLicence = hcsaLicenceClient.getLicDtosWithApproved().getEntity();
        if (!IaisCommonUtils.isEmpty(approvedLicence)) {
            for (LicenceDto licenceDto : approvedLicence) {
                licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_ACTIVE);
            }
            updateLicenceStatusApproved(approvedLicence);
        }

    }

    private void updateLicenceStatus(List<LicenceDto> licenceDtos, Date date) {
        log.info(StringUtil.changeForLog("The updateLicenceStatus start ..."));
        List<LicenceDto> updateLicenceDtos = IaisCommonUtils.genNewArrayList();
        AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
        for (LicenceDto licenceDto : licenceDtos) {
            try {
            List<String> serviceCodes = IaisCommonUtils.genNewArrayList();
            licenceDto.setAuditTrailDto(auditTrailDto);
            String licId = licenceDto.getId();
            String svcName = licenceDto.getSvcName();
            String licenceNo = licenceDto.getLicenceNo();
            log.info(StringUtil.changeForLog("The updateLicenceStatus  licenceNo is -->:"+licenceNo));
            StringBuilder svcNameLicNo = new StringBuilder();
            svcNameLicNo.append(svcName).append(" : ").append(licenceNo);
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(svcName);
            serviceCodes.add(hcsaServiceDto.getSvcCode());

            //pan daun shi dou you xin de licence sheng cheng
            LicenceDto newLicDto = hcsaLicenceClient.getLicdtoByOrgId(licId).getEntity();
            if (newLicDto == null) {
                licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_LAPSED);
            } else {
                if (ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(newLicDto.getStatus())) {
                    licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_EXPIRY);
                } else if (ApplicationConsts.LICENCE_STATUS_APPROVED.equals(newLicDto.getStatus())) {
                    licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_EXPIRY);
                    newLicDto.setStatus(ApplicationConsts.LICENCE_STATUS_ACTIVE);
                    updateLicenceDtos.add(newLicDto);
                } else {
                    licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_LAPSED);
                }
            }
            licenceDto.setEndDate(date);
            updateLicenceDtos.add(licenceDto);

                Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
                String appId= licCommService.getLicCorrBylicId(licId).get(0).getApplicationId();
                ApplicationDto applicationDto=applicationClient.getApplicationById(appId).getEntity();
                SubLicenseeDto subLicensee = appCommClient.getSubLicenseeDtoByAppId(applicationDto.getId()).getEntity();
                AppPremisesCorrelationDto appPremisesCorrelationDto=applicationClient.getAppPremCorrByAppNo(applicationDto.getApplicationNo()).getEntity();
                List<AppSvcBusinessDto> appSvcBusinessDtoList=appCommClient.getAppSvcBusinessDtoListByCorrId(appPremisesCorrelationDto.getId()).getEntity();
                ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(applicationDto.getAppGrpId()).getEntity();
                if (applicationGroupDto != null){
                    OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
                    if (orgUserDto != null){
                        emailMap.put("ApplicantName", orgUserDto.getDisplayName());
                    }
                }
                emailMap.put("ServiceLicenceName", svcNameLicNo.toString());
                emailMap.put("CessationDate", DateFormatUtils.format(date,"dd/MM/yyyy"));
                emailMap.put("email", systemParamConfig.getSystemAddressOne());
                emailMap.put("MOH_AGENCY_NAM_GROUP", "<b>" + AppConsts.MOH_AGENCY_NAM_GROUP + "</b>");
                emailMap.put("MOH_AGENCY_NAME", "<b>" + AppConsts.MOH_AGENCY_NAME + "</b>");
                emailMap.put("LicenceNo", licenceNo);
                emailMap.put("BusinessName", "-");
                if(IaisCommonUtils.isNotEmpty(appSvcBusinessDtoList)){
                    emailMap.put("BusinessName", appSvcBusinessDtoList.get(0).getBusinessName());
                }
                emailMap.put("LicenseeName", subLicensee.getDisplayName());

                EmailParam emailParam = new EmailParam();
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE);
                emailParam.setTemplateContent(emailMap);
                emailParam.setQueryCode(licenceNo);
                emailParam.setReqRefNum(licenceNo);
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
                emailParam.setRefId(licId);
                Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE);
                map.put("ServiceLicenceName", svcName);
                map.put("LicenceNumber", licenceNo);
                String subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(), map);
                emailParam.setSubject(subject);
                //email
                notificationHelper.sendNotification(emailParam);
                //sms
                EmailParam smsParam = new EmailParam();
                smsParam.setQueryCode(licenceNo);
                smsParam.setReqRefNum(licenceNo);
                smsParam.setRefId(licId);
                smsParam.setTemplateContent(emailMap);
                rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE_SMS);
                subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(), map);
                smsParam.setSubject(subject);
                smsParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE_SMS);
                smsParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
                notificationHelper.sendNotification(smsParam);
                //msg
                rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE_MSG);
                subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(), map);
                EmailParam msgParam = new EmailParam();
                msgParam.setQueryCode(licenceNo);
                msgParam.setReqRefNum(licenceNo);
                msgParam.setRefId(licId);
                msgParam.setTemplateContent(emailMap);
                msgParam.setSubject(subject);
                msgParam.setSvcCodeList(serviceCodes);
                msgParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE_MSG);
                msgParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                msgParam.setRefId(licenceDto.getId());
                notificationHelper.sendNotification(msgParam);
                try {
                    Date expiryDate=licenceDto.getExpiryDate();
                        AppSubmissionDto appSubmissionDto = licCommService.viewAppSubmissionDto(licenceDto.getId());
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
                            MsgTemplateDto msgTemplateDto = notificationHelper.getMsgTemplate(MsgTemplateConstants.MSG_TEMPLATE_RENEW_APP_SUBMITTED_TOP_YF);

                            for (OrgUserDto aso:orgUserDtos
                            ) {
                                Map<String, Object> emailMap1 = IaisCommonUtils.genNewHashMap();
                                emailMap1.put("aso_officer_name", aso.getDisplayName());
                                emailMap1.put("licenceNumber", licenceNo);
                                emailMap1.put("LicenseeName", subLicensee.getLicenseeName());
                                emailMap1.put("LicenceStartDateEndDate", DateFormatUtils.format(licenceDto.getStartDate(), "dd/MM/yyyy")+" - "+DateFormatUtils.format(expiryDate, "dd/MM/yyyy"));
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
                                emailDto.setSubject(msgTemplateDto.getTemplateName());
                                emailDto.setSender(this.mailSender);
                                emailDto.setClientQueryCode(licenceNo);
                                emailDto.setReqRefNum(licenceNo);
                                emailSmsClient.sendEmail(emailDto, null);
                            }
                        }

                }catch (Exception e ){
                    log.info(e.getMessage(),e);
                }
            } catch (Exception e) {
                log.error(StringUtil.changeForLog("The error LicenceNo -->:"+licenceDto.getLicenceNo()));
                log.error(e.getMessage(), e);
            }
        }
        try {
            hcsaLicenceClient.updateLicences(updateLicenceDtos).getEntity();
            callEicUpdateFeLicDto(updateLicenceDtos);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        log.info(StringUtil.changeForLog("The updateLicenceStatus end ..."));
    }

    private void callEicUpdateFeLicDto(List<LicenceDto> licenceDtos){
        gatewayClient.callEicWithTrack(licenceDtos, gatewayClient::updateFeLicDto, gatewayClient.getClass(),
                "updateFeLicDto", EicClientConstant.LICENCE_CLIENT);
    }

    private void updateLicenceStatusEffect(List<LicenceDto> licenceDtos, Date date) {
        List<LicenceDto> updateLicenceDtos = IaisCommonUtils.genNewArrayList();
        AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
        for (LicenceDto licenceDto : licenceDtos) {
            try {
                if(IaisEGPHelper.isActiveMigrated() && licenceDto.isMigrated()){
                    log.info(StringUtil.changeForLog("The Migrated Data ..."+licenceDto.getId()));
                    continue;
                }
                licenceDto.setAuditTrailDto(auditTrailDto);
                licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_ACTIVE);
                updateLicenceDtos.add(licenceDto);

                String originLicenceId = licenceDto.getOriginLicenceId();
                LicenceDto interimLicDto = hcsaLicenceClient.getLicDtoById(originLicenceId).getEntity();

                while(interimLicDto != null && (ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(interimLicDto.getStatus())
                        ||ApplicationConsts.LICENCE_STATUS_APPROVED.equals(interimLicDto.getStatus()))){
                    interimLicDto.setStatus(ApplicationConsts.LICENCE_STATUS_IACTIVE);
                    interimLicDto.setEndDate(date);
                    updateLicenceDtos.add(interimLicDto);
                    interimLicDto = hcsaLicenceClient.getLicDtoById(interimLicDto.getOriginLicenceId()).getEntity();
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                continue;
            }
        }
        try {
            hcsaLicenceClient.updateLicences(updateLicenceDtos).getEntity();
            callEicUpdateFeLicDto(updateLicenceDtos);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    private void updateLicenceStatusApproved(List<LicenceDto> licenceDtos) {
        AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
        for (LicenceDto licenceDto : licenceDtos) {
            licenceDto.setAuditTrailDto(auditTrailDto);
            String licId = licenceDto.getId();
            String svcName = licenceDto.getSvcName();
            String licenceNo = licenceDto.getLicenceNo();
            try {
                Map<String, Object> emailMap = IaisCommonUtils.genNewHashMap();
                String appId= licCommService.getLicCorrBylicId(licId).get(0).getApplicationId();
                SubLicenseeDto subLicensee = appCommClient.getSubLicenseeDtoByAppId(appId).getEntity();
                ApplicationDto applicationDto=applicationClient.getApplicationById(appId).getEntity();
                AppPremisesCorrelationDto appPremisesCorrelationDto=applicationClient.getAppPremCorrByAppNo(applicationDto.getApplicationNo()).getEntity();
                List<AppSvcBusinessDto> appSvcBusinessDtoList=appCommClient.getAppSvcBusinessDtoListByCorrId(appPremisesCorrelationDto.getId()).getEntity();
                ApplicationGroupDto applicationGroupDto = applicationClient.getAppById(applicationDto.getAppGrpId()).getEntity();
                if (applicationGroupDto != null){
                    OrgUserDto orgUserDto = organizationClient.retrieveOrgUserAccountById(applicationGroupDto.getSubmitBy()).getEntity();
                    if (orgUserDto != null){
                        emailMap.put("ApplicantName", orgUserDto.getDisplayName());
                    }
                }
                emailMap.put("ServiceLicenceName", svcName);
                emailMap.put("LicenceNumber", licenceNo);
                emailMap.put("CessationDate", Formatter.formatDate(new Date()));
                emailMap.put("email", systemParamConfig.getSystemAddressOne());
                emailMap.put("MOH_AGENCY_NAM_GROUP", "<b>" + AppConsts.MOH_AGENCY_NAM_GROUP + "</b>");
                emailMap.put("MOH_AGENCY_NAME", "<b>" + AppConsts.MOH_AGENCY_NAME + "</b>");
                emailMap.put("LicenceNo", licenceNo);
                emailMap.put("BusinessName", "-");
                if(IaisCommonUtils.isNotEmpty(appSvcBusinessDtoList)){
                    emailMap.put("BusinessName", appSvcBusinessDtoList.get(0).getBusinessName());
                }
                emailMap.put("LicenseeName", subLicensee.getDisplayName());
                EmailParam emailParam = new EmailParam();
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE);
                emailParam.setTemplateContent(emailMap);
                emailParam.setQueryCode(licenceNo);
                emailParam.setReqRefNum(licenceNo);
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_LICENCE_ID);
                emailParam.setRefId(licId);
                Map<String, Object> map = IaisCommonUtils.genNewHashMap();
                InspectionEmailTemplateDto rfiEmailTemplateDto = inspEmailService.loadingEmailTemplate(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE);
                map.put("ServiceLicenceName", svcName);
                map.put("LicenceNumber", licenceNo);
                String subject = MsgUtil.getTemplateMessageByContent(rfiEmailTemplateDto.getSubject(), map);
                emailParam.setSubject(subject);
                //email
                notificationHelper.sendNotification(emailParam);
                //sms
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE_SMS);
                emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_LICENCE_ID);
                notificationHelper.sendNotification(emailParam);
                //msg
                HcsaServiceDto svcDto = hcsaConfigClient.getServiceDtoByName(svcName).getEntity();
                List<String> svcCode = IaisCommonUtils.genNewArrayList();
                svcCode.add(svcDto.getSvcCode());
                emailParam.setSvcCodeList(svcCode);
                emailParam.setTemplateId(MsgTemplateConstants.MSG_TEMPLATE_LICENCE_END_DATE_MSG);
                emailParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
                emailParam.setRefId(licenceDto.getId());
                notificationHelper.sendNotification(emailParam);

                //cessationBeService.sendEmail(LICENCEENDDATE, date, svcName, licId, licenseeId, licenceNo);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        }
        try {
            hcsaLicenceClient.updateLicences(licenceDtos).getEntity();
            callEicUpdateFeLicDto(licenceDtos);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }
}
