package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.application.AppGrpPrimaryDocDto;

import java.io.IOException;
import java.util.List;

/**
 * AppGrpPrimaryDocService
 *
 * @author suocheng
 * @date 10/10/2019
 */
public interface AppGrpPrimaryDocService {
    public List<String> SaveFileToRepo(AppGrpPrimaryDocDto appGrpPrimaryDocDto) throws IOException;
    public AppGrpPrimaryDocDto saveAppGrpPremisesDoc(AppGrpPrimaryDocDto appGrpPrimaryDocDto);
    public List getAppGrpPrimaryDocDtosByAppGrpId(String appGrpId);
}
