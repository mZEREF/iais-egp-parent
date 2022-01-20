package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.DraftDto;

/**
 * @author YiMing
 * @version 2022/1/15 14:29
 **/
@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "fpDraft")
public interface IncidentDraftClient {
    @GetMapping(path = "/incident-draft/draft/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DraftDto> retrieveDraftByApplicationId(@PathVariable("appId") String appId);
}
