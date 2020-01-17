package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;

import java.util.List;

/**
 * RequestForInfomationService
 *
 * @author junyu
 * @date 2019/12/16
 */
public interface ResponseForInformationService {

    List<LicPremisesReqForInfoDto> searchLicPreRfiBylicenseeId(String licenseeId);
    LicPremisesReqForInfoDto getLicPreReqForInfo(String id);
    LicPremisesReqForInfoDto acceptLicPremisesReqForInfo(LicPremisesReqForInfoDto licPremisesReqForInfoDto);
    Boolean saveFile(String  str);
    String getData(LicPremisesReqForInfoDto licPremisesReqForInfoDto);
    void compressFile();

}
