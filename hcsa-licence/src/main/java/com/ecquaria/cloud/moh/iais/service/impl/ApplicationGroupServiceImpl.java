package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.ApplicationGroupService;
import lombok.extern.slf4j.Slf4j;
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
    @Override
    public ApplicationGroupDto getApplicationGroupDtoById(String appGroupId) {
        return RestApiUtil.getByPathParam(RestApiUrlConsts.APPLICATION_GROUP_GROUPID,appGroupId,ApplicationGroupDto.class);
    }

    @Override
    public ApplicationGroupDto updateApplicationGroup(ApplicationGroupDto applicationGroupDto) {
        return RestApiUtil.update(RestApiUrlConsts.IAIS_APPLICATION_GROUP_BE,applicationGroupDto,ApplicationGroupDto.class);
    }
}
