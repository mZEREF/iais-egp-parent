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
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalParameterDto;
import com.ecquaria.cloud.moh.iais.common.dto.prs.ProfessionalResponseDto;
import com.ecquaria.cloud.moh.iais.common.helper.HmacHelper;
import com.ecquaria.cloud.moh.iais.common.utils.IaisCommonUtils;
import com.ecquaria.cloud.moh.iais.common.utils.JsonUtil;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.service.AssistedReproductionService;
import com.ecquaria.cloud.moh.iais.service.client.AssistedReproductionClient;
import com.ecquaria.cloud.moh.iais.service.client.BeEicGatewayClient;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AssistedReproductionServiceImpl
 *
 * @author junyu
 * @date 2021/11/19
 */
@Slf4j
@Service
public class AssistedReproductionServiceImpl implements AssistedReproductionService{

    @Value("${moh.halp.prs.enable}")
    private String prsFlag;

    @Value("${iais.hmac.keyId}")
    private String keyId;

    @Value("${iais.hmac.second.keyId}")
    private String secKeyId;

    @Value("${iais.hmac.secretKey}")
    private String secretKey;

    @Value("${iais.hmac.second.secretKey}")
    private String secSecretKey;

    @Autowired
    private BeEicGatewayClient beEicGatewayClient;

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
        return assistedReproductionClient.getActionAllDataSubmissionByCycleId(cycleId).getEntity();
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
    public List<SelectOption> genPremisesOptions(String centerType,String patientCode) {
        List<PremisesDto> premisesDtos=assistedReproductionClient.getAllCenterPremisesDtoByPatientCode(centerType,patientCode,"null").getEntity();
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

    @Override
    public ProfessionalResponseDto retrievePrsInfo(String profRegNo) {
        log.info(StringUtil.changeForLog("Prof Reg No is " + profRegNo + " - PRS flag is " + prsFlag));
        ProfessionalResponseDto professionalResponseDto = null;
        if ("Y".equals(prsFlag) && !StringUtil.isEmpty(profRegNo)) {
            List<String> prgNos = IaisCommonUtils.genNewArrayList();
            prgNos.add(profRegNo);
            ProfessionalParameterDto professionalParameterDto = new ProfessionalParameterDto();
            professionalParameterDto.setRegNo(prgNos);
            professionalParameterDto.setClientId("22222");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            String format = simpleDateFormat.format(new Date());
            professionalParameterDto.setTimestamp(format);
            professionalParameterDto.setSignature("2222");
            try {
                HmacHelper.Signature signature = HmacHelper.getSignature(keyId, secretKey);
                HmacHelper.Signature signature2 = HmacHelper.getSignature(secKeyId, secSecretKey);
                FeignResponseEntity<List> entity = beEicGatewayClient.getProfessionalDetail(professionalParameterDto,signature.date(),signature.authorization(),signature2.date(),signature2.authorization());
                if (401 == entity.getStatusCode()) {
                    professionalResponseDto = new ProfessionalResponseDto();
                    professionalResponseDto.setStatusCode("401");
                } else {
                    List<ProfessionalResponseDto> professionalResponseDtos = entity.getEntity();
                    if (professionalResponseDtos != null && professionalResponseDtos.size() > 0) {
                        professionalResponseDto = professionalResponseDtos.get(0);
                        List<String> qualification = professionalResponseDto.getQualification();
                        List<String> subspecialty = professionalResponseDto.getSubspecialty();
                        if (qualification != null && qualification.size() > 1) {
                            professionalResponseDto.setQualification(Collections.singletonList(qualification.stream()
                                    .collect(Collectors.joining(","))));
                        }
                        if (subspecialty != null && subspecialty.size() > 1) {
                            professionalResponseDto.setSubspecialty(Collections.singletonList(subspecialty.stream()
                                    .collect(Collectors.joining(","))));
                        }
                    }
                    if (professionalResponseDto == null) {
                        professionalResponseDto = new ProfessionalResponseDto();
                        professionalResponseDto.setStatusCode("-1");
                    } else if (StringUtil.isEmpty(professionalResponseDto.getName())) {
                        professionalResponseDto.setStatusCode("-2");
                    }
                }
            } catch (Exception e) {
                professionalResponseDto = new ProfessionalResponseDto();
                professionalResponseDto.setHasException(true);
                log.info(StringUtil.changeForLog("retrieve prs info start ..."));
                log.error(StringUtil.changeForLog(e.getMessage()), e);
            }
        }
        log.info(StringUtil.changeForLog("ProfessionalResponseDto: " + JsonUtil.parseToJson(professionalResponseDto)));
        return professionalResponseDto;
    }
}
