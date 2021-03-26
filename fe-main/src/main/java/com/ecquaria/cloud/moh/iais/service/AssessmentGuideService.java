package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationSubDraftDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;

import java.util.List;
import java.util.Map;


public interface AssessmentGuideService {

    List<HcsaServiceDto> getServicesInActive();
    List<HcsaServiceCorrelationDto> getCorrelation();
    List<AppAlignLicQueryDto> getAppAlignLicQueryDto(String licenseeId, List<String> svcNames);
    List<HcsaServiceDto> getHcsaServiceDtosById(List<String> ids);
    String selectDarft(Map<String, Object> map);
    public SearchResult<MenuLicenceDto> getMenuLicence(SearchParam searchParam);
    Boolean isNewLicensee(String licenseeId);
    List<String> getHciFromPendAppAndLic(String licenseeId, List<HcsaServiceDto> hcsaServiceDtos);
    void deleteDraftNUmber(List<String> draftNumbers);
    HcsaServiceDto getServiceDtoById(String id);
    List<HcsaServiceCorrelationDto> getActiveSvcCorrelation();
    List<MenuLicenceDto> setPremAdditionalInfo(List<MenuLicenceDto> menuLicenceDtos);
    List<ApplicationSubDraftDto> getDraftListBySvcCodeAndStatus(List<String> svcCodeList, String status, String licenseeId, String appType);
}
