package sg.gov.moh.iais.egp.bsb.dto.inbox;

import lombok.Data;
import sg.gov.moh.iais.egp.bsb.dto.PageInfo;
import sg.gov.moh.iais.egp.bsb.entity.Approval;

import java.util.List;


@Data
public class InboxApprovalAfcResultDto {
    private PageInfo pageInfo;
    /* Currently use Approval because we have not implemented the organization mechanism
    *  Need to change, add organization info in the future */
    private List<Approval> approvalInfos;
    private long unreadMsgAmt;
}
