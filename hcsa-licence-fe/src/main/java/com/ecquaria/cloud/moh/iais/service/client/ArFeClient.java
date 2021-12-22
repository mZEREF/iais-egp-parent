package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArTreatmentSubsidiesStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleAgeDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DonorSampleDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.FertilisationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.IuiTreatmentSubsidiesDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientInventoryDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

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
    FeignResponseEntity<List<ArSuperDataSubmissionDto>> saveArSuperDataSubmissionDtoList(@RequestBody List<ArSuperDataSubmissionDto> arSuperList);

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
                                                          @RequestParam(value = "donorSampleCodeType",required = false) String donorSampleCodeType,
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
    FeignResponseEntity<List<DonorSampleAgeDto>> getDonorSampleAgeDtoBySampleKey(@PathVariable("sampleCode") String sampleCode);

    @GetMapping(value = "/data-submission//donorSamples/{sampleKey}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<DonorSampleDto>> getDonorSampleDtoBySampleKey(@PathVariable("sampleKey") String sampleKey);


    @GetMapping(value = "/data-submission/cycle-id", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<DataSubmissionDto>> getAllDataSubmissionByCycleId(@RequestParam("cycleId") String cycleId);

    @GetMapping(value = "/ar-common/cycle-donor-dto-list-by-cycle-id", produces = MediaType.APPLICATION_JSON_VALUE)
    public FeignResponseEntity<List<DonorDto>> getAllDonorDtoByCycleId(@RequestParam(name = "cycleId") String cycleId);

    @GetMapping(value = "/ar-common/cycle-start-date-by-cycle-id", produces = MediaType.APPLICATION_JSON_VALUE)
    public FeignResponseEntity<Date> getCycleStartDate(@RequestParam(name = "cycleId") String cycleId);

    @GetMapping(value = "/ar-common/have-greater-five-day", produces = MediaType.APPLICATION_JSON_VALUE)
    public FeignResponseEntity<Boolean> haveEmbryoTransferGreaterFiveDay(@RequestParam(name = "cycleId") String cycleId);

    @GetMapping(value = "/ar-common/have-enhanced-counselling", produces = MediaType.APPLICATION_JSON_VALUE)
    public FeignResponseEntity<Boolean> haveEnhancedCounselling(@RequestParam(name = "patientCode") String patientCode, @RequestParam(name = "hciCode") String hciCode);

    @GetMapping(value = "/ar-common/treatment-cycle-count", produces = MediaType.APPLICATION_JSON_VALUE)
    public FeignResponseEntity<Integer> treatmentCycleCount(@RequestParam(name = "patientCode") String patientCode);

    @GetMapping(value = "/ar-common/embryo-cycle-count", produces = MediaType.APPLICATION_JSON_VALUE)
    public FeignResponseEntity<Integer> embryoTransferCount(@RequestParam(name = "cycleId") String cycleId);

    @GetMapping(value = "/ar-common/have-stimulation-cycles", produces = MediaType.APPLICATION_JSON_VALUE)
    public FeignResponseEntity<Boolean> haveStimulationCycles(@RequestParam(name = "patientCode") String patientCode);
}
