package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.application.ApplicationViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.service.BelicationViewMainService;
import com.ecquaria.cloud.moh.iais.service.InspectionMainAssignTaskService;
import com.ecquaria.cloud.moh.iais.service.TaskService;
import com.ecquaria.cloud.moh.iais.service.client.BeInspectionStatusClient;
import com.ecquaria.cloud.moh.iais.service.client.BePremisesRoutingHistoryClient;
import com.ecquaria.cloud.moh.iais.service.client.BelicationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigMainClient;
import com.ecquaria.cloud.moh.iais.service.client.InspectionTaskMainClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationMainClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Shicheng
 * @date 2019/11/22 10:19
 **/
@Service
@Slf4j
public class InspectionMainAssignTaskServiceImpl implements InspectionMainAssignTaskService {
    @Autowired
    private InspectionTaskMainClient inspectionTaskClient;

    @Autowired
    private HcsaConfigMainClient hcsaConfigClient;

    @Autowired
    private OrganizationMainClient organizationClient;

    @Autowired
    private TaskService taskService;

    @Autowired
    private BelicationClient belicationClient;

    @Autowired
    private BeInspectionStatusClient beInspectionStatusClient;

    @Autowired
    private BePremisesRoutingHistoryClient bePremisesRoutingHistoryClient;

    @Autowired
    private BelicationViewMainService belicationViewService;

    @Override
    public ApplicationViewDto searchByAppNo(String applicationNo) {
        return belicationClient.getAppViewByCorrelationId(applicationNo).getEntity();
    }

    @Override
    public HcsaServiceDto getHcsaServiceDtoByServiceId(String serviceId) {
        return hcsaConfigClient.getHcsaServiceDtoByServiceId(serviceId).getEntity();
    }

    @Override
    public String taskRead(String taskId){
        return organizationClient.taskRead(taskId).getEntity();
    }
}
