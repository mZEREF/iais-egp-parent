package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;

import java.util.List;

/**
 * @author weilu
 * @date 2020/2/7 13:16
 */
public interface CessationService {

    List<AppCessLicDto> getAppCessDtosByLicIds(List<String> licIds);
    List<AppCessLicDto> getOldCessationByIds(List<String> licIds);


    void saveCessations(List<AppCessationDto> appCessationDtos);
    void updateCesation(List<AppCessationDto> appCessationDtos);

    void saveWithdrawn(WithdrawnDto withdrawnDto);
    AppPremisesCorrelationDto getAppPreCorDto(String appId);

}
