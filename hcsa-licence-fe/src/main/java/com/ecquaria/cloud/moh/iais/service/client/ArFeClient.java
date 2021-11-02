package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArDonorDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.CycleStageSelectionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.FertilisationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "hcsa-licence",configuration = FeignConfiguration.class,fallback = ArFeClientFallback.class)
public interface ArFeClient {

    @GetMapping(value = "/ar-common/patient/idnumber-nationality", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PatientDto> getPatientDto(@RequestParam(name = "idType") String idType,
            @RequestParam(name = "idNumber") String idNumber, @RequestParam(name = "nationality") String nationality,
            @RequestParam(name = "orgId") String orgId);

    @GetMapping(value = "/data-submission/cycle-stage-selection", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<CycleStageSelectionDto> getCycleStageSelectionDtoByConds(@RequestParam(name = "idType") String idType,
            @RequestParam(name = "idNumber") String idNumber, @RequestParam(name = "nationality") String nationality,
            @RequestParam(name = "orgId") String orgId, @RequestParam(name = "hciCode") String hciCode);

    @GetMapping(value = "/ar-common/ar-data-submission/patient-code-hci-code", produces = MediaType.APPLICATION_JSON_VALUE,consumes =
            MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ArSuperDataSubmissionDto> getArSuperDataSubmissionDto(@RequestParam(name = "patientCode") String patientCode,
            @RequestParam(name = "hciCode", required = false) String hciCode);

    @PutMapping(value = "/ar-common/ar-cycle-stage/save", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ArCycleStageDto> saveArCycleStageDto(@RequestBody ArCycleStageDto arCycleStageDto);

    @GetMapping(value = "/ar-common/ar-cycle-stage/id", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ArCycleStageDto> getArCycleStageDtoById(@RequestParam(name = "id") String id);

    @GetMapping(value = "/ar-common/ar-cycle-stage/submissionId", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ArCycleStageDto>> getArCycleStageDtosBySubmissionId(@RequestParam(name = "submissionId") String submissionId) ;

    @PutMapping(value = "/ar-common/ar-donor/save", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ArDonorDto> saveArDonorDto(@RequestBody ArDonorDto arDonorDto);

    @GetMapping(value = "/ar-common/ar-donor/id", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ArDonorDto> getArDonorDtoById(@RequestParam(name = "id") String id) ;

    @GetMapping(value = "/ar-common/ar-donor/cycleStageId", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<ArDonorDto>> getArDonorDtosByCycleStageId(@RequestParam(name = "cycleStageId") String cycleStageId);

    @PutMapping(value = "/ar-common/fertilisation/save", produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FertilisationDto> saveFertilisationDto(@RequestBody FertilisationDto fertilisationDto);

    @GetMapping(value = "/ar-common/fertilisation/id", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<FertilisationDto> getFertilisationDtoById(@RequestParam(name = "id") String id);

    @GetMapping(value = "/ar-common/fertilisation/submissionId", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<FertilisationDto>> getFertilisationDtosBySubmissionId(@RequestParam(name = "submissionId") String submissionId) ;

    @PostMapping(value = "/ar-common/ar-data-submission", produces = MediaType.APPLICATION_JSON_VALUE,consumes =
            MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ArSuperDataSubmissionDto> saveArSuperDataSubmissionDto(@RequestBody ArSuperDataSubmissionDto arSuperDataSubmissionDto);

    @PostMapping(value = "/data-submission/draft", produces = MediaType.APPLICATION_JSON_VALUE,consumes =
            MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<ArSuperDataSubmissionDto> doUpdateDataSubmissionDraft(@RequestBody ArSuperDataSubmissionDto arSuperDataSubmissionDto);

}
