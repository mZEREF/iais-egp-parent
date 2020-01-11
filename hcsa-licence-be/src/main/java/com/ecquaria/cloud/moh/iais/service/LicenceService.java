package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.EventBusLicenceGroupDtos;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.KeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import java.util.List;

/**
 * LicenceService
 *
 * @author suocheng
 * @date 11/29/2019
 */
public interface LicenceService {
    public List<ApplicationLicenceDto> getCanGenerateApplications(int day);
    public List<HcsaServiceDto> getHcsaServiceById(List<String> serviceIds);
    public String getHciCode(String serviceCode);
    public String getLicenceNo(String hciCode,String serviceCode,int yearLength);
    public String getGroupLicenceNo(String hscaCode,int yearLength);
    public AppPremisesRecommendationDto getTcu(String appPremCorrecId);
    public PremisesDto getLatestVersionPremisesByHciCode(String hciCode);
    public KeyPersonnelDto getLatestVersionKeyPersonnelByIdNoAndOrgId(String idNo,String orgId);
    public LicenceDto getLicenceDto(String licenceId);

    public List<LicenceGroupDto> createSuperLicDto(EventBusLicenceGroupDtos eventBusLicenceGroupDtos);
    public List<LicenceGroupDto> createFESuperLicDto(List<LicenceGroupDto> licenceGroupDtos);

}
