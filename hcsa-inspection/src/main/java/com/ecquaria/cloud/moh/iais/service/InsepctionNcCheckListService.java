package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;

import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2019/12/5 9:46
 */
public interface InsepctionNcCheckListService {

    InspectionFillCheckListDto getNcCheckList(InspectionFillCheckListDto infillDto,AppPremisesPreInspectChklDto appPremDto,List<AppPremisesPreInspectionNcItemDto> itemDtoList);
    AppPremisesPreInspectChklDto getAppPremChklDtoByTaskId(String taskId,String configId);
    List<AppPremisesPreInspectionNcItemDto> getNcItemDtoByAppCorrId(String appCorrId);

}
