package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.QuestionAnswer;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDeclSubmitDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDeclaration;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesSelfDeclChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
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
import java.util.UUID;
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

    @Override
    public SelfDeclSubmitDto getSelfDeclRfiData(String groupId) {
        SelfDeclSubmitDto selfDeclSubmitDto = new SelfDeclSubmitDto();
        List<SelfDeclaration> selfDeclarationList = IaisCommonUtils.genNewArrayList();

        boolean addedCommon = false;
        List<String> lastVersionIds = IaisCommonUtils.genNewArrayList();
        List<ApplicationDto> applicationList = applicationClient.listApplicationByGroupId(groupId).getEntity();
        for (ApplicationDto app : applicationList){
            String appId = app.getId();
            String svcId = app.getServiceId();
            HcsaServiceDto service = HcsaServiceCacheHelper.getServiceById(svcId);
            String svcCode = service.getSvcCode();
            String svcName = service.getSvcName();
            List<AppPremisesCorrelationDto> correlationList = applicationClient.listAppPremisesCorrelation(appId).getEntity();

            for (AppPremisesCorrelationDto correlation : correlationList){
                String correlationId = correlation.getId();
                List<AppPremisesSelfDeclChklDto> appPremisesSelfDeclChklDtos = applicationClient.getAppPremisesSelfDeclByCorrelationId(correlationId).getEntity();

                if (appPremisesSelfDeclChklDtos.isEmpty()){
                    log.info("The current application cannot find the submitted self-decl");
                    break;
                }

                if (!addedCommon){
                    SelfDeclaration commonSelfDecl = getCommonSelfDecl(appPremisesSelfDeclChklDtos);
                    selfDeclarationList.add(commonSelfDecl);
                    addedCommon = true;
                }

                lastVersionIds.addAll(appPremisesSelfDeclChklDtos.stream().map(AppPremisesSelfDeclChklDto::getId).collect(Collectors.toList()));

                SelfDeclaration serviceSelfDecl = getServiceSelfDecl(appPremisesSelfDeclChklDtos);
                serviceSelfDecl.setSvcId(svcId);
                serviceSelfDecl.setSvcCode(svcCode);
                serviceSelfDecl.setSvcName(svcName);

                selfDeclarationList.add(serviceSelfDecl);
            }

        }

        selfDeclSubmitDto.setLastVersionIds(lastVersionIds);
        selfDeclSubmitDto.setSelfDeclarationList(selfDeclarationList);
        return selfDeclSubmitDto;
    }

    private SelfDeclaration getServiceSelfDecl(List<AppPremisesSelfDeclChklDto> selfDeclChklList){
        SelfDeclaration serviceSelf = null;
        Iterator<AppPremisesSelfDeclChklDto> iterator = selfDeclChklList.iterator();

        while (iterator.hasNext()){
            AppPremisesSelfDeclChklDto selfdecl = iterator.next();

            String configId = selfdecl.getChkLstConfId();
            Optional<ChecklistConfigDto> optional = Optional.ofNullable(appConfigClient.getChecklistConfigById(configId).getEntity());
            if (optional.isPresent()){
                ChecklistConfigDto checklistConfig = optional.get();
                if (!checklistConfig.isCommon()){
                    if (StringUtils.isEmpty(checklistConfig.getSvcSubType())){
                        serviceSelf = loadServiceData(selfdecl, false, false);
                        serviceSelf.setSvcCode(checklistConfig.getSvcCode());

                        iterator.remove();
                    }
                }
            }
        }

        // load subtype info
        if (serviceSelf != null){
            String svcCode = serviceSelf.getSvcCode();
            iterator = selfDeclChklList.iterator();
            while (iterator.hasNext()){
                AppPremisesSelfDeclChklDto selfdecl = iterator.next();
                String configId = selfdecl.getChkLstConfId();
                Optional<ChecklistConfigDto> optional = Optional.ofNullable(appConfigClient.getChecklistConfigById(configId).getEntity());
                if (optional.isPresent()){
                    ChecklistConfigDto checklistConfig = optional.get();
                    if (!checklistConfig.isCommon()){
                        if (!StringUtils.isEmpty(checklistConfig.getSvcSubType())
                                && checklistConfig.getSvcCode().equals(svcCode)){

                            serviceSelf.setHasSubtype(true);
                            LinkedHashMap<String, List<PremCheckItem>> eachPremQuestion = serviceSelf.getEachPremQuestion();
                            if (eachPremQuestion.containsKey(selfdecl.getAppPremCorreId())){
                                List<PremCheckItem> items = eachPremQuestion.get(selfdecl.getAppPremCorreId());

                                String answerJson = selfdecl.getAnswer();
                                List<Map<String, Object>> answerList = JsonUtil.parseToObject(answerJson, List.class);
                                List<QuestionAnswer> questionAnswers = JsonUtil.transferListContent(answerList, QuestionAnswer.class);
                                questionAnswers.forEach(question -> {
                                    PremCheckItem premCheckItem = new PremCheckItem();
                                    premCheckItem.setRegulation(question.getRegulation());
                                    premCheckItem.setChecklistItem(question.getChecklistItem());
                                    premCheckItem.setConfigId(selfdecl.getChkLstConfId());
                                    premCheckItem.setAnswerKey(UUID.randomUUID().toString());
                                    premCheckItem.setChecklistItemId(question.getChecklistItemId());
                                    premCheckItem.setAnswer(question.getAnswer());
                                    premCheckItem.setSubType(true);
                                    items.add(premCheckItem);
                                });

                                eachPremQuestion.put(selfdecl.getAppPremCorreId(), items);
                                serviceSelf.setEachPremQuestion(eachPremQuestion);
                            }

                        }
                    }

                }
            }
        }
        return serviceSelf;
    }

    private SelfDeclaration loadServiceData(AppPremisesSelfDeclChklDto selfdecl, Boolean isSubtype, Boolean isCommon){
        SelfDeclaration serviceSelf = new SelfDeclaration();
        serviceSelf.setConfigId(selfdecl.getChkLstConfId());
        serviceSelf.setCommon(isCommon);
        serviceSelf.setVersion(selfdecl.getVersion());

        LinkedHashMap<String, List<PremCheckItem>> eachPremQuestion = new LinkedHashMap<>();
        List<PremCheckItem> premCheckItemList = IaisCommonUtils.genNewArrayList();

        String answerJson = selfdecl.getAnswer();
        List<Map<String, Object>> answerList = JsonUtil.parseToObject(answerJson, List.class);
        List<QuestionAnswer> questionAnswers = JsonUtil.transferListContent(answerList, QuestionAnswer.class);
        questionAnswers.forEach(question -> {
            PremCheckItem premCheckItem = new PremCheckItem();
            premCheckItem.setChecklistItem(question.getChecklistItem());
            premCheckItem.setRegulation(question.getRegulation());
            premCheckItem.setConfigId(selfdecl.getChkLstConfId());
            premCheckItem.setChecklistItemId(question.getChecklistItemId());
            premCheckItem.setAnswerKey(UUID.randomUUID().toString());
            premCheckItem.setAnswer(question.getAnswer());
            premCheckItem.setSubType(isSubtype);
            premCheckItemList.add(premCheckItem);
        });

        eachPremQuestion.put(selfdecl.getAppPremCorreId(), premCheckItemList);
        serviceSelf.setEachPremQuestion(eachPremQuestion);

        return serviceSelf;
    }

    private SelfDeclaration getCommonSelfDecl(List<AppPremisesSelfDeclChklDto> selfDeclChklList){
        SelfDeclaration commonSelfDecl = null;
        List<String> saveByInactivePkId = IaisCommonUtils.genNewArrayList();
        for (AppPremisesSelfDeclChklDto decl : selfDeclChklList){
            String configId = decl.getChkLstConfId();
            Optional<ChecklistConfigDto> optional = Optional.ofNullable(appConfigClient.getChecklistConfigById(configId).getEntity());
            if (optional.isPresent()){
                ChecklistConfigDto checklistConfig = optional.get();
                if (checklistConfig.isCommon()){
                    commonSelfDecl = loadServiceData(decl, false, true);
                    saveByInactivePkId.add(decl.getId());
                    break;
                }
            }
        }
        return commonSelfDecl;
    }
}
