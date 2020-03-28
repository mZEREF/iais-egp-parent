package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicPremisesReqForInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiApplicationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.system.ProcessFileTrackDto;

import java.util.List;

/**
 * RequestForInfomationService
 *
 * @author junyu
 * @date 2019/12/16
 */
public interface RequestForInformationService {
    List<SelectOption> getAppTypeOption();
    List<SelectOption> getAppStatusOption();
    List<SelectOption> getLicSvcTypeOption();
    List<SelectOption> getLicSvcSubTypeOption();
    List<SelectOption> getLicStatusOption();
    List<String> getActionBysByLicPremCorrId(String licPremCorrId);
    SearchResult<RfiApplicationQueryDto> appDoQuery(SearchParam searchParam);
    SearchResult<RfiLicenceQueryDto> licenceDoQuery(SearchParam searchParam);
    List<String> getSvcNamesByType(String type);
    LicPremisesReqForInfoDto updateLicPremisesReqForInfo(LicPremisesReqForInfoDto licPremisesReqForInfoDto);

    LicPremisesReqForInfoDto createLicPremisesReqForInfo(LicPremisesReqForInfoDto licPremisesReqForInfoDto);
    List<LicPremisesReqForInfoDto> searchLicPremisesReqForInfo(String licPremId);
    LicPremisesReqForInfoDto getLicPreReqForInfo(String id);
    void deleteLicPremisesReqForInfo(String id);
    void acceptLicPremisesReqForInfo(LicPremisesReqForInfoDto licPremisesReqForInfoDto);
    List<LicPremisesReqForInfoDto> getAllReqForInfo();
    byte[] downloadFile(String fileRepoId);
    void compress();
    boolean download(ProcessFileTrackDto processFileTrackDto , String fileName,String groupPath,String submissionId);
    void delete();
}
