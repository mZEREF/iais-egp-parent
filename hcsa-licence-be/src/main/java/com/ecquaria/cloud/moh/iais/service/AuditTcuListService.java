package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.inspection.AuditTaskDataFillterDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesRecommendationDto;

import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2020/2/20 15:09
 */
public interface AuditTcuListService {
    List<AuditTaskDataFillterDto> getAuditTcuList();
    void saveAuditTcuList(List<LicPremisesRecommendationDto> licPremisesRecommendationDtos);
}
