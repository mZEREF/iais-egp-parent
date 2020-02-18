package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.service.LicenceViewService;
import com.ecquaria.cloud.moh.iais.service.client.LicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.OrganizationLienceseeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * LicenceViewServiceImpl
 *
 * @author suocheng
 * @date 2/18/2020
 */
@Service
@Slf4j
public class LicenceViewServiceImpl implements LicenceViewService {

    @Autowired
    private LicenceClient licenceClient;

    private OrganizationLienceseeClient organizationLienceseeClient;

    @Override
    public LicenceViewDto getLicenceViewDtoByLicenceId(String licenceId) {
        LicenceViewDto licenceViewDto =  licenceClient.getLicenceViewByLicenceId(licenceId).getEntity();
        if(licenceViewDto!=null){
            LicenceDto licenceDto = licenceViewDto.getLicenceDto();
            String licenseeId = licenceDto.getLicenseeId();
            LicenseeDto licenseeDto = this.getLicenseeDtoBylicenseeId(licenseeId);
            licenceViewDto.setLicenseeDto(licenseeDto);
        }
        return licenceViewDto;
    }

    @Override
    public LicenseeDto getLicenseeDtoBylicenseeId(String licenseeId) {
        return organizationLienceseeClient.getLicenseeById(licenseeId).getEntity();
    }
}
