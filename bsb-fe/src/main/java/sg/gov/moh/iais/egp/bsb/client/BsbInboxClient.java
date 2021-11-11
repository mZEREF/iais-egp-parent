package sg.gov.moh.iais.egp.bsb.client;

import com.ecquaria.cloudfeign.FeignResponseEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.*;

import java.util.List;


@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class)
public interface BsbInboxClient {
    @GetMapping(value = "/bsb-inbox/msg", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InboxMsgSearchResultDto> getInboxMsg(@SpringQueryMap InboxMsgSearchDto dto);

    @GetMapping(value = "/bsb-inbox/app", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InboxAppSearchResultDto> getInboxApplication(@SpringQueryMap InboxAppSearchDto dto);

    @GetMapping(value = "/bsb-inbox/approval/facAdmin", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InboxApprovalFacAdminResultDto> getInboxApprovalForFacAdmin(@SpringQueryMap InboxApprovalSearchDto dto);

    @GetMapping(value = "/bsb-inbox/approval/afc", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InboxApprovalAfcResultDto> getInboxApprovalForAfcAdmin(@SpringQueryMap InboxApprovalSearchDto dto);

    @GetMapping(value = "/bsb-inbox/searchDataSubmission", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InboxDataSubResultDto> getInboxDataSubmission(@SpringQueryMap InboxDataSubSearchDto dto);

    @GetMapping(value = "/bsb-audit/facName")
    FeignResponseEntity<List<String>> queryDistinctFN();
}
