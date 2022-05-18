package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.RfiDisplayDto;
import sg.gov.moh.iais.egp.bsb.dto.rfi.save.SaveInspectionReportDto;

@FeignClient(name = "bsb-api", configuration = FeignConfiguration.class, contextId = "rfi")
public interface RfiClient {
    @GetMapping(value = "/rfi/{applicationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<RfiDisplayDto> getProcessingRfiByApplicationId(@PathVariable("applicationId") String applicationId);

    @PostMapping(value = "/rfi", produces = MediaType.APPLICATION_JSON_VALUE)
    void saveRfi(@RequestParam("rfiId") String rfiId);

    @PostMapping(value = "/rfi/inspection/report", produces = MediaType.APPLICATION_JSON_VALUE)
    void saveInspectionReport(@RequestBody SaveInspectionReportDto saveInspectionReportDto);
}
