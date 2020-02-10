package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppCessDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppCessHciDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.service.CessationService;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author weilu
 * @date 2020/2/7 13:17
 */
@Service
public class CessationServiceImpl implements CessationService {

    @Autowired
    private LicenceClient licenceClient;

    @Override
    public List<AppCessDto> getAppCessDtosByLicIds(List<String> licIds) {
        List<AppCessDto> appCessDtos = new ArrayList<>();
        if(licIds!=null&&!licIds.isEmpty()){
            for(String licId :licIds){
                AppCessDto appCessDto = new AppCessDto();
                LicenceDto licenceDto = licenceClient.getLicBylicId(licId).getEntity();
                List<PremisesDto> premisesDtos = licenceClient.getPremisesDto(licId).getEntity();
                String svcName = licenceDto.getSvcName();
                String licenceNo = licenceDto.getLicenceNo();
                appCessDto.setLicenceNo(licenceNo);
                appCessDto.setSvcName(svcName);
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
}
