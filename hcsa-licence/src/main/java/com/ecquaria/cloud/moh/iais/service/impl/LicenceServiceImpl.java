package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.SuperLicDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * LicenceServiceImpl
 *
 * @author suocheng
 * @date 11/29/2019
 */
public class LicenceServiceImpl implements LicenceService {
    @Override
    public List<ApplicationLicenceDto> getCanGenerateApplications(int day) {
        return RestApiUtil.getListByPathParam(RestApiUrlConsts.APPLICATION_GROUP,String.valueOf(day),ApplicationLicenceDto.class);
    }

    @Override
    public List<HcsaServiceDto> getHcsaServiceById(List<String> serviceIds) {
        return RestApiUtil.postGetList(RestApiUrlConsts.GET_HCSA_SERVICE_BY_IDS,serviceIds,HcsaServiceDto.class);
    }

    @Override
    public String getHciCode(String serviceCode) {
        return RestApiUtil.getByPathParam(RestApiUrlConsts.HCL_CODE_SERVICECODE,serviceCode,String.class);
    }

    @Override
    public String getLicenceNo(String hciCode, String serviceCode, int yearLength) {
        Map<String,Object> param = new HashMap();
        param.put("hciCode",hciCode);
        param.put("serviceCode",serviceCode);
        param.put("yearLength",yearLength);
        return RestApiUtil.getByReqParam(RestApiUrlConsts.LICENCE_NUMBER,param,String.class);
    }

    @Override
    public List<SuperLicDto> createSuperLicDto(List<SuperLicDto> superLicDtos) {
        //todo:create licence
        return null;
    }
}
