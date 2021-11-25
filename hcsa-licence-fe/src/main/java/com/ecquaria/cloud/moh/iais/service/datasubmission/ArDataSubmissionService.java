package com.ecquaria.cloud.moh.iais.service.datasubmission;

import com.ecquaria.cloud.moh.iais.common.dto.SelectOption;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSubFreezingStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleAgeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInventoryDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.licence.PremisesDto;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface ArDataSubmissionService {

    Map<String, PremisesDto> getArCenterPremises(String licenseeId);

    CycleStageSelectionDto getCycleStageSelectionDtoByConds(String idType, String idNumber, String nationality, String orgId,
            String hciCode);

    ArSuperDataSubmissionDto getArSuperDataSubmissionDtoBySubmissionNo(String submissionNo);

    ArSuperDataSubmissionDto getArSuperDataSubmissionDto(String patientCode, String hciCOde);

    ArSuperDataSubmissionDto saveArSuperDataSubmissionDto(ArSuperDataSubmissionDto arSuperDataSubmission);

    ArSuperDataSubmissionDto saveArSuperDataSubmissionDtoToBE(ArSuperDataSubmissionDto arSuperDataSubmission);

    ArSuperDataSubmissionDto saveDataSubmissionDraft(ArSuperDataSubmissionDto arSuperDataSubmissionDto);

    ArSuperDataSubmissionDto getArSuperDataSubmissionDtoDraftById(String id);

    ArSuperDataSubmissionDto getArSuperDataSubmissionDtoDraftByDraftNo(String draftNo);

    ArSuperDataSubmissionDto getArSuperDataSubmissionDtoDraftByConds(String idType, String idNumber, String nationality, String orgId,
            String hciCode);

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

    List<CycleDto> getByPatientCodeAndHciCodeAndCycleTypeAndStatuses(String patientCode,String hciCode, String cycleType,String... status);

    void updateDataSubmissionDraftStatus(String draftId, String status);

    Date getLastCompletedCycleStartDate(String patientCode, String hciCode);

    /**
      * @author: shicheng
      * @Date 2021/11/9
      * @Param: patientInventoryDto, arSubFreezingStageDto
      * @return: PatientInventoryDto
      * @Descripation: setFreezingPatientChange
      */
    PatientInventoryDto setFreezingPatientChange(PatientInventoryDto patientInventoryDto, ArSubFreezingStageDto arSubFreezingStageDto);

    DonorSampleDto getDonorSampleDto(String idType,String idNumber,String donorSampleCode,String sampleFromHciCode,String sampleFromOthers);

    /**
      * @author: shicheng
      * @Date 2021/11/15
      * @Param: null
      * @return: List<SelectOption>
      * @Descripation: getSourceOfSemenOption
      */
    List<SelectOption> getSourceOfSemenOption();

    /**
     * @author: shicheng
     * @Date 2021/11/15
     * @Param: null
     * @return: List<SelectOption>
     * @Descripation: getChildNumOption
     */
    List<SelectOption> getChildNumOption();

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
      * @Param: numberStr
      * @return: Integer
      * @Descripation: stringTransferInteger
      */
    Integer stringTransferInteger(String numberStr);

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
}
