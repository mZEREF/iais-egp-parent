package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesDoQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.appeal.AppPremiseMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppEditSelectDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGroupMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremSpecialisedDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcRelatedInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSvcVehicleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.PreOrPostInspectionResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.utils.CopyUtil;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.MiscUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.constant.HcsaAppConst;
import com.ecquaria.cloud.moh.iais.constant.RfcConst;
import com.ecquaria.cloud.moh.iais.helper.AppValidatorHelper;
import com.ecquaria.cloud.moh.iais.helper.ApplicationHelper;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.helper.MessageUtil;
import com.ecquaria.cloud.moh.iais.helper.RfcHelper;
import com.ecquaria.cloud.moh.iais.service.AppCommService;
import com.ecquaria.cloud.moh.iais.service.ConfigCommService;
import com.ecquaria.cloud.moh.iais.service.LicCommService;
import com.ecquaria.cloud.moh.iais.service.client.AppCommClient;
import com.ecquaria.cloud.moh.iais.service.client.GenerateIdClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * @Auther chenlei on 5/3/2022.
 */
@Slf4j
@Service
public class AppCommServiceImpl implements AppCommService {

    @Value("${moh.halp.prs.enable:Y}")
    private String prsFlag;

    @Autowired
    private AppCommClient appCommClient;

    @Autowired
    private SystemAdminClient systemAdminClient;

    @Autowired
    private GenerateIdClient generateIdClient;

    @Autowired
    private LicCommService licCommService;

    @Autowired
    private ConfigCommService configCommService;

    @Override
    public AppSubmissionDto getAppSubmissionDtoByAppNo(String appNo) {
        log.info(StringUtil.changeForLog("AppNo is " + appNo));
        if (StringUtil.isEmpty(appNo)) {
            return null;
        }
        return appCommClient.getAppSubmissionDtoByAppNo(appNo).getEntity();
    }

    @Override
    public AppSubmissionDto getRfiAppSubmissionDtoByAppNo(String appNo) {
        log.info(StringUtil.changeForLog("AppNo is " + appNo));
        if (StringUtil.isEmpty(appNo)) {
            return null;
        }
        return appCommClient.getRfiAppSubmissionDtoByAppNo(appNo).getEntity();
    }

    @Override
    public List<ApplicationDto> getApplicationsByGroupNo(String appGrpNo) {
        log.info(StringUtil.changeForLog("AppGrpNo is " + appGrpNo));
        if (StringUtil.isEmpty(appGrpNo)) {
            return IaisCommonUtils.genNewArrayList();
        }
        return appCommClient.getApplicationsByGroupNo(appGrpNo).getEntity();
    }

    @Override
    public ApplicationDto getApplicationById(String appId) {
        log.info(StringUtil.changeForLog("AppId is " + appId));
        if (StringUtil.isEmpty(appId)) {
            return null;
        }
        return appCommClient.getApplicationById(appId).getEntity();
    }

    @Override
    public ApplicationDto getApplicationDtoByAppNo(String appNo) {
        log.info(StringUtil.changeForLog("AppNo is " + appNo));
        if (StringUtil.isEmpty(appNo)) {
            return null;
        }
        return appCommClient.getApplicationDtoByAppNo(appNo).getEntity();
    }

    @Override
    public String getDraftNo(String appType) {
        return systemAdminClient.draftNumber(appType).getEntity();
    }

    @Override
    public String getGroupNo(String appType) {
        return systemAdminClient.applicationNumber(appType).getEntity();
    }

