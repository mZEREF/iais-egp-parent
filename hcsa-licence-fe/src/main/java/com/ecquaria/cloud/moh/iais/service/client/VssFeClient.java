package com.ecquaria.cloud.moh.iais.service.client;

import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.DpSuperDataSubmissionDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.PgtStageDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssFileDto;
import com.ecquaria.cloud.moh.iais.common.dto.hcsa.dataSubmission.VssSuperDataSubmissionDto;
import com.ecquaria.cloudfeign.FeignConfiguration;
import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@FeignClient(name = "hcsa-licence", configuration = FeignConfiguration.class, fallback = VssFeClientFallback.class)
public interface VssFeClient {

    @PostMapping(value = "/vss-common/vss-data-submission", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<VssSuperDataSubmissionDto> saveVssSuperDataSubmissionDto(
            @RequestBody VssSuperDataSubmissionDto vssSuperDataSubmissionDto);

    @PostMapping(value = "/data-submission/draft/vss", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<VssSuperDataSubmissionDto> doUpdateDataSubmissionDraft(
            @RequestBody VssSuperDataSubmissionDto vssSuperDataSubmissionDto);

    @GetMapping(value = "/data-submission/draft-vss-data-submission/{draftNo}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<VssSuperDataSubmissionDto> getVssSuperDataSubmissionDtoDraftByDraftNo(@PathVariable("draftNo") String draftNo);

    @PostMapping(value = "/data-submission/draft/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateDataSubmissionDraftStatus(@RequestParam("draftId") String draftId,
                                                              @RequestParam("Status") String status);

    @GetMapping(value = "/data-submission/vss-data-submission/{orgId}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<VssSuperDataSubmissionDto> getVssSuperDataSubmissionDtoDraftByConds(@PathVariable("orgId") String orgId,
                                                                                          @RequestParam("submissionType") String submissionType, @RequestParam("svcName") String svcName, @RequestParam("hciCode") String hciCode);


    @DeleteMapping(value = "/data-submission/draft-vss-data-submission/special", consumes = MediaType.APPLICATION_JSON_VALUE)
    void deleteVssSuperDataSubmissionDtoDraftByConds(@RequestParam(name = "orgId") String orgId,
                                                    @RequestParam(name = "submissionType") String submissionType,
                                                    @RequestParam(name = "hciCode") String hciCode);

    @DeleteMapping(value = "/data-submission/draft/{draftId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> deleteVssSuperDataSubmissionDraftById(@PathVariable("draftId") String draftId);

    @GetMapping(value = "/vss-common/vss-data-submission/{submissionNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<VssSuperDataSubmissionDto> getVssSuperDataSubmissionDto(@PathVariable("submissionNo") String submissionNo);


    @GetMapping(value = "/vss-common/vssDocument/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<List<VssFileDto>> getListVssDocumentDtoStatus(@RequestParam("status") String status);

    @PostMapping(value = "/vss-common/vssDocument/treatmentId/status", consumes = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> updateVssDocumentStatusByTreId(@RequestParam("treatmentId") String treatmentId, @RequestParam("status") String status);
}
