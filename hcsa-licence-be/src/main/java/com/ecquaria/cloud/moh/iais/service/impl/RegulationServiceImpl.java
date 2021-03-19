package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklSvcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.RegulationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.ErrorMsgContent;
import com.ecquaria.cloud.moh.iais.service.RegulationService;
import com.ecquaria.cloud.moh.iais.service.client.HcsaChklClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return hcsaChklClient.createRegulation(regulationDto).getEntity();
    }

    @Override
    public Boolean deleteRegulation(String id) {
        return hcsaChklClient.deleteRegulation(id).getEntity();
    }

    @Override
    public IaisApiResult<HcsaChklSvcRegulationDto> updateRegulation(HcsaChklSvcRegulationDto regulationDto) {
        return hcsaChklClient.updateRegulation(regulationDto).getEntity();
    }

    @Override
    public List<ErrorMsgContent> submitUploadRegulation(List<HcsaChklSvcRegulationDto> regulationDtoList) {
        IaisApiResult<List<ErrorMsgContent>> apiResult = hcsaChklClient.submitHcsaChklSvcRegulation(regulationDtoList).getEntity();
        return apiResult.getEntity();
    }

    @Override
    public List<HcsaChklSvcRegulationDto> listRegulationByIds(List<String> ids) {
        return hcsaChklClient.listRegulationByIds(ids).getEntity();
    }
}
