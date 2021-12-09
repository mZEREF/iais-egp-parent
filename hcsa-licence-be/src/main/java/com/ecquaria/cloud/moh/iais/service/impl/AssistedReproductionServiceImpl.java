package com.ecquaria.cloud.moh.iais.service.impl;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArEnquiryCoFundingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionAdvEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquiryAjaxPatientResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquirySubResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInventoryDto;
import com.ecquaria.cloud.moh.iais.service.AssistedReproductionService;
import com.ecquaria.cloud.moh.iais.service.client.AssistedReproductionClient;
import com.ecquaria.cloud.moh.iais.service.client.HcsaLicenceClient;
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
    private AssistedReproductionClient assistedReproductionClient;

    @Autowired
    private HcsaLicenceClient licenceClient;

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

    @Override
    public SearchResult<AssistedReproductionAdvEnquiryResultsDto> searchPatientAdvByParam(SearchParam searchParam) {
        return assistedReproductionClient.searchPatientAdvByParam(searchParam).getEntity();
    }

    @Override
    public PatientInfoDto patientInfoDtoByPatientCode(String patientCode) {
        return licenceClient.patientInfoDtoByPatientCode(patientCode).getEntity();
    }

    @Override
    public PatientInfoDto patientInfoDtoBySubmissionId(String submissionId) {
        return licenceClient.patientInfoDtoBySubmissionId(submissionId).getEntity();
    }


    @Override
    public PatientInventoryDto patientInventoryByCode(String patientCode, String hciCode) {
        return licenceClient.patientInventoryByCode(patientCode,hciCode).getEntity();
    }

    @Override
    public ArEnquiryCoFundingHistoryDto patientCoFundingHistoryByCode(String patientCode) {
        return licenceClient.patientCoFundingHistoryByCode(patientCode).getEntity();
    }


}
