package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.service.CessationService;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import org.springframework.stereotype.Service;

/**
 * @author weilu
 * @date 2020/2/7 13:17
 */
@Service
public class CessationServiceImpl implements CessationService {

    private CessationClient cessationClient;
    @Override
    public AppInsRepDto getAppData(String appNo) {
        AppInsRepDto appInsRepDto = cessationClient.getAppCessationDto(appNo).getEntity();
        return appInsRepDto;
    }
}
