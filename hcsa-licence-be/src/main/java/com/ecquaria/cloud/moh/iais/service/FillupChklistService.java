package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.CheckListDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;

import java.util.List;

/**
 * @author Guyin
 * @date 2019/11/22 14:09
 **/

public interface FillupChklistService {
    /**
     * list checklist config by SearchParam
     * backend method: listChecklistConfig
     * @param searchParam
     * @return
     */

    InspectionFillCheckListDto getInspectionFillCheckListDto(String taskId,String svcType);

    InspectionFillCheckListDto fillInspectionFillCheckListDto(InspectionFillCheckListDto dto);

    void saveDto(InspectionFillCheckListDto dto);
    ApplicationViewDto getAppViewDto(String taskId);
    TaskDto getTaskDtoById(String taskId);
    ChecklistConfigDto getcommonCheckListDto(String configType,String module);
    InspectionFillCheckListDto transferToInspectionCheckListDto(ChecklistConfigDto commonCheckListDto,String appPremCorrId);
    void merge(InspectionFillCheckListDto comDto,InspectionFillCheckListDto icDto);
    AdCheckListShowDto getAdhoc(String appremCorrId);
    void saveAdhocDto(AdCheckListShowDto showDto,String appPremId);
    void saveDraft(InspectionFillCheckListDto icDto,InspectionFillCheckListDto comDto,AdCheckListShowDto adDto);
    CheckListDraftDto getDraftByTaskId(String taskId,String svcType);
    List<AdhocDraftDto> getAdhocDraftByAppPremId(String appPremId);
    AdCheckListShowDto getAdhocDraftByappCorrId(String appremCorrId);
    void routingTask(TaskDto taskDto, String preInspecRemarks);
}
