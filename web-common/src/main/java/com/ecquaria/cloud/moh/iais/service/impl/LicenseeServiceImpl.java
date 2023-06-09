package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.service.LicenseeService;
import com.ecquaria.cloud.moh.iais.service.client.LicenseeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * LicenseeServiceImpl
 *
 * @author suocheng
 * @date 3/12/2020
 */
@Service
@Slf4j
public class LicenseeServiceImpl implements LicenseeService {
    @Autowired
    private LicenseeClient licenseeClient;

    @Override
    public LicenseeDto getLicenseeDtoById(String licenseeId) {
        return licenseeClient.getLicenseeDtoById(licenseeId).getEntity();
    }

    @Override
    public List<String> getLicenseeEmails(String licenseeId) {
        return licenseeClient.getLicenseeEmails(licenseeId).getBody();
    }

    @Override
    public List<String> getLicenseeEmails(String licenseeId, List<String> roles) {
        List<OrgUserDto> userDtos = licenseeClient.getLicenseeAccountByRolesAndLicenseeId(licenseeId, roles).getBody();
        if (IaisCommonUtils.isEmpty(userDtos)){
            return null;
        }
        List<String> emailList = IaisCommonUtils.genNewArrayList(userDtos.size());
        for (OrgUserDto ou : userDtos) {
            emailList.add(ou.getEmail());
        }
        return emailList;
    }

    @Override
    public List<OrgUserDto> getLicenseeAccountByRolesAndLicenseeId(String licenseeId, List<String> roleIds){
        return licenseeClient.getLicenseeAccountByRolesAndLicenseeId(licenseeId,roleIds).getBody();
    }

    @Override
    public List<String> getLicenseeMobiles(String licenseeId) {
        return licenseeClient.getLicenseeMobiles(licenseeId).getBody();
    }
}
