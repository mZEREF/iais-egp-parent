package com.ecquaria.cloud.moh.iais.service.impl;

/*
 *author: yichen
 *date time:11/20/2019 1:56 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.ChecklistQuestionDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDecl;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.helper.QueryHelp;
import com.ecquaria.cloud.moh.iais.service.AppPremSelfDeclService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class AppPremSelfDeclServiceImpl implements AppPremSelfDeclService {
    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private AppConfigClient appConfigClient;

    @Override
    public List<SelfDecl> getSelfDeclByGroupId(String groupId) {
        List<ApplicationDto> appList = RestApiUtil.getListByPathParam(RestApiUrlConsts.APPLICATION_RESULTS_BY_GROUP_ID, groupId, ApplicationDto.class);
        List<SelfDecl> selfDeclList = new ArrayList<>();
        List<String> premIdList = new ArrayList<>();
        for (ApplicationDto app : appList){
            SelfDecl selfDecl = new SelfDecl();
            List<String> confList = new ArrayList<>();


            String appId = app.getId();
            String svcId = app.getServiceId();
            HcsaServiceDto hcsa = RestApiUtil.getByPathParam(RestApiUrlConsts.HCSA_CONFIG + "/iais-hcsa-service/one-of-hcsa-service/{serviceId}", svcId, HcsaServiceDto.class);
            String svcCode = hcsa.getSvcCode();
            String svcName = hcsa.getSvcName();

            //may be has list
            String configId = RestApiUtil.getByPathParam(RestApiUrlConsts.HCSA_CONFIG + "/iais-hcsa-checklist/config/id/{svcCode}", svcCode, String.class);
            confList.add(configId);

            List<PremCheckItem> premCheckItems = loadPremChecklistItem(configId, false);

           // List<AppGrpPremisesDto> premisesDto = RestApiUtil.getListByPathParam(RestApiUrlConsts.IAIS_APPLICATION + "/application/app-group-premises-results/{appid}", appId, AppGrpPremisesDto.class);
            AppGrpPremisesDto premisesDto = RestApiUtil.getByPathParam(RestApiUrlConsts.IAIS_APPLICATION + "/application-premises-by-app-id/{applicationId}", appId, AppGrpPremisesDto.class);
            String premId = premisesDto.getId();
            premIdList.add(premId);

            Map<String, List<PremCheckItem>> premAnswerMap = new HashMap<>(16);
            premAnswerMap.put(premId, premCheckItems);

            selfDecl.setPremAnswerMap(premAnswerMap);
            selfDecl.setSvcCode(svcCode);
            selfDecl.setSvcName(svcName);
            selfDecl.setConfIdList(confList);
            selfDecl.setSvcId(svcId);
            selfDeclList.add(selfDecl);
        }

        SelfDecl common = overlayCommon(premIdList);
        if (common != null){
            selfDeclList.add(common);
        }

        selfDeclList.sort((s1, s2) -> {
            if (s1.isCommon() ^ s2.isCommon()) {
                return s1.isCommon() ? -1 : 1;
            } else {
                return 0;
            }
        });
        return selfDeclList;
    }

    @Override
    public void saveSelfDecl(List<SelfDecl> selfDeclList) {
        RestApiUtil.save(RestApiUrlConsts.IAIS_APPLICATION + "/self-decl", selfDeclList);
    }

    private SelfDecl overlayCommon(List<String> premIdList){
        if (premIdList == null){
            return null;
        }

        SelfDecl selfDecl = new SelfDecl();
        List<PremCheckItem> premCheckItems = loadPremChecklistItem(null, true);
        Map<String, List<PremCheckItem>> premAnswerMap = new HashMap<>(16);

        for (String s : premIdList){
            premAnswerMap.put(s, premCheckItems);
        }

        selfDecl.setPremAnswerMap(premAnswerMap);
        selfDecl.setCommon(true);

        String cfid = RestApiUtil.getByPathParam(RestApiUrlConsts.HCSA_CONFIG  + RestApiUrlConsts.HCSA_CONFIG_CHECKLIST_URL + "/config/common-self-desc/id", "", String.class);
        List<String> configList = new ArrayList<>();
        configList.add(cfid);

        selfDecl.setConfIdList(configList);
        return selfDecl;
    }


    private List<PremCheckItem> loadPremChecklistItem(String configId, Boolean isCommon){
        List<PremCheckItem> list = new ArrayList<>();

        SearchParam searchParam = new SearchParam(ChecklistQuestionDto.class.getName());
        searchParam.addFilter("svc_type", "Self-Assessment", true);
        searchParam.addFilter("common", isCommon, true);

        if (configId != null){
            searchParam.addFilter("configId", configId, true);
        }

        QueryHelp.setMainSql("applicationQuery", "listSelfDesc", searchParam);

        SearchResult searchResult = RestApiUtil.query(RestApiUrlConsts.HCSA_CONFIG + RestApiUrlConsts.CHECKLIST_SELF_DESC_CONFIG, searchParam);
        List<ChecklistQuestionDto> rows = searchResult.getRows();
        for (ChecklistQuestionDto question : rows){
            PremCheckItem premCheckItem = new PremCheckItem();
            premCheckItem.setChecklistItem(question.getChecklistItem());
            premCheckItem.setRegulation(question.getRegClauseNo());
            premCheckItem.setChecklistItemId(question.getItemId());
            list.add(premCheckItem);
        }

        return list;
    }
}
