package com.ecquaria.cloud.moh.iais.service.datasubmission.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArMgrQueryPatientDto;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * ArManagementService
 *
 * @author Jinhua
 * @date 2022/12/2 15:41
 */
@Service
@Slf4j
public class ArManagementService {
    @Autowired
    private HcsaLicenceClient client;

    public SearchResult<ArMgrQueryPatientDto> queryPatient(SearchParam searchParam) {
        return client.queryForArPatients(searchParam).getEntity();
    }
}
