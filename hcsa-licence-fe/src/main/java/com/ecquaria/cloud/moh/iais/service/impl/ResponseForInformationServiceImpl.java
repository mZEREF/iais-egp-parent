package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.service.ResponseForInformationService;
import com.ecquaria.cloud.moh.iais.service.client.FileRepoClient;
import com.ecquaria.cloud.moh.iais.service.client.ResponseForInformationClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ResponseForInformationServiceImpl
 *
 * @author junyu
 * @date 2019/12/30
 */
@Service
@Slf4j
public class ResponseForInformationServiceImpl implements ResponseForInformationService {
    @Autowired
    ResponseForInformationClient responseForInformationClient;
    @Autowired
    FileRepoClient fileRepoClient;


    @Override
    public List<LicPremisesReqForInfoDto> searchLicPreRfiBylicenseeId(String licenseeId) {
        return responseForInformationClient.searchLicPreRfiBylicenseeId(licenseeId).getEntity();
    }

    @Override
    public LicPremisesReqForInfoDto getLicPreReqForInfo(String id) {
        return responseForInformationClient.getLicPreReqForInfo(id).getEntity();
    }



    @Override
    public void acceptLicPremisesReqForInfo(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        responseForInformationClient.acceptLicPremisesReqForInfo(licPremisesReqForInfoDto);
    }

    @Override
    public LicPremisesReqForInfoDto updateLicPremisesReqForInfo(LicPremisesReqForInfoDto licPremisesReqForInfoDto) {
        return responseForInformationClient.updateLicPremisesReqForInfoFe(licPremisesReqForInfoDto).getEntity();
    }
}
