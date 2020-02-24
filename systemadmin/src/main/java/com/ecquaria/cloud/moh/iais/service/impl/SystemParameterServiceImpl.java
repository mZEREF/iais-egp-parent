package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.parameter.SystemParameterQueryDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.SystemParameterService;
import com.ecquaria.cloud.moh.iais.service.client.ConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SystemParameterServiceImpl implements SystemParameterService {

    @Autowired
    private SystemClient systemClient;

    @Autowired
    private ConfigClient configClient;

    @Override
    @SearchTrack(catalog = "systemAdmin",key = "queryMessage")
    public SearchResult<SystemParameterQueryDto> doQuery(SearchParam param) {
        return systemClient.doQuery(param).getEntity();
    }

    @Override
    public void saveSystemParameter(SystemParameterDto dto) {
        dto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        systemClient.saveSystemParameter(dto).getEntity();

        /*JSONObject jsonObject = new JSONObject();
        jsonObject.put(JsonKeyConstants.CONFIG_PROPERTIES_KEY, dto.getPropertiesKey());
        jsonObject.put(JsonKeyConstants.CONFIG_PROPERTIES_VALUE, dto.getValue());
        systemClient.saveSystemConfigProperties(jsonObject.toString());

        int statusCode = configClient.refreshConfig().getStatusCode();*/
    }

    @Override
    public SystemParameterDto getParameterByPid(String pid) {
       return systemClient.getParameterByRowguid(pid).getEntity();
    }
}
