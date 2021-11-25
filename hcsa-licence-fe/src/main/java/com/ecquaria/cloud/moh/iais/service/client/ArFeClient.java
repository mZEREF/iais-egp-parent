package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@FeignClient(name = "hcsa-licence", configuration = FeignConfiguration.class, fallback = ArFeClientFallback.class)
public interface ArFeClient {

    @GetMapping(value = "/ar-common/patient/idnumber-nationality", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PatientDto> getPatientDto(@RequestParam(name = "idType") String idType,
            @RequestParam(name = "idNumber") String idNumber, @RequestParam(name = "nationality") String nationality,
            @RequestParam(name = "orgId") String orgId);

    @GetMapping(value = "/data-submission/cycle-stage-selection", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<CycleStageSelectionDto> getCycleStageSelectionDtoByConds(@RequestParam(name = "idType") String idType,
            @RequestParam(name = "idNumber") String idNumber, @RequestParam(name = "nationality") String nationality,
            @RequestParam(name = "orgId") String orgId, @RequestParam(name = "hciCode") String hciCode);

    @GetMapping(value = "/ar-common/ar-data-submission/patient-code-hci-code", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDto(@RequestParam(name = "patientCode") String patientCode,
            @RequestParam(name = "hciCode", required = false) String hciCode);

    @GetMapping(value = "/ar-common/ar-data-submission/{submissionNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDto(@PathVariable("submissionNo")String submissionNo);

    @GetMapping(value = "/ar-common/ar-cycle-stage/id", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ArCycleStageDto> getArCycleStageDtoById(@RequestParam(name = "id") String id);

    @PutMapping(value = "/ar-common/fertilisation/save", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FertilisationDto> saveFertilisationDto(@RequestBody FertilisationDto fertilisationDto);

    @GetMapping(value = "/ar-common/fertilisation/id", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FertilisationDto> getFertilisationDtoById(@RequestParam(name = "id") String id);

    @GetMapping(value = "/ar-common/fertilisation/submissionId", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<FertilisationDto>> getFertilisationDtosBySubmissionId(
            @RequestParam(name = "submissionId") String submissionId);

    @PostMapping(value = "/ar-common/ar-data-submission", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ArSuperDataSubmissionDto> saveArSuperDataSubmissionDto(
            @RequestBody ArSuperDataSubmissionDto arSuperDataSubmissionDto);

    @PostMapping(value = "/data-submission/draft", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ArSuperDataSubmissionDto> doUpdateDataSubmissionDraft(
            @RequestBody ArSuperDataSubmissionDto arSuperDataSubmissionDto);

    @GetMapping(value = "/data-submission/draft-ar-data-submission/id", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDtoDraftById(@RequestParam(name = "id") String id);

    @GetMapping(value = "/data-submission/draft-ar-data-submission/{draftNo}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDtoDraftByDraftNo(@PathVariable("draftNo") String draftNo);

    @GetMapping(value = "/data-submission/draft-ar-data-submission/cycle", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDtoDraftByConds(@RequestParam(name = "idType") String idType,
            @RequestParam(name = "idNumber") String idNumber, @RequestParam(name = "nationality") String nationality,
            @RequestParam(name = "orgId") String orgId, @RequestParam(name = "hciCode") String hciCode);

    @GetMapping(value = "/data-submission/draft-ar-data-submission/special", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDtoDraftByConds(@RequestParam(name = "orgId") String orgId,
            @RequestParam(name = "submissionType") String submissionType,
            @RequestParam(name = "hciCode", required = false) String hciCode);

    @PostMapping(value = "/data-submission/draft-ar-data-submission/cycle", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> deleteArSuperDataSubmissionDtoDraftByConds(@RequestParam(name = "idType") String idType,
            @RequestParam(name = "idNumber") String idNumber, @RequestParam(name = "nationality") String nationality,
            @RequestParam(name = "orgId") String orgId, @RequestParam(name = "hciCode") String hciCode);

    @DeleteMapping(value = "/data-submission/draft-ar-data-submission/special", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> deleteArSuperDataSubmissionDtoDraftByConds(@RequestParam(name = "orgId") String orgId,
            @RequestParam(name = "submissionType") String submissionType,
            @RequestParam(name = "hciCode", required = false) String hciCode);

    @PostMapping(value = "/data-submission/cycle/patientcode-hcicode-cycletype-stauses", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<CycleDto>> getByPatientCodeAndHciCodeAndCycleTypeAndStatuses(@RequestBody CycleDto cycleDto);

    @PostMapping(value = "/data-submission/draft/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateDataSubmissionDraftStatus(@RequestParam("draftId") String draftId,
            @RequestParam("Status") String status);

    @GetMapping(value = "/data-submission/last-completed-cycle/start-date", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Date> getLastCompletedCycleStartDate(@RequestParam("patientCode") String patientCode,
            @RequestParam("hciCode") String hciCode);


    @GetMapping(value = "/data-submission/data-submission-donor-sample", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<DonorSampleDto> getDonorSampleDto(@RequestParam(value = "idType",required = false) String idType, @RequestParam(value = "idNumber",required = false) String idNumber,
                                                          @RequestParam(value = "donorSampleCode",required = false) String donorSampleCode,
                                                          @RequestParam(value = "sampleFromHciCode",required = false) String sampleFromHciCode,
                                                          @RequestParam(value = "sampleFromOthers",required = false) String sampleFromOthers);

    @GetMapping(value = "/data-submission/patient-inventory/{patientCode}/{hciCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PatientInventoryDto> patientInventoryByCode(@PathVariable("patientCode") String patientCode,@PathVariable("hciCode") String hciCode);

    @GetMapping(value = "/ar-common/arTreatmentSubsidiesStageDtos/patientcode-hcicode-cycletype", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ArTreatmentSubsidiesStageDto>> getArTreatmentSubsidiesStagesByPatientInfo(@RequestParam(name = "patientCode") String patientCode,
                                                                                                       @RequestParam(name = "hciCode") String hciCode,
                                                                                                       @RequestParam(name = "cycleType") String cycleType);

    @GetMapping(value = "/ar-common/iuiTreatmentSubsidiesDtos/patientcode-hcicode-cycletype", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<IuiTreatmentSubsidiesDto>> getIuiTreatmentSubsidiesDtosByPhc(@RequestParam(name = "patientCode") String patientCode,
                                                                                                                          @RequestParam(name = "hciCode") String hciCode,
                                                                                                                          @RequestParam(name = "cycleType") String cycleType);
    @GetMapping(value = "/data-submission/donorSampleAges/{sampleCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<DonorSampleAgeDto>> getDonorSampleAgeDtoBySampleKey(@PathVariable("sampleCode") String sampleCode) ;
}
