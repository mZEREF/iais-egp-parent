package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.dto.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.dto.SystemParameterQueryDto;

public interface SystemParameterService {
    SearchResult<SystemParameterQueryDto> doQuery(SearchParam param);

    void saveSystemParameter(SystemParameterDto dto);

    SystemParameterDto getParameterByRowguid(String rowguid);
}
