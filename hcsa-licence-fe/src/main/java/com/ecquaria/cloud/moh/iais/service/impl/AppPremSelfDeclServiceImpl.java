package com.ecquaria.cloud.moh.iais.service.impl;


import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDecl;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.AppPremSelfDeclService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.FeEicGatewayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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



    /**
    * @author: yichen
    * @description: Set service config
    * @param:
    * @return:
    */
    private void setServiceSelfDeclConfig(List<PremCheckItem> premItemList, List<String> confList,
                                          String svcCode, String type, String module, String grpPremId){
        for (AppSvcPremisesScopeDto premise : premScopeList){
            String scopeId = premise.getId();
            ChecklistConfigDto svcConfig = appConfigClient.getMaxVersionConfigByParams(svcCode, type, module).getEntity();
            if (svcConfig != null){
                setPremChecklistItem(premItemList, svcConfig.getId(), false, scopeId, grpPremId);
                confList.add(svcConfig.getId());
            }
        }
    }

    /**
    * @author: yichen
    * @description: If there is a subtype, Then get the corresponding config
    * @param:
    * @return:
    */
    private void setSubTypeSelfDeclConfig(List<PremCheckItem> premItemList, List<String> confList, String svcCode, String type, String module, String grpPremId){
        for (AppSvcPremisesScopeDto premise : premScopeList){
            if (!premise.isSubsumedType()){
                String scopeId = premise.getId();
                String subTypeId = premise.getScopeName();
                HcsaServiceSubTypeDto subType = appConfigClient.getHcsaServiceSubTypeById(subTypeId).getEntity();
                String subTypeName = subType.getSubtypeName();
                ChecklistConfigDto subTypeConfig = appConfigClient.getMaxVersionConfigByParams(svcCode, type, module, subTypeName).getEntity();
                if (subTypeConfig != null){
                    setPremChecklistItem(premItemList, subTypeConfig.getId(), false, scopeId, grpPremId);
                    confList.add(subTypeConfig.getId());
                }
            }
        }
    }

    /**
    * @author: yichen
    * @description: Set common config
    * @param:
    * @return:
    */
    private void setCommonDeclSelfConfig(List<SelfDecl> selfDeclList,List<String> premIdList, String grpPremId){
            ChecklistConfigDto commonConfig = appConfigClient.getMaxVersionCommonConfig().getEntity();
        SelfDecl commonSelfDecl = overlayCommon(premIdList, commonConfig.getId(), grpPremId);
        selfDeclList.add(commonSelfDecl);
    }

    @Override
    /**
    * @author: yichen
    * @description: Selfdecl entity is Represent a tab in the web page, (Single)first common , (Multiple)then service, (Multiple)behind subtype
    * @param: [groupId]
    * @return: java.util.List<com.ecquaria.cloud.moh.iais.common.dto.application.SelfDecl>
    */
    public List<SelfDecl> getSelfDeclByGroupId(String groupId){
        // (S) Group , (M) application
        List<ApplicationDto> appList = applicationClient.listApplicationByGroupId(groupId).getEntity();
        if (IaisCommonUtils.isEmpty(appList)) {
            return null;
        }

        List<SelfDecl> selfDeclList = IaisCommonUtils.genNewArrayList();
        List<String> premiseList = IaisCommonUtils.genNewArrayList();

        boolean commonFlag = false;
        for (ApplicationDto app : appList){
            SelfDecl selfDecl = new SelfDecl();
            List<String> confList = IaisCommonUtils.genNewArrayList();
            String appId = app.getId();
            String svcId = app.getServiceId();
            String type = MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.SELF_ASSESSMENT);
            String module = MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.NEW);

            HcsaServiceDto hcsa = HcsaServiceCacheHelper.getServiceById(svcId);
            String svcCode = hcsa.getSvcCode();
            String svcName = hcsa.getSvcName();
            List<PremCheckItem> premItemList = IaisCommonUtils.genNewArrayList();
            Map<String, List<PremCheckItem>> premiseAnswerMap = new HashMap<>(16);
            List<AppPremisesCorrelationDto>  correlationList = applicationClient.listAppPremisesCorrelation(appId).getEntity();
            for (AppPremisesCorrelationDto corre : correlationList){
                String correId = corre.getId();
                String grpPremId = corre.getAppGrpPremId();

                premScopeList = applicationClient.getAppSvcPremisesScopeListByCorreId(correId).getEntity();

                setServiceSelfDeclConfig(premItemList, confList, svcCode, type, module, grpPremId);
                setSubTypeSelfDeclConfig(premItemList, confList, svcCode, type, module, grpPremId);


                //Do not display without config
                if (!IaisCommonUtils.isEmpty(confList)){
                    selfDecl.setPremAnswerMap(premiseAnswerMap);
                    selfDecl.setSvcCode(svcCode);
                    selfDecl.setSvcName(svcName);
                    selfDecl.setConfIdList(confList);
                    selfDecl.setSvcId(svcId);
                    premiseAnswerMap.put(correId, premItemList);
                    selfDecl.setPremAnswerMap(premiseAnswerMap);
                    premiseList.add(correId);
                    selfDeclList.add(selfDecl);
                }

                //only save one time
                if (!commonFlag){
                    setCommonDeclSelfConfig(selfDeclList, premiseList, grpPremId);
                    commonFlag = true;
                }
            }
        }



        ascendOrderByCommon(selfDeclList);

        return selfDeclList;
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

    /**
    * @author: yichen
    * @description: Load checklist
    * @param:
    * @return:
    */
    private void setPremChecklistItem(List<PremCheckItem> premItemList, String configId, Boolean isCommon, String scopeId, String grpPremId){
        if (premItemList != null){
            SearchParam searchParam = new SearchParam(ChecklistQuestionDto.class.getName());
            searchParam.addFilter("common", isCommon, true);
            if (!isCommon){
                searchParam.addFilter("svc_type", "Self-Assessment", true);
            }

            if (configId != null){
                searchParam.addFilter("configId", configId, true);
            }

            QueryHelp.setMainSql("applicationQuery", "listSelfDesc", searchParam);

            if (grpPremId != null){
                AppGrpPremisesEntityDto addressDto = applicationClient.getAppGrpPremise(grpPremId).getEntity();
                SearchResult<ChecklistQuestionDto> searchResult = appConfigClient.listSelfDescConfig(searchParam).getEntity();
                List<ChecklistQuestionDto> rows = searchResult.getRows();
                for (ChecklistQuestionDto question : rows){
                    PremCheckItem premCheckItem = new PremCheckItem();
                    premCheckItem.setChecklistItem(question.getChecklistItem());
                    premCheckItem.setRegulation(question.getRegClauseNo());
                    premCheckItem.setChecklistItemId(question.getItemId());

                    if (addressDto != null){
                        premCheckItem.setAddress(IaisCommonUtils.appendPremisesAddress(addressDto.getBlkNo(),
                                addressDto.getStreetName(),
                                addressDto.getBuildingName(), addressDto.getFloorNo(), addressDto.getUnitNo(), addressDto.getPostalCode()));
                    }

                    String pkId = question.getId().substring(0, 10);   //from db section item id
                    String itemId = question.getItemId().substring(0, 10);

                    //Answer key, ensure uniqueness
                    premCheckItem.setAnswerKey(pkId + itemId + scopeId);

                    premItemList.add(premCheckItem);
                }
            }


        }
    }

    @Override
    /**
    * @author: yichen
    * @description: If the transaction for FE fails, the action saved to BE is not triggered.
    * If BE fails, will be bactch-job synchronizes the data
    * @param: [selfDeclList]
    * @return: void
    */
    public void saveSelfDecl(List<SelfDecl> selfDeclList, String groupId) {
        //save fe after return data
        List<String> contentJsonList = applicationClient.saveAllSelfDecl(selfDeclList).getEntity();

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
    public Date getBlockPeriodByAfterApp(String groupId, List<SelfDecl> selfDeclList) {
        if (IaisCommonUtils.isEmpty(selfDeclList)){
            return null;
        }

        ApplicationGroupDto groupDto = applicationClient.getApplicationGroup(groupId).getEntity();
        int min = Integer.MAX_VALUE;
        for (SelfDecl selfDecl : selfDeclList){
            if (!selfDecl.isCommon()){
                HcsaServicePrefInspPeriodDto periodDto = selfDecl.getPeriodDto();
                if (periodDto != null){
                    min = Math.min(min, periodDto.getPeriodAfterApp());
                }
            }
        }
        Calendar c = Calendar.getInstance();
        c.setTime(groupDto.getSubmitDt());
        c.add(Calendar.DAY_OF_MONTH, min);

        return c.getTime();
    }

    private SelfDecl overlayCommon(List<String> premIdList, String configId, String grpPremId){
        if (premIdList == null){
            return null;
        }

        SelfDecl selfDecl = new SelfDecl();
        List<PremCheckItem> premCheckItems = IaisCommonUtils.genNewArrayList();
        setPremChecklistItem(premCheckItems, configId, true, null, grpPremId);

        Map<String, List<PremCheckItem>> premAnswerMap = new HashMap<>(16);

        for (String s : premIdList){
            premAnswerMap.put(s, premCheckItems);
        }

        selfDecl.setPremAnswerMap(premAnswerMap);
        selfDecl.setCommon(true);

        List<String> configList = IaisCommonUtils.genNewArrayList();
        configList.add(configId);

        selfDecl.setConfIdList(configList);
        return selfDecl;
    }
}
