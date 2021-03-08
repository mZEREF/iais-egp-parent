package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessment;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationFeClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
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
    public static LinkedHashMap<String, List<PremCheckItem>> loadPremisesQuestion(final ChecklistConfigDto configDto, final boolean isSubType){
        log.info("FeSelfChecklistHelper [loadPremisesQuestion] START......isSubType {}", isSubType);
        LinkedHashMap<String, List<PremCheckItem>> answerMap = new LinkedHashMap<>();
        List<ChecklistSectionDto> checklistSection = configDto.getSectionDtos();
        if (IaisCommonUtils.isNotEmpty(checklistSection)){
            for(ChecklistSectionDto i : checklistSection){
                String sectionName = i.getSection();
                List<ChecklistItemDto> item = i.getChecklistItemDtos();
                if (IaisCommonUtils.isNotEmpty(item)){
                    List<PremCheckItem> premCheckItemList = answerMap.get(sectionName);
                    premCheckItemList = Optional.ofNullable(premCheckItemList).orElseGet(() -> IaisCommonUtils.genNewArrayList());
                    for (ChecklistItemDto j : item){
                        PremCheckItem premCheckItem = new PremCheckItem();

                        //record subtype config id
                        if (isSubType){
                            premCheckItem.setConfigId(configDto.getId());
                        }

                        premCheckItem.setSectionName(sectionName);
                        premCheckItem.setSubType(isSubType);
                        premCheckItem.setRegulationId(j.getRegulationId());
                        premCheckItem.setRegulation(j.getRegulationClauseNo());
                        premCheckItem.setRegulationClause(j.getRegulationClause());
                        premCheckItem.setAnswerKey(UUID.randomUUID().toString());
                        premCheckItem.setChecklistItem(j.getChecklistItem());
                        premCheckItem.setChecklistItemId(j.getItemId());
                        premCheckItemList.add(premCheckItem);
                    }
                    answerMap.put(sectionName, premCheckItemList);
                }
            }
        }

        log.info("FeSelfChecklistHelper [loadPremisesQuestion] END......");
        return answerMap;
    }

    public static List<SelfAssessment> receiveSelfAssessmentDataByCorrId(String corrId){
        ApplicationFeClient appFeClient = SpringContextHelper.getContext().getBean(ApplicationFeClient.class);
        if (Optional.ofNullable(appFeClient).isPresent()){
            return appFeClient.receiveSelfAssessmentDataByCorrId(corrId).getEntity();
        }
        return Collections.EMPTY_LIST;
    }
}
