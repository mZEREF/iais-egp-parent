package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessHciDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
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
        if(licIds!=null&&!licIds.isEmpty()){
            for(String licId :licIds){
                AppCessLicDto appCessDto = new AppCessLicDto();
                LicenceDto licenceDto = licenceClient.getLicBylicId(licId).getEntity();
                List<PremisesDto> premisesDtos = licenceClient.getPremisesDto(licId).getEntity();
                String svcName = licenceDto.getSvcName();
                String licenceNo = licenceDto.getLicenceNo();
                appCessDto.setLicenceNo(licenceNo);
                appCessDto.setSvcName(svcName);
                appCessDto.setLicenceId(licId);
                List<AppCessHciDto> appCessHciDtos = new ArrayList<>();
                if(premisesDtos!=null&&!premisesDtos.isEmpty()){
                    for(PremisesDto premisesDto :premisesDtos){
                        AppCessHciDto appCessHciDto = new AppCessHciDto();
                        String hciName = premisesDto.getHciName();
                        String hciAddress=premisesDto.getHciName()+"/"+premisesDto.getStreetName()+" "+premisesDto.getUnitNo()+","+premisesDto.getBuildingName()+
                                " "+premisesDto.getBlkNo()+",#"+premisesDto.getFloorNo()+"Singapore "+premisesDto.getPostalCode();
                        appCessHciDto.setHciName(hciName);
                        appCessHciDto.setHciAddress(hciAddress);
                        appCessHciDtos.add(appCessHciDto);
                    }
                }
                appCessDto.setAppCessHciDtos(appCessHciDtos);
                appCessDtos.add(appCessDto);
            }
            return appCessDtos;
        }else {
            return null;
        }

    }

    @Override
    public void saveCessations(AppCessMiscDto appCessMiscDto,String licId) {
        String appNo = systemAdminClient.applicationNumber(ApplicationConsts.APPLICATION_TYPE_REINSTATEMENT).getEntity();
        ApplicationGroupDto applicationGroupDto = getApplicationGroupDto(appNo,licId);
        List<AppGrpPremisesDto> appGrpPremisesDto = getAppGrpPremisesDto();
        appCessMiscDto.setAppGrpPremisesDtos(appGrpPremisesDto);
        ApplicationDto applicationDto =new ApplicationDto();
        applicationDto.setApplicationType("APTY001");
        applicationDto.setApplicationNo(appNo);
        applicationDto.setStatus("APST007");
        applicationDto.setServiceId("35F99D15-820B-EA11-BE7D-000C29F371DC");
        applicationDto.setVersion(1);
        applicationDto.setLicenceId(licId);
        List<ApplicationDto> applicationDtos=new ArrayList<>();
        applicationDtos.add(applicationDto);
        appCessMiscDto.setApplicationGroupDto(applicationGroupDto);
        appCessMiscDto.setApplicationDto(applicationDtos);
        cessationClient.saveCessation(appCessMiscDto).getEntity();
    }

    @Override
    public void updateCesation(AppCessMiscDto appCessMiscDto, List<String> licIds) {
        List<String> appNos = new ArrayList<>();
        for(String licId :licIds){
            List<ApplicationDto> applicationDtos = applicationClient.getApplicationByLicId(licId).getEntity();
            String applicationNo = applicationDtos.get(0).getApplicationNo();
            String appId = applicationDtos.get(0).getId();
            appNos.add(applicationNo);
            List<AppPremisesCorrelationDto> entity = applicationClient.listAppPremisesCorrelation(appId).getEntity();
        }

    }


    private ApplicationGroupDto getApplicationGroupDto(String appNo,String licId){
        ApplicationGroupDto applicationGroupDto=new ApplicationGroupDto();
        applicationGroupDto.setSubmitDt(new Date());
        applicationGroupDto.setGroupNo(appNo);
        applicationGroupDto.setStatus(ApplicationConsts.APPLICATION_GROUP_STATUS_SUBMITED);
        applicationGroupDto.setAmount(0.0);
        applicationGroupDto.setIsPreInspection(1);
        applicationGroupDto.setIsInspectionNeeded(1);
        applicationGroupDto.setLicenseeId(licId);
        applicationGroupDto.setIsBundledFee(0);
        applicationGroupDto.setIsCharitable(0);
        applicationGroupDto.setIsByGiro(0);
        applicationGroupDto.setIsGrpLic(0);
        applicationGroupDto.setDeclStmt("N");
        applicationGroupDto.setSubmitBy("C55C9E62-750B-EA11-BE7D-000C29F371DC");
        applicationGroupDto.setAppType(ApplicationConsts.APPLICATION_TYPE_APPEAL);
        return applicationGroupDto;

    }
    private List<AppGrpPremisesDto> getAppGrpPremisesDto(){
        List<AppGrpPremisesDto> appGrpPremisesDtos = new ArrayList<>();
        AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
        appGrpPremisesDto.setPremisesType(ApplicationConsts.PREMISES_TYPE_ON_SITE);
        appGrpPremisesDto.setPostalCode("789789");
        appGrpPremisesDto.setAddrType(ApplicationConsts.ADDRESS_TYPE_APT_BLK);
        appGrpPremisesDto.setStreetName("Lor 27 Geylang");
        appGrpPremisesDtos.add(appGrpPremisesDto);
        return appGrpPremisesDtos;
    }
}
