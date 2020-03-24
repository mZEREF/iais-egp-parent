package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.*;

import java.util.List;

/****
 *
 *   @date 12/26/2019
 *   @author zixian
 */
public interface RequestForChangeService {
    List<PremisesListQueryDto> getPremisesList(String licenseeId);

    AppSubmissionDto getAppSubmissionDtoByLicenceId(String licenceId);

    List<ApplicationDto> getAppByLicIdAndExcludeNew(String licenceId);

    AppSubmissionDto submitChange(AppSubmissionDto appSubmissionDto);

    String getApplicationGroupNumber(String appType);
    
    void upDateLicStatus(LicenceDto licenceDto);

    void saveLicence(LicenceDto licenceDto);

    List<LicenseeKeyApptPersonDto> getLicenseeKeyApptPersonDtoListByUen(String uenNo);

    LicenceDto getByLicNo(String licNo);

    List<LicenseeKeyApptPersonDto> getLicenseeKeyApptPersonDtoListByLicenseeId(String licenseeId);

    List<PersonnelListQueryDto> getLicencePersonnelListQueryDto(String licenseeId);

    List<AppSubmissionDto> getAppSubmissionDtoByLicenceIds(List<String> licenceIds);

    List<AppSubmissionDto> saveAppsBySubmissionDtos(List<AppSubmissionDto> appSubmissionDtos);

    LicenceDto getLicenceDtoByLicenceId(String licenceId);

    LicenseeIndividualDto getLicIndByNRIC(String nric);

    LicenseeDto getLicenseeByUenNo(String uenNo);


    SearchResult<PersonnelListQueryDto> psnDoQuery(SearchParam searchParam);
}
