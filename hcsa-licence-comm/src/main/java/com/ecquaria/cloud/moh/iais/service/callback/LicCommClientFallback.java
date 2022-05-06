package com.ecquaria.cloud.moh.iais.service.callback;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.SubLicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.CheckCoLocationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.GiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicAppCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicBaseSpecifiedCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicKeyPersonnelDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PersonnelListQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.client.LicCommClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;

/**
 * @Auther chenlei on 5/3/2022.
 */
public class LicCommClientFallback implements LicCommClient {

    @Override
    public FeignResponseEntity<LicenceDto> getActiveLicenceById(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity(licenceId);
    }

    @Override
    public FeignResponseEntity<List<LicenceDto>> getLicenceDtoByHciCode(String hciCode, String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity(hciCode, licenseeId);
    }

    @Override
    public FeignResponseEntity<AppSubmissionDto> getAppSubmissionDto(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity(licenceId);
    }

    @Override
    public FeignResponseEntity<AppSubmissionDto> viewAppSubmissionDto(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity(licenceId);
    }

    @Override
    public FeignResponseEntity<List<AppSubmissionDto>> getAlginAppSubmissionDtos(String licenceId, Boolean checkSpec) {
        return IaisEGPHelper.getFeignResponseEntity(licenceId, checkSpec);
    }

    @Override
    public FeignResponseEntity<List<AppSubmissionDto>> getAppSubmissionDtosBySubLicensee(SubLicenseeDto sublicenseeDto) {
        return IaisEGPHelper.getFeignResponseEntity(JsonUtil.parseToJson(sublicenseeDto));
    }

    @Override
    public FeignResponseEntity<List<AppSubmissionDto>> getAppSubmissionDtosByLicenceIds(List<String> licenceIds) {
        return IaisEGPHelper.getFeignResponseEntity(licenceIds);
    }

    @Override
    public FeignResponseEntity<List<GiroAccountInfoDto>> getGiroAccountsByLicIds(List<String> licIds) {
        return IaisEGPHelper.getFeignResponseEntity(licIds);
    }

    @Override
    public FeignResponseEntity<List<LicAppCorrelationDto>> getAllRelatedLicAppCorrs(String licenceId, String svcName) {
        return IaisEGPHelper.getFeignResponseEntity(licenceId,svcName);
    }

    @Override
    public FeignResponseEntity<PremisesDto> getPremisesDtoForBusinessName(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity(licenceId);
    }

    @Override
    public FeignResponseEntity<List<LicBaseSpecifiedCorrelationDto>> getLicBaseSpecifiedCorrelationDtos(String svcType,
            String originLicenceId) {
        return IaisEGPHelper.getFeignResponseEntity(svcType, originLicenceId);
    }

    @Override
    public FeignResponseEntity<List<AppGrpPremisesDto>> getDistinctPremisesByLicenseeId(String licenseeId, String serviceName) {
        return IaisEGPHelper.getFeignResponseEntity(licenseeId, serviceName);
    }

    @Override
    public FeignResponseEntity<List<PremisesDto>> getPremisesDtoByHciNameAndPremType(String hciName, String premType,
            String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity(hciName, premType, licenseeId);
    }

    @Override
    public FeignResponseEntity<Boolean> getOtherLicseePremises(CheckCoLocationDto checkCoLocationDto) {
        return IaisEGPHelper.getFeignResponseEntity(checkCoLocationDto);
    }

    @Override
    public FeignResponseEntity<List<PersonnelListQueryDto>> getPersonnel(String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity(licenseeId);
    }

    @Override
    public FeignResponseEntity<List<SubLicenseeDto>> getIndividualSubLicensees(String orgId) {
        return IaisEGPHelper.getFeignResponseEntity(orgId);
    }

    @Override
    public FeignResponseEntity<List<PremisesDto>> getPremisesByLicseeIdAndSvcName(String licenseeId, List<String> svcNames) {
        return IaisEGPHelper.getFeignResponseEntity(licenseeId, svcNames);
    }

    @Override
    public FeignResponseEntity<List<LicAppCorrelationDto>> getInactiveLicAppCorrelations() {
        return IaisEGPHelper.getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<List<String>> getPersonnelDtoByIdNo(String idNo) {
        return null;
    }

    @Override
    public FeignResponseEntity<List<LicKeyPersonnelDto>> getLicKeyPersonnelDtoByPersonId(List<String> personIds) {
        return null;
    }

    @Override
    public FeignResponseEntity<List<AppGrpPremisesDto>> getLicPremisesById(String id) {
        return null;
    }

}
