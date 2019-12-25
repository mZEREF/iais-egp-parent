package com.ecquaria.cloud.moh.iais.service.impl;


import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDecl;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.AppPremSelfDeclService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.EicGatewayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    private EicGatewayClient gatewayClient;

    private List<AppSvcPremisesScopeDto> premScopeList;

    private List<String> svcCodeList;

    @Value("${iais.hmac.keyId}")
    private String keyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;



    /**
    * @author: yichen 
    * @description: Set service config
    * @param: 
    * @return: 
    */
    private void setServiceSelfDeclConfig(List<PremCheckItem> premItemList, List<String> confList,
                                          String svcCode, String type, String module, String grpPremId){
        for (AppSvcPremisesScopeDto premise : this.premScopeList){
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
        for (AppSvcPremisesScopeDto premise : this.premScopeList){
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
    private void setCommonDeclSelfConfig(List<SelfDecl> selfDeclList,
                                         List<String> premIdList,
                                         String type, String module){
        ChecklistConfigDto commonConfig = appConfigClient.getMaxVersionCommonConfigByParams(type, module).getEntity();
        SelfDecl commonSelfDecl = overlayCommon(premIdList, commonConfig.getId());
        selfDeclList.add(commonSelfDecl);
    }

    @Override
    /**
    * @author: yichen
    * @description: Selfdecl entity is Represent a tab in the web page, (Single)first common , (Multiple)then service, (Multiple)behind subtype
    * @param: [groupId]
    * @return: java.util.List<com.ecquaria.cloud.moh.iais.common.dto.application.SelfDecl>
    */
    public List<SelfDecl> getSelfDeclByGroupId(String groupId) {

        // (S) Group , (M) application
        List<ApplicationDto> appList = applicationClient.listApplicationByGroupId(groupId).getEntity();
        List<SelfDecl> selfDeclList = new ArrayList<>();
        List<String> premIdList = new ArrayList<>();
        String type = MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.SELF_ASSESSMENT);
        String module = MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.NEW);
        for (ApplicationDto app : appList){
            SelfDecl selfDecl = new SelfDecl();
            List<String> confList = new ArrayList<>();
            String appId = app.getId();
            String svcId = app.getServiceId();
            HcsaServiceDto hcsa = appConfigClient.getHcsaServiceDtoByServiceId(svcId).getEntity();
            String svcCode = hcsa.getSvcCode();

            String svcName = hcsa.getSvcName();
            List<PremCheckItem> premItemList = new ArrayList<>();
            Map<String, List<PremCheckItem>> premAnswerMap = new HashMap<>(16);
            List<AppPremisesCorrelationDto>  correlationList = applicationClient.listAppPremisesCorrelation(appId).getEntity();
            for (AppPremisesCorrelationDto corre : correlationList){
                String correId = corre.getId();
                String grpPremId = corre.getAppGrpPremId();

                this.premScopeList = applicationClient.getAppSvcPremisesScopeListByCorreId(correId).getEntity();

                setServiceSelfDeclConfig(premItemList, confList,svcCode, type, module, grpPremId);
                setSubTypeSelfDeclConfig(premItemList, confList, svcCode, type, module, grpPremId);

                selfDecl.setPremAnswerMap(premAnswerMap);
                selfDecl.setSvcCode(svcCode);
                selfDecl.setSvcName(svcName);
                selfDecl.setConfIdList(confList);
                selfDecl.setSvcId(svcId);
                premAnswerMap.put(correId, premItemList);
                selfDecl.setPremAnswerMap(premAnswerMap);
                premIdList.add(correId);
                selfDeclList.add(selfDecl);
            }
        }

        setCommonDeclSelfConfig(selfDeclList, premIdList, type, module);

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
            searchParam.addFilter("svc_type", "Self-Assessment", true);
            searchParam.addFilter("common", isCommon, true);

            if (configId != null){
                searchParam.addFilter("configId", configId, true);
            }

            QueryHelp.setMainSql("applicationQuery", "listSelfDesc", searchParam);

            AppGrpPremisesDto addressDto = null;
            if (grpPremId != null){
                addressDto = applicationClient.getAppGrpPremise(grpPremId).getEntity();
            }

            SearchResult<ChecklistQuestionDto> searchResult = appConfigClient.listSelfDescConfig(searchParam).getEntity();
            List<ChecklistQuestionDto> rows = searchResult.getRows();
            for (ChecklistQuestionDto question : rows){
                PremCheckItem premCheckItem = new PremCheckItem();
                premCheckItem.setChecklistItem(question.getChecklistItem());
                premCheckItem.setRegulation(question.getRegClauseNo());
                premCheckItem.setChecklistItemId(question.getItemId());

                if (addressDto != null){
                    premCheckItem.setAddress(addressDto.getAddress());
                }

                String pkId = question.getId().substring(0, 10);   //from db section item id
                String itemId = question.getItemId().substring(0, 10);

                //Answer key, ensure uniqueness
                premCheckItem.setAnswerKey(pkId + itemId + scopeId);

                premItemList.add(premCheckItem);
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
    public void saveSelfDeclAndInspectionDate(List<SelfDecl> selfDeclList, String groupId, Date inspStartDate, Date inspEndDate) {
        //save fe after return data
        List<String> contentJsonList = applicationClient.saveAllSelfDecl(selfDeclList).getEntity();

        //route to be
        if (contentJsonList != null && !contentJsonList.isEmpty()){
            ApplicationGroupDto applicationGroupDto = new ApplicationGroupDto();
            applicationGroupDto.setId(groupId);
            applicationGroupDto.setPrefInspStartDate(inspStartDate);
            applicationGroupDto.setPrefInspEndDate(inspEndDate);
            applicationClient.doUpDate(applicationGroupDto);

            HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
            gatewayClient.routeSelfDeclData(contentJsonList, signature.date(), signature.authorization()).getEntity();
        }
    }

    private SelfDecl overlayCommon(List<String> premIdList, String configId){
        if (premIdList == null){
            return null;
        }

        SelfDecl selfDecl = new SelfDecl();
        List<PremCheckItem> premCheckItems = new ArrayList<>();
        setPremChecklistItem(premCheckItems, configId, true, null, null);

        Map<String, List<PremCheckItem>> premAnswerMap = new HashMap<>(16);

        for (String s : premIdList){
            premAnswerMap.put(s, premCheckItems);
        }

        selfDecl.setPremAnswerMap(premAnswerMap);
        selfDecl.setCommon(true);

        List<String> configList = new ArrayList<>();
        configList.add(configId);

        selfDecl.setConfIdList(configList);
        return selfDecl;
    }
}
