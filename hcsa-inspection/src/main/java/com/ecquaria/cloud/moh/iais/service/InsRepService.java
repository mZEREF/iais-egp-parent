package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.InspectionReportDto;

/**
 * @author weilu
 * date 2019/11/20 16:10
 */
public interface InsRepService {

    InspectionReportDto getInsRepDto (String appNo);
    String saveRecommendation(AppPremisesRecommendationDto appPremisesRecommendationDto);


}
