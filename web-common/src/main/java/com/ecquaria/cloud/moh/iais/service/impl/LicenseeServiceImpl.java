package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.service.LicenseeService;
import com.ecquaria.cloud.moh.iais.service.client.LicenseeClient;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public List<String> getLicenseeMobiles(String licenseeId) {
        return licenseeClient.getLicenseeMobiles(licenseeId).getBody();
    }
}
