package com.ecquaria.cloud.moh.iais.service;

/*
 *author: yichen
 *date time:9/25/2019 2:26 PM
 *description:
 */

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklItemDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklItemQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.serviceconfig.HcsaServiceQueryDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface HcsaChklService {
    SearchResult<HcsaServiceQueryDto> listHcasaService(SearchParam searchParam);
    SearchResult<HcsaChklItemQueryDto> listChklItem(SearchParam searchParam);

    List<HcsaChklItemDto> listChklItemByItemId(List<String> itemIds);

    HcsaChklItemDto getChklItemById(String id);
    void saveChklItem(HcsaChklItemDto itemDto);

    void saveConfigItemOfCommon();

    void addConfigItemOfService();
}
