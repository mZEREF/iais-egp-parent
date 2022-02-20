package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyFindingFormDto;
import sg.gov.moh.iais.egp.bsb.dto.inspection.RectifyInsReportSaveDto;


@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "inspectionNCs")
public interface InspectionNCsClient {
    @GetMapping(path = "/ncs/pre/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<RectifyFindingFormDto> getRectifyFindingFormDtoByAppId(@PathVariable("appId") String appId);


    @PostMapping(value = "/ncs/save", consumes = MediaType.APPLICATION_JSON_VALUE)
    void saveRectifyInsReport(@RequestBody RectifyInsReportSaveDto saveDto);

}
