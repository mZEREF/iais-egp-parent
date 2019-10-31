package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppGrpPremisesDto;

import java.util.List;

/**
 * AppGrpPremisesService
 *
 * @author suocheng
 * @date 10/8/2019
 */
public interface AppGrpPremisesService {
     AppGrpPremisesDto saveAppGrpPremises(AppGrpPremisesDto appGrpPremisesDto);
     List<AppGrpPremisesDto> getAppGrpPremisesDtoByLoginId(String loginId);
     List getAppGrpPremisesDtosByAppId(String appId);
}
