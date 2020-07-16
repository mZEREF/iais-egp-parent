package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.AppAlignLicQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.MenuLicenceDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceCorrelationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.AssessmentGuideService;
import com.ecquaria.cloud.moh.iais.service.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AssessmentGuideImpl implements AssessmentGuideService {

    @Autowired
    private ConfigInboxClient configInboxClient;

    @Autowired
    private LicenceInboxClient licenceClient;

    @Autowired
    private AppInboxClient appInboxClient;

    @Override
    public List<HcsaServiceDto> getServicesInActive() {
        return configInboxClient.getActiveServices().getEntity();
    }

    @Override
    public List<HcsaServiceCorrelationDto> getCorrelation() {
        return configInboxClient.serviceCorrelation().getEntity();
    }

    @Override
    public List<AppAlignLicQueryDto> getAppAlignLicQueryDto(String licenseeId, List<String> svcNameList) {
        List<AppAlignLicQueryDto> appAlignLicQueryDtos = IaisCommonUtils.genNewArrayList();
        if(!StringUtil.isEmpty(licenseeId) && !IaisCommonUtils.isEmpty(svcNameList)) {
            String svcNames = JsonUtil.parseToJson(svcNameList);
            appAlignLicQueryDtos = licenceClient.getAppAlignLicQueryDto(licenseeId,svcNames).getEntity();
        }
        return appAlignLicQueryDtos;
    }

    @Override
    public List<HcsaServiceDto> getHcsaServiceDtosById(List<String> ids) {
        return  configInboxClient.getHcsaService(ids).getEntity();
    }

    @Override
    public String selectDarft(Map<String, Object> map) {
        return appInboxClient.selectDarft(map).getEntity();
    }

    @Override
    public SearchResult<MenuLicenceDto> getMenuLicence(SearchParam searchParam) {
        return licenceClient.getMenuLicence(searchParam).getEntity();
    }
}
