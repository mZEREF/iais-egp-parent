package sg.gov.moh.iais.egp.bsb.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.FeignClientsConfiguration;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import sg.gov.moh.iais.egp.bsb.dto.ResponseDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxAppSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxAppSearchResultDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxMsgSearchDto;
import sg.gov.moh.iais.egp.bsb.dto.inbox.InboxMsgSearchResultDto;


@FeignClient(value = "bsb-fe-api", configuration = FeignClientsConfiguration.class)
public interface BsbInboxClient {
    @GetMapping(value = "/bsb-inbox/msg", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InboxMsgSearchResultDto> getInboxMsg(@SpringQueryMap InboxMsgSearchDto dto);

    @GetMapping(value = "/bsb-inbox/app", consumes = MediaType.APPLICATION_JSON_VALUE, produces =MediaType.APPLICATION_JSON_VALUE)
    ResponseDto<InboxAppSearchResultDto> getInboxApplication(@SpringQueryMap InboxAppSearchDto dto);
}
