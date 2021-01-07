package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.message.MessageConstants;
import com.ecquaria.cloud.moh.iais.common.constant.role.RoleConsts;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionRequestInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationTruckDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.WithdrawApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.recall.RecallApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcRoutingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspRectificationSaveDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.templates.MsgTemplateDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NewApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.WithdrawalService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.AppEicClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigFeClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceFeMsgTemplateClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationLienceseeClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.sz.commons.util.MsgUtil;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class WithdrawalServiceImpl implements WithdrawalService {


    @Autowired
    private SystemAdminClient systemAdminClient;

    @Autowired
    private LicenceFeMsgTemplateClient licenceFeMsgTemplateClient;


    @Autowired
    private ApplicationFeClient applicationFeClient;

    @Autowired
    private CessationClient cessationClient;

    @Autowired
    private OrganizationLienceseeClient organizationLienceseeClient;

    @Autowired
    private AppConfigClient appConfigClient;

    @Autowired
    private HcsaConfigFeClient hcsaConfigFeClient;

    @Autowired
    private FeEicGatewayClient feEicGatewayClient;

    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;

    @Autowired
    private Environment env;

    @Autowired
    private AppEicClient appEicClient;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    private SystemParamConfig systemParamConfig;

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

    @Value("${iais.system.one.address}")
    private String systemAddressOne;

    @Override
    public void saveWithdrawn(List<WithdrawnDto> withdrawnDtoList, HttpServletRequest httpServletRequest) {
        boolean charity = NewApplicationHelper.isCharity(httpServletRequest);
        List<WithdrawnDto> autoApproveApplicationDtoList = IaisCommonUtils.genNewArrayList();
        withdrawnDtoList.forEach(h -> {
            String grpNo = systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL).getEntity();
            String licenseeId = h.getLicenseeId();
            AppSubmissionDto appSubmissionDto = applicationFeClient.gainSubmissionDto(h.getApplicationNo()).getEntity();
            transform(appSubmissionDto,licenseeId);
            appSubmissionDto.setAppGrpNo(grpNo);
            log.info(StringUtil.changeForLog(JsonUtil.parseToJson(appSubmissionDto.getAppSvcRelatedInfoDtoList())+"-----appSubmissionDto-------"));
            AppSubmissionDto newAppSubmissionDto = applicationFeClient.saveSubmision(appSubmissionDto).getEntity();
            List<ApplicationDto> applicationDtoList = newAppSubmissionDto.getApplicationDtos();
            for (ApplicationDto applicationDto:applicationDtoList
                 ) {
                List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = newAppSubmissionDto.getAppSvcRelatedInfoDtoList();
                if (appSvcRelatedInfoDtoList != null && appSvcRelatedInfoDtoList.size() >0){
                    String serviceId = appSvcRelatedInfoDtoList.get(0).getServiceId();
                    log.info(StringUtil.changeForLog(JsonUtil.parseToJson(appSvcRelatedInfoDtoList)+"-----appSvcRelatedInfoDtoList"));
                    log.info(StringUtil.changeForLog(serviceId +"-------serviceId"));
                    String appStatus = getAppStatus(serviceId,ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL);
                    if (!StringUtil.isEmpty(appStatus)){
                        applicationDto.setStatus(appStatus);
                    }
                }
            }
            applicationFeClient.updateApplicationList(applicationDtoList);
            h.setNewApplicationId(applicationDtoList.get(0).getId());
            applicationFeClient.saveApps(newAppSubmissionDto).getEntity();
            /**
             *
             */
            String newApplicationId = h.getNewApplicationId();
            String oldApplicationId = h.getApplicationId();
            RecallApplicationDto recallApplicationDto = new RecallApplicationDto();
            List<String> refNoList = IaisCommonUtils.genNewArrayList();
            List<AppPremisesCorrelationDto> appPremisesCorrelationDtoList = applicationFeClient.listAppPremisesCorrelation(oldApplicationId).getEntity();
            for (AppPremisesCorrelationDto appPremisesCorrelationDto:appPremisesCorrelationDtoList) {
                refNoList.add(appPremisesCorrelationDto.getId());
            }
            ApplicationDto newApplication = applicationFeClient.getApplicationById(newApplicationId).getEntity();
            ApplicationDto oldApplication = applicationFeClient.getApplicationById(oldApplicationId).getEntity();
            ApplicationGroupDto oldApplicationGroupDtox = applicationFeClient.getApplicationGroup(oldApplication.getAppGrpId()).getEntity();
            recallApplicationDto.setAppId(oldApplicationId);
            recallApplicationDto.setRefNo(refNoList);
            recallApplicationDto.setAppNo(newApplication.getApplicationNo());
            recallApplicationDto.setAppGrpId(oldApplication.getAppGrpId());

            recallApplicationDto.setNewAppId(h.getNewApplicationId());
            if ( !charity&&!StringUtil.isEmpty(oldApplicationGroupDtox.getPayMethod())
                    && !ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(oldApplication.getApplicationType())){
                recallApplicationDto.setNeedReturnFee(true);
            }else{
                recallApplicationDto.setNeedReturnFee(false);
            }
            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
            try {
                EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.WithdrawalServiceImpl", "saveWithdrawn",
                        "hcsa-license-web", InspRectificationSaveDto.class.getName(), JsonUtil.parseToJson(recallApplicationDto));
                String eicRefNo = eicRequestTrackingDto.getRefNo();
                recallApplicationDto = feEicGatewayClient.withdrawAppChangeTask(recallApplicationDto, signature.date(), signature.authorization(),
                        signature2.date(), signature2.authorization()).getEntity();
                //get eic record
                eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
                //update eic record status
                eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
                eicRequestTrackingDtos.add(eicRequestTrackingDto);
                appEicClient.updateStatus(eicRequestTrackingDtos);

            }catch (Exception e){
                log.error(e.getMessage(),e);
            }
            if (recallApplicationDto.getResult()){
                ApplicationTruckDto applicationTruckDto = new ApplicationTruckDto();
                List<ApplicationDto> updateWithdrawApp = IaisCommonUtils.genNewArrayList();
                oldApplication.setStatus(ApplicationConsts.APPLICATION_STATUS_WITHDRAWN);
                newApplication.setStatus(ApplicationConsts.APPLICATION_STATUS_LICENCE_GENERATED);
                updateWithdrawApp.add(oldApplication);
                updateWithdrawApp.add(newApplication);
                applicationFeClient.updateApplicationList(updateWithdrawApp);
                applicationTruckDto.setApplicationDtoList(updateWithdrawApp);
                autoApproveApplicationDtoList.add(h);
                /**
                 *  SAVE FE APP DATA TO BE
                 */
                List<ApplicationGroupDto> applicationGroupDtoList = IaisCommonUtils.genNewArrayList();
                String newApplicationGroupId = newApplication.getAppGrpId();
                String oldApplicationGroupId = oldApplication.getAppGrpId();
                ApplicationGroupDto newApplicationGroupDto = applicationFeClient.getApplicationGroup(newApplicationGroupId).getEntity();
                ApplicationGroupDto oldApplicationGroupDto = applicationFeClient.getApplicationGroup(oldApplicationGroupId).getEntity();
                applicationGroupDtoList.add(newApplicationGroupDto);
                applicationGroupDtoList.add(oldApplicationGroupDto);
                applicationTruckDto.setApplicationGroupDtoList(applicationGroupDtoList);

                List<AppGrpPremisesEntityDto> appGrpPremisesEntityDtoList = IaisCommonUtils.genNewArrayList();
                AppGrpPremisesEntityDto oldAppGrpPremisesEntityDto = applicationFeClient.getPremisesByAppNo(oldApplication.getApplicationNo()).getEntity();
                AppGrpPremisesEntityDto newAppGrpPremisesEntityDto = applicationFeClient.getPremisesByAppNo(newApplication.getApplicationNo()).getEntity();
                appGrpPremisesEntityDtoList.add(newAppGrpPremisesEntityDto);
                appGrpPremisesEntityDtoList.add(oldAppGrpPremisesEntityDto);
                applicationTruckDto.setAppGrpPremisesEntityDtoList(appGrpPremisesEntityDtoList);

                List<AppPremisesCorrelationDto> appPremisesCorrelationDtoList1 = IaisCommonUtils.genNewArrayList();
                AppPremisesCorrelationDto oldAppPremisesCorrelationDto = applicationFeClient.getCorrelationByAppNo(oldApplication.getApplicationNo()).getEntity();
                AppPremisesCorrelationDto newAppPremisesCorrelationDto = applicationFeClient.getCorrelationByAppNo(newApplication.getApplicationNo()).getEntity();
                appPremisesCorrelationDtoList1.add(oldAppPremisesCorrelationDto);
                appPremisesCorrelationDtoList1.add(newAppPremisesCorrelationDto);
                applicationTruckDto.setAppPremisesCorrelationDtoList(appPremisesCorrelationDtoList1);

                try {
                    EicRequestTrackingDto eicRequestTrackingDto = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT, "com.ecquaria.cloud.moh.iais.service.impl.WithdrawalServiceImpl", "saveWithdrawn",
                            "hcsa-license-web", InspRectificationSaveDto.class.getName(), JsonUtil.parseToJson(applicationTruckDto));
                    String eicRefNo = eicRequestTrackingDto.getRefNo();
                    feEicGatewayClient.saveApplicationDtosForFe(applicationTruckDto, signature.date(), signature.authorization(),
                            signature2.date(), signature2.authorization()).getEntity();
                    //get eic record
                    eicRequestTrackingDto = appEicClient.getPendingRecordByReferenceNumber(eicRefNo).getEntity();
                    //update eic record status
                    eicRequestTrackingDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
                    eicRequestTrackingDto.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                    List<EicRequestTrackingDto> eicRequestTrackingDtos = IaisCommonUtils.genNewArrayList();
                    eicRequestTrackingDtos.add(eicRequestTrackingDto);
                    appEicClient.updateStatus(eicRequestTrackingDtos);
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        });
        List<String> withdrawnList = cessationClient.saveWithdrawn(withdrawnDtoList).getEntity();
        if (!IaisCommonUtils.isEmpty(withdrawnList)){
            withdrawnDtoList.forEach(h -> {
                ApplicationDto oldApplication = applicationFeClient.getApplicationById(h.getApplicationId()).getEntity();
                boolean isRfc = false;
                if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(oldApplication.getApplicationType())){
                    isRfc = true;
                }
                sendNMS(h,isRfc);
            });
        }
        autoApproveApplicationDtoList.forEach(h -> {
            boolean isRfc = false;
            ApplicationDto oldApplication = applicationFeClient.getApplicationById(h.getApplicationId()).getEntity();
            if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(oldApplication.getApplicationType())){
                isRfc = true;
            }
            sendWithdrawApproveNMS(h,isRfc);
        });
    }

    private void sendWithdrawApproveNMS(WithdrawnDto withdrawnDto,boolean isRfc){
        Double fee = 0.0;
        String applicantName = "";
        List<ApplicationDto> applicationDtoList = IaisCommonUtils.genNewArrayList();
        AppSubmissionDto appSubmissionDto = applicationFeClient.gainSubmissionDto(withdrawnDto.getApplicationNo()).getEntity();
        ApplicationDto oldApplicationDto = applicationFeClient.getApplicationById(withdrawnDto.getApplicationId()).getEntity();
        ApplicationDto newApplicationDto = applicationFeClient.getApplicationById(withdrawnDto.getNewApplicationId()).getEntity();
        ApplicationGroupDto applicationGroupDto =  applicationFeClient.getApplicationGroup(oldApplicationDto.getAppGrpId()).getEntity();
        String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getInterServerName() + MessageConstants.MESSAGE_INBOX_URL_INTER_LOGIN;
        OrgUserDto orgUserDto = organizationLienceseeClient.retrieveOneOrgUserAccount(applicationGroupDto.getSubmitBy()).getEntity();
        if (appSubmissionDto != null){
            String serviceId = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceId();
            String serviceName = HcsaServiceCacheHelper.getServiceById(serviceId).getSvcName();
            String payMethod = applicationGroupDto.getPayMethod();
            if (orgUserDto != null){
                applicantName = orgUserDto.getDisplayName();
            }
            applicationDtoList.add(oldApplicationDto);
            for(ApplicationDto applicationDto1 : applicationDtoList){
                applicationDto1.setStatus(ApplicationConsts.APPLICATION_STATUS_REJECTED);
            }
            List<ApplicationDto> applicationDtoList2 = hcsaConfigFeClient.returnFee(applicationDtoList).getEntity();
            if (!IaisCommonUtils.isEmpty(applicationDtoList2)){
                fee = applicationDtoList2.get(0).getReturnFee();
            }
            Map<String, Object> msgInfoMap = IaisCommonUtils.genNewHashMap();
            msgInfoMap.put("Applicant", applicantName);
            msgInfoMap.put("ApplicationType", MasterCodeUtil.getCodeDesc(oldApplicationDto.getApplicationType()));
            msgInfoMap.put("ApplicationNumber", oldApplicationDto.getApplicationNo());
            msgInfoMap.put("reqAppNo", oldApplicationDto.getApplicationNo());
            msgInfoMap.put("S_LName",serviceName);
            msgInfoMap.put("MOH_AGENCY_NAME",AppConsts.MOH_AGENCY_NAME);
            msgInfoMap.put("ApplicationDate",Formatter.formatDate(applicationGroupDto.getSubmitDt()));
            if (isRfc || StringUtil.isEmpty(payMethod)){
                msgInfoMap.put("paymentType","2");
                msgInfoMap.put("paymentMode","");
                msgInfoMap.put("returnMount",0.0);
            }else{
                msgInfoMap.put("returnMount", fee);
                if (ApplicationConsts.PAYMENT_METHOD_NAME_GIRO.equals(payMethod)){
                    msgInfoMap.put("paymentType", "0");
                    msgInfoMap.put("paymentMode", MasterCodeUtil.getCodeDesc(ApplicationConsts.PAYMENT_METHOD_NAME_GIRO));
                }else{
                    msgInfoMap.put("paymentType", "1");
                    msgInfoMap.put("paymentMode", MasterCodeUtil.getCodeDesc(payMethod));
                }
            }
            msgInfoMap.put("adminFee", ApplicationConsts.PAYMRNT_ADMIN_FEE);
            msgInfoMap.put("systemLink", loginUrl);
            msgInfoMap.put("emailAddress", systemAddressOne);
            try {
                sendInboxMessage(oldApplicationDto, serviceId, msgInfoMap, MsgTemplateConstants.MSG_TEMPLATE_WITHDRAWAL_APP_APPROVE_MESSAGE);
                EmailParam emailParamSms = sendSms(msgInfoMap, oldApplicationDto, MsgTemplateConstants.MSG_TEMPLATE_WITHDRAWAL_APP_APPROVE_SMS);
                notificationHelper.sendNotification(emailParamSms);
                EmailParam emailParam = sendNotification(msgInfoMap, oldApplicationDto, MsgTemplateConstants.MSG_TEMPLATE_WITHDRAWAL_APP_APPROVE_EMAIL);
                notificationHelper.sendNotification(emailParam);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

        }
    }

    private void sendNMS(WithdrawnDto withdrawnDto,boolean isRfc){
        List<ApplicationDto> applicationDtoList = IaisCommonUtils.genNewArrayList();
        AppSubmissionDto appSubmissionDto = applicationFeClient.gainSubmissionDto(withdrawnDto.getApplicationNo()).getEntity();
        if (appSubmissionDto != null){
            ApplicationDto applicationDto = applicationFeClient.getApplicationById(withdrawnDto.getApplicationId()).getEntity();
            ApplicationGroupDto applicationGroupDto =  applicationFeClient.getApplicationGroup(applicationDto.getAppGrpId()).getEntity();
            String serviceId = appSubmissionDto.getAppSvcRelatedInfoDtoList().get(0).getServiceId();
            String applicantName = "";
            Double fee = 0.0;
            if (!StringUtil.isEmpty(serviceId)){
                applicationDtoList.add(applicationDto);
                Map<String, Object> msgInfoMap = IaisCommonUtils.genNewHashMap();
                msgInfoMap.put("ApplicationNumber", withdrawnDto.getApplicationNo());
                msgInfoMap.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
                OrgUserDto orgUserDto = organizationLienceseeClient.retrieveOneOrgUserAccount(applicationGroupDto.getSubmitBy()).getEntity();
                for(ApplicationDto applicationDto1 : applicationDtoList){
                    applicationDto1.setStatus(ApplicationConsts.APPLICATION_STATUS_REJECTED);
                }
                List<ApplicationDto> applicationDtoList2 = hcsaConfigFeClient.returnFee(applicationDtoList).getEntity();
                if (!IaisCommonUtils.isEmpty(applicationDtoList2)){
                    fee = applicationDtoList2.get(0).getReturnFee();
                }
                String payMethod = applicationGroupDto.getPayMethod();
                if (orgUserDto != null){
                    applicantName = orgUserDto.getDisplayName();
                }
                msgInfoMap.put("Applicant", applicantName);
                if (!isRfc && !StringUtil.isEmpty(payMethod)){
                    msgInfoMap.put("paymentType","0");
                    msgInfoMap.put("returnMount",fee);
                }
                else{
                    msgInfoMap.put("paymentType","1");
                    msgInfoMap.put("returnMount",fee);
                }
                msgInfoMap.put("MOH_AGENCY_NAME",AppConsts.MOH_AGENCY_NAME);
                msgInfoMap.put("emailAddress",systemAddressOne);
                msgInfoMap.put("ApplicationDate",Formatter.formatDateTime(new Date(),"dd/MM/yyyy"));
                try {
                    EmailParam emailParam = sendNotification(msgInfoMap,applicationDto,MsgTemplateConstants.TEMPLATE_WITHDRAWAL_EMAIL);
                    notificationHelper.sendNotification(emailParam);
                    EmailParam emailParamSms = sendSms(msgInfoMap,applicationDto,MsgTemplateConstants.TEMPLATE_WITHDRAWAL_SMS);
                    notificationHelper.sendNotification(emailParamSms);
                    sendInboxMessage(applicationDto,serviceId,msgInfoMap,MsgTemplateConstants.TEMPLATE_WITHDRAWAL_MESSAGE);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }

    @Override
    public void saveRfiWithdrawn(List<WithdrawnDto> withdrawnDtoList,HttpServletRequest httpRequest) {
        withdrawnDtoList.forEach(h -> {
            String appId = h.getNewApplicationId();
            ApplicationDto applicationDto = applicationFeClient.getApplicationById(appId).getEntity();
            String originLicenceId = applicationDto.getOriginLicenceId();
            List<String> licIds = IaisCommonUtils.genNewArrayList();
            licIds.add(originLicenceId);
            AppSubmissionDto appSubmissionDto = applicationFeClient.getAppSubmissionDtoByAppNo(applicationDto.getApplicationNo()).getEntity();
            try {
                appSubmissionDto = transformRfi(appSubmissionDto, h.getLicenseeId(), applicationDto);
                List<ApplicationDto> applicationDtos = appSubmissionDto.getApplicationDtos();
                if (applicationDtos != null && applicationDtos.size() > 0){
                    ApplicationDto applicationDto1 = applicationDtos.get(0);
                    h.setNewApplicationId(applicationDto1.getId());
                }
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        });
        List<String> withdrawnList = cessationClient.saveWithdrawn(withdrawnDtoList).getEntity();
    }

    private AppSubmissionDto transformRfi(AppSubmissionDto appSubmissionDto, String licenseeId, ApplicationDto applicationDto) throws Exception {
        AppSubmissionRequestInformationDto appSubmissionRequestInformationDto = new AppSubmissionRequestInformationDto();
        AppSubmissionDto oldAppSubmissionDto = (AppSubmissionDto)CopyUtil.copyMutableObject(appSubmissionDto);
        appSubmissionRequestInformationDto.setOldAppSubmissionDto(oldAppSubmissionDto);
        Double amount = 0.0;
        AuditTrailDto internet = AuditTrailHelper.getCurrentAuditTrailDto();
        appSubmissionDto.setAppGrpId(applicationDto.getAppGrpId());
        ApplicationGroupDto entity1 = applicationFeClient.getApplicationGroup(applicationDto.getAppGrpId()).getEntity();
        appSubmissionDto.setFromBe(false);
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL);
        appSubmissionDto.setAppGrpNo(entity1.getGroupNo());
        appSubmissionDto.setAmount(amount);
        appSubmissionDto.setAuditTrailDto(internet);
        appSubmissionDto.setPreInspection(true);
        appSubmissionDto.setRequirement(true);
        appSubmissionDto.setLicenseeId(licenseeId);
        appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
        appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
        setRiskToDto(appSubmissionDto);
        List<AppPremisesRoutingHistoryDto> hisList;
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        String gatewayUrl = env.getProperty("iais.inter.gateway.url");
        Map<String, Object> params = IaisCommonUtils.genNewHashMap(1);
        params.put("appNo", applicationDto.getApplicationNo());
        hisList = IaisEGPHelper.callEicGatewayWithParamForList(gatewayUrl + "/v1/app-routing-history", HttpMethod.GET, params,
                MediaType.APPLICATION_JSON, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization(), AppPremisesRoutingHistoryDto.class).getEntity();
        if(hisList!=null){
            for(AppPremisesRoutingHistoryDto appPremisesRoutingHistoryDto : hisList){
                if(ApplicationConsts.PROCESSING_DECISION_REQUEST_FOR_INFORMATION.equals(appPremisesRoutingHistoryDto.getProcessDecision())
                        || InspectionConstants.PROCESS_DECI_REQUEST_FOR_INFORMATION.equals(appPremisesRoutingHistoryDto.getProcessDecision())){
                    if(ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING.equals(appPremisesRoutingHistoryDto.getAppStatus())){
                        applicationDto.setStatus(ApplicationConsts.PENDING_ASO_REPLY);
                    }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING.equals(appPremisesRoutingHistoryDto.getAppStatus())){
                        applicationDto.setStatus(ApplicationConsts.PENDING_PSO_REPLY);
                    }else if(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS.equals(appPremisesRoutingHistoryDto.getAppStatus())){
                        applicationDto.setStatus(ApplicationConsts.PENDING_INP_REPLY);
                    }
                }
            }
        }
        appSubmissionRequestInformationDto.setRfiStatus(applicationDto.getStatus());
        appSubmissionRequestInformationDto.setAppSubmissionDto(appSubmissionDto);
        return applicationFeClient.saveRfcWithdrawSubmission(appSubmissionRequestInformationDto).getEntity();
    }

    @Override
    public WithdrawnDto getWithdrawAppInfo(String appNo) {
        return applicationFeClient.getWithdrawAppInfo(appNo).getEntity();
    }

    @Override
    public List<WithdrawApplicationDto> getCanWithdrawAppList(List<String[]> appTandS,String licenseeId) {
        List<WithdrawApplicationDto> withdrawApplicationDtoList = applicationFeClient.getApplicationByAppTypesAndStatus(appTandS,licenseeId).getEntity();
        return withdrawApplicationDtoList;
    }

    private EmailParam sendSms(Map<String, Object> msgInfoMap, ApplicationDto applicationDto,String templateId) throws IOException, TemplateException {
        log.info(StringUtil.changeForLog("***************** send withdraw application sms  *****************"));
        EmailParam emailParam = new EmailParam();
        MsgTemplateDto msgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(templateId).getEntity();
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        map.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
        map.put("ApplicationNumber", applicationDto.getApplicationNo());
        String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(),map);
        emailParam.setTemplateId(templateId);
        emailParam.setTemplateContent(msgInfoMap);
        emailParam.setQueryCode(applicationDto.getApplicationNo());
        emailParam.setReqRefNum(applicationDto.getApplicationNo());
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_SMS_APP);
        emailParam.setRefId(applicationDto.getApplicationNo());
        emailParam.setSubject(subject);
        return emailParam;
    }

    private void sendInboxMessage(ApplicationDto applicationDto,String serviceId,Map<String, Object> map,String templateId) throws IOException, TemplateException {
        log.info(StringUtil.changeForLog("***************** send withdraw application message  *****************"));
        EmailParam messageParam = new EmailParam();
        MsgTemplateDto msgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(templateId).getEntity();
        Map<String, Object> subMap = IaisCommonUtils.genNewHashMap();
        subMap.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
        subMap.put("ApplicationNumber", applicationDto.getApplicationNo());
        String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(),subMap);
        HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceById(serviceId);
        List<String> svcCodeList = IaisCommonUtils.genNewArrayList();
        if(serviceDto != null){
            svcCodeList.add(serviceDto.getSvcCode());
        }
        messageParam.setTemplateId(templateId);
        messageParam.setTemplateContent(map);
        messageParam.setQueryCode(applicationDto.getApplicationNo());
        messageParam.setReqRefNum(applicationDto.getApplicationNo());
        messageParam.setRefIdType(NotificationHelper.MESSAGE_TYPE_NOTIFICATION);
        messageParam.setRefId(applicationDto.getApplicationNo());
        messageParam.setSubject(subject);
        messageParam.setSvcCodeList(svcCodeList);
        notificationHelper.sendNotification(messageParam);
    }

    private EmailParam sendNotification(Map<String, Object> msgInfoMap, ApplicationDto applicationDto,String templateId) throws IOException, TemplateException {
        log.info(StringUtil.changeForLog("***************** send withdraw application Notification  *****************"));
        MsgTemplateDto msgTemplateDto = licenceFeMsgTemplateClient.getMsgTemplate(templateId).getEntity();
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        map.put("ApplicationType", MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType()));
        map.put("ApplicationNumber", applicationDto.getApplicationNo());
        String subject = MsgUtil.getTemplateMessageByContent(msgTemplateDto.getTemplateName(),map);
        EmailParam emailParam = new EmailParam();
        emailParam.setTemplateId(templateId);
        emailParam.setTemplateContent(msgInfoMap);
        emailParam.setQueryCode(applicationDto.getApplicationNo());
        emailParam.setReqRefNum(applicationDto.getApplicationNo());
        emailParam.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
        emailParam.setRefId(applicationDto.getApplicationNo());
        emailParam.setSubject(subject);
        log.info(StringUtil.changeForLog("***************** send withdraw application Notification  end*****************"));
        return emailParam;
    }

    private void transform(AppSubmissionDto appSubmissionDto,String licenseeId){
        AuditTrailDto auditTrailDto = AuditTrailHelper.getCurrentAuditTrailDto();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        String serviceId = appSvcRelatedInfoDtoList.get(0).getServiceId();
        HcsaServiceDto hsd = HcsaServiceCacheHelper.getServiceById(serviceId);

        if(hsd!=null){
            log.info(StringUtil.changeForLog(JsonUtil.parseToJson(hsd)+"hcsaServiceDto1"));
            String svcCode = hsd.getSvcCode();
            appSvcRelatedInfoDtoList.get(0).setServiceId(hsd.getId());
            appSvcRelatedInfoDtoList.get(0).setServiceCode(svcCode);
        }

        appSubmissionDto.setAppGrpId(null);
        appSubmissionDto.setFromBe(false);
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL);
        appSubmissionDto.setAmount(0.0);
        appSubmissionDto.setAuditTrailDto(auditTrailDto);
        appSubmissionDto.setPreInspection(true);
        appSubmissionDto.setRequirement(true);
        appSubmissionDto.setLicenseeId(licenseeId);
        appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
        appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
        setRiskToDto(appSubmissionDto);
    }

    private void setRiskToDto(AppSubmissionDto appSubmissionDto) {
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<RiskAcceptiionDto> riskAcceptiionDtoList = IaisCommonUtils.genNewArrayList();
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
            riskAcceptiionDto.setApptype(appSubmissionDto.getAppType());
            riskAcceptiionDto.setScvCode(appSvcRelatedInfoDto.getServiceCode());
            riskAcceptiionDtoList.add(riskAcceptiionDto);
        }
        List<RiskResultDto> riskResultDtoList = appConfigClient.getRiskResult(riskAcceptiionDtoList).getEntity();
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            String serviceCode = appSvcRelatedInfoDto.getServiceCode();
            RiskResultDto riskResultDto = getRiskResultDtoByServiceCode(riskResultDtoList, serviceCode);
            if (riskResultDto != null) {
                appSvcRelatedInfoDto.setDoRiskDate(riskResultDto.getDoRiskDate());
                appSvcRelatedInfoDto.setScore(riskResultDto.getScore());
            }
        }
    }

    private RiskResultDto getRiskResultDtoByServiceCode(List<RiskResultDto> riskResultDtoList, String serviceCode) {
        RiskResultDto result = null;
        if (riskResultDtoList == null || StringUtil.isEmpty(serviceCode)) {
            return result;
        }
        for (RiskResultDto riskResultDto : riskResultDtoList) {
            if (serviceCode.equals(riskResultDto.getSvcCode())) {
                result = riskResultDto;
            }
        }
        return result;
    }

    private String getAppStatus(String serviceId,String appType) {
        String appStatus;
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        List<HcsaSvcRoutingStageDto> serviceConfig = feEicGatewayClient.getServiceConfig(serviceId, appType, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();
        if (IaisCommonUtils.isEmpty(serviceConfig)) {
            return null;
        } else {
            String stageCode = serviceConfig.get(0).getStageCode();
            switch (stageCode) {
                case RoleConsts.USER_ROLE_ASO:
                    appStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_ADMIN_SCREENING;
                    break;
                case RoleConsts.USER_ROLE_INSPECTIOR:
                    appStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION;
                    break;
                case RoleConsts.USER_ROLE_PSO:
                    appStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_PROFESSIONAL_SCREENING;
                    break;
                case RoleConsts.USER_ROLE_AO1:
                    appStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL01;
                    break;
                case RoleConsts.USER_ROLE_AO2:
                    appStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL02;
                    break;
                default:
                    appStatus = ApplicationConsts.APPLICATION_STATUS_PENDING_APPROVAL03;
            }
        }
        return appStatus;
    }
}