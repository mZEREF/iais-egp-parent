package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.organization.WorkingGroupDto;

import com.ecquaria.cloud.moh.iais.service.WorkingGroupService;

import com.ecquaria.cloud.moh.iais.service.client.OrganizationClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * WorkingGroupServiceImpl
 *
 * @author suocheng
 * @date 12/10/2019
 */
@Service
public class WorkingGroupServiceImpl implements WorkingGroupService {

    @Autowired
    private OrganizationClient organizationClient;

    @Override
    public WorkingGroupDto createWorkGroup(WorkingGroupDto workingGroupDto) {

        return  organizationClient.createWorkGroup(workingGroupDto).getEntity();
    }
}
