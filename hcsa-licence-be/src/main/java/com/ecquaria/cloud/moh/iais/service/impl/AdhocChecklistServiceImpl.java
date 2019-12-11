package com.ecquaria.cloud.moh.iais.service.impl;

/*
 *author: yichen
 *date time:12/10/2019 1:00 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.service.AdhocChecklistService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AdhocChecklistServiceImpl implements AdhocChecklistService {

    @Autowired
    private HcsaConfigClient hcsaConfigClient;

    @Autowired
    private ApplicationClient applicationClient;

    @Autowired
    private TaskApplicationClient taskApplicationClient;

    @Autowired
    private HcsaChklClient hcsaChklClient;

    @Override
    public List<ChecklistConfigDto> getInspectionChecklist(ApplicationDto application) {
        String appId = application.getId();
        List<AppPremisesCorrelationDto> correlation = taskApplicationClient.getAppPremisesCorrelationsByAppId(appId).getEntity();

        String svcId = application.getServiceId();
        //String appStatus = application.getStatus();

        HcsaServiceDto hcsaSvcEntity = hcsaConfigClient.getHcsaServiceDtoByServiceId(svcId).getEntity();
        String svcCode = hcsaSvcEntity.getSvcCode();
        String chklModule = application.getApplicationType();
        String type = "Inspection";

        List<ChecklistConfigDto> inspChecklist = new ArrayList<>();
        ChecklistConfigDto commonConfig = hcsaChklClient.getMaxVersionCommonConfigByParams(type, chklModule).getEntity();

        log.info("inspection checklist for common info: " + commonConfig.toString());
        if (commonConfig != null){
            inspChecklist.add(commonConfig);
        }

        correlation.stream().forEach(corre -> {
            String correId = corre.getId();
            List<AppSvcPremisesScopeDto> premScope = applicationClient.getAppSvcPremisesScopeListByCorreId(correId).getEntity();
            premScope.stream().filter(AppSvcPremisesScopeDto::isSubsumedType)
                    .forEach(scope -> {
                        String subTypeId = scope.getScopeName();
                        HcsaServiceSubTypeDto subType = hcsaConfigClient.getHcsaServiceSubTypeById(subTypeId).getEntity();
                        String subTypeName = subType.getSubtypeName();
                        ChecklistConfigDto serviceConfig = hcsaChklClient.getMaxVersionConfigByParams(svcCode, type, chklModule, subTypeName).getEntity();
                        if (serviceConfig != null){
                            inspChecklist.add(serviceConfig);
                        }
                    });
        });
        return inspChecklist;
    }
}
