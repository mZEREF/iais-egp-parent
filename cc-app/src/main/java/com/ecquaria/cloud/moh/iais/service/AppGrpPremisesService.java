package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;

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
    List getAppGrpPremisesType();
    PostCodeDto getPremisesByPostalCode(String postalCode);
}
