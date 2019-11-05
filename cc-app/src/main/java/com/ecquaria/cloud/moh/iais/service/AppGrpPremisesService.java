package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcSpePremisesTypeDto;
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
    List<HcsaSvcSpePremisesTypeDto> getAppGrpPremisesTypeBySvcId(String svcId);
    PostCodeDto getPremisesByPostalCode(String searchField, String filterValue);
    String getSvcIdBySvcCode(String svcCode);

}
