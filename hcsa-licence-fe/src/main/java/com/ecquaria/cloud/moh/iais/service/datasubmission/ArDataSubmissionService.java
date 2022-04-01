package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ArDataSubmissionService {

    Map<String, PremisesDto> getArCenterPremises(String licenseeId);

    CycleStageSelectionDto getCycleStageSelectionDtoByConds(String idType, String idNumber, String nationality, String orgId,
            String hciCode);

    CycleStageSelectionDto getCycleStageSelectionDtoByConds(String patientCode, String hciCode, String cycleId);

    ArSuperDataSubmissionDto getArSuperDataSubmissionDtoBySubmissionNo(String submissionNo);

    ArSuperDataSubmissionDto getArSuperDataSubmissionDto(String patientCode, String hciCOde, String cycleId);

    ArSuperDataSubmissionDto saveArSuperDataSubmissionDto(ArSuperDataSubmissionDto arSuperDataSubmission);

    ArSuperDataSubmissionDto saveArSuperDataSubmissionDtoToBE(ArSuperDataSubmissionDto arSuperDataSubmission);

    List<ArSuperDataSubmissionDto> saveArSuperDataSubmissionDtoList(List<ArSuperDataSubmissionDto> arSuperList);

    List<ArSuperDataSubmissionDto> saveArSuperDataSubmissionDtoListToBE(List<ArSuperDataSubmissionDto> arSuperList);

    void  saveBeArSuperDataSubmissionDtoForEic(EicArSuperDataSubmissionDto eicArSuperDataSubmissionDto);

    ArSuperDataSubmissionDto saveDataSubmissionDraft(ArSuperDataSubmissionDto arSuperDataSubmissionDto);

    ArSuperDataSubmissionDto getArSuperDataSubmissionDtoDraftById(String id);

    ArSuperDataSubmissionDto getArSuperDataSubmissionDtoByDraftNo(String draftNo);

    ArSuperDataSubmissionDto getArSuperDataSubmissionDtoDraftByConds(String idType, String idNumber, String nationality, String orgId,
            String hciCode, boolean onlyStage);

    ArSuperDataSubmissionDto getArSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType, String hciCode);

    void deleteArSuperDataSubmissionDtoDraftByConds(String idType, String idNumber, String nationality, String orgId,
            String hciCode);

    void deleteArSuperDataSubmissionDtoDraftByConds(String orgId, String submissionType, String hciCode);

    String getSubmissionNo(CycleStageSelectionDto selectionDto, String dsType);

    String getDraftNo(String dsType, String draftNo);

    /**
      * @author: shicheng
      * @Date 2021/11/1
      * @Param: ArSubFreezingStageDto
      * @return: arSubFreezingStageDto, cryopreservedNum, cryopreservationDate
      * @Descripation: set cryopreservedNum and cryopreservationDate
      */
    ArSubFreezingStageDto setFreeCryoNumAndDate(ArSubFreezingStageDto arSubFreezingStageDto, String cryopreservedNum, String cryopreservationDate);

    /**
      * @author: shicheng
      * @Date 2021/11/2
      * @Param: freeCryoRadio, arSubFreezingStageDto, freeCryoOptions
      * @return: ArSubFreezingStageDto
      * @Descripation: Verify that the value is dirty data
      */
    ArSubFreezingStageDto checkValueIsDirtyData(String freeCryoRadio, ArSubFreezingStageDto arSubFreezingStageDto, List<SelectOption> freeCryoOptions);

    List<CycleDto> getCyclesByConds(String patientCode, String hciCode, String svcName);

    List<CycleDto> getByPatientCodeAndHciCodeAndCycleTypeAndStatuses(String patientCode,String hciCode, String cycleType,String... status);

    void updateDataSubmissionDraftStatus(String draftId, String status);

    Date getLastCompletedCycleStartDate(String patientCode, String hciCode);

    DonorSampleDto getDonorSampleDto(boolean directedDonation,String idType,String idNumber,String donorSampleCodeType,String donorSampleCode,String liceId,String hciCode);

    /**
      * @author: shicheng
      * @Date 2021/11/15
      * @Param: arSuperDataSubmission
      * @return: ArSuperDataSubmissionDto
      * @Descripation: setIuiCycleStageDtoDefaultVal
      */
    ArSuperDataSubmissionDto setIuiCycleStageDtoDefaultVal(ArSuperDataSubmissionDto arSuperDataSubmission);
    /**
      * @author: shicheng
      * @Date 2021/11/16
      * @Param: stringArr, selectOptionList
      * @return: List<String>
      * @Descripation: checkBoxIsDirtyData
      */
    List<String> checkBoxIsDirtyData(String[] stringArr, List<SelectOption> selectOptionList);

    ArSuperDataSubmissionDto setFreeStageDtoDefaultVal(ArSuperDataSubmissionDto arSuperDataSubmission);

    List<DonorSampleAgeDto> getDonorSampleAgeDtoBySampleKey(String sampleKey);

    List<DonorSampleDto> getDonorSampleDtoBySampleKey(String sampleKey);

    List<String> saveFileRepo(List<File> files);

    List<DonorDto> getAllDonorDtoByCycleId(String cycleId);

    Date getCycleStartDate(String cycleId);

    boolean flagOutEnhancedCounselling(ArSuperDataSubmissionDto arSuperDataSubmissionDto);

    boolean flagOutEmbryoTransferAgeAndCount(ArSuperDataSubmissionDto arSuperDataSubmissionDto);

    boolean flagOutEmbryoTransferCountAndPatAge(ArSuperDataSubmissionDto arSuperDataSubmissionDto);

    boolean haveStimulationCycles(String patientCode);

    int embryoTransferCount(String cycleDtoId);

    boolean haveEmbryoTransferGreaterFiveDay(String cycleId);

    void sendIncompleteCycleNotificationPeriod();

    int getArCycleStageCountByIdTypeAndIdNoAndNationality(PatientDto patientDto);

    ArCurrentInventoryDto getArCurrentInventoryDtoByConds(String hciCode, String licenseeId, String patientCode, String svcName);

    ArCurrentInventoryDto getArCurrentInventoryDtoBySubmissionNo(String submissionNo, boolean hasAfter);

    DonorSampleDto getDonorSampleDto(DonorSampleDto donorSampleDto);

    void remindAndDeleteDraftSubJob();
}
