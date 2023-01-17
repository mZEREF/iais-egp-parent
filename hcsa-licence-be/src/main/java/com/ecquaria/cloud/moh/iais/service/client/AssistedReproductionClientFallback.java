package com.ecquaria.cloud.moh.iais.service.client;


import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCurrentInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArEnquiryCoFundingHistoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArEnquiryCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArEnquiryDonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArEnquiryTransactionHistoryResultDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionAdvEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquiryAjaxPatientResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.AssistedReproductionEnquirySubResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DoctorInformationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsDrpEnquiryAjaxResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsDrpEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsLaboratoryDevelopTestEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsTopEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsVssEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.IncompleteCycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PgtStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.TopSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.http.HttpHeaders;

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
        return IaisEGPHelper.getFeignResponseEntity("searchPatientByParam", searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<AssistedReproductionEnquiryAjaxPatientResultsDto>> searchPatientAjaxByParam(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchPatientAjaxByParam", searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<AssistedReproductionEnquirySubResultsDto>> searchSubmissionByParam(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchSubmissionByParam", searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<AssistedReproductionAdvEnquiryResultsDto>> searchPatientAdvByParam(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchPatientAdvByParam", searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<ArEnquiryTransactionHistoryResultDto>> searchTransactionHistoryByParam(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchTransactionHistoryByParam", searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<ArEnquiryCycleStageDto>> searchCycleStageByParam(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchCycleStageByParam", searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<ArEnquiryDonorSampleDto>> searchDonorSampleByParam(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchDonorSampleByParam", searchParam);
    }

    @Override
    public FeignResponseEntity<List<DataSubmissionDto>> getActionAllDataSubmissionByCycleId(String cycleId) {
        return IaisEGPHelper.getFeignResponseEntity("getActionAllDataSubmissionByCycleId", cycleId);
    }

    @Override
    public FeignResponseEntity<List<PremisesDto>> getAllArCenterPremisesDtoByPatientCode(String patientCode,String orgId) {
        return IaisEGPHelper.getFeignResponseEntity("getAllArCenterPremisesDtoByPatientCode", patientCode, orgId);
    }

    @Override
    public FeignResponseEntity<List<PremisesDto>> getAllCenterPremisesDtoByPatientCode(String centerType, String patientCode, String orgId) {
        return IaisEGPHelper.getFeignResponseEntity("getAllCenterPremisesDtoByPatientCode", centerType, patientCode, orgId);
    }

    @Override
    public FeignResponseEntity<PatientInfoDto> patientInfoDtoByPatientCode(String patientCode) {
        return IaisEGPHelper.getFeignResponseEntity("patientInfoDtoByPatientCode", patientCode);
    }

    @Override
    public FeignResponseEntity<PatientInfoDto> patientInfoDtoBySubmissionId(String submissionId) {
        return IaisEGPHelper.getFeignResponseEntity("patientInfoDtoBySubmissionId", submissionId);
    }

    @Override
    public FeignResponseEntity<ArEnquiryCoFundingHistoryDto> patientCoFundingHistoryByCode(String patientCode) {
        return IaisEGPHelper.getFeignResponseEntity("patientCoFundingHistoryByCode", patientCode);
    }

    @Override
    public FeignResponseEntity<List<PgtStageDto>> listPgtStageByPatientCode(String patientCode) {
        return IaisEGPHelper.getFeignResponseEntity("listPgtStageByPatientCode", patientCode);
    }

    @Override
    public FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDto(String submissionNo) {
        return IaisEGPHelper.getFeignResponseEntity("getArSuperDataSubmissionDto", submissionNo);
    }

    @Override
    public FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDtoByDsId(String submissionId) {
        return IaisEGPHelper.getFeignResponseEntity("getArSuperDataSubmissionDtoByDsId", submissionId);
    }

    @Override
    public FeignResponseEntity<SearchResult<DsLaboratoryDevelopTestEnquiryResultsDto>> searchLdtByParam(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchLdtByParam", searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<DsTopEnquiryResultsDto>> searchTopByParam(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchTopByParam", searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<DsVssEnquiryResultsDto>> searchVssByParam(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchVssByParam", searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<DsDrpEnquiryResultsDto>> searchDrpByParam(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchDrpByParam", searchParam);
    }

    @Override
    public FeignResponseEntity<SearchResult<DsDrpEnquiryAjaxResultsDto>> searchDrpAjaxByParam(SearchParam searchParam) {
        return IaisEGPHelper.getFeignResponseEntity("searchDrpAjaxByParam", searchParam);
    }

    @Override
    public FeignResponseEntity<TopSuperDataSubmissionDto> getTopSuperDataSubmissionDto(String submissionNo) {
        return IaisEGPHelper.getFeignResponseEntity("getTopSuperDataSubmissionDto", submissionNo);
    }

    @Override
    public FeignResponseEntity<DpSuperDataSubmissionDto> getDpSuperDataSubmissionDto(String submissionNo) {
        return IaisEGPHelper.getFeignResponseEntity("getDpSuperDataSubmissionDto", submissionNo);
    }

    @Override
    public FeignResponseEntity<VssSuperDataSubmissionDto> getVssSuperDataSubmissionDto(String submissionNo) {
        return IaisEGPHelper.getFeignResponseEntity("getVssSuperDataSubmissionDto", submissionNo);
    }

    @Override
    public FeignResponseEntity<List<ArCurrentInventoryDto>> getArCurrentInventoryDtosByPatientCode(String patientCode) {
        return IaisEGPHelper.getFeignResponseEntity("getArCurrentInventoryDtosByPatientCode", patientCode);
    }

    @Override
    public FeignResponseEntity<List<IncompleteCycleDto>> getOverDayNotCompletedCycleDto(Integer day) {
        return IaisEGPHelper.getFeignResponseEntity("getOverDayNotCompletedCycleDto", day);
    }

    @Override
    public FeignResponseEntity<DoctorInformationDto> getAllDoctorInformationDtoByConds(String doctorReignNo, String doctorSource) {
        return IaisEGPHelper.getFeignResponseEntity("getAllDoctorInformationDtoByConds", doctorReignNo, doctorSource);
    }

    @Override
    public FeignResponseEntity<DoctorInformationDto> getDoctorInformationDtoByConds(String doctorReignNo, String doctorSource, String hciCode) {
        return IaisEGPHelper.getFeignResponseEntity("getDoctorInformationDtoByConds", doctorReignNo, doctorSource, hciCode);
    }

    @Override
    public FeignResponseEntity<DoctorInformationDto> getRfcDoctorInformationDtoByConds(String doctorInformationId) {
        return IaisEGPHelper.getFeignResponseEntity("getRfcDoctorInformationDtoByConds", doctorInformationId);
    }

    @Override
    public FeignResponseEntity<List<DoctorInformationDto>> saveDoctorInformationDtos(List<DoctorInformationDto> doctorInformationDtos) {
        return IaisEGPHelper.getFeignResponseEntity("saveDoctorInformationDtos", doctorInformationDtos);
    }

    @Override
    public FeignResponseEntity<Integer> deleteDoctorByConds(List<String> prns, String doctorSource) {
        return IaisEGPHelper.getFeignResponseEntity("deleteDoctorByConds", prns, doctorSource);
    }

    @Override
    public FeignResponseEntity<List<DonorSampleDto>> getDonorSampleListByIdNumber(String idNumber) {
        return IaisEGPHelper.getFeignResponseEntity(idNumber);
    }
}
