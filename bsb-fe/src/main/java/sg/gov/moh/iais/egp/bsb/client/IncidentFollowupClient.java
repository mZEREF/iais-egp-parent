package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.ValidationResultDto;
import sg.gov.moh.iais.egp.bsb.dto.followup.*;

import java.util.List;

/**
 * @author YiMing
 * @version 2022/1/11 16:31
 **/
@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "followup")
public interface IncidentFollowupClient {

    @GetMapping(path = "/followup/query/reportA/refNo",produces =MediaType.APPLICATION_JSON_VALUE)
    List<String> queryAll1ARefNo();

    @GetMapping(path = "/followup/query/reportB/refNo", produces =MediaType.APPLICATION_JSON_VALUE)
    List<String> queryAll1BRefNo();

    @GetMapping(path = "/followup/query/infoA/{refNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FollowupPreviewADto> queryFollowupInfoAByRefNo(@PathVariable("refNo") String refNo);

    @GetMapping(path = "/followup/query/infoB/{refNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FollowupPreviewBDto> queryFollowupInfoBByRefNo(@PathVariable("refNo") String refNo);

    @PostMapping(path = "/followup/save/reportA", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewFollowupReport1A(@RequestBody FollowupReport1ADto dto);

    @PostMapping(path = "/followup/save/reportB", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewFollowupReport1B(@RequestBody FollowupReport1BDto dto);

    @PostMapping(path = "/followup/validate/followup1A", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFollowup1A(@RequestBody Followup1AMetaDto dto);

    @PostMapping(path = "/followup/validate/followup1B", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFollowup1B(@RequestBody Followup1BMetaDto dto);

    @PostMapping(path = "/followup/draft/reportA", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftFollowup1A(@RequestBody FollowupReport1ADto dto);

    @PostMapping(path = "/register/draft/reportB", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftFollowup1B(@RequestBody FollowupReport1BDto dto);
}
