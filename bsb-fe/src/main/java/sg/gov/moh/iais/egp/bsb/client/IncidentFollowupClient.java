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
import sg.gov.moh.iais.egp.bsb.dto.followup.view.Followup1AViewDto;
import sg.gov.moh.iais.egp.bsb.dto.followup.view.Followup1BViewDto;

import java.util.List;


@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class, contextId = "followup")
public interface IncidentFollowupClient {

    @GetMapping(path = "/incident/follow-up/query/reportA/refNo",produces =MediaType.APPLICATION_JSON_VALUE)
    List<String> queryAll1ARefNo();

    @GetMapping(path = "/incident/follow-up/query/reportB/refNo", produces =MediaType.APPLICATION_JSON_VALUE)
    List<String> queryAll1BRefNo();

    @GetMapping(path = "/incident/follow-up/query/infoA/{refNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FollowupPreviewADto> queryFollowupInfoAByRefNo(@PathVariable("refNo") String refNo);

    @GetMapping(path = "/incident/follow-up/query/infoB/{refNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FollowupPreviewBDto> queryFollowupInfoBByRefNo(@PathVariable("refNo") String refNo);

    @GetMapping(path = "/incident/follow-up/re/infoB/{refNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FollowupReport1BDto> retrieveFollowupReport1B(@PathVariable("refNo") String refNo);

    @GetMapping(path = "/incident/follow-up/re/infoA/{refNo}", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FollowupReport1ADto> retrieveFollowupReport1A(@PathVariable("refNo") String refNo);

    @PostMapping(path = "/incident/follow-up/save/reportA", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewFollowupReport1A(@RequestBody FollowupReport1ADto dto);

    @PostMapping(path = "/incident/follow-up/save/reportB", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> saveNewFollowupReport1B(@RequestBody FollowupReport1BDto dto);

    @PostMapping(path = "/incident/follow-up/validate/followup1A", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFollowup1A(@RequestBody Followup1AMetaDto dto);

    @PostMapping(path = "/incident/follow-up/validate/followup1B", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateFollowup1B(@RequestBody Followup1BMetaDto dto);

    @PostMapping(path = "/incident/follow-up/draft/reportA", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftFollowup1A(@RequestBody FollowupReport1ADto dto);

    @PostMapping(path = "/incident/follow-up/draft/reportB", consumes = MediaType.APPLICATION_JSON_VALUE)
    String saveDraftFollowup1B(@RequestBody FollowupReport1BDto dto);

    @GetMapping(path = "/incident/follow-up/application/report1A/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FollowupReport1ADto> retrieveFollowup1AByApplicationId(@PathVariable("appId") String appId);

    @GetMapping(path = "/incident/follow-up/application/report1B/{appId}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<FollowupReport1BDto> retrieveFollowup1BByApplicationId(@PathVariable("appId") String appId);

    @GetMapping(path = "/incident/follow-up/view/followup1A/{referNo}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<Followup1AViewDto> findFollowup1AViewDtoByReferenceNo(@PathVariable("referNo") String referNo);

    @GetMapping(path = "/incident/follow-up/view/followup1B/{referNo}", produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<Followup1BViewDto> findFollowup1BViewDtoByReferenceNo(@PathVariable("referNo") String referNo);
}
