package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ProfessionalInformationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.organization.LicenseeQueryDto;

/**
 * OnlineEnquiriesService
 *
 * @author junyu
 * @date 2020/2/11
 */
public interface OnlineEnquiriesService {
    SearchResult<LicenseeQueryDto> searchLicenseeIdsParam(SearchParam searchParam);
   SearchResult<HcsaSvcQueryDto> searchSvcNamesParam(SearchParam searchParam);


    SearchResult<ProfessionalInformationQueryDto> searchProfessionalInformation(SearchParam searchParam);
}
