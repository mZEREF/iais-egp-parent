package com.ecquaria.cloud.moh.iais.service.impl;


import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessment;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDecl;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDeclGroup;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServicePrefInspPeriodDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private EicGatewayClient gatewayClient;

    private List<AppSvcPremisesScopeDto> premScopeList;

    private List<String> svcCodeList;

    @Value("${iais.hmac.keyId}")
    private String keyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;


    private String transformModule(String module) {
        if (StringUtil.isEmpty(module)) {
            return null;
        }

        String retStr = null;
        switch (module) {
            case "N":
                retStr = "New";
                break;
            case "R":
                retStr = "Renewal";
                break;
            default:
        }

        return retStr;
    }

    @Override
    /**
     * @author: yichen
     * @description: Selfdecl entity is Represent a tab in the web page, (Single)first common , (Multiple)then service, (Multiple)behind subtype
     * @param: [groupId]
     * @return: java.util.List<com.ecquaria.cloud.moh.iais.common.dto.application.SelfDecl>
     */
    public List<SelfDecl> getSelfDeclByGroupId(String groupId) {
        List<ApplicationDto> appList = applicationClient.listApplicationByGroupId(groupId).getEntity();
        SelfDeclGroup selfDeclGroup = new SelfDeclGroup();
        List<SelfAssessment> selfAssessmentList = new ArrayList<>();
        for (ApplicationDto app : appList) {
            SelfAssessment selfAssessment = new SelfAssessment();
            SelfAssessment commonSelfAssessment = new SelfAssessment();


            String appId = app.getId();
            String svcId = app.getServiceId();
            String appType = app.getApplicationType();
            String type = MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.SELF_ASSESSMENT);
            String module = transformModule(MasterCodeUtil.getCodeDesc("APTY002").substring(0, 1));

            HcsaServiceDto hcsa = appConfigClient.getHcsaServiceDtoByServiceId(svcId).getEntity();
            String svcCode = hcsa.getSvcCode();
            String svcName = hcsa.getSvcName();

            // search inspection period
            HcsaServicePrefInspPeriodDto inspPeriod = appConfigClient.getHcsaServicePrefInspPeriod(svcCode).getEntity();


            Map<String, List<PremCheckItem>> premiseAnswerMap = new HashMap<>(16);
            List<AppPremisesCorrelationDto> correlationList = applicationClient.listAppPremisesCorrelation(appId).getEntity();
            List<PremCheckItem> premItemList = new ArrayList<>();
            for (AppPremisesCorrelationDto corre : correlationList) {
                String correId = corre.getId();
                String grpPremId = corre.getAppGrpPremId();

                ChecklistConfigDto commonConfig = appConfigClient.getMaxVersionCommonConfig().getEntity();
                if (commonConfig != null){
                    loadingAnswerItem(premItemList, commonConfig, grpPremId);
                }

                //service
                ChecklistConfigDto svcConfig = appConfigClient.getMaxVersionConfigByParams(svcCode, type, module).getEntity();
                if (svcConfig != null) {
                    loadingAnswerItem(premItemList, svcConfig, grpPremId);
                }

                //sub type
                List<AppSvcPremisesScopeDto> premisesScopeList = applicationClient.getAppSvcPremisesScopeListByCorreId(correId).getEntity();
                for (AppSvcPremisesScopeDto scope : premisesScopeList) {
                    if (!scope.isSubsumedType()) {
                        String subTypeId = scope.getScopeName();
                        HcsaServiceSubTypeDto subType = appConfigClient.getHcsaServiceSubTypeById(subTypeId).getEntity();
                        String subTypeName = subType.getSubtypeName();
                        ChecklistConfigDto subTypeConfig = appConfigClient.getMaxVersionConfigByParams(svcCode, type, module, subTypeName).getEntity();
                        if (subTypeConfig != null) {
                            loadingAnswerItem(premItemList, subTypeConfig, grpPremId);
                        }
                    }

                }

                premiseAnswerMap.put(correId, premItemList);
                selfAssessment.setInspAfterApp(inspPeriod.getPeriodAfterApp());
                selfAssessment.setInspBeforeExp(inspPeriod.getPeriodBeforeExp());
                selfAssessment.setPremAnswerMap(premiseAnswerMap);
                selfAssessment.setSvcName(svcName);
                selfAssessment.setSvcCode(svcCode);
                selfAssessment.setAppType(appType);
                selfAssessmentList.add(selfAssessment);
            }

        }





        selfDeclGroup.setSelfAssessmentList(selfAssessmentList);
        return null;
    }

    @Override
    public void saveSelfDeclAndInspectionDate(List<SelfDecl> selfDeclList, String groupId, Date inspStartDate, Date inspEndDate) {

    }

    @Override
    public Date getBlockPeriodByAfterApp(String groupId, List<SelfDecl> selfDeclList) {
        return null;
    }

    private void loadingAnswerItem (List<PremCheckItem> premItemList, ChecklistConfigDto config, String grpPremId){
        AppGrpPremisesDto addressDto = applicationClient.getAppGrpPremise(grpPremId).getEntity();
        List<ChecklistSectionDto> sectionDtos = config.getSectionDtos();
        for (ChecklistSectionDto section : sectionDtos) {
            List<ChecklistItemDto> checklistItemDtos = section.getChecklistItemDtos();
            for (ChecklistItemDto item : checklistItemDtos) {
                PremCheckItem answerItem = new PremCheckItem();
                answerItem.setAnswerKey(UUID.randomUUID().toString());
                answerItem.setChecklistItem(item.getChecklistItem());
                answerItem.setChecklistItemId(item.getItemId());
                answerItem.setRegulation(item.getRegulationClause());
                answerItem.setAddress(addressDto.getAddress());
                premItemList.add(answerItem);
            }
        }
    }


}