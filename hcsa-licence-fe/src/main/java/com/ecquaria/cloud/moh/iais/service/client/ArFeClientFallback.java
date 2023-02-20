package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloud.moh.iais.helper.IaisEGPHelper;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import lombok.extern.slf4j.Slf4j;

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
        log.warn(StringUtil.changeForLog("Params: " + Arrays.toString(params)));
        return IaisEGPHelper.getFeignResponseEntity(params);
    }

    @Override
    public FeignResponseEntity<List<PatientDto>> getPatientsByConds(String idType, String idNumber, String nationality, String orgId,
            String patientType) {
        return getFeignResponseEntity(idType, idNumber, nationality, orgId, patientType);
    }

    @Override
    public FeignResponseEntity<PatientDto> getActivePatientByConds(String idType, String idNumber, String nationality, String orgId,
            String patientType) {
        return getFeignResponseEntity(idType, idNumber, nationality, orgId, patientType);
    }

    @Override
    public FeignResponseEntity<DataSubmissionDto> getPatientDataSubmissionByConds(String idType, String idNumber, String nationality,
            String orgId, String patientType) {
        return getFeignResponseEntity(idType, idNumber, nationality, orgId, patientType);
    }

    @Override
    public FeignResponseEntity<PatientInfoDto> getPatientInfoDtoByIdTypeAndIdNumber(String idType, String idNumber, String orgId) {
        return getFeignResponseEntity(idType, idNumber,orgId);
    }

    @Override
    public FeignResponseEntity<PatientInfoDto> getPatientByIdTypeAndIdNumberAndBirthDate(String idType, String idNumber, String birthDate, String orgId) {
        return getFeignResponseEntity(idType, idNumber, birthDate,orgId);
    }

    @Override
    public FeignResponseEntity<CycleStageSelectionDto> getCycleStageSelectionDtoByConds(String idType, String idNumber,
            String nationality,
            String orgId, String hciCode) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<CycleStageSelectionDto> getCycleStageSelectionDtoByConds(String patientCode, String hciCode,
            String cycleId) {
        return getFeignResponseEntity(patientCode, hciCode, cycleId);
    }

    @Override
    public FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDto(String patientCode, String hciCode,
            String cycleId) {
        return getFeignResponseEntity(patientCode, hciCode, cycleId);
    }

    @Override
    public FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDto(String submissionNo) {
        return getFeignResponseEntity(submissionNo);
    }

    @Override
    public FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDtoByDsId(String submissionId) {
        return getFeignResponseEntity(submissionId);
    }

    @Override
    public FeignResponseEntity<ArCycleStageDto> getArCycleStageDtoById(String id) {
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
    public FeignResponseEntity<List<ArSuperDataSubmissionDto>> saveArSuperDataSubmissionDtoList(
            List<ArSuperDataSubmissionDto> arSuperList) {
        return getFeignResponseEntity(arSuperList.size());
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
    public FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDtoByDraftNo(String draftNo) {
        return getFeignResponseEntity(draftNo);
    }

    @Override
    public FeignResponseEntity<List<ArSuperDataSubmissionDto>> getArSuperDataSubmissionDtoDraftByConds(String idType, String idNumber,
            String nationality, String orgId, String hciCode, boolean onlyStage, String userId) {
        return getFeignResponseEntity(idType, idNumber, nationality, orgId, hciCode, onlyStage, userId);
    }

    @Override
    public FeignResponseEntity<ArSuperDataSubmissionDto> getDraftArSuperDataSubmissionDtoByConds(String orgId, String hciCode, String submissionStage, String userId) {
        return getFeignResponseEntity(orgId, hciCode, submissionStage, userId);
    }

    @Override
    public FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDtoDraftByConds(String orgId, String type,
            String hciCode, String userId) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<ArSuperDataSubmissionDto> getArPatientSubmissionDraftByConds(String orgId, String submissionType, String idType, String identityNo, String userId) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<Void> deleteArSuperDataSubmissionDtoDraftByConds(String idType, String idNumber, String nationality,
            String orgId, String hciCode) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<Void> deleteArSuperDataSubmissionDtoDraftByConds(String orgId, String type, String hciCode) {
        return getFeignResponseEntity(orgId, type, hciCode);
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
    public FeignResponseEntity<List<CycleDto>> getCyclesByConds(String patientCode, String hciCode, String svcName) {
        return getFeignResponseEntity(patientCode, hciCode, svcName);
    }

    @Override
    public FeignResponseEntity<String> getDonorSampleKey(String idType, String idNumber) {
        return getFeignResponseEntity(idType, idNumber);
    }

    @Override
    public FeignResponseEntity<List<String>> getDonorSampleTypeKey(String idType, String idNumber, String donorSampleType) {
        return getFeignResponseEntity(idType, idNumber, donorSampleType);
    }

    @Override
    public FeignResponseEntity<List<DonorSampleAgeDto>> getDonorSampleAges(String idType, String idNumber) {
        return getFeignResponseEntity(idType, idNumber);
    }

    @Override
    public FeignResponseEntity<List<ArTreatmentSubsidiesStageDto>> getArTreatmentSubsidiesStagesByPatientInfo(String patientCode, String hciCode, String cycleType) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<List<IuiTreatmentSubsidiesDto>> getIuiTreatmentSubsidiesDtosByPhc(String patientCode, String hciCode, String cycleType) {
        return getFeignResponseEntity();
    }
    @Override
    public FeignResponseEntity<List<DonorSampleAgeDto>> getDonorSampleAgeDtoBySampleKey(String sampleCode) {
        return getFeignResponseEntity();
    }
    @Override
    public FeignResponseEntity<List<DonorSampleDto>> getDonorSampleDtoBySampleKey(String sampleKey) {
        return getFeignResponseEntity();
    }
    @Override
    public FeignResponseEntity<List<DonorSampleDto>> getMaleDonorSampleDtoByIdTypeAndIdNo(String idType, String idNo) {
        return getFeignResponseEntity();
    }
    @Override
    public FeignResponseEntity<List<DonorSampleDto>> getFemaleDonorSampleDtoByIdTypeAndIdNo(String idType, String idNo) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<List<DataSubmissionDto>> getAllDataSubmissionByCycleId(String cycleId) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<List<DonorDto>> getAllDonorDtoByCycleId(String cycleId) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<Date> getCycleStartDate(String cycleId) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<Boolean> haveEmbryoTransferGreaterFiveDay(String cycleId) {
        return null;
    }

    @Override
    public FeignResponseEntity<Boolean> haveEnhancedCounselling(String patientCode, String hciCode) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<Integer> treatmentCycleCount(String patientCode) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<Integer> embryoTransferCount(String patientCode) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<Boolean> haveStimulationCycles(String patientCode) {
        return getFeignResponseEntity();
    }

    @Override
    public FeignResponseEntity<String> getTransferConfirmationDsNo(String patientCode, String hciCode, String svcName, String submissionId) {
        return getFeignResponseEntity(patientCode, hciCode, svcName, submissionId);
    }

    @Override
    public FeignResponseEntity<Integer> getArCycleStageCountByIdTypeAndIdNoAndNationality(String idType, String idNo, String nationality) {
        return getFeignResponseEntity(idType,idNo,nationality);
    }

    @Override
    public FeignResponseEntity<PatientInfoDto> patientInfoDtoByPatientCode(String patientCode) {
        return getFeignResponseEntity(patientCode);
    }

    @Override
    public FeignResponseEntity<ArCurrentInventoryDto> getArCurrentInventoryDtoByConds(String hciCode, String licenseeId, String patientCode, String svcName) {
        return getFeignResponseEntity(hciCode, licenseeId,patientCode,svcName);
    }

    @Override
    public FeignResponseEntity<ArCurrentInventoryDto> getArCurrentInventoryDtoBySubmissionNo(String submissionNo, boolean hasAfter) {
        return getFeignResponseEntity(submissionNo, hasAfter);
    }

    @Override
    public FeignResponseEntity<Void> doUpdateDraftStatusMoreThanDays(String status, String oldStatus, int moreDays) {
        return getFeignResponseEntity(status,oldStatus,moreDays);
    }

    @Override
    public FeignResponseEntity<List<DataSubmissionDraftDto>> getRemindDraftsByRemindDays(String status, int remindDays) {
        return  getFeignResponseEntity(status,remindDays);
    }

    @Override
    public FeignResponseEntity<Void> doUpdateDraftRemindEmailStatus(List<String> draftNos) {
        return  getFeignResponseEntity(draftNos);
    }

    @Override
    public FeignResponseEntity<List<EmbryoTransferDetailDto>> getEmbryoTransferDetail(String transferStageId) {
        return  getFeignResponseEntity(transferStageId);
    }

    @Override
    public FeignResponseEntity<DsCenterDto> getDsCenterById(String id) {
        return  getFeignResponseEntity(id);
    }

    @Override
    public FeignResponseEntity<Void> deleteArSuperDataSubmissionDtoDraftByDraftNo(String draftNo) {
        return null;
    }
}
