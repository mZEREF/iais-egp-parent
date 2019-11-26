package com.ecquaria.cloud.moh.iais.service;

/*
 *author: yichen
 *date time:11/20/2019 1:55 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppPremisesCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.application.SelfDeclRenderDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;

import java.util.List;


public interface AppPremSelfDeclService {

    List<ApplicationDto> listApplicationByGroupId(String groupId);

    List<HcsaServiceDto> listHcsaService();

    SearchResult<SelfDeclRenderDto> listSelfDescConfig(SearchParam searchParam);

    List<AppPremisesCorrelationDto> listAppPremisesCorrelationByAppId(String appid);


    AppGrpPremisesDto getAppGrpPremisesDto(String appId);
}
