package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.*;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "hcsa-licence", configuration = FeignConfiguration.class, fallback = DpFeClientFallback.class)
public interface DpFeClient {

    @PostMapping(value = "/dp-common/dp-data-submission", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<DpSuperDataSubmissionDto> saveDpSuperDataSubmissionDto(
            @RequestBody DpSuperDataSubmissionDto dpSuperDataSubmissionDto);

    @PostMapping(value = "/data-submission/draft/dp", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<DpSuperDataSubmissionDto> doUpdateDataSubmissionDraft(
            @RequestBody DpSuperDataSubmissionDto dpSuperDataSubmissionDto);

    @GetMapping(value = "/data-submission/draft-dp-data-submission/{draftNo}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<DpSuperDataSubmissionDto> getDpSuperDataSubmissionDtoByDraftNo(@PathVariable("draftNo") String draftNo);

    @PostMapping(value = "/data-submission/draft/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateDataSubmissionDraftStatus(@RequestParam("draftId") String draftId,
            @RequestParam("Status") String status);

    @GetMapping(value = "/data-submission/dp-data-submission/{orgId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<DpSuperDataSubmissionDto> getDpSuperDataSubmissionDtoDraftByConds(@PathVariable("orgId") String orgId,
            @RequestParam("submissionType") String submissionType, @RequestParam("svcName") String svcName, @RequestParam("hciCode") String hciCode);

    @GetMapping(value = "/data-submission/rfc-dp-data-submission/{orgId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<DpSuperDataSubmissionDto> getDpSuperDataSubmissionDtoRfcDraftByConds(@PathVariable("orgId") String orgId,
                                                                                             @RequestParam("submissionType") String submissionType,
                                                                                             @RequestParam("svcName") String svcName,
                                                                                             @RequestParam("hciCode") String hciCode,
                                                                                             @RequestParam("dataSubmissionId") String dataSubmissionId);

    @GetMapping(value = "/data-submission/pgt-stage/{patientCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<PgtStageDto>> listPgtStageByPatientCode(@PathVariable("patientCode") String patientCode) ;

    @DeleteMapping(value = "/data-submission/draft-dp-data-submission/special", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> deleteDpSuperDataSubmissionDtoDraftByConds(@RequestParam(name = "orgId") String orgId,
            @RequestParam(name = "submissionType") String submissionType,
            @RequestParam(name = "hciCode") String hciCode);

    @DeleteMapping(value = "/data-submission/rfc-draft-dp-data-submission/special", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> deleteDpSuperDataSubmissionDtoRfcDraftByConds(@RequestParam(name = "orgId") String orgId,
                                                                            @RequestParam(name = "submissionType") String submissionType,
                                                                            @RequestParam(name = "hciCode") String hciCode,
                                                                            @RequestParam(name = "dataSubmissionId") String dataSubmissionId);

    @DeleteMapping(value = "/data-submission/draft/{draftId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> deleteDpSuperDataSubmissionDraftById(@PathVariable("draftId") String draftId);

    @GetMapping(value = "/dp-common/dp-data-submission/{submissionNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<DpSuperDataSubmissionDto> getDpSuperDataSubmissionDto(@PathVariable("submissionNo") String submissionNo);

    @GetMapping(value = "/dp-common/patient/idnumber-nationality", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PatientDto> getDpPatientDto(@RequestParam(name = "idType") String idType,
                                                  @RequestParam(name = "idNumber") String idNumber, @RequestParam(name = "nationality") String nationality,
                                                  @RequestParam(name = "orgId") String orgId);

    @GetMapping(value = "/data-submission/DrugMedicationDtos/{submissionNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<DrugPrescribedDispensedDto> getDrugMedicationDtoBySubmissionNo(@PathVariable("submissionNo") String submissionNo);

    @GetMapping(value = "/data-submission/DrugMedicationDtos/Dispensed", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<DrugMedicationDto>> getDrugMedicationDtoBySubmissionNoForDispensed(@RequestParam(name = "pSubmissionNo") String pSubmissionNo,
                                                                                                @RequestParam(name = "dSubmissionNo") String dSubmissionNo);
}