    @Override
    public ProfessionalResponseDto retrievePrsInfo(String profRegNo) {
        log.info(StringUtil.changeForLog("Prof Reg No is " + profRegNo + " - PRS flag is " + prsFlag));
        ProfessionalResponseDto professionalResponseDto = null;
        if ("Y".equals(prsFlag) && !StringUtil.isEmpty(profRegNo)) {
            List<String> prgNos = IaisCommonUtils.genNewArrayList();
            prgNos.add(profRegNo);
            ProfessionalParameterDto professionalParameterDto = new ProfessionalParameterDto();
            professionalParameterDto.setRegNo(prgNos);
            professionalParameterDto.setClientId("22222");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String format = simpleDateFormat.format(new Date());
            professionalParameterDto.setTimestamp(format);
            professionalParameterDto.setSignature("2222");
            try {
                FeignResponseEntity<List> entity = null;
                if (ApplicationHelper.isFrontend()) {
                    entity = IaisEGPHelper.invokeBeanMethod("feEicGatewayClient", "getProfessionalDetail", professionalParameterDto);
                } else if (ApplicationHelper.isBackend()) {
                    entity = IaisEGPHelper.invokeBeanMethod("com.ecquaria.cloud.moh.iais.service.client.ApplicationClient",
                            "getProfessionalDetail", professionalParameterDto);
                }
                if (entity != null && 401 == entity.getStatusCode()) {
                    professionalResponseDto = new ProfessionalResponseDto();
                    professionalResponseDto.setStatusCode("401");
                } else {
                    List<ProfessionalResponseDto> professionalResponseDtos = entity != null ? entity.getEntity() : null;
                    if (professionalResponseDtos != null && professionalResponseDtos.size() > 0) {
                        professionalResponseDto = professionalResponseDtos.get(0);
                        List<String> qualification = professionalResponseDto.getQualification();
                        List<String> subspecialty = professionalResponseDto.getSubspecialty();
                        if (qualification != null && qualification.size() > 1) {
                            professionalResponseDto.setQualification(Collections.singletonList(qualification.stream()
                                    .collect(Collectors.joining(","))));
                        }
                        if (subspecialty != null && subspecialty.size() > 1) {
                            professionalResponseDto.setSubspecialty(Collections.singletonList(subspecialty.stream()
                                    .collect(Collectors.joining(","))));
                        }
                    }
                    if (professionalResponseDto == null) {
                        professionalResponseDto = new ProfessionalResponseDto();
                        professionalResponseDto.setStatusCode("-1");
                    } else if (StringUtil.isEmpty(professionalResponseDto.getName())) {
                        professionalResponseDto.setStatusCode("-2");
                    }
                }
            } catch (Exception e) {
                professionalResponseDto = new ProfessionalResponseDto();
                professionalResponseDto.setHasException(true);
                log.info(StringUtil.changeForLog("retrieve prs info start ..."));
                log.error(StringUtil.changeForLog(e.getMessage()), e);
            }
        }
        log.info(StringUtil.changeForLog("ProfessionalResponseDto: " + JsonUtil.parseToJson(professionalResponseDto)));
        return professionalResponseDto;
    }

    @Override
    public List<ApplicationDto> getAppByLicIdAndExcludeNew(String licenceId) {
        List<ApplicationDto> applicationDtos = appCommClient.getAppByLicIdAndExcludeNew(licenceId).getEntity();
        List<ApplicationDto> newApplicationDtos = IaisCommonUtils.genNewArrayList();
        List<String> finalStatusList = IaisCommonUtils.getAppFinalStatus();
        for (ApplicationDto applicationDto : applicationDtos) {
            if (!finalStatusList.contains(applicationDto.getStatus())) {
                newApplicationDtos.add(applicationDto);
            }
        }
        return newApplicationDtos;
    }

    @Override
    public AppGrpPremisesDto getAppGrpPremisesById(String appPreId) {
        log.info(StringUtil.changeForLog("App Prem Id: " + appPreId));
        if (StringUtil.isEmpty(appPreId)) {
            return null;
        }
        return appCommClient.getAppGrpPremisesById(appPreId).getEntity();
    }

    @Override
    public Boolean isOtherOperation(String licenceId) {
        return appCommClient.isLiscenceAppealOrCessation(licenceId).getEntity();
    }

