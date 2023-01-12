package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.inputFiles.SearchInputFilesDto;
import com.ecquaria.cloud.moh.iais.service.client.SystemAdminMainFeClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class InputFilesManagementServiceImpl {

    @Autowired
    private SystemAdminMainFeClient client;

    public SearchResult<SearchInputFilesDto> searchInputFiles(SearchParam searchParam){
        return client.searchInputFiles(searchParam).getEntity();
    }
}
