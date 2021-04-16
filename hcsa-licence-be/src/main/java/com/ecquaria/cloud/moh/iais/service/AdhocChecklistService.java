package com.ecquaria.cloud.moh.iais.service;

/*
 *author: yichen
 *date time:12/10/2019 1:00 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;

import java.util.List;

public interface AdhocChecklistService {

    List<ChecklistConfigDto> getInspectionChecklist(ApplicationDto application);

    void saveAdhocChecklist(AdhocCheckListConifgDto adhocConfig);

    boolean hasSampleChecklistItem(AdhocCheckListConifgDto adhocConfig);

    void removeAdhocItem(List<AdhocChecklistItemDto> itemList, int index);

    void filterAdhocItem(SearchParam searchParam, AdhocCheckListConifgDto config);

    void addSelectedChecklistItemToAdhocConfig(List<ChecklistItemDto> itemList, AdhocCheckListConifgDto config);
}
