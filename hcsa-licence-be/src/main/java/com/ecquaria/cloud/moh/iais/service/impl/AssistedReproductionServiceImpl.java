package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquiryAjaxPatientResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquirySubResultsDto;
import com.ecquaria.cloud.moh.iais.service.AssistedReproductionService;
import com.ecquaria.cloud.moh.iais.service.client.AssistedReproductionClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * AssistedReproductionServiceImpl
 *
 * @author junyu
 * @date 2021/11/19
 */
@Slf4j
@Service
public class AssistedReproductionServiceImpl implements AssistedReproductionService{

    @Autowired
    AssistedReproductionClient assistedReproductionClient;

    @Override
    public SearchResult<AssistedReproductionEnquiryResultsDto> searchPatientByParam(SearchParam searchParam) {
        return assistedReproductionClient.searchPatientByParam(searchParam).getEntity();
    }

    @Override
    public SearchResult<AssistedReproductionEnquiryAjaxPatientResultsDto> searchPatientAjaxByParam(SearchParam searchParam) {
        return assistedReproductionClient.searchPatientAjaxByParam(searchParam).getEntity();
    }

    @Override
    public SearchResult<AssistedReproductionEnquirySubResultsDto> searchSubmissionByParam(SearchParam searchParam) {
        return assistedReproductionClient.searchSubmissionByParam(searchParam).getEntity();
    }
}
