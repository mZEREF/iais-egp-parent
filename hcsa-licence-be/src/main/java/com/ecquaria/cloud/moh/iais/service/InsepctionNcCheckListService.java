package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectChklDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesPreInspectionNcItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.AdCheckListShowDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFDtosDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionFillCheckListDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.NcAnswerDto;

import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2019/12/5 9:46
 */
public interface InsepctionNcCheckListService {

    InspectionFillCheckListDto getNcCheckList(InspectionFillCheckListDto infillDto,AppPremisesPreInspectChklDto appPremDto,List<AppPremisesPreInspectionNcItemDto> itemDtoList, AppPremisesRecommendationDto appPremisesRecommendationDto);
    AppPremisesPreInspectChklDto getAppPremChklDtoByTaskId(String taskId,String configId);
    List<AppPremisesPreInspectionNcItemDto> getNcItemDtoByAppCorrId(String appCorrId);
    AppPremisesRecommendationDto getAppRecomDtoByAppCorrId(String appCorrId,String type);
    void submit(InspectionFillCheckListDto infillDto,AdCheckListShowDto showDto, InspectionFDtosDto serListDto,String appPremId);
    List<NcAnswerDto> getNcAnswerDtoList(String appPremCorrId);
    void updateTaskStatus(ApplicationDto applicationDto,String appPremCorrId);
    void getCommonDto(InspectionFillCheckListDto commonDto, AppPremisesPreInspectChklDto appPremPreCklDto);
    AdCheckListShowDto getAdhocCheckListDto(String appPremCorrId);
}
