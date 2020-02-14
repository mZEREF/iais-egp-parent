package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiLicenceQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.onlinenquiry.ProfessionalInformationQueryDto;

/**
 * OnlineEnquiriesService
 *
 * @author junyu
 * @date 2020/2/11
 */
public interface OnlineEnquiriesService {
    SearchResult<RfiLicenceQueryDto> searchLicenseeIdsParam(SearchParam searchParam);
   SearchResult<RfiLicenceQueryDto> searchSvcNamesParam(SearchParam searchParam);


    SearchResult<ProfessionalInformationQueryDto> searchProfessionalInformation(SearchParam searchParam);
}
