package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppCessDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppInsRepDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.service.CessationService;
import com.ecquaria.cloud.moh.iais.service.client.CessationClient;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author weilu
 * @date 2020/2/7 13:17
 */
@Service
public class CessationServiceImpl implements CessationService {

    private LicenceClient licenceClient;

    @Override
    public AppCessDto getAppCessDtoByLicNo(String licId) {
        LicenceDto licenceDto = licenceClient.getLicBylicId(licId).getEntity();
        List<PremisesDto> premisesDtos = licenceClient.getPremisesDto(licId).getEntity();
        PremisesDto premisesDto = premisesDtos.get(0);
        String svcName = licenceDto.getSvcName();
        String licenceNo = licenceDto.getLicenceNo();

        return null;
    }
}
