package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
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
        Integer licenceSeq = RestApiUtil.getByPathParam(RestApiUrlConsts.HCI_CODE_LICENCE_NUMBER_HCICODE,hciCode,Integer.class);
        Map<String,Object> param = new HashMap();
        param.put("hciCode",hciCode);
        param.put("serviceCode",serviceCode);
        param.put("yearLength",yearLength);
        param.put("licenceSeq",licenceSeq);
        return RestApiUtil.getByReqParam(RestApiUrlConsts.LICENCE_NUMBER,param,String.class);
    }

    @Override
    public AppPremisesRecommendationDto getTcu(String appPremCorrecId) {
        Map<String,Object> param = new HashMap<>();
        param.put("appPremId",appPremCorrecId);
        param.put("recomType",InspectionConstants.RECOM_TYPE_TCU);
        return RestApiUtil.getByReqParam(RestApiUrlConsts.APPLICATION_BE,param,AppPremisesRecommendationDto.class);
    }

    @Override
    public List<SuperLicDto> createSuperLicDto(List<SuperLicDto> superLicDtos) {
        //todo:create licence
        return null;
    }
}
