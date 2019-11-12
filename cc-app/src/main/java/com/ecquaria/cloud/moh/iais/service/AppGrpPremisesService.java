package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;

import java.util.List;
import java.util.Set;

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
    Set<String> getAppGrpPremisesTypeBySvcId(List<String> svcIds);
    PostCodeDto getPremisesByPostalCode(String searchField, String filterValue);
    String getSvcIdBySvcCode(String svcCode);

}
