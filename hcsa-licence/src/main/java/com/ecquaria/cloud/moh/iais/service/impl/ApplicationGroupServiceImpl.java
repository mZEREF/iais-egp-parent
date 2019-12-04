package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationGroupService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ApplicationGroupServiceImpl
 *
 * @author suocheng
 * @date 11/28/2019
 */
@Service
@Slf4j
public class ApplicationGroupServiceImpl implements ApplicationGroupService {
    @Autowired
    private ApplicationClient applicationClient;
    @Override
    public ApplicationGroupDto getApplicationGroupDtoById(String appGroupId) {

        return   applicationClient.getAppById(appGroupId).getEntity();
    }

    @Override
    public ApplicationGroupDto updateApplicationGroup(ApplicationGroupDto applicationGroupDto) {

        return   applicationClient.updateApplication(applicationGroupDto).getEntity();
    }
}
