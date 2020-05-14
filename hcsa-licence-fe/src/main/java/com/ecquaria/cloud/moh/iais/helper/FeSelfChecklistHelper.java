package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessment;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessmentConfig;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesSelfDeclChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: yichen
 * @Description:
 * @Date:2020/4/28
 **/

@Slf4j
public final class FeSelfChecklistHelper {
    private FeSelfChecklistHelper(){}

    /**
     * @Author yichen
     * @Description: new
     * @Date: 15:34 2020/4/28
     * @Param: []
     * @return:
     **/
    public static List<PremCheckItem> loadPremisesQuestion(final ChecklistConfigDto configDto, final boolean isSubType){
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

    public static List<SelfAssessment> receiveSelfAssessmentDataByCorrId(String corrId){
        List<SelfAssessment> rfiData = IaisCommonUtils.genNewArrayList();
        Map<String, SelfAssessmentConfig> selfAssessmentConfigMap = IaisCommonUtils.genNewHashMap();

        ApplicationClient applicationClient = SpringContextHelper.getContext().getBean(ApplicationClient.class);
        AppConfigClient appConfigClient = SpringContextHelper.getContext().getBean(AppConfigClient.class);
        if (applicationClient == null || appConfigClient == null){
            log.info("receiveSelfAssessmentRfiByCorrId ==>> ApplicationClient or AppConfigClient is null");
            return rfiData;
        }

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
}
