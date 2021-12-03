package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;

import java.util.List;

/**
 * @Author: jiahao
 * @Date: 2020/1/14 16:44
 */
public interface HcsaRiskSupportBeService{
    HcsaLastInspectionDto getLastSecRiskSocre(String licId,String svcCode);
    List<PreOrPostInspectionResultDto> preOrPostInspection(List<RecommendInspectionDto> recommendInspectionDtoList);
    List<RiskResultDto> getRiskResult(List<RiskAcceptiionDto> riskAcceptiionDtoList);
    List<AutoRenewDto> isAutoRenew(List<String> licNo,boolean isRenew);
    void feCreateRiskData( HcsaRiskFeSupportDto supportDto);
    void sysnRiskSaveEic(int httpStatus,HcsaRiskFeSupportDto supportDto);
    boolean versionSameForRisk(String version,String dbVersion);
    boolean versionSameForRisk(Integer version,Integer dbVersion);
    List<HcsaServiceDto>  getNameSortHcsaServiceDtos();
}
