package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessHciDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.cessation.AppCessMiscDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.service.CessationService;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
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
        String appNo = "AN191226000259";
        ApplicationGroupDto applicationGroupDto = getApplicationGroupDto(appNo);
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
        AppCessMiscDto entity = cessationClient.saveCessation(appCessMiscDto).getEntity();
    }

    private ApplicationGroupDto getApplicationGroupDto(String appNo){
        ApplicationGroupDto applicationGroupDto=new ApplicationGroupDto();
        applicationGroupDto.setSubmitDt(new Date());
        applicationGroupDto.setGroupNo(appNo);
        applicationGroupDto.setStatus("AGST006");
        applicationGroupDto.setAmount(0.0);
        applicationGroupDto.setIsPreInspection(1);
        applicationGroupDto.setIsInspectionNeeded(1);
        applicationGroupDto.setLicenseeId("36F8537B-FE17-EA11-BE78-000C29D29DB0");
        applicationGroupDto.setIsBundledFee(0);
        applicationGroupDto.setIsCharitable(0);
        applicationGroupDto.setIsByGiro(0);
        applicationGroupDto.setIsGrpLic(0);
        applicationGroupDto.setDeclStmt("N");
        applicationGroupDto.setSubmitBy("C55C9E62-750B-EA11-BE7D-000C29F371DC");
        applicationGroupDto.setAppType(ApplicationConsts.APPLICATION_TYPE_APPEAL);
        return applicationGroupDto;

    }
}
