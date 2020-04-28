package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDeclSubmitDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDeclaration;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesEntityDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesSelfDeclChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.SelfChecklistHelper;
import com.ecquaria.cloud.moh.iais.service.SelfDeclRfiService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author: yichen
 * @description:
 * @date:2020/3/24
 **/

@Service
@Slf4j
public class SelfDeclRfiServiceImpl implements SelfDeclRfiService {

    @Autowired
    private AppConfigClient appConfigClient;

    @Autowired
    private ApplicationClient applicationClient;

    // Todo Logic can be modified more easily
    @Override
    public SelfDeclSubmitDto getSelfDeclRfiData(String groupId) {
        SelfDeclSubmitDto selfDeclSubmitDto = new SelfDeclSubmitDto();
        List<SelfDeclaration> selfDeclarationList = IaisCommonUtils.genNewArrayList();

        boolean addedCommon = false;
        List<String> lastVersionIds = IaisCommonUtils.genNewArrayList();
        List<ApplicationDto> applicationList = applicationClient.listApplicationByGroupId(groupId).getEntity();
        Map<String, SelfDeclaration> svcDeclMapping = IaisCommonUtils.genNewHashMap();
        for (ApplicationDto app : applicationList) {
            String appId = app.getId();
            String svcId = app.getServiceId();
            HcsaServiceDto service = HcsaServiceCacheHelper.getServiceById(svcId);
            String svcCode = service.getSvcCode();
            String svcName = service.getSvcName();

            List<AppPremisesCorrelationDto> correlationList = applicationClient.listAppPremisesCorrelation(appId).getEntity();

            for (AppPremisesCorrelationDto correlation : correlationList){
                String correlationId = correlation.getId();
                String appGrpPremId = correlation.getAppGrpPremId();
                AppGrpPremisesEntityDto appGrpPremises = applicationClient.getAppGrpPremise(appGrpPremId).getEntity();
                String address = IaisCommonUtils.appendPremisesAddress(appGrpPremises.getBlkNo(), appGrpPremises.getStreetName(),
                        appGrpPremises.getBuildingName(), appGrpPremises.getFloorNo(), appGrpPremises.getUnitNo(), appGrpPremises.getPostalCode());

                List<AppPremisesSelfDeclChklDto> selfDeclChklList = applicationClient
                        .getAppPremisesSelfDeclByCorrelationId(correlationId).getEntity();

                if (!addedCommon){
                    SelfDeclaration commonSelfDecl = getCommonSelfDecl(selfDeclChklList);
                    selfDeclarationList.add(commonSelfDecl);
                    addedCommon = true;
                }

                lastVersionIds.addAll(selfDeclChklList.stream()
                        .map(AppPremisesSelfDeclChklDto::getId).collect(Collectors.toList()));

                Iterator<AppPremisesSelfDeclChklDto> iterator = selfDeclChklList.iterator();
                while (iterator.hasNext()){
                    AppPremisesSelfDeclChklDto selfDecl = iterator.next();
                    String configId = selfDecl.getChkLstConfId();
                    Optional<ChecklistConfigDto> optional = Optional.ofNullable(appConfigClient.getChecklistConfigById(configId).getEntity());
                    if (optional.isPresent()){
                        ChecklistConfigDto checklistConfig = optional.get();
                        if(!checklistConfig.isCommon()){
                            boolean hasSubtype = !StringUtils.isEmpty(checklistConfig.getSvcSubType()) ? true : false;

                            SelfDeclaration selfDeclaration;
                            if (svcDeclMapping.containsKey(svcId)){
                                selfDeclaration = svcDeclMapping.get(svcId);
                                loadDeclarationData(selfDeclaration, selfDecl, hasSubtype, false);
                            }else {
                                selfDeclaration = new SelfDeclaration();
                                selfDeclaration.setSvcCode(svcCode);
                                selfDeclaration.setSvcId(svcId);
                                selfDeclaration.setSvcName(svcName);
                                LinkedHashMap<String, List<PremCheckItem>> eachPremQuestion = new LinkedHashMap<>();
                                selfDeclaration.setEachPremQuestion(eachPremQuestion);

                                loadDeclarationData(selfDeclaration, selfDecl, hasSubtype, false);

                                svcDeclMapping.put(svcId, selfDeclaration);
                                selfDeclarationList.add(selfDeclaration);
                            }

                            if (!selfDeclaration.isHasSubtype() && hasSubtype){
                                selfDeclaration.setHasSubtype(hasSubtype);
                            }else {
                                selfDeclaration.setConfigId(selfDecl.getChkLstConfId());
                            }

                        }
                    }
                }
            }

            selfDeclSubmitDto.setLastVersionIds(lastVersionIds);
            selfDeclSubmitDto.setSelfDeclarationList(selfDeclarationList);

        }
        return selfDeclSubmitDto;
    }

    private void loadDeclarationData(SelfDeclaration selfDeclaration, AppPremisesSelfDeclChklDto selfDecl, boolean isSubtype, boolean isCommon){
        selfDeclaration.setConfigId(selfDecl.getChkLstConfId());
        selfDeclaration.setCommon(isCommon);
        selfDeclaration.setVersion(selfDecl.getVersion());
        LinkedHashMap<String, List<PremCheckItem>> eachPremQuestion = selfDeclaration.getEachPremQuestion();
        SelfChecklistHelper.reloadPremisesQuestion(eachPremQuestion, selfDecl, isSubtype);
        selfDeclaration.setEachPremQuestion(eachPremQuestion);
    }

    private SelfDeclaration getCommonSelfDecl(List<AppPremisesSelfDeclChklDto> selfDeclChklList){
        SelfDeclaration commonSelfDecl = new SelfDeclaration();
        LinkedHashMap<String, List<PremCheckItem>> eachPremQuestion = new LinkedHashMap<>();
        commonSelfDecl.setEachPremQuestion(eachPremQuestion);
        List<String> saveByInactivePkId = IaisCommonUtils.genNewArrayList();
        for (AppPremisesSelfDeclChklDto selfDecl : selfDeclChklList){
            String configId = selfDecl.getChkLstConfId();
            Optional<ChecklistConfigDto> optional = Optional.ofNullable(appConfigClient.getChecklistConfigById(configId).getEntity());
            if (optional.isPresent()){
                ChecklistConfigDto checklistConfig = optional.get();
                if (checklistConfig.isCommon()){
                    loadDeclarationData(commonSelfDecl, selfDecl, false, true);
                    saveByInactivePkId.add(selfDecl.getId());
                    break;
                }
            }
        }
        return commonSelfDecl;
    }
}
