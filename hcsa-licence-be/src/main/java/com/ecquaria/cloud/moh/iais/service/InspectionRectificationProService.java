package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremPreInspectionNcDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.appointment.ApptNonWorkingDateDto;
import com.ecquaria.cloud.moh.iais.common.dto.filerepo.FileRepoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRoutingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspEmailFieldDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionPreTaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;

import freemarker.template.TemplateException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2019/12/18 14:26
 **/
public interface InspectionRectificationProService {
    /**
     * @author: shicheng
     * @Date 2019/12/18
     * @Param: appNo, stageId
     * @return: AppPremisesRoutingHistoryDto
     * @Descripation: get ppPremisesRoutingHistoryDto By appNo
     */
    AppPremisesRoutingHistoryDto getAppHistoryByTask(String appNo, String stageId);

    /**
     * @author: shicheng
     * @Date 2019/12/18
     * @Param: null
     * @return:
     * @Descripation:
     */
    List<SelectOption> getProcessRecDecOption();

    /**
     * @author: shicheng
     * @Date 2019/12/19
     * @Param: taskDto, inspectionPreTaskDto, applicationViewDto, loginContext
     * @return: void
     * @Descripation: routing Task To Report
     */
    void routingTaskToReport(TaskDto taskDto, InspectionPreTaskDto inspectionPreTaskDto, ApplicationViewDto applicationViewDto, LoginContext loginContext) throws IOException, TemplateException;

    /**
     * @author: shicheng
     * @Date 2020/2/24
     * @Param: appPremCorrId
     * @return: List<ChecklistItemDto>
     * @Descripation: getQuesAndClause
     */
    List<ChecklistItemDto> getQuesAndClause(String appPremCorrId);

    /**
     * @author: shicheng
     * @Date 2020/2/25
     * @Param: appPremPreInspectionNcDocDtos
     * @return: List<FileRepoDto>
     * @Descripation: get File By appPremPreInspectionNcDocDtos
     */
    List<FileRepoDto> getFileByItemId(List<AppPremPreInspectionNcDocDto> appPremPreInspectionNcDocDtos);

    /**
     * @author: shicheng
     * @Date 2020/2/25
     * @Param: id
     * @return: List<AppPremPreInspectionNcDocDto>
     * @Descripation: get AppNcDocList
     */
    List<AppPremPreInspectionNcDocDto> getAppNcDocList(String id);

    /**
     * @author: shicheng
     * @Date 2020/2/26
     * @Param: fileRepoId
     * @return: byte[]
     * @Descripation: downloadFile
     */
    byte[] downloadFile(String fileRepoId);

    /**
     * @author: shicheng
     * @Date 2020/3/1
     * @Param: refNo
     * @return: AppPremPreInspectionNcDto
     * @Descripation: getAppPremPreInspectionNcDtoByCorrId
     */
    AppPremPreInspectionNcDto getAppPremPreInspectionNcDtoByCorrId(String refNo);

    /**
     * @author: shicheng
     * @Date 2020/3/1
     * @Param: ncId
     * @return: Map<String, AppPremisesPreInspectionNcItemDto>
     * @Descripation: getNcItemDtoMap
     */
    Map<String, AppPremisesPreInspectionNcItemDto> getNcItemDtoMap(String id);

    /**
     * @author: shicheng
     * @Date 2020/3/28
     * @Param: workGroupId
     * @return: List<String>
     * @Descripation: getInspectorLeadsByWorkGroupId
     */
    List<String> getInspectorLeadsByWorkGroupId(String workGroupId);

    /**
     * @author: shicheng
     * @Date 2020/3/28
     * @Param: refNo
     * @return: int
     * @Descripation: get How Much Nc By AppPremCorrId
     */
    int getHowMuchNcByAppPremCorrId(String refNo);

    /**
     * @author: shicheng
     * @Date 2020/4/2
     * @Param: fileRepoId
     * @return: FileRepoDto
     * @Descripation: getFileReportById
     */
    FileRepoDto getFileReportById(String fileRepoId);

    /**
     * @author: shicheng
     * @Date 2020/6/3
     * @Param: adhocItemId
     * @return: AdhocChecklistItemDto
     * @Descripation: getAdhocChecklistItemById
     */
    AdhocChecklistItemDto getAdhocChecklistItemById(String adhocItemId);

    /**
     * @author: shicheng
     * @Date 2020/6/3
     * @Param: itemId
     * @return: ChecklistItemDto
     * @Descripation: getChklItemById
     */
    ChecklistItemDto getChklItemById(String itemId);

    FileRepoDto getCheckListFileRealName(FileRepoDto fileRepoDto, String refNo, String status, String checkListFileType);

    /**
     * @author: shicheng
     * @Date 2021/3/13
     * @Param: appNo
     * @return: List<ApptNonWorkingDateDto>
     * @Descripation: getApptNonWorkingDateByAppNo
     */
    List<ApptNonWorkingDateDto> getApptNonWorkingDateByAppNo(String appNo);

    /**
     * @author: shicheng
     * @Date 2021/8/25
     * @Param: inspEmailFieldDtos
     * @return: List<InspEmailFieldDto>
     * @Descripation: sortInspEmailFieldDtoByCategory
     */
    List<InspEmailFieldDto> sortInspEmailFieldDtoByCategory(List<InspEmailFieldDto> inspEmailFieldDtos);

    /**
      * @author: shicheng
      * @Date 2021/9/22
      * @Param: appSvcVehicleDtos, vehicleName
      * @return: String
      * @Descripation: getVehicleShowName
      */
    String getVehicleShowName(String vehicleName, List<AppSvcVehicleDto> appSvcVehicleDtos);
}
