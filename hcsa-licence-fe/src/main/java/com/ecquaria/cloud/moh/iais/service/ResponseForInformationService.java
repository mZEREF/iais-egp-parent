package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;

import java.io.IOException;
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
    void saveFile(LicPremisesReqForInfoDto licPremisesReqForInfoDto) throws IOException;
    String getData(LicPremisesReqForInfoDto licPremisesReqForInfoDto);
    void compressFile(String rfiId);
    String createBeRfiLicProcessFileTrack(ProcessFileTrackDto processFileTrackDto);

}
