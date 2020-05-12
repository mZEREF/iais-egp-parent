package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.FeSelfAssessmentSyncDataDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessment;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessmentConfig;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesSelfDeclChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.SelfChecklistHelper;
import com.ecquaria.cloud.moh.iais.service.SelfAssessmentService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;
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

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

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
        List<PremCheckItem> commonQuestion = SelfChecklistHelper.loadPremisesQuestion(common, false);

        SelfAssessmentConfig commonConfig = new SelfAssessmentConfig();
        commonConfig.setConfigId(common.getId());
        commonConfig.setCommon(true);
        commonConfig.setQuestion(commonQuestion);

        for(ApplicationDto app : appList){
            String appId = app.getId();
            String svcId = app.getServiceId();
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(svcId);
            String svcCode = hcsaServiceDto.getSvcCode();
            String svcName = hcsaServiceDto.getSvcName();
            String type = MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.SELF_ASSESSMENT);
            String module = MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.NEW);

            ChecklistConfigDto serviceConfig = appConfigClient.getMaxVersionConfigByParams(svcCode, type, module).getEntity();

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

                List<PremCheckItem> serviceQuestion = SelfChecklistHelper.loadPremisesQuestion(serviceConfig, false);
                List<String> serviceSubtypeName = getServiceSubTypeName(corrId);
                for(String subTypeName : serviceSubtypeName){
                    ChecklistConfigDto subTypeConfig = appConfigClient.getMaxVersionConfigByParams(svcCode, type, module, subTypeName).getEntity();
                    if (subTypeConfig != null){
                        svcConfig.setHasSubtype(true);
                        List<PremCheckItem> subTypeQuestion = SelfChecklistHelper.loadPremisesQuestion(subTypeConfig, true);
                        serviceQuestion.addAll(subTypeQuestion);
                    }
                }

                svcConfig.setQuestion(serviceQuestion);
                configList.add(svcConfig);
                selfAssessment.setSelfAssessmentConfig(configList);
                selfAssessmentList.add(selfAssessment);
            }
        }

        return selfAssessmentList;
    }


    @Override
    public List<SelfAssessment> receiveSelfAssessmentRfiByCorrId(String corrId) {
        List<SelfAssessment> rfiData = IaisCommonUtils.genNewArrayList();
        Map<String, SelfAssessmentConfig> selfAssessmentConfigMap = IaisCommonUtils.genNewHashMap();

        FeignResponseEntity<List<AppPremisesSelfDeclChklDto>> result = applicationClient.getAppPremisesSelfDeclByCorrelationId(corrId);
        if(HttpStatus.SC_OK == result.getStatusCode()){
            List<AppPremisesSelfDeclChklDto> entity = result.getEntity();
            SelfAssessment selfAssessment = new SelfAssessment();
            List<SelfAssessmentConfig> selfAssessmentConfigList = IaisCommonUtils.genNewArrayList();

            FeignResponseEntity<AppGrpPremisesDto> fetchPremisesResult = applicationClient.getAppGrpPremisesByCorrId(corrId);
            if (HttpStatus.SC_OK == fetchPremisesResult.getStatusCode()){
                AppGrpPremisesDto appGrpPremises = fetchPremisesResult.getEntity();
                String address = MiscUtil.getAddress(appGrpPremises.getBlkNo(), appGrpPremises.getStreetName(),
                        appGrpPremises.getBuildingName(), appGrpPremises.getFloorNo(), appGrpPremises.getUnitNo(), appGrpPremises.getPostalCode());
                selfAssessment.setPremises(address);
            }

            selfAssessmentConfigList.add(null);
            for (AppPremisesSelfDeclChklDto ent : entity){
                String answerJson = ent.getAnswer();
                List<PremCheckItem> answerData = JsonUtil.parseToList(answerJson, PremCheckItem.class);

                //set unique key
                answerData.forEach(i -> i.setAnswerKey(UUID.randomUUID().toString()));

                String checklistConfigId = ent.getChkLstConfId();
                FeignResponseEntity<ChecklistConfigDto> fetchConfigResult = appConfigClient.getChecklistConfigById(checklistConfigId);
                if (HttpStatus.SC_OK == fetchConfigResult.getStatusCode()) {
                    ChecklistConfigDto checklistConfigDto = fetchConfigResult.getEntity();

                    boolean isCommonChecklistConfig = checklistConfigDto.isCommon();
                    if (isCommonChecklistConfig) {
                        SelfAssessmentConfig selfAssessmentConfig = new SelfAssessmentConfig();
                        selfAssessmentConfig.setCommon(true);
                        selfAssessmentConfig.setQuestion(answerData);
                        selfAssessmentConfig.setConfigId(checklistConfigDto.getId());
                        selfAssessmentConfig.setVersion(ent.getVersion());

                        selfAssessmentConfigMap.put("common", selfAssessmentConfig);
                        //the first config always common checklist config
                        selfAssessmentConfigList.set(0, selfAssessmentConfig);
                    }else {
                        String svcCode = checklistConfigDto.getSvcCode();
                        HcsaServiceDto serviceInfo = HcsaServiceCacheHelper.getServiceByCode(svcCode);
                        if (serviceInfo != null) {
                            String svcName = serviceInfo.getSvcName();
                            selfAssessment.setSvcName(svcName);
                            selfAssessment.setSvcId(serviceInfo.getId());
                            SelfAssessmentConfig selfAssessmentConfig;

                            if (selfAssessmentConfigMap.containsKey(svcCode)) {
                                selfAssessmentConfig = selfAssessmentConfigMap.get(svcCode);
                                selfAssessmentConfig.getQuestion().addAll(answerData);
                            } else {
                                selfAssessmentConfig = new SelfAssessmentConfig();
                                selfAssessmentConfig.setCommon(false);
                                selfAssessmentConfig.setSvcName(svcName);

                                if (!StringUtils.isEmpty(checklistConfigDto.getSvcSubType())) {
                                    selfAssessmentConfig.setHasSubtype(true);
                                }

                                selfAssessmentConfig.setVersion(ent.getVersion());
                                selfAssessmentConfig.setQuestion(answerData);
                                selfAssessmentConfig.setConfigId(checklistConfigDto.getId());
                                selfAssessmentConfigList.add(selfAssessmentConfig);
                                selfAssessmentConfigMap.put(svcCode, selfAssessmentConfig);
                            }
                        }
                    }
                }
            }

            selfAssessment.setSelfAssessmentConfig(selfAssessmentConfigList);
            selfAssessment.setCanEdit(true);
            selfAssessment.setCorrId(corrId);
            rfiData.add(selfAssessment);
        }

        return rfiData;
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

    @Override
    public Boolean saveAllSelfAssessment(List<SelfAssessment> selfAssessmentList) {
        boolean successSubmit = false;
        FeignResponseEntity<List<AppPremisesSelfDeclChklDto>> result =  applicationClient.saveAllSelfAssessment(selfAssessmentList);
        if (result.getStatusCode() == HttpStatus.SC_OK){
            //ToDo If the foreground is saved successfully, and the group information is updated.
            // But there is an error in the synchronization background, but the group information will be synchronized by other places. How to deal with it
            successSubmit = true;
                /* try {
                sendNotificationToInspector(syncData.stream().map(AppPremisesSelfDeclChklDto::getAppPremCorreId).collect(Collectors.toList()));
            }catch (Exception e){
                log.info(StringUtil.changeForLog("encounter failure when self decl send notification" + e.getMessage()));
            }*/

            try {
                //route to be
                HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
                HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);

                FeSelfAssessmentSyncDataDto selfDeclSyncDataDto = new FeSelfAssessmentSyncDataDto();
                selfDeclSyncDataDto.setFeSyncData(result.getEntity());

                gatewayClient.routeSelfAssessment(selfDeclSyncDataDto, signature.date(), signature.authorization(),
                        signature2.date(), signature2.authorization()).getStatusCode();
            }catch (Exception e){
                log.error(StringUtil.changeForLog("encounter failure when sync self assessment to be" + e.getMessage()));
            }
        }

        return successSubmit;
    }

    @Override
    public Boolean hasSubmittedSelfAssMtByGroupId(String groupId) {
        FeignResponseEntity<Integer> result = applicationClient.getApplicationSelfAssMtStatusByGroupId(groupId);
        if (HttpStatus.SC_OK == result.getStatusCode()){
            if (ApplicationConsts.PENDING_SUBMIT_SELF_ASSESSMENT == result.getEntity().intValue()){
                return false;
            }
        }
        return true;
    }

    @Override
    public Boolean hasSubmittedSelfAssMtRfiByCorrId(String corrId) {
        List<ApplicationDto> appList = applicationClient.getPremisesApplicationsByCorreId(corrId).getEntity();
        if (IaisCommonUtils.isEmpty(appList)) {
            return true;
        }

        boolean submitted = true;
        List<Integer> status = appList.stream()
                .map(ApplicationDto::getSelfAssMtFlag).collect(Collectors.toList());

        for (Integer i : status){
            if (ApplicationConsts.SUBMITTED_RFI_SELF_ASSESSMENT == i){
                submitted = false;
            }
        }

        return submitted;
    }

    @Override
    public void changePendingSelfAssMtStatus(String groupId) {
        changePendingSelfAssMtStatus(groupId, ApplicationConsts.SUBMITTED_SELF_ASSESSMENT);
    }

    @Override
    public void changePendingSelfAssMtStatus(String groupId, Integer selfAssMtFlag) {
        List<ApplicationDto> appList = applicationClient.listApplicationByGroupId(groupId).getEntity();
        if (IaisCommonUtils.isEmpty(appList)) {
            return;
        }

        appList.forEach(i -> i.setSelfAssMtFlag(selfAssMtFlag));

        applicationClient.updateApplicationList(appList);
    }
}
