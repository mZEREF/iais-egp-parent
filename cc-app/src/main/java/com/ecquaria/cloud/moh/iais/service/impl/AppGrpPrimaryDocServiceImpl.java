package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.dto.AppGrpPrimaryDocDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.AppGrpPrimaryDocService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * AppGrpPremisesDocServiceImpl
 *
 * @author suocheng
 * @date 10/10/2019
 */
@Service
@Slf4j
public class AppGrpPrimaryDocServiceImpl implements AppGrpPrimaryDocService {
    private static final String URL="iais-application:8881/iais-premisesdoc";

    @Override
    public String SaveFileToRepo(AppGrpPrimaryDocDto appGrpPrimaryDocDto) throws IOException {
       // return  RestApiUtil.saveFile(appGrpPrimaryDocDto.getFile());
        return null;
    }

    @Override
    public AppGrpPrimaryDocDto saveAppGrpPremisesDoc(AppGrpPrimaryDocDto appGrpPrimaryDocDto) {
        appGrpPrimaryDocDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        return RestApiUtil.save(URL,appGrpPrimaryDocDto,AppGrpPrimaryDocDto.class);
    }
}
