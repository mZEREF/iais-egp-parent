package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.CheckCoLocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicBaseSpecifiedCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;

import java.util.List;
import java.util.Map;

/**
 * @Auther chenlei on 5/3/2022.
 */
public interface LicCommService {

    LicenceDto getActiveLicenceById(String licenceId);

    List<LicenceDto> getLicenceDtoByHciCode(String hciCode, String licenseeId);

    List<LicenceDto> getLicenceDtoByHciCode(String licenseeId, AppGrpPremisesDto appGrpPremisesDto, String... excludeNos);

    /**
     * Only retrieve active / approved licence
     *
     * @param licenceId licence id
     * @return
     */
    AppSubmissionDto getAppSubmissionDtoByLicenceId(String licenceId);

    /**
     * Retrive licence without checking status
     *
     * @param licenceId licence id
     * @return
     */
    AppSubmissionDto viewAppSubmissionDto(String licenceId);

    List<AppSubmissionDto> getAppSubmissionDtosByLicenceIds(List<String> licenceIds);

    List<GiroAccountInfoDto> getGiroAccountsByLicIds(List<String> licIds);

    List<LicBaseSpecifiedCorrelationDto> getLicBaseSpecifiedCorrelationDtos(String svcType, String originLicenceId);

    boolean baseSpecLicenceRelation(LicenceDto licenceDto);

    String baseSpecLicenceRelation(LicenceDto licenceDto, boolean flag);

    List<AppGrpPremisesDto> getLicencePremisesDtoList(String licenseeId);

    List<PremisesDto> getPremisesDtoByHciNameAndPremType(String hciName, String premisesType, String licenseeId);

    Boolean getOtherLicseePremises(CheckCoLocationDto checkCoLocationDto);

    List<PersonnelListQueryDto> getLicencePersonnelListQueryDto(String licenseeId);

    List<SubLicenseeDto> getIndividualSubLicensees(String orgId);

    List<PremisesDto> getPremisesByLicseeIdAndSvcName(String licenseeId, List<String> svcNames);

    List<LicAppCorrelationDto> getInactiveLicAppCorrelations();

    List<AppSubmissionDto> getAlginAppSubmissionDtos(String licenceId, boolean checkSpec);

    List<AppSubmissionDto> getAppSubmissionDtosBySubLicensee(SubLicenseeDto subLicenseeDto);

    /**
     * Check personnel affected data
     *
     * @param licenseeId
     * @param appSubmissionDto
     * @param oldAppSubmissionDto
     * @param check               0: only check changed;
     *                            1: check changed and retrieve affected data;
     *                            2: check changed, retrieve affected data and reset them.
     * @return
     */
    List<AppSubmissionDto> personContact(String licenseeId, AppSubmissionDto appSubmissionDto,
            AppSubmissionDto oldAppSubmissionDto, int check) throws Exception;

    List<AppGrpPremisesDto> getLicPremisesInfo(String id);


}
