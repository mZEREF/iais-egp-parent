package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppIntranetDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdhocDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.CheckListDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFDtosDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;

import java.util.List;
import java.util.Map;

/**
 * @author Guyin
 * @date 2019/11/22 14:09
 **/

public interface FillupChklistService {
    /**
     * list checklist config by SearchParam
     * backend method: listChecklistConfig
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
    void saveDraft(InspectionFillCheckListDto comDto,AdCheckListShowDto adDto,InspectionFDtosDto serListDto,String refNo);
    CheckListDraftDto getDraftByTaskId(String taskId,String svcType);
    AdCheckListShowDto getAdhocDraftByappCorrId(String appremCorrId);
    void routingTask(TaskDto taskDto, String preInspecRemarks, LoginContext loginContext, boolean flag);
    List<InspectionFillCheckListDto> getInspectionFillCheckListDtoList(String taskId,String configType);

    List<InspectionFillCheckListDto> getInspectionFillCheckListDtoListForReview(String taskId, String service);

    void getTcuInfo(InspectionFDtosDto serListDto, String appPremCorrId);

    String getInspectionDate(String appPremCorrId);

    List<String> getInspectiors(TaskDto taskDto);

    String getInspectionLeader(TaskDto taskDto);

    void getRateOfCheckList(InspectionFDtosDto serListDto, AdCheckListShowDto adchklDto, InspectionFillCheckListDto commonDto);

    void getSvcName( InspectionFDtosDto serListDto);

    InspectionFillCheckListDto getMaxVersionComAppChklDraft(String appPremCorrId);

    List<InspectionFillCheckListDto> getAllVersionComAppChklDraft(String appPremCorrId);

    List<InspectionFDtosDto> geAllVersionServiceDraftList(String appPremCorrId);

    InspectionFDtosDto getMaxVersionServiceDraft(List<InspectionFDtosDto> fdtosdraft);

    List<InspectionFDtosDto> getOtherVersionfdtos(List<InspectionFDtosDto> fdtosdraft);

    List<AdCheckListShowDto> getOtherAdhocList(String appPremCorrId);

    String  getStringByRecomType(String appPremCorrId,String recomType);

    InspectionFDtosDto  getInspectionFDtosDto(String appPremCorrId,TaskDto taskDto,List<InspectionFillCheckListDto> cDtoList);
    AppIntranetDocDto getCopyAppPremisesSpecialDocDtoByAppPremisesSpecialDocDto(AppPremisesSpecialDocDto appPremisesSpecialDocDto);
    InspectionFDtosDto getInspectionFDtosDtoOnlyForChecklistLetter(String refNo);
    AppPremisesSpecialDocDto getAppPremisesSpecialDocDtoByRefNo(String RefNo);

    AdCheckListShowDto getAdCheckListShowDtoByAdCheckListShowDto(AdCheckListShowDto adCheckListShowDto, Map<String,String> orgUserDtos);

    List<TaskDto> getCurrTaskByRefNo(TaskDto taskDto);

    void saveOtherTasks(List<TaskDto> taskDtos,TaskDto taskDto);

    List<OrgUserDto> getOrgUserDtosByTaskDatos(List<TaskDto> taskDtos);

    Map<String,String> userIdNameMapByOrgUserDtos(List<OrgUserDto> orgUserDtos);

    InspectionFillCheckListDto getInspectionFillCheckListDtoByInspectionFillCheckListDto(InspectionFillCheckListDto inspectionFillCheckListDto, Map<String,String> orgUserDtos);

    String getOtherOffGropByInspectionFillCheckListDtos(List<InspectionFillCheckListDto> inspectionFillCheckListDtos);

    AdhocCheckListConifgDto getAdhocCheckListConifgDtoByCorId(String corId);

    boolean editAhocByAdhocCheckListConifgDtoAndOldAdhocCheckListConifgDto(AdhocCheckListConifgDto adhocCheckListConifgDto,AdhocCheckListConifgDto adhocCheckListConifgDtoOld);

    void sendModifiedChecklistEmailToAOStage(ApplicationViewDto appViewDto);

    void saveAdhocDto(AdhocCheckListConifgDto adhocCheckListConifgDto);
}
