package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.FeSelfAssessmentSyncDataDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessment;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessmentConfig;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesSelfDeclChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.EicClientConstant;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.EicRequestTrackingHelper;
import com.ecquaria.cloud.moh.iais.helper.FeSelfChecklistHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.SelfAssessmentService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author: yichen
 * @Description:
 * @Date:2020/5/6
 **/

@Service
@Slf4j
public class SelfAssessmentServiceImpl implements SelfAssessmentService {
    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private AppConfigClient appConfigClient;

    @Autowired
    private FeEicGatewayClient gatewayClient;

    @Autowired
    private NotificationHelper notificationHelper;

    @Autowired
    private AppSubmissionService appSubmissionService;

    @Autowired
    private SystemParamConfig systemParamConfig;

    @Autowired
    private LicenceClient licenceClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Value("${spring.application.name}")
    private String currentApp;
    @Value("${iais.current.domain}")
    private String currentDomain;
    @Value("${iais.email.sender}")
    private String mailSender;

    @Autowired
    private EicRequestTrackingHelper eicRequestTrackingHelper;

    @Override
    public List<SelfAssessment> receiveSelfAssessmentByGroupId(String groupId) {
        List<SelfAssessment> selfAssessmentList = IaisCommonUtils.genNewArrayList();

        // (S) Group , (M) application
        List<ApplicationDto> appList = applicationClient.listApplicationByGroupId(groupId).getEntity();
        if (IaisCommonUtils.isEmpty(appList)) {
            return selfAssessmentList;
        }

        //common data
        ChecklistConfigDto common = appConfigClient.getMaxVersionCommonConfig().getEntity();
        LinkedHashMap<String, List<PremCheckItem>> sqMap  = FeSelfChecklistHelper.loadPremisesQuestion(common, false);

        SelfAssessmentConfig commonConfig = new SelfAssessmentConfig();
        commonConfig.setConfigId(common.getId());
        commonConfig.setCommon(true);
        commonConfig.setSqMap(sqMap);

        for(ApplicationDto app : appList){
            String appId = app.getId();
            String svcId = app.getServiceId();
            String appNo = app.getApplicationNo();
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(svcId);
            String svcCode = hcsaServiceDto.getSvcCode();
            String svcName = hcsaServiceDto.getSvcName();
            String type = MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.SELF_ASSESSMENT);
            String module = MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.NEW);

            ChecklistConfigDto serviceConfig = appConfigClient.getMaxVersionConfigByParams(svcCode, type, module).getEntity();

            if (serviceConfig == null){
                //if don't have checklist , cannot do next step.
                continue;
            }

