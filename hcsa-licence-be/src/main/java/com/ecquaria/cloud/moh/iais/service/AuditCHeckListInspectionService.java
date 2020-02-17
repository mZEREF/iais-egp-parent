package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditAdhocCheckShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFDtosDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;

import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2020/2/14 8:55
 */
public interface AuditCHeckListInspectionService {
    List<InspectionFillCheckListDto> getInspectionFillCheckListDtoList(String taskId, String configType);
    InspectionFillCheckListDto fillInspectionFillCheckListDto(InspectionFillCheckListDto infillCheckListDto);
    AuditAdhocCheckShowDto getAdhocDraftByLicPremId(String appremCorrId);
    String getInspectionDate(String licPremId);
    List<String> getInspectiors(TaskDto taskDto);
    String getInspectionLeader(TaskDto taskDto);
    boolean isHaveNcOrBestPractice(InspectionFDtosDto serListDto, InspectionFillCheckListDto comDto, AuditAdhocCheckShowDto showDto);
    void submit(InspectionFillCheckListDto infillDto, AuditAdhocCheckShowDto showDto, InspectionFDtosDto serListDto, String premId);
}
