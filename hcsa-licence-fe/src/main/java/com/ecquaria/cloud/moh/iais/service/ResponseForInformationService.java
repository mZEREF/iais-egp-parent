package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.EicRequestTrackingDto;
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
    void saveFile(String  str) throws IOException;
    String getData(LicPremisesReqForInfoDto licPremisesReqForInfoDto);
    void compressFile(String licPreId);
    String createBeRfiLicProcessFileTrack(ProcessFileTrackDto processFileTrackDto);
    void   updateSysAdmEicRequestTrackingDto(EicRequestTrackingDto licEicRequestTrackingDto);

}
