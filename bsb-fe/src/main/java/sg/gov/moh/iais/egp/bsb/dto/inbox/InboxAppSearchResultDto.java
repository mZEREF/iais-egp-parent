package sg.gov.moh.iais.egp.bsb.dto.inbox;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.entity.Application;
import java.util.List;


@Data
public class InboxAppSearchResultDto {
    private PageInfo pageInfo;
    private List<Application> applications;
    private long unreadMsgAmt;
}
