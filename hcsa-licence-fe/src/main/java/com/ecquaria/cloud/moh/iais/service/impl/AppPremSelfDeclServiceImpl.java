package com.ecquaria.cloud.moh.iais.service.impl;


import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDecl;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDeclaration;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesSelfDeclChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.AppPremSelfDeclService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author yichen
 * @date time:11/20/2019 1:56 PM
 * @description:  applicant will be submit self-assessment after application submitted and payment over.
                Each premises will have a common config, one or more service config、subtype config if have.
 */
@Service
@Slf4j
public class AppPremSelfDeclServiceImpl implements AppPremSelfDeclService {
    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private AppConfigClient appConfigClient;

    @Autowired
    private FeEicGatewayClient gatewayClient;

    private List<AppSvcPremisesScopeDto> premScopeList;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Override
    /**
    * @author: yichen
    * @description: Selfdecl entity is Represent a tab in the web page, (Single)first common , (Multiple)then service, (Multiple)behind subtype
    * @param: [groupId]
    * @return: java.util.List<com.ecquaria.cloud.moh.iais.common.dto.application.SelfDecl>
    */
    public List<SelfDeclaration> getSelfDeclByGroupId(String groupId){
        List<SelfDeclaration> selfDeclGroupList = IaisCommonUtils.genNewArrayList();
        // (S) Group , (M) application
        List<ApplicationDto> appList = applicationClient.listApplicationByGroupId(groupId).getEntity();
        if (IaisCommonUtils.isEmpty(appList)) {
            return selfDeclGroupList;
        }

        boolean addedCoomon = false;
        for(ApplicationDto app : appList){
            String appId = app.getId();
            String svcId = app.getServiceId();
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(svcId);

            String svcCode = hcsaServiceDto.getSvcCode();
            String svcName = hcsaServiceDto.getSvcName();
            String type = MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.SELF_ASSESSMENT);
            String module = MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.NEW);

