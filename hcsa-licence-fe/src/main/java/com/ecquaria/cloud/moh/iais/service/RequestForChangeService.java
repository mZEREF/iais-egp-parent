package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeKeyApptPersonDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesListQueryDto;
import sop.webflow.rt.api.Process;

import java.util.List;

/****
 *
 *   @date 12/26/2019
 *   @author zixian
 */
public interface RequestForChangeService {
    List<PremisesListQueryDto> getPremisesList(String licenseeId);

    AppSubmissionDto getAppSubmissionDtoByLicenceId(String licenceId);

    ApplicationDto getApplicationByLicenceId(String licenceId);

    AppSubmissionDto submitChange(AppSubmissionDto appSubmissionDto, Process process);

    String getApplicationGroupNumber(String appType);
    
    void upDateLicStatus(LicenceDto licenceDto);


    List<LicenseeKeyApptPersonDto> getLicenseeKeyApptPersonDtoListByUen(String uenNo);

    LicenceDto getByLicNo(String licNo);

    List<LicenseeKeyApptPersonDto> getLicenseeKeyApptPersonDtoListByLicenseeId(String licenseeId);

    List<PersonnelListQueryDto> getLicencePersonnelListQueryDto(String licenseeId);
    void sendEmail();

    List<AppSubmissionDto> getAppSubmissionDtoByLicenceIds(List<String> licenceIds);

    List<AppSubmissionDto> saveAppsBySubmissionDtos(List<AppSubmissionDto> appSubmissionDtos);

    LicenceDto getLicenceDtoByLicenceId(String licenceId);
}
