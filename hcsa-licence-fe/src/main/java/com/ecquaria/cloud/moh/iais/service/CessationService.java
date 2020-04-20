package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;

import java.util.List;

/**
 * @author weilu
 * @date 2020/2/7 13:16
 */
public interface CessationService {


    List<String> getActiveLicence(List<String> licIds);

    List<AppCessLicDto> getAppCessDtosByLicIds(List<String> licIds);

    List<AppCessLicDto> getOldCessationByIds(List<String> licIds);

    void updateLicenceFe(List<String> licNos);

    List<String> saveCessations(List<AppCessationDto> appCessationDtos);

    void updateCesation(List<AppCessationDto> appCessationDtos);

    AppPremisesCorrelationDto getAppPreCorDto(String appId);

    List<String> listHciName();

    Boolean getlicIdToCessation(List<String> licIds);

}
