package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.dto.ApplicationDto;

import java.util.List;

public interface InboxTabService {
    public List<ApplicationDto> selectAll();
    public List<ApplicationDto> searchByAppType(String type);
    SearchResult<ApplicationDto> doQuery(SearchParam param);
    public List<ApplicationDto> searchByAppStatus(String status);
}
