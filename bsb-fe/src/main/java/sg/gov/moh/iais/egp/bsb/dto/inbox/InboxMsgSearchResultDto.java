package sg.gov.moh.iais.egp.bsb.dto.inbox;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.entity.BsbInbox;

import java.util.List;


/**
 * Response data DTO used by inbox tab of inbox module
 */
@Data
public class InboxMsgSearchResultDto {
    private PageInfo pageInfo;
    private List<BsbInbox> bsbInboxes;
    private long unreadMsgAmt;
}
