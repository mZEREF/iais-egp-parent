package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.giro.GiroDeductionDto;

import java.util.List;

/**
 * @author Shicheng
 * @date 2020/10/19 15:52
 **/
public interface GiroDeductionBeService {

    SearchResult<GiroDeductionDto> giroDeductionDtoSearchResult(SearchParam searchParam);

    List<ApplicationGroupDto> sendMessageEmail(List<String> appGroupList);

    List<GiroDeductionDto> syncDeductionDtoSearchResultUseGroups(List<GiroDeductionDto> giroDeductionDtoList);

    void syncFeApplicationGroupStatus(List<ApplicationGroupDto> applicationGroupDtos);

}
