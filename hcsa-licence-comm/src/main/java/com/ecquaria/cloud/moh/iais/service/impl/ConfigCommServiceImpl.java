package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.constant.HcsaConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SpecicalPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.AmendmentFeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.fee.FeeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RecommendInspectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceStepSchemeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpecifiedCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSubtypeOrSubsumedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.SuppleFormItemConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.RfcHelper;
import com.ecquaria.cloud.moh.iais.service.ConfigCommService;
import com.ecquaria.cloud.moh.iais.service.client.ComFileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.ConfigCommClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author chenlei on 5/3/2022.
 */
@Slf4j
@Service
public class ConfigCommServiceImpl implements ConfigCommService {

    @Autowired
    private ConfigCommClient configCommClient;

    @Autowired
    private SystemAdminClient systemAdminClient;

    @Autowired
    private ComFileRepoClient comFileRepoClient;

    @Override
    public HcsaServiceDto getActiveHcsaServiceDtoByName(String svcName) {
        log.info(StringUtil.changeForLog("Service Name: " + svcName));
        if (StringUtil.isEmpty(svcName)) {
            return null;
        }
        return configCommClient.getActiveHcsaServiceDtoByName(svcName).getEntity();
    }

    @Override
    public HcsaServiceDto getHcsaServiceDtoById(String id) {
        if (StringUtil.isEmpty(id)) {
            return null;
        }
        List<HcsaServiceDto> result = getHcsaServiceDtosByIds(Collections.singletonList(id));
        if (result.isEmpty()) {
            return null;
        }
        return result.get(0);
    }

    @Override
    public List<HcsaServiceDto> getHcsaServiceDtosByIds(List<String> ids) {
        log.info(StringUtil.changeForLog("Id List: " + ids));
        if (ids == null || ids.isEmpty()) {
            return IaisCommonUtils.genNewArrayList();
        }
        return configCommClient.getHcsaServiceDtosByIds(ids).getEntity();
    }

    @Override
    public List<HcsaServiceDto> getActiveHcsaSvcByNames(List<String> names) {
        log.info(StringUtil.changeForLog("Name List: " + names));
        if (names == null || names.isEmpty()) {
            return IaisCommonUtils.genNewArrayList();
        }
        List<HcsaServiceDto> result = IaisCommonUtils.genNewArrayList();
        List<HcsaServiceDto> hcsaServiceDtos = getHcsaServiceByNames(names);
        if (!IaisCommonUtils.isEmpty(hcsaServiceDtos)) {
            for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtos) {
                if (AppConsts.COMMON_STATUS_ACTIVE.equals(hcsaServiceDto.getStatus())) {
                    result.add(hcsaServiceDto);
                }
            }
        }
        return result;
    }

    @Override
    public List<HcsaServiceDto> getHcsaServiceByNames(List<String> svcNames) {
        return configCommClient.getHcsaServiceByNames(svcNames).getEntity();
    }

