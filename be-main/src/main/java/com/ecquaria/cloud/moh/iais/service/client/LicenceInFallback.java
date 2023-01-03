package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsCenterDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.LicPremisesAuditDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.PostInsGroupDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public class LicenceInFallback implements LicenceClient {

    @Override
    public FeignResponseEntity<LicenceDto> getLicenceByAppId(String appId) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenceByAppId",appId);
    }

    @Override
    public FeignResponseEntity<LicenceViewDto> getLicenceViewDtoByLicPremCorrId(String licPremCorrId) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenceViewDtoByLicPremCorrId",licPremCorrId);
    }

    @Override
    public FeignResponseEntity<LicenceDto> getLicBylicId(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity("getLicBylicId",licenceId);
    }

    @Override
    public FeignResponseEntity<List<String>> getActSpecIdByActBaseId(String licId) {
        return IaisEGPHelper.getFeignResponseEntity("getActSpecIdByActBaseId",licId);
    }

    @Override
    public FeignResponseEntity<List<LicenceDto>> getLicenceDtosByLicenseeId(String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenceDtosByLicenseeId",licenseeId);
    }

    @Override
    public FeignResponseEntity<LicPremisesAuditDto> getLicPremisesAuditDtoByLicIdAndHCICode(String licId, String hCICode) {
        return IaisEGPHelper.getFeignResponseEntity("getLicPremisesAuditDtoByLicIdAndHCICode",licId,hCICode);
    }


    @Override
    public FeignResponseEntity<PostInsGroupDto> getPostInsGroupDto(String licId, String corrId) {
        return IaisEGPHelper.getFeignResponseEntity("getPostInsGroupDto",licId,corrId);
    }

    @Override
    public FeignResponseEntity<PostInsGroupDto> savePostInsGroupDto(PostInsGroupDto postInsGroupDto) {
        return IaisEGPHelper.getFeignResponseEntity("savePostInsGroupDto",postInsGroupDto);
    }

    @Override
    public FeignResponseEntity<LicenceDto> getLicDtoById(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity("getLicDtoById",licenceId);
    }

    @Override
    public FeignResponseEntity<LicenceDto> getLicenceDtoById(String licenceId) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenceDtoById",licenceId);
    }

    @Override
    public FeignResponseEntity<PremisesDto> getPremiseDtoByHciCodeOrName(String hciCodeName) {
        return IaisEGPHelper.getFeignResponseEntity("getPremiseDtoByHciCodeOrName",hciCodeName);
    }

    @Override
    public FeignResponseEntity<Map<String, LicenceDto>> getLicenceList(List<String> licIds) {
        return IaisEGPHelper.getFeignResponseEntity("getLicenceList",licIds);
    }

    @Override
    public FeignResponseEntity<List<DsCenterDto>> updateBeDsCenterStatus() {
        return IaisEGPHelper.getFeignResponseEntity("updateBeDsCenterStatus");
    }
}
