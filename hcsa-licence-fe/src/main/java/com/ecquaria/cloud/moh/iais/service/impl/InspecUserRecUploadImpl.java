package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.InspecUserRecUploadService;
import com.ecquaria.cloud.moh.iais.service.client.AppConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Shicheng
 * @date 2019/12/23 15:25
 **/
@Service
@Slf4j
public class InspecUserRecUploadImpl implements InspecUserRecUploadService {

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private AppConfigClient appConfigClient;

    @Override
    public List<ChecklistItemDto> getQuesAndClause(String appNo) {
        List<ChecklistItemDto> checklistItemDtos = new ArrayList<>();
        if(!(StringUtil.isEmpty(appNo))){
            List<String> itemIds = applicationClient.getItemIdsByAppNo(appNo).getEntity();
            checklistItemDtos = getcheckDtosByItemIds(itemIds);
        }
        return checklistItemDtos;
    }

    private List<ChecklistItemDto> getcheckDtosByItemIds(List<String> itemIds) {
        List<ChecklistItemDto> checklistItemDtos = new ArrayList<>();
        if(itemIds != null && !(itemIds.isEmpty())) {
            for (String itemId:itemIds) {
                ChecklistItemDto checklistItemDto = appConfigClient.getChklItemById(itemId).getEntity();
                checklistItemDtos.add(checklistItemDto);
            }
        }
        return checklistItemDtos;
    }
}
