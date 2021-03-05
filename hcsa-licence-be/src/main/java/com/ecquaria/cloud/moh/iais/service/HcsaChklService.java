package com.ecquaria.cloud.moh.iais.service;

/*
 *author: yichen
 *date time:9/25/2019 2:26 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.CheckItemQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigExcel;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemExcel;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklSvcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.ErrorMsgContent;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HcsaChklService {
    /**
     * list checklist item by SearchParam
     * backend method: listChecklistItem
     * @param searchParam
     * @return
     */
    SearchResult<CheckItemQueryDto> listChklItem(SearchParam searchParam);

    /**
     * list checklist config by SearchParam
     * backend method: listChecklistConfig
     * @param searchParam
     * @return
     */
    SearchResult<ChecklistConfigQueryDto> listChecklistConfig(SearchParam searchParam);

    /**
     * list checklist item by item id
     * backend method: listChecklistItem
     * @param itemIds
     * @return
     */
    List<ChecklistItemDto> listChklItemByItemId(List<String> itemIds);

    /**
     * get checklist item by item id
     * backend method: getChecklistItemById
     * @param id
     * @return
     */
    ChecklistItemDto getChklItemById(String id);

    /**
     * save checklist item, call backend api , include create or update logic
     * backend method: saveChecklistItem
     * @param itemDto
     */
    IaisApiResult<ChecklistItemDto> saveChklItem(ChecklistItemDto itemDto);

    /**
     * list de-weight regulation
     * backend method: listRegulationClauseNo
     * @return
     */
    List<HcsaChklSvcRegulationDto> listRegulationClause();

    /**
     * submit clone item
     * backend method: saveCloneItem
     * @param checklistItemDtos
     */
    Boolean submitCloneItem(List<ChecklistItemDto> checklistItemDtos);

    /**
     * submit single config to backend, backend need clean to uuid of each section obj then generate by db
     * backend method: submitConfigInfo
     * @param checklistConfigDto
     */
    ChecklistConfigDto submitConfig(ChecklistConfigDto checklistConfigDto);

    /**
     *
     * backend method: listSubTypeName
     */
    List<String> listSubTypeName();

    /**
     *
     * backend method: listServiceName
     */
    List<String> listServiceName();

    ChecklistConfigDto getChecklistConfigById(String id);

    Boolean isExistsRecord(ChecklistConfigDto dto);

    List<ErrorMsgContent> createConfigTemplate(ChecklistConfigDto excelTemplate);

    List<ErrorMsgContent> updateConfigTemplate(ChecklistConfigDto excelTemplate);

    Boolean deleteRecord(String configId);

    Boolean inActiveItem(String itemId);

    List<ErrorMsgContent> submitUploadItems(List<ChecklistItemExcel> uploadItems);

    List<ErrorMsgContent> updateUploadItems(List<ChecklistItemExcel> updateItems);

    void convertToUploadTemplateByConfig(List<ChecklistConfigExcel> configExcelList, ChecklistConfigDto config);

    String callProceduresGenUUID();
}
