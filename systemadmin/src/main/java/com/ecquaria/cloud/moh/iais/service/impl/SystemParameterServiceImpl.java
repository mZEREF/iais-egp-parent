package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.SystemParameterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SystemParameterServiceImpl implements SystemParameterService {

    @Override
    public SearchResult<SystemParameterQueryDto> doQuery(SearchParam param) {
        return  RestApiUtil.query("system-admin-service:8886/system-parameter/results", param);
    }

    @Override
    public void saveSystemParameter(SystemParameterDto dto) {
        dto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        RestApiUtil.save("system-admin-service:8886/system-parameter", dto);
    }

    @Override
    public SystemParameterDto getParameterByPid(String pid) {
        return IaisEGPHelper.getRecordByPrimaryKey("system-admin-service:8886/system-parameter", pid, SystemParameterDto.class);
    }
}
