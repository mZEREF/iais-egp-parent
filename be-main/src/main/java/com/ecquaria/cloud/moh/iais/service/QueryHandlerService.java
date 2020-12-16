package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.QueryHelperResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;

import java.util.List;

public interface QueryHandlerService {
    QueryHelperResultDto getQueryHelperResultDtoList(String querySql,String modulName);

    LicenseeDto getLicenseeByUserAccountInfo(String userAccountString);
}
