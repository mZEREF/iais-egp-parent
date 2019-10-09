package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.dto.AppGrpPremisesDto;

import java.util.List;

/**
 * AppGrpPremisesService
 *
 * @author suocheng
 * @date 10/8/2019
 */
public interface AppGrpPremisesService {
    public AppGrpPremisesDto saveAppGrpPremises(AppGrpPremisesDto appGrpPremisesDto);
    public List<AppGrpPremisesDto> getAppGrpPremisesDtoByLoginId(String loginId);
}
