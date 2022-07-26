package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxAppSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxAppPageInfoResultDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxApprovalAFCResultDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxApprovalFacAdminResultDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxApprovalSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxDashboardDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxDataSubResultDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxDataSubSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxFacPageInfoResultInfo;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxFacSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxMsgContentDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxMsgSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxMsgSearchResultDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxRepResultDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxRepSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.validation.ValidationResultDto;

import java.util.List;


@FeignClient(value = "bsb-api", configuration = FeignClientsConfiguration.class)
public interface BsbInboxClient {
    @GetMapping(value = "/fe-inbox/dashboard", produces = MediaType.APPLICATION_JSON_VALUE)
    InboxDashboardDto retrieveDashboardData();

    @DeleteMapping(value = "/fe-inbox/app/{appId}")
    void deleteDraftApplication(@PathVariable("appId") String appId);

    @GetMapping(value = "/fe-inbox/msg", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InboxMsgSearchResultDto> getInboxMsg(@SpringQueryMap InboxMsgSearchDto dto);

    @GetMapping(value = "/fe-inbox/app", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InboxAppPageInfoResultDto> getInboxApplication(@SpringQueryMap InboxAppSearchDto dto);

    @GetMapping(value = "/fe-inbox/approval/facAdmin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InboxApprovalFacAdminResultDto> getInboxApprovalForFacAdmin(@SpringQueryMap InboxApprovalSearchDto dto);

    @GetMapping(value = "/fe-inbox/approval/afc", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InboxApprovalAFCResultDto> getInboxApprovalForAFCAdmin(@SpringQueryMap InboxApprovalSearchDto dto);

    @GetMapping(value = "/fe-inbox/searchDataSubmission", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InboxDataSubResultDto> getInboxDataSubmission(@SpringQueryMap InboxDataSubSearchDto dto);

    @GetMapping(value = "/facility-info/names")
    FeignResponseEntity<List<String>> queryDistinctFN();

    @GetMapping(value = "/fe-inbox/incident", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InboxRepResultDto> searchInboxReportableEvent(@SpringQueryMap InboxRepSearchDto dto);

    @GetMapping(value = "/fe-inbox/msg-content/{msgId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InboxMsgContentDto> searchInboxContentByMsgId(@PathVariable("msgId") String msgId);

    @PostMapping(value = "/fe-inbox/status/read", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> updateInboxMsgStatusRead(@RequestParam("msgId") String msgId);

    @PostMapping(value = "/fe-inbox/messages/status/archive", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> updateInboxMsgStatusArchive(@RequestBody List<String> msgIds);

    @PostMapping(value = "/fe-inbox/messages/status/inbox", consumes = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<String> updateInboxMsgStatusRead(@RequestBody List<String> msgIds);

    @GetMapping(value = "/fe-inbox/facility", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InboxFacPageInfoResultInfo> searchInboxFacility(@SpringQueryMap InboxFacSearchDto dto);

    @PostMapping(value = "/fe-inbox/validate/archive", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ValidationResultDto validateInboxMsgArchive(@RequestBody List<String> msgIds);

    // todo: liran update
    @GetMapping(value = "/fe-inbox/afc", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InboxFacPageInfoResultInfo> searchInboxAFC(@SpringQueryMap InboxFacSearchDto dto);

}