//    @Override
//    public List<HcsaServiceDto> getSpecialServices(String baseSvcId) {
//        return IaisCommonUtils.genNewArrayList();
//    }

    @Override
    public List<HcsaServiceDto> allHcsaService() {
        return configCommClient.allHcsaService().getEntity();
    }

    @Override
    public List<HcsaServiceCorrelationDto> getActiveSvcCorrelation() {
        return configCommClient.getActiveSvcCorrelation().getEntity();
    }

    @Override
    public List<HcsaSvcSpecifiedCorrelationDto> getSvcSpeCorrelationsByBaseSvcId(String baseSvcId, String... type) {
        log.info(StringUtil.changeForLog("Base Service Id: " + baseSvcId + " - type: " + Arrays.toString(type)));
        if (StringUtil.isEmpty(baseSvcId)) {
            return IaisCommonUtils.genNewArrayList();
        }
        return configCommClient.getSvcSpeCorrelationsByBaseSvcId(baseSvcId, type).getEntity();
    }

    @Override
    public List<HcsaSvcSubtypeOrSubsumedDto> listSubtype(String serviceId) {
        log.info(StringUtil.changeForLog("Service Id: " + serviceId));
        if (StringUtil.isEmpty(serviceId)) {
            return IaisCommonUtils.genNewArrayList();
        }
        return configCommClient.listSubtype(serviceId).getEntity();
    }

    @Override
    public String getServiceNameById(String svcId) {
        log.info(StringUtil.changeForLog("Service Id: " + svcId));
        if (StringUtil.isEmpty(svcId)) {
            return null;
        }
        return configCommClient.getServiceNameById(svcId).getEntity();
    }

    @Override
    public boolean serviceConfigIsChange(List<String> serviceIds, String premiseType) {
        log.info(StringUtil.changeForLog("Params: " + serviceIds + " | " + premiseType));
        if (serviceIds != null && !serviceIds.isEmpty() && premiseType != null) {
            Set<String> appGrpPremisesTypeBySvcId = getAppGrpPremisesTypeBySvcId(serviceIds);
            if (appGrpPremisesTypeBySvcId.contains(premiseType)) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public Set<String> getAppGrpPremisesTypeBySvcId(List<String> serviceIds) {
        log.info(StringUtil.changeForLog("Service Ids: " + serviceIds));
        if (serviceIds == null || serviceIds.isEmpty()) {
            return IaisCommonUtils.genNewHashSet();
        }
        return configCommClient.getAppGrpPremisesTypeBySvcId(serviceIds).getEntity();
    }

    @Override
    public List<HcsaSvcDocConfigDto> getAllHcsaSvcDocs(String serviceId) {
        Map<String, String> docMap = IaisCommonUtils.genNewHashMap();
        if (StringUtil.isEmpty(serviceId)) {
            docMap.put("common", "0");
            docMap.put("premises", "1");
        } else {
            docMap.put("svc", serviceId);
            docMap.put("common", "0");
        }
        String docMapJson = JsonUtil.parseToJson(docMap);
        return configCommClient.getHcsaSvcDocConfig(docMapJson).getEntity();
    }

    @Override
    public List<HcsaSvcDocConfigDto> listSvcDocConfigByIds(List<String> ids) {
        log.info(StringUtil.changeForLog("Ids: " + ids));
        if (ids == null || ids.isEmpty()) {
            return IaisCommonUtils.genNewArrayList();
        }
        return configCommClient.listSvcDocConfigByIds(ids).getEntity();
    }

    @Override
    public List<HcsaSvcDocConfigDto> getPrimaryDocConfigById(String id) {
        log.info(StringUtil.changeForLog("Id: " + id));
        if (StringUtil.isEmpty(id)) {
            return IaisCommonUtils.genNewArrayList();
        }
        return configCommClient.getPrimaryDocConfigList(id).getEntity();
    }

    @Override
    public HcsaSvcDocConfigDto getHcsaSvcDocConfigDtoById(String svcDocId) {
        log.info(StringUtil.changeForLog("Svc Doc Id: " + svcDocId));
        if (StringUtil.isEmpty(svcDocId)) {
            return null;
        }
        return configCommClient.getHcsaSvcDocConfigDtoById(svcDocId).getEntity();
    }

    @Override
    public List<HcsaServiceStepSchemeDto> getHcsaServiceStepSchemesByServiceId(String serviceId) {
        if (StringUtil.isEmpty(serviceId)) {
            return IaisCommonUtils.genNewArrayList();
        }
        List<HcsaServiceStepSchemeDto> stepDtos = configCommClient.getServiceStepsByServiceId(serviceId).getEntity();
        if (stepDtos != null && !stepDtos.isEmpty()) {
            stepDtos.stream()
                    .filter(dto -> HcsaConsts.STEP_CLINICAL_DIRECTOR.equals(dto.getStepCode()))
                    .forEach(dto -> dto.setStepName(HcsaConsts.CLINICAL_DIRECTORS));
        }
        return stepDtos;
    }

    @Override
    public List<HcsaSvcPersonnelDto> getSvcAllPsnConfig(List<HcsaServiceStepSchemeDto> svcStep, String svcId) {
        List<SpecicalPersonDto> specicalPersonDtos = IaisCommonUtils.genNewArrayList();
        SpecicalPersonDto specicalPersonDto = new SpecicalPersonDto();
        List<String> psnTypes = IaisCommonUtils.genNewArrayList();
        for (HcsaServiceStepSchemeDto hcsaServiceStepSchemeDto : svcStep) {
            if (svcId.equals(hcsaServiceStepSchemeDto.getServiceId())) {
                String stepCode = hcsaServiceStepSchemeDto.getStepCode();
                if (HcsaConsts.STEP_CLINICAL_GOVERNANCE_OFFICERS.equals(stepCode)) {
                    psnTypes.add(ApplicationConsts.PERSONNEL_PSN_TYPE_CGO);
                } else if (HcsaConsts.STEP_PRINCIPAL_OFFICERS.equals(stepCode)) {
                    psnTypes.add(ApplicationConsts.PERSONNEL_PSN_TYPE_PO);
                    psnTypes.add(ApplicationConsts.PERSONNEL_PSN_TYPE_DPO);
                } else if (HcsaConsts.STEP_SERVICE_PERSONNEL.equals(stepCode)) {
                    psnTypes.add(ApplicationConsts.PERSONNEL_PSN_TYPE_SVC_PERSONNEL);
                } else if (HcsaConsts.STEP_MEDALERT_PERSON.equals(stepCode)) {
                    psnTypes.add(ApplicationConsts.PERSONNEL_PSN_TYPE_MAP);
                } else if (HcsaConsts.STEP_VEHICLES.equals(stepCode)) {
                    psnTypes.add(ApplicationConsts.PERSONNEL_VEHICLES);
                } else if (HcsaConsts.STEP_CLINICAL_DIRECTOR.equals(stepCode)) {
                    psnTypes.add(ApplicationConsts.PERSONNEL_CLINICAL_DIRECTOR);
                } else if (HcsaConsts.STEP_CHARGES.equals(stepCode)) {
                    psnTypes.add(ApplicationConsts.PERSONNEL_CHARGES);
                } else if (HcsaConsts.STEP_CHARGES_OTHER.equals(stepCode)) {
                    psnTypes.add(ApplicationConsts.PERSONNEL_CHARGES_OTHER);
                } else if (HcsaConsts.STEP_SECTION_LEADER.equals(stepCode)) {
                    psnTypes.add(ApplicationConsts.PERSONNEL_PSN_SVC_SECTION_LEADER);
                } else if (HcsaConsts.STEP_KEY_APPOINTMENT_HOLDER.equals(stepCode)) {
                    psnTypes.add(ApplicationConsts.PERSONNEL_PSN_KAH);
                }
            }
        }
        specicalPersonDto.setServiceId(svcId);
        specicalPersonDto.setType(psnTypes);
        specicalPersonDtos.add(specicalPersonDto);
        return configCommClient.getServiceSpecificPerson(specicalPersonDtos).getEntity();
    }

    @Override
    public PreOrPostInspectionResultDto judgeIsPreInspection(AppSubmissionDto appSubmissionDto) {
        RecommendInspectionDto recommendInspectionDto = new RecommendInspectionDto();
        List<RiskAcceptiionDto> riskAcceptiionDtos = IaisCommonUtils.genNewArrayList();
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
            riskAcceptiionDto.setScvCode(appSvcRelatedInfoDto.getServiceCode());
            riskAcceptiionDto.setSvcType(appSvcRelatedInfoDto.getServiceType());
            riskAcceptiionDto.setBaseServiceCodeList(appSvcRelatedInfoDto.getBaseServiceCodeList());
            riskAcceptiionDtos.add(riskAcceptiionDto);
        }

        return configCommClient.recommendIsPreInspection(recommendInspectionDto).getEntity();
    }

    @Override
    public List<RiskResultDto> getRiskResult(List<RiskAcceptiionDto> riskAcceptiionDtoList) {
        if (riskAcceptiionDtoList == null || riskAcceptiionDtoList.isEmpty()) {
            return IaisCommonUtils.genNewArrayList();
        }
        return configCommClient.getRiskResult(riskAcceptiionDtoList).getEntity();
    }

    @Override
    public FeeDto getGroupAmendAmount(AmendmentFeeDto amendmentFeeDto) {
        if (amendmentFeeDto == null) {
            return new FeeDto();
        }
        return configCommClient.amendmentFee(amendmentFeeDto).getEntity();
    }

    @Override
    public FeeDto getGroupAmendAmount(AppSubmissionDto appSubmissionDto, AppEditSelectDto appEditSelectDto, boolean isCharity) {
        if (appSubmissionDto == null || appEditSelectDto == null) {
            return new FeeDto();
        }
        AmendmentFeeDto amendmentFeeDto = RfcHelper.getAmendmentFeeDto(appSubmissionDto, appEditSelectDto, isCharity);
        return getGroupAmendAmount(amendmentFeeDto);
    }

    @Override
    public List<HcsaSvcSubtypeOrSubsumedDto> getSvcSubtypeOrSubsumedByIdList(List<String> idList) {
        log.info(StringUtil.changeForLog("Id List: " + idList));
        if (idList == null || idList.isEmpty()) {
            return IaisCommonUtils.genNewArrayList();
        }
        return configCommClient.getSvcSubtypeOrSubsumedByIdList(idList).getEntity();
    }

    @Override
    public List<HcsaSvcPersonnelDto> getHcsaSvcPersonnel(String serviceId, String...psnType) {
        Map<String, Object> map = IaisCommonUtils.genNewHashMap();
        map.put("serviceId", serviceId);
        map.put("psnType", psnType);
        if (!StringUtil.isEmpty(serviceId)&&!IaisCommonUtils.isSpecialEmpty(psnType)){
            return configCommClient.getServiceType(serviceId, psnType).getEntity();
        }
        else{
            return IaisCommonUtils.genNewArrayList();
        }
    }

    @Override
    public HcsaServiceStepSchemeDto getHcsaServiceStepSchemeByConds(String serviceId, String stepCode) {
        if (StringUtil.isEmpty(serviceId) || StringUtil.isEmpty(stepCode)) {
            return null;
        }
        List<HcsaServiceStepSchemeDto> stepDtos = configCommClient.getServiceStepsByServiceId(serviceId).getEntity();
        if (stepDtos == null || stepDtos.isEmpty()) {
            return null;
        }
        return stepDtos.stream()
                .filter(dto -> stepCode.equals(dto.getStepCode()))
                .findAny()
                .orElse(null);
    }

    @Override
    public PostCodeDto getPostCodeByCode(String postalCode) {
        if (StringUtil.isEmpty(postalCode)) {
            return null;
        }
        PostCodeDto postCodeDto = null;
        if (ApplicationHelper.isFrontend()) {
            postCodeDto = IaisEGPHelper.invokeFeignRespMethod("feEicGatewayClient", "getPostalCode", postalCode);
        } else if (ApplicationHelper.isBackend()) {
            postCodeDto = systemAdminClient.getPostCodeByCode(postalCode).getEntity();
        }
        return postCodeDto;
    }

    @Override
    public List<String> saveFileRepo(List<File> files) {
        if (IaisCommonUtils.isEmpty(files)) {
            return IaisCommonUtils.genNewArrayList();
        }
        return comFileRepoClient.saveFileRepo(files);
    }
    //fileRepoClient

    @Override
    public byte[] downloadFileRepo(String fileRepoId) {
        if (StringUtil.isEmpty(fileRepoId)) {
            return new byte[0];
        }
        return IaisEGPHelper.invokeFeignRespMethod("com.ecquaria.cloud.moh.iais.service.client.FileRepoClient",
                "getFileFormDataBase", fileRepoId);
    }

    @Override
    public List<SuppleFormItemConfigDto> getSuppleFormItemConfigs(String serviceCode, String type) {
        return configCommClient.getHcsaSvcSuppleFormItemBySvcCode(serviceCode,type).getEntity();
    }
}
