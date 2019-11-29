package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;

/**
 * ApplicationGroupService
 *
 * @author suocheng
 * @date 11/28/2019
 */
public interface ApplicationGroupService {
    public ApplicationGroupDto getApplicationGroupDtoById(String appGroupId);
    public ApplicationGroupDto updateApplicationGroup(ApplicationGroupDto applicationGroupDto);
}
