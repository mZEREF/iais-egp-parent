package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArCycleStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.ArDonorDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.FertilisationDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PatientDto;
import com.ecquaria.cloud.moh.iais.common.utils.StringUtil;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "hcsa-licence",configuration = FeignConfiguration.class,fallback = ArFeClientFallback.class)
public interface ArFeClient {

    @GetMapping(value = "/ar-common/patient/idnumber-nationality", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<PatientDto> getPatientDto(@RequestParam(name = "idNumber") String idNumber,
                                                  @RequestParam(name = "nationality") String nationality,  @RequestParam(name = "orgId") String orgId);

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

}
