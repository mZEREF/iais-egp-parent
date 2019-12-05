package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

/**
 * LicenceServiceImpl
 *
 * @author suocheng
 * @date 11/29/2019
 */
@Service
public class LicenceServiceImpl implements LicenceService {
    @Override
    public List<ApplicationLicenceDto> getCanGenerateApplications(int day) {
        Map<String,Object> param = new HashMap<>();
        param.put("day",day);
        return RestApiUtil.getListByReqParam(RestApiUrlConsts.APPLICATION_GROUP,param,ApplicationLicenceDto.class);
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
    public String getGroupLicenceNo(String hscaCode, int licenceNum, int yearLength) {
        Map<String,Object> param = new HashMap();
        param.put("hscaCode",hscaCode);
        param.put("licenceNum",licenceNum);
        param.put("yearLength",yearLength);
        return RestApiUtil.getByReqParam(RestApiUrlConsts.GROUP_LICENCE,param,String.class);
    }

    @Override
    public AppPremisesRecommendationDto getTcu(String appPremCorrecId) {
        Map<String,Object> param = new HashMap<>();
        param.put("appPremId",appPremCorrecId);
        param.put("recomType",InspectionConstants.RECOM_TYPE_TCU);
        return RestApiUtil.getByReqParam(RestApiUrlConsts.APPLICATION_BE,param,AppPremisesRecommendationDto.class);
    }

    @Override
    public PremisesDto getLatestVersionPremisesByHciCode(String hciCode) {
        return hcsaLicenceClient.getLatestVersionPremisesByHciCode(hciCode).getEntity();
    }

    @Override
    public List<LicenceGroupDto> createSuperLicDto(List<LicenceGroupDto> licenceGroupDtos) {

        return RestApiUtil.save(RestApiUrlConsts.LICENCES,licenceGroupDtos,List.class);
    }
}
