package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.constant.rest.RestApiUrlConsts;
import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.CheckItemQueryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.checklist.ChecklistConfigQueryDto;
import com.ecquaria.cloud.moh.iais.common.utils.RestApiUtil;
import com.ecquaria.cloud.moh.iais.service.FillupChklistService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FillupChklistServiceImpl implements FillupChklistService {

    @Override
    public SearchResult<ChecklistConfigQueryDto> listChecklistConfig(SearchParam searchParam) {
        return RestApiUtil.query(RestApiUrlConsts.HCSA_CONFIG + RestApiUrlConsts.CHECKLIST_CONFIG_RESULTS, searchParam);
    }

    @Override
    public SearchResult<CheckItemQueryDto> listChklItem(SearchParam searchParam) {
        return RestApiUtil.query(RestApiUrlConsts.HCSA_CONFIG +  RestApiUrlConsts.CHECKLIST_ITEM_RESULTS, searchParam);
    }

}
