package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.AppConsts;
import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.AuditTrailDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessHciDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskAcceptiionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.risksm.RiskResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.AuditTrailHelper;
import com.ecquaria.cloud.moh.iais.helper.HcsaServiceCacheHelper;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.CessationService;
import com.ecquaria.cloud.moh.iais.service.client.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author weilu
 * @date 2020/2/7 13:17
 */
@Service
public class CessationServiceImpl implements CessationService {

    @Autowired
    private LicenceClient licenceClient;
    @Autowired
    private CessationClient cessationClient;
    @Autowired
    private SystemAdminClient systemAdminClient;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private FeEicGatewayClient feEicGatewayClient;
    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;
    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;
    @Autowired
    private AppConfigClient appConfigClient;

    @Override
    public List<String> getActiveLicence(List<String> licIds) {
        List<String> activeLicIds = IaisCommonUtils.genNewArrayList();
        for (String licId : licIds) {
            LicenceDto licenceDto = licenceClient.getLicBylicId(licId).getEntity();
            String status = licenceDto.getStatus();
            if (ApplicationConsts.LICENCE_STATUS_ACTIVE.equals(status)) {
                activeLicIds.add(licId);
            }
        }
        return activeLicIds;
    }

    @Override
    public List<AppCessLicDto> getAppCessDtosByLicIds(List<String> licIds) {
        List<AppCessLicDto> appCessLicDtos = IaisCommonUtils.genNewArrayList();
        if (licIds != null && !licIds.isEmpty()) {
            for (String licId : licIds) {
                AppCessLicDto appCessDto = new AppCessLicDto();
                LicenceDto licenceDto = licenceClient.getLicBylicId(licId).getEntity();
                List<PremisesDto> premisesDtos = licenceClient.getPremisesDto(licId).getEntity();
                String svcName = licenceDto.getSvcName();
                String licenceNo = licenceDto.getLicenceNo();
                appCessDto.setLicenceNo(licenceNo);
                appCessDto.setSvcName(svcName);
                appCessDto.setLicenceId(licId);
                List<AppCessHciDto> appCessHciDtos = IaisCommonUtils.genNewArrayList();
                if (premisesDtos != null && !premisesDtos.isEmpty()) {
                    for (PremisesDto premisesDto : premisesDtos) {
                        String blkNo = premisesDto.getBlkNo();
                        String streetName = premisesDto.getStreetName();
                        String buildingName = premisesDto.getBuildingName();
                        String floorNo = premisesDto.getFloorNo();
                        String unitNo = premisesDto.getUnitNo();
                        String postalCode = premisesDto.getPostalCode();
                        String hciAddress = IaisCommonUtils.appendPremisesAddress(blkNo, streetName, buildingName, floorNo, unitNo, postalCode);
                        AppCessHciDto appCessHciDto = new AppCessHciDto();
                        String hciName = premisesDto.getHciName();
                        appCessHciDto.setHciName(hciName);
                        appCessHciDto.setHciAddress(hciAddress);
                        appCessHciDtos.add(appCessHciDto);
                    }
                }
                appCessDto.setAppCessHciDtos(appCessHciDtos);
                appCessLicDtos.add(appCessDto);
            }
            return appCessLicDtos;
        } else {
            return null;
        }

    }


    @Override
    public List<AppCessLicDto> getOldCessationByIds(List<String> licIds) {
        List<AppCessLicDto> appCessLicDtos = cessationClient.getCessationByLicIds(licIds).getEntity();
        if (appCessLicDtos != null && !appCessLicDtos.isEmpty()) {
            for (AppCessLicDto appCessLicDto : appCessLicDtos) {
                String licenceId = appCessLicDto.getLicenceId();
                LicenceDto licenceDto = licenceClient.getLicBylicId(licenceId).getEntity();
                List<PremisesDto> premisesDtos = licenceClient.getPremisesDto(licenceId).getEntity();
                String svcName = licenceDto.getSvcName();
                String licenceNo = licenceDto.getLicenceNo();
                appCessLicDto.setLicenceNo(licenceNo);
                appCessLicDto.setSvcName(svcName);
                if (premisesDtos != null && !premisesDtos.isEmpty()) {
                    String hciAddress;
                    String hciName;
                    List<AppCessHciDto> appCessHciDtos = appCessLicDto.getAppCessHciDtos();
                    for (int i = 0; i < premisesDtos.size(); i++) {
                        PremisesDto premisesDto = premisesDtos.get(i);
                        String blkNo = premisesDto.getBlkNo();
                        String streetName = premisesDto.getStreetName();
                        String buildingName = premisesDto.getBuildingName();
                        String floorNo = premisesDto.getFloorNo();
                        String unitNo = premisesDto.getUnitNo();
                        String postalCode = premisesDto.getPostalCode();
                        hciAddress = IaisCommonUtils.appendPremisesAddress(blkNo, streetName, buildingName, floorNo, unitNo, postalCode);
                        hciName = premisesDto.getHciName();
                        AppCessHciDto appCessHciDto = appCessHciDtos.get(i);
                        appCessHciDto.setHciName(hciName);
                        appCessHciDto.setHciAddress(hciAddress);
                    }
                }
            }
        }
        return appCessLicDtos;
    }

