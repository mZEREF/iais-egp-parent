package com.ecquaria.cloud.moh.iais.service;

import com.ecquaria.cloud.moh.iais.common.dto.SearchParam;
import com.ecquaria.cloud.moh.iais.common.dto.SearchResult;
import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsDrpEnquiryAjaxResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsDrpEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsLaboratoryDevelopTestEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsTopEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DsVssEnquiryResultsDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.IncompleteCycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInfoDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PgtStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;

import java.util.List;

/**
 * AssistedReproductionService
 *
 * @author junyu
 * @date 2021/11/19
 */
public interface AssistedReproductionService{

    SearchResult<AssistedReproductionEnquiryResultsDto> searchPatientByParam(SearchParam searchParam);

    SearchResult<AssistedReproductionEnquiryAjaxPatientResultsDto> searchPatientAjaxByParam(SearchParam searchParam);

    SearchResult<AssistedReproductionEnquirySubResultsDto> searchSubmissionByParam(SearchParam searchParam);

    SearchResult<AssistedReproductionAdvEnquiryResultsDto> searchPatientAdvByParam(SearchParam searchParam);

    SearchResult<ArEnquiryTransactionHistoryResultDto> searchTransactionHistoryByParam(SearchParam searchParam);

    SearchResult<ArEnquiryCycleStageDto> searchCycleStageByParam(SearchParam searchParam);

    SearchResult<ArEnquiryDonorSampleDto> searchDonorSampleByParam(SearchParam searchParam);

    SearchResult<DsLaboratoryDevelopTestEnquiryResultsDto> searchDsLdtByParam(SearchParam searchParam);

    SearchResult<DsTopEnquiryResultsDto> searchDsTopByParam(SearchParam searchParam);

    SearchResult<DsVssEnquiryResultsDto> searchDsVssByParam(SearchParam searchParam);

    SearchResult<DsDrpEnquiryResultsDto> searchDrpByParam(SearchParam searchParam);

    SearchResult<DsDrpEnquiryAjaxResultsDto> searchDrpAjaxByParam(SearchParam searchParam);

    List<DataSubmissionDto> allDataSubmissionByCycleId(String cycleId);

    PatientInfoDto patientInfoDtoByPatientCode( String patientCode);

    PatientInfoDto patientInfoDtoBySubmissionId( String submissionId);

    ArEnquiryCoFundingHistoryDto patientCoFundingHistoryByCode(String patientCode);

    ArSuperDataSubmissionDto getArSuperDataSubmissionDto(String submissionNo);

    ArSuperDataSubmissionDto getArSuperDataSubmissionDtoById(String submissionId);


    List<SelectOption> genPremisesOptions(String centerType,String patientCode);

    List<PgtStageDto> listPgtStageByPatientCode( String patientCode);

    List<ArCurrentInventoryDto> arCurrentInventoryDtosByPatientCode(String patientCode);


    List<IncompleteCycleDto> getOverDayNotCompletedCycleDto(int day);

    ProfessionalResponseDto retrievePrsInfo(String profRegNo);

}
