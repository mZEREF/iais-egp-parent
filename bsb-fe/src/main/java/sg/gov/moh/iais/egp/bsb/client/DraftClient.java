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
import sg.gov.moh.iais.egp.bsb.dto.entity.DraftDto;

import java.util.List;


@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class, contextId = "fpDraft")
public interface DraftClient {
    @GetMapping(path = "/draft/{appId}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<DraftDto> retrieveDraftByApplicationId(@PathVariable("appId") String appId);

    @PostMapping(path = "/draft/remove/draft/{appId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> doRemoveDraftByDraftAppId(@PathVariable("appId") String appId);

    @PostMapping(path = "/draft/soft-delete/draft/{appId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    FeignResponseEntity<Void> doSoftDeleteDraftByDraftAppId(@PathVariable("appId") String appId);

    @GetMapping(path = "/draft/all/active-draft", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<List<DraftDto>> findAllActiveDraft();

    @GetMapping(path = "/draft/remove/draft/draftAppNo", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<Void> doRemoveDraftByDraftAppNo(@RequestParam("draftAppNo") String draftAppNo);
}
