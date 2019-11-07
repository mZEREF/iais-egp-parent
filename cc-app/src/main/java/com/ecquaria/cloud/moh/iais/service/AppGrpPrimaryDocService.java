package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaSvcDocConfigDto;

import java.io.IOException;
import java.util.List;


/**
 * AppGrpPrimaryDocService
 *
 * @author suocheng
 * @date 10/10/2019
 */
public interface AppGrpPrimaryDocService {
    List<String> SaveFileToRepo(AppGrpPrimaryDocDto appGrpPrimaryDocDto) throws IOException;
    AppGrpPrimaryDocDto saveAppGrpPremisesDoc(AppGrpPrimaryDocDto appGrpPrimaryDocDto);
    List getAppGrpPrimaryDocDtosByAppGrpId(String appGrpId);
    List<HcsaSvcDocConfigDto> getAllHcsaSvcCommonDocDtos();
    List<AppGrpPrimaryDocDto> saveAppGrpPremisesDocs(List<AppGrpPrimaryDocDto> appGrpPrimaryDocDtoList);
}
