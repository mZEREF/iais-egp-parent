package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.IaisApiResult;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.HcsaChklSvcRegulationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.RegulationQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.message.ErrorMsgContent;

import java.util.List;

/**
 * @author: yichen
 * @description:
 * @date:2020/3/23
 **/

public interface RegulationService {
    SearchResult<RegulationQueryDto> searchRegulation(SearchParam searchParam);

    IaisApiResult<HcsaChklSvcRegulationDto> createRegulation(HcsaChklSvcRegulationDto regulationDto);

    Boolean deleteRegulation(String id);

    IaisApiResult<HcsaChklSvcRegulationDto> updateRegulation(HcsaChklSvcRegulationDto regulationDto);

    List<ErrorMsgContent> submitUploadRegulation(List<HcsaChklSvcRegulationDto> regulationDtoList);
}
