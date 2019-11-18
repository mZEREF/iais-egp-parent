package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.ApplicationConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppGrpPremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.postcode.PostCodeDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.AppGrpPremisesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts.GET_PREMISES_TYPE_BY_ID;
import static com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts.POSTAL_CODE_INFO;
import static com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts.SERVICEID_BY_SVCCODE;


/**
 * AppGrpPremisesServiceImpl
 *
 * @author suocheng
 * @date 10/8/2019
 */
@Service
@Slf4j
public class AppGrpPremisesServiceImpl implements AppGrpPremisesService {
    //dont be used
    private static final String URL="iais-application:8881/iais-premises";


    @Override
    public AppGrpPremisesDto saveAppGrpPremises(AppGrpPremisesDto appGrpPremisesDto) {
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
    public Set<String> getAppGrpPremisesTypeBySvcId(List<String> svcIds) {
        return RestApiUtil.getByList(GET_PREMISES_TYPE_BY_ID, svcIds, Set.class);
    }

    @Override
    public PostCodeDto getPremisesByPostalCode(String searchField, String filterValue) {
        Map<String,Object> map = new HashMap<>();
        map.put("searchField", searchField);
        map.put("filterValue", filterValue);
        return RestApiUtil.getByReqParam(POSTAL_CODE_INFO, map, PostCodeDto.class);
    }

    @Override
    public String getSvcIdBySvcCode(String svcCode) {
        Map<String,Object> map = new HashMap<>();
        map.put("code", svcCode);
        return RestApiUtil.getByReqParam(SERVICEID_BY_SVCCODE, map, String.class);
    }
}
