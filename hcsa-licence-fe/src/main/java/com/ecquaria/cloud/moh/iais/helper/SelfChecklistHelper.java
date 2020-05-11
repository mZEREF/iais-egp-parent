package com.ecquaria.cloud.moh.iais.helper;

import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.application.QuestionAnswer;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesSelfDeclChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistSectionDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: yichen
 * @Description:
 * @Date:2020/4/28
 **/

public final class SelfChecklistHelper {
    private SelfChecklistHelper(){}

    public static List<PremCheckItem> loadPremisesQuestion(final ChecklistConfigDto configDto, final String address){
        return loadPremisesQuestion(configDto, false);
    }

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
}
