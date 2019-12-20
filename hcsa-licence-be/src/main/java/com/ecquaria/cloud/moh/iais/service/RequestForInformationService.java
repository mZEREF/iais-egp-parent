package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiApplicationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.inspection.RfiLicenceQueryDto;

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
    List<SelectOption> getLicStatusOption();
    SearchResult<RfiApplicationQueryDto> appDoQuery(SearchParam searchParam);
    SearchResult<RfiLicenceQueryDto> licenceDoQuery(SearchParam searchParam);
}
