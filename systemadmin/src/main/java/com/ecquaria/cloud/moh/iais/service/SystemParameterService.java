package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterQueryDto;

public interface SystemParameterService {
    SearchResult<SystemParameterQueryDto> doQuery(SearchParam param);

    void saveSystemParameter(SystemParameterDto dto);

    SystemParameterDto getParameterByPid(String rowguid);

    OrgUserDto retrieveOrgUserAccountById(String userId);

    void initPropertyKeyOffset();

    boolean getPropertyOffsetStatus(String propertiesKey);

    void setPropertyOffset(String propertiesKey, boolean flag);

}
