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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.SelfChecklistHelper;
import com.ecquaria.cloud.moh.iais.service.SelfAssessmentService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEmailClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
    private FeEmailClient feEmailClient;

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
            selfAssessmentConfigList.add(new SelfAssessmentConfig());
            for (AppPremisesSelfDeclChklDto ent : entity){
                String answerJson = ent.getAnswer();
                List<PremCheckItem> answerData = JsonUtil.parseToList(answerJson, PremCheckItem.class);
                String checklistConfigId = ent.getChkLstConfId();
                FeignResponseEntity<ChecklistConfigDto> fetchConfigResult = appConfigClient.getChecklistConfigById(checklistConfigId);
                if (HttpStatus.SC_OK == fetchConfigResult.getStatusCode()) {
                    ChecklistConfigDto checklistConfigDto = fetchConfigResult.getEntity();
                    if (!checklistConfigDto.isCommon()) {
                        String svcCode = checklistConfigDto.getSvcCode();
                        HcsaServiceDto serviceInfo = HcsaServiceCacheHelper.getServiceByCode(svcCode);
                        if (serviceInfo != null) {
                            String svcName = serviceInfo.getSvcName();
                            selfAssessment.setSvcName(svcName);
                            selfAssessment.setSvcId(serviceInfo.getId());
                            SelfAssessmentConfig selfAssessmentConfig;

                            if (selfAssessmentConfigMap.containsKey(svcCode)){
                                selfAssessmentConfig = selfAssessmentConfigMap.get(svcCode);
                            }else {
                                selfAssessmentConfig = new SelfAssessmentConfig();
                            }

                            if (!StringUtils.isEmpty(checklistConfigDto.getSvcSubType())){
                                selfAssessmentConfig.setHasSubtype(true);
                            }

                            selfAssessmentConfig.setCommon(false);
                            selfAssessmentConfig.setSvcName(svcName);

                            if (selfAssessmentConfig.getQuestion() != null){
                                selfAssessmentConfig.getQuestion().addAll(answerData);
                            }else {
                                selfAssessmentConfig.setQuestion(answerData);
                            }

                            selfAssessmentConfig.setConfigId(checklistConfigDto.getId());
                            selfAssessmentConfigList.add(selfAssessmentConfig);
                            selfAssessmentConfigMap.put(svcCode, selfAssessmentConfig);
                        }
                    }else {
                        SelfAssessmentConfig selfAssessmentConfig = new SelfAssessmentConfig();
                        selfAssessmentConfig.setCommon(true);
                        selfAssessmentConfig.setQuestion(answerData);
                        selfAssessmentConfig.setConfigId(checklistConfigDto.getId());
                        selfAssessmentConfigMap.put("common", selfAssessmentConfig);
                        selfAssessmentConfigList.set(0, selfAssessmentConfig);
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
        List<SelfAssessment> rfiData = IaisCommonUtils.genNewArrayList();
        List<AppPremisesSelfDeclChklDto> selfDeclChklDtoList = applicationClient.getAppPremisesSelfDeclChklListByGroupId(groupId).getEntity();
        TreeMap<String, List<AppPremisesSelfDeclChklDto>> premisesSelfChecklistMap = classifiyToEachAddress(selfDeclChklDtoList);

        for (Map.Entry<String, List<AppPremisesSelfDeclChklDto>> entry : premisesSelfChecklistMap.entrySet()){
            String corrId = entry.getKey();
            SelfAssessment selfAssessment = new SelfAssessment();
            FeignResponseEntity<AppGrpPremisesDto> fetchPremisesResult = applicationClient.getAppGrpPremisesByCorrId(corrId);
            if (HttpStatus.SC_OK == fetchPremisesResult.getStatusCode()){
                AppGrpPremisesDto appGrpPremises = fetchPremisesResult.getEntity();
                String address = MiscUtil.getAddress(appGrpPremises.getBlkNo(), appGrpPremises.getStreetName(),
                        appGrpPremises.getBuildingName(), appGrpPremises.getFloorNo(), appGrpPremises.getUnitNo(), appGrpPremises.getPostalCode());
                selfAssessment.setPremises(address);
            }

            selfAssessment.setCanEdit(true);
            selfAssessment.setCorrId(corrId);
            List<SelfAssessmentConfig> selfAssessmentConfigList = IaisCommonUtils.genNewArrayList();
            Map<String, SelfAssessmentConfig> selfAssessmentConfigMap = classifyToConfig(selfAssessment, entry);
            for (Map.Entry<String,SelfAssessmentConfig> entry1 : selfAssessmentConfigMap.entrySet()){
                selfAssessmentConfigList.add(entry1.getValue());
            }

            selfAssessment.setSelfAssessmentConfig(selfAssessmentConfigList);
            rfiData.add(selfAssessment);
        }

        return rfiData;
    }

    private Map<String, SelfAssessmentConfig> classifyToConfig(SelfAssessment selfAssessment, Map.Entry<String, List<AppPremisesSelfDeclChklDto>> entry){
        Map<String, SelfAssessmentConfig> selfAssessmentConfigMap = IaisCommonUtils.genNewHashMap();
        List<AppPremisesSelfDeclChklDto> premisesChecklist = entry.getValue();
        for (AppPremisesSelfDeclChklDto selfDeclChklDto : premisesChecklist){
            String answerJson = selfDeclChklDto.getAnswer();
            String checklistConfigId = selfDeclChklDto.getChkLstConfId();
            List<PremCheckItem> answerData = JsonUtil.parseToList(answerJson, PremCheckItem.class);

            FeignResponseEntity<ChecklistConfigDto> fetchConfigResult = appConfigClient.getChecklistConfigById(checklistConfigId);
            if (HttpStatus.SC_OK == fetchConfigResult.getStatusCode()){
                ChecklistConfigDto checklistConfigDto = fetchConfigResult.getEntity();
                if (!checklistConfigDto.isCommon()){
                    String svcCode = checklistConfigDto.getSvcCode();
                    HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByCode(svcCode);
                    if (hcsaServiceDto != null){
                        String svcName = hcsaServiceDto.getSvcName();
                        selfAssessment.setSvcName(svcName);
                        selfAssessment.setSvcId(hcsaServiceDto.getId());
                        SelfAssessmentConfig selfAssessmentConfig;
                        if (selfAssessmentConfigMap.containsKey(svcCode)){
                            selfAssessmentConfig = selfAssessmentConfigMap.get(svcCode);

                        }else {
                            selfAssessmentConfig = new SelfAssessmentConfig();
                        }
                        if (!StringUtils.isEmpty(checklistConfigDto.getSvcSubType())){
                            selfAssessmentConfig.setHasSubtype(true);
                        }

                        selfAssessmentConfig.setCommon(false);
                        selfAssessmentConfig.setSvcName(svcName);

                        if (selfAssessmentConfig.getQuestion() != null){
                            selfAssessmentConfig.getQuestion().addAll(answerData);
                        }else {
                            selfAssessmentConfig.setQuestion(answerData);
                        }

                        selfAssessmentConfig.setConfigId(checklistConfigDto.getId());

                        selfAssessmentConfigMap.put(svcCode, selfAssessmentConfig);
                    }
                }else {
                    SelfAssessmentConfig selfAssessmentConfig = new SelfAssessmentConfig();
                    selfAssessmentConfig.setCommon(true);
                    selfAssessmentConfig.setQuestion(answerData);
                    selfAssessmentConfig.setConfigId(checklistConfigDto.getId());
                    selfAssessmentConfigMap.put("common", selfAssessmentConfig);
                }


            }
        }

        return selfAssessmentConfigMap;
    }


    private TreeMap<String, List<AppPremisesSelfDeclChklDto>> classifiyToEachAddress(List<AppPremisesSelfDeclChklDto> answerData){
        TreeMap<String, List<AppPremisesSelfDeclChklDto>> retData = new TreeMap<>();
        for (AppPremisesSelfDeclChklDto s : answerData){
            String corrId = s.getAppPremCorreId();
            if (retData.containsKey(corrId)){
                List<AppPremisesSelfDeclChklDto> data = retData.get(corrId);
                data.add(s);
                retData.put(corrId, data);
            }else {
                List<AppPremisesSelfDeclChklDto> data = IaisCommonUtils.genNewArrayList();
                data.add(s);
                retData.put(corrId, data);
            }
        }



        return retData;
    }

    @Override
    public Boolean hasSubmittedSelfAssessment(String groupId) {
        return applicationClient.hasSubmittedSelfAssessment(groupId).getEntity();
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
                //selfDeclSubmitDto.setLastVersionIds(lastVersionIds);
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
    public void changePendingSelfAssMtStatus(String groupId) {
        ApplicationGroupDto groupDto = new ApplicationGroupDto();
        groupDto.setId(groupId);
        groupDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        groupDto.setPendingSubmitSelfAssMt(ApplicationConsts.SUBMITTED_SELF_ASSESSMENT);
        applicationClient.doUpDate(groupDto);
    }
}
