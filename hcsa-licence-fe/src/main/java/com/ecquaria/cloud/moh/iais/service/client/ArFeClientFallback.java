package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @Description ArCommonFeClientFallback
 * @Auther chenlei on 10/26/2021.
 */
@Slf4j
public class ArFeClientFallback implements ArFeClient {

    private FeignResponseEntity getFeignResponseEntity(Object... params) {
        log.warn("--------Params: " + Arrays.toString(params) + "-----------");
        FeignResponseEntity entity = new FeignResponseEntity<>();
        HttpHeaders headers = new HttpHeaders();
        entity.setHeaders(headers);
        return entity;
    }

    @Override
    public FeignResponseEntity<PatientDto> getPatientDto(String idType, String idNumber, String nationality, String orgId) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<CycleStageSelectionDto> getCycleStageSelectionDtoByConds(String idType, String idNumber,
            String nationality,
            String orgId, String hciCode) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDto(String patientCode, String hciCode) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<ArCycleStageDto> saveArCycleStageDto(ArCycleStageDto arCycleStageDto) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<ArCycleStageDto> getArCycleStageDtoById(String id) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<List<ArCycleStageDto>> getArCycleStageDtosBySubmissionId(String submissionId) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<ArDonorDto> saveArDonorDto(ArDonorDto arDonorDto) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<ArDonorDto> getArDonorDtoById(String id) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<List<ArDonorDto>> getArDonorDtosByCycleStageId(String cycleStageId) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<FertilisationDto> saveFertilisationDto(FertilisationDto fertilisationDto) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<FertilisationDto> getFertilisationDtoById(String id) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<List<FertilisationDto>> getFertilisationDtosBySubmissionId(String submissionId) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<ArSuperDataSubmissionDto> saveArSuperDataSubmissionDto(
            ArSuperDataSubmissionDto arSuperDataSubmissionDto) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<ArSuperDataSubmissionDto> doUpdateDataSubmissionDraft(
            ArSuperDataSubmissionDto arSuperDataSubmissionDto) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDtoDraftById(String id) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDtoDraftByDraftNo(String draftNo) {
        return getFeignResponseEntity(draftNo);
    }

    @Override
    public FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDtoDraftByConds(String idType, String idNumber,
            String nationality, String orgId, String hciCode) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType,
            String hciCode) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<Void> deleteArSuperDataSubmissionDtoDraftByConds(String idType, String idNumber, String nationality,
            String orgId, String hciCode) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<Void> deleteArSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType, String hciCode) {
        return getFeignResponseEntity(submissionType, hciCode);
    }

    @Override
    public FeignResponseEntity<List<CycleDto>> getByPatientCodeAndHciCodeAndCycleTypeAndStatuses(CycleDto cycleDto) {
        return getFeignResponseEntity(cycleDto);
    }

    @Override
    public FeignResponseEntity<Void> updateDataSubmissionDraftStatus(String draftId, String status) {
        return getFeignResponseEntity(draftId, status);
    }

    @Override
    public FeignResponseEntity<Date> getLastCompletedCycleStartDate(String patientCode, String hciCode) {
        return getFeignResponseEntity(patientCode, hciCode);
    }

    @Override
    public FeignResponseEntity<DonorSampleDto> getDonorSampleDto(String idType, String idNumber, String donorSampleCode,String sampleFromHciCode,String sampleFromOthers) {
        return  getFeignResponseEntity(idType,idNumber,donorSampleCode,sampleFromHciCode,sampleFromOthers);
    }

    @Override
    public FeignResponseEntity<PatientInventoryDto> patientInventoryByCode(String patientCode) {
        return  getFeignResponseEntity(patientCode);
    }

}