    @Override
    public void updateLicenceFe(List<String> licNos) {
        List<LicenceDto> licenceDtos = licenceClient.getLicDtosByLicNos(licNos).getEntity();
        if (licenceDtos != null && !licenceDtos.isEmpty()) {
            for (LicenceDto licenceDto : licenceDtos) {
                licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_CEASED);
                licenceClient.doUpdate(licenceDto);
            }
        }
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        feEicGatewayClient.updateLicenceStatus(licenceDtos, signature.date(), signature.authorization(), signature2.date(), signature2.authorization());
    }

    @Override
    public List<String> saveCessations(List<AppCessationDto> appCessationDtos) {
        List<AppCessMiscDto> appCessMiscDtos = IaisCommonUtils.genNewArrayList();
        for(AppCessationDto appCessationDto : appCessationDtos){
            AppCessMiscDto appCessMiscDto = new AppCessMiscDto();
            String licId = appCessationDto.getWhichTodo();
            Double amount = 0.0;
            AuditTrailDto internet = AuditTrailHelper.getBatchJobDto(AppConsts.DOMAIN_INTERNET);
            String grpNo = systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_REINSTATEMENT).getEntity();
            AppSubmissionDto appSubmissionDto = licenceClient.getAppSubmissionDto(licId).getEntity();
            List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtoList = appSubmissionDto.getAppSvcRelatedInfoDtoList();
            String serviceName = appSvcRelatedInfoDtoList.get(0).getServiceName();
            HcsaServiceDto hcsaServiceDto = HcsaServiceCacheHelper.getServiceByServiceName(serviceName);
            String svcId = hcsaServiceDto.getId();
            String svcCode = hcsaServiceDto.getSvcCode();
            appSvcRelatedInfoDtoList.get(0).setServiceId(svcId);
            appSvcRelatedInfoDtoList.get(0).setServiceCode(svcCode);
            appSubmissionDto.setAppGrpNo(grpNo);
            appSubmissionDto.setAppType(ApplicationConsts.APPLICATION_TYPE_CESSATION);
            appSubmissionDto.setAmount(amount);
            appSubmissionDto.setAuditTrailDto(internet);
            appSubmissionDto.setPreInspection(true);
            appSubmissionDto.setRequirement(true);
            appSubmissionDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
            setRiskToDto(appSubmissionDto);
            AppSubmissionDto entity = applicationClient.saveSubmision(appSubmissionDto).getEntity();
            AppSubmissionDto appSubmissionDtoSave = applicationClient.saveApps(entity).getEntity();
            List<ApplicationDto> applicationDtos = appSubmissionDtoSave.getApplicationDtos();
            String appId = applicationDtos.get(0).getId();
            setMiscData(appCessationDto, appCessMiscDto, appId);
            appCessMiscDtos.add(appCessMiscDto);
        }
        List<String> listAppIds = cessationClient.saveCessation(appCessMiscDtos).getEntity();
        return listAppIds;
    }


    @Override
    public AppPremisesCorrelationDto getAppPreCorDto(String appId) {
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtoList = applicationClient.listAppPremisesCorrelation(appId).getEntity();
        return appPremisesCorrelationDtoList.get(0);
    }

    @Override
    public void updateCesation(List<AppCessationDto> appCessationDtos) {
        List<AppCessMiscDto> appCessMiscDtos = IaisCommonUtils.genNewArrayList();
//        for (AppCessationDto appCessationDto : appCessationDtos) {
//            AppCessMiscDto appCessMiscDto = new AppCessMiscDto();
//            String licId = appCessationDto.getWhichTodo();
//            ApplicationGroupDto applicationGroupDto = new ApplicationGroupDto();
//            ApplicationDto applicationDto = applicationClient.getApplicationByLicId(licId).getEntity();
//            String appGrpId = applicationDto.getAppGrpId();
//            applicationGroupDto.setId(appGrpId);
//            List<ApplicationDto> applicationDtoList = IaisCommonUtils.genNewArrayList();
//            String applicationNo = applicationDto.getApplicationNo();
//            applicationDto.setStatus("APST009");
//            applicationDtoList.add(applicationDto);
//            ApplicationDto applicationDto1 = new ApplicationDto();
//            applicationDto1.setApplicationType("APTY001");
//            applicationDto1.setApplicationNo(applicationNo);
//            applicationDto1.setStatus("APST007");
//            applicationDto1.setAppGrpId(appGrpId);
//            applicationDto1.setServiceId("35F99D15-820B-EA11-BE7D-000C29F371DC");
//            applicationDto1.setVersion(1);
//            applicationDto1.setOriginLicenceId(licId);
//            applicationDtoList.add(applicationDto1);
//            appCessMiscDto.setApplicationDto(applicationDtoList);
//            List<AppGrpPremisesDto> appGrpPremisesDto = getAppGrpPremisesDto();
//            appCessMiscDto.setAppGrpPremisesDtos(appGrpPremisesDto);
//            appCessMiscDto.setApplicationGroupDto(applicationGroupDto);
//            appCessMiscDtos.add(appCessMiscDto);
//        }
        cessationClient.updateCessation(appCessMiscDtos).getEntity();
    }

    @Override
    public List<String> listHciName() {
        List<String> hciNames = cessationClient.listHciNames().getEntity();
        return hciNames;
    }

    /*
    utils
     */
    private AppCessMiscDto setMiscData(AppCessationDto appCessationDto, AppCessMiscDto appCessMiscDto,String appId) {
        Date effectiveDate = appCessationDto.getEffectiveDate();
        String reason = appCessationDto.getReason();
        String otherReason = appCessationDto.getOtherReason();
        Boolean patNeedTrans = appCessationDto.getPatNeedTrans();
        String patientSelect = appCessationDto.getPatientSelect();
        String patHciName = appCessationDto.getPatHciName();
        String patRegNo = appCessationDto.getPatRegNo();
        String patOthers = appCessationDto.getPatOthers();
        String patNoRemarks = appCessationDto.getPatNoRemarks();

        appCessMiscDto.setAppealType(ApplicationConsts.CESSATION_TYPE_APPLICATION);
        appCessMiscDto.setEffectiveDate(effectiveDate);
        appCessMiscDto.setReason(reason);
        appCessMiscDto.setOtherReason(otherReason);
        appCessMiscDto.setPatNeedTrans(patNeedTrans);
        appCessMiscDto.setPatNoReason(patNoRemarks);
        appCessMiscDto.setPatTransType(patientSelect);
        appCessMiscDto.setAppPremCorreId(appId);
        if (!StringUtil.isEmpty(patHciName)) {
            appCessMiscDto.setPatTransTo(patHciName);
        }
        if (!StringUtil.isEmpty(patRegNo)) {
            appCessMiscDto.setPatTransTo(patRegNo);
        }
        if (!StringUtil.isEmpty(patOthers)) {
            appCessMiscDto.setPatTransTo(patOthers);
        }
        return appCessMiscDto;
    }

    private void setRiskToDto(AppSubmissionDto appSubmissionDto) {
        List<AppSvcRelatedInfoDto> appSvcRelatedInfoDtos = appSubmissionDto.getAppSvcRelatedInfoDtoList();
        List<RiskAcceptiionDto> riskAcceptiionDtoList = new ArrayList();
        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            RiskAcceptiionDto riskAcceptiionDto = new RiskAcceptiionDto();
            riskAcceptiionDto.setScvCode(appSvcRelatedInfoDto.getServiceCode());
            riskAcceptiionDto.setApptype(appSubmissionDto.getAppType());
            riskAcceptiionDtoList.add(riskAcceptiionDto);
        }

        List<RiskResultDto> riskResultDtoList = appConfigClient.getRiskResult(riskAcceptiionDtoList).getEntity();

        for (AppSvcRelatedInfoDto appSvcRelatedInfoDto : appSvcRelatedInfoDtos) {
            String serviceCode = appSvcRelatedInfoDto.getServiceCode();
            RiskResultDto riskResultDto = getRiskResultDtoByServiceCode(riskResultDtoList, serviceCode);
            if (riskResultDto != null) {
                appSvcRelatedInfoDto.setScore(riskResultDto.getScore());
                appSvcRelatedInfoDto.setDoRiskDate(riskResultDto.getDoRiskDate());
            }
        }
    }

    private RiskResultDto getRiskResultDtoByServiceCode(List<RiskResultDto> riskResultDtoList, String serviceCode) {
        RiskResultDto result = null;
        if (riskResultDtoList == null || StringUtil.isEmpty(serviceCode)) {
            return result;
        }
        for (RiskResultDto riskResultDto : riskResultDtoList) {
            if (serviceCode.equals(riskResultDto.getSvcCode())) {
                result = riskResultDto;
            }
        }
        return result;
    }
}
