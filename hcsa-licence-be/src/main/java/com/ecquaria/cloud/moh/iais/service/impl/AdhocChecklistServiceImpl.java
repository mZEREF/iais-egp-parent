package com.ecquaria.cloud.moh.iais.service.impl;

/*
 *author: yichen
 *date time:12/10/2019 1:00 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcPremisesScopeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceSubTypeDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.service.AdhocChecklistService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskApplicationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;

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

    private String acquireModule(String appType, Function<String, String> t){
        return t.apply(appType);
    }

    private static String compareParameters(String appType){
        if (ApplicationConsts.APPLICATION_TYPE_NEW_APPLICATION.equals(appType)){
            return MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.NEW);
        }else if (ApplicationConsts.APPLICATION_TYPE_RENEWAL.equals(appType)){
            return MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.RENEWAL);
        }else if (ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE.equals(appType)){
            return MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.AMENDMENT);
        }else if (ApplicationConsts.APPLICATION_TYPE_REINSTATEMENT.equals(appType)){
            return MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.REINSTATEMENT);
        }else if (ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(appType)){
            return MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.AUDIT);
        }
        return MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.NEW);
    }

    @Override
    public List<ChecklistConfigDto> getInspectionChecklist(ApplicationDto application) {
        String appId = application.getId();
        List<AppPremisesCorrelationDto> correlation = taskApplicationClient.getAppPremisesCorrelationsByAppId(appId).getEntity();

        String svcId = application.getServiceId();

        HcsaServiceDto hcsaSvcEntity = hcsaConfigClient.getHcsaServiceDtoByServiceId(svcId).getEntity();
        String svcCode = hcsaSvcEntity.getSvcCode();

        String chklModule = acquireModule(application.getApplicationType(), AdhocChecklistServiceImpl::compareParameters);

        String type = MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.INSPECTION);

        List<ChecklistConfigDto> inspChecklist = IaisCommonUtils.genNewArrayList();
        ChecklistConfigDto commonConfig = hcsaChklClient.getMaxVersionCommonConfig().getEntity();
        if (commonConfig != null){
            log.info(StringUtil.changeForLog("inspection checklist for common info: " + commonConfig.toString()));
            inspChecklist.add(commonConfig);
        }

        ChecklistConfigDto svcConfig = hcsaChklClient.getMaxVersionConfigByParams(svcCode, type, chklModule).getEntity();
        if (svcConfig != null){
            inspChecklist.add(svcConfig);
        }

        correlation.forEach(corre -> {
            String correId = corre.getId();
            List<AppSvcPremisesScopeDto> premScope = applicationClient.getAppSvcPremisesScopeListByCorreId(correId).getEntity();

            if(!IaisCommonUtils.isEmpty(premScope)){
                premScope.stream().filter(scope -> scope.isSubsumedType() == false)
                        .forEach(scope -> {
                            String subTypeId = scope.getScopeName();
                            HcsaServiceSubTypeDto subType = hcsaConfigClient.getHcsaServiceSubTypeById(subTypeId).getEntity();
                            String subTypeName = subType.getSubtypeName();
                            ChecklistConfigDto subTypeConfig = hcsaChklClient.getMaxVersionConfigByParams(svcCode, type, chklModule, subTypeName).getEntity();
                            if (subTypeConfig != null){
                                inspChecklist.add(subTypeConfig);
                            }
                        });
            }

        });
        return inspChecklist;
    }

    @Override
    public void saveAdhocChecklist(AdhocCheckListConifgDto adhocConfig) {
        applicationClient.saveAdhocChecklist(adhocConfig);
    }
}
