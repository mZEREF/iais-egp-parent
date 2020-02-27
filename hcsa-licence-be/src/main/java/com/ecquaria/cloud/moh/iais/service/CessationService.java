package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;

import java.util.List;

/**
 * @author weilu
 * @date 2020/2/26 16:27
 */
public interface CessationService {


    List<AppCessLicDto> getAppCessDtosByLicIds(List<String> licIds);
    void saveCessations(List<AppCessationDto> appCessationDtos);
    void updateCesation(List<AppCessationDto> appCessationDtos);
}
