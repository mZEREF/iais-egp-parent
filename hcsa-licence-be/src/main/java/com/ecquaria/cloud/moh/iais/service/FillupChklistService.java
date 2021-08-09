package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremisesSpecialDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppIntranetDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.*;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;

import java.util.List;

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
    List<InspectionFillCheckListDto> getInspectionFillCheckListDtoList(String taskId,String configType,boolean needVehicleSeparation);

    List<InspectionFillCheckListDto> getInspectionFillCheckListDtoListForReview(String taskId, String service,boolean needVehicleSeparation);

    void getTcuInfo(InspectionFDtosDto serListDto, String appPremCorrId);

    String getInspectionDate(String appPremCorrId);

    List<String> getInspectiors(TaskDto taskDto);

    String getInspectionLeader(TaskDto taskDto);

    void getRateOfCheckList(InspectionFDtosDto serListDto, AdCheckListShowDto adchklDto, InspectionFillCheckListDto commonDto);

    void getSvcName( InspectionFDtosDto serListDto);

    String  getStringByRecomType(String appPremCorrId,String recomType);

    InspectionFDtosDto  getInspectionFDtosDto(String appPremCorrId,TaskDto taskDto,List<InspectionFillCheckListDto> cDtoList);
    AppIntranetDocDto getCopyAppPremisesSpecialDocDtoByAppPremisesSpecialDocDto(AppPremisesSpecialDocDto appPremisesSpecialDocDto);
    InspectionFDtosDto getInspectionFDtosDtoOnlyForChecklistLetter(String refNo);
    AppPremisesSpecialDocDto getAppPremisesSpecialDocDtoByRefNo(String RefNo);

    AdCheckListShowDto getAdCheckListShowDtoByAdCheckListShowDto(AdCheckListShowDto adCheckListShowDto,  List<OrgUserDto>  orgUserDtos);

    List<TaskDto> getCurrTaskByRefNo(TaskDto taskDto);

    void saveOtherTasks(List<TaskDto> taskDtos,TaskDto taskDto);

    List<OrgUserDto> getOrgUserDtosByTaskDatos(List<TaskDto> taskDtos);

    InspectionFillCheckListDto getInspectionFillCheckListDtoByInspectionFillCheckListDto(InspectionFillCheckListDto inspectionFillCheckListDto,  List<OrgUserDto>  orgUserDtos);

    String getOtherOffGropByInspectionFillCheckListDtos(List<InspectionFillCheckListDto> inspectionFillCheckListDtos);

    AdhocCheckListConifgDto getAdhocCheckListConifgDtoByCorId(String corId);

    boolean editAhocByAdhocCheckListConifgDtoAndOldAdhocCheckListConifgDto(AdhocCheckListConifgDto adhocCheckListConifgDto,AdhocCheckListConifgDto adhocCheckListConifgDtoOld);

    void sendModifiedChecklistEmailToAOStage(ApplicationViewDto appViewDto);

    void saveAdhocDto(AdhocCheckListConifgDto adhocCheckListConifgDto);

    void changeDataForNc( List<InspectionFillCheckListDto> inspectionFillCheckListDtos,String refNo);

    void setInspectionCheckQuestionDtoByAnswerForDifDtosAndDeconflict(InspectionCheckQuestionDto inspectionCheckQuestionDto, List<AnswerForDifDto> answerForDifDtos,String deconflict);

    void setAdhocNcCheckItemDtoByAnswerForDifDtosAndDeconflict(AdhocNcCheckItemDto  adhocNcCheckItemDto, List<AnswerForDifDto> answerForDifDtos,String deconflict);

    String setRemarksAndStartTimeAndEndTimeForCheckList(InspectionFDtosDto serListDto,InspectionFillCheckListDto commonDto,String refNo);

    boolean isBeforeFinishCheckList(String refNo);

    void getRateOfSpecCheckList( List<InspectionSpecServiceDto> inspectionSpecServiceDtos, InspectionFillCheckListDto commonDto,InspectionFDtosDto serListDto, AdCheckListShowDto adchklDto);

    AdCheckListShowDto getSpecAhocData(AdCheckListShowDto adCheckListShowDto,String identify,boolean beforeFinishList,List<OrgUserDto>  orgUserDtos);

    AppPremisesPreInspectChklDto getAppPremChklDtoByCorrIdAndVehicleName(String CorrId, String configId, String vehicleName);

    boolean checklistNeedVehicleSeparation(ApplicationViewDto appViewDto);

    InspectionSpecServiceDto getOriginalInspectionSpecServiceDtoByTaskId(boolean needVehicleSeparation,boolean beforeFinishList,String taskId);

    AdCheckListShowDto getNoVehicleAdhoc(AdCheckListShowDto adCheckListShowDto);

    /**
      * @author: shicheng
      * @Date 2021/8/9
      * @Param: appPremCorrId
      * @return: String
      * @Descripation: getObservationByAppPremCorrId
      */
    String getObservationByAppPremCorrId(String appPremCorrId);
}
