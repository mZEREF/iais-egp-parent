package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceViewDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenseeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;

import java.util.List;

/**
 * LicenceViewService
 *
 * @author suocheng
 * @date 2/18/2020
 */

public interface LicenceViewService {
    public LicenceViewDto getLicenceViewDtoByLicenceId(String licenceId);
    public LicenseeDto getLicenseeDtoBylicenseeId(String licenseeId);
    public List<LicenceDto> getLicenceDtoByLicenseeId(String licenseeId);
    public SearchResult<MenuLicenceDto> getMenuLicence(SearchParam searchParam);
}
