package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.AppGrpPremisesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AppGrpPremisesServiceImpl
 *
 * @author suocheng
 * @date 10/8/2019
 */
@Service
@Slf4j
public class AppGrpPremisesServiceImpl implements AppGrpPremisesService {
    private static final String URL="iais-application:8881/iais-premises";
    @Override
    public AppGrpPremisesDto saveAppGrpPremises(AppGrpPremisesDto appGrpPremisesDto) {
        appGrpPremisesDto.setAuditTrailDto(IaisEGPHelper.getCurrentAuditTrailDto());
       return RestApiUtil.save(URL,appGrpPremisesDto,AppGrpPremisesDto.class);
    }

    @Override
    public List<AppGrpPremisesDto> getAppGrpPremisesDtoByLoginId(String loginId) {
        List<AppGrpPremisesDto> result = new ArrayList<>();
        AppGrpPremisesDto appGrpPremisesDto = new AppGrpPremisesDto();
        appGrpPremisesDto.setId("123");
        appGrpPremisesDto.setPostalCode("019191");
        appGrpPremisesDto.setBlkNo("123");
        appGrpPremisesDto.setBuildingName("building Name");
        appGrpPremisesDto.setStreetName("String Name");
        appGrpPremisesDto.setFloorNo("6");
        appGrpPremisesDto.setUnitNo("3");
        appGrpPremisesDto.setPremisesType(ApplicationConsts.PREMISES_TYPE_ON_SITE);
        return result;
    }

    @Override
    public List getAppGrpPremisesDtosByAppId(String appId) {
        Map<String, Object> map = new HashMap<>();
        map.put("appId", appId);
        return RestApiUtil.getByReqParam(URL, map, List.class);
    }
}
