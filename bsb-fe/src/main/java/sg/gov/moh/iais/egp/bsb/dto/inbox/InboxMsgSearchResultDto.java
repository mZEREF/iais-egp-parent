package sg.gov.moh.iais.egp.bsb.dto.inbox;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.dto.entity.BsbInboxDto;

import java.util.List;
import java.util.Map;


/**
 * Response data DTO used by inbox tab of inbox module
 */
@Data
public class InboxMsgSearchResultDto {
    private PageInfo pageInfo;
    private List<BsbInboxDto> bsbInboxes;
    private Map<String,Boolean> inboxActionRequiredMsgIdMap;
}
