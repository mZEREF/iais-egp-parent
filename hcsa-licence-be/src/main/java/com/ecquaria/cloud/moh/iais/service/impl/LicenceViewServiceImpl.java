package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Override
    public AppSubmissionDto getAppSubmissionByAppId(String appId) {

        return  applicationClient.getAppSubmissionByAppId(appId).getEntity();
    }
}
