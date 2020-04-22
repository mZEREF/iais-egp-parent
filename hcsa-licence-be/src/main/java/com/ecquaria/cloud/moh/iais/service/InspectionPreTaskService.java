package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.task.TaskDto;

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
      * @Param: null
      * @return: List<SelectOption>
      * @Descripation: get Processing Decision Option
      */
    List<SelectOption> getProcessDecOption();

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
      * @Param: taskDto, reMarks
      * @return: void
      * @Descripation: routing back
      */
    void routingBack(TaskDto taskDto, String reMarks);


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
      */
    List<SelectOption> getRfiCheckOption();

    /**
      * @author: shicheng
      * @Date 2020/4/22
      * @Param: taskDto, reMarks
      * @return: void
      * @Descripation: route back to ASO/PSO
      */
    void routingAsoPsoBack(TaskDto taskDto, String reMarks);
}
