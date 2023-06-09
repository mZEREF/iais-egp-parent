package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;

import java.util.List;

/**
 * AssistedReproductionClientFallback
 *
 * @author junyu
 * @date 2021/11/19
 */
public class AssistedReproductionClientFallback implements AssistedReproductionClient{
    @Override
    public FeignResponseEntity<SearchResult<AssistedReproductionEnquiryResultsDto>> searchPatientByParam(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchPatientByParam",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<AssistedReproductionEnquiryAjaxPatientResultsDto>> searchPatientAjaxByParam(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchPatientAjaxByParam",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<AssistedReproductionEnquirySubResultsDto>> searchSubmissionByParam(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchSubmissionByParam",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<AssistedReproductionAdvEnquiryResultsDto>> searchPatientAdvByParam(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchPatientAdvByParam",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<ArEnquiryTransactionHistoryResultDto>> searchTransactionHistoryByParam(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchTransactionHistoryByParam",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<ArEnquiryCycleStageDto>> searchCycleStageByParam(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchCycleStageByParam",searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<ArEnquiryDonorSampleDto>> searchDonorSampleByParam(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchDonorSampleByParam",searchParam);
    }

    @Override
    public FeignResponseEntity<List<DataSubmissionDto>> getAllDataSubmissionByCycleId(String cycleId) {
        return IaisEGPHelper.getFeignResponseEntity("getAllDataSubmissionByCycleId",cycleId);
    }

    @Override
    public FeignResponseEntity<List<PremisesDto>> getAllArCenterPremisesDtoByPatientCode(String patientCode,String orgId) {
        return IaisEGPHelper.getFeignResponseEntity("getAllArCenterPremisesDtoByPatientCode",patientCode,orgId);
    }

    @Override
    public FeignResponseEntity<List<PremisesDto>> getAllCenterPremisesDtoByPatientCode(String centerType, String patientCode, String orgId) {
        return IaisEGPHelper.getFeignResponseEntity("getAllCenterPremisesDtoByPatientCode",centerType,patientCode,orgId);
    }

    @Override
    public FeignResponseEntity<PatientInfoDto> patientInfoDtoByPatientCode(String patientCode) {
        return IaisEGPHelper.getFeignResponseEntity("patientInfoDtoByPatientCode",patientCode);
    }

    @Override
    public FeignResponseEntity<PatientInfoDto> patientInfoDtoBySubmissionId(String submissionId) {
        return IaisEGPHelper.getFeignResponseEntity("patientInfoDtoByPatientCode",submissionId);
    }

    @Override
    public FeignResponseEntity<ArEnquiryCoFundingHistoryDto> patientCoFundingHistoryByCode(String patientCode) {
        return IaisEGPHelper.getFeignResponseEntity("patientInfoDtoByPatientCode",patientCode);
    }

    @Override
    public FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDto(String submissionNo) {
        return IaisEGPHelper.getFeignResponseEntity("patientInfoDtoByPatientCode",submissionNo);
    }

    @Override
    public FeignResponseEntity<List<ArCurrentInventoryDto>> getArCurrentInventoryDtosByPatientCode(String patientCode) {
        return IaisEGPHelper.getFeignResponseEntity("patientInfoDtoByPatientCode",patientCode);
    }

    @Override
    public FeignResponseEntity<SearchResult<DsLaboratoryDevelopTestEnquiryResultsDto>> searchLdtByParam(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("patientInfoDtoByPatientCode",searchParam);
    }

    @Override
    public FeignResponseEntity<List<DonorSampleDto>> getDonorSampleListByIdNumber(String idNumber) {
        return IaisEGPHelper.getFeignResponseEntity(idNumber);
    }

    @Override
    public FeignResponseEntity<List<DonorSampleAgeDto>> getByDonorSampleId(String donorSampleId) {
        return IaisEGPHelper.getFeignResponseEntity(donorSampleId);
    }
}
