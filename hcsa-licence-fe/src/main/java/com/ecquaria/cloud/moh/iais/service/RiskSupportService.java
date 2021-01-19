package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.AutoRenewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.HcsaLastInspectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RecommendInspectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;

import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2020/1/9 11:00
 */
public interface RiskSupportService {
    HcsaLastInspectionDto getLastSecRiskSocre(String licId, String svcCode);
    List<PreOrPostInspectionResultDto> preOrPostInspection(List<RecommendInspectionDto> recommendInspectionDtoList);
    List<RiskResultDto> getRiskResult(List<RiskAcceptiionDto> riskAcceptiionDtoList);
    List<AutoRenewDto> isAutoRenew(List<String> licNo, boolean isRenew);
}
