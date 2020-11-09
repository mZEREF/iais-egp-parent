package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.QueryHelperResultDto;

public interface QueryHandlerFeService {
    QueryHelperResultDto getQueryHelperResultDtoList(String querySql, String moduleName);
}