            List<AppPremisesCorrelationDto>  correlationList = applicationClient.listAppPremisesCorrelation(appId).getEntity();
            for (AppPremisesCorrelationDto correlationDto : correlationList){
                String correlationId = correlationDto.getId();
                String appGrpPremId = correlationDto.getAppGrpPremId();
                AppGrpPremisesEntityDto appGrpPremises = applicationClient.getAppGrpPremise(appGrpPremId).getEntity();
                String address = IaisCommonUtils.appendPremisesAddress(appGrpPremises.getBlkNo(), appGrpPremises.getStreetName(),
                        appGrpPremises.getBuildingName(), appGrpPremises.getFloorNo(), appGrpPremises.getUnitNo(), appGrpPremises.getPostalCode());

                //if have common
                if (!addedCoomon){
                    ChecklistConfigDto common = appConfigClient.getMaxVersionCommonConfig().getEntity();
                    if (common != null){
                        SelfDeclaration commonSelfDecl = new SelfDeclaration();
                        commonSelfDecl.setHasSubtype(false);
                        commonSelfDecl.setConfigId(common.getId());
                        LinkedHashMap<String, List<PremCheckItem>> eachPremQuestion = new LinkedHashMap<>();
                        List<PremCheckItem> premCheckItemList = getQuestionItemList(common, address);
                        commonSelfDecl.setCommon(true);
                        eachPremQuestion.put(correlationId, premCheckItemList);
                        commonSelfDecl.setEachPremQuestion(eachPremQuestion);
                        selfDeclGroupList.add(commonSelfDecl);
                        addedCoomon = true;
                    }
                }

                // get service config and question
                ChecklistConfigDto serviceConfig = appConfigClient.getMaxVersionConfigByParams(svcCode, type, module).getEntity();
                if (serviceConfig != null){
                    SelfDeclaration service = new SelfDeclaration();
                    service.setSvcId(svcId);
                    service.setSvcName(svcName);
                    service.setSvcCode(svcCode);
                    service.setConfigId(serviceConfig.getId());
                    List<PremCheckItem> premCheckItemList = getQuestionItemList(serviceConfig, address);
                    List<String> serviceSubtypeName = getServiceSubTypeName(correlationId);
                    service.setSubtypeName(serviceSubtypeName);
                    for(String subTypeName : serviceSubtypeName){
                        service.setHasSubtype(true);
                        ChecklistConfigDto subTypeConfig = appConfigClient.getMaxVersionConfigByParams(svcCode, type, module, subTypeName).getEntity();
                        if (subTypeConfig != null){
                            List<PremCheckItem> subtypeCheckItemList = getQuestionItemList(subTypeConfig, true, address);
                            subtypeCheckItemList.stream().forEach(s -> {
                                premCheckItemList.add(s);
                            });
                        }
                    }
                    LinkedHashMap<String, List<PremCheckItem>> eachPremQuestion = new LinkedHashMap<>();
                    eachPremQuestion.put(correlationId, premCheckItemList);
                    service.setCommon(false);
                    service.setEachPremQuestion(eachPremQuestion);
                    selfDeclGroupList.add(service);
                }



            }


        }

        String json = JsonUtil.parseToJson(selfDeclGroupList);
        return selfDeclGroupList;
    }

    private List<String> getServiceSubTypeName(String correlationId){
        List<String> serviceSubtypeName = IaisCommonUtils.genNewArrayList();
        List<AppSvcPremisesScopeDto> scopeList = applicationClient.getAppSvcPremisesScopeListByCorreId(correlationId).getEntity();
        for (AppSvcPremisesScopeDto premise : scopeList){
            boolean isSubSerivce = premise.isSubsumedType();
            if (!isSubSerivce){
                String subTypeId = premise.getScopeName();
                HcsaServiceSubTypeDto subType = appConfigClient.getHcsaServiceSubTypeById(subTypeId).getEntity();
                String subTypeName = subType.getSubtypeName();
                serviceSubtypeName.add(subTypeName);
            }
        }
        return serviceSubtypeName;
    }

    private  List<PremCheckItem> getQuestionItemList(ChecklistConfigDto configDto, String address){
        return getQuestionItemList(configDto, false, address);
    }

    private  List<PremCheckItem> getQuestionItemList(ChecklistConfigDto configDto, boolean isSubType, String address){
        List<PremCheckItem> premCheckItemList = IaisCommonUtils.genNewArrayList();
        List<ChecklistSectionDto> checklistSectionDtos = configDto.getSectionDtos();
        if (!IaisCommonUtils.isEmpty(checklistSectionDtos)){
            for(ChecklistSectionDto sectionDto : checklistSectionDtos){
                List<ChecklistItemDto> checklistItemDtos = sectionDto.getChecklistItemDtos();
                if (!IaisCommonUtils.isEmpty(checklistItemDtos)){
                    for (ChecklistItemDto checklistItemDto : sectionDto.getChecklistItemDtos()){
                        PremCheckItem premCheckItem = new PremCheckItem();

                        //record subtype config id
                        if (isSubType){
                            premCheckItem.setConfigId(configDto.getId());
                        }

                        premCheckItem.setSubType(isSubType);
                        premCheckItem.setRegulation(checklistItemDto.getRegulationClauseNo());
                        premCheckItem.setAnswerKey(UUID.randomUUID().toString());
                        premCheckItem.setChecklistItem(checklistItemDto.getChecklistItem());
                        premCheckItem.setChecklistItemId(checklistItemDto.getItemId());
                        premCheckItemList.add(premCheckItem);
                    }
                }
            }
        }

        return premCheckItemList;
    }




    /**
    * @author: yichen
    * @description: The configuration for common is displayed in the first location of the TAB
    * @param:
    * @return:
    */
    private void ascendOrderByCommon(List<SelfDecl> selfDeclList){
        selfDeclList.sort((s1, s2) -> {
            if (s1.isCommon() ^ s2.isCommon()) {
                return s1.isCommon() ? -1 : 1;
            } else {
                return 0;
            }
        });
    }

    @Override
    /**
    * @author: yichen
    * @description: If the transaction for FE fails, the action saved to BE is not triggered.
    * If BE fails, will be bactch-job synchronizes the data
    * @param: [selfDeclList]
    * @return: void
    */
    public void saveSelfDecl(List<SelfDeclaration> selfDeclList) {
        //save fe after return data
        List<AppPremisesSelfDeclChklDto> contentJsonList = applicationClient.saveAllSelfDecl(selfDeclList).getEntity();

        try {
            //route to be
            if (contentJsonList != null && !contentJsonList.isEmpty()){
                HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
                HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);

                int statusCode = gatewayClient.routeSelfDeclData(contentJsonList, signature.date(), signature.authorization(),
                        signature2.date(), signature2.authorization()).getStatusCode();
            }
        }catch (Exception e){
            log.error("encounter failure when sync self decl to be" + e.getMessage());
        }

    }

    @Override
    public Boolean hasSelfDeclRecord(String groupId) {
        return applicationClient.hasSelfDeclRecord(groupId).getEntity();
    }
}