    @Override
    public List<AppGrpPremisesDto> getActivePendingPremiseList(String licenseeId) {
        log.info(StringUtil.changeForLog("LicenseeId is " + licenseeId));
        if (StringUtil.isEmpty(licenseeId)) {
            return IaisCommonUtils.genNewArrayList();
        }
        List<AppGrpPremisesDto> appGrpPremisesDtos = appCommClient.getActivePendingPremises(licenseeId).getEntity();
        if (appGrpPremisesDtos == null || appGrpPremisesDtos.isEmpty()) {
            return IaisCommonUtils.genNewArrayList();
        }
        return StreamSupport.stream(appGrpPremisesDtos.spliterator(), appGrpPremisesDtos.size() > 4)
                .peek(appGrpPremisesDto -> {
                    appGrpPremisesDto.setExistingData(AppConsts.YES);
                    //appGrpPremisesDto.setFromDB(true);
                    /*List<String> relatedServices = appGrpPremisesDto.getRelatedServices();
                    if (relatedServices != null && !relatedServices.isEmpty()) {
                        List<String> svcNames = relatedServices.stream()
                                .map(svcId -> HcsaServiceCacheHelper.getServiceById(svcId))
                                .filter(Objects::nonNull)
                                .map(HcsaServiceDto::getSvcName)
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
                        appGrpPremisesDto.setRelatedServices(svcNames);
                    }*/
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<AppSvcDocDto> getMaxSeqNumSvcDocList(String appGrpId) {
        log.info(StringUtil.changeForLog("App Grp Id is " + appGrpId));
        if (StringUtil.isEmpty(appGrpId)) {
            return IaisCommonUtils.genNewArrayList();
        }
        return appCommClient.getMaxSeqNumSvcDocList(appGrpId).getEntity();
    }

    @Override
    public List<String> getHciFromPendAppAndLic(String licenseeId, List<HcsaServiceDto> hcsaServiceDtos,
            List<PremisesDto> excludePremisesList, List<AppGrpPremisesDto> excludeAppPremList) {
        List<String> result = IaisCommonUtils.genNewArrayList();
        if (!StringUtil.isEmpty(licenseeId) && !IaisCommonUtils.isEmpty(hcsaServiceDtos)) {
            List<String> svcNames = IaisCommonUtils.genNewArrayList();
            for (HcsaServiceDto hcsaServiceDto : hcsaServiceDtos) {
                svcNames.add(hcsaServiceDto.getSvcName());
            }
            log.info(StringUtil.changeForLog("Svc Names: " + svcNames));
            AppPremisesDoQueryDto appPremisesDoQueryDto = new AppPremisesDoQueryDto();
            List<HcsaServiceDto> HcsaServiceDtoList = configCommService.getHcsaServiceByNames(svcNames);
            List<String> svcIds = IaisCommonUtils.genNewArrayList();
            for (HcsaServiceDto hcsaServiceDto : HcsaServiceDtoList) {
                svcIds.add(hcsaServiceDto.getId());
            }
            appPremisesDoQueryDto.setLicenseeId(licenseeId);
            appPremisesDoQueryDto.setSvcIdList(svcIds);
            List<PremisesDto> premisesDtos = licCommService.getPremisesByLicseeIdAndSvcName(licenseeId, svcNames);
            List<AppGrpPremisesDto> appGrpPremisesDtos = getPendAppPremises(appPremisesDoQueryDto);
            if (!IaisCommonUtils.isEmpty(premisesDtos)) {
                for (PremisesDto premisesHciDto : premisesDtos) {
                    // rfi
                    if (excludePremisesList != null && !excludePremisesList.isEmpty() && excludePremisesList.stream()
                            .anyMatch(dto -> Objects.equals(dto.getId(), premisesHciDto.getId()))) {
                        continue;
                    }
                    // rfc & renewal
                    if (excludeAppPremList != null && !excludeAppPremList.isEmpty() && excludeAppPremList.stream()
                            .anyMatch(dto -> Objects.equals(dto.getId(), premisesHciDto.getId())
                                    || Objects.equals(dto.getPremisesIndexNo(), premisesHciDto.getId()))) {
                        continue;
                    }
                    result.addAll(ApplicationHelper.genPremisesHciList(premisesHciDto));
                }
            }
            if (!IaisCommonUtils.isEmpty(appGrpPremisesDtos)) {
                for (AppGrpPremisesDto premisesEntityDto : appGrpPremisesDtos) {
                    // rfi
                    if (excludeAppPremList != null && !excludeAppPremList.isEmpty() && excludeAppPremList.stream()
                            .anyMatch(dto -> Objects.equals(dto.getId(), premisesEntityDto.getId())
                                    || Objects.equals(dto.getPremisesIndexNo(), premisesEntityDto.getId()))) {
                        continue;
                    }
                    PremisesDto premisesDto = MiscUtil.transferEntityDto(premisesEntityDto, PremisesDto.class);
                    result.addAll(ApplicationHelper.genPremisesHciList(premisesDto));
                }
            }
        }
        log.info(StringUtil.changeForLog("HciFromPendAppAndLic Result: " + result));
        return result;
    }

    @Override
    public List<AppSvcVehicleDto> getActiveVehicles(List<String> excludeIds) {
        //TODO need to be removed before UAT
        List<AppSvcVehicleDto> result = getAppActiveVehicles(excludeIds);
        log.error(StringUtil.changeForLog("App Vehicle size: " + result.size()));
        result = getLicActiveVehicles(excludeIds);
        log.info(StringUtil.changeForLog("App Vehicle size: " + result.size()));
        return result;
    }

    private List<AppSvcVehicleDto> getLicActiveVehicles(List<String> excludeIds) {
        List<AppSvcVehicleDto> vehicles = licCommService.getActiveVehicles();
        if (vehicles == null) {
            vehicles = IaisCommonUtils.genNewArrayList();
        }
        List<AppSvcVehicleDto> appVehicles = appCommClient.getActivePendingVehicles().getEntity();
        if (appVehicles != null) {
            vehicles.addAll(appVehicles);
        }
        if (excludeIds == null) {
            excludeIds = IaisCommonUtils.genNewArrayList(0);
        }
        final List<String> finalIds = excludeIds;
        List<AppSvcVehicleDto> result = IaisCommonUtils.genNewArrayList(vehicles.size());
        vehicles.forEach(vehicle -> {
            if (!finalIds.contains(vehicle.getAppId())) {
                result.add(vehicle);
            }
        });
        return vehicles.stream()
                .filter(vehicle -> !finalIds.contains(vehicle.getAppId())
                        && (vehicle.getLicenceDto() == null || !finalIds.contains(vehicle.getLicenceDto().getId())))
                .collect(Collectors.toList());
    }

    private List<AppSvcVehicleDto> getAppActiveVehicles(List<String> excludeIds) {
        List<AppSvcVehicleDto> vehicles = appCommClient.getActiveVehicles().getEntity();
        if (vehicles == null || vehicles.isEmpty()) {
            return vehicles;
        }
        if (excludeIds == null) {
            excludeIds = IaisCommonUtils.genNewArrayList(0);
        }
        final List<String> finalIds = excludeIds;
        List<LicAppCorrelationDto> licAppCorrList = licCommService.getInactiveLicAppCorrelations();
        List<AppSvcVehicleDto> result = IaisCommonUtils.genNewArrayList(vehicles.size());
        vehicles.forEach(vehicle -> {
            if (!isIn(vehicle.getAppId(), licAppCorrList) && !finalIds.contains(vehicle.getAppId())) {
                result.add(vehicle);
            }
        });
        return result;
    }

    private boolean isIn(String appId, List<LicAppCorrelationDto> licAppCorrList) {
        if (licAppCorrList == null || licAppCorrList.isEmpty()) {
            return false;
        }
        if (StringUtil.isEmpty(appId)) {
            return true;
        }
        return licAppCorrList.stream().anyMatch(corr -> appId.equals(corr.getApplicationId()));
    }

    @Override
    public List<String> getActiveConveyanceVehicles(List<String> excludeIds) {
        List<String> result = IaisCommonUtils.genNewArrayList();
        if (excludeIds == null) {
            excludeIds = IaisCommonUtils.genNewArrayList(0);
        }
        final List<String> finalIds = excludeIds;
        String premType = ApplicationConsts.PREMISES_TYPE_CONVEYANCE;
        List<PremisesDto> premisesDtos = licCommService.getPremisesDtosByPremType(premType);
        for (PremisesDto premisesDto : premisesDtos) {
            List<LicenceDto> licenceDtos = premisesDto.getLicenceDtos();
            if (licenceDtos != null && licenceDtos.stream().anyMatch(lic -> finalIds.contains(lic.getId()))) {
                continue;
            }
            result.add(premisesDto.getVehicleNo());
        }

        List<AppGrpPremisesDto> appGrpPremisesDtos = appCommClient.getActivePendingPremisesByPremType(premType).getEntity();
        for (AppGrpPremisesDto premisesDto : appGrpPremisesDtos) {
            List<ApplicationDto> appDtos = premisesDto.getApplicationDtos();
            if (appDtos != null && appDtos.stream().anyMatch(app -> finalIds.contains(app.getId()))) {
                continue;
            }
            result.add(premisesDto.getVehicleNo());
        }
        return result;
    }

    @Override
    public List<AppPremiseMiscDto> getActiveWithdrawAppPremiseMiscsByApp(String appId) {
        log.info(StringUtil.changeForLog("The appId: " + appId));
        if (StringUtil.isEmpty(appId)) {
            return IaisCommonUtils.genNewArrayList();
        }
        List<String> excludeStatus = IaisCommonUtils.genNewArrayList();
        excludeStatus.add(ApplicationConsts.APPLICATION_STATUS_REJECTED);
        excludeStatus.add(ApplicationConsts.APPLICATION_STATUS_DELETED);
        return appCommClient.getAppPremiseMiscsByConds(ApplicationConsts.WITHDROW_TYPE_APPLICATION, appId,
                excludeStatus).getEntity();
    }

    @Override
    public void transform(AppSubmissionDto appSubmissionDto, String licenseeId, String appType, boolean isRfi) {
        Double amount = 0.0;
        //judge is the preInspection
        PreOrPostInspectionResultDto preOrPostInspectionResultDto = configCommService.judgeIsPreInspection(appSubmissionDto);
        if (preOrPostInspectionResultDto == null) {
            appSubmissionDto.setPreInspection(true);
            appSubmissionDto.setRequirement(true);
        } else {
            appSubmissionDto.setPreInspection(preOrPostInspectionResultDto.isPreInspection());
            appSubmissionDto.setRequirement(preOrPostInspectionResultDto.isRequirement());
        }
        appSubmissionDto.setAppGrpId(null);
        appSubmissionDto.setFromBe(ApplicationHelper.isBackend());
        appSubmissionDto.setAppType(appType);
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (appSvcRelatedInfoDtoList != null) {
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtoList) {
                String svcId = appSvcRelatedInfoDto.getServiceId();
                String name = appSvcRelatedInfoDto.getServiceName();
                if (!StringUtil.isEmpty(svcId)) {
                    HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceById(svcId);
                    if (hcsaServiceDto != null) {
                        name = hcsaServiceDto.getSvcName();
                        appSvcRelatedInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
                        appSvcRelatedInfoDto.setServiceType(hcsaServiceDto.getSvcType());
                        appSvcRelatedInfoDto.setServiceName(name);
                    }
                } else if (!StringUtil.isEmpty(name)) {
                    HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(name);
                    // configCommService.getActiveHcsaServiceDtoByName(serviceName);
                    svcId = hcsaServiceDto.getId();
                    if (hcsaServiceDto != null) {
                        appSvcRelatedInfoDto.setServiceId(svcId);
                        appSvcRelatedInfoDto.setServiceCode(hcsaServiceDto.getSvcCode());
                        appSvcRelatedInfoDto.setServiceType(hcsaServiceDto.getSvcType());
                    }
                }
            }
        }
        if (!StringUtil.isEmpty(licenseeId)) {
            appSubmissionDto.setLicenseeId(licenseeId);
        }
        changeDocToNewVersion(appSubmissionDto, isRfi);
        appSubmissionDto.setAmount(appSubmissionDto.getAmount() == null ? amount : appSubmissionDto.getAmount());
        appSubmissionDto.setAuditTrailDto(AuditTrailHelper.getCurrentAuditTrailDto());
        appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_PAYMENT);
        appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
        RfcHelper.setRiskToDto(appSubmissionDto);
        RfcHelper.setRelatedInfoBaseServiceId(appSubmissionDto);
    }

    private void changeDocToNewVersion(AppSubmissionDto appSubmissionDto, boolean isRfi) {
        log.debug(StringUtil.changeForLog("do changeDocToNewVersion start ..."));
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        if (!IaisCommonUtils.isEmpty(appSvcRelatedInfoDtos)) {
            List<HcsaSvcDocConfigDto> primaryDocConfig = configCommService.getAllHcsaSvcDocs(null);
            for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
                String currentSvcId = appSvcRelatedInfoDto.getServiceId();
                if (!StringUtil.isEmpty(currentSvcId)) {
                    List<AppSvcDocDto> appSvcDocDtos = appSvcRelatedInfoDto.getAppSvcDocDtoLit();
                    List<String> svcDocConfigIdList = IaisCommonUtils.genNewArrayList();
                    if (!IaisCommonUtils.isEmpty(appSvcDocDtos)) {
                        for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                            svcDocConfigIdList.add(appSvcDocDto.getSvcDocId());
                        }
                    }
                    List<HcsaSvcDocConfigDto> oldSvcDocConfigDtos = configCommService.listSvcDocConfigByIds(svcDocConfigIdList);
                    List<HcsaSvcDocConfigDto> svcDocConfig = configCommService.getAllHcsaSvcDocs(currentSvcId);
                    appSvcDocDtos = updateSvcDoc(appSvcDocDtos, oldSvcDocConfigDtos, svcDocConfig);
                    appSvcRelatedInfoDto.setAppSvcDocDtoLit(appSvcDocDtos);
                }
            }
        }
        log.debug(StringUtil.changeForLog("do changeDocToNewVersion end ..."));
    }

    private List<AppSvcDocDto> updateSvcDoc(List<AppSvcDocDto> appSvcDocDtos, List<HcsaSvcDocConfigDto> oldSvcDocConfigDtos,
            List<HcsaSvcDocConfigDto> svcDocConfigDtos) {
        // old psn doc use psn type 1 config change to psn type 2, doc will be missing
        List<AppSvcDocDto> newAppSvcDocDtoList = IaisCommonUtils.genNewArrayList();
        if (!IaisCommonUtils.isEmpty(appSvcDocDtos) && !IaisCommonUtils.isEmpty(oldSvcDocConfigDtos) && !IaisCommonUtils.isEmpty(
                svcDocConfigDtos)) {
            for (AppSvcDocDto appSvcDocDto : appSvcDocDtos) {
                for (HcsaSvcDocConfigDto oldSvcDocConfigDto : oldSvcDocConfigDtos) {
                    if (appSvcDocDto.getSvcDocId() != null && oldSvcDocConfigDto.getId() != null) {
                        if (appSvcDocDto.getSvcDocId().equals(oldSvcDocConfigDto.getId())) {
                            String titleName = oldSvcDocConfigDto.getDocTitle();
                            if (!StringUtil.isEmpty(titleName)) {
                                for (HcsaSvcDocConfigDto svcDocConfigDto : svcDocConfigDtos) {
                                    if (titleName.equals(svcDocConfigDto.getDocTitle())) {
                                        AppSvcDocDto newAppSvcDocDto = CopyUtil.copyMutableObject(appSvcDocDto);
                                        newAppSvcDocDto.setSvcDocId(svcDocConfigDto.getId());
                                        newAppSvcDocDto.setDupForPerson(svcDocConfigDto.getDupForPerson());
                                        newAppSvcDocDto.setDupForPrem(svcDocConfigDto.getDupForPrem());
                                        newAppSvcDocDto.setPersonType(ApplicationHelper.getPsnType(newAppSvcDocDto.getDupForPerson()));
                                        newAppSvcDocDtoList.add(newAppSvcDocDto);
                                        break;
                                    }
                                }
                            }
                            break;
                        }
                    }
                }
            }
        }
        return newAppSvcDocDtoList;
    }

    @Override
    public String getSeqId() {
        return generateIdClient.getSeqId().getEntity();
    }

    @Override
    public void saveAutoRFCLinkAppGroupMisc(String notAutoGroupId, String autoGroupId) {
        log.info(StringUtil.changeForLog("The GrouptId: " + notAutoGroupId + " | " + autoGroupId));
        if (StringUtil.isEmpty(notAutoGroupId) || StringUtil.isEmpty(autoGroupId)) {
            return;
        }
        AppGroupMiscDto appGroupMiscDto = new AppGroupMiscDto();
        appGroupMiscDto.setAppGrpId(notAutoGroupId);
        appGroupMiscDto.setMiscValue(autoGroupId);
        appGroupMiscDto.setMiscType(ApplicationConsts.APP_GROUP_MISC_TYPE_AMEND_GROUP_ID);
        appGroupMiscDto.setStatus(AppConsts.COMMON_STATUS_ACTIVE);
        saveAppGrpMisc(appGroupMiscDto);
    }

    private void saveAppGrpMisc(AppGroupMiscDto appGroupMiscDto) {
        appCommClient.saveAppGroupMiscDto(appGroupMiscDto);
    }

    @Override
    public Map<String, String> checkAffectedAppSubmissions(List<LicenceDto> selectLicences, AppGrpPremisesDto appGrpPremisesDto,
            double amount,  String draftNo, String appGroupNo, AppEditSelectDto appEditSelectDto,
            List<AppSubmissionDto> appSubmissionDtos) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if (selectLicences == null || selectLicences.isEmpty()) {
            return errorMap;
        }
        /**
         * check all these licences whether are eligible or not
         */
        String licenseeId = selectLicences.get(0).getLicenseeId();
        List<LicenceDto> licenceDtoByHciCode = licCommService.getLicenceDtoByHciCode(licenseeId, appGrpPremisesDto);
        if (licenceDtoByHciCode == null || licenceDtoByHciCode.isEmpty()) {
            errorMap.put(RfcConst.INVALID_LIC, MessageUtil.getMessageDesc("RFC_ERR024"));
            return errorMap;
        }
        boolean parallel = selectLicences.size() >= RfcConst.DFT_MIN_PARALLEL_SIZE;
        boolean allMatch = StreamSupport.stream(selectLicences.spliterator(), parallel)
                .allMatch(dto -> licenceDtoByHciCode.parallelStream()
                        .anyMatch(obj -> Objects.equals(obj.getId(), dto.getId())));
        if (!allMatch) {
            errorMap.put(RfcConst.INVALID_LIC, MessageUtil.getMessageDesc("RFC_ERR024"));
            return errorMap;
        }
        String presmiseType = appGrpPremisesDto.getPremisesType();
        if (StringUtil.isEmpty(presmiseType)) {
            return errorMap;
        }
        /**
         *  check whether there is another operation for the original licence
         */
        Set<String> preseTypes = Collections.singleton(presmiseType);
        errorMap = StreamSupport.stream(selectLicences.spliterator(), parallel)
                .map(licenceDto -> AppValidatorHelper.validateLicences(licenceDto, preseTypes,
                        HcsaAppConst.SECTION_PREMISES))
                .filter(Objects::nonNull)
                .collect(IaisCommonUtils::genNewHashMap, Map::putAll, Map::putAll);
        if (!errorMap.isEmpty()) {
            return errorMap;
        }
        if (StringUtil.isEmpty(draftNo)) {
            draftNo = systemAdminClient.draftNumber(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE).getEntity();
        }
        String draft = draftNo;
        List<AppSubmissionDto> appSubmissionDtoList = StreamSupport.stream(selectLicences.spliterator(), parallel)
                .map(licence -> {
                    AppSubmissionDto appSubmissionDtoByLicenceId = licCommService.getAppSubmissionDtoByLicenceId(licence.getId());
                    // Premises
                    ApplicationHelper.reSetPremeses(appSubmissionDtoByLicenceId, appGrpPremisesDto);
                    // check mains
                    checkAffectedAppSubmissions(appSubmissionDtoByLicenceId, licence, amount, draft, appGroupNo,
                            appEditSelectDto, null);
                    return appSubmissionDtoByLicenceId;
                })
                .collect(Collectors.toList());
        Map<AppSubmissionDto, List<String>> errorListMap = StreamSupport.stream(appSubmissionDtoList.spliterator(), parallel)
                .collect(Collectors.toMap(Function.identity(), dto -> AppValidatorHelper.doPreviewSubmitValidate(null, dto, false)));
        String errorMsg = AppValidatorHelper.getErrorMsg(errorListMap);
        if (StringUtil.isNotEmpty(errorMsg)) {
            errorMap.put(RfcConst.SHOW_OTHER_ERROR, errorMsg);
        } else if (appSubmissionDtos != null) {
            appSubmissionDtos.addAll(appSubmissionDtoList);
        }
        return errorMap;
    }

    @Override
    public Map<String, String> checkAffectedAppSubmissions(AppSubmissionDto appSubmissionDto, LicenceDto licence, Double amount,
            String draftNo, String appGroupNo, AppEditSelectDto appEditSelectDto, List<AppSubmissionDto> appSubmissionDtos) {
        Map<String, String> errorMap = IaisCommonUtils.genNewHashMap();
        if (appSubmissionDto == null) {
            return errorMap;
        }
        if (licence == null) {
            licence = licCommService.getActiveLicenceById(appSubmissionDto.getLicenceId());
        }
        if (licence == null) {
            log.info("Invalid Licence");
            errorMap.put(RfcConst.INVALID_LIC, MessageUtil.getMessageDesc("RFC_ERR024"));
            return errorMap;
        }
        String appType = appSubmissionDto.getAppType();
        if (StringUtil.isEmpty(appType)) {
            appType = ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE;
        }
        transform(appSubmissionDto, licence.getLicenseeId(), appType, false);
        appSubmissionDto.setAppGrpNo(appGroupNo);
        appSubmissionDto.setDraftNo(draftNo);

        double total = 0.0;
        if (amount != null) {
            total = amount.doubleValue();
        }
        if (licence.getMigrated() == 1 && IaisEGPHelper.isActiveMigrated()) {
            total = 0.0;
        }
        appSubmissionDto.setAmount(total);
        // check app edit select dto
        if (appEditSelectDto == null) {
            appEditSelectDto = appSubmissionDto.getChangeSelectDto();
        }
        if (appEditSelectDto == null) {
            appEditSelectDto = new AppEditSelectDto();
        }
        AppEditSelectDto editDto = MiscUtil.transferEntityDto(appEditSelectDto, AppEditSelectDto.class);
        appSubmissionDto.setChangeSelectDto(editDto);
        appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_REQUEST_FOR_CHANGE);
        appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_STATUS_REQUEST_FOR_CHANGE_SUBMIT);
        appSubmissionDto.setCreateAuditPayStatus(ApplicationConsts.PAYMENT_STATUS_PENDING_PAYMENT);
        if (MiscUtil.doubleEquals(0.0, total)) {
            appSubmissionDto.setCreatAuditAppStatus(ApplicationConsts.APPLICATION_STATUS_NOT_PAYMENT);
        }
        appSubmissionDto.setGetAppInfoFromDto(true);
        appSubmissionDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        // set app GrpPremisess
        boolean groupLic = appSubmissionDto.isGroupLic();
        int hciNameChange = appEditSelectDto.isChangeHciName() ? 1 : 0;
        appSubmissionDto.setGroupLic(groupLic);
        List<AppGrpPremisesDto> appGrpPremisesDtos = appSubmissionDto.getAppGrpPremisesDtoList();
        if (groupLic) {
            appGrpPremisesDtos.get(0).setGroupLicenceFlag(licence.getId());
            appSubmissionDto.setPartPremise(true);
        }
        appSubmissionDto.getAppGrpPremisesDtoList().get(0).setHciNameChanged(hciNameChange);
        appSubmissionDto.setAppGrpPremisesDtoList(appGrpPremisesDtos);
        ApplicationHelper.reSetAdditionalFields(appSubmissionDto, appEditSelectDto);
        if (appSubmissionDtos != null) {
            appSubmissionDtos.add(appSubmissionDto);
        }
        return errorMap;
    }

    @Override
    public AppSvcDocDto getMaxVersionSvcComDoc(String appGrpId, String configDocId,String seqNum) {
        return appCommClient.getMaxVersionSvcComDoc(appGrpId,configDocId,seqNum).getEntity();
    }

    @Override
    public AppSvcDocDto getMaxVersionSvcSpecDoc(String svcDocId, String appGrpId, String appNo, int seqNum) {
        if (!StringUtil.isEmpty(appGrpId) && !StringUtil.isEmpty(svcDocId) && !StringUtil.isEmpty(appNo)) {
            AppSvcDocDto appSvcDocDto = new AppSvcDocDto();
            appSvcDocDto.setAppGrpId(appGrpId);
            appSvcDocDto.setSvcDocId(svcDocId);
            appSvcDocDto.setSeqNum(seqNum);
            return getMaxVersionSvcSpecDoc(appSvcDocDto, appNo);
        }
        return null;
    }

    @Override
    public AppSvcDocDto getMaxVersionSvcSpecDoc(AppSvcDocDto appSvcDocDto,String appNo) {
        return appCommClient.getMaxVersionSvcSpecDoc(appSvcDocDto,appNo).getEntity();
    }

    @Override
    public List<AppGrpPremisesDto> getPendAppPremises(AppPremisesDoQueryDto appPremisesDoQueryDto) {
        if (appPremisesDoQueryDto == null) {
            return IaisCommonUtils.genNewArrayList();
        }
        return appCommClient.getPendAppPremises(appPremisesDoQueryDto).getEntity();
    }

    @Override
    public AppGrpPremisesDto getActivePremisesByAppNo(String appNo) {
        log.info(StringUtil.changeForLog("App No is " + appNo));
        if (StringUtil.isEmpty(appNo)) {
            return null;
        }
        return appCommClient.getActivePremisesByAppNo(appNo).getEntity();
    }

    @Override
    public List<AppPremSpecialisedDto> getAppPremSpecialisedDtoList(List<String> appPremCorreIds){
        return appCommClient.getAppPremSpecialisedDtoList(appPremCorreIds).getEntity();
    }
}
