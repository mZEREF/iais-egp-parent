package com.ecquaria.cloud.moh.iais.service.impl;

/*
 *author: yichen
 *date time:12/10/2019 1:00 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.checklist.HcsaChecklistConstants;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocCheckListConifgDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AdhocChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSpecialisedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSubSvcRelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MasterCodeUtil;
import com.ecquaria.cloud.moh.iais.helper.SqlHelper;
import com.ecquaria.cloud.moh.iais.service.AdhocChecklistService;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.ConfigCommClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.TaskApplicationClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    private BeEicGatewayClient gatewayClient;

    @Autowired
    private HcsaChklClient hcsaChklClient;

    @Autowired
    private AppCommService appCommService;

    @Autowired
    private ConfigCommClient configCommClient;

    private String acquireParameter(String appType, Function<String, String> t){
        return t.apply(appType);
    }

    public static String compareType(String appType){
        if (ApplicationConsts.APPLICATION_TYPE_CREATE_AUDIT_TASK.equals(appType)){
            return MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.AUDIT_INSPECTION);
        } else {
            return MasterCodeUtil.getCodeDesc(HcsaChecklistConstants.INSPECTION);
        }
    }


    public static String compareModule(String appType){
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
    public List<ChecklistConfigDto> getInspectionChecklist(ApplicationDto application, boolean needVehicle) {
        String appId = application.getId();

        List<AppPremisesCorrelationDto> correlation = taskApplicationClient.getAppPremisesCorrelationsByAppId(appId).getEntity();

        String svcId = application.getServiceId();

        HcsaServiceDto hcsaSvcEntity = HcsaServiceCacheHelper.getServiceById(svcId);
        String svcCode = hcsaSvcEntity.getSvcCode();

        String chklModule = acquireParameter(application.getApplicationType(), AdhocChecklistServiceImpl::compareModule);
        String type = acquireParameter(application.getApplicationType(), AdhocChecklistServiceImpl::compareType);

        log.info(StringUtil.changeForLog("inspection pick up service config service code ====>>>>" + svcCode));
        log.info(StringUtil.changeForLog("get checklist for pre , module " + chklModule));
        log.info(StringUtil.changeForLog("get checklist for pre , type " + type));

        ChecklistConfigDto baseSvcConfig = hcsaChklClient.getMaxVersionServiceConfigByParams(svcCode, type, chklModule,
                "", "").getEntity();

        List<ChecklistConfigDto> specSvcChecklistConfig = getSpecSvcChecklistConfigDtos(type, chklModule, correlation);
        ChecklistConfigDto vehicleConfig = null;
        if (needVehicle) {
            vehicleConfig = hcsaChklClient.getMaxVersionInspectionEntityConfig(svcCode, type, chklModule,
                    HcsaChecklistConstants.INSPECTION_ENTITY_VEHICLE).getEntity();
        }
        List<ChecklistConfigDto> inspChecklist = IaisCommonUtils.genNewArrayList();
        boolean oneTime = false;
        for (AppPremisesCorrelationDto corr : correlation){
            String corrId = corr.getId();
            String premId = corr.getAppGrpPremId();

            if (!oneTime){
                String hciCode = "";
                AppGrpPremisesDto appGrpPremisesDto = appCommService.getAppGrpPremisesById(premId);
                if (appGrpPremisesDto != null){
                    hciCode  = appGrpPremisesDto.getHciCode();
                }

                ChecklistConfigDto commonConfig = hcsaChklClient.getMaxVersionCommonConfig().getEntity();
                if (commonConfig != null){
                    log.info(StringUtil.changeForLog("inspection checklist for common info: " + commonConfig));
                    inspChecklist.add(commonConfig);
                }
                // issue 65522
                ChecklistConfigDto svcConfig = getChecklistConfigDtoWithHciCode(svcCode, chklModule, type, baseSvcConfig, hciCode);

                log.info(StringUtil.changeForLog("inspection pick up service config hci code ====>>>>" + hciCode));
                log.info(StringUtil.changeForLog("inspection pick up service config ====>>>>" + svcConfig));

                if (svcConfig != null){
                    inspChecklist.add(svcConfig);
                }
// spec check list
                if (IaisCommonUtils.isNotEmpty(specSvcChecklistConfig)){
                    inspChecklist.addAll(specSvcChecklistConfig);
                }

                log.info(StringUtil.changeForLog("inspection pick up vehicle service config ====>>>>" + vehicleConfig));
                if (vehicleConfig != null) {
                    inspChecklist.add(MiscUtil.transferEntityDto(vehicleConfig, ChecklistConfigDto.class));
                }

                oneTime = true;
            }

        }

        return inspChecklist;
    }

    private List<ChecklistConfigDto> getSpecSvcChecklistConfigDtos(String type, String module, List<AppPremisesCorrelationDto> correlationList) {
        List<AppPremSpecialisedDto> appPremSpecialisedDtos = appCommService.getAppPremSpecialisedDtoList(
                correlationList.stream()
                        .map(AppPremisesCorrelationDto::getId)
                        .collect(Collectors.toList())
        );
        List<ChecklistConfigDto> specSvcChecklistConfig = IaisCommonUtils.genNewArrayList();
        for (AppPremSpecialisedDto appPremSpecialisedDto : appPremSpecialisedDtos){
            appPremSpecialisedDto.setBaseSvcConfigDto(HcsaServiceCacheHelper.getServiceById(appPremSpecialisedDto.getBaseSvcId()));
            for (AppPremSubSvcRelDto appPremSubSvcRelDto : appPremSpecialisedDto.getCheckedAppPremSubSvcRelDtoList()){
                appPremSubSvcRelDto.setSvcName(HcsaServiceCacheHelper.getServiceNameById(appPremSubSvcRelDto.getSvcId()));
                ChecklistConfigDto specSvceConfig = configCommClient.getMaxVersionConfigByParams(
                        appPremSpecialisedDto.getBaseSvcCode(), type, module, appPremSubSvcRelDto.getSvcName()).getEntity();
                if (!Objects.isNull(specSvceConfig)){
                    specSvceConfig.setSvcName(HcsaServiceCacheHelper.getServiceByCode(appPremSubSvcRelDto.getSvcCode()).getSvcName());
                    specSvcChecklistConfig.add(specSvceConfig);
                }
            }
        }
        return specSvcChecklistConfig;
    }

    private ChecklistConfigDto getChecklistConfigDtoWithHciCode(String svcCode, String chklModule, String type, ChecklistConfigDto baseSvcConfig, String hciCode) {
        ChecklistConfigDto svcConfig = null;
        if (!StringUtil.isEmpty(hciCode)) {
            svcConfig = hcsaChklClient.getMaxVersionServiceConfigByParams(svcCode, type, chklModule, "", hciCode).getEntity();
        }
        if (svcConfig == null) {
            svcConfig = MiscUtil.transferEntityDto(baseSvcConfig, ChecklistConfigDto.class);
        }
        return svcConfig;
    }

    @Override
    public void saveAdhocChecklist(AdhocCheckListConifgDto adhocConfig) {
        adhocConfig.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        FeignResponseEntity<AdhocCheckListConifgDto> result = applicationClient.saveAdhocChecklist(adhocConfig);
        if (HttpStatus.SC_OK == result.getStatusCode()) {
            callEicGatewaySaveItem(result.getEntity());
        }
    }

    @Override
    public boolean hasSampleChecklistItem(AdhocCheckListConifgDto adhocConfig) {
        Set<String > hashSet = IaisCommonUtils.genNewHashSet();
        List<AdhocChecklistItemDto> itemList = adhocConfig.getAllAdhocItem();
        for (AdhocChecklistItemDto item : itemList){
            String question = item.getQuestion();
            if (!hashSet.add(question)){
                return true;
            }
        }
        return false;
    }

    @Override
    public void removeAdhocItem(List<AdhocChecklistItemDto> itemList, int index) {
        int i = 0;
        for (AdhocChecklistItemDto item : itemList){
            if (index == i){
                itemList.remove(i);
                break;
            }

            i++;
        }
    }

    @Override
    public void filterAdhocItem(SearchParam searchParam, AdhocCheckListConifgDto config) {
        if (Optional.ofNullable(config).isPresent()){
            List<AdhocChecklistItemDto> itemList = config.getAllAdhocItem();
            log.debug("indicates that a record has been selected ");
            if (IaisCommonUtils.isNotEmpty(itemList)){
                itemList.removeIf(i -> StringUtil.isEmpty(i.getItemId()));
                SqlHelper.builderNotInSql(searchParam, "item.id", "adhocItemId",
                        itemList.stream().map(AdhocChecklistItemDto::getItemId).collect(Collectors.toList()));
            }
        }
    }

    @Override
    public void addSelectedChecklistItemToAdhocConfig(List<ChecklistItemDto> itemList, AdhocCheckListConifgDto config) {
        List<AdhocChecklistItemDto> allAdhocItem = config.getAllAdhocItem();
        itemList.forEach(selectItem -> {
            AdhocChecklistItemDto adhocItem = new AdhocChecklistItemDto();
            String question = selectItem.getChecklistItem();
            String answerType = selectItem.getAnswerType();
            String riskLevel = selectItem.getRiskLevel();

            adhocItem.setItemId(selectItem.getItemId());
            adhocItem.setAnswerType(answerType);
            adhocItem.setQuestion(question);
            adhocItem.setRiskLvl(riskLevel);
            adhocItem.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
            allAdhocItem.add(adhocItem);
        });
    }

    public void callEicGatewaySaveItem(AdhocCheckListConifgDto data) {
        //route to fe
        gatewayClient.callEicWithTrack(data, gatewayClient::syncAdhocItemData, "syncAdhocItemData");
    }
}
