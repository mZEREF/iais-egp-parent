package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessHciDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.withdrawn.WithdrawnDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
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
    public List<AppCessationDto> getCessationByIds(List<String> licIds) {
        List<AppCessationDto> appCessationDtos = new ArrayList<>();
        String type = ApplicationConsts.APPLICATION_TYPE_CESSATION;
        String status = ApplicationConsts.APPLICATION_STATUS_CESSATION_PENDING_APPROVE;
        for(String licId :licIds){
            AppCessMiscDto appCessMiscDto = cessationClient.getCessationByLicId(type, status, licId).getEntity();
        }
        return appCessationDtos;
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
            applicationDto.setApplicationType(ApplicationConsts.APPLICATION_TYPE_CESSATION);
            applicationDto.setApplicationNo(appNo);
            applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_CESSATION_PENDING_APPROVE);
            applicationDto.setServiceId("35F99D15-820B-EA11-BE7D-000C29F371DC");
            applicationDto.setLicenceId(licId);
            applicationDto.setVersion(1);
            List<ApplicationDto> applicationDtos = new ArrayList<>();
            applicationDtos.add(applicationDto);
            appCessMiscDto.setApplicationGroupDto(applicationGroupDto);
            appCessMiscDto.setApplicationDto(applicationDtos);
            setMiscData(appCessationDto,appCessMiscDto);
            appCessMiscDtos.add(appCessMiscDto);
        }
        String entity = cessationClient.saveCessation(appCessMiscDtos).getEntity();
    }

    @Override
    public void saveWithdrawn(WithdrawnDto withdrawnDto) {
        ApplicationDto applicationDto = applicationClient.getApplicationById(withdrawnDto.getApplicationId()).getEntity();
        applicationDto.setId(null);
        applicationDto.setApplicationNo(systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_WITHDRAWAL).getEntity());
        withdrawnDto.setApplicationDto(applicationDto);
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
    public AppPremisesCorrelationDto getAppPreCorDto(String appId) {
        List<AppPremisesCorrelationDto> appPremisesCorrelationDtoList = applicationClient.listAppPremisesCorrelation(appId).getEntity();
        return appPremisesCorrelationDtoList.get(0);
    }

    @Override
    public void updateCesation(List<AppCessationDto> appCessationDtos) {
        List<AppCessMiscDto> appCessMiscDtos = new ArrayList<>();
        for (AppCessationDto appCessationDto : appCessationDtos) {
            AppCessMiscDto appCessMiscDto = new AppCessMiscDto();
            String licId = appCessationDto.getWhichTodo();
            ApplicationGroupDto applicationGroupDto = new ApplicationGroupDto();
            ApplicationDto applicationDto = applicationClient.getApplicationByLicId(licId).getEntity();
            String appGrpId = applicationDto.getAppGrpId();
            applicationGroupDto.setId(appGrpId);
            List<ApplicationDto> applicationDtoList = new ArrayList<>();
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
        applicationGroupDto.setLicenseeId("36F8537B-FE17-EA11-BE78-000C29D29DB0");
        applicationGroupDto.setIsBundledFee(9966);
        applicationGroupDto.setIsCharitable(0);
        applicationGroupDto.setIsByGiro(0);
        applicationGroupDto.setIsGrpLic(0);
        applicationGroupDto.setDeclStmt("N");
        applicationGroupDto.setSubmitBy("C55C9E62-750B-EA11-BE7D-000C29F371DC");
        applicationGroupDto.setAppType(appType);
        //applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
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
        if(!StringUtil.isEmpty(patHciName)){
            appCessMiscDto.setPatTransTo(patHciName);
        }if(!StringUtil.isEmpty(patRegNo)){
            appCessMiscDto.setPatTransTo(patRegNo);
        }if(!StringUtil.isEmpty(patOthers)){
            appCessMiscDto.setPatTransTo(patOthers);
        }
        return appCessMiscDto;
    }
}
