package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.inspection.InspectionConstants;
import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.AppPremisesRecommendationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.application.ApplicationLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.LicenceGroupDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.LicenceService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ecquaria.cloud.moh.iais.service.client.ApplicationClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaConfigClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import com.ecquaria.cloud.moh.iais.service.client.SystemClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * LicenceServiceImpl
 *
 * @author suocheng
 * @date 11/29/2019
 */
@Service
public class LicenceServiceImpl implements LicenceService {
    @Autowired
    private ApplicationClient applicationClient;
    @Autowired
    private HcsaConfigClient hcsaConfigClient;
    @Autowired
    private SystemClient systemClient;
    @Autowired
    private HcsaLicenceClient hcsaLicenceClient;
    @Override
    public List<ApplicationLicenceDto> getCanGenerateApplications(int day) {
        Map<String,Object> param = new HashMap<>();
        param.put("day",day);

        return   applicationClient.getGroup(day).getEntity();
    }

    @Override
    public List<HcsaServiceDto> getHcsaServiceById(List<String> serviceIds) {

        return  hcsaConfigClient.getHcsaService(serviceIds).getEntity();
    }

    @Override
    public String getHciCode(String serviceCode) {
        return     systemClient.hclCodeByCode(serviceCode).getEntity();
    }

    @Override
    public String getLicenceNo(String hciCode, String serviceCode, int yearLength) {

        Integer licenceSeq =  hcsaLicenceClient.licenceNumber(hciCode).getEntity();
        Map<String,Object> param = new HashMap();
        param.put("hciCode",hciCode);
        param.put("serviceCode",serviceCode);
        param.put("yearLength",yearLength);
        param.put("licenceSeq",licenceSeq);

        return    systemClient.licence(hciCode,serviceCode,yearLength,licenceSeq).getEntity();
    }

    @Override
    public String getGroupLicenceNo(String hscaCode, int yearLength) {
        String entity = hcsaLicenceClient.groupLicenceNumber(hscaCode).getEntity();

        return   systemClient.groupLicence(hscaCode,yearLength+"",entity).getEntity();
    }

    @Override
    public AppPremisesRecommendationDto getTcu(String appPremCorrecId) {
        Map<String,Object> param = new HashMap<>();
        param.put("appPremId",appPremCorrecId);
        param.put("recomType",InspectionConstants.RECOM_TYPE_TCU);
        return RestApiUtil.getByReqParam(RestApiUrlConsts.APPLICATION_BE,param,AppPremisesRecommendationDto.class);
    }

    @Override
    public List<LicenceGroupDto> createSuperLicDto(List<LicenceGroupDto> licenceGroupDtos) {

        return    hcsaLicenceClient.createLicence(licenceGroupDtos).getEntity();
    }
}
