package com.ecquaria.cloud.moh.iais.service;


import com.ecquaria.cloud.moh.iais.common.dto.application.AppCessDto;

import java.util.List;

/**
 * @author weilu
 * @date 2020/2/7 13:16
 */
public interface CessationService {

    List<AppCessDto> getAppCessDtosByLicIds(List<String> licIds);
}
