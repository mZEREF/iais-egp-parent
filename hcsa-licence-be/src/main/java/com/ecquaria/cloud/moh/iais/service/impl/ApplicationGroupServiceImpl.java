package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationGroupService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import java.util.List;
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
        return RestApiUtil.getByPathParam(RestApiUrlConsts.APPLICATION_GROUP_GROUPID,appGroupId,ApplicationGroupDto.class);
    }

    @Override
    public ApplicationGroupDto updateApplicationGroup(ApplicationGroupDto applicationGroupDto) {
        return RestApiUtil.update(RestApiUrlConsts.IAIS_APPLICATION_GROUP_BE,applicationGroupDto,ApplicationGroupDto.class);
    }

    @Override
    public List<ApplicationGroupDto> updateApplicationGroups(List<ApplicationGroupDto> applicationGroupDtos) {

        return applicationClient.updateApplications(applicationGroupDtos).getEntity();
    }
}
