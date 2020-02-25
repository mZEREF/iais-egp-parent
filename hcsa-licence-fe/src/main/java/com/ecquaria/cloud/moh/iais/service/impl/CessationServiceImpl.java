package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessHciDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloud.moh.iais.service.CessationService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Override
    public List<AppCessLicDto> getAppCessDtosByLicIds(List<String> licIds) {
        List<AppCessLicDto> appCessDtos = new ArrayList<>();
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
                List<AppCessHciDto> appCessHciDtos = new ArrayList<>();
                if (premisesDtos != null && !premisesDtos.isEmpty()) {
                    for (PremisesDto premisesDto : premisesDtos) {
                        AppCessHciDto appCessHciDto = new AppCessHciDto();
                        String hciName = premisesDto.getHciName();
                        String hciAddress = premisesDto.getHciName() + "/" + premisesDto.getStreetName() + " " + premisesDto.getUnitNo() + "," + premisesDto.getBuildingName() +
                                " " + premisesDto.getBlkNo() + ",#" + premisesDto.getFloorNo() + "Singapore " + premisesDto.getPostalCode();
                        appCessHciDto.setHciName(hciName);
                        appCessHciDto.setHciAddress(hciAddress);
                        appCessHciDtos.add(appCessHciDto);
                    }
                }
                appCessDto.setAppCessHciDtos(appCessHciDtos);
                appCessDtos.add(appCessDto);
            }
            return appCessDtos;
        } else {
            return null;
        }

    }

    @Override
    public void saveCessations(List<AppCessationDto> appCessationDtos) {
        List<AppCessMiscDto> appCessMiscDtos = new ArrayList<>();
        for (AppCessationDto appCessationDto : appCessationDtos) {
            AppCessMiscDto appCessMiscDto = new AppCessMiscDto();
            String licId = appCessationDto.getWhichTodo();
            String appNo = systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_REINSTATEMENT).getEntity();
            ApplicationGroupDto applicationGroupDto = getApplicationGroupDto(appNo, licId,ApplicationConsts.APPLICATION_TYPE_APPEAL);
            List<AppGrpPremisesDto> appGrpPremisesDto = getAppGrpPremisesDto();
            appCessMiscDto.setAppGrpPremisesDtos(appGrpPremisesDto);
            ApplicationDto applicationDto = new ApplicationDto();
            applicationDto.setApplicationType("cessation");
            applicationDto.setApplicationNo(appNo);
            applicationDto.setStatus(ApplicationConsts.APPLICATION_TYPE_CESSATION);
            applicationDto.setServiceId("35F99D15-820B-EA11-BE7D-000C29F371DC");
            applicationDto.setVersion(1);
            applicationDto.setLicenceId(licId);
            List<ApplicationDto> applicationDtos = new ArrayList<>();
            applicationDtos.add(applicationDto);
            appCessMiscDto.setApplicationGroupDto(applicationGroupDto);
            appCessMiscDto.setApplicationDto(applicationDtos);
            setMiscData(appCessationDto,appCessMiscDto);
            appCessMiscDtos.add(appCessMiscDto);
        }
        cessationClient.saveCessation(appCessMiscDtos).getEntity();
    }

    @Override
    public void saveWithdrawn(WithdrawnDto withdrawnDto, String appId) {
        List<ApplicationDto> applicationDtoList = new ArrayList<>();
        ApplicationDto applicationDto = applicationClient.getApplicationById(appId).getEntity();
        applicationDtoList.add(applicationDto);
        withdrawnDto.setApplicationDtoList(applicationDtoList);;
        ApplicationGroupDto applicationGroupDto = getApplicationGroupDto(applicationDto.getApplicationNo(),applicationDto.getLicenceId(),ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL);
        withdrawnDto.setApplicationGroupDto(applicationGroupDto);
        List<AppGrpPremisesDto> appGrpPremisesDtoList = new ArrayList<>();
        AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
        appGrpPremisesDto.setPremisesType(ApplicationConsts.PREMISES_TYPE_ON_SITE);
        appGrpPremisesDto.setPostalCode("78979");
        appGrpPremisesDto.setAddrType(ApplicationConsts.ADDRESS_TYPE_APT_BLK);
        appGrpPremisesDto.setStreetName("Lor 27 Gey");
        appGrpPremisesDtoList.add(appGrpPremisesDto);
        withdrawnDto.setAppGrpPremisesDtoList(appGrpPremisesDtoList);
        cessationClient.saveWithdrawn(withdrawnDto);
    }

    @Override
    public void updateCesation(List<AppCessationDto> appCessationDtos) {
        List<AppCessMiscDto> appCessMiscDtos = new ArrayList<>();
        for (AppCessationDto appCessationDto : appCessationDtos) {
            AppCessMiscDto appCessMiscDto = new AppCessMiscDto();
            String licId = appCessationDto.getWhichTodo();
            ApplicationGroupDto applicationGroupDto = new ApplicationGroupDto();
            List<ApplicationDto> appDtos = applicationClient.getApplicationByLicId(licId).getEntity();
            String appGrpId = appDtos.get(0).getAppGrpId();
            applicationGroupDto.setId(appGrpId);
            List<ApplicationDto> applicationDtoList = new ArrayList<>();
            List<ApplicationDto> applicationDtos = applicationClient.getApplicationByLicId(licId).getEntity();
            ApplicationDto applicationDto = applicationDtos.get(0);
            String applicationNo = applicationDto.getApplicationNo();
            applicationDto.setStatus("APST009");
            applicationDtoList.add(applicationDto);
            ApplicationDto applicationDto1 = new ApplicationDto();
            applicationDto1.setApplicationType("APTY001");
            applicationDto1.setApplicationNo(applicationNo);
            applicationDto1.setStatus("APST007");
            applicationDto1.setAppGrpId(appGrpId);
            applicationDto1.setServiceId("35F99D15-820B-EA11-BE7D-000C29F371DC");
            applicationDto1.setVersion(1);
            applicationDto1.setLicenceId(licId);
            applicationDtoList.add(applicationDto1);
            appCessMiscDto.setApplicationDto(applicationDtoList);
            List<AppGrpPremisesDto> appGrpPremisesDto = getAppGrpPremisesDto();
            appCessMiscDto.setAppGrpPremisesDtos(appGrpPremisesDto);
            appCessMiscDto.setApplicationGroupDto(applicationGroupDto);
            appCessMiscDtos.add(appCessMiscDto);
        }
        cessationClient.updateCessation(appCessMiscDtos).getEntity();

    }


    private ApplicationGroupDto getApplicationGroupDto(String appNo, String licId,String appType) {
        ApplicationGroupDto applicationGroupDto = new ApplicationGroupDto();
        applicationGroupDto.setSubmitDt(new Date());
        applicationGroupDto.setGroupNo(appNo);
        applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
        applicationGroupDto.setAmount(0.0);
        applicationGroupDto.setIsPreInspection(1);
        applicationGroupDto.setIsInspectionNeeded(1);
        applicationGroupDto.setLicenseeId(licId);
        applicationGroupDto.setIsBundledFee(9966);
        applicationGroupDto.setIsCharitable(0);
        applicationGroupDto.setIsByGiro(0);
        applicationGroupDto.setIsGrpLic(0);
        applicationGroupDto.setDeclStmt("N");
        applicationGroupDto.setSubmitBy("C55C9E62-750B-EA11-BE7D-000C29F371DC");
        applicationGroupDto.setAppType(appType);
        return applicationGroupDto;

    }

    private List<AppGrpPremisesDto> getAppGrpPremisesDto() {
        List<AppGrpPremisesDto> appGrpPremisesDtos = new ArrayList<>();
        AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
        appGrpPremisesDto.setPremisesType(ApplicationConsts.PREMISES_TYPE_ON_SITE);
        appGrpPremisesDto.setPostalCode("999666");
        appGrpPremisesDto.setAddrType(ApplicationConsts.ADDRESS_TYPE_APT_BLK);
        appGrpPremisesDto.setStreetName("Lor 27 Geylang");
        appGrpPremisesDtos.add(appGrpPremisesDto);
        return appGrpPremisesDtos;
    }

    private AppCessMiscDto setMiscData(AppCessationDto appCessationDto,AppCessMiscDto appCessMiscDto){
        Date effectiveDate = appCessationDto.getEffectiveDate();
        String cessationReason = appCessationDto.getCessationReason();
        String otherReason = appCessationDto.getOtherReason();
        String patientSelect = appCessationDto.getPatientSelect();
        String patHciName = appCessationDto.getPatHciName();
        String patRegNo = appCessationDto.getPatRegNo();
        String patOthers = appCessationDto.getPatOthers();
        String patNoRemarks = appCessationDto.getPatNoRemarks();
        appCessMiscDto.setEffectiveDate(effectiveDate);
        appCessMiscDto.setReason(cessationReason);
        appCessMiscDto.setAppealType(ApplicationConsts.CESSATION_TYPE_APPLICATION);
        return appCessMiscDto;
    }
}
