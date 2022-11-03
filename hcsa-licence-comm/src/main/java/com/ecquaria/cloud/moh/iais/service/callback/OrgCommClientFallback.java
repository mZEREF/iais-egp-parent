package com.ecquaria.cloud.moh.iais.service.callback;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.FeUserDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgGiroAccountInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.OrgUserDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.client.OrgCommClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;

/**
 * @Auther chenlei on 5/4/2022.
 */
public class OrgCommClientFallback implements OrgCommClient {

    @Override
    public FeignResponseEntity<List<FeUserDto>> getFeUserDtoByLicenseeId(String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity(licenseeId);
    }

    @Override
    public FeignResponseEntity<LicenseeDto> getLicenseeById(String id) {
        return IaisEGPHelper.getFeignResponseEntity(id);
    }

    @Override
    public FeignResponseEntity<List<OrgGiroAccountInfoDto>> getGiroAccByLicenseeId(String licenseeId) {
        return IaisEGPHelper.getFeignResponseEntity(licenseeId);
    }

    @Override
    public FeignResponseEntity<OrgUserDto> retrieveOrgUserAccountById(String id) {
        return IaisEGPHelper.getFeignResponseEntity(id);
    }

}
