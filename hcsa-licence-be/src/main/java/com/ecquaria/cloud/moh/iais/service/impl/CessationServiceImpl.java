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
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.service.CessationService;
import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * @author weilu
 * @date 2020/2/26 16:29
 */
@Service
public class CessationServiceImpl implements CessationService {
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Autowired
    private CessationClient cessationClient;
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private BeEicGatewayClient beEicGatewayClient;

    @Value("${iais.hmac.keyId}")
    private String keyId;
    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;
    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Override
    public List<String> getActiveLicence(List<String> licIds) {

        return null;
    }

    @Override
    public List<AppCessLicDto> getAppCessDtosByLicIds(List<String> licIds) {
        List<AppCessLicDto> appCessDtos = IaisCommonUtils.genNewArrayList();
        if (licIds != null && !licIds.isEmpty()) {
            for (String licId : licIds) {
                AppCessLicDto appCessDto = new AppCessLicDto();
                LicenceDto licenceDto = hcsaLicenceClient.getLicenceDtoById(licId).getEntity();
                List<PremisesDto> premisesDtos = hcsaLicenceClient.getPremisess(licId).getEntity();
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
                        String hciAddress = IaisCommonUtils.appendPremisesAddress(blkNo,streetName,buildingName,floorNo,unitNo,postalCode);
                        AppCessHciDto appCessHciDto = new AppCessHciDto();
                        String hciName = premisesDto.getHciName();
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
        HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
        HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
        List<AppCessMiscDto> appCessMiscDtos = IaisCommonUtils.genNewArrayList();
        for (int i = 0; i < appCessationDtos.size(); i++) {
            AppCessationDto appCessationDto = appCessationDtos.get(0);
            AppCessMiscDto appCessMiscDto = new AppCessMiscDto();
            String licId = appCessationDto.getWhichTodo();
            String appNo = beEicGatewayClient.getAppNo(ApplicationConsts.APPLICATION_TYPE_REINSTATEMENT,signature.date(), signature.authorization(), signature2.date(), signature2.authorization()).getEntity();
            ApplicationGroupDto applicationGroupDto = getApplicationGroupDto(appNo,ApplicationConsts.APPLICATION_TYPE_APPEAL);
            List<AppGrpPremisesDto> appGrpPremisesDto = getAppGrpPremisesDto();
            appCessMiscDto.setAppGrpPremisesDtos(appGrpPremisesDto);
            ApplicationDto applicationDto = new ApplicationDto();
            applicationDto.setApplicationType(ApplicationConsts.APPLICATION_TYPE_CESSATION);
            applicationDto.setApplicationNo(appNo+"-0"+i);
            applicationDto.setStatus(ApplicationConsts.APPLICATION_STATUS_APPROVED);
            applicationDto.setServiceId("35F99D15-820B-EA11-BE7D-000C29F371DC");
            applicationDto.setVersion(1);
            applicationDto.setOriginLicenceId(licId);
            List<ApplicationDto> applicationDtos = IaisCommonUtils.genNewArrayList();
            applicationDtos.add(applicationDto);
            appCessMiscDto.setApplicationGroupDto(applicationGroupDto);
            appCessMiscDto.setApplicationDto(applicationDtos);
            setMiscData(appCessationDto,appCessMiscDto);
            appCessMiscDtos.add(appCessMiscDto);
        }
        cessationClient.saveCessation(appCessMiscDtos).getEntity();
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
//            List<ApplicationDto> applicationDtos = applicationClient.getApplicationByLicId(licId).getEntity();
//            ApplicationDto applicationDto = applicationDtos.get(0);
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
//            applicationDto1.setLicenceId(licId);
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
    public void updateLicence(List<String> licNos) {
        List<LicenceDto> licenceDtos = hcsaLicenceClient.getLicDtosByLicNos(licNos).getEntity();
        List<LicenceDto> licenceDtoNew = IaisCommonUtils.genNewArrayList();
        if(licenceDtos!=null&&!licenceDtos.isEmpty()){
            for(LicenceDto licenceDto :licenceDtos){
                licenceDto.setStatus(ApplicationConsts.LICENCE_STATUS_CEASED);
                licenceDtoNew.add(licenceDto);
            }
        }
        hcsaLicenceClient.updateLicences(licenceDtoNew);
    }

    @Override
    public List<String> listLicIdsCeased(List<String> licIds) {
        for(String licId :licIds){

        }
        return null;
    }


    private ApplicationGroupDto getApplicationGroupDto(String appNo,String appType) {
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
        applicationGroupDto.setGrpLic(false);
        applicationGroupDto.setDeclStmt("N");
        applicationGroupDto.setSubmitBy("C55C9E62-750B-EA11-BE7D-000C29F371DC");
        applicationGroupDto.setAppType(appType);
        applicationGroupDto.setPmtStatus(ApplicationConsts.PAYMENT_STATUS_NO_NEED_PAYMENT);
        return applicationGroupDto;

    }

    private List<AppGrpPremisesDto> getAppGrpPremisesDto() {
        List<AppGrpPremisesDto> appGrpPremisesDtos = IaisCommonUtils.genNewArrayList();
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
