package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.dto.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.AppGrpPremisesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * AppGrpPremisesServiceImpl
 *
 * @author suocheng
 * @date 10/8/2019
 */
@Service
@Slf4j
public class AppGrpPremisesServiceImpl implements AppGrpPremisesService {
    @Override
    public void saveAppGrpPremises(AppGrpPremisesDto appGrpPremisesDto) {
        appGrpPremisesDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
        RestApiUtil.save("iais-application:8881",appGrpPremisesDto);
    }
}