            List<AppPremisesCorrelationDto>  correlationList = applicationClient.listAppPremisesCorrelation(appId).getEntity();
            for (AppPremisesCorrelationDto correlationDto : correlationList) {
                String corrId = correlationDto.getId();
                String appGrpPremId = correlationDto.getAppGrpPremId();
                AppGrpPremisesEntityDto appGrpPremises = applicationClient.getAppGrpPremise(appGrpPremId).getEntity();
                String address = MiscUtil.getAddress(appGrpPremises.getBlkNo(), appGrpPremises.getStreetName(),
                        appGrpPremises.getBuildingName(), appGrpPremises.getFloorNo(), appGrpPremises.getUnitNo(), appGrpPremises.getPostalCode());

                SelfAssessment selfAssessment = new SelfAssessment();
                selfAssessment.setSvcId(svcId);
                selfAssessment.setCorrId(corrId);
                selfAssessment.setApplicationNumber(appNo);
                selfAssessment.setCanEdit(true);
                selfAssessment.setSvcName(svcName);
                selfAssessment.setPremises(address);

                List<SelfAssessmentConfig> configList = IaisCommonUtils.genNewArrayList();
                configList.add(commonConfig);

                SelfAssessmentConfig svcConfig = new SelfAssessmentConfig();
                svcConfig.setSvcId(svcId);
                svcConfig.setSvcName(svcName);
                svcConfig.setConfigId(serviceConfig.getId());
                svcConfig.setCommon(false);
                svcConfig.setSvcCode(serviceConfig.getSvcCode());
                svcConfig.setSvcName(serviceConfig.getSvcName());

                LinkedHashMap<String, List<PremCheckItem>> serviceQuestion = FeSelfChecklistHelper.loadPremisesQuestion(serviceConfig, false);
                List<String> serviceSubtypeName = getServiceSubTypeName(corrId);
                for(String subTypeName : serviceSubtypeName){
                    ChecklistConfigDto subTypeConfig = appConfigClient.getMaxVersionConfigByParams(svcCode, type, module, subTypeName).getEntity();
                    if (subTypeConfig != null){
                        svcConfig.setHasSubtype(true);
                        Map<String, List<PremCheckItem>> subTypeQuestion = FeSelfChecklistHelper.loadPremisesQuestion(subTypeConfig, true);
                        serviceQuestion.putAll(subTypeQuestion);
                    }
                }

                svcConfig.setSqMap(serviceQuestion);
                configList.add(svcConfig);
                selfAssessment.setSelfAssessmentConfig(configList);
                selfAssessmentList.add(selfAssessment);
            }
        }

        return selfAssessmentList;
    }

    @Override
    public List<SelfAssessment> receiveSelfAssessmentRfiByCorrId(String corrId) {
        return FeSelfChecklistHelper.receiveSelfAssessmentDataByCorrId(corrId);
    }

    private List<String> getServiceSubTypeName(String correlationId){
        List<String> serviceSubtypeName = IaisCommonUtils.genNewArrayList();
        List<AppSvcPremisesScopeDto> scopeList = applicationClient.getAppSvcPremisesScopeListByCorreId(correlationId).getEntity();
        for (AppSvcPremisesScopeDto premise : scopeList){
            boolean isSubService = premise.isSubsumedType();
            if (!isSubService){
                String subTypeId = premise.getScopeName();
                HcsaServiceSubTypeDto subType = appConfigClient.getHcsaServiceSubTypeById(subTypeId).getEntity();
                String subTypeName = subType.getSubtypeName();
                serviceSubtypeName.add(subTypeName);
            }
        }
        return serviceSubtypeName;
    }

    @Override
    public List<SelfAssessment> receiveSubmittedSelfAssessmentDataByGroupId(String groupId) {
        List<SelfAssessment> viewData = IaisCommonUtils.genNewArrayList();
        List<ApplicationDto> appList = applicationClient.listApplicationByGroupId(groupId).getEntity();
        if (IaisCommonUtils.isEmpty(appList)) {
            return viewData;
        }

        for(ApplicationDto app : appList){
           String appId = app.getId();
            List<AppPremisesCorrelationDto>  correlationList = applicationClient.listAppPremisesCorrelation(appId).getEntity();
            for (AppPremisesCorrelationDto correlationDto : correlationList) {
                String corrId = correlationDto.getId();
                List<SelfAssessment> dataByCorrId = receiveSelfAssessmentRfiByCorrId(corrId);
                dataByCorrId.forEach(i -> viewData.add(i));
            }
        }


        return viewData;
    }


    /**
     * EN-CHM-003 EN-INS-004
     * @param correlationIds
     */
    private void sendEmailToInspector(List<String> correlationIds){
        String email_003_template = MsgTemplateConstants.MSG_TEMPLATE_SELF_ASS_MT_EMAIL_INSPECTOR_EMAIL_CHM;
        String email_004_template = MsgTemplateConstants.MSG_TEMPLATE_SELF_ASS_MT_EMAIL_INSPECTOR;

        List<String> svcNameList = IaisCommonUtils.genNewArrayList();
        Map<String, Object> emailContent_003 = IaisCommonUtils.genNewHashMap();
        String tlGroupNumber = "-";
        String tlAppType = "-";
        Date tlDate = null;
        String emailGroupId_003 = null;
        String loginUrl = HmacConstants.HTTPS +"://" + systemParamConfig.getIntraServerName() + "/main-web/";
        Map<String, Object> templateContent = IaisCommonUtils.genNewHashMap();
        for (String s : correlationIds){
            ApplicationDto applicationDto = applicationClient.getApplicationByCorrId(s).getEntity();
            if (Optional.ofNullable(applicationDto).isPresent()){
                    String appNo = applicationDto.getApplicationNo();
                    String appType = MasterCodeUtil.getCodeDesc(applicationDto.getApplicationType());
                    emailGroupId_003 = applicationDto.getAppGrpId();
                    tlGroupNumber = appNo.substring(0, appNo.length() - 3);
                    List<AppGrpPremisesDto> appGrpPremisesList = appSubmissionService.getAppGrpPremisesDto(appNo);

                    if (Optional.ofNullable(appGrpPremisesList).isPresent()){
                        AppGrpPremisesDto appGrpPremises = appGrpPremisesList.get(0);
                        if (Optional.ofNullable(appGrpPremises).isPresent()){
                            String address = IaisEGPHelper.getAddress(appGrpPremises.getBlkNo(), appGrpPremises.getStreetName(), appGrpPremises.getBuildingName(),
                                    appGrpPremises.getFloorNo(), appGrpPremises.getUnitNo(), appGrpPremises.getPostalCode());
                            templateContent.put("hciName", appGrpPremises.getHciName());
                            templateContent.put("hciCode", appGrpPremises.getHciCode());
                            templateContent.put("hciAddress", address);
                        }
                    }

                    String svcId = applicationDto.getServiceId();

                    Date appDate = applicationDto.getCreateAt();

                    tlAppType = appType;
                    tlDate = appDate;

                    templateContent.put("applicationNo", appNo);
                    templateContent.put("applicationType", appType);
                    templateContent.put("applicationDate", Formatter.formatDate(appDate));
                    templateContent.put("officer_name", "");

                    // get inspection from eic
                    HashMap<String, Object > reqParams = new HashMap<String, Object>(){{
                        put("appPremId",  s);
                        put("recomType", InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
                    }};

                    try {
                        AppPremisesRecommendationDto appPremisesRecommendation=
                                gatewayClient.getBeAppPremisesRecommendationByIdAndType(reqParams).getEntity();
                        if (Optional.ofNullable(appPremisesRecommendation).isPresent()){
                            Date inspectionDate = appPremisesRecommendation.getRecomInDate();

                            log.info(StringUtil.changeForLog("===>>>>>>>>inspectionDate>>>>>>> " + inspectionDate));
                            templateContent.put("inspectionDate", Formatter.formatDate(inspectionDate));
                        }

                        if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
                            String originLicId = applicationDto.getOriginLicenceId();
                            LicenceDto licenceDto = licenceClient.getLicBylicId(originLicId).getEntity();

                            log.info(StringUtil.changeForLog("===>>>>>>>>inspectionDate>>>>>>> " + licenceDto.getEndDate()));

                            Optional.ofNullable(licenceDto).ifPresent(i -> {
                                templateContent.put("licenceDueDate", Formatter.formatDate( i.getEndDate()));
                            });
                        }
                    }catch (Exception e){
                        // this try catch will remove after test case
                        log.error(e.getMessage(), e);
                    }


                    HcsaServiceDto serviceDto = HcsaServiceCacheHelper.getServiceById(svcId);
                    Optional.ofNullable(serviceDto).ifPresent(i -> {
                        String svcName = serviceDto.getSvcName();
                        templateContent.put("serviceName", svcName);
                        svcNameList.add(svcName);
                    });

                    String randomStr = IaisEGPHelper.generateRandomString(26);
                    templateContent.put("MOH_GROUP_NAME", AppConsts.MOH_AGENCY_NAM_GROUP);
                    templateContent.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);

                    EmailParam email_004 = new EmailParam();
                    email_004.setTemplateId(email_004_template);
                    email_004.setTemplateContent(templateContent);
                    email_004.setQueryCode(HcsaChecklistConstants.SELF_ASS_MT_EMAIL_TO_CURRENT_INSPECTOR);
                    email_004.setReqRefNum(randomStr);
                    email_004.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                    email_004.setRefId(appNo);
                    email_004.setModuleType(NotificationHelper.OFFICER_MODULE_TYPE_INSPECTOR_BY_CURRENT_TASK);
                    email_004.setSmsOnlyOfficerHour(false);
                    notificationHelper.sendNotification(email_004);
            }
        }


        String emailRandomStr_003 = IaisEGPHelper.generateRandomString(26);
        emailContent_003.put("applicationNo", tlGroupNumber);
        emailContent_003.put("applicationType", tlAppType);
        emailContent_003.put("applicationDate", Formatter.formatDate(tlDate));
        emailContent_003.put("serviceNames", svcNameList);
        emailContent_003.put("systemLink", loginUrl);
        emailContent_003.put("officer_name", "");
        emailContent_003.put("MOH_GROUP_NAME", AppConsts.MOH_AGENCY_NAM_GROUP);
        emailContent_003.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);

        EmailParam email_003 = new EmailParam();
        HashMap<String, String> subjectMap = IaisCommonUtils.genNewHashMap();
        subjectMap.put("[appType]", tlAppType);
        subjectMap.put("[applicationNo]", tlGroupNumber);
        email_003.setSubjectParams(subjectMap);

        email_003.setTemplateId(email_003_template);
        email_003.setTemplateContent(emailContent_003);
        email_003.setQueryCode(HcsaChecklistConstants.SELF_ASS_MT_EMAIL_TO_CURRENT_INSPECTOR);
        email_003.setReqRefNum(emailRandomStr_003);
        email_003.setRefId(emailGroupId_003);
        email_003.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP_GRP);
        email_003.setModuleType(NotificationHelper.OFFICER_MODULE_TYPE_INSPECTOR_BY_CURRENT_TASK);
        email_003.setSmsOnlyOfficerHour(false);
        notificationHelper.sendNotification(email_003);
    }


    public boolean callFeEicAppPremisesSelfDeclChkl(FeSelfAssessmentSyncDataDto data) {
        //route to be
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);

        IaisApiResult apiResult = gatewayClient.routeSelfAssessment(data, signature.date(), signature.authorization(),
                signature2.date(), signature2.authorization()).getEntity();

        if (apiResult == null){
            return false;
        }

        boolean hasError = apiResult.isHasError();
        if (hasError){
            log.info(StringUtil.changeForLog("=========>>>>>>" + apiResult.getMessage()));
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void saveAllSelfAssessment(List<SelfAssessment> selfAssessmentList) {
       //TODO if from inbox , should not create task
        if (!IaisCommonUtils.isEmpty(selfAssessmentList)){
            //set audit trail
            selfAssessmentList.get(0).setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        }

        FeignResponseEntity<List<AppPremisesSelfDeclChklDto>> result =  applicationClient.saveAllSelfAssessment(selfAssessmentList);
        if (result.getStatusCode() == HttpStatus.SC_OK){
            FeSelfAssessmentSyncDataDto syncDataDto = new FeSelfAssessmentSyncDataDto();
            syncDataDto.setAppNoList(selfAssessmentList.stream().map(SelfAssessment::getApplicationNumber).collect(Collectors.toList()));
            syncDataDto.setFeSyncData(result.getEntity());
            EicRequestTrackingDto postSaveTrack = eicRequestTrackingHelper.clientSaveEicRequestTracking(EicClientConstant.APPLICATION_CLIENT,
                    SelfAssessmentServiceImpl.class.getName(),
                    "callFeEicAppPremisesSelfDeclChkl", currentApp + "-" + currentDomain,
                    FeSelfAssessmentSyncDataDto.class.getName(), JsonUtil.parseToJson(syncDataDto));

            try {
                FeignResponseEntity<EicRequestTrackingDto> fetchResult =  eicRequestTrackingHelper.getAppEicClient().getPendingRecordByReferenceNumber(postSaveTrack.getRefNo());
                if (HttpStatus.SC_OK == fetchResult.getStatusCode()){
                    EicRequestTrackingDto preEicRequest = fetchResult.getEntity();
                    if (AppConsts.EIC_STATUS_PENDING_PROCESSING.equals(preEicRequest.getStatus())){
                        boolean success = callFeEicAppPremisesSelfDeclChkl(syncDataDto);
                        if (success){
                            preEicRequest.setProcessNum(1);
                            Date now = new Date();
                            preEicRequest.setFirstActionAt(now);
                            preEicRequest.setLastActionAt(now);
                            preEicRequest.setStatus(AppConsts.EIC_STATUS_PROCESSING_COMPLETE);
                            eicRequestTrackingHelper.getAppEicClient().saveEicTrack(preEicRequest);
                        }
                    }
                }
            }catch (Exception e){
                log.error(StringUtil.changeForLog("encounter failure when sync self assessment to be"), e);
            }
        }

        try {
            List<String> correlationIds = selfAssessmentList.stream().map(SelfAssessment::getCorrId).collect(Collectors.toList());
            sendEmailToInspector(correlationIds);
        }catch (Exception e){
            log.error(StringUtil.changeForLog("encounter failure when self decl send notification"), e);
        }
    }

    @Override
    public Boolean hasSubmittedSelfAssMtByGroupId(String groupId) {
        FeignResponseEntity<Integer> result = applicationClient.getApplicationSelfAssMtStatusByGroupId(groupId);
        if (HttpStatus.SC_OK == result.getStatusCode()){
            int status = result.getEntity().intValue();
            if (ApplicationConsts.SUBMITTED_SELF_ASSESSMENT == status){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean hasSubmittedSelfAssMtRfiByCorrId(String corrId) {
        ApplicationDto applicationDto = applicationClient.getApplicationByCorreId(corrId).getEntity();
        if (applicationDto == null){
            log.info(StringUtil.changeForLog("applicationDto is null" + applicationDto));
            return Boolean.TRUE;
        }

        return applicationDto.getSelfAssMtFlag() == 1;
    }

    @Override
    public void changePendingSelfAssMtStatus(String value, Boolean isGroupId) {
        log.info(StringUtil.changeForLog("changePendingSelfAssMtStatus =====>>" + value + "isGroupId" + isGroupId));
        List<ApplicationDto> appList;
        if (isGroupId){
            appList = applicationClient.listApplicationByGroupId(value).getEntity();
        }else {
            appList = applicationClient.getPremisesApplicationsByCorreId(value).getEntity();
        }

        if (IaisCommonUtils.isEmpty(appList)) {
            return;
        }

        log.info(StringUtil.changeForLog("=====>>>>>>>>need update application size" + appList.size()));
        for(ApplicationDto app : appList){
            //change app status when rfi
            if (ApplicationConsts.APPLICATION_STATUS_PENDING_CLARIFICATION.equals(app.getStatus())) {
                app.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS);
            }
            app.setSelfAssMtFlag(ApplicationConsts.SUBMITTED_SELF_ASSESSMENT);
        }

        applicationClient.updateApplicationList(appList);
    }

    @Override
    public AppPremisesCorrelationDto getCorrelationByAppNo(String appNo) {
        return applicationClient.getCorrelationByAppNo(appNo).getEntity();
    }
}
