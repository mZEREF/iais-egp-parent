package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.config.SystemParamConfig;
import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.systemadmin.MsgTemplateConstants;
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
import com.ecquaria.cloud.moh.iais.common.utils.Formatter;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HmacConstants;
import com.ecquaria.cloud.moh.iais.dto.EmailParam;
import com.ecquaria.cloud.moh.iais.helper.FeSelfChecklistHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.NotificationHelper;
import com.ecquaria.cloud.moh.iais.service.AppSubmissionService;
import com.ecquaria.cloud.moh.iais.service.SelfAssessmentService;
import com.ecquaria.cloud.moh.iais.service.client.ConfigCommClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
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
    private ApplicationFeClient applicationFeClient;

    @Autowired
    private ConfigCommClient configCommClient;

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

    @Value("${iais.email.sender}")
    private String mailSender;

    @Override
    public List<SelfAssessment> receiveSelfAssessmentByGroupId(String groupId) {
        log.info("SelfAssessmentServiceImpl [receiveSelfAssessmentByGroupId] START GroupID... {}", groupId);
        List<SelfAssessment> selfAssessmentList = IaisCommonUtils.genNewArrayList();
        // (S) Group , (M) application
        List<ApplicationDto> appList = applicationFeClient.listApplicationByGroupId(groupId).getEntity();
        if (IaisCommonUtils.isEmpty(appList)) {
            return selfAssessmentList;
        }

        //uat bug 108659
        appList = appList.stream()
                .filter(i -> !ApplicationConsts.APPLICATION_STATUS_WITHDRAWN.equals(i.getStatus()))
                .filter(i -> !ApplicationConsts.APPLICATION_STATUS_RECALLED.equals(i.getStatus())).collect(Collectors.toList());


        //common data
        ChecklistConfigDto common = configCommClient.getMaxVersionCommonConfig().getEntity();
        LinkedHashMap<String, List<PremCheckItem>> sqMap  = FeSelfChecklistHelper.loadPremisesQuestion(common, false);
        SelfAssessmentConfig commonConfig = new SelfAssessmentConfig();
        commonConfig.setConfigId(common.getId());
        commonConfig.setCommon(true);
        commonConfig.setSqMap(sqMap);

        for(ApplicationDto app : appList){
            String appId = app.getId();
            String svcId = app.getServiceId();
            String appNo = app.getApplicationNo();
            log.info("SelfAssessmentServiceImpl [receiveSelfAssessmentByGroupId]  appId... {} , service id ...{} app number  ...{} ", appId, svcId, appNo);
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(svcId);
            String svcCode = hcsaServiceDto.getSvcCode();
            String svcName = hcsaServiceDto.getSvcName();
            String type = MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.SELF_ASSESSMENT);
            String module = MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.NEW);
            ChecklistConfigDto serviceConfig = configCommClient.getMaxVersionConfigByParams(svcCode, type, module).getEntity();
            List<AppPremisesCorrelationDto> correlationList = applicationFeClient.listAppPremisesCorrelation(appId).getEntity();
            for (AppPremisesCorrelationDto correlation : correlationList) {
                String corrId = correlation.getId();
                String appGrpPremId = correlation.getAppGrpPremId();
                AppGrpPremisesEntityDto appGrpPremises = applicationFeClient.getAppGrpPremise(appGrpPremId).getEntity();
                String address = MiscUtil.getAddressForApp(appGrpPremises.getBlkNo(), appGrpPremises.getStreetName(), appGrpPremises.getBuildingName(),
                        appGrpPremises.getFloorNo(), appGrpPremises.getUnitNo(), appGrpPremises.getPostalCode(),appGrpPremises.getAppPremisesOperationalUnitDtos());

                SelfAssessment selfAssessment = new SelfAssessment();
                selfAssessment.setSvcId(svcId);
                selfAssessment.setCorrId(corrId);
                selfAssessment.setApplicationNumber(appNo);
                selfAssessment.setCanEdit(true);
                selfAssessment.setSvcName(svcName);
                selfAssessment.setPremises(address);

                List<SelfAssessmentConfig> configList = IaisCommonUtils.genNewArrayList();
                configList.add(commonConfig);
                // check list
                if (serviceConfig != null) {
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
                        ChecklistConfigDto subTypeConfig = configCommClient.getMaxVersionConfigByParams(svcCode, type, module, subTypeName).getEntity();
                        if (Optional.ofNullable(subTypeConfig).isPresent()){
                            svcConfig.setHasSubtype(true);
                            Map<String, List<PremCheckItem>> subTypeQuestion = FeSelfChecklistHelper.loadPremisesQuestion(subTypeConfig, true);
                            serviceQuestion.putAll(subTypeQuestion);
                        }
                    }

                    svcConfig.setSqMap(serviceQuestion);
                    configList.add(svcConfig);
                }
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
        List<AppSvcPremisesScopeDto> scopeList = applicationFeClient.getAppSvcPremisesScopeListByCorreId(correlationId).getEntity();
        for (AppSvcPremisesScopeDto premise : scopeList){
            boolean isSubService = premise.isSubsumedType();
            if (!isSubService){
                String subTypeId = premise.getScopeName();
                HcsaServiceSubTypeDto subType = configCommClient.getHcsaServiceSubTypeById(subTypeId).getEntity();
                String subTypeName = subType.getSubtypeName();
                serviceSubtypeName.add(subTypeName);
            }
        }
        return serviceSubtypeName;
    }

    @Override
    public List<SelfAssessment> receiveSubmittedSelfAssessmentDataByGroupId(String groupId) {
        List<SelfAssessment> viewData = IaisCommonUtils.genNewArrayList();
        List<ApplicationDto> appList = applicationFeClient.listApplicationByGroupId(groupId).getEntity();
        if (IaisCommonUtils.isEmpty(appList)) {
            return viewData;
        }

        //uat bug 108659
        appList = appList.stream().filter(i -> !ApplicationConsts.APPLICATION_STATUS_WITHDRAWN.equals(i.getStatus())
        || !ApplicationConsts.APPLICATION_STATUS_RECALLED.equals(i.getStatus())).collect(Collectors.toList());

        for(ApplicationDto app : appList){
           String appId = app.getId();
            List<AppPremisesCorrelationDto>  correlationList = applicationFeClient.listAppPremisesCorrelation(appId).getEntity();
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
        String emailRandomStr_003 = IaisEGPHelper.generateRandomString(26);
        for (String s : correlationIds){
            ApplicationDto applicationDto = applicationFeClient.getApplicationByCorrId(s).getEntity();
            if (Optional.ofNullable(applicationDto).isPresent()){
                    String appNo = applicationDto.getApplicationNo();
                    String originAppType = applicationDto.getApplicationType();
                    String appType = MasterCodeUtil.getCodeDesc(originAppType);
                    emailGroupId_003 = applicationDto.getAppGrpId();
                    tlGroupNumber = appNo.substring(0, appNo.length() - 3);
                    List<AppGrpPremisesDto> appGrpPremisesList = appSubmissionService.getAppGrpPremisesDto(appNo);

                    if (Optional.ofNullable(appGrpPremisesList).isPresent()){
                        AppGrpPremisesDto appGrpPremises = appGrpPremisesList.get(0);
                        if (Optional.ofNullable(appGrpPremises).isPresent()){
                            String address = MiscUtil.getAddressForApp(appGrpPremises.getBlkNo(), appGrpPremises.getStreetName(), appGrpPremises.getBuildingName(),
                                    appGrpPremises.getFloorNo(), appGrpPremises.getUnitNo(), appGrpPremises.getPostalCode(),appGrpPremises.getAppPremisesOperationalUnitDtos());
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
                    HashMap<String, Object > reqParams = IaisCommonUtils.genNewHashMap();
                    reqParams.put("appPremId",  s);
                    reqParams.put("recomType", InspectionConstants.RECOM_TYPE_INSEPCTION_DATE);
                    try {
                        AppPremisesRecommendationDto appPremisesRecommendation=
                                gatewayClient.getBeAppPremisesRecommendationByIdAndType(reqParams).getEntity();
                        if (Optional.ofNullable(appPremisesRecommendation).isPresent()){
                            Date inspectionDate = appPremisesRecommendation.getRecomInDate();

                            log.info(StringUtil.changeForLog("===>>>>>>>>inspectionDate>>>>>>> " + inspectionDate));
                            templateContent.put("inspectionDate", Formatter.formatDate(inspectionDate));
                        }

                        if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(originAppType)){
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

                    templateContent.put("MOH_GROUP_NAME", AppConsts.MOH_AGENCY_NAM_GROUP);
                    templateContent.put("MOH_AGENCY_NAME", AppConsts.MOH_AGENCY_NAME);

                    EmailParam email_004 = new EmailParam();
                    email_004.setTemplateId(email_004_template);
                    email_004.setTemplateContent(templateContent);
                    email_004.setQueryCode(HcsaChecklistConstants.SELF_ASS_MT_EMAIL_TO_CURRENT_INSPECTOR);
                    email_004.setReqRefNum(emailRandomStr_003);
                    email_004.setRefIdType(NotificationHelper.RECEIPT_TYPE_APP);
                    email_004.setRefId(appNo);
                    email_004.setModuleType(NotificationHelper.OFFICER_MODULE_TYPE_INSPECTOR_BY_CURRENT_TASK);
                    email_004.setSmsOnlyOfficerHour(false);
                    notificationHelper.sendNotification(email_004);
            }
        }

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
        subjectMap.put("{appType}", tlAppType);
        subjectMap.put("{applicationNo}", tlGroupNumber);
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
        data.setAuditTrail(IaisEGPHelper.getCurrentAuditTrailDto());
        IaisApiResult apiResult = gatewayClient.callEicWithTrack(data, gatewayClient::routeSelfAssessment,
                "routeSelfAssessment").getEntity();
        if (apiResult == null) {
            return false;
        }

        boolean hasError = apiResult.isHasError();
        if (hasError) {
            log.info(StringUtil.changeForLog("=========>>>>>>" + apiResult.getMessage()));
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void saveAllSelfAssessment(List<SelfAssessment> selfAssessmentList) {
        //TODO if from inbox , should not create task
        FeignResponseEntity<List<AppPremisesSelfDeclChklDto>> result =  applicationFeClient.saveAllSelfAssessment(selfAssessmentList);
        if (result.getStatusCode() == HttpStatus.SC_OK){
            FeSelfAssessmentSyncDataDto syncDataDto = new FeSelfAssessmentSyncDataDto();
            syncDataDto.setAppNoList(selfAssessmentList.stream().map(SelfAssessment::getApplicationNumber).collect(Collectors.toList()));
            syncDataDto.setFeSyncData(result.getEntity());
            syncDataDto.setAuditTrail(IaisEGPHelper.getCurrentAuditTrailDto());

            callFeEicAppPremisesSelfDeclChkl(syncDataDto);
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
        FeignResponseEntity<Integer> result = applicationFeClient.getApplicationSelfAssMtStatusByGroupId(groupId);
        if (HttpStatus.SC_OK == result.getStatusCode()){
            int status = result.getEntity().intValue();
            if (ApplicationConsts.SUBMITTED_SELF_ASSESSMENT.equals(status)){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    public Boolean hasSubmittedSelfAssMtRfiByCorrId(String corrId) {
        ApplicationDto applicationDto = applicationFeClient.getApplicationByCorreId(corrId).getEntity();
        if (applicationDto == null){
            log.info(StringUtil.changeForLog("applicationDto is null" + applicationDto));
            return Boolean.TRUE;
        }

        //if prohibit , should't submit and that  cannot access this module
        return applicationDto.getSelfAssMtFlag().equals(ApplicationConsts.SUBMITTED_SELF_ASSESSMENT)
                || applicationDto.getSelfAssMtFlag().equals(ApplicationConsts.PROHIBIT_SUBMIT_RFI_SELF_ASSESSMENT) ? Boolean.TRUE : Boolean.FALSE;
    }

    @Override
    public void changePendingSelfAssMtStatus(String value, Boolean isGroupId) {
        log.info("SelfAssessmentServiceImpl [changePendingSelfAssMtStatus] value {}, isGroupId {}", value, isGroupId);
        List<ApplicationDto> appList;
        if (isGroupId){
            appList = applicationFeClient.listApplicationByGroupId(value).getEntity();
        }else {
            appList = applicationFeClient.getPremisesApplicationsByCorreId(value).getEntity();
        }

        if (IaisCommonUtils.isEmpty(appList)) {
            return;
        }

        for(ApplicationDto app : appList){
            //change app status when rfi
            if (ApplicationConsts.APPLICATION_STATUS_PENDING_CLARIFICATION.equals(app.getStatus())) {
                app.setStatus(ApplicationConsts.APPLICATION_STATUS_PENDING_INSPECTION_READINESS);
            }
            app.setSelfAssMtFlag(ApplicationConsts.SUBMITTED_SELF_ASSESSMENT);

            log.info("SelfAssessmentServiceImpl [changePendingSelfAssMtStatus] need update application application number {}", app.getApplicationNo());
            log.info("SelfAssessmentServiceImpl [changePendingSelfAssMtStatus] self ass status {}", app.getStatus());
        }

        applicationFeClient.updateApplicationList(appList);
    }

    @Override
    public AppPremisesCorrelationDto getCorrelationByAppNo(String appNo) {
        return applicationFeClient.getCorrelationByAppNo(appNo).getEntity();
    }
}
