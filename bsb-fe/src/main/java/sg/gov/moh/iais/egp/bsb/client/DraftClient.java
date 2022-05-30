package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.ApplicationDto;
import sg.gov.moh.iais.egp.bsb.dto.entity.DraftDto;

import java.util.List;

/**
 * @author YiMing
 * @version 2022/1/15 14:29
 **/
@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class, contextId = "fpDraft")
public interface DraftClient {
    @GetMapping(path = "/draft/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DraftDto> retrieveDraftByApplicationId(@PathVariable("appId") String appId);

    @GetMapping(path = "/draft/application/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<ApplicationDto> retrieveDraftApplicationById(@PathVariable("appId") String appId);

    @PostMapping(path = "/draft/remove/draft/{appId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> doRemoveDraft(@PathVariable("appId") String appId);

    @PostMapping(value = "/draft/remove/draft/draftAppNo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> doRemoveDraftByDraftAppNo(@RequestParam("draftAppNo") String draftAppNo);

    @GetMapping(path = "/draft/all/data", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<List<DraftDto>> retrieveDraftDtoAll();
}
