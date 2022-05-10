package com.ecquaria.cloud.moh.iais.service.impl;

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
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.service.AssistedReproductionService;
import com.ecquaria.cloud.moh.iais.service.client.AssistedReproductionClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    public SearchResult<ArEnquiryTransactionHistoryResultDto> searchTransactionHistoryByParam(SearchParam searchParam) {
        return assistedReproductionClient.searchTransactionHistoryByParam(searchParam).getEntity();
    }

    @Override
    public SearchResult<ArEnquiryCycleStageDto> searchCycleStageByParam(SearchParam searchParam) {
        return assistedReproductionClient.searchCycleStageByParam(searchParam).getEntity();
    }

    @Override
    public SearchResult<ArEnquiryDonorSampleDto> searchDonorSampleByParam(SearchParam searchParam) {
        return assistedReproductionClient.searchDonorSampleByParam(searchParam).getEntity();
    }

    @Override
    public SearchResult<DsLaboratoryDevelopTestEnquiryResultsDto> searchDsLdtByParam(SearchParam searchParam) {
        return assistedReproductionClient.searchLdtByParam(searchParam).getEntity();
    }

    @Override
    public SearchResult<DsTopEnquiryResultsDto> searchDsTopByParam(SearchParam searchParam) {
        return assistedReproductionClient.searchTopByParam(searchParam).getEntity();
    }

    @Override
    public SearchResult<DsVssEnquiryResultsDto> searchDsVssByParam(SearchParam searchParam) {
        return assistedReproductionClient.searchVssByParam(searchParam).getEntity();
    }

    @Override
    public SearchResult<DsDrpEnquiryResultsDto> searchDrpByParam(SearchParam searchParam) {
        return assistedReproductionClient.searchDrpByParam(searchParam).getEntity();
    }

    @Override
    public SearchResult<DsDrpEnquiryAjaxResultsDto> searchDrpAjaxByParam(SearchParam searchParam) {
        return assistedReproductionClient.searchDrpAjaxByParam(searchParam).getEntity();
    }

    @Override
    public List<DataSubmissionDto> allDataSubmissionByCycleId(String cycleId) {
        return assistedReproductionClient.getAllDataSubmissionByCycleId(cycleId).getEntity();
    }

    @Override
    public PatientInfoDto patientInfoDtoByPatientCode(String patientCode) {
        return assistedReproductionClient.patientInfoDtoByPatientCode(patientCode).getEntity();
    }

    @Override
    public PatientInfoDto patientInfoDtoBySubmissionId(String submissionId) {
        return assistedReproductionClient.patientInfoDtoBySubmissionId(submissionId).getEntity();
    }

    @Override
    public ArEnquiryCoFundingHistoryDto patientCoFundingHistoryByCode(String patientCode) {
        return assistedReproductionClient.patientCoFundingHistoryByCode(patientCode).getEntity();
    }

    @Override
    public ArSuperDataSubmissionDto getArSuperDataSubmissionDto(String submissionNo) {
        return assistedReproductionClient.getArSuperDataSubmissionDto(submissionNo).getEntity();
    }

    @Override
    public ArSuperDataSubmissionDto getArSuperDataSubmissionDtoById(String submissionId) {
        return assistedReproductionClient.getArSuperDataSubmissionDtoByDsId(submissionId).getEntity();
    }

    @Override
    public List<SelectOption> genPremisesOptions(String patientCode) {
        List<PremisesDto> premisesDtos=assistedReproductionClient.getAllArCenterPremisesDtoByPatientCode(patientCode,"null").getEntity();
        Map<String, PremisesDto> premisesMap = IaisCommonUtils.genNewHashMap();
        if(IaisCommonUtils.isNotEmpty(premisesDtos)){
            for (PremisesDto premisesDto : premisesDtos) {
                if(premisesDto!=null){
                    premisesMap.put(premisesDto.getHciCode(), premisesDto);
                }
            }
        }

        Map<String, String> map = IaisCommonUtils.genNewLinkedHashMap();
        if (!premisesMap.isEmpty()) {
            for (Map.Entry<String, PremisesDto> entry : premisesMap.entrySet()) {
                map.put(entry.getKey(), entry.getValue().getPremiseLabel());
            }
        }
        List<SelectOption> opts = IaisCommonUtils.genNewArrayList();
        if (!map.isEmpty()) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                opts.add(new SelectOption(entry.getKey(), entry.getValue()));
            }
        }
        Collections.sort(opts);

        return opts;
    }

    @Override
    public List<PgtStageDto> listPgtStageByPatientCode(String patientCode) {
        return assistedReproductionClient.listPgtStageByPatientCode(patientCode).getEntity();
    }

    @Override
    public List<ArCurrentInventoryDto> arCurrentInventoryDtosByPatientCode(String patientCode) {
        return assistedReproductionClient.getArCurrentInventoryDtosByPatientCode(patientCode).getEntity();
    }

    @Override
    public List<IncompleteCycleDto> getOverDayNotCompletedCycleDto(int day) {
        return assistedReproductionClient.getOverDayNotCompletedCycleDto(day).getEntity();
    }
}
