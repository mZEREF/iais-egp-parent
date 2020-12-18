package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.QueryHelperResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;

public interface QueryHandlerFeService {
    QueryHelperResultDto getQueryHelperResultDtoList(String querySql, String moduleName);

    LicenseeDto getLicenseeByUserAccountInfo(String userAccountString);
}
