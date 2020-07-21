package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.helper.SpringContextHelper;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfAssessment;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
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
    public static Map<String, List<PremCheckItem>> loadPremisesQuestion(final ChecklistConfigDto configDto, final boolean isSubType){
        Map<String, List<PremCheckItem>> sqMap = IaisCommonUtils.genNewHashMap();
        List<ChecklistSectionDto> checklistSectionDtos = configDto.getSectionDtos();
        if (!IaisCommonUtils.isEmpty(checklistSectionDtos)){
            for(ChecklistSectionDto i : checklistSectionDtos){
                String sectionName = i.getSection();
                List<ChecklistItemDto> item = i.getChecklistItemDtos();
                if (!IaisCommonUtils.isEmpty(item)){
                    List<PremCheckItem> premCheckItemList = sqMap.get(sectionName);
                    if (premCheckItemList == null){
                        premCheckItemList = IaisCommonUtils.genNewArrayList();
                    }

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
                    sqMap.put(sectionName, premCheckItemList);
                }
            }
        }

        return sqMap;
    }

    public static List<SelfAssessment> receiveSelfAssessmentDataByCorrId(String corrId){
        ApplicationClient applicationClient = SpringContextHelper.getContext().getBean(ApplicationClient.class);
        if (applicationClient != null){
            return applicationClient.receiveSelfAssessmentDataByCorrId(corrId).getEntity();
        }
        return Collections.EMPTY_LIST;
    }
}
