package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * LicenceViewServiceImpl
 *
 * @author suocheng
 * @date 12/16/2019
 */
@Service
@Slf4j
public class LicenceViewServiceImpl implements LicenceViewService {

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Override
    public AppSubmissionDto getAppSubmissionByAppId(String appId) {

        return  applicationClient.getAppSubmissionByAppId(appId).getEntity();
    }

    @Override
    public AppEditSelectDto saveAppEditSelect(AppEditSelectDto appEditSelectDto) {
        return applicationClient.createAppEditSelectDto(appEditSelectDto).getEntity();
    }

    @Override
    public AppEditSelectDto saveAppEditSelectToFe(AppEditSelectDto appEditSelectDto) {
        return beEicGatewayClient.callEicWithTrack(appEditSelectDto, beEicGatewayClient::createAppEditSelectDto,
                "createAppEditSelectDto").getEntity();
    }

}
