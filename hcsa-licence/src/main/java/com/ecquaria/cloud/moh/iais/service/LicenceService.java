package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.SuperLicDto;
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
    public AppPremisesRecommendationDto getTcu(String appPremCorrecId);

    public List<SuperLicDto> createSuperLicDto(List<SuperLicDto> superLicDtos);


}
