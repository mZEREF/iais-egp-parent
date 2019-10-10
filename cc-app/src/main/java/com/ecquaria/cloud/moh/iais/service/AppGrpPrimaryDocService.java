package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.dto.AppGrpPrimaryDocDto;

import java.io.IOException;

/**
 * AppGrpPrimaryDocService
 *
 * @author suocheng
 * @date 10/10/2019
 */
public interface AppGrpPrimaryDocService {
    public String SaveFileToRepo(AppGrpPrimaryDocDto appGrpPrimaryDocDto) throws IOException;
    public AppGrpPrimaryDocDto saveAppGrpPremisesDoc(AppGrpPrimaryDocDto appGrpPrimaryDocDto);
}
