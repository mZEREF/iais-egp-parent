package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.HcsaSvcSpePremisesTypeDto;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
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
    private static final String PREMISESURL="config-service:8888/application-type";
    private static final String POSTCODEURL = "system-admin:8886/api/postcodes";
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

    @Override
    public List getAppGrpPremisesType() {
        /*Map<String,Object> map = new HashMap<>();
        List serviceId = new ArrayList();
        serviceId.add("4029F370-EDEE-E911-BE76-000C294908E1");
        map.put("ServiceId", serviceId);*/
        Map<String,Object> map1 = new HashMap<>();
        map1.put("serviceId", "4029F370-EDEE-E911-BE76-000C294908E1");
        List<HcsaSvcSpePremisesTypeDto> premisesType = RestApiUtil.getListByReqParam(PREMISESURL, map1, HcsaSvcSpePremisesTypeDto.class);
        List<String> type = new ArrayList<>();
        type.add("On-site");
        type.add("Conveyance");
        return type;
    }

    @Override
    public PostCodeDto getPremisesByPostalCode(String postalCode) {
        Map<String,Object> map = new HashMap<>();
        map.put("searchField", "postalCode");
        map.put("filterValue", postalCode);
        return RestApiUtil.getByReqParam(POSTCODEURL, map, PostCodeDto.class);
    }
}
