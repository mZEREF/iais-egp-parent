package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklSvcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.RegulationQueryDto;
import com.ecquaria.cloud.moh.iais.service.RegulationService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: yichen
 * @description:
 * @date:2020/3/23
 **/

@Service
public class RegulationServiceImpl implements RegulationService {
    @Autowired
    private HcsaChklClient hcsaChklClient;

    @Override
    @SearchTrack(catalog = "hcsaconfig", key = "regulationQuery")
    public SearchResult<RegulationQueryDto> searchRegulation(SearchParam searchParam) {
        return hcsaChklClient.searchRegulation(searchParam).getEntity();
    }

    @Override
    public IaisApiResult<HcsaChklSvcRegulationDto> createRegulation(HcsaChklSvcRegulationDto regulationDto) {
        return null;
    }

    @Override
    public void deleteRegulation(String id) {

    }

    @Override
    public HcsaChklSvcRegulationDto updateRegulation(HcsaChklSvcRegulationDto regulationDto) {
        return null;
    }
}
