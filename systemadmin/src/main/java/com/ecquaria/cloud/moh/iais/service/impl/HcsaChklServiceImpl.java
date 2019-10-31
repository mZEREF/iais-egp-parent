package com.ecquaria.cloud.moh.iais.service.impl;

/*
 *author: yichen
 *date time:9/24/2019 5:10 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.annotation.SearchTrack;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.checklist.HcsaChklItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.checklist.HcsaChklItemQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.HcsaServiceQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloud.moh.iais.service.HcsaChklService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HcsaChklServiceImpl implements HcsaChklService {

    @SearchTrack(catalog = "HcasaService", key = "search")
    public SearchResult<HcsaServiceQueryDto> listHcasaService(SearchParam searchParam) {
        return RestApiUtil.query("system-admin-service:8886/iais-check-list/hcasa-service/results", searchParam);
    }

    @Override
    public SearchResult<HcsaChklItemQueryDto> listChklItem(SearchParam searchParam) {
        return RestApiUtil.query("system-admin-service:8886/iais-hcsa-chkl/item/results", searchParam);
    }

    @Override
    public HcsaChklItemDto getChklItemById(String id) {
        return IaisEGPHelper.getRecordByPrimaryKey("system-admin-service:8886/iais-hcsa-chkl/item", id, HcsaChklItemDto.class);
    }

    public void saveChklItem(HcsaChklItemDto itemDto) {
        RestApiUtil.save("system-admin-service:8886/iais-hcsa-chkl/item", itemDto);
    }

    @Override
    public void saveConfigItemOfCommon() {

    }

    public void addConfigItemOfCommon() {

    }

    @Override
    public void addConfigItemOfService() {

    }
}
