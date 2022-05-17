package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.PremCheckItem;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionHistoryShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionPreTaskDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;
import com.ecquaria.cloud.moh.iais.dto.LoginContext;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @author Shicheng
 * @date 2019/12/9 9:58
 **/
public interface InspectionPreTaskService {

    /**
      * @author: shicheng
      * @Date 2019/12/9
      * @Param: taskDto
      * @return: String
      * @Descripation: get Application Status By Task Id
      */
    ApplicationDto getAppStatusByTaskId(TaskDto taskDto);

    /**
      * @author: shicheng
      * @Date 2019/12/9
      * @Param: applicationDto
      * @return: List<SelectOption>
      * @Descripation: get Processing Decision Option
      */
    List<SelectOption> getProcessDecOption(ApplicationDto applicationDto);

    /**
      * @author: shicheng
      * @Date 2019/12/12
      * @Param: taskDto, preInspecRemarks
      * @return: void
      * @Descripation: routing Task
      */
    void routingTask(TaskDto taskDto, String preInspecRemarks, List<ChecklistConfigDto> inspectionChecklist);

    /**
      * @author: shicheng
      * @Date 2020/1/7
      * @Param: taskDto, inspectionPreTaskDto, loginContext, premCheckItems
      * @return: void
      * @Descripation: routing back
      */
    void routingBack(TaskDto taskDto, InspectionPreTaskDto inspectionPreTaskDto, LoginContext loginContext, List<PremCheckItem> premCheckItems, ApplicationViewDto applicationViewDto) throws IOException, TemplateException;


    /**
      * @author: shicheng
      * @Date 2020/1/14
      * @Param: refNo
      * @return: Map<InspectionFillCheckListDto, List<InspectionFillCheckListDto>>
      * @Descripation: get Self-CheckList By CorrId
      */
    Map<InspectionFillCheckListDto, List<InspectionFillCheckListDto>> getSelfCheckListByCorrId(String refNo);

    /**
      * @author: shicheng
      * @Date 2020/4/9
      * @Param: originLicenceId
      * @return: LicenceDto
      * @Descripation: getLicenceDtoByLicenceId
      */
    LicenceDto getLicenceDtoByLicenceId(String originLicenceId);

    /**
      * @author: shicheng
      * @Date 2020/4/22
      * @Param: null
      * @return: List<SelectOption>
      * @Descripation: get Application checkbox and Self-Checklist checkbox
     * @param applicationType
      */
    List<SelectOption> getRfiCheckOption(String applicationType);

    /**
      * @author: shicheng
      * @Date 2020/4/22
      * @Param: taskDto, inspectionPreTaskDto, loginContext
      * @return: void
      * @Descripation: route back to ASO/PSO
      */
    void routingAsoPsoBack(TaskDto taskDto, InspectionPreTaskDto inspectionPreTaskDto, LoginContext loginContext);

    /**
      * @author: shicheng
      * @Date 2020/5/7
      * @Param: originLicenceId
      * @return: List<InspectionHistoryShowDto>
      * @Descripation: get Inspection History(past two) By licenceId
      */
    List<InspectionHistoryShowDto> getInspectionHistory(String originLicenceId, String appId);

    /**
      * @author: shicheng
      * @Date 2020/5/13
      * @Param: applicationViewDto, inspectionPreTaskDto
      * @return: InspectionPreTaskDto
      * @Descripation: get history stage with appNo
      */
    InspectionPreTaskDto getPreInspRbOption(ApplicationViewDto applicationViewDto, InspectionPreTaskDto inspectionPreTaskDto);

    int preInspRfiTogether(ApplicationDto applicationDto);

    /**
      * @author: shicheng
      * @Date 2021/3/12
      * @Param: refNo
      * @return: void
      * @Descripation: selfAssMtPdfReport
      */
    void selfAssMtPdfReport(String refNo);
}
